package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.DirectRecord;
import com.sict.entity.EchartSeries;
import com.sict.entity.Files;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.Param;
import com.sict.entity.PracticeTask;
import com.sict.entity.ReportLogStateTreated;
import com.sict.entity.ReportTrainDetail;
import com.sict.entity.StuGraStateCount;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.ExcelService;
import com.sict.service.FilesService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.ParamService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RegionalDistributionService;
import com.sict.service.ReportLogStateService;
import com.sict.service.ScoreService;
import com.sict.service.SignService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.service.TrainDetailService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.DateService;
import com.sict.util.Ichartjs_Color;
import com.sict.util.ListSort;

/*
 * 功能：领导控制器
 * 使用 @Controller 注释
 * by郑春光20140910	 * 
 * 
 * */
@Controller
public class LeaderController {

	/*
	 * 注入teacherService 郑春光20140910 *
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入ScoreService by邢志武20150405 *
	 * 
	 * */
	@Resource(name = "ScoreService")
	private ScoreService scoreService;

	@Resource(name = "filesService")
	private FilesService filesService;
	/**
	 * 注入noticeService bywtt2014 1130 *
	 * 
	 * */
	@Resource(name = "noticeService")
	private NoticeService noticeService;
	/**
	 * 注入directRecordService bY xzw 2015-03-23 *
	 */
	@Resource(name = "directRecordService")
	private DirectRecordService directRecordService;
	/**
	 * /** 注入trainDetailService by张超 2015/1/29 *
	 * 
	 * */
	@Resource(name = "trainDetailService")
	private TrainDetailService trainDetailService;
	
	@Resource(name = "signService")
	private SignService signService;
	/**
	 * 注入studentService
	 * 
	 * @author zcg 2015-2-3
	 */
	@Resource(name = "studentService")
	private StudentService studentService;

	/**
	 * 注入orgService
	 * 
	 * @author zcg 2015-2-3
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	@Resource(name = "regionalDistributionService")
	private RegionalDistributionService regionalDistributionService;
	/**
	 * 注入practiceTaskService by邢志武20150401 *
	 * 
	 * */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	/**
	 * 注入practiceTaskService by邢志武20150401 *
	 * 
	 * */
	@Resource(name = "reportLogStateService")
	private ReportLogStateService reportLogStateService;

	@Resource(name = "excelService")
	private ExcelService excelService;
	/**
	 * 注入ParamService by周睿20160419 *
	 * 
	 * */
	@Resource(name = "paramService")
	private ParamService paramService;

	/*
	 * 领导首页 郑春光20140910 by zcg 2015-2-3
	 */
	@RequestMapping("leader/index.do")
	public String index(String current_role_selected, HttpSession session,
			ModelMap modelMap) throws IOException {
		if (current_role_selected != null) {
			session.setAttribute("current_role_selected", current_role_selected);
		}
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(college_id).getOrg_level();
		if (!org_level.equals("1")) {
			return "redirect:index2.do"; // 添加成功后重定向到列表页面
		} else {
			String grade = Common.getDefaultYear();// String.valueOf(defaultyear);
			modelMap.put("grade", grade);
			return "leader/index";
		}
	}

	/**
	 * 功能：修改个人信息 wjg2015-12-30
	 * 
	 * */
	@RequestMapping("leader/editmyInfo.do")
	public ModelAndView editmyInfo(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		List<Teacher> teaList = this.teacherService
				.selectListByTeaCode(tea_code);// selectByCode(tea_code);
		map.put("teaList", teaList);
		return new ModelAndView("leader/editmyInfo", map);
	}

	/**
	 * 功能：修改个人信息 吴敬国2015-12-30
	 * 
	 * */

	@RequestMapping("leader/doeditmyInfo.do")
	public String editTeacher1(HttpSession session, HttpServletRequest request)
			throws IOException {
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		String phone = request.getParameter("phone");
		String expertise = request.getParameter("expertise");
		teacher.setPhone(phone);
		teacher.setExpertise(expertise);
		teacherService.update(teacher);
		return "redirect:index.do"; // 添加成功后重定向到列表页面
	}

