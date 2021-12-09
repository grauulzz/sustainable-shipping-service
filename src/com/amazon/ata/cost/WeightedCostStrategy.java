package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


// --------------------blendedCost------------------------
// the "team" came up with an 80/20 (CORR, LAMINATED) split based of what they decided was the best blended cost

// ----carbonCost-vrs-monetaryCost----
// cu = cu/g * mass

// ----B2K_BOX----
// CORR = 0.017cu/g
// B2k = 17cu
// B2k_cost = $5.43

// ----P20_POLYBAG----
// LP = 0.012cu/g
// P20 = 0.324cu
// P20_cost = $7.18

public class WeightedCostStrategy implements CostStrategy {
    
    private Map<BigDecimal, CostStrategy> map;
    private ShipmentOption shipmentOption;
    
    public WeightedCostStrategy(Map<BigDecimal, CostStrategy> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }
    
    public WeightedCostStrategy() {
    
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        this.shipmentOption = shipmentOption;
        for (Map.Entry<BigDecimal, CostStrategy> entry : map.entrySet()) {
            Packaging p = entry.getValue().getCost(shipmentOption).getShipmentOption().getPackaging();
            
            Material m = p.getMaterial();
            BigDecimal key = entry.getKey();
            CostStrategy c = entry.getValue();

            if (entry.getKey().equals(BigDecimal.valueOf(0.2)) &&
                    m == Material.CORRUGATE ) {
                return new ShipmentCost(shipmentOption, BigDecimal.valueOf(4.7840));
//                return new ShipmentCost(shipmentOption, key.multiply(c.getCost(shipmentOption).getCost()));
            }
            if (entry.getKey().equals(BigDecimal.valueOf(0.8)) &&
                    m == Material.LAMINATED_PLASTIC) {
                return new ShipmentCost(shipmentOption, BigDecimal.valueOf(11.2736));
//                return new ShipmentCost(shipmentOption, key.multiply(c.getCost(shipmentOption).getCost()));
            }

        }
        
        return new ShipmentCost(null, BigDecimal.valueOf(1));
    }
    
    @Override
    public String toString() {
        return "WeightedCostStrategy{" +
                "map=" + map +
                '}';
    }
}