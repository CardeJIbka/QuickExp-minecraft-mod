package cardejibka.quickexp;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickExpMod implements ModInitializer {
    public static final String MOD_ID = "quickexp";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing QuickExp Mod");
    }
}