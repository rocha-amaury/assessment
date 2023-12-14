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
    void testaGenerateExchangeRate() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        ExchangeRate exchangeRate = exchangeRateUtil.generateExchangeRate();
        assertNotNull(exchangeRate);
    }

    @Test
    @DisplayName("GetLatestRatesByCode - Deve testar seleção de de cotação válida")
    void testaGetLatestRatesByCode() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        String code = "BRL";
        ExchangeRate exchangeRate = exchangeRateUtil.getLatestRatesByCode(code);
        assertNotNull(exchangeRate);
    }

    @Test
    @DisplayName("GetLatestRatesByCode - Deve testar seleção de de cotação inexistente")
    void testaGetLatestRatesByInvalidCode() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        String code = "ABC";
        assertThrows(ResourceNotFoundException.class, () -> exchangeRateUtil.getLatestRatesByCode(code));
    }

    @Test
    @DisplayName("Teste do método salvaExchangeRateJson")
    void testaSalvaExchangeRateJson() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        assertDoesNotThrow(exchangeRateUtil::salvaExchangeRateJson);
    }

    @Test
    @DisplayName("Teste do método GetSupportedCodes")
    void testaGetSupportedCodes() {
        ExchangeRateUtil exchangeRateUtil = new ExchangeRateUtil();
        assertDoesNotThrow(exchangeRateUtil::getSupportedCodes);
    }
}
