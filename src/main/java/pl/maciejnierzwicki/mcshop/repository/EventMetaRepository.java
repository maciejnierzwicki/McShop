package pl.maciejnierzwicki.mcshop.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.EventMeta;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;

/**
 * @author Maciej Nierzwicki
 *
 */
@Repository
public interface EventMetaRepository extends CrudRepository<EventMeta, Long> {
	
	List<EventMeta> findAllByServer(ServerConfig server);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET server_id = null WHERE server_id = :server_id")
	void deleteServerData(@Param("server_id") Long server_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET service_id = null WHERE service_id = :service_id")
	void deleteServiceData(@Param("service_id") Long service_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET category_id = null WHERE category_id = :category_id")
	void deleteCategoryData(@Param("category_id") Long category_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET actor_id = null WHERE actor_id = :actor_id")
	void deleteActorData(@Param("actor_id") Long actor_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET user_id = null WHERE user_id = :user_id")
	void deleteUserData(@Param("user_id") Long user_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta SET order_id = null WHERE order_id = :order_id")
	void deleteOrderData(@Param("order_id") Long order_id);
	
	
	


}
