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
class MainActivity : AppCompatActivity(), AquagearEngine.OnEngineInterface {
    private lateinit var aquagearEngine: AquagearEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aquagearEngine = AquagearEngine(
            this,
            AquagearEngine.AquagearEngineRenderCreator()
        ) {
            it.loadModule(
                "test",
                LayoutModule.LayoutModuleData(
                    getFileString("index/index.vxml"),
                    getFileString("index/index.vcss"),
                    getFileString("index/index.js")
                )
            ) {
                root.addView(it)
            }

            it.loadModule(
                "test1",
                LayoutModule.LayoutModuleData(
                    getFileString("index/index.vxml"),
                    getFileString("index/index.vcss"),
                    getFileString("index/index.js")
                )
            ) {
                root.addView(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        aquagearEngine.onResume()
    }

    override fun onPause() {
        super.onPause()
        aquagearEngine.onPause()
    }

    private fun getFileString(path: String): String {
        val input: InputStream = assets.open(path)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()

        return String(buffer, Charsets.UTF_8)
    }

    override fun getEngine(): AquagearEngine {
        return aquagearEngine
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
