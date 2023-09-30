package pl.maciejnierzwicki.mcshop.repository;

import java.util.List;

import jakarta.transaction.Transactional;

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
	@Query("UPDATE EventMeta e SET e.server = null WHERE e.server = :server_id")
	void deleteServerData(@Param("server_id") Long server_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta e SET e.service = null WHERE e.service = :service_id")
	void deleteServiceData(@Param("service_id") Long service_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta e SET e.category = null WHERE e.category = :category_id")
	void deleteCategoryData(@Param("category_id") Long category_id);
	 
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta e SET e.actor = null WHERE e.actor = :actor_id")
	void deleteActorData(@Param("actor_id") Long actor_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta e SET e.user = null WHERE e.user = :user_id")
	void deleteUserData(@Param("user_id") Long user_id);
	
	@Modifying
	@Transactional
	@Query("UPDATE EventMeta e SET e.order = null WHERE e.order = :order_id")
	void deleteOrderData(@Param("order_id") Long order_id);
	
	
	


}
