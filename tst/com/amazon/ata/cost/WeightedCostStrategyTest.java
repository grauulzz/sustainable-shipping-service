package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.*;

import java.math.*;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedCostStrategyTest {

    private static final Packaging BOX_10x10x20 =
            new Box(Material.CORRUGATE, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private static final Packaging BoxLaminatedPlastic_10x10x20 =
            new Box(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private static final Packaging POLYBAG_10X10X20 =
            new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private static final Packaging PolyBagCorrugate_10x10x20 =
            new PolyBag(Material.CORRUGATE, BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private CostStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new WeightedCostStrategy();
    }

    @Test
    void laminatedPlastic_getCarbonCost_returnsCost() {
        // GIVEN
        ShipmentOption laminatedPlastic = ShipmentOption.builder()
                .withPackaging(POLYBAG_10X10X20)
                .build();

        // WHEN
        strategy = new CarbonCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(laminatedPlastic);

        // THEN 0.2592 is %80 of the cost strategy being called
        assertEquals(0,
                BigDecimal.valueOf(0.3240).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a polybag with a volume of 20000cc.");
    }


    @Test
    void corrugate_getCarbonCost_returnsCost() {
        // GIVEN
        ShipmentOption corrugate = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();

        // WHEN
        strategy = new CarbonCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(corrugate);

        // THEN
        assertEquals(0,
                BigDecimal.valueOf(17.00).compareTo(shipmentCost.getCost()),
                "Incorrect carbon cost calculation for a box with dimensions 10x10x20.");
    }

    @Test
    void corrugate_getMonetaryCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();

        // WHEN
        strategy = new MonetaryCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertEquals(0, BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()),
                "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
    }

    @Test
    void laminatedPlastic_getMonetaryCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_10x10x20)
                .build();

        // WHEN
        strategy = new MonetaryCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertEquals(0, BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()),
                "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
    }

    @Test
    void polybagCorrugateCombo_getMonetaryCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(PolyBagCorrugate_10x10x20)
                .build();

        // WHEN material and packaging are swapped
        strategy = new MonetaryCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN cost should be very cheap (PolyBag, Corrugate)
        assertEquals(BigDecimal.valueOf(0.5650).compareTo(shipmentCost.getCost()), 0,
                "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
    }

    @Test
    void boxLaminatedPlastic_getMonetaryCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BoxLaminatedPlastic_10x10x20)
                .build();

        // WHEN material and packaging are swapped
        strategy = new MonetaryCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN Price should be very expensive (Box, LaminatedPlastic)
        assertEquals(BigDecimal.valueOf(250.43).compareTo(shipmentCost.getCost()), 0,
                "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
    }
    @Test
    void polybagCorrugateCombo_getCarbonCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(PolyBagCorrugate_10x10x20)
                .build();

        // WHEN material and packaging are swapped
        strategy = new CarbonCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN cost should be very cheap (PolyBag, Corrugate)
        assertEquals(BigDecimal.valueOf(0.4590).compareTo(shipmentCost.getCost()), 0,
                "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
    }

    @Test
    void boxLaminatedPlastic_getCarbonCost_returnsCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BoxLaminatedPlastic_10x10x20)
                .build();

        // WHEN material and packaging are swapped
        strategy = new CarbonCostStrategy();
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN Price should be very expensive (Box, LaminatedPlastic)
        assertEquals(BigDecimal.valueOf(12.00).compareTo(shipmentCost.getCost()), 0,
                "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
    }

}

