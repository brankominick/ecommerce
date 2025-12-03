package com.wcupa.app.session;

import java.util.Set;

import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.Permission;
import com.wcupa.app.domain.core.User;


public class Session {

    private User user;
    private Cart cart;

    public Session() {

    }

    public User getUser() { 
        return user; 
    }
    public Cart getCart() { 
        return cart; 
    }

    public void clearCart() {
        cart.clearCart();
    }

    public void signIn(User user) {
        this.user = user;
        this.cart = new Cart();
    }

    public void signOut() {
        this.user = null;
        this.cart = null;
    }

    public boolean signedIn() {
        return (user != null);
    }

    public Set<Permission> getPermissions() {
        return user.getPermissions();
    }

}
