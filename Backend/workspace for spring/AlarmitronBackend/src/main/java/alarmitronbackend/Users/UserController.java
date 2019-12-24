package alarmitronbackend.Users;

import java.util.ArrayList;
import java.util.List; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import alarmitronbackend.UserAlarms.UserAlarms;

/**
 * controller class for Users
 * @author The Alarmitron Team
 *
 */
@RestController
@RequestMapping("/alarmitron")
public class UserController 
{
	
	/**
	 * instance of the UserRepository for 
	 * accessing methods used in communicating
	 * with database
	 */
	@Autowired
	UserRepository usersRepository;
	
	/**
	 * logger variable keeps track of when methods are entered in to
	 */
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * this get method returns all users saved in the database
	 * @return
	 * 		all users saved in database
	 */
	@GetMapping(path = "/users")
    public List<Users> getAllUsers() 
	{
        logger.info("Entered into Controller Layer");
        List<Users> results = usersRepository.findAll();
        logger.info("Number of Users Found:" + results.size());
        return results;
    }
	
	/**
	 * method saves a new user to database
	 * @param username
	 * 		username of the user
	 * @param password
	 * 		password of the user
	 * @return
	 * 		success string
	 */
	@PostMapping(path = "/new/user")
	public @ResponseBody String saveUser(@RequestParam String username, @RequestParam String password) 
	{
		logger.info("Entered into Controller Layer");
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(password);
		usersRepository.save(user);
		return "New user has been saved!";
	}
	
	/**
	 * get the list of all alarms that are under
	 * a particular user
	 * 
	 * this confirms the OneToMany relationship mapping
	 * @param id
	 * 		id of user
	 * @return
	 * 		list of all alarms associated with the user id
	 */
	@GetMapping(path = "/users/{id}")
    public List<UserAlarms> getAlarmsForaUser(@PathVariable(name = "id") int id) 
	{
        if(usersRepository.findById(id).isPresent()) 
        {
        	return usersRepository.findById(id).get().getUserAlarms();
        } 
        else 
        {
        	return new ArrayList<>();
        }
            
    }

	
	/**
	 * method purely here for testing purposes with postman
	 * @return
	 * 		blah
	 *//*
	@GetMapping(path = "/blah")
    public String blah() {
        logger.info("Entered into Controller Layer");
        return "blah";
    }*/

}