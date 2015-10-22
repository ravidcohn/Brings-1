package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.models.Task;
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
@ApiClass(resource = "task",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class TaskEndpoint {

    private static final Logger logger = Logger.getLogger(TaskEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

        /**
         * Returns the {@link Event} with the corresponding ID.
         *
         * @param Event_ID the ID of the entity to be retrieved
         * @return the entity with the corresponding ID
         * @throws NotFoundException if there is no {@code Event} with the provided ID.
         */
        @ApiMethod( name = "TaskGet",path = "TaskGet")
        public Task Get(@Named("Event_ID") String Event_ID, @Named("Task_ID_Number") String Task_ID_Number) {
                Task task = new Task();
                try {
                        ResultSet rs = MySQL_Util.select(null, "Tasks", new String[]{"Event_ID","Task_ID_Number"}, new String[]{Event_ID, Task_ID_Number}, new int[]{1});
                        if(rs.next()) {
                                task.setEvent_ID(rs.getString("Event_ID"));
                                task.setTask_ID_Number(rs.getString("Task_ID_Number"));
                                task.setTask_Name(rs.getString("Task_Name"));
                                task.setDescription(rs.getString("Description"));
                                task.setFriend_ID(rs.getString("Friend_ID"));
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
                return task;
        }

        @ApiMethod( name = "TaskGetAll",path = "TaskGetAll")
        public ArrayList<Task> GetAll(@Named("Event_ID") String Event_ID) {
                ArrayList<Task> taskArrayList = new ArrayList<>();
                try {
                        ResultSet rs = MySQL_Util.select(null,"Tasks",new String[]{"Event_ID"}, new String[]{Event_ID},null);
                        while(rs.next()){
                                taskArrayList.add(new Task(rs.getString("Event_ID"),rs.getString("Task_ID_Number"),rs.getString("Task_Name"),rs.getString("Description"),rs.getString("Friend_ID")));
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
                return taskArrayList;
        }

        /**
         * Inserts a new {@code Event}.
         */
        @ApiMethod(name = "TaskInsert",path = "TaskInsert")
        public void Insert(@Named("AEvent_ID")String Event_ID, @Named("BTask_ID_Number")String Task_ID_Number, @Named("CTask_Name")String Task_Name, @Named("DDescription")String Description,
                           @Named("EFriend_ID")String Friend_ID) {
                try {
                        MySQL_Util.insert("Tasks", new String[]{Event_ID, Task_ID_Number, Task_Name, Description, Friend_ID});

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
         *

         */
        @ApiMethod(name = "TaskUpdate",path = "TaskUpdate")
        public void Update(@Named("AEvent_ID")String Event_ID, @Named("BTask_ID_Number")String Task_ID_Number, @Named("CTask_Name")String Task_Name, @Named("DDescription")String Description,
                           @Named("EFriend_ID")String Friend_ID){
                try {
                        MySQL_Util.update("Tasks", new String[]{"Task_Name", "Description", "Friend_ID"},
                                new String[]{Task_Name, Description, Friend_ID},
                                new String[]{"Event_ID", "Task_ID_Number"}, new String[]{Event_ID, Task_ID_Number});

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
         * Deletes the specified {@code Event}.
         *
         * @param Task_ID_Number the ID of the entity to delete
         * @throws NotFoundException if the {@code id} does not correspond to an existing
         *                           {@code Event}
         */
        @ApiMethod(name = "TaskDelete",path = "TaskDelete")
        public void Delete(@Named("Event_ID") String Event_ID, @Named("Task_ID_Number") String Task_ID_Number){
                try {
                        MySQL_Util.delete("Tasks", new String[]{"Event_ID", "Task_ID_Number"}, new String[]{Event_ID, Task_ID_Number}, new int[]{1});
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

        @ApiMethod(name = "TaskDeleteByEvent",path = "TaskDeleteByEvent")
        public void DeleteByEvent(@Named("Event_ID") String Event_ID){
                try {
                        MySQL_Util.delete("Tasks",new String[]{"Event_ID"}, new String[]{Event_ID}, new int[]{1});
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