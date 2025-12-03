package com.wcupa.app.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.Product;

import java.util.ArrayList;

public class CartTest {

    @Test
    void sameProductsCombineInCart() {
        Cart cart = new Cart();
        Product p = new Product(1, "Clock", 10.0, "desc", new ArrayList<String>(), 5, "img", 5);

        cart.addProduct(p, 1);
        cart.addProduct(p, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    void totalPriceIsCorrect() {
        Cart cart = new Cart();
        Product p1 = new Product(1, "Clock", 10.0, "d", new ArrayList<String>(), 1, "img", 5);
        Product p2 = new Product(2, "Lamp", 20.0, "d", new ArrayList<String>(), 1, "img", 5);

        cart.addProduct(p1, 2); // $20
        cart.addProduct(p2, 1); // $20

        assertEquals(40.0, cart.getTotalCost());
    }
}
