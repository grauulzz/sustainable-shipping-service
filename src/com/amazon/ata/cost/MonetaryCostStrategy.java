package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


// ----monetaryCost----
// LABOR_COST: 0.43
// CORRUGATE_COST: .005/g
// monetaryCostCOR = (mass * CORRUGATE_COST) + LABOR_COST
// monetaryCostCOR = (mass * .005/g) + 0.43
// monetaryCostLP = (mass * LAMINATED_PLASTIC_COST) + LABOR_COST
// monetaryCostLP = (mass * .25/g) + 0.43

public class MonetaryCostStrategy implements CostStrategy {

    private static final BigDecimal LABOR_COST = BigDecimal.valueOf(0.43);
    private final Map<Material, BigDecimal> materialCostPerGram;
    ShipmentOption shipmentOption;
    
    
    public MonetaryCostStrategy() {
        materialCostPerGram = new HashMap<>();
        materialCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(.005));
        materialCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(.25));
    }

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        this.shipmentOption = shipmentOption;
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal materialCost = this.materialCostPerGram.get(packaging.getMaterial());

        BigDecimal cost = packaging.getMass().multiply(materialCost)
            .add(LABOR_COST);

        return new ShipmentCost(shipmentOption, cost);
    }
    
    @Override
    public String toString() {
        return "MonetaryCostStrategy{" +
                "materialCostPerGram=" + materialCostPerGram +
                ", shipmentOption=" + shipmentOption +
                '}';
    }
}
