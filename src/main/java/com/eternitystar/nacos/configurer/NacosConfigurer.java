package com.eternitystar.nacos.configurer;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eternitystar.nacos.instance.NacosInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.net.InetAddress;
import java.util.List;

/**
 * @program: nacos-spring-boot
 * @author: LiQingYong
 * @description: nacos注册
 * @create: 2022-01-10 14:18
 **/

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class NacosConfigurer {
    @Value("${server.port}")
    private Integer port;
    @Value("${nacos.discovery.server-addr}")
    private String serverAddr;
    @Value("${nacos.server-name:spring-nacos-server}")
    private String serverName;
    @Value("${spring.application.name:spring-nacos-server}")
    private String localServerName;
    @Value("${nacos.discovery.cluster-name:spring-nacos-cluster}")
    private String clusterName;

    @Bean(name = "nacosConfigurer")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public NacosConfigurer nacosConfigurer() {
        registerInstance();
        return new NacosConfigurer();
    }

    /**
     * 注册nacos服务
     */
    private void registerInstance() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            List<NamingService> namingServices = NacosInstance.getNamingServices(serverAddr);
            for (int i = 0;i<namingServices.size();i++) {
                //通过Instance注册服务
                Instance instance=new Instance();
                instance.setWeight(1);  //1~100
                instance.setPort(port);
                instance.setEnabled(true);   //是否启用
                instance.setEphemeral(true);  //临时节点/持久化节点, CP（Raft）, AP（Distro）
                instance.setClusterName(clusterName);  //集群名称
                instance.setIp(localHost.getHostAddress());
                instance.addMetadata("serverName",serverName);//服务名
                namingServices.get(i).registerInstance(localServerName,instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
