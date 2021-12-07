package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The type Poly bag.
 */
public class PolyBag extends Packaging {

    private BigDecimal volume;
    
    public PolyBag(Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }
    
    
    @Override
    public BigDecimal getMass() {
        return BigDecimal.valueOf(Math.ceil(Math.sqrt(this.volume.doubleValue()) * 0.6));
    }
    
    @Override
    public boolean canFitItem(Item item) {
        BigDecimal itemVolume = item.getLength().multiply(item.getWidth()).multiply(item.getHeight());
        int b = itemVolume.compareTo(volume);
        
        return b <= 0;
    }
    
    public BigDecimal getVolume() {
        return volume;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolyBag polyBag = (PolyBag) o;
        return getVolume().equals(polyBag.getVolume());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getVolume().hashCode());
    }
}
