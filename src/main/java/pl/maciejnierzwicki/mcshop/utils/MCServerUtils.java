package pl.maciejnierzwicki.mcshop.utils;

import java.net.ConnectException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import net.kronos.rkon.core.Rcon;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;

@Slf4j
public class MCServerUtils {
	

	public static boolean testConnect(ServerConfig config) {
		return testConnect(config.getAddress(), config.getRconPort(), config.getRconPassword());
	}
	
	public static boolean testConnect(String address, Integer rconPort, String rconPassword) {
		try {
			Rcon rcon = new Rcon(address, rconPort, rconPassword.getBytes());
			rcon.command("say McShop RCON Connection test");
			rcon.disconnect();
		} catch (Exception e) {
			log.warn(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static void sendCommands(ServerConfig server, Iterable<String> commands)  {
		sendCommands(server, commands, null, null);
	}
	
	public static void sendCommands(ServerConfig server, Iterable<String> commands, String playername, String placeholder) {
		try {
			Rcon rcon = new Rcon(server.getAddress(), server.getRconPort(), server.getRconPassword().getBytes());
			for(String command : commands) {
				if(playername != null && placeholder != null) {
					rcon.command(command.replaceAll(placeholder, playername));
				}
				else {
					rcon.command(command);
				}
			}
			rcon.disconnect();
		} catch (Exception e) {
			log.error(e.getMessage()); // cannot connect to server
		}
	}
	
	public static void sendCommand(ServerConfig server, String command) {
		sendCommands(server, Arrays.asList(command));
	}
	
	public static void sendCommand(ServerConfig server, String command, String playername, String placeholder) throws ConnectException {
		sendCommands(server, Arrays.asList(command), playername, placeholder);
	}
	
}
