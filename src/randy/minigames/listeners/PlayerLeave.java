package randy.minigames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import randy.minigames.GameManager;
import core.Custody.CustodySwitchEvent;

public class PlayerLeave implements Listener
{
	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		GameManager.RemovePlayer(event.getPlayer());
	}
}
