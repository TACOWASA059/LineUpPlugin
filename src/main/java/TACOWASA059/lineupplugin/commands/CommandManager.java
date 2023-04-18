package TACOWASA059.lineupplugin.commands;

import TACOWASA059.lineupplugin.LineUpPlugin;
import TACOWASA059.lineupplugin.utils.LineUpSetting;
import TACOWASA059.lineupplugin.utils.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CommandManager implements CommandExecutor {
    private TeamManager teamManager;
    private LineUpSetting lineUpSetting;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player=(Player) sender;
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED+"このコマンドを実行する権限がありません");
            }
            if(args.length==1){
                if(args[0].equalsIgnoreCase("START")){
                    LineUpPlugin.plugin.run=true;
                    lineUpSetting =new LineUpSetting(teamManager);
                    player.sendMessage(ChatColor.GREEN+"プラグインが有効化されました");
                }else if(args[0].equalsIgnoreCase("STOP")){
                    LineUpPlugin.plugin.run=false;
                    lineUpSetting.reset();
                    lineUpSetting=null;
                    player.sendMessage(ChatColor.GREEN+"プラグインが無効化されました");
                }else if(args[0].equalsIgnoreCase("showTeamInfo")){
                    if(teamManager==null){
                        Scoreboard scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
                        Set<Team> teams=scoreboard.getTeams();
                        if(teams.size()==0){
                            player.sendMessage(ChatColor.RED+"先に/lup resetを実行してください");
                            return true;
                        }
                        else{
                            int num=teams.size();
                            teamManager=new TeamManager(num);
                            teamManager.set();
                        }
                    }
                    int i=0;
                    player.sendMessage(ChatColor.AQUA+"----------------------------");
                    for(Team team:teamManager.teams){
                        player.sendMessage(team.getColor()+team.getName()+ChatColor.WHITE+":"+ChatColor.GREEN+team.getEntries().size()+"人");
                        if(teamManager.ref_Player[i]!=null) player.sendMessage(ChatColor.GREEN+"基準プレイヤー : "+teamManager.ref_Player[i].getName());
                        else player.sendMessage(ChatColor.GREEN+"基準プレイヤー : "+"なし");
                        i++;
                    }
                    player.sendMessage(ChatColor.AQUA+"----------------------------");
                }
                else if(args[0].equalsIgnoreCase("set")){
                    Scoreboard scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
                    Set<Team> teams=scoreboard.getTeams();
                    if(teams.size()==0) player.sendMessage(ChatColor.RED+"先に/lup resetを実行してください");
                    else{
                        int num=teams.size();
                        teamManager=new TeamManager(num);
                        teamManager.set();
                    }
                }
            }
            else if(args.length==2){
                if(args[0].equalsIgnoreCase("reset")){
                    Integer integer;
                    try{
                        integer=Integer.parseInt(args[1]);
                    }catch (NumberFormatException e){
                        player.sendMessage(ChatColor.RED+"チーム数を入力してください.");
                        player.sendMessage(ChatColor.AQUA+"(ex) /lup reset 5");
                        return true;
                    }
                    if(integer<=0||integer>8){
                        player.sendMessage(ChatColor.RED+"チーム数は1から8を指定してください");
                    }
                    teamManager=new TeamManager(integer);
                    teamManager.init();
                    teamManager.random_team_join();
                }
            }
        }
        return true;
    }
}
