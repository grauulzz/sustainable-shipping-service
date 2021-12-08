package com.amazon.ata.types;

import java.math.BigDecimal;

/**
 * Represents a packaging option.
 * <p>
 * This packaging supports standard boxes, having a length, width, and height.
 * Items can fit in the packaging so long as their dimensions are all smaller than
 * the packaging's dimensions.
 */
public class Packaging {

    
    /**
     * The material this packaging is made of.
     */
    private final Material material;
    
    public Packaging(Material material) {
        this.material = material;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Can fit item boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean canFitItem(Item item) {
        return false;
    }
    
    
    /**
     * Gets mass.
     *
     * @return the mass
     */
    public BigDecimal getMass() {
        return BigDecimal.ONE;
    }
    
    @Override
    public String toString() {
        return "Packaging{" +
                "material=" + material +
                '}';
    }
}
