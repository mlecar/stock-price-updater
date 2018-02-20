package exercise;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoadHandler {

    private static final int MAX_PRICE_UPDATES = 100;
    private final ConcurrentHashMap<String, PriceUpdate> priceUpdates;
    private ConcurrentLinkedQueue<PriceUpdate> priceUpdatesQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public LoadHandler() {
        priceUpdates = new ConcurrentHashMap<>();
        priceUpdatesQueue = new ConcurrentLinkedQueue<>();
    }

    public void receive(PriceUpdate priceUpdate) {

        boolean removed = priceUpdatesQueue.removeIf(p -> p.getCompanyName().equals(priceUpdate.getCompanyName()) && p.getPrice() < priceUpdate.getPrice());

        if (removed || !priceUpdatesQueue.contains(priceUpdate)) {
            priceUpdatesQueue.add(priceUpdate);
        }
        /*
         * PriceUpdate existingPrice =
         * priceUpdates.get(priceUpdate.getCompanyName());
         * 
         * if (existingPrice != null && existingPrice.getPrice() <
         * priceUpdate.getPrice()) {
         * priceUpdates.put(priceUpdate.getCompanyName(), priceUpdate); } else
         * if (existingPrice == null) {
         * priceUpdates.put(priceUpdate.getCompanyName(), priceUpdate); }
         */

    }

    public void sendMessages() {

        int limit = priceUpdatesQueue.size() < MAX_PRICE_UPDATES ? priceUpdatesQueue.size() : MAX_PRICE_UPDATES;

        LinkedList<PriceUpdate> list = new LinkedList<>();
        for (int i = 0; i < limit; i++) {
            list.add(priceUpdatesQueue.poll());
        }

        jmsTemplate.convertAndSend("stockPrice", list);
        /*
         * priceUpdatesQueue.stream().collect(Collectors.toCollection(LinkedList
         * ::new));
         * 
         * LinkedList<PriceUpdate> priceList =
         * priceUpdates.values().stream().collect(); if (priceList.size() >
         * MAX_PRICE_UPDATES) { jmsTemplate.convertAndSend("stockPrice",
         * priceList.subList(priceUpdates.size() - MAX_PRICE_UPDATES,
         * priceUpdates.size())); } else {
         * jmsTemplate.convertAndSend("stockPrice", priceList); }
         * priceUpdates.clear();
         */
    }

}
