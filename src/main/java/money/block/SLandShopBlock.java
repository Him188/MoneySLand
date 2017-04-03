package money.block;

import cn.nukkit.block.Block;
import cn.nukkit.metadata.MetadataValue;
import money.MoneySLand;

/**
 * The initial block which can buy the land
 *
 * @author Him188
 */
public class SLandShopBlock extends SLandBlock {
	public static final int ID = Block.NETHERRACK;

	public SLandShopBlock() throws Exception {
		this(0);
	}

	protected SLandShopBlock(Integer meta) throws Exception {
		super(meta);
		this.setMetadata("sland", new MetadataValue(MoneySLand.getInstance()) {
			@Override
			public Object value() {
				return true;
			}

			@Override
			public void invalidate() {

			}
		});
	}

	@Override
	public String getName() {
		return "领地商店方块";
	}

	@Override
	public int getId() {
		return Block.NETHERRACK;
	}

	public static boolean equals(Block block) {
		try {
			return block.getId() == SLandShopBlock.ID && block.hasMetadata("sland");
		} catch (Exception e) {
			return false;
		}
	}
}
