package kr.teamcocoa.buildffa.game.kit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.kitmanager.api.KitCodec;
import kr.teamcocoa.kitmanager.api.KitData;
import kr.teamcocoa.kitmanager.api.KitDecodeException;
import kr.teamcocoa.kitmanager.api.KitManagerApi;
import kr.teamcocoa.kitmanager.api.Kits;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

@Singleton
public class KitLoader {

  private static final long LOAD_TIMEOUT_SECONDS = 5L;

  private final Plugin plugin;

  @Inject
  public KitLoader(Plugin plugin) {
    this.plugin = plugin;
  }

  public static boolean isAvailable(Plugin plugin) {
    return plugin.getServer().getServicesManager().getRegistration(KitManagerApi.class) != null;
  }

  /** Blocks until the kit is fetched, so it must not be called on the main thread. */
  public ItemStack[] load(Player player) {
    KitManagerApi kitManager = getKitManager();
    if (kitManager == null) {
      plugin.getLogger().warning("KitManager service is not registered, cannot load kits");
      return null;
    }

    Map<String, KitData> kits;
    try {
      kits =
          kitManager
              .get(player.getUniqueId(), Kits.BUILD_FFA)
              .get(LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    } catch (Exception e) {
      plugin.getLogger().log(Level.WARNING, "Failed to load " + player.getName() + "'s kit", e);
      return null;
    }

    return decode(player, kits);
  }

  public void apply(BuildFFAPlayer buildFFAPlayer) {
    ItemStack[] kit = buildFFAPlayer.getKit();
    if (kit == null) {
      plugin.getLogger().warning(buildFFAPlayer.getPlayer().getName() + " has no loaded kit");
      return;
    }

    Player player = buildFFAPlayer.getPlayer();
    player.getInventory().setContents(kit);
    player.updateInventory();
  }

  private ItemStack[] decode(Player player, Map<String, KitData> kits) {
    KitData data = kits == null ? null : kits.get(Kits.BUILD_FFA);
    if (data == null) {
      plugin.getLogger().warning(player.getName() + " has no " + Kits.BUILD_FFA + " kit");
      return null;
    }

    try {
      return KitCodec.decode(data);
    } catch (KitDecodeException e) {
      plugin.getLogger().log(Level.WARNING, "Failed to decode " + player.getName() + "'s kit", e);
      return null;
    }
  }

  private KitManagerApi getKitManager() {
    RegisteredServiceProvider<KitManagerApi> provider =
        plugin.getServer().getServicesManager().getRegistration(KitManagerApi.class);
    return provider == null ? null : provider.getProvider();
  }
}
