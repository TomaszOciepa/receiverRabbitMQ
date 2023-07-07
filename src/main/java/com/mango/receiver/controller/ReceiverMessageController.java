package com.mango.receiver.controller;

import com.mango.receiver.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReceiverMessageController {

    private final RabbitTemplate rabbitTemplate;

    public ReceiverMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/message")
    public String receiverMessage() {
        Object message = rabbitTemplate.receiveAndConvert("stau");
        if (message != null) {
            return "udało się odczytać wiadomość: " + message.toString();
        } else {
            return "Nie ma wiadomości do odczytu";
        }
    }

    @GetMapping("/notification")
    public ResponseEntity<Notification> receiveNotification(){
        Notification notification = rabbitTemplate
                .receiveAndConvert("stau", ParameterizedTypeReference.forType(Notification.class));
        if(notification != null){
            return ResponseEntity.ok(notification);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @RabbitListener(queues = "stau")
    public void listenerMessage(Notification notification){
        System.out.println(notification.getEmail() +" "+ notification.getTitle()+" "+notification.getBody());
    }
}
