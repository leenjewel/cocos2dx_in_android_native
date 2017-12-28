# 将 Cocos2d-x 以控件形式嵌入 Android 原生工程

使用 AndroidStudio 新建一个原生的 Android 工程，将 Cocos2d-x 引擎以控件的形式嵌入到这个原生 Android 工程的 Activity 中。开发环境：

- AndroidStudio 3.0

- Cocos2d-x 3.16 (使用了 JS 支持，Lua 和 C++ 也可以)

## 思路

首先，cocos2d-x 出包的 Android 程序基本上由 JNI 也即 C++ 代码编译的链接库 `so` 文件和纯 Java 代码的公共模块组成。将引擎插件话这件事儿本身与 JNI 部分没有什么关系，这部分负责具体的游戏逻辑，所以不在讨论范围内。要说唯一需要修改的 JNI 部分就是需要将 `CCFrameBuffer.cpp` 文件中设置帧缓冲背景改为透明（cocos2d-x 引擎默认是不透明黑色背景）这样比较适配安卓原生程序。

其实 cocos2d-x 引擎默认启动显示的 `org.cocos2dx.lib.Cocos2dxActivity` 里面也是嵌入了真正用于显示游戏内容的控件 `org.cocos2dx.lib.Cocos2dxGLSurfaceView` 只不过引擎是全屏显示了这个控件，并在自己的这个 `org.cocos2dx.lib.Cocos2dxActivity` 里面配合写了很多逻辑代码。

而我们想要实现我们将引擎控件话的目的，需要做的大概有这么几件事儿：

- 将 `org.cocos2dx.lib.Cocos2dxGLSurfaceView` 作为控件内嵌到 `layout` 中

- 将 `org.cocos2dx.lib.Cocos2dxActivity` 做的很多事儿移植到我们自己的 Activity 里面去做。

具体怎么做大家仔细看看我提交的源码以及修改记录就明白了。其实这件事儿 cocos2d-x 引擎可以直接支持的，无奈引擎 Android 部分的 Java 代码写的不够灵活，不进行一番修改的话是没办法做到的。

## 关于项目里 cocos2dx 这个子模块

其实这个子模块就是 cocos2d-x 引擎中 `cocos2d-x/cocos/platform/android/libcocos2dx` 这部分的源代码了。我之所以用子模块的形式引入是方便大家学习如何修改这部分代码以达到将引擎控件话的目的。这部分也可以直接构建生成 `aar` 文件作为公共模块库引入。这样日后在编写其他原生 Android 工程时若想要使用内嵌 cocos2d-x 引擎，只需要导入这个 `aar` 库和相应的引擎 `so` 链接库和游戏具体逻辑脚本即可。

## 写在最后

这只是一个简单的 HelloWorld 示例，只提供一个思路，并没有做过相近的测试，坑肯定是有的。目前只是简单的测试了一下控件话，声音音效调用，程序前后台切换。祝玩的愉快！
