
package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import jxl.demo.Write;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.dao.LevelApprovalDao;
import com.sict.entity.Application;
import com.sict.entity.Company;
import com.sict.entity.DirectRecord;
import com.sict.entity.EvalsIndex;
import com.sict.entity.Evaluate;
import com.sict.entity.Files;
import com.sict.entity.GroupMembers;
import com.sict.entity.Groups;
import com.sict.entity.LevelApproval;
import com.sict.entity.Notice;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.RecruitInfo;
import com.sict.entity.Score;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.service.ApplicationService;
import com.sict.service.CompanyService;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateService;
import com.sict.service.FilesService;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.LevelApprovalService;
import com.sict.service.NoticeService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RecruitInfoService;
import com.sict.service.ScoreService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.DateService;

/*
 * 功能：在校生教师控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */

@Controller
public class CampusTeacherController {
	/**
	 * @author WuGee
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至首页
	 * @throws IOException
	 * @throws ServletException
	 *             2015年12月2日14:44:06
	 */

	/**
	 * 注入practiceRecordService *by丁乐晓20151224
	 */
	@Resource(name = "practiceRecordService")
	private PracticeRecordService practiceRecordService;

	/**
	 * 注入companyService byxzw20140917 *by丁乐晓20151224
	 */
	@Resource(name = "companyService")
	private CompanyService companyService;
	/**
	 * 注入recruitInfoService by贾建昶2015-12-12 *
	 */
	@Resource(name = "recruitInfoService")
	private RecruitInfoService recruitInfoService;
	/**
	 * 注入groupsService by周睿20151214 *
	 */
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	/**
	 * 注入StudentService by周睿20151214 *
	 */
	@Resource(name = "studentService")
	private StudentService studentService;
	/**
	 * 注入teacherService byjjc20160112 *
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入directRecordService by周睿 20151217 *
	 */
	@Resource(name = "directRecordService")
	private DirectRecordService directRecordService;
	/**
	 * 注入filesService by周睿 20151217 *
	 */
	@Resource(name = "filesService")
	private FilesService filesService;

	/**
	 * 注入NoticeService 张向杨 20151217 *
	 */

	@Resource(name = "noticeService")
	private NoticeService noticeService;
	/**
	 * 注入PracticeTaskService 张向杨 20151217 *
	 */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	/**
	 * 注入GroupMembersService 张向杨 20151217 *
	 */
	@Resource(name = "groupMembersService")
	private GroupMembersService groupMembersService;
	/**
	 * 注入evaluateService by周睿20151229 *
	 */
	@Resource(name = "evaluateService")
	private EvaluateService evaluateService;
	/**
	 * 注入EvalsIndexService by周睿20151229 *
	 */
	@Resource(name = "evalsIndexService")
	private EvalsIndexService evalsIndexService;
	/**
	 * 注入ScoreService by李达 20160113 *
	 */
	@Resource(name = "ScoreService")
	private ScoreService ScoreService;
	/**
	 * 注入applicationService by 师杰 20160116
	 */
	@Resource(name = "applicationService")
	private ApplicationService applicationService;
	/**
	 * 注入levelApprovalService by 师杰 20160116
	 */
	@Resource(name = "levelApprovalService")
	private LevelApprovalService levelApprovalService;

	private int pageSizeConstant = Constants.pageSize; // 获取常量分页页大小

