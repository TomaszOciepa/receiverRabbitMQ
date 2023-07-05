package com.mango.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


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

    @RabbitListener(queues = "stau")
    public void listenerMessage(String message){
        System.out.println(LocalDateTime.now() + " "+ message);
    }
}
