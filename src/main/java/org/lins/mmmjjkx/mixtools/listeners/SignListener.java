package org.lins.mmmjjkx.mixtools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.events.SignEditEvent;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixToolsListener;
import org.lins.mmmjjkx.mixtools.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

public class SignListener implements MixToolsListener {
    private static boolean useBukkitOpenSign;
    private static Constructor<?> nmsBlockPositionConstructor;
    private static Constructor<?> nmsPacketPlayOutOpenSignEditorConstructor;
    private static Class<?> craftWorldClass;
    private static Method craftWorldGetHandleMethod;
    private static Method getTileEntityMethod;
    private static Class<?> nmsTileEntitySignClass;
    private static Class<?> nmsEntityHumanClass;
    private static boolean tileEntitySignUsesModernMethods;
    private static Field nmsTileEntitySignIsEditableField;
    private static Field nmsTileEntitySignEntityHumanField;
    private static Method nmsTileEntitySignSetEditableMethod;
    private static Method nmsTileEntitySignSetUUIDMethod;
    private static Class<?> craftPlayerClass;
    private static Method craftPlayerGetHandleMethod;
    private static Field playerConnectionField;
    private static Method sendPacketMethod;

    @Override
    public void register() {
        try {
            if (Arrays.stream(Player.class.getMethods()).noneMatch(each -> each.getName().equals("openSign"))) {
                useBukkitOpenSign = false;
                Class<?> nmsPacketPlayOutOpenSignEditorClass = getNMSClass("net.minecraft.server.%s.PacketPlayOutOpenSignEditor", "net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor");
                Class<?> nmsBlockPositionClass = getNMSClass("net.minecraft.server.%s.BlockPosition", "net.minecraft.core.BlockPosition");
                nmsBlockPositionConstructor = nmsBlockPositionClass.getConstructor(int.class, int.class, int.class);
                nmsPacketPlayOutOpenSignEditorConstructor = nmsPacketPlayOutOpenSignEditorClass.getConstructor(nmsBlockPositionClass);
                craftWorldClass = getNMSClass("org.bukkit.craftbukkit.%s.CraftWorld");
                craftWorldGetHandleMethod = craftWorldClass.getMethod("getHandle");
                getTileEntityMethod = craftWorldGetHandleMethod.getReturnType().getMethod("getTileEntity", nmsBlockPositionClass);
                nmsTileEntitySignClass = getNMSClass("net.minecraft.server.%s.TileEntitySign", "net.minecraft.world.level.block.entity.TileEntitySign");
                nmsEntityHumanClass = getNMSClass("net.minecraft.server.%s.EntityHuman", "net.minecraft.world.entity.player.EntityHuman");
                tileEntitySignUsesModernMethods = false;
                try {
                    nmsTileEntitySignIsEditableField = nmsTileEntitySignClass.getDeclaredField("isEditable");
                    nmsTileEntitySignEntityHumanField = Arrays.stream(nmsTileEntitySignClass.getDeclaredFields()).filter(each -> each.getType().equals(nmsEntityHumanClass)).findFirst().orElseThrow();
                } catch (NoSuchFieldException e) {
                    nmsTileEntitySignSetEditableMethod = nmsTileEntitySignClass.getMethod("a", boolean.class);
                    nmsTileEntitySignSetUUIDMethod = nmsTileEntitySignClass.getMethod("a", UUID.class);
                    tileEntitySignUsesModernMethods = true;
                }
                craftPlayerClass = getNMSClass("org.bukkit.craftbukkit.%s.entity.CraftPlayer");
                craftPlayerGetHandleMethod = craftPlayerClass.getMethod("getHandle");
                try {
                    playerConnectionField = craftPlayerGetHandleMethod.getReturnType().getField("playerConnection");
                } catch (NoSuchFieldException e) {
                    playerConnectionField = craftPlayerGetHandleMethod.getReturnType().getField("b");
                }
                Class<?> nmsPacketClass = getNMSClass("net.minecraft.server.%s.Packet", "net.minecraft.network.protocol.Packet");
                try {
                    sendPacketMethod = playerConnectionField.getType().getMethod("sendPacket", nmsPacketClass);
                } catch (NoSuchMethodException e) {
                    sendPacketMethod = playerConnectionField.getType().getMethod("a", nmsPacketClass);
                }
            } else {
                useBukkitOpenSign = true;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        MixToolsListener.super.register();
    }

    private static Class<?> getNMSClass(String path, String... paths) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        ClassNotFoundException error;
        try {
            return Class.forName(path.replace("%s", version));
        } catch (ClassNotFoundException e) {
            error = e;
        }
        for (String classpath : paths) {
            try {
                return Class.forName(classpath.replace("%s", version));
            } catch (ClassNotFoundException e) {
                error = e;
            }
        }
        throw error;
    }

    @EventHandler
    public void onEditSign(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && block.getType().toString().contains("SIGN") && !player.isSneaking() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && isInteractingWithAir(player)) {
            Material material = block.getType();
            Block attachedBlock;
            BlockData data = block.getBlockData();
            if (data instanceof Directional directional) {
                attachedBlock = block.getRelative(directional.getFacing().getOppositeFace());
            } else {
                attachedBlock = block.getRelative(BlockFace.DOWN);
            }

            BlockPlaceEvent placeEvent = new BlockPlaceEvent(block, block.getState(), attachedBlock, new ItemStack(material), player, true, EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(placeEvent);
            if (!placeEvent.isCancelled()) {
                Sign sign = (Sign) block.getState();
                String[] lines = sign.getLines();
                for (int i = 0; i < lines.length; i++) {
                    sign.setLine(i, MixTools.messageHandler.colorize(lines[i]));
                }
                sign.update();
                Bukkit.getScheduler().runTaskLater(MixTools.INSTANCE, () -> {
                    if (useBukkitOpenSign) {
                        sign.setEditable(true);
                        sign.update();
                        Bukkit.getScheduler().runTaskLater(MixTools.INSTANCE, () -> player.openSign(sign), 1);
                    } else {
                        try {
                            Object entityPlayer = craftPlayerGetHandleMethod.invoke(craftPlayerClass.cast(player));
                            Object blockPosition = nmsBlockPositionConstructor.newInstance(block.getX(), block.getY(), block.getZ());
                            Object tileEntity = getTileEntityMethod.invoke(craftWorldGetHandleMethod.invoke(craftWorldClass.cast(block.getWorld())), blockPosition);
                            Object nmsSign = nmsTileEntitySignClass.cast(tileEntity);
                            if (tileEntitySignUsesModernMethods) {
                                nmsTileEntitySignSetEditableMethod.invoke(nmsSign, true);
                                nmsTileEntitySignSetUUIDMethod.invoke(nmsSign, player.getUniqueId());
                            } else {
                                nmsTileEntitySignIsEditableField.setAccessible(true);
                                nmsTileEntitySignIsEditableField.set(nmsSign, true);
                                nmsTileEntitySignEntityHumanField.setAccessible(true);
                                nmsTileEntitySignEntityHumanField.set(nmsSign, nmsEntityHumanClass.cast(entityPlayer));
                            }
                            Object packet = nmsPacketPlayOutOpenSignEditorConstructor.newInstance(blockPosition);
                            sendPacketMethod.invoke(playerConnectionField.get(entityPlayer), packet);
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                                 InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2);
            }
        }
    }

    @EventHandler
    public void onSignChanged(SignChangeEvent e){
        String[] lines = e.getLines();
        Sign sign = (Sign) e.getBlock().getState();
        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, MixTools.messageHandler.colorize(lines[i]));
        }
        sign.update(true);
    }

    @EventHandler
    public void onSignEdit(SignEditEvent e){
        String[] lines = e.getSign().getLines();
        Sign sign = e.getSign();
        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, StringUtils.unformatString(lines[i]));
        }
        sign.update();
    }

    private boolean isInteractingWithAir(Player player) {
        try {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            return mainHand.getType().equals(Material.AIR) && !offHand.getType().toString().contains("DYE");
        } catch (Throwable e) {
            @SuppressWarnings("deprecation")
            ItemStack item = player.getItemInHand();
            return item.getType().equals(Material.AIR);
        }
    }
}
