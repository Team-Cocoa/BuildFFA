package kr.teamcocoa.buildffa.models;

import kr.teamcocoa.core.network.cache.Cache;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BuildFFAStatsManager {

    @Getter
    private static Cache<UUID, BuildFFAStats> cache = new Cache<>(6, TimeUnit.HOURS);

}
