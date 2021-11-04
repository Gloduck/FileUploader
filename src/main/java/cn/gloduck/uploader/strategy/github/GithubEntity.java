package cn.gloduck.uploader.strategy.github;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 请求的请求体
 */
public class GithubEntity {
    /**
     * github请求体
     *
     * @author Gloduck
     * @date 2021/11/04
     */
    @Data
    public static class GithubRequestBody {
        private String message;
        private Commiter commiter;
        private String content;

        public GithubRequestBody(String message, String name, String email, String content) {
            this.message = message;
            this.commiter = new Commiter(name, email);
            this.content = content;
        }

        /**
         * 转换为Json
         *
         * @return {@link String}
         */
        public String toJson(){
            return JSON.toJSONString(this);
        }
    }

    /**
     * 请求者信息
     *
     * @author Gloduck
     * @date 2021/11/04
     */
    @Data
    public static class Commiter {
        private Commiter(String name, String email) {
            this.name = name;
            this.email = email;
        }
        private String name;
        private String email;
    }

    @Data
    public static class Content{
        private String name;
        private String path;
        private String sha;
        private Integer size;
        private String url;
        @JSONField(alternateNames = "html_url")
        private String htmlUrl;
        @JSONField(alternateNames = "download_url")
        private String downloadUrl;
        @JSONField(alternateNames = "git_url")
        private String type;
    }
}
