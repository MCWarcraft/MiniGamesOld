package randy.minigames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import randy.gametypes.MiniGame;
import randy.gametypes.MiniGameKOTH;
import randy.gametypes.MiniGameLMS;

public class Config {

	//File
	static File configfile = new File("plugins" + File.separator + "Minigames" + File.separator + "config.yml");
	static FileConfiguration configuration = YamlConfiguration.loadConfiguration(configfile);
	
	static File kitsfile = new File("plugins" + File.separator + "Kits" + File.separator + "kits.yml");
	static FileConfiguration kits = YamlConfiguration.loadConfiguration(kitsfile);

	//Load config
	public static void LoadConfig(){

		File directory = new File("plugins" + File.separator + "Minigames");
		if(!directory.exists()){
			directory.mkdir();
		}

		//Create file if it does not exist
		if(!configfile.exists()) {
			try {
				configfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The config file could not be created.");
			}
		}
		
		if(!kitsfile.exists()) {
			try {
				kitsfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The kits file could not be created.");
			}
		}
		
		//General info
		if(configuration.contains("General.MinigameLobbyLocation")){
			String[] locSplit = configuration.getString("General.MinigameLobbyLocation").split(":");
			MiniGame.minigameLobbyLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		
		//KOTH info
		if(configuration.contains("KOTH.LobbyLocation")){
			String[] locSplit = configuration.getString("KOTH.LobbyLocation").split(":");
			MiniGameKOTH.lobbyLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		
		if(configuration.contains("KOTH.TopLocation")){
			String[] locSplit = configuration.getString("KOTH.TopLocation").split(":");
			MiniGameKOTH.topLoc = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		
		//LMS info
		if(configuration.contains("LMS.LobbyLocation")){
			String[] locSplit = configuration.getString("LMS.LobbyLocation").split(":");
			MiniGameLMS.lobbyLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		
		//Prizes
		if(configuration.contains("Rewards.KOTH")){
			ArrayList<Integer> prizeList = new ArrayList<Integer>();
			prizeList.add(configuration.getInt("Rewards.KOTH.First"));
			prizeList.add(configuration.getInt("Rewards.KOTH.Second"));
			prizeList.add(configuration.getInt("Rewards.KOTH.Third"));
			MiniGameKOTH.prizeList = prizeList;
			
			System.out.print("Prizelist KOTH: " + prizeList.toString());
		}else{
			System.out.print("Prizelist KOTH not found!");
		}
		
		if(configuration.contains("Rewards.LMS")){
			ArrayList<Integer> prizeList = new ArrayList<Integer>();
			prizeList.add(configuration.getInt("Rewards.LMS.First"));
			prizeList.add(configuration.getInt("Rewards.LMS.Second"));
			prizeList.add(configuration.getInt("Rewards.LMS.Third"));
			MiniGameLMS.prizeList = prizeList;
		}
	}

	public static void SaveConfig(){
		
		//General info
		if(MiniGame.minigameLobbyLocation != null){
			Location minigameLobbyLoc = MiniGame.minigameLobbyLocation;
			configuration.set("General.MinigameLobbyLocation", minigameLobbyLoc.getBlockX() + ":" +  minigameLobbyLoc.getBlockY()+ ":" + minigameLobbyLoc.getBlockZ()+ ":" + minigameLobbyLoc.getYaw());
		}

		//KOTH info
		if(MiniGameKOTH.lobbyLocation != null){
			Location kothLobbyLoc = MiniGameKOTH.lobbyLocation;
			configuration.set("KOTH.LobbyLocation", kothLobbyLoc.getBlockX() + ":" +  kothLobbyLoc.getBlockY()+ ":" + kothLobbyLoc.getBlockZ()+ ":" + kothLobbyLoc.getYaw());
		}

		if(MiniGameKOTH.topLoc != null){
			Location kothTopLoc = MiniGameKOTH.topLoc;
			configuration.set("KOTH.TopLocation", kothTopLoc.getBlockX() + ":" +  kothTopLoc.getBlockY()+ ":" + kothTopLoc.getBlockZ()+ ":" + kothTopLoc.getYaw());
		}
		
		//LMS info
		if(MiniGameLMS.lobbyLocation != null){
			Location lmsLobbyLoc = MiniGameLMS.lobbyLocation;
			configuration.set("LMS.LobbyLocation", lmsLobbyLoc.getBlockX() + ":" +  lmsLobbyLoc.getBlockY()+ ":" + lmsLobbyLoc.getBlockZ()+ ":" + lmsLobbyLoc.getYaw());
		}
		
		//Save file
		try {
			configuration.save(configfile);
		} catch (IOException e) {
			System.out.print("The config file could not be saved.");
		}
	}
}
