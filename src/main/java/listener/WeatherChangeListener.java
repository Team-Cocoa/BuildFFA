package listener;

import utils.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {
  @EventHandler
  public static void onWeatherChange(WeatherChangeEvent e) {
    if (!Config.config.getBoolean("weather"))
      e.setCancelled(true); 
  }
}
