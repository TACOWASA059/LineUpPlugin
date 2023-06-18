package TACOWASA059.lineupplugin.utils;

import TACOWASA059.lineupplugin.LineUpPlugin;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.sound.sampled.Line;
import java.io.File;
import java.util.*;

import static TACOWASA059.lineupplugin.LineUpPlugin.plugin;

public class TeamManager {
    private final List<NamedTextColor> colorList= Arrays.asList(
            NamedTextColor.RED,
            NamedTextColor.BLUE,
            NamedTextColor.YELLOW,
            NamedTextColor.GREEN,
            NamedTextColor.AQUA,
            NamedTextColor.DARK_GREEN,
            NamedTextColor.BLACK,
            NamedTextColor.GRAY,
            NamedTextColor.GOLD,
            NamedTextColor.LIGHT_PURPLE
    );
    public TeamManager(){

    }
    public TeamManager(int num){
        init(num);
    }
    //初期化してチームを作る
    public void init(int team_num){
        Scoreboard scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
        Set<Team> teams = scoreboard.getTeams();
        for(Team team:teams)team.unregister();

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        configFile.delete();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        for(int i=0;i<team_num;i++){
            Team team=scoreboard.registerNewTeam(colorList.get(i).toString());
            team.color(colorList.get(i));
        }
    }
    public void random_team_join(){
        // オンラインのプレイヤーを取得する
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        // 各チームにプレイヤーを割り当てる
        List<Team> teams=new ArrayList<>(Bukkit.getScoreboardManager().getMainScoreboard().getTeams());
        int team_num=teams.size();
        int i = 0;
        for (Player p : onlinePlayers) {
            teams.get(i % team_num).addEntry(p.getName());
            p.sendMessage(teams.get(i%team_num).getColor()+ teams.get(i%team_num).getName()+ChatColor.GREEN+"に割り振られました。");
            i++;
        }
        for(Team team:teams){
            List<Player> onlineplayerlist=getOnlinePlayerList(team.getEntries());
            if(onlineplayerlist.size()==0)continue;
            allocate_player(onlineplayerlist.get(0));
        }
    }
    //基準プレイヤーの割り当て
    public void allocate_player(Player player){
        List<Team> teams=new ArrayList<>(Bukkit.getScoreboardManager().getMainScoreboard().getTeams());
        if(player==null)return;
        int team_id=getPlayerTeam(player);
        if(team_id!=-1) {
            plugin.getConfig().set(teams.get(team_id).getName(),player.getName());
            plugin.saveConfig();
            player.sendTitle("あなたが基準プレイヤーです","",10,20,10);
        };
    }
    //基準プレイヤーが全体の何番目かを返す
    public Integer getNumber(int team_number){
        List<Team> teams=new ArrayList<>(Bukkit.getScoreboardManager().getMainScoreboard().getTeams());
        Team team=teams.get(team_number);
        List<Player> list=getOnlinePlayerList(team.getEntries());
        String playername= plugin.getConfig().getString(team.getName());
        if(playername==null){//登録されていない状況
            if(list.size()==0)return -1;//チームにそもそも誰もいない
            allocate_player(list.get(0));
            return 0;
        }
        else{//登録はされている
            Player player=Bukkit.getPlayer(playername);
            if(player!=null&&player.isOnline()){//オンラインのプレイヤーかどうか
                Integer j=0;
                for(Player player1:list){
                    if(player1.equals(player)){
                        return j;
                    }
                    j++;
                }
                allocate_player(list.get(0));
                return 0;
            }
            else{//オンラインでないとき
                allocate_player(list.get(0));
                return 0;
            }
        }
    }
    //プレイヤーが所属しているチーム番号を返す
    public int getPlayerTeam(Player player) {
        List<Team> teams=new ArrayList<>(Bukkit.getScoreboardManager().getMainScoreboard().getTeams());
        int team_num=teams.size();
        // 全てのチームを調べる
        for (int i=0;i<team_num;i++) {
            if (teams.get(i).hasEntry(player.getName())) {
                return i;
            }
        }
        // プレイヤーがどのチームにも所属していない場合は、-1を返す
        return -1;
    }
    public static List<Player> getOnlinePlayerList(Set<String> entries){
        List<Player> onlineplayerlist=new ArrayList<>();
        for(String name:entries){
            Player player=Bukkit.getPlayer(name);
            if(player!=null&&player.isOnline())onlineplayerlist.add(player);
        }
        return onlineplayerlist;
    }
}
