package TACOWASA059.lineupplugin.utils;

import TACOWASA059.lineupplugin.LineUpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Set;

public class LineUpSetting {
        private final TeamManager teamManager;
        int taskid;
        public LineUpSetting(TeamManager teamManager){
                this.teamManager=teamManager;
                taskid=Bukkit.getScheduler().scheduleSyncRepeatingTask(LineUpPlugin.plugin,()->{
                        int i=0;
                        for(Team team:teamManager.teams){
                                Player player=teamManager.ref_Player[i];

                                Vector perspective=player.getLocation().getDirection();
                                perspective.normalize();

                                double x=perspective.getX();
                                double z=perspective.getZ();
                                perspective.setY(0.0);
                                perspective.setX(-z);
                                perspective.setZ(x);
                                if(x*x+z*z==0){
                                        perspective.setX(1.0);
                                }
                                perspective.normalize();
                                Set<String> name_list=team.getEntries();
                                int array=0;
                                for(String string:name_list){
                                        if(string.equalsIgnoreCase(player.getName())){
                                                break;
                                        }
                                        array++;
                                }
                                if(array==name_list.size()-1){
                                        continue;
                                }
                                int j=0;
                                for(String string:name_list){
                                        Player player1=Bukkit.getPlayer(string);
                                        Location location=player.getLocation().clone();
                                        location.add(perspective.clone().multiply(j-array));
                                        if(player1!=null){
                                                location.setPitch(player1.getLocation().getPitch());
                                                location.setYaw(player1.getLocation().getYaw());
                                                player1.teleport(location);
                                        }
                                        j++;
                                }

                                i++;
                        }
                },0L,10L);
        }
        public void reset(){
                Bukkit.getScheduler().cancelTask(taskid);
        }
}
