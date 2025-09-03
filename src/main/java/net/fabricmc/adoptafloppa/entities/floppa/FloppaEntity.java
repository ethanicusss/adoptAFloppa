package net.fabricmc.adoptafloppa.entities.floppa;

import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

import java.util.Iterator;
import java.util.List;

public class FloppaEntity extends PathAwareEntity {
    private int sniffCooldown = 0;
    private static final int sniffDelay = 60;
    private final int hungeryTime = 31000;
    private static final TrackedData<Integer> hungeryTimer;
    private final int giftTime = 96000;
    private static final TrackedData<Integer> giftTimer;
    private static final Ingredient EATING_INGREDIENT = Ingredient.ofItems(Items.TROPICAL_FISH, Items.SALMON, Items.COD);
    private static final Identifier GIFT_LOOT_ID = new Identifier(AdoptAFloppa.MOD_ID, "gameplay/floppa_gift");

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 0.5));
        this.goalSelector.add(2, new TemptGoal(this, 1.0, EATING_INGREDIENT, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.2));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }
    @Override
    protected void playHurtSound(DamageSource source){
        this.setHungeryTimer(this.getHungeryTimer() - 5167);
        super.playHurtSound(source);
    }

    public FloppaEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.sniffCooldown == 0) {
            player.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_FOX_SNIFF, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
            sniffCooldown = sniffDelay + (int)(this.random.nextFloat() * 20) - 10;
        }
        super.onPlayerCollision(player);
    }

    @Override
    public void tick() {
        if (sniffCooldown > 0){
            sniffCooldown--;
        }
        //this.setHungeryTimer(this.getHungeryTimer() - 1); no more hunger
        /*if (this.getHungeryTimer() == 2400){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CAT_HISS, SoundCategory.NEUTRAL, 1.0F, 1.0F + this.random.nextFloat() * 0.1F);
        }*/
        if (this.getHungeryTimer() <= hungeryTime/2){
            float f = this.random.nextFloat() * ((float) Math.PI * 2);
            float g = this.random.nextFloat() * 0.5f + 0.5f;
            float h = MathHelper.sin(f) * 0.5f * g;
            float k = MathHelper.cos(f) * 0.5f * g;
            float y = this.random.nextFloat() * 0.5f;
            this.world.addParticle(ParticleTypes.ASH, this.getX() + (double) h, this.getY() + y, this.getZ() + (double) k, 0.0, 0.0, 0.0);

        }
        /*if (this.getHungeryTimer() == 200){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CAT_HISS, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        }*/
        if (this.getHungeryTimer() <= hungeryTime/6*2){
            for (int j = 0; j < 2; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2);
                float g = this.random.nextFloat() * 0.5f + 0.5f;
                float h = MathHelper.sin(f) * 0.5f * g;
                float k = MathHelper.cos(f) * 0.5f * g;
                float y = this.random.nextFloat() * 0.5f;
                this.world.addParticle(ParticleTypes.SMOKE, this.getX() + (double)h, this.getY() + y, this.getZ() + (double)k, 0.0, 0.0, 0.0);
            }
        }
        /*if (this.getHungeryTimer() == 60){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CAT_HISS, SoundCategory.NEUTRAL, 1.0F, 0.6F + this.random.nextFloat() * 0.1F);
        }*/
        if (this.getHungeryTimer() <= hungeryTime/6){
            for (int j = 0; j < 2; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2);
                float g = this.random.nextFloat() * 0.5f + 0.5f;
                float h = MathHelper.sin(f) * 0.5f * g;
                float k = MathHelper.cos(f) * 0.5f * g;
                float y = this.random.nextFloat() * 0.5f;
                this.world.addParticle(ParticleTypes.FLAME, this.getX() + (double)h, this.getY() + y, this.getZ() + (double)k, 0.0, 0.0, 0.0);
            }
        }
        if (this.getHungeryTimer() <= 0){
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.dead = true;
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2, destructionType);
            this.discard();
        }
        if (this.getGiftTimer() > 0) {
            this.setGiftTimer(this.getGiftTimer() - 1);
        }
        else{
            if ((float)this.getGiftTimer() / 30 == MathHelper.floor((float)this.getGiftTimer() / 30)) {
                float f = this.random.nextFloat() * ((float) Math.PI * 2);
                float g = this.random.nextFloat() * 0.5f + 0.5f;
                float h = MathHelper.sin(f) * 0.5f * g;
                float k = MathHelper.cos(f) * 0.5f * g;
                float y = 1.0f;
                this.world.addParticle(ParticleTypes.HEART, this.getX() + (double) h, this.getY() + y, this.getZ() + (double) k, 0.0, 0.0, 0.0);
            }
        }

        super.tick();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean food_bl = EATING_INGREDIENT.test(itemStack);
        ActionResult actionResult = super.interactMob(player, hand);
        if (!actionResult.isAccepted()) {
            if (food_bl) {
                if (this.getGiftTimer() > 1200) {
                    setGiftTimer(getGiftTimer() - 1200);
                }
                this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
                for (int j = 0; j < 8; ++j) {
                    float f = this.random.nextFloat() * ((float)Math.PI * 2);
                    float g = this.random.nextFloat() * 0.5f + 0.5f;
                    float h = MathHelper.sin(f) * 0.5f * g;
                    float k = MathHelper.cos(f) * 0.5f * g;
                    this.world.addParticle(ParticleTypes.HEART, this.getX() + (double)h, this.getY() + 1.0f, this.getZ() + (double)k, 0.0, 0.0, 0.0);
                }
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                this.setHungeryTimer(hungeryTime);
                this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
                if (this.world.isClient) {
                    return ActionResult.SUCCESS;
                }
            }
            else{
                if (this.getGiftTimer() == 0){
                    this.setGiftTimer(giftTime + (int)(this.random.nextFloat()*48000));
                    this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
                    if (!this.getWorld().isClient) {
                        LootTable GIFT_LOOT = player.world.getServer().getLootManager().getTable(GIFT_LOOT_ID);
                        List<ItemStack> itemList = GIFT_LOOT.generateLoot((new LootContext.Builder((ServerWorld) this.getWorld())).parameter(LootContextParameters.THIS_ENTITY, player).random(player.world.random).build(LootContextTypes.BARTER));
                        Iterator iterator = itemList.iterator();
                        ItemStack itemStackGive = (ItemStack) iterator.next();
                        this.dropItem(itemStackGive.getItem());
                    }
                    return ActionResult.SUCCESS;
                }
                else{
                    if (this.getGiftTimer() > 20) {
                        setGiftTimer(getGiftTimer() - 20);
                    }
                    this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_CAT_PURR, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
                    float f = this.random.nextFloat() * ((float) Math.PI * 2);
                    float g = this.random.nextFloat() * 0.5f + 0.5f;
                    float h = MathHelper.sin(f) * 0.5f * g;
                    float k = MathHelper.cos(f) * 0.5f * g;
                    float y = 1.0f;
                    this.world.addParticle(ParticleTypes.HEART, this.getX() + (double) h, this.getY() + y, this.getZ() + (double) k, 0.0, 0.0, 0.0);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return actionResult;
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("hungeryTimer", this.getHungeryTimer());
        nbt.putInt("giftTimer", this.getGiftTimer());
    }
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("hungeryTimer")) {
            this.setHungeryTimer(nbt.getInt("hungeryTimer"));
        }
        if (nbt.contains("giftTimer")) {
            this.setGiftTimer(nbt.getInt("giftTimer"));
        }
    }
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(hungeryTimer, hungeryTime);
        this.dataTracker.startTracking(giftTimer, giftTime);
    }
    static {
        hungeryTimer = DataTracker.registerData(net.fabricmc.adoptafloppa.entities.floppa.FloppaEntity.class, TrackedDataHandlerRegistry.INTEGER);
        giftTimer = DataTracker.registerData(net.fabricmc.adoptafloppa.entities.floppa.FloppaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    public int getHungeryTimer() {
        return (Integer)this.dataTracker.get(hungeryTimer);
    }

    public void setHungeryTimer(int val) {
        this.dataTracker.set(hungeryTimer, val);
    }

    public int getGiftTimer() {
        return (Integer)this.dataTracker.get(giftTimer);
    }

    public void setGiftTimer(int val) {
        this.dataTracker.set(giftTimer, val);
    }
}
