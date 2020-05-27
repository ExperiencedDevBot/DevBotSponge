package net.experienceddev.devbot;

import java.util.UUID;

public class DevMessageObject {
    public enum TYPE{
        CONNECTED,
        DISCONNECTED,
        CHAT,
        VERIFY,
        STATUS_ONLINE,
        STATUS_OFFLINE
    }
    private String topic;
    private String msg;
    private TYPE type;
    /*private DevPlayer player;
    private DevUser user;*/
    private UUID minecraftUUID;
    private String minecraftName;
    private String discordUUID;
    private String discordChannelID;

    private String notes;

    public DevMessageObject() {
        this.topic = DevBot.cfg.mqttTopic;
        this.discordChannelID = DevBot.cfg.discordChannelID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public UUID getMinecraftUUID() {
        return minecraftUUID;
    }

    public void setMinecraftUUID(UUID minecraftUUID) {
        this.minecraftUUID = minecraftUUID;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    public String getDiscordUUID() {
        return discordUUID;
    }

    public void setDiscordUUID(String discordUUID) {
        this.discordUUID = discordUUID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiscordChannelID() {
        return discordChannelID;
    }

    public void setDiscordChannelID(String discordChannelID) {
        this.discordChannelID = discordChannelID;
    }

    public void prepareMessage(){
        if (discordChannelID.equalsIgnoreCase("default")){
            this.discordChannelID = DevBot.cfg.discordChannelID;
        }
    }
}

class DevPlayer {
    private String UUID;
}

class DevUser {
    private String discordUserID;
}
