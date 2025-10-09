package com.ms.mail.consumer;

import com.ms.mail.models.Email;
import com.ms.mail.models.dtos.EmailDto;
import com.ms.mail.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDto dto) {
        Email email = new Email();
        BeanUtils.copyProperties(dto, email);
        emailService.sendEmail(email);

        System.out.println("Email enviado!");
    }
}
