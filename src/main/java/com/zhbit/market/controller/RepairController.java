package com.zhbit.market.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhbit.market.entity.BRepair;
import com.zhbit.market.entity.BRepairOrder;
import com.zhbit.market.service.RepairService;

@Controller
@RequestMapping("/repair")
public class RepairController {
	@Autowired
	private RepairService repairService;
	
	//维修管理（保存新维修表）
	@PostMapping("/home/addnewrepair")
	public @ResponseBody Object saveNewRepair(BRepair repair,String userId,Integer sentUserId) {
		Map<String,Object> result=new HashMap<String,Object>();
		Integer theresult=repairService.insertNewRepair(repair);
		System.out.println("维修更新数："+theresult);
		if(theresult>0) {
			BRepair repairid=new BRepair();
			repairid.setTitle(repair.getTitle());
			repairid.setContent(repair.getContent());
			repairid.setStyle(repair.getStyle());
			List<BRepair> repairs=repairService.getRepair(repairid);
			if(repairs.size()!=0) {
				Integer theresult1=0;
				BRepairOrder order=new BRepairOrder();
				order.setSentUserId(sentUserId);
				order.setCreateTime(new java.sql.Date((new java.util.Date()).getTime()));
				order.setRepairId(repairs.get(0).getRepairId());
				JSONArray json=JSONObject.parseArray(userId);
				for(int i=0;i<json.size();i++) {
					order.setUserId(json.getInteger(i));
					theresult1+=repairService.insertNewRepairOrder(order);
				}
				if(theresult1>0) {
					result.put("code",200);
					return result;
				}
				result.put("code",300);
				return result;
			}
		}
		result.put("code", 500);
		return result;
	}
	
	//维修管理（获取维修单）
	@GetMapping("/home/getrepairorder")
	public @ResponseBody Object repairorderInfo(BRepairOrder order) {
		Map<String,Object> result=new HashMap<String,Object>();
		//根据发送者id获取维修单
		List<BRepairOrder> theorder=repairService.getRepairOrder(order);
		if(theorder.size()!=0) {
			BRepairOrder[] orders=new BRepairOrder[theorder.size()];
			for(int i=0;i<theorder.size();i++) {
				orders[i]=theorder.get(i);
			}
			result.put("code", 200);
			result.put("repairorder",orders);
			return result;
		}
		result.put("code", 500);
		return result;
	}
	
	//维修管理（获取维修表）
	@GetMapping("/home/getrepair")
	public @ResponseBody Object repairInfo(BRepair repair) {
		Map<String,Object> result=new HashMap<String,Object>();
		//根据维修表id获取维修单
		List<BRepair> therepair=repairService.getRepair(repair);
		if(therepair.size()!=0) {
			BRepair[] repairs=new BRepair[therepair.size()];
			for(int i=0;i<therepair.size();i++) {
				repairs[i]=therepair.get(i);
			}
			result.put("code", 200);
			result.put("repair",repairs);
			return result;
		}
		result.put("code", 500);
		return result;
	}
	
	//通知列表
	//前端判断角色：管理员（UserId）/用户（sentUserId）=>获取通知单
	//（用户）通知单去重=>获取通知+时间
	@GetMapping("/home/getrepairorderadmin")
	public @ResponseBody Object getRepairOrderAdmin(BRepairOrder order) {
		Map<String,Object> result=new HashMap<String,Object>();
		List<BRepairOrder> theorder=repairService.getRepairOrder(order);
		if(theorder.size()!=0) {
			result.put("repairorder", theorder);
			BRepair[] repairs=new BRepair[theorder.size()];
			for(int i=0;i<theorder.size();i++) {
				BRepair therepairid=new BRepair();
				therepairid.setRepairId(theorder.get(i).getRepairId());
				List<BRepair> therepair=repairService.getRepair(therepairid);
				repairs[i]=therepair.get(0);
			}
			if(repairs.length!=0) {
				result.put("code", 200);
				result.put("repairs", repairs);
				return result;
			}else {
				result.put("code", 300);
				return result;
			}
		}
		result.put("code", 500);
		return result;
	}
}
