package utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import server.add_logAsyncTask;


/**
 * Created by pinhas on 14/10/2015.
 */
public final class sqlHelper {

    private static Context context;

    public static void setContext(Context context) {
        sqlHelper.context = context;
    }

    private static SQLiteDatabase getConnection() {
        SQLiteDatabase db = null;
        if (context != null) {
            db = context.openOrCreateDatabase(Constants.SQL_DB_NAME, context.MODE_PRIVATE, null);
        }
        // SQLiteDatabase.openOrCreateDatabase(Constants.SQL_DIR + Constants.SQL_DB_NAME, null);
        //SQLiteDatabase db = SQLiteDatabase.openDatabase(Constants.SQL_DIR + Constants.SQL_DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return db;
    }

    public static void update(String table_name, String[] set_columns, String[] set_values, String[] where_columns, String[] where_values) {
        try {
            clean(set_values);
            clean(where_values);
            String query = "update `" + table_name + "` set ";
            int end = set_columns.length - 1;
            for (int i = 0; i < end; i++) {
                query += "`" + set_columns[i] + "` = '" + set_values[i] + "',";
            }
            query += "`" + set_columns[end] + "` = '" + set_values[end] + "' ";
            query += "where ";
            end = where_columns.length - 1;
            for (int i = 0; i < end; i++) {
                query += "`" + where_columns[i] + "` = '" + where_values[i] + "' and ";
            }
            query += "`" + where_columns[end] + "` = '" + where_values[end] + "';";

            SQLiteDatabase db = getConnection();
            db.execSQL(query);
            db.close();
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
                String eString = sw.toString();
                if(eString.length() > 1000){
                    eString = eString.substring(0,1000)+"...";
                }
                new add_logAsyncTask().execute(eString, date, time);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    public static void insert(String table_name, String[] values) {
        try{
        clean(values);
        String query = "insert into `" + table_name + "` values('";
        int end = values.length - 1;
        for (int i = 0; i < end; i++) {
            query += values[i] + "','";
        }
        query += values[end];
        query += "');";
        SQLiteDatabase db = getConnection();
        db.execSQL(query);
        db.close();
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
                String eString = sw.toString();
                if(eString.length() > 1000){
                    eString = eString.substring(0,1000)+"...";
                }
                new add_logAsyncTask().execute(eString, date, time);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    public static void delete(String table, String[] where_columns, String[] where_values, int[] limit) {
        try{
        clean(where_values);
        int end = where_columns.length - 1;
        String query = "delete from `" + table + "` where ";
        for (int i = 0; i < end; i++) {
            query += "`" + where_columns[i] + "` = '" + where_values[i] + "' and ";
        }
        query += "`" + where_columns[end] + "` = '" + where_values[end] + "' ";
        if (false) {//limit != null) {
            query += "limit ";
            end = limit.length - 1;
            for (int i = 0; i < end; i++) {
                query += limit[i] + ", ";
            }
            query += limit[end];
        }

        query += ";";
        SQLiteDatabase db = getConnection();
        db.execSQL(query);
        db.close();
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
                String eString = sw.toString();
                if(eString.length() > 1000){
                    eString = eString.substring(0,1000)+"...";
                }
                new add_logAsyncTask().execute(eString, date, time);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    /**
     * @param what          set null for *
     * @param table
     * @param where_columns set null for all
     * @param where_values  assuming that the size is equal to 'where_columns' size!
     * @param limit         set null for no limit
     * @return
     */
    public static ArrayList<String>[] select(String[] what, String table, String[] where_columns, String[] where_values, int[] limit) {
        try{
        clean(where_values);
        String query = "select ";
        if (what == null) {
            query += "* ";
        } else {
            int end = what.length - 1;
            for (int i = 0; i < end; i++) {
                query += "`" + what[i] + "`,";
            }
            query += "`" + what[end] + "` ";
        }
        query += "from `" + table + "` ";
        if (where_columns != null) {
            int end = where_columns.length - 1;
            query += "where ";
            for (int i = 0; i < end; i++) {
                query += "`" + where_columns[i] + "` = '" + where_values[i] + "' and ";
            }
            query += "`" + where_columns[end] + "` = '" + where_values[end] + "'";
        }
        if (limit != null) {
            query += " limit ";
            int end = limit.length - 1;
            for (int i = 0; i < end; i++) {
                query += limit[i] + " , ";
            }
            query += limit[end];
        }
        query += ";";
        SQLiteDatabase db = getConnection();
        Cursor c = db.rawQuery(query, null);
        ArrayList<String>[] result = new ArrayList[c.getColumnCount()];

        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayList<>();
        }
        while (c.moveToNext()) {
            for (int i = 0; i < result.length; i++) {
                result[i].add(c.getString(i));
            }
        }
        c.close();
        db.close();
        return result;
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
                String eString = sw.toString();
                if(eString.length() > 1000){
                    eString = eString.substring(0,1000)+"...";
                }
                new add_logAsyncTask().execute(eString, date, time);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }


    private static void clean(String[] values) {
        if (values != null)
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null)
                    values[i] = values[i].replaceAll("\'", "\'\'");
            }
    }

    public static void createALLTabels() {
        try {
            SQLiteDatabase db = getConnection();
            db.execSQL("create table if not exists " + Constants.Table_Events + "(" + Constants.Table_Events_Fields[0] + " varchar NOT NULL primary key," + Constants.Table_Events_Fields[1] + " varchar NOT NULL,"
                    + Constants.Table_Events_Fields[2] + " VARCHAR NOT NULL," + Constants.Table_Events_Fields[3] + " not null," + Constants.Table_Events_Fields[4] + " not null,"
                    + Constants.Table_Events_Fields[5] + " varchar," + Constants.Table_Events_Fields[6] + " varchar," + Constants.Table_Events_Fields[7] + " VARCHAR NOT NULL)");
            db.execSQL("create table if not exists " + Constants.Table_Tasks + "(" + Constants.Table_Tasks_Fields[0] + " varchar NOT NULL," + Constants.Table_Tasks_Fields[1] + " varchar NOT NULL primary key,"
                    + Constants.Table_Tasks_Fields[2] + "VARCHAR NOT NULL," + Constants.Table_Tasks_Fields[3] + "," + Constants.Table_Tasks_Fields[4] + ")");
            db.execSQL("create table if not exists " + Constants.Table_Events_Friends + "(Event_ID varchar NOT NULL,Friend_ID varchar NOT NULL,Attending NOT NULL)");
            db.execSQL("create table if not exists " + Constants.Table_Friends + "(Name varchar NOT NULL,Phone varchar NOT NULL,email varchar,regester varchar NOT NULL )");
            db.execSQL("create table if not exists " + Constants.Table_Users + "(" + Constants.Table_Users_Fields[0] + " varchar NOT NULL primary key," +
                    Constants.Table_Tasks_Fields[1] + " varchar NOT NULL)");
            db.close();
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
            String eString = sw.toString();
            if(eString.length() > 1000){
                eString = eString.substring(0,1000)+"...";
            }
            new add_logAsyncTask().execute(eString, date, time);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    }


}
