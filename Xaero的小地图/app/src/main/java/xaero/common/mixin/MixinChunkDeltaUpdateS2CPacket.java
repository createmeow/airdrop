package xaero.common.mixin;

import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.core.IXaeroMinimapSMultiBlockChangePacket;

@Mixin({ClientboundSectionBlocksUpdatePacket.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinChunkDeltaUpdateS2CPacket.class */
public class MixinChunkDeltaUpdateS2CPacket implements IXaeroMinimapSMultiBlockChangePacket {

    @Shadow
    SectionPos sectionPos;

    @Override // xaero.common.core.IXaeroMinimapSMultiBlockChangePacket
    public SectionPos xaero_mm_getSectionPos() {
        return this.sectionPos;
    }
}
