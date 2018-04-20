package com.lxh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxh.bean.Department;
import com.lxh.bean.Msg;
import com.lxh.service.DepartMentService;

@Controller
public class DepartMentController {
	
	@Autowired
	private DepartMentService departMentService;
	
	@RequestMapping("depts")
	@ResponseBody
	public Msg getDepts() {
		List<Department> depts = departMentService.getDepts();
		return Msg.success().add("depts",depts);
	}
}	