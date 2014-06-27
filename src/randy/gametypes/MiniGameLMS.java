package randy.gametypes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import randy.core.tools.CoreDatabase;
import randy.minigames.GameManager;
import randy.minigames.GameManager.GameTypes;
import randy.minigames.main;

public class MiniGameLMS extends MiniGame {
	
	public static Location lobbyLocation;
	
	public static ArrayList<Integer> prizeList;
	
	public MiniGameLMS() {
		super(GameTypes.LMS);
	}
	
	public void StartGame(){
		
		if(lobbyLocation == null){
			main.broadcastMessage(ChatColor.RED + "LMS locations not set! Waiting for locations to be set...");
			gamePaused = true;
		}else{
			super.StartGame();
		}
	}
	
	public void EndGame(){
		super.EndGame();
	}
	
	public boolean AddPlayer(Player player){
		boolean result = super.AddPlayer(player);
		if(result){
			//Do stuff here
			ResetPlayer(player);
		}
		return result;
	}
	
	public void ResetPlayer(Player player){
		super.ResetPlayer(player);
		player.teleport(lobbyLocation);
	}
	
	public void GiveRewards(){
		ArrayList<Player> top = GameManager.currentGame.getTopScores();
		for(int e = 0; e < top.size(); e++){
			Player player = top.get(e);
			player.sendMessage(""+ChatColor.GREEN + ChatColor.BOLD + "+ 10 Honor " + ChatColor.GOLD + "for participation");
			CoreDatabase.ModifyCurrency(player.getName(), 10);
			
			if(e == 0){
				int honor = prizeList.get(0);
				player.sendMessage(""+ChatColor.GREEN + ChatColor.BOLD + "+ " + honor + " Honor " + ChatColor.GOLD + "for " + ChatColor.RED + "FIRST " + ChatColor.GOLD + "place");
				CoreDatabase.ModifyCurrency(player.getName(), honor);
			}
			if(e == 1){
				int honor = prizeList.get(1);
				player.sendMessage(""+ChatColor.GREEN + ChatColor.BOLD + "+ " + honor + " Honor " + ChatColor.GOLD + "for " + ChatColor.RED + "SECOND " + ChatColor.GOLD + "place");
				CoreDatabase.ModifyCurrency(player.getName(), honor);
			}
			if(e == 2){
				int honor = prizeList.get(2);
				player.sendMessage(""+ChatColor.GREEN + ChatColor.BOLD + "+ " + honor + " Honor " + ChatColor.GOLD + "for " + ChatColor.RED + "THIRD " + ChatColor.GOLD + "place");
				CoreDatabase.ModifyCurrency(player.getName(), honor);
			}
			if(playerKills.containsKey(player) && playerKills.get(player) != 0){
				player.sendMessage(""+ChatColor.GREEN + ChatColor.BOLD + "+ " + playerKills.get(player) + " Honor " + ChatColor.GOLD + "for " + ChatColor.RED + playerKills.get(player) + ChatColor.GOLD + " kills");
				CoreDatabase.ModifyCurrency(player.getName(), playerKills.get(player));
			}
		}
	}
	
	public void Update(){
		
		if(!gamePaused){
			super.Update();
			
			if(participants.size() == 1){
				Player player = participants.get(0);
				SetMiniGameInventory(player);
				EndGame();
			}
			
		}else{
			if(lobbyLocation != null){
				main.broadcastMessage(ChatColor.GREEN + "LMS locations set, resuming game!");
				StartGame();
				gamePaused = false;
			}
		}
	}

}
