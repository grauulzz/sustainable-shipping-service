package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackagingDAOTest {
    
    @Mock
    private Item testItem = createItem("30", "30", "30");
    private Item smallItem = createItem("5", "5", "5");
    
    @Mock
    private FulfillmentCenter ind1 = new FulfillmentCenter("IND1");
    private FulfillmentCenter abe2 = new FulfillmentCenter("ABE2");
    private FulfillmentCenter iad2 = new FulfillmentCenter("IAD2");
    
    @Mock
    FcPackagingOption p1 = new FcPackagingOption(ind1, new Box(Material.CORRUGATE,
            BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    FcPackagingOption p2 = new FcPackagingOption(iad2, new Box(Material.CORRUGATE,
            BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    FcPackagingOption p3 = new FcPackagingOption(abe2, new Box(Material.CORRUGATE,
            BigDecimal.TEN,  BigDecimal.TEN,  BigDecimal.TEN));
    
//    FcPackagingOption p2Poly1 = new FcPackagingOption(iad2, new PolyBag(Material.LAMINATED_PLASTIC, new BigDecimal("10000")));
//    FcPackagingOption p2Poly2 = new FcPackagingOption(iad2, new PolyBag(Material.LAMINATED_PLASTIC, new BigDecimal("2000")));
    
    @Mock
    Packaging sameDimensions = p1.getPackaging();
    Packaging diffDimensions = p3.getPackaging();
    private final PackagingDatastore datastore = new PackagingDatastore();
    private PackagingDAO packagingDAO;
    Map<FulfillmentCenter, Set<Packaging>> setMap;
    Map<FulfillmentCenter, Set<Packaging>> setMap2;
    
    @Test
    public void whenAddingDiffPackagingDimensions_withSameFulfillmentCenter_returnsTwoDiffMapEntries() {
        // GIVEN
        Set<FcPackagingOption> singlePackingSet1 = new HashSet<>(Collections.singleton(p1));
        Set<FcPackagingOption> singlePackingSet2 = new HashSet<>(Collections.singleton(p2));
        setMap = new HashMap<>();
        
        // WHEN
        singlePackingSet1.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
        
        singlePackingSet2.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
        
        // THEN
        Assertions.assertEquals(2, setMap.size());
    }
    
//    @Test
//    public void diffPackagingOptions_withSameFulfillmentCenter_returnsTwoDiffMapEntries() {
//        // GIVEN
//        setMap = new HashMap<>();
//        Packaging pack = new PolyBag(Material.LAMINATED_PLASTIC,
//                BigDecimal.valueOf(10000));
//
//        Packaging pack2 = new PolyBag(Material.LAMINATED_PLASTIC,
//                BigDecimal.valueOf(2000));
//
//        Set<Packaging> set1 = new HashSet<>(Collections.singleton(pack));
//        Set<Packaging> set2 = new HashSet<>(Collections.singleton(pack2));
//        // WHEN
//        setMap.put(iad2, set1);
//        setMap.put(iad2, set2);
//        // THEN
//        Assertions.assertEquals(1, setMap.size());
//    }
    
    @Test
    public void whenHashCodeIsCalledOnPackaging_withDiffDimensions_thenDiffHashcode() {
        // GIVEN + WHEN + THEN
        Assertions.assertNotEquals(sameDimensions.hashCode(), diffDimensions.hashCode());
    }
    
    @Test
    public void whenHashCodeIsCalledOnPackaging_withSameDimensions_thenSameHashcode() {
        // GIVEN + WHEN + THEN
        Assertions.assertEquals(sameDimensions.hashCode(), sameDimensions.hashCode());
    }
    
    @Test
    public void testAddMethodOfSet_withDifferentDimensions_acceptsAddingElements() {
        // WHEN
        Set<FcPackagingOption> packagingSet = new HashSet<>();

        // GIVEN
        packagingSet.add(p1);
        packagingSet.add(p2);
        
        //THEN
        assertEquals(packagingSet.size(), 2);
    }
    
    @Test
    public void testAddMethodOfSet_withSameDimensions_rejectsAddingElements() {
        // WHEN
        Set<FcPackagingOption> packagingSet = new HashSet<>();

        // GIVEN
        packagingSet.add(p1);
        packagingSet.add(p1);
        
        //THEN
        assertEquals(packagingSet.size(), 1);
    }
    
    @Test
    public void whenAddingToASet_setCanAcceptAnArbitraryNumberOfAddToSet_returnTrue() {
        // GIVEN
        Set<Packaging> packageSet1 = new HashSet<>();
        Random random = new Random();
        int value = random.nextInt(5);
    
        // WHEN
        for (int i = 0; i < value; i++) {
            packageSet1.add(sameDimensions);
            packageSet1.add(diffDimensions);
        }
        var ref = new Object() {
            int count = 0;
        };
        packageSet1.forEach(packaging -> {
            if (packaging != null) {
                ref.count++;
            }
        });
        
        // THEN
        Assertions.assertEquals(ref.count, packageSet1.size());
    }

    
    @Test
    public void whenAddingSamePackagingDimensions_withSameFulfillmentCenter_returnsNoDuplicateKeys() {
        // GIVEN
        setMap = new HashMap<>();
        Set<Packaging> packageSet1 = new HashSet<>();
        Set<Packaging> packageSet2 = new HashSet<>();

        // WHEN
        packageSet1.add(sameDimensions);
        packageSet2.add(sameDimensions);
        setMap.put(ind1, new HashSet<>(packageSet1));
        setMap.put(ind1, new HashSet<>(packageSet2));
        
        // THEN
        Assertions.assertEquals(1, setMap.size());
    }
    
    @Test
    public void whenAddingFulfillmentCenterAsKeyInMap_CheckForDuplicateKeysByNextKeyOfMap_returnsFalse() {
        
        packagingDAO = new PackagingDAO(datastore);
        Map<FulfillmentCenter, Set<Packaging>> setMapFc = packagingDAO.getSetMap();
        
        Iterator<FulfillmentCenter> iterator = setMapFc.keySet().iterator();
    
        setMapFc.forEach((k, v) -> {
            boolean dupe = k.getFcCode().equals(iterator.next().toString());
            Assertions.assertFalse(dupe);
        });
        
    }
    
    @Test
    public void whenPutIsCalled_withDifFulfillmentCenterAndSamePackagingDimensions_thenMapReturnsDiffHashcode() {
        // GIVEN
        setMap = new HashMap<>();
        setMap2 = new HashMap<>();
        
        // WHEN
        setMap.put(p1.getFulfillmentCenter(), new HashSet<>(Collections.singleton(p1.getPackaging())));
        setMap2.put(p2.getFulfillmentCenter(), new HashSet<>(Collections.singleton(p1.getPackaging())));
        
        // THEN
        Assertions.assertNotEquals(setMap.hashCode(), setMap2.hashCode());
    }
    

    @Test
    public void findShipmentOptions_unknownFulfillmentCenter_throwsUnknownFulfillmentCenterException() {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter("nonExistentFcCode");

        // WHEN + THEN
        assertThrows(UnknownFulfillmentCenterException.class, () -> {
            packagingDAO.findShipmentOptions(testItem, fulfillmentCenter);
        }, "When asked to ship from an unknown fulfillment center, throw UnknownFulfillmentCenterException.");
    }

    @Test
    public void findShipmentOptions_packagingDoesntFit_throwsNoPackagingFitsItemException() {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN + THEN
        assertThrows(NoPackagingFitsItemException.class, () -> {
            packagingDAO.findShipmentOptions(testItem, ind1);
        }, "When no packaging can fit the item, throw NoPackagingFitsItemException.");
    }

    @Test
    public void findShipmentOptions_onePackagingAvailableAndFits_singlePackaging() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(smallItem, ind1);

        // THEN
        assertEquals(1, shipmentOptions.size(),
            "When fulfillment center has packaging that can fit item, return a ShipmentOption with the item, "
                + "fulfillment center, and packaging that can fit the item.");
    }

    @Test
    public void findShipmentOptions_twoPackagingAvailableAndOneFits_singlePackaging() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(testItem, abe2);

        // THEN
        assertEquals(1, shipmentOptions.size(),
            "When fulfillment center has packaging that can fit item, return a ShipmentOption with the item, "
                + "fulfillment center, and packaging that can fit the item.");
    }

    @Test
    public void findShipmentOptions_twoPackagingAvailableAndBothFit_twoPackagingOptions() throws Exception {
        // GIVEN
        packagingDAO = new PackagingDAO(datastore);

        // WHEN
        List<ShipmentOption> shipmentOptions = packagingDAO.findShipmentOptions(smallItem, abe2);

        // THEN
        assertEquals(2, shipmentOptions.size(),
            "When fulfillment center has multiple packaging that can fit item, return a ShipmentOption "
                + "for each.");
    }

    private Item createItem(String length, String width, String height) {
        return Item.builder()
                .withAsin("B00TEST")
                .withDescription("Test Item")
                .withHeight(new BigDecimal(length))
                .withWidth(new BigDecimal(width))
                .withLength(new BigDecimal(height))
                .build();
    }
}
