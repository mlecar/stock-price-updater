package exercise;

import java.util.List;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Please do not change the Consumer.
 */
@Component
public class Consumer {

    @JmsListener(destination = "stockPrice")
    public void send(List<PriceUpdate> priceUpdates) {
        priceUpdates.forEach(System.out::println);
    }
}
