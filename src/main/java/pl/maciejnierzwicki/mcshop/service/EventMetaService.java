package pl.maciejnierzwicki.mcshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.EventMeta;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.repository.EventMetaRepository;

@Service
public class EventMetaService {
	
	@Autowired
	private EventMetaRepository eventMetaRepo;
	
	@Cacheable(value = "eventMeta")
	public List<EventMeta> getAllByServer(ServerConfig server) {
		return eventMetaRepo.findAllByServer(server);
	}
	
	@CacheEvict(value = "eventMeta", allEntries = true)
	public EventMeta save(EventMeta meta) {
		return eventMetaRepo.save(meta);
	}
	
	public void deleteServerData(Long server_id) {
		eventMetaRepo.deleteServerData(server_id);
	}
	
	public void deleteServiceData(Long service_id) {
		eventMetaRepo.deleteServiceData(service_id);
	}
	
	public void deleteCategoryData(Long category_id) {
		eventMetaRepo.deleteCategoryData(category_id);
	}

	public void deleteActorData(Long actor_id) {
		eventMetaRepo.deleteActorData(actor_id);
	}
	
	public void deleteUserData(Long user_id) {
		eventMetaRepo.deleteUserData(user_id);
	}

	public void deleteOrderData(Long order_id) {
		eventMetaRepo.deleteOrderData(order_id);
	}

}
