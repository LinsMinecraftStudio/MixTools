package org.lins.mmmjjkx.mixtools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.lins.mmmjjkx.mixtools.objects.listener.MixToolsListener;

import java.lang.reflect.Field;

public class SignListener implements MixToolsListener {
    public Class<?> getNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @EventHandler
    public void onEditSign(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        if (block==null) return;
        if (!Tag.ALL_SIGNS.isTagged(block.getType())) return;
        Sign sign = (Sign) block.getState();
        open(event.getPlayer(), sign);
    }

    private void open(Player player, Sign sign) {
        for (int i = 0; i < 4; ++i)
            sign.setLine(i, sign.getLine(i).replace("§", "&"));
        sign.update();

        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);

            Field tileField = sign.getClass().getDeclaredField("sign");
            tileField.setAccessible(true);
            Object tileSign = tileField.get(sign);

            Field editable = tileSign.getClass().getDeclaredField("isEditable");
            editable.setAccessible(true);
            editable.set(tileSign, true);

            Field handler = tileSign.getClass().getDeclaredField("h");
            handler.setAccessible(true);
            handler.set(tileSign, handle);

            Object position = getNMSClass("BlockPosition$PooledBlockPosition")
                    .getMethod("d", double.class, double.class, double.class)
                    .invoke(null, sign.getX(), sign.getY(), sign.getZ());

            Object packet = getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(getNMSClass("BlockPosition"))
                    .newInstance(position);

            connection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
