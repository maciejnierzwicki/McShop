package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	Category getById(Long id);
	Category findByName(String name);
	Iterable<Category> findAll();
	Iterable<Category> findByOrderByPositionAsc();
	void delete(Category category);

}
