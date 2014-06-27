package randy.minigames;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import randy.core.CoreScoreboard;
import randy.minigames.GameManager.GameState;

public class MGScoreboardManager {
	
	public static String currentTimeString = "0:00";
	public static String previousTimeString = "0:01";
	public static ArrayList<String> oldScores = new ArrayList<String>();
	public static ArrayList<String> scores = new ArrayList<String>();
	
	public static void UpdateScoreboard(final String player){
		if(GameManager.currentState == GameState.MidGame || GameManager.currentState == GameState.PreGame){
			switch(GameManager.currentGame.gameType){
			case KOTH:				
				CoreScoreboard.SetTitle(player, ChatColor.AQUA + "KOTH");
				
				for(Player plyr : GameManager.currentGame.playerScore.keySet()){
					CoreScoreboard.SetScore(player, ChatColor.GREEN + plyr.getName(), "minigame", GameManager.currentGame.playerScore.get(plyr));
				}
				/*if(!oldScores.isEmpty()){
					for(int i = oldScores.size() - 1; i > 0; i--){
						if(i < oldScores.size() - 1)
							CoreScoreboard.RemoveScore(player, oldScores.get(i), "minigame");
					}
					oldScores.clear();
				}
					
				ArrayList<Player> top = GameManager.currentGame.getTopScores();	
				
				for(int i = 0; i < top.size(); i++){
						CoreScoreboard.SetScore(player, 
							scores.get(i),
							"minigame", 
							GameManager.currentGame.playerScore.get(top.get(i)));
					
					oldScores.add(scores.get(i));
				}*/
				
				/*Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("CorePlugin"), new Runnable() {
					@Override
					public void run() {
						if(!oldScores.containsKey(player)) oldScores.put(player, new ArrayList<String>());
						
						if(!oldScores.get(player).isEmpty()){
							
							for(int i = oldScores.get(player).size() - 1; i > 0; i--){
								try{
									if(oldScores.get(player).get(i) != null)
										CoreScoreboard.RemoveScore(player, oldScores.get(player)
												.get(i), "minigame");
								}catch (IndexOutOfBoundsException e){
									System.out.print("[ERROR] Couldn't find " + i + " in list: ");
									System.out.print(oldScores.get(player).toString());
								}
							}
							oldScores.get(player).clear();
						}
							
						ArrayList<Player> top = GameManager.currentGame.getTopScores();
						for(int i = 0; i < 10 && i < top.size(); i++){
							if(GameManager.currentGame.playerScore.containsKey(top.get(i)) && GameManager.currentGame.playerScore.get(top.get(i)) != 0) {
								String score = ChatColor.WHITE + "" + (i + 1) + " " + ChatColor.GREEN + top.get(i).getName();
								CoreScoreboard.SetScore(player, score, "minigame", GameManager.currentGame.playerScore.get(top.get(i)));
								
								oldScores.get(player).add(score);
							}
						}
					}
				});*/
				
				break;
			case LMS:
				CoreScoreboard.SetTitle(player, ChatColor.AQUA + "LMS");
				CoreScoreboard.SetScore(player, ChatColor.WHITE + "Alive: ", "minigame", GameManager.currentGame.participants.size());
				break;
			case Sumo:
				CoreScoreboard.SetTitle(player, ChatColor.AQUA + "SUMO");
				break;
			default:
				CoreScoreboard.SetTitle(player, ChatColor.AQUA + "UNKNOWN");
				break;
			
			}
		} else if(GameManager.currentState == GameState.Break){
			
			CoreScoreboard.SetTitle(player, ChatColor.DARK_GREEN + "MINIGAMES");
			
			CoreScoreboard.SetScore(player, ""+ChatColor.GRAY + ChatColor.BOLD + "Next Game:", "minigame", 2);
			CoreScoreboard.SetScore(player, currentTimeString, "minigame", 1);
			
			CoreScoreboard.RemoveScore(player,  ""+ChatColor.GREEN + ChatColor.BOLD + "Waiting for", "minigame");
			CoreScoreboard.RemoveScore(player,  ""+ChatColor.GREEN + ChatColor.BOLD + "players", "minigame");
			if(!currentTimeString.equals(previousTimeString)) CoreScoreboard.RemoveScore(player, previousTimeString, "minigame");
			
		} else if(GameManager.currentState == GameState.Paused){
			
			CoreScoreboard.SetTitle(player, ChatColor.DARK_GREEN + "MINIGAMES");
			
			CoreScoreboard.SetScore(player, ""+ChatColor.GREEN + ChatColor.BOLD + "Waiting for", "minigame", 2);
			CoreScoreboard.SetScore(player, ""+ChatColor.GREEN + ChatColor.BOLD + "players", "minigame", 1);
		}
	}
}
