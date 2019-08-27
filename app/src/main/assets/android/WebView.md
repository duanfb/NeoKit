### [WebView全解](https://yq.aliyun.com/articles/32559)

    //创建方式通过new使用AppContext，如果是xml,则context为Activity
    WebView webView = new WebView(getAppContext())
    //WebView设置match，并add
    viewGroup.addView(webView);
    
    if (null != mWebView) {
        //解除webView与父控件的依附关系
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.loadUrl("about:blank");
            mWebView.setVisibility(View.GONE);
            mWebView.destroy();
            mWebView = null;
    }

    webViewClient.onPageFinished方法会多次调用
    onProgressChanged方法虽然也多次调用，但是又进度値，比较靠谱
    
    jsbridge：解决app和h5页面互调问题
    
    后台耗电：
    
    加载图片的缓存
    
    内存泄露
     解决方法：
     1.使用独立进程，简单暴力，不过可能涉及到进程间通信问题
     2.动态添加WebView，对传入WebView的Context使用弱引用，动态添加WebView意思是在布局创建ViewGroup用来放置WebView，Activity Destroy是否remove掉



    腾讯T5内核浏览器

        
### 混淆问题

    #保留annotation， 例如 @JavascriptInterface 等 annotation
    -keepattributes *Annotation*
    
    #保留跟 javascript相关的属性 
    -keepattributes JavascriptInterface
    
    #保留JavascriptInterface中的方法
    -keepclassmembers class * {
        @android.webkit.JavascriptInterface <methods>;
    }
    #这个类是用来与js交互，所以这个类中的 字段 ，方法， 不能被混淆、全路径名称.类名
    -keepclassmembers public class com.youpackgename.xxx.H5CallBackAndroid{
       <fields>;
       <methods>;
       public *;
       private *;
    }

### WebView常见坑

    http://mp.weixin.qq.com/s/FyxuOuTFyZ_F8D0jQ8w5bg
    
    常见坑有：
        1.播放视频或音乐，退出页面，后台还在播放
        2.用网页标题设置自己的标题
        3.混淆导致js调用失败
        4.5.0以后http和https混合，导致https图片显示不出来
        5.WebView调用手机系统相册上传图片
        6.长按保存图片
        7.Android 4.0+ 版本中的EditText字符重叠问题
        8.ViewPager里面显示WebView，非首屏创建点击无效问题
        9.Android8.0新特性，清单文件添加代码，增强安全性
        10.WebView显示进度条
        11.监听WebView滑动到底部
        12.证书未通过问题
        13.第一次加载成功，第二次加载空白问题。解决：设置为noCache，webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        14.Android9.0不支持http url问题:Application标签添加android:usesCleartextTraffic="true"