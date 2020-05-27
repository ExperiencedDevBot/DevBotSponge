package net.experienceddev.devbot;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Utils {
    public static Text format(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }
}
