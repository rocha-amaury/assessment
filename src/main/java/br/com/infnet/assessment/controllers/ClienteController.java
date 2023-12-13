package br.com.infnet.assessment.controllers;

import br.com.infnet.assessment.exception.ResourceNotFoundException;
import br.com.infnet.assessment.model.Cliente;
import br.com.infnet.assessment.model.payload.ClientePayload;
import br.com.infnet.assessment.model.payload.Info;
import br.com.infnet.assessment.model.payload.ResponsePayload;
import br.com.infnet.assessment.service.ClienteService;
import br.com.infnet.assessment.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    Logger logger = LoggerFactory.getLogger(ClienteController.class);
    @Autowired
    ClienteService clienteService;
    @Autowired
    HttpUtil httpUtil;

    @GetMapping("/paginado")
    public ResponseEntity getAllByPageAndSize(@RequestParam(required = false, defaultValue = "100") Integer size,
                                 @RequestParam(required = false, defaultValue = "1") int page) {
        logger.info("Acessando getAllByPageAndSize(size: " + size + ", page: " + page + ")");
        int totalPaginas = clienteService.getTotalDePaginas(size);

        if (page > totalPaginas) {
            ResponsePayload errorPayload = new ResponsePayload("Página solicitada é maior que o total de páginas disponíveis.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
        }

        List<Cliente> all = clienteService.getByPageAndSize(page, size);
        Info info = new Info(all.size(), totalPaginas, page);
        ClientePayload clientePayload = new ClientePayload(all, info);
        HttpHeaders httpHeaders = httpUtil.getHttpHeaders(size, page);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(clientePayload);
    }

    @GetMapping
    public ResponseEntity getAll() {
        logger.info("Acessando getAll()");
        List<Cliente> all = clienteService.getAll();
        int totalPaginas = clienteService.getTotalDePaginas(all.size());

        Info info = new Info(all.size(), 1, 1);
        ClientePayload clientePayload = new ClientePayload(all, info);
        HttpHeaders httpHeaders = httpUtil.getHttpHeaders(all.size(), 1);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(clientePayload);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        logger.info("Acessando getById(id: " + id + ")");
        try{
            Cliente cliente = clienteService.getById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente Inexistente"));
            return ResponseEntity.ok(cliente);
        }catch (ResourceNotFoundException ex){
            ResponsePayload responsePayload = new ResponsePayload(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePayload);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        logger.info("Acessando deleteById(id: " + id + ")");
        try {
            Cliente removed = clienteService.deleteById(id);
            return ResponseEntity.ok(removed);
        }catch (ResourceNotFoundException ex){
            ResponsePayload responsePayload = new ResponsePayload(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePayload);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity updateCliente(@PathVariable Long id, @RequestBody  Cliente cliente){

        try{
            Cliente clienteAtual = clienteService.getById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente Inexistente"));
            Cliente returned = clienteService.update(id,cliente);
            logger.info("Acessando updateCliente(id: " + id + ", novo cliente: " + returned );
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("cliente-id",String.valueOf(clienteAtual.getId()));

            return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body(returned);

        }catch (ResourceNotFoundException ex){
            ResponsePayload responsePayload = new ResponsePayload(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePayload);
        }
    }

    @PostMapping
    public ResponseEntity createCliente(@RequestBody Cliente cliente){
        HttpHeaders httpHeaders = new HttpHeaders();
        Cliente returned = clienteService.create(cliente);
        logger.info("Acessando createCliente(novo cliente: " + returned );
        httpHeaders.set("cliente-id",String.valueOf(returned.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(returned);
    }
}