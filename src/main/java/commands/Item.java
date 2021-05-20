package commands;

import main.Main;
import utils.Config;
import utils.ItemManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    StringBuilder sb = new StringBuilder();
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("item"))) {
        if (args.length >= 1 && !args[0].equals("list")) {
          if (p.getItemInHand() != null && p.getItemInHand().getData().getItemType() != Material.AIR) {
            ItemStack item = p.getItemInHand();
            String str;
            switch ((str = args[0]).hashCode()) {
              case -1829133216:
                if (!str.equals("unbreakable"))
                  break; 
                if (args.length == 1) {
                  ItemManager itemManager = new ItemManager(item);
                  item = itemManager.modify().setUnbreakable(true).build();
                  p.setItemInHand(item);
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.unbreakable.01").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.unbreakable.02").replaceAll("&", "§"));
                } 
                return false;
              case -934594754:
                if (!str.equals("rename"))
                  break; 
                if (args.length > 1) {
                  ItemMeta newItemMeta = null;
                  ItemMeta otherItemMeta = null;
                  if (item.hasItemMeta()) {
                    newItemMeta = item.getItemMeta();
                  } else {
                    p.getItemInHand().setItemMeta(otherItemMeta);
                    newItemMeta = item.getItemMeta();
                  } 
                  String newName = "";
                  sb.append(args[1]);
                  for (int i = 2; i < args.length; i++)
                    sb.append(" " + args[i]); 
                  newName = sb.toString().replaceAll("&", "§");
                  newItemMeta.setDisplayName(newName);
                  p.getItemInHand().setItemMeta(newItemMeta);
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.rename.01").replaceAll("%NEWNAME%", newName).replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.rename.02").replaceAll("&", "§"));
                } 
                return false;
              case 3327734:
                if (!str.equals("lore"))
                  break; 
                if (args.length > 1) {
                  String lore = "";
                  sb.append(args[1]);
                  for (int i = 2; i < args.length; i++)
                    sb.append(" " + args[i]); 
                  lore = sb.toString().replaceAll("&", "§");
                  String[] loreList = { lore };
                  ItemManager itemManager = new ItemManager(item);
                  item = itemManager.modify().setLore(loreList).build();
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.lore.01").replaceAll("&", "§").replaceAll("%LORE%", lore));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.lore.02").replaceAll("&", "§"));
                } 
                return false;
            } 
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.cmd").replaceAll("&", "§"));
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.cmd2").replaceAll("&", "§"));
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.hand").replaceAll("&", "§"));
          } 
        } else if (args.length == 1 && args[0].equals("list")) {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.01").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.02").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.03").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.04").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.05").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.06").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.07").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.08").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.09").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.list.10").replaceAll("&", "§"));
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.cmd").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("item.cmd2").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
