package kr.teamcocoa.buildffa.task;

import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;

public class DeSpawnBlockTask implements Runnable {

  private final BuildFFAMap map;

  public DeSpawnBlockTask(BuildFFAMap map) {
    this.map = map;
  }

  @Override
  public void run() {
    for (DeSpawnBlock deSpawnBlock : map.getBlocks()) {
      deSpawnBlock.tick();
      if (deSpawnBlock.isExpire()) {
        map.removeBlock(deSpawnBlock);
      }
    }
  }
}
