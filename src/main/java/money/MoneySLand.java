package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.*;
import money.event.MoneySLandBuyEvent;
import money.event.MoneySLandOwnerChangeEvent;
import money.event.MoneySLandPriceCalculateEvent;
import money.generator.SLandGenerator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class MoneySLand extends PluginBase implements MoneySLandAPI {
    private static MoneySLand instance;
    private SLandPool lands;

    private SLandPool modifiedLands;

    private int id;

    private ConfigSection language;

    {
        instance = this;
    }

    private TaskHandler savingTask;


    public static MoneySLand getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        //当地形生成器已注册时, 方法返回 false. 因此无需考虑 reload
        Generator.addGenerator(SLandGenerator.class, "land", Generator.TYPE_INFINITE);
        Generator.addGenerator(SLandGenerator.class, "sland", Generator.TYPE_INFINITE);
        Generator.addGenerator(SLandGenerator.class, "地皮", Generator.TYPE_INFINITE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {
        getDataFolder().mkdir();

        lands = new SLandPool();
        modifiedLands = new SLandPool();

        reloadConfig();
        getConfig().getSections().values().forEach((o) -> lands.add((ConfigSection) o));

        getServer().getPluginManager().registerEvents(new SLandEventListener(this), this);
        savingTask = Server.getInstance().getScheduler().scheduleRepeatingTask(this, this::save, 20 * 60);



        saveResource("language/chs.properties", "language.properties", false);

        Config config = new Config(getDataFolder() + File.separator + "language.properties");
        this.language = config.getRootSection();

        try {
            String file = cn.nukkit.utils.Utils.readFile(getResource("language/chs.properties"));
            int size = this.language.size();
            new Config(file, Config.PROPERTIES).getAll().forEach((key, value) -> {
                if (!this.language.containsKey(key)) {
                    this.language.set(key, value);
                }
            });
            if (this.language.size() != size) {
                config.setAll(this.language);
                config.save();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        for (SLand land : modifiedLands) {
            getConfig().set(String.valueOf(land.getTime()), land.save());
        }
        modifiedLands.clear();
    }

    @Override
    public void onDisable() {
        save();
        savingTask.cancel();
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
        for (SLand land : lands) {
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
     * @return all of the lands that {@code player} had
     */
    public SLand[] getLands(String player) {
        List<SLand> list = new ArrayList<>();
        for (SLand land : lands) {
            if (land.getOwner().equalsIgnoreCase(player)) {
                list.add(land);
            }
        }
        return (SLand[]) list.toArray();
        // TODO: 2017/4/2  test it
    }

    public boolean buyLand(SLand land, Player player) {
        float price = calculatePrice(player, land);
        if (getMoney(player) > price) {

            MoneySLandBuyEvent event = new MoneySLandBuyEvent(land, player, price);
            Server.getInstance().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }

            MoneySLandOwnerChangeEvent ev = new MoneySLandOwnerChangeEvent(land, land.getOwner(), player.getName(), MoneySLandOwnerChangeEvent.CAUSE_BUY);
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

    float getMoney(Player player) {
        return Money.getInstance().getMoney(player);
    }


    public float calculatePrice(Player player, SLand land) {
        float price = (float) (this.getConfig().getDouble("pricePerSquare", 0) * land.getX().getLength() * land.getZ().getLength());

        MoneySLandPriceCalculateEvent event = new MoneySLandPriceCalculateEvent(land, player, price);
        Server.getInstance().getPluginManager().callEvent(event);
        return event.getPrice();
    }
}
