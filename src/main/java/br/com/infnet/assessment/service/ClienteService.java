package br.com.infnet.assessment.service;

import com.github.javafaker.Faker;
import br.com.infnet.assessment.exception.ResourceNotFoundException;
import br.com.infnet.assessment.model.Cliente;
import br.com.infnet.assessment.model.ExchangeRate;
import br.com.infnet.assessment.util.ExchangeRateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClienteService {
    Logger logger = LoggerFactory.getLogger(ClienteService.class);
    static ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        private static ExchangeRate exchangeRate = exchangeRateUtil.getLatestRatesByCode("BRL");
//    private static ExchangeRate exchangeRate = exchangeRateUtil.generateExchangeRate();
    private Map<Long, Cliente> clientes = generateClientes(100);
    private Long lastId=100L;

    private Map<Long, Cliente> generateClientes(int quantidade) {
        logger.info("Gerando Clientes");
        Map<Long, Cliente> clientes = new HashMap<Long, Cliente>();
        for (int i = 1; i <= quantidade; i++) {
            Cliente cliente=generateCliente(i);
            clientes.put((long) i, cliente);
        }
        return clientes;
    }

    public Cliente generateCliente(long id) {
        Faker faker = new Faker();
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(faker.name().fullName());
        cliente.setCpf(faker.number().digits(11));
        cliente.setSaldos(generateRandomSaldos());
        setSaldoTotalMoedaLocalCliente(cliente, cliente.getMoedaLocal());
        return cliente;
    }

    private static Map<String, Double> generateRandomSaldos() {
        Map<String, Double> saldos = new HashMap<>();
        saldos.put("BRL", Math.random() * 100);
        saldos.put("USD", Math.random() * 100);
        saldos.put("EUR", Math.random() * 100);

        return saldos;
    }

    public static Cliente setSaldoTotalMoedaLocalCliente(Cliente cliente, String moedaLocal) {

        Map<String, Double> conversionResults = new HashMap<>();
        double saldoTotal = 0;
        for (String currency : cliente.getSaldos().keySet()) {
            if (exchangeRate.getConversionRates().containsKey(currency)) {
                double balance = cliente.getSaldos().get(currency);
                double rate = exchangeRate.getConversionRates().get(currency);
                double results = balance / rate;
                conversionResults.put(currency, results);
                saldoTotal += results;
            }
        }
        cliente.setSaldoTotalMoedaLocal(saldoTotal);
        return cliente;
    }

    public List<Cliente> getAll() {
        return clientes.values().stream().toList();
    }

    public Optional<Cliente> getById(Long id) {
        Cliente cliente = clientes.get(id);
        if(cliente == null) return Optional.empty();
        return Optional.of(cliente);
    }

    public Cliente deleteById(Long id) {
        if(!clientes.containsKey(id)) throw new ResourceNotFoundException("Cliente Inexistente");
        Cliente removed = clientes.remove(id);
        return removed;
    }

    public Cliente update(long id, Cliente cliente){
        if(!clientes.containsKey(id)) throw new ResourceNotFoundException("Cliente Inexistente");
        clientes.remove(id);
        cliente.setId( id);
        setSaldoTotalMoedaLocalCliente(cliente, cliente.getMoedaLocal());
        clientes.put(id,cliente);
        return cliente;
    }

    private Long incrementId(){
        this.lastId++;
        return lastId;
    }

    public Cliente create(Cliente cliente){
        Long id = incrementId();
        cliente.setId(id);
        setSaldoTotalMoedaLocalCliente(cliente, cliente.getMoedaLocal());
        clientes.put(id, cliente);
        return cliente;
    }

    public long count() {
        return clientes.size();
    }

    public int getTotalDePaginas(int size){
        double totalSize = (double) count();
        double totalDePaginas =  totalSize / (double)size;
        return (int) Math.ceil(totalDePaginas);
    }

    public List<Cliente> getByPageAndSize(int page, int size){
        List<Cliente> all = getAll();
        int start = (page - 1) * size;
        int end = Math.min(page * size, all.size());
        return all.subList(start,end);
    }
}
