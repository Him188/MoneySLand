package money.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

import java.util.Set;

public class BigChunk {
	private final Set<FullChunk> chunks;

	public BigChunk(Set<FullChunk> chunks) {
		this.chunks = chunks;
	}

	public Set<FullChunk> getChunks() {
		return chunks;
	}

	public boolean setBlock(int x, int y, int z, int id, int meta) {
		for (FullChunk chunk : chunks) {
			if (x >= chunk.getX() && x <= chunk.getX()+16 && z >= chunk.getZ() && z <= chunk.getZ()+16) {
				return chunk.setBlock(x, y, z, id, meta);
			}
		}
		
		return false;
	}

	public boolean setBlock(int x, int y, int z, Block block) {
		return setBlock(x, y, z, block.getId(), block.getDamage());
	}
}
