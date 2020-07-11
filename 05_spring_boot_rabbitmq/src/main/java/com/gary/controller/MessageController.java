package com.gary.controller;

import com.gary.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MessageController implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * send short message to queue about queue_email
     * @param msg
     * @return
     */
    @RequestMapping("sendEmail")
    public String sendEmail(String msg){
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTINGKEY1, msg,
                correlationId);
        return null;
    }

    /**
     * send short message to queue about queue_short_message
     * @param msg
     * @return
     */
    @RequestMapping("sendShortMessage")
    public String sendShortMessage(String msg){
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTINGKEY2, msg,
                correlationId);
        return null;
    }

    /**
     * message callback
     * It was only send a message to rabbitmq server already, it wasn't mean that message was successfully or received
     * @param correlationData
     * @param ack
     * @param cause
     */
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("customer's uuid:"+correlationData);
        if (ack) {
            System.out.println("send message success");
        } else {
            System.out.println("send message failed:"+cause);
        }
    }

}
