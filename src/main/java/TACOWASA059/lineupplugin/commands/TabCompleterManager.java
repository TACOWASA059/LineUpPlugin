package TACOWASA059.lineupplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterManager implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String>list=new ArrayList<>();
        if(args.length==1){
            list.add("start");
            list.add("stop");
            list.add("set");
            list.add("reset");
            list.add("showTeamInfo");
        }
        return list;
    }
}
