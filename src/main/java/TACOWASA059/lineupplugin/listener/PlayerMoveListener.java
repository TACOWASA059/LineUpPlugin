package TACOWASA059.lineupplugin.listener;

import TACOWASA059.lineupplugin.LineUpPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // プレイヤーは基本的に動けない
        // event.getPlayer()でプレイヤーのオブジェクトを取得できる
        // event.getFrom()で移動前の座標、event.getTo()で移動後の座標を取得できる
        // 例えば、以下のようにコンソールにメッセージを出力するだけの処理を行うことができる
        if(LineUpPlugin.plugin.run){
            if(LineUpPlugin.plugin.getConfig().getInt("LineType")==1) event.getTo().set(event.getFrom().getX(),event.getTo().getY(),event.getFrom().getZ());
            if(LineUpPlugin.plugin.getConfig().getInt("LineType")==0) event.getTo().set(event.getFrom().getX(),event.getFrom().getY(),event.getFrom().getZ());
        }
    }
}