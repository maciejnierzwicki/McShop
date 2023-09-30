package pl.maciejnierzwicki.mcshop.dbentity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "Roles")
public class Role implements Serializable {

	private static final long serialVersionUID = 6118508242792031828L;

	@Id
	private final String id;
	
	private String displayname;
	
}
