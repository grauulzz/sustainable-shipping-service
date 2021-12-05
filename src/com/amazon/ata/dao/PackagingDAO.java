package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    /**
     * A list of fulfillment centers with a packaging options they provide.
     */
    private final Set<FcPackagingOption> packagingOptionsSet;
    private final List<FcPackagingOption> packagingOptionsList;
    private final Map<FulfillmentCenter, Set<Packaging>> setMap;
    
    /**
     * Instantiates a PackagingDAO object.
     *
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {

        this.packagingOptionsList = new ArrayList<>(datastore.getFcPackagingOptions());
        this.setMap = new HashMap<>();
        this.packagingOptionsSet = new HashSet<>();
        packagingOptionsList.forEach(this::addPackaging);
        
        packagingOptionsList.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
    }
    
    public void printMp() {
        setMap.forEach((k, v) -> System.out.printf("key: %s, value: %s%n", k, v));
    }
    
    public void addPackaging(FcPackagingOption p) {
        this.packagingOptionsSet.add(p);
    }
    
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        
        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;
    
        for (Map.Entry<FulfillmentCenter, Set<Packaging>> entry : setMap.entrySet()) {
            if (entry.getKey().equals(fulfillmentCenter)) {
                fcFound = true;
                entry.getValue().forEach(value -> {
                    if (value.canFitItem(item)) {
                        result.add(ShipmentOption.builder()
                                .withItem(item)
                                .withPackaging(value)
                                .withFulfillmentCenter(fulfillmentCenter)
                                .build());
                    }
                });
            }
        }
        
//        for (Map.Entry<FulfillmentCenter, Set<Packaging>> e : setMap.entrySet()) {
//            if (e.getKey().equals(fulfillmentCenter)) {
//                fcFound = true;
//                for (Packaging p : e.getValue()) {
//                    if (p.canFitItem(item)) {
//                        result.add(ShipmentOption.builder()
//                                .withItem(item)
//                                .withPackaging(p)
//                                .withFulfillmentCenter(fulfillmentCenter)
//                                .build());
//                    }
//                }
//            }
//        }
        
        // Notify caller about unexpected results
        if (!fcFound) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }
        
        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }
        
        return result;
    }
    
    public Map<FulfillmentCenter, Set<Packaging>> getSetMap() {
        return setMap;
    }
    
}
