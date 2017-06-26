package money.utils;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * Block placer
 *
 * @author Him188 @ MoneySLand Project
 */
public class RangeBlockPlacer extends Range implements BlockPlacer {
	public final Block block;

	public RangeBlockPlacer(Block block, int min, int max) {
		super(min, max);
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void placeBlock(FullChunk chunk, int x, int y, int z) {
		chunk.setBlockId(x, y, z, block.getId());
		chunk.setBlockData(x, y, z, block.getDamage());
	}
}
