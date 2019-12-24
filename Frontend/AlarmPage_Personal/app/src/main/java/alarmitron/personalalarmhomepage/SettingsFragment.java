package alarmitron.personalalarmhomepage;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.graphics.PorterDuff.Mode.DARKEN;

/**
 * fragment class representing the settings for the Alarmitron application
 * @author The Alarmitron Team
 */
public class SettingsFragment extends Fragment {

    /**
     * method inflates the layout of this fragment for the user to interact with
     * @param inflater
     *      layout inflater used to inflate layout
     * @param container
     *      container for fragment
     * @param savedInstanceState
     * @return
     *      view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_settings, null);
         //final View alarmView = inflater.
        final View view2 = inflater.inflate(R.layout.alarm_list, null);
        Button red = view.findViewById(R.id.red);
        Button green = view.findViewById(R.id.green);
        Button yellow = view.findViewById(R.id.yellow);

       /*
       When user presses red as their choice of button background for main activity set to red
        */
        red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                view2.getBackground().setColorFilter(Color.parseColor("00ff00"), PorterDuff.Mode.DARKEN);


            }
        });
        /*
         When user presses yellow as their choice of button background for main activity set to yellow
         */
        yellow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view2.getBackground().setColorFilter(Color.parseColor("ffffff00"), PorterDuff.Mode.DARKEN);


            }
        });
       /*
       When user presses green as their choice of button background for main activity set to green
        */
        green.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view2.getBackground().setColorFilter(Color.parseColor("00ff00"), PorterDuff.Mode.DARKEN);


            }
        });

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /**
     * standard toString method for this fragment
     * @return
     *      string name of fragment
     */
    @Override
    public String toString(){
        return "SettingsFragment";
    }


}
