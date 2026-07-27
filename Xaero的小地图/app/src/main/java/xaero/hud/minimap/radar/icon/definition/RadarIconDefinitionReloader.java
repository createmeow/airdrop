package xaero.hud.minimap.radar.icon.definition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import xaero.hud.minimap.MinimapLogs;
import xaero.minimap.XaeroMinimap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/RadarIconDefinitionReloader.class */
public class RadarIconDefinitionReloader {
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public void reloadResources(Map<ResourceLocation, RadarIconDefinition> iconDefinitions) {
        MinimapLogs.LOGGER.info("Reloading radar icon resources...");
        Set<ResourceLocation> entityIds = BuiltInRegistries.ENTITY_TYPE.keySet();
        for (int i = 0; i < 5; i++) {
            try {
                reloadResourcesAttempt(iconDefinitions, this.gson, entityIds);
                break;
            } catch (IOException ioe) {
                if (i == 5 - 1) {
                    throw new RuntimeException(ioe);
                }
            }
        }
        MinimapLogs.LOGGER.info("Reloaded radar icon resources!");
    }

    private void reloadResourcesAttempt(Map<ResourceLocation, RadarIconDefinition> iconDefinitions, Gson gson, Set<ResourceLocation> entityIds) throws IOException {
        iconDefinitions.clear();
        for (ResourceLocation id : entityIds) {
            InputStream resourceInput = null;
            BufferedReader reader = null;
            try {
                Optional<Resource> oResource = Minecraft.getInstance().getResourceManager().getResource(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "entity/icon/definition/" + id.getNamespace() + "/" + id.getPath() + ".json"));
                if (oResource.isPresent()) {
                    Resource resource = oResource.get();
                    if (resource == null) {
                        if (0 != 0) {
                            reader.close();
                        }
                        if (0 != 0) {
                            resourceInput.close();
                        }
                    } else {
                        InputStream resourceInput2 = resource.open();
                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(resourceInput2));
                        StringBuilder stringBuilder = new StringBuilder();
                        reader2.lines().forEach(line -> {
                            stringBuilder.append(line);
                            stringBuilder.append('\n');
                        });
                        String entityDefinitionJson = stringBuilder.toString();
                        if (reader2 != null) {
                            reader2.close();
                        }
                        if (resourceInput2 != null) {
                            resourceInput2.close();
                        }
                        try {
                            RadarIconDefinition radarIconDefinition = (RadarIconDefinition) gson.fromJson(entityDefinitionJson, RadarIconDefinition.class);
                            radarIconDefinition.construct(id);
                            iconDefinitions.put(id, radarIconDefinition);
                        } catch (JsonSyntaxException jse) {
                            MinimapLogs.LOGGER.error("Json syntax exception when loading the radar icon definition for " + String.valueOf(id) + ".", jse);
                        }
                    }
                } else {
                    if (0 != 0) {
                        reader.close();
                    }
                    if (0 != 0) {
                        resourceInput.close();
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    reader.close();
                }
                if (0 != 0) {
                    resourceInput.close();
                }
                throw th;
            }
        }
    }
}
