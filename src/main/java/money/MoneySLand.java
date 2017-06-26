package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import money.command.GenerateLandCommand;
import money.event.MoneySLandBuyEvent;
import money.event.MoneySLandOwnerChangeEvent;
import money.event.MoneySLandPriceCalculateEvent;
import money.generator.SLandGenerator;
import money.sland.SLand;
import money.sland.SLandPool;
import money.utils.SLandUtils;

import java.io.*;
import java.util.*;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class MoneySLand extends PluginBase implements MoneySLandAPI {
	private static MoneySLand instance;

	{
		instance = this;
	}

	public static MoneySLand getInstance() {
		return instance;
	}


	private SLandPool lands;
	private SLandPool modifiedLands;
	private Config landConfig;

	private int id;

	private ConfigSection language;
	private MoneySLandEventListener eventListener;

	private TaskHandler savingTask;


	@Override
	public void onLoad() {
		//当地形生成器已注册时, 方法返回 false
		//服务器重启不会清空地形生成器

		reloadGeneratorDefaultSettings();
		for (String name : SLandGenerator.GENERATOR_NAMES) {
			Generator.addGenerator(SLandGenerator.class, name, Generator.TYPE_INFINITE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		getDataFolder().mkdir();
		new File(getDataFolder() + File.separator + "generator_settings").mkdir();

		lands = new SLandPool();
		modifiedLands = new SLandPool();

		landConfig = new Config(getDataFolder() + File.separator + "lands.dat", Config.JSON);
		landConfig.getSections().values().forEach((o) -> lands.add((ConfigSection) o));

		initConfigSettings();

		try {
			initLanguageSettings(getConfig().get("language", "chs"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().critical("无法读取语言文件!! 请删除语言文件以恢复初始或修复其中的问题");
			getLogger().critical("Could not load language file!! Please delete language file or fix bugs in it");
		}

		String command = getConfig().getString("generator-command", null);
		if (command != null && !command.isEmpty()) { //for disable command
			Server.getInstance().getCommandMap().register(command, new GenerateLandCommand(command, this));
		}

		if (eventListener == null) { //for reload
			eventListener = new MoneySLandEventListener(this);
			getServer().getPluginManager().registerEvents(eventListener, this);
		}

		savingTask = Server.getInstance().getScheduler().scheduleRepeatingTask(this, this::save, 20 * 60);
	}

	private void reloadGeneratorDefaultSettings() {
		saveResource("generator_default.properties");
		SLandGenerator.setDefaultSettings(SLandUtils.loadProperties(getDataFolder() + File.separator + "generator_default.properties"));
	}

	public Map<String, Object> loadGeneratorSettings(String name) {
		File file;

		file = new File(getDataFolder() + File.separator + "generator_settings" + File.separator + name);
		if (!file.exists()) {
			file = new File(getDataFolder() + File.separator + "generator_settings" + File.separator + name + ".properties");
			if (!file.exists()) {
				file = new File(getDataFolder() + File.separator + "generator_settings" + File.separator + name + ".prop");
				if (!file.exists()) {
					file = new File(getDataFolder() + File.separator + "generator_settings" + File.separator + name + ".property");
					if (!file.exists()) {
						return new HashMap<>();
					}
				}
			}
		}

		return SLandUtils.loadProperties(file);
	}

	private void initConfigSettings() {
		saveDefaultConfig();
		reloadConfig();

		int size = getConfig().getAll().size();
		new Config(Config.YAML) {
			{
				load(getResource("config.yml"));
			}
		}.getAll().forEach((key, value) -> {
			if (!getConfig().exists(key)) {
				getConfig().set(key, value);
			}
		});
		if (getConfig().getAll().size() != size) {
			getConfig().save();
		}
	}

	private void initLanguageSettings(String language) throws IOException {
		saveResource("language/" + language + "/language.properties", "language.properties", false);

		Properties properties = new Properties();
		File file = new File(getDataFolder() + File.separator + "language.properties");
		properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		this.language = new ConfigSection(new LinkedHashMap<String, Object>() {
			{
				properties.forEach((key, value) -> put(key.toString(), value));
			}
		});

		int size = this.language.size();
		SLandUtils.loadProperties(getResource("language/" + language + "/language.properties")).forEach((key, value) -> {
			if (!this.language.containsKey(key)) {
				this.language.set(key, value);
			}
		});

		if (this.language.size() != size) {
			properties.putAll(this.language);

			properties.store(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), "MoneySLand language config");
		}
	}

	private void save() {
		for (SLand land : modifiedLands.values()) {
			landConfig.set(String.valueOf(land.getId()), land.save());
		}
		landConfig.save();
		modifiedLands.clear();
	}

	@Override
	public void onDisable() {
		save();

		if (savingTask != null) {
			savingTask.cancel();
			savingTask = null;
		}
	}

	public String translateMessage(String message) {
		if (language.get(message) == null) {
			return TextFormat.colorize(message);
		}

		return TextFormat.colorize(language.get(message).toString());
	}

	public String translateMessage(String message, Map<String, Object> args) {
		if (language.get(message) == null) {
			return message;
		}

		final String[] msg = {translateMessage(message)};
		args.forEach((key, value) -> {
			if (value instanceof Double || value instanceof Float) {
				msg[0] = msg[0].replace("$" + key + "$", String.valueOf(Math.round(Double.parseDouble(value.toString()))));
			} else {
				msg[0] = msg[0].replace("$" + key + "$", value.toString());
			}
		});
		return msg[0];
	}

	public String translateMessage(String message, Object... keys_values) {
		Map<String, Object> map = new HashMap<>();

		String key = null;
		for (Object o : keys_values) {
			if (key == null) {
				key = o.toString();
			} else {
				map.put(key, o);
				key = null;
			}
		}

		return translateMessage(message, map);
	}


	public SLandPool getLandPool() {
		return lands;
	}

	public SLandPool getModifiedLandPool() {
		return modifiedLands;
	}

	public SLand getLand(Position position) {
		for (SLand land : lands.values()) {
			if (land.inRange(position) || land.getShopBlock().equals(position)) {
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
	public SLand[] getLands(String player) {
		List<SLand> list = new ArrayList<>();
		for (SLand land : lands.values()) {
			if (land.getOwner().equalsIgnoreCase(player)) {
				list.add(land);
			}
		}
		return list.toArray(new SLand[list.size()]);
	}

	public boolean buyLand(SLand land, Player player) {
		float price = calculatePrice(player, land);
		if (Money.getInstance().getMoney(player) > price) {

			MoneySLandBuyEvent event = new MoneySLandBuyEvent(land, player, price);
			Server.getInstance().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return false;
			}

			MoneySLandOwnerChangeEvent ev = new MoneySLandOwnerChangeEvent(land, land.getOwner(), player.getName(), MoneySLandOwnerChangeEvent.Cause.BUY);
			Server.getInstance().getPluginManager().callEvent(ev);
			if (event.isCancelled()) {
				return false;
			}

			if (!land.setOwner(player.getName())) {
				return false;
			}

			if (!Money.getInstance().reduceMoney(player, event.getPrice())) {
				return false;
			}

			Server.getInstance().getLevelByName(land.getLevel()).setBlock(land.getShopBlock(), Block.get(Block.AIR));
			return true;
		} else {
			return false;
		}
	}

	public float calculatePrice(Player player, SLand land) {
		float price = (float) this.getConfig().getDouble("pricePerSquare", 0) * land.getSquare();

		MoneySLandPriceCalculateEvent event = new MoneySLandPriceCalculateEvent(land, player, price);
		Server.getInstance().getPluginManager().callEvent(event);
		return event.getPrice();
	}
}
