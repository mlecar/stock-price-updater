package com.mlc.stock.price.updater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class PriceUpdateTest {

    @Test
    public void equal() {
        assertEquals(new PriceUpdate("Google", 160.01), new PriceUpdate("Google", 160.90));
    }

    @Test
    public void notEqual() {
        assertNotEquals(new PriceUpdate("Google", 160.01), new PriceUpdate("Facebook", 97.66));
    }

    @Test
    public void notEqualToNull() {
        assertNotEquals(new PriceUpdate("Google", 160.01), null);
    }

    @Test
    public void notEqualToDifferentObject() {
        assertNotEquals(new PriceUpdate("Google", 160.01), "any string");
    }

}
