# xfgo

捐赠我以支持我继续开发维护 [捐赠地址](https://github.com/locbytes/donation)

**特别说明：本软件完全免费发布，遵循自订开源协议开源，接受自愿捐赠，没有任何分销商，如果遇到销售本软件副本的情况请及时向我举报。**

Fate/Grand Order b服过sign检测xposed模块 v1.4

当前游戏客户端版本: 1.21.2

如有任何问题请提交issue，我会及时进行回复。

核心代码 [Main.java](https://github.com/locbytes/xfgo/blob/master/app/src/main/java/com/locbytes/xfgo/Main.java)

## 关于渠道服

渠道服可以提交issue申请发布对应的release。

目前已上传baidu渠道服的v1.1版，请前往release查看。

## 说明

**本模块工作原理为修改验证函数返回值或传入参数。**

**B服FGO经测试无法在VirtualXposed中运行，并非本模块造成的问题。**

Android版本需求: >=4.4.2

Xposed API需求: >=54

经测试可以通过[Magisk](https://forum.xda-developers.com/apps/magisk)以框中框的方式来使用本模块。

v1.2版因加入了撤退胜利的数据处理逻辑，导致需要使用JSONArray类，要求Android SDK最低为API-19，故将min SDK更改为API-19。

Android版本低于4.4.2的用户只能使用v1.1版的模块，即只有基础的过sign验证功能，调整参数需要改服务端代码。

v1.1版：[https://github.com/locbytes/xfgo/releases/tag/v1.1](https://github.com/locbytes/xfgo/releases/tag/v1.1)

**想要使用撤退胜利功能的用户请等待自己科技的作者适配完成。**

## 使用方法

当前已更新至v1.4版，从此版开始使用okhttp3。

安装xfgo模块，在xp框架中开启模块并重启手机。

打开xfgo，给予联网权限和文件读写权限，联网是向科技服务端上传科技配置文件，文件读写权限是在本地记录你的用户配置。

确保服务端已经运行的情况下，设置WiFi代理，在xfgo中设置相关功能，应用后服务端会提示配置文件已更新。

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

v1.3版加入了通过请求服务器生成随机数作为战斗回合数`elapsedTurn`的功能，请求地址通过xfgo用户界面进行设置。

科技作者需在科技服务端对request进行修改，修改方法，具体示例代码请先参考模块中的代码。

先监测包含`key=battleresult`的`requestData`，对该`requestData`进行处理。

拆分数据，获得`result`的value值，该值为一个json字符串。

对该json数据进行处理，有如下数据需要修改，推荐参考AnyProxy版服务器代码。

```json
{
    "battleResult": 1,
    "elapsedTurn": 11,
    "aliveUniqueIds": []
}
```

重新建立`requestData`，无需对sign进行处理。

## 科技服务端代码参考

自用的话推荐使用[AnyProxy版的一键整合包](https://github.com/locbytes/FGO_AnyProxy/releases)，更新版本可以直接替换文件，免去下载安装配置Fiddler的苦恼。

AnyProxy版(v1.4, 推荐): [https://github.com/locbytes/FGO_AnyProxy](https://github.com/locbytes/FGO_AnyProxy)

ModifyFGO(使用Fiddler, v1.3): [https://github.com/heqyoufree/modifyfgo](https://github.com/heqyoufree/modifyfgo)

Fiddler版(v1.1, 暂停更新): [https://github.com/locbytes/FGO_FiddlerScript](https://github.com/locbytes/FGO_FiddlerScript)
