package com.example.user.kt_mqtt_ser;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    private Switch simpleSwitch1;
    private Switch simpleSwitch2;
    private TextView TextView;
    public TextView TextView2;
    private TextView Subtext;
    private ListView lvChat;
    private Button ciondiotion;
    static String MQTTHOST  = "tcp://211.38.86.93:1883";
    String topicSTR = "$open-it/relay/order";
    String topiCStatus = "$open-it/relay/status";
    MqttAndroidClient client;
    static String Condition;
    Handler setDelay;
    Runnable startDelay;
    static boolean mbool;
    public boolean isMbool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Subtext = (TextView)findViewById(R.id.subtext);

        setDelay = new Handler();
        mqttconncetion();
        initView();

    }

    // connection to MQTT sercver
    public void mqttconncetion(){
        String clientId = MqttClient.generateClientId();
        client =  new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                   sendMessagestatus();
                   setSubscrition();
                   sucmassage();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //set buttons
    private void initView() {
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                Subtext.setText(payload);
                if(payload.equals("on")) {
                    simpleSwitch1.setChecked(true);
                    simpleSwitch2.setChecked(true);
                } else {
                    simpleSwitch1.setChecked(false);
                    simpleSwitch2.setChecked(false);
                }

            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }

        });

        simpleSwitch1 = (Switch) findViewById(R.id.simpleSwitch1);
        simpleSwitch2 = (Switch) findViewById(R.id.switch1);
        TextView = (TextView) findViewById(R.id.Textview);

      //  Toast.makeText(MainActivity.this, Condition, Toast.LENGTH_LONG).show();

        //set the switch to ON

        //attach a listener to check for changes in state
        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch1.isChecked()) {
                    TextView.setText("on");
                    sendMessage();
                } else {
                    TextView.setText("off");
                    sendMessage();
                }
                startDelay = new Runnable() {
                    @Override
                    public void run() {
                        sendMessagestatus();
                    }
                };

                setDelay.postDelayed(startDelay, 1000);
            }

        });

        simpleSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (simpleSwitch2.isChecked()) {
                    TextView.setText("on");
                    sendMessage();
                } else {
                    TextView.setText("off");
                    sendMessage();
                }
            }

        });

//
//       simpleSwitch1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(simpleSwitch1.isChecked()){
//                    TextView.setText("on");
//                    sendMessage();
//                }else{
//                    TextView.setText("off");
//                    sendMessage();
//                }
//
//                startDelay = new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessagestatus();
//                    }
//                };
//                    setDelay.postDelayed(startDelay, 1000);
//            }
//        });
    }

    public void sucmassage(){
        isMbool = mbool;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    // mesage is sending
    private void  sendMessagestatus(){
        String topic = topicSTR;
        String masseage = "status";
        try{
            client.publish(topic, masseage.getBytes() ,2,false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    // mesage is sending
    private void sendMessage(){
     String topic = topicSTR;
     String masseage = TextView.getText().toString();
        try{
            client.publish(topic, masseage.getBytes() ,1,false);
        }catch (MqttException e){
         e.printStackTrace();
        }
    }

    private void setSubscrition(){
        try{
            client.subscribe(topiCStatus, 1);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

}
