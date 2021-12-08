package com.amazon.ata.types;


import java.math.BigDecimal;
import java.util.Objects;

public class Box extends Packaging {
    
    BigDecimal length; BigDecimal width; BigDecimal height;
    
    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     * @param length   - the length of the package
     * @param width    - the width of the package
     * @param height   - the height of the package
     */
    public Box(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material);
        this.length = length;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean canFitItem(Item item) {
        return length.compareTo(item.getLength()) > 0 &&
                width.compareTo(item.getWidth()) > 0 &&
                height.compareTo(item.getHeight()) > 0;
    }
    
    @Override
    public BigDecimal getMass() {
        BigDecimal two = BigDecimal.valueOf(2);
        
        // For simplicity, we ignore overlapping flaps
        BigDecimal endsArea = this.length.multiply(this.width).multiply(two);
        BigDecimal shortSidesArea = this.length.multiply(this.height).multiply(two);
        BigDecimal longSidesArea = this.width.multiply(this.height).multiply(two);
        
        return endsArea.add(shortSidesArea).add(longSidesArea);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return getLength().equals(box.getLength()) && getWidth().equals(box.getWidth()) && getHeight().equals(box.getHeight());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getLength(), getWidth(), getHeight());
    }
    
    @Override
    public String toString() {
        return "Box{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
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
}
