package br.com.infnet.assessment;

import br.com.infnet.assessment.controllers.ClienteController;
import br.com.infnet.assessment.model.Cliente;
import br.com.infnet.assessment.service.ClienteService;
import br.com.infnet.assessment.util.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;

class ClienteControllerTest {
    private ClienteService clienteService = new ClienteService();
    private HttpUtil httpUtil = new HttpUtil(clienteService);
    private ClienteController clienteController = new ClienteController(clienteService, httpUtil);

    @Test
    void testaGetAllByPageAndSize() {
        int size = (int) clienteService.count();
        int page = 1;
        ResponseEntity responseEntity = clienteController.getAllByPageAndSize(size, page);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaGetAllByPageAndSizeInvalidPage() {
        int size = 10;
        int page = clienteService.getTotalDePaginas(size) + 1;
        ResponseEntity responseEntity = clienteController.getAllByPageAndSize(size, page);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaGetAll() {
        ResponseEntity responseEntity = clienteController.getAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaGetById() {
        Long existingId = 1L;
        ResponseEntity responseEntity = clienteController.getById(existingId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaGetByIdWithNonExistingId() {
        Long nonExistingId = 1000L;
        ResponseEntity responseEntity = clienteController.getById(nonExistingId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaDeleteById() {
        Long existingId = 1L;
        ResponseEntity responseEntity = clienteController.deleteById(existingId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaDeleteByIdWithNonExistingId() {
        Long nonExistingId = 1000L;
        ResponseEntity responseEntity = clienteController.deleteById(nonExistingId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaUpdateCliente() {
        Long id = 1L;
        Cliente updatedCliente = clienteService.generateCliente(id);
        ResponseEntity responseEntity = clienteController.updateCliente(id, updatedCliente);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        HttpHeaders headers = responseEntity.getHeaders();
        assertNotNull(headers.getFirst("cliente-id"));
    }

    @Test
    void testaUpdateClienteWithNonExistingId() {
        Long id = 1000L;
        Cliente updatedCliente = clienteService.generateCliente(id);
        ResponseEntity responseEntity = clienteController.updateCliente(id, updatedCliente);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testaCreateCliente() {
        long id = clienteService.count()+1;
        Cliente newCliente = clienteService.generateCliente(id);

        ResponseEntity responseEntity = clienteController.createCliente(newCliente);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        HttpHeaders headers = responseEntity.getHeaders();
        assertNotNull(headers.getFirst("cliente-id"));
    }
}
