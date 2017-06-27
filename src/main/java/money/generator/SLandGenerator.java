package money.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import com.google.common.collect.Maps;
import money.MoneySLand;
import money.sland.SLand;
import money.utils.FrameRangeBlockPlacer;
import money.utils.Range;
import money.utils.RangeBlockPlacer;
import money.utils.SingleBlockPlacer;

import java.util.*;

/**
 * SLand 世界生成器
 *
 * @author Him188 @ MoneySLand Project
 */
public class SLandGenerator extends Generator {
	public static final String[] GENERATOR_NAMES = {
			"sland", //第一个值会被作为默认生成器名字(使用指令创建地皮时)
			"land",
			"地皮",
			"plot",
			"moneysland"
	};

	/**
	 * unmodifiable
	 */
	public static Map<String, Object> DEFAULT_SETTINGS;

	public static void setDefaultSettings(Map<String, Object> defaultSettings) {
		if (DEFAULT_SETTINGS != null) {
			throw new RuntimeException("default settings is already set");
		}
		DEFAULT_SETTINGS = Collections.unmodifiableMap(defaultSettings);
	}


	public static int TYPE_LAND = 1004;

	private final Map<String, Object> options;

	//private final List<Populator> populators = new ArrayList<>();

	private static final Block AIR = Block.get(Block.AIR);

	private static final int DEFAULT_AISLE_WIDTH = 2;
	private static final int DEFAULT_FRAME_WIDTH = 1;

	private static final Block DEFAULT_FILL_BLOCK = Block.get(Block.DIRT);
	private static final Block DEFAULT_LAST_BLOCK = Block.get(Block.BEDROCK);
	private static final Block DEFAULT_AISLE_BLOCK = Block.get(Block.PLANK, 2);
	private static final Block DEFAULT_FRAME_BLOCK = Block.get(Block.DOUBLE_STONE_SLAB);
	private static final Block DEFAULT_GROUND_BLOCK = Block.get(Block.GRASS);
	//顶层半砖
	private static final Block DEFAULT_FRAME_TOP_BLOCK = Block.get(Block.SLAB);

	private static final Block DEFAULT_SHOP_BLOCK = Block.get(Block.NETHERRACK);

	protected int totalWidth;

	protected ChunkManager level;

	//填充地下的方块
	protected Block fillBlock;

	//最底层方块
	protected Block lastBlock;

	//最底层方块
	protected Block topBlock;

	//底部基岩填充
	protected RangeBlockPlacer lastBlockCreate;

	//边框顶部方块填充
	protected RangeBlockPlacer frameTopBlock;
	//过道宽度
	protected RangeBlockPlacer aisleBlockLeft;
	//过道宽度
	protected RangeBlockPlacer aisleBlockRight;

	//过道方块
	protected Block aisleBlock;

	//每块地边框宽度
	protected RangeBlockPlacer frameBlockLeft;

	//顶层边框方块
	protected RangeBlockPlacer frameBlockTopLeft;
	//每块地边框宽度
	protected RangeBlockPlacer frameBlockRight;

	//边框方块
	protected Block frameBlock;

	//地表方块
	protected Block groundBlock;

	//地表高度
	protected int groundHeight;

	//地表宽度
	protected RangeBlockPlacer groundWidth; //center

	//购买地皮的方块
	protected Block shopBlock;

	protected SingleBlockPlacer shopPlacer;

	//边框和过道宽度是否大于总宽度
	protected boolean broken;


	@Override
	public int getId() {
		return TYPE_LAND;
	}

