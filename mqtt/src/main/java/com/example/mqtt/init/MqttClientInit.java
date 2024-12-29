package com.example.mqtt.init;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class MqttClientInit {

    @Value("${mqtt.host}")
    private String serverURI;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    public MqttClient getClient(String clientId, MqttCallback callback) {
        try {
            MqttClient client = new MqttClient(serverURI, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            // 设置为true表示每次连接服务器都是以新的身份
            options.setCleanSession(true);
            // 设置连接用户名
            options.setUserName(username);
            // 设置连接密码
            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            // 设置心跳时间 单位为秒，表示服务器每隔 1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (clientId + "与服务器断开连接").getBytes(), 0, false);
            // 设置回调
            if (callback != null) {
                client.setCallback(callback);
            }
            client.connect(options);
            return client;
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
