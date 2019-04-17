package com.zhbit.market.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhbit.market.entity.BStaff;
import com.zhbit.market.entity.BTakeOffice;
import com.zhbit.market.service.StaffService;

@Controller
@RequestMapping("/staff")
public class StaffController {
	@Autowired
	private StaffService staffService;
	
	//门店管理（保存新门店）
	@PostMapping("/home/addnewstaff")
	public @ResponseBody Object saveNewStaff(BStaff staff) {
		Map<String,Object> result=new HashMap<String,Object>();
		Integer theresult=staffService.insertNewStaff(staff);
		System.out.println("更新数："+theresult);
		if(theresult>0) {
			result.put("code",200);
			return result;
		}
		result.put("code", 500);
		return result;
	}
	
	//选择采购员
	@GetMapping("/home/selectPurchaseStaff")
	public @ResponseBody Object selectPurchaseStaff(BTakeOffice office) {
		Map<String,Object> result=new HashMap<String,Object>();
		List<BTakeOffice> staff=staffService.getTakeOffice(office);
		if(staff.size()!=0) {
			result.put("staff", staff);
			result.put("code", 200);
			return result;
		}
		result.put("code", 500);
		return result;
	}
}
