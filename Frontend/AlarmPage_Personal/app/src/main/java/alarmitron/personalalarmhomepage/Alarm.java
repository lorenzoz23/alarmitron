package alarmitron.personalalarmhomepage;

/**
 * class representing a user's alarm
 * @author The Alarmitron Team
 */
public class Alarm {

    private int hour;
    private int minute;
    private StringBuilder sb;
    private String timeOfDay;
    private int choice;
    private boolean isEnabled;
    private double longitude;
    private double latitude;

    /**
     * constructor that takes in the hour, minute, and
     * whether or not the alarm has been chosen to be stored
     * locally or remotely
     *
     * @param hour
     *      hour of the alarm
     * @param minute
     *      minute of the alarm
     * @param choice
     *      if choice is 1, alarm will be stored remotely, and locally is 0
     */
    public Alarm(int hour, int minute, int choice, double lat, double lon){
        this.hour = hour;
        this.minute = minute;
        this.choice = choice;
        this.latitude = lat;
        this.longitude = lon;
    }

    /**
     * empty constructor for using Gson
     */
    public Alarm(){

    }

    /**
     * method for returning the the time of day in 12-hour AM/PM form
     * @return
     *      time of day in 12-hour AM/PM format
     */
    public String setTime() {
        sb = new StringBuilder();

        if (this.hour > 12) {
            if (this.minute >= 0 && this.minute < 10) {
                sb.append("" + (this.hour - 12) + ":" + "0" + this.minute + " PM");
                timeOfDay = sb.toString();
                return timeOfDay;
            } else {
                sb.append("" + (this.hour - 12) + ":" + this.minute + " PM");
                timeOfDay = sb.toString();
                return timeOfDay;
            }

        } else {
            if(this.hour == 0){
                if (this.minute >= 0 && this.minute < 10) {
                    sb.append("" + 12 + ":" + "0" + this.minute + " AM");
                    timeOfDay = sb.toString();
                    return timeOfDay;
                } else {
                    sb.append("" + 12 + ":" + this.minute + " AM");
                    timeOfDay = sb.toString();
                    return timeOfDay;
                }
            }
            else{
                if (this.minute >= 0 && this.minute < 10) {
                    sb.append("" + this.hour + ":" + "0" + this.minute + " AM");
                    timeOfDay = sb.toString();
                    return timeOfDay;
                } else {
                    sb.append("" + this.hour + ":" + this.minute + " AM");
                    timeOfDay = sb.toString();
                    return timeOfDay;
                }
            }


        }
    }

    /**
     * method for returning either local or remote so user
     * can choose whether they want their alarm stored locally on
     * device or in database
     * @return
     *      local if user wants alarm on device, remote if user wants alarm
     *      saved remotely in database
     */
    public String localRemoteChoice(){
        String s = "";
        if(choice == 0){
            s = "Local";
        }
        if(choice == 1){
            s = "Remote";
        }

        return s;
    }

    public String gps(){
        String s = "";
        if(latitude != 0 && longitude != 0){
            s = "GPS Alarm";
        }

        return s;
    }

    /**
     * getter method that returns hour of alarm
     * @return
     *      hour of alarm
     */
    public int getHour(){
        return this.hour;
    }

    /**
     * getter method that returns minute of alarm
     * @return
     *      minute of alarm
     */
    public int getMinute(){
        return this.minute;
    }

    /**
     * setter method that updates alarm's minute to new value
     * @param min
     *      new minute value
     */
    public void setMinute(int min){
        this.minute = min;
    }

    /**
     * setter method that updates alarm's hour to new value
     * @param h
     *      new hour value
     */
    public void setHour(int h){
        this.hour = h;
    }

    /**
     * setter method for whether or not alarm is enabled
     * @param state
     *      if true, alarm is enabled, disabled otherwise
     */
    public void setEnabledState(boolean state){
        this.isEnabled = state;
    }

    /**
     * getter method that returns the state of the alarm
     * @return
     *      true if alarm is active, false otherwise
     */
    public boolean getEnabledState(){
        return isEnabled;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }


}
