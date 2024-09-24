package com.gyzer.legendaryrealms.Data.Quest.Objective;

public enum ObjectiveType {
    KILL_ENTITY(true),KILL_CUSTOM(true),BREAK(true),PLACE(true),GIVE_ITEM(true),TRADE(true),
    CRAFT(true),MOVE(false),GAIN_EXP(true),GAIN_LEVEL(true),GAIN_FOOD(true),TAKE_FOOD(true),
    MILKING(true),
    HARVEST(true),DEATH(true),FISH(true),BREED(true),COOK(true),SHEAR(true),ATTACK_DAMAGE(false),
    PROJECTILE_DAMAGE(false),DAMAGED(false),BLOCKING(false),BLOCKING_PROJECTILE(false),CONSUME(true),
    ENCHANT(true),ITEM_BROKEN(true),CHAT(true),TAME(true),DELAY(true),BREW(true),TRIGGER(true)

    ,AURASKILLS_EXP(false,"AuraSkills"),AURELIUMSKILLS_EXP(false, "AureliumSkills"),MCMMO_EXP(false,"mcMMO"),PLACEHOLDERAPI(false,"PlaceholderAPI");
    private final boolean useInt;
    private final String plugin;

    ObjectiveType(boolean useInt) {
        this.useInt = useInt;
        this.plugin = null;

    }

    ObjectiveType(boolean useInt, String plugin) {
        this.useInt = useInt;
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }

    public boolean isUseInt() {
        return useInt;
    }
}
