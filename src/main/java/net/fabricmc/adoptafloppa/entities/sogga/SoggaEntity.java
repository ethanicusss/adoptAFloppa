package net.fabricmc.adoptafloppa.entities.sogga;

import net.fabricmc.adoptafloppa.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SoggaEntity extends HostileEntity implements SkinOverlayOwner, RangedAttackMob {
    private static final TrackedData<Integer> TRACKED_ENTITY_ID;
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final int DEFAULT_INVUL_TIMER = 220;
    private static final TrackedData<String> PHASE;
    private static final TrackedData<Integer> PHASE_TIMER;
    private static final int DEFAULT_PHASE_TIMER = 240;
    private static final double PHASE2_HP_MULT = 0.66;
    private static final double PHASE3_HP_MULT = 0.1;
    private final ServerBossBar bossBar;
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE;
    private static final TargetPredicate HEAD_TARGET_PREDICATE;
    private static final Predicate<Entity> FIREBALL_PREDICATE;

    public SoggaEntity(EntityType<? extends SoggaEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints = 50;
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SoggaEntity.DescendAtHalfHealthGoal());
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0, 40, 40.0F));
        this.goalSelector.add(5, new FlyGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRACKED_ENTITY_ID, -1);
        this.dataTracker.startTracking(INVUL_TIMER, DEFAULT_INVUL_TIMER);
        this.dataTracker.startTracking(PHASE, "Wither");
        this.dataTracker.startTracking(PHASE_TIMER, DEFAULT_PHASE_TIMER);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
        nbt.putString("Phase", this.getPhase());
        nbt.putInt("PhaseTimer", this.getPhaseTimer());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        this.setPhase(nbt.getString("Phase"));
        this.setPhaseTimer(nbt.getInt("PhaseTimer"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
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
    private int getTrackedEntityId(){
        Box box = (new Box(new BlockPos(getX(), getY(), getZ()))).expand(50).stretch(0.0, (double)world.getHeight(), 0.0);
        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
        Iterator pList = list.iterator();
        PlayerEntity playerEntity;
        PlayerEntity nearestPlayer = null;
        while(pList.hasNext()) {
            playerEntity = (PlayerEntity)pList.next();
            if (nearestPlayer == null){
                nearestPlayer = playerEntity;
                return nearestPlayer.getId();
            }
            /*else{
                if (Math.abs(nearestPlayer.getX() - getX()) < Math.abs(playerEntity.getX() - getX()))
            }*/
        }
        return -1;
    }

    public void tickMovement() {
        Vec3d vec3d = this.getVelocity().multiply(1.0, 0.6, 1.0);
        if (!this.world.isClient && this.getTrackedEntityId() != -1) {
            Entity entity = this.world.getEntityById(this.getTrackedEntityId());
            if (entity != null) {
                double d = vec3d.y;
                double h = switch (this.getPhase()) {
                    case "Wither" -> 5.0;
                    case "Sand" -> 10.0;
                    case "Arrow" -> 4.0;
                    case "Ghast" -> 10.0;
                    case "Blaze" -> 10.0;
                    default -> 5.0;
                };
                if (this.getY() < entity.getY() || !this.shouldRenderOverlay() && this.getY() < entity.getY() + 5.0) {
                    d = Math.max(0.0, d);
                    d += 0.3 - d * 0.6000000238418579;
                }

                vec3d = new Vec3d(vec3d.x, d, vec3d.z);
                Vec3d vec3d2 = new Vec3d(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());

                double r = switch (this.getPhase()) {
                    case "Wither" -> 9.0;
                    case "Sand" -> 0.0;
                    case "Arrow" -> 25.0;
                    case "Ghast" -> 40.0;
                    case "Blaze" -> 100.0;
                    default -> 9.0;
                };
                if (vec3d2.horizontalLengthSquared() > r) {
                    Vec3d vec3d3 = vec3d2.normalize();
                    vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
                }
            }
        }

        this.setVelocity(vec3d);
        if (vec3d.horizontalLengthSquared() > 0.05) {
            this.setYaw((float)MathHelper.atan2(vec3d.z, vec3d.x) * 57.295776F - 90.0F);
        }

        super.tickMovement();

        boolean bl = this.shouldRenderOverlay();
        this.world.addParticle(ParticleTypes.SMOKE, getX() + this.random.nextGaussian() * 0.30000001192092896, getY() + this.random.nextGaussian() * 0.30000001192092896, getZ() + this.random.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0);
        if (bl && this.world.random.nextInt(4) == 0) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, getX() + this.random.nextGaussian() * 0.30000001192092896, getY() + this.random.nextGaussian() * 0.30000001192092896, getZ() + this.random.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5);
        }

        if (this.getInvulnerableTimer() > 0) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.699999988079071, 0.699999988079071, 0.8999999761581421);
        }

        if (Objects.equals(this.getPhase(), "Blaze")){
            for (int _i = 0; _i < 5; _i++) {
                this.world.addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextFloat() - 0.5)*2, this.getY() + (this.random.nextFloat() - 0.5) * 2 - 1, this.getZ() + (this.random.nextFloat() - 0.5)*2, (this.random.nextFloat() - 0.5)*0.2, 0.5, (this.random.nextFloat() - 0.5)*0.2);
            }
        }
        if (Objects.equals(this.getPhase(), "Ghast")){
            Box box = (new Box(new BlockPos(getX(), getY(), getZ()))).expand(50).stretch(0.0, (double)world.getHeight(), 0.0);

            for (int _i = 0; _i < 5; _i++) {
                List<FireballEntity> list = this.world.getEntitiesByType(EntityType.FIREBALL, box, FIREBALL_PREDICATE);

                for (FireballEntity fb : list) {
                    this.world.addParticle(ParticleTypes.FLAME, fb.getX() + (this.random.nextFloat() - 0.5) * 2, fb.getY() - (this.random.nextFloat()) * 5, fb.getZ() + (this.random.nextFloat() - 0.5) * 2, 0.0, -0.5, 0.0);
                }
            }
        }

    }

    protected void mobTick() {
        int i;
        if (this.getInvulnerableTimer() > 0) {
            i = this.getInvulnerableTimer() - 1;
            this.bossBar.setPercent(1.0F - (float)i / 220.0F);
            this.setRotation(this.getYaw()+1, this.getPitch());
            if (i <= 0) {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                this.world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, destructionType);
                if (!this.isSilent()) {
                    this.world.syncGlobalEvent(1023, this.getBlockPos(), 0);
                }
            }
            this.setInvulTimer(i);
            if (this.age % 10 == 0) {
                this.heal(10.0F);
            }
        } else {//do shtuff
            super.mobTick();
            Entity entity;
            if (this.getTrackedEntityId() != -1) {
                entity = this.world.getEntityById(this.getTrackedEntityId());
            }
            else {
                entity = null;
            }
            double clientParticleAmount = 0;
            double clientParticleX = 0;
            double clientParticleY = 0;
            double clientParticleZ = 0;
            if (!this.world.isClient && this.getTrackedEntityId() != -1) {
                if (this.getHealth() > this.getMaxHealth() * PHASE3_HP_MULT) {
                    if (this.age % 60 == 0) {
                        this.heal(1.0F);
                    }
                    if (this.getHealth() > this.getMaxHealth() * PHASE2_HP_MULT) {
                        if (this.age % 60 == 0) {
                            this.heal(1.0F);
                        }
                    }
                    if (this.getPhaseTimer() <= 0) {
                        this.setPhaseTimer(DEFAULT_PHASE_TIMER);
                        if (this.getHealth() < this.getMaxHealth() * PHASE2_HP_MULT) {
                            this.setPhaseTimer(DEFAULT_PHASE_TIMER - 60);
                        }
                        switch (this.getPhase()) {
                            case "Wither":
                                this.setPhase("Sand");
                                if (this.getHealth() < this.getMaxHealth() * PHASE2_HP_MULT) {
                                    this.setPhaseTimer(DEFAULT_PHASE_TIMER - 120);
                                } else {
                                    this.setPhaseTimer(DEFAULT_PHASE_TIMER - 30);
                                }
                                break;
                            case "Sand":
                                this.setPhase("Arrow");
                                break;
                            case "Arrow":
                                this.setPhase("Wither");
                                if (this.getHealth() < getMaxHealth() * PHASE2_HP_MULT) {
                                    this.setPhase("Ghast");
                                    this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_CAT_HISS, SoundCategory.HOSTILE, 1.2F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F), true);
                                }
                                break;
                            case "Ghast":
                                this.setPhase("Blaze");
                                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_CAT_PURREOW, SoundCategory.HOSTILE, 1.2F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F), true);
                                this.setVelocity((this.random.nextFloat() - 0.5) * 2 * 5, 0, (this.random.nextFloat() - 0.5) * 2 * 5);
                                break;
                            case "Blaze":
                                this.setPhase("Wither");
                                break;
                            default:
                                this.setPhase("Wither");
                        }
                    }
                    this.setPhaseTimer(this.getPhaseTimer() - 1);
                    switch (this.getPhase()) {
                        case "Wither":
                            if (this.age % 60 == 0) {
                                if (entity != null) {
                                    shootSkullAt(entity);
                                    if (this.getHealth() < this.getMaxHealth() * PHASE2_HP_MULT) {
                                        shootSkullAt(entity.getX(), entity.getY() + 2.0, entity.getZ(), false);
                                        shootSkullAt(entity.getX(), entity.getY() - 2.0, entity.getZ(), false);
                                    }
                                }
                            }
                            if (this.getHealth() < this.getMaxHealth() * PHASE2_HP_MULT) {
                                if (this.age % 150 == 0) {
                                    if (entity != null) {
                                        shootSkullAt(entity.getX(), entity.getY() - 1.0, entity.getZ(), true);
                                    }
                                }
                            }
                            break;
                        case "Sand":
                            if (this.age % 5 == 0) {
                                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, this.world);
                                fallingBlockEntity.setPosition(Math.round(this.getX()), Math.round(this.getY()) - 1.0, Math.round(this.getZ()));
                                fallingBlockEntity.setVelocity(Vec3d.ZERO);
                                fallingBlockEntity.dropItem = false;
                                fallingBlockEntity.setHurtEntities(2.0f, 4);
                                fallingBlockEntity.intersectionChecked = true;
                                fallingBlockEntity.prevX = Math.round(this.getX());
                                fallingBlockEntity.prevY = Math.round(this.getY()) - 1.0;
                                fallingBlockEntity.prevZ = Math.round(this.getZ());
                                fallingBlockEntity.setFallingBlockPos(fallingBlockEntity.getBlockPos());
                                fallingBlockEntity.createSpawnPacket();
                                this.world.spawnEntity(fallingBlockEntity);
                            }
                            break;
                        case "Arrow":
                            if (this.age % 70 == 0) {
                                ItemStack arrow = new ItemStack(Items.ARROW);
                                PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this, arrow, 3.0f);
                                double d = entity.getX() - this.getX();
                                double e = entity.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
                                double f = entity.getZ() - this.getZ();
                                double g = Math.sqrt(d * d + f * f);
                                persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, (float) (1.4 + this.world.getDifficulty().getId() * 0.2), (float) (14 - this.world.getDifficulty().getId() * 4));
                                persistentProjectileEntity.setPunch(3);
                                this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                                this.world.spawnEntity(persistentProjectileEntity);
                            }
                            break;
                        case "Ghast":
                            if (this.age % 20 == 0) {
                                double _x = this.getX() + (this.random.nextFloat() - 0.5)*2*10;
                                double _y = this.getY() + 5.0;
                                double _z = this.getZ() + (this.random.nextFloat() - 0.5)*2*10;
                                /*for (int _i = 0; _i < 40; _i++) {
                                    this.world.addParticle(ParticleTypes.HEART, _x, _y - (this.random.nextFloat()) * 10 - 10, _z, 0.0, 0.0, 0.0);
                                }*/
                                clientParticleAmount = 40;
                                clientParticleX = _x;
                                clientParticleY = _y - (this.random.nextFloat()) * 10 - 10;
                                clientParticleZ = _z;
                                FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, this.world);
                                fireball.setPosition(_x, _y, _z);
                                fireball.setVelocity(0, -1, 0);
                                this.world.spawnEntity(fireball);
                            }
                            break;
                        case "Blaze":
                            if (this.age % 2 == 0) {
                                this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                                double angle = this.random.nextFloat() * 360;
                                double _x = 1 * Math.sin(angle);
                                double _y = (this.random.nextFloat()) * -2 - 0.5;
                                double _z = 1 * Math.cos(angle);
                                /*for (int _i = 0; _i < 10; _i++) {
                                    this.world.addParticle(ParticleTypes.HEART, this.getX() + (this.random.nextFloat() - 0.5)*2, this.getY() - (this.random.nextFloat() - 0.5) * 5, this.getZ() + (this.random.nextFloat() - 0.5)*2, 0.0, 1.0, 0.0);
                                }*/
                                clientParticleAmount = 10;
                                clientParticleX = this.getX() + (this.random.nextFloat() - 0.5)*2;
                                clientParticleY = this.getY() - (this.random.nextFloat() - 0.5) * 5;
                                clientParticleZ = this.getZ() + (this.random.nextFloat() - 0.5)*2;
                                SmallFireballEntity fireball = new SmallFireballEntity(EntityType.SMALL_FIREBALL, this.world);
                                fireball.setPosition(this.getX(), this.getY() - 1.0, this.getZ());
                                fireball.setVelocity(_x, _y, _z);
                                this.world.spawnEntity(fireball);
                            }
                            break;
                    }
                } else {
                    if (this.age % 20 == 0) {
                        this.damage(DamageSource.MAGIC, 5.0F);
                    }
                    if (this.age % 5 == 0) {
                        if (entity != null) {
                            shootSkullAt(entity);
                        }
                    }
                }

                this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            }

            if (clientParticleAmount > 0){
                for (int _i = 0; _i < clientParticleAmount; _i++) {
                    MinecraftClient.getInstance().world.addParticle(ParticleTypes.FLAME, clientParticleX, clientParticleY - (this.random.nextFloat()) * 10 - 10, clientParticleZ, 0.0, 0.0, 0.0);
                }
                clientParticleAmount = 0;
            }
        }
    }

    public static boolean canDestroy(BlockState block) {
        return !block.isAir() && !block.isIn(BlockTags.WITHER_IMMUNE);
    }

    public void onSummoned() {
        this.setInvulTimer(220);
        this.bossBar.setPercent(0.0F);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    public void slowMovement(BlockState state, Vec3d multiplier) {
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    private float getNextAngle(float prevAngle, float desiredAngle, float maxDifference) {
        float f = MathHelper.wrapDegrees(desiredAngle - prevAngle);
        if (f > maxDifference) {
            f = maxDifference;
        }

        if (f < -maxDifference) {
            f = -maxDifference;
        }

        return prevAngle + f;
    }

    private void shootSkullAt(Entity target) {
        this.shootSkullAt(target.getX(), target.getY() + (double)target.getStandingEyeHeight() * 0.5, target.getZ(), false);
    }

    private void shootSkullAt(double targetX, double targetY, double targetZ, boolean charged) {
        if (!this.isSilent()) {
            this.world.syncWorldEvent((PlayerEntity)null, 1024, this.getBlockPos(), 0);
        }

        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        double g = targetX - d;
        double h = targetY - e;
        double i = targetZ - f;
        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(this.world, this, g, h, i);
        witherSkullEntity.setOwner(this);
        if (charged) {
            witherSkullEntity.setCharged(true);
        }

        witherSkullEntity.setPos(d, e, f);
        this.world.spawnEntity(witherSkullEntity);
    }

    public void attack(LivingEntity target, float pullProgress) {
        this.shootSkullAt(target);
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (this.getInvulnerableTimer() > 0 && source != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                Entity entity;
                if (this.shouldRenderOverlay()) {
                    entity = source.getSource();
                    if (entity instanceof PersistentProjectileEntity) {
                        return false;
                    }
                }

                entity = source.getAttacker();
                if (entity != null && !(entity instanceof PlayerEntity) && entity instanceof LivingEntity && ((LivingEntity)entity).getGroup() == this.getGroup()) {
                    return false;
                } else {

                    return super.damage(source, amount);
                }
            }
        }
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        ItemEntity itemEntity = this.dropItem(ModItems.BEBE_ITEM);
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }

    }

    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
        } else {
            this.despawnCounter = 0;
        }
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createSoggaAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 250.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6000000238418579).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0).add(EntityAttributes.GENERIC_ARMOR, 4.0);
    }

    public int getInvulnerableTimer() {
        return (Integer)this.dataTracker.get(INVUL_TIMER);
    }
    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    public int getPhaseTimer() {
        return (Integer)this.dataTracker.get(PHASE_TIMER);
    }
    public void setPhaseTimer(int ticks) {
        this.dataTracker.set(PHASE_TIMER, ticks);
    }

    public String getPhase() {
        return (String)this.dataTracker.get(PHASE);
    }
    public void setPhase(String phase) {
        this.dataTracker.set(PHASE, phase);
    }

    public boolean shouldRenderOverlay() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    public boolean canUsePortals() {
        return false;
    }

    static {
        INVUL_TIMER = DataTracker.registerData(SoggaEntity.class, TrackedDataHandlerRegistry.INTEGER);
        TRACKED_ENTITY_ID = DataTracker.registerData(SoggaEntity.class, TrackedDataHandlerRegistry.INTEGER);
        PHASE = DataTracker.registerData(SoggaEntity.class, TrackedDataHandlerRegistry.STRING);
        PHASE_TIMER = DataTracker.registerData(SoggaEntity.class, TrackedDataHandlerRegistry.INTEGER);
        CAN_ATTACK_PREDICATE = Entity::isPlayer;
        HEAD_TARGET_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(20.0).setPredicate(CAN_ATTACK_PREDICATE);
        FIREBALL_PREDICATE = (entity) -> {
            return true;
        };
    }

    private class DescendAtHalfHealthGoal extends Goal {
        public DescendAtHalfHealthGoal() {
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
        }

        public boolean canStart() {
            return SoggaEntity.this.getInvulnerableTimer() > 0;
        }
    }
}
