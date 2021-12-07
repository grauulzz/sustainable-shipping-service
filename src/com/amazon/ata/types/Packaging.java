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
    private Material material;

    /**
     * This packaging's length.
     */
    private BigDecimal length;

    /**
     * This packaging's smallest dimension.
     */
    private BigDecimal width;

    /**
     * This packaging's largest dimension.
     */
    private BigDecimal height;
    
    private BigDecimal volume;
    
    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     * @param length   the length
     * @param width    the width
     * @param height   the height
     */
    public Packaging(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        this.material = material;
        this.length = length;
        this.width = width;
        this.height = height;
//        this.volume = length.multiply(width).multiply(height);
    }
    
    /**
     * Instantiates a new Packaging.
     *
     * @param material the material
     */
    public Packaging(Material material) {
        this.material = material;
    }
    
    public Packaging(Material material, BigDecimal volume) {
        this.material = material;
        this.volume = volume;
    }
    
    /**
     * Gets material.
     *
     * @return the material
     */
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
    
    
    public BigDecimal getVolume() {
        return volume;
    }
    
    @Override
    public String toString() {
        return "Packaging{" +
                "material=" + material +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
