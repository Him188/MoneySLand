package money.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import money.MoneySLand;
import money.range.BlockPlacer;

import java.util.*;

public class SLandGenerator extends Generator {
	public static int TYPE_LAND = 1004;
	private final Map<String, Object> options;

	//private final List<Populator> populators = new ArrayList<>();

	private static final Block AIR = Block.get(Block.AIR);
	private static final Block DEFAULT_FILL_BLOCK = Block.get(Block.DIRT);
	private static final Block DEFAULT_LAST_BLOCK = Block.get(Block.BEDROCK);
	private static final Block DEFAULT_AISLE_BLOCK = Block.get(125, 2);
	private static final Block DEFAULT_FRAME_BLOCK = Block.get(Block.DOUBLE_STONE_SLAB);
	private static final Block DEFAULT_GROUND_BLOCK = Block.get(Block.GRASS);

	protected int totalWidth = 3 * 16;

	protected ChunkManager level;

	protected int range;

	//填充地下的方块
	protected Block fillBlock;

	//最底层方块
	protected Block lastBlock;

	//过道宽度
	protected BlockPlacer aisleBlockLeft;
	//过道宽度
	protected BlockPlacer aisleBlockRight;

	//过道方块
	protected Block aisleBlock;

	//每块地边框宽度
	protected BlockPlacer frameBlockLeft;
	//每块地边框宽度
	protected BlockPlacer frameBlockRight;

	//边框方块
	protected Block frameBlock;

	//地表方块
	protected Block groundBlock;

	//地表高度
	protected int groundHeight;

	//地表宽度
	protected BlockPlacer groundWidth; //center

	//边框和过道宽度是否大于总宽度
	protected boolean broken;


	@Override
	public int getId() {
		return TYPE_LAND;
	}

	@Override
	public void init(ChunkManager level, NukkitRandom random) {
		this.level = level;

		//   64 32 16
		//4^  4  3  2
		//limited range
		this.totalWidth = Integer.parseInt(options.getOrDefault("totalWidth", 1).toString());
		if (this.totalWidth % 16 != 0) {
			MoneySLand.getInstance().getLogger().warning("总宽度配置有误, 总宽度必须为16的正整数倍!");
		}

		this.groundHeight = toInt(options.getOrDefault("groundHeight", 2));

		this.fillBlock = getBlock(options, "fillBlock", DEFAULT_FILL_BLOCK);
		this.lastBlock = getBlock(options, "lastBlock", DEFAULT_LAST_BLOCK);
		this.aisleBlock = getBlock(options, "aisleBlock", DEFAULT_AISLE_BLOCK);
		this.frameBlock = getBlock(options, "frameBlock", DEFAULT_FRAME_BLOCK);
		this.groundBlock = getBlock(options, "groundBlock", DEFAULT_GROUND_BLOCK);

		this.aisleBlockLeft = new BlockPlacer(this.aisleBlock, 0, toInt(options.getOrDefault("aisleBlockWidth", 2)));
		this.frameBlockLeft = new BlockPlacer(this.frameBlock, this.aisleBlockLeft.getMax(), this.aisleBlockLeft.getMax() + toInt(options.getOrDefault("frameBlockWidth", 2)));
		this.groundWidth = new BlockPlacer(this.groundBlock, this.frameBlockLeft.getMax(), this.totalWidth - this.aisleBlockLeft.getLength() * 2 - this.frameBlockLeft.getLength() * 2);
		this.frameBlockRight = new BlockPlacer(this.frameBlock, this.groundWidth.getMax(), this.groundWidth.getMax() + toInt(options.getOrDefault("frameBlockWidth", 2)));
		this.aisleBlockRight = new BlockPlacer(this.aisleBlock, this.frameBlockRight.getMax(), this.frameBlockRight.getMax() + toInt(options.getOrDefault("aisleBlockWidth", 2)));

		this.broken = this.groundWidth.getLength() <= 0;

		/*
		PopulatorCaves caves = new PopulatorCaves();
		this.populators.add(caves);

		PopulatorRavines ravines = new PopulatorRavines();
		this.populators.add(ravines);

		PopulatorOre ores = new PopulatorOre();
		ores.setOreTypes(new OreType[]{
				new OreType(new BlockOreCoal(), 20, 16, 0, 128),
				new OreType(new BlockOreIron(), 20, 8, 0, 64),
				new OreType(new BlockOreRedstone(), 8, 7, 0, 16),
				new OreType(new BlockOreLapis(), 1, 6, 0, 32),
				new OreType(new BlockOreGold(), 2, 8, 0, 32),
				new OreType(new BlockOreDiamond(), 1, 7, 0, 16),
				new OreType(new BlockDirt(), 20, 32, 0, 128),
				new OreType(new BlockGravel(), 10, 16, 0, 128)
		});
		this.populators.add(ores);*/
	}

