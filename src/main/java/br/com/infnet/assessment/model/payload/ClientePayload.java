package br.com.infnet.assessment.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import br.com.infnet.assessment.model.Cliente;

import java.util.List;

@Data@AllArgsConstructor
public class ClientePayload {
    List<Cliente> clientes;
    Info infos;
}