	@Override
	public void init(ChunkManager level, NukkitRandom random) {
		this.level = level;

		this.totalWidth = Integer.parseInt(options.getOrDefault("totalWidth", 20).toString());

		this.groundHeight = toInt(options.getOrDefault("groundHeight", 48));

		this.fillBlock = getBlock(options, "fillBlock", DEFAULT_FILL_BLOCK);
		this.lastBlock = getBlock(options, "lastBlock", DEFAULT_LAST_BLOCK);
		this.aisleBlock = getBlock(options, "aisleBlock", DEFAULT_AISLE_BLOCK);
		this.frameBlock = getBlock(options, "frameBlock", DEFAULT_FRAME_BLOCK);
		this.topBlock = getBlock(options, "frameTopBlock", DEFAULT_FRAME_TOP_BLOCK);
		this.groundBlock = getBlock(options, "groundBlock", DEFAULT_GROUND_BLOCK);
		this.shopBlock = getBlock(options, "shopBlock", DEFAULT_SHOP_BLOCK);

		this.aisleBlockLeft = new RangeBlockPlacer(
				this.aisleBlock,
				0,
				toInt(options.getOrDefault("aisleBlockWidth", DEFAULT_AISLE_WIDTH)));

		this.lastBlockCreate = new RangeBlockPlacer(
				this.lastBlock,
				0,
				toInt(options.getOrDefault("lastBlock", DEFAULT_LAST_BLOCK)));

		this.frameTopBlock = new RangeBlockPlacer(
				this.topBlock,
				0,
				toInt(options.getOrDefault("frameTopBlock", DEFAULT_FRAME_TOP_BLOCK)));
		this.frameBlockLeft = new FrameRangeBlockPlacer(
				this.frameBlock,
				this.aisleBlockLeft.getMax(),
				this.aisleBlockLeft.getMax()
				+ toInt(options.getOrDefault("frameBlockWidth", DEFAULT_FRAME_WIDTH)));

		this.groundWidth = new RangeBlockPlacer(
				this.groundBlock,
				this.frameBlockLeft.getMax(),
				this.totalWidth - this.aisleBlockLeft.getLength() - this.frameBlockLeft.getLength());

		this.frameBlockRight = new RangeBlockPlacer(this.frameBlock,
				this.groundWidth.getMax(),
				this.groundWidth.getMax()
				+ toInt(options.getOrDefault("frameBlockWidth", DEFAULT_FRAME_WIDTH)));

		this.aisleBlockRight = new RangeBlockPlacer(this.aisleBlock,
				this.frameBlockRight.getMax(),
				this.frameBlockRight.getMax()
				+ toInt(options.getOrDefault("aisleBlockWidth", DEFAULT_AISLE_WIDTH)));

		this.shopPlacer = new SingleBlockPlacer(this.shopBlock);

		this.broken = this.groundWidth.getLength() < 0;
		if (this.broken) {
			MoneySLand.getInstance().getLogger().critical("地皮总宽度设置有误, 当前边框宽度, 过道宽度设置下总宽度至少需要 " + (this.totalWidth + -(this.totalWidth - this.aisleBlockLeft.getLength() - this.frameBlockLeft.getLength() - this.frameBlockLeft.getMax())));
		}

		/*
		PopulatorCaves caves = new PopulatorCaves();
		this.populators.add(caves);

		PopulatorRavines ravines = new PopulatorRavines();
		this.populators.add(ravines);

		PopulatorOre ores = new PopulatorOre();
		ores.setOreTypes(new OreType[]{
				new OreType(new BlockOreCoal(), 20, 16, 0, 256),
				new OreType(new BlockOreIron(), 20, 8, 0, 64),
				new OreType(new BlockOreRedstone(), 8, 7, 0, 16),
				new OreType(new BlockOreLapis(), 1, 6, 0, 32),
				new OreType(new BlockOreGold(), 2, 8, 0, 32),
				new OreType(new BlockOreDiamond(), 1, 7, 0, 16),
				new OreType(new BlockDirt(), 20, 32, 0, 256),
				new OreType(new BlockGravel(), 10, 16, 0, 256)
		});
		this.populators.add(ores);*/
	}

