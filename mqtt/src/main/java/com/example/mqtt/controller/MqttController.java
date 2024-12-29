package com.example.mqtt.controller;

import com.example.mqtt.producer.MqttProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiage
 */
@RestController
public class MqttController {

    @Autowired
    private MqttProducer mqttProducer;

    @GetMapping("send")
    public String send(@RequestParam String topic, @RequestParam String msg) {
        mqttProducer.publish(0, false, topic, msg);
        return "SUCCESS";
    }
}
