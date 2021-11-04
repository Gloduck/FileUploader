package cn.gloduck.uploader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调度器
 */
public class FileUploadContext {
    private FileUploadContext(){}
    private static final FileUploadContext FILE_UPLOAD_CONTEXT = new FileUploadContext();

    private static final Map<String, UploadStrategy> STRATEGY_MAP;
    static {
        STRATEGY_MAP = new HashMap<>(4);
    }

    /**
     * 获取单例
     * @return
     */
    public static FileUploadContext instance(){
        return FILE_UPLOAD_CONTEXT;
    }

    /**
     * 获取策略
     *
     * @param strategyName 策略名称
     * @return {@link UploadStrategy}
     */
    public UploadStrategy getStrategy(String strategyName){
        return STRATEGY_MAP.get(strategyName);
    }

    /**
     * 注册策略，反射调用{@link AbstractUploadStrategy#AbstractUploadStrategy(Map)}初始化
     *
     * @param strategyName 策略名称
     * @param configMaps   配置映射
     * @param tClass       class类
     */
    public boolean registerStrategy(String strategyName, Class<? extends AbstractUploadStrategy> tClass, Map<String, String> configMaps){
        boolean success = false;
        try {
            Constructor<? extends AbstractUploadStrategy> constructor = tClass.getConstructor(Map.class);
            AbstractUploadStrategy uploadStrategy = constructor.newInstance(configMaps);
            if(!uploadStrategy.checkConfig()){
                // 配置检测失败
                return false;
            }
            STRATEGY_MAP.put(strategyName, uploadStrategy);
            success = true;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return success;
    }
}
