package com.createmeow.airdrop.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;

public class BaseCraftIntegration {
    private static boolean basecraftLoaded = false;
    private static Method getBasecoreMethod = null;

    static {
        try {
            Class.forName("dev.anye.mc.basecore.BaseCore");
            Class<?> helperClass = Class.forName("dev.anye.mc.basecore.basecore.BasecoreServerHelper");
            getBasecoreMethod = helperClass.getMethod("getBasecore",
                    net.minecraft.world.level.Level.class, Vec3.class, int.class);
            basecraftLoaded = true;
        } catch (Exception e) {
            basecraftLoaded = false;
        }
    }

    public static boolean isBaseCraftLoaded() {
        return basecraftLoaded;
    }

    public static boolean isNearBase(ServerLevel level, BlockPos pos, int minDistance) {
        if (!isBaseCraftLoaded()) {
            return false;
        }
        try {
            Object result = getBasecoreMethod.invoke(null, level, pos.getCenter(), minDistance);
            return result != null;
        } catch (Exception e) {
            return false;
        }
    }
}