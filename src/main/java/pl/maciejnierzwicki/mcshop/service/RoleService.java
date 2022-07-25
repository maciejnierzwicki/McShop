package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Cacheable(cacheNames = "roles")
	public Role getById(String id) {
		return roleRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "roles")
	public Iterable<Role> getAll() {
		return roleRepo.findAll();
	}
	
	@CacheEvict(cacheNames = "roles", allEntries = true)
	public Role save(Role role) {
		return roleRepo.save(role);
	}
	
	@CacheEvict(cacheNames = "roles", allEntries = true)
	public void delete(Role role) {
		roleRepo.delete(role);
	}

}
