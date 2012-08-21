package net.kaoslabs.launchme;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.Listener;

public class KickListener implements Listener {
	public static LaunchMe plugin;

	public KickListener(LaunchMe instance) {
		plugin = instance;
	}
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e)
	{
		if(e.getReason().startsWith("You moved too") || e.getReason().startsWith("Flying is not"))
		{
			Boolean p = plugin.getPlayerFromSet(e.getPlayer());
			if(p == true)
			{        				
				e.setCancelled(true);        			
				}        	
			}        	
		}
	}

				
