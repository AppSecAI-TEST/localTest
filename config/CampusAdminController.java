package com.sict.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sict.entity.Company;
import com.sict.entity.Position;
import com.sict.entity.RecruitInfo;
import com.sict.entity.Teacher;
import com.sict.service.CompanyService;
import com.sict.service.DictionaryService;
import com.sict.service.PositionService;
import com.sict.service.RecruitInfoService;
import com.sict.util.Common;

@Controller
public class CampusAdminController {
	/**
	 * 注入recruitInfoService by宋浩 2015-12-20 *
	 * 
	 * */
	@Resource(name = "recruitInfoService")
	private RecruitInfoService recruitInfoService;
	
	/**
	 * 注入companyService by宋浩20151226 *
	 * 
	 * */
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	/**
	 * 注入positionService by宋浩20151226 *
	 * 
	 * */
	@Resource(name = "positionService")
	private PositionService positionService;
	
	
	@RequestMapping("CampusAdmin/index.do")
	public String toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/campusViews/campusAdmin/index";
	}
	
	
	
}
