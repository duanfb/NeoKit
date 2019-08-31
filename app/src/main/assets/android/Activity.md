## 生命周期

### 正常情况：

    onCreate(Bundle) --onStart()---onResume()---onPause()--onStop()--onDestory()
    
    按Home键：
    onPause()--onStop()
    再次回到Activity：onRestart()--onStart()--onResume
    
    弹出Dialog不会对Activity什么周期有变化
    
    
    ps:onResume是当前Activity可以获取焦点回调，失去焦点onPause


### 异常情况：
    
    内存不足杀死Activity的优先级：
        前台Activity > 可见非前台Activity > 后台Activity
        
    异常回收： 回调onSaveInstanceState(Bundle)且重建
        保存数据：onCreate中判断bundle是否为空，不为空则获取。
            onSaveInstanceState中保存上个页面传过来的数据。
    
    屏幕旋转：回调onConfigurationChange,为了不重建Activity，
        在清单文件中注册属性configuration
    
    
### 启动模式

    Standard:标准
    SingleTop:栈顶复用，回调onNewIntent 
        场景：app大部分页面，防住多次点击产生多个Activity，
            或者是消息页面，多次收到QQ消息等
    
    SingTask: singleTop的加强版，清空顶部Activity，回调onNewIntent
        场景：首页设置，进入首页，清除其他页面
    
    SingIntance: singleTask的加强版，另开一个栈，只有它。
        可分为两种情况：
           1.如果将要启动的目标Activity不存在，系统将会创建一个全新的Task栈，在创建目标Activity实例，并将它加入到新的Task栈顶。
               2.如果将要启动的目标Activity已经存在，无论它位于那个程序中、位于哪个Task栈中，系统都会将该Activity所在的Task转 到前台，从而使该Activity显示出来。
         在一个新栈中创建该Activity实例，并让多个应用共享该栈中的该Activity实例。一旦该模式的Activity的实例存在于某个栈中，任何应用再激活该Activity时都会重用该栈中的实例，其效果相当于多个应用程序共享一个应用，不管谁激活该Activity都会进入同一个应用中。
           
        场景：app全局的一个页面，账户异地登录等
        
~~~
    Activity中findViewById原理
     Activity.findViewById ----> PhoneWindow.findViewById--->DecorView.findViewById(即ViewGroup.findViewById)

     for ( int i = 0; i < mChildrenCount; i++) {
            Viewm childrenView = views[i].findViewById(id);
            if (childrenView != null) {
                return childrenView;
            }
        }

        class View {
            int mID;

            View findViewById(int id) {
                if (mID == id) {
                    return this;
                } else {
                    return null;
                }
            }
        }  
~~~

### Activity,Window,View的关系

    Activity类中含有Window对象(PhoneWindow)，DecorView为Window
    的根布局，View作为子View add到DecorView中.为什么不直接用Window作为Android中的页面？
    原因是Activity对页面做了堆栈和生命周期管理，Intent启动等等，如果是Window，用户得自己管理Window，
    显然增加开发难度。
    