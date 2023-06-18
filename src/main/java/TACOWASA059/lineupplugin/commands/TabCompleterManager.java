package TACOWASA059.lineupplugin.commands;

import TACOWASA059.lineupplugin.LineUpPlugin;
import TACOWASA059.lineupplugin.utils.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.slf4j.MDC.remove;

public class TabCompleterManager implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String>list=new ArrayList<>();
        if(args.length==1){
            if(sender instanceof Player){
                Player player=(Player) sender;
                if(player.isOp()){
                    list.add("start");
                    list.add("stop");
                    list.add("reset");
                    list.add("showTeamInfo");
                    list.add("usage");
                    list.add("SetLineType");
                    list.add("SetDistance");
                    list.add("showConfig");
                }
                list.add("give");
            }
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("give")){
            if(sender instanceof Player){
                Player player=(Player) sender;
                for(Team team:Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
                    if(team.hasEntry(player.getName())){
                        List<Player> players=TeamManager.getOnlinePlayerList(team.getEntries());
                        for(Player player1:players){
                            if(player1!=player) list.add(player1.getName());
                        }
                    }
                }
            }
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("SetLineType")){
            list.add("perspective");
            list.add("lateral");
        }
        return list;
    }
}
