package kr.teamcocoa.buildffa.game.models;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;
import kr.teamcocoa.buildffa.game.items.extra.ExtraItemManager;
import kr.teamcocoa.buildffa.game.items.shop.Bow;
import kr.teamcocoa.buildffa.game.items.shop.SnowBall;
import kr.teamcocoa.buildffa.game.kit.KitLoader;
import kr.teamcocoa.buildffa.game.prestige.Prestige;
import kr.teamcocoa.buildffa.game.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.translation.InventoryEnum;
import kr.teamcocoa.buildffa.translation.ItemEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

@Getter
@Setter
public class BuildFFAPlayer {

  public interface Factory {
    BuildFFAPlayer create(Player player, BuildFFAStats buildFFAStats);
  }

  @Getter(AccessLevel.NONE)
  private final Plugin plugin;

  @Getter(AccessLevel.NONE)
  private final BuildFFAPlayerManager buildFFAPlayerManager;

  @Getter(AccessLevel.NONE)
  private final WorldManager worldManager;

  @Getter(AccessLevel.NONE)
  private final PrestigeManager prestigeManager;

  @Getter(AccessLevel.NONE)
  private final ExtraItemManager extraItemManager;

  @Getter(AccessLevel.NONE)
  private final Bow bow;

  @Getter(AccessLevel.NONE)
  private final SnowBall snowBall;

  @Getter(AccessLevel.NONE)
  private final KitLoader kitLoader;

  private Player player;

  private long threwPearlTime;

  private long latestDeadTime;

  private boolean build;

  private boolean inGame;

  private Player lastHitPlayer;

  private boolean shootAble;

  private boolean snowBallBought, bowBought;

  private HashMap<Player, Double> damageTable;

  private Prestige prestige;
  private int grade;

  private BuildFFAStats buildFFAStats;

  private int kbStickDurability;

  private ItemStack[] kit;

  @Inject
  public BuildFFAPlayer(
      @Assisted Player player,
      @Assisted BuildFFAStats buildFFAStats,
      Plugin plugin,
      BuildFFAPlayerManager buildFFAPlayerManager,
      WorldManager worldManager,
      PrestigeManager prestigeManager,
      ExtraItemManager extraItemManager,
      Bow bow,
      SnowBall snowBall,
      KitLoader kitLoader) {
    this.plugin = plugin;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.worldManager = worldManager;
    this.prestigeManager = prestigeManager;
    this.extraItemManager = extraItemManager;
    this.bow = bow;
    this.snowBall = snowBall;
    this.kitLoader = kitLoader;

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
    // TODO(economy): Re-enable the shop item once the coin system is implemented.
    // ItemStack shopItem = new ItemStack(Material.CHEST);
    // ItemUtils.name(shopItem, LangUtils.getMessage(this.player, ItemEnum.SHOP));

    ItemStack leaveItem = new ItemStack(Material.SLIME_BALL);
    ItemUtils.name(leaveItem, LangUtils.getMessage(player, ItemEnum.LEAVE_ITEM));

    ItemStack voteItem = new ItemStack(Material.PAPER);
    ItemUtils.name(voteItem, LangUtils.getMessage(player, InventoryEnum.VOTE));

    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    // player.getInventory().setItem(0, shopItem);
    player.getInventory().setItem(8, leaveItem);
    if (player.hasPermission("teamcocoa.prime")) {
      ItemStack killEffectItem = new ItemStack(Material.GOLD_SWORD);
      ItemUtils.name(killEffectItem, "&cKillEffects");
      player.getInventory().setItem(3, voteItem);
      player.getInventory().setItem(5, killEffectItem);
    } else {
      player.getInventory().setItem(4, voteItem);
    }
  }

  public void arenaJoin() {
    player.closeInventory();
    player.getInventory().clear();
    player.setLevel(0);
    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 100.0F, 0.0F);

    inGame = true;

