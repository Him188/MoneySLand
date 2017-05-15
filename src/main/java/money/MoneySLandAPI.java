package money;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * API
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public interface MoneySLandAPI {
    /**
     * 获取地皮池 (所有地皮)<br>
     * Gets all lands
     *
     * @return 所有地皮 <br>all lands
     */
    SLandPool getLandPool();

    /**
     * 获取修改过的地皮池 (修改过的所有地皮)<br>
     * Gets all lands which is modified
     * <p>
     * 若 {@code config.yml} 中 {@code saving-ticks} 项值为 0, 本方法将恒返回一个地皮数量为 0 的 {@link SLandPool}<br>
     * If the value of section {@code saving-ticks} in {@code config.yml} is 0,
     * this method will always return a {@link SLandPool} which has 0 land.
     * <p>
     * 当 {@link MoneySLand#save()} 被调用后, 修改过的地皮池都将被清空 <br>
     * When {@link MoneySLand#save()} is called, modified lands will be clean,
     * and this method will return a {@link SLandPool} which has 0 land.
     *
     * @return 修改过的所有地皮 <br>all lands which is modified
     */
    SLandPool getModifiedLandPool();

    /**
     * 获取包含这个坐标的地皮 <br>
     * Gets the land which includes {@code position}
     *
     * @param position the position
     * @return the land which includes {@code position}
     */
    SLand getLand(Position position);

    /**
     * 获取这个玩家拥有的所有地皮 <br>
     * Gets all lands which the player owned
     *
     * @param player 玩家名 <br>player's name
     * @return 这个玩家拥有的所有地皮 <br>all lands which the player owned
     */
    SLand[] getLands(String player);

    /**
     * 购买地皮 <br>
     * Buy a land
     *
     * @param land   地皮 <br>the land
     * @param player 玩家名 <br>player's name
     * @return 是否成功 <br>if success
     */
    boolean buyLand(SLand land, Player player);
}

