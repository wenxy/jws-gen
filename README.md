# jws-gen
#####uc框架-Jws-代码生成器
##概述
######通过表结构生成简易的CURD代码，支持listCache，rowcache；
##使用说明
#####1、下载gen代码；
#####2、将gen复制到你的jws workspace中；
#####3、设置gen目录为link source；
#####4、找到gen下的gen.java代码；
#####5、很容易懂啦，配置数据源，需要生成代码的表名，及代码保存的路径；
#####6、执行main就可以啦，代码自动生成啦；


###当然，你也可以将gen作为独立的工程运行，就可以忽略2,3步骤了。


##Support
#####支持自动生成DDL，DAO，Cache，database配置;
#####不支持自动分库的配置生成，代码方法都带有Shard入参，需要在service层设置传入；
#####支持JWS版本 >=1.4.0
##注意事项
#####xml配置：如果配置项已经存在则不会覆盖更新；
#####java代码：如果代码存在，则覆盖更新；
#####默认cache配置为client0，默认数据源名为dbbase；需要修改请在代码生成后调整；

