package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.*;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {

//    private final Set<FcPackagingOption> packagingOptionsSet;
//    private final List<FcPackagingOption> packagingOptionsList;
    private final Map<FulfillmentCenter, Set<Packaging>> setMap;
    
    /**
     * Instantiates a PackagingDAO object.
     *
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
//        this.packagingOptionsList = new ArrayList<>(datastore.getFcPackagingOptions());
//        this.packagingOptionsSet = new HashSet<>();
//        packagingOptionsList.forEach(this::addPackaging);
//        this.packagingOptionsSet = new HashSet<>(datastore.getFcPackagingOptions());
        this.setMap = new HashMap<>();
        datastore.getFcPackagingOptions().forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
    }
//    public void printMp() {
//        setMap.forEach((k, v) -> System.out.printf("key: %s, value: %s%n", k, v));
//    }
//    public void addPackaging(FcPackagingOption p) {
//        this.packagingOptionsSet.add(p);
//    }
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
