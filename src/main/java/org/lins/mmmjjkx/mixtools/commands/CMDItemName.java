package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CMDItemName extends PolymerCommand {
    public CMDItemName(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (sender instanceof Player p){
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (!hand.getType().equals(Material.AIR) && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    Component displayName = meta.displayName();
                    if (args.length==1 & displayName != null) {
                        return Collections.singletonList(MiniMessage.miniMessage().serialize(displayName));
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (!hand.getType().equals(Material.AIR)) {
                    if (args.length==1) {
                        ItemMeta meta = hand.getItemMeta();
                        meta.displayName(MixTools.messageHandler.colorize(args[0]));
                        hand.setItemMeta(meta);
                        return true;
                    }else {
                        sendMessage(p, "Command.ArgError");
                        return false;
                    }
                }else {
                    sendMessage(p,"Item.AIR");
                    return false;
                }
            }
        }
        return false;
    }
}
