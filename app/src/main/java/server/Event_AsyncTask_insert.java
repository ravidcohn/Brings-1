package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.ImagesPath;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import utils.Constants;
import utils.bitmapHelper;

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
            ImagesPath ipath = myApiService.eventInsert(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]).execute();
            String urlP = ipath.getPath();
            URL url = new URL(urlP);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            DataOutputStream request = new DataOutputStream(
                    urlConnection.getOutputStream());
            File f = new File(params[6]);
            String f_name = f.getName();
            RandomAccessFile aFile = new RandomAccessFile
                    (params[6], "r");
            FileChannel inChannel = aFile.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
            String attachmentName = "file";
            String attachmentFileName = f_name;
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            request.write(buffer.array());

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            request.flush();
            request.close();

            buffer.clear();
            inChannel.close();
            aFile.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


