package com.craftminerd.handheld_utilities.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class HandheldSmithing extends Item {
    public HandheldSmithing(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        Component component = new TranslatableComponent("container.upgrade");
        if (!pPlayer.getItemInHand(pUsedHand).getHoverName().equals(this.getDefaultInstance().getHoverName())) {
            component = pPlayer.getItemInHand(pUsedHand).getHoverName();
        }
        NetworkHooks.openGui((ServerPlayer) pPlayer, new SimpleMenuProvider((id, inventory, player) -> {
            return new SmithingMenu(id, inventory, ContainerLevelAccess.create(pLevel,
                    new BlockPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ()))) {
                @Override
                public boolean stillValid(Player pPlayer) {
                    return pPlayer.getItemInHand(pUsedHand).getItem() instanceof HandheldSmithing;
                }
            };
        }, component));
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }
}