package kr.teamcocoa.buildffa.enums;

public enum ItemEnum {

    INVENTORY_SORTING("item_inventory_sorting"),
    LEAVE_ITEM("item_leave_item"),
    KIT("item_kit"),
    SAVE("item_save"),
    RESET("item_reset"),
    DEFAULT_KIT("item_kit_default"),
    ARCHER_KIT("item_kit_archer"),
    FISHER_KIT("item_kit_fisher");


    private final String name;

    public String getName() {
        return name;
    }

    private ItemEnum(String name) {
        this.name = name;
    }
}
