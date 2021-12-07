package com.amazon.ata.datastore;

import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Stores all configured packaging pairs for all fulfillment centers.
 */
public class PackagingDatastore {
    
    /**
     * The stored pairs of fulfillment centers to the packaging options they support.
     */
    private final List<FcPackagingOption> fcPackagingOptions = Arrays.asList(
            createFcPackagingOption("IND1", Material.CORRUGATE, "10", "10", "10"),
            createFcPackagingOption("ABE2", Material.CORRUGATE, "20", "20", "20"),
            createFcPackagingOption("ABE2", Material.CORRUGATE, "40", "40", "40"),
            createFcPackagingOption("YOW4", Material.CORRUGATE, "10", "10", "10"),
            createFcPackagingOption("YOW4", Material.CORRUGATE, "20", "20", "20"),
            createFcPackagingOption("YOW4", Material.CORRUGATE, "60", "60", "60"),
            createFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20"),
            createFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "40", "40", "40"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60")
//            createOptionsUsingVolume("IAD2",  Material.LAMINATED_PLASTIC, "2000"),
//            createOptionsUsingVolume("IAD2",  Material.LAMINATED_PLASTIC, "10000")
    );
    
    private FcPackagingOption createOptionsUsingVolume(String fcCode, Material m, String vol) {
        FulfillmentCenter fc = new FulfillmentCenter(fcCode);
        Packaging p = new PolyBag(m, new BigDecimal(vol));
        return new FcPackagingOption(fc, p);
    }
    
    /**
     * Create fulfillment center packaging option from provided parameters.
     */
    private FcPackagingOption createFcPackagingOption(String fcCode, Material material,
                                                      String length, String width, String height) {
        
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        Packaging packaging = new Packaging(material, new BigDecimal(length),
                new BigDecimal(width), new BigDecimal(height));
        
        if (packaging.getMaterial() == Material.LAMINATED_PLASTIC) {
            
            packaging = new PolyBag(material, packaging.getVolume());
            return new FcPackagingOption(fulfillmentCenter, packaging);
        }
        
        packaging = new Box(material, new BigDecimal(length), new BigDecimal(width), new BigDecimal(height));
        return new FcPackagingOption(fulfillmentCenter, packaging);
    
    }
    
    public List<FcPackagingOption> getFcPackagingOptions() {
        return fcPackagingOptions;
    }
    
}


