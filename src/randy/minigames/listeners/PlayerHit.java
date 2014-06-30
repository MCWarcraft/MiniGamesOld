package randy.minigames.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import randy.minigames.GameManager;
import randy.minigames.main;
import randy.minigames.GameManager.GameState;

public class PlayerHit implements Listener
{
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event)
	{
		if(event.getEntity().getType() == EntityType.PLAYER)
		{
			Player player = (Player)event.getEntity();
			if(main.minigamePlayers.contains(player))
			{
				if(GameManager.currentState == GameState.PreGame)
				{
					event.setCancelled(true);
				}
			}
		}
	}
}
