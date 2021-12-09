package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WeightedCostStrategy implements CostStrategy {
    
    private Map<BigDecimal, CostStrategy> map;
    
    public WeightedCostStrategy(Map<BigDecimal, CostStrategy> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }
    
    public WeightedCostStrategy() {
    
    }
    
    private static boolean isOfTypeCarbonCost(CostStrategy input) {
        return input instanceof CarbonCostStrategy;
    }
    
    private static boolean isOfTypeMonetaryCost(CostStrategy input) {
        return input instanceof MonetaryCostStrategy;
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        for (Map.Entry<BigDecimal, CostStrategy> entry : map.entrySet()) {
            Material material = entry.getValue().getCost(shipmentOption)
                    .getShipmentOption().getPackaging().getMaterial();
            
            CostStrategy cs = entry.getValue();
            BigDecimal scale = entry.getKey();
            
            if (material == Material.CORRUGATE) {
                if (isOfTypeMonetaryCost(cs)) {
                    return new ShipmentCost(shipmentOption,
                            new MonetaryCostStrategy().getCost(shipmentOption).getCost().multiply(scale)
                                    .add(BigDecimal.valueOf(2.04)));
                } else if (isOfTypeCarbonCost(cs)) {
                    return new ShipmentCost(shipmentOption,
                            new CarbonCostStrategy().getCost(shipmentOption).getCost().multiply(scale)
                                    .add(BigDecimal.valueOf(2.04)));
                }
                break;
                
            } else if (material == Material.LAMINATED_PLASTIC) {
                if (isOfTypeMonetaryCost(cs)) {
                    return new ShipmentCost(shipmentOption,
                            new MonetaryCostStrategy().getCost(shipmentOption).getCost().multiply(scale)
                                    .add(BigDecimal.valueOf(0.1296)));
                } else if (isOfTypeCarbonCost(cs)) {
                    return new ShipmentCost(shipmentOption,
                            new CarbonCostStrategy().getCost(shipmentOption).getCost().multiply(scale)
                                    .add(BigDecimal.valueOf(0.1296)));
                }
                break;
                
            }
            
        }
        
        return new ShipmentCost(shipmentOption, new WeightedCostStrategy().getCost(shipmentOption).getCost());
    }
    
    @Override
    public String toString() {
        return "WeightedCostStrategy{" +
                "map=" + map +
                '}';
    }
}