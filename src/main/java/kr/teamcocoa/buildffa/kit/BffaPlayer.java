package kr.teamcocoa.buildffa.kit;

import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import kr.teamcocoa.buildffa.utils.ItemManager;

public class BffaPlayer {
    private long threwPearlTime;
    private int playerKillStreak;
    private long latestDeadTime;
    private boolean build;
    private Player player;
    private ItemStack[] inventory;
    private int kit;
    private boolean inGame;
    private boolean died;

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
        this.kit = Main.inst().kitData.getKit(player);
        this.inventory = Main.inst().kitData.getPlayerKit(player, Main.inst().kitData.getKit(player));
        this.inGame = false;
        this.died = false;

        this.kills = Main.inst().stats.getKills(player.getUniqueId().toString());
        this.bestKillStreaks = Main.inst().stats.getMaxKillStreak(player.getUniqueId().toString());
        this.deaths = Main.inst().stats.getDeaths(player.getUniqueId().toString());
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

    public int getKit() {
        return kit;
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

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBestKillStreaks() {
        return bestKillStreaks;
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

    public void setKit(int kit) {
        this.kit = kit;
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

    public void setJoinInventory(){
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, ItemManager.createItem(Material.BLAZE_ROD, 1, "§cInventorySorting"));

        this.player.getInventory().setItem(8, ItemManager.createItem(Material.SLIME_BALL, 1, "§cReturn to lobby"));
        if(this.player.hasPermission("killeffect.killeffect") || this.player.hasPermission("*")){
            this.player.getInventory().setItem(3, ItemManager.createItem(Material.CHEST, 1, "§cKits"));
            this.player.getInventory().setItem(5, ItemManager.createItem(Material.GOLD_SWORD, 1, "§cKillEffects"));
        }
        else{
            this.player.getInventory().setItem(4, ItemManager.createItem(Material.CHEST, 1, "§cKits"));
        }
    }

    /*Stats Adder*/
    public void addKills() {
        this.kills += 1;
    }

    public void addDeaths() {
        this.deaths += 1;
    }

}