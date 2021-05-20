package utils;

import main.Main;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
  private static String teaming;
  
  public static File configFile = new File(Main.inst().getDataFolder() + "/config.yml");
  
  public static FileConfiguration config;
  
  public static File messagesFile = new File(Main.inst().getDataFolder() + "/messages.yml");
  
  public static FileConfiguration messages;
  
  public static File permissionsFile = new File(Main.inst().getDataFolder() + "/permissions.yml");
  
  public static FileConfiguration permissions;
  
  public static File locationsFile = new File(Main.inst().getDataFolder() + "/locations.yml");
  
  public static FileConfiguration locations;
  
  public static File kitsFile = new File(Main.inst().getDataFolder() + "/kits.yml");
  
  public static FileConfiguration kits;
  
  public static File playerFile = new File(Main.inst().getDataFolder() + "/player.yml");
  
  public static FileConfiguration player;
  
  public static File statsFile = new File(Main.inst().getDataFolder() + "/stats.yml");
  
  public static FileConfiguration stats;
  
  public static void createFile(File file, FileConfiguration config) throws IOException {
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
    if (file.getName().equalsIgnoreCase("config.yml")) {
      config.set("colorchat", Boolean.valueOf(true));
      config.set("startmap", "none");
      config.set("stats", Boolean.valueOf(true));
      config.set("statswalldirection", "EAST/NORTH/SOUTH/WEST");
      config.set("lobbyitem", Boolean.valueOf(true));
      config.set("lobbyitemcommand", "hub");
      config.set("kits", Boolean.valueOf(true));
      config.set("hunger", Boolean.valueOf(false));
      config.set("weather", Boolean.valueOf(false));
      config.set("scoreboard", Boolean.valueOf(true));
      config.set("teaming", Boolean.valueOf(false));
      config.set("mapchange", Boolean.valueOf(true));
      config.set("mapchangedelaysek", Integer.valueOf(600));
      config.set("displayname.chat", Boolean.valueOf(false));
      config.set("displayname.deaths", Boolean.valueOf(false));
      config.set("displayname.killstreak", Boolean.valueOf(false));
      config.set("displayname.joinmessage", Boolean.valueOf(false));
      config.set("displayname.quitmessage", Boolean.valueOf(false));
      config.set("join-quit-message", Boolean.valueOf(true));
      config.set("message.playerdeath", Boolean.valueOf(false));
      config.set("message.playerkill", Boolean.valueOf(true));
      config.set("message.killstreak", Boolean.valueOf(true));
      config.set("mysql.support", Boolean.valueOf(false));
      config.set("mysql.host", "host");
      config.set("mysql.port", "port");
      config.set("mysql.database", "database");
      config.set("mysql.username", "username");
      config.set("mysql.password", "password");
      config.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
    } 
    if (file.getName().equalsIgnoreCase("messages.yml")) {
      yamlConfiguration.set("prefix", "&6♦ &eBuildFFA &6► &r");
      yamlConfiguration.set("chatmessage", "%PREFIX%&7%PLAYER%:&r ");
      yamlConfiguration.set("joinmessage", "&0[&a+&0] &7%PLAYER%");
      yamlConfiguration.set("quitmessage", "&0[&c-&0] &7%PLAYER%");
      yamlConfiguration.set("nopermission", "&cKeine Rechte.");
      yamlConfiguration.set("scoreboard.map.score", "&f&lMap:");
      yamlConfiguration.set("scoreboard.map.prefix", "&c&l► ");
      yamlConfiguration.set("scoreboard.map.entry", "&9");
      yamlConfiguration.set("scoreboard.map.suffix", "%MAP%");
      yamlConfiguration.set("scoreboard.mapchange.score", "&f&lMapchange:");
      yamlConfiguration.set("scoreboard.mapchange.prefix", "&c&l► ");
      yamlConfiguration.set("scoreboard.mapchange.entry", "&9");
      yamlConfiguration.set("scoreboard.mapchange.suffix", "%MINUTES%:%SECONDS% &9min");
      yamlConfiguration.set("scoreboard.online.score", "&f&lSpieler:");
      yamlConfiguration.set("scoreboard.online.prefix", "&c&l► ");
      yamlConfiguration.set("scoreboard.online.entry", "&6");
      yamlConfiguration.set("scoreboard.online.suffix", "%ONLINEPLAYERS%&8/&6%MAXPLAYERS%");
      yamlConfiguration.set("scoreboard.kills.score", "&f&lKills:");
      yamlConfiguration.set("scoreboard.kills.prefix", "&c&l► ");
      yamlConfiguration.set("scoreboard.kills.entry", "&b");
      yamlConfiguration.set("scoreboard.kills.suffix", "%KILLS%");
      yamlConfiguration.set("scoreboard.teaming.score", "&f&lSpielvariante:");
      yamlConfiguration.set("scoreboard.teaming.prefix", "&c&l► ");
      yamlConfiguration.set("scoreboard.teaming.entry", "&c");
      yamlConfiguration.set("scoreboard.teaming.suffix", "%STATE%");
      yamlConfiguration.set("mapchanged", "&aDie Map hat zu &6%NEWMAP% &agewechselt");
      yamlConfiguration.set("mapnotchanged", "&aDie Map hat &cnicht &agewechselt");
      yamlConfiguration.set("mapchange.01", "&aDie Map wechselt in &63");
      yamlConfiguration.set("mapchange.02", "&aDie Map wechselt in &62");
      yamlConfiguration.set("mapchange.03", "&aDie Map wechselt in &61");
      yamlConfiguration.set("teaming.true", "Teaming erlaubt");
      yamlConfiguration.set("teaming.false", "Teaming verboten");
      yamlConfiguration.set("teaming.01", "&cTeaming ist ab jetzt verboten!");
      yamlConfiguration.set("teaming.02", "&aTeaming ist ab jetzt erlaubt!");
      yamlConfiguration.set("teaming.03", "&7Bitte nutze &6/teaming");
      yamlConfiguration.set("player.killall", "&7Der Spieler &8%PLAYER% &7wurde von &8%KILLER% &7getötet");
      yamlConfiguration.set("player.kill", "&cDu wurdest von &4%KILLER%&0(&c%KILLERHEALTH%❤&0) &cgetötet.");
      yamlConfiguration.set("player.death", "&7Der Spieler &8%PLAYER% &7ist gestorben!");
      yamlConfiguration.set("player.killstreak", "&7Der Spieler &c%PLAYER% &7hat einen Killstreak von &c[&4%KILLSTREAK%&c] &7erreicht!");
      yamlConfiguration.set("player.killstreakbroken", "&7Der &cKillstreak &7von &c%PLAYER%&c[&4%KILLSTREAK%&c] &7wurde von &8%KILLER% &cbeendet&7!");
      yamlConfiguration.set("nostartmap.01", "&7Es wurde &ckeine Startmap &7gefunden. Setze diese mit &6/setstartmap [Map-Name]&7.");
      yamlConfiguration.set("nostartmap.02", "&7Falls du noch keine Map erstellt hast nutze &6/setspawn [Map-Name]&7.");
      yamlConfiguration.set("nostartmap.03", "&7Nachdem du eine Map erstellt hast setze die Deathheight deiner Map (Höhe ab der man sterben soll) &6/setdeathheight &7sowie die Arenaheight (Höhe ab der man sein Kit erhält) &6/setarenaheight &7gehe dazu in den &aBuildmode &6/build");
      yamlConfiguration.set("nodeathheight.01", "&cKeine Deathheight.");
      yamlConfiguration.set("nodeathheight.02", "&7Setze die Deathheight deiner Map (Höhe ab der man sterben soll) &6/setdeathheight &7gehe dazu in den &aBuildmode &6/build");
      yamlConfiguration.set("noarenaheight.01", "&cKeine Arenaheight.");
      yamlConfiguration.set("noarenaheight.02", "&7Setze die Arenaheight deiner Map (Höhe ab der man sein Kit erhält) &6/setarenaheight &7gehe dazu in den &aBuildmode &6/build");
      yamlConfiguration.set("nodefaultkit.01", "&cKein Default-Kit");
      yamlConfiguration.set("nodefaultkit.02", "&7Setze ein Default-Kit (Kit welches man ohne ausgewähltes Kit erhält) &6/kit setdefault");
      yamlConfiguration.set("nokitselection.admin", "&7Es wurde noch &ckein Kit gewählt &7welches in der Kitauswahl vorhanden sein soll. Setze eins mit &6/kit select");
      yamlConfiguration.set("nokitselection.user", "&cKeine Kits vorhanden.");
      yamlConfiguration.set("nokitsymbol", "&7Ein Kit, welches selected wurde, hat &cnoch kein Symbol&7. Setze dies während du das Symbol(ein Item) in der Hand hälst &6/kit setsymbol");
      yamlConfiguration.set("nokitprefix", "&7Ein Kit, welches selected wurde, hat &cnoch keinen Prefix&7. Setze einen mit &6/kit setprefix");
      yamlConfiguration.set("nokitselected", "&cKein Kit ausgewählt.");
      yamlConfiguration.set("setspawn.01", "&7Du hast den &aSpawn für die Map &c%MAP% &7gesetzt.");
      yamlConfiguration.set("setspawn.02", "&7Bitte nutze &6/setspawn [Map-Name]&7.");
      yamlConfiguration.set("setstartmap.01", "&7Du hast die &aStartmap(&c%MAP%&A) &7gesetzt.");
      yamlConfiguration.set("setstartmap.02", "&7Die &aMap &c%MAP% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("setstartmap.03", "&7Bitte nutze &6/setstartmap [Map-Name]&7.");
      yamlConfiguration.set("setdeathheight.01", "&7Du hast die &aTodeshöhe(&c%TODESHÖHE%&a) für die Map &c%MAP% &7gesetzt.");
      yamlConfiguration.set("setdeathheight.02", "&7Die &aMap &c%MAP% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("setdeathheight.03", "&7Bitte nutze &6/setdeathheight [Map-Name]&7.");
      yamlConfiguration.set("setarenaheight.01", "&7Du hast die &aArenahöhe(&c%ARENAHÖHE%&a) für die Map &c%MAP% &7gesetzt.");
      yamlConfiguration.set("setarenaheight.02", "&7Die &aMap &c%MAP% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("setarenaheight.03", "&7Bitte nutze &6/setarenaheight [Map-Name]&7.");
      yamlConfiguration.set("build.01", "&7Du bist nun im &aBuildmode&7.");
      yamlConfiguration.set("build.02", "&7Du bist nun &cnicht mehr &7im &aBuildmode&7.");
      yamlConfiguration.set("build.03", "&7Der &ASpieler &c%TARGET% &7ist nun im &ABuildmode&7.");
      yamlConfiguration.set("build.04", "&7Der &aSpieler &c%PLAYER% &7hat dich in den &aBuildmode &7gesetzt.");
      yamlConfiguration.set("build.05", "&7Der &aSpieler &c%TARGET% &7ist nun &cnicht mehr &7im &aBuildmode&7.");
      yamlConfiguration.set("build.06", "&7Der &aSpieler &c%PLAYER% &7hat dich &caus dem &aBuildmode &7gesetzt.");
      yamlConfiguration.set("build.07", "&7Der &aSpieler &c%TARGET% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("build.08", "&7Bitte nutze &6/build [Spieler-Name]&7.");
      yamlConfiguration.set("build.info", "&7Du befindest dich noch im &aBuildmode&7, um ihn zu verlassen nutze &6/build&7.");
      yamlConfiguration.set("kit.deactivated", "&cDie Kits sind deaktiviert.");
      yamlConfiguration.set("kit.create.01", "&aKit wurde erstellt.");
      yamlConfiguration.set("kit.create.02", "&7Dieses &aKit &c%KIT% &7ist &cbereits vorhanden&7.");
      yamlConfiguration.set("kit.set.01", "&aKit gesetzt.");
      yamlConfiguration.set("kit.set.02", "&7Das &aKit &c%KIT% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("kit.get.01", "&7Du hast das &aKit &c%KIT% &7erhalten");
      yamlConfiguration.set("kit.get.02", "&7Das &aKit &c%KIT% &7enthält &ckeine Items&7.");
      yamlConfiguration.set("kit.remove.01", "&aKit &c%KIT% &aentfernt.");
      yamlConfiguration.set("kit.remove.02", "&7Das &aKit &c%KIT% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("kit.select.01", "&aKit ausgewählt.");
      yamlConfiguration.set("kit.select.02", "&7Das &aKit &c%KIT% &7enthält &ckeine Items&7.");
      yamlConfiguration.set("kit.select.03", "&7Bitte nutze &6/kit select [Kit-Name] [(Position) 1-4]&7.");
      yamlConfiguration.set("kit.setprefix.01", "&aKit-Prefix &c%PREFIX% &7für das &aKit &c%KIT% &7gesetzt.");
      yamlConfiguration.set("kit.setprefix.02", "&7Dass &aKit &c%KIT% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("kit.setsymbol.01", "&aKit-Symbol &c%SYMBOL% &7für das &aKit &c%KIT% &7gesetzt.");
      yamlConfiguration.set("kit.setsymbol.02", "&7Bitte halte ein &aItem &7in der Hand.");
      yamlConfiguration.set("kit.setsymbol.03", "&7Das &aKit &c%KIT% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("kit.setdefault.01", "&aDefault Kit gesetzt.");
      yamlConfiguration.set("kit.setdefault.02", "&7Das &aKit &c%KIT% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("kit.cmd.01", "&0═════════════════════════════");
      yamlConfiguration.set("kit.cmd.02", "&0► &aBitte nutze:            &cFragen? &6/kit help");
      yamlConfiguration.set("kit.cmd.03", "&0► &6/kit create [Kit-Name]");
      yamlConfiguration.set("kit.cmd.04", "&0► &6/kit set [Kit-Name]");
      yamlConfiguration.set("kit.cmd.05", "&0► &6/kit get [Kit-Name]");
      yamlConfiguration.set("kit.cmd.06", "&0► &6/kit remove [Kit-Name]");
      yamlConfiguration.set("kit.cmd.07", "&0► &6/kit setsymbol [Kit-Name]");
      yamlConfiguration.set("kit.cmd.08", "&0► &6/kit setdefault [Kit-Name]");
      yamlConfiguration.set("kit.cmd.09", "&0► &6/kit setprefix [Kit-Name] [Kit-Prefix]");
      yamlConfiguration.set("kit.cmd.10", "&0► &6/kit select [Kit-Name] [(Position) 1-4]");
      yamlConfiguration.set("kit.cmd.11", "&0═════════════════════════════");
      yamlConfiguration.set("kit.help.01", "&0═════════════════════════════");
      yamlConfiguration.set("kit.help.02", " &0► &7Kit ertellen: &6/kit create");
      yamlConfiguration.set("kit.help.03", " &0► &7Eigene Hotbar und eigene Rüstung ");
      yamlConfiguration.set("kit.help.04", "   &7einem Kit zuweisen: &6/kit set");
      yamlConfiguration.set("kit.help.05", " &0► &7Kit erhalten: &6/kit get");
      yamlConfiguration.set("kit.help.06", " &0► &7Kit entfernen: &6/kit remove");
      yamlConfiguration.set("kit.help.07", " &0► &7Standartkit setzten: &6/kit setdefault");
      yamlConfiguration.set("kit.help.08", " &0► &7Dem Kit einen Prefix zuweisen, welcher");
      yamlConfiguration.set("kit.help.09", "   &7im Kits-Menü vorkommt: &6/kit setprefix");
      yamlConfiguration.set("kit.help.10", " &0► &7Dem Kit ein Symbol in Form von einem");
      yamlConfiguration.set("kit.help.11", "   &7Item, welches sich in der Hand befinden");
      yamlConfiguration.set("kit.help.12", "   &7muss, zuweisen: &6/kit setsymbol");
      yamlConfiguration.set("kit.help.13", " &0► &7Kits auswählen, welche im Kits-Menü");
      yamlConfiguration.set("kit.help.14", "   &7vorhanden sein sollen: &6/kit select");
      yamlConfiguration.set("kit.help.15", "&0═════════════════════════════");
      yamlConfiguration.set("stats.deactivated", "&cDie Stats sind deaktiviert.");
      yamlConfiguration.set("stats.01", "&e═════════════════");
      yamlConfiguration.set("stats.02", " &0 &eStats ► &6%PLAYER%");
      yamlConfiguration.set("stats.03", " &0► &eKills: &6%KILLS%");
      yamlConfiguration.set("stats.04", " &0► &eDeaths: &6%DEATHS%");
      yamlConfiguration.set("stats.05", " &0► &eKD: &6%K/D%");
      yamlConfiguration.set("stats.06", "&e═════════════════");
      yamlConfiguration.set("stats.07", "&7Der &aSpieler &c%TARGET% &7konnte &cnicht gefunden &7werden.");
      yamlConfiguration.set("stats.08", "&7Bitte nutze &6/stats [Spieler-Name]&7.");
      yamlConfiguration.set("stats.setup.01", "&7Bitte nutze &6/stats setsign [Platz(1-3)]&7.");
      yamlConfiguration.set("item.list.01", "&0═══════════");
      yamlConfiguration.set("item.list.02", "&0► &6Enterhaken:");
      yamlConfiguration.set("item.list.03", "    &7Itemname: (Farbcode: ROT:c) Enterhaken");
      yamlConfiguration.set("item.list.04", "    &7Itemmaterial: FISHING_ROD");
      yamlConfiguration.set("item.list.05", "    &7unbreakable: true");
      yamlConfiguration.set("item.list.06", "&0► &6Jump:");
      yamlConfiguration.set("item.list.07", "    &7Itemname: (Farbcode: ROT:c) Jump");
      yamlConfiguration.set("item.list.08", "    &7Itemmaterial: FEATHER");
      yamlConfiguration.set("item.list.09", "    &7unbreakable: true");
      yamlConfiguration.set("item.list.10", "&0═══════════");
      yamlConfiguration.set("item.cmd", "&7Bitte nutze &6/item [unbreakable/rename/lore]");
      yamlConfiguration.set("item.cmd2", "&7Special-Items -> &6/item list");
      yamlConfiguration.set("item.rename.01", "&7Item erfolgreich zu &6%NEWNAME% &7umbenannt.");
      yamlConfiguration.set("item.rename.02", "&7Bitte nutze &6/item rename [Name]");
      yamlConfiguration.set("item.hand", "&7Halte ein &6Item &7in der Hand.");
      yamlConfiguration.set("item.unbreakable.01", "&7Dein Item ist nun &6unbreakable.");
      yamlConfiguration.set("item.unbreakable.02", "&7Bitte nutze &6/item unbreakable");
      yamlConfiguration.set("item.lore.01", "&7Itemlore erfolgreich zu &6%LORE% &7gesetzt.");
      yamlConfiguration.set("item.lore.02", "&7Bitte nutze &6/item lore [Lore]");
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(messagesFile);
    } 
    if (file.getName().equalsIgnoreCase("permissions.yml")) {
      yamlConfiguration.set("colorchat", "buildffa.colorchat");
      yamlConfiguration.set("teaming", "buildffa.teaming");
      yamlConfiguration.set("setspawn", "buildffa.setspawn");
      yamlConfiguration.set("setstartmap", "buildffa.setstartmap");
      yamlConfiguration.set("setdeathheight", "buildffa.setdeathheight");
      yamlConfiguration.set("setarenaheight", "buildffa.setarenaheight");
      yamlConfiguration.set("stats.setup", "buildffa.statssetup");
      yamlConfiguration.set("build", "buildffa.build");
      yamlConfiguration.set("item", "buildffa.item");
      yamlConfiguration.set("kit.cmd", "buildffa.kit");
      yamlConfiguration.set("kit.create", "buildffa.kit.create");
      yamlConfiguration.set("kit.set", "buildffa.kit.set");
      yamlConfiguration.set("kit.get", "buildffa.kit.get");
      yamlConfiguration.set("kit.remove", "buildffa.kit.remove");
      yamlConfiguration.set("kit.select", "buildffa.kit.select");
      yamlConfiguration.set("kit.setprefix", "buildffa.kit.setprefix");
      yamlConfiguration.set("kit.setsymbol", "buildffa.kit.setsymbol");
      yamlConfiguration.set("kit.setdefault", "buildffa.kit.setdefault");
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(permissionsFile);
    } 
    if (file.getName().equalsIgnoreCase("locations.yml")) {
      yamlConfiguration.set("maps", "keine vorhanden");
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(locationsFile);
    } 
    if (file.getName().equalsIgnoreCase("kits.yml")) {
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(kitsFile);
    } 
    if (file.getName().equalsIgnoreCase("player.yml")) {
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);
    } 
    if (file.getName().equalsIgnoreCase("stats.yml")) {
      yamlConfiguration.save(file);
      yamlConfiguration = YamlConfiguration.loadConfiguration(statsFile);
    } 
  }
  
  public static boolean checkIfExtists(File file) {
    if (file.exists())
      return true; 
    return false;
  }
  
  public static void loadFiles() {
    config = (FileConfiguration)YamlConfiguration.loadConfiguration(configFile);
    messages = (FileConfiguration)YamlConfiguration.loadConfiguration(messagesFile);
    permissions = (FileConfiguration)YamlConfiguration.loadConfiguration(permissionsFile);
    locations = (FileConfiguration)YamlConfiguration.loadConfiguration(locationsFile);
    kits = (FileConfiguration)YamlConfiguration.loadConfiguration(kitsFile);
    player = (FileConfiguration)YamlConfiguration.loadConfiguration(playerFile);
    stats = (FileConfiguration)YamlConfiguration.loadConfiguration(statsFile);
  }
  
  public static String getTeaming() {
    if (config.getBoolean("teaming")) {
      teaming = messages.getString("teaming.true").replaceAll("&", "§");
    } else {
      teaming = messages.getString("teaming.false").replaceAll("&", "§");
    } 
    return teaming;
  }
}
