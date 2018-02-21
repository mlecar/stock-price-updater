package com.mlc.stock.price.updater;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoadHandler {

    @Value("${stock.max.price.updates}")
    private int MAX_PRICE_UPDATES;

    private ConcurrentLinkedQueue<PriceUpdate> priceUpdatesQueue;
    private ConcurrentHashMap<String, PriceUpdate> priceUpdates;

    @Autowired
    private JmsTemplate jmsTemplate;

    public LoadHandler() {
        priceUpdatesQueue = new ConcurrentLinkedQueue<>();
        priceUpdates = new ConcurrentHashMap<>();
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

        for (int i = 0; i < limit; i++) {
            PriceUpdate priceUpdate = priceUpdatesQueue.poll();
            priceUpdates.put(priceUpdate.getCompanyName(), priceUpdate);
        }

        if (priceUpdates.size() > 0) {
            jmsTemplate.convertAndSend("stockPrice", priceUpdates.values().parallelStream().collect(Collectors.toList()));
        }
    }

}
