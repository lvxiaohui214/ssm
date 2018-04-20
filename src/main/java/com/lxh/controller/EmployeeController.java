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
			//����ɾ��
			List<Integer> emp_ids = new ArrayList<Integer>();	
			String[] idstr = ids.split("-");
			for (String id : idstr) {
				emp_ids.add(Integer.parseInt(id));
			}
			employeeService.deleteBatch(emp_ids);
		}else {
			//ɾ������
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
	 * ��ѯ����
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
	 * ��֤�û����Ƿ����
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public Msg checkUser(@RequestParam("empName") String empName) {
		
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
		
		if(!empName.matches(regx)) {
			return Msg.fail().add("va_msg","�û���������6-16λӢ�ĺ����ֵ���ϻ���2-5λ����");
		}
		
		boolean b = employeeService.checkUser(empName);
		if(b) {
			return Msg.success();
		}else {
			return Msg.fail().add("va_msg","�û����Ѿ�����");
		}
	}
	
	//����
	@RequestMapping(value="/saveEmp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result) {
		if(result.hasErrors()) {
			Map<String,Object> map = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError fieldError : errors) {
				System.out.println("�����ֶ���"+fieldError.getField());
				System.out.println("������Ϣ"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(),fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields",map);
		}else {
			employeeService.save(employee);
			return Msg.success();
		}
	}
	
	/*
	 * ����jackson����
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1") Integer pn) {
		
		PageHelper.startPage(pn,5);
		List<Employee> all = employeeService.getAll();
		// ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		// ��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ���������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(all, 5);
		return Msg.success().add("pageInfo",page);
	}
	
	/**
	 * �����д��  ����һ��ҳ��
	 * @param pn ��ǰ�ڼ�ҳ 
	 * @param model
	 * @return
	 */
	@RequestMapping("all")
	public String getAll(@RequestParam(value="pn",defaultValue="1") Integer pn, Model model) {
		// ����PageHelper��ҳ���,�ڲ�ѯ֮ǰֻ��Ҫ���ã�����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn,5);
		List<Employee> all = employeeService.getAll();
		// ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		// ��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ���������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(all, 5);
		model.addAttribute("pageInfo", page);
		return "list";
	}
}