package de.mrvinrsk.challengebase.util;

import org.bukkit.Material;

public enum ChallengeEventType {

    POSITIVE("§2Positiv", Material.LIME_WOOL),
    NEGATIVE("§cNegativ", Material.RED_WOOL),
    UNKNOWN("§fUnbestimmt", Material.LIGHT_GRAY_WOOL),
    POINTS("§6Punkte", Material.GOLD_INGOT);

    private String iconTitle;
    private Material material;

    ChallengeEventType(String iconTitle, Material material) {
        this.iconTitle = iconTitle;
        this.material = material;
    }

    public String getIconTitle() {
        return iconTitle;
    }

    public Material getMaterial() {
        return material;
    }

    public static ChallengeEventType getByIconTitle(String title){
        for(ChallengeEventType type : values()) {
            if(type.getIconTitle().equalsIgnoreCase(title)) {
                return type;
            }
        }
        return null;
    }

}
