package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Event_Friend;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */

@Api(name = "brings", version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        )
)
@ApiClass(resource = "eventFriend",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class Event_Friend_Endpoint {

    private static final Logger logger = Logger.getLogger(Event_Friend_Endpoint.class.getName());


    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param Friend_ID The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendDeleteByFriend", path="EventFriendDeleteByFriend")
    public void DeleteBy_Friend(@Named("Friend_ID")String Friend_ID) {
        try {
            MySQL_Util.delete("Events_Friends", new String[]{"Friend_ID"}, new String[]{Friend_ID}, null);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param Event_ID The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendDeleteByEvent", path="EventFriendDeleteByEvent")
    public void DeleteBy_Event(@Named("Event_ID")String Event_ID) {
        try {
            MySQL_Util.delete("Events_Friends",new String[]{"Event_ID"}, new String[]{Event_ID},null);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param Friend_ID The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendInsert", path = "EventFriendInsert")
    public void Insert(@Named("Event_ID")String Event_ID,@Named("Friend_ID")String Friend_ID) {
        try {
            MySQL_Util.insert("Events_Friends",new String[]{Event_ID,Friend_ID});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param friend The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendGetEvents", path="get_events")
    public ArrayList<Event_Friend> GetEvents(@Named("friend") String friend) {
        ArrayList<Event_Friend> eventFriendArrayList = new ArrayList<>();
        try {
            ResultSet rs = MySQL_Util.select(null,"Events_Friends",new String[]{"Friend_ID"},new String[]{friend},null);
            while(rs.next()){
                eventFriendArrayList.add(new Event_Friend(rs.getString("Event_ID"),rs.getString("Friend_ID")));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return eventFriendArrayList;
    }

    /**
     * This method gets the <code>Event_Friend</code> object associated with the specified <code>id</code>.
     *
     * @param event The id of the object to be returned.
     * @return The <code>Event_Friend</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "EventFriendGetFriends", path="get_friends")
    public ArrayList<Event_Friend> GetFriends(@Named("event") String event) {
        ArrayList<Event_Friend> eventFriendArrayList = new ArrayList<>();
        try {
            ResultSet rs = MySQL_Util.select(null,"Events_Friends",new String[]{"Event_ID"}, new String[]{event},null);
            while(rs.next()){
                eventFriendArrayList.add(new Event_Friend(rs.getString("Event_ID"),rs.getString("Friend_ID")));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return eventFriendArrayList;
    }
}