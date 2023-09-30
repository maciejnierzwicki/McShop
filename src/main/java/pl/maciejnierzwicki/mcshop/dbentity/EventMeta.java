/**
 * 
 */
package pl.maciejnierzwicki.mcshop.dbentity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "EventMeta")
@Data
public class EventMeta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "actor_id", referencedColumnName = "id")
	private User actor;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private Service service;
	
	@OneToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	@OneToOne
	@JoinColumn(name = "server_id", referencedColumnName = "id")
	private ServerConfig server;
	
	@OneToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	
}
