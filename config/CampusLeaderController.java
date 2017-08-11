package com.sict.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sict.util.Common;

/*
 * 功能：手机端领导控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class CampusLeaderController {
	
	/**
	 * @author WuGee
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至首页
	 * @throws IOException
	 * @throws ServletException
	 * 2015年12月2日14:44:06
	 */
	@RequestMapping("CampusLeader/index.do")
	public String toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		return "/campusViews/campusLeader/index";
	}
}
