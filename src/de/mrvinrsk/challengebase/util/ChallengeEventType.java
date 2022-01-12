package de.mrvinrsk.challengebase.util;

import org.bukkit.Material;

/**
 * Represents the type of an event.
 */
public enum ChallengeEventType {

    /**
     * Positive event.
     */
    POSITIVE("§2Positiv", Material.LIME_WOOL),

    /**
     * Negative event.
     */
    NEGATIVE("§cNegativ", Material.RED_WOOL),

    /**
     * Event without defined type.
     */
    UNKNOWN("§fUnbestimmt", Material.LIGHT_GRAY_WOOL),

    /**
     * Event which includes point transactions.
     */
    POINTS("§6Punkte", Material.GOLD_INGOT);

    private String iconTitle;
    private Material material;

    ChallengeEventType(String iconTitle, Material material) {
        this.iconTitle = iconTitle;
        this.material = material;
    }

    /**
     * Get the title of the icon from the {@link de.mrvinrsk.challengebase.commands.Command_Event} inventory.
     *
     * @return the title.
     */
    public String getIconTitle() {
        return iconTitle;
    }

    /**
     * Get the material of the icon from the {@link de.mrvinrsk.challengebase.commands.Command_Event} inventory.
     *
     * @return
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Get the type by name.
     *
     * @param title the title.
     * @return the {@link ChallengeEventType}.
     */
    public static ChallengeEventType getByIconTitle(String title) {
        for (ChallengeEventType type : values()) {
            if (type.getIconTitle().equalsIgnoreCase(title)) {
                return type;
            }
        }
        return null;
    }

}
