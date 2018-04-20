package com.lxh.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxh.bean.Employee;
import com.lxh.bean.Msg;
import com.lxh.service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@ResponseBody
	@RequestMapping(value="/delete/{ids}")
	public Msg deleteEmp(@PathVariable("ids") String ids) {
		if(ids.contains("-")) {
			//批量删除
			List<Integer> emp_ids = new ArrayList<Integer>();	
			String[] idstr = ids.split("-");
			for (String id : idstr) {
				emp_ids.add(Integer.parseInt(id));
			}
			employeeService.deleteBatch(emp_ids);
		}else {
			//删除单个
			Integer id = Integer.parseInt(ids);	
			employeeService.deleteEmpById(id);
		}
		return Msg.success();
	}
	
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	public Msg updateEmp(Employee employee) {
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * 查询方法
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee =  employeeService.getEmp(id);
		return Msg.success().add("emp",employee);
	}
	
	/**
	 * 验证用户名是否可用
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public Msg checkUser(@RequestParam("empName") String empName) {
		
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
		
		if(!empName.matches(regx)) {
			return Msg.fail().add("va_msg","用户名可以是6-16位英文和数字的组合或者2-5位中文");
		}
		
		boolean b = employeeService.checkUser(empName);
		if(b) {
			return Msg.success();
		}else {
			return Msg.fail().add("va_msg","用户名已经存在");
		}
	}
	
	//保存
	@RequestMapping(value="/saveEmp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result) {
		if(result.hasErrors()) {
			Map<String,Object> map = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError fieldError : errors) {
				System.out.println("错误字段名"+fieldError.getField());
				System.out.println("错误信息"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(),fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields",map);
		}else {
			employeeService.save(employee);
			return Msg.success();
		}
	}
	
	/*
	 * 导入jackson包。
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1") Integer pn) {
		
		PageHelper.startPage(pn,5);
		List<Employee> all = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
		PageInfo page = new PageInfo(all, 5);
		return Msg.success().add("pageInfo",page);
	}
	
	/**
	 * 常规的写法  返回一个页面
	 * @param pn 当前第几页 
	 * @param model
	 * @return
	 */
	@RequestMapping("all")
	public String getAll(@RequestParam(value="pn",defaultValue="1") Integer pn, Model model) {
		// 引入PageHelper分页插件,在查询之前只需要调用，传入页码，以及每页的大小
		PageHelper.startPage(pn,5);
		List<Employee> all = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
		PageInfo page = new PageInfo(all, 5);
		model.addAttribute("pageInfo", page);
		return "list";
	}
}