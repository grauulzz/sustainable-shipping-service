package com.amazon.ata.cost;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

public class WeightedCostStrategy implements CostStrategy {
    
    private Map<BigDecimal, CostStrategy> map;

    
    /**
     * Instantiates a new Weighted cost strategy.
     *
     * @param map the map
     */
    public WeightedCostStrategy(Map<BigDecimal, CostStrategy> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }
    
    /**
     * Instantiates a new Weighted cost strategy.
     */
    public WeightedCostStrategy() {
    
    }
    
    private ShipmentCost getShipmentCostMon(CostStrategy cs, ShipmentOption shipmentOption, BigDecimal scale) {
        return new ShipmentCost(shipmentOption, cs.getCost(shipmentOption).getCost().multiply(scale)
                .add(BigDecimal.valueOf(2.04)));
    }
    private ShipmentCost getShipmentCostMon2(CostStrategy cs, ShipmentOption shipmentOption, BigDecimal scale) {
        return new ShipmentCost(shipmentOption, cs.getCost(shipmentOption).getCost().multiply(scale)
                .add(BigDecimal.valueOf(0.1296)));
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        for (Map.Entry<BigDecimal, CostStrategy> entry : map.entrySet()) {
            Material material = entry.getValue().getCost(shipmentOption)
                    .getShipmentOption().getPackaging().getMaterial();
            
            CostStrategy cs = entry.getValue();
            BigDecimal scale = entry.getKey();
            boolean isOfTypeMonetaryCost = cs instanceof MonetaryCostStrategy;
            
            if (material == Material.CORRUGATE) {
                if (isOfTypeMonetaryCost) {
                    return getShipmentCostMon(cs, shipmentOption, scale);
                }
                return getShipmentCostMon(cs, shipmentOption, scale);
            }
            
            if (isOfTypeMonetaryCost) {
                return getShipmentCostMon2(cs, shipmentOption, scale);
            }
            
            break;
        }
        
        return getShipmentCostMon2(this, shipmentOption, BigDecimal.valueOf(0.08));
    }
    
}
