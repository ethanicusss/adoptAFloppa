package net.fabricmc.adoptafloppa.items;

import net.fabricmc.adoptafloppa.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AughItem extends Item {
    public AughItem(Settings settings){
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand){
        playerEntity.playSound(ModSounds.AUGH_EVENT, 0.9F, 1.0F);
        playerEntity.getItemCooldownManager().set(this, 7);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
