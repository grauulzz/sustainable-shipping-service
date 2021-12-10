package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.*;

import java.math.*;

import static org.junit.jupiter.api.Assertions.*;

public class MonetaryCostStrategyTest {

    private static final Packaging BOX_10x10x20 =
        new Box(Material.CORRUGATE, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(20));
    private static final Packaging Poly_10x10x20 =
        new PolyBag(Material.CORRUGATE, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private MonetaryCostStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MonetaryCostStrategy();
    }

    @Test
    void getCost_corrugateMaterial_returnsCorrectCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
            .withPackaging(BOX_10x10x20)
            .build();

        // WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertEquals(0, BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()),
                "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
    }
    
    @Test
    void getCost_laminatedPlasticMaterial_returnsCorrectCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
            .withPackaging(Poly_10x10x20)
            .build();

        // WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertEquals(BigDecimal.valueOf(0.5650).compareTo(shipmentCost.getCost()), 0);
    }
}
