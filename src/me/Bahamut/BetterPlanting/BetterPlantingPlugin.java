package me.Bahamut.BetterPlanting;

/**
 * Created by Yun on 3/5/2015.
 */

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterPlantingPlugin extends JavaPlugin
{
    public static BetterPlantingLogger log;

    // Called when the server is being disabled.
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    public void onEnable ()
    {
        log = new BetterPlantingLogger (this);

        // Register the events.
        getServer().getPluginManager().registerEvents(new BetterPlantingBlockListener(this), this);
        log.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");
    }

    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("ping"))
        {
            sender.sendMessage("pong!");
            return true;
        }
        return false;
    }

    /*
        TODO: Make configuration files.
     */
}
