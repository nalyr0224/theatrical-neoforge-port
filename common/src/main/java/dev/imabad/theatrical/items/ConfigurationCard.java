package dev.imabad.theatrical.items;

import dev.imabad.theatrical.Theatrical;
import dev.imabad.theatrical.client.gui.screen.ConfigurationCardScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class ConfigurationCard extends Item {
    public ConfigurationCard() {
        super(new Item.Properties().arch$tab(Theatrical.TAB));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isCrouching() && level.isClientSide()) {
            CompoundTag cardData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            openUI(cardData);
            return InteractionResultHolder.pass(stack);
        }
        return super.use(level, player, usedHand);
    }

    private static void openUI(CompoundTag data) {
        Minecraft.getInstance().setScreen(new ConfigurationCardScreen(data));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable("item.configurationcard.description.1"));
        tooltipComponents.add(Component.translatable("item.configurationcard.description.2"));
    }
}