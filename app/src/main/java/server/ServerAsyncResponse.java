package server;

import com.example.some_lie.backend.brings.model.Event;

import java.util.ArrayList;

/**
 * Created by pinhas on 11/10/2015.
 */
public interface ServerAsyncResponse {
     void processFinish(String output);
     void EventProcessFinish(Event output);
}
