package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The cost associated with a particular ShipmentOption as calculated by a specific strategy.
 */
public class ShipmentCost implements Comparable<ShipmentCost> {
    private final ShipmentOption shipmentOption;
    private final BigDecimal cost;

    /**
     * Initializes a ShipmentCost object.
     * @param shipmentOption - the ShipmentOption the cost was calculated for
     * @param cost - the cost of using the provided ShipmentOption
     */
    public ShipmentCost(ShipmentOption shipmentOption, BigDecimal cost) {
        this.shipmentOption = shipmentOption;
        this.cost = cost;
    }

    public ShipmentOption getShipmentOption() {
        return shipmentOption;
    }

    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentCost other = (ShipmentCost) o;
        return cost.compareTo(other.cost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost.doubleValue());
    }

    @Override
    public int compareTo(ShipmentCost other) {
        return cost.compareTo(other.cost);
    }

}
