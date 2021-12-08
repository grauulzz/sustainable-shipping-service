package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeightedCostStrategyTest {
    private static final Packaging BOX_10x10x20 =
            new Box(Material.CORRUGATE, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));
    
    private static final Packaging POLYBAG_VOLUME_20000cc =
            new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));
    
    private WeightedCostStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new WeightedCostStrategy();
    }
    
    @Test
    void fromWeightedCS_getEightyPercentOfCarbonCost_returnsWeightedCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(POLYBAG_VOLUME_20000cc)
                .build();
        
        // WHEN
        strategy = new WeightedCostStrategy(new CarbonCostStrategy(),  BigDecimal.valueOf(.80));
        ShipmentCost shipmentCost = strategy.getCost(option);
        
        // THEN 0.2592 is %80 of the cost strategy being called
        assertEquals(0,
                BigDecimal.valueOf(0.2592).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a polybag with a volume of 20000cc.");
    }
    
     @Test
    void fromWeightedCS_getCostLaminatedPlasticMaterial_returnsCorrectCarbonCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(POLYBAG_VOLUME_20000cc)
                .build();
        
        // WHEN
        strategy = new WeightedCostStrategy(new CarbonCostStrategy());
        ShipmentCost shipmentCost = strategy.getCostStrategy(option);
        
        // THEN 0.3240
        assertEquals(0,
                BigDecimal.valueOf(0.3240).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a polybag with a volume of 20000cc.");
    }
    
    @Test
    void fromWeightedCS_getCostCorrugateMaterial_returnsCorrectCarbonCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();
        
        // WHEN
        strategy = new WeightedCostStrategy(new CarbonCostStrategy());
        ShipmentCost shipmentCost = strategy.getCostStrategy(option);
        
        // THEN
        assertEquals(0,
                BigDecimal.valueOf(17.00).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a box with dimensions 10x10x20.");
    }
    
    @Test
    void fromWeightedCS_corrugateMaterial_returnsCorrectMonetaryCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();
        
        // WHEN
        strategy = new WeightedCostStrategy(new MonetaryCostStrategy());
        ShipmentCost shipmentCost = strategy.getCostStrategy(option);
        
        // THEN
        assertTrue(BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()) == 0,
                "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
    }
    
    @Test
    void fromWeightedCS_corrugatePlasticMaterial_returnsCorrectMonetaryCost() {
        // GIVEN
        Packaging Poly_10x10x20 =
                new PolyBag(Material.CORRUGATE, BigDecimal.valueOf(10),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(20));
        
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(Poly_10x10x20)
                .build();
        
        // WHEN
        strategy = new WeightedCostStrategy(new MonetaryCostStrategy());
        ShipmentCost shipmentCost = strategy.getCostStrategy(option);
        
        // THEN
        assertEquals(BigDecimal.valueOf(0.5650).compareTo(shipmentCost.getCost()), 0,
                "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
    }
    
}
