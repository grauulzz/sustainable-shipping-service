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
            createFcPackagingOption("IAD2", Material.LAMINATED_PLASTIC, "20", "20", "20"),
            createFcPackagingOption("IND2", Material.LAMINATED_PLASTIC, "10", "10", "10"),
            createFcPackagingOption("IAD2", Material.CORRUGATE, "20", "20", "20"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "40", "40", "40"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60"),
            createFcPackagingOption("PDX1", Material.CORRUGATE, "60", "60", "60")
//            createWithVolume("IAD2",Material.LAMINATED_PLASTIC,"5000"),
//            createWithVolume("YOW4",Material.LAMINATED_PLASTIC,"2000"),
//            createWithVolume("YOW4",Material.LAMINATED_PLASTIC,"5000"),
//            createWithVolume("YOW4",Material.LAMINATED_PLASTIC,"10000"),
//            createWithVolume("IND1",Material.LAMINATED_PLASTIC,"2000"),
//            createWithVolume("IND1",Material.LAMINATED_PLASTIC,"5000"),
//            createWithVolume("ABE2",Material.LAMINATED_PLASTIC,"2000"),
//            createWithVolume("ABE2",Material.LAMINATED_PLASTIC,"6000"),
//            createWithVolume("PDX1",Material.LAMINATED_PLASTIC,"5000"),
//            createWithVolume("PDX1",Material.LAMINATED_PLASTIC,"10000"),
//            createWithVolume("YOW4",Material.LAMINATED_PLASTIC,"5000")

    );

    private FcPackagingOption createWithVolume(String fcCode, Material m, String vol) {
        FulfillmentCenter ffc = new FulfillmentCenter(fcCode);
        Packaging p = new PolyBag(m, new BigDecimal(vol));
        return new FcPackagingOption(ffc, p);
    }
    /**
     * Create fulfillment center packaging option from provided parameters.
     */
    private FcPackagingOption createFcPackagingOption(String fcCode, Material material,
                                                      String length, String width, String height) {
        
        FulfillmentCenter fulfillmentCenter = new FulfillmentCenter(fcCode);
        
        Packaging p;
        
        if (material == Material.LAMINATED_PLASTIC) {
            
            p = new PolyBag(material, new BigDecimal(length), new BigDecimal(width), new BigDecimal(height));
            return new FcPackagingOption(fulfillmentCenter, p);
            
        } else if (material == Material.CORRUGATE) {
            p = new Box(material, new BigDecimal(length), new BigDecimal(width), new BigDecimal(height));
            return new FcPackagingOption(fulfillmentCenter, p);
        }
        
        return null;
    }
    
    public List<FcPackagingOption> getFcPackagingOptions() {
        return fcPackagingOptions;
    }
    
}


