package kr.teamcocoa.buildffa.translate;

public enum InventoryNode implements Translatable {

    KIT_SELECT("kit_selection"),
    INVENTORY_SORTING("inventory_sorting"),
    VOTE("vote"),
    EXTRA_ITEM("shop");

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    private InventoryNode(String name) {
        this.name = "inventory." + name;
    }
}
