package net.fabricmc.adoptafloppa.entities.floppa;

import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.fabricmc.adoptafloppa.AdoptAFloppaClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class FloppaEntityRenderer extends MobEntityRenderer<FloppaEntity, FloppaEntityModel> {

    public FloppaEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FloppaEntityModel(context.getPart(AdoptAFloppaClient.MODEL_FLOPPA_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(FloppaEntity entity) {
        return new Identifier(AdoptAFloppa.MOD_ID, "textures/entity/floppa/floppa.png");
    }
}
