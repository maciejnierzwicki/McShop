package pl.maciejnierzwicki.mcshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;
import pl.maciejnierzwicki.mcshop.repository.EventRepository;

@Service
public class EventService {
	
	@Autowired
	private EventRepository eventRepo;
	
	@Cacheable(value = "events")
	public AppEvent getById(Long id) {
		return eventRepo.findById(id).orElse(null);
	}
	
	@Cacheable(value = "events")
	public Iterable<AppEvent> getAll() {
		return eventRepo.findAll();
	}
	
	@Cacheable(value = "events")
	public Long countAll() {
		return eventRepo.count();
	}
	
	@Cacheable(value = "events")
	public List<AppEvent> getAllEventsFromNewest(Pageable pageable) {
		return eventRepo.findByOrderByDateDesc(pageable);
	}
	
	@CacheEvict(value = "events", allEntries = true)
	public<S extends AppEvent> AppEvent save(AppEvent event) {
		return eventRepo.save(event);
	}
	
	@CacheEvict(value = "events", allEntries = true)
	public void delete(AppEvent event) {
		eventRepo.delete(event);
	}
	

}
