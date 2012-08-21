package net.kaoslabs.launchme;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class LaunchMe extends JavaPlugin {

	public static LaunchMe plugin;
	public Set<Player> launched = new HashSet<Player>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public final LandingEntityListener cannonListener = new LandingEntityListener(this);
	public final SignListener signListener = new SignListener(this);
	public final KickListener kickListener = new KickListener(this);
    public static Economy economy = null;
	
	public void onDisable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled!");
		this.getServer().getScheduler().cancelAllTasks();
	}

	public void onEnable() {

		PluginDescriptionFile pdfFile = this.getDescription();
	    setupConfig();
	    setupDefaultCannonMessages();
	    setupDefaultTeleporterMessages();
		FileConfiguration config = this.getConfig();
		launched.clear();
		
		if (config.getBoolean("enable", false)) {
			logger.info("[" + pdfFile.getName() + "] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled!");
			registerListeners();
			registerCommands();
			if(this.getServer().getPluginManager().getPlugin("Vault") != null) 
			{
			setupEconomy();
			}
		} 
		else 
		{
			logger.info("[" + pdfFile.getName() + "] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled by config.yml!");
			this.getPluginLoader().disablePlugin(this);
		}
		logger.setFilter(new Filter()
        {
         public boolean isLoggable(LogRecord record) {
         if(record.getMessage() != null)
         {
         if(record.getMessage().endsWith("was kicked for floating too long!"))
         {
         return false;
         }
         }
         return true;
}
        
        });
		this.saveConfig();
	}
	public void setupConfig()
	{			
		FileConfiguration config = this.getConfig();
		if (config.get("enable") == null) 
		{
			this.getConfig().set("enable", true);
		}
		if (config.get("teleporterblock") == null) 
		{
			this.getConfig().set("teleporterblock", 22);
		}
		if (config.get("cannonblock") == null) 
		{
			this.getConfig().set("cannonblock", 22);
		}
	}
	public void setupDefaultCannonMessages() 
	{
		FileConfiguration config = this.getConfig();
		if (config.get("messages.cannon.nocreate") == null) 
		{
			this.getConfig().set("messages.cannon.nocreate", "You do not have the permission to create cannon signs!");
		}
		if (config.get("messages.cannon.success") == null) 
		{
			this.getConfig().set("messages.cannon.success", "Cannon creation successful!");
		}
		if (config.get("messages.cannon.badcoords") == null) 
		{
			this.getConfig().set("messages.cannon.badcoords", "Check your coordinates, are you sure they are numbers?");
		}
		if (config.get("messages.cannon.noprice") == null) 
		{
			this.getConfig().set("messages.cannon.noprice", "Cannon creation successful; no price set.");
		}
		if (config.get("messages.cannon.nocannon") == null) 
		{
			this.getConfig().set("messages.cannon.nocannon", "You need to be on a cannon to do this!");
		}
		if (config.get("messages.cannon.nolaunch") == null) 
		{
			this.getConfig().set("messages.cannon.nolaunch", "You don't have the permission to launch!");
		}
		if (config.get("messages.cannon.ingame") == null) 
		{
			this.getConfig().set("messages.cannon.ingame", "Must be done in-game.");
		}
		if (config.get("messages.cannon.price") == null) 
		{
			this.getConfig().set("messages.cannon.price", "removed from your funds to use the cannon!");
		}
		if (config.get("messages.cannon.notcharged") == null) 
		{
			this.getConfig().set("messages.cannon.notcharged", "Not charged to use this cannon!");
		}
	}
	public void setupDefaultTeleporterMessages() 
	{
		FileConfiguration config = this.getConfig();
		if (config.get("messages.teleport.nocreate") == null) 
		{
			this.getConfig().set("messages.teleport.nocreate", "You do not have the permission to create teleporter signs!");
		}
		if (config.get("messages.teleport.success") == null) 
		{
			this.getConfig().set("messages.teleport.success", "Teleporter creation successful!");
		}
		if (config.get("messages.teleport.badcoords") == null) 
		{
			this.getConfig().set("messages.teleport.badcoords", "Check your coordinates, are you sure they are numbers?");
		}
		if (config.get("messages.teleport.noteleporter") == null) 
		{
			this.getConfig().set("messages.teleport.noteleporter", "You need to be on a teleport to do this!");
		}
		if (config.get("messages.teleport.noteleport") == null) 
		{
			this.getConfig().set("messages.teleport.noteleport", "You don't have the permission to teleport!");
		}
		if (config.get("messages.teleport.ingame") == null) 
		{
			this.getConfig().set("messages.teleport.ingame", "Must be done in-game.");
		}
	}
	
	public void registerListeners() 
	{		
		PluginManager pm = getServer().getPluginManager();
		//pm.registerEvent(Event.Type.SIGN_CHANGE, signListener,Event.Priority.Normal, this);
		//pm.registerEvent(Event.Type.ENTITY_DAMAGE, cannonListener,Event.Priority.Normal, this);
		//pm.registerEvent(Event.Type.PLAYER_KICK, kickListener,Event.Priority.Normal, this);
		pm.registerEvents(this.signListener, this);
		pm.registerEvents(this.cannonListener, this);
		pm.registerEvents(this.kickListener, this);
	}
		
	public void registerCommands() 
	{
		this.getCommand("launch").setExecutor(new CannonLaunch(this));
		this.getCommand("tele").setExecutor(new CannonTeleport(this));
	}
		
	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
      if (economyProvider != null) 
        {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	public void addToSet(Player p) 
	{
		launched.add(p);
	}
	public void removeFromSet(Player p) 
	{
		launched.remove(p);
	}
    public Boolean getPlayerFromSet(Player p)    
    { 
    	return launched.contains(p);    
	}
}


