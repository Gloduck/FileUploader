import cn.gloduck.uploader.FileUploadContext;
import cn.gloduck.uploader.strategy.github.GithubConfigConst;
import cn.gloduck.uploader.strategy.github.GithubEntity;
import cn.gloduck.uploader.strategy.github.GithubUploadStrategy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UploaderTest {
    //
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

}
