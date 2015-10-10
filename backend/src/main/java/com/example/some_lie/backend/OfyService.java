package com.example.some_lie.backend;

import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.models.Event_Friend;
import com.example.some_lie.backend.models.RegistrationRecord;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(Event_Friend.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
