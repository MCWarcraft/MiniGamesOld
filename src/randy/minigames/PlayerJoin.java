package randy.minigames;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(GameManager.currentGame != null){
			if(GameManager.currentGame.participants.contains(player)) { 
				GameManager.currentGame.participants.remove(player);
			}
			
			GameManager.CheckPlayerAmount(false);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		GameManager.CheckPlayerAmount(false);
	}
}
