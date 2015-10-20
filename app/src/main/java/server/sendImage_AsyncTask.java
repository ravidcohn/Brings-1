package server;

/**
 * Created by pinhas on 19/10/2015.
 */
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import utils.Constants;

public class sendImage_AsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try{
            URL url = new URL(Constants.ROOT_URL.substring(0,Constants.ROOT_URL.length()-8)+"blob_image");
            URLConnection connection = url.openConnection();

            String inputString = params[0];//inputValue.getText().toString();
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(inputString);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String returnString="";
            //doubledValue =0;

            while ((returnString = in.readLine()) != null)
            {
                //doubledValue= Integer.parseInt(returnString);
            }
            in.close();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


}
