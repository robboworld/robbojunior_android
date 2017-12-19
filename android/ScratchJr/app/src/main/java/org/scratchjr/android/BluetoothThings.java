package org.scratchjr.android;


/**
 * Created by yarila on 09.08.17.
 */


import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import android.bluetooth.*;
import android.content.*;
import android.util.*;

public class BluetoothThings {

    public final BluetoothAdapter bluetoothAdapter;
    public volatile BroadcastReceiver discoverDevicesReceiver;
    public volatile BroadcastReceiver discoveryFinishedReceiver;
    public volatile BluetoothSocket socket = null;




    public BluetoothThings(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }



    public volatile List<Integer> response = null;


    public volatile Reader reader;

    public class Reader extends Thread{

        public void run(){

            while (true){
                int[] responseTmp = null;
                int iCounter = 0;
                while (iCounter < 14){
                    int in;
                    try{
                        in = socket.getInputStream().read();

                        if(responseTmp == null){
                            if(in == 0x23){
                                // A new command
                                responseTmp = new int[14];
                                continue;
                            }
                        }
                        else{
                            responseTmp[iCounter] = in;
                            iCounter++;
                        }
                    }
                    catch (IOException e){
                        socket = null;
                        return;
                    }
                }

                Log.i("[ANE]", "Response: " + Arrays.toString(responseTmp));

                response = new ArrayList<Integer>();

                //Encoders
                response.add(responseTmp[0] * 256 + responseTmp[1]);
                response.add(responseTmp[2] * 256 + responseTmp[3]);

                response.add(responseTmp[4] * 256 + responseTmp[5]);
                response.add(responseTmp[6] * 256 + responseTmp[7]);

                response.add((int) responseTmp[8]);
                response.add((int) responseTmp[9]);
                response.add((int) responseTmp[10]);
                response.add((int) responseTmp[11]);
                response.add((int) responseTmp[12]);
                response.add((int) responseTmp[13]);

                Log.i("[ANE]", "Response PARSED: " + response);
            }
        }
    }


}
