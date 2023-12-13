package br.com.infnet.assessment.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class Info {
    private int totalRegistros;
    private int totalPaginas;
    private int paginaCorrente;
}
