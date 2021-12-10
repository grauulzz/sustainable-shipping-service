package tct;

import com.amazon.ata.cost.*;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.*;
import tct.basewrappers.BoxWrapper;
import tct.basewrappers.CarbonCostStrategyWrapper;
import tct.basewrappers.PolyBagWrapper;
import tct.basewrappers.ShipmentCostWrapper;
import tct.basewrappers.ShipmentOptionWrapper;

import java.math.BigDecimal;

import static com.amazon.ata.test.assertions.AtaAssertions.assertClose;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("MT05_CARBON")
public class MT5CarbonIntrospectionTests {
    @Test
    void mt5_carbonCostStrategy_getCostOfBox_resultsInCorrectCarbonCost() {
        // GIVEN - valid Box
        BoxWrapper boxWrapper = PackagingFactory.boxWrapperOfAnyDimensions();
        assertNotNull(boxWrapper, "Could not find any Boxes in PackagingDatastore");
        // shipment option wrapper using that Box
        ShipmentOptionWrapper shipmentOptionWrapper =
            ShipmentOptionFactory.shipmentOptionWrapperForPackaging(boxWrapper);
        // CarbonCostStrategyWrapper
        CarbonCostStrategyWrapper carbonCostStrategyWrapper = new CarbonCostStrategyWrapper();

        // WHEN
        ShipmentCostWrapper shipmentCostWrapper = carbonCostStrategyWrapper.getCost(shipmentOptionWrapper);

        // THEN - cost is accurate
        BigDecimal result = shipmentCostWrapper.getCost();
        BigDecimal expectedCarbonCost = CarbonCostStrategyWrapper.computeCarbonCost(boxWrapper);
        assertClose(
            expectedCarbonCost,
            result,
            String.format(
                "Expected carbon cost of %s to be %s, but was %s",
                boxWrapper.toString(),
                expectedCarbonCost,
                result)
        );
    }

    @Test
    void mt5_carbonCostStrategy_getCostOfPolyBag_resultsInCorrectCarbonCost() {
        // GIVEN - valid PolyBag
        PolyBagWrapper polyBagWrapper = PackagingFactory.polyBagWrapperOfAnyVolume();
        assertNotNull(polyBagWrapper, "Could not find any PolyBags in PackagingDatastore");
        // shipment option wrapper using that PolyBag
        ShipmentOptionWrapper shipmentOptionWrapper =
            ShipmentOptionFactory.shipmentOptionWrapperForPackaging(polyBagWrapper);
        // CarbonCostStrategyWrapper
        CarbonCostStrategyWrapper carbonCostStrategyWrapper = new CarbonCostStrategyWrapper();

        // WHEN
        ShipmentCostWrapper shipmentCostWrapper = carbonCostStrategyWrapper.getCost(shipmentOptionWrapper);

        // THEN - cost is accurate
        BigDecimal result = shipmentCostWrapper.getCost();
        BigDecimal expectedCarbonCost = CarbonCostStrategyWrapper.computeCarbonCost(polyBagWrapper);
        assertClose(
            expectedCarbonCost,
            result,
            String.format(
                "Expected carbon cost of %s to be %s, but was %s",
                polyBagWrapper.toString(),
                expectedCarbonCost,
                result)
        );
    }

    public static class CarbonCostStrategyTest {

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

}
