package pl.maciejnierzwicki.mcshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.repository.OrderRepository;


@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Cacheable(cacheNames = "orders")
	public Order getById(Long id) {
		return orderRepo.findById(id).orElse(null);
	}
	
	public Long countAllByUser(User user) {
		return orderRepo.countAllByUser(user);
	}
	
	@Cacheable(cacheNames = "orders")
	public Iterable<Order> getAll() {
		return orderRepo.findAll();
	}
	
	@Cacheable(cacheNames = "orders")
	public List<Order> getAllWithStatus(OrderStatus status) {
		return orderRepo.findAllByStatus(status);
	}
	
	@Cacheable(cacheNames = "orders")
	public Long countAll() {
		return orderRepo.count();
	}
	
	@Cacheable(cacheNames = "orders")
	public List<Order> getUserOrdersFromNewest(User user, Pageable pageable) {
		return orderRepo.findByUserOrderByCreationDateDesc(user, pageable);
	}
	@Cacheable(cacheNames = "orders")
	public List<Order> getAllOrdersFromNewest(Pageable pageable) {
		return orderRepo.findByOrderByCreationDateDesc(pageable);
	}
	
	@CacheEvict(cacheNames = "orders", allEntries = true)
	public Order save(Order Order) {
		return orderRepo.save(Order);
	}
	
	@CacheEvict(cacheNames = "orders", allEntries = true)
	public Iterable<Order> saveAll(Iterable<Order> categories) {
		return orderRepo.saveAll(categories);
	}
	
	@CacheEvict(cacheNames = "orders", allEntries = true)
	public void delete(Order Order) {
		orderRepo.delete(Order);
	}
	
	public void deleteUserData(Long user_id) {
		orderRepo.deleteUserData(user_id);
	}
	
	public void deleteServiceData(Long service_id) {
		orderRepo.deleteServiceData(service_id);
	}

}
