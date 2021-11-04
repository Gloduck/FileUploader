package cn.gloduck.uploader;

/**
 * 常量类，其余策略的常量类需要实现此接口。
 * 定义在实现类的常量将会被用于配置校验，即传入的配置必须有这些字段
 * 定义在此类的字段为可选字段，不会被用于配置校验。{@link AbstractUploadStrategy#checkConfig()}。非必须字段可能实现会给默认值，但是可能都不一样
 */
public interface StrategyConfigConst {
    /**
     * 连接超时时间
     */
    String CONNECTION_TIMEOUT = "connection_timeout";
    /**
     * 读取超时时间
     */
    String READ_TIMEOUT = "read_timeout";
}
