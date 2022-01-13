# nacos-spring-boot-ds
Nacos2x + Springboot2.3+，解决Nacos-SpringBoot不支持高版本SpringBoot，封装启用服务注册

打包完成后手动删除jar里面的org.springframework目录，否则将会跟项目springframework冲突
###使用：
# 1、yml文件中配置：
```
spring:
  application:
    name: nacos-spring-boot #服务名 - 可选将此服务名注册进nacos
nacos:
  server-name: nacos服务 #在元数据serverName中可以看到此服务名
  discovery:
    cluster-name: spring-nacos-cluster #集群名 -可选
    server-addr: 127.0.0.1:8848 #注册中心地址 服务发现配置
  config:
    server-addr: 127.0.0.1:8848 #注册中心地址 服务配置管理
```
# 2、将“nacos-spring-boot-0.1.0.jar”拷贝至工程目录下，在pom.xml中加入
```
 <dependency>
            <groupId>com.eternitystar.nacos</groupId>
            <artifactId>nacos-spring-boot</artifactId>
            <version>0.1.0</version>
            <scope>system</scope>
            <systemPath>${basedir}/src/main/resources/lib/nacos-spring-boot-0.1.0.jar</systemPath>
        </dependency>
```
# 3、在SpringBoot入口类上加入@EnableDSNacos，如
```
@EnableDSNacos
@SpringBootApplication
public class NacosSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosSpringBootApplication.class, args);
    }

}
```
