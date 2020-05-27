package net.experienceddev.devbot.events;

import br.net.fabiozumbi12.UltimateChat.Sponge.API.SendChannelMessageEvent;
import com.google.inject.Inject;
import net.experienceddev.devbot.DevBot;
import net.experienceddev.devbot.DevMessageObject;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;

public class UltimateChatEvents {
    @Inject
    Logger logger;
    @Listener
    public void uChatMessage(SendChannelMessageEvent event) {
        if (event.getChannel() == null || !(event.getSender() instanceof Player)){
            return;
        }
        Player player = (Player) event.getSender();
        String channelID = DevBot.cfg.chatChannelDiscordChannel.getOrDefault(event.getChannel().getName(), DevBot.cfg.discordChannelID);

        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setDiscordChannelID(channelID);
        msgObj.setMinecraftUUID(player.getUniqueId());
        msgObj.setMinecraftName(player.getName());
        msgObj.setMsg(event.getMessage().toPlain());
        msgObj.setType(DevMessageObject.TYPE.CHAT);
        msgObj.setNotes("ChannelName: " + event.getChannel().getName());
        DevBot.devMqttClient.publishMessage(msgObj);
    }
}
