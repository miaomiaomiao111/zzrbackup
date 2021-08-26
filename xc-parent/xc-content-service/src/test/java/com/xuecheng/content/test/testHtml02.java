package com.xuecheng.content.test;

import com.xuecheng.content.service.CourseBaseService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SpringBootTest
public class testHtml02 {

    @Autowired
    private CourseBaseService courseBaseService;

    @Test
    public void testHtml01() throws IOException, TemplateException {
        // 1.创建配置类信息
        Configuration configuration = new Configuration(Configuration.getVersion());

        // 2.对配置类进行配置
        String path = this.getClass().getResource("/templates/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path));
        configuration.setDefaultEncoding("utf-8");

        // 3.获得模板文件对象
        Template template = configuration.getTemplate("learing_article.ftl");


        // 4.获得数据模型
        Map<String, Object> map = courseBaseService.preview(42L, 1232141425L);

        // 5.静态化操作
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);



        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("d:/index.html"));
        IOUtils.copy(inputStream, outputStream);
    }
}