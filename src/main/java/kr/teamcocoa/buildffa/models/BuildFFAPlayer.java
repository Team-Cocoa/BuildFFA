package kr.teamcocoa.buildffa.models;

//import ch.dkrieger.coinsystem.core.CoinSystem;
//import ch.dkrieger.coinsystem.core.player.CoinPlayer;

import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.items.extra.ExtraItemManager;
import kr.teamcocoa.buildffa.items.shop.Bow;
import kr.teamcocoa.buildffa.items.shop.SnowBall;
import kr.teamcocoa.buildffa.prestige.Prestige;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class BuildFFAPlayer {

    private Player player;

    private long threwPearlTime;

    private long latestDeadTime;

    private boolean build;

    private boolean inGame;

    private Player lastHitPlayer;

    private boolean shootAble;

    private boolean snowBallBought, bowBought;

    private HashMap<Player, Double> damageTable;

    /* Prestige */
    private Prestige prestige;
    private int grade;

    /* Stats */
    private BuildFFAStats buildFFAStats;

    /* KB stick */
    private int kbStickDurability;

    public BuildFFAPlayer(Player player) {
        this(player, new BuildFFAStats(player.getUniqueId()));
    }

    public BuildFFAPlayer(Player player, BuildFFAStats buildFFAStats) {
        this.threwPearlTime = 0L;
        this.latestDeadTime = 0L;
        this.build = false;
        this.player = player;
        this.inGame = false;
        this.lastHitPlayer = null;
        this.shootAble = true;
        this.kbStickDurability = 15;

        this.snowBallBought = false;
        this.bowBought = false;
        this.damageTable = new HashMap<>();

        this.buildFFAStats = buildFFAStats;
    }

    public void setJoinInventory() {
        ItemStack shopItem = new ItemStack(Material.CHEST);
        ItemUtils.name(shopItem, LangUtils.getMessage(this.player, ItemEnum.SHOP));

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, shopItem);
        if (this.player.hasPermission("teamcocoa.prime")) {
            ItemStack killEffectItem = new ItemStack(Material.GOLDEN_SWORD);
            ItemUtils.name(killEffectItem, "&cKillEffects");
            this.player.getInventory().setItem(4, killEffectItem);
        }
    }

    public void arenaJoin() {
        player.closeInventory();
        player.getInventory().clear();
        player.setLevel(0);
        // kit load
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100.0F, 0.0F);

        inGame = true;

        if(bowBought) {
            ItemStack bowItem = Bow.getInstance().getItemStack(player, 1);
            ItemStack arrowItem = new ItemStack(Material.ARROW, 16);
            player.getInventory().addItem(bowItem, arrowItem);
        }

        if(snowBallBought) {
            ItemStack snowballItem = SnowBall.getInstance().getItemStack(player, 16);
            player.getInventory().addItem(snowballItem);
        }
    }

    public synchronized void kill(BuildFFAPlayer killed) {
        buildFFAStats.addKills(1);
//        CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(this.player.getUniqueId());
//        coinPlayer.addCoins(50);
//        this.player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &6+50 coins!"));
        PrestigeManager.getInstance().updatePrestige(this);
        try {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
            String health = (new DecimalFormat("0.00")).format(player.getHealth());

            killed.getPlayer().sendMessage(MessageFormat.format(
                    LangUtils.getMessage(killed.getPlayer(), MessageEnum.PLAYER_KILL),
                    player.getName(), health));

            player.setLevel(buildFFAStats.getKillStreaks());

            int killStreaks = buildFFAStats.getKillStreaks();

            if(killStreaks != 0 &&
                    (killStreaks % 5 == 0 || killStreaks > 15)) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(MessageFormat.format(
                            LangUtils.getMessage(onlinePlayer, MessageEnum.KILL_STREAK),
                            player.getName(), killStreaks));
                }
            }

            if (killStreaks % 3 == 0) {
                if (!player.getInventory().contains(new ItemStack(Material.ENDER_PEARL, 2))) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                }
                ExtraItemManager.getInstance().giveExtraItem(player);
                List<ItemStack> list = Arrays.asList(player.getInventory().getContents());
                if (isBowBought()) {
                    int arrayIndex = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i) == null) {
                            continue;
                        }
                        if (list.get(i).getType() == Material.ARROW) {
                            arrayIndex = i;
                            break;
                        }
                    }
                    if (arrayIndex == -1) {
                        player.getInventory().addItem(new ItemStack(Material.ARROW, 5));
                    } else {
                        int amount = list.get(arrayIndex).getAmount();
                        player.getInventory().addItem(new ItemStack(Material.ARROW, amount + 5 < 16 ? 5 : 5 - (amount + 5 - 16)));
                    }
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 0.0F);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void death(boolean quit) {
        buildFFAStats.addDeaths(1);
        if (!quit) {
            Location spawn = WorldManager.getInstance().getCurrentMap().getSpawn();

            player.teleport(spawn);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            setJoinInventory();

            if (player.equals(getLastHitPlayer())) {
                return;
            }

            if(buildFFAStats.getKillStreaks() >= 5) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(MessageFormat.format(
                            LangUtils.getMessage(onlinePlayer, MessageEnum.KILL_STREAK_BROKEN),
                            player.getName(), buildFFAStats.getKillStreaks(), getLastHitPlayer().getName()));
                }
            }
        }

        reset();
    }

    public void addDamage(Player hitter, double damage) {
        if (this.damageTable.containsKey(hitter)) {
            damage += this.damageTable.get(hitter);
        }
        this.damageTable.put(hitter, damage);
    }

    public void resetDamage() {
        for (Player damagedPlayer : this.damageTable.keySet()) {
            BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(damagedPlayer);
            if (buildFFAPlayer != null && buildFFAPlayer.isInGame()) {
                double totalHealth = damagedPlayer.getHealth() + this.damageTable.get(damagedPlayer);
                damagedPlayer.setHealth(Math.min(totalHealth, 20.0));
            }
        }

        this.damageTable.clear();
    }

    public void resetKBStickDurability() {
        this.kbStickDurability = 15;
    }

    public void reset() {
        long currentTime = System.currentTimeMillis();

        inGame = false;
        latestDeadTime = currentTime;
        bowBought = false;
        snowBallBought = false;
        lastHitPlayer = null;
        shootAble = false;

        player.setHealth(20);

        resetKBStickDurability();
        resetDamage();
        buildFFAStats.resetKillStreaks();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

}