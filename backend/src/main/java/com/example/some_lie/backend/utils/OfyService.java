package com.example.some_lie.backend.utils;

import com.example.some_lie.backend.models.Chat;
import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.models.Event_User;
import com.example.some_lie.backend.models.RegistrationRecord;
import com.example.some_lie.backend.models.Task;
import com.example.some_lie.backend.models.User;
import com.example.some_lie.backend.models.Vote_Date;
import com.example.some_lie.backend.models.Vote_Location;
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
        ObjectifyService.register(Event_User.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Task.class);
        ObjectifyService.register(Chat.class);
        ObjectifyService.register(Vote_Date.class);
        ObjectifyService.register(Vote_Location.class);
    }


    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
