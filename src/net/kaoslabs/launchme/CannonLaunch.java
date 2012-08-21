package net.kaoslabs.launchme;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.World;

public class CannonLaunch implements CommandExecutor {

	private LaunchMe plugin;

	public CannonLaunch(LaunchMe plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{
		FileConfiguration config = plugin.getConfig();
		
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			Location trigger = player.getLocation();
			World w = trigger.getWorld();
			trigger.setY(trigger.getY() - 1);
			Block b = w.getBlockAt(trigger);
			int l = b.getTypeId();
			int lcomp = plugin.getConfig().getInt("cannonblock", 22);

			if (player.hasPermission("launchme.launch")) 
			{
				if(l == lcomp) {
					
				Location trigger2 = trigger;
				World ws = trigger2.getWorld();
				trigger2.setY(trigger.getY() - 1);
				Block s = ws.getBlockAt(trigger2);
				Material sm = s.getType();
				
				if (sm.equals(Material.SIGN_POST) || sm.equals(Material.WALL_SIGN)) {
					Sign sign = (Sign) trigger2.getBlock().getState();
					String launchop = sign.getLine(0);

						if (launchop.equalsIgnoreCase("[cannon]")) {
							String coordX = sign.getLine(1);
							String coordZ = sign.getLine(2);

							double x = Double.parseDouble(coordX);
							double z = Double.parseDouble(coordZ);				
							
							float yaw = player.getLocation().getYaw();
							float pitch = player.getLocation().getPitch();

							final Location end = new Location(ws, x, 400, z, yaw, pitch);
							Plugin Spout = plugin.getServer().getPluginManager().getPlugin("Spout");
							final Vector v = new Vector(0, 10, 0);
							if (Spout != null) {
								v.setY(5);
							}

							if(plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
								String charge = player.getName();
								try {
								Double price = Double.parseDouble(sign.getLine(3));
								LaunchMe.economy.withdrawPlayer(charge, price);
								player.sendMessage(ChatColor.LIGHT_PURPLE + "$" + price + "0 " + config.getString("messages.cannon.price"));
								}
								catch(Exception badex) 
								{
									player.sendMessage(ChatColor.GOLD + config.getString("messages.cannon.notcharged"));
								}
								
								}
							player.sendMessage(ChatColor.GREEN + "Whoosh!");
							plugin.addToSet(player);

							final int nyan = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin,new Runnable() 
							{
								public void run() 
								{
									player.setVelocity(v);
								}
							}, 5, 5);

							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
								public void run() 
								{
									player.teleport(end);
									plugin.getServer().getScheduler().cancelTask(nyan);
								}// end of run
							}, 100); // end of teleport
						}// end of launchop stage
						return true;
					}// end of sign stage
				} 
				else 
				{
					player.sendMessage(ChatColor.RED + config.getString("messages.cannon.nocannon"));// end of block stage
					return true;
				}
			} 
			else 
			{
				player.sendMessage(ChatColor.RED + config.getString("messages.cannon.nolaunch"));// end of permissions check
				return true;
			}
		}// end of player stage
		else 
		{
			sender.sendMessage(config.getString("messages.cannon.ingame"));
			return true;
		} // end of else player stage
		return false;
	} // end of boolean oncommand
}// end

