package br.com.infnet.assessment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.infnet.assessment.model.ExchangeRate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonFileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DIRECTORY_NAME = "resources";

    public static void saveJsonToFile(String json, String fileName) {
        try {
            Path directory = Paths.get(DIRECTORY_NAME);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path fullFilePath = directory.resolve(fileName);

            Files.write(fullFilePath, json.getBytes());
            System.out.println("JSON salvo em: " + fullFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ExchangeRate readJsonFromFile(String fileName) {
        try {
            Path fullFilePath = Paths.get(DIRECTORY_NAME, fileName);

            String jsonContent = new String(Files.readAllBytes(fullFilePath));

            return objectMapper.readValue(jsonContent, ExchangeRate.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

