package exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer extends Thread {

    @Autowired
    private LoadHandler loadHandler;

    @Override
    public void run() {
        generateUpdates();
    }

    public void generateUpdates() {
        for (int i = 1; i < 100; i++) {
            loadHandler.receive(new PriceUpdate("Apple", 97.85));
            loadHandler.receive(new PriceUpdate("Google", 160.71));
            loadHandler.receive(new PriceUpdate("Facebook", 91.66));
            loadHandler.receive(new PriceUpdate("Google", 160.73));
            loadHandler.receive(new PriceUpdate("Facebook", 91.71));
            loadHandler.receive(new PriceUpdate("Google", 160.76));
            loadHandler.receive(new PriceUpdate("Apple", 97.85));
            loadHandler.receive(new PriceUpdate("Google", 160.71));
            loadHandler.receive(new PriceUpdate("Facebook", 91.63));
        }

        loadHandler.sendMessages();
    }

}
