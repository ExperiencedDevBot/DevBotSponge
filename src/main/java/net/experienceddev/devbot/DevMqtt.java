package net.experienceddev.devbot;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;

public class DevMqtt {
    Gson gson = new Gson();

    public MqttClient mqttClient;

    private MqttConnectOptions mqttOptions;

    DevMqtt(Logger logger){
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setCleanSession(true);
        mqttOptions.setUserName(DevBot.cfg.mqttUser);
        mqttOptions.setPassword(DevBot.cfg.mqttPass.toCharArray());
        mqttOptions.setWill(DevBot.cfg.mqttTopic + "/will", "Disconnected!".getBytes(), 0, false);

        createClient();
        try {
            mqttClient.connect(mqttOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(DevMessageObject msgObj){

        msgObj.prepareMessage(); // Making sure everything is töfte
        MqttMessage message = new MqttMessage(gson.toJson(msgObj).getBytes());
        message.setQos(0);
        message.setRetained(false);
        try {
            mqttClient.publish(msgObj.getTopic(), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void restartClient(){
        try {
            mqttClient.disconnect();
            mqttClient.close();
            createClient();
            mqttClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void createClient(){
        String brokerString = "tcp://" + DevBot.cfg.mqttBrokerIP + ":" + DevBot.cfg.mqttBrokerPort;
        try {
            mqttClient = new MqttClient(brokerString, MqttClient.generateClientId()); // TODO: Disable folder generation https://github.com/eclipse/paho.mqtt.java/issues/777
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
