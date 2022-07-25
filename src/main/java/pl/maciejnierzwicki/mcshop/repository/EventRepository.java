package pl.maciejnierzwicki.mcshop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;


@Repository
public interface EventRepository extends CrudRepository<AppEvent, Long> {

	AppEvent getById(Long id);
	
	Iterable<AppEvent> findAll();
	
	List<AppEvent> findByOrderByDateDesc(Pageable pageable);
	
	void delete(AppEvent event);


}
