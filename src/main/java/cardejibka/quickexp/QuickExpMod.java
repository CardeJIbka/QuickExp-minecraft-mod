package cardejibka.quickexp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickExpMod implements ModInitializer {
	public static final String MOD_ID = "quickexp";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final int THROW_DELAY_TICKS = 1;
	private int tickCounter = 0;
	private final boolean isEnabled = true;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing QuickExp Mod");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!isEnabled || client.player == null || client.world == null || client.interactionManager == null) {
				resetTickCounter();
				return;
			}

			ClientPlayerEntity player = client.player;

			ItemStack mainHand = player.getMainHandStack();
			ItemStack offHand = player.getOffHandStack();

			boolean hasBottle = (!mainHand.isEmpty() && mainHand.getItem() == Items.EXPERIENCE_BOTTLE) ||
					(!offHand.isEmpty() && offHand.getItem() == Items.EXPERIENCE_BOTTLE);

			if (client.options.useKey.isPressed() && hasBottle && tickCounter <= 0) {
				Hand hand = !mainHand.isEmpty() && mainHand.getItem() == Items.EXPERIENCE_BOTTLE ? Hand.MAIN_HAND : Hand.OFF_HAND;

				client.interactionManager.interactItem(player, hand);
				player.swingHand(hand);
				tickCounter = THROW_DELAY_TICKS;

				LOGGER.debug("Thrown XP bottle from {} (cooldown started)", hand);
			}

			if (tickCounter > 0) {
				tickCounter--;
			}
		});
	}

	private void resetTickCounter() {
		tickCounter = 0;
	}
}