package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;


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
    
    private CostStrategy cs;
    private BigDecimal percentage;
    
    public WeightedCostStrategy() {}
    public WeightedCostStrategy(CostStrategy cs ) {
        this.cs = cs;
    }
    public WeightedCostStrategy(CostStrategy cs, BigDecimal percentage) {
        this.cs = cs;
        this.percentage = percentage;
    }
    
    public ShipmentCost getCostStrategy(ShipmentOption shipmentOption) {
        return cs.getCost(shipmentOption);
    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        ShipmentCost sc = this.cs.getCost(shipmentOption);
        
        BigDecimal weighted = sc.getCost().multiply(this.percentage);
        
        return new ShipmentCost(shipmentOption, weighted);
    }
}
