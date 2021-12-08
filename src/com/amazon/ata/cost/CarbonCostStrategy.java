package com.amazon.ata.cost;

import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// ----carbonCost----
// carbon units: cu
// CORRUGATE_CARBON_UNITS_PER_GRAM: 0.017cu/g
// carbonCostCOR = CORRUGATE_CARBON_UNITS_PER_GRAM * mass
// carbonCostCOR = 0.017 * mass
// carbonCostLP = LAMINATED_PLASTIC_CARBON_UNITS_PER_GRAM * mass
// carbonCostLP = 0.012 * mass

public class CarbonCostStrategy implements CostStrategy {

    private final Map<Material, BigDecimal> carbonCost;
    
    public CarbonCostStrategy() {
        carbonCost = new HashMap<>();
        
        BigDecimal CORRUGATE_CARBON_UNITS_PER_GRAM = BigDecimal.valueOf(0.017);
        BigDecimal LAMINATED_PLASTIC_CARBON_UNITS_PER_GRAM = BigDecimal.valueOf(0.012);
        
        carbonCost.put(Material.CORRUGATE, CORRUGATE_CARBON_UNITS_PER_GRAM);
        carbonCost.put(Material.LAMINATED_PLASTIC, LAMINATED_PLASTIC_CARBON_UNITS_PER_GRAM);
    }
    
    public Map<Material, BigDecimal> getCarbonCost() {
        return carbonCost;
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        
        Packaging p = shipmentOption.getPackaging();
        
        BigDecimal carbonCost = this.carbonCost.get(p.getMaterial());
        
        BigDecimal cost = p.getMass().multiply(carbonCost);
        
        return new ShipmentCost(shipmentOption, cost);
    }
}
