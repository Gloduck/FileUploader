+ 一个基于策略模式实现的文件上传工具。
# Github上传
```java
public static void main(String[] args) {
    Map<String, String> configMap = new HashMap<>();
    configMap.put(GithubConfigConst.EMAIL, "邮箱");
    configMap.put(GithubConfigConst.REPO, "仓库名");
    configMap.put(GithubConfigConst.USER, "用户名");
    configMap.put(GithubConfigConst.TOKEN, "生成的token");
    System.out.println(FileUploadContext.instance().registerStrategy("github", GithubUploadStrategy.class, configMap));
    System.out.println(FileUploadContext.instance().getStrategy("github")
            .uploadFile("test.png", "path", new File("C:\\Users\\Administrator\\Desktop\\test.png")));
}
```