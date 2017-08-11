package com.sict.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sict.entity.BMapEntity;
import com.sict.entity.Company;
import com.sict.entity.DirectRecord;
import com.sict.entity.EvalsIndex;
import com.sict.entity.Evaluate;
import com.sict.entity.GroupMembers;
import com.sict.entity.Groups;
import com.sict.entity.InfoCheckRecord;
import com.sict.entity.Knowledge;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.RecruitInfo;
import com.sict.entity.Score;
import com.sict.entity.Student;
import com.sict.entity.SysMenu;
import com.sict.entity.Teacher;
import com.sict.entity.UserRole;
import com.sict.service.CompanyService;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateService;
import com.sict.service.FilesService;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.InfoCheckRecordService;
import com.sict.service.KnowledgeService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RecruitInfoService;
import com.sict.service.ScoreService;
import com.sict.service.SignService;
import com.sict.service.StudentService;
import com.sict.service.SysMenuService;
import com.sict.service.TeacherService;
import com.sict.service.UserRoleService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.CustomMenuForm;
import com.sict.util.DateService;

/*
 * 功能：手机端教师控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class MobileTeacherController {
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
	 * 注入CheckInfoService by张向杨 2015-01-13 *
	 */
	@Resource(name = "checkInfoService")
	private InfoCheckRecordService checkInfoService;
	/**
	 * 注入knowledgeService by岳贤昌20160117 *!!!!!!
	 */
	@Resource(name = "knowledgeService")
	private KnowledgeService knowledgeService;
	/**
	 * 注入SignService by 李达 20160119 *
	 */
	@Resource(name = "signService")
	private SignService signService;
	/**
	 * 注入orgService by 周睿 20160122 *
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * 注入sysMenuService by吴宝20160122 *
	 */
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	/**
	 * 注入userRoleService by吴宝20160122 *
	 * 
	 * */
	@Resource(name = "userRoleService")
	private UserRoleService userRoleService;

	private int pageSizeConstant = Constants.pageSize; // 获取常量分页页大小

	/**
	 * 功能：显示教师主页 by周睿20160314 显示指导教师通知和学院通知
	 * */
	@RequestMapping("MobileTeacher/index.do")
	public ModelAndView toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String  org_name=orgService.selectByID(orgService.selectOrgByTeaId(tea.getId())).getOrg_name();
		List<Notice> noticeList = noticeService.getCollegeNoticeList(session);
		List<Notice> noticeFive = new ArrayList<Notice>();
		int i = 0;
		if(noticeList!=null){
		for (Notice n : noticeList) {
			if (i < 5) {
				noticeFive.add(n);
				i++;
			}
		}}
		Notice t = new Notice();

		t.setTea_id(Common.getOneTeaId(session));
		t.setNotice_type("2");

		List<Notice> result = this.noticeService.selectList(t);// 得到自己发布的通知

		// 分页
		int pageSize = pageSizeConstant;
		int Currentpage = 1;

		// 获取当前页集合
		List<Notice> notices = Common.getListCurrentPage(result,
				pageSize, Currentpage);

		List<Notice> myNoticeFive = new ArrayList<Notice>();
		int a = 0;
		if(notices!=null){
		for (Notice n : notices) {
			if (a < 5) {
				myNoticeFive.add(n);
				a++;
			}
		}}
		map.put("myNoticeFive", myNoticeFive);
		map.put("noticeFive", noticeFive);
		map.put("org_name", org_name);
		return new ModelAndView("/mobileViews/mobileTeacher/index",map);
	}

	@RequestMapping("MobileTeacher/practiceManagementList.do")
	public String topracticeManagementList(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileTeacher/practiceManagementList";
	}

	/*
	 * 获取学院通知和老师通知 author 李龙 赵东华
	 */
	@RequestMapping("MobileTeacher/NoticeList.do")
	public ModelAndView toNoticeList(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {

		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_college_id = tea.getDept_id();

		Map<String, Object> map = new HashMap<String, Object>();

		Notice t = new Notice();

		t.setTea_id(Common.getOneTeaId(session));
		t.setNotice_type("2");

		List<Notice> result = this.noticeService.selectList(t);// 得到自己发布的通知

		// 分页
		int pageSize = pageSizeConstant;
		int Currentpage = 1;

		// 获取当前页集合
		List<Notice> listCurrentPage = Common.getListCurrentPage(result,
				pageSize, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(result, pageSize, Currentpage);
		// 放到session，方便分页时调用

		// session.setAttribute("SelfNoticeList", result);

		map.put("SelfCount", pageCount);
		map.put("SelfnowPage", Currentpage);

		List<Notice> college_NoticeList = noticeService
				.getCollegeNoticeList(session);

		map.put("result", listCurrentPage);
		map.put("college_NoticeList", college_NoticeList);

		return new ModelAndView("/mobileViews/mobileTeacher/NoticeList", map);

	}

	/*
	 * 老师通知、获取通知学生 author 张向杨 苏衍静
	 */
	@RequestMapping("MobileTeacher/NoticeAdd.do")
	public ModelAndView noticeAdd(HttpSession session) {

		Teacher tea = (Teacher) session.getAttribute("current_user");// 获取到当前用户

		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = true;
		// String checkinfo_Grade = Common.getDefaultYear();// 得到默认年份2013
		String tea_id = tea.getId();
		PracticeTask pt = new PracticeTask();
		pt.setTea_id(tea_id);
		pt.setState("1");
		pt.setTask_type("1");
		// 初始化登录老师的对应的实习任务
		List<PracticeTask> checkinfo_pulltaskList = null;
		checkinfo_pulltaskList = (List<PracticeTask>) practiceTaskService
				.selectList(pt);
		// 判断登录老师是否有对应的实习任务
		if (checkinfo_pulltaskList == null) {
			flag = false;
			map.put("flag", flag);
			return new ModelAndView(
					"/mobileViews/mobileTeacher/ReleaseNotice_section", map);
		}

		map.put("checkinfo_pulltaskList", checkinfo_pulltaskList);// 任务列表添加map
		map.put("flag", flag);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/ReleaseNotice_section", map);
	}

	/*
	 * 保存老师发布的通知 author 张向杨 苏衍静
	 */
	@RequestMapping("MobileTeacher/doAddNotice.do")
	public String saveNotice(HttpServletRequest request, HttpSession session)
			throws IOException {
		String practice_id = request.getParameter("praTask");
		String content = request.getParameter("content");
		String title = request.getParameter("title");
		String notice_range = request.getParameter("notice_range");

		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();

		String practice_code = DictionaryService.findPracticeTask(practice_id)
				.getPractice_code();

		String notice_code = "";

		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());

		try {
			time = new Timestamp(format.parse

			(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 生成通知编码 王磊 2015年1月30日
		String notice_cur_code = practice_code + "TZGG";
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

		Notice notice = new Notice();
		String times = DateService.formatNowTimeforUpload();

		notice.setTea_id(tea_id);

		notice.setOrg_id("");
		notice.setCreate_time(time);
		notice.setNotice_code(notice_code);

		notice.setNotice_type("2");
		notice.setContent(content);
		notice.setTitle(title);
		notice.setStu_id(notice_range);

		noticeService.insert(notice);

		return "redirect:NoticeList.do"; // 添加成功后重定向到列表页面
	}

	/*
	 * 功能：ajax获得对应实习任务的学生 时间：２０１６－０３－１０ 作者：张向杨
	 * 
	 * 日记： return 一定要写 null 否则ajax不能执行成功
	 */

	@RequestMapping("MobileTeacher/getStudent.do")
	public String getStudent(HttpServletRequest request, HttpServletResponse response, String practice_id)
			throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		List<GroupMembers> SuList = this.groupMembersService.studentsList(practice_id);//
		// List<Student> studentList = new ArrayList<Student>();
		String StuName = null;
		StringBuffer sb = new StringBuffer();
		for (GroupMembers gm : SuList) {

			if (DictionaryService.findStudent(gm.getUser_id()).getState().equals("1")) {
				StuName = DictionaryService.findStudent(gm.getUser_id()).getTrue_name();

				sb.append("<li class='nopadding'><a href='#' data-role='checkbox'>" + "<label for='" + gm.getUser_id()
						+ "' class='black'>");
				sb.append(StuName + "</label><input type='checkbox' id='" + gm.getUser_id()
						+ "' name='student' value='" + StuName + "'/>");
				sb.append("</a></li>");

			}

		}
		response.getWriter().println(sb.toString());
		return null;
	}


	/*
	 * 显示详细的学院通知和老师通知 author 李龙 赵东华
	 */
	@RequestMapping("MobileTeacher/detailNotice.do")
	public ModelAndView detailNotice(String notice_id, String id,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		Notice t = new Notice();

		// t.setTea_id("5963E281FF8D2A31");
		t.setTea_id(Common.getOneTeaId(session));
		t.setNotice_type("2");
		if (notice_id != null && notice_id.length() > 0) {

			String teacherName = DictionaryService.findTeacher(t.getTea_id())
					.getTrue_name();
			List<Notice> result = this.noticeService.selectList(t);// 得到自己发布的通知
			List<String> StuNameList = new ArrayList<String>();
			for (Notice n : result) {
				if (n.getId().equals(notice_id)) {
					String stuIds = n.getStu_id();
					String[] stu_ids = stuIds.split(",");
					int length = stu_ids.length;
					String studentName=new String();
					for (int i = 0; i < length; i++) {
						System.out.println("----------");
						System.out.println(stuIds);
						System.out.println("----------");

						studentName= DictionaryService.findStudent(
								stu_ids[i]).getTrue_name()
								+ " ";
						StuNameList.add(studentName);
					}
					map.put("notice_info", n);
					break;
				}
			}
			map.put("StuNameList", StuNameList);
			map.put("teacherName", teacherName);
		} else if (id != null && id.length() > 0) {

			List<Notice> college_NoticeList = noticeService
					.getCollegeNoticeList(session);

			for (Notice n : college_NoticeList) {
				if (n.getId().equals(id)) {

					String org_id = n.getOrg_id();

					String LeaderName = DictionaryService.findTeacher(
							n.getTea_id()).getTrue_name();

					String noticeRange = DictionaryService.findOrg(org_id)
							.getOrg_name();
					// System.out.println(noticeRange+"==============="+n.getContent()+"+++++++"+n.getCreate_time()+"----"+noticeRange);
					map.put("collageDetailNoice", n);
					map.put("LeaderName", LeaderName);
					map.put("noticeRange", noticeRange);
					break;
				}

			}

		}

		return new ModelAndView("/mobileViews/mobileTeacher/info_section", map);
	}

	/**
	 * 根据老师ID查询所带分组学生 by周睿20151214
	 * 
	 */
	@RequestMapping("MobileTeacher/InternshipStudentList.do")
	public ModelAndView InternshipStudentList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		List<Groups> groupList = new ArrayList();
		String lastGroupNmae = new String();
		String defaultYear = Common.getDefaultYear();
		for (Groups g : groups) {
			String groupName = g.getGroup_name();
			String task = groupName.substring(5, 7);
			if (task.equals("实习")) {
				groupList.add(g);
				String year = groupName.substring(0, 4);
				if (year.equals(defaultYear)) {
					lastGroupNmae = g.getGroup_name();
				}
			}
		}
		if (groupList.size() == 0) {
			lastGroupNmae = "";
		}
		map.put("lastGroupNmae", lastGroupNmae);
		map.put("groupList", groupList);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/InternshipStudentList", map);
	}

	/**
	 * 根据学生学号，返回学生详细信息 by周睿20151214
	 * 
	 */
	@RequestMapping("MobileTeacher/InternshipStudentList_details.do")
	public ModelAndView toInternshipStudentList_details(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// String code=request.getParameter();
		Map<String, Object> map = new HashMap<String, Object>();
		String stu_code = request.getParameter("code");
		System.out.println(stu_code);
		Student student = (Student) this.studentService.selectByCode(stu_code);
		Org org = orgService.selectByID(student.getClass_id());
		map.put("org", org);
		map.put("student", student);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/InternshipStudentList_details", map);
	}

	/**
	 * 根据教师id，返回教师总结by周睿20151217
	 * 
	 */
	@RequestMapping("MobileTeacher/TeacherSummary.do")
	public ModelAndView toTeacherSummary(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String teaName = tea.getTrue_name();
		String tea_id = tea.getId();
		List<DirectRecord> directRecordList = this.directRecordService
				.getTeaDirecRecsByTeaIdAndDr_type(tea_id, "1");
		map.put("teaName", teaName);
		map.put("directRecordList", directRecordList);
		return new ModelAndView("/mobileViews/mobileTeacher/TeacherSummary",
				map);
	}

	/**
	 * 显示教师总结详情by周睿20151217
	 * 
	 */
	@RequestMapping("MobileTeacher/TeacherSummary_details.do")
	public ModelAndView toTeacherSummary_details(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String DirectRecord_id = request.getParameter("id");
		System.out.println(DirectRecord_id);
		DirectRecord directRecord = (DirectRecord) this.directRecordService
				.selectById(DirectRecord_id);

		map.put("directRecord", directRecord);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/TeacherSummary_details", map);
	}

	/**
	 * 进入添加教师总结页面by周睿20151217
	 * 
	 */
	@RequestMapping("MobileTeacher/AddTeacherSummary.do")
	public ModelAndView toAddTeacherSummary(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		List<Groups> groupList = new ArrayList();
		String lastPractice_id = new String();
		String defaultYear = Common.getDefaultYear();
		for (Groups g : groups) {
			String groupName = g.getGroup_name();
			String task = groupName.substring(5, 7);
			if (task.equals("实习")) {
				groupList.add(g);
				String year = groupName.substring(0, 4);
				if (year.equals(defaultYear)) {
					lastPractice_id = g.getPractice_id();
				}
			}
		}
		if (groupList.size() == 0) {
			lastPractice_id = "";
		}
		map.put("lastPractice_id", lastPractice_id);
		map.put("groupList", groupList);
		map.put("tea", tea);

		return new ModelAndView("/mobileViews/mobileTeacher/AddTeacherSummary",
				map);
	}

	/**
	 * 添加教师总结by周睿20151217
	 * 
	 * */
	@RequestMapping("MobileTeacher/doAddTeacherSummary.do")
	public String doAddTeacherSummary(HttpSession session,
			HttpServletRequest request) throws IOException {
		String title = request.getParameter("title");
		String direct_place = request.getParameter("direct_place");
		String direct_time = request.getParameter("direct_time");
		String temp2 = request.getParameter("temp2");
		// String stu_ids = request.getParameter("stu_ids");
		String direct_desc = request.getParameter("direct_desc");
		String stu_id = request.getParameter("stu_id");
		/* String worktype = request.getParameter("worktype"); */
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String practice_id = request.getParameter("practice_id");
		Groups group = groupsService.selectGroupsByPraid(practice_id);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		DirectRecord directRecord = new DirectRecord();
		// 检查form中是否有enctype="multipart/form-data"
		directRecord.setId(Common.returnUUID());
		directRecord.setPractice_id(practice_id);
		directRecord.setTitle(title);
		directRecord.setTemp1("1");// 工作总结类型
		directRecord.setTemp2(temp2);// 指导结束时间
		directRecord.setCreate_time(DateService.getNowTimeTimestamp());// 创建时间
		directRecord.setDirect_place(direct_place);
		directRecord.setStu_id(stu_id);
		directRecord.setDirect_desc(direct_desc);
		if (direct_time == null) {
			direct_time = "2010-01-01";
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(direct_time).getTime());// 时间类型可以封装
		} catch (ParseException e) {
			e.printStackTrace();
		}

		directRecord.setDirect_time(ts);
		directRecord.setOrg_id(group.getId());
		// directRecord.setStu_id(stu_ids);
		directRecord.setDirect_desc(direct_desc);
		directRecordService.insert(directRecord);
		return "redirect:TeacherSummary.do";
	}

	/**
	 * 功能：老师编辑总结 by周睿20151217
	 * */
	@RequestMapping("MobileTeacher/doEditTeacehrSummary.do")
	public String doEditTeacehrSummary(HttpSession session,
			HttpServletRequest request) throws IOException {
		String id = (String) request.getParameter("id");
		String title = (String) request.getParameter("title");
		String direct_place = (String) request.getParameter("direct_place");
		String direct_desc = (String) request.getParameter("direct_desc");
		DirectRecord directRecord = new DirectRecord();
		directRecord.setId(id);
		directRecord.setTitle(title);
		directRecord.setDirect_place(direct_place);
		directRecord.setDirect_desc(direct_desc);
		directRecordService.update(directRecord);
		return "redirect:TeacherSummary.do";
	}

	/**
	 * 功能：老师删除总结 by周睿20151218
	 * 
	 * */
	@RequestMapping("MobileTeacher/deleteTeacherSummary.do")
	public String deleteTeacherSummary(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String DirectRecord_id = (String) request.getParameter("id");
		DirectRecord d = (DirectRecord) directRecordService
				.selectById(DirectRecord_id);
		String file_id = d.getFile_id();

		if (file_id != "") {
			filesService.deleteFile(file_id, request);
		}
		directRecordService.delete(DirectRecord_id);
		return "redirect:TeacherSummary.do";
	}

	/**
	 * 得到各种情况下的实习申请记录.
	 * 
	 * @author 丁乐晓
	 * @createDate 2015-12-12
	 * @since
	 * 
	 */
	@RequestMapping("MobileTeacher/internshipApproval.do")
	public ModelAndView internshipApproval(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PracticeRecord> modelList = new ArrayList<PracticeRecord>();
		// 得到默认的年级
		String checkTask_grade = Common.getDefaultYear();
		// 根据教师id和入学年份获得实习任务
		List<PracticeTask> checkTaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(Common.getOneTeaId(session),
						checkTask_grade);
		String FirstTaskName = null;
		String praticeTaskId = null;
		String default_TaskName;
		String check_state = null;
		if (checkTaskList != null && checkTaskList.size() != 0) {
			// 取得第一个任务的id
			praticeTaskId = checkTaskList.get(0).getId();
			// 取得第一个的任务名
			FirstTaskName = checkTaskList.get(0).getTask_name();
		}
		// 获得审批申请的所有值
		List<PracticeRecord> prList = this.practiceRecordService
				.selectPracticeRecordByPracticeTaskId(praticeTaskId);
		// 将常用值存到session
		session.setAttribute("checkTask_grade", checkTask_grade);// 审批实习默认的年级
		session.setAttribute("praticeTaskId", praticeTaskId);// 默认实习ID
		session.setAttribute("FirstTaskName", FirstTaskName);// 审批实习默认的任务名
		session.setAttribute("checkTaskList", checkTaskList);// 实习任务列表
		// 从session中获取当前显示的年级
		String checkPraTask_grade = (String) session
				.getAttribute("checkTask_grade");
		// 获取默认的任务名
		default_TaskName = (String) session.getAttribute("FirstTaskName");
		// 判断实习任务id为null的情况
		if (praticeTaskId == null) {
			// 从session中获取任务的id
			praticeTaskId = (String) session
					.getAttribute("checkPraticeTask_taskId");
			// 从数据字典中获取到该任务的任务名
			default_TaskName = DictionaryService
					.findPracticeTask(praticeTaskId).getTask_name();
			// 将该任务名放到session中
			session.setAttribute("default_TaskName", default_TaskName);
		} else if (praticeTaskId != null) {
			session.setAttribute("checkPraticeTask_taskId", praticeTaskId);
			default_TaskName = DictionaryService
					.findPracticeTask(praticeTaskId).getTask_name();
			session.setAttribute("default_TaskName", default_TaskName);
		}
		// 定义三个数组，分别存放未审核，通过，未通过。
		List<PracticeRecord> noCheck = new ArrayList<PracticeRecord>();// 存放未审核
		List<PracticeRecord> pass = new ArrayList<PracticeRecord>();// 存放通过
		List<PracticeRecord> noPass = new ArrayList<PracticeRecord>();// 存放未通过
		for (PracticeRecord pr : prList) {
			check_state = pr.getCheck_state();
			if (check_state.equals("0")) {
				if (pr.getCheck_state().equals("0")
						&& pr.getDimission_time() == null) {
					noCheck.add(pr);
				}
			} else if (check_state.equals("1")) {
				if (pr.getCheck_state().equals("1")) {
					pass.add(pr);
				}
			} else if (check_state.equals("2")) {
				if (pr.getCheck_state().equals("2")) {
					noPass.add(pr);
				}
			}

		}
		map.put("noCheck", noCheck);
		map.put("pass", pass);
		map.put("noPass", noPass);
		map.put("default_TaskName", default_TaskName);
		map.put("checkPraTask_grade", checkPraTask_grade);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/internshipApproval", map);
	}

	/**
	 * 查看实习申请详情 审核申请具体内容和审核公司
	 * 
	 * @author 丁乐晓
	 * @createDate 2015-12-12
	 * 
	 */
	@RequestMapping("MobileTeacher/practiceManagement.do")
	public String practiceManagement(HttpSession session, ModelMap modelMap,
			String id) {
		// 得到这条实习就业记录
		PracticeRecord practicerecord = (PracticeRecord) practiceRecordService
				.selectByID(id);
		// 获取到当前教师
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 根据企业id得到该企业
		Company company = (Company) companyService.selectByID(practicerecord
				.getCompany_id());
		// 得到创建企业的学生id
		String createStu_id = companyService
				.selectStuIdByCompanyId(practicerecord.getCompany_id());
		// 得到审核企业的教师id
		String CheckMan = companyService.selectCheckMan(practicerecord
				.getCompany_id());
		String CheckManName;
		// 判断审核人为null的情况
		if (CheckMan == null) {
			CheckMan = tea.getId();
			// 根据字典查找该审核人的名字
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			// 将审核人的名字存到session中
			session.setAttribute("checkstucompanyTea", CheckMan);
		} else {
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			session.setAttribute("checkstucompanyTea", CheckMan);
		}
		// 保留现在是哪种状态下（未审核，通过，未通过）
		String check_state = (String) session.getAttribute("check_state");
		// 将页面需要的数据存到modelMap
		modelMap.put("check_state", check_state);
		modelMap.put("state", company.getCheck_state());
		modelMap.put("tea_id", tea.getId());
		modelMap.put("CheckManName", CheckManName);
		modelMap.put("stu_id", practicerecord.getStu_id());
		modelMap.put("company", company);
		modelMap.put("createStu_id", createStu_id);
		modelMap.put("practicerecord", practicerecord);
		return "/mobileViews/mobileTeacher/practiceManagement";
	}

	/**
	 * 查看实习申请详情 审核申请具体内容和审核公司
	 * 
	 * @author 丁乐晓
	 * @createDate 2015-12-12
	 * 
	 */
	@RequestMapping("MobileTeacher/practiceManagementYES.do")
	public String practiceManagementYES(HttpSession session, ModelMap modelMap,
			String id) {
		// 得到这条实习就业记录
		PracticeRecord practicerecord = (PracticeRecord) practiceRecordService
				.selectByID(id);
		// 获取到当前教师
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 根据企业id得到该企业
		Company company = (Company) companyService.selectByID(practicerecord
				.getCompany_id());
		// 得到创建企业的学生id
		String createStu_id = companyService
				.selectStuIdByCompanyId(practicerecord.getCompany_id());
		// 得到审核企业的教师id
		String CheckMan = companyService.selectCheckMan(practicerecord
				.getCompany_id());
		String CheckManName;
		// 判断审核人为null的情况
		if (CheckMan == null) {
			CheckMan = tea.getId();
			// 根据字典查找该审核人的名字
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			// 将审核人的名字存到session中
			session.setAttribute("checkstucompanyTea", CheckMan);
		} else {
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			session.setAttribute("checkstucompanyTea", CheckMan);
		}
		// 保留现在是哪种状态下（未审核，通过，未通过）
		String check_state = (String) session.getAttribute("check_state");
		// 将页面需要的数据存到modelMap
		modelMap.put("check_state", check_state);
		modelMap.put("state", company.getCheck_state());
		modelMap.put("tea_id", tea.getId());
		modelMap.put("CheckManName", CheckManName);
		modelMap.put("stu_id", practicerecord.getStu_id());
		modelMap.put("company", company);
		modelMap.put("createStu_id", createStu_id);
		modelMap.put("practicerecord", practicerecord);
		return "/mobileViews/mobileTeacher/practiceManagementYES";
	}

	/**
	 * 
	 * @param bylql
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("MobileTeacher/practiceManagementNO.do")
	public String practiceManagementNO(HttpSession session, ModelMap modelMap,
			String id) {
		// 得到这条实习就业记录
		PracticeRecord practicerecord = (PracticeRecord) practiceRecordService
				.selectByID(id);
		// 获取到当前教师
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 根据企业id得到该企业
		Company company = (Company) companyService.selectByID(practicerecord
				.getCompany_id());
		// 得到创建企业的学生id
		String createStu_id = companyService
				.selectStuIdByCompanyId(practicerecord.getCompany_id());
		// 得到审核企业的教师id
		String CheckMan = companyService.selectCheckMan(practicerecord
				.getCompany_id());
		String CheckManName;
		// 判断审核人为null的情况
		if (CheckMan == null) {
			CheckMan = tea.getId();
			// 根据字典查找该审核人的名字
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			// 将审核人的名字存到session中
			session.setAttribute("checkstucompanyTea", CheckMan);
		} else {
			CheckManName = DictionaryService.findTeacher(CheckMan)
					.getTrue_name();
			session.setAttribute("checkstucompanyTea", CheckMan);
		}
		// 保留现在是哪种状态下（未审核，通过，未通过）
		String check_state = (String) session.getAttribute("check_state");
		// 将页面需要的数据存到modelMap
		modelMap.put("check_state", check_state);
		modelMap.put("state", company.getCheck_state());
		modelMap.put("tea_id", tea.getId());
		modelMap.put("CheckManName", CheckManName);
		modelMap.put("stu_id", practicerecord.getStu_id());
		modelMap.put("company", company);
		modelMap.put("createStu_id", createStu_id);
		modelMap.put("practicerecord", practicerecord);
		return "/mobileViews/mobileTeacher/practiceManagementNO";
	}

	/*
	 * 审核详情和公司 author:丁乐晓 time：2015-12-23
	 */
	@RequestMapping("MobileTeacher/check.do")
	public void check(HttpServletRequest request, HttpServletResponse response,
			String nonote, HttpSession session) throws IOException {
		// 更新学生申请表信息
		String check_state1 = request.getParameter("check_state");
		// 实习就业记录表的id
		String practiceRecordId = request.getParameter("id");
		// 如果不需要格式,可直接用dt,dt就是当前系统时
		Date dt = new Date();
		// 设置显示格式
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			time = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String checkstucompanyTea;
		// 根据字典查到申请记录
		PracticeRecord pr = DictionaryService
				.findPracticeRecord(practiceRecordId);
		// 向申请记录中存入审核的属性
		pr.setId(practiceRecordId);
		pr.setCheck_state(check_state1);
		pr.setNote(nonote);
		pr.setCheck_time(time);
		// 更新实习申请记录
		this.practiceRecordService.update(pr);
		// 更新公司审核信息
		String check_state = request.getParameter("compayCheckState");
		// 得到公司的id
		String companyId = request.getParameter("companyId");
		// 得到审核教师的id
		String tea_id = request.getParameter("checkTeaID");
		// 判断审核教师的id为空的情况
		if (tea_id.equalsIgnoreCase("无")) {
			checkstucompanyTea = tea_id;
		} else {
			checkstucompanyTea = (String) session
					.getAttribute("checkstucompanyTea");
		}
		// 取得审核意见
		String note2 = request.getParameter("companyCheckNote");
		// 得到审核的公司
		Company company = DictionaryService.findCompany(companyId);
		// 给公司更新一些属性值
		company.setId(companyId);
		company.setCheck_man(checkstucompanyTea);
		company.setCheck_state(check_state);
		company.setCheck_note(note2);
		// 公司状态改为2不可用（申请时查询不到）
		company.setState(check_state);
		company.setCreate_time(time);
		// 将更新的公司存到数据库
		this.companyService.update(company);

		/* 审批完实习申请之后发一条通知 */
		Notice notice = new Notice();
		Teacher tea = DictionaryService.findTeacher(DictionaryService
				.findPracticeTask(pr.getPractice_id()).getTea_id());
		String notice_code = noticeService.getNoticeCode(pr.getPractice_id(),
				pr.getStu_id());
		notice.setTitle("实习申请已审批的通知");
		String noticeContent = "你的实习申请已经审批,请及时查看自己的审批结果。";
		notice.setContent(noticeContent);
		notice.setNotice_type("8");
		notice.setCreate_time(DateService.getNowTimeTimestamp());
		notice.setNotice_code(notice_code);
		notice.setStu_id(pr.getStu_id());
		notice.setTea_id(tea.getId());
		noticeService.insert(notice);

		response.sendRedirect("internshipApproval.do?check_state="
				+ check_state1);
		// return
		// "redirect:/MobileTeacher/internshipApproval.do?check_state="+check_state1;
	}

	/**
	 * 进入教师指导记录页面by周睿20151229
	 * 
	 */
	@RequestMapping("MobileTeacher/GuideRecordManagement.do")
	public ModelAndView GuideRecordManagement(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String teaName = tea.getTrue_name();
		List<DirectRecord> directRecordList = this.directRecordService
				.getTeaDirecRecsByTeaIdAndDr_type(tea_id, "2");
		map.put("teaName", teaName);
		map.put("directRecordList", directRecordList);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/GuideRecordManagement", map);
	}

	/**
	 * 显示教师指导记录详情by周睿20151229
	 * 
	 */
	@RequestMapping("MobileTeacher/GuideRecordManagement_details.do")
	public ModelAndView GuideRecordManagement_details(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String DirectRecord_id = request.getParameter("id");
		System.out.println(DirectRecord_id);
		DirectRecord directRecord = (DirectRecord) this.directRecordService
				.selectById(DirectRecord_id);

		map.put("directRecord", directRecord);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/GuideRecordManagement_details", map);
	}

	/**
	 * 进入添加教师指导记录页面by周睿20151217
	 * 
	 */
	@RequestMapping("MobileTeacher/addGuideRecordManagement.do")
	public ModelAndView addGuideRecordManagement(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		List<Groups> groupList = new ArrayList();
		String lastPractice_id = new String();
		String defaultYear = Common.getDefaultYear();
		for (Groups g : groups) {
			String groupName = g.getGroup_name();
			String task = groupName.substring(5, 7);
			if (task.equals("实习")) {
				groupList.add(g);
				String year = groupName.substring(0, 4);
				if (year.equals(defaultYear)) {
					lastPractice_id = g.getPractice_id();
				}

			}
		}
		if (groupList.size() == 0) {
			lastPractice_id = "";
		}
		map.put("lastPractice_id", lastPractice_id);
		map.put("groupList", groupList);
		map.put("tea", tea);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/GuideRecordManagement_add", map);
	}

	/**
	 * 添加教师指导记录by周睿20151217
	 * 
	 * */
	@RequestMapping("MobileTeacher/doAddGuideRecordManagement.do")
	public String doAddGuideRecordManagement(HttpSession session,
			HttpServletRequest request) throws IOException {
		String title = request.getParameter("title");
		String direct_place = request.getParameter("direct_place");
		String direct_time = request.getParameter("direct_time");
		String temp2 = request.getParameter("temp2");
		// String stu_ids = request.getParameter("stu_ids");
		String direct_desc = request.getParameter("direct_desc");
		String stu_id = request.getParameter("stu_id");
		/* String worktype = request.getParameter("worktype"); */
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String practice_id = request.getParameter("practice_id");
		Groups group = groupsService.selectGroupsByPraid(practice_id);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		DirectRecord directRecord = new DirectRecord();
		// 检查form中是否有enctype="multipart/form-data"
		directRecord.setId(Common.returnUUID());
		directRecord.setPractice_id(practice_id);
		directRecord.setTitle(title);
		directRecord.setTemp1("2");// 工作总结类型
		directRecord.setTemp2(temp2);// 指导结束时间
		directRecord.setCreate_time(DateService.getNowTimeTimestamp());// 创建时间
		directRecord.setDirect_place(direct_place);
		directRecord.setStu_id(stu_id);
		directRecord.setDirect_desc(direct_desc);
		if (direct_time == null) {
			direct_time = "2010-01-01";
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(direct_time).getTime());// 时间类型可以封装
		} catch (ParseException e) {
			e.printStackTrace();
		}

		directRecord.setDirect_time(ts);
		directRecord.setOrg_id(group.getId());
		// directRecord.setStu_id(stu_ids);
		directRecord.setDirect_desc(direct_desc);
		directRecordService.insert(directRecord);
		return "redirect:GuideRecordManagement.do";
	}

	/**
	 * 功能：老师编辑教师指导记录 by周睿20151217
	 * */
	@RequestMapping("MobileTeacher/doEditGuideRecordManagement.do")
	public String doEditGuideRecordManagement(HttpSession session,
			HttpServletRequest request) throws IOException {
		String id = (String) request.getParameter("id");
		String title = (String) request.getParameter("title");
		String direct_place = (String) request.getParameter("direct_place");
		String direct_desc = (String) request.getParameter("direct_desc");
		DirectRecord directRecord = new DirectRecord();
		directRecord.setId(id);
		directRecord.setTitle(title);
		directRecord.setDirect_place(direct_place);
		directRecord.setDirect_desc(direct_desc);
		directRecordService.update(directRecord);
		return "redirect:GuideRecordManagement.do";
	}

	/**
	 * 功能：老师删除教师指导记录 by周睿20151218
	 * 
	 * */
	@RequestMapping("MobileTeacher/deleteGuideRecordManagement.do")
	public String deleteGuideRecordManagement(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String DirectRecord_id = (String) request.getParameter("id");
		DirectRecord d = (DirectRecord) directRecordService
				.selectById(DirectRecord_id);
		String file_id = d.getFile_id();

		if (file_id != "") {
			filesService.deleteFile(file_id, request);
		}
		directRecordService.delete(DirectRecord_id);
		return "redirect:GuideRecordManagement.do";
	}

	/**
	 * 功能：招聘信息列表的显示 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/RecruitmentInformationList.do")
	public ModelAndView toRecruitmentInformationList(
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		// String dept_id = "6ab3542126175a8aa5295668f7d910d1";
		String college_id = dept_id;
		String org_level = DictionaryService.findOrg(college_id).getOrg_level();
		if (org_level.equals("3")) {
			college_id = DictionaryService.findOrg(college_id).getParent_id();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		RecruitInfo ri = new RecruitInfo();
		ri.setCollege_id(college_id);
		ri.setState("1");
		List<RecruitInfo> recruitInfoList = this.recruitInfoService
				.selectList(ri);
		map.put("result", recruitInfoList);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/RecruitmentInformationList", map);
	}

	/**
	 * 功能：某个招聘信息的详细内容显示 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/RecruitmentInformationList_section.do")
	public ModelAndView topracticeManagementList_section(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		RecruitInfo recruitInfo = DictionaryService.findRecruitInfo(id);
		session.setAttribute("recruitInfo_id", id);
		map.put("recruitInfo", recruitInfo);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/RecruitmentInformationList_section",
				map);
	}

	/**
	 * 功能：选择学生，将招聘信息推送到学生 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/SendInforToStudents.do")
	public ModelAndView toSendInforToStudents(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");// 获得招聘信息的id
		RecruitInfo ri = (RecruitInfo) recruitInfoService.selectByID(id);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();

		PracticeTask pt = new PracticeTask();
		pt.setTea_id(tea_id);
		pt.setTask_type("1");
		pt.setState("1");
		List<PracticeTask> pts = this.practiceTaskService.selectList(pt);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pts.size(); i++) {
			sb.append("<li class='listitem'> "
					+ "<input type='hidden' name='practice_id' value='"
					+ pts.get(i).getId() + "'></input>"
					+ "<a href='#' data-role='radio'>"
					+ " <label for='PracticeTask" + i + "' class='black'>"
					+ pts.get(i).getTask_name() + "</label> "
					+ "<input type='radio' onchange=\"state('"
					+ pts.get(i).getId() + "')\"/>" + "</a>");
			List<GroupMembers> SuList = groupMembersService
					.selectStuIdListByPracticeId(pts.get(i).getId());
			List<String> list_stu = new ArrayList<String>();
			String state;
			for (int j = 0; j < SuList.size(); j++) {
				state = DictionaryService.findStudent(
						SuList.get(j).getUser_id()).getPractice_state();
				if (state.equals("100")) {
					list_stu.add(SuList.get(j).getUser_id());
				}
			}
			if (list_stu.size() != 0) {
				sb.append("<ul class='listitem' id='" + pts.get(i).getId()
						+ "' >");
				for (String l : list_stu) {
					sb.append("<li class='nopadding disable outline'><a href='#' data-role='checkbox'><label for='basketball' class='black'>"
							+ DictionaryService.findStudent(l).getTrue_name()
							+ "</label><input type='checkbox'  id='PracticeTask"
							+ i + "' value='" + l + "' /></a></li>");
				}
				sb.append("</ul>");
			}

			sb.append("</li>");

		}
		map.put("badyhtml", sb);
		map.put("ri", ri);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/SendInforToStudents", map);
	}

	//
	/**
	 * 功能：发布招聘信息 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/doAddRecruInfo.do")
	public String doAddRecruInfo(HttpServletRequest request, HttpSession session)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String practice_id = request.getParameter("practice_id");
		String practice_code = DictionaryService.findPracticeTask(practice_id)
				.getPractice_code();
		String notice_code = "";

		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			time = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 生成通知编码 王磊 2015年1月30日
		String notice_cur_code = practice_code + "ZPXX";
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
		String content = request.getParameter("content");
		String notice_range = request.getParameter("notice_range");
		String notice_title = "招聘信息通知：" + time;
		Notice notice = new Notice();
		notice.setTea_id(tea_id);
		notice.setOrg_id("");
		notice.setCreate_time(time);
		notice.setNotice_code(notice_code);
		notice.setNotice_type("5");
		notice.setContent(content);
		notice.setTitle(notice_title);
		notice.setStu_id(notice_range);
		String recruitInfo_id = (String) session.getAttribute("recruitInfo_id");
		notice.setTemp1(recruitInfo_id);
		noticeService.insert(notice);
		return "redirect:sendDetails.do";
	}

	/**
	 * 功能：查看某个招聘信息的已发布通知 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/sendDetails.do")
	public ModelAndView sendDetails(HttpSession session,
			HttpServletRequest request) {
		String recruitInfo_id = request.getParameter("recruitInfo_id");
		if (recruitInfo_id == null) {
			recruitInfo_id = (String) session.getAttribute("recruitInfo_id");
		}
		String tea_id = Common.getOneTeaId(session);
		List<Notice> noticeList = noticeService.selectNoticeByRecruitId(
				recruitInfo_id, tea_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noticeList", noticeList);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/sendInfoNotiesList", map);
	}

	/**
	 * 功能：显示奖惩信息 by周睿201512229
	 * 
	 * */
	@RequestMapping("MobileTeacher/RewardPunishmentList.do")
	public ModelAndView RewardPunishmentList(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String tea_name = tea.getTrue_name();
		Evaluate ev = new Evaluate();
		ev.setTea_id(tea_id);
		List<Evaluate> evs = evaluateService.ListById(tea_id);
		EvalsIndex ei = new EvalsIndex();
		List<EvalsIndex> eiList = evalsIndexService.selectList(ei);
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		List<Groups> groupList = new ArrayList();
		String lastPractice_id = new String();
		String defaultYear = Common.getDefaultYear();
		for (Groups g : groups) {
			String groupName = g.getGroup_name();
			String task = groupName.substring(5, 7);
			if (task.equals("实习")) {
				groupList.add(g);
				String year = groupName.substring(0, 4);
				if (year.equals(defaultYear)) {
					lastPractice_id = g.getPractice_id();
				}
			}
		}
		if (groupList.size() == 0) {
			lastPractice_id = "";
		}
		map.put("lastPractice_id", lastPractice_id);
		map.put("groupList", groupList);
		map.put("tea_name", tea_name);
		map.put("evs", evs);
		map.put("eiList", eiList);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/RewardPunishmentList", map);
	}

	/**
	 * 功能：显示奖惩信息详细信息 by周睿20151230
	 * 
	 * */
	@RequestMapping("MobileTeacher/RewardPunishmentList_details.do")
	public ModelAndView RewardPunishmentList_details(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String ev_id = request.getParameter("id");
		Evaluate e = (Evaluate) evaluateService.selectByID(ev_id);

		String index_name = DictionaryService.findEvalsIndex(e.getIndex_id())
				.getIndex_name();
		Student stu = DictionaryService.findStudent(e.getStu_id());
		String class_name = DictionaryService.findOrg(stu.getClass_id())
				.getOrg_name();
		String Year = stu.getGroup_id().substring(0, 4);
		map.put("e", e);
		map.put("Year", Year);
		map.put("index_name", index_name);
		map.put("stu", stu);
		map.put("class_name", class_name);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/RewardPunishmentList_details", map);
	}

	/**
	 * 功能：老师删除奖惩信息详细信息 by周睿20151230
	 * 
	 * */
	@RequestMapping("MobileTeacher/deleteRewardPunishment.do")
	public String deleteRewardPunishment(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String ev_id = (String) request.getParameter("id");
		Evaluate e = (Evaluate) evaluateService.selectByID(ev_id);

		evaluateService.delete(e);
		return "redirect:RewardPunishmentList.do";
	}

	/**
	 * 功能：老师添加奖惩信息详细信息 by周睿20151230
	 * 
	 * */
	@RequestMapping("MobileTeacher/doAddRewardPunishment.do")
	public String doAddRewardPunishment(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Evaluate e = new Evaluate();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String time = request.getParameter("time");
		String place = request.getParameter("place");
		String ei_id = request.getParameter("ei_id");
		String description = request.getParameter("description");
		String practice_id = request.getParameter("practice_id");
		EvalsIndex ei = (EvalsIndex) evalsIndexService.selectByID(ei_id);
		String standard_id = ei.getStandard_id();
		String index_id = ei.getId();
		double score = ei.getScore();
		if (time == null) {
			time = "2016-01-01";
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(time).getTime());// 时间类型可以封装
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		e.setTime(ts);
		e.setPlace(place);
		e.setStandard_id(standard_id);
		e.setIndex_id(index_id);
		e.setDescription(description);
		e.setPractice_id(practice_id);
		e.setScore(score);
		e.setTea_id(tea_id);
		String str = request.getParameter("stu_id");
		String[] arr = str.split(",");
		for (int i = 0; i < arr.length; i++) {
			e.setId(Common.returnUUID());
			e.setStu_id(arr[i]);
			evaluateService.insert(e);

		}

		return "redirect:RewardPunishmentList.do";
	}

	/**
	 * 功能：跳转到个人信息页面 by jiajianchang 20151218
	 * 
	 * */

	@RequestMapping("MobileTeacher/PersonalInformation.do")
	public String to_PersonalInformation(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileTeacher/PersonalInformation";
	}

	/**
	 * 功能：跳转到修改密码页面 by jiajianchang 20151218
	 * 
	 * */
	@RequestMapping("MobileTeacher/ChangePassword.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		map.put("teacher", teacher);
		return new ModelAndView("/mobileViews/mobileTeacher/ChangePassword",
				map);
	}

	/**
	 * 功能：判断密码是否匹配 by jiajianchang 20151218
	 * 
	 * @throws IOException
	 * 
	 * */
	@RequestMapping("MobileTeacher/ajax_check_login_pass_Teacher.do")
	public ModelAndView ajax_check_login_pass_Teacher(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 获取当前用户
		String login_pass_old = request.getParameter("login_pass_old");
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		String err = null;
		String stare = "false";
		if (!teacher.getLogin_pass().equalsIgnoreCase(login_pass_old)) {
			stare = "false";
		} else {
			stare = "true";
		}
		JSONObject json = new JSONObject();
		json.put("state", stare);

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(json.toString());

		return null;
	}

	/**
	 * 功能：修改密码 by jiajianchang 20151218
	 * 
	 * @throws IOException
	 * 
	 * */
	@RequestMapping("MobileTeacher/doPasswordEdit.do")
	public String doPasswordEdit(HttpSession session, String tea_id,
			HttpServletRequest request) throws IOException {
		String password = request.getParameter("login_pass_new_two");
		Teacher teacher = DictionaryService.findTeacher(tea_id);
		teacher.setLogin_pass(password);
		teacherService.update(teacher);
		session.removeAttribute("current_user");
		return "/mobileViews/mobileTeacher/ChangeSuccess";
	}

	/*
	 * 功能：查看实训成绩 by 李达 2016-01-13
	 */
	@RequestMapping("MobileTeacher/InternshipTaskViewList.do")
	public ModelAndView InternshipTaskViewList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获取当前用户
		String tea_id = tea.getId();
		List<PracticeTask> result = this.practiceTaskService
				.selectOutSchoolPracticeTasks(tea_id);
		map.put("result", result);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/InternshipTaskViewList", map);
	}

	/**
	 * 功能：ajax传递学生信息 by李达 2016-01-13 *
	 * 
	 * */
	@RequestMapping("MobileTeacher/ajaxSerchStus.do")
	public String ajaxSerchStus(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String practice_id = request.getParameter("practice_id");
		String month = request.getParameter("month");
		String thesis = request.getParameter("thesis");
		String evaluate = request.getParameter("evaluate");
		double dblMonth = Double.parseDouble(month);
		double dblThesis = Double.parseDouble(thesis);
		double dblEvaluate = Double.parseDouble(evaluate);
		PracticeTask p = DictionaryService.findPracticeTask(practice_id);
		p.setWeight_summary(dblMonth);
		p.setWeight_evaluate(dblEvaluate);
		p.setWeight_thesis(dblThesis);
		this.practiceTaskService.update(p);
		List<Score> stusScore = new ArrayList<Score>();
		List<String> stusId = this.practiceTaskService
				.selectStusId(practice_id);
		Score s = new Score();
		for (int i = 0; i < stusId.size(); i++) {
			s = this.ScoreService.getStuScore(practice_id, stusId.get(i));
			stusScore.add(s);
		}
		StringBuffer allStusScore = new StringBuffer();
		allStusScore.append("</ul>");
		for (Score oneStuScore : stusScore) {
			String class_id = DictionaryService.findStudent(
					oneStuScore.getStu_id()).getClass_id();
			DecimalFormat df = new DecimalFormat("######0");
			allStusScore
					.append("<li><label href='InternshipTaskViewList_details.do?id="
							+ DictionaryService.findStudent(
									oneStuScore.getStu_id()).getStu_code()
							+ "&practice_id="
							+ practice_id
							+ "&month="
							+ month
							+ "&thesis="
							+ thesis
							+ "&evaluate="
							+ evaluate
							+ "' data-toggle='html'>");
			allStusScore.append("<div class='text'>");
			allStusScore.append(DictionaryService.findStudent(
					oneStuScore.getStu_id()).getTrue_name()
					+ "<i class='right iconfont iconline-fav'></i>");
			allStusScore.append("<small>总成绩"
					+ df.format(oneStuScore.getScore()) + "</small>");
			allStusScore.append("</label></td></li>");
		}
		allStusScore.append("</ul>");
		try {
			response.getWriter().println(allStusScore.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax查询权重 系by 李达 20160122 *
	 * 
	 * */
	@RequestMapping("MobileTeacher/ajaxGetWeight.do")
	@ResponseBody
	public PracticeTask ajaxGetWeight(HttpServletRequest request)
			throws IOException {
		String prac_id = request.getParameter("practice_id");
		PracticeTask p = DictionaryService.findPracticeTask(prac_id);
		return p;
	}

	/**
	 * 功能：查找学生详细信息 by 李达 2016-01-13
	 * 
	 * */
	@RequestMapping("MobileTeacher/InternshipTaskViewList_details.do")
	public ModelAndView InternshipTaskViewList_details(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String stu_code = request.getParameter("id");
		Student student = (Student) this.studentService.selectByCode(stu_code);
		String id1 = student.getId();
		String class_name = DictionaryService.findOrg(student.getClass_id())
				.getOrg_name();
		String practice_id = request.getParameter("practice_id");
		String month = request.getParameter("month");
		String thesis = request.getParameter("thesis");
		String evaluate = request.getParameter("evaluate");
		double dblMonth = Double.parseDouble(month);
		double dblThesis = Double.parseDouble(thesis);
		double dblEvaluate = Double.parseDouble(evaluate);
		PracticeTask p = DictionaryService.findPracticeTask(practice_id);
		p.setWeight_summary(dblMonth);
		p.setWeight_evaluate(dblEvaluate);
		p.setWeight_thesis(dblThesis);
		this.practiceTaskService.update(p);
		List<Score> stusScore = new ArrayList<Score>();
		List<String> stusId = this.practiceTaskService
				.selectStusId(practice_id);
		Score s = new Score();
		for (int i = 0; i < stusId.size(); i++) {
			s = this.ScoreService.getStuScore(practice_id, stusId.get(i));
			stusScore.add(s);
		}
		double mouthScore = 0;
		double theScore = 0;
		double evaScore = 0;
		double score = 0;
		for (Score sc : stusScore) {
			if (sc.getStu_id().equalsIgnoreCase(id1)) {
				mouthScore = sc.getMouthScore();
				theScore = sc.getTheScore();
				evaScore = sc.getEvaScore();
				score = sc.getScore();
			}
		}
		map.put("student", student);
		map.put("mouthScore", mouthScore);
		map.put("theScore", theScore);
		map.put("evaScore", evaScore);
		map.put("score", score);
		map.put("class_name", class_name);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/InternshipTaskViewList_details",
				map);
	}

	/**
	 * 功能：教师月工作量查看 by 苏衍静20160102
	 */
	@RequestMapping("MobileTeacher/WorkloadView.do")
	public ModelAndView WorkloadView(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 默认当月的即时性图
		// 默认年份
		String grade = Common.getDefaultYear();
		Map<String, String> month = Common.getTimeForTeacherAplic();
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		String tea_id = tea.getId();
		Calendar calendar = Calendar.getInstance();
		int datenum = calendar.get(Calendar.DATE);
		int month1; // 月份
		if (datenum > 5) {
			month1 = calendar.get(Calendar.MONTH) + 1;
		} else {
			month1 = calendar.get(Calendar.MONTH);
		}
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		List<Groups> groupList = new ArrayList();
		String lastPractice_id = new String();
		String defaultYear = Common.getDefaultYear();
		for (Groups g : groups) {
			String groupName = g.getGroup_name();
			String task = groupName.substring(5, 7);
			if (task.equals("实习")) {
				groupList.add(g);
				String year = groupName.substring(0, 4);
				if (year.equals(defaultYear)) {
					lastPractice_id = g.getPractice_id();
				}
			}
		}
		if (groupList.size() == 0) {
			lastPractice_id = "";
		}
		System.out.println("------------");
		System.out.println(groupList);
		System.out.println("------------");
		map.put("lastPractice_id", lastPractice_id);
		map.put("groupList", groupList);
		map.put("tea", tea);
		map.put("month1", month1);
		return new ModelAndView("/mobileViews/mobileTeacher/WorkloadView", map);
	}

	/**
	 * 
	 * @param modelMap
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 *             功能：个人信息维护!!!!
	 */
	@RequestMapping("MobileTeacher/PersonalInformationMaintenance.do")
	public ModelAndView toPersonalInformationMaintenance(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// 获取当前用户!!!!!
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.selectByID(Common
				.getOneTeaId(session));// 通过session获得当前老师的ID
		map.put("teacher", teacher);
		/* return "/mobileViews/mobileTeacher/PersonalInformationMaintenance"; */
		return new ModelAndView(
				"/mobileViews/mobileTeacher/PersonalInformationMaintenance",
				map);
	}

	/**
	 * 功能：保存个人信息 .!!!!!
	 * 
	 * @author
	 * @createDate 2015-1-12
	 * @since 3.0
	 * 
	 */
	@RequestMapping("MobileTeacher/doeditmyInfo.do")
	public String editTeacher(HttpSession session, HttpServletRequest request)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		tea.setTrue_name(request.getParameter("true_name"));
		tea.setPhone(request.getParameter("phone"));
		tea.setQqnum(request.getParameter("qqnum"));
		tea.setEmail(request.getParameter("email"));
		tea.setHomepage(request.getParameter("homepage"));
		tea.setExpertise(request.getParameter("expertise"));
		tea.setDuties(request.getParameter("duties"));
		teacherService.update(tea);

		return "redirect:index.do";
	}

	/**
	 * 功能：通过实习任务得到学生 针对实习任务 by周睿20160115
	 */
	@RequestMapping("MobileTeacher/studentListByPraCodeTeaCode.do")
	public String studentListByPraCodeTeaCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String practice_id = request.getParameter("practice_id");// 获取任务id
		List<GroupMembers> SuList = this.groupMembersService
				.studentsList(practice_id);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (GroupMembers gm : SuList) {
			if (DictionaryService.findStudent(gm.getUser_id()).getState()
					.equals("1")) {
				String name = DictionaryService.findStudent(gm.getUser_id())
						.getTrue_name();

				sb.append("<li class='nopadding'><a href='#' data-role='checkbox'><label class='black' id='stu_true_name' name='stu_true_name' value='"
						+ name
						+ "'>"
						+ name
						+ "</label><input type='checkbox' id='stu_true_id' name='stu_true_id' value='"
						+ gm.getUser_id() + "' /></a></li>");
				i++;
			}
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：通过实习任务得到学生 针对实习任务 by周睿20160115
	 */
	@RequestMapping("MobileTeacher/studentListByPractice_name.do")
	public String studentListByPractice_name(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String practice_name = request.getParameter("practice_name");// 获取任务name
		Student stu = new Student();
		stu.setGroup_id(practice_name);
		List<Student> stus = studentService.selectList(stu);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Student s : stus) {
			if (s.getState().equals("1")) {
				sb.append("<ul class='listitem'href='InternshipStudentList_details.do?code="
						+ s.getStu_code()
						+ "'data-toggle='html'><li>"
						+ s.getTrue_name()
						+ "<small>"
						+ s.getStu_code()
						+ "</small></li></ul>");
				i++;
			}
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：显示信息核对任务列表 ----简略 by 宋浩 2015-01-12
	 * */
	@RequestMapping("MobileTeacher/InformationCheckList.do")
	public ModelAndView checkInfoList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String checkinfo_Grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<PracticeTask> checkinfo_pulltaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(tea_id, checkinfo_Grade);

		String checkinfo_pullTaskId = null;
		String checkinfo_pullTaskName = null;
		if (checkinfo_pulltaskList.size() > 0) {
			checkinfo_pullTaskId = checkinfo_pulltaskList.get(0).getId();
			checkinfo_pullTaskName = checkinfo_pulltaskList.get(0)
					.getTask_name();
		} else {
			checkinfo_pullTaskId = "请选择实习任务";
			checkinfo_pullTaskName = "请选择任务";
			checkinfo_Grade = "入学年份";
		}
		List<PracticeTask> checkInfo_taskList = this.checkInfoService
				.selectCheckInfoListByTeaIdAndGrade(tea_id, checkinfo_Grade,
						checkinfo_pullTaskId);

		session.setAttribute("checkinfo_Grade", checkinfo_Grade);// 过滤条件的年级
		session.setAttribute("checkinfo_TaskId", checkinfo_pullTaskId);// 过滤条件选择的任务id
		session.setAttribute("checkInfo_taskList", checkinfo_pulltaskList);// 过滤条件任务下拉列表

		map.put("checkinfo_pulltaskList", checkinfo_pulltaskList);
		map.put("checkinfo_Grade", checkinfo_Grade);
		map.put("checkinfo_pullTaskName", checkinfo_pullTaskName);
		map.put("checkInfo_taskList", checkInfo_taskList);

		return new ModelAndView("/mobileViews/mobileTeacher/checkInfoList", map);
	}

	/**
	 * 功能：显示信息核对单个任务 ----详细 by 宋浩 2015-01-12
	 * */
	@RequestMapping("MobileTeacher/checkDetailInfoList.do")
	public ModelAndView checkDetailInfoList(ModelMap modelMap, String id) {
		PracticeTask ic = (PracticeTask) practiceTaskService.selectByID(id);
		List<InfoCheckRecord> result = checkInfoService
				.selectCheckInfoByChecktask_id(id);
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getCheck_result().equals("0")) {
				result.get(i).setCheck_result("还未核对");
			} else if (result.get(i).getCheck_result().equals("1")) {
				result.get(i).setCheck_result("信息正确");
			} else if (result.get(i).getCheck_result().equals("2")) {
				result.get(i).setCheck_result("信息已修改");
			}
		}
		for (int i = 0; i < result.size(); i++) {
			if (DictionaryService.findStudent(result.get(i).getStu_id()) == null) {
				result.remove(i);
			}
		}
		modelMap.put("result", result);
		modelMap.put("ic", ic);

		return new ModelAndView(
				"/mobileViews/mobileTeacher/checkDetailInfoList", modelMap);
	}

	/**
	 * 功能：添加 信息核对任务 by 张向杨 宋浩 2015-01-12
	 * */
	@RequestMapping("MobileTeacher/checkInfoAdd.do")
	public String addCheckInfo(ModelMap modelMap, HttpSession session) {
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获取到当前用户

		Boolean flag = true;
		// String checkinfo_Grade = Common.getDefaultYear();// 得到默认年份2013
		String tea_id = tea.getId();
		PracticeTask pt = new PracticeTask();
		pt.setTea_id(tea_id);
		pt.setState("1");
		pt.setTask_type("1");
		// 初始化登录老师的对应的实习任务
		List<PracticeTask> checkinfo_pulltaskList = null;
		checkinfo_pulltaskList = (List<PracticeTask>) practiceTaskService
				.selectList(pt);
		// 判断登录老师是否有对应的实习任务
		if (checkinfo_pulltaskList == null) {
			flag = false;
			modelMap.put("flag", flag);
			return "/mobileViews/mobileTeacher/checkInfoAdd";
		}

		modelMap.put("checkinfo_pulltaskList", checkinfo_pulltaskList);// 任务列表添加map
		modelMap.put("flag", flag);
		return "/mobileViews/mobileTeacher/checkInfoAdd";
	}

	/**
	 * 功能：保存 核对信息 将核对信息分别保存到 实践任务表 和 信息核对表 通知表 by 宋浩 2016-01-15 修改
	 * 
	 * 
	 * */
	@RequestMapping("MobileTeacher/doAddCheckInfo.do")
	public String saveCheckInfo(HttpServletRequest request, HttpSession session)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		PracticeTask pra = new PracticeTask();

		String tea_id = tea.getId();
		pra.setTea_id(tea_id);
		Notice notice = new Notice();

		String stu_range = request.getParameter("stu_range");
		String create_Time = request.getParameter("create_Time");//
		String begin_Time = request.getParameter("begin_Time");
		String end_Time = request.getParameter("end_Time");
		String task_desc = request.getParameter("task_desc");
		String practice_id = request.getParameter("praTask");
		String task_Name = request.getParameter("task_name");
		String grade = DictionaryService.findPracticeTask(practice_id)
				.getGrade();
		String practice_code = DictionaryService.findPracticeTask(practice_id)
				.getPractice_code();
		// String task_Name =
		// DictionaryService.findPracticeTask(practice_id).getTask_name();
		// String task_name ="";
		/* 动态输出服务器时间端当前时间 */
		/* Date nt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时 */
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTimes = format.format(new Date());
		Timestamp times = new Timestamp(System.currentTimeMillis());

		try {
			times = new Timestamp(format.parse

			(nowTimes).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(begin_Time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (begin_Time == null) {
			begin_Time = "2010-01-01";
		}

		if (end_Time == null) {
			end_Time = "2010-01-01";
		}

		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());

		try {
			ts1 = new Timestamp(format2.parse(end_Time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 生成通知编码 郑春光 2015年1月30日
		String checkInfo_cur_code = practice_code + "XXHD";
		String maxCheckInfo_cur_code = this.noticeService
				.getMaxNoticCode(checkInfo_cur_code);
		if (maxCheckInfo_cur_code == null) {
			checkInfo_cur_code = checkInfo_cur_code + "001";
			// task_name = task_Name + "信息核对";
		} else {
			int maxCode = Integer.parseInt(maxCheckInfo_cur_code) + 1;
			if (String.valueOf(maxCode).length() == 1) {
				checkInfo_cur_code = checkInfo_cur_code + "00" + maxCode;
				// task_name = task_Name + "信息核对";
			} else if (String.valueOf(maxCode).length() == 2) {
				checkInfo_cur_code = checkInfo_cur_code + "0" + maxCode;
				// task_name = task_Name + "信息核对";
			} else {
				checkInfo_cur_code = checkInfo_cur_code + maxCode;
				// task_name = task_Name + "信息核对";
			}
		}
		notice.setNotice_code(checkInfo_cur_code);
		notice.setContent(task_desc);
		notice.setNotice_type("3");
		notice.setStu_id(stu_range);

		notice.setCreate_time(times);// 通知中的创建时间

		notice.setTea_id(tea_id);
		notice.setTitle(task_Name);
		noticeService.insert(notice);// 在通知数据库添加发布信息

		String checkInfoId = Common.returnUUID();
		pra.setBegin_time(ts);
		pra.setEnd_time(ts1);
		pra.setCreate_time(times);// 任务的创建时间

		pra.setId(checkInfoId);

		pra.setPractice_code(checkInfo_cur_code);
		pra.setTask_name(task_Name);
		pra.setTask_place("");
		pra.setTask_desc(task_desc);
		pra.setParent_id(practice_id);
		pra.setGrade(grade);
		pra.setTask_type("3");// 在任务添加信息

		practiceTaskService.insert(pra);
		// 获取复选框数组
		String[] studentSelected = stu_range.split(",");
		InfoCheckRecord ic = new InfoCheckRecord();
		Timestamp d = new Timestamp(System.currentTimeMillis());
		// 循环插入到数据库中
		if (studentSelected != null) {
			for (int i = 0; i < studentSelected.length; i++) {
				ic.setCheck_result("0");
				ic.setStu_id(studentSelected[i]);
				ic.setId(Common.returnUUID());
				ic.setChecktask_id(pra.getId());
				ic.setNote("无");
				checkInfoService.insert(ic);
			}
		}

		return "redirect:InformationCheckList.do";
	}

	/**
	 * 功能：编辑已经发布的核对信息任务 张向阳 2015-01-13
	 * 
	 * */
	@RequestMapping("MobileTeacher/doEditCheckInfo.do")
	public String saveCheckInfo(String id, HttpServletRequest request)
			throws IllegalStateException, IOException {
		PracticeTask practiceTask = DictionaryService.findPracticeTask(id);

		String task_name = request.getParameter("task_name");
		String task_desc = request.getParameter("task_desc");
		String end_time = request.getParameter("end_time");
		String begin_time = request.getParameter("begin_time");

		if (begin_time == null) {
			begin_time = "2010-01-01";
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(begin_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		practiceTask.setBegin_time(ts);

		if (end_time == null) {
			end_time = "2010-01-01";
		}
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(end_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		practiceTask.setEnd_time(ts1);
		practiceTask.setId(id);
		practiceTask.setTask_desc(task_desc);
		practiceTask.setTask_name(task_name);
		practiceTaskService.update(practiceTask);

		return "redirect:InformationCheckList.do";
	}

	/**
	 * 功能：教师删除核对信息 by 张向杨 2016-01-13 操作实践任务表和核对信息表
	 * 
	 * */
	@RequestMapping("MobileTeacher/deleteCheckInfo.do")
	public String deleteCheckInfo(String id) {
		PracticeTask pt = (PracticeTask) practiceTaskService.selectByID(id);
		String task_name = pt.getTask_name();
		String newtask_name = task_name + "-无效";
		pt.setTask_name(newtask_name);
		pt.setState("2");
		practiceTaskService.update(pt);
		List<InfoCheckRecord> InfoCheckRecordList = checkInfoService
				.selectCheckInfoByChecktask_id(id);// 查询信息核对表的数据，是一个list
		for (InfoCheckRecord i : InfoCheckRecordList) {
			InfoCheckRecord checkinfo = i;
			checkinfo.setCheck_result("3");
			checkInfoService.update(checkinfo);
		}
		return "redirect:InformationCheckList.do";
	}

	// 转发到专家回复页面 苏衍静 2016年1月18日 09:26:54
	@RequestMapping("MobileTeacher/StudentQuestionsList.do")
	public String toStudentQuestionsList(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Knowledge> knowledges = this.knowledgeService
				.getUnAnswerQuestion(tea_id);
		int pageSize = 15;// 分页
		int Currentpage = 1;// 初始值1
		// 获取当前页集合
		List<Knowledge> knowledgesList = Common.getListCurrentPage(knowledges,
				pageSize, Currentpage);
		List<Knowledge> passKnowledgesList = new ArrayList<Knowledge>();// 得到总页数
		List<Knowledge> failtKnowledgesList = new ArrayList<Knowledge>();// 通过的
		if (knowledgesList != null) {
			for (Knowledge k : knowledgesList) {
				if (k.getAnswer_time() != null) {
					passKnowledgesList.add(k);
				} else {
					failtKnowledgesList.add(k);
				}
			}
		}
		int pageCount = Common.getPageCount(knowledges, pageSize, Currentpage);
		session.setAttribute("TEACHER_CONTROLLER_KNOWLEDGES", knowledges);
		modelMap.put("count", pageCount);
		modelMap.put("result", knowledgesList);
		modelMap.put("passKnowledgesList", passKnowledgesList);
		modelMap.put("failtKnowledgesList", failtKnowledgesList);
		modelMap.put("nowPage", Currentpage);
		return "/mobileViews/mobileTeacher/StudentQuestionsList";
	}

	// 转发修改问题页面，专家指导，苏衍静 2016年1月18日
	@RequestMapping("MobileTeacher/taskDetailsEdit.do")
	public String totaskDetailsEdit(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String question_id = request.getParameter("id");
		Knowledge knowledge = (Knowledge) this.knowledgeService
				.selectById(question_id);
		String stuName = DictionaryService.findStudent(
				knowledge.getMessenger_id()).getTrue_name();

		String Class_id = DictionaryService.findStudent(
				knowledge.getMessenger_id()).getClass_id();
		String stuClassName = DictionaryService.findOrg(Class_id).getOrg_name();

		modelMap.put("knowledge", knowledge);
		modelMap.put("stuClassName", stuClassName);
		modelMap.put("stuName", stuName);
		return "/mobileViews/mobileTeacher/taskDetailsEdit";
	}

	// 转发回答问题页面，专家指导，岳贤昌 2016年1月18日
	@RequestMapping("MobileTeacher/TaskDetailsAnswer.do")
	public String toTaskDetailsAnswer(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String question_id = request.getParameter("id");
		Knowledge knowledge = (Knowledge) this.knowledgeService
				.selectById(question_id);
		String stuName = DictionaryService.findStudent(
				knowledge.getMessenger_id()).getTrue_name();// 利用数据字典将学生ID转换成姓名

		String Class_id = DictionaryService.findStudent(
				knowledge.getMessenger_id()).getClass_id();
		String stuClassName = DictionaryService.findOrg(Class_id).getOrg_name();

		modelMap.put("knowledge", knowledge);
		modelMap.put("stuClassName", stuClassName);
		modelMap.put("stuName", stuName);
		return "/mobileViews/mobileTeacher/TaskDetailsAnswer";

	}

	// 专家指导，问题回答完后处理 岳贤昌 2016年1月18日
	@RequestMapping("MobileTeacher/updateAnswer.do")
	public String updateAnswer(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Teacher tea = (Teacher) session.getAttribute("current_user");

		String id = request.getParameter("id");
		String answerer = request.getParameter("answerer");
		String teaId = tea.getId();// 老师ID

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String answerTime = format.format(date);// 获取当前时间

		Knowledge k = new Knowledge();
		k.setId(id);
		k.setAnswerer(teaId);
		k.setAnswer(answerer);
		k.setAnswer_time(Timestamp.valueOf(answerTime));

		knowledgeService.update(k);

		return "redirect: StudentQuestionsList.do";
	}

	/**
	 * 功能：ajax传递教师月工作量的相关信息 by syj 2016-01-20
	 */
	@RequestMapping("MobileTeacher/ajaxSerchTea.do")
	public String ajaxSerchTea(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String practice_id = request.getParameter("practice_id");// 获取select选项的值（value值对应groups组中的practice_id
																	// ）
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		Groups group = (Groups) groupsService.selectGroupsByPraid(practice_id);// 通过practice_id查询在groups中对应的一条记录
		String grade = group.getGroup_name().substring(0, 4); // 通过group_name截取相应的年份

		Map<String, Double> teacherMouthApicData = practiceTaskService
				.getTeacherMouthApicData(practice_id, tea.getId(), grade);

		// 教师实际工作量
		double score = teacherMouthApicData.get("score");
		// 合格实习人数（合格实习人数以学生上传实习月总结并且老师已批阅的数量为准）
		double qualifiedCount = teacherMouthApicData.get("qualifiedCount");
		// 教师所带的实习人数
		double studentSize = teacherMouthApicData.get("studentSize");
		// 教师完成度（实际工作量/理想工作量）
		double theoryApicScore = teacherMouthApicData.get("theoryApicScore") * 100;
		// 教师是否上传月总结
		double guideCoefficient = teacherMouthApicData.get("guideCoefficient");

		// 小数点处理
		String guideCoefficientString = Common
				.getDoubetoString(guideCoefficient);
		String scoreString = Common.getDoubetoString(score);
		String qualifiedCountString = Common.getDoubetoString(qualifiedCount);
		String studentSizeString = Common.getDoubetoString(studentSize);
		String theoryApicScoreString = Common.getDoubetoString(theoryApicScore)
				+ "%";

		StringBuffer sb = new StringBuffer();

		sb.append("<label class='label-left'>合格人数</label><div style='height:auto;' class='label-right' id = 'qualifiedCount' name = 'qualifiedCount'><output>"
				+ qualifiedCountString
				+ "</output></div><hr><label class='label-left'>实习人数</label><div style='height:auto;' class='label-right' id = 'studentSize' name = 'studentSize'><output>"
				+ studentSizeString
				+ "</output></div><hr><label class='label-left'>月总结系数</label><div style='height:auto;' class='label-right' id = 'yesOrNo' name = 'yesOrNo'><output> "
				+ guideCoefficientString
				+ " </output></div><hr><label class='label-left'>月完成工作量</label><div style='height:auto;' class='label-right' id = 'score' name = 'score'><output>"
				+ scoreString
				+ "</output></div><hr><label class='label-left'>月完成度</label><div style='height:auto;' class='label-right' id = 'theoryApicScore' name = 'theoryApicScore'><output>"
				+ theoryApicScoreString + "</output></div>");

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**查看学生签到情况 start by 李达 20160119 
	 * StudentAttendanc 查看学生签到情况 使用@RequestMapping by 李达 20160119
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	@RequestMapping("MobileTeacher/StudentAttendance.do")
	public ModelAndView StudentAttendanc(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		request.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String moth = String.valueOf(month);
		int year = cal.get(Calendar.YEAR);
		String year1 = String.valueOf(year);
		String trueMouth = ""; // 获取当前年月
		if (month < 10) {
			trueMouth = year1 + "-0" + moth;
		} else {
			trueMouth = year1 + "-" + moth;
		}
		// 设置默认的查询年级
		String grade = "";
		List<PracticeTask> pks = this.practiceTaskService
				.selectOutSchoolPracticeTasks(tea_id);
		if (pks.size() > 0) {
			String pk_id = pks.get(0).getId(); // 获得实践任务的id
			grade = DictionaryService.findPracticeTask(pk_id).getGrade(); // 获取时间任务的年级

			List<BMapEntity> allSignStuMes = new ArrayList<BMapEntity>(); // 获取该老师管理的学生中签到学生的所有信息
			List<List<BMapEntity>> all = new ArrayList<List<BMapEntity>>(); // 声明一个存放该老师所有学生信息的list

			List<String> allSignStuId = this.practiceTaskService
					.getGroupMembersBypId(pk_id, grade); // 获取该老师管理的学生中签到的学生的stu_id
			for (int i = 0; i < allSignStuId.size(); i++) {
				String stu_id = allSignStuId.get(i);
				allSignStuMes = this.signService
						.selectSignAllStuStateByStuIdAndMoth(stu_id, trueMouth); // 根据stu_code查询所需要的学生信息
				all.add(allSignStuMes);
			}
			map.put("all", all);
		}
		map.put("pks", pks);
		map.put("trueMouth", trueMouth);

		return new ModelAndView("/mobileViews/mobileTeacher/StudentAttendance",
				map);

	}

	/**
	 * ajaxStusSinState ajax获取某年月该实习任务下学生签到情况 使用@RequestMapping by 李达 20160119
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	@RequestMapping("MobileTeacher/ajaxStusSinState.do")
	public String ajaxStusSinState(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String ym = request.getParameter("ym");
		String grade = ym.substring(0, 4);
		String moth = ym.substring(7, 9);
		
		int mouth = Integer.parseInt(moth);
		
		String trueMouth = grade + "-" + moth;
		String pk_id = request.getParameter("pks_id");
		List<BMapEntity> StuSinMes = new ArrayList<BMapEntity>();
		List<List<BMapEntity>> StuSin = new ArrayList<List<BMapEntity>>();
		List<String> allStuID = this.practiceTaskService.selectStusId(pk_id);

		for (int i = 0; i < allStuID.size(); i++) {
			String stu_id = allStuID.get(i);
			StuSinMes = this.signService.selectSignAllStuStateByStuIdAndMoth(
					stu_id, trueMouth);
			StuSin.add(StuSinMes);
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<ul class='listitem'>");
		for (int i = 0; i < StuSin.size(); i++) {
			for (BMapEntity b : StuSin.get(i)) {
				sb.append("<li>");
				sb.append("<label href='StudentAttendance_details.do?stu_code="
						+ b.getStu_code() + "&true_name=" + b.getTrue_name()
						+ "&signCount=" + b.getSignCount() + "&phone="
						+ b.getPhone() + "' data-toggle='html'>");
				sb.append("<div class='text'>");
				sb.append(b.getTrue_name());
				sb.append("<small>本月签到次数" + " " + b.getSignCount() + "</small>");
				sb.append("</div></label></li>");
			}
		}
		sb.append("</ul>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * StudentAttendance_details 查看签到学生详细信息 使用@RequestMapping by 李达 20160120
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	@RequestMapping("MobileTeacher/StudentAttendance_details.do")
	public ModelAndView StudentAttendance_details(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		request.setCharacterEncoding("UTF-8");
		String stu_code = request.getParameter("stu_code");
		String true_name = new String(request.getParameter("true_name")
				.getBytes("iso8859-1"), "UTF-8");
		String signCount = request.getParameter("signCount");
		String phone = request.getParameter("phone");

		Student stu = DictionaryService.findStudentByCode(stu_code);
		String class_name = DictionaryService.findOrg(stu.getClass_id())
				.getOrg_name();

		map.put("class_name", class_name);
		map.put("true_name", true_name);
		map.put("signCount", signCount);
		map.put("phone", phone);
		map.put("stu_code", stu_code);
		return new ModelAndView(
				"/mobileViews/mobileTeacher/StudentAttendance_details", map);
	}

	@RequestMapping("MobileTeacher/help.do")
	public String help(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		return "/mobileViews/mobileTeacher/Help";
	}

	@RequestMapping("MobileTeacher/erroy.do")
	public String erroy(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		return "/mobileViews/mobileTeacher/bdErrroy";
	}

	/**
	 * 功能：获得可快速导航菜单 吴宝 20150314
	 * */
	@RequestMapping("MobileTeacher/getRoleIdMenu.do")
	public void getRoleIdMenu(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		String srm_role_id = (String) session
				.getAttribute("current_role_selected");// 获得角色id
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		List<SysMenu> listall = sysMenuService
				.selectListByRoleIdMP(srm_role_id);
		UserRole userRole = new UserRole();
		userRole.setUser_id(tea.getId());
		userRole.setRole_id(srm_role_id);

		userRole = (UserRole) userRoleService.selectList(userRole).get(0);

		String custom_menu_ids = userRole.getCustom_menu_ids();// 获得用户自定义菜单

		StringBuffer sb = CustomMenuForm.menuResourceFormMP(custom_menu_ids,
				listall);
		sb.append(CustomMenuForm.hideMenuResourceFormPC(custom_menu_ids));
		response.getWriter().println(sb.toString());
		response.getWriter().close();
	}

	/**
	 * 功能：保存用户自定义菜单 吴宝 20150319
	 * */

	@RequestMapping("MobileTeacher/updateCustomMenuAjax.do")
	public void updateCustomMenuAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("UTF-8");
		String[] csysMenu = request.getParameterValues("roleIdMenu");
		String csysMenus = "";
		if (csysMenu != null) {
			for (int i = 0; i < csysMenu.length; i++) {
				if (csysMenus.contains(csysMenu[i])) {
					continue;
				} else {
					if (i + 1 == csysMenu.length) {
						csysMenus += csysMenu[i];
					} else {
						csysMenus += csysMenu[i] + ",";
					}
				}
			}
		}
		UserRole userRole = new UserRole();
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		String srm_role_id = (String) session
				.getAttribute("current_role_selected");// 获得角色id
		userRole.setCustom_menu_ids(csysMenus);
		userRole.setUser_id(tea.getId());
		userRole.setRole_id(srm_role_id);
		userRoleService.updatecustomMenuIds(userRole);

	}

	/**
	 * 功能：显示用户自定义菜单 吴宝 20150319
	 * */

	@RequestMapping("MobileTeacher/updateCustomMenuShow.do")
	public void updateCustomMenuShow(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		UserRole userRole = new UserRole();
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		String srm_role_id = (String) session
				.getAttribute("current_role_selected");// 获得角色id
		userRole.setUser_id(tea.getId());
		userRole.setRole_id(srm_role_id);

		userRole = (UserRole) userRoleService.selectList(userRole).get(0);

		String custom_menu_ids = userRole.getCustom_menu_ids();// 获得用户自定义菜单

		StringBuffer sb = CustomMenuForm.saveMenuFormMP(custom_menu_ids);
		if (sb == null) {
			return;
		}
		response.getWriter().println(sb.toString());
		response.getWriter().close();
	}
}
