package org.robbojunior.android;

/**
 * Created by yarila on 09.08.17.
 */


import java.io.*;
import java.util.ArrayList;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.support.annotation.NonNull;
import android.util.*;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class BluetoothDevices {


    public final static String UUID = "e91521df-92b9-47bf-96d5-c52ee838f6f6";

    private volatile AlertDialog.Builder builder;
    public volatile RobotProgressDialog dialogLoading;

    public final ArrayList<BluetoothDevice> ListDiscoveredDevices = new ArrayList<BluetoothDevice>();


    private  AlertDialog alertDialog;

    private  AlertDialog  item_dialog_instance;

    public  boolean isSeraching = false;

    private boolean isComConnected = false;

    protected  BluetoothThings bt;
    private boolean already_clicked = false;

    private Activity _activity;

    public BluetoothDevices( final Activity activity,BluetoothThings _bt){
        this.bt = _bt;
        this.already_clicked = false;

        this._activity = activity;


        bt.discoveryFinishedReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {


                String action = intent.getAction();

                if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                   // dialogLoading.dismiss();

                   // isSeraching = false;

                   //  activity.unregisterReceiver(bt.discoveryFinishedReceiver);

                    BluetoothDevices.this.StopSearching();


                    synchronized (BluetoothDevices.this){
                          if (ListDiscoveredDevices.size() == 0) {


                                BluetoothDevices.this.CloseAllDialogs();
                                alertDialog = new AlertDialog.Builder(activity).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("No device found");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                        } else {
                            ttt(activity);
                        }
                  }

                }
            }
        };


        bt.discoverDevicesReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                String action = intent.getAction();

                if(BluetoothDevice.ACTION_FOUND.equals(action)) {

                    synchronized (BluetoothDevices.this){

                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                                if (!ListDiscoveredDevices.contains(device)) {

                                    if (device.getName() != null) {

                                        if (device.getName().startsWith("RNBT") || device.getName().startsWith("SQ") || device.getName().startsWith("ROB") || device.getName().startsWith("Scratch")) {
                                            Log.i("[Bluetooth_device]", device.getName());
                                            ListDiscoveredDevices.add(device);
                                            ttt(activity);
                                        }
                                }
                                }
                   }
                }
            }
        };

       // activity.registerReceiver(bt.discoverDevicesReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
       // activity.registerReceiver(bt.discoveryFinishedReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

    }

    private void ttt(final Context activity) {
// Already started


        Log.i("[Bluetooth_device]", "ttt()");
//
       // if (!already_clicked) {

            if (bt.socket != null) {
                return;
            }

            Log.i("[Bluetooth_device]", "begin_process_device()");

            dialogLoading.dismiss();

            if (builder != null) {

                String[] arrNames = new String[ListDiscoveredDevices.size()];
                int index = 0;
                for (BluetoothDevice device : ListDiscoveredDevices) {
                    arrNames[index] = device.getName();
                    index++;
                }

                ListView list = item_dialog_instance.getListView();
                ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                adapter.notifyDataSetChanged();

                item_dialog_instance.show();


            } else {


                builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select device");

                String[] arrNames = new String[ListDiscoveredDevices.size()];
                int index = 0;
                for (BluetoothDevice device : ListDiscoveredDevices) {
                    arrNames[index] = device.getName();
                    index++;
                }

                builder.setItems(arrNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                       // activity.unregisterReceiver(bt.discoverDevicesReceiver);
                       // bt.bluetoothAdapter.cancelDiscovery();

                        BluetoothDevices.this.StopSearching();

                        already_clicked = true;

                        try {
                            bt.socket = ListDiscoveredDevices.get(item).createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                            bt.socket.connect();
                        } catch (Throwable e) {
                            try {
                                bt.socket = (BluetoothSocket) ListDiscoveredDevices.get(item).getClass()
                                        .getMethod("createRfcommSocket", new Class[]{int.class})
                                        .invoke(ListDiscoveredDevices.get(item), 1);
                                bt.socket.connect();
                            } catch (Exception e1) {
                            }
                        }

                        bt.reader = bt.new Reader();
                        bt.reader.start();
                    }
                });



                builder.setCancelable(true);

                item_dialog_instance = builder.create();

                item_dialog_instance.setOnKeyListener(new DialogInterface.OnKeyListener(){

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, @NonNull KeyEvent event){

                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_BACK:


                                    if (BluetoothDevices.this.isSeraching) {

                                        BluetoothDevices.this.StopSearching();
                                        dialog.dismiss();


                                    }else{

                                        dialog.dismiss();

                                    }
                            }
                        }

                        return true;
                    }


                });

                item_dialog_instance.show();
               // item_dialog_instance.onBackPressed();

            }



    }


   public void CloseAllDialogs(){


       if (alertDialog != null) alertDialog.cancel();


   }

   public void StopSearching(){

       _activity.unregisterReceiver(bt.discoverDevicesReceiver);
       _activity.unregisterReceiver(bt.discoveryFinishedReceiver);
       dialogLoading.cancel();


       bt.bluetoothAdapter.cancelDiscovery();
       isSeraching = false;

       synchronized (ListDiscoveredDevices) {
      //     ListDiscoveredDevices.clear();
       }


   }



    public void btService(final Context activity){

        Log.i("[Bluetooth_device]", "locateDevices()");

        ListDiscoveredDevices.clear();

        if(bt.socket != null){
            try{
                bt.socket.close();
            }
            catch (IOException e){
            }
        }

        bt.socket = null;


        if(bt.bluetoothAdapter == null || !bt.bluetoothAdapter.isEnabled()){
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Blueooth is disabled.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else{


            activity.registerReceiver(bt.discoverDevicesReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            activity.registerReceiver(bt.discoveryFinishedReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

            bt.bluetoothAdapter.startDiscovery();

            isSeraching = true;





//

// Toast toast = Toast.makeText(toastContext, "Ha!", Toast.LENGTH_SHORT);
// toast.show();


            dialogLoading = new RobotProgressDialog(activity);
            dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogLoading.setMessage("Looking for devices. Please wait...");
            dialogLoading.setIndeterminate(true);
            dialogLoading.setCanceledOnTouchOutside(false); dialogLoading.show();
        }
    }




}
