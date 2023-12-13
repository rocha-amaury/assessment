package br.com.infnet.assessment.util;

import br.com.infnet.assessment.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class HttpUtil {
    @Autowired
    ClienteService clienteService;
    public HttpHeaders getHttpHeaders(Integer size, int page) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("total-size", String.valueOf(clienteService.count()));
        httpHeaders.set("total-paginas", String.valueOf(clienteService.getTotalDePaginas(size)));
        httpHeaders.set("current-page", String.valueOf(page));
        return httpHeaders;
    }
}
