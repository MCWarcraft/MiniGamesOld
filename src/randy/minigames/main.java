package randy.minigames;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import randy.core.CoreAPI;
import randy.core.tools.CoreSQL;
import randy.gametypes.MiniGame;
import randy.kits.CustomKit;
import randy.kits.Kits;
import randy.minigames.GameManager.GameTypes;

public class main extends JavaPlugin{
	
	//Listeners
	private final SetPosition setPositionListener = new SetPosition();
	private final PlayerJoin playerJoinListener = new PlayerJoin();
	private final PlayerDeath playerDeathListener = new PlayerDeath();
	private final PlayerDamage playerDamageListener = new PlayerDamage();
	
	//Minigame players
	public static ArrayList<Player> minigamePlayers = new ArrayList<Player>();
	
	//Timer
	Timer timer = new Timer();
	TimerTask timerTask;
	
	@Override
	public void onDisable() {
		
		Config.SaveConfig();
		
		timer.cancel();		
		timerTask.cancel();
		Bukkit.getScheduler().cancelAllTasks();
		
		System.out.print("[Minigames] Succesfully disabled.");
	}
	
	@Override
	public void onEnable() {
		
		//Register events
		getServer().getPluginManager().registerEvents(setPositionListener, this);
		getServer().getPluginManager().registerEvents(playerJoinListener, this);
		getServer().getPluginManager().registerEvents(playerDeathListener, this);
		getServer().getPluginManager().registerEvents(playerDamageListener, this);
		
		Config.LoadConfig();
		
		startTimer();
		
		GameManager.CheckPlayerAmount(false);
		
		System.out.print("[Minigames] Succesfully enabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			/*
			 * MINI GAME
			 */
			if(commandName.equalsIgnoreCase("minigame")){
				if(args.length == 0){									
					CoreAPI.ExitGameModes(player);
					
					player.teleport(MiniGame.minigameLobbyLocation);
					
					//Clear inventory
					player.getInventory().clear();
					player.getInventory().setArmorContents(new ItemStack[4]);
					
					//Give compass
					ItemStack item = new ItemStack(Material.COMPASS, 1);
					ItemMeta itemMeta = item.getItemMeta();
					
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Right click to go");
					
					itemMeta.setDisplayName(""+ ChatColor.GREEN + ChatColor.BOLD + "Game Menu");
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					
					player.getInventory().addItem(item);
					
					minigamePlayers.add(player);
					return true;
				}
				
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("lobby") && args[1].equalsIgnoreCase("set")){
						if(player.hasPermission("Minigame.setlobby")){
							SetPosition.positiontype = "minigame_lobby";
							SetPosition.stickPlayer = player;
							
							player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the minigame lobby.");
							return true;
						}
					}
				}
			}
			
			/*
			 * KOTH GAME
			 */
			if(commandName.equalsIgnoreCase("koth")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("start")){
						GameManager.ForceStart(GameTypes.KOTH);
					}
					return true;
				}
				
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("lobby") && args[1].equalsIgnoreCase("set")){
						if(player.hasPermission("Minigame.kothsetlobby")){
							SetPosition.positiontype = "koth_lobby";
							SetPosition.stickPlayer = player;
							
							player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the KOTH lobby.");
						}
						return true;
					}
					
					if(args[0].equalsIgnoreCase("top") && args[1].equalsIgnoreCase("set")){
						if(player.hasPermission("Minigame.kothsettop")){
							SetPosition.positiontype = "koth_top";
							SetPosition.stickPlayer = player;
							
							player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the KOTH top.");
						}
						return true;
					}
					
					if(args[0].equalsIgnoreCase("kit")){
						String kitname = args[1];
						CustomKit kit = Kits.getKitByName(kitname, "koth");
						if(kit != null){
							if(!CoreSQL.GetKitLocked(player.getName(), kit)){
								kit.AssignKitToPlayer(player);
							}else{
								player.sendMessage(ChatColor.RED + "You do not own this kit! You can purchase it through the upgrade menu.");
							}
						}else{
							player.sendMessage(ChatColor.RED + "That kit doesn't exist.");
						}
						return true;
					}
					
					if(args[0].equalsIgnoreCase("purchase")){
						String kitname = args[1];
						CustomKit kit = Kits.getKitByName(kitname, "koth");
						if(kit != null){
							randy.kits.main.AttemptPurchase(player, kit);
						}else{
							player.sendMessage(ChatColor.RED + "That kit doesn't exist.");
						}
						return true;
					}
				}
			}
			
			/*
			 * LMS GAME
			 */
			if(commandName.equalsIgnoreCase("lms")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("start")){
						GameManager.ForceStart(GameTypes.LMS);
					}
					return true;
				}
				
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("lobby") && args[1].equalsIgnoreCase("set")){
						if(player.hasPermission("Minigame.lmssetlobby")){
							SetPosition.positiontype = "lms_lobby";
							SetPosition.stickPlayer = player;
						
							player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the LMS lobby.");
						}
						return true;
					}
					
					if(args[0].equalsIgnoreCase("kit")){
						String kitname = args[1];
						CustomKit kit = Kits.getKitByName(kitname, "koth");
						if(kit != null){
							if(!CoreSQL.GetKitLocked(player.getName(), kit)){
								kit.AssignKitToPlayer(player);
							}else{
								player.sendMessage(ChatColor.RED + "You do not own this kit! You can purchase it through the upgrade menu.");
							}
						}else{
							player.sendMessage(ChatColor.RED + "That kit doesn't exist.");
						}
						return true;
					}
					
					if(args[0].equalsIgnoreCase("purchase")){
						String kitname = args[1];
						CustomKit kit = Kits.getKitByName(kitname, "koth");
						if(kit != null){
							randy.kits.main.AttemptPurchase(player, kit);
						}else{
							player.sendMessage(ChatColor.RED + "That kit doesn't exist.");
						}
						return true;
					}
				}
			}
			
			/*
			 * JOIN
			 */
			if(commandName.equalsIgnoreCase("join")){
				if(GameManager.currentGame != null){
					if(!GameManager.currentGame.AddPlayer(player)){
						player.sendMessage(ChatColor.GOLD + "The " + ChatColor.RED + GameManager.currentGame.gameType.toString() + ChatColor.GOLD + " has already started!");
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	public static void broadcastMessage(String message){
		Player[] players = Bukkit.getOnlinePlayers();
		for(int i = 0; i < players.length; i++){
			players[i].sendMessage(message);
		}
	}
	
	private void startTimer(){

		//Start timer, triggers every second
		timerTask = new TimerTask() {
			public void run() {
				GameManager.UpdateCountdown();
			}
		};
		timer.schedule(timerTask, 100, 100);
	}
}
