package cn.gloduck.uploader;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractUploadStrategy implements UploadStrategy {
    protected Map<String, String> configMap;

    public AbstractUploadStrategy(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    @Override
    public final String uploadFile(String fileName, String path, File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(inputStream == null){
            return null;
        }
        return uploadFile(fileName, path, inputStream);
    }

    @Override
    public final String uploadFile(String fileName, String path, InputStream fileInputStream) {
        byte[] readBytes = null;
        try (fileInputStream){
            readBytes = IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(readBytes == null){
            return null;
        }
        return uploadFile(fileName, path, readBytes);
    }

    /**
     * 检查配置文件是否包含所需字段（不包括StrategyConfigConst中的字段）
     *
     * @return
     */
    boolean checkConfig() {
        boolean success = false;
        try {
            Set<String> unCheckFieldName = new HashSet<>(16);
            Field[] interfaceField = StrategyConfigConst.class.getFields();
            for (Field field : interfaceField) {
                if(checkConst(field) && field.getType().equals(String.class)){
                    unCheckFieldName.add(field.get(null).toString());
                }
            }
            Class<? extends StrategyConfigConst> constClass = getConstClass();
            Field[] fields = constClass.getFields();
            for (Field field : fields) {
                if (checkConst(field)) {
                    // 如果是final并且是static并且是public
                    Class<?> type = field.getType();
                    if (type.equals(String.class)) {
                        // String类型
                        String configConst = field.get(null).toString();
                        // 如果此配置项常量不是接口的，并且配置中不包括此配置项常量，则返回false
                        if (!unCheckFieldName.contains(configConst) && !configMap.containsKey(configConst)) {
                            // 如果configMap中不包含配置
                            return false;
                        }
                    }
                }
            }
            success = true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 判断常量是否符合规则
     *
     * @return
     */
    private boolean checkConst(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    /**
     * 子类实现，获取Config所需要的常量，使用public static final String的字段标注。这部分后面会用于反射验证配置文件是否完整。
     *
     * @return
     */
    protected abstract Class<? extends StrategyConfigConst> getConstClass();


}
