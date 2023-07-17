package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
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
import java.util.List;

public class CMDItemLore extends PolymerCommand {
    public CMDItemLore(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (sender instanceof Player p) {
            ItemStack i = p.getInventory().getItemInMainHand();
            if (!i.getType().equals(Material.AIR)) {
                ItemMeta m = i.getItemMeta();
                Component lore = Component.empty();
                List<Component> components = m.lore();
                if (components!=null && !components.isEmpty()) {
                    try {lore = components.get(args.length-1);
                    } catch (Exception ignored) {}
                    if (lore != null) {
                        return List.of(MiniMessage.miniMessage().serialize(lore));
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
            if (p != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                if (!i.getType().equals(Material.AIR)){
                    ItemMeta m = i.getItemMeta();
                    List<Component> lines = new ArrayList<>();
                    if (args.length>0){
                        for (String arg : args) {
                            lines.add(MixTools.messageHandler.colorize(arg.replaceAll(":space:"," ")));
                        }
                        m.lore(lines);
                        i.setItemMeta(m);
                        return true;
                    }else {
                        Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }else {
                    sendMessage(sender, "Item.AIR");
                    return false;
                }
            }
        }
        return false;
    }
}
