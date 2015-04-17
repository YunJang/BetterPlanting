package me.Bahamut.BetterPlanting;

/**
 * Created by Yun on 3/5/2015.
 */

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.lang.String;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class BetterPlantingPlugin extends JavaPlugin
{
    public static BetterPlantingLogger log;
    public Hashtable<Material, Integer> Tools = new Hashtable<Material, Integer>();
    public Hashtable<Material, Material> CropToSeed = new Hashtable<Material, Material>();
    protected Vector<Material> PlantableCrops = new Vector<Material>();

    // Called when the server is being disabled.
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    public void onEnable ()
    {
        log = new BetterPlantingLogger (this);

        File file = new File(this.getDataFolder(),"config.yml");
        if(!file.exists()){
            saveDefaultConfig();
        }

        loadConfig();

        // Register the events.
        getServer().getPluginManager().registerEvents(new BetterPlantingBlockListener(this), this);
        log.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");
    }

    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("author"))
        {
            sender.sendMessage("Made by Yun - https://github.com/YunJang/BetterPlanting");
            return true;
        }
        return false;
    }

    /*
        Reads in config file for crops we can plant and those that are set to
        be planted, Tools usable with this mod, and CropToSeed helper structure
     */
    void loadConfig(){
        FileConfiguration fc = getConfig();

        //Get list of Plantable crops editable by players
        List<String> plantableCrops = fc.getStringList("PlantableCrops");
        for(String plantName : plantableCrops){
            //TODO error check against misspelled Materials form config
            PlantableCrops.add(Material.getMaterial(plantName));
        }

        //Get usable tools and their respective plant size
        final String tool = "BetterPlantableTools.tool";
        int i = 0;
        while(fc.contains(tool + ++i)){
            Tools.put(Material.getMaterial(fc.getString(tool + i + ".Material")),
                      fc.getInt(tool + i + ".PlantSize"));
        }

        //Get Crop to seed conversions
        final String crop = "CropToSeed.crop";
        i = 0;
        while(fc.contains(crop + ++i)){
            List currentCrop = fc.getList(crop+i);
            CropToSeed.put(Material.getMaterial((String)currentCrop.get(0)),
                           Material.getMaterial((String)currentCrop.get(1)));
        }
    }
}
