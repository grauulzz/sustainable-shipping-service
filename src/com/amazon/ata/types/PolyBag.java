package com.amazon.ata.types;

import java.math.BigDecimal;

public class PolyBag extends Packaging {
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume;
    
    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     * @param length   - the length of the package
     * @param width    - the width of the package
     * @param height   - the height of the package
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
    
    @Override
    public String toString() {
        return "PolyBag{" +
                "length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", volume=" + volume +
                "} " + super.toString();
    }
    
    
    public static void main(String[] args) {
        PolyBag p1 = new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        
        BigDecimal vol = p1.volume(p1.length, p1.width, p1.height);
        p1.setVolume(vol);
    
        Item i = Item.builder().withWidth(BigDecimal.ONE).withHeight(BigDecimal.ONE).withLength(BigDecimal.ONE).build();
    
        System.out.println(i);
        System.out.println(p1.canFitItem(i));
        System.out.println(p1.getMass());
        
    }
}
