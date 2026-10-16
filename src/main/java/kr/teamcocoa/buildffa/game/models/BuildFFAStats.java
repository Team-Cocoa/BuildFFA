package kr.teamcocoa.buildffa.game.models;

import java.util.UUID;
import lombok.Getter;

// Mutated on the main thread and saved from other threads, so every access is synchronized
public class BuildFFAStats {

  @Getter private final UUID uuid;

  private int kills;
  private int deaths;
  private int killStreaks;
  private int bestKillStreaks;

  private boolean edited;

  private long revision;

  public BuildFFAStats(UUID uuid) {
    this.uuid = uuid;
  }

  public synchronized void init(int kills, int deaths, int killStreaks, int bestKillStreaks) {
    this.kills = kills;
    this.deaths = deaths;
    this.killStreaks = killStreaks;
    this.bestKillStreaks = bestKillStreaks;
    this.edited = false;
  }

  public synchronized int getKills() {
    return kills;
  }

  public synchronized int getDeaths() {
    return deaths;
  }

  public synchronized int getKillStreaks() {
    return killStreaks;
  }

  public synchronized int getBestKillStreaks() {
    return bestKillStreaks;
  }

  public synchronized boolean isEdited() {
    return edited;
  }

  public synchronized long getRevision() {
    return revision;
  }

  /** Clears the edited flag only if nothing changed since the saved snapshot was taken. */
  public synchronized void markSaved(long savedRevision) {
    if (revision == savedRevision) {
      edited = false;
    }
  }

  public synchronized void addKills(int i) {
    this.kills += i;
    addKillStreaks(i);
    markEdited();
  }

  public synchronized void addDeaths(int i) {
    this.deaths += i;
    resetKillStreaks();
    markEdited();
  }

  public synchronized void addKillStreaks(int i) {
    this.killStreaks += i;
    if (killStreaks > bestKillStreaks) {
      this.bestKillStreaks = killStreaks;
    }
  }

  public synchronized void resetKillStreaks() {
    this.killStreaks = 0;
  }

  private void markEdited() {
    this.edited = true;
    this.revision++;
  }
}
