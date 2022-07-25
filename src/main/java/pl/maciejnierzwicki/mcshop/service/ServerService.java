package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.repository.ServerRepository;

@Service
public class ServerService {
	
	@Autowired
	private ServerRepository serverRepo;
	
	@Cacheable(cacheNames = "servers")
	public ServerConfig getById(Long id) {
		return serverRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "servers")
	public Iterable<ServerConfig> getAll() {
		return serverRepo.findAll();
	}
	
	@Cacheable(cacheNames = "servers")
	public Iterable<ServerConfig> getAllOrderedByPositionHighest() {
		return serverRepo.findByOrderByPositionAsc();
	}
	
	@CacheEvict(cacheNames = "servers", allEntries = true)
	public ServerConfig save(ServerConfig server) {
		return serverRepo.save(server);
	}
	
	@CacheEvict(cacheNames = "servers", allEntries = true)
	public Iterable<ServerConfig> saveAll(Iterable<ServerConfig> servers) {
		return serverRepo.saveAll(servers);
	}
	
	@CacheEvict(cacheNames = "servers", allEntries = true)
	public void delete(ServerConfig server) {
		serverRepo.delete(server);
	}

}
