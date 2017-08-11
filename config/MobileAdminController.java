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
public class MobileAdminController {
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
	
	
	@RequestMapping("MobileAdmin/index.do")
	public String toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/mobileViews/mobileAdmin/index";
	}
	
	@RequestMapping("MobileAdmin/practiceManagementList.do")
	public String topracticeManagementList(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileAdmin/practiceManagementList";
	}
	
	/**
	 * 功能：管理员查询招聘信息 注解请求地址(映射)宋浩2015-12-28
	 * 
	 * 
	 * */
	@RequestMapping("MobileAdmin/RecruitmentInformationList.do")
	public ModelAndView recruitInfoList() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RecruitInfo> result = this.recruitInfoService.select();
		map.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		map.put("result", result);
		return new ModelAndView("/mobileViews/mobileAdmin/RecruitmentInformationList", map);
	}
	
	/**
	 * 功能：管理员添加招聘信息注解请求地址(映射)--添加页面宋浩 20151226
	 * 
	 * */
	@RequestMapping("MobileAdmin/RecruitmentInformationListAdd_section.do")
	public ModelAndView addRecruitInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		Company c = new Company();
		List<Company> result1 = this.companyService.selectList(c);
		Position p = new Position();
		List<Position> result2 = this.positionService.selectList(p);
		map.put("rs1", result1);
		map.put("rs2", result2);
		map.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		return new ModelAndView("/mobileViews/mobileAdmin/RecruitmentInformationListAdd_section", map);
	}
	
	/**
	 * @param industry
	 * @return 根据INDUSTRY 显示企业信息 by 宋浩 2015-12-26
	 */
	@RequestMapping("MobileAdmin/ajaxgetCompanyByIndustry.do")
	@ResponseBody
	public List<Company> getCompanyByIndustry(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		String industry = request.getParameter("industry");
		List<Company> result = companyService.getCompanyByIndustry(industry);
		return result;
	}
	
	/**
	 * 功能：管理员添加招聘信息 保存 注解请求地址(映射) 添加页面宋浩2015-12-26
	 * 
	 * 邢志武修改 20150118 COLLEGE_ID
	 * */
	@RequestMapping("MobileAdmin/doAddRecruitInfo.do")
	public String saveRecruitInfo(HttpServletRequest request,
			HttpSession session) throws IOException {
		/*Teacher tea = (Teacher) session.getAttribute("current_user");
		String org_id = tea.getDept_id();//获取部门ID
		String org_level = DictionaryService.findOrg(org_id).getOrg_level();//获取组织级别
		if (org_level.equals("2")) {
			org_id = tea.getDept_id();
		} else if (org_level.equals("3")) {
			org_id = DictionaryService.findOrg(tea.getDept_id()).getParent_id();
		}*/
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String com_id = request.getParameter("com_id");//企业名称
		String type1 = "1";
		String type2 = "1";
		String type3 = "1";
		String post_id = request.getParameter("post_id");//岗位名称
		String recruit_prof = request.getParameter("recruit_prof");//招聘专业
		String recruit_desc = request.getParameter("recruit_desc");//招聘描述
		Integer recruit_num = Integer.parseInt(request
				.getParameter("recruit_num"));//招聘人数
		/*String state = request.getParameter("state");*/
		String effect_time = request.getParameter("recruit_eff");//开始时间
		String end_time = request.getParameter("recruit_end");//截止时间
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(effect_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		try {
			ts2 = new Timestamp(format3.parse(end_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		RecruitInfo r = new RecruitInfo();

		r.setEffect_time(ts1);
		r.setEnd_time(ts2);
		r.setId(Common.returnUUID());
		r.setCreate_time(ts);
		r.setCom_id(com_id);
		r.setType1(type1);
		r.setType2(type2);
		r.setType3(type3);
		/*r.setCollege_id(org_id);*/
		r.setPost_id(post_id);
		r.setRecruit_prof(recruit_prof);
		r.setRecruit_desc(recruit_desc);
		r.setRecruit_num(recruit_num);
		/*r.setState(state);*/
		recruitInfoService.insert(r);

		return "redirect:RecruitmentInformationList.do"; // 添加成功后重定向到列表页面
	}
	
	/**
	 * 功能：管理员修改招聘信息 注解请求地址(映射) 宋浩2015-12-26
	 * 
	 * 
	 * 邢志武修改20150121
	 * */
	@RequestMapping("MobileAdmin/RecruitmentInformationList_section.do")
	public String editRecruitInfo(ModelMap modelMap, String id) {
		RecruitInfo r = (RecruitInfo) recruitInfoService.selectByID(id);
		Position p = new Position();
		List<Position> result2 = this.positionService.selectList(p);
		modelMap.put("r2", result2);
		modelMap.put("recruitInfo", r);
		return "/mobileViews/mobileAdmin/RecruitmentInformationList_section";
	}
	
}
