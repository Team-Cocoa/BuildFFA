package kr.teamcocoa.buildffa.enums;

public enum InventoryEnum {

    KIT_SELECT("kit_selection"),
    INVENTORY_SORTING("inventory_sorting"),
    VOTE("vote"),
    EXTRA_ITEM("extra_item");

    private final String name;

    public String getName() {
        return name;
    }

    private InventoryEnum(String name) {
        this.name = "inventory." + name;
    }
}
