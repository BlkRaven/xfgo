# xfgo

捐赠我以支持我继续开发维护 [捐赠地址](https://github.com/locbytes/donation)

**特别说明：本软件完全免费发布，遵循自订开源协议开源，接受自愿捐赠，没有任何分销商，如果遇到销售本软件副本的情况请及时向我举报。**

严禁倒卖：[https://github.com/locbytes/FGO_AnyProxy/blob/master/严禁倒卖.md](https://github.com/locbytes/FGO_AnyProxy/blob/master/严禁倒卖.md)

Fate/Grand Order b服过sign检测xposed模块 v1.5

当前游戏客户端版本: 1.21.2

如有任何问题请提交issue，我会及时进行回复。

核心代码 [Main.java](https://github.com/locbytes/xfgo/blob/master/app/src/main/java/com/locbytes/xfgo/Main.java)

## 关于渠道服

从v1.5版开始适配了渠道服的包名通用部分，但是无法保证适配了全部的渠道服，如果使用渠道服出现异常请提交issue进行说明。

## 说明

**本模块工作原理为修改验证函数返回值或传入参数。**

**B服FGO经测试无法在VirtualXposed中运行，并非本模块造成的问题。**

Android版本需求: >=4.4.2

Xposed API需求: >=54

经测试可以通过[Magisk](https://forum.xda-developers.com/apps/magisk)以框中框的方式来使用本模块。

v1.2版因加入了撤退胜利的数据处理逻辑，导致需要使用JSONArray类，要求Android SDK最低为API-19，故将min SDK更改为API-19。

Android版本低于4.4.2的用户只能使用v1.1版的模块，即只有基础的过sign验证功能，调整参数需要改服务端代码。

v1.1版：[https://github.com/locbytes/xfgo/releases/tag/v1.1](https://github.com/locbytes/xfgo/releases/tag/v1.1)

## 使用方法

参考xfgo_anyproxy整合包使用教程。

[整合包使用教程](https://github.com/locbytes/FGO_AnyProxy/blob/master/Course/Course.md)

## 模块核心功能适配方法

### 修改战斗数据过sign验证

科技作者需在科技服务端将`response`的`sign`改为`""`。

```json
{
    "response": {},
    "cache": {},
    "sign": ""
}
```

### 撤退胜利

自v1.3版加入了通过请求服务器生成随机数作为战斗回合数`elapsedTurn`的功能，请求地址通过xfgo用户界面进行设置。

科技作者需在科技服务端对request进行修改，修改方法、具体示例代码请先参考模块和科技服务端的示例代码。

```json
{
    "battleResult": 1,
    "elapsedTurn": 3,
    "aliveUniqueIds": []
}
```

重新建立`requestData`，无需对sign进行处理。

## 科技服务端代码参考

自用的话推荐使用[AnyProxy版的一键整合包](https://github.com/locbytes/FGO_AnyProxy/releases)，更新版本可以直接替换文件，免去下载安装配置Fiddler的苦恼。

AnyProxy版(v1.4, 推荐): [https://github.com/locbytes/FGO_AnyProxy](https://github.com/locbytes/FGO_AnyProxy)

ModifyFGO(使用Fiddler, v1.3): [https://github.com/heqyoufree/modifyfgo](https://github.com/heqyoufree/modifyfgo)

Fiddler版(v1.1, 暂停更新): [https://github.com/locbytes/FGO_FiddlerScript](https://github.com/locbytes/FGO_FiddlerScript)
