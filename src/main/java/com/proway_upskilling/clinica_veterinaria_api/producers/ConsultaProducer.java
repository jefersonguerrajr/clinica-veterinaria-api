package com.proway_upskilling.clinica_veterinaria_api.producers;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsultaProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public ConsultaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarEmailConfirmacaoConsulta(Consulta consulta) {
        EmailDto emailDto = new EmailDto();
        emailDto.setSubject("Agendamento de Consulta");
        emailDto.setEmailFrom("clinicadev@gmail.com");
        emailDto.setEmailTo(consulta.getCliente().getEmail());

        String text = """
                    <html>
                        <body>
                            <p>Olá, [cliente]! Sua consulta foi agendada com sucesso.</p>
                            <p><b>Clínica Veterinária</b></p>
                        </body>
                    </html>
                """;

        text = text.replace("[cliente]", consulta.getCliente().getNome());

        emailDto.setText(text);

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
