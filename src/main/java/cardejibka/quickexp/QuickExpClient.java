package cardejibka.quickexp;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(value = QuickExp.MOD_ID, dist = Dist.CLIENT)
public class QuickExpClient {

    private static final Logger LOGGER = LogUtils.getLogger();

    public QuickExpClient() {
        LOGGER.info("QuickExpClient initialized – client only");

        // Регистрируем слушатель событий ТОЛЬКО на клиенте
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null || mc.options == null) return;

        LocalPlayer player = mc.player;

        if (!mc.options.keyUse.isDown()) return;

        boolean hasMain = player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE;
        boolean hasOff  = player.getOffhandItem().getItem() == Items.EXPERIENCE_BOTTLE;

        if (!hasMain && !hasOff) return;

        InteractionHand hand = hasMain ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        mc.gameMode.useItem(player, hand);
        player.swing(hand);

        LOGGER.debug("QuickExp: Thrown XP bottle from {}", hand);
    }
}