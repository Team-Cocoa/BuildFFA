package kr.teamcocoa.buildffa.kit;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.items.extra.ExtraItemManager;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import kr.teamcocoa.buildffa.utils.ItemManager;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class BffaPlayer {
    private long threwPearlTime;
    private int playerKillStreak;
    private long latestDeadTime;
    private boolean build;
    private Player player;
    private ItemStack[] inventory;
    private boolean inGame;
    private boolean died;
    private Player lastHitPlayer;
    private NickedBffaPlayer nickedBffaPlayer;
    private boolean shootAble;

    private boolean gappleBought, bowBought;

    /* Stats */
    private int kills;
    private int deaths;
    private int bestKillStreaks;

    public BffaPlayer(Player player) {
        this.threwPearlTime = 0L;
        this.playerKillStreak = 0;
        this.latestDeadTime = 0L;
        this.build = false;
        this.player = player;
        this.inventory = Main.inst().kitData.getPlayerKit(player);
        this.inGame = false;
        this.died = false;
        this.lastHitPlayer = null;
        this.shootAble = true;
        this.nickedBffaPlayer = null;

        this.kills = Main.inst().stats.getKills(player.getUniqueId().toString());
        this.bestKillStreaks = Main.inst().stats.getMaxKillStreak(player.getUniqueId().toString());
        this.deaths = Main.inst().stats.getDeaths(player.getUniqueId().toString());
        this.gappleBought = false;
        this.bowBought = false;
    }

    /*Getter*/

    public ItemStack[] getInventory() {
        return inventory;
    }

    public int getPlayerKillStreak() {
        return playerKillStreak;
    }

    public long getLatestDeadTime() {
        return latestDeadTime;
    }

    public long getThrewPearlTime() {
        return threwPearlTime;
    }

    public boolean isBuild() {
        return build;
    }

    public Player getPlayer() {
        return player;
    }

    public void setDied(boolean died) {
        this.died = died;
    }

    public boolean isDied() {
        return died;
    }

    public boolean isInGame() {
        return inGame;
    }

    public Player getLastHitPlayer() {
        return lastHitPlayer;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBestKillStreaks() {
        return bestKillStreaks;
    }

    public boolean isShootAble() {
        return shootAble;
    }

    public boolean isGappleBought() {
        return gappleBought;
    }

    public boolean isBowBought() {
        return bowBought;
    }

    /*Setter*/

    public void setBuild(boolean build) {
        this.build = build;
    }

    public void setLatestDeadTime(long latestDeadTime) {
        this.latestDeadTime = latestDeadTime;
    }

    public void setPlayerKillStreak(int playerKillStreak) {
        this.playerKillStreak = playerKillStreak;
    }

    public void setThrewPearlTime(long threwPearlTime) {
        this.threwPearlTime = threwPearlTime;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBestKillStreaks(int bestKillStreaks) {
        this.bestKillStreaks = bestKillStreaks;
    }

    public void setLastHitPlayer(Player lastHitPlayer) {
        this.lastHitPlayer = lastHitPlayer;
    }

    public void setShootAble(boolean shootAble) {
        this.shootAble = shootAble;
    }

    public void setJoinInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, ItemManager.createItem(Material.BLAZE_ROD, 1, LangUtils.getMessage(this.player, ItemEnum.INVENTORY_SORTING)));

        this.player.getInventory().setItem(8, ItemManager.createItem(Material.SLIME_BALL, 1, LangUtils.getMessage(this.player, ItemEnum.LEAVE_ITEM)));
        if (this.player.hasPermission("killeffect.killeffect") || this.player.hasPermission("*")) {
            this.player.getInventory().setItem(3, ItemManager.createItem(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemEnum.SHOP)));
            this.player.getInventory().setItem(5, ItemManager.createItem(Material.GOLD_SWORD, 1, "§cKillEffects"));
        } else {
            this.player.getInventory().setItem(4, ItemManager.createItem(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemEnum.SHOP)));
        }
    }

    public void setGappleBought(boolean gappleBought) {
        this.gappleBought = gappleBought;
    }

    public void setBowBought(boolean bowBought) {
        this.bowBought = bowBought;
    }

    /*Stats Adder*/
    public void addKills() {
        this.kills += 1;
        CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(this.player.getUniqueId());
        coinPlayer.addCoins(50);
        this.player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &6+50 coins!"));
    }

    public void addDeaths() {
        this.deaths += 1;
    }

    public NickedBffaPlayer getNickedBffaPlayer() {
        if (this.nickedBffaPlayer == null) {
            return null;
        }
        return this.nickedBffaPlayer;
    }

    public boolean isNicked() {
        if (this.nickedBffaPlayer == null) {
            return false;
        }
        return true;
    }

    public void addNicked() {
        this.nickedBffaPlayer = new NickedBffaPlayer(this.player);
    }

    public void removeNicked() {
        this.nickedBffaPlayer = null;
    }

    public void death(boolean quit) {
        addDeaths();
        if(!quit) {
            setThrewPearlTime(System.currentTimeMillis());
            setPlayerKillStreak(0);
            setBowBought(false);
            setGappleBought(false);
            if (isNicked()) {
                getNickedBffaPlayer().addDeaths();
            }

            Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
                player.setHealth(20);
                player.teleport(spawn);
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                setInGame(false);
                setLatestDeadTime(System.currentTimeMillis());
                Main.playerData.get(player).setJoinInventory();
            }, 1L);

            if (player.equals(getLastHitPlayer())) {
                return;
            }
        }

        if (getLastHitPlayer() instanceof Player) {
            try {
                BffaPlayer killerBffaPlayer = Main.playerData.get(getLastHitPlayer());
                String killerName = getLastHitPlayer().getName();
                Player killer = getLastHitPlayer();
                killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 2);
                String KillerHealth = (new DecimalFormat("#0.0")).format(killer.getHealth() / 2.0D);

                killerBffaPlayer.addKills();
                if (killerBffaPlayer.isNicked()) {
                    killerBffaPlayer.getNickedBffaPlayer().addKills();
                }

                player.sendMessage(LangUtils.getMessage(player, MessageEnum.PLAYER_KILL).replaceAll("%KILLER%", killerName).replaceAll("%KILLERHEALTH%", KillerHealth));

                int killerKillstreak = killerBffaPlayer.getPlayerKillStreak() + 1;
                killer.setHealth(20.0D);
                killer.setLevel(killerKillstreak);
                killerBffaPlayer.setPlayerKillStreak(killerKillstreak);
                if (killerKillstreak > killerBffaPlayer.getBestKillStreaks()) {
                    killerBffaPlayer.setBestKillStreaks(killerKillstreak);
                }

                if (killerKillstreak % 3 == 0) {
                    try {
                        ExtraItemManager.getInstance().giveExtraItem(killer);
                        List<ItemStack> list = Arrays.asList(killerBffaPlayer.getPlayer().getInventory().getContents());
                        if (!killer.getInventory().contains(new ItemStack(Material.ENDER_PEARL, 2))) {
                            killer.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                        }
                        if (killerBffaPlayer.isBowBought()) {
                            int arrayIndex = -1;
                            for (int i = 0; i < list.size(); i++) {
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
                        try {
                            killer.playSound(player.getKiller().getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
                        }
                        catch (Exception e2) {

                        }
                        // ^ NPE(NullPointerException)? How?
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                setLastHitPlayer(null);


                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
                    if (playerKillStreak >= 5) {
                        String killstreakPlayerString = String.valueOf(playerKillStreak);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", killerName).replaceAll("%PLAYER%", player.getName()));
                        }
                    }
                    if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", killerName));
                        }
                    }
                }, 3L);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "BffaPlayer{" +
                "threwPearlTime=" + threwPearlTime +
                ", playerKillStreak=" + playerKillStreak +
                ", latestDeadTime=" + latestDeadTime +
                ", build=" + build +
                ", player=" + player +
                ", inventory=" + Arrays.toString(inventory) +
                ", inGame=" + inGame +
                ", died=" + died +
                ", lastHitPlayer=" + lastHitPlayer +
                ", nickedBffaPlayer=" + nickedBffaPlayer +
                ", shootAble=" + shootAble +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", bestKillStreaks=" + bestKillStreaks +
                '}';
    }
}