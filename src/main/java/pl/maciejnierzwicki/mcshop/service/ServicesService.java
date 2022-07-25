package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServicesService {
	
	@Autowired
	private ServiceRepository serviceRepo;
	
	@Cacheable(cacheNames = "services")
	public Service getById(Long id) {
		return serviceRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAll() {
		return serviceRepo.findAll();
	}
	
	@Cacheable(cacheNames = "services")
	public Long countAll() {
		return serviceRepo.count();
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAllOrderedByPositionHighest() {
		return serviceRepo.findByOrderByPositionAsc();
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAllByServer(ServerConfig server) {
		return serviceRepo.findAllByServer(server);
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAllByCategory(Category category) {
		return serviceRepo.findAllByCategory(category);
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAllEnabledServicesWithServerAndCategoryAndOrderedByPositionHighest(ServerConfig server, Category category) {
		return serviceRepo.findAllByServerAndCategoryAndEnabledIsTrueOrderByPositionAsc(server, category);
	}
	
	@Cacheable(cacheNames = "services")
	public Iterable<Service> getAllBySmsCode(SMSCode sms_code) {
		return serviceRepo.findAllBySmsCode(sms_code);
	}
	
	@CacheEvict(cacheNames = "services", allEntries = true)
	public Service save(Service server) {
		return serviceRepo.save(server);
	}
	
	@CacheEvict(cacheNames = "services", allEntries = true)
	public Iterable<Service> saveAll(Iterable<Service> servers) {
		return serviceRepo.saveAll(servers);
	}
	
	@CacheEvict(cacheNames = "services", allEntries = true)
	public void delete(Service server) {
		serviceRepo.delete(server);
	}

}
