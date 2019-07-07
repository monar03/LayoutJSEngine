# LayoutJSEngine
JSを通じてレイアウトをいじれる様な処理を行うことができるライブラリ


## gradleの設定
```
repositories {
    maven {
        url 'https://raw.githubusercontent.com/monar03/LayoutJSEngine/master/repository'
    }
    maven {
        url "https://monar03.github.io/mvn-repo"
    }
}

dependencies {
    implementation 'jp.aquabox.app.layout:aquagear-engine:1.0.0'
}
```

## サンプルコード
https://github.com/monar03/LayoutJSEngine/blob/master/app/src/main/java/jp/aquabox/app/layoutjsengine/MainActivity.kt
```
class MainActivity : AppCompatActivity(), JSEngine.JSEngineInterface {
    lateinit var jsEngine: JSEngine

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsEngine = JSEngine(
            this,
            object : JSEngine.JSLoadListener() {
                override fun onReady() {
                    jsEngine.loadModule(
                        "test",
                        getFileString("index/index.vxml"),
                        getFileString("index/index.vcss"),
                        getFileString("index/index.js"),
                        object : JSEngine.JSViewLoadListener {
                            override fun onViewLoadEnd(v: View) {
                                root.addView(v)
                            }
                        }
                    )
                }

                private fun getFileString(path: String): String {
                    val input: InputStream = assets.open(path)
                    val buffer = ByteArray(input.available())
                    input.read(buffer)
                    input.close()

                    return String(buffer, Charsets.UTF_8)
                }
            })
    }
}
```

レイアウトファイル（index.vxml）
```
<scroll-view class="scroll">
     <view for="{{list}}" class="box">
        <view class="box2" tap="tap">
            <image src="{{url}}" />
            <view class="box">
                <view class="test">
                    {{test}}
                </view>
                <view class="test1" tap="tap1">
                    {{test1}}
                </view>
                <text class="text">
                    {{test2}}
                </text>
            </view>
        </view>
    </view>
</scroll-view>
```

デザインファイル(index.vcss)
```
.test {
    padding:10px;
}
.test1 {
    padding:10dp;
}
.box {
    orientation : vertical;
    width :fill;
}

.box2 {
    margin : 10dp;
    orientation : horizon;
    width :fill;
}

.text {
    textSize:20sp;
}

.scroll {
    height : fill;
    width  : fill;
}
```

JSファイル
```
{
  data: {
    list: [
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World1',
            test1: 'Hello World2',
            test2: 'Hello World3',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World4',
            test1: 'Hello World5',
            test2: 'Hello World6',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World4',
            test1: 'Hello World5',
            test2: 'Hello World6',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World7',
            test1: 'Hello World8',
            test2: 'Hello World9',
        }]
  },
  tap: function(data) {
    aquagear.navigateTo('hoge')
  },
  tap1: function() {
    aquagear.showToast('test')
    this.setData('list', [
    {
      test: 'Hello World1',
      test1: 'Hello World2',
      test2: 'Hello World3'
    },
    {
      test: 'Hello World10',
      test1: 'Hello World20',
      test2: 'Hello World30'
    }])
  }
}
```
