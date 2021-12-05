package com.amazon.ata.types;

import com.google.j2objc.annotations.ObjectiveCName;

import java.math.BigDecimal;
import java.util.Objects;

public class Box extends Packaging {
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    
    
    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     * @param length   - the length of the package
     * @param width    - the width of the package
     * @param height   - the height of the package
     */
    public Box(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material, length, width, height);
        this.length = length;
        this.width = width;
        this.height = height;
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
    

    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Box box = (Box) o;
        return getLength().equals(box.getLength()) && getWidth().equals(box.getWidth()) && getHeight().equals(box.getHeight());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLength().hashCode(), getHeight().hashCode(), getWidth().hashCode());
    }
    
    @Override
    public String toString() {
        return "Box{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
                "} " + super.toString();
    }
}
