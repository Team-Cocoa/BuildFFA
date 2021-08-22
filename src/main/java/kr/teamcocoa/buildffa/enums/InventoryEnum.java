package kr.teamcocoa.buildffa.enums;

public enum InventoryEnum {

    KIT_SELECT("inventory_kit_selection"),
    INVENTORY_SORTING("inventory_inventory_sorting");

    private final String name;

    public String getName() {
        return name;
    }

    private InventoryEnum(String name) {
        this.name = name;
    }
}
