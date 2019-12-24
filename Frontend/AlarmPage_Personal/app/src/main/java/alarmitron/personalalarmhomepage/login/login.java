package alarmitron.personalalarmhomepage.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import alarmitron.personalalarmhomepage.MainActivity;
import alarmitron.personalalarmhomepage.R;

/**
 * @author The Alarmitron Team
 */
public class login extends Fragment {

    public interface sendInfo{
        void sendInformation(String s);
    }

    sendInfo sI;

    /**
     * method to make sure container activity implements the interface
     * found in this class
     * @param context
     *      context of container activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            sI = (sendInfo) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement SaveAlertListener");
        }
    }

    /**
     * method that creates the view associated with the login aspect of this application
     *
     * user will be asked to sign in using their username/password, and if they don't have one,
     * they will be asked to create one by going to the creatLoginFragment class
     *
     * @param inflater
     *      layout inflator for view
     * @param container
     *      container for view
     * @param savedInstanceState
     * @return
     *      view associated with the login aspect of this application
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.alarmitron_login, container, false);

        //final TextInputLayout usernameTextInput = view.findViewById(R.id.alarmitron_username_text_input);
        final TextInputEditText usernameEditText = view.findViewById(R.id.alarmitron_username_edit_text);
        //final TextInputLayout passwordTextInput = view.findViewById(R.id.alarmitron_password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.alarmitron_password_edit_text);

        Button createButton = view.findViewById(R.id.alarmitron_account);
        Button nextButton = view.findViewById(R.id.alarmitron_next_button);

        TextView tv = view.findViewById(R.id.alarmitron_skip);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "You are using Alarmitron without an account.", Toast.LENGTH_LONG).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (users(usernameEditText.toString(), passwordEditText.toString())) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Error: You either entered in a wrong username/password combination," +
                                "\n or you need to make an account.", Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout, new CreateLoginFragment()).commit();
            }
        });

        return view;
    }

    /**
     * private method that returns true if user is in the database with
     * the correct username and password combination, false otherwise
     * @param u
     *      username string
     * @param p
     *      password string
     * @return
     *      true if username/password combination is in database, false otherwise
     * @throws SQLException
     */
    private boolean users(String u, String p) throws SQLException {
        String query = "SELECT username, password FROM User where username=? and password=?";
        ResultSet results = null;
        Connection con = null;
        try{
            String db = "jdbc:mysql://proj309-ad-05.misc.iastate.edu:3306/Alarmitron1";
            String user = "teamAD_5";
            String pass = "AD_5projectuser";
            //jdbc:mysql://proj309-ad-05.misc.iastate.edu:3306/Alarmitron1?verifyServerCertificate=false&useSSL=false&requireSSL=false
            con = DriverManager.getConnection(db, user, pass);
        }catch(SQLException sql){
            Toast.makeText(this.getContext(), "fuck", Toast.LENGTH_LONG);
            sql.printStackTrace();
        }

        try{
            Toast.makeText(this.getContext(), "shit", Toast.LENGTH_LONG);
            /*PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, u);
            ps.setString(2, p);
            results = ps.executeQuery(query);*/
        }catch(Exception sql){
            sql.printStackTrace();
        }

        /*if(!results.next()){
            results.close();
            return false;
        }
        else{
            sI.sendInformation(u);
            results.close();
            return true;
        }*/
        return false;
    }


}
