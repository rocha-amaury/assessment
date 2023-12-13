package br.com.infnet.assessment;

import br.com.infnet.assessment.exception.ResourceNotFoundException;
import br.com.infnet.assessment.model.ExchangeRate;
import br.com.infnet.assessment.util.ExchangeRateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateUtilTest {

    @Test
    @DisplayName("GenerateExchangeRate - Deve retornar objeto ExchangeRate")
    void testGenerateExchangeRate() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        ExchangeRate exchangeRate = exchangeRateUtil.generateExchangeRate();
        assertNotNull(exchangeRate);
    }

    @Test
    @DisplayName("GetLatestRatesByCode - Deve testar seleção de de cotação válida")
    void testGetLatestRatesByCode() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        String code = "BRL";
        ExchangeRate exchangeRate = exchangeRateUtil.getLatestRatesByCode(code);
        assertNotNull(exchangeRate);
    }

    @Test
    @DisplayName("GetLatestRatesByCode - Deve testar seleção de de cotação inexistente")
    void testGetLatestRatesByInvalidCode() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        String invalidCode = "ABC";
        assertThrows(ResourceNotFoundException.class, () -> exchangeRateUtil.getLatestRatesByCode(invalidCode));
    }

    @Test
    @DisplayName("Teste do método salvaExchangeRateJson")
    void testSalvaExchangeRateJson() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        assertDoesNotThrow(exchangeRateUtil::salvaExchangeRateJson);
    }

    @Test
    @DisplayName("Teste do método GetSupportedCodes")
    void testGetSupportedCodes() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        assertDoesNotThrow(exchangeRateUtil::getSupportedCodes);
    }
}
