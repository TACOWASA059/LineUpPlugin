package TACOWASA059.lineupplugin.listener;

import TACOWASA059.lineupplugin.LineUpPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // プレイヤーが動いたときの処理をここに書く
        // event.getPlayer()でプレイヤーのオブジェクトを取得できる
        // event.getFrom()で移動前の座標、event.getTo()で移動後の座標を取得できる
        // 例えば、以下のようにコンソールにメッセージを出力するだけの処理を行うことができる
        if(LineUpPlugin.plugin.run) event.getTo().set(event.getFrom().getX(),event.getFrom().getY(),event.getFrom().getZ());
    }
}