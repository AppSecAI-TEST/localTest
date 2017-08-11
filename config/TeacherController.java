package com.sict.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.entity.Association;
import com.sict.entity.BMapEntity;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.Company;
import com.sict.entity.DirectRecord;
import com.sict.entity.EvalsIndex;
import com.sict.entity.Evaluate;
import com.sict.entity.EvaluateStandard;
import com.sict.entity.Files;
import com.sict.entity.GraduationMaterials;
import com.sict.entity.GraduationThesis;
import com.sict.entity.GroupMembers;
import com.sict.entity.Groups;
import com.sict.entity.InfoCheckRecord;
import com.sict.entity.Knowledge;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.Param;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.RecruitInfo;
import com.sict.entity.ReportTrainDetail;
import com.sict.entity.RightRegion;
import com.sict.entity.Role;
import com.sict.entity.Score;
import com.sict.entity.Sign;
import com.sict.entity.Student;
import com.sict.entity.SysMenu;
import com.sict.entity.Teacher;
import com.sict.entity.TrainDetail;
import com.sict.entity.UserRole;
import com.sict.service.AssociationService;
import com.sict.service.CompanyService;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateService;
import com.sict.service.EvaluateStandardService;
import com.sict.service.ExcelService;
import com.sict.service.FilesService;
import com.sict.service.GraduationMaterialService;
import com.sict.service.GraduationThesisService;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.InfoCheckRecordService;
import com.sict.service.KnowledgeService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.ParamService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RecruitInfoService;
import com.sict.service.RightRegionService;
import com.sict.service.RoleService;
import com.sict.service.ScoreService;
import com.sict.service.SignService;
import com.sict.service.StudentService;
import com.sict.service.SysMenuService;
import com.sict.service.TeaStuService;
import com.sict.service.TeacherService;
import com.sict.service.TrainDetailService;
import com.sict.service.UserRoleService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.CustomMenuForm;
import com.sict.util.DateService;
import com.sict.util.Ichartjs_Color;
import com.sict.util.jsonUtil;

/**
 * 功能：教师控制器 使用 @Controller by吴敬国20140917 *
 * */
@Controller
public class TeacherController {
	/**
	 * 注入teacherService wtt20140910
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;

	/**
	 * 注入studentService by吴敬国20140917 *
	 */
	@Resource(name = "studentService")
	private StudentService studentService;

	/**
	 * 注入orgService by吴敬国20140917 *
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * 注入practiceRecordService by王磊20140925 *
	 * 
	 * */
	@Resource(name = "practiceRecordService")
	private PracticeRecordService practiceRecordService;
	/**
	 * 注入groupMembersService by吴敬国20140925 *
	 * 
	 * */
	@Resource(name = "groupMembersService")
	private GroupMembersService groupMembersService;
	/**
	 * 注入filesService byccc20141008 *
	 */
	@Resource(name = "filesService")
	private FilesService filesService;
	/**
	 * 注入userRoleService by吴敬国20141105 *
	 * 
	 * */
	@Resource(name = "userRoleService")
	private UserRoleService userRoleService;
	/**
	 * 注入graduationThesisService byccc20141008 *
	 */
	@Resource(name = "graduationThesisService")
	private GraduationThesisService graduationThesisService;

	/**
	 * 注入PracticeTaskService by吴敬国20140927 *
	 */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	/**
	 * 注入GraduationMaterialService byccc20141101 *
	 */
	@Resource(name = "graduationMaterialService")
	private GraduationMaterialService graduationMaterialService;

	/**
	 * 注入evaluateService by王磊20141101 *
	 */
	@Resource(name = "evaluateService")
	private EvaluateService evaluateService;
	/**
	 * 注入evaluateStandardService by王磊20141101 *
	 */
	@Resource(name = "evaluateStandardService")
	private EvaluateStandardService evaluatestandardService;
	/**
	 * 注入evalsIndexService by王磊20141101 *
	 */
	@Resource(name = "evalsIndexService")
	private EvalsIndexService evalsIndexService;

	/**
	 * 注入teaStuService by王磊20141101 *
	 */
	@Resource(name = "teaStuService")
	private TeaStuService teaStuService;

	/**
	 * 注入evalsIndexService by王磊20141101 *
	 */
	@Resource(name = "evalsIndexService")
	private EvalsIndexService evalsIndexservice;
	/**
	 * 注入noticeService by王腾腾20141101 *
	 */
	@Resource(name = "noticeService")
	private NoticeService noticeService;
	/**
	 * 注入CheckInfoService by吴敬国20141122 *
	 */
	@Resource(name = "checkInfoService")
	private InfoCheckRecordService checkInfoService;

	/**
	 * 注入groupsService by吴敬国20141101 *
	 */
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	/**
	 * 注入SignService by邢志武20141101 *
	 */
	@Resource(name = "signService")
	private SignService signService;

	/**
	 * 注入companyService byxzw20140917 *
	 */
	@Resource(name = "companyService")
	private CompanyService companyService;

	/**
	 * 注入knowledgeService by孙家胜20141212 *
	 */
	@Resource(name = "knowledgeService")
	private KnowledgeService knowledgeService;
	/**
	 * 注入directRecordService bY wtt 20141217 *
	 */
	@Resource(name = "directRecordService")
	private DirectRecordService directRecordService;

	/**
	 * 注入ScoreService by吕付豹20141216 *
	 */
	@Resource(name = "ScoreService")
	private ScoreService ScoreService;

	/**
	 * 注入rightRegionService by孙磊 2015-03-21 *
	 */
	@Resource(name = "rightRegionService")
	private RightRegionService rightRegionService;
	/**
	 * 注入trainDetailService by张超 2015年1月29日*
	 * 
	 * */
	@Resource(name = "trainDetailService")
	private TrainDetailService trainDetailService;
	/**
	 * 注入recruitInfoService by王磊2015-3-20 *
	 */
	@Resource(name = "recruitInfoService")
	private RecruitInfoService recruitInfoService;

	/**
	 * 注入sysMenuService by师杰 2016-03-21
	 * 
	 * */
	@Resource(name = "associationService")
	private AssociationService associationService;
	/**
	 * 注入roleService by吴敬国20140917 *
	 */
	@Resource(name = "roleService")
	private RoleService roleService;
	/**
	 * 注入sysMenuService by吴宝20160122 *
	 */
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	/**
	 * 注入ParamService by周睿20160419 *
	 * 
	 * */
	@Resource(name = "paramService")
	private ParamService paramService;
	// 定义全局返回值变量
	String ret = "";
	private int pageSizeConstant = Constants.pageSize; // 获取常量分页页大小

	/**
	 * 维护学生信息第一个方法 进行学生信息的过滤 by吴敬国20140917 修改：周睿20160406
	 * 
	 */
	@RequestMapping("teacher/studentList.do")
	public ModelAndView studentList(HttpSession session, String type) {
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
		return new ModelAndView("teacher/studentList", map);
	}

	/**
	 * 根据id取出当前学生信息转到编辑页面 by吴敬国20140917 *
	 * 
	 * @param id
	 * 
	 * */
	@RequestMapping("teacher/editStudent.do")
	public String toEditStudent(ModelMap modelMap, String id) {
		Student student = (Student) studentService.selectByID(id);
		String homeAddr = student.getHome_addr().trim();
		student.setHome_addr(homeAddr);
		modelMap.put("student", student);
		return "teacher/studentEdit";
	}

	/**
	 * 功能：编辑保存学生 by吴敬国20140917
	 * 
	 * @ModelAttribute
	 * @param student
	 * 
	 * */
	@RequestMapping("teacher/doEditStudent.do")
	public String editStudent(HttpServletRequest request) throws IOException {
		String studentId = request.getParameter("id");
		String qq = request.getParameter("qqnum");
		String phone = request.getParameter("phone");
		String home_phone = request.getParameter("home_phone");
		String email = request.getParameter("email");
		String login_pass = request.getParameter("login_pass");
		String home_addr = request.getParameter("home_addr");
		String stu_code = request.getParameter("stu_code").trim();
		String id_card = request.getParameter("id_card").trim();
		Student student = DictionaryService.findStudent(studentId);
		student.setQqnum(qq);
		student.setId_card(id_card);
		student.setStu_code(stu_code);
		student.setPhone(phone);
		student.setHome_phone(home_phone);
		student.setEmail(email);
		student.setLogin_pass(login_pass);
		student.setHome_addr(home_addr);
		studentService.update(student);
		return "redirect:studentList.do";
	}

	/**
	 * 审批实习申请类.
	 * 
	 * @author 王磊 、邢志武
	 * @author 吴敬国 李泽
	 * @createDate 2015-7-20 2015-11-9
	 * @since 6.0
	 * 
	 */
	@RequestMapping("teacher/checkPracticeList.do")
	public ModelAndView practiceRecordList(HttpSession session,
			HttpServletRequest request) {
		// 定义一个map和一个List,用于接收数据
		Map<String, Object> map = new HashMap<String, Object>();
		List<PracticeRecord> modelList = new ArrayList<PracticeRecord>();
		// 给审核通过个数，不通过个数，未审核个数、未提交的设定一个初始值
		int nonChecked = 0, Haspassed = 0, Nopassed = 0, unCommitNum = 0;
		// 得到默认显示的年级
		String checkPraTask_grade = Common.getDefaultYear();
		// 根据教师id和入学年份获得实习任务
		List<PracticeTask> checkPraTaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(Common.getOneTeaId(session),
						checkPraTask_grade);
		String checkPraticeTask_taskId = null;
		String default_TaskName = null;
		// 判断任务list为空的情况
		if (checkPraTaskList == null || checkPraTaskList.size() == 0) {
			checkPraticeTask_taskId = null;
			default_TaskName = "请选择实习任务";
		} else {
			// 取得第一个任务的id
			checkPraticeTask_taskId = checkPraTaskList.get(0).getId();
			// 取得第一个的任务名
			default_TaskName = checkPraTaskList.get(0).getTask_name();
			/*
			 * List<GroupMembers>
			 * studentList=groupMembersService.selectStuIdListByPracticeId
			 * (checkPraticeTask_taskId);
			 */
			unCommitNum = practiceRecordService
					.getUnCommitStuCount(checkPraticeTask_taskId);
			// 根据任务id得到实习申请记录
			List<PracticeRecord> prList = this.practiceRecordService
					.selectPracticeRecordByPracticeTaskId(checkPraticeTask_taskId);
			String check_state = "0";
			Map<String, Object> getPracticeRecordListAndCount = practiceRecordService
					.getPracticeRecordListAndCount(prList, check_state);
			Nopassed = (Integer) getPracticeRecordListAndCount.get("Nopassed");
			Haspassed = (Integer) getPracticeRecordListAndCount
					.get("Haspassed");
			nonChecked = (Integer) getPracticeRecordListAndCount
					.get("nonChecked");
			modelList = (List<PracticeRecord>) getPracticeRecordListAndCount
					.get("modelList");
		}
		// 将一些常用值存到session
		session.setAttribute("checkPraTask_grade", checkPraTask_grade);// 审批实习默认的年级
		session.setAttribute("checkPraTaskList", checkPraTaskList);// 审批实习页面中下拉任务列表
		session.setAttribute("default_TaskName", default_TaskName);// 审批实习默认的任务名
		session.setAttribute("checkPraticeTask_taskId", checkPraticeTask_taskId);
		// 获得教师指导的学生记录
		map.put("default_TaskName", default_TaskName);
		map.put("checkPraTaskList", checkPraTaskList);
		map.put("checkPraTask_grade", checkPraTask_grade);
		map.put("modelList", modelList);
		map.put("check_count_0", nonChecked);
		map.put("check_count_1", Haspassed);
		map.put("check_count_2", Nopassed);
		map.put("unCommitNum", unCommitNum);
		return new ModelAndView("teacher/checkPracticeList", map);
	}

