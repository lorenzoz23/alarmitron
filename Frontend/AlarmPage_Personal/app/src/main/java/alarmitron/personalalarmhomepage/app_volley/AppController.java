package alarmitron.personalalarmhomepage.app_volley;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import alarmitron.personalalarmhomepage.backGroundRun.AlarmRun;

/**
 * @author The Alarmitron Team
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    /**
     * method creates background running services
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, AlarmRun.class)); //this line was added by paul in attempt to create background running
        mInstance = this;
    }

    /**
     *
     * @return
     *      instance of AppController class
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * method returns request queue
     * @return
     *      request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * method that adds request to queue w/ string tag
     * @param req
     *      request to add
     * @param tag
     *      string tag for request to add
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * method that adds request to queue w/o string tag
     * @param req
     *      request to add
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * method that cancels request associated with the object parameter
     * @param tag
     *      tag associated with request to cancel
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
