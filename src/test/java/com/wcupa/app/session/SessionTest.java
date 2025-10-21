package com.wcupa.app.session;

import com.wcupa.app.domain.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SessionTest {
    
    @Test
    void testNoUser() {
        Session session = new Session();
        assertFalse(session.signedIn());
    }

    @Test
    void testSetUser() {
        Session session = new Session();
        session.setUser(new Customer("Brian", "Kominick", "kominickb@gmail.com", "123"));
    }

    
}
