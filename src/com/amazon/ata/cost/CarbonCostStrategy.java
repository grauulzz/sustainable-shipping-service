package com.amazon.ata.cost;

import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarbonCostStrategy implements CostStrategy {
    
    // carbon units (cu)
    
    // sustainability index: (si)
    //  CORRUGATE: 0.017 cu/gram
    //  LAMINATED_PLASTIC: 0.012 cu/gram
    
    // carbonCost = mass * si(cu/gram)
    // carbonCost = mass.multiply(BigDecimal.valueOf(0.012)); //// lp
    // carbonCost = mass.multiply(BigDecimal.valueOf(0.017)); //// c
    
    private final Map<Material, BigDecimal> cuCostPerGram;
    
    public CarbonCostStrategy() {
        cuCostPerGram = new HashMap<>();
        cuCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(0.017));
        cuCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(0.012));
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        
        Packaging p = shipmentOption.getPackaging();
        
        BigDecimal carbonCost = this.cuCostPerGram.get(p.getMaterial());
        
        BigDecimal cost = p.getMass().multiply(carbonCost);
        
        return new ShipmentCost(shipmentOption, cost);
    }
}
