package money.utils;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * @author Him188 @ MoneySLand Project
 */
public class SingleBlockPlacer implements BlockPlacer {
	private final Block block;

	public SingleBlockPlacer(Block block) {
		this.block = block;
	}

	@Override
	public void placeBlock(FullChunk chunk, int x, int y, int z) {
		chunk.setBlock(x, y, z, block.getId());
		chunk.setBlockData(x, y, z, block.getDamage());
	}
}
