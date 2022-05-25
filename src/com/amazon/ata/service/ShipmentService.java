package com.amazon.ata.service;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is responsible for finding the appropriate shipment option from all available options returned
 * by the PackagingDAO.
 */
public class ShipmentService {
    
    /**
     * PackagingDAO is used to retrieve all valid shipment options for a given fulfillment center and item.
     */
    private final PackagingDAO packagingDAO;
    
    /**
     * A CostStrategy used to calculate the relative cost of a ShipmentOption.
     */
    private final CostStrategy costStrategy;

    /**
     * Instantiates a new ShipmentService object.
     * @param packagingDAO packaging data access object used to retrieve all available shipment options
     * @param costStrategy cost strategy used to calculate the relative cost of a shipment option
     */
    public ShipmentService(PackagingDAO packagingDAO, CostStrategy costStrategy) {
        this.packagingDAO = packagingDAO;
        this.costStrategy = costStrategy;
    }
    /**
     * Finds the shipment option for the given item and fulfillment center with the lowest cost.
     *
     * @param item the item to package
     * @param fulfillmentCenter fulfillment center in which to look for the packaging
     * @return the lowest cost shipment option for the item and fulfillment center, or null if none found
     */
    public ShipmentOption findShipmentOption(final Item item,
                                             final FulfillmentCenter fulfillmentCenter) {

        List<ShipmentCost> sortOps = new ArrayList<>();

        packagingDAO.getSetMap().forEach((key, value) -> {

            try {
                throw new UnknownFulfillmentCenterException("Unknown Fulfillment Center");
            } catch (UnknownFulfillmentCenterException e){
                e.printStackTrace();
            }
            new ArrayList<>(value).forEach(packaging -> {
                try {
                    packaging.canFitItem(item);
                    throw new NoPackagingFitsItemException("No packaging found for item");
                } catch (NoPackagingFitsItemException e) {
                    e.printStackTrace();
                }
                sortOps.add(costStrategy.getCost(ShipmentOption.builder()
                        .withItem(item)
                        .withPackaging(packaging)
                        .withFulfillmentCenter(fulfillmentCenter)
                        .build()));
            });
        });

        Collections.sort(sortOps);

        return sortOps.get(0).getShipmentOption();
    }

}
