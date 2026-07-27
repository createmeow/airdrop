package xaero.hud.minimap.radar.icon.definition;

import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.ArmadilloModel;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.BreezeModel;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.GoatModel;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.PufferfishBigModel;
import net.minecraft.client.model.PufferfishMidModel;
import net.minecraft.client.model.PufferfishSmallModel;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.model.RavagerModel;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xaero.common.minimap.render.radar.EntityIconDefinitions;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.radar.icon.cache.id.variant.EndermanVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.HorseVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.IronGolemVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.LlamaVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.SaddleVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.TamableVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.TropicalFishVariant;
import xaero.hud.minimap.radar.icon.cache.id.variant.VillagerVariant;
import xaero.hud.minimap.radar.icon.creator.render.form.model.custom.RadarIconCustomPrerenderer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/BuiltInRadarIconDefinitions.class */
public class BuiltInRadarIconDefinitions {
    public static float slimeSquishBU;
    public static final Method BUILD_VARIANT_ID_STRING_METHOD;
    public static final Method GET_VARIANT_ID_STRING_METHOD;

    public static List<String> getMainModelPartFields(EntityRenderer<?> renderer, EntityModel<?> model, Entity entity) {
        List<String> result = new ArrayList<>();
        if (model instanceof BatModel) {
            String modelClassPath = BatModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath, "head"));
            result.add(String.format("%s;%s", modelClassPath, "field_3321"));
            result.add(String.format("%s;%s", modelClassPath, "f_102184_"));
        } else if (model instanceof BlazeModel) {
            String modelClassPath2 = BlazeModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath2, "head"));
            result.add(String.format("%s;%s", modelClassPath2, "field_3329"));
            result.add(String.format("%s;%s", modelClassPath2, "f_102245_"));
        } else if (model instanceof SpiderModel) {
            String modelClassPath3 = SpiderModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath3, "head"));
            result.add(String.format("%s;%s", modelClassPath3, "field_3583"));
            result.add(String.format("%s;%s", modelClassPath3, "f_103852_"));
        } else if (model instanceof CreeperModel) {
            String modelClassPath4 = CreeperModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath4, "head"));
            result.add(String.format("%s;%s", modelClassPath4, "field_3360"));
            result.add(String.format("%s;%s", modelClassPath4, "f_102451_"));
        } else if (model instanceof LlamaModel) {
            String modelClassPath5 = LlamaModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath5, "head"));
            result.add(String.format("%s;%s", modelClassPath5, "field_27443"));
            result.add(String.format("%s;%s", modelClassPath5, "f_103031_"));
        } else if (model instanceof ParrotModel) {
            String modelClassPath6 = ParrotModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath6, "head"));
            result.add(String.format("%s;%s", modelClassPath6, "field_3452"));
            result.add(String.format("%s;%s", modelClassPath6, "f_103188_"));
        } else if (model instanceof RabbitModel) {
            String modelClassPath7 = RabbitModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath7, "head"));
            result.add(String.format("%s;%s", modelClassPath7, "field_27486"));
            result.add(String.format("%s;%s", modelClassPath7, "f_103523_"));
        } else if (model instanceof RavagerModel) {
            String modelClassPath8 = RavagerModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath8, "head"));
            result.add(String.format("%s;%s", modelClassPath8, "field_3386"));
            result.add(String.format("%s;%s", modelClassPath8, "f_103598_"));
        } else if (model instanceof IronGolemModel) {
            String modelClassPath9 = IronGolemModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath9, "head"));
            result.add(String.format("%s;%s", modelClassPath9, "field_3415"));
            result.add(String.format("%s;%s", modelClassPath9, "f_102936_"));
        } else if (model instanceof SnowGolemModel) {
            String modelClassPath10 = SnowGolemModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath10, "head"));
            result.add(String.format("%s;%s", modelClassPath10, "field_3568"));
            result.add(String.format("%s;%s", modelClassPath10, "f_103839_"));
        } else if (model instanceof EnderDragonRenderer.DragonModel) {
            String modelClassPath11 = EnderDragonRenderer.DragonModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath11, "head"));
            result.add(String.format("%s;%s", modelClassPath11, "field_3630"));
            result.add(String.format("%s;%s", modelClassPath11, "f_114235_"));
        } else if (model instanceof ShulkerModel) {
            String modelClassPath12 = ShulkerModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath12, "head"));
            result.add(String.format("%s;%s", modelClassPath12, "field_3554"));
            result.add(String.format("%s;%s", modelClassPath12, "f_103724_"));
        } else if (model instanceof SlimeModel) {
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("children['%s']", "cube")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("field_3661['%s']", "cube")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("f_104213_['%s']", "cube")));
        } else if (model instanceof AxolotlModel) {
            String modelClassPath13 = AxolotlModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath13, "head"));
            result.add(String.format("%s;%s", modelClassPath13, "field_28379"));
            result.add(String.format("%s;%s", modelClassPath13, "f_170365_"));
        } else if (model instanceof LavaSlimeModel) {
            result.add(String.format("%s;%s", ModelPart.class.getName(), "children['inside_cube']"));
            result.add(String.format("%s;%s", ModelPart.class.getName(), "field_3661['inside_cube']"));
            result.add(String.format("%s;%s", ModelPart.class.getName(), "f_104213_['inside_cube']"));
        } else if ((model instanceof SquidModel) || (model instanceof GhastModel) || (model instanceof StriderModel) || (model instanceof PhantomModel)) {
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("children['%s']", "body")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("field_3661['%s']", "body")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("f_104213_['%s']", "body")));
        } else if ((model instanceof WardenModel) || (model instanceof FrogModel)) {
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("children['%s']", "head")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("field_3661['%s']", "head")));
            result.add(String.format("%s;%s", ModelPart.class.getName(), String.format("f_104213_['%s']", "head")));
        } else if (model instanceof SnifferModel) {
            String modelClassPath14 = SnifferModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath14, "head"));
            result.add(String.format("%s;%s", modelClassPath14, "field_43085"));
            result.add(String.format("%s;%s", modelClassPath14, "f_273862_"));
        } else if (model instanceof CamelModel) {
            String modelClassPath15 = CamelModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath15, "head"));
            result.add(String.format("%s;%s", modelClassPath15, "field_40464"));
            result.add(String.format("%s;%s", modelClassPath15, "f_243837_"));
        } else if (model instanceof BreezeModel) {
            String modelClassPath16 = BreezeModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath16, "head"));
            result.add(String.format("%s;%s", modelClassPath16, "field_47435"));
            result.add(String.format("%s;%s", modelClassPath16, "f_302678_"));
        } else if (model instanceof ArmadilloModel) {
            String modelClassPath17 = ArmadilloModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath17, "head"));
            result.add(String.format("%s;%s", modelClassPath17, "field_47872"));
        } else if (model instanceof WolfModel) {
            String modelClassPath18 = WolfModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath18, "realHead"));
            result.add(String.format("%s;%s", modelClassPath18, "field_20788"));
        }
        return result;
    }

    public static List<String> getSecondaryModelPartsFields(EntityRenderer<?> renderer, EntityModel<?> model, Entity entity) {
        List<String> result = new ArrayList<>();
        if (model instanceof RabbitModel) {
            String modelClassPath = RabbitModel.class.getName();
            result.add(String.format("%s;%s", modelClassPath, "rightEar"));
            result.add(String.format("%s;%s", modelClassPath, "field_27487"));
            result.add(String.format("%s;%s", modelClassPath, "f_170877_"));
            result.add(String.format("%s;%s", modelClassPath, "leftEar"));
            result.add(String.format("%s;%s", modelClassPath, "field_27488"));
            result.add(String.format("%s;%s", modelClassPath, "f_170878_"));
            result.add(String.format("%s;%s", modelClassPath, "nose"));
            result.add(String.format("%s;%s", modelClassPath, "field_3530"));
            result.add(String.format("%s;%s", modelClassPath, "f_103527_"));
        }
        return result;
    }

    public static Object getModelRoot(EntityModel<?> entityModel) {
        if ((entityModel instanceof SquidModel) || (entityModel instanceof GhastModel) || (entityModel instanceof SlimeModel) || (entityModel instanceof PhantomModel) || (entityModel instanceof StriderModel) || (entityModel instanceof LavaSlimeModel)) {
            return ((HierarchicalModel) entityModel).root();
        }
        if (entityModel instanceof WardenModel) {
            return ((HierarchicalModel) entityModel).root().getChild("bone").getChild("body");
        }
        if (entityModel instanceof FrogModel) {
            return ((HierarchicalModel) entityModel).root().getChild("body");
        }
        return entityModel;
    }

    public static boolean forceFieldCheck(EntityModel<?> entityModel) {
        return (entityModel instanceof AxolotlModel) || (entityModel instanceof WolfModel);
    }

    public static void defaultTransformation(PoseStack matrixStack, EntityModel em, Entity entity) {
        if ((em instanceof CodModel) || (em instanceof SalmonModel)) {
            OptimizedMath.rotatePose(matrixStack, 90.0f, OptimizedMath.YP);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if (em instanceof ColorableHierarchicalModel) {
            OptimizedMath.rotatePose(matrixStack, 90.0f, OptimizedMath.YP);
            return;
        }
        if (em instanceof BatModel) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if (em instanceof HorseModel) {
            OptimizedMath.rotatePose(matrixStack, 65.0f, OptimizedMath.XP);
            matrixStack.scale(0.7f, 0.7f, 0.7f);
            return;
        }
        if ((em instanceof DolphinModel) || (em instanceof GoatModel)) {
            matrixStack.scale(0.7f, 0.7f, 0.7f);
            return;
        }
        if ((em instanceof GuardianModel) || (em instanceof SquidModel)) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if (em instanceof HoglinModel) {
            OptimizedMath.rotatePose(matrixStack, 45.0f, OptimizedMath.XP);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if ((em instanceof LlamaModel) || (em instanceof CamelModel) || (em instanceof SnifferModel)) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            slimeSquishBU = slime.squish;
            slime.squish = 0.0f;
            return;
        }
        if ((em instanceof GhastModel) || (em instanceof RavagerModel) || (em instanceof StriderModel) || (em instanceof EnderDragonRenderer.DragonModel)) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }
        if (em instanceof WitherBossModel) {
            matrixStack.scale(0.35f, 0.35f, 0.35f);
            return;
        }
        if (em instanceof PhantomModel) {
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            OptimizedMath.rotatePose(matrixStack, 90.0f, OptimizedMath.XP);
        } else if (em instanceof PandaModel) {
            matrixStack.scale(0.7f, 0.7f, 0.7f);
        } else if (em instanceof WardenModel) {
            matrixStack.scale(0.7f, 0.7f, 0.7f);
        }
    }

    public static void defaultPostIconModelRender(PoseStack matrixStack, EntityModel entityModel, Entity entity) {
        if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            slime.squish = slimeSquishBU;
        }
    }

    public static boolean fullModelIcon(EntityModel em) {
        return (em instanceof CodModel) || (em instanceof SalmonModel) || (em instanceof ColorableHierarchicalModel) || (em instanceof BeeModel) || (em instanceof DolphinModel) || (em instanceof GuardianModel) || (em instanceof EndermiteModel) || (em instanceof LavaSlimeModel) || (em instanceof SlimeModel) || (em instanceof PufferfishBigModel) || (em instanceof PufferfishMidModel) || (em instanceof PufferfishSmallModel) || (em instanceof SilverfishModel) || (em instanceof WitherBossModel);
    }

    public static <T extends Entity> RadarIconCustomPrerenderer getCustomLayer(EntityRenderer<? super T> entityRenderer, T entity) {
        return null;
    }

    public static <E extends Entity> Object getVariant(ResourceLocation entityTexture, EntityRenderer<? super E> entityRenderer, E entity) {
        if (entity instanceof Horse) {
            return new HorseVariant(entityTexture, ((Horse) entity).getMarkings());
        }
        if (entity instanceof VillagerDataHolder) {
            VillagerData villagerdata = ((VillagerDataHolder) entity).getVillagerData();
            VillagerType villagertype = villagerdata.getType();
            VillagerProfession villagerprofession = villagerdata.getProfession();
            int villagerprofessionlevel = villagerdata.getLevel();
            return new VillagerVariant(entityTexture, ((LivingEntity) entity).isBaby(), villagertype, villagerprofession, villagerprofessionlevel);
        }
        if ((entity instanceof Cat) || (entity instanceof Wolf)) {
            return new TamableVariant(entityTexture, ((TamableAnimal) entity).isTame());
        }
        if (entity instanceof IronGolem) {
            return new IronGolemVariant(entityTexture, ((IronGolem) entity).getCrackiness());
        }
        if (entity instanceof Llama) {
            Llama llama = (Llama) entity;
            return new LlamaVariant(entityTexture, llama.isTraderLlama(), llama.getSwag());
        }
        if (entity instanceof Pig) {
            return new SaddleVariant(entityTexture, ((Pig) entity).isSaddled());
        }
        if (entity instanceof Strider) {
            return new SaddleVariant(entityTexture, ((Strider) entity).isSaddled());
        }
        if (entity instanceof TropicalFish) {
            TropicalFish fish = (TropicalFish) entity;
            return new TropicalFishVariant(entityTexture, fish.getVariant(), fish.getBaseColor(), fish.getPatternColor());
        }
        if (entity instanceof EnderMan) {
            EnderMan enderman = (EnderMan) entity;
            return new EndermanVariant(entityTexture, enderman.isCreepy());
        }
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            return BuiltInRegistries.ITEM.getKey(itemEntity.getItem().getItem());
        }
        if (!(entity instanceof ItemFrame)) {
            return entityTexture == null ? "default" : entityTexture;
        }
        ItemFrame itemFrame = (ItemFrame) entity;
        ItemStack itemFrameStack = itemFrame.getItem();
        Item item = itemFrameStack == null ? Items.AIR : itemFrameStack.getItem();
        return BuiltInRegistries.ITEM.getKey(item);
    }

    static {
        try {
            BUILD_VARIANT_ID_STRING_METHOD = EntityIconDefinitions.class.getDeclaredMethod("buildVariantIdString", StringBuilder.class, EntityRenderer.class, Entity.class);
            GET_VARIANT_ID_STRING_METHOD = EntityIconDefinitions.class.getDeclaredMethod("getVariantString", EntityRenderer.class, Entity.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
