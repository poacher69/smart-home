package com.ameko.iotaapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class TimeActivity extends AppCompatActivity {

    private Context context;
    //private Intent intent;
    private TextView textViewBT;
    private String remoteDeviceInfo;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private String TAG="control";
    private Button buttonLink;
    private Switch switchlamp1, switchlamp2, switchlamp3, switchlamp4;
    public static boolean lamp1Flag=false,lamp2Flag=false, lamp3Flag=false,lamp4Flag=false;
    private String lampCMD;
    private final String Lamp1_on ="A";
    private final String Lamp1_off ="0";
    private final String Lamp2_on ="C";
    private final String Lamp2_off ="0";
    private final String Lamp3_on ="E";
    private final String Lamp3_off ="0";
    private final String Lamp4_on ="G";
    private final String Lamp4_off ="0";
    private Button bottonClean;
    private TextView textViewTdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_time );

        setTitle( "日常燈光模式" );
        context = this;



        remoteDeviceInfo = "00:21:13:02:C6:11";

        textViewBT =(TextView) findViewById( R.id.textView_Tcontrol );
        textViewTdata = (TextView) findViewById(R.id.textView_Tdata);
        textViewTdata.setText("現在的模式為:\n");

        textViewBT.setText( remoteDeviceInfo );
        btAdapter =BluetoothAdapter.getDefaultAdapter();
        mChatService = new BTChatService(context,mHandler);

        bottonClean =(Button) findViewById(R.id.button_Tclean);
        bottonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTdata.setText("");
                textViewTdata.append("現在的模式為:\n");

            }
        });


        buttonLink = (Button) findViewById(R.id.button_Tcontrol);
        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remoteDeviceInfo != null){

                    String remoteMACAddress = remoteDeviceInfo;
                    Log.d(TAG,"remoteMACAddress = " +remoteMACAddress);
                    BluetoothDevice device=btAdapter.getRemoteDevice(remoteMACAddress);
                    Log.d(TAG,"device = "+device);
                    mChatService.connect(device);

                }else{
                    Toast.makeText(context,"No paired BT module.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        switchlamp1= (Switch) findViewById(R.id.switch_lamp1 );
        switchlamp2= (Switch) findViewById(R.id.switch_lamp2);
        switchlamp3= (Switch) findViewById(R.id.switch_lamp3);
        switchlamp4= (Switch) findViewById(R.id.switch_lamp4);

        switchlamp1.setChecked(lamp1Flag);
        switchlamp2.setChecked(lamp2Flag);
        switchlamp3.setChecked(lamp3Flag);
        switchlamp4.setChecked(lamp4Flag);


        switchlamp1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG,"lamp1Flag = "+ lamp1Flag);
                Log.d(TAG,"lamp2Flag = "+ lamp2Flag);
                Log.d(TAG,"lamp3Flag = "+ lamp3Flag);
                Log.d(TAG,"lamp4Flag = "+ lamp4Flag);


                if(lamp2Flag == false && lamp3Flag == false && lamp4Flag==false) {
                    lamp1Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp1_on;
                        textViewTdata.append("日光模式開啟\n");
                    } else {
                        lampCMD = Lamp1_off;
                        textViewTdata.append("日光模式關閉\n");
                    }
                    sendCMD(lampCMD);
                } else {

                    switchlamp1.setChecked(false);
                    lamp1Flag = false;

                }
            }
        });

        switchlamp2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG,"lamp1Flag = "+ lamp1Flag);
                Log.d(TAG,"lamp2Flag = "+ lamp2Flag);
                Log.d(TAG,"lamp3Flag = "+ lamp3Flag);
                Log.d(TAG,"lamp4Flag = "+ lamp4Flag);


                if (lamp1Flag == false && lamp3Flag == false && lamp4Flag == false) {
                    lamp2Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp2_on;
                        textViewTdata.append("黃光模式開啟\n");
                    } else {
                        lampCMD = Lamp2_off;
                        textViewTdata.append("黃光模式關閉\n");
                    }
                    sendCMD(lampCMD);
                }else {

                    switchlamp2.setChecked(false);
                    lamp2Flag = false;

                }
            }

        });

        switchlamp3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG,"lamp1Flag = "+ lamp1Flag);
                Log.d(TAG,"lamp2Flag = "+ lamp2Flag);
                Log.d(TAG,"lamp3Flag = "+ lamp3Flag);
                Log.d(TAG,"lamp4Flag = "+ lamp4Flag);


                if(lamp1Flag == false && lamp2Flag == false && lamp4Flag==false) {
                    lamp3Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp3_on;
                        textViewTdata.append("舒眠模式開啟\n");
                    } else {
                        lampCMD = Lamp3_off;
                        textViewTdata.append("舒眠模式關閉\n");
                    }
                    sendCMD(lampCMD);
                } else {

                    switchlamp3.setChecked(false);
                    lamp3Flag = false;

                }


            }
        });

        switchlamp4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG,"lamp1Flag = "+ lamp1Flag);
                Log.d(TAG,"lamp2Flag = "+ lamp2Flag);
                Log.d(TAG,"lamp3Flag = "+ lamp3Flag);
                Log.d(TAG,"lamp4Flag = "+ lamp4Flag);


                if(lamp1Flag == false && lamp2Flag == false && lamp3Flag==false) {
                    lamp4Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp4_on;
                        textViewTdata.append("夜燈模式開啟\n");
                    } else {
                        lampCMD = Lamp4_off;
                        textViewTdata.append("夜燈模式關閉\n");
                    }
                    sendCMD(lampCMD);
                } else {

                    switchlamp4.setChecked(false);
                    lamp4Flag = false;

                }
            }
        });






    } // end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.led,menu);
        return  true;
    }


    private void sendCMD(String message) {

        int mState = mChatService.getState();
        Log.d(TAG, "btstate in sendMessage =" + mState);

        if (mState != BTChatService.STATE_CONNECTED) {
            Log.d(TAG, "btstate =" + mState);

            return;

        } else {

            if (message.length() > 0) {

                byte[] send = message.getBytes();
                mChatService.BTWrite(send);

            }
        }

    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    textViewTdata.append(readMessage );   //display on TextView
                    Log.d(TAG,"Receive data : "+readMessage);

                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDevice = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context, "Connected to "+ mConnectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_ServerMode:
                    // Toast.makeText(context,"Enter Server accept state.",Toast.LENGTH_SHORT).show();   //display on TextView
                    break;

                case Constants.MESSAGE_ClientMode:
                    //  Toast.makeText(context,"Enter Client connect state.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_main:
                Toast.makeText(context,"回首頁",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_led:
                Toast.makeText(context,"日常模式",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,LedActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            Log.d(TAG,"CarActivity onDestry()");
            mChatService.stop();
            mChatService=null;
        }
    }


}//end of class
