package randy.minigames.listeners;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;
import randy.gametypes.MiniGame;
import randy.gametypes.MiniGameKOTH;
import randy.kits.Kits;
import randy.minigames.GameManager;
import randy.minigames.GameManager.GameState;
import randy.minigames.GameManager.GameTypes;

public class PlayerDeath implements Listener{
	
	public static ArrayList<Player> deadMinigamePlayers = new ArrayList<Player>();
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		
		if(GameManager.currentGame != null){
			MiniGame game = GameManager.currentGame;
			if(game.participants.contains(player)){
				CoreDatabase.ModifyDeaths(player.getName(), "minigame", 1);
				CoreDatabase.ModifyKills(event.getEntity().getKiller().getName(), "minigame", 1);
				if(GameManager.currentState == GameState.PreGame){
					player.setHealth(20d);
				} else {
					event.getDrops().clear();
					event.setDroppedExp(0);
					Kits.hasKit.put(player, false);
					
					Player killer = event.getEntity().getKiller();
					game.playerKills.put(killer, game.playerKills.get(killer) + 1);
				
					if(game.gameType == GameTypes.LMS){
						game.participants.remove(player);
						
						//Update all scoreboards
						for(int i = 0; i < game.participants.size(); i++){
							CoreScoreboard.UpdateScoreboard(game.participants.get(i).getName());
						}
					}
					deadMinigamePlayers.add(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if(deadMinigamePlayers.contains(player)){
			deadMinigamePlayers.remove(player);
			if(GameManager.currentGame != null){
				if(GameManager.currentGame.participants.contains(player)){				
					switch(GameManager.currentGame.gameType){
					case KOTH:
						GameManager.currentGame.ResetPlayer(player);
						event.setRespawnLocation(MiniGameKOTH.lobbyLocation);
						break;
					case LMS:
						GameManager.ResetPlayerToLobby(player);
						event.setRespawnLocation(MiniGame.minigameLobbyLocation);
						break;
					case Sumo:
						break;
					default:
						break;
					
					}
					return;
				}
			}else{
				event.setRespawnLocation(MiniGame.minigameLobbyLocation);
				GameManager.ResetPlayerToLobby(player);					
				return;
			}
			
			GameManager.ResetPlayerToLobby(player);
			event.setRespawnLocation(MiniGame.minigameLobbyLocation);
		}
	}
}
