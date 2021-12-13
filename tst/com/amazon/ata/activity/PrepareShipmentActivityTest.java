package com.amazon.ata.activity;

import com.amazon.ata.service.ShipmentService;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * The type Prepare shipment activity test.
 */
public class PrepareShipmentActivityTest {
    @Mock
    private PrepareShipmentRequest request = PrepareShipmentRequest.builder()
        .withFcCode("fcCode")
        .withItemAsin("itemAsin")
        .withItemDescription("description")
        .withItemLength("10.0")
        .withItemWidth("10.0")
        .withItemHeight("10.0")
        .build();

    @Mock
    private ShipmentService shipmentService;
    
    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        initMocks(this);
    }
    
    /**
     * Handle request no available shipment option returns null.
     *
     * @throws Exception the exception
     */
    @Test
    public void handleRequestNoAvailableShipmentOptionReturnsNull() throws Exception {
        // GIVEN
        PrepareShipmentActivity activity = new PrepareShipmentActivity(shipmentService);
        when(shipmentService.findShipmentOption(any(Item.class), any(FulfillmentCenter.class))).thenReturn(null);

        // WHEN
        String response = activity.handleRequest(request, null);

        // THEN
        assertNull(response);
    }


    /**
     * Handle request available shipment option returns non empty response.
     */
    @Test
    public void handleRequestAvailableShipmentOptionReturnsNonEmptyResponse() {
        // GIVEN
        // PrepareShipmentActivity activity = new PrepareShipmentActivity(shipmentService, dataConverter);
        PrepareShipmentActivity activity = new PrepareShipmentActivity(shipmentService);
        when(shipmentService.findShipmentOption(any(Item.class), any(FulfillmentCenter.class)))
            .thenReturn(ShipmentOption.builder().build());

        // WHEN
        // PrepareShipmentResponse response = activity.handleRequest(request);
        String response = activity.handleRequest(request, null);


        // THEN
        //assertNotNull(response.getAttributes());
        assertNotNull(response);
    }
}
