package com.amazon.ata.types;

import java.math.BigDecimal;

/**
 * The type Poly bag.
 */
public class PolyBag extends Packaging {
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume;
    
    /**
     * Instantiates a new Poly bag.
     *
     * @param material the material
     * @param length   the length
     * @param width    the width
     * @param height   the height
     */
    public PolyBag(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material);
        this.length = length;
        this.height = height;
        this.width = width;
    }
    
    private BigDecimal volume(BigDecimal l, BigDecimal w, BigDecimal h) {
        return l.multiply(w).multiply(h);
    }
    
    @Override
    public BigDecimal getMass() {
        return BigDecimal.valueOf(Math.ceil(Math.sqrt(this.getVolume().doubleValue()) * 0.6));
    }
    
    @Override
    public boolean canFitItem(Item item) {
        BigDecimal itemVolume = this.volume(item.getLength(), item.getWidth(), item.getHeight());
        int b = itemVolume.compareTo(this.getVolume());
        
        return b <= 0;
    }
    
    public BigDecimal getLength() {
        return length;
    }
    
    public void setLength(BigDecimal length) {
        this.length = length;
    }
    
    public BigDecimal getWidth() {
        return width;
    }
    
    public void setWidth(BigDecimal width) {
        this.width = width;
    }
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public void setHeight(BigDecimal height) {
        this.height = height;
    }
    
    public BigDecimal getVolume() {
        return volume;
    }
    
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
    
}
