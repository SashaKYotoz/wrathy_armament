package net.sashakyotoz.wrathy_armament.utils.capabilities.items;

public enum XPTiers {
    XP_FOR_FIRST_TIER(100),
    XP_FOR_SECOND_TIER(250),
    XP_FOR_THIRD_TIER(500),
    XP_FOR_FOURTH_TIER(750),
    XP_FOR_FIFTH_TIER(1000);

    public int getNeededXP() {
        return neededXP;
    }

    private final int neededXP;
    XPTiers(int xpNeededForLevelUp){
        this.neededXP = xpNeededForLevelUp;
    }
}