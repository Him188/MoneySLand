package money.block;

import cn.nukkit.block.Block;

/**
 * The initial block which can buy the land
 *
 * @author Him188
 */
public class SLandShopBlock extends SLandBlock {
	public SLandShopBlock() {
		this(0);
	}

	protected SLandShopBlock(Integer meta) {
		super(meta);
	}

	@Override
	public String getName() {
		return "领地商店方块";
	}

	@Override
	public int getId() {
		return Block.NETHERRACK;
	}
}
