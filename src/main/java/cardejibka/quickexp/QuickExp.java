package cardejibka.quickexp;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(QuickExp.MOD_ID)
public class QuickExp {

    public static final String MOD_ID = "quickexp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public QuickExp() {
        LOGGER.info("QuickExp loaded – NeoForge 1.21");
    }
}