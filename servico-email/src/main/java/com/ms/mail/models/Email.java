package com.ms.mail.models;

import com.ms.mail.models.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "email")
public class Email {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emailfrom", length = 150, nullable = false)
    private String emailFrom;

    @Column(name = "emailto", length = 150, nullable = false)
    private String emailTo;

    @Column(name = "subject", length = 80, nullable = false)
    private String subject;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "senddateemail")
    private LocalDateTime sendDateEmail;

    @Column(name = "statusemail")
    private StatusEmail statusEmail;
}
