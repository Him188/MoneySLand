package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import money.event.SLandOwnerChangeEvent;
import money.generator.SLandGenerator;
import money.sland.LandPool;
import money.sland.SLand;

import java.util.*;

public final class MoneySLand extends PluginBase {
	private static MoneySLand instance;
	private LandPool lands;

	private LandPool modifiedLands;

	private int id;

	public static MoneySLand getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		instance = this;
		calculators = new HashSet<>();

		Generator.addGenerator(SLandGenerator.class, "land", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "sland", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "地皮", Generator.TYPE_INFINITE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		getDataFolder().mkdir();
		reloadConfig();
		calculators.clear();

		lands = new LandPool();
		modifiedLands = new LandPool();

		getConfig().getAll().values().forEach((o) -> lands.add((Map<String, Object>) o));

		Server.getInstance().getScheduler().scheduleRepeatingTask(this, () -> {
			for (SLand land : modifiedLands) {
				Map<String, Object> data = land.save();
				getConfig().set(data.get("time").toString(), data);
			}
			modifiedLands.clear();
		}, 20 * 60 * 2);
	}


	public LandPool getLandPool() {
		return lands;
	}

	public LandPool getModifiedLandPool() {
		return modifiedLands;
	}

	public SLand getLand(Position position) {
		for (SLand land : lands) {
			if (land.inRange(position)) {
				return land;
			}
		}
		return null;
	}

	/**
	 * Gets all of the lands that {@code player} had
	 *
	 * @param player player's name
	 *
	 * @return all of the lands that {@code player} had
	 */
	// TODO: 2017/4/2  test it
	public SLand[] getLands(String player) {
		List<SLand> list = new ArrayList<>();
		for (SLand land : lands) {
			if (land.getOwner().equalsIgnoreCase(player)) {
				list.add(land);
			}
		}
		return (SLand[]) list.toArray();
	}

	public boolean buyLand(SLand land, Player player) {
		double price = calculatePrice(player, land);
		if (getMoney(player) > price) {
			SLandOwnerChangeEvent event = new SLandOwnerChangeEvent(land, land.getOwner(), player.getName(), SLandOwnerChangeEvent.CAUSE_BUY);
			Server.getInstance().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return false;
			}

			reduceMoney(player, price);
			land.setOwner(player.getName());
			Server.getInstance().getLevelByName(land.getLevel()).setBlock(land.getShopBlock(), Block.get(Block.AIR));
			return true;
		} else {
			return false;
		}
	}


	void reduceMoney(Player player, double amount) {
		Money.getInstance().addMoney(player, -amount);
	}

	double getMoney(Player player) {
		return Money.getInstance().getMoney(player);
	}


	private Set<PriceCalculator> calculators;

	/**
	 * Adds calculator which can change the price of one land
	 */
	public void addPriceCalculator(PriceCalculator calculator) {
		calculators.add(calculator);
	}

	public double calculatePrice(Player player, SLand land) {
		double price = this.getConfig().getInt("pricePerSquare", 0) * land.getX().getLength() * land.getZ().getLength();
		for (PriceCalculator calculator : calculators) {
			price = calculator.calculate(price, player, land);
		}
		return price;
	}
}
