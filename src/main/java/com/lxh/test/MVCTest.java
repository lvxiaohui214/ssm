package com.lxh.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.lxh.bean.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations= {"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MVCTest {
	
	@Autowired
	WebApplicationContext applicationContext;
	
	//����mvc����,��ȡ��������
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}
	
	@Test
	public void	testPage() throws Exception {
		//ģ�������õ�����ֵ
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/all").param("pn","1")).andReturn();
		//������ɺ�,���������pageInfo,���ǿ���ȡ��pageInFo������֤
		MockHttpServletRequest request = result.getRequest();
		PageInfo p = (PageInfo) request.getAttribute("pageInfo");
	
		System.out.println("��ǰҳ��:"+p.getPageNum());
		System.out.println("��ҳ�� :"+p.getPages());
		System.out.println("�ܼ�¼�� :"+p.getTotal());
		
		System.out.println("��ҳ����Ҫ������ʾ��ҳ��");
		int[] nums = p.getNavigatepageNums();
		for(int i : nums) {
			System.out.print(" "+ i);
		}
		System.out.println("------"+p.getList());
		//��ȡԱ������
		List<Employee> list = p.getList();
		for(Employee e : list) {
			System.out.println(e.getEmpName()+"=====>"+e.getDepartment().getDeptId());
		}
	}
}