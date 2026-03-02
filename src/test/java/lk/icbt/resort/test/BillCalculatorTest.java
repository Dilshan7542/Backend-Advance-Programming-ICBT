package lk.icbt.resort.test;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillCalculatorTest {

    @Test
    void total_shouldBeNightsMultiplyRate() {
        int nights = 3;
        BigDecimal rate = new BigDecimal("12000.00");

        BigDecimal total = rate.multiply(BigDecimal.valueOf(nights));

        assertEquals(new BigDecimal("36000.00"), total);
    }
}