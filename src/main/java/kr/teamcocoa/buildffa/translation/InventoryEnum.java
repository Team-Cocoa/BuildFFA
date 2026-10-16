package kr.teamcocoa.buildffa.translation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InventoryEnum implements Translatable {
  KIT_SELECT("kit_selection"),
  INVENTORY_SORTING("inventory_sorting"),
  VOTE("vote"),
  EXTRA_ITEM("shop");

  private final String key;

  @Override
  public String getName() {
    return "inventory." + key;
  }
}
