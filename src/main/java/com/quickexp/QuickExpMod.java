package com.quickexp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("quickexp")
public class QuickExpMod {
	public static final String MOD_ID = "quickexp";
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final int THROW_DELAY_TICKS = 1; // Задержка между выбрасываниями
	private int tickCounter = 0;
	private boolean isEnabled = true;

	public QuickExpMod() {
		LOGGER.info("Initializing QuickExp Mod");
		// Регистрация обработчика событий
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		// Выполняем логику только в фазе END
		if (event.phase != TickEvent.Phase.END) {
			return;
		}

		Minecraft client = Minecraft.getInstance();
		
		if (!isEnabled || client.player == null || client.world == null || client.playerController == null) {
			tickCounter = 0;
			return;
		}

		ClientPlayerEntity player = client.player;
		ItemStack mainHand = player.getHeldItemMainhand();
		ItemStack offHand = player.getHeldItemOffhand();

		// Проверяем, зажата ли ПКМ и есть ли бутылочка опыта в любой руке
		if (client.gameSettings.keyBindUseItem.isKeyDown() &&
				(mainHand.getItem() == Items.EXPERIENCE_BOTTLE || offHand.getItem() == Items.EXPERIENCE_BOTTLE)) {
			tickCounter++;
			Hand hand = mainHand.getItem() == Items.EXPERIENCE_BOTTLE ? Hand.MAIN_HAND : Hand.OFF_HAND;

			if (tickCounter >= THROW_DELAY_TICKS) {
				// Имитируем нажатие ПКМ
				client.playerController.processRightClick(player, client.world, hand);
				player.swingArm(hand);
				tickCounter = 0;
			}
		} else {
			tickCounter = 0;
		}
	}
}
