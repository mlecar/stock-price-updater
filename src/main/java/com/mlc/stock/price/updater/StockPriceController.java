package com.mlc.stock.price.updater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@Validated
@RequestMapping("/stock")
public class StockPriceController {

    @Autowired
    private LoadHandler loadHandler;

    @ResponseBody
    @PutMapping(value = { "/add/{companyName}/price/{price:.*}/", "/add/{companyName}/price/{price:.*}" }, produces = { "application/json" })
    public ResponseEntity<String> transactions(@Valid @PathVariable String companyName, @Valid @PathVariable Double price) {

        loadHandler.receive(new PriceUpdate(companyName, price));

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping(consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<String> transactions(@Valid @RequestBody String body) {

        PriceUpdate[] prices = new Gson().fromJson(body, PriceUpdate[].class);

        for (PriceUpdate price : prices) {
            loadHandler.receive(price);
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping(value = { "/generateUpdates" }, produces = { "application/json" })
    public ResponseEntity<String> generateUpdates() {

        for (int i = 1; i < 100; i++) {
            loadHandler.receive(new PriceUpdate("Apple", 97.85));
            loadHandler.receive(new PriceUpdate("Google", 160.71));
            loadHandler.receive(new PriceUpdate("Facebook", 91.66));
            loadHandler.receive(new PriceUpdate("Google", 160.73));
            loadHandler.receive(new PriceUpdate("Facebook", 91.71));
            loadHandler.receive(new PriceUpdate("Google", 160.76));
            loadHandler.receive(new PriceUpdate("Apple", 97.99));
            loadHandler.receive(new PriceUpdate("Google", 160.71));
            loadHandler.receive(new PriceUpdate("Facebook", 91.63));
            loadHandler.receive(new PriceUpdate("Apple", 97.01));
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    /*
     * @ResponseBody
     * 
     * @PutMapping(value = { "/loadFile" }, consumes = { "application/json" },
     * produces = { "application/json" }) public ResponseEntity<String>
     * loadFile() throws FileNotFoundException, IOException {
     * 
     * Resource resource =
     * resourceLoader.getResource("classpath:source.stock.price.json");
     * JsonReader reader = new JsonReader(new FileReader(resource.getFile()));
     * 
     * PriceUpdate[] additional = new Gson().fromJson(reader,
     * PriceUpdate[].class); for (int i = 0; i < additional.length; i++) {
     * loadHandler.receive(additional[i]); }
     * 
     * return new ResponseEntity<String>(HttpStatus.CREATED); }
     */

}
