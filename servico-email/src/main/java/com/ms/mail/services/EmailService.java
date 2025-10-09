package com.ms.mail.services;

import com.ms.mail.models.Email;
import com.ms.mail.models.dtos.EmailStatusResponse;
import com.ms.mail.models.enums.StatusEmail;
import com.ms.mail.repositories.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.response}")
    private String emailResponseQueue;

    public EmailService(EmailRepository emailRepository, JavaMailSender javaMailSender, RabbitTemplate rabbitTemplate) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Email sendEmail(Email email) {
        try {
            email.setSendDateEmail(LocalDateTime.now());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("clinicadev@gmail.com");
            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);

            javaMailSender.send(mimeMessage);
            email.setStatusEmail(StatusEmail.SENT);

        } catch (MailException e) {
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {

            email = emailRepository.save(email);

            EmailStatusResponse response = new EmailStatusResponse(
                    email.getId(),
                    email.getEmailTo(),
                    email.getStatusEmail().toString()
            );

            rabbitTemplate.convertAndSend(emailResponseQueue, response);

            return email;
        }
    }
}
