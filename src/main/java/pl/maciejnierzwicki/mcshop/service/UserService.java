package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Cacheable(cacheNames = "users")
	public User getByUsername(String username) {
		return userRepo.getByUsername(username);
	}
	
	@Cacheable(cacheNames = "users")
	public User getById(Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "users")
	public Iterable<User> getAll() {
		return userRepo.findAll();
	}
	
	@CacheEvict(cacheNames = "users", allEntries = true)
	public User save(User user) {
		return userRepo.save(user);
	}
	
	@CacheEvict(cacheNames = "users", allEntries = true)
	public void delete(User user) {
		userRepo.delete(user);
	}

}