	private static int toInt(Object o) {
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	private static Block getBlock(Map<String, Object> map, String name, Block defaultBlock) {
		try {
			Block block = Block.get(toInt(map.getOrDefault(name + "Id", defaultBlock.getId())), toInt(map.getOrDefault(name + "Damage", defaultBlock.getDamage())));
			return block == null ? defaultBlock : block;
		} catch (Exception e) {
			return defaultBlock;
		}
	}

	public SLandGenerator() {
		this(new HashMap<>());
	}

	public SLandGenerator(Map<String, Object> options) {
		this.options = Optional.ofNullable(options).filter(o -> !o.isEmpty()).orElse(Maps.newHashMap(DEFAULT_SETTINGS));
	}


	private Set<Integer> generatedLands = new HashSet<>();

	@Override
	public void generateChunk(int chunkX, int chunkZ) {
		BaseFullChunk chunk = this.level.getChunk(chunkX, chunkZ);
		if (this.broken) {
			return;
		}

		int realChunkX = chunkX * 16;
		int realChunkZ = chunkZ * 16;

		int id = (realChunkX % totalWidth) >> 4 | (realChunkZ % totalWidth);

		int x, z, y; // TODO: 2017/5/16 主动放置方块而不是被动判断范围
		for (int _x = 0; _x < 16; _x++) { //16 不能用 totalWidth 替换, 因为 chunk 的大小只有 16
			x = (_x + realChunkX) % totalWidth;
			for (int _z = 0; _z < 16; _z++) {
				z = (_z + realChunkZ) % totalWidth;
				for (int _y = 0; _y < this.groundHeight; _y++) {

					if (this.aisleBlockLeft.inRange(x) || this.aisleBlockLeft.inRange(z) ||
					    this.aisleBlockRight.inRange(x) || this.aisleBlockRight.inRange(z)) {
						this.aisleBlockLeft.placeBlock(chunk, _x, _y + 1, _z);
					} else if (this.frameBlockLeft.inRange(x) || this.frameBlockLeft.inRange(z) ||
					           this.frameBlockRight.inRange(x) || this.frameBlockRight.inRange(z)) {
						this.frameBlockLeft.placeBlock(chunk, _x, _y, _z);
						this.frameTopBlock.placeBlock(chunk, _x, this.groundHeight + 1, _z);
					} else {
						this.groundWidth.placeBlock(chunk, _x, _y + 1, _z);
					}
					this.lastBlockCreate.placeBlock(chunk, _x, 0, _z);
				}
			}
		}
	}

	@Override
	public void populateChunk(int chunkX, int chunkZ) {
		BaseFullChunk chunk = this.level.getChunk(chunkX, chunkZ);
		if (this.broken) {
			return;
		}

		int realChunkX = chunkX * 16;
		int realChunkZ = chunkZ * 16;

		int id = (realChunkX % totalWidth) >> 4 | (realChunkZ % totalWidth);

		int x, z, y; // TODO: 2017/5/16 主动放置方块而不是被动判断范围
		for (int _x = 0; _x < 16; _x++) { //16 不能用 totalWidth 替换, 因为 chunk 的大小只有 16
			x = (_x + realChunkX) % totalWidth;
			for (int _z = 0; _z < 16; _z++) {
				z = (_z + realChunkZ) % totalWidth;
				label:
				for (int _y = 0; _y < this.groundHeight; _y++) {
					if (this.frameBlockLeft.inRange(x) && this.frameBlockLeft.inRange(z)) {
						//领地方块
						this.shopPlacer.placeBlock(chunk, _x, this.groundHeight + 2, _z);
						if (MoneySLand.getInstance().getLand(new Position(1 + realChunkX + _x, 0, 1 + realChunkZ + _z, chunk.getProvider().getLevel())) !=
						    null) {
							continue label;
						}
						int temp;
						SLand land = SLand.newInitialLand(
								MoneySLand.getInstance().getLandPool().nextLandId(),
								new Range(temp = 1 + realChunkX + _x, temp + this.groundWidth.getLength()),
								new Range(temp = 1 + realChunkZ + _z, temp + this.groundWidth.getLength()),
								chunk.getProvider().getLevel().getFolderName(), //only can be used in populateChunk
								new Vector3(_x + realChunkX, this.groundHeight + 2, _z + realChunkZ)
						);

						MoneySLand.getInstance().getLandPool().add(land);
						MoneySLand.getInstance().getModifiedLandPool().add(land);
						MoneySLand.getInstance().getLogger().debug("SLand #" + land.getId() + " in " + chunk.getProvider().getLevel().getFolderName() + " generated");
					}
				}
			}
		}
	}

	/*
	@Override
	public void populateChunk(int chunkX, int chunkZ) {
		FullChunk chunk = this.level.getChunk(chunkX, chunkZ);
		if (this.broken) {
			return;
		}


		int realChunkX = chunkX * 16;
		int realChunkZ = chunkZ * 16;

		int baseX = realChunkX % totalWidth;
		int baseZ = realChunkZ % totalWidth;

		if (baseX == 0 || baseZ == 0) {
			int minX = baseX + this.aisleBlockLeft.getLength() + this.frameBlockLeft.getLength();
			int maxX = minX + this.groundWidth.getLength();

			int minZ = baseZ + this.aisleBlockLeft.getLength() + this.frameBlockLeft.getLength();
			int maxZ = minZ + this.groundWidth.getLength();


			//if (this.frameBlockLeft.inRange(Math.abs(minX))) {

			//}
		}
	}
	*/
	@Override
	public Map<String, Object> getSettings() {
		return options;
	}

	@Override
	public String getName() {
		return GENERATOR_NAMES[0];
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
