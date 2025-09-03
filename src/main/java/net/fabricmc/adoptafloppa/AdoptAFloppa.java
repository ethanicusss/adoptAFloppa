package net.fabricmc.adoptafloppa;

import net.fabricmc.adoptafloppa.entities.floppa.FloppaEntity;
import net.fabricmc.adoptafloppa.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.adoptafloppa.entities.sogga.SoggaEntity;
import net.fabricmc.adoptafloppa.registry.ModBlocks;
import net.fabricmc.adoptafloppa.registry.ModItems;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdoptAFloppa implements ModInitializer {
	public static final String MOD_ID = "adoptafloppa";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ItemGroup FLOPPA_ITEM_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "floppa_item_group"),
			() -> new ItemStack(ModBlocks.FLOPPA_BLOCK_ITEM));
	public static final EntityType<FloppaEntity> FLOPPA = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AdoptAFloppa.MOD_ID, "floppa"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FloppaEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
	);
	public static final EntityType<SoggaEntity> SOGGA = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AdoptAFloppa.MOD_ID, "sogga"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SoggaEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
	);

	@Override
	public void onInitialize() {
		//ModConfiguredFeatures.registerConfiguredFeatures();
		ModSounds.registerSounds();
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		//OreGenerator.registerOres();
		//ModWorldGen.generateModWorldGen();
		FabricDefaultAttributeRegistry.register(FLOPPA, FloppaEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(SOGGA, SoggaEntity.createSoggaAttributes());

		LOGGER.info("Adopt a Floppa is active!");
	}
}