	/**
	 * 实习学生信息管理 根据年级得到实习任务ajax.
	 * 
	 * @author 王磊 、邢志武
	 * @author 吴敬国 李泽、贾建昶
	 * @author 周睿20160328 简化代码
	 * @createDate 2015-7-20
	 * @since 6.0 "teacher/showComTeacher"复用
	 */
	@RequestMapping("teacher/ajaxchangeGrade.do")
	public String ajaxchangeGrade(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String grade = request.getParameter("grade");
		String tea_id = Common.getOneTeaId(session);// 获得老师的id号
		List<PracticeTask> checkPraTaskList = this.practiceTaskService
				.getOutPracticeTasksBytea_id(tea_id, grade);
		StringBuffer sb = new StringBuffer();
		sb.append("<option value='请选择实习任务'>请选择实习任务</option>");
		if (checkPraTaskList.size() != 0) {
			for (PracticeTask c : checkPraTaskList) {
				sb.append("<option " + "value='" + c.getTask_name() + "'>"
						+ c.getTask_name() + "</option>");
			}
		} else {
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到各种情况下的实习申请记录.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-20
	 * @since 6.0
	 * 
	 */
	@RequestMapping("teacher/getPracticeRecode.do")
	public ModelAndView getPracticeRecode(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PracticeRecord> modelList = new ArrayList<PracticeRecord>();
		String praticeTaskId;
		String default_TaskName;
		// 得到实习任务的id
		praticeTaskId = request.getParameter("praticeTaskId");
		// 可能的情况有null 0,1,2
		String check_state = request.getParameter("check_state");
		// 将审核状态存到seseion
		session.setAttribute("check_state", check_state);
		int Nopassed = 0, Haspassed = 0, nonChecked = 0;
		// 从session中获取当前显示的年级
		String checkPraTask_grade = (String) session
				.getAttribute("checkPraTask_grade");
		// 从session中获取任务列表
		List<PracticeTask> checkPraTaskList = (List<PracticeTask>) session
				.getAttribute("checkPraTaskList");
		default_TaskName = (String) session.getAttribute("default_TaskName");
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
			// 判断审核状态为null的情况
			if (check_state == null) {
				check_state = "0";
			}
			session.setAttribute("checkPraticeTask_taskId", praticeTaskId);
			default_TaskName = DictionaryService
					.findPracticeTask(praticeTaskId).getTask_name();
			session.setAttribute("default_TaskName", default_TaskName);
		}
		// 根据任务id得到实习申请记录
		int unCommitNum = practiceRecordService
				.getUnCommitStuCount(praticeTaskId);
		List<PracticeRecord> prList = this.practiceRecordService
				.selectPracticeRecordByPracticeTaskId(praticeTaskId);
		Map<String, Object> getPracticeRecordListAndCount = practiceRecordService
				.getPracticeRecordListAndCount(prList, check_state);
		Nopassed = (Integer) getPracticeRecordListAndCount.get("Nopassed");
		Haspassed = (Integer) getPracticeRecordListAndCount.get("Haspassed");
		nonChecked = (Integer) getPracticeRecordListAndCount.get("nonChecked");
		modelList = (List<PracticeRecord>) getPracticeRecordListAndCount
				.get("modelList");

		map.put("default_TaskName", default_TaskName);
		map.put("checkPraTaskList", checkPraTaskList);
		map.put("checkPraTask_grade", checkPraTask_grade);
		map.put("modelList", modelList);
		map.put("check_count_0", nonChecked);
		map.put("check_count_1", Haspassed);
		map.put("check_count_2", Nopassed);
		map.put("unCommitNum", unCommitNum);
		return new ModelAndView("teacher/checkPracticeList", map);
	}

	/**
	 * 查看实习申请详情
	 * 
	 * @author 邢志武 审核申请具体内容和审核公司
	 * @author 吴敬国
	 * @createDate 2015-7-20
	 * @since 6.0
	 * 
	 */
	@RequestMapping("teacher/checkPracticeRecord.do")
	public String checkPracticeRecord(HttpSession session, ModelMap modelMap,
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
		return "teacher/checkPracticeRecord";
	}

	/**
	 * 审核通过或不通过的响应
	 * 
	 * @author 王磊、邢志武 审核申请具体内容和审核公司
	 * @author 吴敬国
	 * @createDate 2015-7-20
	 * @since 6.0
	 * 
	 */
	@RequestMapping("teacher/check.do")
	public String check(HttpServletRequest request, String note,
			HttpSession session) throws IOException {
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
		pr.setNote(note);
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
		String note2 = request.getParameter("CheckNote");
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

		return "redirect:getPracticeRecode.do?check_state=" + check_state1;
	}

	/**
	 * 功能：ajax传递 根据年级得到实习任务 共用方法： 审批实习申请 ，信息核对 by 吴敬国2015-6-11
	 * */
	@RequestMapping("teacher/ajaxGetPraByChangeGrade.do")
	public String ajaxGetPraByChangeGrade(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String checkPraTask_grade = request.getParameter("grade");
		List<PracticeTask> checkPraTaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(Common.getOneTeaId(session),
						checkPraTask_grade);
		session.setAttribute("checkPraTaskList", checkPraTaskList);// 实习申请
		session.setAttribute("checkPraTask_grade", checkPraTask_grade);// 实习申请
		session.setAttribute("checkInfo_taskList", checkPraTaskList);// 过滤条件任务下拉列表
		session.setAttribute("checkinfo_Grade", checkPraTask_grade);// 信息核对 年级
		session.setAttribute("stuInfo_grade", checkPraTask_grade);
		session.setAttribute("stuInfo_taskList", checkPraTaskList);
		StringBuffer sb = new StringBuffer();
		sb.append("<option " + "value='" + "请选择实习任务" + "'>" + "请选择实习任务"
				+ "</option>");
		for (PracticeTask c : checkPraTaskList) {
			sb.append("<option " + "value='" + c.getId() + "'>"
					+ c.getTask_name() + "</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：审阅论文功能，初始化数据 论文管理 by ccc 20141009 吴敬国 2015-5-5
	 */
	@RequestMapping("teacher/checkThesisList.do")
	public ModelAndView graduationThesisList(HttpServletRequest request,
			HttpSession session) {
		session.setAttribute("Task_type", "1");
		String Task_type = "1";
		Map<String, Object> map = new HashMap<String, Object>();
		String thesis_grade = Common.getDefaultYear();
		List<PracticeTask> thesis_practicetaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_idAndTasktype(
						Common.getOneTeaId(session), thesis_grade, Task_type);
		String thesis_praTaskId = null;
		String thesis_taskname = null;
		if (thesis_practicetaskList.size() > 0) {
			thesis_praTaskId = thesis_practicetaskList.get(0).getId();
			thesis_taskname = thesis_practicetaskList.get(0).getTask_name();
		} else {
			thesis_praTaskId = null;
			thesis_taskname = "请选择任务";
			thesis_grade = "入学年份";
		}
		List<Student> student = studentService
				.getStuByPracticeTaskId(thesis_praTaskId);
		session.setAttribute("thesis_grade", thesis_grade);
		session.setAttribute("thesis_praTaskId", thesis_praTaskId);
		session.setAttribute("thesis_tasklist", thesis_practicetaskList);
		map.put("Task_type", Task_type);
		map.put("thesis_grade", thesis_grade);
		map.put("thesis_praTaskId", thesis_praTaskId);
		map.put("thesis_taskname", thesis_taskname);
		map.put("student", student);
		map.put("thesis_practicetaskList", thesis_practicetaskList);
		return new ModelAndView("teacher/ThesisStuList", map);
	}

	/**
	 * 功能：返回上一个论文，实训作品页面 by ccc 20141009 吴敬国 2015-4-17
	 */
	@RequestMapping("teacher/backThesisList.do")
	public ModelAndView backThesisList(HttpServletRequest request,
			HttpSession session) {
		String Task_type = (String) session.getAttribute("Task_type");
		String thesis_grade = (String) session.getAttribute("thesis_grade");
		String thesis_praTaskId = (String) session
				.getAttribute("thesis_praTaskId");
		String thesis_taskname = null;
		if (thesis_praTaskId != null) {
			thesis_taskname = DictionaryService.findPracticeTask(
					thesis_praTaskId).getTask_name();
		} else {
			thesis_taskname = "请选择任务";
		}
		List<PracticeTask> thesis_practicetaskList = (List<PracticeTask>) session
				.getAttribute("thesis_tasklist");
		List<Student> student = studentService
				.getStuByPracticeTaskId(thesis_praTaskId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Task_type", Task_type);
		map.put("thesis_praTaskId", thesis_praTaskId);
		map.put("thesis_grade", thesis_grade);
		map.put("thesis_taskname", thesis_taskname);
		map.put("thesis_practicetaskList", thesis_practicetaskList);
		map.put("student", student);
		return new ModelAndView("teacher/ThesisStuList", map);
	}

	/**
	 * 功能：ajax传递得到实习学生根据实习任务的id 实习论文 by吴敬国20150319 *
	 * */
	@RequestMapping("teacher/ajaxThesisStuListByPraId.do")
	public String ajaxThesisStuListByPraId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String Task_type = (String) session.getAttribute("Task_type");
		String cont = request.getParameter("praticeTaskId");
		int a = cont.indexOf("-");
		String thesis_praTaskId = cont.substring(0, a);
		String grade = cont.substring(a + 1, cont.length());
		session.setAttribute("thesis_grade", grade);
		session.setAttribute("thesis_praTaskId", thesis_praTaskId);
		if (thesis_praTaskId != null) {
			List<Student> student = studentService
					.getStuByPracticeTaskId(thesis_praTaskId);
			// List<GroupMembers>
			// members=groupMembersService.selectStuIdListByPracticeId(thesis_praTaskId);
			StringBuffer sb = new StringBuffer();
			for (Student t : student) {
				// 查询学生是否提交，在这写语句，在用if判断
				String stu_id = t.getId();
				GraduationThesis max_version = graduationThesisService
						.getMaxVersionByStuAndPraId(stu_id, thesis_praTaskId);
				String prog = "";
				String score = "";
				if (max_version != null) {
					prog = max_version.getProgress();
					score = max_version.getScore();
				}
				sb.append("<tr>");
				sb.append("<td>"
						+ DictionaryService.findOrg(
								DictionaryService.findStudent(t.getId())
										.getClass_id()).getOrg_name() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getId())
								.getStu_code() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getId())
								.getTrue_name() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getId()).getPhone()
						+ "</td>");
				if (Task_type.equalsIgnoreCase("1")) {
					if (prog.equalsIgnoreCase("初次提交")) {
						sb.append("<td><font color='red'>" + "新提交"
								+ "</font></td>");
						sb.append("<td>" + score + "</td>");
					} else if (prog.equalsIgnoreCase("新提交")) {
						sb.append("<td><font color='red'>" + "新提交"
								+ "</font></td>");
						sb.append("<td>" + score + "</td>");
					} else if (prog.equalsIgnoreCase("论文定稿")) {
						sb.append("<td>" + "已完结" + "</td>");
						sb.append("<td>" + score + "</td>");
					} else if (prog.equalsIgnoreCase("继续修改")) {
						sb.append("<td>" + "修改中" + "</td>");
						sb.append("<td>" + score + "</td>");
					} else {
						sb.append("<td>" + "未提交" + "</td>");
						sb.append("<td>" + score + "</td>");
					}
					sb.append("<td><input type='button' value='审阅论文' onclick=\"doSee('"
							+ t.getId() + " ')\"></td>");
				} else {
					sb.append("<td><input type='button' value='审阅实训作品' onclick=\"doSee('"
							+ t.getId() + " ')\"></td>");
				}
				sb.append("</tr>");
			}
			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：根据实习任务id和学生的id得到这个学生各个版本的实习论文或者实训作品 by吴敬国20150319
	 * 
	 * */
	@RequestMapping("teacher/getCheckThesisList.do")
	public ModelAndView getCheckThesisList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap modelMap)
			throws IOException {
		String task_type = (String) session.getAttribute("Task_type");
		String stu_id = request.getParameter("stu_id");
		session.setAttribute("Thesis_stu_id", stu_id);
		session.setAttribute("Train_stu_id", stu_id);
		String praTask_id = "";
		if (task_type.equalsIgnoreCase("1")) {
			praTask_id = (String) session.getAttribute("thesis_praTaskId");
		} else {
			praTask_id = (String) session.getAttribute("train_praTaskId");
		}
		int max_version = 0;
		String check_state;// 得到版本号最大的论文 的审核状态
		if (task_type.equalsIgnoreCase("1")) {
			GraduationThesis graduationThesis = new GraduationThesis();
			graduationThesis.setStu_id(stu_id);
			graduationThesis.setPractice_id(praTask_id);
			List<GraduationThesis> thesis_list = graduationThesisService
					.selectList(graduationThesis);
			if (thesis_list != null) {
				for (GraduationThesis g : thesis_list) {
					int version = Integer.parseInt(g.getVersion());
					String progrsss = g.getProgress();
					if (version > max_version) {
						max_version = version;
						check_state = progrsss;
						modelMap.put("check_state", check_state);
					}
				}
			}
			modelMap.put("thesis_list", thesis_list);
			modelMap.put("praTask_id", praTask_id);
			modelMap.put("Thesis_stu_id", stu_id);
			return new ModelAndView("teacher/checkThesisList", modelMap);
		} else {
			GraduationThesis graduationThesis = new GraduationThesis();
			graduationThesis.setStu_id(stu_id);
			graduationThesis.setPractice_id(praTask_id);
			List<GraduationThesis> trainList = graduationThesisService
					.selectList(graduationThesis);
			modelMap.put("praTask_id", praTask_id);
			modelMap.put("Thesis_stu_id", stu_id);
			modelMap.put("trainList", trainList);
			return new ModelAndView("teacher/checkTrainList", modelMap);
		}
	}

	/**
	 * 功能： 审阅论文或实训作品保存到数据库 byccc20141010
	 * 
	 */
	@RequestMapping("teacher/doCheckThesis.do")
	public String doCheckThesis(HttpServletRequest request, HttpSession session)
			throws IllegalStateException, IOException {
		String Thesis_stu_id = (String) session.getAttribute("Thesis_stu_id");
		String thesis_praTaskId = (String) session
				.getAttribute("thesis_praTaskId");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String comment = request.getParameter("comment");
		String progress = request.getParameter("progress");
		String score = request.getParameter("score");
		if (type.equalsIgnoreCase("毕业论文")) {
			if (progress == null) {
				progress = "";
				score = "0";
			} else if (progress.equalsIgnoreCase("继续修改")) {
				score = "0";
			}
		}
		String time = DateService.formatNowTime();
		GraduationThesis graduationThesis = DictionaryService
				.findGraduationThesis(id);
		if (type.equals("毕业论文")) {
			graduationThesis.setId(id);
			graduationThesis.setReview_time(time);
			graduationThesis.setProgress(progress);
			graduationThesis.setScore(score);
			graduationThesis.setComment(comment);
			graduationThesisService.update(graduationThesis);
			/* 审阅完论文同时发一条通知 */
			Notice notice = new Notice();
			Teacher tea = DictionaryService.findTeacher(DictionaryService
					.findPracticeTask(thesis_praTaskId).getTea_id());
			String notice_code = noticeService.getNoticeCode(thesis_praTaskId,
					Thesis_stu_id);
			notice.setTitle("毕业论文已审阅的通知");
			String noticeContent = "你的第" + graduationThesis.getVersion()
					+ "版毕业论文已被指导教师审阅，请及时查看。";
			notice.setContent(noticeContent);
			notice.setNotice_type("8");
			notice.setCreate_time(DateService.getNowTimeTimestamp());
			notice.setNotice_code(notice_code);
			notice.setStu_id(Thesis_stu_id);
			notice.setTea_id(tea.getId());
			noticeService.insert(notice);
			ret = "redirect:getCheckThesisList.do?stu_id=" + Thesis_stu_id;
		} else {
			graduationThesis.setId(id);
			graduationThesis.setReview_time(time);
			graduationThesis.setProgress(progress);
			graduationThesis.setComment(comment);
			graduationThesis.setScore(score);
			graduationThesis.setScore_type("百分制");
			graduationThesisService.update(graduationThesis);
			ret = "redirect:getCheckThesisList.do?stu_id=" + Thesis_stu_id;
		}
		return ret;
	}

	/**
	 * 功能：老师审阅毕业论文 或 实训作品 根据论文id得到这个学生这个任务这个版本的论文情况 byccc20141011
	 * 
	 * */
	@RequestMapping("teacher/checkThesisedit.do")
	public String toCheck(ModelMap modelMap, String id) {
		GraduationThesis gthesis = (GraduationThesis) graduationThesisService
				.selectByID(id);
		if (gthesis.getType().equals("毕业论文")) {
			ret = "teacher/checkThesisEdit";
		} else {
			ret = "teacher/checkTrainEdit";
		}
		modelMap.put("gthesis", gthesis);
		return ret;
	}

	/**
	 * 功能：老师下载毕业论文实训作品 byccc2014 0111
	 * */
	@RequestMapping("teacher/downloadFile.do")
	public String downloadFile(HttpSession session, HttpServletRequest request,
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
	 * 功能：展示实训作品信息 ，实训作品管理 by ccc 20141013 吴敬国 2015-5-5 2015-6-5
	 */
	@RequestMapping("teacher/checkTrainList.do")
	public ModelAndView checkTrainList(HttpSession session,
			HttpServletRequest request) {
		session.setAttribute("Task_type", "2");
		String Task_type = "2";
		Map<String, Object> map = new HashMap<String, Object>();
		String train_grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<PracticeTask> train_tasklist = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_idAndTasktype(tea.getId(),
						train_grade, Task_type);
		String train_praTaskId = null;
		String train_taskname = null;
		if (train_tasklist.size() > 0) {
			train_praTaskId = train_tasklist.get(0).getId();
			train_taskname = train_tasklist.get(0).getTask_name();
		} else {
			train_praTaskId = null;
			train_taskname = "请选择任务";
			train_grade = "入学年份";
		}
		List<GroupMembers> members = groupMembersService
				.studentsList(train_praTaskId);
		session.setAttribute("train_tasklist", train_tasklist);
		session.setAttribute("train_grade", train_grade);
		session.setAttribute("train_praTaskId", train_praTaskId);
		map.put("train_praTaskId", train_praTaskId);
		map.put("train_grade", train_grade);
		map.put("defaultTask_name", train_taskname);
		map.put("train_tasklist", train_tasklist);
		map.put("members", members);
		map.put("Task_type", Task_type);
		return new ModelAndView("teacher/trainStuList", map);
	}

	/**
	 * 功能：ajax传递根据 根据教师id和入学年份，任务类型（实习 实训）获得实习实训任务 审阅实习论文 实训作品都用到此方法 by
	 * 吴敬国2015-5-5
	 * 
	 * */
	@RequestMapping("teacher/ajaxGradechange.do")
	public String ajaxGradechange(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String task_type = (String) session.getAttribute("Task_type");
		String grade = request.getParameter("grade");
		if (task_type.equalsIgnoreCase("1")) {
			session.setAttribute("thesis_grade", grade);
		} else {
			session.setAttribute("train_grade", grade);
		}
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<PracticeTask> practiceTaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_idAndTasktype(tea_id, grade,
						task_type);
		session.setAttribute("thesis_tasklist", practiceTaskList);
		session.setAttribute("train_tasklist", practiceTaskList);
		StringBuffer sb = new StringBuffer();
		sb.append("<option " + "value='" + "请选择任务" + "'>" + "请选择任务"
				+ "</option>");
		for (PracticeTask c : practiceTaskList) {
			sb.append("<option " + "value='" + c.getId() + "'>"
					+ c.getTask_name() + "</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax传递 根据实训任务的id得到实训学生 实训作品 by吴敬国20150319 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxTrainStuListByPraId.do")
	public String ajaxTrainStuListByPraId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String Task_type = (String) session.getAttribute("Task_type");
		String cont = request.getParameter("trainPraIdAndGrade");
		int a = cont.indexOf("-");
		String train_praticeTaskId = cont.substring(0, a);
		String train_grade = cont.substring(a + 1, cont.length());
		session.setAttribute("train_grade", train_grade);
		session.setAttribute("train_praTaskId", train_praticeTaskId);
		if (train_praticeTaskId != null) {
			List<GroupMembers> members = groupMembersService
					.studentsList(train_praticeTaskId);
			StringBuffer sb = new StringBuffer();
			for (GroupMembers t : members) {
				// 查询学生是否提交，在这写语句，在用if判断
				String stu_id = t.getUser_id();// 得到单个学生的id
				GraduationThesis newTrain = graduationThesisService
						.getNewTrainByStuAndPraId(stu_id, train_praticeTaskId);
				String prog = "";
				if (newTrain != null) {
					prog = newTrain.getReview_time();
				}
				sb.append("<tr>");
				sb.append("<td>"
						+ DictionaryService.findOrg(
								DictionaryService.findStudent(t.getUser_id())
										.getClass_id()).getOrg_name() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getUser_id())
								.getStu_code() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getUser_id())
								.getTrue_name() + "</td>");
				sb.append("<td>"
						+ DictionaryService.findStudent(t.getUser_id())
								.getPhone() + "</td>");
				if (newTrain != null) {
					if (prog != null) {
						sb.append("<td>" + "无需要审核" + "</td>");
						sb.append("<td>" + newTrain.getCreate_time() + "</td>");
					} else {
						sb.append("<td><font color='red'>" + "新提交"
								+ "</font></td>");
						sb.append("<td>" + newTrain.getCreate_time() + "</td>");
					}
				} else {
					sb.append("<td>" + "未提交" + "</td>");
					sb.append("<td>" + "未提交" + "</td>");
				}
				sb.append("<td><input type='button' value='审阅实训作品' onclick=\"doSee('"
						+ t.getUser_id() + " ')\"></td>");
				sb.append("</tr>");
			}
			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：返回上一个实训作品，实训作品页面 by ccc 20141009 吴敬国 2015-4-17
	 */
	@RequestMapping("teacher/backTrainList.do")
	public ModelAndView backTrainList(HttpServletRequest request,
			HttpSession session) {
		String Task_type = (String) session.getAttribute("Task_type");
		String train_grade = (String) session.getAttribute("train_grade");
		String train_praTaskId = (String) session
				.getAttribute("train_praTaskId");
		String defaultTask_name = null;
		if (train_praTaskId != null) {
			defaultTask_name = DictionaryService.findPracticeTask(
					train_praTaskId).getTask_name();
		} else {
			defaultTask_name = "请选择任务";
		}
		List<PracticeTask> train_tasklist = (List<PracticeTask>) session
				.getAttribute("train_tasklist");
		List<GroupMembers> members = groupMembersService
				.studentsList(train_praTaskId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Task_type", Task_type);
		map.put("train_praTaskId", train_praTaskId);
		map.put("train_grade", train_grade);
		map.put("defaultTask_name", defaultTask_name);
		map.put("train_tasklist", train_tasklist);
		map.put("members", members);
		return new ModelAndView("teacher/trainStuList", map);
	}

	/**
	 * 功能：ajax传递根据 根据教师id和入学年份，任务类型（实习 实训）获得实习实训任务 审阅实习论文 实训作品都用到此方法 by
	 * 吴敬国2015-5-5
	 * 
	 * */
	@RequestMapping("teacher/ajaxThesisGradechange.do")
	public String ajaxGradechange1(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String task_type = (String) session.getAttribute("Task_type");
		String grade1 = request.getParameter("grade");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<PracticeTask> pa = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_idAndTasktype(tea_id, grade1,
						task_type);
		session.setAttribute("thesis_tasklist", pa);
		StringBuffer sb = new StringBuffer();
		sb.append("<option " + "value='" + "请选择任务" + "'>" + "请选择任务"
				+ "</option>");
		for (PracticeTask c : pa) {
			sb.append("<option " + "value='" + c.getId() + "'>"
					+ c.getTask_name() + "</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：根据实习任务id和学生id查看就业材料详情 byccc20141102* 吴敬国20150324
	 * 
	 * */
	@RequestMapping("teacher/doCheckMaterialsList.do")
	public String doCheckMaterialsList(ModelMap modelMap, String stu_id,
			HttpSession session) {
		session.setAttribute("Materials_stuid", stu_id);
		String true_name = DictionaryService.findStudent(stu_id).getTrue_name();
		String Materials_praid = (String) session
				.getAttribute("Materials_praid");
		List<GraduationMaterials> gs = graduationMaterialService
				.selectMaterialsByStuIdAndPraid(stu_id, Materials_praid);
		modelMap.put("gs", gs);
		modelMap.put("true_name", true_name);
		return "teacher/checkMaterialsList";
	}

	/**
	 * 功能：根据就业材料的id审核该次就业材料的具体信息 byccc20141011 wjg20150324
	 * */
	@RequestMapping("teacher/checkMaterialsedit.do")
	public String checkMaterialsedit(ModelMap modelMap, String id,
			HttpSession session) {
		GraduationMaterials materials = (GraduationMaterials) graduationMaterialService
				.selectByID(id);
		modelMap.put("materials", materials);
		return "teacher/checkGraduationMaterials";
	}

	/**
	 * 教师审核就业材料保存就业状态等信息 by ccc20140917 wjg20150324
	 * 
	 */
	@RequestMapping("teacher/doEditMaterials.do")
	public String doEditGraduation(HttpServletRequest request,
			HttpSession session) throws IllegalStateException, IOException {
		String Materials_stuid = (String) session
				.getAttribute("Materials_stuid");
		String id = request.getParameter("id");
		String check_state = request.getParameter("check_state");
		String note = request.getParameter("content");
		GraduationMaterials gMaterials = DictionaryService
				.findGraduationMaterials(id);
		gMaterials.setCheck_time(DateService.getNowTimeTimestamp());
		gMaterials.setCheck_state(check_state);
		gMaterials.setId(id);
		gMaterials.setNote(note);
		Object o = graduationMaterialService.update(gMaterials);
		if (o.equals(null)) {
			return null;
		} else {
			return "redirect:doCheckMaterialsList.do?stu_id=" + Materials_stuid;
		}
	}

	/**
	 * 遍历所有违纪记录 by王磊20141203 * 修改王磊，过滤筛选
	 */
	@RequestMapping("teacher/evaluateList.do")
	public ModelAndView evaluateList(HttpSession session, String type) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String evaluate_practiceId = (String) session
				.getAttribute("evaluate_practiceId");
		String evaluate_grade = (String) session.getAttribute("evaluate_grade");
		List<PracticeTask> pts = null;
		List<Evaluate> result = null;
		if (type != null) {
			session.removeAttribute("evaluate_practiceId");
			session.removeAttribute("evaluate_grade");
			evaluate_practiceId = null;
			evaluate_grade = null;
		}
		List<PracticeTask> Grade = practiceTaskService.getPracticeTasksGrade();
		Map<String, Object> map = new HashMap<String, Object>();
		if (Grade != null && Grade.size() != 0) {
			PracticeTask pt = new PracticeTask();
			pt.setGrade(Grade.get(0).getGrade());
			pt.setTea_id(tea_id);
			pt.setTask_type("1");
			pt.setState("1");
			pts = this.practiceTaskService.selectList(pt);
		}
		if (pts != null && pts.size() != 0) {
			result = this.evaluateService.listByTeaIdAndPracticeId(tea_id, pts
					.get(0).getId());
		}
		map.put("Grade", Grade);
		map.put("result", result);
		map.put("evaluate_practiceId", evaluate_practiceId);
		map.put("evaluate_grade", evaluate_grade);
		map.put("pts", pts);
		return new ModelAndView("teacher/evaluateList", map);
	}

	/**
	 * 功能：通过实践教学任务得到所对应的奖惩列表 by：王磊 2015年4月16日 2015年6月11日
	 */
	@RequestMapping("teacher/ajaxEvaluate.do")
	public String ajaxEvaluate(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String practice_id = request.getParameter("practice_id");
		session.setAttribute("evaluate_practiceId", practice_id);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Evaluate> evs = this.evaluateService.listByTeaIdAndPracticeId(
				tea_id, practice_id);
		StringBuffer sb = new StringBuffer();
		for (Evaluate evaluates : evs) {
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
			String createTime = format.format(evaluates.getTime());
			sb.append("<tr>");
			sb.append("<td align='center'>" + createTime + "</td>");
			sb.append("<td align='center'>"
					+ DictionaryService.findStudent(evaluates.getStu_id())
							.getStu_code() + "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findStudent(evaluates.getStu_id())
							.getTrue_name() + "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findOrg(
							DictionaryService
									.findStudent(evaluates.getStu_id())
									.getClass_id()).getOrg_name() + "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findPracticeTask(
							evaluates.getPractice_id()).getTask_name()
					+ "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findEvalsIndex(evaluates.getIndex_id())
							.getIndex_name() + "</td>");
			sb.append("	<td align='center'>" + evaluates.getDescription()
					+ "</td>");
			sb.append("	<td align='center'>" + evaluates.getScore() + "</td>");
			sb.append("	<td align='center's><input type='button' value='删除' onclick=doDel('"
					+ evaluates.getId() + "')></td>");
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
	 * 功能：遍历奖惩标准所有信息 修改：筛选出本学院和本校的奖惩标准 by王磊2015-1-25
	 * 
	 */
	@RequestMapping("teacher/evaluateAdd.do")
	public ModelAndView calssList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String tea_college_id = (String) session.getAttribute("tea_college_id");
		List<EvaluateStandard> evaResult = this.evaluatestandardService
				.selectByIds(tea_college_id, dept_id);
		List<PracticeTask> Grade = practiceTaskService.getPracticeTasksGrade();
		map.put("Grade", Grade);
		map.put("evaResult", evaResult);
		return new ModelAndView("teacher/evaluateAdd", map); // 添加成功后重定向到列表页面
	}

	/*
	 * 功能：查找奖惩标准对应的奖惩指标
	 * 
	 * 王磊
	 */
	@RequestMapping("teacher/getEvaIdex.do")
	public String evaluateIdexList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String standard_id = request.getParameter("standard_id");
		EvalsIndex evalsindex = new EvalsIndex();
		evalsindex.setStandard_id(standard_id);
		List<EvalsIndex> ei = this.evalsIndexService.selectList(evalsindex);
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='index_id'>");
		for (EvalsIndex eix : ei) {
			sb.append("<option " + "value=" + eix.getId() + ">"
					+ eix.getIndex_name() + "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 功能：添加奖惩记录 王磊 by20141204
	 * 
	 * */
	@RequestMapping("teacher/addevaluate.do")
	public String saveEvaluate(HttpSession session, HttpServletRequest request)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String standard_id = request.getParameter("standard_id");
		String stu_id = request.getParameter("stu_id");
		String index_id = request.getParameter("index_id");
		String practice_id = request.getParameter("practice_id");
		Double score = DictionaryService.findEvalsIndex(index_id).getScore();
		String description = request.getParameter("description");
		String time = request.getParameter("time");
		if (time == null) {
			time = "2010-01-01";
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format1.parse(time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String place = request.getParameter("place");

		String[] stu_ids = stu_id.split(",");
		Evaluate evaluateTemp;// 数据库修改，所以第一个遍历错误。
		for (int i = 0; i < stu_ids.length; i++) {
			evaluateTemp = new Evaluate();
			evaluateTemp.setTime(ts);
			evaluateTemp.setStandard_id(standard_id);
			evaluateTemp.setDescription(description);
			evaluateTemp.setId(Common.returnUUID());
			evaluateTemp.setTea_id(tea_id);
			evaluateTemp.setIndex_id(index_id);
			evaluateTemp.setScore(score);
			evaluateTemp.setPlace(place);
			evaluateTemp.setPractice_id(practice_id);
			evaluateTemp.setStu_id(stu_ids[i]);
			evaluateService.insert(evaluateTemp);
		}

		return "redirect:evaluateList.do?type='1'"; // 添加成功后重定向到列表页面
	}

	/*
	 * 功能：查找奖惩标准对应的奖惩指标
	 * 
	 * 王磊
	 */
	@RequestMapping("teacher/evaluateIdexList.do")
	public String typeList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String standard_id = request.getParameter("standard_id");
		EvalsIndex evalsindex = new EvalsIndex();
		evalsindex.setStandard_id(standard_id);
		List<EvalsIndex> ei = this.evalsIndexservice.selectList(evalsindex);
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='index_id' style='width:150px'>");
		sb.append("<option value='0'>请选择类型</option>");
		for (EvalsIndex eix : ei) {
			sb.append("<option " + "value=" + eix.getId() + ">"
					+ eix.getIndex_name() + "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 功能：根据id删除奖惩记录 by 王磊 20141203
	 * 
	 */
	@RequestMapping("teacher/deleteEvaluate.do")
	public String deleteEvaluate(String id) {
		Evaluate e = new Evaluate();
		e.setId(id);
		evaluateService.delete(e);
		return "redirect:evaluateList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：根据实训id查找出对应的学生 信息核对任务添加用到此方法 by wl 20141205 修改 王磊 20150121
	 * 实训id查找出对应的学生
	 * */
	@RequestMapping("teacher/studentListByPraCodeTeaCode.do")
	public String studentListByPraCodeTeaCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String practice_id = request.getParameter("practice_id");
		List<GroupMembers> SuList = this.groupMembersService
				.studentsList(practice_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<tr id = 'select'>");
		int i = 0;
		for (GroupMembers gm : SuList) {
			if (DictionaryService.findStudent(gm.getUser_id()).getState()
					.equals("1")) {
				String name = DictionaryService.findStudent(gm.getUser_id())
						.getTrue_name();
				if (i % 5 == 0) {
					sb.append("</tr>");
					sb.append("<br>");
					sb.append("<tr>");
				}
				sb.append("<td><label for='" + gm.getUser_id()
						+ "'><input type='checkbox' id='" + gm.getUser_id()
						+ "' name='students' value ='" + gm.getUser_id() + "'>"
						+ name + "</label></td>");
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
	 * 功能：通过实习任务得到学生 针对实习任务 学生信息管理功能中用到此方法 再此修改2016年4月6日 by周睿
	 */
	@RequestMapping("teacher/studentListByPraId.do")
	@ResponseBody
	public String studentListByPraCode(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String practice_name = request.getParameter("practice_id");// 获取任务name
		List<Student> stus = studentService.getStuListByGroupID(practice_name);
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<th width='30' align='center'>选择</th>");
		sb.append("<th width='70' align='center'>学号</th>");
		sb.append("<th width='70' align='center'>姓名</th>");
		sb.append("<th width='80' align='center'>手机号</th>");
		sb.append("<th width='100' align='center'>家庭电话</th>");
		sb.append("<th width='120' align='center'>家庭住址</th>");
		sb.append("<th width='80' align='center'>QQ号</th>");
		sb.append("<th width='80' align='center'>EMAIL</th>");
		sb.append("<th width='70' align='center'>入学年份</th>");
		sb.append("<th width='70' align='center'>实习状态</th>");
		sb.append("<th width='70' align='center'>岗位</th>");
		sb.append("</tr>");
		int i = 0;
		for (Student s : stus) {
			if (s.getState().equals("1")) {
				sb.append("<tr>");
				sb.append("<td><input type='radio' name='postName' id='postName' value='"
						+ s.getId() + "'></td>");
				sb.append("<td>" + s.getStu_code() + "</td>");
				sb.append("<td>" + s.getTrue_name() + "</td>");
				sb.append("<td>" + s.getPhone() + "</td>");
				sb.append("<td>" + s.getHome_phone() + "</td>");
				sb.append("<td>" + s.getHome_addr() + "</td>");
				sb.append("<td>" + s.getQqnum() + "</td>");
				sb.append("<td>" + s.getEmail() + "</td>");
				sb.append("<td>" + s.getEntry_year() + "</td>");
				String state = DictionaryService.findStuGraStateName(
						s.getPractice_state()).getStateDesc();
				sb.append("<td>" + state + "</td>");

				PracticeRecord stu_pra = practiceRecordService
						.getPrecordUndimission_time(s.getId());
				String post_id = new String();
				if (stu_pra != null) {
					post_id = stu_pra.getPost_id();
				} else {
					post_id = "";
				}

				sb.append("<td>" + post_id + "</td>");
				sb.append("</tr>");
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
	 * 功能：通过年份查出老师所带的实习任务 针对实习任务 招聘信息的智能推送用到 指导记录的过滤用到 王磊20150121
	 * 
	 * */
	@RequestMapping("teacher/getPracticeTask.do")
	public String getPracticeTask(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String year = request.getParameter("grade");
		session.setAttribute("year", year);
		session.setAttribute("evaluate_grade", year);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		response.setCharacterEncoding("UTF-8");
		PracticeTask pt = new PracticeTask();
		pt.setGrade(year);
		pt.setTea_id(tea_id);
		pt.setTask_type("1");
		pt.setState("1");
		List<PracticeTask> pts = this.practiceTaskService.selectList(pt);
		PracticeTask p = new PracticeTask();
		StringBuffer sb = new StringBuffer();
		String evaluate_practiceId = (String) session
				.getAttribute("evaluate_practiceId");
		sb.append("<select name='practice_id' id='practice_id' style='width:200px' onChange='changeCK()'>");
		boolean b = false;
		if (DictionaryService.findPracticeTask(evaluate_practiceId) != null) {
			b = evaluate_practiceId.equals(DictionaryService.findPracticeTask(
					evaluate_practiceId).getId());
		}
		if (b == false || pts.size() == 0) {
			sb.append("<option value='0'>请选择任务</option>");
		} else {
			p = DictionaryService.findPracticeTask(evaluate_practiceId);
			for (int i = 0; i < pts.size(); i++) {
				if (pts.get(i).getId().equals(evaluate_practiceId)) {
					pts.add(0, p);
					pts.remove(i + 1);
				}
			}
		}
		for (PracticeTask ps : pts) {
			sb.append("<option value=" + ps.getId() + ">" + ps.getTask_name()
					+ "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通知管理 类.
	 * 
	 * @author 王磊 2014.12.02
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/noticeList.do")
	public ModelAndView noticeList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Notice t = new Notice();
		t.setTea_id(Common.getOneTeaId(session));
		t.setNotice_type("2");
		List<Notice> result = this.noticeService.selectList(t);// 得到自己发布的通知
		// 分页
		int pageSize = pageSizeConstant;
		int Currentpage = 1;
		/*
		 * if(result.size()<5){//防止集合大小不满足每页记录，进行赋值。 pageSize=result.size(); }
		 */
		// 获取当前页集合
		List<Notice> listCurrentPage = Common.getListCurrentPage(result,
				pageSize, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(result, pageSize, Currentpage);
		// 放到session，方便分页时调用
		session.setAttribute("SelfNoticeList", result);
		map.put("SelfCount", pageCount);
		map.put("SelfnowPage", Currentpage);
		List<Notice> college_NoticeList = noticeService
				.getCollegeNoticeList(session);
		map.put("result", listCurrentPage);
		map.put("college_NoticeList", college_NoticeList);
		return new ModelAndView("teacher/noticeList", map);
	}

	/**
	 * 实现分页功能类.
	 * 
	 * @author 王磊 2015年4月4日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/getNOticeByPage.do")
	public String getNOticeByPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		// 获取教师集合，增加条件：下拉框选中的组织id
		List<Notice> result = (List<Notice>) session
				.getAttribute("SelfNoticeList");
		String toPage = request.getParameter("toPage");
		List<Notice> newResult;// 当前页的集合
		newResult = Common.getListCurrentPage(result, pageSizeConstant,
				Integer.parseInt(toPage));
		StringBuffer sb = new StringBuffer();
		for (Notice ns : newResult) {
			/*
			 * String stu_ids[] = ns.getStu_id().split(","); String stu_name =
			 * ""; for(int i=0;i<stu_ids.length;i++){
			 * stu_name=stu_name+DictionaryService
			 * .findStudent(stu_ids[i]).getTrue_name()+" "; }
			 */
			String[] stu_ids = ns.getStu_id().split(",");
			String stu_name = "";
			int showCount = pageSizeConstant;// 5
			if (stu_ids.length <= pageSizeConstant) {// 5){
				showCount = stu_ids.length;
				for (int i = 0; i < showCount; i++) {
					if (DictionaryService.findStudent(stu_ids[i]) != null) {
						stu_name = stu_name
								+ DictionaryService.findStudent(stu_ids[i])
										.getTrue_name() + " ";
					}
				}
			} else {
				for (int i = 0; i < showCount; i++) {
					if (DictionaryService.findStudent(stu_ids[i]) == null) {
						stu_name = null;
					} else {
						stu_name = stu_name
								+ DictionaryService.findStudent(stu_ids[i])
										.getTrue_name() + " ";
					}
				}
			}
			String newContent;
			if (ns.getContent().length() >= 15) {
				newContent = ns.getContent().substring(0, 15) + "......";
			} else {
				newContent = ns.getContent();
			}
			sb.append("<tr>");
			sb.append("	<td align='center'><a href='detailNotice.do?id="
					+ ns.getId() + "'>" + ns.getTitle() + "</a></td>");
			sb.append("<td align='center'>" + "指导教师通知" + "</td>");
			sb.append("	<td align='center'>" + newContent + "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findTeacher(ns.getTea_id())
							.getTrue_name() + "</td>");
			sb.append("	<td align='center'>" + stu_name + "</td>");
			sb.append("	<td align='center'><input type='button' value='修改' onclick=doEditNotice('"
					+ ns.getId() + "');>");
			sb.append("<input type='button' value='删除' onclick=doDel('"
					+ ns.getId() + "');></td>");
			sb.append("</tr>");
		}
		sb.append("," + toPage);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 老师发布通知公告类.
	 * 
	 * @author 王磊 2014年12月02日
	 * @author 吴敬国 张向阳组
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/addNotice.do")
	public ModelAndView addNotice(HttpSession session, ModelMap modelMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		// List<PracticeTask> Grade =
		// practiceTaskService.getPracticeTasksGrade();得到年份的集合----现在已经没有用
		String checkinfo_Grade = Common.getDefaultYear();// 得到默认年份2013
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获取到当前用户
		String tea_id = tea.getId();// 得到当前用户的ID
		// 通过当前用户的id和默认年份2013找到实习任务列表
		List<PracticeTask> checkinfo_pulltaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(tea_id, checkinfo_Grade);

		// 初始化任务的id和任务的名称为null
		String checkinfo_pullTaskId = null;
		String checkinfo_pullTaskName = null;

		// 判断是否获取到实习任务列表
		if (checkinfo_pulltaskList.size() > 0) {// 如果拿到实习任务列表、对任务id和任务名称赋值
			checkinfo_pullTaskId = checkinfo_pulltaskList.get(0).getId();
			checkinfo_pullTaskName = checkinfo_pulltaskList.get(0)
					.getTask_name();
		} else {
			// 否则，初始化变量
			checkinfo_pullTaskId = "请选择实习任务";
			checkinfo_pullTaskName = "请选择任务";
			checkinfo_Grade = "入学年份";
		}

		// 拿到任务列表，现在已经没有，被其他方法代替
		// List<PracticeTask> checkInfo_taskList =
		// this.checkInfoService.selectCheckInfoListByTeaIdAndGrade(tea_id,checkinfo_Grade,checkinfo_pullTaskId);

		// 以下代码是拿到默认年份2013、对应实习任务的学生列表
		String practice_id = checkinfo_pullTaskId;// 将默认年份2013、对应实习任务的id赋值给practice_id
		List<GroupMembers> SuList = this.groupMembersService
				.studentsList(practice_id);// 根据任务id拿到默认年份2013、对应实习任务的学生类表
		StringBuffer sb = new StringBuffer();//
		sb.append("<tr id = 'select'>");
		int i = 0;
		for (GroupMembers gm : SuList) {
			if (DictionaryService.findStudent(gm.getUser_id()).getState()
					.equals("1")) {
				String name = DictionaryService.findStudent(gm.getUser_id())
						.getTrue_name();
				if (i % 5 == 0) {// 每行显示五个学生
					sb.append("</tr>");
					sb.append("<br>");
					sb.append("<tr>");
				}
				sb.append("<td><label for='" + gm.getUser_id()
						+ "'><input type='checkbox' id='" + gm.getUser_id()
						+ "' name='students' value ='" + gm.getUser_id() + "'>"
						+ name + "</label></td>");
				i++;
			}
		}

		map.put("student_name", sb.toString());// 将拿到的包装后的学生列表放进map
		map.put("checkinfo_pulltaskList", checkinfo_pulltaskList);// 任务列表添加map
		map.put("task_grade", checkinfo_Grade);// 将默认年份添加map
		map.put("checkinfo_pullTaskName", checkinfo_pullTaskName);// 默认任务添加map

		// session.setAttribute("checkinfo_Grade", checkinfo_Grade);//过滤条件的年级
		// session.setAttribute("checkinfo_TaskId",
		// checkinfo_pullTaskId);//过滤条件选择的任务id
		// session.setAttribute("checkInfo_taskList",
		// checkinfo_pulltaskList);//过滤条件任务下拉列表
		// map.put("checkInfo_taskList", checkInfo_taskList);
		// map.put("Grade", Grade);
		return new ModelAndView("teacher/noticeAdd", map);// 将map带到界面
	}

	/**
	 * 教师删除通知类.
	 * 
	 * @author 王磊 20141205
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/deleteNotice.do")
	public String deleteNotice(String id, HttpServletRequest request) {
		Notice n = DictionaryService.findNotice(id);
		String file_id = n.getTemp2();
		if (file_id != null) {
			filesService.deleteFile(file_id, request);
		}
		noticeService.delete(n);
		return "redirect:noticeList.do";
	}

	/**
	 * 教师查看通知公告 类.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/detailNotice.do")
	public String detailNotice(ModelMap modelMap, String id) {
		Notice n = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", n);
		return "teacher/noticeDetail";
	}

	/**
	 * 教师修改自己的通知公告类.
	 * 
	 * @author 王磊 2015年3月31日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/editlNotice.do")
	public String editlNotice(ModelMap modelMap, String id) {
		Notice n = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", n);
		return "teacher/noticeEdit";
	}

	/**
	 * 教师修改通知公告类.
	 * 
	 * @author 王磊 2015年4月1日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/doEditlNotice.do")
	public String doEditlNotice(String id, HttpServletRequest request)
			throws IOException {
		String content = request.getParameter("content");
		String title = request.getParameter("title");
		Notice n = (Notice) this.noticeService.selectByID(id);

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
					fil.setStu_id(n.getTea_id());//
					filesService.insert(fil);
					n.setTemp2(file_id);
				}
			}
		}

		n.setTitle(title);
		n.setContent(content);
		noticeService.update(n);
		return "redirect:noticeList.do";
	}

	/**
	 * 老师添加通知公告保存 .
	 * 
	 * @author 王磊 2015年4月1日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/doAddNotice.do")
	public String saveNotice(HttpServletRequest request, HttpSession session)
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
					fil.setStu_id(practice_id);// 保存创建实践任务的id
					filesService.insert(fil);
					notice.setTemp2(file_id);
				}
			}
		}
		String content = request.getParameter("content");
		String title = request.getParameter("title");
		String notice_range = request.getParameter("notice_range");
		notice.setTea_id(tea_id);
		notice.setOrg_id("");
		notice.setCreate_time(time);
		notice.setNotice_code(notice_code);
		notice.setNotice_type("2");
		notice.setContent(content);
		notice.setTitle(title);
		notice.setStu_id(notice_range);
		noticeService.insert(notice);
		return "redirect:noticeList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 老师维护个人信息 .
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/editmyInfo.do")
	public ModelAndView editmyInfo(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.selectByID(Common
				.getOneTeaId(session));
		map.put("teacher", teacher);
		return new ModelAndView("teacher/editmyInfo", map);

	}

	/**
	 * 保存个人信息 .
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/doeditmyInfo.do")
	public String editTeacher1(HttpSession session, HttpServletRequest request)
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

	@RequestMapping("teacher/myInfo.do")
	public ModelAndView myInfo(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());
		map.put("List", teaList);
		return new ModelAndView("teacher/index", map);

	}

	/**
	 * 修改密码 .
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/passwordEdit.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.selectByID(Common
				.getOneTeaId(session));
		map.put("teacher", teacher);
		return new ModelAndView("teacher/passwordEdit", map);
	}

	/**
	 * 保存新密码 .
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/doPasswordEdit.do")
	public String doPasswordEdit(String tea_id, HttpServletRequest request)
			throws IOException {
		String password = request.getParameter("login_pass2");
		Teacher teacher = DictionaryService.findTeacher(tea_id);
		teacher.setLogin_pass(password);
		teacherService.update(teacher);
		return "redirect:../login.jsp";
	}

	/**
	 * 功能：修改密码 注解请求地址(映射) 郑春光20140910
	 * 
	 * */
	@RequestMapping("teacher/password.do")
	public ModelAndView password(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());
		map.put("List", teaList);
		return new ModelAndView("teacher/index", map);
	}

	/**
	 * 信息核对管理 初始化数据 by吴敬国 20141123 String practice_id
	 * */
	@RequestMapping("teacher/checkInfoList.do")
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
		return new ModelAndView("teacher/checkInfoList", map);
	}

	/**
	 * 根据年级重新进入该模块主界面 by吴敬国20150312 2015-6-11
	 * 
	 * */
	@RequestMapping("teacher/backcheckInfoList.do")
	public ModelAndView backpracticetaskList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String checkinfo_Grade = (String) session
				.getAttribute("checkinfo_Grade");
		String checkinfo_TaskId = (String) session
				.getAttribute("checkinfo_TaskId");
		List<PracticeTask> checkInfo_taskList = this.checkInfoService
				.selectCheckInfoListByTeaIdAndGrade(
						Common.getOneTeaId(session), checkinfo_Grade,
						checkinfo_TaskId);
		String checkinfo_pullTaskName = null;
		if (checkinfo_TaskId.equalsIgnoreCase("请选择实习任务")) {
			checkinfo_pullTaskName = "请选择实习任务";
		} else {
			checkinfo_pullTaskName = DictionaryService.findPracticeTask(
					checkinfo_TaskId).getTask_name();
		}
		List<PracticeTask> checkinfo_pulltaskList = (List<PracticeTask>) session
				.getAttribute("checkInfo_taskList");
		map.put("checkinfo_pullTaskName", checkinfo_pullTaskName);
		map.put("checkinfo_pulltaskList", checkinfo_pulltaskList);
		map.put("checkInfo_taskList", checkInfo_taskList);
		map.put("checkinfo_Grade", checkinfo_Grade);
		return new ModelAndView("teacher/checkInfoList", map);
	}

	/**
	 * 功能：ajax传递 根据年级和实习任务id搜索信息核对任务 by 吴敬国20150117 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxgetCheckInfo.do")
	public String ajaxgetCheckInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String checkinfo_Grade = request.getParameter("grade");
		String checkinfo_TaskId = request.getParameter("praticeTaskId");
		session.setAttribute("checkinfo_TaskId", checkinfo_TaskId);// 记录过滤查询中的年级和实习任务变化
		session.setAttribute("checkinfo_Grade", checkinfo_Grade);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		if (checkinfo_Grade != null) {
			List<PracticeTask> pa = this.checkInfoService
					.selectCheckInfoListByTeaIdAndGrade(tea_id,
							checkinfo_Grade, checkinfo_TaskId);
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy/MM/dd");

			int j = 1;// 定义序号变量

			for (PracticeTask ts : pa) {
				Date d = ts.getBegin_time();
				Date e = ts.getEnd_time();
				Date c = ts.getCreate_time();// 定义一个创建时间
				sb.append("<tr>");
				// sb.append("<td>" + ts.getPractice_code() + "</td>");

				sb.append("<td>" + (j++) + "</td>");// 更改任务编码为序号

				sb.append("<td>" + ts.getTask_name() + "</td>");
				sb.append("	<td>" + dateFm.format(c) + "</td>");// 显示出创建时间列
				sb.append("	<td>" + dateFm.format(d) + "</td>");
				sb.append("	<td>" + dateFm.format(e) + "</td>");
				sb.append("	<td>" + ts.getTask_desc() + "</td>");
				sb.append("	<td><input type='button' value='查看核对情况' onclick=\"doSee('"
						+ ts.getId() + " ')\"></td>");
				sb.append("	<td><input type='button' value='修改' onclick=\"doEdit('"
						+ ts.getId() + " ')\"></td>");
				sb.append("	<td><input type='button' value='删除' onclick=\"doDel('"
						+ ts.getId() + " ')\"></td>");
				sb.append("</tr>");
			}
			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：添加信息核对页面 by 吴敬国20141123 *
	 * 
	 * */
	@RequestMapping("teacher/addCheckInfo.do")
	public ModelAndView addCheckInfo(HttpSession session, ModelMap modelMap) {
		List<PracticeTask> Grade = practiceTaskService.getPracticeTasksGrade();// 得到实践任务已有的年份
		modelMap.put("Grade", Grade);
		return new ModelAndView("teacher/checkInfoAdd", modelMap);
	}

	/**
	 * 功能：保存 核对信息 将核对信息分别保存到实践任务表和信息核对表 通知表 by吴敬国20141123 修改 by ccc2015年0124
	 * 
	 * */
	@RequestMapping("teacher/doAddCheckInfo.do")
	public String saveCheckInfo(HttpServletRequest request, HttpSession session)
			throws IOException {
		PracticeTask pra = new PracticeTask();
		Notice notice = new Notice();
		String stu = request.getParameter("stu_range");
		String grade = request.getParameter("grade");
		String create_Time = request.getParameter("create_Time");
		String begin_Time = request.getParameter("begin_Time");
		String end_Time = request.getParameter("end_Time");
		String task_desc = request.getParameter("task_desc");
		String practice_id = request.getParameter("practice_id");
		String task_Name = request.getParameter("task_name");
		String practice_code = DictionaryService.findPracticeTask(practice_id)
				.getPractice_code();
		// String task_Name =
		// DictionaryService.findPracticeTask(practice_id).getTask_name();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		pra.setTea_id(tea_id);
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
		notice.setStu_id(stu);

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
		String[] studentSelected = request.getParameterValues("students");
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
		return "redirect:checkInfoStudent.do?id=" + pra.getId();
	}

	/**
	 * 功能：教师核对信息 by ccc20150130 操作实践任务表和核对信息表 String
	 * id,id为实践任务表中信息核对类型的id，在信息核对表中是checktask_id
	 * 
	 * */
	@RequestMapping("teacher/checkInfoStudent.do")
	public String checkInfoStudent(HttpSession session, ModelMap modelMap,
			String id) {
		String checkInfo_id = id;
		List<InfoCheckRecord> result = checkInfoService
				.selectCheckInfoByChecktask_id(checkInfo_id);
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
		return "teacher/checkInfoStudent";
	}

	/**
	 * 功能：教师删除核对信息 by 吴敬国20141110 操作实践任务表和核对信息表
	 * 
	 * */
	@RequestMapping("teacher/deleteCheckInfo.do")
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
		return "redirect:backcheckInfoList.do";
	}

	/**
	 * 功能：转到编辑发布的核对信息任务 by吴敬国20141124
	 * 
	 * */
	@RequestMapping("teacher/editCheckInfo.do")
	public ModelAndView editCheckInfo(ModelMap modelMap, String id) {
		PracticeTask ic = (PracticeTask) practiceTaskService.selectByID(id);
		modelMap.put("ic", ic);
		return new ModelAndView("teacher/checkInfoEdit", modelMap);
	}

	/**
	 * 编辑已经发布的核对信息任务 功能： 吴敬国20141123
	 * 
	 * */
	@RequestMapping("teacher/doEditCheckInfo.do")
	public String saveCheckInfo(HttpServletRequest request)
			throws IllegalStateException, IOException {
		String id = request.getParameter("id");
		PracticeTask practiceTask = DictionaryService.findPracticeTask(id);
		String task_name = request.getParameter("task_name");
		String task_desc = request.getParameter("task_desc");
		String end_time = request.getParameter("end_time");
		String begin_time = request.getParameter("begin_time");
		String create_time = request.getParameter("create_time");
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
		return "redirect:backcheckInfoList.do";
	}

	/**
	 * Bmap 查看学生签到情况 使用@RequestMapping by 邢志武 20141115 修改mouth为ym by 李达 20160115
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("teacher/bMap.do")
	public String bMap(HttpSession session, ModelMap modelMap)
			throws UnsupportedEncodingException {
		/*
		 * Teacher tea = (Teacher) session.getAttribute("current_user"); String
		 * tea_id = tea.getId();
		 * 
		 * // 获取该老师管理的学生中签到学生的所有信息 List<BMapEntity> allSinStuMes = new
		 * ArrayList<BMapEntity>(); // 声明一个存放该老师所有学生信息的list
		 * List<List<BMapEntity>> all = new ArrayList<List<BMapEntity>>(); //
		 * 获取该老师管理的学生中从未签到的学生的所有信息 List<BMapEntity> neverSinStuMes = new
		 * ArrayList<BMapEntity>(); // 声明一个存放该老师从未签到过的学生的list
		 * List<List<BMapEntity>> never = new ArrayList<List<BMapEntity>>(); //
		 * 存放所有签到学生经度的list List<BMapEntity> allLatitude = new
		 * ArrayList<BMapEntity>(); // 存放所有签到同学纬度的list List<BMapEntity>
		 * allLongitude = new ArrayList<BMapEntity>(); // 用于转化数据成json
		 * ByteArrayOutputStream os = new ByteArrayOutputStream(); //
		 * 可以捕获内存缓冲区的数据，转换成字节数组。 ObjectMapper objectMapper = new ObjectMapper();
		 * 
		 * 
		 * // 自定义时间格式 SimpleDateFormat fmt = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * objectMapper.setDateFormat(fmt); // 获取该老师管理的学生中签到的学生的stu_id
		 * List<BMapEntity> allSinStuId = this.signService
		 * .selectAllSignStuIdByTeaCode(tea_id);
		 * 
		 * for (int i = 0; i < allSinStuId.size(); i++) { String stu_id =
		 * allSinStuId.get(i).getUser_id(); // 根据stu_code查询所需要的学生信息 allSinStuMes
		 * = this.signService.selectAllStuByStuID(stu_id);
		 * all.add(allSinStuMes); } // 获取每个签到学生最近一次的经度 double Xmax = 0; double
		 * Xmin = 1000; for (int i = 0; i < allSinStuId.size(); i++) { String
		 * stu_id = allSinStuId.get(i).getUser_id(); allLatitude =
		 * this.signService.selectStuLongitudeByStuId(stu_id); for (int j = 0; j
		 * < allLatitude.size(); j++) { BMapEntity bx = allLatitude.get(j);
		 * double x = bx.getLatitude(); if (Xmax < x) { Xmax = x; } if (x <
		 * Xmin) { Xmin = x; } } } // 获取每个签到学生最近一次的纬度 double Ymax = 0; double
		 * Ymin = 1000; for (int i = 0; i < allSinStuId.size(); i++) { String
		 * stu_id = allSinStuId.get(i).getUser_id(); allLongitude =
		 * this.signService.selectStuLatitudeByStuId(stu_id); for (int j = 0; j
		 * < allLongitude.size(); j++) { BMapEntity bx = allLongitude.get(j);
		 * double x = bx.getLongitude(); if (Ymax < x) { Ymax = x; } if (x <
		 * Ymin) { Ymin = x; } } } // 获取从未签到过学生的stu_id List<BMapEntity>
		 * NeverSignStuId = this.signService
		 * .selectNeverSignStuIdByTeaCode(tea_id); for (int k = 0; k <
		 * NeverSignStuId.size(); k++) { String stu_id =
		 * NeverSignStuId.get(k).getUser_id(); // 根据stu_code查询所需要的学生信息
		 * neverSinStuMes = this.signService.selectUnSignAllStuByStuId(stu_id);
		 * never.add(neverSinStuMes); } try { JsonGenerator jsonGenerator =
		 * objectMapper.getJsonFactory() .createJsonGenerator(os,
		 * JsonEncoding.UTF8); jsonGenerator.writeObject(all);
		 * 
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace(); } byte[] buf = os.toByteArray(); //
		 * 获取内存缓冲中的数据,返回字节数组 String sJson = new String(buf, "UTF8");
		 * modelMap.put("ajson", sJson); modelMap.put("Xmax", Xmax);
		 * modelMap.put("Xmin", Xmin); modelMap.put("Ymax", Ymax);
		 * modelMap.put("Ymin", Ymin); modelMap.put("never", never);
		 */
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		// 获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		java.util.Date date = new java.util.Date();
		String nowTime = sdf.format(date);
		/* int month = cal.get(Calendar.MONTH) + 1; */
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		String ym = sdf2.format(cal.getTime());
		// 设置默认的查询年级
		String grade = "";
		List<PracticeTask> pks = this.practiceTaskService
				.selectOutSchoolPracticeTasks(tea_id);
		if (pks.size() > 0) {
			// 获得实践任务的id
			String pk_id = pks.get(0).getId();
			// 获取时间任务的年级
			grade = DictionaryService.findPracticeTask(pk_id).getGrade();
			// 获取该老师管理的学生中签到学生的所有信息
			List<BMapEntity> allSinStuMes = new ArrayList<BMapEntity>();
			// 声明一个存放该老师所有学生信息的list
			List<List<BMapEntity>> all = new ArrayList<List<BMapEntity>>();
			// 获取该老师管理的学生中签到的学生的stu_id
			List<String> allSinStuId = this.practiceTaskService
					.getGroupMembersBypId(pk_id, grade);
			// 用于转化数据成json
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			// 可以捕获内存缓冲区的数据，转换成字节数组。
			ObjectMapper objectMapper = new ObjectMapper();
			// 自定义时间格式
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(fmt);
			for (int i = 0; i < allSinStuId.size(); i++) {
				String stu_id = allSinStuId.get(i);
				// 根据stu_code查询所需要的学生信息
				allSinStuMes = this.signService.selectAllStuByStuID(stu_id);
				all.add(allSinStuMes);
			}
			try {
				JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
						.createJsonGenerator(os, JsonEncoding.UTF8);
				jsonGenerator.writeObject(all);
			} catch (IOException e) {

				e.printStackTrace();
			}
			byte[] buf = os.toByteArray(); // 获取内存缓冲中的数据,返回字节数组
			String sJson = new String(buf, "UTF8");
			modelMap.put("sJson", sJson);
		}
		modelMap.put("ym", ym);
		modelMap.put("grade", grade);
		modelMap.put("pks", pks);
		modelMap.put("nowTime", nowTime);
		return "teacher/bMap";
	}

	/**
	 * 功能：ajax传递pk_id 邢志武2015-01-24 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxPk_id.do")
	@ResponseBody
	public String ajaxStudents(HttpSession session, HttpServletRequest request,
			ModelMap modelMap) throws IOException {
		String pk_id = request.getParameter("pk_id");
		String year = request.getParameter("year");
		// 获取该老师管理的学生中签到学生的所有信息
		List<BMapEntity> allSinStuMes = new ArrayList<BMapEntity>();
		// 声明一个存放该老师所有学生信息的list
		List<List<BMapEntity>> all = new ArrayList<List<BMapEntity>>();
		// 获取该老师管理学生的stu_id
		List<String> allSinStuId = this.practiceTaskService
				.getGroupMembersBypId(pk_id, year);
		// 用于转化数据成json
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 可以捕获内存缓冲区的数据，转换成字节数组。
		ObjectMapper objectMapper = new ObjectMapper();
		// 自定义时间格式
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(fmt);
		for (int i = 0; i < allSinStuId.size(); i++) {
			String stu_id = allSinStuId.get(i);
			// 根据stu_code查询所需要的学生信息
			allSinStuMes = this.signService.selectAllStuByStuID(stu_id);
			all.add(allSinStuMes);
		}
		try {
			JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
					.createJsonGenerator(os, JsonEncoding.UTF8);
			jsonGenerator.writeObject(all);
		} catch (IOException e) {

			e.printStackTrace();
		}
		byte[] buf = os.toByteArray(); // 获取内存缓冲中的数据,返回字节数组
		String sJson = new String(buf, "UTF8");
		return sJson;

	}

	/**
	 * 根据年份查询该老师该年内的小组
	 * 
	 */
	@RequestMapping("teacher/ajaxYear.do")
	public String ajaxYear(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String year = request.getParameter("year");
		List<PracticeTask> PracticeTask = practiceTaskService
				.getOutPracticeTasksBytea_id(tea_id, year);
		StringBuffer sb = new StringBuffer();
		sb.append("<option >" + "请选择" + "</option>");
		for (PracticeTask c : PracticeTask) {
			sb.append("<option " + "value='" + c.getId() + "'>"
					+ c.getTask_name() + "</option>");
		}

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ajaxUnSinStus ajax传递未签到的学生信息
	 */

	@RequestMapping("teacher/ajaxUnSinStus.do")
	public String ajaxUnSinStus(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String pk_id = request.getParameter("pk_id");
		List<BMapEntity> neverSinStuMes = new ArrayList<BMapEntity>();
		List<List<BMapEntity>> never = new ArrayList<List<BMapEntity>>();
		List<String> allUnSinStuId = this.practiceTaskService
				.getUnSinStusIdByPk_id(pk_id);

		for (int i = 0; i < allUnSinStuId.size(); i++) {
			String stu_id = allUnSinStuId.get(i);
			neverSinStuMes = this.signService.selectUnSignAllStuByStuId(stu_id);
			never.add(neverSinStuMes);
		}
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < never.size(); i++) {

			for (BMapEntity b : never.get(i)) {
				sb.append("<tr>");
				sb.append("	<td>" + b.getTrue_name() + "</td>");
				sb.append("	<td>" + b.getSex() + "</td>");
				sb.append("	<td>" + b.getStu_code() + "</td>");
				sb.append("<td >"
						+ DictionaryService.findOrg(b.getClass_id())
								.getOrg_name() + "</td>");
				sb.append("<td >" + b.getPhone() + "</td>");
				sb.append("</tr>");

			}
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("teacher/ajaxStusSinState.do")
	public String ajaxStusSinState(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String pk_id = request.getParameter("pk_id");
		List<BMapEntity> StuSinMes = new ArrayList<BMapEntity>();
		List<List<BMapEntity>> StuSin = new ArrayList<List<BMapEntity>>();
		List<String> allStuID = this.practiceTaskService.selectStusId(pk_id);

		for (int i = 0; i < allStuID.size(); i++) {
			String stu_id = allStuID.get(i);
			StuSinMes = this.signService.selectSignAllStuStateByStuId(stu_id);
			StuSin.add(StuSinMes);
		}
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < StuSin.size(); i++) {

			for (BMapEntity b : StuSin.get(i)) {
				String stu_id = b.getId();
				sb.append(" <tr>");
				sb.append(" <td>" + (i + 1) + "</td>");
				sb.append("	<td>"
						+ "<a href='showStudentsMouthSinCount.do?stu_id="
						+ stu_id + "&mouth=" + month + "'>" + b.getTrue_name()
						+ "</a></td>");
				sb.append("	<td>" + b.getSignCount() + "</td>");
				sb.append("	<td>" + b.getStu_code() + "</td>");
				sb.append("<td >"
						+ DictionaryService.findOrg(b.getClass_id())
								.getOrg_name() + "</td>");
				sb.append("<td >" + b.getPhone() + "</td>");
				sb.append("<td ><input type='checkbox' style='width: 16px;height: 16px;' name='ck' id='"
						+ b.getId() + "' value='" + b.getId() + "'  ></td>");
				sb.append("</tr>");
			}
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据月份查看签到情况 修改 by 李达 20160115
	 * 
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("teacher/ajaxStusSinState2.do")
	public String ajaxStusSinState2(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String ym = request.getParameter("ym");
		Date d = new SimpleDateFormat("yyyy-MM").parse(ym);
		SimpleDateFormat y = new SimpleDateFormat("yyyy");
		SimpleDateFormat m = new SimpleDateFormat("MM");
		String grade = y.format(d);
		String mm = m.format(d);
		int mouth = Integer.parseInt(mm);
		String trueMouth = "";
		if (mouth >= 7) {
			if (mouth < 10) {
				trueMouth = grade + "-0" + mouth;
			} else {
				trueMouth = grade + "-" + mouth;
			}
		} else {
			int year = Integer.parseInt(grade);
			trueMouth = String.valueOf(year) + "-0" + mouth;
		}

		String pk_id = request.getParameter("pk_id");
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

		for (int i = 0; i < StuSin.size(); i++) {

			for (BMapEntity b : StuSin.get(i)) {
				sb.append("<tr>");
				sb.append("	<td>" + (i + 1) + "</td>");
				sb.append("	<td><a href='showStudentsMouthSinCount.do?stu_id="
						+ b.getId() + "&mouth=" + mouth + "'>"
						+ b.getTrue_name() + "</a></td>");
				sb.append("	<td>" + b.getSignCount() + "</td>");
				sb.append("	<td>" + b.getStu_code() + "</td>");
				sb.append("<td >"
						+ DictionaryService.findOrg(b.getClass_id())
								.getOrg_name() + "</td>");
				sb.append("<td >" + b.getPhone() + "</td>");
				sb.append("<td ><input type='checkbox' style='width: 16px;height: 16px;' name='ck' id='"
						+ b.getId() + "' value='" + b.getId() + "'  ></td>");
				sb.append("</tr>");
			}
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据学号，查看学生照片 功能： 李俊泽 2014-12-13
	 * 
	 * @param stu_code
	 * */
	@RequestMapping("teacher/showPhoto.do")
	public ModelAndView showPhoto(ModelMap modelMap, HttpServletRequest request) {
		String stu_code = request.getParameter("stu_code");
		List<Map<String, String>> result = this.filesService
				.selectPhoto(stu_code);
		modelMap.put("result", result);
		return new ModelAndView("teacher/showPhoto", modelMap); // 添加成功后重定向到列表页面
	}

	/**
	 * 根据分组，教师ID，日期查询，查看学生照片 李俊泽 2014-12-13
	 * 
	 * @param tea_id
	 *            ,begin_upload_time,end_upload_time,group_name
	 * */

	@RequestMapping("teacher/showPhotoList.do")
	public ModelAndView showPhotoList(ModelMap modelMap,
			HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String begin_upload_time = request.getParameter("begin_upload_time");
		String end_upload_time = request.getParameter("end_upload_time");
		String group_name = request.getParameter("group_name");
		List<Map<String, String>> result = this.filesService.selectPhotoList(
				tea_id, begin_upload_time, end_upload_time, group_name);
		map.put("result", result);
		return new ModelAndView("teacher/showPhotoList", map);
	}

	/**
	 * 根据教师ID查询分组，为查询学生照片， 功能： 李俊泽 2014-12-13
	 * 
	 * 2015-01-25 邢志武修改
	 * 
	 * @param tea_id
	 * */

	@RequestMapping("teacher/doshowPhoto.do")
	public ModelAndView doshowPhotoList(ModelMap modelMap, String id,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Groups> result = this.groupsService.getGroupsByTeaId(tea_id);
		map.put("result", result);
		return new ModelAndView("teacher/doshowPhoto", map);

	}

	/**
	 * 功能：ajax传递学生信息 邢志武2015-01-24 *
	 * 
	 * */
	@RequestMapping("*/ajaxStus.do")
	public String ajaxStus(HttpSession session, HttpServletRequest request,
			ModelMap modelMap, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String groupId = request.getParameter("groupId");
		List<String> stusId = this.groupMembersService
				.selectGroupMembersStuIdByGroupId(groupId);
		List<List<Student>> stus = new ArrayList<List<Student>>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < stusId.size(); i++) {
			String stu_id = stusId.get(i);
			List<Student> stuts = (List<Student>) this.studentService
					.selectByID1(stu_id);
			stus.add(stuts);
		}
		for (int i = 0; i < stus.size(); i++) {
			for (Student s : stus.get(i)) {
				sb.append("<tr>");
				sb.append("<td>"
						+ DictionaryService.findOrg(s.getClass_id())
								.getOrg_name() + "</td>");
				sb.append("<td>" + s.getStu_code() + "</td>");
				sb.append("<td><a href='showStudentPhoto.do?stu_id="
						+ s.getId() + "'>" + s.getTrue_name() + "</a></td>");
				sb.append("	<td>" + s.getPhone() + "</td>");
				sb.append("</tr>");
			}

		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}

		return null;

	}

	/*
	 * 2015-01-26 邢志武 查看学生实习照片
	 */
	@RequestMapping("teacher/showStudentPhoto.do")
	public ModelAndView showStudentPhoto(ModelMap modelMap, String id,
			HttpSession session, HttpServletRequest request) {
		id = request.getParameter("stu_id");
		Map<String, Object> map = new HashMap<String, Object>();
		String stu_name = DictionaryService.findStudent(id).getTrue_name();
		List<Files> stuPhotos = this.filesService.selectStuPhotoByStu_id(id);
		map.put("stu_name", stu_name);
		map.put("stuPhotos", stuPhotos);
		return new ModelAndView("teacher/showStudentPhoto", map);

	}

	/**
	 * 功能：查询常见问题 修改：筛选常见问题列表默认为全院的最新20条记录问题 by王磊 20150121
	 * 
	 * */

	@RequestMapping("teacher/knowledgeList.do")
	public ModelAndView knowledgeList(HttpSession session, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String knowledge_part_Id = (String) session
				.getAttribute("knowledge_part_Id");
		String knowledge_tea_id = (String) session
				.getAttribute("knowledge_tea_id");
		String knowledgecontent = (String) session
				.getAttribute("knowledgecontent");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		if (type != null) {
			session.removeAttribute("knowledge_part_Id");
			session.removeAttribute("knowledge_tea_id");
			session.setAttribute("knowledgecontent", "问题或答案或年份过滤");
			knowledge_part_Id = null;
			knowledge_tea_id = null;
			knowledgecontent = "问题或答案或年份过滤";
		} else if (knowledge_part_Id == null && knowledge_tea_id == null
				&& knowledgecontent == null) {
			knowledgecontent = "问题或答案或年份过滤";
		}
		String tea_id = tea.getId();
		String college_id = (String) session.getAttribute("tea_college_id");
		List<Knowledge> result = this.knowledgeService.ByXyId(college_id);
		List<Org> o = this.orgService.selectByXueYuanId(college_id);

		map.put("tea_id", tea_id);
		map.put("result", result);
		map.put("orgs", o);
		map.put("knowledge_part_Id", knowledge_part_Id);
		map.put("knowledge_tea_id", knowledge_tea_id);
		map.put("knowledgecontent", knowledgecontent);
		return new ModelAndView("teacher/knowledgeList", map);
	}

	/**
	 * 功能：通过系部id得到系部老师 by王磊2015年3月23日
	 * 
	 * */
	@RequestMapping("teacher/getTeacherByDeptId.do")
	public String getTeacherByDeptId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");

		// 获取选中部门的所有老师
		String departId = request.getParameter("partId");
		// 从字典表过滤出部门老师
		List<Teacher> deptTeaList = new ArrayList<Teacher>();// this.teacherService.getTeachersByDeptId(partId);
		deptTeaList = Common.getDeptTeaListByDictionary(departId);
		Teacher teacher = new Teacher();
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='tea_id' name='tea_id' style='width:100px'>");
		// 判断选中的部门id是否等于 session中发布常见问题的教师的部门
		boolean b = false;
		String knowledge_tea_id = (String) session
				.getAttribute("knowledge_tea_id");
		if (DictionaryService.findTeacher(knowledge_tea_id) != null) {
			b = departId.equals(DictionaryService.findTeacher(knowledge_tea_id)
					.getDept_id());
		}
		// 如果选中的部门id不等于 session中发布常见问题的教师的部门
		if (b == false || deptTeaList.size() == 0) {
			sb.append("<option value='0'>请选择老师</option>");
		} else {
			teacher = DictionaryService.findTeacher(knowledge_tea_id);
			for (int i = 0; i < deptTeaList.size(); i++) {
				// 如果当前老师是第i个，则将该老师放到0位置，并去掉多余的该老师，即i+1的值
				if (deptTeaList.get(i).getId().equals(knowledge_tea_id)) {
					deptTeaList.add(0, teacher);
					deptTeaList.remove(i + 1);
				}
			}
		}
		for (Teacher teacher_i : deptTeaList) {
			sb.append("<option " + "value=" + teacher_i.getId() + ">"
					+ teacher_i.getTrue_name() + "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.setAttribute("part_Id", null);
		session.setAttribute("tea_id", null);
		return null;
	}

	/*
	 * 通过条件查询出符合条件的问题by王磊 20150121
	 */
	@RequestMapping("teacher/getKnowledgeByConditions.do")
	public String getQuestion(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String teaId = tea.getId();
		String tj = request.getParameter("tiaojian");
		String[] strs = tj.split(",");
		String part_Id = strs[0];
		String tea_id = strs[1];
		String knowledgecontent = strs[2];
		String cont = "";
		List<Knowledge> result = new ArrayList<Knowledge>();
		try {
			cont = URLDecoder.decode(knowledgecontent, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		session.setAttribute("knowledge_part_Id", part_Id);
		session.setAttribute("knowledge_tea_id", tea_id);
		session.setAttribute("knowledgecontent", cont);
		String xy_id = (String) session.getAttribute("tea_college_id");
		if (part_Id.equals("0") && cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.ByXyId(xy_id);
		} else if (!part_Id.equals("0") && tea_id.equals("0")
				&& cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.byConditions(part_Id, null, null);
		} else if (!part_Id.equals("0") && tea_id.equals("0")
				&& !cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.byConditions(part_Id, null, cont);
		} else if (!tea_id.equals("0") && cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.byConditions(null, tea_id, null);
		} else if (!tea_id.equals("0") && !cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.byConditions(null, tea_id, cont);
		} else if (tea_id.equals("0") && part_Id.equals("0")
				&& !cont.equals("问题或答案或年份过滤")) {
			result = this.knowledgeService.byKey(xy_id, cont);
		}
		StringBuffer sb = new StringBuffer();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		for (int i = 0; i < result.size(); i++) {
			String createTime = format.format(result.get(i).getCreate_time());
			sb.append("<tr>");
			sb.append("<td align='center'>" + (i + 1) + "</td>");
			sb.append("<td align='center'>" + result.get(i).getQuestion()
					+ "</td>");
			sb.append("<td align='center'>" + result.get(i).getAnswer()
					+ "</td>");
			sb.append("<td align='center'>"
					+ DictionaryService.findTeacher(
							result.get(i).getMessenger_id()).getTrue_name()
					+ "</td>");
			sb.append("<td align='center'>"
					+ DictionaryService.findOrg(result.get(i).getScope())
							.getOrg_name() + "</td>");
			sb.append("<td align='center'>" + createTime + "</td>");
			sb.append("<td align='center'><input type='button' value='修改' onclick=editKnowledge('"
					+ result.get(i).getId() + "');>" + "</td>");
			sb.append("<td align='center'><input type='button' value='删除' onclick=doDel('"
					+ result.get(i).getId()
					+ "','"
					+ teaId
					+ "','"
					+ result.get(i).getMessenger_id() + "');>" + "</td>");
			sb.append("</tr>");
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
	 * 功能：添加常见问题注解请求地址(映射)--添加页面孙家胜 20141212 修改by王磊 通过条件筛选问题并且过滤
	 * 
	 * */
	@RequestMapping("teacher/addKnowledge.do")
	public ModelAndView addKnowledge(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		List<Org> o = new ArrayList<Org>();
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
			o.add((Org) this.orgService.selectByID(xy_id));
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
			o.add((Org) this.orgService.selectByID(xy_id));
			o.add((Org) this.orgService.selectByID(dept_id));
		}
		map.put("orgs", o);
		return new ModelAndView("teacher/knowledgeAdd", map);
	}

	/**
	 * 功能：添加常见问题 保存 注解请求地址(映射) 添加页面孙家胜 20141212 修改：王磊，范围设定
	 * */
	@RequestMapping("teacher/doAddKnowledge.do")
	public String saveKnowledge(HttpServletRequest request, HttpSession session)
			throws IOException {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时

		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			time = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String question = request.getParameter("question");
		String answer = request.getParameter("answer");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String scope = request.getParameter("scope");
		// Integer category= Integer.parseInt(request.getParameter("category"));
		Knowledge k = new Knowledge();
		k.setCreate_time(time);
		k.setId(Common.returnUUID());
		k.setQuestion(question);
		k.setAnswer(answer);
		k.setScope(scope);
		k.setMessenger_id(tea.getId());
		k.setCategory(1);
		knowledgeService.insert(k);

		return "redirect:knowledgeList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：修改常见问题 注解请求地址(映射) 孙家胜 20141212 修改:王磊,得到本院和老师所在的系
	 * */
	@RequestMapping("teacher/editKnowledge.do")
	public ModelAndView editKnowledge(String id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Knowledge k = (Knowledge) knowledgeService.selectByID(id);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		Org org = new Org();
		org = (Org) this.orgService.selectByID(k.getScope());
		String dept_id = tea.getDept_id();
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		List<Org> o = new ArrayList<Org>();
		if (dept_level.equals("2")) {
			xy_id = dept_id;
			o.add((Org) this.orgService.selectByID(xy_id));
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();
			o.add((Org) this.orgService.selectByID(xy_id));
			o.add((Org) this.orgService.selectByID(dept_id));
		}
		for (int i = 0; i < o.size(); i++) {
			if (o.get(i).getId().equals(org.getId())) {
				o.remove(i);
			}
		}
		o.add(0, org);
		map.put("tea", tea);
		map.put("orgs", o);
		map.put("knowledge", k);
		return new ModelAndView("teacher/knowledgeEdit", map);
	}

	/**
	 * 功能：编辑常见问题保存 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * */
	@RequestMapping("teacher/doEditKnowledge.do")
	public String editKnowledge(
			@ModelAttribute("knowledge") Knowledge knowledge,
			HttpServletRequest request) throws IOException {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时

		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			time = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		knowledge.setCreate_time(time);
		knowledge.setScope(request.getParameter("scope"));
		knowledgeService.update(knowledge);
		return "redirect:knowledgeList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：删除常见问题 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * */
	@RequestMapping("teacher/deleteKnowledge.do")
	public String deleteKnowledge(String id) {
		knowledgeService.delete(id);
		return "redirect:knowledgeList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：老师指导记录管理 wtt 20141217 吴敬国 2015-9-10
	 */
	@RequestMapping("teacher/directrecordList.do")
	public ModelAndView directrecordList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DirectRecord> directRecordList = this.directRecordService
				.getTeaDirecRecsByTeaIdAndDr_type(Common.getOneTeaId(session),
						"2");
		map.put("directRecordList", directRecordList);
		return new ModelAndView("teacher/directrecordList", map);
	}

	/**
	 * 功能：老师添加记录 注解请求地址(映射)--添加页面 wtt 20141217 邢志武修改 2015-03-17
	 * 
	 * */
	@RequestMapping("teacher/adddirectrecord.do")
	public String adddirectrecord(HttpSession session, ModelMap modelMap) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		/*
		 * List<PracticeTask> practiceTaskList =
		 * this.practiceTaskService.getPracticeTasksByGradeAndTea_id
		 * (Common.getOneTeaId(session),Common.getDefaultYear());
		 */
		/*
		 * List<Groups> Groups =
		 * this.groupsService.getGroupsByTeaId(Common.getOneTeaId(session));
		 */
		modelMap.put("tea", tea);
		/* modelMap.put("result", practiceTaskList); */
		/* modelMap.put("Groups", Groups); */
		return "teacher/directrecordAdd";
	}

	/**
	 * 功能：管理员添加用户 保存 wtt 20141103 邢志武修改 2015-03-17 吴敬国 2015-9-10
	 * 
	 * */
	@RequestMapping("teacher/doAdddirectrecord.do")
	public String saveDirectRecord(HttpSession session,
			HttpServletRequest request) throws IOException {
		String practice_id = request.getParameter("practice_id");
		String title = request.getParameter("title");
		String direct_place = request.getParameter("direct_place");
		String direct_time = request.getParameter("direct_time");
		String temp2 = request.getParameter("temp2");
		String stu_ids = request.getParameter("stu_ids");
		String direct_desc = request.getParameter("content");
		/* String worktype = request.getParameter("worktype"); */
		Groups g = new Groups();
		g.setPractice_id(practice_id);
		List<Groups> group = groupsService.selectList(g);
		Groups gp = (Groups) group.get(0);
		String org_id = gp.getId();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）,创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					String file_type = "Directrecord";// file.getContentType();//使用getContentType()方法获得文件类型，以此决定允许上传的文件类型
					String file_name = file.getOriginalFilename();
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					// String filePosition = "WEB-INF/uploadedfiles/" +
					// file_type+ "/"+ file_name;
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + file_name;// "Directrecord-20151229111000_uploadedfiles.rar";//
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					// String file_path = project_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					file.transferTo(new File(file_path));// Directrecord-20151229111000_uploadedfiles.rar
					String file_id = Common.returnUUID();
					Files fil = new Files();
					fil.setFile_size(filesize);
					Timestamp d = new Timestamp(System.currentTimeMillis());
					fil.setUpload_time(d);
					fil.setId(file_id);
					fil.setFile_type(file_type);
					fil.setFile_name(file_name);
					fil.setPosition(filePosition);
					fil.setStu_id(Common.getOneTeaId(session));
					filesService.insert(fil);
					DirectRecord o = new DirectRecord();
					o.setId(Common.returnUUID());
					o.setPractice_id(practice_id);
					o.setTitle(title);
					o.setFile_id(file_id);
					o.setTemp1("2");// 指导记录类型
					o.setTemp2(temp2);// 指导结束时间
					o.setCreate_time(DateService.getNowTimeTimestamp());// 创建时间
					o.setDirect_place(direct_place);
					if (direct_time == null) {
						direct_time = "2010-01-01";
					}
					DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					try {
						ts = new Timestamp(format1.parse(direct_time).getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}// 时间类型可以封装
					o.setDirect_time(ts);
					o.setOrg_id(org_id);
					o.setStu_id(stu_ids);
					o.setDirect_desc(direct_desc);
					o.setId(file_id);
					directRecordService.insert(o);
				}
			}
		}
		return "redirect:directrecordList.do";
	}

	/**
	 * 功能：老师编辑 记录 注解请求地址(映射) wtt 20141217
	 * 
	 * */
	@RequestMapping("teacher/editdirectrecord.do")
	public String editdirectrecord(ModelMap modelMap, String id) {
		DirectRecord d = (DirectRecord) directRecordService.selectByID(id);
		modelMap.put("directrecord", d);
		return "teacher/directrecordEdit";
	}

	/**
	 * 功能：老师编辑 记录 by wjg 2015-10-10
	 * */
	@RequestMapping("teacher/doEditdirectrecord.do")
	public String editDirectRecord(
			@ModelAttribute("directrecord") DirectRecord directrecord)
			throws IOException {
		directRecordService.update(directrecord);
		return "redirect:directrecordList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：老师删除记录 by wjg 2015-10-10
	 * 
	 * */
	@RequestMapping("teacher/deletedirectrecord.do")
	public String deletedirectrecord(String id, HttpServletRequest request) {
		DirectRecord d = (DirectRecord) directRecordService.selectById(id);
		String file_id = d.getFile_id();
		if (file_id != null) {
			filesService.deleteFile(file_id, request);
		}
		directRecordService.delete(id);
		return "redirect:directrecordList.do";
	}

	/*
	 * 查看实训成绩 2015-01-26 邢志武修改
	 */

	@RequestMapping("teacher/coreList.do")
	public ModelAndView showPractice(HttpServletRequest request,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获取当前用户
		String tea_id = tea.getId();
		List<PracticeTask> result = this.practiceTaskService
				.selectOutSchoolPracticeTasks(tea_id);
		map.put("result", result);
		return new ModelAndView("teacher/showPractice", map);
	}

	/**
	 * 功能：ajax传递学生信息 邢志武2015-01-26 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxSerchStus.do")
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
		// PracticeTask p=new PracticeTask(); wjg 2015年6月4日 出现null之后修改
		p.setWeight_summary(dblMonth);
		p.setWeight_evaluate(dblEvaluate);
		p.setWeight_thesis(dblThesis);
		// p.setId(practice_id);
		this.practiceTaskService.update(p);
		List<Score> stusScore = new ArrayList<Score>();
		// 通过任务id查询成员
		List<String> stusId = this.practiceTaskService
				.selectStusIdByPractice(practice_id);
		Score s = new Score();
		for (int i = 0; i < stusId.size(); i++) {
			s = this.ScoreService.getStuScore(practice_id, stusId.get(i));
			stusScore.add(s);
		}
		// 降序
		// Collections.reverse(stusScore);
		StringBuffer allStusScore = new StringBuffer();
		for (Score oneStuScore : stusScore) {
			String class_id = DictionaryService.findStudent(
					oneStuScore.getStu_id()).getClass_id();
			// 分数保留两位小数
			DecimalFormat df = new DecimalFormat("######0");
			allStusScore.append("<tr>");
			allStusScore.append("<td>"
					+ DictionaryService.findOrg(class_id).getOrg_name()
					+ "</td>");
			allStusScore.append("<td>"
					+ DictionaryService.findStudent(oneStuScore.getStu_id())
							.getStu_code() + "</td>");
			allStusScore.append("<td>"
					+ DictionaryService.findStudent(oneStuScore.getStu_id())
							.getTrue_name() + "</td>");
			allStusScore.append("<td>"
					+ DictionaryService.findStudent(oneStuScore.getStu_id())
							.getPhone() + "</td>");
			allStusScore.append("<td>" + df.format(oneStuScore.getMouthScore())
					+ "</td>");
			allStusScore.append("<td>" + df.format(oneStuScore.getTheScore())
					+ "</td>");
			allStusScore.append("<td>" + df.format(oneStuScore.getEvaScore())
					+ "</td>");
			allStusScore.append("<td>" + df.format(oneStuScore.getScore())
					+ "</td>");
			allStusScore.append("</tr>");

		}
		try {
			response.getWriter().println(allStusScore.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax传递 系by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxGetWeight.do")
	@ResponseBody
	public PracticeTask ajaxGetWeight(HttpServletRequest request)
			throws IOException {
		String prac_id = request.getParameter("practice_id");
		PracticeTask p = DictionaryService.findPracticeTask(prac_id);
		return p;
	}

	/**
	 * 功能已修改 ， 此方法准备删除 邢志武 2015-01-28 功能：查询各项成绩
	 * 
	 * @param request
	 * @param session
	 * @param response
	 * @param modelMap
	 * @return ModelAndView
	 * @author 吕付豹
	 * @since 2014年12月15日
	 */

	@RequestMapping("teacher/showscore.do")
	public ModelAndView showScore(HttpServletRequest request,
			HttpSession session, HttpServletResponse response, ModelMap modelMap) {
		/*
		 * List<List<MonthSummary>> monthIDAll = new
		 * ArrayList<List<MonthSummary>>(); List<List<GraduationThesis>>
		 * thesisIDAll = new ArrayList<List<GraduationThesis>>();
		 * List<List<Evaluate>> EvaluateIDAll = new ArrayList<List<Evaluate>>();
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		response.setCharacterEncoding("UTF-8");
		String practice_id = request.getParameter("practice_id");// 获取任务ID
		// 根据任务id查学生成绩和学生信息
		List<Map<String, String>> studentScores = this.practiceTaskService
				.getPracticeScoreList(practice_id);
		String myYear = request.getParameter("myYear");
		String term = request.getParameter("term");

		map.put("myYear", myYear);
		map.put("term", term);
		map.put("practice_id", practice_id);
		map.put("result", studentScores);
		return new ModelAndView("teacher/showScore", map);
	}

	/**
	 * 功能：生成综合成绩
	 * 
	 * @param request
	 * @param session
	 * @param response
	 * @param modelMap
	 * @return ModelAndView
	 * @author 吕付豹
	 * @since 2014年12月15日
	 */
	@RequestMapping("teacher/totalscore.do")
	public ModelAndView totalScore(HttpServletRequest request,
			HttpSession session, HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		response.setCharacterEncoding("UTF-8");
		String practice_id = request.getParameter("practice_id");
		int myYear = Integer.parseInt(request.getParameter("myYear"));
		int term = Integer.parseInt(request.getParameter("term"));

		// 得到综合成绩列表
		String weight_month = request.getParameter("month");
		String weight_thesis = request.getParameter("thesis");
		String weight_evaluate = request.getParameter("Evaluate");
		List<Map<String, Object>> totalscores = this.ScoreService.ScoreList(
				practice_id, weight_month, weight_thesis, weight_evaluate);
		String weight = "总结：" + weight_month + ",论文：" + weight_thesis + ",奖惩："
				+ weight_evaluate;
		// 遍历每一个学生的成绩，存入数据库
		// dao.xml中取map数据：#{stu_id} #{score}
		// 获取系统时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		long startTime = System.currentTimeMillis();
		for (Map<String, Object> m : totalscores) {
			System.out.println(m);
		}
		for (Map<String, Object> m : totalscores) {
			m.put("id", Common.returnUUID());
			m.put("Practice_id", practice_id);
			m.put("stu_id", m.get("USER_ID"));
			m.put("type", m.get("TASK_NAME"));
			m.put("time", time);
			m.put("year", myYear);
			m.put("term", term);
			m.put("score", m.get("totalscore"));
			m.put("Weight", weight);
			m.put("Note",
					"论文成绩：" + m.get("thesisscore") + ",月总结成绩："
							+ m.get("monthscore") + ",奖惩成绩："
							+ m.get("evaluatescore"));
			ScoreService.insertMap(m);
		}
		map.put("result", totalscores);
		return new ModelAndView("teacher/studentScore", map);
	}

	/**
	 * 功能： 返回选择报表内容的jsp页面 实训安排报表 by 张超 2015年1月29日 吴敬国 2015-6-8
	 * 
	 * @return
	 */
	@RequestMapping("teacher/getTrainDetailReport.do")
	public String getTrainDetailReport() {
		return "teacher/trainDetailReport";
	}

	/**
	 * 功能：通过实践教学任务id得到本任务的课表
	 * 
	 * */
	@RequestMapping("teacher/getTrainDetail.do")
	public String getTrainDetail(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String yearAndterm = request.getParameter("yearAndterm");
		int a = yearAndterm.indexOf("=");
		String year = yearAndterm.substring(0, a);
		String term = yearAndterm.substring(a + 1, yearAndterm.length());
		Map<String, String> time = Common.getBeginTimeAndEndTime(year, term);
		String begin_time = time.get("begin_time");
		String end_time = time.get("end_time");
		List<TrainDetail> tds = null;
		Map<String, String> mapYearAndTeaIdAndTerm = new HashMap<String, String>();
		mapYearAndTeaIdAndTerm.put("tea_id", Common.getOneTeaId(session));
		mapYearAndTeaIdAndTerm.put("begin_time", begin_time);
		mapYearAndTeaIdAndTerm.put("end_time", end_time);
		mapYearAndTeaIdAndTerm.put("term", term);
		tds = this.trainDetailService
				.getTrainsByTermAndYearAndTeaId(mapYearAndTeaIdAndTerm);

		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='1200'>");
		sb.append("<tr> <th width='250' align='center'>任务名称</th><th width='200' align='center'>小组名称</th><th width='100' align='center'>课程名称</th><th width='120' align='center'>实训时间</th><th width='150' align='center'>实训地点</th><th width='50' align='center'>周次</th><th width='100' align='center'>节次</th></tr>");
		for (TrainDetail trainDetail : tds) {
			String createTime = format.format(trainDetail.getTrain_time());
			sb.append("<tr>");
			sb.append("<td align='center'>"
					+ DictionaryService.findPracticeTask(
							trainDetail.getTask_id()).getTask_name() + "</td>");
			sb.append("<td align='center'>"
					+ DictionaryService.findGroups(trainDetail.getGroup_id())
							.getGroup_name() + "</a></td>");
			sb.append("<td align='center'>"
					+ DictionaryService.findCourses(trainDetail.getCourse_id())
							.getCourse_name() + "</td>");
			/*
			 * sb.append("<td align='center'>" +
			 * DictionaryService.findTeacher(trainDetail
			 * .getTea_id()).getTrue_name() + "</td>");
			 */
			sb.append("<td align='center'>" + createTime + "</td>");
			sb.append("<td align='center'>" + trainDetail.getTrain_place()
					+ "</td>");
			sb.append("<td align='center'>" + trainDetail.getWeek_num()
					+ "</td>");
			sb.append("<td align='center'>" + trainDetail.getClass_num()
					+ "</td>");
			/*
			 * sb.append(
			 * "<td align='center'><input type='button' value='修改' onclick=doEdit('"
			 * + trainDetail.getId() + "')></td>");
			 */
			/*
			 * sb.append(
			 * "<td align='center'><input type='button' value='删除' onclick=doDel('"
			 * + trainDetail.getId() + "')></td>");
			 */
			sb.append("</tr>");
		}
		sb.append("</table>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能： 获取报表数据并显示在报表中 by 张超 2015年1月29日 wjg 2015-6-8
	 * 
	 * @return
	 */
	@RequestMapping(value = "teacher/doGetTrainDetailReport.do", method = RequestMethod.POST)
	public ModelAndView doA_TrainDetail(ModelAndView modelAndView,
			HttpServletRequest request, HttpSession session) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		String tea_name = tea.getTrue_name();
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		String term = request.getParameter("term");

		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");
		// parameterMap 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService
				.getTeacherReportData(tea_code, term, begin_time, end_time);
		String report_title = tea_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		model.put("data", gradu);
		model.put("report_title", report_title);
		model.put("yearAndTerm", yearAndTerm);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("trainDetail_teacher", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能： 修改首页 by 郑春光 2015-2-9 杨梦肖 2016/4/8 设置待办事项模块
	 */
	@RequestMapping("teacher/index.do")
	public ModelAndView index(HttpSession session, String current_role_selected) {
		String checkPraTask_grade = Common.getDefaultYear();
		// 根据教师id和入学年份获得实习任务
		List<PracticeRecord> modelList = new ArrayList<PracticeRecord>();
		List<PracticeTask> checkPraTaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(Common.getOneTeaId(session),
						checkPraTask_grade);
		String li = "1";
		String checkPraticeTask_taskId = null;
		if (checkPraTaskList == null || checkPraTaskList.size() == 0) {
			checkPraticeTask_taskId = null;
		} else {
			// 取得第一个任务的id
			checkPraticeTask_taskId = checkPraTaskList.get(0).getId();
			// 取得第一个的任务名
			List<PracticeRecord> practiceRecordList = this.practiceRecordService
					.selectPracticeRecordByPracticeTaskId(checkPraticeTask_taskId);
			for (PracticeRecord pr : practiceRecordList) {

				if (pr.getCheck_state().equals("0")
						&& pr.getDimission_time() == null) {
					modelList.add(pr);
					if (modelList != null) {
						li = "li";
						break;
					}
				}
			}
		}
		// nonChecked = (Integer)
		// getPracticeRecordListAndCount.get("nonChecked");

		if (current_role_selected != null) {
			session.setAttribute("current_role_selected", current_role_selected);
		}
		// 设置默认年份：当前-3
		String grade = Common.getDefaultYear();
		// 获取登录用户信息 吴敬国 2015-9-4
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(tea_college_id)
				.getOrg_level();
		if (org_level.equals("3")) {
			tea_college_id = DictionaryService.findOrg(tea_college_id)
					.getParent_id();
		}
		// 存入session 供其他模块使用 保存教师模块的登陆教师所在学院的id号
		session.setAttribute("tea_college_id", tea_college_id);

		// 获取单个学院各项状态人数
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("grade", grade);
		mapParam.put("teaId", tea.getId());
		// mapParam.put("nonChecked",nonChecked);
		List<Map<String, Object>> listStuStateCount = studentService
				.getStuStateCountByGradeAndTeaId(mapParam);
		// 转为报表数据
		ChartModel cm = new ChartModel();
		// 2015年5月18日 21:07:03 邢志武 解决不显示问题
		if (listStuStateCount.size() == 0) {
			Map<String, Object> stuState = new HashMap<String, Object>();
			String stateCode = "100";
			String stuCount = "1";
			stuState.put("stateCode", stateCode);
			stuState.put("stuCount", stuCount);
			listStuStateCount.add(stuState);
			cm = Ichartjs_Color.getChartModel(listStuStateCount);
		} else {
			cm = Ichartjs_Color.getChartModel(listStuStateCount);
		}
		String jsonData = cm.getJsonDataNew();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cm", jsonData);
		map.put("grade", grade);
		map.put("li", li);
		map.put("tea_name", tea.getTrue_name());
		CommonSession.setUserRole(session, CommonSession.roleTeacher);
		return new ModelAndView("teacher/index", map);
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
	@RequestMapping("teacher/ajaxIndex.do")
	public String ajaxIndex2(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(tea_college_id)
				.getOrg_level();
		if (org_level.equals("3")) {
			tea_college_id = DictionaryService.findOrg(tea_college_id)
					.getParent_id();
		}
		// 存入session 供其他模块使用 保存教师模块的登陆教师所在学院的id号
		session.setAttribute("tea_college_id", tea_college_id);

		// 获取单个学院各项状态人数
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("grade", grade);
		mapParam.put("teaId", tea.getId());
		List<Map<String, Object>> listStuStateCount = studentService
				.getStuStateCountByGradeAndTeaId(mapParam);
		// 转为报表数据
		ChartModel cm = new ChartModel();
		// 2015年5月18日 21:07:03 邢志武 解决不显示问题
		if (listStuStateCount.size() == 0) {
			Map<String, Object> stuState = new HashMap<String, Object>();
			String stateCode = "100";
			String stuCount = "1";
			stuState.put("stateCode", stateCode);
			stuState.put("stuCount", stuCount);
			listStuStateCount.add(stuState);
			cm = Ichartjs_Color.getChartModel(listStuStateCount);
		} else {
			cm = Ichartjs_Color.getChartModel(listStuStateCount);
		}
		String jsonData = cm.getJsonDataNew();
		try {
			response.getWriter().println(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：实习状态详细情况
	 * 
	 * @author WuGee 2015年9月1日 11:21:39
	 */
	@RequestMapping("teacher/outSchoolState.do")
	public ModelAndView outSchoolState(HttpSession session,
			HttpServletRequest request) {
		// 设置默认年份：当前-3
		String grade = Common.getDefaultYear();
		// 状态编码
		String curStateCode = request.getParameter("stateCode");
		// 获取登录用户信息
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 获取单个学院各项状态人数
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("grade", grade);
		mapParam.put("teaId", tea.getId());
		mapParam.put("curStateCode", curStateCode);
		List<Map<String, Object>> listStuStateStudent = studentService
				.getStuStateCountByGradeAndTeaId(mapParam);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grade", grade);
		map.put("tea_name", tea.getTrue_name());

		return new ModelAndView("teacher/index", map);
	}

	/**
	 * 功能： 更改签到信息 by 孙磊 2015-3-21
	 * 
	 * @throws Exception
	 */
	// 根据学生Id查询学生合理区域表的数据
	@RequestMapping("teacher/showLatitude.do")
	public ModelAndView getLaById(HttpSession session, String id)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		RightRegion result = this.rightRegionService.getLaById(id);
		double longitude_r = result.getLongitude_r();
		double latitude_r = result.getLatitude_r();
		String lon = Double.toString(longitude_r);
		String lat = Double.toString(latitude_r);
		String city = jsonUtil.getCityNameByLoc(lat, lon);
		Sign sss = this.signService.changeLaById(id);
		String city2;
		try {
			double longitude = sss.getLongitude();
			double latitude = sss.getLatitude();
			String lon1 = Double.toString(longitude);
			String lat1 = Double.toString(latitude);
			city2 = jsonUtil.getCityNameByLoc(lat1, lon1);
		} catch (Exception e) {
			// TODO: handle exception
			city2 = "今日未签到";
		}
		map.put("result", result);
		map.put("city", city);
		map.put("city2", city2);
		return new ModelAndView("teacher/showLatitude", map);
	}

	// 修改最新的签到数据
	@RequestMapping("teacher/qshowLatitude.do")
	@ResponseBody
	public Sign changeLaById(HttpServletRequest request) throws Exception {
		String sid = request.getParameter("id");
		Sign sss = this.signService.changeLaById(sid);
		double longitude_r = sss.getLongitude();
		double latitude_r = sss.getLatitude();
		String lon = Double.toString(longitude_r);
		String lat = Double.toString(latitude_r);
		String city = jsonUtil.getCityNameByLoc(lat, lon);
		sss.setTemp3(city);
		return sss;
	}

	// 保存修改的签到数据
	@RequestMapping("teacher/saveLatitude.do")
	public String saveLatitude(HttpSession session, HttpServletRequest request)
			throws IOException {
		String latitude_r = request.getParameter("LATITUDE_R");
		String longitude_r = request.getParameter("LONGITUDE_R");
		String begin_time = request.getParameter("begin_time");
		String end_time = request.getParameter("end_time");
		String sid = request.getParameter("id");
		String id = request.getParameter("aid");
		RightRegion rightRegion = new RightRegion();
		double la = Double.parseDouble(latitude_r);
		double lo = Double.parseDouble(longitude_r);
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(begin_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		try {
			ts2 = new Timestamp(format2.parse(end_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		rightRegion.setLatitude_r(la);
		rightRegion.setLongitude_r(lo);
		rightRegion.setStu_id(sid);
		rightRegion.setId(id);
		rightRegion.setBegin_time(ts1);
		rightRegion.setEnd_time(ts2);
		rightRegionService.update(rightRegion);
		return "redirect:bMap.do";
	}

	/**
	 * 获取该学院的招聘信息 招聘信息管理 by王磊 2015年3月21日 吴敬国 2015-5-11
	 */
	@RequestMapping("teacher/recruitInfoList.do")
	public ModelAndView recruitInfoList(HttpSession session) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String college_id = Common.getCollegeId(dept_id);
		Map<String, Object> map = new HashMap<String, Object>();
		RecruitInfo ri = new RecruitInfo();
		ri.setCollege_id(college_id);
		ri.setState("1");
		List<RecruitInfo> recruitInfoList = this.recruitInfoService
				.selectList(ri);
		map.put("result", recruitInfoList);
		return new ModelAndView("teacher/recruitInfoList", map); // 添加成功后重定向到列表页面
	}

	/**
	 * 得到招聘信息的发送详情 即查看通知的内容 吴敬国 2015-5-11
	 */
	@RequestMapping("teacher/sendDetails.do")
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
		return new ModelAndView("teacher/sendInfoNotiesList", map);
	}

	/**
	 * 得到该公司的详情，跳到推送界面 by王磊 2015年3月21日 吴敬国 2015-4-14 修改
	 */
	@RequestMapping("teacher/sendToStudents.do")
	public ModelAndView sendToStudents(String id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PracticeTask> Grade = practiceTaskService.getPracticeTasksGrade();// 有问题
		session.setAttribute("recruitInfo_id", id);
		RecruitInfo ri = (RecruitInfo) recruitInfoService.selectByID(id);
		map.put("ri", ri);
		map.put("Grade", Grade);
		return new ModelAndView("teacher/sendInforToStudents", map);
	}

	/**
	 * 功能：根据老师id和实习id查找出对应的学生并且此学生没有就业 实习状态编码为100 智能信息推送 by王磊 2015年3月21日 吴敬国
	 * 2015-4-14 修改
	 * */
	@RequestMapping("teacher/getStudents.do")
	public String getStudents(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String practice_id = request.getParameter("practice_id");
		List<GroupMembers> SuList = this.groupMembersService
				.selectStuIdListByPracticeId(practice_id);// 有问题
		List<String> list = new ArrayList();
		String state;
		for (int i = 0; i < SuList.size(); i++) {
			state = DictionaryService.findStudent(SuList.get(i).getUser_id())
					.getPractice_state();
			if (state.equals("100")) {
				list.add(SuList.get(i).getUser_id());
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<tr id = 'select'>");
		int i = 0;
		for (String l : list) {
			if (i % 5 == 0) {
				sb.append("</tr>");
				sb.append("<br>");
				sb.append("<tr>");
			}
			sb.append("<td><label for='"
					+ l
					+ "'> <input type='checkbox' id='students' name='students' value ='"
					+ l + "'>"
					+ DictionaryService.findStudent(l).getTrue_name()
					+ "</label></td>");
			i++;
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：老师给同学发招聘信息 生成一条有关招聘信息的通知，通知类型为5,操作通知表 by王磊 2015年3月21日 吴敬国 2015-4-14 修改
	 * 
	 * */
	@RequestMapping("teacher/doAddRecruInfo.do")
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
		String notice_title = request.getParameter("notice_title");
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
		// session.setAttribute("recruitInfo_id", recruitInfo_id);
		notice.setTemp1(recruitInfo_id);
		noticeService.insert(notice);
		return "redirect:sendDetails.do";
	}

	/**
	 * 遍历列表 邢志武 2015年4月11日
	 * */
	@RequestMapping("teacher/groupsList.do")
	public ModelAndView groupsList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String org_id1 = (String) session.getAttribute("org_id");
		String grade1 = (String) session.getAttribute("grade");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// List<Groups> result = groupsService.getGroupsByDeptId(dept_id);
		List<Org> org = this.orgService.selectByXueYuanId(dept_id);
		map.put("org", org);
		map.put("org_id1", org_id1);
		map.put("grade1", grade1);
		// map.put("result", result);
		return new ModelAndView("teacher/groupsList", map);
	}

	/**
	 * 功能：ajax传递小组信息 邢志武 2015年4月11日
	 * 
	 * */
	@RequestMapping("teacher/ajaxSerchGroups.do")
	public String ajaxTeacherSerchStus(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String org_id = tea.getDept_id();
		String grade = request.getParameter("grade");
		// 将 组织id 创建年份放进session 供其他页面调用
		session.setAttribute("org_id", org_id);
		session.setAttribute("grade", grade);
		StringBuffer allGroups = new StringBuffer();
		List<Groups> groups = this.groupsService.selectGroupsByTea_idAndGrade(
				tea_id, grade);
		for (Groups g : groups) {
			String practice_id = DictionaryService.findGroups(g.getId())
					.getPractice_id();
			String state = DictionaryService.findPracticeTask(practice_id)
					.getTask_type();
			if (state.equals("2")) {
				allGroups.append("<tr>");
				allGroups.append("<td><a href='editGroups.do?id=" + g.getId()
						+ "'>" + g.getGroup_name() + "</a></td>");
				allGroups.append("<td>" + g.getDescription() + "</td>");
				allGroups.append("<td>"
						+ DictionaryService
								.findPracticeTask(g.getPractice_id())
								.getTask_name() + "</td>");
				allGroups.append("<td>" + g.getPurpose() + "</td>");
				allGroups.append("<td>" + g.getCreate_time() + "</td>");
				allGroups.append("<td>"
						+ DictionaryService.findTeacher(g.getTea_id())
								.getTrue_name() + "</td>");
				allGroups
						.append("<td><input type='button' value='修改' onclick=\"updateGroupMembers('"
								+ g.getId() + "')\"></td>");
				allGroups
						.append("<td><input type='button' value='删除' onclick=\"doDel('"
								+ g.getId() + "')\"></td>");
				allGroups
						.append("<td><input type='button' value='导入学生' onclick=\"importStudentsId('"
								+ g.getId() + "')\"></td>");
				allGroups.append("</tr>");
			} else {
				continue;
			}

		}
		try {
			response.getWriter().println(allGroups.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存修改的分组信息任务 功能：邢志武2015年5月8日 09:47:51
	 * 
	 * */
	@RequestMapping("teacher/doEditGroups.do")
	public String saveEditGroups(HttpServletRequest request) {
		String id = request.getParameter("id");
		String description = request.getParameter("description");
		String purpose = request.getParameter("purpose");
		Groups groups = DictionaryService.findGroups(id);
		groups.setDescription(description);
		groups.setPurpose(purpose);
		groupsService.update(groups);
		return "redirect:groupsList.do"; // 修改成功后重定向到列表页面
	}

	/**
	 * 删除小组，小组成员 邢志武20150118
	 * 
	 * */
	@RequestMapping("teacher/deleteGroups.do")
	public String deleteGroups(String id, HttpSession session) {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Groups g = new Groups();
		String groupName = DictionaryService.findGroups(id).getGroup_name();
		StringBuffer sb = new StringBuffer();
		sb.append(groupName + "(无效)");
		String gN = sb.toString();
		g.setId(id);
		g.setGroup_name(gN);
		g.setDismiss_time(ts);
		groupsService.update(g);

		List<String> gmId = groupMembersService
				.selectGroupMembersIdByGroupId(id);
		for (int i = 0; i < gmId.size(); i++) {
			GroupMembers gm = new GroupMembers();
			gm.setState("2");
			String gId = gmId.get(i);
			gm.setId(gId);
			groupMembersService.update(gm);
		}
		String org_id = (String) session.getAttribute("org_id");
		String grade = (String) session.getAttribute("grade");
		return "redirect:groupsList.do";
	}

	/**
	 * 查看并修改小组成员 功能：邢志武 20150118
	 * 
	 * */
	@RequestMapping("teacher/updateGroupMembers.do")
	public ModelAndView updateGroupMembers(ModelMap modelMap, String id,
			HttpSession session) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String org_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(org_id).getOrg_level();
		if (org_level.equals("2")) {
			List<Org> xiliebiao = orgService.selectByXyId(org_id);
			modelMap.put("xiliebiao", xiliebiao);

		} else if (org_level.equals("3")) {
			String par_id = DictionaryService.findOrg(org_id).getParent_id();
			List<Org> xiliebiao = orgService.selectByXyId(par_id);
			modelMap.put("xiliebiao", xiliebiao);

		}
		List<GroupMembers> groups = this.groupMembersService
				.selectGroupMembersByGroupId(id);
		String group_id = groups.get(0).getGroup_id();
		modelMap.put("group_id", group_id);
		modelMap.put("groups", groups);
		return new ModelAndView("teacher/updateGroupMembers", modelMap);
	}

	/**
	 * 查看并修改小组 功能：邢志武 20150118
	 * 
	 * */
	@RequestMapping("teacher/updateGroups.do")
	public String updateGroups(HttpServletRequest request, HttpSession session) {
		String group_id = request.getParameter("group_id");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String Ttea_id = tea.getId();
		groupMembersService.deleteByGroup_id(group_id);
		String ID = request.getParameter("shuZu");// 获取新创建分组的人员
		String[] allID = ID.split(",");
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
											// HH:mm:ss格式显示
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int tNumber = 0;
		// 这里插入小组成员
		for (int i = 0; i < allID.length; i++) {
			// 创建成员表（分组成员表）
			GroupMembers groupMember = new GroupMembers();
			groupMember.setId(Common.returnUUID16());
			groupMember.setGroup_id(group_id);
			groupMember.setBegin_time(ts);
			groupMember.setState("1");
			groupMember.setEnd_time(null);
			String a = allID[i];
			int f = a.lastIndexOf("-");
			String tId = "";
			if (f == -1) {
				tId = a;
			} else {
				tId = a.substring(0, f);
			}
			int b = tId.length();

			if (b > 16) {
				// 执行插入学生sql
				String stu_id = tId;
				groupMember.setUser_id(stu_id);
				groupMember.setDuty("学生");
				System.out.println("+++++++++++++++++stu_id:" + stu_id);
				groupMembersService.insert(groupMember);

			} else {
				tNumber = tNumber + 1;
				// 执行插入教师sql
				String tea_id = tId;
				if (tea_id.equals("")) {
					// 判断成员是否为空！
					groupMember.setUser_id(Ttea_id);
				} else {
					groupMember.setUser_id(tea_id);
				}
				groupMember.setDuty("老师");
				System.out.println("+++++++++++++++++tea_id:" + tea_id);
				groupMembersService.insert(groupMember);
			}
		}
		// 针对如果只是删除老师保留学生这种情况 2015年6月22日 15:01:58 邢志武
		if (tNumber == 0) {
			GroupMembers groupMember = new GroupMembers();
			groupMember.setId(Common.returnUUID16());
			groupMember.setGroup_id(group_id);
			groupMember.setBegin_time(ts);
			groupMember.setState("1");
			groupMember.setEnd_time(null);
			groupMember.setUser_id(Ttea_id);
			groupMember.setDuty("老师");
			groupMembersService.insert(groupMember);
		}

		return "redirect:editGroups.do?id=" + group_id;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return 验证小组是否重复
	 * @throws IOException
	 *             2015年5月10日 14:37:50 邢志武
	 */
	@RequestMapping("teacher/checkGroupName.do")
	public String checkGroupName(HttpServletRequest request,
			HttpSession session, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String group_name = request.getParameter("group_name");
		// 解决乱码问题
		group_name = new String(group_name.getBytes("iso-8859-1"), "UTF-8");
		int trueOrFlase = groupsService.selectCountByGroup_name(group_name);
		StringBuffer sb = new StringBuffer();
		if (trueOrFlase == 1) {
			sb.append("<font color='red'size='2'>分组名已重复不可用! X</font>");
		} else if (group_name.equals("")) {
			sb.append("<font color='red'size='2'>分组名不能为空! X</font>");
		} else {
			sb.append("<font color='green'size='2'>分组名可用! √</font>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 功能：ajax传递org_level by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxOrg_level.do")
	@ResponseBody
	public String ajaxOrg_level(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String class_id = request.getParameter("orgId");
		Org o = DictionaryService.findOrg(class_id);
		String org_level = o.getOrg_level();
		/* String org_name=o.getOrg_name(); */
		return org_level;
	}

	/**
	 * 功能：ajax查询该组织的成员(映射) by 邢志武20140927 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxSearchPerson.do")
	@ResponseBody
	public List ajaxSearchPerson(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String class_id = request.getParameter("orgId");
		Org o = DictionaryService.findOrg(class_id);
		String org_level = o.getOrg_level();
		if (org_level.equals("5")) {
			List<Student> students = studentService
					.getStudentsByClassId(class_id);
			return students;
		} else {
			List<Teacher> teachers = teacherService
					.getTeachersByDeptId(class_id);
			return teachers;
		}
	}

	/**
	 * 功能：ajax传递 实践任务by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxShiJian.do")
	@ResponseBody
	public List ajaxShiJian(HttpServletRequest request, HttpSession session)
			throws IOException {
		String data = request.getParameter("data");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		int a = data.indexOf("-");
		String nianji = data.substring(a + 1, data.length());
		if (data != null) {
			List<PracticeTask> ren = practiceTaskService
					.getPracticeTasksGradeByGradeAndTea_id(nianji, tea_id);
			List<PracticeTask> renwu = new ArrayList<PracticeTask>();
			for (PracticeTask p : ren) {
				if (p.getTask_type().equals("2")) {
					renwu.add(p);
				}
			}

			return renwu;
		}
		return null;
	}

	// ajax 查询系老师 20150119

	@RequestMapping("teacher/ajaxTeacher2.do")
	@ResponseBody
	public List ajaxTeacher2(HttpServletRequest request) throws IOException {
		String c = request.getParameter("xibu");
		if (c != null) {
			List<Teacher> t = teacherService.getTeachersByDeptId(c);
			return t;
		}
		return null;
	}

	// ajax 查询班级 20150119 xzw
	@RequestMapping("teacher/ajaxClass.do")
	@ResponseBody
	public List ajaxClass(HttpServletRequest request) throws IOException {
		String c = request.getParameter("xibu");
		String grade = request.getParameter("grade");
		if (c != null && grade != null) {
			List<Org> cla = orgService.selectBygradeAndXiId(c, grade);
			return cla;
		}
		return null;
	}

	// ajax 查询班级成员 20150119 xzw
	@RequestMapping("teacher/ajaxPersons.do")
	@ResponseBody
	public List ajaxPersons(HttpServletRequest request) throws IOException {
		String c = request.getParameter("cla");
		if (c != null) {
			List<Student> stu = studentService.getStudentsByClassId(c);
			return stu;
		}
		return null;
	}

	/**
	 * 功能：创建分组信息 by 邢志武
	 * 
	 * */
	@RequestMapping("teacher/doAddGroups.do")
	public ModelAndView doAddGroups(HttpServletRequest request,
			ModelMap modelMap, HttpSession session) {
		// 为返回视图数据准备
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String t_id = tea.getId();
		String dept_id = tea.getDept_id();
		String group_name = request.getParameter("group_name");// 小组名称
		// 去掉前后空格
		String trueGroupName = group_name.trim();
		String purpose = request.getParameter("purpose");// 小组目的
		String description = request.getParameter("description"); // 小组描述
		String renWuId = request.getParameter("renWuId"); // 选择的任务id
		String EstaTea_id = DictionaryService.findPracticeTask(renWuId)
				.getTea_id();
		/*
		 * String ID = request.getParameter("shuZu");// 获取新创建分组的人员 String[]
		 * allID = ID.split(",");
		 */
		// 创建小组（用户分组表）
		Groups group = new Groups();
		group.setId(Common.returnUUID16());
		group.setGroup_name(trueGroupName);
		group.setPurpose(purpose);
		group.setDescription(description);
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
											// HH:mm:ss格式显示
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		group.setCreate_time(ts);
		group.setDismiss_time(null);
		group.setTea_id(t_id);
		group.setPractice_id(renWuId);
		groupsService.insert(group);
		String group_id = group.getId();
		// 插入小组负责老师
		GroupMembers gm = new GroupMembers();
		gm.setId(Common.returnUUID16());
		gm.setGroup_id(group_id);
		gm.setBegin_time(ts);
		gm.setState("1");
		gm.setEnd_time(null);
		gm.setUser_id(EstaTea_id);
		gm.setDuty("老师");
		groupMembersService.insert(gm);
		/*
		 * // 这里插入小组成员 for (int i = 0; i < allID.length; i++) { // 创建成员表（分组成员表）
		 * GroupMembers groupMember = new GroupMembers();
		 * groupMember.setId(Common.returnUUID16());
		 * groupMember.setGroup_id(group_id); groupMember.setBegin_time(ts);
		 * groupMember.setState("1"); groupMember.setEnd_time(null);
		 * System.out.println("+++++++++++++++++allID.length:" + allID.length);
		 * String a = allID[i]; int b = a.indexOf("-"); String org_level =
		 * a.substring(0, b); System.out.println("+++++++++++++++++org_level:" +
		 * org_level);
		 * 
		 * if (org_level.equals("5")) { // 执行插入学生sql int f = a.lastIndexOf("-")
		 * + 1; String stu_id = a.substring(f, a.length());
		 * groupMember.setUser_id(stu_id); groupMember.setDuty("学生");
		 * System.out.println("+++++++++++++++++stu_id:" + stu_id);
		 * groupMembersService.insert(groupMember);
		 * 
		 * } else { // 执行插入教师sql int t = a.lastIndexOf("-") + 1; String tea_id =
		 * a.substring(t, a.length()); if (tea_id.equals(EstaTea_id)) {
		 * continue; } else { groupMember.setUser_id(tea_id);
		 * groupMember.setDuty("老师");
		 * System.out.println("+++++++++++++++++tea_id:" + tea_id);
		 * groupMembersService.insert(groupMember); } } }
		 */
		List<Groups> result = groupsService.getGroupsByDeptId(dept_id);
		modelMap.put("result", result);
		return new ModelAndView("redirect:editGroups.do?id=" + group_id,
				modelMap);
	}

	/**
	 * 功能：转到编辑分组信息 by吴敬国20141209
	 * 
	 * 邢志武修改 20150119
	 * */
	@RequestMapping("teacher/editGroups.do")
	public ModelAndView editGroups(ModelMap modelMap, String id) {
		Groups groups = (Groups) groupsService.selectByID(id);
		List<GroupMembers> groupMember = this.groupMembersService
				.selectGroupMembersByGroupId(id);
		int teaSize = this.groupMembersService.getTeachersSize(id);
		int stusSize = this.groupMembersService.getStudentsSize(id);
		modelMap.put("teaSize", teaSize);
		modelMap.put("stusSize", stusSize);
		modelMap.put("groups", groups);
		modelMap.put("groupMember", groupMember);
		return new ModelAndView("teacher/groupsEdit", modelMap);
	}

	/**
	 * 功能：邢志武 2015年4月11日
	 * 
	 * */
	@RequestMapping("teacher/addGroups.do")
	public ModelAndView addGroups(HttpSession session, ModelMap modelMap) {
		// 获取当前教师
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 获取该老师所属学院id
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		List<PracticeTask> result = groupsService
				.getPracticeTasksByDeptId(dept_id);
		String org_name = DictionaryService.findOrg(dept_id).getOrg_name();
		List<PracticeTask> Grade = practiceTaskService.getPracticeTasksGrade();
		List<Org> Org_Name = orgService.selectByXueYuanId(dept_id);
		modelMap.addAttribute("result", result);
		modelMap.put("org_name", org_name);
		modelMap.put("dept_id", dept_id);
		modelMap.put("Org_Name", Org_Name);
		modelMap.put("Grade", Grade);
		return new ModelAndView("teacher/groupsAdd", modelMap);
	}

	/**
	 * 查看实习任务、实训任务类.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("teacher/teaPraTaskList.do")
	public ModelAndView practiceTaskList(HttpSession session,
			HttpServletRequest request, String tasktype) {
		Map<String, Object> map = new HashMap<String, Object>();
		session.setAttribute("task_type", tasktype);// 记录是实习还是实训
		session.setAttribute("task_grade", Common.getDefaultYear());// 默认的年份
		List<PracticeTask> praTask_List = practiceTaskService
				.getPracticeTasksByGradeAndTea_idAndTasktype(
						Common.getOneTeaId(session), Common.getDefaultYear(),
						tasktype);
		map.put("praTask_List", praTask_List);
		map.put("task_type", tasktype);
		map.put("task_grade", Common.getDefaultYear());
		return new ModelAndView("teacher/teaPraTaskList", map);
	}

	/**
	 * 教师查看自己的实习，实训任务 实习任务查看 初始化数据、根据当前时间默认年级 by吴敬国2015-5-11
	 * 
	 * */
	@RequestMapping("teacher/seeStudent.do")
	public ModelAndView seeStudent(HttpSession session,
			HttpServletRequest request, String practice_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> studentList = studentService
				.getStuByPracticeTaskId(practice_id);
		map.put("studentList", studentList);
		return new ModelAndView("teacher/seeStudentList", map);
	}

	/**
	 * 功能：ajax传递 根据年级得到教师的实习实训任务 by 吴敬国20150117 *
	 * 
	 * */
	@RequestMapping("teacher/ajaxGetPraTask.do")
	public String ajaxGetPraTask(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String task_type = (String) session.getAttribute("task_type");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String task_grade = request.getParameter("grade");
		session.setAttribute("task_grade", task_grade);
		if (task_grade != null) {
			List<PracticeTask> pa = this.practiceTaskService
					.getPracticeTasksByGradeAndTea_idAndTasktype(tea_id,
							task_grade, task_type);
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy/MM/dd");
			for (PracticeTask ts : pa) {
				Date d = ts.getBegin_time();
				Date e = ts.getEnd_time();
				Date c = ts.getCreate_time();
				sb.append("<tr>");
				/* sb.append("<td>" + ts.getPractice_code() + "</td>"); */
				sb.append("<td align='left'>" + ts.getTask_name() + "</td>");
				sb.append("<td>" + dateFm.format(c) + "</td>");// ////////////////////////
				sb.append("<td>" + dateFm.format(d) + "</td>");
				sb.append("<td>" + dateFm.format(e) + "</td>");
				sb.append("<td>" + ts.getTask_desc() + "</td>");
				sb.append("<td>" + ts.getTask_place() + "</td>");
				if (ts.getFile_id() == null) {
					sb.append("<td>" + "没有文件" + "</td>");
				} else {
					sb.append("<td><a href='downloadFile.do?file_id="
							+ ts.getFile_id() + "'>" + "下载" + "</a></td>");
				}
				sb.append("<td><a href='seeStudent.do?practice_id="
						+ ts.getId() + "'>" + "查看学生" + "</a></td>");
				sb.append("</tr>");
			}
			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：就业指导 注解请求地址(映射) by王磊 2015年5月24日
	 * */
	@RequestMapping("teacher/instruction.do")
	public ModelAndView instructionList(HttpSession session, String post,
			String practiceId, String stu_id) {
		try {
			practiceId = new String(practiceId.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (post.equals("chongfu") && practiceId.equals("chongfu")) {

		} else {
			String newPost = "";
			try {
				newPost = URLDecoder.decode(post, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.setAttribute("instruction_post", newPost);
			session.setAttribute("instruction_practiceId", practiceId);
			session.setAttribute("instruction_practiceIdstu_id", stu_id);
		}
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		Map<String, Object> map = new HashMap<String, Object>();
		Notice t = new Notice();
		t.setTea_id(tea_id);
		t.setNotice_type("6");
		List<Notice> result = this.noticeService.selectList(t);
		map.put("result", result);
		return new ModelAndView("teacher/instructionList", map);
	}

	/**
	 * 功能：添加就业指导界面 注解请求地址(映射) by王磊 2015年5月24日
	 * */
	@RequestMapping("teacher/instructionAdd.do")
	public ModelAndView instructionAdd(HttpSession session) {

		String stu_id = (String) session
				.getAttribute("instruction_practiceIdstu_id");
		PracticeRecord stu_pra = practiceRecordService
				.getPrecordUndimission_time(stu_id);
		String post_id = stu_pra.getPost_id();
		String practiceId = (String) session
				.getAttribute("instruction_practiceId");
		PracticeRecord practiceRecord = new PracticeRecord();
		practiceRecord.setPractice_id(practiceTaskService
				.getIdByName(practiceId));
		practiceRecord.setPost_id(post_id);

		Map<String, Object> map = new HashMap<String, Object>();

		List<PracticeRecord> ptResult = this.practiceRecordService
				.selectList(practiceRecord);
		String sendInfor = "";
		for (PracticeRecord pt : ptResult) {
			String name = "";
			if (DictionaryService.findStudent(pt.getStu_id()) != null) {
				name = DictionaryService.findStudent(pt.getStu_id())
						.getTrue_name();
			}
			int i = 0;
			if (i % 5 == 0) {
				sendInfor = sendInfor + "</tr><br><tr id='infor'>";
			}
			sendInfor = sendInfor + "<td><label for='" + pt.getStu_id()
					+ "'><input type='checkbox' id='" + pt.getStu_id()
					+ "' name='students' value ='" + pt.getStu_id() + "'>"
					+ name + "</label></td>";
			i++;
		}
		map.put("post", post_id);
		map.put("task_name", practiceId);
		map.put("sendInfor", sendInfor);
		return new ModelAndView("teacher/instructionAdd", map);
	}

	/**
	 * 功能：保存添加就业指导 注解请求地址(映射) by王磊 2015年5月24日
	 * */
	@RequestMapping("teacher/doInstructionAdd.do")
	public String doInstructionAdd(HttpSession session,
			HttpServletRequest request) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();

		String practice_id = (String) session
				.getAttribute("instruction_practiceId");
		String pid = practiceTaskService.getIdByName(practice_id);
		String practice_code = DictionaryService.findPracticeTask(pid)
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
		String notice_cur_code = practice_code + "JYZD";
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
		String title = request.getParameter("title");
		String notice_range = request.getParameter("notice_range");
		Notice notice = new Notice();
		notice.setTea_id(tea_id);
		notice.setOrg_id("");
		notice.setCreate_time(time);
		notice.setNotice_code(notice_code);
		notice.setNotice_type("6");
		notice.setContent(content);
		notice.setTitle(title);
		notice.setStu_id(notice_range);
		noticeService.insert(notice);
		return "redirect:instruction.do?post=" + "chongfu" + "&practiceId="
				+ "chongfu"; // 添加后成功重定向到列表页面

	}

	/**
	 * 功能：删除就业指导 by ：王磊 2015年5月25日
	 * 
	 * */
	@RequestMapping("teacher/deleteInstruction.do")
	public String deleteInstruction(String id, HttpSession session) {
		String post = (String) session.getAttribute("instruction_post");
		String practiceId = (String) session
				.getAttribute("instruction_practiceId");
		Notice n = new Notice();
		n.setId(id);
		noticeService.delete(n);
		return "redirect:instruction.do?post=" + post + "&practiceId="
				+ practiceId; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：教师查看就业指导 详情注解请求地址(映射)王磊2015年5月25日
	 * 
	 * */
	@RequestMapping("teacher/instructionDetail.do")
	public String instructionDetail(ModelMap modelMap, String id) {
		Notice n = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", n);
		return "teacher/instructionDetail";

	}

	/**
	 * 功能：教师编辑就业指导。 by：王磊 2015年5月25日
	 * */
	@RequestMapping("teacher/editInstruction.do")
	public String editInstruction(ModelMap modelMap, String id) {
		Notice n = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", n);
		return "teacher/instructionEdit";

	}

	/**
	 * 功能：教师修改就业指导 by王磊2015年5月25日
	 * */
	@RequestMapping("teacher/doEditInstruction.do")
	public String doEditInstruction(String id, HttpServletRequest request,
			HttpSession session) throws IOException {
		String content = request.getParameter("content");
		String title = request.getParameter("title");
		Notice n = new Notice();
		n = (Notice) this.noticeService.selectByID(id);
		n.setTitle(title);
		n.setContent(content);
		noticeService.update(n);
		return "redirect:instruction.do?post=" + "chongfu" + "&practiceId="
				+ "chongfu"; // 添加成功后重定向到列表页面
	}

	/**
	 * @author 王磊
	 * @time 2015年5月28日
	 * @return practiceRecord
	 */
	@RequestMapping("teacher/practiceRecord.do")
	public ModelAndView practiceRecordList(HttpSession session, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String practiceRecord_grade = (String) session
				.getAttribute("practiceRecord_grade");
		String practiceRecord_practiceTaskId = (String) session
				.getAttribute("practiceRecord_practiceTaskId");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<PracticeTask> practiceTasks = null;
		List<PracticeRecord> practiceRecordList = null;
		List<String> grades = this.practiceTaskService.getGrades(tea_id);
		if (grades.size() != 0) {
			String grade = grades.get(0);
			practiceTasks = this.practiceTaskService
					.getPracticeTaskByGradeAndTeaId(tea_id, grade);// 查出老师所带的有效的实习任务
			String practiceTaskid = practiceTasks.get(0).getId();
			practiceRecordList = this.practiceRecordService
					.getPracticeRecord(practiceTaskid);
		}
		if (type != null) {
			session.removeAttribute("practiceRecord_grade");
			session.removeAttribute("practiceRecord_practiceTaskId");
			practiceRecord_grade = null;
			practiceRecord_practiceTaskId = null;

		}
		map.put("grades", grades);
		map.put("practiceTasks", practiceTasks);
		map.put("practiceRecordList", practiceRecordList);
		map.put("practiceRecord_grade", practiceRecord_grade);
		map.put("practiceRecord_practiceTaskId", practiceRecord_practiceTaskId);
		return new ModelAndView("teacher/practiceRecord", map);
	}

	/**
	 * @author 王磊
	 * @time 2015年5月29日
	 * @return practiceRecord
	 * 
	 */
	@RequestMapping("teacher/getMorePracticeRecordList.do")
	public ModelAndView getMorePracticeRecordList(String stu_id, String prac_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		PracticeRecord practiceRecord = new PracticeRecord();
		practiceRecord.setPractice_id(prac_id);
		practiceRecord.setStu_id(stu_id);
		List<PracticeRecord> practiceRecordList = this.practiceRecordService
				.selectList(practiceRecord);
		map.put("practiceRecordList", practiceRecordList);
		return new ModelAndView("teacher/practiceRecordDetail", map);
	}

	/**
	 * 功能：通过年份得到此老师的实习id
	 * 
	 * @author 王磊 2015年5月29日
	 * 
	 * */
	@RequestMapping("teacher/getPracticeTaskByGrade.do")
	public String getPracticeTaskByGrade(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String year = request.getParameter("grade");
		session.setAttribute("practiceRecord_grade", year);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		response.setCharacterEncoding("UTF-8");
		PracticeTask pt = new PracticeTask();
		pt.setGrade(year);
		pt.setTea_id(tea_id);
		pt.setTask_type("1");
		pt.setState("1");
		List<PracticeTask> pts = this.practiceTaskService.selectList(pt);
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='practice_id' id='practice_id' style='width:200px'>");
		for (PracticeTask ps : pts) {
			sb.append("<option value=" + ps.getId() + ">" + ps.getTask_name()
					+ "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：实习id得到该实习学生的最近就业记录
	 * 
	 * @author 王磊 2015年5月29日
	 * 
	 * */
	@RequestMapping("teacher/getPracticeRecordById.do")
	public String getPracticeRecordById(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String practiceTaskId = request.getParameter("practiceTaskId");
		session.setAttribute("practiceRecord_practiceTaskId", practiceTaskId);
		List<PracticeRecord> practiceRecordList = this.practiceRecordService
				.getPracticeRecord(practiceTaskId);
		StringBuffer sb = new StringBuffer();
		String is_netsign = "是";
		String is_contract = "是";
		for (PracticeRecord prs : practiceRecordList) {
			if (prs.getIs_netsign().equals("2")) {
				is_netsign = "否";
			}
			;
			if (prs.getIs_contract().equals("2")) {
				is_contract = "否";
			}
			;
			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findStudent(prs.getStu_id())
							.getStu_code() + "</td>");
			sb.append("<td>"
					+ DictionaryService.findStudent(prs.getStu_id())
							.getTrue_name() + "</td>");
			sb.append("<td>"
					+ DictionaryService.findPracticeTask(prs.getPractice_id())
							.getTask_name() + "</td>");
			sb.append("<td>"
					+ DictionaryService.findCompany(prs.getCompany_id())
							.getCom_name() + "</td>");
			sb.append("<td>" + prs.getPost_id() + "</td>");
			sb.append("<td>" + is_netsign + "</td>");
			sb.append("<td>" + is_contract + "</td>");
			sb.append("<td>" + prs.getCom_teacher() + "</td>");
			sb.append("<td>" + prs.getCom_phone() + "</td>");
			sb.append("<td>" + prs.getCom_orgion() + "</td>");
			sb.append("<td>" + prs.getWork_orgion() + "</td>");
			sb.append("<td><input type='button' value='历史更多' onclick=getMorePracticeRecord('"
					+ prs.getStu_id() + "','" + practiceTaskId + "');></td>");
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
	 * 功能：奖惩管理避免与ajax刷新返回冲突，重新定义此方法。 by 王磊 日期：2015年6月9日
	 * 
	 * 
	 * */
	@RequestMapping("teacher/getPracticeTaskByGradeAndTeaId.do")
	public String getPracticeTaskByGradeAndTeaId(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String year = request.getParameter("grade");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		response.setCharacterEncoding("UTF-8");
		PracticeTask pt = new PracticeTask();
		pt.setGrade(year);
		pt.setTea_id(tea_id);
		pt.setTask_type("1");
		pt.setState("1");
		List<PracticeTask> pts = this.practiceTaskService.selectList(pt);
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='practice_id' id='practice_id' style='width:200px' onChange='changeCK()'>");
		sb.append("<option value='0'>请选择任务</option>");
		for (PracticeTask ps : pts) {
			sb.append("<option value=" + ps.getId() + ">" + ps.getTask_name()
					+ "</option>");
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param modelMap
	 * @param session
	 * @param request
	 * @param response
	 * @return 转发到专家回复页面 邢志武 2015年6月9日 09:26:54
	 */
	@RequestMapping("teacher/toShortQuestionsList.do")
	public String toShortQuestions(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<Knowledge> knowledges = this.knowledgeService
				.getUnAnswerQuestion(tea_id);
		// 分页
		int pageSize = 15;
		int Currentpage = 1;// 初始值1
		// 获取当前页集合
		List<Knowledge> knowledgesList = Common.getListCurrentPage(knowledges,
				pageSize, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(knowledges, pageSize, Currentpage);
		session.setAttribute("TEACHER_CONTROLLER_KNOWLEDGES", knowledges);
		modelMap.put("count", pageCount);
		modelMap.put("result", knowledgesList);
		modelMap.put("nowPage", Currentpage);
		return "teacher/shortQuestionsList";

	}

	// 转发回答问题页面，专家指导，邢志武 2015年6月9日 10:14:32
	@RequestMapping("teacher/shortQuestions.do")
	public String shortQuestions(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String question_id = request.getParameter("question_id");
		Knowledge knowledge = (Knowledge) this.knowledgeService
				.selectById(question_id);
		modelMap.put("knowledge", knowledge);
		return "teacher/shortQuestions";

	}

	// 转发回答问题页面，专家指导，邢志武 2015年6月9日 10:14:32
	@RequestMapping("teacher/doSavaShortQuestion.do")
	public String doSavaShortQuestion(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String answer = request.getParameter("answer");
		String question_id = request.getParameter("question_id");
		Date now = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp answerQuestionTime = new Timestamp(System.currentTimeMillis());
		answerQuestionTime = new Timestamp(now.getTime());
		Knowledge knowledge = (Knowledge) this.knowledgeService
				.selectById(question_id);
		knowledge.setAnswer_time(answerQuestionTime);
		knowledge.setAnswer(answer);
		this.knowledgeService.update(knowledge);
		return "redirect:toShortQuestionsList.do";

	}

	/**
	 * 功能：用户手册下载 by sl 吴敬国 2015-6-30 type: stuWX 学生微信 stuWeb 学生web teaWeb 教师web
	 * */
	@RequestMapping("teacher/helpsUserdownload.do")
	public String ShouCeDownload(HttpServletRequest request,
			HttpSession session, HttpServletResponse response, String type)
			throws Exception {
		String project_path = request.getSession().getServletContext()
				.getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		String file_type = "Helps";
		String posistion = "";
		if (type.equalsIgnoreCase("teaWeb")) {
			posistion = file_type + "/" + "教师web端用户手册.doc";
		} else if (type.equalsIgnoreCase("stuWX")) {
			posistion = file_type + "/" + "学生微信端用户手册.doc";
		} else {
			posistion = file_type + "/" + "学生web端用户手册.doc";
		}
		/* String a = file_type+"/"+file_type1+"/"+"教师用户手册.doc"; */
		filesService.downloadfile(posistion, ctxPath, request, response);
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 *             专家回复 分页上下页 2015年6月12日 11:36:52 邢志武
	 */
	@RequestMapping("teacher/getQuestionByPage.do")
	public String getTeacherByPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		// 获取教师集合，增加条件：下拉框选中的组织id
		List<Knowledge> result = (List<Knowledge>) session
				.getAttribute("TEACHER_CONTROLLER_KNOWLEDGES");
		String toPage = request.getParameter("toPage");
		List<Knowledge> newResult;// 当前页的集合
		newResult = Common.getListCurrentPage(result, pageSizeConstant,
				Integer.parseInt(toPage));
		StringBuffer sb = new StringBuffer();
		int i = 0;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		for (Knowledge k : newResult) {
			i = i + 1;
			String stu_name = DictionaryService
					.findStudent(k.getMessenger_id()).getTrue_name();
			String class_id = DictionaryService
					.findStudent(k.getMessenger_id()).getClass_id();
			String class_name = DictionaryService.findOrg(class_id)
					.getOrg_name();
			String createTime = format.format(k.getCreate_time());
			String state = "<font color='green'>已解答</font>";
			String operation = "<a href='shortQuestions.do?question_id="
					+ k.getId() + "'><font color='green'>修改答案</font></a>";
			if (k.getAnswer_time() == null) {
				state = "<font color='red'>未解答</font>";
				operation = "<a href='shortQuestions.do?question_id="
						+ k.getId() + "'><font color='red'>回答问题</font></a>";
			}
			sb.append("<tr>");
			sb.append("	<td align='center'>" + i + "</td>");
			sb.append("<td align='center'>" + k.getQuestion() + "</td>");
			sb.append("	<td align='center'>" + stu_name + "</td>");
			sb.append("	<td align='center'>" + class_name + "</td>");
			sb.append("	<td align='center'>" + createTime + "</td>");
			sb.append("	<td align='center'>" + state + "</td>");
			sb.append("	<td align='center'>" + operation + "</td>");
			sb.append("</tr>");
		}
		sb.append("," + toPage);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 企业教师管理 吴敬国2015-9-1 贾建昶 15/11/21
	 */
	@RequestMapping("teacher/ComTeaManage.do")
	public ModelAndView comTeaManage(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获得年级列表
		List<String> grades = this.practiceTaskService.getGrades(Common
				.getOneTeaId(session));
		// 将列表传到前台
		map.put("grades", grades);
		String stuList_practiceId = (String) session
				.getAttribute("stuList_practiceId");
		String stulist_grade = (String) session.getAttribute("stulist_grade");

		// 如果年级为空，显示默认年级
		if (stulist_grade == null) {
			stulist_grade = Common.getDefaultYear();
		}
		// 如果实习任务为空，根据老师ID与入学年纪任务列表查询实习任务
		if (stuList_practiceId == null) {
			List<PracticeTask> list = this.practiceTaskService
					.getOutPracticeTasksBytea_id(Common.getOneTeaId(session),
							stulist_grade);
			if (list.size() == 0) { // 判断老师是否有任务
				return new ModelAndView("teacher/ComTeaManage", map);
			} else {
				stuList_practiceId = list.get(0).getId();
			}
		}
		map.put("stuList_practiceId", stuList_practiceId);
		map.put("stulist_grade", stulist_grade);
		return new ModelAndView("teacher/showComTeacher", map);
	}

	/**
	 * 企业教师管理-检查企业老师是否有登陆信息 by sl
	 */
	@RequestMapping("teacher/checkLogin.do")
	public ModelAndView comManageCheckLogin(HttpServletRequest request,
			HttpSession session, HttpServletResponse response,
			String Com_teacher_id, String stu_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = this.teacherService.selectTeacode(Com_teacher_id);
		String tea_college_id = (String) session.getAttribute("tea_college_id");
		Role role = new Role();
		role.setCollege_id(tea_college_id);
		role.setRole_type("5");
		List<Role> rolelist = roleService.selectList(role);
		String S_code = stu_code;
		session.setAttribute("S_code", S_code);
		map.put("rolelist", rolelist);
		map.put("teacher", teacher);
		return new ModelAndView("teacher/EditComteacher", map);
	}

	/**
	 * 分配信息给企业老师登陆权限 by sl
	 */
	@RequestMapping("teacher/getLoginAndPass.do")
	public String comgetLoginAndPass(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		System.out.println("dfdsafdsa");
		String sid = request.getParameter("id");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String xiId = tea.getDept_id();
		String xueyuanid = DictionaryService.findOrg(xiId).getParent_id();
		String jiequ = xueyuanid.substring(0, 2);
		// 动态分配数据库中老师code
		String tea_code = this.teacherService.getLoginAndPass(jiequ);
		if (tea_code == null) {// 为第一个老师分配code
			tea_code = jiequ + "0001";
		} else {// 不是第一个，默认最比大的加2
			int maxCode = Integer.parseInt(tea_code) + 1;
			if (String.valueOf(maxCode).length() == 1) {// 如果是个位数
				tea_code = jiequ + "000" + maxCode;
			} else if (String.valueOf(maxCode).length() == 2) {// 如果是两位位数
				tea_code = jiequ + "00" + maxCode;
			} else {// 如果是三位数
				tea_code = jiequ + "0" + maxCode;
			}
		}
		String login_pass = "123456";
		String LoginAndPass = tea_code + "-" + login_pass;
		response.getWriter().println(LoginAndPass);
		return null;

	}

	/**
	 * 企业教师管理-保存企业老师的登陆信息，并发给学生通知，分配企业老师权限 by sl
	 */
	@RequestMapping("teacher/submitForm.do")
	public String comTeaMangersubmitForm(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");// 当前用户
		String tea_code = request.getParameter("login");
		String login_pass = request.getParameter("pass");
		String id = request.getParameter("aaa");
		String content = request.getParameter("content");
		String userrole_id = request.getParameter("userrole");
		String state = "";
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			time = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 通知分Id
		String jiequ = "qy";
		String stu_id = (String) session.getAttribute("S_code");
		String tea_id = tea.getId();
		String teaCode = this.teacherService.getLoginAndPass(tea_id);
		if (teaCode == null) {// 为通知分Id
			teaCode = jiequ + "0001";
		} else {// 不是第一个，默认最比大的加2
			int maxCode = Integer.parseInt(teaCode) + 1;
			if (String.valueOf(maxCode).length() == 1) {// 如果是个位数
				teaCode = jiequ + "000" + maxCode;
			} else if (String.valueOf(maxCode).length() == 2) {// 如果是两位位数
				teaCode = jiequ + "00" + maxCode;
			} else {// 如果是三位数
				teaCode = jiequ + "0" + maxCode;
			}
		}
		if (content == "") {
			state = "1";
			Teacher teacher = DictionaryService.findTeacher(id);
			teacher.setTea_code(tea_code);
			teacher.setLogin_pass(login_pass);
			teacher.setState(state);
			teacherService.update(teacher);
			// 分配基本用户角色
			userRoleService.saveBasicUserRole(id, null, "ROLE_TEACHER", null,
					null, "ROLE_COMPANY");
			// 分配企业指导教师角色 2016-1-11
			String collegeCode = DictionaryService.findOrg(
					(String) session.getAttribute("tea_college_id"))
					.getOrg_code();
			UserRole rct = new UserRole();
			rct.setUser_id(id);
			rct.setRole_id(userrole_id);
			this.userRoleService.insert(rct);

			String notice_code = teaCode;
			// 发通知告诉学生信息通过
			Notice notice = new Notice();
			notice.setContent("企业老师审核已通过。");
			notice.setTea_id(tea_id);
			notice.setStu_id(stu_id);
			notice.setOrg_id("");
			notice.setTitle("关于企业老师审核的通知");
			notice.setNotice_type("2");
			notice.setCreate_time(time);
			notice.setNotice_code(notice_code);
			noticeService.insert(notice);
			// 更新实习就业记录表的老师信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("com_teacher", id);
			map.put("stu_id", stu_id);
			practiceRecordService.updateComteacher(map);
		} else {
			state = "2";
			Teacher teacher = DictionaryService.findTeacher(id);
			teacher.setState(state);
			teacherService.update(teacher);
			String notice_code = teaCode;
			// 发通知告诉学生信息不通过
			Notice notice = new Notice();
			notice.setContent("审核不通过的原因：" + content);
			notice.setTea_id(tea_id);
			notice.setStu_id(stu_id);
			notice.setTitle("关于企业老师审核不通过的通知");
			notice.setNotice_type("2");
			notice.setCreate_time(time);
			notice.setOrg_id("");
			notice.setNotice_code(notice_code);
			noticeService.insert(notice);
		}
		return "redirect:ComTeaManage.do";
	}

	/**
	 * 功能：教师提交表格by王磊2015年6月20日
	 */
	@RequestMapping(value = "teacher/doGetImports.do", method = RequestMethod.POST)
	public String doGuidenceTeacherImport(MultipartHttpServletRequest request,
			ModelMap modelMap, HttpSession se) throws Exception {
		// 导入类型
		String type = Common.NulltoBlank(request.getParameter("type"));
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）,创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					String file_type = "";// file.getContentType();//使用getContentType()方法获得文件类型，以此决定允许上传的文件类型
					if (type.equals("excelGroupMemberStuId")) {
						file_type = "excelGroupMemberStuId";
					}
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					String fileName = file.getOriginalFilename();
					String filepos = "Import" + "/" + file_type + "_";
					String filePosition = "Import" + "/" + file_type + "_"
							+ fileName;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					String file_pa = project_path + real_path + filepos;
					// float filesize = file.getSize();//
					// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件
					File f = new File(file_pa + fileName);
					// 判断导入数据表类型
					if (type.equals("excelGroupMemberStuId")) {
						List<GroupMembers> groupMembers = ExcelService
								.AnalysisGroupMembers(f);
						HttpSession session = request.getSession();
						session.setAttribute("groupMembers", groupMembers);
						String groupId = (String) session
								.getAttribute("groupId");
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						int count;// 声明count判断该学生是否已在此任务中。
						List<String> codeList = new ArrayList();// 声明集合，存储学生编号，为了验证表格中的学生编号是否重复。
						for (GroupMembers gms : groupMembers) {
							String groupName = DictionaryService
									.findGroups(groupId).getGroup_name().trim();// 防止因为小组名称首尾有空格，致使导入数据不成功。
							GroupMembers gm = new GroupMembers();
							if (gms.getTemp2() == null) {
								infor = infor + "小组名称不能为空，";
							}
							;
							if (gms.getUser_id() == null) {
								infor = infor + "学号不能为空，";
							} else {
								if (DictionaryService.findStudent(gms
										.getUser_id()) == null) {
									infor = infor + "此学号错误没此学生信息，";
								} else {
									gm.setGroup_id(groupId);
									gm.setUser_id(gms.getUser_id());
									List<GroupMembers> gs = this.groupMembersService
											.selectList(gm);
									if (gs.size() > 0) {
										infor = infor + "此学生已在此实训任务中，";
									}

								}
							}
							;
							if (codeList.size() != 0) {
								infor = infor
										+ AdminController.isCodeExist(
												gms.getUser_id(), codeList,
												"学生编号");
							}
							;
							if (!groupName.equals(gms.getTemp2())) {
								infor = infor + "此小组名称不用一致，";
							}
							;
							if (infor.trim().equals("")) {
								infor = "无";
							}
							gms.setTemp1(infor);
							infor = "";
							if (gms.getUser_id() != null) {
								codeList.add(gms.getUser_id());
							}
							;
							// 判断学生编号是否在excel中重复
							String[] co = new String[codeList.size()];
							for (int a = 0; a < codeList.size(); a++) {
								co[a] = codeList.get(a);
							}
						}
						modelMap.put("groupMemberList", groupMembers);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					}
				}
			}
		}
		if (type.equals("excelGroupMemberStuId")) {
			ret = "teacher/groupMemberStuIdImport";
		}
		return ret;
	}

	/**
	 * 功能：跳转到实习分组导入学生界面 by：王磊 2015年6月20日
	 * 
	 * */
	@RequestMapping("teacher/importStudentIds.do")
	public String importStudentIds(String id, HttpSession session) {
		session.setAttribute("groupId", id);
		return "teacher/groupMemberStuIdImport";
	}

	/**
	 * 功能：教师保存提交的表格数据。by 王磊2015年6月20日
	 * 
	 */
	@RequestMapping("teacher/doSaveDates.do")
	public String doSaveDates(MultipartHttpServletRequest request,
			ModelMap modelMap, String fileName, String type) {
		HttpSession session = request.getSession();
		List<GroupMembers> gms = (List<GroupMembers>) session
				.getAttribute("groupMembers");
		String groupId = (String) session.getAttribute("groupId");
		if (type.equals("excelGroupMemberStuId")) {
			String group_name = DictionaryService.findGroups(groupId)
					.getGroup_name();
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
			for (GroupMembers groupmebers : gms) {
				groupmebers.setDuty("学生");
				groupmebers.setState("1");
				groupmebers.setBegin_time(time);
				groupmebers.setGroup_id(groupId);
				groupmebers.setTemp2(null);
				groupmebers.setTemp1(null);
				groupmebers.setId(Common.returnUUID16());
				this.groupMembersService.insert(groupmebers);
			}
			ret = "redirect:editGroups.do?id=" + groupId;
		}

		return ret;
	}

	/**
	 * 功能：联系我们 by：吴敬国 2015-7-28
	 * 
	 * */
	@RequestMapping("teacher/contactUS.do")
	public String contactUS(HttpSession session) {
		return "teacher/contactUS";
	}

	@RequestMapping("teacher/setNotic.do")
	public String setNotic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap modelMap) {
		String pk = request.getParameter("pk_id");
		String cd = request.getParameter("cd");
		modelMap.put("pk", pk);
		modelMap.put("cd", cd);
		return "teacher/noticeAdd2";
	}

	// 学生状态详细列表
	@RequestMapping("teacher/showStateStudents.do")
	public ModelAndView showStateStudents(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap modelMap) {
		String stateCode = request.getParameter("stateCode");
		// 设置默认年份：当前-3
		String grade = request.getParameter("year");
		// 状态编码
		String curStateCode = request.getParameter("stateCode");
		// 获取登录用户信息
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 获取单个学院各项状态人数
		String title = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc() + "的学生";

		List<PracticeTask> pk = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(tea.getId(), grade);
		List<List<Student>> allStudent = new ArrayList<List<Student>>();
		List<Student> listStudent = new ArrayList<Student>();
		for (int i = 0; i < pk.size(); i++) {
			String pk_id = pk.get(i).getId();
			listStudent = studentService.getStusByGradeAndTeaIdAndState(grade,
					pk_id, curStateCode);
			allStudent.add(listStudent);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listStudent", listStudent);
		map.put("title", title);
		return new ModelAndView("teacher/showStateStudents", map);
	}

	/**
	 * 查看未提交总结的学生 吴敬国 2015-9-3
	 * 
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("teacher/ajaxGetUnCommitStus.do")
	public String ajaxGetUnCommitStus(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String Practice_id = request.getParameter("praticeTaskId");
		List<Student> studentList = groupMembersService
				.getStuListByPraId(Practice_id);
		List<Student> unCommitStuList = new ArrayList<Student>();
		for (Student s : studentList) {
			List<PracticeRecord> practiceRecordList = practiceRecordService
					.selectCountPracticeRecordByStuId(s.getId());
			if (practiceRecordList.size() == 0) {
				unCommitStuList.add(s);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < unCommitStuList.size(); i++) {
			sb.append("<tr>");
			sb.append("	<td>" + (i + 1) + "</td>");
			sb.append("	<td>" + unCommitStuList.get(i).getTrue_name() + "</td>");
			sb.append("	<td>" + unCommitStuList.get(i).getStu_code() + "</td>");
			sb.append("<td >"
					+ DictionaryService.findOrg(
							unCommitStuList.get(i).getClass_id()).getOrg_name()
					+ "</td>");
			sb.append("<td >" + unCommitStuList.get(i).getPhone() + "</td>");
			sb.append("</tr>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	// 学生签到详情
	@RequestMapping("teacher/showStudentsMouthSinCount.do")
	public ModelAndView showStudentsMouthSinCount(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap modelMap) {
		String stu_id = request.getParameter("stu_id");
		String stu_name = DictionaryService.findStudent(stu_id).getTrue_name();
		String fmouth = request.getParameter("mouth");
		int mouth = Integer.parseInt(fmouth);
		String grade = Common.getNowYear();
		String trueMouth = "";
		if (mouth >= 7) {
			if (mouth < 10) {
				trueMouth = grade + "-0" + fmouth;
			} else {
				trueMouth = grade + "-" + fmouth;
			}

		} else {
			int year = Integer.parseInt(grade) + 1;
			trueMouth = String.valueOf(year) + "-0" + fmouth;
		}
		List<BMapEntity> StuSinMes = this.signService
				.selectSignStuStateByStuIdAndMothMes(stu_id, trueMouth);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listStudent", StuSinMes);
		map.put("stu_name", stu_name);
		map.put("mouth", mouth);
		return new ModelAndView("teacher/showStudentsMouthSinCount", map);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param modelMap
	 * @return 实习老师，所带学生地区分布统计
	 * @throws Exception
	 */
	@RequestMapping("teacher/getStudentCityStatistics.do")
	public ModelAndView getStudentCityStatistics(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap modelMap)
			throws Exception {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String tea_name = DictionaryService.findTeacher(tea_id).getTrue_name();
		// 设置默认的查询年级
		String grade = Common.getDefaultYear();
		// 存放各省市的學生數量
		Map<String, Integer> map = this.practiceTaskService.getMapStudent(
				tea_id, grade);
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
		result.put("tea_name", tea_name);
		result.put("grade", grade);
		result.put("cm", jsonData);
		result.put("bm", jsonData2);
		return new ModelAndView("teacher/StudentCityStatistics", result);
	}

	/**
	 * 功能教师工作成绩 2015年9月12日10:23:24 杨梦肖 在教师月工作量页面显示五个月的数据 2016、4、9
	 * 
	 * @author WuGee
	 * @throws ParseException
	 * */
	@SuppressWarnings("unused")
	@RequestMapping("teacher/teacherMouthWorkload.do")
	public ModelAndView teacherMouthWorkload(HttpSession session,
			HttpServletRequest request) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<Teacher> teaWorkloadList = new ArrayList<Teacher>();// 定义list，存放每月老师数据
		List<PracticeTask> prcaList = practiceTaskService
				.getPracticeTaskByGradeAndTeaId(tea.getId(), grade);
		String prc_id = prcaList.get(0).getId();
		Calendar c1 = Calendar.getInstance();
		int year1 = c1.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH) + 1;
		// 取得五个月份数据
		for (int i = 0; i < 5; i++) {
			// Teacher tea1 = (Teacher) session.getAttribute("current_user");
			if (month1 == 0) {
				year1 = year1 - 1;
				month1 = 12;
			}
			String month = String.valueOf(month1);
			String year = String.valueOf(year1);

			Map<String, Double> teacherMouthApicData = practiceTaskService
					.getTeacherMouthApicData2(prc_id, tea.getId(), month, year,
							grade,session);
			double score = teacherMouthApicData.get("score");
			// 合格实习人数（合格实习人数以学生上传实习月总结并且老师已批阅的数量为准）
			double qualifiedCount = teacherMouthApicData.get("qualifiedCount");
			// 教师所带的实习人数
			double studentSize = teacherMouthApicData.get("studentSize");
			// 教师完成度（实际工作量/理想工作量）
			double theoryApicScore = teacherMouthApicData
					.get("theoryApicScore") * 100;
			String org_id = orgService.getCollegeIdByTeaId(session);
			Org org = orgService.selectByID(org_id);
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
			String guideCoefficientString = Common
					.getDoubetoString(guideCoefficient);
			String scoreString = Common.getDoubetoString(score);
			String qualifiedCountString = Common
					.getDoubetoString(qualifiedCount);
			String studentSizeString = Common.getDoubetoString(studentSize);
			String theoryApicScoreString = Common
					.getDoubetoString(theoryApicScore) + "%";
			String time = year + "-" + month;
			Teacher tea1 = new Teacher();// new一个新的老师防止在add list的时候被覆盖
			tea1.setTemp1(time);
			tea1.setScore(scoreString);
			tea1.setQualifiedCount(qualifiedCountString);
			tea1.setStudentSize(studentSizeString);
			tea1.setTheoryApicScore(theoryApicScoreString);
			tea1.setPrac_id(prc_id);
			tea1.setYesOrNo(guideCoefficientString);
			teaWorkloadList.add(tea1);
			month1--;
		}
		map.put("tea", tea);
		map.put("t", teaWorkloadList);
		return new ModelAndView("teacher/teacherMouthWorkLoad", map);
	}

	/**
	 * 功能：ajax传递小组信息 邢志武 2015年4月11日
	 * 
	 * */
	@RequestMapping("teacher/getTeacherMouthWorkload.do")
	public String getTeacherMouthWorkload(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String date = request.getParameter("date");
		String[] arr = date.split("-");
		String year = arr[0];
		String mouth = arr[1];
		StringBuffer allTeachers = new StringBuffer();
		// 默认当月的即时性图
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		Teacher teacher = DictionaryService.findTeacher(tea_id);
		List<PracticeTask> prcaList = practiceTaskService
				.getPracticeTaskByGradeAndTeaId(tea_id, grade);
		String prc_id = prcaList.get(0).getId();
		Map<String, Double> teacherMouthApicData = practiceTaskService
				.getTeacherMouthApicData2(prc_id, tea_id, mouth, year, grade,session);
		// 教师实际工作量
		double score = teacherMouthApicData.get("score");
		// 合格实习人数（合格实习人数以学生上传实习月总结并且老师已批阅的数量为准）
		double qualifiedCount = teacherMouthApicData.get("qualifiedCount");
		// 教师所带的实习人数
		double studentSize = teacherMouthApicData.get("studentSize");
		// 教师完成度（实际工作量/理想工作量）
		double theoryApicScore = teacherMouthApicData.get("theoryApicScore") * 100;
		String org_id = orgService.getCollegeIdByTeaId(session);
		Org org = orgService.selectByID(org_id);
		Param p1 = new Param();
		p1.setParam_name("教师总结");
		p1.setDept_id(org.getOrg_code());
		Param param = new Param();
		param = paramService.selectParambyIdAndParam_name(p1);// 取出学院是否有总结参数
		double guideCoefficient = 0;
		//判断学院是否有要求上传教师总结
		if (param != null) {
			if (param.getParam_code().equals("false")) {
				//若学院无要求设置系数为1.0
				guideCoefficient = 1.0;
			} else {
				// 若学院有要求，教师是上传月总结系数为1.0，未上传为0.7
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
		String qualifiedCountString = Common.getDoubetoString(qualifiedCount);
		String studentSizeString = Common.getDoubetoString(studentSize);
		String theoryApicScoreString = Common.getDoubetoString(theoryApicScore)
				+ "%";
		teacher.setScore(scoreString);
		teacher.setQualifiedCount(qualifiedCountString);
		teacher.setStudentSize(studentSizeString);
		teacher.setTheoryApicScore(theoryApicScoreString);
		teacher.setPrac_id(prc_id);
		teacher.setYesOrNo(guideCoefficientString);
		String prc_name = DictionaryService.findPracticeTask(
				teacher.getPrac_id()).getTask_name();
		allTeachers.append("<tr>");
		allTeachers.append("<td>" + 1 + "</td>");
		allTeachers.append("<td>" + date + "</td>");
		allTeachers.append("<td>" + prc_name + "</td>");
		allTeachers.append("<td>" + teacher.getQualifiedCount() + "</td>");
		allTeachers.append("<td>" + teacher.getStudentSize() + "</td>");
		allTeachers.append("<td>" + teacher.getYesOrNo() + "</td>");
		allTeachers.append("<td>" + teacher.getScore() + "</td>");
		allTeachers.append("<td>" + teacher.getTheoryApicScore() + "</td>");
		allTeachers.append("</tr>");
		try {
			response.getWriter().println(allTeachers.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 功能：教师总结管理 吴敬国 2015-10-10
	 */
	@RequestMapping("teacher/teacherSummaryList.do")
	public ModelAndView teacherSummaryList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DirectRecord> directRecordList = this.directRecordService
				.getTeaDirecRecsByTeaIdAndDr_type(Common.getOneTeaId(session),
						"1");
		map.put("directRecordList", directRecordList);
		return new ModelAndView("teacher/teacherSummaryList", map);
	}

	/**
	 * 功能：老师添加总结 吴敬国2015-10-10
	 * 
	 * */
	@RequestMapping("teacher/addteacherSummary.do")
	public String addteacherSummary(HttpSession session, ModelMap modelMap) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		modelMap.put("tea", tea);
		return "teacher/teacherSummaryAdd";
	}

	/**
	 * 功能：管理员添加用户 保存 wtt 20141103 邢志武修改 2015-03-17 吴敬国 2015-9-10
	 * 
	 * */
	@RequestMapping("teacher/doAddTeacherSummary.do")
	public String doAddTeacherSummary(HttpSession session,
			HttpServletRequest request) throws IOException {
		String practice_id = request.getParameter("practice_id");
		String title = request.getParameter("title");
		String direct_place = request.getParameter("direct_place");
		String direct_time = request.getParameter("direct_time");
		String temp2 = request.getParameter("temp2");
		String stu_ids = request.getParameter("stu_ids");
		String direct_desc = request.getParameter("content");
		/* String worktype = request.getParameter("worktype"); */
		Groups g = new Groups();
		g.setPractice_id(practice_id);
		List<Groups> group = groupsService.selectList(g);
		Groups gp = (Groups) group.get(0);
		String org_id = gp.getId();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		DirectRecord directRecord = new DirectRecord();
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {// 如果有文件，保存文件
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					String file_type = "Directrecord";// file.getContentType();//使用getContentType()方法获得文件类型，以此决定允许上传的文
					String file_name = file.getOriginalFilename();
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					// String filePosition = "WEB-INF/uploadedfiles/" +
					// file_type+ "/"+ file_name;
					String filePosition = file_type + "/" + file_type + "-"
							+ time + "_" + file_name;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					// String file_path = project_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					file.transferTo(new File(file_path));
					String file_id = Common.returnUUID();
					Files fil = new Files();
					fil.setFile_size(filesize);
					Timestamp d = new Timestamp(System.currentTimeMillis());
					fil.setUpload_time(d);
					fil.setId(file_id);
					fil.setFile_type(file_type);
					fil.setFile_name(file_name);
					fil.setPosition(filePosition);
					fil.setStu_id(Common.getOneTeaId(session));
					filesService.insert(fil);
					directRecord.setFile_id(file_id);
					directRecord.setId(file_id);

				}
			}
			// 无论有没有文件，都保存实习总结
			directRecord.setId(Common.returnUUID());
			directRecord.setPractice_id(practice_id);
			directRecord.setTitle(title);
			directRecord.setTemp1("1");// 工作总结类型
			directRecord.setTemp2(temp2);// 指导结束时间
			directRecord.setCreate_time(DateService.getNowTimeTimestamp());// 创建时间
			directRecord.setDirect_place(direct_place);
			if (direct_time == null) {
				direct_time = "2010-01-01";
			}
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			try {
				ts = new Timestamp(format1.parse(direct_time).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}// 时间类型可以封装
			directRecord.setDirect_time(ts);
			directRecord.setOrg_id(org_id);
			directRecord.setStu_id(stu_ids);
			directRecord.setDirect_desc(direct_desc);
			directRecordService.insert(directRecord);

		}
		return "redirect:teacherSummaryList.do";
	}

	/**
	 * 功能：老师修改工作总结 BY wjg 2015-10-10
	 * 
	 * */
	@RequestMapping("teacher/editteacherSummary.do")
	public String editteacherSummary(ModelMap modelMap, String id) {
		DirectRecord d = (DirectRecord) directRecordService.selectByID(id);
		modelMap.put("directrecord", d);
		return "teacher/teacherSummaryEdit";
	}

	/**
	 * 功能：老师编辑 记录 by wjg 2015-10-10
	 * */
	@RequestMapping("teacher/doEditTeacehrSummary.do")
	public String doEditTeacehrSummary(
			@ModelAttribute("directrecord") DirectRecord directrecord)
			throws IOException {
		directRecordService.update(directrecord);
		return "redirect:teacherSummaryList.do";
	}

	/**
	 * 功能：老师删除记录 by wjg 2015-10-10
	 * 
	 * */
	@RequestMapping("teacher/deleteTeacherSummary.do")
	public String deleteTeacherSummary(String id, HttpServletRequest request) {
		DirectRecord d = (DirectRecord) directRecordService.selectById(id);
		String file_id = d.getFile_id();
		if (file_id != null) {
			filesService.deleteFile(file_id, request);
		}
		directRecordService.delete(id);
		return "redirect:teacherSummaryList.do";
	}

	/**
	 * 企业老师管理 根据任务id得到分组学生信息
	 * 
	 * @author 贾建昶
	 * @createDate 2015-11-21
	 * @since 6.0
	 */
	@RequestMapping("teacher/ComTeaManageByPraId.do")
	public String comTeaManageByPraId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		response.setCharacterEncoding("utf-8");// 设置字符编码
		String practice_id = request.getParameter("practice_id");// 获得传入的数据
		String stuInfo_grade = request.getParameter("year");// 获得传入的数据
		// 截取字符串
		/*
		 * int a = practice_idAndYear.indexOf("-"); String practice_id =
		 * practice_idAndYear.substring(0, a); String stuInfo_grade =
		 * practice_idAndYear.substring(a + 1,practice_idAndYear.length());
		 */
		// 将值保存到session中
		session.setAttribute("stuList_practiceId", practice_id);
		session.setAttribute("stulist_grade", stuInfo_grade);
		// 获得成员 GroupMembers 分组成员表
		List<GroupMembers> SuList = this.groupMembersService
				.studentsList(practice_id);

		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (GroupMembers gm : SuList) {
			Student stu = DictionaryService.findStudent(gm.getUser_id());// 学生对象
			String com_id = "";// 企业ID
			String getCom_name = ""; // 企业名称
			String getTrue_name = "";// 企业老师
			String getPhone = "";// 企业老师电话
			String getState = "";// 状态

			// 获得企业ID
			if (DictionaryService.findTeacher(stu.getCom_teacher_id()) != null) {
				com_id = DictionaryService.findTeacher(stu.getCom_teacher_id())
						.getDept_id();
			}
			// 获得企业名称，如果没有设为 ”无“
			if (DictionaryService.findCompany(com_id) != null) {
				getCom_name = DictionaryService.findCompany(com_id)
						.getCom_name();
			} else {
				getCom_name = "无";
			}
			// 获得企业老师的名字，如没有设为”无“
			if (DictionaryService.findTeacher(stu.getCom_teacher_id()) != null) {
				getTrue_name = DictionaryService.findTeacher(
						stu.getCom_teacher_id()).getTrue_name();
			} else {
				getTrue_name = "无";
			}
			// 获得企业老师的手机号，如没有设为”无“
			if (DictionaryService.findTeacher(stu.getCom_teacher_id()) != null) {
				getPhone = DictionaryService.findTeacher(
						stu.getCom_teacher_id()).getPhone();
			} else {
				getPhone = "无";
			}
			// 获得当前状态。
			String sta = "";
			if (DictionaryService.findTeacher(stu.getCom_teacher_id()) != null) {
				sta = DictionaryService.findTeacher(stu.getCom_teacher_id())
						.getState();
				if (sta.equals("3")) {
					getState = "未审核";
				}
				if (sta.equals("1")) {
					getState = "已审核";
				}
				if (sta.equals("2")) {
					getState = "未通过";
				}
			} else {
				getState = "暂无状态";

			}

			sb.append("<tr>");
			sb.append("	<td align='center'>" + (i++) + "</td>");
			sb.append("	<td align='center'>" + stu.getStu_code() + "</td>");
			sb.append("	<td align='center'>" + stu.getTrue_name() + "</td>");
			sb.append("	<td align='center'>" + getCom_name + "</td>");
			sb.append("	<td align='center'>" + getTrue_name + "</td>");
			sb.append("	<td align='center'>" + getPhone + "</td>");
			sb.append("	<td align='center'>" + getState + "</td>");
			sb.append("<td><button onclick=\"check('" + getState + "','"
					+ stu.getCom_teacher_id() + "','" + stu.getStu_code()
					+ "')\">审核</button></td>");
			sb.append("<td><input type='button' value='查看企业评价' onclick=\"change('"
					+ stu.getStu_code() + "')\"></td> ");
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
	 * 根据角色id获得角色对应的菜单资源
	 * 
	 * @Description
	 * @author 吴宝
	 * @param session
	 *            获得角色id
	 */

	@RequestMapping("teacher/menuByUserAjax.do")
	public void menuByUserAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String srm_role_id = (String) session
				.getAttribute("current_role_selected");// 获得角色id
		List<SysMenu> listall = sysMenuService.getAllListMenu(srm_role_id);// 获得所有子菜单
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		UserRole userRole = new UserRole();
		userRole.setUser_id(tea.getId());
		userRole.setRole_id(srm_role_id);
		userRole = (UserRole) userRoleService.selectList(userRole).get(0);// 获得用户自定义菜单
		String custom_menu_ids = userRole.getCustom_menu_ids();
		StringBuffer sb = CustomMenuForm.menuResourceForm(custom_menu_ids,
				listall);// 获得拼写的表格
		response.getWriter().println(sb.toString());
		response.getWriter().close();
	}

	/**
	 * 保存用户设置的快速导航菜单
	 * 
	 * @Description
	 * @author 吴宝
	 */

	@RequestMapping("teacher/updateCustomMenuAjax.do")
	public void updateCustomMenuAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("UTF-8");
		String[] csysMenu = request.getParameterValues("CsysMenu");
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
	 * 显示用户自定义菜单
	 * 
	 * @Description
	 * @author 吴宝
	 */

	@RequestMapping("teacher/updateCustomMenuShow.do")
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

		StringBuffer sb = CustomMenuForm.saveMenuForm(custom_menu_ids);
		if (sb == null) {
			return;
		}
		response.getWriter().println(sb.toString());
		response.getWriter().close();
	}

	/**
	 * 学生会社团管理 列表 李秋林 师杰 2016-3-12
	 */
	@RequestMapping("teacher/associationList.do")
	public ModelAndView associationList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		return new ModelAndView("/admin/associationList", map);
	}

	/**
	 * 功能：查询所有的学生会社团列表 by 师杰 李秋林 2016-3-12
	 * */
	@RequestMapping("teacher/ajaxGetAssociation.do")
	public String associationAjaxGet(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {

		response.setCharacterEncoding("UTF-8");// 设置编码格式为utf-8
		String dept = request.getParameter("dept");// 选择部门 社团或者学生会
		String xueyuan_id = (String) session.getAttribute("college_id");// 获取当前学院id
		session.setAttribute("Ass_association", dept);
		List<Association> associationlist = null;// 初始化列表为空
		Association asso = new Association();
		asso.setSa_category(dept);// 将社团或者学生会赋值给数据库中的部门信息
		asso.setSa_college_id(xueyuan_id);// 将学院id赋值给数据库中的学院id
		if (dept != null) {// 如果部门信息不为空
			if (dept.equalsIgnoreCase("1"))
				associationlist = associationService.SelectBySaCategory(asso);// 如果部门信息为
																				// 1
																				// 查出所有学生会信息

			if (dept.equalsIgnoreCase("2"))
				associationlist = associationService.SelectBySaCategory(asso);// 如果部门信息为
																				// 2
																				// 查出所有社团信息

			StringBuffer sb = new StringBuffer();// 数据缓冲池
			if (associationlist != null) {// 如果列表不为空

				for (Association ass : associationlist) {// 循环遍历学生会列表
					sb.append("<tr>");// 赋值
					sb.append("<td>" + ass.getSa_code() + "</td>");
					sb.append("<td>" + ass.getSa_name() + "</td>");

					if (ass.getSa_level().equalsIgnoreCase("1")) {
						sb.append("	<td>" + "校级" + "</td>");
					} else if (ass.getSa_level().equalsIgnoreCase("2")) {
						sb.append("	<td>" + "院级" + "</td>");
					} else {
						sb.append("	<td>" + "系级" + "</td>");
					}

					try {
						sb.append("	<td>"
								+ DictionaryService.findTeacher(
										ass.getSa_tea_id()).getTrue_name()
								+ "</td>");
					} catch (Exception e) {
						// TODO: handle exception
						sb.append("	<td>" + "无" + "</td>");
					}

					try {
						sb.append("	<td>"
								+ DictionaryService.findStudent(
										ass.getSa_stu_id()).getTrue_name()
								+ "</td>");
					} catch (Exception e) {
						// TODO: handle exception
						sb.append("	<td>" + "无" + "</td>");
					}

					sb.append("<td>" + ass.getSa_describe() + "</td>");
					try {
						sb.append("	<td>"
								+ DictionaryService.findOrg(
										ass.getSa_parent_id()).getOrg_name()
								+ "</td>");
					} catch (Exception e) {
						// TODO: handle exception
						sb.append("	<td>" + "无" + "</td>");
					}
					sb.append("<td></td>");
					sb.append("<td><input id='"
							+ ass.getId()
							+ "' type='button' onclick='editAss(this)' value='修改'/><button>删除</button></td>");

				}
			}

			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * 功能：管理员添加组织 添加页面sql 2016-3-12 李秋林、师杰
	 * */
	@RequestMapping("teacher/addAssociation.do")
	public String associationAdd(HttpSession session, ModelMap modelMap) {

		String college_id = (String) session.getAttribute("college_id");

		List<Org> Org_NameList = orgService.selectByXueYuanId(college_id);
		List<Org> departmentlist;
		Association a = associationService.selectMaxCode(college_id);
		String sa_code = "";
		if (a == null) {
			sa_code = college_id + "0001";
		} else {
			String s = a.getSa_code();
			String num = s.substring(s.length() - 4, s.length());
			int i = Integer.parseInt(num) + 1;
			DecimalFormat decimalFormat = new DecimalFormat("0000");
			String l = decimalFormat.format(i);
			sa_code = college_id + l;
		}
		departmentlist = orgService.selectXiOrg_Name(college_id);// 只获取系的id
		modelMap.put("sa_code", sa_code);
		modelMap.put("Org_Name", Org_NameList);
		modelMap.put("departmentlist", departmentlist);

		return "admin/associationAdd";
	}

	/**
	 * 添加学生会社团 师杰 李秋林 2016-03-15
	 */
	@RequestMapping("teacher/doAddAss.do")
	public String assSave(HttpServletRequest request, HttpSession session)
			throws IOException {
		String college_id1 = (String) session.getAttribute("college_id");
		String sa_code = request.getParameter("sa_code").trim();
		String sa_name = request.getParameter("sa_name");
		sa_name = new String(sa_name.getBytes("ISO-8859-1"), "UTF-8");
		String sa_category = request.getParameter("sa_category").trim();
		String contacts = request.getParameter("contacts");
		String description = request.getParameter("description");
		description = new String(description.getBytes("ISO-8859-1"), "UTF-8");
		String college_id = request.getParameter("college_id");

		if (contacts == null) {
			contacts = "无";
		}

		Teacher teacher = (Teacher) session.getAttribute("current_user");// 获取当前用户
		String sa_create_user = teacher.getId();

		Timestamp sa_create_time = new Timestamp(System.currentTimeMillis());

		Association ass = new Association();
		ass.setId(Common.returnUUID());
		ass.setSa_create_time(sa_create_time);
		ass.setSa_code(sa_code);
		ass.setSa_name(sa_name);
		ass.setSa_level("2");
		ass.setSa_category(sa_category);
		ass.setSa_college_id(college_id);
		ass.setSa_tea_id(contacts);

		ass.setSa_describe(description);
		ass.setSa_create_user(sa_create_user);
		ass.setState("1");
		ass.setSa_parent_id(college_id1);
		ass.setSa_last_edit_user(sa_create_user);
		associationService.insert(ass);

		return "redirect:associationList.do";
	}

	/**
	 * 功能：管理员修改学生会社团 师杰 李秋林 2016-03-16
	 * */
	@RequestMapping("teacher/editAsso.do")
	public String editAsso(ModelMap modelMap, HttpServletRequest request,
			HttpSession session) {
		String id = request.getParameter("id");
		String college_id1 = (String) session.getAttribute("college_id");

		Association ass = associationService.selectById1(id);
		Teacher t = (Teacher) teacherService.selectByID(ass.getSa_tea_id());
		String tea_name = t.getTrue_name();
		List<Org> Org_NameList = orgService.selectByXueYuanId(college_id1);
		modelMap.put("id", id);
		modelMap.put("Org_Name", Org_NameList);
		modelMap.put("tea_name", tea_name);
		modelMap.put("ass", ass);
		return "admin/associationEdit";
	}

	/**
	 * 功能：管理员修改学生会社团 实现 师杰 李秋林 2016-03-18
	 * */

	@RequestMapping("teacher/doEditSubmit.do")
	public String doEditSubmit(HttpServletRequest request, HttpSession session)
			throws IOException {
		String id = request.getParameter("id").trim();
		String sa_name = new String(request.getParameter("sa_name").getBytes(
				"ISO-8859-1"), "UTF-8");
		String sa_category = request.getParameter("sa_category").trim();
		String contacts = request.getParameter("contacts");
		String description = request.getParameter("description");
		description = new String(description.getBytes("ISO-8859-1"), "UTF-8");
		if (contacts == null) {
			contacts = "无";
		}
		Teacher teacher = (Teacher) session.getAttribute("current_user");// 获取当前用户
		String Sa_last_edit_user = teacher.getId();

		Timestamp sa_last_edit_time = new Timestamp(System.currentTimeMillis());

		Association ass = associationService.selectById1(id);

		ass.setSa_level("2");
		ass.setSa_category(sa_category);
		ass.setSa_tea_id(contacts);
		ass.setSa_describe(description);
		ass.setSa_last_edit_user(Sa_last_edit_user);
		ass.setSa_last_edit_time(sa_last_edit_time);
		ass.setSa_name(sa_name);
		associationService.update(ass);

		return "redirect:associationList.do";
	}
}
