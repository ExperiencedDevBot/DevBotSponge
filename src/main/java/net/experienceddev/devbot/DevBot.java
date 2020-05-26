package net.experienceddev.devbot;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "devbot",
        name = "DevBot",
        description = "Discord bridge",
        authors = {
                "ExperiencedDev"
        }
)
public class DevBot {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
