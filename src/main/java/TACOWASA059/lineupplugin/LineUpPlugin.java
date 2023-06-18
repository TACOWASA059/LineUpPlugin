package TACOWASA059.lineupplugin;

import TACOWASA059.lineupplugin.commands.CommandManager;
import TACOWASA059.lineupplugin.commands.TabCompleterManager;
import TACOWASA059.lineupplugin.listener.PlayerMoveListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LineUpPlugin extends JavaPlugin {
    static public LineUpPlugin plugin;
    public Boolean run = false;

    @Override
    public void onEnable() {
        plugin=this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        // Plugin startup logic
        getCommand("lup").setExecutor(new CommandManager());
        getCommand("lup").setTabCompleter(new TabCompleterManager());
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(),this);
    }
}
