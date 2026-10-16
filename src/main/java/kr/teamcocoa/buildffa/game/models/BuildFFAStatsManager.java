package kr.teamcocoa.buildffa.game.models;

import com.google.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import kr.teamcocoa.core.network.cache.Cache;
import lombok.Getter;

@Singleton
public class BuildFFAStatsManager {

  @Getter private final Cache<UUID, BuildFFAStats> cache = new Cache<>(6, TimeUnit.HOURS);
}
