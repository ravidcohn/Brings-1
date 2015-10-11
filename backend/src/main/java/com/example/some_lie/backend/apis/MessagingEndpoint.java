/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.models.RegistrationRecord;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.repackaged.com.google.storage.onestore.v3.proto2api.OnestoreEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.example.some_lie.backend.OfyService.ofy;

/**
 * An endpoint to send messages to devices registered with the backend
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
@ApiClass(resource = "messaging",
        clientIds = {
                com.example.some_lie.backend.Constants.ANDROID_CLIENT_ID,
                com.example.some_lie.backend.Constants.IOS_CLIENT_ID,
                com.example.some_lie.backend.Constants.WEB_CLIENT_ID},
        audiences = {com.example.some_lie.backend.Constants.AUDIENCE_ID}
)
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());
    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    //TODO improve this !!!!
    public void sendMessage(@Named("message") String message, @Named("from") String from, @Named("to") String to) throws IOException {
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message", from + ": " + message).build();
        ArrayList<String> regId = checkIfUserExist(to);
        for (String id: regId) {
            if (!id.equals("NOT FOUND")) {
                Result result = sender.send(msg, id, 5);
                if (result.getMessageId() != null) {
                    String canonicalRegId = result.getCanonicalRegistrationId();
                    if (canonicalRegId != null) {
                        update(to,id,canonicalRegId);
                    }
                } else {
                    //TODO
                }
            } else {
                log.warning("NOT FOUND");
            }
        }
    }

    private void update(String email, String prevId, String RegId) {
        try {
            MySQL_Util.update("UsersDevices", new String[]{"reg_id"}, new String[]{RegId}, new String[]{"email", "reg_id"}, new String[]{email, prevId});
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
    }

    private ArrayList<String> checkIfUserExist(String user) {
        ArrayList<String> regID = new ArrayList<>();
        try {
            ResultSet rs = MySQL_Util.select(null,"UsersDevices",new String[]{"email"},new String[]{user},null);
            while (rs.next()) {
                regID.add(rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return regID;
    }

    private boolean legalMessage(String msg) {
        boolean ok = false;
        if (msg.contains(" - ")) {
            String content[] = msg.split(" - ");
            if (content.length == 2) {
                if (content[0].equals("M")) {//message
                    ok = true;
                } else if (content[0].equals("NE") || content[0].equals("UE")) {//event
                    String[] data = content[1].split(", ");
                    if (data.length == 2) {
                        ok = true;
                    }
                }
            }
        }
        return ok;
    }
}
