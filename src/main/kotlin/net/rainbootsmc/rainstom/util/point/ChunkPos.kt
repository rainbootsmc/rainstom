package net.rainbootsmc.rainstom.util.point

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Chunk
import net.minestom.server.utils.chunk.ChunkUtils

data class ChunkPos(val x: Int, val z: Int) {
    constructor(long: Long) : this(long.toInt(), (long shr 32).toInt())

    fun toLong(): Long {
        return x.toLong() and 0xFFFFFFFFL or (z.toLong() and 0xFFFFFFFFL shl 32)
    }

    companion object {
        fun fromIndex(index: Long): ChunkPos {
            return ChunkPos(index.toInt(), (index shr 32).toInt())
        }

        fun Point.toChunkPos(): ChunkPos {
            return fromIndex(ChunkUtils.getChunkIndex(this))
        }

        fun Chunk.toChunkPos(): ChunkPos {
            return ChunkPos(chunkX, chunkZ)
        }
    }
}
