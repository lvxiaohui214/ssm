package com.lxh.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lxh.bean.Department;
import com.lxh.bean.Employee;
import com.lxh.dao.DepartmentMapper;
import com.lxh.dao.EmployeeMapper;

/**
 * @describe Spring 单元测试
 * @author lxh
 * @date 2018年4月8日
 */

@RunWith(SpringJUnit4ClassRunner.class)
//指定Spring的配置文件的位置
@ContextConfiguration(locations= {"classpath:applicationContext.xml"}) 
public class MapperTest {
		
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	SqlSession sqlSession;
	
	@Test
	public void testCurd() {
		
//		System.out.println(departmentMapper);
//		departmentMapper.insertSelective(new Department(null,"开发部"));
//		departmentMapper.insertSelective(new Department(null,"测试部"));
		
		//插入员工
//		employeeMapper.insertSelective(new Employee(null,"Jerry", "M", "Jerry@qq.com", 1));
		
		//批量插入
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		for(int i = 0 ; i < 1000 ; i++) {
			String uid = UUID.randomUUID().toString().substring(0, 4)+i;
			mapper.insertSelective(new Employee(null,uid,"M", uid+"@qq.com",1));
		}
		System.out.println("完成------");
	}
}