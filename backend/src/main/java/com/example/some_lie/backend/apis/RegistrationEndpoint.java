/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.models.RegistrationRecord;
import com.example.some_lie.backend.utils.EndpointUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.example.some_lie.backend.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */

@Api(name = "brings", version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = com.example.some_lie.backend.Constants.API_OWNER,
                ownerName = com.example.some_lie.backend.Constants.API_OWNER,
                packagePath = com.example.some_lie.backend.Constants.API_PACKAGE_PATH
        )
)
@ApiClass(resource = "registration",
        clientIds = {
                com.example.some_lie.backend.Constants.ANDROID_CLIENT_ID,
                com.example.some_lie.backend.Constants.IOS_CLIENT_ID,
                com.example.some_lie.backend.Constants.WEB_CLIENT_ID},
        audiences = {com.example.some_lie.backend.Constants.AUDIENCE_ID}
)
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

    /**
     * Register a device to the backend
     */

    @ApiMethod(name = "Register", httpMethod = "POST")
    public RegistrationRecord registerDevice(@Named("mail") String mail, @Named("name") String name, @Named("phone") String phone
            , @Named("password") String password, @Named("regId") String regId) {// throws UnauthorizedException {
        // EndpointUtil.throwIfNotAuthenticated(user);
        RegistrationRecord record = new RegistrationRecord();

        if (!checkIfUserExist(mail)) {
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                String url = "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";
                Connection conn = DriverManager.getConnection(url);

                String query = "insert into `Users` values('" + mail + "','" + name + "','" + phone + "','" + password + "','"+regId+"');";
                conn.createStatement().execute(query);
                record.setRegistration_message("O.K");
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                record.setRegistration_message(sw.toString());
            }

        } else {
            record.setRegistration_message("User alrady exist!");
        }
        return record;
    }

    private boolean checkIfUserExist(String user) {
        boolean isUserExist = true;
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            String url = "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";
            Connection conn = DriverManager.getConnection(url);

            String query = "select * from `Users` where `email` = '" + user + "' limit 1;";
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (!rs.next()) {
                isUserExist = false;
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return isUserExist;
    }

    private boolean checkIfUserExist(String user, String pass) {
        boolean isUserExist = true;
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            String url = "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";
            Connection conn = DriverManager.getConnection(url);
            String query = "select * from `Users` where `email` = '" + user + "' and `password` = '" + pass + "' limit 1;";
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (!rs.next()) {
                isUserExist = false;
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return isUserExist;
    }

    /**
     * Unregister a device from the backend
     */
    @ApiMethod(name = "Unregister", httpMethod = "DELETE")
    public RegistrationRecord unregisterDevice(@Named("mail") String mail, @Named("password") String password) {
        RegistrationRecord record = new RegistrationRecord();
        if (checkIfUserExist(mail, password)) {

            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                String url = "jdbc:google:mysql://encoded-keyword-106406:test/datdbase1?user=root";
                Connection conn = DriverManager.getConnection(url);

                String query = "delete * from `Users` where `email` = '" + mail + "' limit 1;";
                conn.createStatement().execute(query);
                record.setRegistration_message("User was deleted");

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                // record.setRegistration_message(sw.toString());
                // log.info(sw.toString());
            }
        } else {
            record.setRegistration_message("email or password are wrong!");
        }
        return record;
    }

    /**
     * Return a collection of registered devices
     *
     * @param count The number of devices to list
     * @return a list of Google Cloud Messaging registration Ids
     */
  /*
    @ApiMethod(name = "listDevices",httpMethod = "GET")
    public CollectionResponse<RegistrationRecord> listDevices(@Named("count") int count) throws UnauthorizedException {
      //  EndpointUtil.throwIfNotAdmin(user);
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(count).list();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }

    private RegistrationRecord findRecord(String regId) {
        return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
    }
*/
}
