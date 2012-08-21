package net.kaoslabs.launchme;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
	public static LaunchMe plugin;

	public SignListener(LaunchMe instance) {
		plugin = instance;
	}
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		FileConfiguration config = plugin.getConfig();
		Player player = event.getPlayer();
		String line1 = event.getLine(0);
		if (line1.equalsIgnoreCase("[cannon]")) {
			if (event.getPlayer().hasPermission("launchme.cannon")) {
				try {
					Double.parseDouble(event.getLine(1));
					Double.parseDouble(event.getLine(2));	
				} 
				catch(NumberFormatException badint) 
				{
					player.sendMessage(ChatColor.RED + config.getString("messages.cannon.badcoords"));
					event.setCancelled(true);
					return;
				}	
				if(plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
					try {
						Double.parseDouble(event.getLine(3));	
					} 
					catch(NumberFormatException badint) 
					{
						player.sendMessage(ChatColor.GOLD + config.getString("messages.cannon.noprice"));
						return;
					}	
				}
				player.sendMessage(config.getString("messages.cannon.success"));
				return;
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + config.getString("messages.cannon.nocreate"));
			}


		} else if (line1.equalsIgnoreCase("[teleport]")) {
			if (event.getPlayer().hasPermission("launchme.teleporter")) {
				try {
					Double.parseDouble(event.getLine(1));
					Double.parseDouble(event.getLine(2));	
					Double.parseDouble(event.getLine(3));
				} 
				catch(NumberFormatException badteleint) 
				{
					player.sendMessage(ChatColor.RED + config.getString("messages.teleport.badcoords"));
					event.setCancelled(true);
					return;
				}	
				player.sendMessage(ChatColor.GREEN + config.getString("messages.teleport.success"));
				return;
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + config.getString("messages.teleport.nocreate"));
			}

		}

	}

}
