package org.spongepowered.common.mixin.core.world.chunk.storage;

import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionFileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.DataInputStream;
import java.io.File;

@Mixin(RegionFileCache.class)
public abstract class RegionFileCacheMixin {

    @Shadow public static RegionFile getRegionFileIfExists(File worldDir, int chunkX, int chunkZ) {return null;}

    /**
     * @author JBYoshi
     * @reason Support for ChunkSerializationBehaviors that don't save chunks:
     * don't create files if they aren't necessary. (The original method
     * already returns null if the chunk doesn't exist.)
     */
    @Overwrite
    public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
        // Sponge start
        RegionFile regionfile = getRegionFileIfExists(worldDir, chunkX, chunkZ);
        if (regionfile == null) {
            return null;
        }
        // Sponge end
        return regionfile.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
    }
}
