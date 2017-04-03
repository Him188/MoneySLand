package money.range;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;

/**
 * @author Him188
 */
public class AisleBlockPlacer extends BlockPlacer {
	public AisleBlockPlacer(Block block, int min, int max) {
		super(block, min, max);
	}

	public void placeBlock(FullChunk chunk, int x, int y, int z) {
		super.placeBlock(chunk, x, Math.min(y + 1, 255), z);
	}
}
