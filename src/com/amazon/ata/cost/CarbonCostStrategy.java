package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarbonCostStrategy implements CostStrategy {
    
    private final Map<Material, BigDecimal> carbonCost;
    
    /**
     * Instantiates a new Carbon cost strategy.
     */
    public CarbonCostStrategy() {
        carbonCost = new HashMap<>();
        carbonCost.put(Material.CORRUGATE, BigDecimal.valueOf(0.017));
        carbonCost.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(0.012));
    }
    
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal shipmentCost = this.carbonCost.get(packaging.getMaterial());
        BigDecimal cost = packaging.getMass().multiply(shipmentCost);
        
        return new ShipmentCost(shipmentOption, cost);
    }
}
