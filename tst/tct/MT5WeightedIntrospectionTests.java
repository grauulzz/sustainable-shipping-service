package tct;

import com.amazon.ata.test.reflect.*;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.*;
import tct.basewrappers.*;

import java.lang.reflect.*;
import java.math.*;

import static com.amazon.ata.test.assertions.AtaAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("MT05_WEIGHTED")
public class MT5WeightedIntrospectionTests {
    private MonetaryCostStrategyWrapper monetaryCostStrategyWrapper = new MonetaryCostStrategyWrapper();
    private CarbonCostStrategyWrapper carbonCostStrategyWrapper = new CarbonCostStrategyWrapper();

    @Test
    void mt5_weightedCostStrategy_getCostOfBox_resultsInCorrectWeightedCost() {
        // GIVEN - valid Box
        BoxWrapper boxWrapper = PackagingFactory.boxWrapperOfAnyDimensions();
        assertNotNull(boxWrapper, "Could not find any Boxes in PackagingDatastore");
        // shipment option wrapper using that Box with FC IAD2
        ShipmentOptionWrapper shipmentOptionWrapper =
            ShipmentOptionFactory.shipmentOptionWrapperForPackaging(boxWrapper);

        WeightedCostStrategyWrapper weightedCostStrategyWrapper = new WeightedCostStrategyWrapper(
            monetaryCostStrategyWrapper,
            carbonCostStrategyWrapper
        );

        // WHEN
        ShipmentCostWrapper shipmentCostWrapper = weightedCostStrategyWrapper.getCost(shipmentOptionWrapper);

        // THEN - cost is accurate, should be cheapest for IAD2
        BigDecimal result = shipmentCostWrapper.getCost();
        BigDecimal expectedWeightedCost = WeightedCostStrategyWrapper.computeWeightedCost(boxWrapper);
        assertClose(
            expectedWeightedCost,
            result,
            String.format(
                "Expected weighted cost of %s to be %s, but was %s",
                boxWrapper.toString(),
                expectedWeightedCost,
                result)
        );
    }

    @Test
    void mt5_weightedCostStrategy_getCostOfPolyBag_resultsInCorrectWeightedCost() {
        // GIVEN - valid Box
        PolyBagWrapper boxWrapper = PackagingFactory.polyBagWrapperOfAnyVolume();
        assertNotNull(boxWrapper, "Could not find any Boxes in PackagingDatastore");
        // shipment option wrapper using that Box with FC IAD2
        ShipmentOptionWrapper shipmentOptionWrapper =
            ShipmentOptionFactory.shipmentOptionWrapperForPackaging(boxWrapper);

        WeightedCostStrategyWrapper weightedCostStrategyWrapper = new WeightedCostStrategyWrapper(
            monetaryCostStrategyWrapper,
            carbonCostStrategyWrapper
        );

        // WHEN
        ShipmentCostWrapper shipmentCostWrapper = weightedCostStrategyWrapper.getCost(shipmentOptionWrapper);

        // THEN - cost is accurate, should be cheapest for IAD2
        BigDecimal result = shipmentCostWrapper.getCost();
        BigDecimal expectedWeightedCost = WeightedCostStrategyWrapper.computeWeightedCost(boxWrapper);
        assertClose(
            expectedWeightedCost,
            result,
            String.format(
                "Expected weighted cost of %s to be %s, but was %s",
                boxWrapper.toString(),
                expectedWeightedCost,
                result)
        );
    }

