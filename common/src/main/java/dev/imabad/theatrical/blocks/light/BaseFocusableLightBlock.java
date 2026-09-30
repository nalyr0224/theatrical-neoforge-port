package dev.imabad.theatrical.blocks.light;

import dev.imabad.theatrical.api.FocusableFixture;
import dev.imabad.theatrical.items.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseFocusableLightBlock extends BaseLightBlock {
    protected BaseFocusableLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemInteractionResult superResult = super.useItemOn(stack, state, level, pos, player, hand, hit);
        if(superResult == ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) {
            if (!level.isClientSide()) {
                if(stack.is(Items.FIXTURE_FOCUSER.get())){
                    CompoundTag itemTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                    if(level.getBlockEntity(pos) instanceof FocusableFixture focusableFixture) {
                        if (!itemTag.contains("Light") && focusableFixture.getTrackingEntity() == null) {
                            itemTag.put("Light", NbtUtils.writeBlockPos(pos));
                            focusableFixture.setTrackingEntity(player);
                        } else if(focusableFixture.getTrackingEntity() != null) {
                            focusableFixture.setTrackingEntity(null);
                        }
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(itemTag));
                    }
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return superResult;
    }
}