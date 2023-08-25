# FQWeb
[![GitHub license](https://img.shields.io/github/license/fengyuecanzhu/FQWeb?style=flat-square)](https://github.com/fengyuecanzhu/FQWeb/blob/master/LICENSE)
![Android SDK min 24](https://img.shields.io/badge/Android%20SDK-%3E%3D%2024-brightgreen?style=flat-square&logo=android)
![Android SDK target 33](https://img.shields.io/badge/Android%20SDK-target%2033-brightgreen?style=flat-square&logo=android)
![Xposed Module](https://img.shields.io/badge/Xposed-Module-blue?style=flat-square)

番茄小说Web服务

交流群：[887847462](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=2cgswx48xaTgYmQjSLfH0XNom5n4vm1z&authKey=npnemJO7L6NyLdKvjePLU%2Ffav5v75Q8alXVzCK%2FypGJtFqp1DV35fyukWhhmvTQU&noverify=0&group_code=887847462)

## 共享API

感谢[苏念](https://github.com/sunianOvO)、[鲸落](https://github.com/xw589589)和玖玖提供的服务器支持

苏念API：http://reading.api-fanqienovel.sunianyun.live

鲸落API：http://list.fqapi.jilulu.cn

玖玖API：http://list.fqapi.sfacg.link

## 使用
Web服务可在番茄小说设置界面开启，默认运行在9999端口

注：目前仅适配了`5.2.3.32`, `5.7.9.32`, `5.8.3.32`, `5.8.5.32`, `5.8.7.32`, `5.8.9.32`, `5.9.1.32`版本，其他版本不一定能用(可自行尝试，一般临近几个版本应该都能用)

书源和适配番茄版本蓝奏云链接：[点此下载](https://fycz.lanzoum.com/b00o3g1kf)

~~免root lspatch集成版（已失效）：[番茄小说](https://www.123pan.com/s/ib9LVv-IHiwd.html)~~

如需适配其他版本，请发[Issue](https://github.com/fengyuecanzhu/FQWeb/issues/new)并附上对应apk

如需定制其他APP，请加[QQ群](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=2cgswx48xaTgYmQjSLfH0XNom5n4vm1z&authKey=npnemJO7L6NyLdKvjePLU%2Ffav5v75Q8alXVzCK%2FypGJtFqp1DV35fyukWhhmvTQU&noverify=0&group_code=887847462)私聊群主

## API列表
### 搜索
```
method：GET
url：http://localhost:9999/search?query=关键字&page=页数
```
### 获取书籍详情
```
method：GET
url1：http://localhost:9999/info?book_id=书籍ID
url2：http://localhost:9999/minfo?book_id=书籍ID
```
### 获取书籍目录
```
method：GET
url：http://localhost:9999/catalog?book_id=书籍ID
```
### 获取章节内容
```
method：GET
url：http://localhost:9999/content?item_id=章节itemId
```
### 获取发现书籍
```
method：GET
url：http://localhost:9999/bookmall
params：和番茄api"/reading/bookapi/bookmall/cell/change/v1/"保持一致

method：GET
url：http://localhost:9999/category
params：和番茄api"/reading/bookapi/new_category/landing/v/"保持一致
```
### 任意API
```
method：GET
url：http://localhost:9999/{anyUrl}
params：和番茄对应保持一致
```
注：此接口仅对[此域名](https://api5-normal-sinfonlineb.fqnovel.com)做请求转发

## 下载
从[RELEASE](https://github.com/fengyuecanzhu/FQWeb/releases/latest)或[ACTION](https://github.com/fengyuecanzhu/FQWeb/actions/workflows/build.yml)下载

## 共享API说明

共享式内网穿透版本已上线，我们诚邀您与我们一起建立共享API服务，在开始之前，您必须了解此功能相关问题和风险：

* 1、什么是共享式内网穿透版本？

  此版本内置了frpc客户端，并由[苏念](https://github.com/sunianOvO)、[鲸落](https://github.com/xw589589)提供服务器，可以实现免费内网穿透

* 2、什么是内网穿透？

  简单来说，就是你可以在家里的手机或者电脑虚拟机上搭建了番茄Web，你可以在任何地方都可以访问这个服务，无需在当前手机上搭建

* 3、什么是共享式？

  使用内网穿透版本开启内网穿透服务后，系统将会自动分配一个临时域名给你，并将这个域名保存在服务端共享地址池里，任何人通过特定的API可以从这个池里随机获取一个地址，然后使用这个地址访问某个网络的番茄Web（类似于P2P）

* 4、为什么要建立共享式？

  因为内网穿透的特殊性，单个服务存在不稳定的问题，一旦节点掉线了，API将全部瘫痪，建立共享式服务可以有效避免这个问题，只要共享地址池中有一个可用的地址，API服务都能够使用

* 5、内网穿透有什么风险？

  内网穿透是一种功能强大但潜在风险较高的技术。它允许用户在互联网上访问其内部网络或设备，然而，这也意味着内网中的资源暴露在公共网络上。这种开放性可能导致未经授权的访问、数据泄露，甚至成为恶意攻击者的潜在入口。此外，内网穿透服务可能受到网络拥堵、服务不稳定等问题影响，给用户带来连接不稳定或服务不可用的风险。

## 常见问题

1、提示"Failed to connect to localhost/127.0.0.1:9999"怎么办？

答：番茄没有打开或者后台被杀死，重新打开番茄即可

2、提示"failed to connect to localhost/127.0.0.1 (port 9999) from /127.0.0.1 (port xxxxx)"怎么办？

答：番茄后台被冻结了，请重新切换番茄到前台。将番茄后台改成无限制可能解决这个问题

## 免责声明
* 该Xposed模块仅供学习交流使用，使用者必须自行承担使用该模块所带来的风险和责任。

* 使用该Xposed模块可能导致设备不稳定、崩溃和数据丢失等问题。作者不对任何因使用该模块而导致的问题承担责任。

* 开发者保留对该Xposed模块的更新、修改、暂停、终止等权利，使用者应该自行确认其使用版本的安全性和稳定性。

* 任何人因使用该Xposed模块而导致的任何问题，作者不承担任何责任，一切后果由使用者自行承担。

* 对于使用该Xposed模块所产生的任何问题，作者不提供任何形式的技术支持和解决方案。

请在使用该Xposed模块之前认真阅读以上免责声明并自行权衡风险和利益，如有异议请勿使用。如果您使用了该Xposed模块，即代表您已经完全接受本免责声明。
