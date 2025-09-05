package com.cardejibka.quickexp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod(QuickExpMod.MODID)
@EventBusSubscriber(modid = QuickExpMod.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class QuickExpMod {
    public static final String MODID = "quickexp";
    private boolean isRightClickHeld = false; // Флаг зажатия ПКМ
    private int tickCounter = 0; // Счётчик тиков для задержки
    private static final int THROW_DELAY_TICKS = 1;

    public QuickExpMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Отслеживание нажатия и отпускания ПКМ
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null && (player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE ||
                player.getOffhandItem().getItem() == Items.EXPERIENCE_BOTTLE)) {
            if (event.getButton() == 1) { // ПКМ = кнопка 1
                isRightClickHeld = event.getAction() == 1; // 1 = нажатие, 0 = отпускание
            }
        } else {
            isRightClickHeld = false; // Сбрасываем, если пузырёк не в руке
            tickCounter = 0;
        }
    }

    // Обработка тиков клиента
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null && isRightClickHeld && (player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE ||
                    player.getOffhandItem().getItem() == Items.EXPERIENCE_BOTTLE)) {
                // Определяем, какая рука используется
                boolean isMainHand = player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE;
                InteractionHand hand = isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

                // Имитация использования предмета
                if (tickCounter >= THROW_DELAY_TICKS) {
                    mc.gameMode.useItem(player, hand); // Аналог interactItem в Fabric
                    player.swing(hand, true); // Анимация руки
                    // Воспроизводим звук
                    player.level().playLocalSound(player.getX(), player.getY(), player.getZ(),
                            SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.PLAYERS, 0.5F,
                            0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F), false);
                    tickCounter = 0;
                }
                tickCounter++;
            } else {
                isRightClickHeld = false; // Сбрасываем флаг, если пузырёк не в руке
                tickCounter = 0;
            }
        }
    }
}