package com.example.some_lie.backend.utils;

import com.example.some_lie.backend.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by pinhas on 11/10/2015.
 */
public class MySQL_Util {

    private static Connection getConnection() throws Exception {
        Class.forName(Constants.sqlClassName);
        Connection conn = DriverManager.getConnection(Constants.Database_PATH);
        return conn;
    }

    public static void update(String table_name, String[] set_columns, String[] set_values, String[] where_columns, String[] where_values) throws Exception {
        String query = "update `" + table_name + "` set ";
        int end = set_columns.length - 1;
        for (int i = 0; i < end; i++) {
            query += "`"+set_columns[i]+"` = '"+set_values[i]+ "',";
        }
        query += "`"+set_columns[end]+"` = '"+set_values[end]+ "' ";

        end = where_columns.length - 1;
        for (int i = 0; i < end; i++) {
            query += "`" + where_columns[i] + "` = '" + where_values[i] + "' and ";
        }
        query += "`" + where_columns[end] + "` = '" + where_values[end] + "';";

        Connection conn = getConnection();
        conn.createStatement().execute(query);
    }

    public static void insert(String table_name, String[] values) throws Exception {
        String query = "insert into `" + table_name + "` values('";
        int end = values.length - 1;
        for (int i = 0; i < end; i++) {
            query += values[i] + "','";
        }
        query += values[end];
        query += "');";
        Connection conn = getConnection();
        conn.createStatement().execute(query);
    }

    public static void delete(String table, String[] where_columns, String[] where_values, int[] limit)throws Exception {
        int end = where_columns.length - 1;
        String query = "delete from `" + table + "` where ";
        for (int i = 0; i < end; i++) {
            query += "`" + where_columns[i] + "` = '" + where_values[i] + "' and ";
        }
        query += "`" + where_columns[end] + "` = '" + where_values[end] + "' ";
        if (limit != null) {
            query += "limit ";
            end = limit.length - 1;
            for (int i = 0; i < end; i++) {
                query += limit[i]+", ";
            }
            query += limit[end];
        }

        query +=";";
        Connection conn = getConnection();
        conn.createStatement().execute(query);

    }

    /**
     * @param what         set null for *
     * @param table
     * @param where_columns set null for all
     * @param where_values assuming that the size is equal to 'where_columns' size!
     * @param limit        set null for no limit
     * @return
     */
    public static ResultSet select(String[] what, String table, String[] where_columns, String[] where_values, int[] limit) throws Exception {
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
            int end = where_columns.length -1;
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
        Connection conn = getConnection();
        return conn.createStatement().executeQuery(query);
    }


}