	@RequestMapping("leader/myInfo.do")
	public ModelAndView myInfo(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());// selectByCode(tea_code);
		map.put("List", teaList);
		return new ModelAndView("leader/index", map);

	}

	/**
	 * 功能：修改密码 吴敬国2015-12-30
	 * */

	@RequestMapping("leader/passwordEdit.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		List<Teacher> teaList = this.teacherService
				.selectListByTeaCode(tea_code);// selectByCode(tea_code);
		map.put("teaList", teaList);
		return new ModelAndView("leader/passwordEdit", map);

	}

	/**
	 * 功能：修改密码 吴敬国2015-12-30
	 * 
	 * */

	@RequestMapping("leader/doPasswordEdit.do")
	public String doPasswordEdit(@ModelAttribute("teacher") Teacher teacher)
			throws IOException {
		teacherService.update(teacher);
		return "redirect:../login.jsp"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：修改密码 注解请求地址(映射) 郑春光20140910
	 * 
	 * */

	@RequestMapping("leader/password.do")
	public ModelAndView password(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");

		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());
		map.put("List", teaList);
		return new ModelAndView("leader/index", map);

	}

	/**
	 * 功能： 返回选择报表内容的jsp页面 校级领导看到实训安排报表 by 张超 2015年1月29日
	 * 
	 * @return
	 */
	@RequestMapping("leader/getTrainDetailReport.do")
	public ModelAndView getTrainDetailReport() {
		Map<String, Object> map = new HashMap<String, Object>();
		Org org = new Org();
		org.setOrg_level("2");
		org.setState("1");
		List<Org> orgList = orgService.selectList(org);
		map.put("orgList", orgList);
		return new ModelAndView("leader/trainDetailReport", map);
	}

	/**
	 * 功能： 获取报表数据并显示在报表中 实训安排报表 by 张超 2015年1月29日 吴敬国 2015-6-8
	 * 
	 * @return
	 */
	@RequestMapping(value = "leader/doGetTrainDetailReport.do", method = RequestMethod.POST)
	public ModelAndView doA_TrainDetail(ModelAndView modelAndView,
			HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String term = request.getParameter("term");
		String org_id = request.getParameter("org_id");
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");

		Map<String, String> map = new HashMap<String, String>();
		map.put("term", term);
		map.put("org_id", org_id);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		// parameterMap 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getReportData(map);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		model.put("yearAndTerm", yearAndTerm);
		model.put("data", gradu);
		model.put("report_title", report_title);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("trainDetail_adminAndLeader", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能： 获取报表数据并显示在报表中 实训安排报表 excel by 张超 2015年1月29日 吴敬国 2015-6-8
	 * 
	 * @return
	 */
	@RequestMapping(value = "leader/doGetTrainDetailReportExcel.do", method = RequestMethod.POST)
	public ModelAndView doA_TrainDetail_Excel(ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String term = request.getParameter("term");
		String org_id = request.getParameter("org_id");
		String year = request.getParameter("year");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");

		Map<String, String> map = new HashMap<String, String>();
		map.put("term", term);
		map.put("org_id", org_id);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		// parameterMap 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getReportData(map);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		model.put("yearAndTerm", yearAndTerm);
		model.put("data", gradu);
		model.put("report_title", report_title);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		response.setContentType("APPLICATION/vnd.ms-excel");
		modelAndView = new ModelAndView("trainDetail_adminAndLeader_excel",
				model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能： 返回选择报表内容的jsp页面 院级领导看到实训安排报表 by 张超 2015年1月29日
	 * 
	 * @return
	 */
	@RequestMapping("leader/getCollegeTrainDetailReport.do")
	public ModelAndView getCollegeTrainDetailReport(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String collegeLeaderCollegeId = (String) session
				.getAttribute("collegeLeaderCollegeId");
		List<Org> departmentlist;
		departmentlist = orgService
				.getOrgDeptByCollegeId(collegeLeaderCollegeId);// 获取系及学院自身
		map.put("departmentlist", departmentlist);
		return new ModelAndView("leader/collegetrainDetailReport", map);
	}

	/**
	 * 实训安排报表管理 . 学院领导
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "leader/collegeGetTrainDetail.do", method = RequestMethod.POST)
	public ModelAndView collegeGetTrainDetail(ModelAndView modelAndView,
			HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String org_id = request.getParameter("org_id");// 部门id
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		String term = request.getParameter("term");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");

		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getOrgReportData(
				org_id, term, begin_time, end_time);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		dataAndReportTitle.put("yearAndTerm", yearAndTerm);
		dataAndReportTitle.put("data", gradu);
		dataAndReportTitle.put("report_title", report_title);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("trainDetail_adminAndLeader",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 实训安排报表管理，导出ecxel. 学院领导
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "leader/collegedoGetTrainDetailExcel.do", method = RequestMethod.POST)
	public ModelAndView CollegedoGetTrainDetailExcel(ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String org_id = request.getParameter("org_id");
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		String term = request.getParameter("term");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");
		/*
		 * Map<String, String> mapYearAndOrgIdAndTerm = new HashMap<String,
		 * String>(); mapYearAndOrgIdAndTerm.put("org_id", org_id);
		 * mapYearAndOrgIdAndTerm.put("begin_time", begin_time);
		 * mapYearAndOrgIdAndTerm.put("end_time", end_time);
		 * mapYearAndOrgIdAndTerm.put("term", term);
		 */
		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getOrgReportData(
				org_id, term, begin_time, end_time);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		dataAndReportTitle.put("yearAndTerm", yearAndTerm);
		dataAndReportTitle.put("data", gradu);
		dataAndReportTitle.put("report_title", report_title);
		// trainDetail_adminAndLeader_excel 是视图逻辑名，在 /WEB-INF/jasper-views.xml
		// 中定义
		response.setContentType("APPLICATION/vnd.ms-excel");
		modelAndView = new ModelAndView("trainDetail_adminAndLeader_excel",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能： 学生实习状态成绩折线报表 by 张超 2015年1月29日 吴敬国 2015-6-8
	 * 
	 * @return
	 */
	@RequestMapping(value = "leader/doGetStateReport.do")
	public ModelAndView doStateReport(ModelAndView modelAndView) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<Org> org = orgService.getAllColleges();
		List<ReportLogStateTreated> gradu = new ArrayList();
		List<ReportLogStateTreated> newGradu = new ArrayList();
		for (int i = 0; i < org.size(); i++) {
			String college_id = org.get(i).getId();
			gradu = reportLogStateService
					.getReportLogStateTreatedData(college_id);
			newGradu.addAll(gradu);
			// System.out.print(newGradu.get(i).getMonth());
		}
		model.put("data", newGradu);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("train_state", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能：转到学院实习状态图表
	 * 
	 * */
	@RequestMapping("leader/toPracticeStateChartCollege.do")
	public ModelAndView toPracticeStateChartCollege(HttpServletRequest request) {
		// 获取状态编码
		String curStateCode = request.getParameter("stateCode");
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		List<String> list=new ArrayList<String>();
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		String grade = request.getParameter("year");
		// 各学院各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = orgService.selectOrg_Name();
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = studentService
					.getStuStateCountByGradeAndOrg(mapParam);
			String percent = "";// 各项的百分比
			// 遍历该学院的各项状态，找到curStateCode，生成新的list listDeptStuState
			if (listStuStateCount.size() != 0)
				for (StuGraStateCount stuCount : listStuStateCount) {
					if (stuCount.getStateCode().equals(curStateCode)) {
						mapStateCount = new HashMap<String, Object>();
						// 计算该项状态在该学院的百分比
						percent = Ichartjs_Color.getPercent(listStuStateCount,
								curStateCode);
						// 生成新的list listDeptStuState
						mapStateCount.put("deptId", stuCount.getDeptId());
						mapStateCount.put("percent", percent);
						listMapDeptStuState.add(mapStateCount);
						list.add(stuCount.getDeptId());
						
					}
				}
			else//其他学院显示0 2016年2月25日 wugee
			{
				mapStateCount = new HashMap<String, Object>();
				// 生成新的list listDeptStuState
				mapStateCount.put("deptId", o.getId());
				mapStateCount.put("percent", "0");
				listMapDeptStuState.add(mapStateCount);
			}
		}
		
		ChartModel cm = new ChartModel();
		// 将新生成的数据转成json，在图表在显示
		cm = Ichartjs_Color.getChartModelByPercent(listMapDeptStuState);
		String jsonData = cm.getJsonData();
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		map.put("stateName", stateName);
		map.put("cm", jsonData);
		map.put("grade", grade);
		map.put("calculateRules", "计算规则：各二级学院"+ stateName+"人数/各二级学院总人数");
		return new ModelAndView("leader/practiceStateChart", map);
	}

	/**
	 * 
	 * @param session
	 * @return 管理员查看签到情况 by 邢志武 2015/4/1
	 */
	@RequestMapping("leader/schoolBmapPie.do")
	public ModelAndView schoolBmapPie(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 设置默认年份：当前-3
		String grade = Common.getDefaultYear();// String.valueOf(defaultyear);
		// 获取当前学院的名字
		String deptName = "商职学院";
		/*
		 * // 获取全校实习人数 int a = this.practiceTaskService.getSchoolStus(grade);
		 * System.out.println("全校实习人数：" + a); // 获取本周学校签到学生的比例 double SignPro =
		 * practiceTaskService.getSchoolDaySignPro(grade); // 获取本周该学院未签到学生的比例
		 * double unSignPro = 1 - SignPro; String SignProString =
		 * Double.toString(SignPro); String unSignProString =
		 * Double.toString(unSignPro); ChartData BmapIchar = new ChartData();
		 * BmapIchar.setName("1"); BmapIchar.setText("本周签到的人");
		 * BmapIchar.setValue(SignProString); BmapIchar.setColor("#CCFFFF");
		 * ChartData BmapIchar2 = new ChartData(); BmapIchar2.setName("2");
		 * BmapIchar2.setValue(unSignProString); BmapIchar2.setColor("#FFFF99");
		 * BmapIchar2.setText("本周未签到的人"); List<ChartData> BmapIcharList = new
		 * ArrayList<ChartData>(); BmapIcharList.add(BmapIchar2);
		 * BmapIcharList.add(BmapIchar); ChartModel mode = new ChartModel();
		 * mode.setListData(BmapIcharList); String json = mode.getJsonDataNew();
		 * Map<String, Object> map = new HashMap<String, Object>();
		 */
		map.put("deptName", deptName);
		map.put("grade", grade);
		/* map.put("cm", json); */
		return new ModelAndView("leader/schoolBmapPie", map);

	}

	/**
	 * ajax获取签到数据
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("leader/ajaxSchoolBmapPie.do")
	public String ajaxSchoolBmapPie(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		// 获取本周学校签到学生的比例
		double SignPro = practiceTaskService.getSchoolDaySignPro(grade);
		// 获取本周该学院未签到学生的比例
		double unSignPro = 1 - SignPro;
		String SignProString = Double.toString(SignPro);
		String unSignProString = Double.toString(unSignPro);
		ChartData BmapIchar = new ChartData();
		BmapIchar.setName("1");
		BmapIchar.setText("本周签到的人");
		BmapIchar.setValue(SignProString);
		BmapIchar.setColor("#CCFFFF");
		ChartData BmapIchar2 = new ChartData();
		BmapIchar2.setName("2");
		BmapIchar2.setValue(unSignProString);
		BmapIchar2.setColor("#FFFF99");
		BmapIchar2.setText("本周未签到的人");
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		BmapIcharList.add(BmapIchar2);
		BmapIcharList.add(BmapIchar);
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonDataNew();
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param session
	 * @return 学院领导查看签到情况 by 邢志武 2015/4/1
	 */
	@RequestMapping("leader/BmapPie2.do")
	public ModelAndView BmapPie2(HttpSession session) {
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptName", deptName);
		map.put("grade", grade);
		return new ModelAndView("leader/BmapPie", map);

	}

	/**
	 * ajax获取签到数据
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("leader/ajaxBmapPie.do")
	public String ajaxBmapPie(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		// 获取本周该学院签到学生的比例
		double SignPro = practiceTaskService.getCollegeDaySignPro(dept_id,
				grade);
		// 处理小数点保留位数问题
		String falseRate = new java.text.DecimalFormat("#.00").format(SignPro);
		SignPro = Double.parseDouble(falseRate);
		// 获取本周该学院未签到学生的比例
		double unSignPro = 1 - SignPro;
		String SignProString = Double.toString(SignPro);
		String unSignProString = Double.toString(unSignPro);
		ChartData BmapIchar = new ChartData();
		BmapIchar.setName("1");
		BmapIchar.setText("本周签到的人");
		BmapIchar.setValue(SignProString);
		BmapIchar.setColor("#CCFFFF");
		ChartData BmapIchar2 = new ChartData();
		BmapIchar2.setName("2");
		BmapIchar2.setValue(unSignProString);
		BmapIchar2.setColor("#FFFF99");
		BmapIchar2.setText("本周未签到的人");
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		BmapIcharList.add(BmapIchar2);
		BmapIcharList.add(BmapIchar);
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonDataNew();
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：转到各二级学院签到状态图表
	 * 
	 * */
	@RequestMapping("leader/tobMapCollegeSignState.do")
	public ModelAndView tobMapCollegeSignState(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = request.getParameter("year");
		String stateName = request.getParameter("stateName");
		Map<String, Object> map = new HashMap<String, Object>();
		String dept_id = "szxy";
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		// 标题
		String signState="";
		StringBuffer sb = new StringBuffer();
		if (stateName.equals("2")) {
			signState="未签到";
			sb.append(deptName + grade + "学生本周未签到情况");
		} else {
			signState="签到";
			sb.append(deptName + grade + "学生本周签到情况 ");
		}
		// 获取该学院管理下的系
		// List<Org> departments = orgService.selectByXyId(dept_id);
		List<Org> departments = orgService.selectOrg_Name();
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			double scale = practiceTaskService.getCollegeDaySignPro(org_id,
					grade);
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - (scale * 100);
			} else {
				scale = scale * 100;
			}
			// 处理小数点保留位数问题
			String falseRate = new java.text.DecimalFormat("#.00")
					.format(scale);
			scale = Double.parseDouble(falseRate);

			String SignProString = Double.toString(scale);
			ChartData BmapIchar = new ChartData();
			String org_name = DictionaryService.findOrg(org_id).getOrg_name();
			BmapIchar.setName(org_name);
			BmapIchar.setValue(SignProString);
			BmapIchar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			BmapIcharList.add(i, BmapIchar);
		}
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonData();
		String title = sb.toString();
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		map.put("calculateRules", "计算规则：各二级学院"+signState+"人数/二级学院实习总人数");
		return new ModelAndView("leader/bMapSignState", map);
	}

	/**
	 * 功能：转到系部签到状态图表 邢志武 2015/4/1
	 * 
	 * */
	@RequestMapping("leader/bMapSignState2.do")
	public ModelAndView tobMapSignState2(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = request.getParameter("year");
		String stateName = request.getParameter("stateName");
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		// 标题
		StringBuffer sb = new StringBuffer();
		if (stateName.equals("2")) {
			sb.append(deptName + grade + "学生本周未签到情况");
		} else {
			sb.append(deptName + grade + "学生本周签到情况 ");
		}
		// 获取该学院管理下的系
		// List<Org> departments = orgService.selectByXyId(dept_id);
		List<Org> departments = orgService.selectByXyId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			double scale = practiceTaskService.getDepartmentDaySignPro(org_id,
					grade);
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - scale;
			}
			// 处理小数点保留位数问题
			String falseRate = new java.text.DecimalFormat("#.00")
					.format(scale);
			scale = Double.parseDouble(falseRate);

			String SignProString = Double.toString(scale);
			ChartData BmapIchar = new ChartData();
			String org_name = DictionaryService.findOrg(org_id).getOrg_name();
			BmapIchar.setName(org_name);
			BmapIchar.setValue(SignProString);
			BmapIchar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			BmapIcharList.add(i, BmapIchar);
		}
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonData();
		String title = sb.toString();
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("leader/bMapSignState", map);
	}

	/**
	 * 功能：转到学院部签到状态图表 邢志武 2015/4/1
	 * 
	 * */
	@RequestMapping("leader/bMapSignState.do")
	public ModelAndView tobMapSignState(HttpSession session,
			HttpServletRequest request) {
		Calendar now = Calendar.getInstance();
		int defaultyear = now.get(Calendar.YEAR) - 3;
		String grade = String.valueOf(defaultyear);
		String stateName = request.getParameter("stateName");
		Map<String, Object> map = new HashMap<String, Object>();
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		// 标题
		String deptName = "商职学院";
		StringBuffer sb = new StringBuffer();
		// 获取该学校所有的学院
		List<String> Colleges = orgService.getColleges();
		for (int i = 0; i < Colleges.size(); i++) {
			String org_id = Colleges.get(i);
			double scale = practiceTaskService.getCollegeDaySignPro(org_id,
					grade) * 100;
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - scale;
				sb.append(deptName + grade + "学院学生本周未签到情况");
			}
			sb.append(deptName + grade + "学院学生本周签到情况 ");
			String SignProString = Double.toString(scale);
			ChartData BmapIchar = new ChartData();
			String org_name = DictionaryService.findOrg(org_id).getOrg_name();
			BmapIchar.setName(org_name);
			BmapIchar.setValue(SignProString);
			BmapIchar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			BmapIcharList.add(i, BmapIchar);
		}

		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonData();
		String title = sb.toString().substring(0, 19);
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("leader/bMapSignState", map);
	}

	/**
	 * 学院领导就业情况
	 * 
	 * 
	 */
	@RequestMapping("leader/index2.do")
	public ModelAndView index2(HttpSession session, String current_role_selected) {
		if (current_role_selected != null) {
			session.setAttribute("current_role_selected", current_role_selected);
		}
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String collegeLeaderCollegeId = tea.getDept_id();
		String org_level = DictionaryService.findOrg(collegeLeaderCollegeId)
				.getOrg_level();
		if (org_level.equals("3")) {
			collegeLeaderCollegeId = DictionaryService.findOrg(
					collegeLeaderCollegeId).getParent_id();
		}
		// 存入session 供其他模块使用 保存管理员模块的登陆教师所在学院的id号
		session.setAttribute("collegeLeaderCollegeId", collegeLeaderCollegeId);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		// 获取单个学院各项状态人数
		mapParam.put("grade", grade);
		mapParam.put("deptId", collegeLeaderCollegeId);
		List<StuGraStateCount> listStuStateCount = studentService
				.getStuStateCountByGradeAndOrg(mapParam);

		// 遍历该学院的各项状态，找到curStateCode，生成新的list listDeptStuState
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		if (listStuStateCount.size() != 0) {
			for (StuGraStateCount stuCount : listStuStateCount) {
				mapStateCount = new HashMap<String, Object>();
				mapStateCount.put("stateCode", stuCount.getStateCode());
				mapStateCount.put("stuCount", stuCount.getStuCount());
				listMapDeptStuState.add(mapStateCount);
			}
		}
		// 转为报表数据
		ChartModel cm = new ChartModel();
		cm = Ichartjs_Color.getChartModel(listMapDeptStuState);
		String jsonData = cm.getJsonDataNew();
		String college_name = DictionaryService.findOrg(collegeLeaderCollegeId)
				.getOrg_name();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cm", jsonData);
		map.put("grade", grade);
		map.put("college_name", college_name);

		// 设置当前权限为管理员 by桑博
		CommonSession.setUserRole(session, CommonSession.roleAdmin);
		return new ModelAndView("leader/index2", map);
	}

	/**
	 * ajax获取就业情况
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("leader/ajaxIndex.do")
	public String ajaxIndex(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		List<Map<String, Object>> listStuState = studentService
				.getSchoolStudentStateCountByGrade(grade);
		ChartModel cm = new ChartModel();
		cm = Ichartjs_Color.getChartModel(listStuState);
		String jsonData = cm.getJsonDataNew();
		if(jsonData.length()<5){
			jsonData="[{name : '初始状态',value : '100%',text : '000',color : '#8B0000'}]";
		}
		try {
			response.getWriter().println(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ajax获取就业情况
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("leader/ajaxIndex2.do")
	public String ajaxIndex2(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String collegeLeaderCollegeId = tea.getDept_id();
		String org_level = DictionaryService.findOrg(collegeLeaderCollegeId)
				.getOrg_level();
		if (org_level.equals("3")) {
			collegeLeaderCollegeId = DictionaryService.findOrg(
					collegeLeaderCollegeId).getParent_id();
		}
		// 存入session 供其他模块使用 保存管理员模块的登陆教师所在学院的id号
		session.setAttribute("collegeLeaderCollegeId", collegeLeaderCollegeId);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		// 获取单个学院各项状态人数
		mapParam.put("grade", grade);
		mapParam.put("deptId", collegeLeaderCollegeId);
		List<StuGraStateCount> listStuStateCount = studentService
				.getStuStateCountByGradeAndOrg(mapParam);

		// 遍历该学院的各项状态，找到curStateCode，生成新的list listDeptStuState
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		if (listStuStateCount.size() != 0) {
			for (StuGraStateCount stuCount : listStuStateCount) {
				mapStateCount = new HashMap<String, Object>();
				mapStateCount.put("stateCode", stuCount.getStateCode());
				mapStateCount.put("stuCount", stuCount.getStuCount());
				listMapDeptStuState.add(mapStateCount);
			}
		}
		// 转为报表数据
		ChartModel cm = new ChartModel();
		cm = Ichartjs_Color.getChartModel(listMapDeptStuState);
		String jsonData = cm.getJsonDataNew();
		try {
			response.getWriter().println(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：转到系部实习状态图表
	 * 
	 * */
	@RequestMapping("leader/toPracticeStateChartDept.do")
	public ModelAndView toPracticeStateChartDept(HttpSession session,
			HttpServletRequest request) {
		// 获取状态编码
		String curStateCode = request.getParameter("stateCode");
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		// 默认年份
		String grade = request.getParameter("year");
		// 获取负责的学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String college_id = Common.getCollegeId(dept_id);
		// 各系各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = orgService.selectByXyId(college_id);
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = studentService
					.getXiStuStateCountByGradeAndOrg(mapParam);
			String percent = "";// 各项的百分比
			// 遍历该学院的各项状态，找到curStateCode，生成新的list listDeptStuState
			if (listStuStateCount.size() != 0)
				for (StuGraStateCount stuCount : listStuStateCount) {
					if (stuCount.getStateCode().equals(curStateCode)) {
						mapStateCount = new HashMap<String, Object>();
						// 计算该项状态在该学院的百分比
						percent = Ichartjs_Color.getPercent(listStuStateCount,
								curStateCode);
						// 生成新的list listDeptStuState
						mapStateCount.put("deptId", stuCount.getDeptId());
						mapStateCount.put("percent", percent);
						listMapDeptStuState.add(mapStateCount);
					}
				}
		}
		ChartModel cm = new ChartModel();
		// 将新生成的数据转成json，在图表在显示
		cm = Ichartjs_Color.getChartModelByPercent(listMapDeptStuState);
		String jsonData = cm.getJsonData();
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		String college_Name = DictionaryService.findOrg(college_id)
				.getOrg_name();
		map.put("stateName", stateName);
		map.put("college_name", college_Name);
		map.put("cm", jsonData);
		map.put("grade", grade);
		map.put("StateCode", curStateCode);
		return new ModelAndView("leader/practiceStateChartDept", map);
	}

	/**
	 * 
	 * wugee111
	 * */
	@RequestMapping("leader/ajaxGetChartStateDate.do")
	public String ajaxGetChartStateDate(HttpSession session,
			HttpServletRequest request) {
		String college_id = request.getParameter("college_id");
		String grade = Common.getDefaultYear();
		List<Map<String, Object>> listStuState = studentService
				.getSchoolStudentStateCountByGrade(grade);
		ChartModel cm = new ChartModel();
		cm = Ichartjs_Color.getChartModel(listStuState);
		String jsonData = cm.getJsonDataNew();
		return jsonData;
	}

	/**
	 * 功能：转到系部教师实习状态图表 2015年9月12日10:23:24
	 * 
	 * @author WuGee
	 * */
	@RequestMapping("leader/practiceStateCharTeacher.do")
	public ModelAndView practiceStateCharTeacher(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取状态编码
		String curStateCode = request.getParameter("stateCode");
		if (curStateCode.equals("0")) {
			curStateCode = "000";
		}
		// 系的名称
		String fDepartmentName = request.getParameter("DepartmentName");
		Org o = new Org();
		o.setOrg_name(fDepartmentName);
		List<Org> orgList = new ArrayList<Org>();
		orgList = this.orgService.selectList(o);
		String DepartmentName = "";
		// 解决服务器与本地环境编码方式不同导致的错误
		if (orgList.size() > 0) {
			o = orgList.get(0);
		} else {
			// 乱码处理
			try {
				DepartmentName = new String(
						fDepartmentName.getBytes("iso-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			o.setOrg_name(DepartmentName);
			orgList = this.orgService.selectList(o);
			o = orgList.get(0);
		}
		// 系的id
		String org_id = o.getId();
		// 查询该系学生对应的任务负责的老师id集合
		List<String> teaList = this.practiceTaskService.selectTeaIdbyDempartId(
				org_id, grade);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		double stateProportion = 0;
		ChartModel cm = new ChartModel();
		List<ChartData> listData = new ArrayList<ChartData>();
		for (int i = 0; i < teaList.size(); i++) {
			String tea_id = teaList.get(i);
			// 构造参数
			mapParam.put("grade", grade);
			mapParam.put("teaId", tea_id);
			mapParam.put("state", curStateCode);
			// 老师带的学生总人数
			double count = studentService.getstateTeacherCount(mapParam);
			// 某个状态下的学生人数
			double stateCount = this.studentService.getStateStuCount(mapParam);
			stateProportion = (stateCount / count) * 100;
			// 小数点处理
			String stateProportionString = new java.text.DecimalFormat("#.00")
					.format(stateProportion);
			System.out.println(stateProportion);
			// 构造图表数据
			ChartData cd = new ChartData();
			cd.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			cd.setText(DictionaryService.findTeacher(tea_id).getTrue_name());
			cd.setValue(stateProportionString);
			cd.setName(curStateCode);
			listData.add(cd);
		}
		cm.setListData(listData);
		String jsonData = cm.getJsonDataNew();
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		map.put("stateName", stateName);
		map.put("college_name", DepartmentName);
		map.put("cm", jsonData);
		map.put("grade", grade);
		return new ModelAndView("leader/practiceStateChartTeacher", map);
	}

	/**
	 * 
	 * @param session
	 * @param request
	 * @return 查看学院任务完成度 xzw 2015年7月22日 09:55:14
	 */
	@RequestMapping("leader/taskAccomplishAll.do")
	public ModelAndView toTaskAccomplishAll(HttpSession session,
			HttpServletRequest request) {
		/* String notPassProp = Double.toString(notPassPro); */
		// 默认年份
		String grade = Common.getDefaultYear();
		// String grade ="2013";
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		List<ChartData> IcharList = new ArrayList<ChartData>();
		// 标题
		StringBuffer sb = new StringBuffer();
		// 获取该学院管理下的系
		// List<Org> departments = orgService.selectByXyId(dept_id);
		Map<String, Double> CollegeTaskAccomplish = scoreService
				.getCollegeTaskAccomplish(dept_id, grade);
		// Map<String, Double>
		// CollegeTaskAccomplish=scoreService.getDepartementTaskAccomplish("rjx",
		// grade);
		double mothScoreAccomplish = CollegeTaskAccomplish
				.get("mothScoreAccomplish") * 100;
		double thesisScoreAccomplish = CollegeTaskAccomplish
				.get("thesisScoreAccomplish") * 100;
		String TmothScoreAccomplish = Double.toString(mothScoreAccomplish);
		String TthesisScoreAccomplish = Double.toString(thesisScoreAccomplish);
		if (TmothScoreAccomplish.length() > 4) {
			TmothScoreAccomplish = TmothScoreAccomplish.substring(0, 4);
		}
		if (TthesisScoreAccomplish.length() > 4) {
			TthesisScoreAccomplish = TthesisScoreAccomplish.substring(0, 4);
		}
		ChartData taskIchar = new ChartData();
		taskIchar.setName("论文完成度");
		taskIchar.setText("thesis");
		taskIchar.setValue(TthesisScoreAccomplish);
		taskIchar.setColor("#76a871");
		ChartData taskIchar2 = new ChartData();
		taskIchar2.setName("月总结完成度");
		taskIchar2.setText("moth");
		taskIchar2.setValue(TmothScoreAccomplish);
		taskIchar2.setColor("#cbab4f");
		IcharList.add(taskIchar);
		IcharList.add(taskIchar2);
		ChartModel mode = new ChartModel();
		mode.setListData(IcharList);
		String json = mode.getJsonData();
		map.put("title", deptName + grade + "级" + "任务完成情况");
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("leader/taskAccomplishAll", map);
	}

	/**
	 * 查看各系任务完成度 邢志武2015年7月22日 09:55:02
	 */
	@RequestMapping("leader/taskAccomplishDepartments.do")
	public ModelAndView taskAccomplishDepartments(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = Common.getDefaultYear();
		// String grade ="2013";
		String stateText = request.getParameter("stateText");
		String TstateText = "";
		try {
			TstateText = new String(stateText.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		// 标题
		StringBuffer sb = new StringBuffer();
		// 获取该学院管理下的系
		List<Org> departments = orgService.selectXiOrg_Name(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			// 某系校外实习成绩优秀率
			Map<String, Double> departementTaskAccomplish = this.scoreService
					.getDepartementTaskAccomplish(org_id, grade);
			// 判断点击的那部分饼图
			double doublePro = 0;
			if (TstateText.equals("月总结完成度")) {
				doublePro = departementTaskAccomplish
						.get("mothScoreAccomplish") * 100;
				sb.append(deptName + grade + "月总结完成度");
			} else {
				doublePro = departementTaskAccomplish
						.get("thesisScoreAccomplish") * 100;
				sb.append(deptName + grade + "论文完成度");
			}
			String SignProString = Double.toString(doublePro);
			if (SignProString.length() > 4) {
				SignProString = SignProString.substring(0, 4);
			}
			ChartData ichar = new ChartData();
			String org_name = DictionaryService.findOrg(org_id).getOrg_name();
			ichar.setName(org_name);
			ichar.setValue(SignProString);
			ichar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			BmapIcharList.add(i, ichar);
		}

		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonData();
		String title = sb.toString().substring(0, 15);
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("leader/taskAccomplishDepartment", map);
	}

	// 跳转到查看公告页面 邢志武 2015年6月30日
	@RequestMapping("leader/selectNotices.do")
	public String selectNotices(HttpSession session,
			HttpServletRequest request, ModelMap modelMap) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
		}
		Notice n = new Notice();
		n.setNotice_type("1");
		n.setOrg_id(xy_id);
		List<Notice> noticeList = this.noticeService.selectList(n);
		modelMap.put("noticeList", noticeList);
		return "leader/noticeList"; // 保存成功后关闭窗口
	}

	// 跳转到查看公告详情页面 邢志武 2015年6月30日
	@RequestMapping("leader/toOneNotice.do")
	public String toOneNotice(HttpServletRequest request, ModelMap modelMap) {
		String notice_id = request.getParameter("notice_id");
		Notice notice = (Notice) noticeService.selectByID(notice_id);
		modelMap.put("notice", notice);
		return "leader/oneNotice"; // 保存成功后关闭窗口
	}

	/**
	 * 功能：领导查看教师指导记录 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/diectRecordList.do")
	public ModelAndView directrecordList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String dept_id = orgService.getCollegeIdByTeaId(session);
		List<Org> orgList = this.orgService.selectByXueYuanId(dept_id);
		session.setAttribute("diec_orgs", orgList);
		map.put("org", orgList);
		return new ModelAndView("leader/diectRecordList", map);
	}

	/**
	 * 功能： 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/backDiectRecordList.do")
	public ModelAndView backDiectRecordList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> diec_orgs = (List<Org>) session.getAttribute("diec_orgs");
		List<DirectRecord> directList = (List<DirectRecord>) session
				.getAttribute("directList");
		List<Teacher> diec_tealist = (List<Teacher>) session
				.getAttribute("diec_tealist");
		String diec_orgname = (String) session.getAttribute("diec_orgname");
		map.put("directList", directList);
		map.put("org", diec_orgs);
		map.put("diec_orgname", diec_orgname);
		map.put("diec_tealist", diec_tealist);
		return new ModelAndView("leader/diectRecordList", map);
	}

	/**
	 * 功能：ajax获取教师指导记录 邢志武2015-03-24
	 * 
	 * */
	@RequestMapping("leader/ajaxTeasDieRecs.do")
	public String ajaxTeasDieRecs(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String tea_idAnddeptment_id = request
				.getParameter("tea_idAnddeptment_id");
		String cont = "";
		try {
			cont = URLDecoder.decode(tea_idAnddeptment_id, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int a = cont.indexOf("-");
		int b = cont.indexOf("*");
		String tea_id = cont.substring(0, a);
		String deptment_id = cont.substring(a + 1, b);
		session.setAttribute("diec_orgname",
				DictionaryService.findOrg(deptment_id).getOrg_name());
		String month = cont.substring(b + 1, cont.length());
		StringBuffer sb = new StringBuffer();
		List<DirectRecord> result;
		if (tea_id.equalsIgnoreCase("请选择")
				& (!deptment_id.equalsIgnoreCase("0"))) {// 如果只选择部门不选择教师的情况
			result = this.directRecordService.getDeptmentDirecRecs(deptment_id,
					month, "2");
		} else {
			if (month.equalsIgnoreCase("") || month == null) {
				result = this.directRecordService
						.getTeaDirecRecsByTeaIdAndDr_type(tea_id, "2");
			} else {
				result = this.directRecordService
						.getTeaDirecRecsByTeaIdAndMonth(tea_id, month, "2");
			}
			result = this.directRecordService.getTeaDirecRecsByTeaIdAndMonth(
					tea_id, month, "2");
		}
		session.setAttribute("directList", result);
		for (DirectRecord d : result) {
			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findPracticeTask(d.getPractice_id())
							.getTask_name() + "</td>");
			sb.append("<td><a href='editdirectrecord.do?id=" + d.getId() + "'>"
					+ d.getTitle() + "</a></td>");
			sb.append("<td>"
					+ DateService.TimestampTimeTurnStringTime(d
							.getDirect_time()) + "</td>");
			sb.append("<td>" + d.getTemp2() + "</td>");
			sb.append("<td>" + d.getDirect_place() + "</td>");
			/*
			 * sb.append("<td>"+
			 * DictionaryService.findGroups(d.getOrg_id()).getGroup_name() +
			 * "</td>");
			 */
			String stuts = d.getStu_id();
			StringBuffer bf = new StringBuffer();
			String[] sts = stuts.split(",");
			for (int i = 0; i < sts.length; i++) {
				String stuName = DictionaryService.findStudent(sts[i])
						.getTrue_name();
				bf.append(stuName + "、");
			}
			bf.deleteCharAt(bf.length() - 1);
			sb.append("<td>" + bf + "</td>");
			/* sb.append("<td>" + d.getDirect_desc() + "</td>"); */
			sb.append("</tr>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：查询系老师 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/ajaxPk_teacher.do")
	public String ajaxPk_teacher(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		String c = request.getParameter("xibu");
		StringBuffer s = new StringBuffer();
		s.append("<option selected='selected'>请选择</option>");
		if (c != null) {
			List<Teacher> t = teacherService.getTeachersByDeptId(c);
			session.setAttribute("diec_tealist", t);
			for (Teacher teacher : t) {
				s.append("<option " + "value=" + teacher.getId() + ">"
						+ teacher.getTrue_name() + "</option>");
			}
			try {
				response.getWriter().println(s.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：领导查看指导记录 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/editdirectrecord.do")
	public String editdirectrecord(ModelMap modelMap, String id) {
		DirectRecord d = (DirectRecord) directRecordService.selectByID(id);
		modelMap.put("directrecord", d);
		return "leader/directrecordEdit";
	}

	/**
	 * 功能：文件下载 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/downloadFile.do")
	public String download(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, String file_id) throws Exception {
		String project_path = session.getServletContext().getRealPath("/")
				+ "/";
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		// String ctxPath = "D:"+"\\"+"uploadedfiles\\";
		Files file1 = (Files) filesService.selectByID(file_id);
		String thesisPosition = file1.getPosition();
		filesService.downloadfile(thesisPosition, ctxPath, request, response);
		return null;
	}

	/**
	 * 功能：通告查询列表 注解请求地址(映射) 王磊20141124
	 * 
	 * */
	@RequestMapping("leader/noticeList.do")
	public ModelAndView noticeList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Notice> result = this.noticeService
				.getCollegeNoticeListByteaid(session);
		map.put("result", result);
		return new ModelAndView("leader/noticeList", map);
	}

	/**
	 * 功能：管理员查看详细通告 注解请求地址(映射) by 王磊20141124
	 * 
	 * */
	@RequestMapping("leader/checkNotice.do")
	public String checkPracticeRecord(ModelMap modelMap, String id) {
		Notice notice = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", notice);

		return "leader/noticeDetail";
	}

	/**
	 * 功能：管理员查看修改通告公告 注解请求地址(映射) by 王磊2015年4月1日
	 * 
	 * */
	@RequestMapping("leader/editNotice.do")
	public String editNotice(ModelMap modelMap, String id) {
		Notice notice = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", notice);

		return "leader/noticeEdit";
	}

	/**
	 * 功能：管理员查看修改通告公告 注解请求地址(映射) by 王磊2015年4月1日
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * 
	 * */
	@RequestMapping("leader/doEditNotice.do")
	public String doEditNotice(String id, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Notice notice = (Notice) noticeService.selectByID(id);

		String times = DateService.formatNowTimeforUpload();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (!file.isEmpty()) {
					String file_type = "Notice";
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					String file_name = file.getOriginalFilename();
					String filePosition = file_type + "/" + file_type + "_"
							+ times + "_" + file_name;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					/* String file_name = file.getOriginalFilename(); */
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件

					// 文件的属性
					Files fil = new Files();
					String file_id = Common.returnUUID();
					fil.setId(file_id);
					fil.setFile_name(file_name);// 存保存的实践任务的名称，一个实践任务对应一个文件，保存的位置的文件名存实践任务的任务编码
					fil.setFile_type(file_type);
					fil.setUpload_time(DateService.getNowTimeTimestamp());
					fil.setPosition(filePosition);// 这里保存的文件位置不是绝对路径
					fil.setFile_size(filesize);
					fil.setStu_id("");//
					filesService.insert(fil);
					notice.setTemp2(file_id);
				}
			}
		}
		notice.setContent(content);
		notice.setTitle(title);
		this.noticeService.update(notice);
		return "redirect:noticeList.do";
	}

	/**
	 * 功能：管理员添加通告 注解请求地址(映射)-- 添加页面 王磊20141125
	 * 
	 * */
	@RequestMapping("leader/addNotice.do")
	public String addNotice(HttpSession session, ModelMap modelMap) {
		return "leader/noticeAdd";
	}

	/**
	 * 功能：管理员添加通知公告 by王磊 2014
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * 
	 * */
	@RequestMapping("leader/doAddNotice.do")
	public String savaNotice(HttpServletRequest request, HttpSession session)
			throws IllegalStateException, IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String tea_code = tea.getTea_code();
		String dept_id = tea.getDept_id();
		String notice_code = "";
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		String xy_code;
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
		}
		xy_code = DictionaryService.findOrg(xy_id).getOrg_code();
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String year = nowTime.substring(0, 4);
		// 生成通知编码 王磊 2015年1月30日
		String notice_cur_code = year + xy_code + tea_code;
		String strNotice_max_code = this.noticeService
				.getMaxNoticCode(notice_cur_code);
		if (strNotice_max_code == null) {// 本任务的第一条通知
			notice_code = notice_cur_code + "001";
		} else {// 不是第一条，取出最多通知编码的后三位，加2
			int maxCode = Integer.parseInt(strNotice_max_code) + 1;
			if (String.valueOf(maxCode).length() == 1) {// 如果是个位数
				notice_code = notice_cur_code + "00" + maxCode;
			} else if (String.valueOf(maxCode).length() == 2) {// 如果是两位位数
				notice_code = notice_cur_code + "0" + maxCode;
			} else {// 如果是三位数
				notice_code = notice_cur_code + maxCode;
			}
		}
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Notice n = new Notice();

		String times = DateService.formatNowTimeforUpload();

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (!file.isEmpty()) {
					String file_type = "Notice";
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					String file_name = file.getOriginalFilename();
					String filePosition = file_type + "/" + file_type + "_"
							+ times + "_" + file_name;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					/* String file_name = file.getOriginalFilename(); */
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件

					// 文件的属性
					Files fil = new Files();
					String file_id = Common.returnUUID();
					fil.setId(file_id);
					fil.setFile_name(file_name);// 存保存的实践任务的名称，一个实践任务对应一个文件，保存的位置的文件名存实践任务的任务编码
					fil.setFile_type(file_type);
					fil.setUpload_time(DateService.getNowTimeTimestamp());
					fil.setPosition(filePosition);// 这里保存的文件位置不是绝对路径
					fil.setFile_size(filesize);
					fil.setStu_id("");// 保存创建实践任务的id
					filesService.insert(fil);
					n.setTemp2(file_id);
				}
			}
		}

		n.setContent(content);
		n.setNotice_code(notice_code);
		n.setNotice_type("1");
		n.setOrg_id(xy_id);
		n.setStu_id("");
		n.setTitle(title);
		n.setTea_id(tea_id);
		n.setCreate_time(ts);
		noticeService.insert(n);
		return "redirect:noticeList.do";// 添加成功后重定向到列表页面

	}

	/**
	 * 功能：管理员删除通知公告 王磊 20141210
	 * 
	 * */
	@RequestMapping("leader/deleteNotice.do")
	public String deleteNotice(String id, HttpServletRequest request) {
		Notice n = DictionaryService.findNotice(id);
		String file_id = n.getTemp2();
		if (file_id != null) {
			filesService.deleteFile(file_id, request);
		}
		noticeService.delete(n);
		return "redirect:noticeList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 图表 功能：获取某个系的教师工作成绩 2015年9月12日10:23:24
	 * 
	 * @author WuGee
	 * @throws ParseException
	 * @RequestMapping("leader/teacherMouthWorkload.do") public ModelAndView
	 *                                                   teacherMouthWorkload
	 *                                                   (HttpSession session,
	 *                                                   HttpServletRequest
	 *                                                   request) throws
	 *                                                   ParseException {
	 *                                                   //默认当月的即时性图 //默认年份
	 *                                                   String grade =
	 *                                                   Common.getDefaultYear
	 *                                                   (); Teacher tea =
	 *                                                   (Teacher)
	 *                                                   session.getAttribute
	 *                                                   ("current_user");
	 *                                                   String dept_id =
	 *                                                   tea.getDept_id();
	 *                                                   String org_level =
	 *                                                   DictionaryService
	 *                                                   .findOrg
	 *                                                   (dept_id).getOrg_level
	 *                                                   (); if
	 *                                                   (org_level.equals("3"))
	 *                                                   { dept_id =
	 *                                                   DictionaryService
	 *                                                   .findOrg
	 *                                                   (dept_id).getParent_id
	 *                                                   (); } List<Org>
	 *                                                   orglist=orgService.
	 *                                                   selectXiOrg_Name
	 *                                                   (dept_id); String
	 *                                                   org_id
	 *                                                   =orglist.get(0).getId
	 *                                                   ();
	 *                                                   //查询该系学生对应的任务负责的老师id集合
	 *                                                   List<String>
	 *                                                   teaList=this
	 *                                                   .practiceTaskService.
	 *                                                   selectTeaIdbyDempartId
	 *                                                   (org_id, grade);
	 *                                                   ChartModel cm = new
	 *                                                   ChartModel();
	 *                                                   List<ChartData>
	 *                                                   listData=new
	 *                                                   ArrayList<ChartData>();
	 *                                                   for(int
	 *                                                   i=0;i<teaList.size
	 *                                                   ();i++){ String
	 *                                                   tea_id=teaList.get(i);
	 *                                                   Teacher teacher=
	 *                                                   DictionaryService
	 *                                                   .findTeacher(tea_id);
	 *                                                   String
	 *                                                   tea_name=teacher.
	 *                                                   getTrue_name();
	 *                                                   List<PracticeTask>
	 *                                                   prcaList
	 *                                                   =practiceTaskService.
	 *                                                   getPracticeTaskByGradeAndTeaId
	 *                                                   (tea_id,grade); String
	 *                                                   prc_id
	 *                                                   =prcaList.get(0).getId
	 *                                                   (); double score
	 *                                                   =practiceTaskService.
	 *                                                   getTeacherMouthApicScore
	 *                                                   (prc_id, tea_id);
	 *                                                   //小数点处理 String
	 *                                                   scoreString= new
	 *                                                   java.text
	 *                                                   .DecimalFormat(
	 *                                                   "#.00").format(score);
	 *                                                   if(score==0.0){
	 *                                                   scoreString="0"; }
	 *                                                   //构造图表数据 ChartData
	 *                                                   cd=new ChartData();
	 *                                                   cd.setColor
	 *                                                   (Ichartjs_Color
	 *                                                   .Ichartjs_Color_List
	 *                                                   .get(i));
	 *                                                   cd.setText(tea_name);
	 *                                                   cd
	 *                                                   .setValue(scoreString);
	 *                                                   cd.setName(tea_name);
	 *                                                   listData.add(cd); }
	 *                                                   cm.setListData
	 *                                                   (listData); String
	 *                                                   jsonData =
	 *                                                   cm.getJsonDataNew();
	 *                                                   Map<String, Object> map
	 *                                                   = new HashMap<String,
	 *                                                   Object>(); Map<String,
	 *                                                   String> m=Common.
	 *                                                   getTimeForTeacherAplic
	 *                                                   (); String
	 *                                                   nowtime=m.get
	 *                                                   ("nowTime"); String
	 *                                                   org_name
	 *                                                   =DictionaryService
	 *                                                   .findOrg
	 *                                                   (org_id).getOrg_name();
	 *                                                   String title=org_name+
	 *                                                   nowtime+"月教师月工作得分情况";
	 *                                                   map.put("orglist",
	 *                                                   orglist); map.put("cm",
	 *                                                   jsonData);
	 *                                                   map.put("title",
	 *                                                   title);
	 *                                                   map.put("grade",
	 *                                                   grade); return new
	 *                                                   ModelAndView(
	 *                                                   "leader/teacherMouthWorkLoad"
	 *                                                   ,map); }
	 */
	/**
	 * 功能：获取某个系的教师工作成绩 2015年9月12日10:23:24
	 * 
	 * @author WuGee
	 * @throws ParseException
	 * */
	/*
	 * @RequestMapping("leader/ajaxTeacherMouthWorkload.do") public String
	 * ajaxTeacherMouthWorkload(HttpSession session, HttpServletRequest request)
	 * throws ParseException { //默认当月的即时性图 //默认年份 String grade =
	 * Common.getDefaultYear(); String org_id=request.getParameter("org_id");
	 * //查询该系学生对应的任务负责的老师id集合 List<String>
	 * teaList=this.practiceTaskService.selectTeaIdbyDempartId(org_id, grade);
	 * ChartModel cm = new ChartModel(); List<ChartData> listData=new
	 * ArrayList<ChartData>(); for(int i=0;i<teaList.size();i++){ String
	 * tea_id=teaList.get(i); Teacher
	 * teacher=DictionaryService.findTeacher(tea_id); String
	 * tea_name=teacher.getTrue_name(); List<PracticeTask>
	 * prcaList=practiceTaskService
	 * .getPracticeTaskByGradeAndTeaId(tea_id,grade); String
	 * prc_id=prcaList.get(0).getId(); double score
	 * =practiceTaskService.getTeacherMouthApicScore(prc_id, tea_id); //小数点处理
	 * String scoreString= new java.text.DecimalFormat("#.00").format(score);
	 * if(score==0.0){ scoreString="0"; } //构造图表数据 ChartData cd=new ChartData();
	 * cd.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
	 * cd.setText(tea_name); cd.setValue(scoreString); cd.setName(tea_name);
	 * listData.add(cd); } cm.setListData(listData); String jsonData =
	 * cm.getJsonDataNew(); Map<String, Object> map = new HashMap<String,
	 * Object>(); Map<String, String> m=Common.getTimeForTeacherAplic(); String
	 * nowtime=m.get("nowTime"); String
	 * org_name=DictionaryService.findOrg(org_id).getOrg_name(); String
	 * title=org_name+nowtime+"月教师月工作得分情况"; return jsonData ; }
	 */

	/**
	 * 功能：获取某个系的教师工作成绩 2015年9月12日10:23:24
	 * 
	 * @author WuGee
	 * @throws ParseException
	 * */
	@RequestMapping("leader/teacherMouthWorkload.do")
	public ModelAndView teacherMouthWorkload(HttpSession session,
			HttpServletRequest request) throws ParseException {
		List<Teacher> listTeacher = new ArrayList<Teacher>();
		// 默认当月的即时性图
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		String org_name = DictionaryService.findOrg(dept_id).getOrg_name();
		List<Org> orglist = orgService.selectXiOrg_Name(dept_id);
		for (int j = 0; j < orglist.size(); j++) {
			String org_id = orglist.get(j).getId();
			// 查询该系学生对应的任务负责的老师id集合
			List<String> teaList = this.practiceTaskService
					.selectTeaIdbyDempartId(org_id, grade);

			for (int i = 0; i < teaList.size(); i++) {
				String tea_id = teaList.get(i);
				Teacher teacher = DictionaryService.findTeacher(tea_id);
				List<PracticeTask> prcaList = practiceTaskService
						.getPracticeTaskByGradeAndTeaId(tea_id, grade);
				String prc_id = prcaList.get(0).getId();
				Map<String, Double> teacherMouthApicData = practiceTaskService
						.getTeacherMouthApicData(prc_id, tea_id, grade);
				// 教师实际工作量
				double score = teacherMouthApicData.get("score");
				// 合格实习人数（合格实习人数以学生上传实习月总结并且老师已批阅的数量为准）
				double qualifiedCount = teacherMouthApicData
						.get("qualifiedCount");
				// 教师所带的实习人数
				double studentSize = teacherMouthApicData.get("studentSize");
				// 教师完成度（实际工作量/理想工作量）
				double theoryApicScore = teacherMouthApicData
						.get("theoryApicScore") * 100;
				String org_id2 = orgService.getCollegeIdByTeaId(session);
				Org org = orgService.selectByID(org_id2);
				Param p1 = new Param();
				p1.setParam_name("教师总结");
				p1.setDept_id(org.getOrg_code());
				Param param = new Param();
				param = paramService.selectParambyIdAndParam_name(p1);// 取出学院是否有总结参数
				double guideCoefficient = 0;
				//判断学院是否有要求上传教师总结
				if (param != null) {
					if (param.getParam_code().equals("false")) {
						guideCoefficient = 1.0;
					} else {
						// 教师是否上传月总结
						guideCoefficient = teacherMouthApicData
								.get("guideCoefficient");
					}
				} else {
					// 教师是否上传月总结
					guideCoefficient = teacherMouthApicData.get("guideCoefficient");
				}
				// 小数点处理
				String guideCoefficientString = Common
						.getDoubetoString(guideCoefficient);
				String scoreString = Common.getDoubetoString(score);
				String qualifiedCountString = Common
						.getDoubetoString(qualifiedCount);
				String studentSizeString = Common.getDoubetoString(studentSize);
				String theoryApicScoreString = Common
						.getDoubetoString(theoryApicScore) + "%";
				teacher.setScore(scoreString);
				teacher.setQualifiedCount(qualifiedCountString);
				teacher.setStudentSize(studentSizeString);
				teacher.setTheoryApicScore(theoryApicScoreString);
				teacher.setPrac_id(prc_id);
				teacher.setYesOrNo(guideCoefficientString);
				listTeacher.add(teacher);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ListSort<Teacher> listSort = new ListSort<Teacher>();
		listSort.Sort(listTeacher, "getTheoryApicScore", "desc");
		map.put("listTeacher", listTeacher);
		map.put("org_name", org_name);
		return new ModelAndView("leader/teacherMouthWorkLoad", map);
	}

	/**
	 * 功能：获取某个系的教师工作成绩 某个月份 2015年4月11日
	 * 
	 * */
	@RequestMapping("leader/getTeacherMouthWorkload.do")
	public String getTeacherMouthWorkload(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String mouth = request.getParameter("mouth");
		String year = request.getParameter("year");
		List<Teacher> listTeacher = new ArrayList<Teacher>();
		StringBuffer allTeachers = new StringBuffer();
		// 默认当月的即时性图
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		List<Org> orglist = orgService.selectXiOrg_Name(dept_id);
		for (int j = 0; j < orglist.size(); j++) {
			String org_id = orglist.get(j).getId();
			// 查询该系学生对应的任务负责的老师id集合
			List<String> teaList = this.practiceTaskService
					.selectTeaIdbyDempartId(org_id, grade);

			for (int i = 0; i < teaList.size(); i++) {
				String tea_id = teaList.get(i);
				Teacher teacher = DictionaryService.findTeacher(tea_id);
				List<PracticeTask> prcaList = practiceTaskService
						.getPracticeTaskByGradeAndTeaId(tea_id, grade);
				String prc_id = prcaList.get(0).getId();
				Map<String, Double> teacherMouthApicData = practiceTaskService
						.getTeacherMouthApicData2(prc_id, tea_id, mouth, year,
								grade,session);
				// 教师实际工作量
				double score = teacherMouthApicData.get("score");
				// 合格实习人数（合格实习人数以学生上传实习月总结并且老师已批阅的数量为准）
				double qualifiedCount = teacherMouthApicData
						.get("qualifiedCount");
				// 教师所带的实习人数
				double studentSize = teacherMouthApicData.get("studentSize");
				// 教师完成度（实际工作量/理想工作量）
				double theoryApicScore = teacherMouthApicData
						.get("theoryApicScore") * 100;
				String org_id2 = orgService.getCollegeIdByTeaId(session);
				Org org = orgService.selectByID(org_id2);
				Param p1 = new Param();
				p1.setParam_name("教师总结");
				p1.setDept_id(org.getOrg_code());
				Param param = new Param();
				param = paramService.selectParambyIdAndParam_name(p1);// 取出学院是否有总结参数
				double guideCoefficient = 0;
				//判断学院是否有要求上传教师总结
				if (param != null) {
					if (param.getParam_code().equals("false")) {
						guideCoefficient = 1.0;
					} else {
						// 教师是否上传月总结
						guideCoefficient = teacherMouthApicData
								.get("guideCoefficient");
					}
				} else {
					// 教师是否上传月总结
					guideCoefficient = teacherMouthApicData.get("guideCoefficient");
				}
				// 小数点处理
				String guideCoefficientString = Common
						.getDoubetoString(guideCoefficient);
				String scoreString = Common.getDoubetoString(score);
				String qualifiedCountString = Common
						.getDoubetoString(qualifiedCount);
				String studentSizeString = Common.getDoubetoString(studentSize);
				String theoryApicScoreString = Common
						.getDoubetoString(theoryApicScore) + "%";
				teacher.setScore(scoreString);
				teacher.setQualifiedCount(qualifiedCountString);
				teacher.setStudentSize(studentSizeString);
				teacher.setTheoryApicScore(theoryApicScoreString);
				teacher.setPrac_id(prc_id);
				teacher.setYesOrNo(guideCoefficientString);
				listTeacher.add(teacher);
			}
		}
		/*
		 * Collections.sort(listTeacher, new Comparator<Teacher>() { public int
		 * compare(Teacher t1, Teacher t2) { int result =
		 * Integer.parseInt(t1.getScore()) -Integer.parseInt(t2.getScore());
		 * return result; } });
		 */
		ListSort<Teacher> listSort = new ListSort<Teacher>();
		listSort.Sort(listTeacher, "getTheoryApicScore", "desc");
		int i = 1;
		for (Teacher t : listTeacher) {
			String prc_name = DictionaryService
					.findPracticeTask(t.getPrac_id()).getTask_name();
			allTeachers.append("<tr>");
			allTeachers.append("<td>" + i + "</td>");
			allTeachers.append("<td>" + t.getTrue_name() + "</td>");
			allTeachers.append("<td>" + prc_name + "</td>");
			allTeachers.append("<td>" + t.getQualifiedCount() + "</td>");
			allTeachers.append("<td>" + t.getStudentSize() + "</td>");
			allTeachers.append("<td>" + t.getYesOrNo() + "</td>");
			allTeachers.append("<td>" + t.getScore() + "</td>");
			allTeachers.append("<td>" + t.getTheoryApicScore() + "</td>");
			allTeachers.append("</tr>");
			i++;
		}
		try {
			response.getWriter().println(allTeachers.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	// 查询某个学院的学生分布（院领导的权限）
	@RequestMapping("leader/getDepartmetStudentCityStatistics.do")
	public ModelAndView getDepartmetStudentCityStatistics(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap modelMap) throws Exception {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 设置默认的查询年级
		String grade = Common.getDefaultYear();
		String org_name = DictionaryService.findOrg(dept_id).getOrg_name();
		// 存放各省市的學生數量
		Map<String, Integer> map1 = this.practiceTaskService.getCollegeStudent(
				dept_id, grade);
		Map<String, Integer> map = Common.mapSort(map1);
		ChartModel cm = new ChartModel();
		ChartModel cm2 = new ChartModel();
		List<ChartData> listData = new ArrayList<ChartData>();
		List<ChartData> listData2 = new ArrayList<ChartData>();
		int i = 1;
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = "
					+ entry.getValue());
			String text = entry.getKey();
			String color = Ichartjs_Color.Ichartjs_Color_List.get(i);
			Integer value = entry.getValue();
			String val = String.valueOf(value);
			ChartData cd = new ChartData();
			cd.setColor(color);
			cd.setText(text);
			cd.setValue(val);
			cd.setName(text);
			listData.add(cd);
			// 拼写lege所需数据
			ChartData cd2 = new ChartData();
			cd2.setColor(color);
			cd2.setText(text + "(" + val + "人)");
			cd2.setValue(val);
			cd2.setName(text + "(" + val + "人)");
			listData2.add(cd2);
			i++;
		}

		cm.setListData(listData);
		cm2.setListData(listData2);
		String jsonData = cm.getJsonDataNew();
		String jsonData2 = cm2.getJsonDataNew();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("org_name", org_name);
		result.put("grade", grade);
		result.put("cm", jsonData);
		result.put("bm", jsonData2);
		return new ModelAndView("leader/StudentCityStatistics", result);
	}

	// 学校实习学生分布（学校领导的权限）
	@RequestMapping("leader/getSchoolStudentCityStatistics.do")
	public ModelAndView getSchoolStudentCityStatistics(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap modelMap) throws Exception {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		// 设置默认的查询年级
		String grade = Common.getDefaultYear();
		String org_name = DictionaryService.findOrg(dept_id).getOrg_name();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("org_name", org_name);
		result.put("grade", grade);
		return new ModelAndView("leader/StudentCityStatistics", result);
	}

	// 学校实习学生分布（学校领导的权限）
	@RequestMapping("leader/getCollegeStudentCityStatistics.do")
	public ModelAndView getCollegeStudentCityStatistics(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap modelMap) throws Exception {
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		String provence = request.getParameter("stateCode");
		String grade = request.getParameter("year");
		provence = new String(provence.getBytes("iso-8859-1"), "utf-8");
		List<String> list = this.orgService.getColleges();
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i);
			double pet = this.regionalDistributionService.getPent(grade, provence,id);
			String SignProString = Double.toString(pet);
			ChartData BmapIchar = new ChartData();
			String org_name = DictionaryService.findOrg(id).getOrg_name();
			BmapIchar.setName(org_name);
			BmapIchar.setValue(SignProString);
			BmapIchar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
			BmapIcharList.add(i, BmapIchar);
		}
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonData();
		modelMap.put("title", grade+"级各二级学院学生分布");
		modelMap.put("grade", grade);
		modelMap.put("provence", provence);
		modelMap.put("calculateRules", "计算规则：各二级学院"+provence+"人数/全校"+provence+"总人数");
		modelMap.put("cm", json);
		return new ModelAndView("leader/bMapSignState", modelMap);
	}

	/**
	 * ajax获取签到数据
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("leader/ajaxCityStatistics.do")
	public String ajaxCityStatistics(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		// 存放各省市的學生數量
		Map<String, Integer> map1 = this.practiceTaskService.getSchoolStudent(
				dept_id, grade);
		Map<String, Integer> map = Common.mapSort(map1);
		ChartModel cm = new ChartModel();
		ChartModel cm2 = new ChartModel();
		List<ChartData> listData = new ArrayList<ChartData>();
		List<ChartData> listData2 = new ArrayList<ChartData>();
		int i = 1;
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = "
					+ entry.getValue());
			String text = entry.getKey();
			String color = Ichartjs_Color.Ichartjs_Color_List.get(i);
			Integer value = entry.getValue();
			String val = String.valueOf(value);
			ChartData cd = new ChartData();
			cd.setColor(color);
			cd.setText(text);
			cd.setValue(val);
			cd.setName(text);
			listData.add(cd);
			// 拼写lege所需数据
			ChartData cd2 = new ChartData();
			cd2.setColor(color);
			cd2.setText(text + "(" + val + "人)");
			cd2.setValue(val);
			cd2.setName(text + "(" + val + "人)");
			listData2.add(cd2);
			i++;
		}

		cm.setListData(listData);
		cm2.setListData(listData2);
		String jsonData = cm.getJsonDataNew();
		String jsonData2 = cm2.getJsonDataNew();
		if(jsonData.length()<5){
			jsonData="[{name : '其他',value : '1',text : '其他',color : '#8B2252'}]";
			jsonData2="[{name : '其他(0人)',value : '1',text : '其他(0人)',color : '#8B2252'}]";
		}
		try {
			response.getWriter().println(jsonData + "---" + jsonData2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：领导查看教师工作总结 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/teaSummaryList.do")
	public ModelAndView teaSummaryList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String dept_id = orgService.getCollegeIdByTeaId(session);
		List<Org> summary_orgs = this.orgService.selectByXueYuanId(dept_id);
		/* session.setAttribute("diec_orgs", orgList); */
		session.setAttribute("summary_orgs", summary_orgs);
		map.put("summary_orgs", summary_orgs);
		return new ModelAndView("leader/teaSummaryList", map);
	}

	/**
	 * 功能： 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/backSummaryList.do")
	public ModelAndView backSummaryList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> summary_orgs = (List<Org>) session
				.getAttribute("summary_orgs");
		List<DirectRecord> summaryList = (List<DirectRecord>) session
				.getAttribute("summaryList");
		List<Teacher> summary_tealist = (List<Teacher>) session
				.getAttribute("summary_tealist");
		String summary_orgname = (String) session
				.getAttribute("summary_orgname");
		map.put("summaryList", summaryList);
		map.put("summary_orgs", summary_orgs);
		map.put("summary_orgname", summary_orgname);
		map.put("summary_tealist", summary_tealist);
		return new ModelAndView("leader/teaSummaryList", map);
	}

	/**
	 * 功能：ajax获取教师指导记录 邢志武2015-03-24
	 * 
	 * */
	@RequestMapping("leader/ajaxTeasSummarys.do")
	public String ajaxTeasSummarys(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String tea_idAnddeptment_id = request
				.getParameter("tea_idAnddeptment_id");
		String cont = "";
		try {
			cont = URLDecoder.decode(tea_idAnddeptment_id, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int a = cont.indexOf("-");
		int b = cont.indexOf("*");
		String tea_id = cont.substring(0, a);
		String deptment_id = cont.substring(a + 1, b);
		/*
		 * session.setAttribute("diec_orgname",
		 * DictionaryService.findOrg(deptment_id).getOrg_name());
		 */
		session.setAttribute("summary_orgname",
				DictionaryService.findOrg(deptment_id).getOrg_name());
		String month = cont.substring(b + 1, cont.length());
		StringBuffer sb = new StringBuffer();
		List<DirectRecord> result;
		if (tea_id.equalsIgnoreCase("请选择")
				& (!deptment_id.equalsIgnoreCase("0"))) {// 如果只选择部门不选择教师的情况
			result = this.directRecordService.getDeptmentDirecRecs(deptment_id,
					month, "1");
		} else {
			if (month.equalsIgnoreCase("") || month == null) {
				result = this.directRecordService
						.getTeaDirecRecsByTeaIdAndDr_type(tea_id, "1");
			} else {
				result = this.directRecordService
						.getTeaDirecRecsByTeaIdAndMonth(tea_id, month, "1");
			}
			result = this.directRecordService.getTeaDirecRecsByTeaIdAndMonth(
					tea_id, month, "1");
		}
		/* session.setAttribute("directList", result); */
		session.setAttribute("summaryList", result);
		for (DirectRecord d : result) {
			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findPracticeTask(d.getPractice_id())
							.getTask_name() + "</td>");
			sb.append("<td><a href='editdirectrecord.do?id=" + d.getId() + "'>"
					+ d.getTitle() + "</a></td>");
			sb.append("<td>"
					+ DateService.TimestampTimeTurnStringTime(d
							.getDirect_time()) + "</td>");
			sb.append("<td>" + d.getTemp2() + "</td>");
			sb.append("<td>" + d.getDirect_place() + "</td>");
			String stuts = d.getStu_id();
			StringBuffer bf = new StringBuffer();
			String[] sts = stuts.split(",");
			for (int i = 0; i < sts.length; i++) {
				String stuName = DictionaryService.findStudent(sts[i])
						.getTrue_name();
				bf.append(stuName + "、");
			}
			bf.deleteCharAt(bf.length() - 1);
			sb.append("<td>" + bf + "</td>");
			sb.append("</tr>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：查询系老师 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/ajaxteacher.do")
	public String ajaxteacher(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		String c = request.getParameter("xibu");
		StringBuffer s = new StringBuffer();
		s.append("<option selected='selected'>请选择</option>");
		if (c != null) {
			List<Teacher> t = teacherService.getTeachersByDeptId(c);
			/* session.setAttribute("diec_tealist", t); */
			session.setAttribute("summary_tealist", t);
			for (Teacher teacher : t) {
				s.append("<option " + "value=" + teacher.getId() + ">"
						+ teacher.getTrue_name() + "</option>");
			}
			try {
				response.getWriter().println(s.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：领导查看工作总结 吴敬国2015-10-11
	 * 
	 * */
	@RequestMapping("leader/editTeaSummary.do")
	public String editTeaSummary(ModelMap modelMap, String id) {
		DirectRecord d = (DirectRecord) directRecordService.selectByID(id);
		modelMap.put("directrecord", d);
		return "leader/teaSummaryEdit";
	}

	/**
	 * 导出学生信息
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("leader/exOutStu.do")
	public ModelAndView exOutStu(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String org_id1 = (String) session.getAttribute("org_id");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// List<Groups> result = groupsService.getGroupsByDeptId(dept_id);
		List<Org> org = this.orgService.selectByXueYuanId(dept_id);
		if (org_id1 != null) {
			map.put("org_id1", org_id1);
		} else {
			map.put("org_id1", "2");
		}
		map.put("org", org);
		return new ModelAndView("leader/extOutStu", map);
	}

	/**
	 * 导出学生信息
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("leader/toExc.do")
	public void toExc(HttpServletRequest request, HttpServletResponse response) {
		String year = request.getParameter("year");
		String org_id = request.getParameter("org_id");
		// 某系的所有老师
		List<Teacher> teaList = this.teacherService.getTeachersByDeptId(org_id);
		List<Student> stulist = new ArrayList<Student>();

		for (int i = 0; i < teaList.size(); i++) {
			String tea_id = teaList.get(i).getId();
			try {
				String pk_id = this.practiceTaskService
						.getPracticeTasksByGradeAndTea_id(tea_id, year).get(0)
						.getId();
				List<String> stu = this.practiceTaskService.selectStusId(pk_id);
				for (int j = 0; j < stu.size(); j++) {
					Student student = DictionaryService.findStudent(stu.get(j));
					student.setTemp3(teaList.get(i).getTrue_name());
					stulist.add(student);
				}
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}
		excelService.writeStuExcel(stulist, request, response);
	}

	/**
	 * 功能：
	 * 
	 * */
	@RequestMapping("leader/leaderUserdownload.do")
	public String templetdownload(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) throws Exception {
		String project_path = request.getSession().getServletContext()
				.getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		String file_type = "Helps";
		String file_type1 = "AdminUser";
		String a = file_type + "/" + file_type1 + "/" + "领导用户手册.doc";
		filesService.downloadfile(a, ctxPath, request, response);
		return null;
	}

	@RequestMapping("leader/signUse.do")
	public ModelAndView test(HttpServletRequest request,HttpSession session, HttpServletResponse response) throws Exception {
		/*Map<String, Object> map = new HashMap<String, Object>();
		List<EchartSeries> liste=new ArrayList<EchartSeries>();
		List<String> orgNameList=new ArrayList<String>();
		List<Org> orgList=this.orgService.selectOrg_Name();
		String x="";
		Map<String, List<String>> result=practiceTaskService.getSinStuSizePro("2013", "dzxxxy", "2015");
		List<String> mouthList=result.get("mouthList");
		for(Org o:orgList){
			orgNameList.add(o.getOrg_name());
			String id=o.getId();
			Map<String, List<String>> resultl=practiceTaskService.getSinStuSizePro("2013", id, "2015");
			List<String> pro=resultl.get("spro");
			EchartSeries e=new EchartSeries();
			e.setName(o.getOrg_name());
			e.setType("line");
			e.setData(pro, mouthList.size());
			liste.add(e);
		}
		x=Common.getXzhou(mouthList);
		String es=Common.getJsonSeries(liste);
		String orgNameJson=Common.getXzhou(orgNameList);
		map.put("e", es);
		map.put("d", x);
		map.put("orgNameJson", orgNameJson);*/
		return new ModelAndView("leader/signUsePro");
	}
	@RequestMapping("leader/ajaxSignUse.do")
	public String ajaxSignUse(HttpServletRequest request,HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		String year=request.getParameter("grade");
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<EchartSeries> liste=new ArrayList<EchartSeries>();
		List<String> orgNameList=new ArrayList<String>();
		List<Org> orgList=this.orgService.selectOrg_Name();
		String x="";
		Map<String, List<String>> result=practiceTaskService.getSinStuSizePro(grade, "dzxxxy", year,null);
		List<String> mouthList=result.get("mouthList");		
		for(Org o:orgList){
			orgNameList.add(o.getOrg_name());
			String id=o.getId();
			Map<String, List<String>> resultl=practiceTaskService.getSinStuSizePro(grade, id, year,mouthList);
			List<String> pro=resultl.get("spro");
			EchartSeries e=new EchartSeries();
			e.setName(o.getOrg_name());
			e.setType("line");
			e.setData(pro, mouthList.size());
			liste.add(e);
		}
		x=Common.getXzhou(mouthList);
		String es=Common.getJsonSeries(liste);
		String orgNameJson=Common.getXzhou(orgNameList);
		map.put("e", es);
		map.put("d", x);
		map.put("orgNameJson", orgNameJson);
		try {
			response.getWriter().println(es+"---"+x+"---"+orgNameJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("leader/SummaryCount.do")
	public String SummaryCount(ModelMap modelMap) {
		return "leader/SummaryCount";
	}

	@RequestMapping("leader/ajaxSummaryCount.do")
	public String ajaxSummaryCount(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		String year = request.getParameter("grade");
		Map<String, Object> map = new HashMap<String, Object>();
		List<EchartSeries> liste = new ArrayList<EchartSeries>();
		List<String> orgNameList = new ArrayList<String>();
		String x = "";
		List<Org> orgList = this.orgService.selectOrg_Name();// 查询所有二级学院
		List<String> mouthList = signService.getMonthByGrade(year);// 月份集合
		for (Org o : orgList) {
			orgNameList.add(o.getOrg_name());
			String id = o.getId();
			List<String> count = signService.getCountByYearAndDpt(grade, id,
					year);
			EchartSeries e = new EchartSeries();
			e.setName(o.getOrg_name());
			e.setType("line");
			e.setData(count, mouthList.size());
			liste.add(e);
		}

		x = Common.getXzhou(mouthList);
		String series = Common.getJsonSeries(liste);
		String orgNameJson = Common.getXzhou(orgNameList);
		map.put("series", series);
		map.put("x", x);
		map.put("orgNameJson", orgNameJson);
		try {
			response.getWriter().println(
					series + "---" + x + "---" + orgNameJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}