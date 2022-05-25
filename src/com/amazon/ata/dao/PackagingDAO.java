package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.*;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {

    private final Set<FcPackagingOption> packagingOptionsSet;
    private final Map<FulfillmentCenter, Set<Packaging>> setMap;


    /**
     * Instantiates a new Packaging dao.
     *
     * @param datastore the datastore
     */
    public PackagingDAO(PackagingDatastore datastore) {
        this.packagingOptionsSet = new HashSet<>(datastore.getFcPackagingOptions());
        this.setMap = new HashMap<>();
        packagingOptionsSet.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
    }

    /**
     * Find shipment options list.
     *
     * @param item              the item
     * @param fulfillmentCenter the fulfillment center
     * @return the list
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;

        if (setMap.containsKey(fulfillmentCenter)) {
            for (Map.Entry<FulfillmentCenter, Set<Packaging>> entry : setMap.entrySet()) {
                if (entry.getKey().equals(fulfillmentCenter)) {
                    fcFound = true;
                    entry.getValue().stream().filter(value -> value.canFitItem(item)).forEach(value -> {

                        result.add(ShipmentOption.builder()
                                .withItem(item)
                                .withPackaging(value)
                                .withFulfillmentCenter(entry.getKey())
                                .build());

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
