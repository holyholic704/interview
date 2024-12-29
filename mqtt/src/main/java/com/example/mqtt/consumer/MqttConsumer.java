package com.example.mqtt.consumer;

import com.example.mqtt.init.MqttClientInit;
import com.example.mqtt.producer.callback.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author jiage
 */
@Slf4j
@Component
public class MqttConsumer {

    @Autowired
    private MqttClientInit mqttClientInit;

    private MqttClient client;

    @PostConstruct
    public void init() {
        receive("carNo");
    }

    public void receive(String topic) {
        if (client == null || !client.isConnected()) {
            client = mqttClientInit.getClient("fuckyou", new MyCallBack());
        }

        try {
            client.subscribe(topic, (topic1, message) -> {
                log.info("topic:" + topic1);
                log.info("message:" + new String(message.getPayload()));
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
