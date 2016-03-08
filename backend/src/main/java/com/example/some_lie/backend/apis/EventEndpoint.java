package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.models.images_path;
import com.example.some_lie.backend.utils.Constans.Constants;
import com.example.some_lie.backend.utils.Constans.Table_Events;
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
@ApiClass(resource = "event",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class EventEndpoint {

    private static final Logger logger = Logger.getLogger(EventEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;


    /**
     * Returns the {@link Event} with the corresponding ID.
     *
     * @param Event_ID the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Event} with the provided ID.
     */
    @ApiMethod(name = "EventGet", path = "EventGet")
    public Event Get(@Named("Event_ID") String Event_ID) {
        Event event = new Event();
        try {
            ResultSet rs = MySQL_Util.select(null, Table_Events.Table_Name, new String[]{Table_Events.Event_ID}, new String[]{Event_ID}, new int[]{1});
            if (rs.next()) {
                event.setId(rs.getString(Table_Events.Event_ID));
                event.setName(rs.getString(Table_Events.Name));
                event.setLocation(rs.getString(Table_Events.Location));
                event.setVote_location(rs.getString(Table_Events.Vote_Location));
                event.setStart_date(rs.getString(Table_Events.Start_Date));
                event.setEnd_date(rs.getString(Table_Events.End_Date));
                event.setAll_day_time(rs.getString(Table_Events.All_Day_Time));
                event.setStart_time(rs.getString(Table_Events.Start_Time));
                event.setEnd_time(rs.getString(Table_Events.End_Time));
                event.setVote_time(rs.getString(Table_Events.Vote_Time));
                event.setDescription(rs.getString(Table_Events.Description));
                event.setImage_url(rs.getString(Table_Events.Image_Path));
                event.setUpdate_time(rs.getString(Table_Events.Update_Time));
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
                String date = day + "/" + month + "/" + year;
                String time = hour + ":" + minute + ":" + second + ":" + millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(), date, time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return event;
    }

    /**
     * Inserts a new {@code Event}.
     */
    @ApiMethod(name = "EventInsert", path = "EventInsert")
    public images_path Insert(@Named("A_Event_ID") String Event_ID, @Named("B_Name") String Name, @Named("C_Location") String Location, @Named("D_Vote_Location") String Vote_Location,
                              @Named("E_Start_Date") String Start_Date, @Named("F_End_Date") String End_Date, @Named("G_All_Day_Time") String All_Day_Time,
                              @Named("H_Start_Time") String Start_Time, @Named("I_End_Time") String End_Time, @Named("J_Vote_Time") String Vote_Time,
                              @Named("K_Description") String Description, @Named("L_Image_Path") String Image_Path, @Named("M_Update_Time") String Update_Time) {
        try {
            String uploadURL = Event_ID + "_pic0";
            MySQL_Util.insert(Table_Events.Table_Name, new String[]{Event_ID, Name, Location, Vote_Location, Start_Date, End_Date,
                    All_Day_Time, Start_Time, End_Time, Vote_Time, Description, uploadURL, Update_Time});
            images_path im_path = new images_path();
            //im_path.setPath(uploadURL);
            return im_path;

        } catch (Exception e) {
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
                String date = day + "/" + month + "/" + year;
                String time = hour + ":" + minute + ":" + second + ":" + millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(), date, time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**

     */
    @ApiMethod(name = "EventUpdate", path = "EventUpdate")
    public void Update(@Named("A_Event_ID") String Event_ID, @Named("B_Name") String Name, @Named("C_Location") String Location, @Named("D_Vote_Location") String Vote_Location,
                       @Named("E_Start_Date") String Start_Date, @Named("F_End_Date") String End_Date, @Named("G_All_Day_Time") String All_Day_Time,
                       @Named("H_Start_Time") String Start_Time, @Named("I_End_Time") String End_Time, @Named("J_Vote_Time") String Vote_Time,
                       @Named("K_Description") String Description, @Named("L_Image_Path") String Image_Path, @Named("M_Update_Time") String Update_Time) {
        try {
            MySQL_Util.update(Table_Events.Table_Name, Table_Events.getAllFields_Except_Event_ID(),
                    new String[]{Name, Location, Vote_Location, Start_Date, End_Date, All_Day_Time, Start_Time, End_Time, Vote_Time,
                            Description, Image_Path, Update_Time}, new String[]{Table_Events.Event_ID}, new String[]{Event_ID});

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
                String date = day + "/" + month + "/" + year;
                String time = hour + ":" + minute + ":" + second + ":" + millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(), date, time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * Deletes the specified {@code Event}.
     *
     * @param Event_ID the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(name = "EventDelete", path = "EventDelete")
    public void Delete(@Named("Event_ID") String Event_ID) {
        try {
            MySQL_Util.delete(Table_Events.Table_Name, new String[]{Table_Events.Event_ID}, new String[]{Event_ID}, new int[]{1});
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
                String date = day + "/" + month + "/" + year;
                String time = hour + ":" + minute + ":" + second + ":" + millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(), date, time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * Deletes the specified {@code Event}.
     *
     * @param Event_ID the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(name = "EventUpdateField", path = "EventUpdateField")
    public void Update_Filed(@Named("A_Event_ID") String Event_ID, @Named("B_Filed") String Filed, @Named("C_Update") String Update) {
        try {
            MySQL_Util.update(Table_Events.Table_Name, new String[]{Filed},
                    new String[]{Update}, new String[]{Table_Events.Event_ID}, new String[]{Event_ID});
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
                String date = day + "/" + month + "/" + year;
                String time = hour + ":" + minute + ":" + second + ":" + millis;
                MySQL_Util.insert("Logs", new String[]{sw.toString(), date, time});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


}