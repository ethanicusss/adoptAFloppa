package net.fabricmc.adoptafloppa.entities.sogga;

import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.fabricmc.adoptafloppa.AdoptAFloppaClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SoggaEntityRenderer extends MobEntityRenderer<SoggaEntity, SoggaEntityModel> {

    public SoggaEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SoggaEntityModel(context.getPart(AdoptAFloppaClient.MODEL_SOGGA_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(SoggaEntity entity) {
        return new Identifier(AdoptAFloppa.MOD_ID, "textures/entity/sogga/sogga.png");
    }
}
