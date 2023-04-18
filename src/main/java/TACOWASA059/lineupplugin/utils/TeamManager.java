package TACOWASA059.lineupplugin.utils;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamManager {
    private final List<NamedTextColor> colorList= Arrays.asList(
            NamedTextColor.RED,
            NamedTextColor.BLUE,
            NamedTextColor.YELLOW,
            NamedTextColor.GREEN,
            NamedTextColor.AQUA,
            NamedTextColor.DARK_PURPLE,
            NamedTextColor.BLACK,
            NamedTextColor.GRAY
            );

    private int team_num;
    public List<Team> teams;
    public Player[] ref_Player;
    //num:チーム数
    public TeamManager(int num){
        team_num=num;
        ref_Player=new Player[num];
    }
    //初期化
    public void init(){
        Scoreboard scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
        Set<Team> teams = scoreboard.getTeams();
        for(Team team:teams)team.unregister();

        this.teams =new ArrayList<>();
        for(int i=0;i<team_num;i++){
            this.teams.add(scoreboard.registerNewTeam(colorList.get(i).toString()));
            this.teams.get(i).color(colorList.get(i));
        }
    }
    //初期化せずに設定だけ
    public void set(){
        Scoreboard scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
        this.teams =new ArrayList<>();
        for(Team team:scoreboard.getTeams()){
            teams.add(team);
            if(team.getEntries().size()==0)continue;
            Player player=Bukkit.getPlayer(new ArrayList<String>(team.getEntries()).get(0));
            if(player!=null) allocate_player(player);
        };

    }
    public void random_team_join(){
        // オンラインのプレイヤーを取得する
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        // 各チームにプレイヤーを割り当てる
        int i = 0;
        for (Player p : onlinePlayers) {
            teams.get(i % team_num).addEntry(p.getName());
            p.sendMessage(teams.get(i%team_num).getColor()+ teams.get(i%team_num).getName()+ChatColor.GREEN+"に割り振られました。");
            i++;
        }

        for(Team team:teams){
            if(team.getEntries().size()==0)continue;
            String name=new ArrayList<>(team.getEntries()).get(0);
            Player player=Bukkit.getPlayer(name);
            allocate_player(player);
        }
    }
    public void allocate_player(Player player){
        int team_id=getPlayerTeam(player);
        ref_Player[team_id]=player;
    }
    private int getPlayerTeam(Player player) {
        // 全てのチームを調べる
        for (int i=0;i<team_num;i++) {
            // プレイヤーが所属しているチームを見つけたら、そのチームを返す
            if (teams.get(i).hasEntry(player.getName())) {
                return i;
            }
        }
        // プレイヤーがどのチームにも所属していない場合は、-1を返す
        return -1;
    }
}