    @Test
    void mt5_appClass_createsShipmentOptionWithWeightedCostStrategy() {
        // GIVEN - the shipment option created from the app class
        Class<?> appClass = ProjectClassFactory.findClass("App");
        Method getShipmentService = MethodQuery.inType(appClass)
            .withExactName("getShipmentService")
            .findMethodOrFail();

        // WHEN
        Object shipmentService = MethodInvoker.invokeStaticMethodWithReturnValue(getShipmentService);

        // THEN - shipmentService's cost strategy is a WeightedCostStrategy
        try {
            Field costStrategyField = shipmentService.getClass().getDeclaredField("costStrategy");
            costStrategyField.setAccessible(true);
            assertEquals(WeightedCostStrategyWrapper.getWrappedClassStatic(),
                costStrategyField.get(shipmentService).getClass(),
                "Expected the ShipmentService to use a WeightedCostStrategy");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            // will print to their RDE logs so we can debug if needed
            e.printStackTrace();
            fail("Expected a 'costStrategy' field in the ShipmentService class. Has it been modified?");
        }
    }

//    public static class WeightedCostStrategyTest {
//
//        private static final Packaging BOX_10x10x20 =
//                new Box(Material.CORRUGATE, BigDecimal.valueOf(10),
//                        BigDecimal.valueOf(10), BigDecimal.valueOf(20));
//
//        private static final Packaging BoxLaminatedPlastic_10x10x20 =
//                new Box(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(10),
//                        BigDecimal.valueOf(10), BigDecimal.valueOf(20));
//
//        private static final Packaging POLYBAG_10X10X20 =
//                new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(10),
//                        BigDecimal.valueOf(10), BigDecimal.valueOf(20));
//
//        private static final Packaging PolyBagCorrugate_10x10x20 =
//                new PolyBag(Material.CORRUGATE, BigDecimal.valueOf(10),
//                        BigDecimal.valueOf(10), BigDecimal.valueOf(20));
//
//        private CostStrategy strategy;
//
//        @BeforeEach
//        void setUp() {
//            strategy = new WeightedCostStrategy();
//        }
//
//        @Test
//        void laminatedPlastic_getCarbonCost_returnsCost() {
//            // GIVEN
//            ShipmentOption laminatedPlastic = ShipmentOption.builder()
//                    .withPackaging(POLYBAG_10X10X20)
//                    .build();
//
//            // WHEN
//            strategy = new CarbonCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(laminatedPlastic);
//
//            // THEN 0.2592 is %80 of the cost strategy being called
//            assertEquals(0,
//                    BigDecimal.valueOf(0.3240).compareTo(shipmentCost.getCost()),
//                    "Incorrect carbon cost calculation for a polybag with a volume of 20000cc.");
//        }
//
//
//        @Test
//        void corrugate_getCarbonCost_returnsCost() {
//            // GIVEN
//            ShipmentOption corrugate = ShipmentOption.builder()
//                    .withPackaging(BOX_10x10x20)
//                    .build();
//
//            // WHEN
//            strategy = new CarbonCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(corrugate);
//
//            // THEN
//            assertEquals(0,
//                    BigDecimal.valueOf(17.00).compareTo(shipmentCost.getCost()),
//                    "Incorrect carbon cost calculation for a box with dimensions 10x10x20.");
//        }
//
//        @Test
//        void corrugate_getMonetaryCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(BOX_10x10x20)
//                    .build();
//
//            // WHEN
//            strategy = new MonetaryCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN
//            assertEquals(0, BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()),
//                    "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
//        }
//
//        @Test
//        void laminatedPlastic_getMonetaryCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(BOX_10x10x20)
//                    .build();
//
//            // WHEN
//            strategy = new MonetaryCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN
//            assertEquals(0, BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()),
//                    "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
//        }
//
//        @Test
//        void polybagCorrugateCombo_getMonetaryCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(PolyBagCorrugate_10x10x20)
//                    .build();
//
//            // WHEN material and packaging are swapped
//            strategy = new MonetaryCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN cost should be very cheap (PolyBag, Corrugate)
//            assertEquals(BigDecimal.valueOf(0.5650).compareTo(shipmentCost.getCost()), 0,
//                    "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
//        }
//
//        @Test
//        void boxLaminatedPlastic_getMonetaryCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(BoxLaminatedPlastic_10x10x20)
//                    .build();
//
//            // WHEN material and packaging are swapped
//            strategy = new MonetaryCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN Price should be very expensive (Box, LaminatedPlastic)
//            assertEquals(BigDecimal.valueOf(250.43).compareTo(shipmentCost.getCost()), 0,
//                    "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
//        }
//        @Test
//        void polybagCorrugateCombo_getCarbonCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(PolyBagCorrugate_10x10x20)
//                    .build();
//
//            // WHEN material and packaging are swapped
//            strategy = new CarbonCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN cost should be very cheap (PolyBag, Corrugate)
//            assertEquals(BigDecimal.valueOf(0.4590).compareTo(shipmentCost.getCost()), 0,
//                    "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
//        }
//
//        @Test
//        void boxLaminatedPlastic_getCarbonCost_returnsCost() {
//            // GIVEN
//            ShipmentOption option = ShipmentOption.builder()
//                    .withPackaging(BoxLaminatedPlastic_10x10x20)
//                    .build();
//
//            // WHEN material and packaging are swapped
//            strategy = new CarbonCostStrategy();
//            ShipmentCost shipmentCost = strategy.getCost(option);
//
//            // THEN Price should be very expensive (Box, LaminatedPlastic)
//            assertEquals(BigDecimal.valueOf(12.00).compareTo(shipmentCost.getCost()), 0,
//                    "Incorrect monetary cost calculation for a polybag of dimensions 10x10x20");
//        }
//
//    }


}
