package alarmitron.personalalarmhomepage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import alarmitron.personalalarmhomepage.app_volley.AppController;
import alarmitron.personalalarmhomepage.login.CreateLoginFragment;
import alarmitron.personalalarmhomepage.login.login;
import alarmitron.personalalarmhomepage.utils_volley.Const;

/**
 * main activity for entire application
 *
 * extends several interfaces for receiving information from other activities
 *
 * @author The Alarmitron Team
 */
public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnAlarmCreateSelectedListener,
        SaveAlertDialog.SaveAlertListener, WebSocketCommunications.SentAlarm, CreateLoginFragment.iLogin,
        login.sendInfo{

    protected ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    Map<String, String> params = new HashMap();
    private String tag_json_obj = "jobj_req", tag_json_array = "jarray_req";
    protected RecyclerView recView;
    protected AlarmAdapter adapter;
    public static ArrayList<Alarm> alarmList;
    protected ArrayList<Alarm> remoteAlarmList;
    protected SharedPreferences sPref;
    protected SharedPreferences.Editor editor;
    protected BottomNavigationView bottomNav;
    protected int hour;
    protected int minute;
    protected String jsonArrayString;
    protected String state;
    protected double lat;
    protected double lon;
    protected String asdfg;
    protected String qwerty;
    /**
     * method called when app is started
     *
     * sets up several aspects of the app including the fragment
     * that is shown upon first opening app
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLocalData();

        bottomNav = findViewById(R.id.bottom);
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PersonalFragment()).commit();
        bottomNav.setSelectedItemId(R.id.alarmitron_personal_alarms);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        createNotificationChannel();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                /**
                 * sets the selected fragment variable to whatever
                 * fragment the user wants to navigate to
                 * @param menuItem
                 *      the IDs of the various menu items
                 * @return
                 *      true
                 */
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    String tag = "";
                    switch (menuItem.getItemId()) {
                        case R.id.alarmitron_personal_alarms:
                            tag = "PersonalFragment";
                            selectedFragment = new PersonalFragment();
                            break;
                        case R.id.alarmitron_gps_alarms:
                            tag = "GPSFragment";
                            selectedFragment = new GPSFragment();
                            break;
                        case R.id.alarmitron_settings:
                            tag = "SettingsFragment";
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    if(tag.equals("PersonalFragment")){
                        lat = 0;
                        lon = 0;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, tag).commit();
                    loadRemoteData();
                    return true;

                }
            };

    /**
     * shows the dialog to user for picking the time of the alarm
     * @param v
     *      view of the dialog
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFrag = new TimePickerDialog();
        newFrag.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * method creates alarm with given parameters
     * adds the alarm to alarm array list
     * notifies the adapter of data set change to display newly added alarm
     * saves alarm locally and sends user a toast message saying alarm has been set for
     * the chosen time
     *
     * @param hour
     *      chosen hour by user
     * @param min
     *      chosen minute by user
     * @param choice
     *      choice of user to have alarm stored locally or remotely
     */
    @Override
    public void onAlarmCreate(int hour, int min, int choice) {
        Alarm alarm = new Alarm(hour, min, choice, this.lat, this.lon);
        addAlarm(alarm);

        adapter.notifyDataSetChanged();
        saveData();

        String s = alarm.setTime();
        Toast.makeText(getApplicationContext(), "Local alarm set for: " + s, Toast.LENGTH_LONG).show();
    }

    /**
     * method for receiving alarm data from another user and
     * adding that alarm to local alarm array list
     * @param a
     *      alarm sent by other user
     * @param senderUsername
     *      username of that user who sent alarm
     */
    @Override
    public void sentAlarm(Alarm a, String senderUsername){
        addAlarm(a);
        adapter.notifyDataSetChanged();
        saveData();
        Toast.makeText(getApplicationContext(), "New alarm from " + senderUsername + " received!", Toast.LENGTH_LONG).show();
    }

    /**
     * shows the dialog the user interacts with when deciding
     * whether they want to save the alarm locally or remotely
     */
    public void showAlertDialog(){
        DialogFragment alert = new SaveAlertDialog();
        alert.show(getSupportFragmentManager(), "alert");
    }

    /**
     * when user decides to save alarm locally, this method
     * redirects to the onAlarmCreate method to then save it locally
     * @param choice
     *      choice of user to save alarm data locally
     */
    @Override
    public void onDialogLocalClick(int choice) {
        onAlarmCreate(this.hour, this.minute, choice);
    }

    /**
     * when user decides to save alarm remotely, this method makes
     * a post request to save the alarm data to database
     * @param choice
     *      choice of user to save alarm remotely
     */
    @Override
    public void onDialogRemoteClick(int choice) {
        Alarm alarm = new Alarm(this.hour, this.minute, choice, this.lat, this.lon);
        addAlarm(alarm);
        adapter.notifyDataSetChanged();

        String s = alarm.setTime();
        params = new HashMap<>();
        params.put("timeOfDay", s);
        params.put("isEnabled", state);
        makeJsonPOSTReq(Const.URL_JSON_POSTOBJECT);
        Toast.makeText(getApplicationContext(), " Remote alarm set for: " + s, Toast.LENGTH_LONG).show();
    }

    /**
     * saves the alarm data locally using shared preferences
     */
    public void saveData(){
        sPref = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        editor = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmList);
        editor.putString("Alarm_List", json);
        editor.apply();
    }

    /**
     * loads locally saved data upon app start-up
     */
    private void loadLocalData(){
        SharedPreferences shared = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = shared.getString("Alarm_List", null);
        Type type = new TypeToken<ArrayList<Alarm>>() {}.getType();
        alarmList = gson.fromJson(json, type);

        if(alarmList == null){
            alarmList = new ArrayList<>();
        }
    }

    /**
     * loads remote data every time user switches between the personal fragment
     * and other fragments
     */
    private void loadRemoteData(){
        PersonalFragment personal = (PersonalFragment) getSupportFragmentManager().findFragmentByTag("PersonalFragment");
        if(personal != null && personal.isVisible()){
            makeJsonArrayReq();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Alarm>>(){}.getType();
            remoteAlarmList = gson.fromJson(jsonArrayString, type);
            if(remoteAlarmList == null){
                remoteAlarmList = new ArrayList<>();
            }
            else{
                for(int i = 0; i < remoteAlarmList.size(); i++){
                    alarmList.add(remoteAlarmList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

        }
    }

    /**
     * adds alarm to alarm list and notifies adapter of data set change
     * @param alarm
     *      alarm to add
     */
    private void addAlarm(Alarm alarm){
        this.alarmList.add(0, alarm);
        adapter.notifyItemInserted(0);
        recView.smoothScrollToPosition(0);
    }

    /**
     * shows progress-loading dialog
     */
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    /**
     * hides progress-loading dialog
     */
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * makes a json http post request to save alarm data to database
     */
    protected void makeJsonPOSTReq(String url) {
        JSONObject parameters = new JSONObject(params);
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, parameters,
                new Response.Listener<JSONObject>() {
                    /**
                     * method invoked when a response is sent
                     * and hides the progress dialog when response is
                     * received
                     * @param response
                     *      received response after post request
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            /**
             * method invoked if error occurs with volley
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        })
        {

            /**
             * Passing some request headers
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    /**
     * Making json array request
     * */
    private void makeJsonArrayReq() {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_JSON_ARRAY,
                new Response.Listener<JSONArray>() {
                    /**
                     * array list of alarms requested from the http
                     * get request
                     * @param response
                     *      alarm data
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        jsonArrayString = response.toString();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            /**
             * method invoked if error occurs with volley
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_array);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }


    /**
     *
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name test";
            String description = "description for test channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel 1 all output", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void makeLoginPost(String u, String p) {
        params = new HashMap<>();
        params.put("username", u);
        params.put("password", p);

        makeJsonPOSTReq(Const.URL_JSON_POSTLOGIN);
    }

    @Override
    public void sendInformation(String s) {
        asdfg = s;
    }
}
