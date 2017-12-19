package org.robbojunior.android;

import android.util.*;


/**
 * Created by yarila on 09.08.17.
 */

public class RobotPowerFunction {

    protected BluetoothThings bt;

    public RobotPowerFunction(BluetoothThings bt){
        this.bt = bt;
    }



    public  void setRobotPower(int leftSpeed, int rightSpeed){
        Log.i("[RobotPowerFunction]", "leftSpeed="+leftSpeed + " rightSpeed" + rightSpeed);
        try{


            if(bt.socket != null && bt.socket.isConnected()){
                bt.socket.getOutputStream().write(0x63);
                bt.socket.getOutputStream().write(leftSpeed);
                bt.socket.getOutputStream().write(rightSpeed);
                bt.socket.getOutputStream().write(0x24);
                bt.socket.getOutputStream().flush();
               // return getResponse(bt.response);
            }
            else{
              //  return getEmptyResponse();
            }
        }
        catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
           // return null;
        }
    }




}
