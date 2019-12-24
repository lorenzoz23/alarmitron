package alarmitron.personalalarmhomepage;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * class for web socket communications
 * @author The Alarmitron Team
 */
public class WebSocketCommunications {

    private Draft[] drafts;
    private String url;
    private WebSocketClient cc;
    private Alarm alarm;

    SentAlarm sAlarm;

    /**
     * interface for sending alarm information to main activity
     */
    public interface SentAlarm{
        void sentAlarm(Alarm a, String senderUsername);
    }

    /**
     * constructor that takes in array of drafts for web
     * socket communication and the context for interface
     * @param drafts
     * @param ctx
     */
    public WebSocketCommunications(Draft[] drafts, Context ctx){
        this.drafts = drafts;
        sAlarm = (SentAlarm) ctx;
    }

    /**
     * method used to establish connection for web socket communications
     */
    public void connection(){
        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(url), (Draft) drafts[0]) {
                /**
                 * this method is invoked when server sends alarm to particular user
                 *
                 * handle case for when it receives string from user
                 *
                 * receive alarm and the username of the person who sent it
                 *
                 * string message should include both alarm and username string
                 *
                 * @param alarmUsername
                 */
                @Override
                public void onMessage(String alarmUsername) {
                    Log.d("MESSAGE", "run() returned: " + alarmUsername);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Alarm>() {}.getType();
                    String[] arr = alarmUsername.split(" ", 2);
                    String receivedAlarm = arr[1];
                    String senderUsername = arr[0];
                    Alarm a = gson.fromJson(receivedAlarm, type);
                    sAlarm.sentAlarm(a, senderUsername);
                }

                /**
                 * when connection is made, this method invokes the server-side onOpen method
                 * @param handshake
                 */
                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                /**
                 * when connection is closed, this method is invokes the server-side method
                 * @param code
                 * @param reason
                 *      reason for closing
                 * @param remote
                 */
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                /**
                 *when an error is encountered, this method is invokes the server-side method
                 * @param e
                 */
                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.toString());
            e.printStackTrace();
        }
        cc.connect();
    }

    /**
     * setter method that sets the url variable of this class to new
     * string url parameter
     * @param url
     *      new url used to establish connection for web socket communication
     */
    public void setURL(String url){
        this.url = url;
    }


    /**
     * method sends json string of data to server-side onMessage
     * method and then sends that to user
     * @param a
     * @param username
     */
    public void sendData(Alarm a, String username){
        //send json string of data
        alarm = a;
        Gson gson = new Gson();
        String json = gson.toJson(alarm);
        String message = username + " " + json;
        try{
            //goes to server-side onMessage method
            cc.send(message);
        }catch(Exception e){
            Log.d("ExceptionSendMessage:", e.toString());
        }

    }

}
