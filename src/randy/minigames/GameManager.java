package randy.minigames;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import randy.core.CoreScoreboard;
import randy.gametypes.MiniGame;
import randy.gametypes.MiniGameKOTH;
import randy.gametypes.MiniGameLMS;
import randy.kits.Kits;

public class GameManager {

	public static enum GameTypes{
		KOTH,
		Sumo,
		LMS
	}

	public static enum GameState{
		PreGame, //Pre game countdown
		MidGame, //Game is playing
		Break,   //Game has ended, 15 break
		Paused   //Not enough participants
	}

	//Game info
	public static MiniGame currentGame;
	public static GameState currentState = GameState.Break;
	public static GameState previousState = GameState.Paused;
	
	//Countdown
	public static int preGameCountdown = 600; //1 minute
	public static int breakCountdown = 9000; //15 minutes (9000)

	public static boolean CheckPlayerAmount(boolean lastCheck){
		int playerAmount = Bukkit.getServer().getOnlinePlayers().length;
		if(playerAmount == 1){
			if(currentState != GameState.Paused){
				main.broadcastMessage(ChatColor.GOLD + "Minigames are paused until there are more players!");
				currentState = GameState.Paused;
			}
			return false;
		} else if(currentState == GameState.Paused){
			main.broadcastMessage(ChatColor.GOLD + "The mini games have resumed!");
			currentState = GameState.Break;
		}
		
		if(currentGame != null && lastCheck){
			
			switch(currentGame.gameType){
			case KOTH:
				if(currentGame.participants.size() <= 1){
					main.broadcastMessage(ChatColor.GOLD + "The " + ChatColor.RED + "KOTH" + ChatColor.GOLD + " game has been cancelled! Not enough players!");
					currentGame.EndGame();
					return false;
				}
				break;
			case LMS:
				if(currentGame.participants.size() <= 1){
					main.broadcastMessage(ChatColor.GOLD + "The " + ChatColor.RED + "LMS" + ChatColor.GOLD + " game has been cancelled! Not enough players!");
					currentGame.EndGame();
					return false;
				}
				break;
			case Sumo:
				
				return false;
			default:
				break;
			
			}
		}
		
		/*if(playerAmount >= 20){
			if(currentState == GameState.Paused){
				Bukkit.broadcastMessage(ChatColor.GOLD + "20 players online! Let the games begin!");
				currentState = previousState;
			}
			return true;
		}else{
			if(currentState != GameState.Paused){
					Bukkit.broadcastMessage(ChatColor.GOLD + "There are no longer 20 players online! Mini-games have been disabled!");
					previousState = currentState;
					currentState = GameState.Paused;
			}
			return false;
		}*/
		return true;
	}

	public static void UpdateCountdown(){
		//System.out.print("Gamestate: " + currentState);
		switch(currentState){
		case Break:
			breakCountdown--;
			if(breakCountdown <= 0){
				StartRandomGame();
			}
			
			MGScoreboardManager.previousTimeString = MGScoreboardManager.currentTimeString;
			float countdown = (float)GameManager.breakCountdown / 10;
			int minutes = (int)(countdown / 60); //Minutes
			String seconds = ""+Math.round(countdown % 60); //Seconds
			if(seconds.length() == 1) seconds = "0" + seconds;
			MGScoreboardManager.currentTimeString = ""+ChatColor.GREEN + ChatColor.BOLD + minutes + ":" + seconds;
			
			for(Player player : main.minigamePlayers) CoreScoreboard.UpdateScoreboard(player.getName());
			break;
		case MidGame:
			if(currentGame != null) currentGame.Update();
			break;
		case PreGame:
			if(preGameCountdown == 600 || 
			preGameCountdown == 500 ||
			preGameCountdown == 400 ||
			preGameCountdown == 300 ||
			preGameCountdown == 200 ||
			preGameCountdown == 100 ||
			preGameCountdown == 90 ||
			preGameCountdown == 80 ||
			preGameCountdown == 70 ||
			preGameCountdown == 60 ||
			preGameCountdown == 50 ||
			preGameCountdown == 40 ||
			preGameCountdown == 30 ||
			preGameCountdown == 20 ||
			preGameCountdown == 10){
				BroadcastCountdown();
			}else if(preGameCountdown <= 0){
				currentGame.StartGame();
				currentState = GameState.MidGame;
			}
			preGameCountdown--;
			break;
		case Paused:
			if(currentGame != null){
				currentGame.Update();
			}else{
				
			}
			
			for(Player player : main.minigamePlayers) CoreScoreboard.UpdateScoreboard(player.getName());
			break;
		default:
			break;
		}
	}

	public static void BroadcastCountdown(){
		main.broadcastMessage(ChatColor.GOLD + "A " + ChatColor.RED + currentGame.gameType.toString() + ChatColor.GOLD + 
				" is starting in " + ChatColor.RED + (preGameCountdown / 10) + ChatColor.GOLD + " seconds!"
				+ " Type " + ChatColor.RED + "/join " + ChatColor.GOLD + "to play and win Honor!");
	}

	public static void ForceStart(GameTypes type){
		if(currentGame != null) currentGame.EndGame();
		
		switch(type){
		case KOTH:
			currentGame = new MiniGameKOTH();
			break;
		case LMS:
			currentGame = new MiniGameLMS();
			break;
		case Sumo:
			break;
		default:
			break;
		}
		
		currentState = GameState.PreGame;
		preGameCountdown = 600;
	}
	
	public static void StartRandomGame(){
		
		currentState = GameState.PreGame;
		preGameCountdown = 600;
		breakCountdown = 9000;
		
		Random randomGen = new Random();
		int randomGame = randomGen.nextInt(GameTypes.values().length);
		if(randomGame == 0){
			currentGame = new MiniGameKOTH();
		}else if(randomGame == 1){
			//SUMO
			currentGame = new MiniGameKOTH();
		}else if(randomGame == 2){
			//LMS
			currentGame = new MiniGameLMS();
		}
	}
	
	public static void ResetPlayerToLobby(Player player){
		
		player.teleport(MiniGame.minigameLobbyLocation);
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta itemMeta = item.getItemMeta();
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Right click to go");
		
		itemMeta.setDisplayName(""+ ChatColor.GREEN + ChatColor.BOLD + "Game Menu");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		
		player.getInventory().addItem(item);
		
		Kits.hasKit.put(player, false);
	}
	
	public static void RemovePlayer(Player player){
		if(currentGame != null) currentGame.participants.remove(player);
		main.minigamePlayers.remove(player);
	}
}
