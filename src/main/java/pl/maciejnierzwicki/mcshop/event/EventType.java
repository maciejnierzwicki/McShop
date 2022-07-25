package pl.maciejnierzwicki.mcshop.event;

public enum EventType {
	
	USER_REGISTER_EVENT("Zarejestrowanie użytkownika"),
	SERVICE_CREATE_EVENT("Utworzenie usługi"), SERVICE_UPDATE_EVENT("Aktualizacja usługi"), SERVICE_DELETE_EVENT("Usunięcie usługi"),
	CATEGORY_CREATE_EVENT("Utworzenie kategorii"), CATEGORY_UPDATE_EVENT("Aktualizacja kategorii"), CATEGORY_DELETE_EVENT("Usunięcie kategorii"),
	ORDER_CREATE_EVENT("Utworzenie zamówienia"), ORDER_UPDATE_EVENT("Aktualizacja zamówienia"), ORDER_COMPLETE_EVENT("Realizacja zamówienia"),
	SERVER_CREATE_EVENT("Utworzenie serwera"), SERVER_UPDATE_EVENT("Aktualizacja serwera"), SERVER_DELETE_EVENT("Usunięcie serwera");

	private String displayName;
	
	EventType(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
