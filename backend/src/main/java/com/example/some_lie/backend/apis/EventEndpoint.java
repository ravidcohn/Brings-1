package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Event;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Event} with the provided ID.
     */
    @ApiMethod( name = "EventGet",path = "EventGet")
    public Event Get(@Named("id") String id) {
        Event event = new Event();
        try {
            ResultSet rs = MySQL_Util.select(null,"Events",new String[]{"id"}, new String[]{id},new int[]{1});
            if(rs.next()) {
                event.setId(rs.getString("id"));
                event.setName(rs.getString("name"));
                event.setLocation(rs.getString("location"));
                event.setStart_date(rs.getString("start_date"));
                event.setEnd_date(rs.getString("end_date"));
                event.setDescription(rs.getString("description"));
                event.setImage_url(rs.getString("image_path"));
                event.setUpdate_time(rs.getString("update_time"));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return event;
    }

    /**
     * Inserts a new {@code Event}.
     */
    @ApiMethod(name = "EventInsert",path = "event")
    public void Insert(@Named("ID")String id, @Named("Name")String name, @Named("Location")String location, @Named("Start")String start,
                        @Named("End")String end, @Named("Description")String description,@Named("ImagePath")String imagePath,@Named("UpdateTime")String updateTime) {
        try {
           MySQL_Util.insert("Events",new String[]{id,name,location,start,end,description,imagePath,updateTime});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
        }
    }

    /**
     *
     * @param id
     * @param name
     * @param start
     * @param end
     * @param description
     * @param imagePath
     * @param updateTime
     */
    @ApiMethod(name = "EventUpdate",path = "EventUpdate")
    public void Update(@Named("ID")String id, @Named("Name")String name, @Named("Location")String location, @Named("Start")String start,
                       @Named("End")String end, @Named("Description")String description,@Named("ImagePath")String imagePath,@Named("UpdateTime")String updateTime){
        try {
            MySQL_Util.update("Events",new String[]{"name","location","start_date","end_date","description","image_path","Update_Time"},
                    new String[]{name,location,start,end,description,imagePath,updateTime},
                    new String[]{"id"}, new String[]{id});

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }

    /**
     * Deletes the specified {@code Event}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(name = "EventDelete",path = "EventDelete")
    public void Delete(@Named("id") String id) {
        try {
            MySQL_Util.delete("Events",new String[]{"id"}, new String[]{id}, new int[]{1});
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }


}