# jws-gen
uc框架-Jws-代码生成器
## 1、下载gen代码；
## 2、将gen复制到你的jws workspace中；
## 3、设置gen目录为link source；
## 4、找到gen下的gen.java代码；
## 5、很容易懂啦，配置数据源，需要生成代码的表名，及代码保存的路径；
## 6、执行main就可以啦，代码自动生成啦；


###当然，你也可以将gen作为独立的工程运行，就可以忽略2,3步骤了。



自动生成DDL，DAO，Servie，Cache配置，建议自己的service另外命名，以防你需要对自动生成的代码做变更，导致后续表结构变化，无法使用gen代码了。
1.0
支持DDL生成
