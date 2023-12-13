package br.com.infnet.assessment.model.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponsePayload {
    private String message;
    private LocalDateTime dataHora;

    public ResponsePayload(String message) {
        this.message = message;
        this.dataHora = LocalDateTime.now();
    }
}
