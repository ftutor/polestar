package com.dianping.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;

/**
 * Created by fangyingming on 16/11/22.
 */
public class ResourceLocate {
    private Configuration cfg;
    public static final ResourceLocate RESOURCE_LOAD = new ResourceLocate();

    private String DIR = "config/";
    private ResourceLocate() {
        init();
    }

    public void init() {
        try {
            //初始化FreeMarker配置
            //创建一个Configuration实例
            cfg = new Configuration();
            //设置FreeMarker的模版文件夹位置
            String path =this.getClass().getResource("/").getPath()+DIR;
            File ins = new File(path);
            cfg.setDirectoryForTemplateLoading(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  Template getTemplateByName(String str) throws IOException {

        return cfg.getTemplate(str);

    }
}
