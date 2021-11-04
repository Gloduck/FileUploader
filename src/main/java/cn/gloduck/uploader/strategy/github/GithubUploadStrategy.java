package cn.gloduck.uploader.strategy.github;

import cn.gloduck.uploader.AbstractUploadStrategy;
import cn.gloduck.uploader.StrategyConfigConst;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Github上传策略
 * 文档地址：https://docs.github.com/en/rest/reference/repos#create-or-update-file-contents
 *
 * @author Gloduck
 * @date 2021/11/04
 */
public class GithubUploadStrategy extends AbstractUploadStrategy {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    private HttpClient httpClient;

    /**
     * 构建并执行请求
     *
     * @param fileName      文件名称
     * @param path          路径
     * @param base64Content base64内容
     */
    private String buildAndExecuteRequest(String fileName, String path, String base64Content) {
        String result = null;
        String user = configMap.get(GithubConfigConst.USER);
        String repo = configMap.get(GithubConfigConst.REPO);
        String token = configMap.get(GithubConfigConst.TOKEN);
        String email = configMap.get(GithubConfigConst.EMAIL);
        String branch = configMap.get(GithubConfigConst.BRANCH);
        // 获取请求链接，同时对path和文件名进行转意，防止中文
        String requestUrl = String.format("https://api.github.com/repos/%s/%s/contents/%s/%s"
                , user, repo, URLEncoder.encode(path, StandardCharsets.UTF_8), URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        String requestBody = new GithubEntity.GithubRequestBody("upload_file", user, email, branch, base64Content).toJson();

        PutMethod putMethod = new PutMethod(requestUrl);
        putMethod.addRequestHeader("Authorization", String.format("token %s", token));
        StringRequestEntity requestEntity = null;
        try {
            requestEntity = new StringRequestEntity(requestBody, "application/json", StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (requestEntity == null) {

        }
        putMethod.setRequestEntity(requestEntity);
        try {
            int statusCode = httpClient.executeMethod(putMethod);
            // github上传文件成功返回201
            if (HttpStatus.SC_CREATED == statusCode) {
                // 请求成功
                String responseBody = putMethod.getResponseBodyAsString();
                JSONObject jsonObject = JSON.parseObject(responseBody);
                JSONObject content = jsonObject.getJSONObject("content");
                return content.get("download_url").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public GithubUploadStrategy(Map<String, String> configMap) {
        super(configMap);
        this.httpClient = new HttpClient();
        // 设置连接和读取超时时间
        String connectionTimeoutStr = configMap.get(StrategyConfigConst.CONNECTION_TIMEOUT);
        String readTimeoutStr = configMap.get(StrategyConfigConst.READ_TIMEOUT);
        int connectionTimeout = connectionTimeoutStr != null && isNumber(connectionTimeoutStr) ? Integer.parseInt(connectionTimeoutStr) : 1000;
        int readTimeout = readTimeoutStr != null && isNumber(readTimeoutStr) ? Integer.parseInt(readTimeoutStr) : 5000;
        HttpConnectionManager httpConnectionManager = httpClient.getHttpConnectionManager();
        httpConnectionManager.getParams().setConnectionTimeout(connectionTimeout);
        httpConnectionManager.getParams().setSoTimeout(readTimeout);
    }

    @Override
    protected Class<? extends StrategyConfigConst> getConstClass() {
        return GithubConfigConst.class;
    }


    @Override
    public String uploadFile(String fileName, String path, byte[] fileBytes) {
        String base64Content = new String(Base64.encodeBase64(fileBytes));
        return buildAndExecuteRequest(fileName, path, base64Content);
    }

    private boolean isNumber(String target) {
        return NUMBER_PATTERN.matcher(target).matches();
    }
}
