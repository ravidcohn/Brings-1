package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.utils.Constans.Constants;
import com.example.some_lie.backend.models.Event_Friend;
import com.example.some_lie.backend.utils.Constans.Table_Events_Friends;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
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
            MySQL_Util.delete(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Friend_ID}, new String[]{Friend_ID}, null);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
            MySQL_Util.delete(Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Event_ID}, new String[]{Event_ID},null);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     *
     * @param Event_ID
     * @param Friend_ID
     */
    @ApiMethod(name = "EventFriendDelete", path="EventFriendDelete")
    public void Delete(@Named("Event_ID")String Event_ID, @Named("Friend_ID")String Friend_ID) {
        try {
            MySQL_Util.delete(Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Event_ID, Table_Events_Friends.Friend_ID}, new String[]{Event_ID, Friend_ID},null);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param Friend_ID The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendInsert", path = "EventFriendInsert")
    public void Insert(@Named("AEvent_ID")String Event_ID,@Named("BFriend_ID")String Friend_ID,@Named("CAttending")String attending, @Named("DPermission")String permission) {
        try {
            MySQL_Util.insert(Table_Events_Friends.Table_Name, new String[]{Event_ID, Friend_ID, attending, permission});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param Friend_ID The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "EventFriendGetEvents", path="get_events")
    public ArrayList<Event_Friend> GetEvents(@Named("Friend_ID") String Friend_ID) {
        ArrayList<Event_Friend> eventFriendArrayList = new ArrayList<>();
        try {
            ResultSet rs = MySQL_Util.select(null,Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Friend_ID},new String[]{Friend_ID},null);
            while(rs.next()){
                eventFriendArrayList.add(new Event_Friend(rs.getString(Table_Events_Friends.Event_ID),rs.getString(Table_Events_Friends.Friend_ID),
                        rs.getString(Table_Events_Friends.Attending),rs.getString(Table_Events_Friends.Permission)));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return eventFriendArrayList;
    }

    /**
     * This method gets the <code>Event_Friend</code> object associated with the specified <code>id</code>.
     *
     * @param Event_ID The id of the object to be returned.
     * @return The <code>Event_Friend</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "EventFriendGetFriends", path="get_friends")
    public ArrayList<Event_Friend> GetFriends(@Named("Event_ID") String Event_ID) {
        ArrayList<Event_Friend> eventFriendArrayList = new ArrayList<>();
        try {
            ResultSet rs = MySQL_Util.select(null,Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Event_ID}, new String[]{Event_ID},null);
            while(rs.next()){
                eventFriendArrayList.add(new Event_Friend(rs.getString(Table_Events_Friends.Event_ID),rs.getString(Table_Events_Friends.Friend_ID),
                        rs.getString(Table_Events_Friends.Attending),rs.getString(Table_Events_Friends.Permission)));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return eventFriendArrayList;
    }

    /**
     * This method gets the <code>Event_Friend</code> object associated with the specified <code>id</code>.
     *
     * @param Friend_ID The id of the object to be returned.
     * @return The <code>Event_Friend</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "EventFriendGet", path="EventFriendGet")
    public Event_Friend Get(@Named("AEvent_ID") String Event_ID, @Named("BFriend_ID") String Friend_ID) {
            Event_Friend event_friend = new Event_Friend();
        try {
            ResultSet rs = MySQL_Util.select(null,Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Event_ID,Table_Events_Friends.Friend_ID},
                    new String[]{Event_ID,Friend_ID},null);
            event_friend.setEvent_name(rs.getString(Table_Events_Friends.Event_ID));
            event_friend.setFriend_name(rs.getString(Table_Events_Friends.Friend_ID));
            event_friend.setAttending(rs.getString(Table_Events_Friends.Attending));
            event_friend.setPermission(rs.getString(Table_Events_Friends.Permission));
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return event_friend;
    }

    @ApiMethod(name = "EventFriendUpdateAttending", path="EventFriendUpdateAttending")
    public void UpdateAttending(@Named("AEvent_ID")String Event_ID,@Named("BFriend_ID")String Friend_ID,@Named("CAttending")String Attending) {
        try {
            MySQL_Util.update(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Attending},
                    new String[]{Attending},
                    new String[]{Table_Events_Friends.Event_ID, Table_Events_Friends.Event_ID}, new String[]{Event_ID, Friend_ID});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @ApiMethod(name = "EventFriendUpdatePermission", path="EventFriendUpdatePermission")
    public void UpdatePermission(@Named("AEvent_ID")String Event_ID,@Named("BFriend_ID")String Friend_ID,@Named("CPermission")String Permission) {
        try {
            MySQL_Util.update(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Permission},
                    new String[]{Permission},
                    new String[]{Table_Events_Friends.Event_ID,Table_Events_Friends.Event_ID}, new String[]{Event_ID,Friend_ID});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LocalDateTime now = LocalDateTime.now();
            try {
                int year = now.getYear();
                int month = now.getMonthOfYear();
                int day = now.getDayOfMonth();
                int hour = now.getHourOfDay();
                int minute = now.getMinuteOfHour();
                int second = now.getSecondOfMinute();
                int millis = now.getMillisOfSecond();
                String date = day+"/"+month+"/"+year;
                String time = hour+":"+minute+":"+second+":"+millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(),date,time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}