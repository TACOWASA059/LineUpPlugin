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

import java.util.List;
import java.util.Set;

public class CommandManager implements CommandExecutor {
    private TeamManager teamManager;
    private LineUpSetting lineUpSetting;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("START")) {
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                    }
                    if(teamManager==null)teamManager=new TeamManager();
                    for(Team team:Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
                        String name=team.getName();
                        String playername=LineUpPlugin.plugin.getConfig().getString(name);
                        if(playername==null)continue;
                        Player player1=Bukkit.getPlayer(playername);
                        player1.sendTitle("あなたが基準プレイヤーです","",1,10,1);
                    }
                    LineUpPlugin.plugin.run = true;
                    lineUpSetting = new LineUpSetting(teamManager);
                    player.sendMessage(ChatColor.GREEN + "プラグインが有効化されました");
                } else if (args[0].equalsIgnoreCase("STOP")) {
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                    }
                    LineUpPlugin.plugin.run = false;
                    lineUpSetting.reset();
                    lineUpSetting = null;
                    player.sendMessage(ChatColor.GREEN + "プラグインが無効化されました");
                }
                else if (args[0].equalsIgnoreCase("showTeamInfo")) {//全チームの割り当てを確認
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                }
                if (teamManager == null) {
                    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                    Set<Team> teams = scoreboard.getTeams();
                    if (teams.size() == 0) {
                        player.sendMessage(ChatColor.RED + "先に/lup reset <num>を実行してください");
                        return true;
                    } else {//既存のチームを設定する
                        teamManager = new TeamManager();
                    }
                }
                player.sendMessage(ChatColor.AQUA + "----------------------------");
                for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                    List<Player> list=teamManager.getOnlinePlayerList(team.getEntries());
                    player.sendMessage(team.getColor() + team.getName() + ChatColor.WHITE + ":" + ChatColor.GREEN + list.size() + "人");
                    String ref_name=LineUpPlugin.plugin.getConfig().getString(team.getName());
                    if (ref_name != null)
                        player.sendMessage(ChatColor.GREEN + "基準プレイヤー : " + ref_name);
                    else player.sendMessage(ChatColor.GREEN + "基準プレイヤー : " + "なし");
                }
                player.sendMessage(ChatColor.AQUA + "----------------------------");
            }
                else if(args[0].equalsIgnoreCase("showConfig")){
                    player.sendMessage(ChatColor.AQUA + "----------------------------");
                    if(LineUpPlugin.plugin.getConfig().getInt("LineType")==0){
                        player.sendMessage(ChatColor.GREEN+"LineType : "+ChatColor.AQUA+"perspective");
                    }else if(LineUpPlugin.plugin.getConfig().getInt("LineType")==1) {
                        player.sendMessage(ChatColor.GREEN+"LineType : "+ChatColor.AQUA+"lateral");
                    }
                    player.sendMessage(ChatColor.GREEN+"Distance : "+ChatColor.AQUA+LineUpPlugin.plugin.getConfig().getDouble("Distance"));
                    player.sendMessage(ChatColor.AQUA + "----------------------------");
                }
              else if(args[0].equalsIgnoreCase("usage")){
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                    }
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"----------------------");
                    player.sendMessage(ChatColor.AQUA+"OP権限ありのみ実行可");
                    player.sendMessage(ChatColor.GREEN+"/lup start : "+ChatColor.AQUA+"プラグインを有効化");
                    player.sendMessage(ChatColor.GREEN+"/lup stop : "+ChatColor.AQUA+"プラグインを無効化");
                    player.sendMessage(ChatColor.GREEN+"/lup showTeamInfo : "+ChatColor.AQUA+"全チームの情報を表示");
                    player.sendMessage(ChatColor.GREEN+"/lup reset <num> : "+ChatColor.AQUA+"チームをリセットし<num>個のチームを作成し直す");
                    player.sendMessage(ChatColor.GREEN+"/lup setLineType perspective/lateral : "+ChatColor.AQUA+"並び方が視点方向か横方向かを設定");
                    player.sendMessage(ChatColor.GREEN+"/lup setDistance <value> : "+ChatColor.AQUA+"二人の間の距離感を設定");
                    player.sendMessage(ChatColor.GREEN+"/lup showConfig : "+ChatColor.AQUA+"コンフィグの値を表示");
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"----------------------");
                    player.sendMessage(ChatColor.AQUA+"OP権限なしでも実行可");
                    player.sendMessage(ChatColor.GREEN+"/lup give <MCID> : "+ChatColor.AQUA+"同じチームの相手に基準プレイヤーを譲る");
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"----------------------");
                }
            }
            else if(args.length==2){//チームをリセットする
                if(args[0].equalsIgnoreCase("reset")){
                    Integer integer;
                    try{
                        integer=Integer.parseInt(args[1]);
                    }catch (NumberFormatException e){
                        player.sendMessage(ChatColor.RED+"チーム数を入力してください.");
                        player.sendMessage(ChatColor.AQUA+"(example) /lup reset 5");
                        return true;
                    }
                    if(integer<=0||integer>10){
                        player.sendMessage(ChatColor.RED+"チーム数は1から10を指定してください");
                    }
                    teamManager=new TeamManager(integer);
                    teamManager.random_team_join();
                }
                else if(args[0].equalsIgnoreCase("give")){
                    Team team0=null;
                    for(Team team:Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                        String team_name=team.getName();
                        String ref_player=LineUpPlugin.plugin.getConfig().getString(team_name);
                        if(ref_player==null){
                            continue;
                        }
                        if(ref_player.equalsIgnoreCase(player.getName())) {
                            team0=team;
                            break;
                        }
                    }
                    if(team0==null) {
                        player.sendMessage(ChatColor.RED+"あなたは現在基準プレイヤーではありません。");
                        return true;
                    }
                    String s=args[1];
                    Player player1=Bukkit.getPlayer(s);
                    if(player1==null){
                        player.sendMessage(ChatColor.RED+"指定されたプレイヤーは存在しません。");
                        return true;
                    }
                    List<Player> playerlist=teamManager.getOnlinePlayerList(team0.getEntries());
                    if(playerlist.contains(player1)) {
                        player1.sendMessage(ChatColor.GREEN+"あなたは基準プレイヤーに設定されました。");
                        player.sendMessage(ChatColor.GREEN+player1.getName()+"に基準プレイヤーを譲りました。");
                        teamManager.allocate_player(player1);
                    }
                    else{
                        player.sendMessage(ChatColor.RED+player1.getName()+"に基準プレイヤーを譲ることはできません。");
                    }
                }
                else if(args[0].equalsIgnoreCase("SetLineType")){
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                    }
                    if(args[1].equalsIgnoreCase("perspective")){
                        LineUpPlugin.plugin.getConfig().set("LineType",0);
                        LineUpPlugin.plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN+"視線方向に変更されました。");
                    }
                    else if (args[1].equalsIgnoreCase("lateral")){
                        LineUpPlugin.plugin.getConfig().set("LineType",1);
                        LineUpPlugin.plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN+"横方向に変更されました。");
                    }
                }else if(args[0].equalsIgnoreCase("SetDistance")){
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "このコマンドを実行する権限がありません");
                    }
                    String s=args[1];
                    try{
                        Double d=Double.parseDouble(s);
                        LineUpPlugin.plugin.getConfig().set("Distance",d);
                        LineUpPlugin.plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN+ "変更後の値:"+LineUpPlugin.plugin.getConfig().getDouble("Distance"));
                    }catch (NumberFormatException e){
                        player.sendMessage(ChatColor.RED+"数値を入力してください。");
                        return true;
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.RED+"コマンドが間違っています。");
            }


            }

        return true;
    }
}
