package commands;

import main.Main;
import utils.Config;

import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit implements CommandExecutor {
  public static String Kitname;
  
  public static String Kitprefix;
  
  public static int selection = 0;
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("kit.cmd"))) {
        if (args.length == 2) {
          Kitname = args[1];
          String str;
          switch ((str = args[0]).hashCode()) {
            case -1441151073:
              if (!str.equals("setdefault"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.setdefault"))) {
                if (Config.kits.getString(Kitname) != null) {
                  Config.kits.set("kits.default", Kitname);
                  try {
                    Config.kits.save(Config.kitsFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setdefault.01").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setdefault.02").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
            case -1352294148:
              if (!str.equals("create"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.create"))) {
                if (Config.kits.getString(Kitname) == null) {
                  Config.kits.createSection(Kitname);
                  try {
                    Config.kits.save(Config.kitsFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.create.01").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.create.02").replaceAll("&", "§").replaceAll("%KIT%", Kitname));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
            case -934610812:
              if (!str.equals("remove"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.remove"))) {
                if (Config.kits.getString(Kitname) != null) {
                  Config.kits.set(Kitname, null);
                  if (Config.kits.getString("kits.default") != null)
                    if (Config.kits.getString("kits.default").equals(Kitname))
                      Config.kits.set("kits.default", null);  
                  Config.player.set("players", null);
                  try {
                    Config.player.save(Config.playerFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  if (Config.kits.getString("kits.view") != null) {
                    if (Config.kits.getString("kits.view.selection.1") != null)
                      if (Config.kits.getString("kits.view.selection.1").equals(Kitname))
                        Config.kits.set("kits.view.selection.1", null);  
                    if (Config.kits.getString("kits.view.selection.2") != null)
                      if (Config.kits.getString("kits.view.selection.2").equals(Kitname))
                        Config.kits.set("kits.view.selection.2", null);  
                    if (Config.kits.getString("kits.view.selection.3") != null)
                      if (Config.kits.getString("kits.view.selection.3").equals(Kitname))
                        Config.kits.set("kits.view.selection.3", null);  
                    if (Config.kits.getString("kits.view.selection.4") != null)
                      if (Config.kits.getString("kits.view.selection.4").equals(Kitname))
                        Config.kits.set("kits.view.selection.4", null);  
                  } 
                  if (Config.player.getString(p.getUniqueId().toString()) != null) {
                    if (Config.player.getString(String.valueOf(p.getUniqueId().toString()) + ".kitselected") != null && Config.player.getString(String.valueOf(p.getUniqueId().toString()) + ".kitselected").equals(Kitname)) {
                      Config.player.set(String.valueOf(p.getUniqueId().toString()) + ".kitselected", null);
                      try {
                        Config.player.save(Config.playerFile);
                      } catch (IOException e) {
                        e.printStackTrace();
                      } 
                    } 
                    if (Config.player.getString(String.valueOf(p.getUniqueId().toString()) + "." + Kitname) != null) {
                      Config.player.set(String.valueOf(p.getUniqueId().toString()) + "." + Kitname, null);
                      try {
                        Config.player.save(Config.playerFile);
                      } catch (IOException e) {
                        e.printStackTrace();
                      } 
                    } 
                  } 
                  try {
                    Config.kits.save(Config.kitsFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  Inventory.setJoinInventory(p);
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.remove.01").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.remove.02").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
            case 102230:
              if (!str.equals("get"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.get"))) {
                if (Config.kits.getString(String.valueOf(Kitname) + ".items") != null) {
                  ItemStack[] arrayOfItemStack1 = Inventory.getInventoryHotbar(Kitname);
                  p.getInventory().setContents(arrayOfItemStack1);
                  ItemStack[] inv2 = Inventory.getInventoryArmorContents(Kitname);
                  p.getInventory().setArmorContents(inv2);
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.get.01").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.get.02").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
            case 113762:
              if (!str.equals("set"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.set"))) {
                if (Config.kits.getString(Kitname) != null) {
                  Inventory.saveInventoryHotbar(p, Kitname);
                  Inventory.saveInventoryArmor(p, Kitname);
                  if (Config.kits.getString("kits.view.selection.1") != null && Config.kits.getString("kits.view.selection.1").equals(Kitname)) {
                    Config.player.set("players", null);
                    try {
                      Config.player.save(Config.playerFile);
                    } catch (IOException iOException) {
                      iOException.printStackTrace();
                    } 
                  } 
                  if (Config.kits.getString("kits.view.selection.2") != null && Config.kits.getString("kits.view.selection.2").equals(Kitname)) {
                    Config.player.set("players", null);
                    try {
                      Config.player.save(Config.playerFile);
                    } catch (IOException iOException) {
                      iOException.printStackTrace();
                    } 
                  } 
                  if (Config.kits.getString("kits.view.selection.3") != null && Config.kits.getString("kits.view.selection.3").equals(Kitname)) {
                    Config.player.set("players", null);
                    try {
                      Config.player.save(Config.playerFile);
                    } catch (IOException iOException) {
                      iOException.printStackTrace();
                    } 
                  } 
                  if (Config.kits.getString("kits.view.selection.4") != null && Config.kits.getString("kits.view.selection.4").equals(Kitname)) {
                    Config.player.set("players", null);
                    try {
                      Config.player.save(Config.playerFile);
                    } catch (IOException iOException) {
                      iOException.printStackTrace();
                    } 
                  } 
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.set.01").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.set.02").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
            case 1510006906:
              if (!str.equals("setsymbol"))
                break; 
              if (p.hasPermission(Config.permissions.getString("kit.setsymbol"))) {
                if (Config.kits.getString(Kitname) != null) {
                  if (p.getInventory().getItemInHand().getData().getItemType() != Material.AIR) {
                    Config.kits.set(String.valueOf(Kitname) + ".symbol", p.getInventory().getItemInHand());
                    try {
                      Config.kits.save(Config.kitsFile);
                    } catch (IOException e) {
                      e.printStackTrace();
                    } 
                    String str1 = String.valueOf(p.getItemInHand().getType());
                    p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setsymbol.01").replaceAll("%KIT%", Kitname).replaceAll("%SYMBOL%", str1).replaceAll("&", "§"));
                  } else {
                    p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setsymbol.02").replaceAll("&", "§"));
                  } 
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setsymbol.03").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
          } 
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.01").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.02").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.03").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.04").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.05").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.06").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.07").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.08").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.09").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.10").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.11").replaceAll("&", "§"));
        } else if (args.length == 3 && args[0].equals("select")) {
          Kitname = args[1];
          if (p.hasPermission(Config.permissions.getString("kit.select"))) {
            try {
              selection = Integer.parseInt(args[2]);
            } catch (Exception exception) {}
            if (selection != 0) {
              if (selection > 0 && selection < 5) {
                if (Config.kits.getString(String.valueOf(Kitname) + ".items") != null) {
                  Config.kits.set("kits.view.selection." + selection, Kitname);
                  try {
                    Config.kits.save(Config.kitsFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  Config.player.set("players", null);
                  try {
                    Config.player.save(Config.playerFile);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } 
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.select.01").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.select.02").replaceAll("%KIT%", Kitname).replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.select.03").replaceAll("&", "§"));
              } 
            } else {
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.select.03").replaceAll("&", "§"));
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
          } 
        } else if (args.length == 3 && args[0].equals("setprefix")) {
          Kitname = args[1];
          if (p.hasPermission(Config.permissions.getString("kit.setprefix"))) {
            if (Config.kits.getString(Kitname) != null) {
              Kitprefix = args[2].replace('&', '§');
              Config.kits.set(String.valueOf(Kitname) + ".prefix", Kitprefix);
              try {
                Config.kits.save(Config.kitsFile);
              } catch (IOException e) {
                e.printStackTrace();
              } 
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setprefix.01").replaceAll("%KIT%", Kitname).replaceAll("%PREFIX%", Kitprefix).replaceAll("&", "§"));
            } else {
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.setprefix.02").replaceAll("&", "§").replaceAll("%KIT%", Kitname));
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
          } 
        } else if (args.length == 1 && args[0].equals("help")) {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.01").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.02").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.03").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.04").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.05").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.06").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.07").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.08").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.09").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.10").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.11").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.12").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.13").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.14").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.help.15").replaceAll("&", "§"));
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.01").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.02").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.03").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.04").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.05").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.06").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.07").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.08").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.09").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.10").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("kit.cmd.11").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
