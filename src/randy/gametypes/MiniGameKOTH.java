package randy.gametypes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;
import randy.minigames.GameManager.GameTypes;
import randy.minigames.GameManager;
import randy.minigames.main;

public class MiniGameKOTH extends MiniGame {
	
	public HashMap<Player, Integer> playerTimers = new HashMap<Player, Integer>();
	
	public static Location topLoc;
	public static Location lobbyLocation;
	
	public static ArrayList<Integer> prizeList;

	public MiniGameKOTH() {
		super(GameTypes.KOTH);
	}
	
	public void StartGame(){
		
		if(lobbyLocation == null || topLoc == null){
			Bukkit.broadcastMessage(ChatColor.RED + "KOTH locations not set! Waiting for locations to be set...");
			gamePaused = true;
		}else{
			super.StartGame();
		}
	}
	
	public void EndGame(){
		super.EndGame();
		playerTimers.clear();
	}
	
	public boolean AddPlayer(Player player){
		boolean result = super.AddPlayer(player);
		if(result){
			playerTimers.put(player, 10);
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
			
			Object[] players = playerTimers.keySet().toArray();
			for(int i = 0; i < players.length; i++){
				Player player = (Player)players[i];
				
				if(!player.isDead() && player.getLocation().distance(topLoc) <= 5){
					int time = playerTimers.get(player) - 1;
					if(time <= 0){
						playerScore.put(player, playerScore.get(player) + 1);
						time = 10;
						
						//Update scores
						/*ArrayList<Player> top = GameManager.currentGame.getTopScores();
						for(int e = 0; e < 10 && e < top.size(); e++){
							if(GameManager.currentGame.playerScore.containsKey(top.get(e)) && GameManager.currentGame.playerScore.get(top.get(e)) != 0) {
								String score = ChatColor.WHITE + "" + (e + 1) + " " + ChatColor.GREEN + top.get(e).getName();
								MGScoreboardManager.scores.add(score);
								
								System.out.print(top.get(e).getName()+": "+GameManager.currentGame.playerScore.get(top.get(e)));
							}
						}
						System.out.print("--------------");*/
						
						//Update all scoreboards
						for(int e = 0; e < participants.size(); e++){
							CoreScoreboard.UpdateScoreboard(participants.get(e).getName());
						}
						//MGScoreboardManager.oldScores.clear();
					}
					playerTimers.put(player, time);
				}
				
				if(playerScore.get(player) == 100){					
					EndGame();
					break;
				}
			}
		}else{
			if(lobbyLocation != null && topLoc != null){
				main.broadcastMessage(ChatColor.GREEN + "KOTH locations set, resuming game!");
				StartGame();
				gamePaused = false;
			}
		}
	}
}
