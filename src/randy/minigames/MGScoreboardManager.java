package randy.minigames;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import randy.minigames.GameManager.GameState;
import core.Scoreboard.CoreScoreboardManager;

public class MGScoreboardManager
{
	
	public static String currentTimeString = "0:00";
	public static String previousTimeString = "0:01";
	public static ArrayList<String> oldScores = new ArrayList<String>();
	public static ArrayList<String> scores = new ArrayList<String>();
	
	public static void GenerateScoreboard(Player player){
		CoreScoreboardManager.getDisplayBoard(player).resetFormat();
		
		if(GameManager.currentState == GameState.MidGame || GameManager.currentState == GameState.PreGame){
			switch(GameManager.currentGame.gameType){
			case KOTH:				
				CoreScoreboardManager.getDisplayBoard(player).setTitle("KOTH", "" + ChatColor.AQUA);
				
				for(Player plyr : GameManager.currentGame.playerScore.keySet())
				{
					//TODO: Implement sorted scoreboard

					CoreScoreboardManager.getDisplayBoard(player).putHeader(ChatColor.GREEN + plyr.getName());
					CoreScoreboardManager.getDisplayBoard(player).putField(ChatColor.GOLD + "Score: ", GameManager.currentGame, plyr.getName() + "|score");
					//CoreScoreboard.SetScore(player, ChatColor.GREEN + plyr.getName(), "minigame", GameManager.currentGame.playerScore.get(plyr));
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
				CoreScoreboardManager.getDisplayBoard(player).setTitle("LMS", "" + ChatColor.AQUA);
				CoreScoreboardManager.getDisplayBoard(player).putField(ChatColor.GOLD + "Alive: ", GameManager.currentGame, "participants");
				break;
			case Sumo:
				CoreScoreboardManager.getDisplayBoard(player).setTitle("SUMO", "" + ChatColor.AQUA);
				CoreScoreboardManager.getDisplayBoard(player).putSpace();
				break;
			default:
				CoreScoreboardManager.getDisplayBoard(player).setTitle("UNKNOWN", "" + ChatColor.AQUA);
				CoreScoreboardManager.getDisplayBoard(player).putSpace();
				break;
			
			}
		} else if(GameManager.currentState == GameState.Break){
			CoreScoreboardManager.getDisplayBoard(player).setTitle("MINIGAMES", "" + ChatColor.DARK_GREEN);
			CoreScoreboardManager.getDisplayBoard(player).putSpace();
			//TODO: Fully implement break board
			//CoreScoreboard.SetScore(player, currentTimeString, "minigame", 1);
			
			//CoreScoreboard.RemoveScore(player,  ""+ChatColor.GREEN + ChatColor.BOLD + "Waiting for", "minigame");
			//CoreScoreboard.RemoveScore(player,  ""+ChatColor.GREEN + ChatColor.BOLD + "players", "minigame");
			//if(!currentTimeString.equals(previousTimeString)) CoreScoreboard.RemoveScore(player, previousTimeString, "minigame");
			
		} else if(GameManager.currentState == GameState.Paused){
			
			CoreScoreboardManager.getDisplayBoard(player).setTitle("MINIGAMES", "" + ChatColor.DARK_GREEN);
			
			CoreScoreboardManager.getDisplayBoard(player).putHeader(""+ChatColor.GREEN + ChatColor.BOLD + "Waiting for");
			CoreScoreboardManager.getDisplayBoard(player).putHeader(""+ChatColor.GREEN + ChatColor.BOLD + "players");
		}

		CoreScoreboardManager.getDisplayBoard(player).update(true);
	}
}
