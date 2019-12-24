package alarmitronbackend.Users;

import java.util.List;
import javax.persistence.CascadeType; 
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import alarmitronbackend.UserAlarms.UserAlarms;


/**
 * class representing an alarmitron user
 * every user will have a userID, a username, and a password
 * 
 * @author The Alarmitron Team
 *
 */
@Entity
@Table(name = "Users")
public class Users {
	
	/**
	 * variable representing the userID of the user
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userID;
	
	/**
	 * variable representing the user's user-name
	 */
	@NotEmpty
	private String username;
	
	/**
	 * variable representing the user's password
	 */
	@NotEmpty
	private String password;
	
	/**
	 * One user can have multiple alarms so a oneToMany relationship
	 * mapping is necessary
	 * 
	 * joinColumn annotation is used here to join list of alarms with 
	 * the id of the user who created them. So, userAlarm table will have a column called 
	 * user_id corresponding to "id" of user it is associated to
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<UserAlarms> userAlarms;
	
	
	/**
	 * getter method for userID
	 * @return
	 * 		the user's ID
	 */
	public Integer getUserID() {
        return userID;
    }

	/**
	 * setter method for setting the user's ID to the new parameter value 
	 * @param userID
	 * 		the new userID
	 */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
	
    /**
     * getter method for getting the username
     * @return
     * 		user's username
     */
	public String getUsername() {
        return username;
    }

	/**
	 * setter method for updating user's username
	 * @param username
	 * 		user's new username
	 */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * getter method for getting the password of the user
     * @return
     * 		password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter method for updating password of user
     * @param password
     * 		name of user
     */
    public void setPassword(String password) {
        this.password = password;
    }
	
    /**
     * getter method that returns full list of alarms
     * associated with user
     * @return
     * 		alarms associated with user
     */
    public List<UserAlarms> getUserAlarms(){
    	return userAlarms;
    }

}