package net.kaoslabs.launchme;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.Listener;

public class LandingEntityListener implements Listener {
	public static LaunchMe plugin;

	public LandingEntityListener(LaunchMe instance) {
		plugin = instance;
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		DamageCause c = event.getCause();
		Entity e = event.getEntity();

		if (e instanceof Player) 
		{
			Player p = (Player) e;
			event.getCause();
			if (c.equals(DamageCause.FALL)) 
			{
				Boolean derp = plugin.getPlayerFromSet(p);
				if(derp == true) {
					event.setCancelled(true);
					plugin.removeFromSet(p);
				}
			}
		}
	}
}