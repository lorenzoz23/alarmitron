package alarmitronbackend.websockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * web socket class for Alarmitron
 * 
 * contains all methods that help in the web socket
 * aspect of the application
 * 
 * @author The Alarmitron Team
 *
 */

@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new HashMap<>();
	private static Map<String, Session> usernameSessionMap = new HashMap<>();

	/**
	 * variable for logging/keeping track of all happenings within this class
	 */
	private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

	/**
	 * method that is visited when a new session is opened and connection is made
	 * @param session
	 * 		user's session
	 * @param username
	 * 		user's username for session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		logger.info("Entered into Open");

		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);
	}

	/**
	 * method that is visited when a message is received from sender
	 * that is to go to another user(s)
	 * @param session
	 * 		session of user
	 * @param message
	 * 		string message that contains both the username and alarm data
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// Handle new messages
		//message is both alarm and username string
		
		logger.info("Entered into Message: Got Message: " + message);
		
		//handle if session is null - no one is connected to server
		if(session == null) {
			throw new NullPointerException("No one is connected to the server!");
		}
		//if session is not null, split to get username and send the message to user
		else {
			
			String[] arr = message.split(" ", 2);
            String username = arr[0];
            System.out.println(username.toString());
            System.out.println(message.toString());
			
			sendAlarmToParticularUser(username, message);
		}
		
		
	}

	/**
	 * method that is visited when connection is closed
	 * @param session
	 * 		session of user
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		String message = username + " disconnected";
		broadcast(message);
	}

	/**
	 * method visited when an error is detected
	 * @param session
	 * 		session of user
	 * @param throwable
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
	}

	/**
	 * private method that sends alarm to particular user found within 
	 * the alarmUsername parameter
	 * @param username
	 * 		name of user
	 * @param alarmUsername
	 * 		string that contains both alarm data and name of user
	 */
	private void sendAlarmToParticularUser(String username, String alarmUsername) {
		try {
			System.out.println("Hey look, you showed up!");
			//sends alarm string to client-side onMessage method
			usernameSessionMap.get(username).getBasicRemote().sendText(alarmUsername);
		} catch (IOException e) {
			System.out.println("Oh nooooooo!");
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	/**
	 * private method that broadcasts a message to all users connected to server
	 * @param message
	 * 		message to send to all users connected to server
	 * @throws IOException
	 */
	private static void broadcast(String message) throws IOException {
		sessionUsernameMap.forEach((session, username) -> {
			synchronized (session) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
