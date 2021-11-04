package cn.gloduck.uploader;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * 上传策略
 */
public interface UploadStrategy {
    /**
     * 上传文件
     *
     * @param fileName  文件名称
     * @param path      路径
     * @param fileBytes 文件字节
     * @return {@link String} 成功返回链接，失败返回null（包括异常）
     */
    String uploadFile(String fileName, String path, byte[] fileBytes);

    /**
     * 上传文件
     *
     * @param fileName 文件名称
     * @param path     路径
     * @param file     文件
     * @return {@link String} 成功返回链接，失败返回null（包括异常）
     */
    String uploadFile(String fileName, String path, File file);

    /**
     * 上传文件
     *
     * @param fileName        文件名称
     * @param path            路径
     * @param fileInputStream 文件输入流
     * @return {@link String} 成功返回链接，失败返回null（包括异常）
     */
    String uploadFile(String fileName, String path, InputStream fileInputStream);

}
