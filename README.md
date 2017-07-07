# MoneySLand

### 简介

这是运行在 Nukkit 服务器上的一款地皮插件

你可以用它创建一个由插件自动划分区域的世界，供玩家自由购买(或是通过权限组创造出更实用的用法)

--------

### 用法

- **安装插件**
  1. 安装 Money 插件，本插件依赖于该插件的经济系统 (本插件更新时仅适配 Money
     最新版，请尽量保持 Money 为最新版)
  2. 将插件放入服务器 plugins 目录中，并重启服务器(不能通过 `/reload` 指令)

- _**创建地皮世界**_
  1. 使用 `/newland <世界名> [世界配置名]`
     创建一个新的地皮世界。其中，“世界配置名”可以在下文中寻找说明

- **玩家购买地皮**
  1. 玩家在地皮世界中使用 `/idleland` 或直接使用 `/idleland [世界名]`
     传送到未购买的地皮
  2. 玩家空手点击地皮边缘的下节反应核，并确认购买

- **玩家传送地皮**
  1. 玩家使用 `/gotoland [id]` 传送到地皮

### 世界配置

本插件支持自定义每个地皮世界的配置，如生成的方块ID，购买地皮的方块ID，地皮宽度等。

- **创建配置**
  1. 打开 “plugins/MoneySLand” 目录，找到 “generator_default.properties”
     文件
  2. 将该文件复制到 “generator_settings” 中
  3. 修改复制的文件的名字，修改后的名字就是创建地皮指令中所需要的 “世界配置名”
  4. 修改设置，并使用UTF8编码保存
  5. 不需要重启服务器，你现在就可以使用这个配置创建地皮世界了

- **修改配置**  
  暂不支持修改已创建的地皮世界的配置

--------

### 指令

所有指令均可在 "config.yml" 中修改  
此表中 "权限" 不包含父权限.

|    指令名     | 参数                                             |                  说明                   |                                权限                                 |
|:------------:|:------------------------------------------------|:--------------------------------------:|:------------------------------------------------------------------:|
| generateland | <name> \[settings_filename\]                    |               创建地皮世界               |                  money.command.sland.generateland                  |
|   gotoland   | <id>                                            |            传送到任意ID的地皮             |                    money.command.sland.gotoland                    |
|   idleland   | \[level\]                                       |          传送到一个未被购买的地皮          |                    money.command.sland.idleland                    |
|    landid    |                                                 |           查看所在位置的地皮的ID           |                     money.command.sland.landid                     |
|   sellland   | \[id\]                                          |        出售所在的地皮或指定ID的地皮         |  money.command.sland.sellland;<br />money.command.sland.sellland.others  |
|  clearland   | \[id\]                                          | 将所在的地皮或指定ID的地皮清空<br />并还原到初始状态 | money.command.sland.clearland;<br />money.command.sland.clearland.others |
|    myland    | \[player\]                                      |       查看自己或他人已拥有的地皮列表        |    money.command.sland.myland;<br />money.command.sland.myland.others    |
| landinvitee  | list \[地皮ID\] <br />或 <add\|remove> <地皮ID> <玩家名> |         管理自己或他人的地皮分享者          |                         过多, 请在附表中查看                          |

附表:

|                landinvitee 权限                |
|:---------------------------------------------:|
|              money.command.sland              |
|        money.command.sland.landinvitee        |
|      money.command.sland.landinvitee.lis      |
|  money.command.sland.landinvitee.list.others  |
|      money.command.sland.landinvitee.add      |
|  money.command.sland.landinvitee.add.others   |
|    money.command.sland.landinvitee.remove     |
| money.command.sland.landinvitee.remove.others |

--------

### 权限

你可以通过修改玩家拥有的权限，来实现自定义特权等特性

**指令权限**


|                 权限名                  |               说明               |    默认拥有者     |
|:--------------------------------------:|:-------------------------------:|:---------------:|
|          money.command.sland           |       所有指令的权限的父权限        | 管理员(包括控制台) |
|    money.command.sland.generateland    |           创建地皮世界            | 管理员(包括控制台) |
|      money.command.sland.gotoland      |         传送到任意ID的地皮         |     所有玩家     |
|      money.command.sland.idleland      |        传送到未被购买的地皮         |     所有玩家     |
|       money.command.sland.lanid        |        查看所在位置的地皮ID        |     所有玩家     |
|      money.command.sland.sellland      |     出售所在的地皮或指定ID的地皮     |     所有玩家     |
|  money.command.sland.sellland.others   |          出售任意ID的地皮          | 管理员(包括控制台) |
|     money.command.sland.clearland      | 将自己已拥有的地皮清空并还原到初始状态 |     所有玩家     |
|  money.command.sland.clearland.others  |  将任意ID的地皮清空并还原到初始状态   | 管理员(包括控制台) |
|       money.command.sland.myland       |      查看自己已拥有的地皮列表       |     所有玩家     |
|   money.command.sland.myland.others    |     查看任意一名玩家已拥有的地皮     | 管理员(包括控制台) |
|    money.command.sland.landinvitee     |         管理自己地皮分享者         |     所有玩家     |
| money.command.sland.landinvitee.others |        管理他人的地皮分享者         | 管理员(包括控制台) |

**操作权限**

|                      权限名                      |                 说明                  |    默认拥有者     |
|:-----------------------------------------------:|:------------------------------------:|:----------------:|
|             money.permission.sland              |           所有操作权限的父权限           | 管理员(包括控制台) |
|          money.permission.sland.modify          |           modify.* 的父权限           | 管理员(包括控制台) |
|     money.permission.sland.modify.interact      |      在自己没有权限的地皮上点击方块       |      管理员       |
| money.permission.sland.modify.interact.{LandId} |      在ID为{LandId}的地皮上点击方块      |      管理员       |
|       money.permission.sland.modify.place       |      在自己没有权限的地皮上放置方块       |      管理员       |
|  money.permission.sland.modify.place.{LandId}   |      在ID为{LandId}的地皮上放置方块      |      管理员       |
|       money.permission.sland.modify.break       |      在自己没有权限的地皮上拆除方块       |      管理员       |
|  money.permission.sland.modify.break.{LandId}   |      在ID为{LandId}的地皮上拆除方块      |      管理员       |
|        money.permission.sland.breakshop         |         breakshop.* 的父权限          |        无        |
|    money.permission.sland.breakshop.{Level}     |   破坏名字为{Level}的地皮世界的购买方块   |        无        |
|      money.permission.sland.interactaisle       |       interactaisle.* 的父权限        |        无        |
|  money.permission.sland.interactaisle.{Level}   | 点击或破坏名字为{Level}的地皮世界的过道方块 |        无        |
|        money.permission.sland.berakframe        |         berakframe.* 的父权限         |        无        |
|    money.permission.sland.berakframe.{Level}    |   破坏名字为{Level}的地皮世界的边框方块   |        无        |
|           money.permission.sland.buy            |          在所有地皮世界购买地皮          |     所有玩家      |
|       money.permission.sland.buy.{LandId}       |           在某地皮世界购买地皮           |        无        |

--------

### 开源

[GitHub(MamoeTech/MoneySLand)](https://github.com/MamoeTech/MoneySLand)

### 作者

[**MamoeTech**](https://github.com/MamoeTech/)
- [Him188](https://github.com/Him188)

### 鸣谢

感谢 SoleMemory(SayHellos) ([GitHub](https://github.com/SayHellos) /
[ZXDA](https://pl.zxda.net/user/11495.html)) 在开发过程中给予帮助
