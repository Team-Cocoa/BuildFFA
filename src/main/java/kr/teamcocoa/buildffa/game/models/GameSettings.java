package kr.teamcocoa.buildffa.game.models;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

@Singleton
@Getter
@Setter
public class GameSettings {

  private boolean teaming = false;
}
