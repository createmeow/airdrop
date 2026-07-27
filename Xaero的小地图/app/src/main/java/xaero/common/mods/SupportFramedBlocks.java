package xaero.common.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xaero.hud.minimap.MinimapLogs;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/SupportFramedBlocks.class */
public class SupportFramedBlocks {
    private Class<?> framedTileBlockClass;
    private Method framedTileEntityCamoStateMethod;
    private Method framedTileEntityCamoMethod;
    private Method camoContainerStateMethod;
    private Method camoContainerContentMethod;
    private Method camoContentStateMethod;
    private boolean usable;
    private Set<Block> framedBlocks;

    public SupportFramedBlocks() throws ClassNotFoundException {
        Class<?> camoContainerClass;
        try {
            this.framedTileBlockClass = Class.forName("xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity");
        } catch (ClassNotFoundException e) {
            try {
                this.framedTileBlockClass = Class.forName("xfacthd.framedblocks.common.tileentity.FramedTileEntity");
            } catch (ClassNotFoundException e2) {
                try {
                    this.framedTileBlockClass = Class.forName("xfacthd.framedblocks.api.block.FramedBlockEntity");
                } catch (ClassNotFoundException cnfe3) {
                    MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", cnfe3);
                    return;
                }
            }
        }
        try {
            this.framedTileEntityCamoStateMethod = this.framedTileBlockClass.getDeclaredMethod("getCamoState", new Class[0]);
        } catch (NoSuchMethodException | SecurityException e1) {
            try {
                try {
                    camoContainerClass = Class.forName("xfacthd.framedblocks.api.data.CamoContainer");
                } catch (ClassNotFoundException e3) {
                    camoContainerClass = Class.forName("xfacthd.framedblocks.api.camo.CamoContainer");
                }
                this.framedTileEntityCamoMethod = this.framedTileBlockClass.getDeclaredMethod("getCamo", new Class[0]);
                try {
                    this.camoContainerStateMethod = camoContainerClass.getDeclaredMethod("getState", new Class[0]);
                } catch (NoSuchMethodException e4) {
                    this.camoContainerContentMethod = camoContainerClass.getDeclaredMethod("getContent", new Class[0]);
                    Class<?> camoContentClass = Class.forName("xfacthd.framedblocks.api.camo.CamoContent");
                    this.camoContentStateMethod = camoContentClass.getDeclaredMethod("getAppearanceState", new Class[0]);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e22) {
                MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", e1);
                MinimapLogs.LOGGER.info("Failed to init Framed Blocks support!", e22);
            }
        }
        this.usable = (this.framedTileBlockClass == null || (this.framedTileEntityCamoStateMethod == null && (this.framedTileEntityCamoMethod == null || (this.camoContainerStateMethod == null && (this.camoContainerContentMethod == null || this.camoContentStateMethod == null))))) ? false : true;
    }

    public void onWorldChange() {
        this.framedBlocks = null;
    }

    private void findFramedBlocks(Level world, Registry<Block> registry) {
        if (this.framedBlocks == null) {
            this.framedBlocks = new HashSet();
            if (registry == null) {
                registry = world.registryAccess().registryOrThrow(Registries.BLOCK);
            }
            registry.entrySet().forEach(entry -> {
                ResourceKey<Block> key = (ResourceKey) entry.getKey();
                if (key.location().getNamespace().equals("framedblocks") && key.location().getPath().startsWith("framed_")) {
                    this.framedBlocks.add((Block) entry.getValue());
                }
            });
        }
    }

    public boolean isFrameBlock(Level world, Registry<Block> registry, BlockState state) {
        if (!this.usable) {
            return false;
        }
        findFramedBlocks(world, registry);
        return this.framedBlocks.contains(state.getBlock());
    }

    public BlockState unpackFramedBlock(Level world, Registry<Block> registry, BlockState original, BlockEntity tileEntity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!this.usable) {
            return original;
        }
        if (this.framedTileBlockClass.isAssignableFrom(tileEntity.getClass())) {
            if (this.framedTileEntityCamoStateMethod != null) {
                return (BlockState) ReflectionUtils.getReflectMethodValue(tileEntity, this.framedTileEntityCamoStateMethod, new Object[0]);
            }
            Object camoContainer = ReflectionUtils.getReflectMethodValue(tileEntity, this.framedTileEntityCamoMethod, new Object[0]);
            if (this.camoContainerStateMethod != null) {
                return (BlockState) ReflectionUtils.getReflectMethodValue(camoContainer, this.camoContainerStateMethod, new Object[0]);
            }
            Object camoContent = ReflectionUtils.getReflectMethodValue(camoContainer, this.camoContainerContentMethod, new Object[0]);
            if (camoContent == null) {
                return original;
            }
            BlockState state = (BlockState) ReflectionUtils.getReflectMethodValue(camoContent, this.camoContentStateMethod, new Object[0]);
            if (state == null) {
                return original;
            }
            return state;
        }
        return original;
    }
}
