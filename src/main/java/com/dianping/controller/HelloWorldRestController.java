package com.dianping.controller;

import com.dianping.core.MagicRender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangyingming on 16/11/22.
 */
@Controller
public class HelloWorldRestController {

    private MagicRender magicRender = new MagicRender();

    @GetMapping("/")
    public String helloWorld(){
        String str ="hello world";
        return "Hello world";
    }

    @RequestMapping("/sample")
    public String sample(Map<String,Object> model){
        Map map = new HashMap();
        map.put("user", "lavasoft");
        map.put("url", "http://www.baidu.com/");
        map.put("name", "百度20");
        String outPuts = magicRender.render("test.ftl", map);
        model.put("str",outPuts);
        return "sample";
    }
}
