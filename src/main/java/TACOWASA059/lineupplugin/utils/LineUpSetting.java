package TACOWASA059.lineupplugin.utils;

import TACOWASA059.lineupplugin.LineUpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineUpSetting {
        private final TeamManager teamManager;
        int taskid;
        public LineUpSetting(TeamManager teamManager){
                this.teamManager=teamManager;
                taskid=Bukkit.getScheduler().scheduleSyncRepeatingTask(LineUpPlugin.plugin,()->{
                        int i=0;
                        for(Team team:Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
                                String team_name=team.getName();
                                int id= teamManager.getNumber(i);
                                if(id==-1)continue;
                                List<Player> list=teamManager.getOnlinePlayerList(team.getEntries());
                                Player player=list.get(id);
                                String playername=LineUpPlugin.plugin.getConfig().getString(team_name);
                                if(playername==null||!playername.equalsIgnoreCase(player.getName())){
                                        continue;
                                }

                                Vector perspective=player.getLocation().getDirection().clone();
                                perspective.normalize();
                                Vector perspective2=perspective.clone();
                                if(LineUpPlugin.plugin.getConfig().getInt("LineType")==1){
                                        double x=perspective.getX();
                                        double z=perspective.getZ();
                                        perspective2.setY(0.0);
                                        perspective2.setX(-z);
                                        perspective2.setZ(x);
                                        if(x*x+z*z==0){
                                                perspective2.setX(1.0);
                                        }
                                        perspective2.normalize();
                                }

                                for(int j=0;j<list.size();j++){
                                        Player player1= list.get(j);
                                        Location location=player.getLocation().clone();
                                        location.add(perspective2.clone().multiply((id-j)*LineUpPlugin.plugin.getConfig().getDouble("Distance")));
                                        if(player1!=null){
                                                location.setPitch(player1.getLocation().getPitch());
                                                location.setYaw(player1.getLocation().getYaw());
                                                player1.teleport(location);
                                        }

                                }
                                i++;
                        }
                },0L,10L);


        }
        public void reset(){
                Bukkit.getScheduler().cancelTask(taskid);
        }
}
