package alarmitronbackend.UserAlarms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the UserAlarms class
 * @author The Alarmitron Team
 *
 */
@Repository
public interface UserAlarmsRepository extends JpaRepository<UserAlarms, Integer>{

}
