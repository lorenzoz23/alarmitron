package alarmitronbackend.UserAlarms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import alarmitronbackend.Users.UserController;

/**
 * controller class for UserAlarms
 * @author The Alarmitron Team
 *
 */
@RestController
@RequestMapping("/alarmitron")
public class UserAlarmsController {

	/**
	 * Instance of the repository interface for UserAlarms package
	 */
	@Autowired
	UserAlarmsRepository userAlarmsRepository;
	
	/**
	 * logger variable keeps track of when methods are entered in to
	 */
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * method that acts as a http post request that saves 
	 * a user's alarm with the time of day and whether or not 
	 * it's active.
	 * @param timeOfDay
	 * 		string indicating the time of day of the alarm
	 * @param isEnabled
	 * 		string is either true for enabled or false for disabled
	 * @return
	 * 		string response to indicate a successful save
	 */
	@PostMapping(path = "/new")
	public @ResponseBody String saveAlarm(@RequestParam String timeOfDay, @RequestParam String isEnabled) {
		logger.info("Entered into Controller Layer");
		UserAlarms alarm = new UserAlarms();
		if(isEnabled.equals("true")){
			alarm.setEnabledState(true);
		}
		else {
			alarm.setEnabledState(false);
		}
		alarm.setTimeOfDay(timeOfDay);
		userAlarmsRepository.save(alarm);
		return "New alarm has been saved!";
	}

	/**
	 * method that acts as a http get request that returns 
	 * a list of all the alarms in the database
	 * @return
	 * 		list of all alarms in database
	 */
	@GetMapping(path = "/all")
	public List<UserAlarms> getAllUserAlarms() {
		logger.info("Entered into Controller Layer");
		List<UserAlarms> results = userAlarmsRepository.findAll();
		return results;
	}

}
