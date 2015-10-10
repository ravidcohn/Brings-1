package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Event_Friend;
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
     * This method gets the <code>Event_Friend</code> object associated with the specified <code>id</code>.
     *
     * @param event The id of the object to be returned.
     * @return The <code>Event_Friend</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "Event_friend_getFriends", path="get_friends")
    public Event_Friend getEvent_Friend(@Named("event") String event) {
        // TODO: Implement this function
        logger.info("Calling getEvent_Friend method");
        return null;
    }

    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param event_Friend The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertEvent_Friend")
    public Event_Friend insertEvent_Friend(Event_Friend event_Friend) {
        // TODO: Implement this function
        logger.info("Calling insertEvent_Friend method");
        return event_Friend;
    }
    /**
     * This inserts a new <code>Event_Friend</code> object.
     *
     * @param friend The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "eventFriendGetEvents", path="get_events")
    public ArrayList<Event_Friend> get_events(@Named("friend") String friend) {
        String url = null;
        ArrayList<Event_Friend> eventFriendArrayList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url =
                    "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";

            Connection conn = DriverManager.getConnection(url);

            String query ="SELECT * FROM `Events_Friends` where Event_id ='"+friend+"';";
            ResultSet rs = conn.createStatement().executeQuery(query);
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