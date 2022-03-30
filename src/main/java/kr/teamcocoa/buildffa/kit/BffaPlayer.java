package kr.teamcocoa.buildffa.kit;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import kr.teamcocoa.buildffa.utils.ItemManager;

import java.util.Arrays;

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

    private boolean gappleBought, rescueBought;

    /* Stats */
    private int kills;
    private int deaths;
    private int bestKillStreaks;

    public BffaPlayer(Player player){
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
        this.rescueBought = false;
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

    public boolean isRescueBought() {
        return rescueBought;
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

    public void setJoinInventory(){
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, ItemManager.createItem(Material.BLAZE_ROD, 1, LangUtils.getMessage(this.player, ItemEnum.INVENTORY_SORTING)));

        this.player.getInventory().setItem(8, ItemManager.createItem(Material.SLIME_BALL, 1, LangUtils.getMessage(this.player, ItemEnum.LEAVE_ITEM)));
        if(this.player.hasPermission("killeffect.killeffect") || this.player.hasPermission("*")){
            this.player.getInventory().setItem(3, ItemManager.createItem(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemEnum.KIT)));
            this.player.getInventory().setItem(5, ItemManager.createItem(Material.GOLD_SWORD, 1, "§cKillEffects"));
        }
        else{
            this.player.getInventory().setItem(4, ItemManager.createItem(Material.CHEST, 1, LangUtils.getMessage(this.player, ItemEnum.KIT)));
        }
    }

    public void setGappleBought(boolean gappleBought) {
        this.gappleBought = gappleBought;
    }

    public void setRescueBought(boolean rescueBought) {
        this.rescueBought = rescueBought;
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
        if(this.nickedBffaPlayer == null) {
            return null;
        }
        return this.nickedBffaPlayer;
    }

    public boolean isNicked() {
        if(this.nickedBffaPlayer == null) {
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