package com.mlc.stock.price.updater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@ContextConfiguration()
public class LoadHandlerTest {

    @InjectMocks
    private LoadHandler loadHandler;

    @Mock
    private JmsTemplate jmsTemlate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void receiveSuccessful() {
        PriceUpdate priceUpdate = new PriceUpdate("Google", 160.76);
        loadHandler.receive(priceUpdate);

        @SuppressWarnings("unchecked")
        ConcurrentLinkedQueue<PriceUpdate> priceUpdatesQueue = (ConcurrentLinkedQueue<PriceUpdate>) ReflectionTestUtils.getField(loadHandler, "priceUpdatesQueue");
        PriceUpdate retrievedPrice = priceUpdatesQueue.poll();
        assertEquals(retrievedPrice.getCompanyName(), priceUpdate.getCompanyName());
        assertTrue(retrievedPrice.getPrice() == priceUpdate.getPrice());
    }

    @Test
    public void mostRecentPrice() {
        PriceUpdate priceUpdate = new PriceUpdate("Google", 160.76);
        loadHandler.receive(priceUpdate);

        PriceUpdate updatedPrice = new PriceUpdate("Google", 160.98);
        loadHandler.receive(updatedPrice);

        @SuppressWarnings("unchecked")
        ConcurrentLinkedQueue<PriceUpdate> priceUpdatesQueue = (ConcurrentLinkedQueue<PriceUpdate>) ReflectionTestUtils.getField(loadHandler, "priceUpdatesQueue");
        PriceUpdate retrievedPrice = priceUpdatesQueue.poll();
        assertEquals(retrievedPrice.getCompanyName(), updatedPrice.getCompanyName());
        assertTrue(retrievedPrice.getPrice() == updatedPrice.getPrice());
    }

    @Test
    public void sendMessages() {
        PriceUpdate priceUpdate = new PriceUpdate("Google", 160.76);
        loadHandler.receive(priceUpdate);

        ReflectionTestUtils.setField(loadHandler, "MAX_PRICE_UPDATES", 100);
        loadHandler.sendMessages();

        verify(jmsTemlate).convertAndSend(eq("stockPrice"), anyList());
    }

}
