package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.NameFetcher;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Stats implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // /stats 를 입력했을때
            int kills = 0;
            int killStreak = 0;
            int deaths = 0;
            String name;
            if (args.length == 0) {
                BffaPlayer bffaPlayer = Main.playerData.get(player);
                kills = bffaPlayer.getKills();
                killStreak = bffaPlayer.getBestKillStreaks();
                deaths = bffaPlayer.getDeaths();
                name = player.getName();
            }
            // /stats <nick> 을 입력했을때
            else {
                Player target = Bukkit.getPlayer(args[0]);
                // 플레이어를 찾을 수 없을 때
                if (target == null) {
                    sendOfflineMessageStats(player, args[0]);
                    return true;
                }
                // 플레이어가 있을 때
                else {
                    if (!args[0].toLowerCase(Locale.ROOT).equals(target.getName().toLowerCase(Locale.ROOT))) {
                        sendOfflineMessageStats(player, args[0]);
                        return true;
                    }
                    BffaPlayer bffaPlayer = Main.playerData.get(target);
                    if (bffaPlayer.isNicked()) {
                        kills = bffaPlayer.getNickedBffaPlayer().getKills();
                        killStreak = bffaPlayer.getNickedBffaPlayer().getBestKillStreaks();
                        deaths = bffaPlayer.getNickedBffaPlayer().getDeaths();
                    } else {
                        kills = bffaPlayer.getKills();
                        killStreak = bffaPlayer.getBestKillStreaks();
                        deaths = bffaPlayer.getDeaths();
                    }
                    name = target.getName();
                }
            }
            String prestige = PrestigeManager.getInstance().getPrestigeName(kills);
            player.sendMessage(StringUtils.color(
                    MessageFormat.format(StringUtils.getListByString(
                            LangUtils.getMessage(player, MessageEnum.STATS_MESSAGE)), name, kills, deaths, killStreak, prestige)));
        }
        return true;
    }

    private void sendOfflineMessageStats(Player player, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            String request = NameFetcher.getUUID(name);
            try {
                String name1 = request.split("\\|")[0].replace("\"", "");
                String uuid = request.split("\\|")[1];
                // 닉네임이 존재 자체도 안할때
                if (uuid == null) {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.STATS_NOT_FOUND));
                    return;
                }
                // 닉네임이 존재는 할때
                // 서버에 접속한 적이 있을 때
                if (Main.getInstance().stats.playerExists(uuid)) {
                    int kills1 = Main.getInstance().stats.getKills(uuid);
                    int deaths1 = Main.getInstance().stats.getDeaths(uuid);
                    int killStreak1 = Main.getInstance().stats.getMaxKillStreak(uuid);
                    String prestige1 = PrestigeManager.getInstance().getPrestigeName(kills1);
                    player.sendMessage(StringUtils.color(
                            MessageFormat.format(StringUtils.getListByString(
                                    LangUtils.getMessage(player, MessageEnum.STATS_MESSAGE)), name1, kills1, deaths1, killStreak1, prestige1)));
                }
                // 서버에 접속한 적도 없을 때
                else {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.STATS_NOT_FOUND));
                }
            }
            catch (NullPointerException e) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.STATS_NOT_FOUND));
                return;
            }
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().info(s);
        if(command.getName().equals("stats")) {
            Bukkit.getLogger().info(s);
            Bukkit.getLogger().info(Arrays.toString(strings));
            List<String> nameList = new LinkedList<>();
            StringUtil.copyPartialMatches(strings[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), nameList);
            return nameList;
        }
        return null;
    }
}
