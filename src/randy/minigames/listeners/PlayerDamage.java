package randy.minigames.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import randy.minigames.GameManager;
import randy.minigames.GameManager.GameState;

public class PlayerDamage implements Listener {
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entEvent = (EntityDamageByEntityEvent)event;
			if(entEvent.getDamager() instanceof Player && entEvent.getEntity() instanceof Player){
				
				//Get information on player and target
				Player player1 = (Player)entEvent.getDamager();
				Player player2 = (Player)entEvent.getEntity();
				if(GameManager.currentGame != null){
					if((GameManager.currentGame.participants.contains(player1) || GameManager.currentGame.participants.contains(player2)) && 
							GameManager.currentState == GameState.PreGame){
						event.setCancelled(true);
					}
				}
			}			
		}
	}
}
