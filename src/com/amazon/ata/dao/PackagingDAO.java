package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    /**
     * A list of fulfillment centers with a packaging options they provide.
     */
    private final Set<FcPackagingOption> packagingOptionsSet;
    private final Map<FulfillmentCenter, Set<Packaging>> setMap;
    
    public void add(FcPackagingOption p) {
        this.packagingOptionsSet.add(p);
    }
    
    // just messing around
    public List<Set<Packaging>> removeEntryOfPredicate(FulfillmentCenter f) {
        return setMap.entrySet()
                .stream()
                .filter(e -> setMap.containsKey(f))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    /**
     * Instantiates a PackagingDAO object.
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
         this.packagingOptionsSet =  new HashSet<>(datastore.getFcPackagingOptions());
        
        this.setMap = new HashMap<>();
    
        packagingOptionsSet.forEach(k -> setMap
                .computeIfAbsent(k.getFulfillmentCenter(), v -> new HashSet<>())
                .add(k.getPackaging()));
    }
    
    /**
     * Gets key.
     *
     * @param <K>   the type parameter
     * @param <V>   the type parameter
     * @param map   the map
     * @param value the value
     * @return the key
     */
    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    

    /**
     * Returns the packaging options available for a given item at the specified fulfillment center. The API
     * used to call this method handles null inputs, so we don't have to.
     *
     * @param item the item to pack
     * @param fulfillmentCenter fulfillment center to fulfill the order from
     * @return the shipping options available for that item; this can never be empty, because if there is no
     * acceptable option an exception will be thrown
     * @throws UnknownFulfillmentCenterException if the fulfillmentCenter is not in the fcPackagingOptions list
     * @throws NoPackagingFitsItemException if the item doesn't fit in any packaging at the FC
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        
        List<ShipmentOption> result = new ArrayList<>();
    
        // need to get packing from the set
        for (Map.Entry<FulfillmentCenter, Set<Packaging>> entry : this.setMap.entrySet()) {
            if (setMap.containsKey(fulfillmentCenter)) {
            }
            
        }
        // Check all FcPackagingOptions for a suitable Packaging in the given FulfillmentCenter
        boolean fcFound = false;
        for (FcPackagingOption fcPackagingOption : packagingOptionsSet) {
            Packaging packaging = fcPackagingOption.getPackaging();
            String fcCode = fcPackagingOption.getFulfillmentCenter().getFcCode();

            if (fcCode.equals(fulfillmentCenter.getFcCode())) {
                fcFound = true;
                if (packaging.canFitItem(item)) {
                    result.add(ShipmentOption.builder()
                            .withItem(item)
                            .withPackaging(packaging)
                            .withFulfillmentCenter(fulfillmentCenter)
                            .build());
                }
            }
        }

        // Notify caller about unexpected results
        if (!fcFound) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }
    
    public Set<FcPackagingOption> getPackagingOptionsSet() {
        return packagingOptionsSet;
    }

    public Map<FulfillmentCenter, Set<Packaging>> getSetMap() {
        return setMap;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackagingDAO dao = (PackagingDAO) o;
        return getPackagingOptionsSet().equals(dao.getPackagingOptionsSet()) && getSetMap().equals(dao.getSetMap());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getPackagingOptionsSet(), getSetMap());
    }
    
    
    public static void main(String[] args) throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        PackagingDatastore p = new PackagingDatastore();
        PackagingDAO dao = new PackagingDAO(p);
        FulfillmentCenter f = new FulfillmentCenter("IND1");

        Item i = Item.builder()
                .withAsin("234234")
                .withDescription("pork")
                .withLength(BigDecimal.ONE)
                .withHeight(BigDecimal.ONE)
                .withWidth(BigDecimal.ONE)
                .build();

        dao.findShipmentOptions(i, f);
    
        FulfillmentCenter ind1 = new FulfillmentCenter("IND1");
        FulfillmentCenter abe2 = new FulfillmentCenter("ABE2");
    
        FcPackagingOption p1 = new FcPackagingOption(ind1, new Packaging(Material.CORRUGATE,
                BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    
        FcPackagingOption p2 = new FcPackagingOption(abe2, new Packaging(Material.CORRUGATE,
                BigDecimal.ONE,  BigDecimal.ONE,  BigDecimal.ONE));
    
        Map<FulfillmentCenter, Set<Packaging>> setMap;
        Map<FulfillmentCenter, Set<Packaging>> setMap2;
    
        Set<Packaging> singlePackingSet1 = new HashSet<>(Collections.singleton(p1.getPackaging()));
        Set<Packaging> singlePackingSet2 = new HashSet<>(Collections.singleton(p2.getPackaging()));
    
        System.out.println(singlePackingSet1);
        System.out.println();
        System.out.println(singlePackingSet2);

//
//        List<Set<Packaging>> lsp = dao.removeEntryOfPredicate(f);
//
//        lsp.forEach(System.out::print);

    }
}
