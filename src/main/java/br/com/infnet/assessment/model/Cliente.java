package br.com.infnet.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private final String moedaLocal = "BRL";
    private double saldoTotalMoedaLocal;
    private Map<String, Double> saldos;
}
