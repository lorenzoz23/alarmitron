package alarmitron.personalalarmhomepage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * dialog fragment class for pop up dialog letting
 * user decide whether they want alarm stored locally
 * or remotely
 *
 * @author The Alarmitron Team
 */
public class SaveAlertDialog extends DialogFragment {

    /**
     * interface includes methods that are invoked when user
     * makes their decision on whether to save the alarm locally
     * or remotely
     */
    public interface SaveAlertListener{
        void onDialogLocalClick(int choice);
        void onDialogRemoteClick(int choice);
    }

    SaveAlertListener mListener;

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
            mListener = (SaveAlertListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement SaveAlertListener");
        }
    }

    /**
     * method that creates the dialog the user sees and interacts with
     * @param savedInstanceState
     * @return
     *      dialog that user interacts with to make his/her choice on
     *      whether alarm should be stored locally or remotely
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] charSequence = {"Locally", "Remotely"};
        builder.setTitle(R.string.alarmitron_save_alert)
                .setItems(charSequence, new DialogInterface.OnClickListener() {
                    /**
                     * if user chooses to store it locally, the local method of
                     * the interface will be called
                     *
                     * if user chooses to store alarm remotely, the remote method
                     * of the interface will be called
                     *
                     * @param dialogInterface
                     * @param i
                     *      choice made by user
                     *      0 for local, 1 for remote
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            mListener.onDialogLocalClick(i);
                        }
                        if(i == 1){
                            mListener.onDialogRemoteClick(i);
                        }
                    }
                });

        return builder.create();
    }
}
