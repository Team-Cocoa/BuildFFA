package kr.teamcocoa.buildffa.task;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldRuleTask implements Runnable {

  @Override
  public void run() {
    for (World world : Bukkit.getWorlds()) {
      world.setGameRuleValue("announceAdvancements", "false");
      world.setGameRuleValue("logAdminCommands", "false");
      world.setGameRuleValue("doDaylightCycle", "false");
      world.setTime(0);
    }
  }
}
