package net.experienceddev.devbot;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;

public class DevMqtt {
    Gson gson = new Gson();
    Logger logger;

    public MqttClient mqttClient;

    private MqttConnectOptions mqttOptions;

    DevMqtt(Logger logger){
        this.logger = logger;
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setCleanSession(true);
        mqttOptions.setUserName(DevBot.cfg.mqttUser);
        mqttOptions.setPassword(DevBot.cfg.mqttPass.toCharArray());
        mqttOptions.setWill(DevBot.cfg.mqttTopic + "/will", "Disconnected!".getBytes(), 0, false);

        createClient();
        try {
            mqttClient.connect(mqttOptions);
            while(!mqttClient.isConnected()){
                wait();
            }
            mqttClient.subscribe(DevBot.cfg.mqttTopic + "/toJava");
        } catch (MqttException | InterruptedException e) {
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

    public void stopClient() throws MqttException {
        if(mqttClient.isConnected()){
            mqttClient.disconnect();
        }
        mqttClient.close();
    }

    public void restartClient() throws MqttException {
        stopClient();
        createClient();
        mqttClient.connect();
        mqttClient.subscribe(DevBot.cfg.mqttTopic + "/toJava");
    }

    private void createClient(){
        String brokerString = "tcp://" + DevBot.cfg.mqttBrokerIP + ":" + DevBot.cfg.mqttBrokerPort;
        try {
            mqttClient = new MqttClient(brokerString, MqttClient.generateClientId()); // TODO: Disable folder generation https://github.com/eclipse/paho.mqtt.java/issues/777
        } catch (MqttException e) {
            e.printStackTrace();
        }
        createCallbacks();
    }

    private void createCallbacks(){
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                logger.warn("MQTT was disconnected: " + cause.getMessage());
                try {
                    restartClient();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                logger.info("Message has arrived!");
                DevMessageObject msgObj = gson.fromJson(new String(message.getPayload()), DevMessageObject.class);
                logger.info("Message has been deserialized!");
                msgObj.handle();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
