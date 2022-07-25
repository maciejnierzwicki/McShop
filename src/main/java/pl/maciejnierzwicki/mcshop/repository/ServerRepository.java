package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;

@Repository
public interface ServerRepository extends CrudRepository<ServerConfig, Long>{
	ServerConfig getById(Long id);
	Iterable<ServerConfig> findAll();
	Iterable<ServerConfig> findByOrderByPositionAsc();
	<S extends ServerConfig> Iterable<S> saveAll(Iterable<S> servers);
	void delete(ServerConfig serverConfig);
}
