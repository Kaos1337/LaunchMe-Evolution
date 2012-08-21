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
import org.bukkit.World;

public class CannonTeleport implements CommandExecutor {

	private LaunchMe plugin;

	public CannonTeleport(LaunchMe plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		FileConfiguration config = plugin.getConfig();

		if (sender instanceof Player) {
			final Player player = (Player) sender;
			Location trigger = player.getLocation();
			World w = trigger.getWorld();
			trigger.setY(trigger.getY() - 1);
			Block b = w.getBlockAt(trigger);
			int l = b.getTypeId();
			int lcomp = plugin.getConfig().getInt("teleporterblock", 22);

			if (player.hasPermission("launchme.teleport")) {
				if (l == lcomp) {
					Location trigger2 = trigger;
					World ws = trigger2.getWorld();
					trigger2.setY(trigger.getY() - 1);
					Block s = ws.getBlockAt(trigger2);
					Material sm = s.getType();

					if (sm.equals(Material.SIGN_POST)
							|| sm.equals(Material.WALL_SIGN)) {
						Sign sign = (Sign) trigger2.getBlock().getState();
						String launchop = sign.getLine(0);

						if (launchop.equalsIgnoreCase("[teleport]")) {
							String coordX = sign.getLine(1);
							String coordY = sign.getLine(2);
							String coordZ = sign.getLine(3);
							double x = Double.parseDouble(coordX);
							double y = Double.parseDouble(coordY);
							double z = Double.parseDouble(coordZ);
							float yaw = player.getLocation().getYaw();
							float pitch = player.getLocation().getPitch();

							final Location porter = new Location(ws, x, y, z, yaw, pitch);
							player.sendMessage(ChatColor.GREEN + "Whoomp!");
							player.teleport(porter);
						}// end of teleport stage

						return true;
					}// end of sign stage

				} else {
					player.sendMessage(ChatColor.RED + config.getString("messages.teleport.noteleporter"));
					return true;
				}// end of blockcheck stage

			} else {
				player.sendMessage(ChatColor.RED + config.getString("messages.teleport.noteleport"));// end
																				// of
																				// permissions
																				// check
				return true;
			}

		}// end of player stage
		else {
			sender.sendMessage(config.getString("messages.teleport.ingame"));
			return true;
		} // end of else player stage
		return false;
	} // end of boolean oncommand

}// end
