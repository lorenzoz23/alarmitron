package alarmitron.personalalarmhomepage.backGroundRun;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import alarmitron.personalalarmhomepage.Alarm;
import alarmitron.personalalarmhomepage.GPSFragment;
import alarmitron.personalalarmhomepage.MainActivity;


/**
 * @author The Alarmitron Team
 * <p>
 * This class is a background running service that first create a notification handler
 * then stats a thread to keep running and check the alarm times of the current alarm list
 */
public class AlarmRun extends Service implements LocationListener {
    public LocationManager locationManager;
    double curLon;
    double curLat;

    /**
     * Create a Binder intent for the handler to use
     *
     * @param intent We create a binder for the hander used later
     * @return null
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * This is onStart method of the service, this is called when the app boots up so that the service starts doing its job
     * this creates a handler for the notifications and then creates and starts a new runnable threat that takes alarm data
     * and compares it to the current system calender clock. If the alarm matches current time the thred then calls on the handler
     * to create a notification and send it to the OS to display to user.
     *
     * @param intent  current intent
     * @param flags   flags that trigger when the service is running
     * @param startId id of the service on
     * @return we return sticky so that the service will restart if it is abruptly ended for any reason
     */
    @SuppressLint("HandlerLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                super.handleMessage(msg);

                Calendar c = Calendar.getInstance();
                Notifications note = new Notifications(getApplicationContext());
                Notification Note = (note.buildNote("" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE), "Your alarm is sounding, Time to go!!!", getApplicationContext())).build();
                Note.flags = Notification.FLAG_INSISTENT;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(1, Note);


            }
        };


        new Thread(new Runnable() {
            public void run() {

                while (true) {

                    ArrayList<Alarm> alarmData = MainActivity.alarmList;
                    Calendar c = Calendar.getInstance();

                    if (alarmData.size() >= 1) {

                        for (int i = 0; i < alarmData.size(); i++) {
                            if (alarmData.get(i).getHour() == c.get(Calendar.HOUR_OF_DAY)) {
                                if (alarmData.get(i).getMinute() == c.get(Calendar.MINUTE)) {

                                    double alarmLon = alarmData.get(i).getLongitude() * (10000 / 90) * 3280.4;
                                    double alarmLat = alarmData.get(i).getLatitude() * (10000 / 90) * 3280.4;


                                    if (alarmLon != 0) {

                                        getLocation();
                                        double curLonft = curLon * (10000 / 90) * 3280.4;
                                        double curLatft = curLat * (10000 / 90) * 3280.4;
                                        double dist = Math.sqrt(Math.pow((alarmLat - curLatft), 2) + Math.pow((alarmLon - curLonft), 2));

                                        if (dist <= 200) {

                                            handler.sendEmptyMessage(0);
                                            try {
                                                Thread.sleep(60000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                        }


                                    } else {

                                        handler.sendEmptyMessage(0);
                                        try {
                                            Thread.sleep(60000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        }).start();


        return START_STICKY;
    }

    /**
     * this method is set to notify us if the service is stopped for any reason
     */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    /**
     * if our servide is killed by the os for any reason this methond is suposed to help restart the service quickly with little delay.
     *
     * @param rootIntent intent of the task removed.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());


        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            onLocationChanged(loc);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        curLat = location.getLatitude();
        curLon = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
