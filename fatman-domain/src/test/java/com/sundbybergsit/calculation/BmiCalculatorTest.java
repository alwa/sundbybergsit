package com.sundbybergsit.calculation;

import com.sundbybergsit.entities.FatmanDbUser;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;

import static org.junit.gen5.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit5.class)
public class BmiCalculatorTest {

    @Test
    public void bmiCalculated() throws Exception {
        FatmanDbUser dummyUser = mock(FatmanDbUser.class);
        when(dummyUser.getHeightInCentimetres()).thenReturn(170);
        Calculator<FatmanDbUser> bmiCalculator =
                (user, weight) ->
                        (float) weight / (
                                ((double) user.getHeightInCentimetres() / 100) *
                                        ((double) user.getHeightInCentimetres() / 100)
                        );
        Number bmi = bmiCalculator.calculate(dummyUser, 65.0F);
        assertTrue(bmi.doubleValue() < 23 && bmi.doubleValue() > 22);
    }

}
