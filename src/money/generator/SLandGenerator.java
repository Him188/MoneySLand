package money.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

public class SLandGenerator extends Generator {
	public static int TYPE_LAND = 1004;
	private final Map<String, Object> options;

	//private final List<Populator> populators = new ArrayList<>();
	private int size;

	private static final Block AIR = Block.get(Block.AIR);
	private static final Block DEFAULT_FILL_BLOCK = Block.get(Block.DIRT);
	private static final Block DEFAULT_LAST_BLOCK = Block.get(Block.BEDROCK);
	private static final Block DEFAULT_AISLE_BLOCK = Block.get(125, 2);
	private static final Block DEFAULT_FRAME_BLOCK = Block.get(Block.DOUBLE_STONE_SLAB);
	private static final Block DEFAULT_GROUND_BLOCK = Block.get(Block.GRASS);

	private ChunkManager level;

	//区域大小
	private int range;

	//填充地下的方块
	private Block fillBlock;

	//最底层方块
	private Block lastBlock;

	//过道宽度
	private int aisleBlockWidth;

	//过道方块
	private Block aisleBlock;

	//每块地边框宽度
	private int frameBlockWidth;

	//边框方块
	private Block frameBlock;

	//地表方块
	private Block groundBlock;

	//地表高度
	private int groundHeight;

	//地表宽度
	private int groundWidth;

	//边框和过道宽度是否大于总宽度
	private boolean broken;


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
		this.size = Math.min(Math.max((int) Math.pow(4, 1 + Integer.parseInt(options.getOrDefault("size", 3).toString())), 64), 0);
		this.range = 64 / size;

		this.fillBlock = getBlock(options, "fillBlock", DEFAULT_FILL_BLOCK);
		this.lastBlock = getBlock(options, "lastBlock", DEFAULT_LAST_BLOCK);

		this.groundBlock = getBlock(options, "groundBlock", DEFAULT_GROUND_BLOCK);
		this.groundHeight = toInt(options.getOrDefault("groundHeight", 2));


		this.aisleBlockWidth = toInt(options.getOrDefault("aisleBlockWidth", 2));
		this.aisleBlock = getBlock(options, "aisleBlock", DEFAULT_AISLE_BLOCK);

		this.frameBlockWidth = toInt(options.getOrDefault("frameBlockWidth", 2));
		this.frameBlock = getBlock(options, "frameBlock", DEFAULT_FRAME_BLOCK);

		this.groundWidth = this.range - this.frameBlockWidth * 2 - this.aisleBlockWidth * 2;

		this.broken = this.range - this.frameBlockWidth - this.aisleBlockWidth <= 0;

		chunks = new HashMap<>();

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


	private Map<Integer, Set<FullChunk>> chunks;

