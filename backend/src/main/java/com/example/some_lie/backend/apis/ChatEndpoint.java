package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Chat;
import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */

@Api(name = "brings", version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        )
)
@ApiClass(resource = "chat",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class ChatEndpoint {

    private static final Logger logger = Logger.getLogger(EventEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;


        /**
         * Returns the {@link Event} with the corresponding ID.
         *
         * @param Chat_ID the ID of the entity to be retrieved
         * @return the entity with the corresponding ID
         * @throws NotFoundException if there is no {@code Event} with the provided ID.
         */
        @ApiMethod( name = "ChatGet",path = "ChatGet")
        public Chat Get(@Named("AChat_ID") String Chat_ID, @Named("BMessage_ID") String Message_ID) {
                Chat chat = new Chat();
                try {
                        ResultSet rs = MySQL_Util.select(null, Chat_ID, new String[]{"Message_ID"}, new String[]{Message_ID}, new int[]{1});
                        if(rs.next()) {
                                chat.setMessage_ID(rs.getString("Message_ID"));
                                chat.setFriend_ID_Sender(rs.getString("Friend_ID_Sender"));
                                chat.setMessage(rs.getString("Message"));
                                chat.setDate(rs.getString("Date"));
                                chat.setTime(rs.getString("Time"));
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
                return chat;
        }

        @ApiMethod( name = "ChatGetAll",path = "ChatGetAll")
        public ArrayList<Chat> GetAll(@Named("Chat_ID") String Chat_ID) {
                ArrayList<Chat> chatArrayList = new ArrayList<>();
                try {
                        ResultSet rs = MySQL_Util.select(null,Chat_ID,null,null,null);
                        while(rs.next()){
                                chatArrayList.add(new Chat(rs.getString("Message_ID"),rs.getString("Friend_ID_Sender"),
                                        rs.getString("Message"),rs.getString("Date"),rs.getString("Time")));
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
                return chatArrayList;
        }

        /**
         * Inserts a new {@code Event}.
         */
        @ApiMethod(name = "ChatInsert",path = "ChatInsert")
        public void Insert(@Named("AChat_ID")String Chat_ID, @Named("BMessage_ID")String Message_ID, @Named("CFriend_ID_Sender")String Friend_ID_Sender,
                           @Named("DMessage")String Message, @Named("EDate")String Date,  @Named("ETime")String Time) {
                try {
                        MySQL_Util.insert(Chat_ID, new String[]{Message_ID, Friend_ID_Sender, Message, Date, Time});

                }catch(Exception e){
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();
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
         * Deletes the specified {@code Event}.
         *
         * @param Chat_ID the ID of the entity to delete
         * @throws NotFoundException if the {@code id} does not correspond to an existing
         *                           {@code Event}
         */
        @ApiMethod(name = "ChatDelete",path = "ChatDelete")
        public void Delete(@Named("AChat_ID") String Chat_ID, @Named("BMessage_ID") String Message_ID){
                try {
                        MySQL_Util.delete(Chat_ID, new String[]{"Message_ID"}, new String[]{Message_ID}, new int[]{1});
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


        @ApiMethod(name = "ChatCreateByEvent",path = "ChatCreateByEvent")
        public void CreateByEvent(@Named("Chat_ID") String Chat_ID){
                try {
                        MySQL_Util.createTable(Chat_ID, new String[]{"Message_ID","Friend_ID_Sender","Message","Date","Time"},
                                new String[]{"VARCHAR(30) NOT NULL ","VARCHAR(30) NOT NULL","LONGTEXT NOT NULL","VARCHAR(30) NOT NULL","VARCHAR(30) NOT NULL"});
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

        @ApiMethod(name = "ChatDeleteByEvent",path = "ChatDeleteByEvent")
        public void DeleteByEvent(@Named("Chat_ID") String Chat_ID){
                try {
                        MySQL_Util.deleteTable(Chat_ID);
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