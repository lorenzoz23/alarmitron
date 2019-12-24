package alarmitron.personalalarmhomepage.backGroundRun;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import alarmitron.personalalarmhomepage.MainActivity;
import alarmitron.personalalarmhomepage.R;

/**
 * @author The Alarmitron Team
 */
public class Notifications {

    /**
     * This is a class that holds data to contruct notification builders so
     * notifications can be used by the rest off the application
     */
    public static PendingIntent pendingIntent;

    /**
     * @variable PendingIntent This holds the intent that the notification builder needs to take in
     */


    /**
     * This contructor takes in context and builds an intent to then build a notification builder with.
     * @param context the context of the current activity
     */
    public Notifications(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }

    /**
     * This method is called to actually create a notification and it takes in some title information and
     * context to build that notification.
     *
     * @param textTitle Title of the notification being built
     * @param textContent subtext of the notification being built
     * @param context context of the current activity the notification is being built in.
     * @return returns the notification builder to be displayed.
     */
    public NotificationCompat.Builder buildNote(String textTitle, String textContent, Context context){


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context , "channel 1 all output")
                .setSmallIcon(R.drawable.ic_alarm_black_144dp)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setSound(Settings.System.DEFAULT_RINGTONE_URI)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        return mBuilder;
    }
}
