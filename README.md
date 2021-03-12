# rpc-framework
a simple com.lhb.rpc framework


自定义服务引用、标识服务、扫描自定义服务注解

@RpcService注解：标识一个服务，
@RpcReference注解：远程服务引用
@RpcScan注解：扫描指定路径下的自定义注解


客户端、服务端：

1、编写class CustomScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware、class CustomScan extends ClassPathBeanDefinitionScanner，实现服务启动时自动扫描注解生成bean放入容器中
2、编写class SpringBeanPostProcessor implements BeanPostProcessor。将带有@RpcService注解的bean放入服务提供者的内存中（ServiceProvider.serviceMap），同时将服务注册到注册中心。将带有@RpcReference注解的bean属性动态生成一个代理对象返回。当调用代理对象的方法时，发送消息到注册中心寻找服务。寻找到服务提供者的地址后封装方法信息，发送消息到服务提供者。


注册中心：

1、注册中心启动监听端口9999
2、注册中心接收到服务提供者的注册消息，将服务放入内存中（ServiceCenter.servicesMap)，返回成功
3、注册中心接收到消费者的寻找服务消息，去内存中寻找（ServiceCenter.servicesMap)，返回服务提供者的地址


服务端：

1、服务提供者接收到消费者的消息，去内存中ServiceProvider.serviceMap）寻找服务，通过反射执行相应的方法，并将结果封装返回。




优化：
1、消息id未同步，可以优化消息id方便跟踪

2、现在注册中心只是随机返回一个服务地址给客户端，优化选择策略

3、如果要发送的数据非常大（例如：包含10万条数据的list等），可以选择压缩后在发送

