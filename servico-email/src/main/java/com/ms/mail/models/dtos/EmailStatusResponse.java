package com.ms.mail.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailStatusResponse {
    private Long emailId;
    private String emailTo;
    private String status;
}
