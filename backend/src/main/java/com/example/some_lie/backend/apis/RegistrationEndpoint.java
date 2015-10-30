/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.models.RegistrationRecord;
import com.example.some_lie.backend.utils.Constans.Constants;
import com.example.some_lie.backend.utils.MySQL_Util;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

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
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        )
)
@ApiClass(resource = "registration",
        clientIds = {
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                Constants.WEB_CLIENT_ID},
        audiences = {Constants.AUDIENCE_ID}
)
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

    /**
     * Register a device to the backend
     */

    @ApiMethod(name = "Register", httpMethod = "POST")
    public RegistrationRecord registerDevice(@Named("AMail") String Mail, @Named("BPhone") String Phone, @Named("CNikeName") String NikeName
            , @Named("DPassword") String Password, @Named("regId") String regId) {// throws UnauthorizedException {
        // EndpointUtil.throwIfNotAuthenticated(user);
        RegistrationRecord record = new RegistrationRecord();
        //record.setRegistration_message(checkIfUserExist(Mail));
        //return record;
        boolean isExist = checkIfUserExist(Mail);
        if (!isExist) {
            try {
                String _phone = Phone;
                if (Phone.charAt(0) == '0') {
                    _phone = "+972" + Phone.substring(1);
                }
                _phone = _phone.replaceAll("-", "");
                _phone = _phone.replaceAll(" ", "");
                MySQL_Util.insert("Users", new String[]{Mail, _phone, NikeName, Password});
                ResultSet rs = MySQL_Util.select(new String[]{"reg_id"}, "UsersDevices", new String[]{"email"}, new String[]{Mail}, new int[]{1});
                boolean done = false;
                while (rs.next() && !done) {
                    if (regId.equals(rs.getString(1))) {
                        done = true;
                    }
                }
                rs.close();
                if (!done) {
                    MySQL_Util.insert("UsersDevices", new String[]{Mail, regId});
                }
                record.setRegistration_message("O.K");
            } catch(Exception e){
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

        } else {
            record.setRegistration_message("User alrady exist!");
        }
        //    record.setRegistration_message(isExist);
        return record;
    }

    @ApiMethod(name = "CheckfriendsRegistration", path = "CheckfriendsRegistration", httpMethod = "POST")
    public CollectionResponse<RegistrationRecord> CheckfriendsRegistration(@Named("user") String user, @Named("pass") String pass
            , @Named("phones") ArrayList<String> phones, @Named("new_reg_id") String new_reg_id, @Named("old_reg_id") String old_reg_id) {
        try {
            if (!checkIfUserExist(user, pass)) {
                return null;
            }
            if (!new_reg_id.equals("!")) {
                try {
                    MySQL_Util.update("UsersDevices", new String[]{"reg_id"}, new String[]{new_reg_id}, new String[]{"reg_id"}, new String[]{old_reg_id});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<RegistrationRecord> result = new ArrayList<>();
            for (int i = 0; i < phones.size(); i++) {
                String exist = checkIfUserExistByPhone(phones.get(i));
                if (!exist.equals("")) {
                    result.add(new RegistrationRecord(exist, phones.get(i)));
                }
            }

            return CollectionResponse.<RegistrationRecord>builder().setItems(result).setNextPageToken(null).build();
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
        return null;
    }

    private String checkIfUserExistByPhone(String user) {
        String isUserExist = "";
        try {
            String phone = user;
            if (phone.charAt(0) == '0') {
                phone = "+972" + user.substring(1);
            }
            phone = phone.replaceAll("-", "");
            phone = phone.replaceAll(" ", "");
            ResultSet rs = MySQL_Util.select(null, "Users", new String[]{"Phone"}, new String[]{phone}, new int[]{1});
            if (rs.next()) {
                isUserExist = rs.getString(1);
            }
            rs.close();
        } catch(Exception e){
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
        return isUserExist;
    }

    private boolean checkIfUserExist(String user) {
        boolean isUserExist = false;
        try {
            ResultSet rs = MySQL_Util.select(null, "Users", new String[]{"Friend_ID"}, new String[]{user}, new int[]{1});
            if (rs.next()) {
                isUserExist = true;
            }
            rs.close();
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
        return isUserExist;
    }

    private boolean checkIfUserExist(String user, String pass) {
        boolean isUserExist = false;
        try {
            ResultSet rs = MySQL_Util.select(null, "Users", new String[]{"Friend_ID", "Password"}, new String[]{user, pass}, new int[]{1});
            if (rs.next()) {
                isUserExist = true;
            }
            rs.close();
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
        return isUserExist;
    }

    @ApiMethod(name = "authentication", httpMethod = "POST")
    public RegistrationRecord authentication(@Named("Email_OR_Phone") String user, @Named("Password") String pass,
                                             @Named("regId") String regId) {
        RegistrationRecord record = null;
        try {
            String email = "";
            String userName = "";
            ResultSet rs = MySQL_Util.select(null, "Users", new String[]{"Friend_ID", "Password"}, new String[]{user, pass}, new int[]{1});
            if (rs.next()) {
                record = new RegistrationRecord();
                email = user;
                userName = rs.getString(2);
                rs.close();
            } else {
                String Phone = user.replaceAll("-","").replaceAll(" ","");
                if(Phone.charAt(0) == '0'){
                    Phone = "+972"+Phone.substring(1);
                }
                rs = MySQL_Util.select(null, "Users", new String[]{"Phone", "Password"}, new String[]{Phone, pass}, new int[]{1});
                if (rs.next()) {
                    email = rs.getString(1);
                    userName = rs.getString(2);
                    record = new RegistrationRecord();
                }
            }
            rs.close();
            if (record != null) {
                record.setMail(email);
                record.setName(userName);
                rs = MySQL_Util.select(new String[]{"reg_id"}, "UsersDevices", new String[]{"email"}, new String[]{email}, new int[]{1});
                boolean done = false;
                while (rs.next() && !done) {
                    if (regId.equals(rs.getString(1))) {
                        done = true;
                    }
                }
                rs.close();
                if (!done) {
                    MySQL_Util.insert("UsersDevices", new String[]{email, regId});
                }
            }
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
        return record;
    }

    /**
     * Unregister a device from the backend
     */
    @ApiMethod(name = "Unregister", httpMethod = "DELETE")
    public RegistrationRecord unregisterDevice(@Named("mail") String mail, @Named("Password") String Password) {
        RegistrationRecord record = new RegistrationRecord();
        try {
            if (checkIfUserExist(mail, Password)) {
                try {
                    MySQL_Util.delete("Users", new String[]{"Friend_ID"}, new String[]{mail}, new int[]{1});
                    record.setRegistration_message("User was deleted");

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                }
            } else {
                record.setRegistration_message("email or Password are wrong!");
            }
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
        return record;
    }
}
