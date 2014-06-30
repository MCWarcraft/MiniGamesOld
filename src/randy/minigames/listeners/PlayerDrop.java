package randy.minigames.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDrop implements Listener
{
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		
		
		if(randy.minigames.GameManager.currentGame != null && randy.minigames.GameManager.currentGame.participants.contains(player) || randy.minigames.main.minigamePlayers.contains(player)){
			ItemStack droppedItem = event.getItemDrop().getItemStack();
			if(droppedItem.getType() == Material.MUSHROOM_SOUP ||
					droppedItem.getType() == Material.BOWL){
				event.getItemDrop().remove();
			}else if(droppedItem.getType().toString().contains("SWORD") ||
					droppedItem.getType().toString().contains("AXE") ||
					droppedItem.getType().toString().contains("BOW") ||
					droppedItem.getType().toString().contains("ARROW") ||
					droppedItem.getType().toString().contains("COMPASS") ||
					droppedItem.getType().toString().contains("WATCH") ||
					droppedItem.getType().toString().contains("NETHER_STAR")){
				event.setCancelled(true);
			}
		}
	}
}
