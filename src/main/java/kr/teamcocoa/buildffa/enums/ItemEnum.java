package kr.teamcocoa.buildffa.enums;

public enum ItemEnum {

    INVENTORY_SORTING("inventory_sorting"),
    LEAVE_ITEM("leave_item"),
    KIT("kit"),
    SAVE("save"),
    RESET("reset"),
    DEFAULT_KIT("kit_default"),
    ARCHER_KIT("kit_archer"),
    FISHER_KIT("kit_fisher"),
    CANCEL_VOTE("cancel_vote");


    private final String name;

    public String getName() {
        return name;
    }

    private ItemEnum(String name) {
        this.name = "item." + name;
    }
}
