package money.range;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * @author Him188 @ MoneySLand Project
 */
public class FrameRangeBlockPlacer extends RangeBlockPlacer implements BlockPlacer {
	public FrameRangeBlockPlacer(Block block, int min, int max) {
		super(block, min, max);
	}

	public void placeBlock(FullChunk chunk, int x, int y, int z) {
		super.placeBlock(chunk, x, Math.min(y + 1, 255), z);
		super.placeBlock(chunk, x, y, z);
	}
}
