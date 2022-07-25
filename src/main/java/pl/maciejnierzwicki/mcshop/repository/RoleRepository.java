package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, String>{
	Role getById(String id);
}
