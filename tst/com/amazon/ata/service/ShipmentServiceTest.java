package com.amazon.ata.service;

import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;


class ShipmentServiceTest {
    
    @InjectMocks
    private Item smallItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1))
            .withWidth(BigDecimal.valueOf(1))
            .withLength(BigDecimal.valueOf(1))
            .withAsin("abcde")
            .build();
    
    @InjectMocks
    private Item largeItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1000))
            .withWidth(BigDecimal.valueOf(1000))
            .withLength(BigDecimal.valueOf(1000))
            .withAsin("12345")
            .build();
    
    @InjectMocks
    private FulfillmentCenter existentFC = new FulfillmentCenter("ABE2");
    private FulfillmentCenter nonExistentFC = new FulfillmentCenter("NonExistentFC");
    
    @InjectMocks
    private ShipmentService shipmentService = new ShipmentService(new PackagingDAO(new PackagingDatastore()),
            new MonetaryCostStrategy());

    @Mock
    private PackagingDAO packagingDAO = new PackagingDAO(new PackagingDatastore());

    @Test
    public void nullFc_whenClientProvidesNullFc_throwsException()  {
        Assertions.assertThrows(RuntimeException.class, () -> Optional
                .ofNullable(shipmentService.findShipmentOption(smallItem, null))
                .orElseThrow(RuntimeException::new));
    }
    
    @Test
    public void nonExistentFc_whenClientProvidesUnknownFc_throwsUnknownFulfillmentCenterException()  {
        Assertions.assertThrows(UnknownFulfillmentCenterException.class, () -> Optional
                .ofNullable(shipmentService.findShipmentOption(smallItem, nonExistentFC))
                .orElseThrow(UnknownFulfillmentCenterException::new));
    }

    @Test
    public void nullItem_whenClientProvidesNullItem_throwsNoPackagingException() {
        Assertions.assertThrows(NoPackagingFitsItemException.class, () -> Optional
                .ofNullable(shipmentService.findShipmentOption(null, existentFC))
                .orElseThrow(NoPackagingFitsItemException::new));
    }


    @Test
    void findBestShipmentOption_existentFCAndItemCanFit_returnsShipmentOption() {
        // GIVEN & WHEN
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, existentFC);

        // THEN
        Assertions.assertNotNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_existentFCAndItemCannotFit_returnsShipmentOption() {
        // GIVEN & WHEN
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);

        // THEN
        Assertions.assertNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCanFit_returnsShipmentOption() {
        // GIVEN & WHEN
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, nonExistentFC);

        // THEN
        Assertions.assertNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCannotFit_returnsShipmentOption() {
        // GIVEN & WHEN
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, nonExistentFC);

        // THEN
        Assertions.assertNull(shipmentOption);
    }
}