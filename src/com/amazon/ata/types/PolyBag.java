package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The type Poly bag.
 */
public class PolyBag extends Packaging {
    
    private BigDecimal volume; private BigDecimal length;
    private BigDecimal width; private BigDecimal height;
    
    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     * @param length   the length
     * @param width    the width
     * @param height   the height
     */
    public PolyBag(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material);
        this.length = length;
        this.width = width;
        this.height = height;
        this.volume = length.multiply(width).multiply(height);
    }
    
    public PolyBag(Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }
    
    @Override
    public BigDecimal getMass() {
        return BigDecimal.valueOf(Math.ceil(Math.sqrt(this.volume.doubleValue()) *
                new BigDecimal("0.6").doubleValue()
        ));
    }
    
    @Override
    public boolean canFitItem(Item item) {
        BigDecimal itemVolume = item.getLength().multiply(item.getWidth()).multiply(item.getHeight());
        int b = itemVolume.compareTo(this.volume);
        
        return b <= 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolyBag polyBag = (PolyBag) o;
        return getLength().equals(polyBag.getLength()) && getWidth().equals(polyBag.getWidth()) && getHeight().equals(polyBag.getHeight()) && getVolume().equals(polyBag.getVolume());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getLength(), getWidth(), getHeight(), getVolume());
    }
    
    @Override
    public String toString() {
        return "PolyBag{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", volume=" + volume +
                "} " + super.toString();
    }
    
    public BigDecimal getLength() {
        return length;
    }
    
    public BigDecimal getWidth() {
        return width;
    }
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public BigDecimal getVolume() {
        return volume;
    }
}
