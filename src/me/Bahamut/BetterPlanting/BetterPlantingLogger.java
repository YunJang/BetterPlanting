package me.Bahamut.BetterPlanting;

/**
 * Created by Yun on 3/5/2015.
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterPlantingLogger
{
    public static final Logger logger = Logger.getLogger("Minecraft");
    private final BetterPlantingPlugin plugin;

    public BetterPlantingLogger (BetterPlantingPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void info (String s)
    {
        logger.log( Level.INFO, "[BetterPlanting] " + s);
    }

    public void severe (String s)
    {
        logger.log( Level.SEVERE, "[BetterPlanting] " + s);
    }

    public void warning (String s)
    {
        logger.log( Level.WARNING, "[BetterPlanting] " + s);
    }
}
