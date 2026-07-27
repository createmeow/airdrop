package xaero.hud.minimap.world;

import java.util.Set;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/MinimapDimensionHelper.class */
public class MinimapDimensionHelper {
    public double getDimensionDivision(MinimapWorld minimapWorld) {
        if (Minecraft.getInstance().level == null) {
            return 1.0d;
        }
        double dimCoordinateScale = getDimCoordinateScale(minimapWorld);
        return Minecraft.getInstance().level.dimensionType().coordinateScale() / dimCoordinateScale;
    }

    public double getDimCoordinateScale(MinimapWorld minimapWorld) {
        if (minimapWorld == null) {
            return 1.0d;
        }
        MinimapWorldRootContainer rootContainer = minimapWorld.getContainer().getRoot();
        ResourceKey<Level> dimKey = minimapWorld.getDimId();
        if (dimKey == null) {
            return 1.0d;
        }
        return rootContainer.getDimensionScale(dimKey);
    }

    public String getDimensionDirectoryName(ResourceKey<Level> dimKey) {
        if (dimKey == Level.OVERWORLD) {
            return "dim%0";
        }
        if (dimKey == Level.NETHER) {
            return "dim%-1";
        }
        if (dimKey == Level.END) {
            return "dim%1";
        }
        ResourceLocation identifier = dimKey.location();
        String path = identifier.getPath().replace('/', '%');
        return "dim%" + identifier.getNamespace() + "$" + IOUtils.replaceTrailingDots(path, ',');
    }

    public ResourceKey<Level> findDimensionKeyForOldName(LocalPlayer player, String oldName) {
        Set<ResourceKey<Level>> allDimensions = player.connection.levels();
        for (ResourceKey<Level> dk : allDimensions) {
            if (oldName.equals(dk.location().getPath().replaceAll("[^a-zA-Z0-9_]+", ""))) {
                return dk;
            }
        }
        return null;
    }

    public ResourceKey<Level> getDimensionKeyForDirectoryName(String dirName) {
        String dimIdPart = dirName.substring(4);
        if (dimIdPart.equals("0")) {
            return Level.OVERWORLD;
        }
        if (dimIdPart.equals("1")) {
            return Level.END;
        }
        if (dimIdPart.equals("-1")) {
            return Level.NETHER;
        }
        String[] idArgs = dimIdPart.split("\\$");
        if (idArgs.length < 2) {
            return null;
        }
        String path = idArgs[1].replace('%', '/');
        try {
            return ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(idArgs[0], path.replace(',', '.')));
        } catch (ResourceLocationException e) {
            return null;
        }
    }
}
