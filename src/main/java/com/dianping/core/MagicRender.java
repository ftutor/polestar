package com.dianping.core;

import com.dianping.utils.ResourceLocate;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by fangyingming on 16/11/22.
 */
public class MagicRender {

    private ResourceLocate resourceUtils = ResourceLocate.RESOURCE_LOAD;

    public String render(String tplName, Map<String, Object> map) {
        String result = "";
        StringWriter sw = new StringWriter();
        try {
            Template t = resourceUtils.getTemplateByName("test.ftl");
            t.process(map, sw);

            result = sw.toString();
            sw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
