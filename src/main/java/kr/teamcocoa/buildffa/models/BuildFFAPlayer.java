package kr.teamcocoa.buildffa.models;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.items.extra.ExtraItemManager;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.prestige.Prestige;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.utils.StringUtils;
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

    private boolean died;

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
        this.died = false;
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
        this.player.getInventory().setItem(0, ItemManager.createItem(Material.BLAZE_ROD, 1, LangUtils.getMessage(this.player, ItemEnum.INVENTORY_SORTING)));
        this.player.getInventory().setItem(8, ItemManager.createItem(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemEnum.SHOP)));
        if (this.player.hasPermission("killeffect.killeffect")) {
            this.player.getInventory().setItem(4, ItemManager.createItem(Material.GOLDEN_SWORD, 1, "§cKillEffects"));
        }
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
                killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                String KillerHealth = (new DecimalFormat("#0.0")).format(killer.getHealth() / 2.0D);

                killerBuildFFAPlayer.addKills();
                if (killerBuildFFAPlayer.isNicked()) {
                    killerBuildFFAPlayer.getNickedBffaPlayer().addKills();
                }

                player.sendMessage(LangUtils.getMessage(player, MessageEnum.PLAYER_KILL).replaceAll("%KILLER%", killerName).replaceAll("%KILLERHEALTH%", KillerHealth));

                int killerKillstreak = killerBuildFFAPlayer.getPlayerKillStreak() + 1;
                killer.setLevel(killerKillstreak);
                killerBuildFFAPlayer.setPlayerKillStreak(killerKillstreak);
                if (killerKillstreak > killerBuildFFAPlayer.getBestKillStreaks()) {
                    killerBuildFFAPlayer.setBestKillStreaks(killerKillstreak);
                }

                if (playerKillStreak >= 5) {
                    String killstreakPlayerString = String.valueOf(playerKillStreak);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", killerName).replaceAll("%PLAYER%", this.player.getName()));
                    }
                }
                if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", killerName));
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
                    killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 0.0F);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                resetDamage(true);
                setLastHitPlayer(null);
                setPlayerKillStreak(0);
                resetKBStickDurability();
            }
        }
    }

// java.lang.NullPointerException
// at kr.teamcocoa.buildffa.kit.BffaPlayer.death(BffaPlayer.java:211)
// at kr.teamcocoa.buildffa.listener.PlayerMoveListener.onPlayerMove(PlayerMoveListener.java:27)

    public void addDamage(Player hitter, double damage) {
        if (this.damageTable.containsKey(hitter)) {
            damage += this.damageTable.get(hitter);
        }
        this.damageTable.put(hitter, damage);
    }

    public void resetDamage(boolean giveHealth) {
        if (giveHealth) {
            for (Player damagedPlayer : this.damageTable.keySet()) {
                BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(damagedPlayer);
                if (buildFFAPlayer != null && buildFFAPlayer.isInGame()) {
                    double totalHealth = damagedPlayer.getHealth() + this.damageTable.get(damagedPlayer);
                    damagedPlayer.setHealth(Math.min(totalHealth, 20.0));
                }
            }
        }
        this.damageTable.clear();
    }

    public void resetKBStickDurability() {
        this.kbStickDurability = 15;
    }

}