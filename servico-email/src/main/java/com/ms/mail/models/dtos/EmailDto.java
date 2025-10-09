package com.ms.mail.models.dtos;

public record EmailDto(
        String emailFrom,
        String emailTo,
        String subject,
        String text
) {
}