    kitLoader.apply(this);
    // TODO(economy): Re-enable giving bought items once the coin system is implemented.
    // giveBoughtItems();
  }

  // TODO(economy): Re-enable once the coin system is implemented.
  // private void giveBoughtItems() {
  //   if (bowBought) {
  //     ItemStack bowItem = bow.getItemStack(player, 1);
  //     ItemStack arrowItem = new ItemStack(Material.ARROW, 16);
  //     player.getInventory().addItem(bowItem, arrowItem);
  //   }
  //
  //   if (snowBallBought) {
  //     ItemStack snowballItem = snowBall.getItemStack(player, 16);
  //     player.getInventory().addItem(snowballItem);
  //   }
  // }

  public void kill(BuildFFAPlayer killed) {
    buildFFAStats.addKills(1);
    prestigeManager.updatePrestige(this);
    try {
      player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
      String health = (new DecimalFormat("0.00")).format(player.getHealth());

      killed
          .getPlayer()
          .sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(killed.getPlayer(), MessageEnum.PLAYER_KILL),
                  player.getName(),
                  health));

      player.setLevel(buildFFAStats.getKillStreaks());

      int killStreaks = buildFFAStats.getKillStreaks();

      if (killStreaks != 0 && (killStreaks % 5 == 0 || killStreaks > 15)) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(onlinePlayer, MessageEnum.KILL_STREAK),
                  player.getName(),
                  killStreaks));
        }
      }

      if (killStreaks != 0 && killStreaks % 3 == 0) {
        if (!player.getInventory().contains(new ItemStack(Material.ENDER_PEARL, 2))) {
          player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
        }
        extraItemManager.giveExtraItem(player);

        Stream<ItemStack> inventoryStream = Arrays.stream(player.getInventory().getContents());

        int blocks =
            inventoryStream
                .filter(itemStack -> itemStack != null && itemStack.getType() == Material.SANDSTONE)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (blocks >= 64) {
          return;
        } else {
          player
              .getInventory()
              .addItem(new ItemStack(Material.SANDSTONE, blocks + 15 > 64 ? 64 - blocks : 15));
        }

        // TODO(economy): Re-enable the arrow refill for a bought bow once the coin system is
        // implemented. inventoryStream is already consumed above, so build a new stream here.
        // if (isBowBought()) {
        //   int arrows =
        //       inventoryStream
        //           .filter(itemStack -> itemStack != null && itemStack.getType() ==
        // Material.ARROW)
        //           .mapToInt(ItemStack::getAmount)
        //           .sum();
        //
        //   if (arrows >= 16) {
        //     return;
        //   } else {
        //     player
        //         .getInventory()
        //         .addItem(new ItemStack(Material.ARROW, arrows + 5 > 16 ? 16 - arrows : 5));
        //   }
        // }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void death(boolean quit) {
    if (!quit) {
      Location spawn = worldManager.getCurrentMap().getSpawn();

      player.teleport(spawn);
      player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);

      if (player.equals(getLastHitPlayer())) {
        return;
      }

      if (buildFFAStats.getKillStreaks() >= 5) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(onlinePlayer, MessageEnum.KILL_STREAK_BROKEN),
                  player.getName(),
                  buildFFAStats.getKillStreaks(),
                  getLastHitPlayer().getName()));
        }
      }

      Bukkit.getScheduler().runTask(plugin, () -> setJoinInventory());
    }
    buildFFAStats.addDeaths(1);
    lastHitPlayer = null;
  }

  public void addDamage(Player hitter, double damage) {
    if (this.damageTable.containsKey(hitter)) {
      damage += this.damageTable.get(hitter);
    }
    this.damageTable.put(hitter, damage);
  }

  public void resetDamage() {
    for (Player damagedPlayer : this.damageTable.keySet()) {
      BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(damagedPlayer);
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
    shootAble = false;

    player.setHealth(20);

    resetKBStickDurability();
    resetDamage();

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }
  }
}
