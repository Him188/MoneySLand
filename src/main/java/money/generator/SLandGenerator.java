package money.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import money.MoneySLand;
import money.SLand;
import money.range.BlockPlacer;
import money.range.FrameBlockPlacer;
import money.range.Range;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SLand 世界生成器
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class SLandGenerator extends Generator {
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

    // TODO: 2017/4/2 支持自定义购买领地的方块
    //private static final Block DEFAULT_SHOP_BLOCK = new SLandShopBlock();

    protected int totalWidth;

    protected ChunkManager level;

    private Block shopBlock;

    //填充地下的方块
    protected Block fillBlock;

    //最底层方块
    protected Block lastBlock;

    //最底层方块
    protected Block topBlock;

    //底部基岩填充
    protected BlockPlacer lastBlockCreate;

    //边框顶部方块填充
    protected BlockPlacer frameTopBlock;
    //过道宽度
    protected BlockPlacer aisleBlockLeft;
    //过道宽度
    protected BlockPlacer aisleBlockRight;

    //过道方块
    protected Block aisleBlock;

    //每块地边框宽度
    protected BlockPlacer frameBlockLeft;

    //顶层边框方块
    protected BlockPlacer frameBlockTopLeft;
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

        this.totalWidth = Integer.parseInt(options.getOrDefault("totalWidth", 20).toString());

        this.groundHeight = toInt(options.getOrDefault("groundHeight", 48));

        this.fillBlock = getBlock(options, "fillBlock", DEFAULT_FILL_BLOCK);
        this.lastBlock = getBlock(options, "lastBlock", DEFAULT_LAST_BLOCK);
        this.aisleBlock = getBlock(options, "aisleBlock", DEFAULT_AISLE_BLOCK);
        this.frameBlock = getBlock(options, "frameBlock", DEFAULT_FRAME_BLOCK);
        this.groundBlock = getBlock(options, "groundBlock", DEFAULT_GROUND_BLOCK);
        this.topBlock = getBlock(options, "frameTopBlock", DEFAULT_FRAME_TOP_BLOCK);

        this.aisleBlockLeft = new BlockPlacer(
                this.aisleBlock,
                0,
                toInt(options.getOrDefault("aisleBlockWidth", DEFAULT_AISLE_WIDTH)));

        this.lastBlockCreate = new BlockPlacer(
                this.lastBlock,
                0,
                toInt(options.getOrDefault("lastBlock", DEFAULT_LAST_BLOCK)));

        this.frameTopBlock = new BlockPlacer(
                this.topBlock,
                0,
                toInt(options.getOrDefault("frameTopBlock", DEFAULT_FRAME_TOP_BLOCK)));
        this.frameBlockLeft = new FrameBlockPlacer(
                this.frameBlock,
                this.aisleBlockLeft.getMax(),
                this.aisleBlockLeft.getMax()
                        + toInt(options.getOrDefault("frameBlockWidth", DEFAULT_FRAME_WIDTH)));

        this.groundWidth = new BlockPlacer(
                this.groundBlock,
                this.frameBlockLeft.getMax(),
                this.totalWidth - this.aisleBlockLeft.getLength() - this.frameBlockLeft.getLength());

        this.frameBlockRight = new BlockPlacer(this.frameBlock,
                this.groundWidth.getMax(),
                this.groundWidth.getMax()
                        + toInt(options.getOrDefault("frameBlockWidth", DEFAULT_FRAME_WIDTH)));

        this.aisleBlockRight = new BlockPlacer(this.aisleBlock,
                this.frameBlockRight.getMax(),
                this.frameBlockRight.getMax()
                        + toInt(options.getOrDefault("aisleBlockWidth", DEFAULT_AISLE_WIDTH)));

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

    private int toInt(Object o) {
        try {
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    private Block getBlock(Map<String, Object> map, String name, Block defaultBlock) {
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
        this.options = options;
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

        int x, z, y;
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

                        if (this.frameBlockLeft.inRange(x) && this.frameBlockLeft.inRange(z)) {
                            //领地方块
                            chunk.setBlock(_x, this.groundHeight + 2, _z, Block.NETHERRACK);

                            int minX, minZ;
                            SLand land = new SLand(
                                    new Range(minX = 1 + realChunkX + _x,
                                            minX + this.groundWidth.getLength()),
                                    new Range(minZ = 1 + realChunkZ + _z,
                                            minZ + this.groundWidth.getLength())
                            );
                            land.reload(new HashMap<>()); //save default data
                            land.setShopBlock(new Vector3(_x + realChunkX, this.groundHeight + 2, _z + realChunkZ));

                            MoneySLand.getInstance().getLandPool().add(land);
                            MoneySLand.getInstance().getModifiedLandPool().add(land);
                        }

                        this.frameBlockLeft.placeBlock(chunk, _x, _y, _z);
                        this.frameTopBlock.placeBlock(chunk, _x, this.groundHeight + 1, _z);
                    } else {
                        this.groundWidth.placeBlock(chunk, _x, _y + 1, _z);
                    }
                    this.lastBlockCreate.placeBlock(chunk, _x, 0, _z);
                }
            }
        }

	/*for (x = 0; x < 16; x++) {
			for (z = 0; z < 16; z++) {
				for (int y = 0; y < this.groundHeight; y++) {
					//noinspection StatementWithEmptyBody
					if (this.frameBlockLeft.inRange(x) || this.frameBlockLeft.inRange(z) ||
							this.frameBlockRight.inRange(x) || this.frameBlockRight.inRange(z)) {

						// TODO: 2017/4/2 边框设置

						/*
						for (int i = 0; i < 256; i++) {
							chunk.setBlockId(x, i, z, Block.DIAMOND_BLOCK);
						}

						continue;
						*/
				/*	}

					chunk.setBlockId(x, y, z, this.fillBlock.getId());
					chunk.setBlockData(x, y, z, this.fillBlock.getDamage());
				}
			}
		}*/
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

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