	@RequestMapping("CampusTeacher/index.do")
	public String toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		return "/campusViews/campusTeacher/index";
	}
	/**
	 * 功能：获取请假审批列表
	 * by 师杰 20160116
	 */
	@RequestMapping("CampusTeacher/applyLeave.do")
	public ModelAndView applyLeave(HttpSession session,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		String tea_id = teacher.getId();
		//获取全部申请记录
		List<Application> application = this.applicationService.selectByTea_id(tea_id);
		//定义三个数组，分别存放未审批，已审批，未通过
		List<Application> approveing = new ArrayList<Application>();//未审批
		List<Application> approved = new ArrayList<Application>();// 已审批
		List<Application> disapprove = new ArrayList<Application>();//未通过
		for(Application ap : application){
			//设置日期格式
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		if(ap.getSla_approval_state().equalsIgnoreCase("1")){//1.审批中 2.同意  3.不同意  | 未审批1 已审批23未通过3
			ap.setTemp1(DictionaryService.findStudent(ap.getSla_stu()).getTrue_name());
			ap.setTemp2(sdf.format(ap.getSla_begin_time()));
			ap.setTemp3(sdf.format(ap.getSla_end_time()));
			approveing.add(ap);
		}else if(ap.getSla_approval_state().equalsIgnoreCase("2") || ap.getSla_approval_state().equalsIgnoreCase("3")){
			ap.setTemp1(DictionaryService.findStudent(ap.getSla_stu()).getTrue_name());
			ap.setTemp2(sdf.format(ap.getSla_begin_time()));
			ap.setTemp3(sdf.format(ap.getSla_end_time()));
			approved.add(ap);
			if(ap.getSla_approval_state().equalsIgnoreCase("3")){
				ap.setTemp1(DictionaryService.findStudent(ap.getSla_stu()).getTrue_name());
				ap.setTemp2(sdf.format(ap.getSla_begin_time()));
				ap.setTemp3(sdf.format(ap.getSla_end_time()));
				disapprove.add(ap);
			}
		}}
		
		
		
		map.put("teacher", teacher);
		map.put("application",application );
		map.put("approveing", approveing);
		map.put("approved",approved );
		map.put("disapprove",disapprove );
		return new ModelAndView("/campusViews/campusTeacher/applyLeave", map);
	}
	/**
	 * 功能：请假审批
	 * by 师杰 20160116
	 */

	@RequestMapping("CampusTeacher/applyLeavedetails.do")
	public ModelAndView applyLeavedetails(HttpSession session,HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		String id = request.getParameter("id");
		String stu_id = request.getParameter("stu_id");
		Student student =  this.studentService.selectByID(stu_id);
		String class_name = DictionaryService.findOrg(student.getClass_id()).getOrg_name();
		Application application = applicationService.selectByID(id);
		
		String sla_type =application.getSla_type();
		if(sla_type.equalsIgnoreCase("1")){
			sla_type = "事假";
		}else if(sla_type.equalsIgnoreCase("2")){
			sla_type = "病假";
		}else if(sla_type.equalsIgnoreCase("3")){
			sla_type = "旅游";
		}else if(sla_type.equalsIgnoreCase("4")){
			sla_type = "探亲";
		}else{
			sla_type = "其他";
		}
		
		String sla_rank = application.getSla_rank();
		if (sla_rank.equalsIgnoreCase("1")){
			 sla_rank = "比较着急";
		}else if(sla_rank.equalsIgnoreCase("2")){
			 sla_rank = "不是很着急";
		}else {
			 sla_rank = "不着急";
		}
		
		map.put("application", application);
		map.put("sla_rank", sla_rank);
		map.put("sla_type", sla_type);
		map.put("teacher", teacher);	
		map.put("student", student);
		map.put("class_name", class_name);
		map.put("id", id);
		return new ModelAndView("/campusViews/campusTeacher/applyLeavedetails", map);
	}
	/**
	 * 不同意的页面
	 * 没有按钮
	 * 师杰  20160116
	 * */
	@RequestMapping("CampusTeacher/applyLeavedDetails.do")
	public ModelAndView applyLeavedDetails(HttpSession session,HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		String id = request.getParameter("id");
		String stu_id = request.getParameter("stu_id");
		Student student = (Student) this.studentService.selectByID(stu_id);
		String class_name = DictionaryService.findOrg(student.getClass_id()).getOrg_name();
		Application application = applicationService.selectByID(id);
		
		String sla_type =application.getSla_type();
		if(sla_type.equalsIgnoreCase("1")){
			sla_type = "事假";
		}else if(sla_type.equalsIgnoreCase("2")){
			sla_type = "病假";
		}else if(sla_type.equalsIgnoreCase("3")){
			sla_type = "旅游";
		}else if(sla_type.equalsIgnoreCase("4")){
			sla_type = "探亲";
		}else{
			sla_type = "其他";
		}
		
		String sla_rank = application.getSla_rank();
		if (sla_rank.equalsIgnoreCase("1")){
			 sla_rank = "比较着急";
		}else if(sla_rank.equalsIgnoreCase("2")){
			 sla_rank = "不是很着急";
		}else {
			 sla_rank = "不着急";
		}
		LevelApproval la = (LevelApproval) this.levelApprovalService.selectByLevel_App_ID(id);
		String opinion = la.getApproval_opinion();
		
		map.put("application", application);
		map.put("sla_rank", sla_rank);
		map.put("sla_type", sla_type);
		map.put("teacher", teacher);	
		map.put("student", student);
		map.put("class_name", class_name);
		map.put("opinion", opinion);
		return new ModelAndView("/campusViews/campusTeacher/applyLeavedDetails", map);
	}
	/**
	 * 功能：更新审批结果
	 * by 师杰 20160116
	 */
	@RequestMapping("CampusTeacher/ajaxcheck.do")
	public String ajaxcheck(HttpServletRequest request, HttpServletResponse response,
			 HttpSession session) throws IOException {
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		request.setCharacterEncoding("UTF-8");
		String tea_id = teacher.getId();
		String i = request.getParameter("i");
		int x = Integer.parseInt(i);
		String id = request.getParameter("id");
		String opinion = request.getParameter("opinion");
		opinion = new String (opinion.getBytes("iso-8859-1"), "UTF-8") ;
		Timestamp d = new Timestamp(System.currentTimeMillis());
		
		if(x == 1){//上交
			Application apl = (Application) this.applicationService.selectByID(id);
			apl.setIs_file(i);
			applicationService.update(apl);
			response.getWriter().println(apl.toString());
		}else if(x == 2){//通过
			Application apl = (Application) this.applicationService.selectByID(id);
			apl.setFinal_approval_tea(tea_id); 
			apl.setFinal_approval_time(d);
			apl.setSla_approval_state("2");
			applicationService.update(apl);
			LevelApproval la = (LevelApproval) this.levelApprovalService.selectByLevel_App_ID(id);
			la.setApproval_tea(tea_id);
			la.setApproval_time(d);
			la.setApproval_state("2");
			la.setApproval_opinion(opinion);
			la.setIs_approval_pass("1");
			
			levelApprovalService.update(la);
			response.getWriter().println(apl.toString());
			response.getWriter().println(la.toString());
		}else if(x == 3){//不通过
			Application apl = (Application) this.applicationService.selectByID(id);
			apl.setFinal_approval_tea(tea_id); 
			apl.setFinal_approval_time(d);
			apl.setSla_approval_state("3");
			applicationService.update(apl);
			LevelApproval la = (LevelApproval) this.levelApprovalService.selectByLevel_App_ID(id);
			la.setApproval_tea(tea_id);
			la.setApproval_time(d);
			la.setApproval_state("2");
			la.setApproval_opinion(opinion);
			la.setIs_approval_pass("2");
			
			levelApprovalService.update(la);
			response.getWriter().println(apl.toString());
			response.getWriter().println(la.toString());

		}
		
	return null;
		}
	
	}
	
	
