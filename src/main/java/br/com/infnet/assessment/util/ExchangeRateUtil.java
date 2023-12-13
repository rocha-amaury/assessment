package br.com.infnet.assessment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import br.com.infnet.assessment.exception.ResourceNotFoundException;
import br.com.infnet.assessment.model.ExchangeRate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateUtil {
    private final String exchangeRateApiUrl = "https://v6.exchangerate-api.com/v6/";
    private final String exchangeRateApiKey= "4a1c42c39c0ff9f211e6cc07";

    public ExchangeRate generateExchangeRate() {
        String fileName = "exchangeRateResponse.txt";
        Path filePath = Paths.get("resources", fileName);
        if (!Files.exists(filePath)) {
            salvaExchangeRateJson();
        }
        return JsonFileUtil.readJsonFromFile(fileName);
    }

    public ExchangeRate getLatestRatesByCode(String code) {
        System.out.println("getLatestRatesByCode: " + code);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(new URI(exchangeRateApiUrl + exchangeRateApiKey + "/latest/" + code ))
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 404){
                throw new ResourceNotFoundException(response.body());
            }
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            ExchangeRate exchangeRate = mapper.readValue(response.body(), ExchangeRate.class);
            return exchangeRate;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertExchangeRateToJson(ExchangeRate exchangeRate) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(exchangeRate);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void salvaExchangeRateJson(){
        ExchangeRate exchangeRate = getLatestRatesByCode("BRL");
        String fileName = "exchangeRateResponse.txt";
        Path path = Path.of("resources/exchangeRateResponse.txt");
        String jsonResponse = convertExchangeRateToJson(exchangeRate);
        JsonFileUtil.saveJsonToFile(jsonResponse, fileName);
    }

    public Map<String, Object> getSupportedCodes() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(new URI(exchangeRateApiUrl + exchangeRateApiKey + "/codes" ))
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 404){
                throw new ResourceNotFoundException(response.body());
            }
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            Map<String, Object> responseMap = mapper.readValue(response.body(), HashMap.class);
            return responseMap;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

