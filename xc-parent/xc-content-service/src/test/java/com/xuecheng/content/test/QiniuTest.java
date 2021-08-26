package com.xuecheng.content.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * <p></p>
 *
 * @Description:
 */
public class QiniuTest {

    @Test
    public void uploadFile() {
 
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.huadong());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);


        //...生成上传凭证，然后准备上传
        String accessKey = "IcDNVQ15ZvuBQzBLqE4AczIxS3UJ8BfaVW7YQ9br";
        String secretKey = "yyB7CMwCchfU-ryje0hYnWnGfZ-fMGhGgq1W_yPS";
        String bucket = "yyzxuecheng";


        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\23846\\Desktop\\新建文件夹\\1.jpg";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "1.gpj";

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet =
                new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }
}