package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;

import java.util.*;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {

    private final Set<FcPackagingOption> packagingOptionsSet;
    private final Map<FulfillmentCenter, Set<Packaging>> setMap;
    
    /**
     * Instantiates a PackagingDAO object.
     *
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
        this.packagingOptionsSet = new HashSet<>(datastore.getFcPackagingOptions());
        this.setMap = new HashMap<>();
        packagingOptionsSet.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
    }
    
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        
        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;
        
        if (setMap.containsKey(fulfillmentCenter)) {
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
