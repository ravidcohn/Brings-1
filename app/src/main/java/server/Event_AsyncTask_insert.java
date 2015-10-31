package server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.ImagesPath;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import utils.Constans.Constants;

/**
 * Created by Ravid on 25/09/2015.
 */
public class Event_AsyncTask_insert extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;
    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
    private static Storage storageService;
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
            File f = new File(params[8]);
            if (f.exists()) {
                String urlP = ipath.getPath();
                cloudStorage.uploadFile(Constants.bucket_name,params[8],context);
  /*              urlP = "test";
                GcsFilename filename = new GcsFilename(Constants.bucket_name, urlP);
                GcsOutputChannel outputChannel =
                        gcsService.createOrReplace(filename, GcsFileOptions.getDefaultInstance());
                RandomAccessFile fr = new RandomAccessFile(params[8], "rw");
                byte[] buffer = new byte[(int) fr.length()];
                fr.read(buffer);
                byte[] byteContent = new byte[] {1, 2, 3, 4, 5};
                outputChannel.write(ByteBuffer.wrap(byteContent));
                outputChannel.close();
                //  1024*1024 = until 10 mb
                GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(filename, 0, 1024 * 1024);
                try {
                    ObjectInputStream oin = new ObjectInputStream(Channels.newInputStream(readChannel));
                    byte[] data = (byte[]) oin.readObject();
                    Bitmap bmp;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable = true;
                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                }catch(Exception e){
                    e.printStackTrace();
                }
*/
/*
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(urlP);

                FileBody fileBody  = new FileBody(f);
                MultipartEntity reqEntity = new MultipartEntity();

                reqEntity.addPart("file", fileBody);

                httppost.setEntity(reqEntity);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);

                */
                 /*
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

*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return null;
    }
    public static void uploadStream(
            String name, String contentType, InputStream stream, String bucketName)
            throws IOException, GeneralSecurityException {
        InputStreamContent contentStream = new InputStreamContent(contentType, stream);
        StorageObject objectMetadata = new StorageObject()
                // Set the destination object name
                .setName(name)
                        // Set the access control list to publicly read-only
                .setAcl(Arrays.asList(
                        new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        // Do the insert

        Storage client = getService();
        Storage.Objects.Insert insertRequest = client.objects().insert(
                bucketName, objectMetadata, contentStream);

        insertRequest.execute();
    }private static Storage getService() throws IOException, GeneralSecurityException {
        if (null == storageService) {
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            // Depending on the environment that provides the default credentials (e.g. Compute Engine,
            // App Engine), the credentials may require us to specify the scopes we need explicitly.
            // Check for this case, and inject the Cloud Storage scope if required.
            if (credential.createScopedRequired()) {
                credential = credential.createScoped(StorageScopes.all());
            }
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            storageService = new Storage.Builder(httpTransport,null, credential)
                    .setApplicationName("test").build();
        }
        return storageService;
    }

}


