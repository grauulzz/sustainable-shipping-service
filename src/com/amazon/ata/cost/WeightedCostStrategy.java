package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;


// --------------------blendedCost------------------------
// note: these have diff mass calculations but the monetary cost value is assumed to be same for both
// given monetary cost and carbon cost
// the "team" came up with an 80/20 (CORR, LAMINATED) split based of what they decided was the best blended cost

// ----monetaryCost----
// mc = (mass * cost/g) + labor

// ----carbonCost----
// cu = cu/g * mass


// ----CORRUGATE_B2K_BOX----
// mc = (mass * .005/g) + $0.43 ... (1,000g)
// mc = $5.43

// cu = 0.017cu/g * mass ... (1,000g)
// cu = 17

// ----LAMINATED_PLASTIC_P20_POLYBAG----
// mc = (27g * .25/g) + $0.43
// mc = $7.18

// cu = 0.012cu/g * 27g
// cu = 0.324

// cu decreased by %98 between B2K and P20

public class WeightedCostStrategy implements CostStrategy {
    
    private CostStrategy cs;
    private MonetaryCostStrategy mc;
    public WeightedCostStrategy() {}
    public WeightedCostStrategy(CostStrategy cs) {
        this.cs = cs;
    }
    
//
//    public WeightedCostStrategy(CostStrategy cs, MonetaryCostStrategy mc) {
//        this.cs = cs;
//        this.mc = mc;
//    }
//
//    private void addStrategyWithWeight(CostStrategy cs, BigDecimal percentage) {
//
//    }
//
//    public ShipmentCost getWeightedCost(Material m, ShipmentOption shipmentOpt) {
//        if (m == Material.CORRUGATE) {
//            cs.getCost(shipmentOpt);
//        }
//    }
//    public ShipmentCost getWeightedCostForAnyStrategy(ShipmentOption shipmentOption) {
//        return this.cs.getCost(shipmentOption);
//    }
    
    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        return this.cs.getCost(shipmentOption);
    }
}
