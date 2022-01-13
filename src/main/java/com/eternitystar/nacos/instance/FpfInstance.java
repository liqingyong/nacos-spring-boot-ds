package com.eternitystar.nacos.instance;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @program: nacos-spring-boot
 * @author: LiQingYong
 * @description: nacos服务获取
 * @create: 2022-01-10 14:34
 **/
public class FpfInstance {
    private static String[] serverList = new String[]{};
    private static List<NamingService> namingService;

    /**
     * 获取服务列表
     * @return
     */
    public static String[] getServerList() {
        return serverList;
    }

    /**
     * 设置实例
     *
     * @return
     */
    public static synchronized void setNamingService(NamingService namingService) {
        if (FpfInstance.namingService == null)
            FpfInstance.namingService = new ArrayList<>();
        FpfInstance.namingService.add(namingService);
    }

    /**
     * 增加实例
     *
     * @return
     */
    private static void addNamingService(String serverAddr) {
        try {
            NamingService naming = NamingFactory.createNamingService(serverAddr);
            setNamingService(naming);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单个实例
     *
     * @return
     */
    public static NamingService getOneNamingService() {
        return getNamingService("localhost:8848");
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static NamingService getNamingService() {
        return getNamingService("localhost:8848");
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static List<NamingService> getNamingServices() {
        return getNamingServices("localhost:8848");
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static List<NamingService> getNamingServices(String serverAddr) {
        getNamingService(serverAddr);
        return namingService;
    }

    /**
     * 获取实例
     *
     * @param serverAddr 注册中心地址
     * @return
     */
    public static NamingService getNamingService(String serverAddr) {
        if (namingService != null)
            return namingService.get(0);
        serverList = serverAddr.split(",");
        for (String s : serverList)
            addNamingService(s);
        return getOneNamingService();
    }

    /**
     * 查询所有服务
     *
     * @param serverName 服务名
     * @return
     */
    public static List<Instance> getAllInstances(String serverName) {
        if (namingService != null) {
            try {
                return getNamingService().getAllInstances(serverName);
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 查询所有服务
     *
     * @param serviceName 服务名
     * @param healthy     是否健康
     * @return
     */
    public static List<Instance> selectInstances(String serviceName, boolean healthy) {
        try {
            return getNamingService().selectInstances(serviceName, healthy);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    /**
     * 增加配置监听
     *
     * @param dataId
     * @param group
     * @param listener
     * @return
     */
    public static boolean addListener(String dataId, String group, Listener listener) {
        try {
            getConfigService(null).addListener(dataId, group, listener);
            return true;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除配置监听
     *
     * @param dataId
     * @param group
     * @param listener
     * @return
     */
    public static boolean removeListener(String dataId, String group, Listener listener) {
        try {
            getConfigService(null).removeListener(dataId, group, listener);
            return true;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return false;
    }

    //配置项

    /**
     * 发布配置
     *
     * @param dataId
     * @param group
     * @param content
     * @param type
     * @return
     */
    public boolean publishConfig(String dataId, String group, String content, com.alibaba.nacos.api.config.ConfigType type) {
        try {
            return getConfigService(null).publishConfig(dataId, group, content, type.getType());
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除配置
     *
     * @param dataId
     * @param group
     * @return
     */
    public boolean removeConfig(String dataId, String group) {
        try {
            return getConfigService(null).removeConfig(dataId, group);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取配置
     *
     * @param dataId
     * @param group
     * @return
     */
    public static String getConfig(String dataId, String group) {
        return getConfig(dataId, group, serverList[0]);
    }

    /**
     * 获取配置，根据服务地址
     *
     * @param dataId
     * @param group
     * @param serverAddr
     * @return
     */
    public static String getConfig(String dataId, String group, String serverAddr) {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            return configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取config服务
     *
     * @param serverAddr
     * @return
     * @throws NacosException
     */
    private static ConfigService getConfigService(String serverAddr) throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr != null ? serverAddr : serverList[0]);
        return NacosFactory.createConfigService(properties);
    }
}
