package server.Event;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.ImagesPath;

import java.io.DataOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import server.CloudEndpointBuilderHelper;

/**
 * Created by Ravid on 25/09/2015.
 */
public class Event_AsyncTask_insert extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Event_AsyncTask_insert(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            ImagesPath ipath = myApiService.eventInsert(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9]).execute();
            File f = new File(params[6]);
            if(f.exists()) {
                String urlP = ipath.getPath();
                URL url = new URL(urlP);
/*
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(urlP);

                FileBody fileBody  = new FileBody(f);
                MultipartEntity reqEntity = new MultipartEntity();

                reqEntity.addPart("file", fileBody);

                httppost.setEntity(reqEntity);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);

                */
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                DataOutputStream request = new DataOutputStream(
                        urlConnection.getOutputStream());

                String f_name = f.getName();

                String attachmentName = "file";
                String attachmentFileName = f_name;
                String crlf = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);
                RandomAccessFile fr = new RandomAccessFile(params[6], "rw");
                byte[] buffer = new byte[(int)fr.length()];
                fr.read(buffer);

                request.write(buffer);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

                request.flush();
                request.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


