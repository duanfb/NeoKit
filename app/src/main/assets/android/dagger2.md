### 依赖引入

    gradler引入
    
### 说明

    @Inject：注明谁需要被注入依赖
    
    @Module：Module类里面的方法是专门提供依赖，所以我们定义一个
        类，然后用@Module去注解，这样的话Dagger2就知道去什么地方
        获取这个依赖实例
        
    @Provide：用这个注解，告诉Dagger2我们想要构造对象提供依赖
    
    @Component：它是@Inject和@Module注解的桥梁，连接这2个
    
    
### 书写
    新建包名：di dependensIndeject依赖注入缩写

    1.创建类Module: 它代表模块的意思
    
    @Module
    public class UserModule {
    
        @Provides
        public User provideUser() {
            return new User();
        }
    }
    
    @Module：代表该类是个提供给外界的模块
    @Provides：代表该方法可以给外界提供User类
    

    2.创建接口Component(组件、元件),其需要一个类Module

    @Component(modules=UserModule.class) //可以点开@Component看看里面是属性
    public interface UserComponent {
        
        //注入的参数必须是子类实例，不可使用抽象对象，如：Activity
        //可理解将UserModule对外提供的实例注入给MainActivity
        void inject(MainActivity activity);
    }
    
    3.Rebuild一下，dagger2将会生成DaggerUserComponent(Dagger+XXXComponent)
    
    MainActivity中使用
    
    @Inject
    User user;
    
    DaggerUserComponent.create().inject(this);
    或
    DaggerUserComponent.builder().userModule(new UserModule()).build().inject(this)
        

        
    