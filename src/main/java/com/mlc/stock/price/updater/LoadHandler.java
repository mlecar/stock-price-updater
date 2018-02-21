package com.mlc.stock.price.updater;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoadHandler {

    @Value("${stock.max.price.updates:100}")
    private static int MAX_PRICE_UPDATES;

    private ConcurrentLinkedQueue<PriceUpdate> priceUpdatesQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public LoadHandler() {
        priceUpdatesQueue = new ConcurrentLinkedQueue<>();
    }

    public void receive(PriceUpdate priceUpdate) {
        boolean removed = priceUpdatesQueue.removeIf(p -> p.getCompanyName().equals(priceUpdate.getCompanyName()));

        if (removed || !priceUpdatesQueue.contains(priceUpdate)) {
            priceUpdatesQueue.add(priceUpdate);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void sendMessages() {

        int limit = priceUpdatesQueue.size() < MAX_PRICE_UPDATES ? priceUpdatesQueue.size() : MAX_PRICE_UPDATES;

        LinkedList<PriceUpdate> list = new LinkedList<>();
        for (int i = 0; i < limit; i++) {
            list.add(priceUpdatesQueue.poll());
        }

        if (list.size() > 0) {
            jmsTemplate.convertAndSend("stockPrice", list);
        }
    }

}
