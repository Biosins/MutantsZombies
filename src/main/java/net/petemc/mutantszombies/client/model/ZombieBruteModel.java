package net.petemc.mutantszombies.client.model;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.petemc.mutantszombies.MutantsZombies;
import net.petemc.mutantszombies.entity.ZombieBruteEntity;

public class ZombieBruteModel<T extends ZombieBruteEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MutantsZombies.MOD_ID, "zombie_brute_layer"), "main");
    public final ModelPart head;
    public final ModelPart torso;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    public final ModelPart left_leg;
    public final ModelPart right_leg;

    public ZombieBruteModel(ModelPart root) {
        this.head = root.getChild("head");
        this.torso = root.getChild("torso");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(71, 49)
                        .addBox(-6.0F, -4.0F, -7.0F, 11.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(69, 99)
                        .addBox(-5.0F, 4.0F, -6.0F, 9.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -15.0F, -17.0F));
        partdefinition.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -41.0F, -15.0F, 29.0F, 21.0F, 28.0F, new CubeDeformation(0.0F)).texOffs(0, 49)
                        .addBox(-13.0F, -20.0F, -9.0F, 25.0F, 7.0F, 21.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(),
                PartPose.offset(-20.0F, -11.0F, -7.0F));
        right_arm.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(86, 0).addBox(-23.0F, 14.0F, 5.0F, 8.0F, 14.0F, 8.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        right_arm
                .addOrReplaceChild("cube_r2",
                        CubeListBuilder.create().texOffs(0, 77).addBox(-4.0F, 0.0F, -3.0F, 10.0F, 18.0F, 10.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(),
                PartPose.offset(19.0F, -11.0F, -7.0F));
        left_arm.addOrReplaceChild("cube_r3",
                CubeListBuilder.create().texOffs(80, 77).addBox(14.0F, 14.0F, 5.0F, 8.0F, 14.0F, 8.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-19.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(40, 77).addBox(13.0F, 0.0F, -3.0F, 10.0F, 18.0F, 10.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-19.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(98, 99)
                .addBox(-2.0F, 11.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 11.0F, -1.0F));
        left_leg.addOrReplaceChild("cube_r5",
                CubeListBuilder.create().texOffs(104, 70).addBox(3.0F, -7.0F, 1.0F, 5.0F, 7.0F, 5.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, 13.0F, -3.0F, 0.3927F, 0.0F, 0.0F));
        left_leg.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.6533F, -3.2527F,
                7.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, -0.3054F, 0.0F, 0.0F));
        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 19)
                .addBox(-2.0F, 11.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-6.0F, 11.0F, -2.0F));
        right_leg.addOrReplaceChild("cube_r7",
                CubeListBuilder.create().texOffs(0, 105).addBox(-9.0F, -18.0F, -4.0F, 7.0F, 12.0F, 7.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, 13.0F, -3.0F, -0.3054F, 0.0F, 0.0F));
        right_leg.addOrReplaceChild("cube_r8",
                CubeListBuilder.create().texOffs(0, 49).addBox(-8.0F, -7.0F, 1.0F, 5.0F, 7.0F, 5.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, 13.0F, -3.0F, 0.3927F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight,
            int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch) {
        // this.right_arm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) *
        // limbSwingAmount;
        this.left_leg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
        // this.left_arm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.right_leg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
    }

    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        int i = pEntity.getAttackAnimationTick();
        if (i > 0) {
            this.right_arm.xRot = -2.0F + 1.5F * Mth.triangleWave((float) i - pPartialTick, 10.0F);
            this.left_arm.xRot = -2.0F + 1.5F * Mth.triangleWave((float) i - pPartialTick, 10.0F);
        } else {
            this.right_arm.xRot = (-0.2F + 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
            this.left_arm.xRot = (-0.2F - 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
        }
    }
}
