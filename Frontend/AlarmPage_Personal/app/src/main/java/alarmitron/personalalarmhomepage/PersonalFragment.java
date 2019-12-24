package alarmitron.personalalarmhomepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import alarmitron.personalalarmhomepage.login.login;

/**
 * fragment class representing the user's personal alarms
 * either stored locally or remotely
 *
 * @author The Alarmitron Team
 */
public class PersonalFragment extends Fragment {

    /**
     * method that creates view of this fragment
     * @param inflater
     *      inflates the layout associated with this fragment
     * @param container
     *      container for fragment
     * @param savedInstanceState
     * @return
     *      view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View alarmView = inflater.inflate(R.layout.fragment_personal, container,false);
        final MainActivity main = (MainActivity) getActivity();
        main.recView = (RecyclerView) alarmView.findViewById(R.id.alarmitron_recView);
        main.recView.setHasFixedSize(true);
        main.recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageButton imageButton = alarmView.findViewById(R.id.alarmitron_image_login);
        imageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * takes you to login fragment if user taps on the login image in upper
             * right hand corner of screen
             * @param view
             */
            @Override
            public void onClick(View view) {
                main.bottomNav.setVisibility(View.INVISIBLE);
                Fragment loginFrag = new login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout, loginFrag).commit();
            }
        });

        main.adapter = new AlarmAdapter(getActivity(), main.alarmList);
        main.recView.setAdapter(main.adapter);

        return alarmView;
    }

    /**
     * standard toString method for this fragment
     * @return
     *      name of fragment
     */
    @Override
    public String toString(){
        return "PersonalFragment";
    }
}
