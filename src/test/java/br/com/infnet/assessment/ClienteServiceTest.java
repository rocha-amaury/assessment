package br.com.infnet.assessment;

import br.com.infnet.assessment.exception.ResourceNotFoundException;
import br.com.infnet.assessment.model.Cliente;
import br.com.infnet.assessment.service.ClienteService;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClienteServiceTest {

    Logger logger = LoggerFactory.getLogger(ClienteServiceTest.class);

    private ClienteService clienteService;

    @BeforeEach
    void setUp() { clienteService = new ClienteService(); }

    @Test
    @DisplayName("Get - Deve retornar todos os clientes")
    void testaGetAll() {
        List<Cliente> clientes = clienteService.getAll();
        assertThat(clientes).hasSize(100);
    }

    @Test
    @DisplayName("Get - Deve testar seleção de Id existente")
    void testaGetById() {
        long id = 1;
        Optional<Cliente> clienteOptional = clienteService.getById(id);
        assertTrue(clienteOptional.isPresent());
        assertEquals(id, clienteOptional.get().getId());
    }

    @Test
    @DisplayName("Get - Deve testar seleção de Id inexistente")
    void testaGetByIdInexistente() {
        long id = 1000;
        Optional<Cliente> clienteOptional = clienteService.getById(id);
        assertTrue(clienteOptional.isEmpty());
    }

    @Test
    @DisplayName("Delete - Deve testar deleção de Id existente")
    void testaDeleteById() {
        long id = 1;
        Cliente removed = clienteService.deleteById(id);
        assertThat(clienteService.getAll()).doesNotContain(removed);
    }

    @Test
    @DisplayName("Delete - Deve testar erro ResourceNotFoundException")
    void testaDeleteByIdThrowResourceNotFoundException() {
        long id = 1000;
        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteById(id));
    }

    @Test
    @DisplayName("Update - Deve testar atualização de Id existente")
    void testaUpdate() {
        long id = 1;
        Cliente clienteToUpdate = clienteService.getById(id).orElseThrow();
        clienteToUpdate.setNome("UpdatedName");
        Cliente clienteUpdated = clienteService.update(id, clienteToUpdate);
        assertEquals("UpdatedName", clienteUpdated.getNome());
    }

    @Test
    @DisplayName("Update - Deve testar erro ResourceNotFoundException")
    void testaUpdateThrowResourceNotFoundException() {
        long id = 1000;
        Cliente clienteUpdate = new Cliente();
        assertThrows(ResourceNotFoundException.class, () -> clienteService.update(id, clienteUpdate));
    }
}
