package com.sandesh.bankingappdemointern.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;

    public EmailDetails(String recipient, String messageBody, String subject) {
        this.recipient=recipient;
        this.messageBody=messageBody;
        this.subject=subject;
    }
}
