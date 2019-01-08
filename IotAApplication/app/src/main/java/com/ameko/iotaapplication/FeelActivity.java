package com.ameko.iotaapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FeelActivity extends AppCompatActivity {

    private Context context;
    private Intent intent;
    private TextView textViewFdata;
    private String remoteDeviceInfo;
    private TextView textViewBT;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button buttonLink;
    private String TAG="feel";

    private Switch switchlamp2, switchlamp3;
    public static boolean lamp2Flag=false, lamp3Flag=false;
    private String lampCMD;

    private final String Lamp2_on ="K";
    private final String Lamp2_off ="0";
    private final String Lamp3_on ="Y";
    private final String Lamp3_off ="0";
    private Button buttonClean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feel );

        setTitle( "情境燈光模式" );
        context= this;
        textViewFdata = (TextView) findViewById(R.id.textView_Fdata);
        textViewFdata.setText("現在的模式為:\n");


        remoteDeviceInfo = "00:21:13:02:C6:11";

        textViewBT =(TextView) findViewById( R.id.textView_Fcontrol );

        textViewBT.setText( remoteDeviceInfo );
        btAdapter =BluetoothAdapter.getDefaultAdapter();
        mChatService = new BTChatService(context,mHandler);

        buttonClean =(Button) findViewById(R.id.button_clean);
        buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFdata.setText("");
                textViewFdata.append("現在的模式為:\n");
            }
        });

        buttonLink = (Button) findViewById(R.id.button_Fcontrol);
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


        switchlamp2= (Switch) findViewById(R.id.switch2);
        switchlamp3= (Switch) findViewById(R.id.switch3);



        switchlamp2.setChecked(lamp2Flag);
        switchlamp3.setChecked(lamp3Flag);




        switchlamp2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(lamp3Flag == false ) {
                    lamp2Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp2_on;
                        textViewFdata.append("慶生模式開啟\n");
                    } else {
                        lampCMD = Lamp2_off;
                        textViewFdata.append("慶生模式關閉\n");
                    }
                    sendCMD(lampCMD);
                }else {
                    switchlamp3.setChecked(false);
                    lamp3Flag=false;
                }
            }
        });

        switchlamp3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(lamp2Flag == false ) {
                    lamp3Flag = isChecked;
                    if (isChecked) {
                        lampCMD = Lamp3_on;
                        textViewFdata.append("Party模式開啟\n");
                    } else {
                        lampCMD = Lamp3_off;
                        textViewFdata.append("Party模式關閉\n");
                    }
                    sendCMD(lampCMD);
                }else {
                    switchlamp2.setChecked(false);
                    lamp2Flag=false;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.led,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                    textViewFdata.append(readMessage );   //display on TextView
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
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            Log.d(TAG,"CarActivity onDestry()");
            mChatService.stop();
            mChatService=null;
        }
    }
}

