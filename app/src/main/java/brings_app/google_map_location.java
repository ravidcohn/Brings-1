package brings_app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class google_map_location extends AppCompatActivity implements OnItemClickListener, View.OnKeyListener, GoogleMap.OnMarkerDragListener {

    // implements OnMapClickListener, OnMapLongClickListener, OnMarkerDragListener{

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDEAV2-JZrcBQRpBBFn3OAznT0bV0a5TdE";
    private static GoogleMap map;
    private static LatLng LOCATION_BURNABY;
    private static String location_str;
    private AutoCompleteTextView autoCompView;
    private Button search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.actv_google_map);
        search = (Button)findViewById(R.id.bGoogleMapSaveLoaction);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.google_map_list_item));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setOnKeyListener(this);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fGoogleMap_map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setOnMarkerDragListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN)
                &&
                (keyCode != KeyEvent.KEYCODE_DEL)) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            autoCompView.dismissDropDown();
            String str = autoCompView.getText().toString();
            Geocoder geoCoder = new Geocoder(this);
            try {
                List<Address> addresses = geoCoder.getFromLocationName(str, 1);
                if (addresses.size() > 0) {
                    LOCATION_BURNABY = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    location_str = str;
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 16);
                    MarkerOptions place = new MarkerOptions().position(LOCATION_BURNABY).title("Event name!").draggable(true);
                    ;
                    map.addMarker(place);
                    map.animateCamera(update);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(LOCATION_BURNABY != null){
            search.setEnabled(true);
        }
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LOCATION_BURNABY = marker.getPosition();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }

    public void onClick_saveLocation(View v){
        Intent _result = new Intent();
        Bundle b = new Bundle();
        b.putString("location",LOCATION_BURNABY.latitude+"-"+LOCATION_BURNABY.longitude+"!"+location_str);
        _result.putExtras(b);
        setResult(Activity.RESULT_OK, _result);
        finish();
    }

    public void onClick_City(View v) {
//		CameraUpdate update = CameraUpdateFactory.newLatLng(LOCATION_BURNABY);
        if (LOCATION_BURNABY != null) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 16);
            map.animateCamera(update);
        }
    }

    public void onClick_Burnaby(View v) {
        if (LOCATION_BURNABY != null) {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 16);
            map.animateCamera(update);
        }

    }

    public void onClick_Surrey(View v) {
        if (LOCATION_BURNABY != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 16);
            map.animateCamera(update);
        }

    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Geocoder geoCoder = new Geocoder(this);
        try {
            List<Address> addresses = geoCoder.getFromLocationName(str, 1);
            if (addresses.size() > 0) {
                LOCATION_BURNABY = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                location_str = str;
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 16);
                MarkerOptions place = new MarkerOptions().position(LOCATION_BURNABY).title("Event name!").draggable(true);
                map.addMarker(place);
                map.animateCamera(update);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(LOCATION_BURNABY != null){
            search.setEnabled(true);
        }
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:il");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //   Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //     Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            // Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}