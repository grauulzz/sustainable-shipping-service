package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.*;

import java.math.*;

import static org.junit.jupiter.api.Assertions.*;

public class CarbonCostStrategyTest {
    private static final Packaging BOX_10x10x20 =
            new Box(Material.CORRUGATE, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private static final Packaging POLYBAG_VOLUME_20000cc =
            new PolyBag(Material.LAMINATED_PLASTIC,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private CarbonCostStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new CarbonCostStrategy();
    }


    @Test
    void getCost_corrugateMaterial_returnsCorrectCarbonCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();

        // WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertEquals(0, BigDecimal.valueOf(17.00).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a box with dimensions 10x10x20.");
    }


    @Test
    void getCost_laminatedPlasticMaterial_returnsCorrectCarbonCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(POLYBAG_VOLUME_20000cc)
                .build();

        // WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN 0.3240
        assertEquals(0, BigDecimal.valueOf(0.3240).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a polybag with a volume of 20000cc.");
    }

}
