package net.fabricmc.adoptafloppa.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoundItem extends Item {
    private SoundEvent sound;
    private int cooldown = 0;
    public SoundItem(Settings settings, SoundEvent _sound, int _cooldown){
        super(settings);
        sound = _sound;
        this.cooldown = _cooldown;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand){
        playerEntity.playSound(sound, 0.9F, 1.0F);
        if (this.cooldown != 0) {
            playerEntity.getItemCooldownManager().set(this, this.cooldown);
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
