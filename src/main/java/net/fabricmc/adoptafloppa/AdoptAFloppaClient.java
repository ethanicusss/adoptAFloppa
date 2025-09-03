package net.fabricmc.adoptafloppa;

import net.fabricmc.adoptafloppa.entities.floppa.FloppaEntityModel;
import net.fabricmc.adoptafloppa.entities.floppa.FloppaEntityRenderer;
import net.fabricmc.adoptafloppa.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.adoptafloppa.entities.sogga.SoggaEntityModel;
import net.fabricmc.adoptafloppa.entities.sogga.SoggaEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class AdoptAFloppaClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_FLOPPA_LAYER = new EntityModelLayer(new Identifier(AdoptAFloppa.MOD_ID, "floppa"), "main");
    public static final EntityModelLayer MODEL_SOGGA_LAYER = new EntityModelLayer(new Identifier(AdoptAFloppa.MOD_ID, "sogga"), "main");
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(AdoptAFloppa.FLOPPA, FloppaEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_FLOPPA_LAYER, FloppaEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(AdoptAFloppa.SOGGA, SoggaEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SOGGA_LAYER, SoggaEntityModel::getTexturedModelData);

        ModBlocks.registerClient();
    }
}
