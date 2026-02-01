package net.petemc.mutantszombies.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.petemc.mutantszombies.MutantsZombies;
import net.petemc.mutantszombies.entity.CommonZombieEntity;

public class CommonZombieRenderer extends HumanoidMobRenderer<CommonZombieEntity, HumanoidModel<CommonZombieEntity>> {

    public CommonZombieRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new HumanoidModel<CommonZombieEntity>($$0.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    public ResourceLocation getTextureLocation(CommonZombieEntity $$0) {
        return ResourceLocation.fromNamespaceAndPath(MutantsZombies.MOD_ID, "textures/entities/common_zombie.png");
    }
}
