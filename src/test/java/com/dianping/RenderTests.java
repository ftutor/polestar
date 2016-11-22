package com.dianping;

import com.dianping.core.MagicRender;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenderTests {

	private MagicRender mR = new MagicRender();
	@Test
	public void contextLoads() throws IOException, TemplateException {

		Map map = new HashMap();
		map.put("user", "lavasoft");
		map.put("url", "http://www.baidu.com/");
		map.put("name", "百度");
		//创建模版对象
		System.out.println("mr:" + mR.render("test.ftl", map));
	}

}
