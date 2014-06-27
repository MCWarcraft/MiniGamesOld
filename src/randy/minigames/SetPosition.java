package randy.minigames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import randy.gametypes.MiniGame;
import randy.gametypes.MiniGameKOTH;
import randy.gametypes.MiniGameLMS;

public class SetPosition implements Listener {
	
	public static Player stickPlayer;
	public static int step = 0;
	public static String positiontype;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(stickPlayer != null && stickPlayer == player){
				
				//Stick in hand?
				if(player.getItemInHand().getType() == Material.STICK){
					
					if(step == 0){
						Location blockLoc = event.getClickedBlock().getLocation();
						Location newLoc = new Location(blockLoc.getWorld(), blockLoc.getBlockX(), blockLoc.getBlockY() + 1, blockLoc.getBlockZ());
						
						if(positiontype.equals("koth_lobby")){
							MiniGameKOTH.lobbyLocation = newLoc;
							stickPlayer.sendMessage(ChatColor.GREEN + "KOTH lobby position set, right click in the direction you want the player to face when teleporting.");
							step++;
						} else if(positiontype.equals("koth_top")){
							MiniGameKOTH.topLoc = newLoc;
							stickPlayer.sendMessage(ChatColor.GREEN + "KOTH top position set.");
							stickPlayer = null;
						} else if(positiontype.equals("minigame_lobby")){
							MiniGame.minigameLobbyLocation = newLoc;
							stickPlayer.sendMessage(ChatColor.GREEN + "Minigame lobby position set, right click in the direction you want the player to face when teleporting.");
							step++;
						} else if(positiontype.equals("lms_lobby")){
							MiniGameLMS.lobbyLocation = newLoc;
							stickPlayer.sendMessage(ChatColor.GREEN + "LMS lobby position set, right click in the direction you want the player to face when teleporting.");
							step++;
						}
					}else if(step == 1){
						
						if(positiontype.equals("koth_lobby")){
							MiniGameKOTH.lobbyLocation = new Location(player.getWorld(), MiniGameKOTH.lobbyLocation.getBlockX(),  MiniGameKOTH.lobbyLocation.getBlockY(), MiniGameKOTH.lobbyLocation.getBlockZ(), player.getLocation().getYaw(), 0);
							stickPlayer.sendMessage(ChatColor.GREEN + "KOTH lobby position and rotation set.");
						} else if(positiontype.equals("minigame_lobby")){
							MiniGame.minigameLobbyLocation = new Location(player.getWorld(), MiniGame.minigameLobbyLocation.getBlockX(),  MiniGame.minigameLobbyLocation.getBlockY(), MiniGame.minigameLobbyLocation.getBlockZ(), player.getLocation().getYaw(), 0);;
							stickPlayer.sendMessage(ChatColor.GREEN + "Minigame lobby position and rotation set.");
						} else if(positiontype.equals("lms_lobby")){
							MiniGameLMS.lobbyLocation = new Location(player.getWorld(), MiniGameLMS.lobbyLocation.getBlockX(),  MiniGameLMS.lobbyLocation.getBlockY(), MiniGameLMS.lobbyLocation.getBlockZ(), player.getLocation().getYaw(), 0);
							stickPlayer.sendMessage(ChatColor.GREEN + "LMS lobby position and rotation set.");
						}
						
						stickPlayer = null;
						step--;
					}
				}
			}
		}
	}
	
}
