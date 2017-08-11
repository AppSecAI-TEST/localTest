package com.sict.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.sict.entity.Application;
import com.sict.entity.Association;
import com.sict.entity.AssociationMembers;
import com.sict.entity.DailyInspect;
import com.sict.entity.DailyInspectDetails;
import com.sict.entity.EvalsIndex;
import com.sict.entity.EvaluateStandard;
import com.sict.entity.LevelApproval;
import com.sict.entity.Org;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.service.ApplicationService;
import com.sict.service.AssociationMembersService;
import com.sict.service.AssociationService;
import com.sict.service.DailyInspectDetailsService;
import com.sict.service.DailyInspectService;
import com.sict.service.DictionaryService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateStandardService;
import com.sict.service.LevelApprovalService;
import com.sict.service.OrgService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.util.Common;
import com.sict.util.Constants;

/*
 * 功能：在校生学生控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class CampusStudentController {
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
	 * 注入studentService by周睿20160113 *
	 */
	@Resource(name = "studentService")
	private StudentService studentService;

	/**
	 * 注入LevelApprovalService 师杰 20160119
	 * 
	 */
	@Resource(name = "levelApprovalService")
	private LevelApprovalService levelApprovalService;

	/**
	 * 注入applicationService by 苏衍静20160115
	 */
	@Resource(name = "applicationService")
	private ApplicationService applicationService;
	/**
	 * 注入orgService by 苏衍静20160120
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * 注入teacherService by 苏衍静20160120
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入DailyInspectService by 李达20160123
	 */
	@Resource(name = "dailyInspectService")
	private DailyInspectService dailyInspectService;
	/**
	 * 注入evaluateStandardService by 李达20160301
	 */
	@Resource(name = "evaluateStandardService")
	private EvaluateStandardService evaluateStandardService;
	/**
	 * 注入dailyInspectDetailsService by 李达20160303
	 */
	@Resource(name = "dailyInspectDetailsService")
	private DailyInspectDetailsService dailyInspectDetailsService;
	/**
	 * 注入associationService by 李达20160329
	 */
	@Resource(name = "associationService")
	private AssociationService associationService;
	/**
	 * 注入associationMembersService by 李达20160329
	 */
	@Resource(name = "associationMembersService")
	private AssociationMembersService associationMembersService;
	/**
	 * 注入evalsIndexService by 李达20160329
	 */
	@Resource(name = "evalsIndexService")
	private EvalsIndexService evalsIndexService;

	@RequestMapping("campusStudent/index.do")
	public String toindex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/campusViews/campusStudent/index";
	}

	/**
	 * 功能：学生在校管理--显示学生的请假申请(主页面) 20160115 syj
	 * 
	 */
	private int pageSizeConstant = Constants.pageSize; // 获取常量分页页大小

	@RequestMapping("campusStudent/MyApplication.do")
	public String toMyApplication(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");// 获得当前的学生用户
		Application a = new Application(); // 新建一个申请
		a.setSla_stu(Common.getOneStuId(session));
		List<Application> appList = this.applicationService.selectList(a);// 得到自己发布的申请

		// 分页
		int pageSize = pageSizeConstant;
		int Currentpage = 1;

		// 获取当前页集合
		List<Application> listCurrentPage = Common.getListCurrentPage(appList,
				pageSize, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(appList, pageSize, Currentpage);

		modelMap.put("SelfCount", pageCount);
		modelMap.put("SelfnowPage", Currentpage);

		List<Application> passApplicationList = new ArrayList<Application>();// 同意之后的集合（已审批）
		List<Application> failApplicationList = new ArrayList<Application>();// 不同意之后的集合（已审批）
		List<Application> noApplicationList = new ArrayList<Application>(); // 未审批的集合
		for (Application k : appList) {
			if (k.getSla_type().equalsIgnoreCase("1")) {
				k.setSla_type("事假");
			} else if (k.getSla_type().equalsIgnoreCase("2")) {
				k.setSla_type("病假");
			} else if (k.getSla_type().equalsIgnoreCase("3")) {
				k.setSla_type("探亲");
			} else if (k.getSla_type().equalsIgnoreCase("4")) {
				k.setSla_type("旅游");
			} else {
				k.setSla_type("其他");
			}

			if (k.getSla_approval_state().equals("1")) {
				k.setSla_approval_state("未审核");
				noApplicationList.add(k);

			} else if (k.getSla_approval_state().equals("2")) {
				k.setSla_approval_state("同意");
				passApplicationList.add(k);

			} else {
				k.setSla_approval_state("未同意");
				failApplicationList.add(k);
			}
		}
		modelMap.put("passApplicationList", passApplicationList);
		modelMap.put("failApplicationList", failApplicationList);
		modelMap.put("noApplicationList", noApplicationList);

		return "/campusViews/campusStudent/MyApplication";
	}

	/**
	 * 
	 * 功能：跳转至请假申请页面
	 * 
	 * @author syj 20160119
	 * 
	 */
	@RequestMapping("campusStudent/ApplyForLeave.do")
	public ModelAndView toApplyForLeave(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");// 获得当前的学生用户
		Org org = orgService.selectByID(stu.getClass_id());// 通过学生的班级的id（即组织表的id）得到一条组织表中的记录

		String head_tea_id = org.getHead_tea_id(); // 通过得到的组织表中的记录得到班主任的id
		String counselor_id = org.getCounselor_id(); // 通过得到的组织表中的记录得到导员的id

		Teacher Head_tea = (Teacher) teacherService.selectByID(head_tea_id); // 通过班主任id得到班主任相对应的记录
		Teacher counselor = (Teacher) teacherService.selectByID(counselor_id);// 通过班主任id得到班主任相对应的记录

		String teacherName = Head_tea.getTrue_name(); // 班主任的姓名
		String teaId = Head_tea.getId(); // 获得班主任的id

		String conselorName = counselor.getTrue_name(); // 班主任的姓名
		String counselorId = counselor.getId(); // 获得班主任的id

		map.put("teacherName", teacherName);
		map.put("teaId", teaId);
		map.put("conselorName", conselorName);
		map.put("counselorId", counselorId);
		map.put("Head_tea", Head_tea);
		return new ModelAndView("/campusViews/campusStudent/ApplyForLeave", map);
	}

	/**
	 * 功能：学生在校管理--查看已提交的请假申请表详情）
	 * 
	 * @author syj 20160116
	 * 
	 */
	@RequestMapping("campusStudent/ApplyForLeavee.do")
	public ModelAndView toApplyForLeavee(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		Application a = (Application) applicationService.selectByID(id);// 通过id得到申请的相关记录
		LevelApproval la = (LevelApproval) this.levelApprovalService
				.selectByLevel_App_ID(id);// 通过id得到审批的相关记录

		if (a.getSla_type().equalsIgnoreCase("1")) {
			a.setSla_type("事假");
		} else if (a.getSla_type().equalsIgnoreCase("2")) {
			a.setSla_type("病假");
		} else if (a.getSla_type().equalsIgnoreCase("3")) {
			a.setSla_type("探亲");
		} else if (a.getSla_type().equalsIgnoreCase("4")) {
			a.setSla_type("旅游");
		} else {
			a.setSla_type("其他");
		}

		if (a.getSla_approval_state().equals("1")) {
			a.setSla_approval_state("未审核");
		} else if (a.getSla_approval_state().equals("2")) {
			a.setSla_approval_state("同意");
		} else {
			a.setSla_approval_state("未同意");
		}

		if (a.getSla_rank().equals("1")) {
			a.setSla_rank("比较着急");
		} else if (a.getSla_approval_state().equals("2")) {
			a.setSla_rank("不是很着急");
		} else {
			a.setSla_rank("不着急");
		}

		String teaId = a.getSla_approval_tea(); // 获得老师的id（根据申请人id）
		String teaName = DictionaryService.findTeacher(teaId).getTrue_name();// 根据老师ID获取教师姓名
		String approval_opinion = la.getApproval_opinion(); // 获得审批人的意见

		modelMap.put("a", a);
		modelMap.put("teaName", teaName);
		modelMap.put("approval_opinion", approval_opinion);
		return new ModelAndView("/campusViews/campusStudent/ApplyForLeavee",
				map);
	}

	/**
	 * 
	 * 功能：学生在校管理---已提交的请假申请表的删除
	 * 
	 * @author syj 20160117
	 * 
	 */
	@RequestMapping("campusStudent/deleteApplication.do")
	public String deleteApplication(HttpServletRequest request) {
		String id = request.getParameter("id");
		Application a = DictionaryService.findLevelApplication(id);
		a.setId(id);
		applicationService.delete(a);
		applicationService.update(a);
		return "redirect:MyApplication.do"; // 撤销成功后重定向到列表页面
	}

	/**
	 * 
	 * 功能:学生在校管理----添加新的请假申请
	 * 
	 * @author syj 20160119
	 * 
	 */
	@RequestMapping("campusStudent/addApplication.do")
	public String addApplication(HttpServletRequest request, HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");// 获得当前的用户
		Application appli = new Application(); // 新建一个申请

		appli.setSla_stu(stu.getId());
		appli.setSla_type(request.getParameter("sla_type"));
		appli.setSla_rank(request.getParameter("sla_rank"));
		appli.setIs_level_school(request.getParameter("is_level_school"));
		appli.setSla_approval_tea(request.getParameter("sla_approval_tea"));
		appli.setSla_place(request.getParameter("sla_place"));
		appli.setSla_reason_desc(request.getParameter("sla_reason_desc"));
		appli.setId(Common.returnUUID());// 随机获得一个ID
		appli.setSla_duration(request.getParameter("sla_duration"));
		appli.setSla_approval_state("1");
		appli.setIs_file("2");

		String sla_begin_time = request.getParameter("sla_begin_time");// 请假开始时间的转化
		String dateBegin = request.getParameter("dateBegin");
		String beginTime = sla_begin_time + " " + dateBegin;
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(beginTime).getTime());// 时间类型可以封装
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		appli.setSla_begin_time(ts);

		String sla_end_time = request.getParameter("sla_end_time");// 请假结束时间的转化
		String dateEnd = request.getParameter("dateEnd");
		String endTime = sla_end_time + " " + dateEnd;

		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
		Timestamp tt = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(endTime).getTime());// 时间类型可以封装
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		appli.setSla_end_time(tt);
		applicationService.insert(appli);

		LevelApproval la = new LevelApproval();
		la.setId(Common.returnUUID());// 随机获得一个ID
		la.setLevel_application_id(appli.getId());
		la.setApproval_tea(appli.getSla_approval_tea());
		la.setApproval_time(appli.getSla_begin_time());
		la.setApproval_state("1");
		levelApprovalService.insert(la);
		return "redirect:MyApplication.do"; // 撤销成功后重定向到列表页面
	}

	/**
	 * 功能：早操记录管理首页 by 李达、师杰 时间：20160301 1/6 START
	 */
	@RequestMapping("campusStudent/morningExercises.do")
	public ModelAndView morningExercises(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");// 获取当前登录的学生
		String stu_id = stu.getId();// 获取学生id
		String type = "1";// 设置当前默认类型为1:'早操'
		List<DailyInspect> dai = this.dailyInspectService
				.getDailyInspectByStuIdAndType(stu_id, type);// 根据学生id和类型获取此人全部早操记录,且先显示时间近的
		List<DailyInspect> dail = new ArrayList<DailyInspect>();
		Date dd = new Date();
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		for (DailyInspect d : dai) {// 遍历集合dai
			String sdi_class_id = d.getSdi_class_id();// 获取班级id
			String Time = ss.format(d.getInspect_time());// 格式化检查时间
			String class_name = DictionaryService.findOrg(sdi_class_id)
					.getOrg_name();// 获取班级名
			d.setTemp1(class_name);// 将班级名储存到temp1中
			d.setTemp2(Time);// 将检查时间名储存到temp2中
			dail.add(d);// 将更改后记录添加到新的集合
		}
		map.put("stu", stu);
		map.put("dail", dail);
		return new ModelAndView("/campusViews/campusStudent/morningExercises",
				map);
	}

	/* 25 */
	/**
	 * 功能：添加早操检查记录 by 李达、师杰 时间： 20160301 2/6
	 */
	@RequestMapping("campusStudent/morningExercisesDetailsAdd.do")
	public ModelAndView morningExercisesDetailsAdd(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = "早操";
		List<EvaluateStandard> e = this.evaluateStandardService
				.selectByType(type);// 根据类型"早操"获取相关指标
		String id1 = Common.returnUUID();// 预设检查表的随机id
		map.put("id1", id1);
		map.put("e", e);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesDetailsAdd", map);
	}

	/**
	 * 功能：ajax查询 by 李达 、师杰 时间：20160301
	 * */
	@RequestMapping("campusStudent/ajaxGetNext.do")
	@ResponseBody
	public String ajaxGetNext(HttpSession session, HttpServletRequest request,
			ModelMap modelMap, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String id = request.getParameter("standard");// 获取指标id
		EvaluateStandard e = (EvaluateStandard) evaluateStandardService
				.selectByID(id);// 根据id查询相应指标
		String inspect_id = e.getScope();// 查询标准适用范围
		List<Association> as = this.associationService
				.selectByParentId(inspect_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<div data-role='select' class='card' id='div2'>");
		sb.append("<select id='xibu' onchange='getMember();'>");
		sb.append("<option>请选择系部</option>");
		for (Association r : as) {
			sb.append("<option value='" + r.getId() + "'>" + r.getSa_name()
					+ "</option>");
		}
		sb.append("</select></div>");
		List<Org> a = orgService.selectClassByXyId(inspect_id);// 根据适用范围查询下属班级
		ArrayList<Org> classlist=new ArrayList<Org>();//存放筛选后的年级信息
		sb.append("<div data-role='select' class='card' id='div4'>");
		sb.append("<select id='class1'>");
		sb.append("<option>请选择班级</option>");
		Calendar now = Calendar.getInstance();
		int nowyear=now.get(Calendar.YEAR);//获取系统时间的年份
		int nowmonth=now.get(Calendar.MONTH)+1;//获取系统时间的月份
		for (Org b : a) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(b.getBegin_time());
			int year=cal.get(Calendar.YEAR);//获取班级创建时间的年份
			if((year==nowyear||year==(nowyear-1)||(year==(nowyear-2)&&nowmonth!=9))){//筛选出需要跑操的年级
				classlist.add(b);
			}
		}
		for(Org c:classlist){
			sb.append("<option value='" + c.getId() + "'>" + c.getOrg_name()
					+ "</option>");
		}
		sb.append("</select></div>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax查询 by 李达 、师杰 20160302
	 * */
	@RequestMapping("campusStudent/ajaxGetMember.do")
	@ResponseBody
	public String ajaxGetMember(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String xibu = request.getParameter("xibu");// 获取学生会部门id
		List<AssociationMembers> asso = associationMembersService
				.selectStusByAssId(xibu);
		List<Student> stus = new ArrayList<Student>();
		for (AssociationMembers a : asso) {
			String stu_id = a.getSam_stu_id();
			Student s = studentService.selectByID1(stu_id);
			stus.add(s);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div data-role='select' class='card' id='div3'>");
		sb.append("<select id='cm'>");
		sb.append("<option>请选择检察人员</option>");
		for (Student stu : stus) {
			sb.append("<option value='" + stu.getId() + "'>"
					+ stu.getTrue_name() + "</option>");
		}
		sb.append("</select></div>");

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：保存早操检查记录 by 李达 、师杰 时间：20160302
	 * 
	 * */
	@RequestMapping("campusStudent/doSaveMoningrExRecord.do")
	public String doSaveMoningrExRecord(HttpServletRequest request) {
		String last_grade = request.getParameter("last_grade");
		double sum_grade = Double.valueOf(last_grade).doubleValue();
		String id1 = request.getParameter("id1");
		String sYear = request.getParameter("sYear");
		String sTerm = request.getParameter("sTerm");
		String week = request.getParameter("week");
		String standard = request.getParameter("standard");
		String xibu = request.getParameter("xibu");
		String class1 = request.getParameter("class1");
		String sWeek = request.getParameter("sWeek");
		String time1 = request.getParameter("time1");
		String time2 = time1 + " 00:00:00";
		Timestamp time = Timestamp.valueOf(time2);
		String cm = request.getParameter("cm");
		EvaluateStandard e = (EvaluateStandard) evaluateStandardService
				.selectByID(standard);// 根据id查询相应指标
		String collegeId = e.getScope();// 查询标准适用范围
		DailyInspect s = (DailyInspect) dailyInspectService.selectByID(id1);
		if (s == null) {
			DailyInspect d = new DailyInspect();
			d.setId(id1);
			d.setInspect_type("1");
			d.setSdi_college_id(collegeId);// 学院id
			d.setSdi_year(sYear);
			d.setSdi_semester(sTerm);
			d.setSdi_week(week);
			d.setSdi_weeks_num(sWeek);
			d.setSdi_index_id(standard);
			d.setStu_union_section(xibu);
			d.setSdi_class_id(class1);
			d.setInspect_person(cm);
			d.setInput_person(cm);// 录入人当前默认检查人
			d.setInspect_time(time);
			d.setSum_grade(sum_grade);
			dailyInspectService.insert(d);
		}
		return "redirect:morningExercises.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：添加扣分项主页 by 李达、师杰 时间：20160301 3/6
	 */
	@RequestMapping("campusStudent/morningExercisesAddPoints.do")
	public ModelAndView morningExercisesAddPoints(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id1 = request.getParameter("id1");
		String sYear = request.getParameter("sYear");
		String sTerm = request.getParameter("sTerm");
		String week = request.getParameter("week");
		String cm = request.getParameter("cm");
		String standard = request.getParameter("standard");
		EvaluateStandard e = (EvaluateStandard) evaluateStandardService
				.selectByID(standard);// 根据id查询相应指标
		String standard_name = e.getStandard_name();
		String xibu = request.getParameter("xibu");
		String class1 = request.getParameter("class1");
		String class_name = DictionaryService.findOrg(class1).getOrg_name();// 获取班级名
		String sWeek = request.getParameter("sWeek");
		String time1 = request.getParameter("time");
		List<DailyInspectDetails> dai = dailyInspectDetailsService
				.selectByInspect_id(id1); // 根据此id查询相关联的所有详情表
		if (dai.size() > 0) {
			double x = 0;
			double last_grade = 0.0;
			for (DailyInspectDetails d : dai) {
				x = x + d.getGrade();
				EvalsIndex reason = (EvalsIndex) this.evalsIndexService
						.selectByID(d.getIndex_id());
				String reasons = reason.getIndex_name();
				d.setTemp1(reasons);
			}
			last_grade = 10.0 + x;// 默认总分为10分 视实际情况更改
			if(last_grade<=0){
				last_grade=0.0;
			}
			map.put("last_grade", last_grade);
			map.put("dai", dai);
		} else {
			List<DailyInspectDetails> dail = new ArrayList<DailyInspectDetails>();
			DailyInspectDetails d = new DailyInspectDetails();
			d.setGrade(0.0);
			d.setTemp1("暂无记录");
			dail.add(d);
			map.put("dai", dail);
			map.put("last_grade", 10);
		}
		String datasend = "id1=" + id1 + "&sYear=" + sYear + "&sTerm=" + sTerm
				+ "&week=" + week + "&standard=" + standard + "&xibu=" + xibu
				+ "&sWeek=" + sWeek + "&time1=" + time1 + "&cm=" + cm
				+ "&class1=" + class1;
		map.put("id1", id1);
		map.put("sYear", sYear);
		map.put("sTerm", sTerm);
		map.put("week", week);
		map.put("standard", standard);
		map.put("standard_name", standard_name);
		map.put("xibu", xibu);
		map.put("sWeek", sWeek);
		map.put("time1", time1);
		map.put("cm", cm);
		map.put("class_name", class_name);
		map.put("class1", class1);
		map.put("datasend", datasend);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesAddPoints", map);
	}

	/**
	 * 功能：添加早操扣分详请 by 李达、师杰 时间：20160301 4/6
	 */
	@RequestMapping("campusStudent/morningExercisesAddPointsDetailsAdd.do")
	public ModelAndView morningExercisesAddPointsDetailsAdd(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id1 = request.getParameter("id1");
		String sYear = request.getParameter("sYear");
		String sTerm = request.getParameter("sTerm");
		String week = request.getParameter("week");
		String standard = request.getParameter("standard");
		String xibu = request.getParameter("xibu");
		String class1 = request.getParameter("class1");
		String sWeek = request.getParameter("sWeek");
		String time1 = request.getParameter("time1");
		String cm = request.getParameter("cm");
		String twice = Common.returnUUID();
		Org o = orgService.selectByID(class1);
		List<Student> stus = studentService.getStudentsByClassId(class1); // 所检查班级的所有人
		List<EvalsIndex> evi = evalsIndexService.selectByStandId(standard);
		map.put("twice", twice);
		map.put("id1", id1);
		map.put("sYear", sYear);
		map.put("sTerm", sTerm);
		map.put("week", week);
		map.put("standard", standard);
		map.put("xibu", xibu);
		map.put("sWeek", sWeek);
		map.put("time1", time1);
		map.put("class1", class1);
		map.put("cm", cm);
		map.put("o", o);
		map.put("evi", evi);
		map.put("stus", stus);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesAddPointsDetailsAdd",
				map);
	}

	/**
	 * 功能：扣分详情提交保存验证 by 李达 、师杰 时间：20160303
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("campusStudent/doCheckMessaSubmit.do")
	public String doCheckMessaSubmit(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		// String twice = request.getParameter("twice");//自动生成ID
		String inspect_id = request.getParameter("inspect_id");// 检查ID
		String points_reasons = request.getParameter("points_reasons");
		String scope = request.getParameter("influence");// 作用对象类型 1.班级 3.宿舍
															// 2.个人
		String n = request.getParameter("num");// 人数
		String sYear = request.getParameter("sYear");// 学年
		String sTerm = request.getParameter("sTerm");// 学期
		String week = request.getParameter("week");// 星期
		String standard = request.getParameter("standard");// 标准
		String xibu = request.getParameter("xibu");// 学生会部门
		String class1 = request.getParameter("class1");// 班级
		String sWeek = request.getParameter("sWeek");//
		String time1 = request.getParameter("time1");//
		String cm = request.getParameter("cm");//
		EvalsIndex num0 = (EvalsIndex) evalsIndexService
				.selectByID(points_reasons);
		int num = Integer.parseInt(n);
		double nums = (double) num;
		double num2 = num0.getScore();
		double num3 = num2 * nums;// 求出扣的总分数
		String student = request.getParameter("a");
		String[] studentList = student.split(",");
		DailyInspectDetails da = new DailyInspectDetails();
		int scope2 = Integer.parseInt(scope);
		for (int i = 0; i < studentList.length; i++) {
			
			if (scope2 == 2) {
				da.setOccur_num(1);
				da.setGrade(num2);
				da.setInspect_object_id(studentList[i]);
			}
			if (scope2 == 1) {
				da.setSdid_class_id(class1);
				da.setOccur_num(num);
				da.setGrade(num3);
			}
			da.setId(Common.returnUUID());
			da.setIndex_id(points_reasons);
			da.setInspect_id(inspect_id);
			da.setAffect_object(scope);
			dailyInspectDetailsService.insert(da);
		}
		modelMap.put("sTerm", sTerm);
		modelMap.put("sYear", sYear);
		modelMap.put("week", week);
		modelMap.put("standard", standard);
		modelMap.put("xibu", xibu);
		modelMap.put("class1", class1);
		modelMap.put("cm", cm);
		modelMap.put("time", time1);
		modelMap.put("id1", inspect_id);
		modelMap.put("sWeek", sWeek);
		return "redirect:morningExercisesAddPoints.do";
	}

	/**
	 * 功能：查看早操记录管理详情 by 李达、师杰 时间：20160301 5/6
	 */
	@RequestMapping("campusStudent/morningExercisesDetails.do")
	public ModelAndView morningExercisesDetails(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id"); // 从检查首页获取到检查表id
		DailyInspect d = (DailyInspect) dailyInspectService.selectByID(id);// 根据id查询到此检查表
		String sdi_index_id = d.getSdi_index_id();// 获取评分标准id
		EvaluateStandard e = (EvaluateStandard) evaluateStandardService
				.selectByID(sdi_index_id);// 根据指标 id查询指标表
		String standard_name = e.getStandard_name();// 获取指标名
		Timestamp t = d.getInspect_time();
		String time = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			time = sdf.format(t);// 格式化时间
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		String Inspect_person_id = d.getInspect_person();
		Student inspect_person_stu = (Student) studentService
				.selectByID1(Inspect_person_id);
		String inspect_person = inspect_person_stu.getTrue_name();
		String Sdi_class_id = d.getSdi_class_id();
		Org class1 = (Org) orgService.selectByID(Sdi_class_id);
		String class_name = class1.getOrg_name();
		map.put("d", d);
		map.put("time", time);
		map.put("class_name", class_name);
		map.put("standard_name", standard_name);
		map.put("inspect_person", inspect_person);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesDetails", map);
	}

	/**
	 * 功能：查看早操扣分详情 by 李达、师杰 时间： 20160301 6/6 END
	 */
	@RequestMapping("campusStudent/morningExercisesAddPointsDetails.do")
	public ModelAndView morningExercisesAddPointsDetails(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id"); // 从检查详情页获取到检查表id
		List<DailyInspectDetails> dais = dailyInspectDetailsService
				.groupByIndexId(id);

		String class_id = "";
		String object_id = "";
		String stus_name = "";
		for (DailyInspectDetails d : dais) {
			class_id = d.getSdid_class_id();
			object_id = d.getInspect_object_id();
			EvalsIndex e = (EvalsIndex) evalsIndexService.selectByID(d
					.getIndex_id());
			String points_reasons = e.getIndex_name();
			d.setTemp2(points_reasons);
			if (class_id != null) {
				Org classI = orgService.selectByID(class_id);
				String class_name = classI.getOrg_name();
				d.setTemp1(class_name);
			}
			if (object_id != null) {
				String[] result = object_id.split(",");
				for (int i = 0; i < result.length; i++) {
					String stu_id = result[i];
					Student stu = studentService.selectByID1(stu_id);
					if (stu != null) {
						String stu_name = stu.getTrue_name();
						stus_name = stus_name + stu_name + ",";
						d.setTemp1(stus_name);
					}
				}

			}
		}
		map.put("dais", dais);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesAddPointsDetails",
				map);
	}

	@RequestMapping("campusStudent/morningExercisesAddPointsDetails2.do")
	public ModelAndView morningExercisesAddPointsDetails2(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		DailyInspectDetails d = (DailyInspectDetails) dailyInspectDetailsService
				.selectByID(id);
		String class_id = d.getSdid_class_id();
		String object_id = d.getInspect_object_id();
		String stus_name = "";
		if (class_id != null) {
			Org classI = orgService.selectByID(class_id);
			String class_name = classI.getOrg_name();
			d.setTemp1(class_name);
		} else if (object_id != null) {
			String[] result = object_id.split(",");
			for (int i = 0; i < result.length; i++) {
				String stu_id = result[i];
				Student stu = studentService.selectByID1(stu_id);
				String stu_name = stu.getTrue_name();
				stus_name = stus_name + stu_name + ",";
				d.setTemp1(stus_name);
			}
		}
		String index_id = d.getIndex_id();
		EvalsIndex e = (EvalsIndex) evalsIndexService.selectByID(index_id);
		String points_reasons = e.getIndex_name();
		String temp1 = d.getTemp1();
		double grade = d.getGrade();
		int occur_num = d.getOccur_num();
		map.put("points_reasons", points_reasons);
		map.put("temp1", temp1);
		map.put("grade", grade);
		map.put("occur_num", occur_num);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesAddPointsDetails2",
				map);
	}

	/**
	 * 功能：xiayibu by 李达 、师杰 时间：20160329
	 * 
	 * */
	@RequestMapping("campusStudent/doNextToSave.do")
	public ModelAndView doNextToSave(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id1 = request.getParameter("id1");
		String sYear = request.getParameter("sYear");
		String sTerm = request.getParameter("sTerm");
		String week = request.getParameter("week");
		String standard = request.getParameter("standard");
		String xibu = request.getParameter("xibu");
		String class1 = request.getParameter("class1");
		String sWeek = request.getParameter("sWeek");
		String time1 = request.getParameter("time1");
		String cm = request.getParameter("cm");
		String twice = Common.returnUUID();
		String points_reasons = request.getParameter("points_reasons");
		String influence = request.getParameter("scope");
		if (influence.equalsIgnoreCase("1")) {
			Org o = orgService.selectByID(class1);
			map.put("o", o);
			map.put("x", 1);
		} else if (influence.equalsIgnoreCase("2")) {
			List<Student> stus = studentService.getStudentsByClassId(class1); // 所检查班级的所有人
			map.put("stus", stus);
			map.put("x", 2);
		}
		List<EvalsIndex> evi = evalsIndexService.selectByStandId(standard);
		map.put("twice", twice);
		map.put("id1", id1);
		map.put("sYear", sYear);
		map.put("sTerm", sTerm);
		map.put("week", week);
		map.put("standard", standard);
		map.put("xibu", xibu);
		map.put("sWeek", sWeek);
		map.put("time1", time1);
		map.put("class1", class1);
		map.put("cm", cm);
		map.put("evi", evi);
		map.put("points_reasons", points_reasons);
		map.put("influence", influence);
		return new ModelAndView(
				"/campusViews/campusStudent/morningExercisesAddPointsDetailsAdd2",
				map);
	}
}
