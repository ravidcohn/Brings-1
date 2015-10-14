package utils;

import android.content.Context;

import com.example.some_lie.backend.brings.model.Event;

import server.Event_AsyncTask_get;
import server.ServerAsyncResponse;

/**
 * Created by Ravid on 14/10/2015.
 */
public class EventHelper implements ServerAsyncResponse{
    private static String Action;
    public static void get_Event(Context context,String action,String message){
        Action = action;
        new Event_AsyncTask_get((ServerAsyncResponse) context,context).execute(message);
    }

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void EventProcessFinish(Event output) {
        String name = output.getName();
        String id = output.getId();
        String location = output.getLocation();
        String start_date = output.getStartDate();
        String end_date = output.getEndDate();
        String description = output.getDescription();
        String image_path = output.getImageUrl();
        String update_time = output.getUpdateTime();
        if(Action.equals(Constants.New_Event)) {
            sqlHelper.insert(Constants.Table_Events, new String[]{id, name, location, start_date, end_date, description, image_path, update_time});
        }

    }
}
