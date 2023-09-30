/**
 * 
 */
package pl.maciejnierzwicki.mcshop.dbentity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maciejnierzwicki.mcshop.event.EventType;

@Data
@Entity(name = "Events")
@Table(name = "Events")
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Component
public class AppEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "creation_date")
	private Date date;
	
	@Column(name = "event_type")
	private EventType type;
	
	@OneToOne
	@JoinColumn(name = "meta_id", referencedColumnName = "id")
	private EventMeta meta;
	
	public AppEvent(EventType type) {
		this.type = type;
		this.date = new Date();
	}


}
