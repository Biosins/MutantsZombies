package net.petemc.mutantszombies.client.renderer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.petemc.mutantszombies.MutantsZombies;
import net.petemc.mutantszombies.client.model.BoomerModel;
import net.petemc.mutantszombies.entity.BoomerEntity;

public class BoomerRenderer extends MobRenderer<BoomerEntity, BoomerModel<BoomerEntity>> {
    public BoomerRenderer(EntityRendererProvider.Context context) {
        super(context, new BoomerModel<>(context.bakeLayer(BoomerModel.LAYER_LOCATION)), 1.3F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BoomerEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MutantsZombies.MOD_ID, "textures/entities/boomer.png");
    }
}
