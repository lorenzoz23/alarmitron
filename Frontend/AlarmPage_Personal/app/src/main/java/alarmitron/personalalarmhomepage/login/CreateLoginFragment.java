package alarmitron.personalalarmhomepage.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import alarmitron.personalalarmhomepage.R;

/**
 * class for creating a showing the fragment associated
 * with the login aspect of the application
 *
 * @author The Alarmitron Team
 */
public class CreateLoginFragment extends Fragment {

    public interface iLogin{
        void makeLoginPost(String u, String p);
    }

    iLogin log;

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
            log = (iLogin) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement SaveAlertListener");
        }
    }

    /**
     * method that returns the createLoginFragment view so user can
     * create an account to login to application
     *
     * sends http post request to spring to store username and password data
     * so user may log in later with those credentials
     *
     * @param inflater
     *      layout inflater for view
     * @param container
     *      container for view
     * @param savedInstanceState
     * @return
     *      view of the createLoginFragment fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createLoginView = inflater.inflate(R.layout.alarmitron_create_login, container, false);
        Button signUpButton = createLoginView.findViewById(R.id.alarmitron_signup);

        final TextInputLayout usernameTextInput = createLoginView.findViewById(R.id.alarmitron_signup_username);
        final TextInputEditText usernameEditText = createLoginView.findViewById(R.id.alarmitron_signup_username_edit_text);
        final TextInputLayout passwordTextInput = createLoginView.findViewById(R.id.alarmitron_signup_password);
        final TextInputEditText passwordEditText = createLoginView.findViewById(R.id.alarmitron_signup_password_edit_text);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().trim().length() < 5){
                    usernameTextInput.setError("Your username must be at least 5 characters long!");
                }
                else if(passwordEditText.getText().toString().trim().length() < 8){
                    passwordTextInput.setError("Your password should be at least 8 characters long!");
                }
                else{
                    log.makeLoginPost(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    Fragment l = new login();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout, l).commit();
                }
            }
        });

        return createLoginView;
    }
}
