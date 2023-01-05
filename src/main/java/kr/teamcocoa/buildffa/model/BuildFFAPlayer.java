package kr.teamcocoa.buildffa.model;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.items.extra.ExtraItemManager;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.prestige.Prestige;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.WorldManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class BuildFFAPlayer {

    @Setter
    private long threwPearlTime;

    @Setter
    private int currentKillStreak;

    private long latestDeadTime;

    @Setter
    private boolean build;
    private Player player;
    private BuildFFAInventory inventory;

    @Setter
    private boolean inGame;

    @Setter
    private boolean died;

    private Player lastHitPlayer;
    private boolean shootAble;

    private boolean snowBallBought, bowBought;

    private HashMap<Player, Double> damageTable;

    /* Stats */
    private BuildFFAStats stats;

    private boolean nicked;
    private BuildFFAStats nickedStats;

    /* Prestige */
    private Prestige prestige;
    private int grade;

    /* KB stick */
    private int kbStickDurability;

    public BuildFFAPlayer(Player player) {
        this.threwPearlTime = 0L;
        this.currentKillStreak = 0;
        this.latestDeadTime = 0L;
        this.build = false;
        this.player = player;
        this.inGame = false;
        this.died = false;
        this.lastHitPlayer = null;
        this.shootAble = true;
        this.nicked = false;
        this.kbStickDurability = 15;
        this.snowBallBought = false;
        this.bowBought = false;
        this.damageTable = new HashMap<>();
    }

    public void setJoinInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, ItemManager.createItemStack(Material.BLAZE_ROD, 1, LangUtils.getMessage(this.player, ItemNode.INVENTORY_SORTING)));
        this.player.getInventory().setItem(8, ItemManager.createItemStack(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemNode.SHOP)));
        if (this.player.hasPermission("killeffect.killeffect")) {
            this.player.getInventory().setItem(4, ItemManager.createItemStack(Material.GOLD_SWORD, 1, "§cKillEffects"));
        }
    }

    public void setInGameInventory(boolean loadArmor) {
        player.getInventory().clear();
        if(loadArmor) {
            player.getInventory().setArmorContents(BuildFFAInventory.getArmors());
        }
        player.getInventory().setItem(inventory.getSwordIndex(), BuildFFAInventory.getGoldenSword());
        player.getInventory().setItem(inventory.getStickIndex(), BuildFFAInventory.getKbStick());
        player.getInventory().setItem(inventory.getPearlIndex(), BuildFFAInventory.getPearl());
        player.getInventory().setItem(inventory.getWebIndex(), BuildFFAInventory.getWeb());
        player.getInventory().setItem(inventory.getBlockIndex(), BuildFFAInventory.getBlock());
    }

    /*Stats Adder*/
    public void addKills() {
        this.kills += 1;
        CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(this.player.getUniqueId());
        coinPlayer.addCoins(50);
        this.player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &6+50 coins!"));
        PrestigeManager.getInstance().updatePrestige(this);
    }

    public void addDeaths() {
        this.deaths += 1;
    }

    public synchronized void death(boolean quit) {
        this.player.setHealth(20.0);
        addDeaths();
        if (!quit) {
            setThrewPearlTime(System.currentTimeMillis());
            setBowBought(false);
            setSnowBallBought(false);
            if (isNicked()) {
                getNickedBffaPlayer().addDeaths();
            }

            Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> {
                player.setHealth(20);
                player.teleport(spawn);
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                setInGame(false);
                setLatestDeadTime(System.currentTimeMillis());
                BuildFFA.playerData.get(player).setJoinInventory();
            }, 1L);

            if (player.equals(getLastHitPlayer())) {
                return;
            }
        }

        if (getLastHitPlayer() instanceof Player) {
            try {
                BuildFFAPlayer killerBuildFFAPlayer = BuildFFA.playerData.get(getLastHitPlayer());
                String killerName = killerBuildFFAPlayer.getPlayer().getName();
                Player killer = killerBuildFFAPlayer.getPlayer();
                killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 2);
                String KillerHealth = (new DecimalFormat("#0.0")).format(killer.getHealth() / 2.0D);

                killerBuildFFAPlayer.addKills();
                if (killerBuildFFAPlayer.isNicked()) {
                    killerBuildFFAPlayer.getNickedBffaPlayer().addKills();
                }

                player.sendMessage(LangUtils.getMessage(player, MessageNode.PLAYER_KILL).replaceAll("%KILLER%", killerName).replaceAll("%KILLERHEALTH%", KillerHealth));

                int killerKillstreak = killerBuildFFAPlayer.getCurrentKillStreak() + 1;
                killer.setLevel(killerKillstreak);
                killerBuildFFAPlayer.setCurrentKillStreak(killerKillstreak);
                if (killerKillstreak > killerBuildFFAPlayer.getBestKillStreaks()) {
                    killerBuildFFAPlayer.setBestKillStreaks(killerKillstreak);
                }

                if (currentKillStreak >= 5) {
                    String killstreakPlayerString = String.valueOf(currentKillStreak);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", killerName).replaceAll("%PLAYER%", this.player.getName()));
                    }
                }
                if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", killerName));
                    }
                }

                if (killerKillstreak % 3 == 0) {
                    if (!killer.getInventory().contains(new ItemStack(Material.ENDER_PEARL, 2))) {
                        killer.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    }
                    ExtraItemManager.getInstance().giveExtraItem(killer);
                    List<ItemStack> list = Arrays.asList(killerBuildFFAPlayer.getPlayer().getInventory().getContents());
                    if (killerBuildFFAPlayer.isBowBought()) {
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
                            killer.getInventory().addItem(new ItemStack(Material.ARROW, 5));
                        } else {
                            int amount = list.get(arrayIndex).getAmount();
                            killer.getInventory().addItem(new ItemStack(Material.ARROW, amount + 5 < 16 ? 5 : 5 - (amount + 5 - 16)));
                        }
                    }
                    killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                resetDamage(true);
                setLastHitPlayer(null);
                setCurrentKillStreak(0);
                resetKBStickDurability();
            }
        }
    }

    public void addDamage(Player hitter, double damage) {
        if (this.damageTable.containsKey(hitter)) {
            damage += this.damageTable.get(hitter);
        }
        this.damageTable.put(hitter, damage);
    }

    public void resetDamage(boolean giveHealth) {
        if (giveHealth) {
            for (Player player : this.damageTable.keySet()) {
                BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.getOrDefault(player, null);
                if (buildFFAPlayer != null && buildFFAPlayer.isInGame()) {
                    double totalHealth = player.getHealth() + this.damageTable.get(player);
                    player.setHealth(totalHealth >= 20.0 ? 20.0 : totalHealth);
                }
            }
        }
        this.damageTable.clear();
    }

    public void loadStats() {
        this.inventory = BuildFFA.getInstance().kitData.getPlayerKit(player);
        this.kills = BuildFFA.getInstance().statsDatabase.getKills(player.getUniqueId().toString());
        this.bestKillStreaks = BuildFFA.getInstance().statsDatabase.getMaxKillStreak(player.getUniqueId().toString());
        this.deaths = BuildFFA.getInstance().statsDatabase.getDeaths(player.getUniqueId().toString());
        PrestigeManager.getInstance().loadPrestige(this);
    }

    public void resetKBStickDurability() {
        this.kbStickDurability = 15;
    }

}