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
  1. 玩家使用 `/gotoland [id]` 传送到自己已拥有的一个地皮

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

### 权限

你可以通过修改玩家拥有的权限，来实现自定义特权等特性


|                 权限名                 |            用处             |    默认拥有者    |
|:-------------------------------------:|:--------------------------:|:---------------:|
|   money.command.sland.generateland    |         创建地皮世界         | 管理员(包括控制台) |
|     money.command.sland.gotoland      |      传送到自己拥有的地皮      |     所有玩家     |
|     money.command.sland.idleland      |      传送到未被购买的地皮      |     所有玩家     |
|  money.permission.sland.modify.touch  |     触摸自己没有权限的地皮     |      管理员      |
|  money.permission.sland.modify.place  | 在自己没有权限的地皮上放置方块  |      管理员      |
|  money.permission.sland.modify.break  | 在自己没有权限的地皮上拆除方块  |      管理员      |
| money.permission.sland.{LandId}.touch |    触摸Id为 LandId 的地皮    |       无        |
| money.permission.sland.{LandId}.place | 在Id为 LandId 的地皮上放置方块 |       无        |
| money.permission.sland.{LandId}.break | 在Id为 LandId 的地皮上拆除方块 |       无        |

--------

### 开源

[GitHub(MamoeTech/MoneySLand)](https://github.com/MamoeTech/MoneySLand)

### 作者

[**MamoeTech**](https://github.com/MamoeTech/)
- [Him188](https://github.com/Him188)

### 鸣谢

感谢 SoleMemory(SayHellos) ([GitHub](https://github.com/SayHellos) /
[ZXDA](https://pl.zxda.net/user/11495.html)) 在开发过程中给予帮助
