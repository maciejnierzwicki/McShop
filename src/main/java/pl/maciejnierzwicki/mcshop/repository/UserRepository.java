package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User getByUsername(String username);
	User getById(Long id);

}