package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Cacheable(cacheNames = "categories")
	public Category getById(Long id) {
		return categoryRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "categories")
	public Category getByName(String name) {
		return categoryRepo.findByName(name);
	}
	
	@Cacheable(cacheNames = "categories")
	public Iterable<Category> getAll() {
		return categoryRepo.findAll();
	}
	
	@Cacheable(cacheNames = "categories")
	public Long countAll() {
		return categoryRepo.count();
	}
	
	@Cacheable(cacheNames = "categories")
	public Iterable<Category> getAllOrderedByPositionHighest() {
		return categoryRepo.findByOrderByPositionAsc();
	}
	
	@CacheEvict(cacheNames = "categories", allEntries = true)
	public Category save(Category category) {
		return categoryRepo.save(category);
	}
	
	@CacheEvict(cacheNames = "categories", allEntries = true)
	public Iterable<Category> saveAll(Iterable<Category> categories) {
		return categoryRepo.saveAll(categories);
	}
	
	@CacheEvict(cacheNames = "categories", allEntries = true)
	public void delete(Category category) {
		categoryRepo.delete(category);
	}

}
