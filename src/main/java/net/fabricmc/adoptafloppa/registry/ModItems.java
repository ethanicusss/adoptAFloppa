package net.fabricmc.adoptafloppa.registry;

import net.fabricmc.adoptafloppa.items.*;
import net.fabricmc.adoptafloppa.sound.ModSounds;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item ORCA_ITEM = new OrcaItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item KITNEY_ITEM = new KitneyItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item BEBE_ITEM = new BebeItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item HUMAN_DOG_ITEM = new AughItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item THE_DOG_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.BOOM_EVENT, 5);
    public static final Item AMAZON_DELIVERY = new AmazonDelivery(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item RADIOACTIVE_SLOP = new RadioactiveSlopItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP));
    public static final Item AMOGUS_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.AMOGUS_EVENT, 5);
    public static final Item GHIBLI_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.GHIBLI_EVENT, 0);//contest
    public static final Item RAPTURE_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.RAPTURE_EVENT, 5);//contest
    public static final Item HORN_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.HORN_EVENT, 50);//contest
    public static final Item THIS_AINT_WORKING_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.THIS_SHIT_AINT_WORKING_EVENT, 15);//contest
    public static final Item SPACE_CORE_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.SPACE_CORE_EVENT, 60);//contest
    public static final Item RYAN_GOSLING_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.RYAN_GOSLING_EVENT, 0);//appa
    public static final Item RIZZ_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.RIZZ_EVENT, 0);//short
    public static final Item JABBA_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.SPLAT_EVENT, 0);
    public static final Item BWOOF_ITEM = new SoundItem(new FabricItemSettings().group(AdoptAFloppa.FLOPPA_ITEM_GROUP), ModSounds.BWOOF_EVENT, 0);
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "orca_item"), ORCA_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "kitney_item"), KITNEY_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "bebe_item"), BEBE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "human_dog_item"), HUMAN_DOG_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "the_dog_item"), THE_DOG_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "amogus_item"), AMOGUS_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "ghibli_item"), GHIBLI_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "rapture_item"), RAPTURE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "horn_item"), HORN_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "this_shit_aint_working_item"), THIS_AINT_WORKING_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "space_core_item"), SPACE_CORE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "ryan_gosling_item"), RYAN_GOSLING_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "rizz_item"), RIZZ_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "jabba_item"), JABBA_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "bwoof_item"), BWOOF_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "amazon_delivery"), AMAZON_DELIVERY);
        Registry.register(Registry.ITEM, new Identifier(AdoptAFloppa.MOD_ID, "radioactive_slop"), RADIOACTIVE_SLOP);
    }
}
