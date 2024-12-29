package com.example.mqtt.producer;

import com.example.mqtt.init.MqttClientInit;
import com.example.mqtt.producer.callback.MyCallBack;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author jiage
 */
@Component
public class MqttProducer {

    @Value("${mqtt.clientId}")
    private String clientId;

    @Autowired
    private MqttClientInit mqttClientInit;

    private MqttClient client;

    @PostConstruct
    public void init() {
        client = mqttClientInit.getClient(clientId, new MyCallBack());
    }

    public void publish(int qos, boolean retained, String topic, String message) {
        if (client == null || !client.isConnected()) {
            init();
        }
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttMessage.setPayload(message.getBytes());

        MqttTopic mqttTopic = client.getTopic(topic);

        // 提供一种机制来跟踪消息的传递进度
        // 用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            // 将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            // 一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
