package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.Constants;
import com.example.some_lie.backend.models.Event;
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
    @ApiMethod(
            name = "Event_getEvent",
            path = "event/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Event get(@Named("id") String id) throws NotFoundException {
        String url = null;
        Event event = new Event();
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url =
                    "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";

            Connection conn = DriverManager.getConnection(url);

            String query ="SELECT * FROM `Events_Friends` where Event_id ='"+id+"';";
            ResultSet rs = conn.createStatement().executeQuery(query);
            event.setId(rs.getString("id"));
            event.setName(rs.getString("name"));
            event.setLocation(rs.getString("location"));
            event.setStart_date(rs.getString("start_date"));
            event.setEnd_date(rs.getString("end_date"));
            event.setDescription(rs.getString("description"));
            event.setImage_url(rs.getString("image_path"));
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
    @ApiMethod(
            name = "insert",
            path = "event",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Event insert(@Named("ID")String id, @Named("Name")String name, @Named("Place")String place, @Named("Start")String start, @Named("End")String end, @Named("Description")String description,@Named("ImagePath")String imagePath) {
        String url = null;
        Event d = new Event();
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url =
                    "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";


            //        d.setFrom(url);
            Connection conn = DriverManager.getConnection(url);
            //String query = "CREATE TABLE `Test`(`from` VARCHAR(50),`to` VARCHAR(50),`message` VARCHAR(500));";
            //String query = "CREATE TABLE `Events`(`id` VARCHAR(50),`name` VARCHAR(50));";
            //String query2 = "insert into Events values('"+id+"','"+name+"');";
            //String query2 = "INSERT INTO `Events` VALUES ('"+id+"', '"+name+"');";
            /*String query ="CREATE TABLE IF NOT EXISTS `datdbase1`.`Events` (" +
                    "  `ID` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `Name` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `Place` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `Start_DATE` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `End_DATE` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `Description` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `imagePath` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  PRIMARY KEY (`ID`)  COMMENT '');";
            conn.createStatement().execute(query);*/

            String query ="INSERT INTO `Events` VALUES('"+id+"','" +name+"','" +place+ "','" +start+ "','"+end+"','"+description+"','"+imagePath+"');";
            //conn.createStatement().execute("DROP TABLE `Test`;");
            //boolean rs2 = conn.createStatement().execute(query);
            conn.createStatement().execute(query);


            /*query ="CREATE TABLE IF NOT EXISTS `datdbase1`.`Events_Friends` (" +
                    "  `Event_ID` VARCHAR(45) NOT NULL COMMENT ''," +
                    "  `Friend_ID` VARCHAR(45) NOT NULL COMMENT '');";
            conn.createStatement().execute(query);*/
            query ="INSERT INTO `Events_Friends` VALUES('"+id+"','" +name+"');";
            conn.createStatement().execute(query);

            //boolean rs = conn.createStatement().execute(query2);

        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            d.setName("Error!: " + exceptionAsString);
        }
        return d;
    }


    /**
     * Updates an existing {@code Event}.
     *
     * @param id    the ID of the entity to be updated
     * @param event the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(
            name = "update",
            path = "event/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Event update(@Named("id") Long id, Event event) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(event).now();
        logger.info("Updated Event: " + event);
        return ofy().load().entity(event).now();
    }

    /**
     * Deletes the specified {@code Event}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(
            name = "remove",
            path = "event/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Event.class).id(id).now();
        logger.info("Deleted Event with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "event",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Event> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Event> query = ofy().load().type(Event.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Event> queryIterator = query.iterator();
        List<Event> eventList = new ArrayList<Event>(limit);
        while (queryIterator.hasNext()) {
            eventList.add(queryIterator.next());
        }
        return CollectionResponse.<Event>builder().setItems(eventList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Event.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Event with ID: " + id);
        }
    }
}