package alarmitron.personalalarmhomepage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

import java.util.ArrayList;

/**
 * adapter class for storing alarm information to be displayed back to user
 * in the form of a recycler list view
 * @author The Alarmitron Team
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context ctx;
    private ArrayList<Alarm> alarmList;
    private String usernameChoice;
    private boolean scan;
    private Draft[] drafts = {new Draft_6455()};
    private WebSocketCommunications wsc;

    /**
     * constructor that takes in a context instance and alarm array list
     * @param ctx
     *      context of adapter class
     * @param alarmList
     *      alarm array list
     */
    public AlarmAdapter(Context ctx, ArrayList<Alarm> alarmList) {
        this.ctx = ctx;
        this.alarmList = alarmList;
        wsc = new WebSocketCommunications(drafts, ctx);
    }

    /**
     * method for creating the view to show alarm list to user
     * @param viewGroup
     * @param i
     * @return
     *      new alarmViewHolder inflated with the alarm_list layout
     */
    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.alarm_list, null);
        AlarmViewHolder holder = new AlarmViewHolder(view);
        return holder;
    }

    /**
     * method that sets up each alarm when created by user
     *
     * sets up all the buttons, text, and style associated with each alarm
     * @param alarmViewHolder
     *      the viewHolder alarms will be shown with
     * @param i
     *      index number used for editing alarm at that index point
     */
    @Override
    public void onBindViewHolder(@NonNull final AlarmViewHolder alarmViewHolder, final int i) {
        final Alarm a = alarmList.get(i);
        alarmViewHolder.timeOfDay.setText(a.setTime());
        alarmViewHolder.localRemote.setText(a.localRemoteChoice());
        alarmViewHolder.cardView.setVisibility(View.VISIBLE);
        alarmViewHolder.timeOfDay.setVisibility(View.VISIBLE);
        alarmViewHolder.dayOfTheWeek.setText("Weekdays");
        alarmViewHolder.onOff.setChecked(true);

        if(a.gps().equals("GPS Alarm")){
            alarmViewHolder.gps.setVisibility(View.VISIBLE);
        }
        else{
            alarmViewHolder.gps.setVisibility(View.INVISIBLE);
        }

        //final Draft[] drafts = {new Draft_6455()};
        //final WebSocketCommunications wsc = new WebSocketCommunications(drafts, ctx);

        alarmViewHolder.onOff.setOnClickListener(new View.OnClickListener(){
            /**
             * switch located on alarm card view that, when on,
             * means the alarm is enabled, and when off, the alarm
             * is disabled
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                if(!(alarmViewHolder.onOff.isChecked())){
                    ((MainActivity)ctx).state = "false";
                    a.setEnabledState(false);
                }
                else{
                    ((MainActivity)ctx).state = "true";
                    a.setEnabledState(true);
                }
            }
        });

        alarmViewHolder.connect.setOnClickListener(new View.OnClickListener() {
            /**
             * establishes connection for web socket communications upon user pressing
             * button
             * @param view
             */
            @Override
            public void onClick(View view) {
                String s = ((MainActivity)ctx).asdfg;
                if(!s.equals("")){
                    //ip address of pc
                    String url = "ws://192.168.1.6:8080/websocket/" + s;//add username after /websocket
                    wsc.setURL(url);
                    wsc.connection();
                }
                else{
                    Toast.makeText(ctx, "You must have an Alarmitron account to use this feature!", Toast.LENGTH_LONG).show();
                }
            }
        });

        alarmViewHolder.send.setOnClickListener(new View.OnClickListener() {
            /**
             * shows user pop dialog asking them to enter in name of user to send
             * alarm to
             * @param view
             */
            @Override
            public void onClick(View view) {
                scan = true;
                if(scan){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    usernameChoice = null;
                    builder.setTitle("Choose a user to send your alarm to!");
                    // Set up the input
                    final EditText input = new EditText(ctx);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * user clicks OK when they are satisfied with the user
                         * they chose to send alarm data to
                         * @param dialog
                         * @param which
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usernameChoice = input.getText().toString();
                            scan = false;
                            if(!scan){
                                wsc.sendData(a, usernameChoice);
                                Toast.makeText(ctx, "Alarm sent!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        /**
                         * cancels the pop up dialog asking user to pick user to send alarm to
                         * @param dialog
                         * @param which
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

            }
        });

        alarmViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            /**
             * removes alarm upon user clicking remove button and updates alarm list
             * to display removal
             * @param view
             */
            @Override
            public void onClick(View view) {
                alarmList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount() - i);
                if(alarmList.size() == 0){
                    ((MainActivity)ctx).saveData();
                }
            }
        });
    }

    /**
     * getter method that returns the size of the alarm list
     * @return
     *      size of alarm array list
     */
    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    /**
     * inner class that holds all the different elements associated with every alarm
     * @author The Alarmitron Team
     */
    class AlarmViewHolder extends RecyclerView.ViewHolder {

        TextView timeOfDay;
        TextView dayOfTheWeek;
        Switch onOff;
        CardView cardView;
        Button remove;
        TextView localRemote;
        Button send;
        Button connect;
        ImageView gps;

        /**
         * constructor that instantiates all instance variables of
         * all the various elements associated with every alarm
         * @param itemView
         *      view for finding all elements
         */
        public AlarmViewHolder(View itemView) {
            super(itemView);

            timeOfDay = itemView.findViewById(R.id.alarmitron_alarm);
            dayOfTheWeek = itemView.findViewById(R.id.alarmitron_daysOfWeek);
            onOff = itemView.findViewById(R.id.alarmitron_onOff);
            cardView = itemView.findViewById(R.id.alarmitron_cView);
            remove = itemView.findViewById(R.id.alarmitron_remove_alarm);
            localRemote = itemView.findViewById(R.id.alarmitron_local_remote);
            send = itemView.findViewById(R.id.alarmitron_send);
            connect = itemView.findViewById(R.id.alarmitron_connect);
            gps = itemView.findViewById(R.id.alarmitron_gps_image);
        }
    }

}
