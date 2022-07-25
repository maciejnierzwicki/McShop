package pl.maciejnierzwicki.mcshop.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
	
	Order getById(Long id);
	long countAllByUser(User user);
	List<Order> findAllByOrderType(OrderType orderType);
	List<Order> findAllByUser(User user);
	List<Order> findAllByUser(User user, Pageable pageable);
	List<Order> findByUserOrderByIdDesc(User user, Pageable pageable);
	List<Order> findByUserOrderByCreationDateDesc(User user, Pageable pageable);
	List<Order> findAllByPlayerName(String playerName);
	List<Order> findAllByPlayerName(String playerName, Pageable pageable);
	List<Order> findAllByStatus(OrderStatus status);
	List<Order> findByOrderByIdDesc(Pageable pageable);
	List<Order> findByOrderByCreationDateDesc(Pageable pageable);
	List<Order> findAll();
	
	@Modifying
	@Transactional
	@Query("UPDATE Orders SET user_id = null WHERE user_id = :user_id")
	void deleteUserData(@Param("user_id") Long user_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Orders SET service_id = null WHERE service_id = :service_id")
	void deleteServiceData(@Param("service_id") Long service_id);
}
