package randy.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import randy.core.CoreAPI;
import randy.kits.Kits;
import randy.minigames.GameManager;
import randy.minigames.GameManager.GameState;
import randy.minigames.GameManager.GameTypes;
import randy.minigames.MGScoreboardManager;
import randy.minigames.main;
import core.Custody.Custody;
import core.Scoreboard.CoreScoreboardManager;
import core.Scoreboard.ScoreboardValue;

public class MiniGame implements ScoreboardValue

{
	
	public static Location minigameLobbyLocation;
	
	public ArrayList<Player> participants = new ArrayList<Player>();
	public HashMap<Player, Integer> playerScore = new HashMap<Player, Integer>();
	public HashMap<Player, Integer> playerKills = new HashMap<Player, Integer>();
	public GameTypes gameType;
	public boolean gamePaused = false;
	
	public MiniGame(GameTypes type) {
		this.gameType = type;
	}
	
	public boolean AddPlayer(Player player){
		if(GameManager.currentState != GameState.MidGame){
			if(!participants.contains(player)) {
				
				CoreAPI.ExitGameModes(player);
				Custody.switchCustody(player, "minigame");
				
				main.minigamePlayers.add(player);
				participants.add(player);
				playerKills.put(player, 0);
				player.sendMessage(ChatColor.GOLD + "You have joined the " + ChatColor.RED + gameType.toString() + ChatColor.GOLD + " games!");
				playerScore.put(player, 0);
				
				return true;
			}
		}
		return false;
	}
	
	public void StartGame(){
		if(GameManager.CheckPlayerAmount(true)){
			main.broadcastMessage(ChatColor.GOLD + "The " + ChatColor.RED + gameType.toString() + ChatColor.GOLD + " has started! Good luck!");
			GameManager.currentState = GameState.MidGame;
			GameManager.currentGame = this;
			
			for(int i = 0; i < participants.size(); i++)
			{
				MGScoreboardManager.GenerateScoreboard(participants.get(i));
				CoreScoreboardManager.getDisplayBoard(participants.get(i)).update(true);
			}
		}
	}
	
	public void EndGame(){
		
		this.GiveRewards();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("CorePlugin"), new Runnable() {
            @Override
            public void run() {
            	
				ArrayList<Player> top = GameManager.currentGame.getTopScores();
				main.broadcastMessage(ChatColor.WHITE + "----------------------------------------");
				if(top.size() >= 1) main.broadcastMessage(ChatColor.YELLOW + "1st - " + ChatColor.BOLD + top.get(0).getName());
				if(top.size() >= 2) main.broadcastMessage(ChatColor.GRAY + "2st - " + ChatColor.BOLD + top.get(1).getName());
				if(top.size() >= 3) main.broadcastMessage(ChatColor.GOLD + "3st - " + ChatColor.BOLD + top.get(2).getName());
				main.broadcastMessage(ChatColor.WHITE + "----------------------------------------");
            	
        		for(int i = 0; i < participants.size(); i++){
        			Player player = participants.get(i);
        			
        			CoreScoreboardManager.getDisplayBoard(player).resetFormat();
    
        			
        			GameManager.ResetPlayerToLobby(player);
        		}
        		
        		participants.clear();
        		
        		GameManager.currentState = GameState.Break;
        		GameManager.currentGame = null;
            }
        }, 150);
		
		GameManager.breakCountdown = 9000;
		GameManager.preGameCountdown = 600;
	}
	
	public void ResetPlayer(Player player){
		SetMiniGameInventory(player);
	}
	
	public void Update(){
		
	}
	
	public ArrayList<Player> getTopScores(){
		ArrayList<Player> top = new ArrayList<Player>();
		
		Object[] players = playerScore.keySet().toArray();
		for(int i = 0; i < players.length; i++){
			top.add((Player)players[i]);
		}
		
		Collections.sort(top, new Comparator<Player>() {

			@Override
			public int compare(Player p1, Player p2) {
				return playerScore.get(p2).compareTo(playerScore.get(p1));
			}
			
		});
		return top;
	}
	
	public void SetMiniGameInventory(Player player){
		//Create the 3 items needed

		/*
		 * 	Compass - Game Menu (Right click to go)
			Clock - Kits (Select your kit)
			Nether star - Upgrades (Upgrade your kits)
		 */
		PlayerInventory inventory = player.getInventory();
		inventory.clear();

		//Watch
		String name = ""+ ChatColor.GREEN + ChatColor.BOLD +"Kits";
		String lore = ""+ ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Select your kit";
		if(gameType == GameTypes.LMS){
			inventory.addItem(Kits.getItem(Material.RECORD_4, 1, name, lore, null, null));
		} else if (gameType == GameTypes.KOTH){
			inventory.addItem(Kits.getItem(Material.RECORD_9, 1, name, lore, null, null));
		}
	}
	
	public void GiveRewards(){
	}
	
	@Override
	public String getScoreboardValue(String key)
	{		
		String[] splitkey = key.split("\\|");
		if (splitkey.length == 2)
		{
			Player player = Bukkit.getPlayer(key);
			if (player == null) return "bad";
			return "" + playerScore.get(player);
		}
		else if (splitkey.length == 1)
		{
			if (splitkey[0].equalsIgnoreCase("participants"))
				return "" + participants.size();
		}
		return "no";
	}
}
