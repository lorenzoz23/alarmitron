package alarmitronbackend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the Users class 
 * @author The Alarmitron Team
 *
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	
	
}