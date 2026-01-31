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
import net.petemc.mutantszombies.entity.BoomerEntity;

public class BoomerModel<T extends BoomerEntity> extends EntityModel<T> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
                        ResourceLocation.fromNamespaceAndPath(MutantsZombies.MOD_ID, "spitter_layer"), "main");
        public final ModelPart head2;
        public final ModelPart head3;
        public final ModelPart head4;
        public final ModelPart head5;
        public final ModelPart head6;
        public final ModelPart head7;
        public final ModelPart head8;
        public final ModelPart torso;
        public final ModelPart right_arm;
        public final ModelPart left_arm;
        public final ModelPart left_leg;
        public final ModelPart right_leg;

        public BoomerModel(ModelPart root) {
                this.head2 = root.getChild("head2");
                this.head3 = root.getChild("head3");
                this.head4 = root.getChild("head4");
                this.head5 = root.getChild("head5");
                this.head6 = root.getChild("head6");
                this.head7 = root.getChild("head7");
                this.head8 = root.getChild("head8");
                this.torso = root.getChild("torso");
                this.right_arm = root.getChild("right_arm");
                this.left_arm = root.getChild("left_arm");
                this.left_leg = root.getChild("left_leg");
                this.right_leg = root.getChild("right_leg");
        }

        public static LayerDefinition createBodyLayer() {
                MeshDefinition meshdefinition = new MeshDefinition();
                PartDefinition partdefinition = meshdefinition.getRoot();
                PartDefinition head2 = partdefinition.addOrReplaceChild("head2", CubeListBuilder.create(),
                                PartPose.offset(-13.0F, 7.0F, -8.0F));
                head2.addOrReplaceChild("cube_r1",
                                CubeListBuilder.create().texOffs(32, 109).addBox(-4.3511F, -22.6056F, 7.8459F, 8.0F,
                                                2.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(13.0F, 18.0F, 10.0F, 2.9626F, -0.6423F, 2.937F));
                head2.addOrReplaceChild("cube_r2",
                                CubeListBuilder.create().texOffs(96, 95).addBox(-1.8795F, -16.2629F, 19.5477F, 8.0F,
                                                6.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(13.0F, 18.0F, 10.0F, -2.6477F, -0.4703F, 2.9023F));
                PartDefinition head3 = partdefinition.addOrReplaceChild("head3", CubeListBuilder.create(),
                                PartPose.offset(-1.0F, 3.0F, -10.0F));
                head3.addOrReplaceChild("cube_r3",
                                CubeListBuilder.create().texOffs(109, 6).addBox(-1.9556F, -24.2196F, 5.3173F, 8.0F,
                                                2.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(1.0F, 22.0F, 12.0F, 3.009F, 0.1129F, -3.1296F));
                head3.addOrReplaceChild("cube_r4",
                                CubeListBuilder.create().texOffs(32, 95).addBox(-0.943F, -20.2629F, 17.5242F, 8.0F,
                                                6.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(1.0F, 22.0F, 12.0F, -2.702F, 0.1186F, -3.086F));
                PartDefinition head4 = partdefinition.addOrReplaceChild("head4", CubeListBuilder.create(),
                                PartPose.offset(0.0F, -6.0F, 4.0F));
                head4.addOrReplaceChild("cube_r5",
                                CubeListBuilder.create().texOffs(84, 81).addBox(-4.0F, -36.0006F, 11.6916F, 8.0F, 6.0F,
                                                8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(0.0F, 31.0F, -2.0F, -2.5744F, 0.0F, 3.1416F));
                head4.addOrReplaceChild("cube_r6",
                                CubeListBuilder.create().texOffs(0, 109).addBox(-4.0F, -31.2629F, 7.759F, 8.0F, 2.0F,
                                                8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(0.0F, 31.0F, -2.0F, -2.7053F, 0.0F, 3.1416F));
                PartDefinition head5 = partdefinition.addOrReplaceChild("head5", CubeListBuilder.create(),
                                PartPose.offset(12.0F, 10.0F, -6.0F));
                head5.addOrReplaceChild("cube_r7",
                                CubeListBuilder.create().texOffs(104, 64).addBox(-4.4237F, -17.0446F, 9.0093F, 8.0F,
                                                2.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-12.0F, 15.0F, 8.0F, 2.7981F, 1.0556F, 2.8826F));
                head5.addOrReplaceChild("cube_r8",
                                CubeListBuilder.create().texOffs(64, 95).addBox(-4.4237F, -22.1988F, 11.24F, 8.0F, 6.0F,
                                                8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-12.0F, 15.0F, 8.0F, 2.8854F, 1.0556F, 2.8826F));
                PartDefinition head6 = partdefinition.addOrReplaceChild("head6", CubeListBuilder.create(),
                                PartPose.offsetAndRotation(12.0F, 5.0F, 10.0F, -0.0436F, 0.0F, 0.0F));
                head6.addOrReplaceChild("cube_r9",
                                CubeListBuilder.create().texOffs(0, 95).addBox(-24.9861F, -23.7525F, -6.1784F, 8.0F,
                                                6.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-12.0F, 20.8288F, -11.9819F, 2.6202F, 0.9349F, 2.792F));
                head6.addOrReplaceChild("cube_r10",
                                CubeListBuilder.create().texOffs(104, 23).addBox(-23.5859F, -17.906F, -6.1784F, 8.0F,
                                                2.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-12.0F, 20.8288F, -11.9819F, 2.6734F, 0.9556F, 2.8576F));
                PartDefinition head7 = partdefinition.addOrReplaceChild("head7", CubeListBuilder.create(),
                                PartPose.offset(-10.0F, -4.0F, 11.0F));
                head7.addOrReplaceChild("cube_r11",
                                CubeListBuilder.create().texOffs(0, 15).addBox(-14.6813F, -21.8139F, 19.5135F, 8.0F,
                                                7.0F, 2.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(10.0F, 29.0F, -9.0F, -2.0567F, 0.2751F, 2.4914F));
                head7.addOrReplaceChild("cube_r12",
                                CubeListBuilder.create().texOffs(108, 75).addBox(-14.6813F, -28.7175F, 14.6915F, 8.0F,
                                                8.0F, 6.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(10.0F, 29.0F, -9.0F, -2.3622F, 0.2751F, 2.4914F));
                PartDefinition head8 = partdefinition.addOrReplaceChild("head8", CubeListBuilder.create(),
                                PartPose.offset(-8.0F, 11.0F, 8.0F));
                head8.addOrReplaceChild("cube_r13",
                                CubeListBuilder.create().texOffs(101, 54).addBox(-8.9696F, -17.4786F, -6.9687F, 8.0F,
                                                2.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(8.0F, 14.0F, -6.0F, -0.9395F, 1.1447F, -1.2238F));
                head8.addOrReplaceChild("cube_r14",
                                CubeListBuilder.create().texOffs(80, 0).addBox(-16.9424F, -19.5751F, -6.9687F, 8.0F,
                                                6.0F, 8.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(8.0F, 14.0F, -6.0F, 0.3258F, 1.3104F, 0.0959F));
                PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create(),
                                PartPose.offset(0.0F, 5.0F, 0.0F));
                torso.addOrReplaceChild("cube_r15",
                                CubeListBuilder.create().texOffs(0, 40).addBox(-13.0F, -13.0637F, -10.3502F, 26.0F,
                                                5.0F, 23.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(0.0F, 20.0F, 2.0F, 3.1067F, 0.0F, 3.1416F));
                torso.addOrReplaceChild("cube_r16",
                                CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -25.9429F, -8.6927F, 28.0F, 16.0F,
                                                24.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(0.0F, 20.0F, 2.0F, -3.0543F, 0.0F, 3.1416F));
                torso.addOrReplaceChild("cube_r17",
                                CubeListBuilder.create().texOffs(0, 68).addBox(-11.0F, -31.9982F, -3.8529F, 23.0F, 8.0F,
                                                19.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(0.0F, 20.0F, 2.0F, -2.9234F, 0.0F, 3.1416F));
                PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(),
                                PartPose.offsetAndRotation(-13.0F, -5.0F, 5.0F, 0.1309F, -0.2182F, 0.0F));
                right_arm.addOrReplaceChild("cube_r18",
                                CubeListBuilder.create().texOffs(75, 40).addBox(-7.376F, -33.7922F, -7.9359F, 15.8F,
                                                6.0F, 7.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(10.916F, 28.4299F, -11.9257F, -2.9136F, 0.1298F, 2.6329F));
                right_arm.addOrReplaceChild("cube_r19",
                                CubeListBuilder.create().texOffs(75, 53).addBox(-10.5294F, -32.7949F, -5.3264F, 11.8F,
                                                4.0F, 4.9F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(10.916F, 28.4299F, -11.9257F, -2.9876F, 0.4358F, 2.1879F));
                PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(),
                                PartPose.offsetAndRotation(11.0F, -4.4F, 7.6F, -0.0089F, 0.1742F, -0.0498F));
                left_arm.addOrReplaceChild("cube_r20",
                                CubeListBuilder.create().texOffs(65, 68).addBox(-7.6672F, -32.9383F, -7.8704F, 15.8F,
                                                6.0F, 7.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-5.2277F, 29.5507F, -10.7158F, -2.948F, -0.0923F, -2.7777F));
                left_arm.addOrReplaceChild("cube_r21",
                                CubeListBuilder.create().texOffs(80, 14).addBox(-1.0144F, -31.6881F, -5.2025F, 11.8F,
                                                4.0F, 4.9F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-5.2277F, 29.5507F, -10.7158F, -3.0035F, -0.3873F,
                                                -2.3301F));
                PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(),
                                PartPose.offset(10.0F, 16.0F, 0.0F));
                left_leg.addOrReplaceChild("cube_r22",
                                CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -10.0F, -1.0F, 5.0F, 9.0F, 6.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(-10.0F, 9.0F, 2.0F, -3.1416F, 0.0F, 3.1416F));
                PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(),
                                PartPose.offset(-10.0F, 16.0F, 0.0F));
                right_leg.addOrReplaceChild("cube_r23",
                                CubeListBuilder.create().texOffs(0, 40).addBox(7.0F, -10.0F, -1.0F, 5.0F, 9.0F, 6.0F,
                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(10.0F, 9.0F, 2.0F, -3.1416F, 0.0F, 3.1416F));
                return LayerDefinition.create(meshdefinition, 256, 256);
        }

        public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer,
                        int packedLight,
                        int packedOverlay, float red, float green, float blue, float alpha) {
                this.head2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.head8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
                this.right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                        float netHeadYaw,
                        float headPitch) {
                this.head8.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head8.xRot = headPitch / (180F / (float) Math.PI);
                this.right_arm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.left_leg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
                this.left_arm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.right_leg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
                this.head2.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head2.xRot = headPitch / (180F / (float) Math.PI);
                this.head3.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head3.xRot = headPitch / (180F / (float) Math.PI);
                this.head4.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head4.xRot = headPitch / (180F / (float) Math.PI);
                this.head5.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head5.xRot = headPitch / (180F / (float) Math.PI);
                this.head6.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head6.xRot = headPitch / (180F / (float) Math.PI);
                this.head7.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head7.xRot = headPitch / (180F / (float) Math.PI);
        }
}