	@Override
	public void generateChunk(int chunkX, int chunkZ) {
		FullChunk chunk = this.level.getChunk(chunkX, chunkZ);

		if (broken) {
			return;
		}

		int xId = chunkX / 64 * 64;
		int zId = chunkZ / 64 * 64;
		// TODO: 2017/2/19 0019
		int id = xId ^ zId;

		Set<FullChunk> set = chunks.getOrDefault(id, new HashSet<>());
		switch (chunkX % 32 + ":" + chunkZ % 32) {
			case "0:0":
				/*
				- 过道, = 边框, * 地表

				   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 ...
				 0 - - - - - - - - - - - - - - - -
				 1 - = = = = = = = = = = = = = = =
				 2 - = * * * * * * * * * * * * * *
				 3 - = * * * * * * * * * * * * * *
				 4 - = * * * * * * * * * * * * * *
				 5 - = * * * * * * * * * * * * * *
				 6 - = * * * * * * * * * * * * * *
				 7 - = * * * * * * * * * * * * * *
				 8 - = * * * * * * * * * * * * * *
				 9 - = * * * * * * * * * * * * * *
				 0 - = * * * * * * * * * * * * * *
				 1 - = * * * * * * * * * * * * * *
				 2 - = * * * * * * * * * * * * * *
				 3 - = * * * * * * * * * * * * * *
				 4 - = * * * * * * * * * * * * * *
				 5 - - * * * * * * * * * * * * * *
				 ...
				*/

				set.add(chunk);
				set.add(chunk.getProvider().getChunk(chunkX + 16, chunkZ + 16, true));
				set.add(chunk.getProvider().getChunk(chunkX, chunkZ + 16, true));
				set.add(chunk.getProvider().getChunk(chunkX + 16, chunkZ, true));
				break;
			case "0:1":
				set.add(chunk);
				set.add(chunk.getProvider().getChunk(chunkX + 16, chunkZ - 16, true));
				set.add(chunk.getProvider().getChunk(chunkX, chunkZ - 16, true));
				set.add(chunk.getProvider().getChunk(chunkX + 16, chunkZ, true));
				break;
			case "1:1":
				set.add(chunk);
				set.add(chunk.getProvider().getChunk(chunkX - 16, chunkZ - 16, true));
				set.add(chunk.getProvider().getChunk(chunkX, chunkZ + 16, true));
				set.add(chunk.getProvider().getChunk(chunkX - 16, chunkZ, true));
				break;
			case "1:0":
				set.add(chunk);
				set.add(chunk.getProvider().getChunk(chunkX - 16, chunkZ + 16, true));
				set.add(chunk.getProvider().getChunk(chunkX, chunkZ + 16, true));
				set.add(chunk.getProvider().getChunk(chunkX - 16, chunkZ, true));
				break;
		}

		if (set.size() == 4) {
			generateChunk(chunks.remove(id));
		} else {
			chunks.put(id, set);
		}
	}

	private void generateChunk(Set<FullChunk> chunks){
		int generationRangeId = 0;

		int x = 0;
		int z = 0;
		BigChunk chunk = new BigChunk(chunks);
		while (generationRangeId++ < size) {
			Block block = getBlock(x++, z++);
			chunk.setBlock(block.getFloorX(), block.getFloorY(), block.getFloorZ(), block.getId(), block.getDamage());
		}

		x = z = 0;
		for (int i = 0; i < 15; i++) {
			chunk.setBlock(x++, 0, z, this.lastBlock.getId(), this.lastBlock.getDamage());
		}
	}

	private boolean range(int val, int min, int max) {
		return val >= min && val <= max;
	}

	private Block getBlock(int x, int z) {
		Block block = getBlockNoPosition(x, z);
		block.setComponents(x, this.groundHeight, z);
		return block;
	}

	private Block getBlockNoPosition(int x, int z) {
		/*
		- 过道, = 边框, * 地表

		   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5
		 0 - - - - - - - - - - - - - - - -
		 1 - = = = = = = = = = = = = = = -
		 2 - = * * * * * * * * * * * * = -
		 3 - = * * * * * * * * * * * * = -
		 4 - = * * * * * * * * * * * * = -
		 5 - = * * * * * * * * * * * * = -
		 6 - = * * * * * * * * * * * * = -
		 7 - = * * * * * * * * * * * * = -
		 8 - = * * * * * * * * * * * * = -
		 9 - = * * * * * * * * * * * * = -
		 0 - = * * * * * * * * * * * * = -
		 1 - = * * * * * * * * * * * * = -
		 2 - = * * * * * * * * * * * * = -
		 3 - = * * * * * * * * * * * * = -
		 4 - = = = = = = = = = = = = = = -
		 5 - - - - - - - - - - - - - - - -

		*/

		int now = 0;

		if (range(x, now, now += this.aisleBlockWidth)) {
			return this.aisleBlock;
		}

		if (range(x, now, now += this.frameBlockWidth)) {
			return this.frameBlock;
		}

		if (range(x, now, now += this.groundWidth)) {
			return this.groundBlock;
		}

		if (range(x, now, now += this.frameBlockWidth)) {
			return this.frameBlock;
		}

		if (range(x, now, this.aisleBlockWidth)) {
			return this.aisleBlock;
		}

		return AIR;
	}


	@Override
	public void populateChunk(int chunkX, int chunkZ) {
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
