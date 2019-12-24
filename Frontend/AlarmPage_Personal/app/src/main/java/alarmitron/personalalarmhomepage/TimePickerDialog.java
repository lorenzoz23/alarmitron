package alarmitron.personalalarmhomepage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import java.util.Calendar;

/**
 * dialog fragment user interacts with to choose time they want alarm
 * to go off at
 *
 * @author The Alarmitron Team
 */
public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    OnAlarmCreateSelectedListener mCallback;

    /**
     * interface sends the hour and minute chosen by the
     * user as well as their choice of whether to save the
     * alarm locally or remotely to container activity that implements
     * the interface
     */
    public interface OnAlarmCreateSelectedListener{
        void onAlarmCreate(int hour, int min, int choice);
    }

    /**
     * method that makes sure the container activity implements
     * the callback interface
     * @param context
     *      context of container activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlarmCreateSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * creates the dialog that the user interacts with to choose the time
     * alarm will go off at
     * @param savedInstanceState
     * @return
     *      dialog user interacts with
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new android.app.TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * when the user is done with the dialog fragment, this method
     * is invoked to send the time chosen by the user to other classes
     * using the interface of this class
     *
     * @param view
     *      view of dialog
     * @param hourOfDay
     *      hour chosen by user
     * @param minute
     *      minute chosen by user
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        //mCallback.onAlarmCreate(hourOfDay, minute);
        MainActivity main = (MainActivity) getActivity();
        main.hour = hourOfDay;
        main.minute = minute;
        main.showAlertDialog();
    }

}
