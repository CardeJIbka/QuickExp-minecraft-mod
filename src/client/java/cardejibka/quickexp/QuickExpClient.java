package cardejibka.quickexp;

import cardejibka.quickexp.packets.OptOutPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickExpClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(QuickExpMod.MOD_ID);

    private static final int THROW_DELAY_TICKS = 1;
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.serverboundPlay().register(OptOutPacket.ID, OptOutPacket.CODEC);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPlayNetworking.send(new OptOutPacket());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null || client.gameMode == null) {
                tickCounter = 0;
                return;
            }

            LocalPlayer player = client.player;

            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();

            boolean hasBottle = (!mainHand.isEmpty() && mainHand.getItem() == Items.EXPERIENCE_BOTTLE) ||
                    (!offHand.isEmpty() && offHand.getItem() == Items.EXPERIENCE_BOTTLE);

            if (client.options.keyUse.isDown() && hasBottle && tickCounter <= 0) {
                InteractionHand hand = !mainHand.isEmpty() && mainHand.getItem() == Items.EXPERIENCE_BOTTLE
                        ? InteractionHand.MAIN_HAND
                        : InteractionHand.OFF_HAND;

                client.gameMode.useItem(player, hand);
                player.swing(hand);
                tickCounter = THROW_DELAY_TICKS;

                LOGGER.debug("Thrown XP bottle from {} (cooldown started)", hand);
            }

            if (tickCounter > 0) {
                tickCounter--;
            }
        });
    }
}