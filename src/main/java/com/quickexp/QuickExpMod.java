package com.quickexp;

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

	private static final int THROW_DELAY_TICKS = 1; // Задержка между выбрасываниями
	private int tickCounter = 0;
	private boolean isEnabled = true;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing QuickExp Mod");

		// Регистрация обработчика тиков на клиентской стороне
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!isEnabled || client.player == null || client.world == null || client.interactionManager == null) {
				tickCounter = 0;
				return;
			}

			ClientPlayerEntity player = client.player;
			ItemStack mainHand = player.getMainHandStack();
			ItemStack offHand = player.getOffHandStack();

			// Проверяем, зажата ли ПКМ и есть ли бутылочка опыта в любой руке
			if (client.options.keyUse.isPressed() &&
					(mainHand.getItem() == Items.EXPERIENCE_BOTTLE || offHand.getItem() == Items.EXPERIENCE_BOTTLE)) {
				tickCounter++;
				Hand hand = mainHand.getItem() == Items.EXPERIENCE_BOTTLE ? Hand.MAIN_HAND : Hand.OFF_HAND;

				if (tickCounter >= THROW_DELAY_TICKS) {
					// Имитируем нажатие ПКМ
					client.interactionManager.interactItem(player, client.world, hand);
					player.swingHand(hand);
					tickCounter = 0;
				}
			} else {
				tickCounter = 0;
			}
		});
	}
}