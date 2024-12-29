package com.example.mqtt.producer.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author jiage
 */
@Slf4j
public class MyCallBack implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        log.info("连接丢失");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("消息到达：" + topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("消息发布成功");
    }
}
