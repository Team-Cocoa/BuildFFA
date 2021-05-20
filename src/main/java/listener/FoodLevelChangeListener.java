package listener;

import utils.Config;
import utils.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent e) {
    if (e.getEntity() instanceof Player) {
      Player p = (Player)e.getEntity();
      Location loc = p.getLocation();
      if (Config.config.getBoolean("hunger")) {
        if (loc.getY() > Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".arenaheight"))
          e.setCancelled(true); 
      } else {
        e.setCancelled(true);
      } 
    } 
  }
}
