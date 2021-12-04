package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackagingDAOTest {

    private Item testItem = createItem("30", "30", "30");
    private Item smallItem = createItem("5", "5", "5");

    private FulfillmentCenter ind1 = new FulfillmentCenter("IND1");
    private FulfillmentCenter abe2 = new FulfillmentCenter("ABE2");
    private FulfillmentCenter iad2 = new FulfillmentCenter("IAD2");
    
    
    FcPackagingOption p1 = new FcPackagingOption(ind1, new Packaging(Material.CORRUGATE,
            BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    
    FcPackagingOption p2 = new FcPackagingOption(abe2, new Packaging(Material.CORRUGATE,
            BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    
    FcPackagingOption p3 = new FcPackagingOption(abe2, new Packaging(Material.CORRUGATE,
            BigDecimal.TEN,  BigDecimal.TEN,  BigDecimal.TEN));
    
    Packaging sameDimensions = p1.getPackaging();
    Packaging diffDimensions = p3.getPackaging();
    
    
    private PackagingDatastore datastore = new PackagingDatastore();
    private PackagingDAO packagingDAO;
    
    Map<FulfillmentCenter, Set<Packaging>> setMap;
    Map<FulfillmentCenter, Set<Packaging>> setMap2;
    
    Set<Packaging> packingSet1;
    Set<Packaging> packageSet2;
    Set<FcPackagingOption> fcpoSet;
    

    
    @Test
    public void whenHashCodeIsCalledOnPackaging_withDiffDimensions_thenDiffHashcode() {
        // GIVEN

        // WHEN + THEN
        Assertions.assertNotEquals(sameDimensions.hashCode(), diffDimensions.hashCode());
    }
    
    @Test
    public void testAddMethodOfSet_withDifferentDimensions_returnTrue() {
        // WHEN
        fcpoSet = new HashSet<>();

        // GIVEN
        fcpoSet.add(p1);
        fcpoSet.add(p2);
        
        //THEN
        assertEquals(fcpoSet.size(), 2);
    }
    
    @Test
    public void whenAddingToASet_oneSetCanAcceptMultiplePackagingDimensions_returnTrue() {
        // GIVEN
        packingSet1 = new HashSet<>();
        packingSet1.add(sameDimensions);
        packingSet1.add(diffDimensions);
        
        var ref = new Object() {
            int count = 0;
        };
        
        packingSet1.forEach(packaging -> {
            if (packaging != null) {
                ref.count++;
            }
        });
        
        // WHEN + THEN
        Assertions.assertEquals(ref.count, packingSet1.size());
    }
    
    
    @Test
    public void whenAddingSamePackagingDimensions_withSameFulfillmentCenter_returnsNoDuplicateKeys() {
        // GIVEN
        packingSet1 = new HashSet<>();
        packageSet2 = new HashSet<>();
        setMap = new HashMap<>();

        // WHEN
        packingSet1.add(sameDimensions);
        packageSet2.add(sameDimensions);
        setMap.put(ind1, new HashSet<>(packingSet1));
        setMap.put(ind1, new HashSet<>(packingSet1));
        
        // THEN
        Assertions.assertEquals(1, setMap.size());
    }
    
    
    @Test
    public void whenAddingDiffPackagingDimensions_withSameFulfillmentCenter_returnsTwoMapEntries() {
        // GIVEN
        packingSet1 = new HashSet<>();
        packageSet2 = new HashSet<>();
        setMap = new HashMap<>();
        
        // WHEN
        packingSet1.add(sameDimensions);
        packageSet2.add(diffDimensions);
        setMap.put(ind1, new HashSet<>(packingSet1));
        setMap.put(ind1, new HashSet<>(packageSet2));
        
        // THEN
        Assertions.assertEquals(2, setMap.size());
    }
    
    @Test
    public void whenAddingFulfillmentCenterAsKeyInMap_CheckForDuplicateKeysByNextKeyOfMap_returnsFalse() {
        
        packagingDAO = new PackagingDAO(datastore);
        setMap = packagingDAO.getSetMap();
        
        
        Iterator<FulfillmentCenter> iterator = setMap.keySet().iterator();
        
        setMap.forEach((k, v) -> {
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
