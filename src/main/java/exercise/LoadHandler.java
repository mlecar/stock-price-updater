package exercise;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
