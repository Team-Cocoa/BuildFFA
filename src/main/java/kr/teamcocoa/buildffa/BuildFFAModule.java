package kr.teamcocoa.buildffa;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFFAModule extends AbstractModule {

  private final BuildFFABootstrap plugin;

  public BuildFFAModule(BuildFFABootstrap plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(BuildFFABootstrap.class).toInstance(plugin);
    bind(JavaPlugin.class).toInstance(plugin);
    bind(Plugin.class).toInstance(plugin);

    install(new FactoryModuleBuilder().build(BuildFFAPlayer.Factory.class));
    install(new FactoryModuleBuilder().build(DeSpawnBlock.Factory.class));
  }

  @Provides
  FileConfiguration provideConfig() {
    return plugin.getConfig();
  }
}
