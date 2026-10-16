package kr.teamcocoa.buildffa.game.economy;

import com.google.inject.Singleton;
import org.bukkit.entity.Player;

// TODO(economy): Stub until a currency system is chosen. Replace with the real coin system.
@Singleton
public class CurrencyService {

  // TODO(economy): Return the player's real balance.
  public long getBalance(Player player) {
    return 0L;
  }

  // TODO(economy): Withdraw `amount` from the player's balance. Return false if it is not enough.
  public boolean withdraw(Player player, long amount) {
    return false;
  }
}
