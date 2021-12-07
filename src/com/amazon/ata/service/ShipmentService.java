package com.amazon.ata.service;

import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for finding the appropriate shipment option from all available options returned
 * by the PackagingDAO.
 */
public class ShipmentService {

    /**
     * PackagingDAO is used to retrieve all valid shipment options for a given fulfillment center and item.
     */
    private PackagingDAO dao;

    /**
     * A CostStrategy used to calculate the relative cost of a ShipmentOption.
     */
    private CostStrategy c;

    /**
     * Instantiates a new ShipmentService object.
     * @param dao packaging data access object used to retrieve all available shipment options
     * @param c cost strategy used to calculate the relative cost of a shipment option
     */
    public ShipmentService(PackagingDAO dao, CostStrategy c) {
        this.dao = dao;
        this.c = c;
    }
    /**
     * Finds the shipment option for the given item and fulfillment center with the lowest cost.
     *
     * @param item the item to package
     * @param center fulfillment center in which to look for the packaging
     * @return the lowest cost shipment option for the item and fulfillment center, or null if none found
     */
    public ShipmentOption findShipmentOption(final Item item, final FulfillmentCenter center) {
        try {
            List<ShipmentOption> results = this.dao.findShipmentOptions(item, center);
            return getLowestCostShipmentOption(results);
        } catch (Exception e) {
            return null;
        }
    }

    private ShipmentOption getLowestCostShipmentOption(List<ShipmentOption> results) {
        List<ShipmentCost> costs = applyCostStrategy(results);
        Collections.sort(costs);
        return costs.get(0).getShipmentOption();
    }

    private List<ShipmentCost> applyCostStrategy(List<ShipmentOption> results) {
        List<ShipmentCost> costs = new ArrayList<>();
        for (ShipmentOption option : results) {
            costs.add(c.getCost(option));
        }
        return costs;
    }
}
