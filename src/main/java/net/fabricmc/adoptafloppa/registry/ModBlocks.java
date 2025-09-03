package net.fabricmc.adoptafloppa.registry;

import net.fabricmc.adoptafloppa.blocks.AmogusBlock;
import net.fabricmc.adoptafloppa.blocks.FloppaBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class ModBlocks {
    public static final Block FLOPPA_BLOCK = new CarvedPumpkinBlock(FabricBlockSettings.of(Material.TNT).strength(3.0f).sounds(BlockSoundGroup.ANVIL));
    public static final BlockItem FLOPPA_BLOCK_ITEM = new BlockItem(FLOPPA_BLOCK, new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Block AMOGUS_BLOCK = new CarvedPumpkinBlock(FabricBlockSettings.of(Material.EGG).strength(2.5f).sounds(BlockSoundGroup.FUNGUS));
    public static final BlockItem AMOGUS_BLOCK_ITEM = new BlockItem(AMOGUS_BLOCK, new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Block CIRNO_BLOCK = new CarvedPumpkinBlock(FabricBlockSettings.of(Material.WOOL).strength(0.5f).sounds(BlockSoundGroup.WOOL).nonOpaque().collidable(false));
    public static final BlockItem CIRNO_BLOCK_ITEM = new BlockItem(CIRNO_BLOCK, new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));


    public static void registerBlocks(){
        FlammableBlockRegistry flamInstance = FlammableBlockRegistry.getDefaultInstance();

        Registry.register(Registry.BLOCK, new Identifier(AdoptAFloppa.MOD_ID, "floppa_block"), FLOPPA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "floppa_block"), FLOPPA_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AdoptAFloppa.MOD_ID, "amogus_block"), AMOGUS_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "amogus_block"), AMOGUS_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AdoptAFloppa.MOD_ID, "cirno_block"), CIRNO_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "cirno_block"), CIRNO_BLOCK_ITEM);
    }
    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CIRNO_BLOCK, RenderLayer.getCutout());
    }
}
