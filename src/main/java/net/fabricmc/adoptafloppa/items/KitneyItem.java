package net.fabricmc.adoptafloppa.items;

import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.fabricmc.adoptafloppa.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class KitneyItem extends Item {
    public KitneyItem(Settings settings){
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand){
        playerEntity.playSound(ModSounds.YIPPEE_EVENT, 0.8F, 1.0F);
        playerEntity.getItemCooldownManager().set(this, 5);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
