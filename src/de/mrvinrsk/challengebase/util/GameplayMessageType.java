package de.mrvinrsk.challengebase.util;

import java.util.Arrays;
import java.util.List;

public enum GameplayMessageType {

    SYSTEM(" §8» <<Message>>", "§7§o");

    private String format;
    private String standardColorFormatting;

    GameplayMessageType(String format, String standardColorFormatting) {
        this.format = format;
        this.standardColorFormatting = standardColorFormatting;
    }

    public String getFormat() {
        return format;
    }

    public String getStandardColorFormatting() {
        return standardColorFormatting;
    }

    public String getFormattedMessage(String message) {
        String msg = message.replace("§r", getStandardColorFormatting());

        List<Character> colorCodes = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

        if (getStandardColorFormatting().contains("§k")) {
            for(Character code : colorCodes) {
                msg = msg.replace("§" + code, "§" + code + "§k");
            }
        }
        if (getStandardColorFormatting().contains("§l")) {
            for(Character code : colorCodes) {
                msg = msg.replace("§" + code, "§" + code + "§l");
            }
        }
        if (getStandardColorFormatting().contains("§m")) {
            for(Character code : colorCodes) {
                msg = msg.replace("§" + code, "§" + code + "§m");
            }
        }
        if (getStandardColorFormatting().contains("§n")) {
            for(Character code : colorCodes) {
                msg = msg.replace("§" + code, "§" + code + "§n");
            }
        }
        if (getStandardColorFormatting().contains("§o")) {
            for(Character code : colorCodes) {
                msg = msg.replace("§" + code, "§" + code + "§o");
            }
        }

        return getFormat().replace("<<Message>>", getStandardColorFormatting() + msg);
    }

}