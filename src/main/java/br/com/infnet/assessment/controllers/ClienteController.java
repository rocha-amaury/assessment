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
//    @GetMapping
//    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "100") Integer size,
//                                 @RequestParam(required = false, defaultValue = "") String sort,
//                                 @RequestParam(required = false, defaultValue = "") String order,
//                                 @RequestParam(required = false, defaultValue = "1") int start,
//                                 @RequestParam(required = false, defaultValue = "10") int end,
//                                 @RequestParam(required = false, defaultValue = "1") int page
//
//
//    ){
//        List<Cliente> all = clienteService.getByPageAndSize(page,size);
//        int x = 1;
//
//        int totalDePaginas = 3;
//        Info info = new Info(200, 4, 1);
//        ClientePayload clientePayload = new ClientePayload(all, info);
//        HttpHeaders httpHeaders = httpUtil.getHttpHeaders(size, page);
//        return  ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(all);
//
//
//        //return clienteService.getAll(size,page,sort,order);
//    }

    @GetMapping
    public List<Cliente> getAll(){
        logger.info("GET ALL CLIENTES");
        return clienteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
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
        try {
            Cliente removed = clienteService.deleteById(id);
            return ResponseEntity.ok(removed);
        }catch (ResourceNotFoundException ex){
            ResponsePayload responsePayload = new ResponsePayload(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePayload);
        }
    }
    @PostMapping
    public ResponseEntity create(@RequestBody Cliente cliente){
        HttpHeaders httpHeaders = new HttpHeaders();
        Cliente returned = clienteService.create(cliente);

        httpHeaders.set("cliente-id",String.valueOf(returned.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).build();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody  Cliente cliente){
        logger.info("UPDATE CLIENTE: " + id + " novo cliente: " +cliente );
        clienteService.update(id,cliente);
    }

}