	private int toInt(Object o) {
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	private Block getBlock(Map<String, Object> map, String name, Block defaultBlock) {
		try {
			Block block = Block.get(toInt(map.getOrDefault(name + "Id", 0)), toInt(map.getOrDefault(name + "Damage", 0)));
			return block == null ? defaultBlock : block;
		} catch (Exception e) {
			return defaultBlock;
		}
	}

	public SLandGenerator() {
		this(new HashMap<>());
	}

	public SLandGenerator(Map<String, Object> options) {
		this.options = options;
	}

	protected void generateBrokenChunk(FullChunk chunk) {
		for (int i = 0; i < 16; i++) {
			for (int i1 = 0; i1 < 16; i1++) {
				for (int i2 = 0; i2 < 128; i2++) {
					chunk.setBlockId(i, i1, i2, 0);
				}
			}
		}
	}

	@Override
	public void generateChunk(int chunkX, int chunkZ) {
		FullChunk chunk = this.level.getChunk(chunkX, chunkZ);
		if (this.broken) {
			generateBrokenChunk(chunk);
			return;
		}

		int realChunkX = chunkX * 16;
		int realChunkZ = chunkZ * 16;

		int x, z;
		for (int _x = 0; _x < 16; _x++) { //16 不能用 totalWidth 替换, 因为 chunk 的大小只有 16
			x = (_x + realChunkX) % totalWidth;
			for (int _z = 0; _z < 16; _z++) {
				z = (_z + realChunkZ) % totalWidth;

				if (this.aisleBlockLeft.inRange(x) || this.aisleBlockLeft.inRange(z) ||
						this.aisleBlockRight.inRange(x) || this.aisleBlockRight.inRange(z)) {
					this.aisleBlockLeft.placeBlock(chunk, _x, this.groundHeight, _z);
				} else if (this.frameBlockLeft.inRange(x) || this.frameBlockLeft.inRange(z) ||
						this.frameBlockRight.inRange(x) || this.frameBlockRight.inRange(z)) {
					this.frameBlockLeft.placeBlock(chunk, _x, this.groundHeight, _z);
				} else {
					this.groundWidth.placeBlock(chunk, _x, this.groundHeight, _z);
				}
			}
		}

		for (x = 0; x < 16; x++) {
			for (z = 0; z < 16; z++) {
				for (int y = 0; y < this.groundHeight - 1; y++) {
					chunk.setBlockId(x, y, z, this.fillBlock.getId());
					chunk.setBlockData(x, y, z, this.fillBlock.getDamage());
				}

				for (int y = this.groundHeight + 1; y < 128; y++) {
					chunk.setBlockId(x, y, z, AIR.getId());
					chunk.setBlockData(x, y, z, AIR.getDamage());
				}
			}
		}
	}

	@Override
	public void populateChunk(int chunkX, int chunkZ) {
		// TODO: 2017/3/31  创建领地购买信息
	}

	@Override
	public Map<String, Object> getSettings() {
		return options;
	}

	@Override
	public String getName() {
		return "sland";
	}

	@Override
	public Vector3 getSpawn() {
		return new Vector3();
	}

	@Override
	public ChunkManager getChunkManager() {
		return level;
	}
}
