package com.sict.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sict.dao.FilesDao;
import com.sict.dao.GroupMembersDao;
import com.sict.dao.GroupsDao;
import com.sict.dao.PracticeTaskDao;
import com.sict.entity.Company;
import com.sict.entity.Evaluate;
import com.sict.entity.Files;
import com.sict.entity.InfoCheckRecord;
import com.sict.entity.Knowledge;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.RecruitInfo;
import com.sict.entity.Score;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.service.CompanyService;
import com.sict.service.DictionaryService;
import com.sict.service.EvaluateService;
import com.sict.service.FilesService;
import com.sict.service.InfoCheckRecordService;
import com.sict.service.KnowledgeService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RecruitInfoService;
import com.sict.service.ScoreService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.service.WeixinService;
import com.sict.util.Common;
import com.sict.util.DateService;
import com.sict.util.HttpRequest;

/*
 * 功能：手机端学生控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class MobileStudentController {
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

	@Resource
	PracticeTaskDao practiceTaskDao;
	@Resource
	GroupsDao userGroupsDao;
	@Resource
	GroupMembersDao groupMembersDao;
	@Resource
	FilesDao filesDao;

	/**
	 * 注入studentService by周睿20160113 *
	 */
	@Resource(name = "studentService")
	private StudentService studentService;
	/**
	 * 注入evaluateService by周睿20150120 *
	 */
	@Resource(name = "evaluateService")
	private EvaluateService evaluateService;

	/**
	 * 注入PracticeTaskService by 张向杨 2016-03-03
	 */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;

	/**
	 * 注入recruitInfoService by 张向杨 2016-03-04 *
	 */
	@Resource(name = "recruitInfoService")
	private RecruitInfoService recruitInfoService;
	/**
	 * 注入ScoreService by宋浩20160308*
	 * 
	 * */
	@Resource(name = "ScoreService")
	private ScoreService scoreService;
	/**
	 * 注入filesService by 贾建昶 2016-03-04 *
	 */
	@Resource(name = "filesService")
	private FilesService filesService;
	/**
	 * 注入knowledgeService by刘喜宝20160307 *
	 */
	@Resource(name = "knowledgeService")
	private KnowledgeService knowledgeService;
	/**
	 * 注入orgService by刘喜宝20160307 *
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * 注入practiceRecordService by syj 20160308
	 */
	@Resource(name = "practiceRecordService")
	private PracticeRecordService practiceRecordService;
	/**
	 * 注入CheckInfoService by杨梦肖20160303 *
	 */
	@Resource(name = "checkInfoService")
	private InfoCheckRecordService checkInfoService;
	/**
	 * 注入teacherService by杨梦肖20160303 *
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入weixinService by张邦卿20160303 *
	 */
	@Resource(name = "weixinService")
	private WeixinService weixinService;

	/**
	 * 注入noticeService by李泽20160307 *
	 */
	@Resource(name = "noticeService")
	private NoticeService noticeService;
	/**
	 * 注入companyService by师杰20160310 *
	 */
	@Resource(name = "companyService")
	private CompanyService companyService;

	/**
	 * 功能：显示学生主页 by周睿20160314 显示指导教师通知和奖惩信息
	 * */
	@RequestMapping("MobileStudent/index.do")
	public ModelAndView toIndex(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String college_id = DictionaryService.findOrg(
				DictionaryService.findOrg(stu.getClass_id()).getParent_id())
				.getParent_id(); // 根据学生id查出学院id
		List<Notice> noticeList = noticeService.stuGetMyNotice(college_id,
				stu.getId()); // 根据学院id与学生id查找出学生的通知公告列表
		List<Notice> noticeFive = new ArrayList<Notice>();

		int i = 0;
		for (Notice n : noticeList) {
			if (i < 5) {
				noticeFive.add(n);
				i++;
			}
		}
		List<Evaluate> evaList = evaluateService.ListByStu_Id(stu.getId());
		List<Evaluate> evaFive = new ArrayList<Evaluate>();
		String teaId = new String();
		int b = 0;
		for (Evaluate e : evaList) {
			if (b < 5) {
				evaFive.add(e);
				teaId = e.getTea_id();
				b++;
			}
		}
		Teacher tea = (Teacher) teacherService.selectByID(teaId);
		map.put("tea", tea);
		map.put("noticeFive", noticeFive);
		map.put("evaFive", evaFive);

		return new ModelAndView("/mobileViews/mobileStudent/index", map);
	}

	@RequestMapping("MobileStudent/DoHomework.do")
	public String toDoHomework(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/mobileViews/mobileStudent/DoHomework";
	}

	@RequestMapping("MobileStudent/LearnKnowledge.do")
	public String toLearnKnowledge(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileStudent/LearnKnowledge";
	}

	@RequestMapping("MobileStudent/LookInformation.do")
	public String toLookInformation(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileStudent/LookInformation";
	}

	@RequestMapping("MobileStudent/PlInformation.do")
	public String PInformation(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/mobileViews/mobileStudent/PInformation";
	}

	@RequestMapping("MobileStudent/Help.do")
	public String Help(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		return "/mobileViews/mobileStudent/Help";
	}

	/**
	 * 功能：显示学生信息 by周睿20160113
	 * 
	 * */
	@RequestMapping("MobileStudent/PersonalData.do")
	public ModelAndView toPersonalData(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		map.put("stu", stu);

		return new ModelAndView("/mobileViews/mobileStudent/PersonalData", map);
	}

	/**
	 * 功能：修改学生信息 by周睿20160113
	 * 
	 * */
	@RequestMapping("MobileStudent/doEidtInfo.do")
	public String doEidtInfo(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		String qqnum = (String) request.getParameter("qqnum");
		String phone = (String) request.getParameter("phone");
		String email = (String) request.getParameter("email");
		String home_addr = (String) request.getParameter("home_addr");
		String home_phone = (String) request.getParameter("home_phone");
		Student stu = (Student) session.getAttribute("current_user");
		stu.setQqnum(qqnum);
		stu.setPhone(phone);
		stu.setEmail(email);
		stu.setHome_addr(home_addr);
		stu.setHome_phone(home_phone);
		studentService.update(stu);

		return "/mobileViews/mobileStudent/AddSuccess";
	}

	@RequestMapping("MobileStudent/CommonProblems.do")
	public String toCommonProblems(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileStudent/CommonProblems";
	}

	/**
	 * 功能：显示学生公告信息 by李泽20160307
	 * 
	 * */
	@RequestMapping("MobileStudent/Announcements.do")
	public String toAnnouncements(ModelMap modelMap, HttpSession session)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		String college_id = DictionaryService.findOrg(
				DictionaryService.findOrg(stu.getClass_id()).getParent_id())
				.getParent_id(); // 根据学生id查出学院id
		List<Notice> noticeList = noticeService.stuGetMyNotice(college_id,
				stu.getId()); // 根据学院id与学生id查找出学生的通知公告列表
		modelMap.put("noticeList", noticeList);
		return "/mobileViews/mobileStudent/Announcements";
	}

	/**
	 * 功能：查看一条公告的详细内容 by李泽20160310
	 * 
	 * */
	@RequestMapping("MobileStudent/Announcements_details.do")
	public String toAnnouncements_details(HttpServletRequest request,
			ModelMap modelMap) throws IOException, ServletException {
		String Announcements_id = request.getParameter("id"); // 接收前台传过来的通知公告ID
		Notice notice = (Notice) noticeService.selectByID(Announcements_id); // 根据通知公告ID查找出通知公告
		modelMap.put("notice", notice);
		return "/mobileViews/mobileStudent/Announcements_details";
	}

	/*
	 * @function：查询招聘信息列表
	 * 
	 * @author：丁乐晓
	 * 
	 * @time ： 2016-03-04
	 */
	@RequestMapping("MobileStudent/RecruitmentInfo.do")
	public String toRecruitmentInfo(ModelMap modelMap, HttpSession session)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		String stuId = stu.getId();
		String college_id = Common.getCollegeByStuID(stuId);
		RecruitInfo ri = new RecruitInfo();
		ri.setCollege_id(college_id);
		ri.setState("1");

		List<RecruitInfo> recruitInfoList = this.recruitInfoService
				.selectList(ri);
		modelMap.put("recruitInfoList", recruitInfoList);
		return "/mobileViews/mobileStudent/RecruitmentInformationList";
	}

	@RequestMapping("MobileStudent/recrDetailInfo.do")
	public String toElect_Info(String id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RecruitInfo details = null;
		Company company = null;
		String comContact = null;
		if (id != null)
			details = (RecruitInfo) recruitInfoService.selectByID(id);
		if (details != null) {
			company = DictionaryService.findCompany(details.getCom_id());
			comContact = company.getContacts();
		}
		modelMap.put("details", details);
		modelMap.put("company", company);
		modelMap.put("comContact", comContact);
		return "/mobileViews/mobileStudent/Elect_Info";
	}

	@RequestMapping("MobileStudent/PersonalInformation.do")
	public String toPersonalInformation(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileStudent/PersonalInformation";
	}

	/**
	 * 功能：判断密码是否匹配 by jiajianchang 20151218
	 * 
	 * @throws IOException
	 * 
	 * */
	@RequestMapping("MobileStudent/ajax_check_login_pass_student.do")
	public ModelAndView ajax_check_login_pass_student(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 获取当前用户
		String login_pass_old = request.getParameter("login_pass_old");
		Student stu = (Student) session.getAttribute("current_user");
		String stare = "false";
		if (!stu.getLogin_pass().equalsIgnoreCase(login_pass_old)) {
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
	 * 功能：显示修改密码页面 by周睿20160302
	 * 
	 * */
	@RequestMapping("MobileStudent/ChangePassword.do")
	public ModelAndView toChangePassword(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		map.put("stu", stu);
		return new ModelAndView("/mobileViews/mobileStudent/ChangePassword",
				map);
	}

	/**
	 * 功能：修改密码by周睿20160302
	 * 
	 * */
	@RequestMapping("MobileStudent/doChangePassword.do")
	public String doChangePassword(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		String password = request.getParameter("login_pass_new_two");
		stu.setLogin_pass(password);
		studentService.update(stu);
		session.removeAttribute("current_user");
		return "/mobileViews/mobileStudent/ChangeSuccess";

	}

	/**
	 * 功能：显示奖惩信息 by周睿20160120
	 * 
	 * */
	@RequestMapping("MobileStudent/myRewardPunishment.do")
	public ModelAndView tomyRewardPunishment(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		List<Evaluate> evs = evaluateService.ListByStu_Id(stu.getId());
		map.put("evs", evs);
		map.put("stu", stu);
		return new ModelAndView(
				"/mobileViews/mobileStudent/myRewardPunishment", map);
	}

	/**
	 * 功能：显示奖惩信息详细信息 by周睿20160120
	 * 
	 * */
	@RequestMapping("MobileStudent/myRewardPunishment_details.do")
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
				"/mobileViews/mobileStudent/myRewardPunishment_details", map);
	}

	/*
	 * function：学生端 查询自己的实习列表 author： 张向杨 time： 2016-03-03
	 */
	@RequestMapping("MobileStudent/taskQuery.do")
	public String totaskQuery(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, String taskType) throws IOException,
			ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		List<PracticeTask> praList = practiceTaskService.getTaskByStuIdAndType(
				stu_id, taskType);
		modelMap.put("praTask_List", praList);
		return "/mobileViews/mobileStudent/taskQuery";
	}

	/*
	 * function: 详细显示一条pricticeTask的记录 author： 张向杨 time ： 2016-03-03
	 */
	@RequestMapping("MobileStudent/taskQueryDetails.do")
	public String totaskQueryDetails(ModelMap modelMap, String id)
			throws IOException, ServletException {
		PracticeTask pt = null;
		if (id != null)
			pt = practiceTaskService.selectByID(id);

		String teaName = DictionaryService.findTeacher(pt.getTea_id())
				.getTrue_name();
		String taskType = null;
		// 查找数据库出来的就是校外实习 所以这里没有做其他判断
		if (pt.getTask_type().equals("1")) {
			taskType = "校外实习";
		}
		modelMap.put("p", pt);
		modelMap.put("teaName", teaName);
		modelMap.put("taskType", taskType);
		return "/mobileViews/mobileStudent/taskQueryDetails";
	}

	// 学生上传实习照片
	// 贾建昶
	// 二〇一六年三月五日

	@RequestMapping("MobileStudent/ShowPhoto.do")
	public ModelAndView toUpPhoto(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		List<Files> myPhotos = this.filesService.selectStuPhotoByStu_id(stu
				.getId());
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String month1 = "";
		if (month < 10) {
			month1 = "0" + String.valueOf(month);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myPhotos", myPhotos);
		map.put("month", month1);
		return new ModelAndView("/mobileViews/mobileStudent/ShowPhoto", map);
	}

	// 学生上传实习照片
	// 贾建昶
	// 二〇一六年三月五日
	@RequestMapping("MobileStudent/UpPhoto.do")
	public ModelAndView toUpPhoto(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();

		return new ModelAndView("/mobileViews/mobileStudent/UpPhoto", map);
	}

	@RequestMapping("MobileStudent/doAddPhoto.do")
	public String doAddPhoto(HttpSession session, HttpServletRequest request)
			throws IllegalStateException, IOException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String photoMonth = request.getParameter("photoMonth");
		MultipartFile file = null;
		try {
			file = Common.getUpliadFile(request);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch bloc
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		if (!file.isEmpty()) {
			int countFiles = 1;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			Timestamp time1 = new Timestamp(System.currentTimeMillis());
			List<Files> filess = filesDao.selectStuStu_id(stu_id, time1);
			String fromUserName = "";
			if (stu.getWx_code() != null) {
				fromUserName = stu.getWx_code();
			} else {
				fromUserName = stu.getStu_code();
			}
			if (filess.size() != 0) {
				for (int i = 0; i < filess.size(); i++) {
					if (filess.get(i).getFile_name().substring(0, 7)
							.equalsIgnoreCase((year + "-" + photoMonth))) {
						countFiles++;
					}
				}
			}
			String fileNames = "/" + year + "-" + photoMonth + "00"
					+ countFiles + fromUserName + ".jpg";
			String project_path = request.getSession().getServletContext()
					.getRealPath("/");
			String fileurl = project_path + "/uploadedfiles/Photos";
			String postion = fileurl + fileNames;
			float filesize = file.getSize();
			String fileName = year + "-" + photoMonth + "00" + countFiles
					+ fromUserName + ".jpg";
			file.transferTo(new File(postion));
			// 文件的属性
			String file_id = Common.returnUUID();
			Files fil = new Files();
			fil.setId(file_id);
			fil.setFile_size(filesize);
			fil.setStu_id(stu_id);
			fil.setUpload_time(DateService.getNowTimeTimestamp());
			fil.setFile_type("4");
			fil.setFile_name(fileName);
			fil.setPosition(postion);
			filesService.insert(fil);

		}
		return "redirect:ShowPhoto.do";
	}

	/**
	 * 学生成绩查询by宋浩20160308
	 */
	@RequestMapping("MobileStudent/ResultsQuery.do")
	public String toResultsQuery(ModelMap modelMap, HttpSession session)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");

		// 通过学生id选出实习任务
		List<PracticeTask> pracTask = practiceTaskDao
				.selectPracticeTasksBypt(stu.getId());
		if (pracTask.size() != 0) {
			for (int i = 0; i < pracTask.size(); i++) {
				String group_id = userGroupsDao.getGroupByPracIdAndStuId(
						pracTask.get(i).getId(), stu.getId()).getId();
				// 取得组员
				List<String> gm = groupMembersDao
						.selectGroupMembersStuIdByGroupId(group_id);
				int groupsmember = gm.size();// 分组人数
				// 本人成绩
				Score myScore = scoreService.getStuScore(pracTask.get(i)
						.getId(), stu.getId());
				double myAllscore = myScore.getScore();// 学生自己本身的
				Score otherStuScore;
				DecimalFormat df = new DecimalFormat("###.##");
				// 比较，得到名次
				int sort = 0;
				for (int g = 0; g < gm.size(); g++) {
					// 小组成员成绩，包含本人
					otherStuScore = scoreService.getStuScore(pracTask.get(i)
							.getId(), gm.get(g));
					double otherAllscores = otherStuScore.getScore();// 其他学生的成绩
					if (myAllscore <= otherAllscores) {// 判断
						sort++;
					}
				}
				String ev = df.format(myScore.getEvaScore());
				String theis = df.format(myScore.getTheScore());
				String month = df.format(myScore.getMouthScore());
				String allScore = df.format(myAllscore);
				if (ev.equals(".0")) {
					ev = "0分";
				}
				if (theis.equals(".0")) {
					theis = "0分";
				}
				if (month.equals(".0")) {
					month = "0分";
				}
				if (allScore.equals(".0")) {
					allScore = "0";
				}
				if (df.format(pracTask.get(i).getWeight_evaluate())
						.equals(".0")) {
					ev = "奖惩权重未生成";
				} else if (df.format(pracTask.get(i).getWeight_summary())
						.equals(".0")) {
					theis = "总结权重未生成";
				} else if (df.format(pracTask.get(i).getWeight_thesis())
						.equals(".0")) {
					month = "论文权重未生成";
				}
				modelMap.put("practiceTaskName", DictionaryService
						.findPracticeTask(pracTask.get(i).getId())
						.getTask_name());

				modelMap.put("Weight_summary", pracTask.get(i)
						.getWeight_summary() * 100);
				modelMap.put("Weight_evaluate", pracTask.get(i)
						.getWeight_evaluate() * 100);

				modelMap.put("Weight_thesis", pracTask.get(i)
						.getWeight_thesis() * 100);
				if (month.equals(".0") || ev.equals(".0") || theis.equals(".0")) {
					modelMap.put("totalScore", "0");
					modelMap.put("groupsmember", groupsmember);
					modelMap.put("mingci", groupsmember);
				} else {
					modelMap.put("totalScore", allScore);
					modelMap.put("groupsmember", groupsmember);
					modelMap.put("mingci", sort);
				}
			}

		}

		return "/mobileViews/mobileStudent/ResultsQuery";
	}

	/**
	 * 功能：通过问题得到常见问题 by lxb 20160307
	 */
	@RequestMapping("MobileStudent/getAnswer.do")
	public String getAnswer(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String question = request.getParameter("question");// 获取任务name
		Student stu = (Student) session.getAttribute("current_user");

		Org org = orgService.selectByID(stu.getClass_id());
		String science_id = org.getParent_id();

		Org science = orgService.selectByID(science_id);

		String college_Id = science.getParent_id();

		List<Knowledge> konwledges = knowledgeService.byWord(college_Id,
				question);
		StringBuffer sb = new StringBuffer();
		int i = 0;

		if (konwledges.size() > 0) {
			for (Knowledge k : konwledges) {
				sb.append("<ul><li>" + k.getQuestion() + "</li><li>"
						+ k.getAnswer() + "</li></ul><br/>");
				i++;
			}
		} else {

			sb.append("<span style='color:red'>暂无查询内容</span>");

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
	 * @function 学生手机端--查询申请（实习）
	 * @time 2016-03-08 by lql、syj
	 * 
	 */
	@RequestMapping("MobileStudent/queryApplication.do")
	public String toqueryApplication(ModelMap modelMap, HttpSession session)
			throws IOException, ServletException {
		// 获得系统当前登录用户
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		// 判断是不是有实习任务
		boolean flag = true;
		// 1.首先得到全部的实习记录申请列表
		PracticeRecord practiceRecord = new PracticeRecord();
		practiceRecord.setStu_id(stu_id);
		List<PracticeRecord> praAllList = this.practiceRecordService
				.selectList(practiceRecord);
		// 2.得到学生现在正在使用的(有效的)实习申请记录
		PracticeRecord pracavaliable = this.practiceRecordService
				.selectPracticeRecordByStuId(stu_id);
		if (pracavaliable == null) {
			flag = false;
			modelMap.put("flag", flag);
			return "/mobileViews/mobileStudent/moreApplication";
		}
		// 3.新创建一个集合来存放过时的申请记录
		List<PracticeRecord> outOfRecord = new ArrayList<PracticeRecord>();
		// 4.通过for循环遍历 学生所有的申请记录列表
		for (PracticeRecord pa : praAllList) {
			// 5.在循环里面判断的条件为 实习记录对象是否有实习结束时间
			if (pa.getDimission_time() != null) {
				// 6.将有实习时间的记录添加到新创建的集合中
				outOfRecord.add(pa);
			}
		}
		String state = null;
		if (pracavaliable.getCheck_state().equals("0")) {
			state = "待审核";
		} else if (pracavaliable.getCheck_state().equals("1")) {
			state = "通过";
		} else if (pracavaliable.getCheck_state().equals("2")) {
			state = "未通过";
		} else {
			state = "暂时没有数据";
		}

		// 将得到的集合传到前台对应的视图中
		modelMap.put("outOfRecord", outOfRecord);
		modelMap.put("pracavaliable", pracavaliable);
		modelMap.put("state", state);
		modelMap.put("flag", flag);

		return "/mobileViews/mobileStudent/moreApplication";
	}

	/**
	 * @function 学生手机端--查询申请详情（实习）
	 * @time 2016-03-08 by lql、syj
	 * 
	 */
	@RequestMapping("MobileStudent/moreApplication_details.do")
	public String tomoreApplication_details(ModelMap modelMap, String practiceId)
			throws IOException, ServletException {
		PracticeRecord prd = DictionaryService.findPracticeRecord(practiceId);
		String comName = DictionaryService.findCompany(prd.getCompany_id())
				.getCom_name();
		String state = null;
		String netSign = null;
		String contract = null;

		if (prd.getCheck_state().equals("0")) {
			state = "待审核";
		} else if (prd.getCheck_state().equals("1")) {
			state = "通过";
		} else if (prd.getCheck_state().equals("2")) {
			state = "未通过";
		} else {
			state = "暂时没有数据";
		}

		if (prd.getIs_netsign().equals("1")) {
			netSign = "是";
		} else {
			netSign = "否";
		}

		if (prd.getIs_contract().equals("1")) {
			contract = "是";
		} else {
			contract = "否";
		}
		modelMap.put("comName", comName);
		modelMap.put("prd", prd);
		modelMap.put("state", state);
		modelMap.put("netSign", netSign);
		modelMap.put("contract", contract);

		return "/mobileViews/mobileStudent/moreApplication_details";
	}

	/**
	 * 功能：信息核对 by杨梦肖20160303
	 * 
	 * */
	@RequestMapping("MobileStudent/InfoCheck.do")
	public String toInfoCheck(HttpSession session, ModelMap modelMap,
			String id, HttpServletRequest request) throws IOException,
			ServletException {
		// Map<String, Object> map = new HashMap<String, Object>();
		String checkinfo_Grade = Common.getDefaultYear();
		Student stu = (Student) session.getAttribute("current_user");
		String stu_code = stu.getStu_code();
		String stu_id = stu.getId();
		String tea_id = teacherService.selectTeaByStuId(stu_id);
		if (tea_id != null) {
			List<PracticeTask> checkinfo_pulltaskList = this.practiceTaskService
					.getPracticeTasksByGradeAndTea_id(tea_id, checkinfo_Grade);
			String checkinfo_pullTaskdesc = checkinfo_pulltaskList.get(0)
					.getTask_desc();
			String checkInfo_id = id;
			List<InfoCheckRecord> result = checkInfoService
					.selectCheckInfoByChecktask_id(checkInfo_id);
			String check_result;
			int i = 0;
			modelMap.put("infoid", id);
			session.setAttribute("stu_code", stu_code);
			session.setAttribute("checkinfo_pullTaskdesc",
					checkinfo_pullTaskdesc);
			session.setAttribute("checkInfo_taskList", checkinfo_pulltaskList);// 过滤条件任务下拉列表
			return "/mobileViews/mobileStudent/InfoCheck";
		} else {
			return "/mobileViews/mobileStudent/InfoCheck_error";
		}
	}

	/**
	 * 功能：信息核对 by杨梦肖20160303
	 * 
	 * */
	@RequestMapping("MobileStudent/saveInfoCheck.do")
	public String saveInfo(HttpServletRequest request, HttpSession session)
			throws IOException {
		// String checktask_id = request.getParameter("checkInfo_taskList");//
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String tea_id = teacherService.selectTeaByStuId(stu_id);
		String checkinfo_Grade = Common.getDefaultYear();
		List<PracticeTask> checkinfo_pulltaskList = this.practiceTaskService
				.getPracticeTasksByGradeAndTea_id(tea_id, checkinfo_Grade);
		String name = checkinfo_pulltaskList.get(0).getTask_name();
		String note = request.getParameter("note");
		String check_result = request.getParameter("check_result");// 核对结果

		PracticeTask pr = DictionaryService.findPracticeTaskByName(name);
		String pId = pr.getId();
		InfoCheckRecord in = DictionaryService.findInfoCheckRecordByPrd(pId);
		if (in != null) {
			in.setNote(note);
			in.setCheck_result(check_result);
			in.setStu_id(stu_id);
			checkInfoService.updateInfo(in);
			DictionaryService.updateInfoCheckRecord(in);
			return "/mobileViews/mobileStudent/AddSuccess";
		} else {
			return "/mobileViews/mobileStudent/AddSuccess";
		}

	}

	/**
	 * 功能：新增企业 by张邦卿20160310
	 * 
	 * */
	@RequestMapping("MobileStudent/saveCom.do")
	public String toExpertQuiz(ModelMap modelMap, HttpServletRequest request) {
		String stu_id = request.getParameter("stu_id");
		List<Org> college = weixinService.selectCollege();
		modelMap.put("college", college);
		modelMap.put("stu_id", stu_id);
		return "/mobileViews/mobileStudent/AddSuccess";
	}

	/**
	 * 功能：新增企业 by张邦卿20160310
	 * 
	 * */
	@RequestMapping("MobileStudent/NewEnterprise.do")
	public String toNewEnterprise(ModelMap modelMap, HttpSession session)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		Company company = new Company();
		String org_id = weixinService.selectCollegeBystu_id(stu.getClass_id());
		// 学院id存进使用范围
		company.setApplicable_scope(org_id);
		company.setCheck_state("0");
		// 行业范围直接在前台取key值
		modelMap.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		modelMap.put("company", company);
		return "/mobileViews/mobileStudent/NewEnterprise";
	}

	/**
	 * 功能：专家指导 by岳贤昌20160309
	 * 
	 * */
	@RequestMapping("MobileStudent/ExpertGuidance.do")
	public String toExpertGuidance(ModelMap modelMap,
			HttpServletRequest request, HttpSession session)
			throws IOException, ServletException {

		return "/mobileViews/mobileStudent/ExpertGuidance";
	}

	/**
	 * 功能：专家指导 by岳贤昌20160309
	 * 
	 * */
	@RequestMapping("MobileStudent/SaveExpertGuidance.do")
	public String SaveExpertGuidance(ModelMap modelMap,
			HttpServletRequest request, HttpSession session)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		// 获取学生的id
		String stu_id = stu.getId();
		// 专家的id
		String expert = request.getParameter("expert");
		// 学生输入的问题
		String quiz = request.getParameter("quiz");
		Date now = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp createQuestionTime = new Timestamp(System.currentTimeMillis());
		createQuestionTime = new Timestamp(now.getTime());
		Knowledge expertKnowledge = new Knowledge();
		expertKnowledge.setMessenger_id(stu_id);
		expertKnowledge.setAnswerer(expert);
		expertKnowledge.setQuestion(quiz);
		expertKnowledge.setQuestion_type("3");
		expertKnowledge.setScope("SDSZXY");
		expertKnowledge.setCreate_time(createQuestionTime);
		this.weixinService.saveExpertQuiz(expertKnowledge);
		return "/mobileViews/mobileStudent/AddSuccess";
	}

	/**
	 * 功能：ajax传递 专家 岳贤昌 2016年3月10日 10:26:47
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("MobileStudent/ajaxExpert.do")
	public String ajaxExpert(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		String expertise = request.getParameter("expertise");
		String trueExpertise = new String(expertise.getBytes("iso-8859-1"),
				"utf-8");
		List<Teacher> teachers = this.weixinService
				.getTeachersByExpertise(trueExpertise);
		StringBuffer sb = new StringBuffer();
		sb.append("<option value='选择专家'>请选择专家</option>");
		if (teachers.size() > 0) {
			for (Teacher t : teachers) {
				sb.append("<option " + "value=" + t.getId() + ">"
						+ t.getExpertise() + "---" + t.getTrue_name()
						+ "</option>");
			}
		} else {
			sb.append("<option " + "value=''>" + "未查询到相关专家" + "</option>");
		}

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：学生手机端---保存企业老师维护 20160312 by ymx\syj
	 * 
	 * 
	 */
	@RequestMapping("MobileStudent/saveComTeaccher.do")
	public String saveComTeaccher(HttpSession session,
			HttpServletRequest request) throws IOException, ServletException {
		String url = new String();
		Student student = (Student) session.getAttribute("current_user");
		String comTeacher = request.getParameter("comTeacher");
		// String stu_id = stu.getId();
		// Student student = DictionaryService.findStudent(stu_id);
		String com_id = request.getParameter("com_id");
		String com_code = "com_code";
		String comTeaherName = request.getParameter("name");
		String comTeacherSex = request.getParameter("sex");
		String comTeaherPhone = request.getParameter("phone");
		// 判断是新增还是选择企业老师 */
		if (comTeacher.equals("1") && comTeaherName == "") {
			System.out.println("什么也不操作！");
		} else if (comTeacher.equals("1") && comTeaherName != "") {
			// 新建企业老师
			Teacher newComTeacher = new Teacher();
			String tea_id = Common.returnUUID16();// 随机生成一个老师id
			newComTeacher.setId(tea_id);
			newComTeacher.setTrue_name(comTeaherName);
			newComTeacher.setDept_id(com_id);
			newComTeacher.setState("3");
			newComTeacher.setSex(comTeacherSex);
			newComTeacher.setPhone(comTeaherPhone);
			newComTeacher.setTea_code(com_code);
			newComTeacher.setTea_type("2");
			this.teacherService.insert(newComTeacher);
			student.setCom_teacher_id(tea_id);
			this.studentService.updatePersonal(student);
			url = "/mobileViews/mobileStudent/AddSuccess";
		} else {
			// 已有的企业老师
			student.setCom_teacher_id(comTeacher);
			this.studentService.updatePersonal(student);
			return "/mobileViews/mobileStudent/DoHomework";
		}
		return "/mobileViews/mobileStudent/DoHomework";
	}

	/**
	 * ajax传递数据 by 师杰 20160121
	 */
	@RequestMapping("MobileStudent/ajaxCompany.do")
	public String ajaxCompany(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		String companyName = request.getParameter("companyName");
		String trueCompanyName = new String(companyName.getBytes("iso-8859-1"),
				"utf-8");
		List<Company> pt = this.companyService.getSomeCompany(trueCompanyName);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='com_name' name='com_name'>");
		sb.append("<option value='1' >请选择实习单位</option>");
		for (Company com : pt) {
			sb.append("<option " + "value=" + com.getId() + ">"
					+ com.getCom_name() + "</option>");
		}
		if (pt.size() > 30) {
			sb.append("<option value='1'>您查询的内容匹配类型太多，只显示前三十条，请完善查询内容</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存实习申请 师杰
	 */

	@RequestMapping("MobileStudent/ajaxSubmitAppl.do")
	public String ajaxSubmitAppl(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		Timestamp d = new Timestamp(System.currentTimeMillis());
		String com_name = request.getParameter("com_name");
		String stu_id = request.getParameter("stu_id");
		String task_id = request.getParameter("task_id");
		String work_name = request.getParameter("work_name");
		work_name = new String(work_name.getBytes("ISO-8859-1"), "UTF-8");
		String is_net = "否";
		String is_con = "否";
		String deptAdmin = request.getParameter("deptAdmin");
		deptAdmin = new String(deptAdmin.getBytes("ISO-8859-1"), "UTF-8");
		String comPla = request.getParameter("comPla");
		comPla = new String(comPla.getBytes("ISO-8859-1"), "UTF-8");
		String workPla = request.getParameter("workPla");
		workPla = new String(workPla.getBytes("ISO-8859-1"), "UTF-8");
		PracticeRecord pr = new PracticeRecord();
		pr.setId(Common.returnUUID());// ID
		pr.setApply_time(d);// 申请时间
		pr.setPractice_id(task_id);// 实习id
		pr.setCompany_id(com_name);// ?获取不到ajax后option选项中value公司名称
		pr.setStu_id(stu_id); // 学生id
		pr.setPost_id(work_name);// 岗位名称
		pr.setIs_netsign(is_net);// 可否网签？获取不到
		pr.setIs_contract(is_con);// 可否签合同？获取不到
		pr.setLeader(deptAdmin);// 部门领导
		pr.setCom_orgion(comPla);// 公司地区
		pr.setWork_orgion(workPla);// 工作地区
		pr.setCom_teacher("");// ?没有企业指导教师
		pr.setCom_phone("");// ？没有企业指导教师手机号
		pr.setCheck_state("0");
		this.practiceRecordService.insert(pr);

		return null;
	}

	/**
	 * 添加实习申请 师杰 20160121
	 */
	@RequestMapping("MobileStudent/AddinternApply.do")
	public String AddinternApply(HttpRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		PracticeTask pt = (PracticeTask) session
				.getAttribute("current_practice");// 获取当前用户任务
		PracticeRecord pr = new PracticeRecord();// 新建一个申请
		pr.setStu_id(stu.getId());
		pr.setPractice_id(pt.getId());

		map.put("pr", pr);
		return "/mobileViews/mobileStudent/AddSuccess";
	}

	/**
	 * 功能：学生手机端---企业老师维护 20160312 by ymx\syj 空指针修改by ymx 2016、3、16
	 * 
	 */
	@RequestMapping("MobileStudent/FirmTeacherMaintain.do")
	public String toFirmTeacherMaintain(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String stu_name = DictionaryService.findStudent(stu_id).getTrue_name();// 获得学生的姓名

		PracticeRecord practiceRecord = this.practiceRecordService
				.getPrecordUndimission_time(stu_id);// 通过学生id获得一条有效的实习记录
		if (practiceRecord != null) {
			String com_id = practiceRecord.getCompany_id();// 获得企业id
			String com_name = DictionaryService.findCompany(com_id)
					.getCom_name();// 获得企业名称

			Teacher t = new Teacher();
			t.setDept_id(com_id);
			List<Teacher> comTeachers = this.teacherService.selectList(t);
			modelMap.put("comTeachers", comTeachers);
			modelMap.put("stu_name", stu_name);
			modelMap.put("com_id", com_id);
			modelMap.put("com_name", com_name);
			modelMap.put("stu_id", stu_id);
			return "/mobileViews/mobileStudent/FirmTeacherMaintain";
		} else {
			return "/mobileViews/mobileStudent/erro";
		}

	}

	/**
	 * 功能：实习申请 获取学生基本信息 by 师杰 20160120 一次更正 解决空指针
	 */
	@RequestMapping("MobileStudent/InternApply.do")
	public ModelAndView toInternApply(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();

		Student stu = (Student) session.getAttribute("current_user");// 获取当前用户
		String stu_id = stu.getId();
		String stu_code = stu.getStu_code();
		String task_type = "1";
		boolean check_state = true;
		List<PracticeTask> practice_task = this.practiceTaskService
				.getTaskByStuIdAndType(stu_id, task_type);
		PracticeRecord p = practiceRecordService.selectByStu_id(stu_id);
		if (p != null) {
			return new ModelAndView("/mobileViews/mobileStudent/erroApply", map);
		} else {
			check_state = false;
			map.put("check_state", check_state);
			map.put("stu_id", stu_id);
			map.put("stu_code", stu_code);
			map.put("practice_task", practice_task);
			return new ModelAndView("/mobileViews/mobileStudent/InternApply",
					map);
		}
	}

	/**
	 * 功能：实习记录 by周睿20160120
	 * 
	 * */
	@RequestMapping("MobileStudent/practiceRecord.do")
	public ModelAndView topracticeRecord(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		PracticeRecord pr = practiceRecordService
				.getPrecordUndimission_time(stu.getId());
		if (pr == null) {
			return new ModelAndView("/mobileViews/mobileStudent/erroRead", map);
		} else {
			map.put("stu", stu);
			map.put("PracticeRecord", pr);
			return new ModelAndView(
					"/mobileViews/mobileStudent/practiceRecord", map);
		}

	}

	/**
	 * 功能：实习记录 by周睿20160120
	 * 
	 * */
	@RequestMapping("MobileStudent/savePracticeState.do")
	public String savePracticeState(HttpServletRequest request,
			HttpSession session) throws IOException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		// String practicerecord = request.getParameter("practicerecord");
		// String stu_id = request.getParameter("stu_id");
		String work_time = request.getParameter("work_time");
		String prct_contract_time = request.getParameter("prct_contract_time");
		String netsign_time = request.getParameter("netsign_time");
		String contract_time = request.getParameter("contract_time");
		String dimission_time = request.getParameter("dimission_time");
		// 将字符串的时间格式化Timestamp类型
		PracticeRecord practicerecords = this.practiceRecordService
				.getPrecordUndimission_time(stu_id);// 通过学生id获得一条有效的实习记录
		if (practicerecords != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			/*
			 * PracticeRecord practicerecords = DictionaryService
			 * .findPracticeRecord(practicerecord);
			 */
			if (dimission_time == null) {
				dimission_time = "2010-01-01";

			} else {

				Timestamp t1 = new Timestamp(System.currentTimeMillis());
				try {
					t1 = new Timestamp(format.parse(dimission_time).getTime());
					practicerecords.setDimission_time(t1);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (contract_time == null) {
				contract_time = "2010-01-01";
			} else {
				Timestamp ct = new Timestamp(System.currentTimeMillis());
				try {
					ct = new Timestamp(format.parse(contract_time).getTime());
					practicerecords.setContract_time(ct);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (netsign_time == null) {
				netsign_time = "2010-01-01";
			} else {
				Timestamp nt = new Timestamp(System.currentTimeMillis());
				try {
					nt = new Timestamp(format.parse(netsign_time).getTime());

					practicerecords.setNetsign_time(nt);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (prct_contract_time == null) {
				prct_contract_time = "2010-01-01";
			} else {
				Timestamp pct = new Timestamp(System.currentTimeMillis());
				try {
					pct = new Timestamp(format.parse(prct_contract_time)
							.getTime());
					practicerecords.setPrct_contract_time(pct);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (work_time == null) {
				work_time = "2010-01-01";
			} else {
				Timestamp wt = new Timestamp(System.currentTimeMillis());
				try {
					wt = new Timestamp(format.parse(work_time).getTime());
					practicerecords.setWork_time(wt);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			practicerecords.setStu_id(stu_id);
			int i = weixinService.updatePr(practicerecords);
			return "/mobileViews/mobileStudent/AddSuccess";
		} else {
			return "/mobileViews/mobileStudent/erroRecord";
		}

	}

	/**
	 * 学生更换实习单位 李泽 2016/4/10
	 * 
	 */
	@RequestMapping("MobileStudent/resign.do")
	public String resign(HttpSession session, ModelMap modelMap) {
		Student stu = (Student) session.getAttribute("current_user");
		String stuId = stu.getId();
		System.out.println("******************学生Id:" + stuId);
		PracticeRecord p = practiceRecordService.selectByStuid(stuId);
		System.out.println("***********************p" + p);
		if (p == null) {
			return "/mobileViews/mobileStudent/errorResign";
		} else {
			Company cp = companyService
					.getCompanyByCompanyId(p.getCompany_id());// 通过学生ID查找所在公司记录
			String comName = cp.getCom_name();// 获取公司名称
			String id = p.getId();// 实习记录ID
			String PracticeRecordId = p.getPractice_id();// 实习任务ID
			// 获取当前时间，转换为String
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String time = format.format(date);
			modelMap.put("comName", comName);
			modelMap.put("id", id);
			modelMap.put("PracticeRecordId", PracticeRecordId);
			modelMap.put("time", time);
			return "/mobileViews/mobileStudent/resign";

		}
	}

	/**
	 * 保存离职信息，添加新的工作单位 李泽2016/4/10
	 * 
	 */
	@RequestMapping("MobileStudent/saveResign.do")
	public String saveResign(HttpSession session, ModelMap modelMap,
			HttpServletRequest request) {
		// 提交离职信息
		String id = request.getParameter("id");// 实习记录ID
		String resignTime = request.getParameter("resignTime");// 离职时间
		resignTime += " 00:00:00";
		String resignCause = request.getParameter("resignCause");// 离职原因
		Timestamp timeStamp = Timestamp.valueOf(resignTime);
		PracticeRecord practiceRecord = new PracticeRecord();
		practiceRecord.setId(id);
		practiceRecord.setDimission_time(timeStamp);
		practiceRecord.setNote(resignCause);
		int a = practiceRecordService.updatePracticeRecord(practiceRecord);
		// 插入新的实习单位记录
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Student stu = (Student) session.getAttribute("current_user");
		String stuId = stu.getId();// 学生ID
		String companyId = request.getParameter("com_name");// 企业ID
		String PracticeRecordId = request.getParameter("PracticeRecordId");// 实习ID
		String post = request.getParameter("position");// 岗位
		String orgion = request.getParameter("orgion");// 公司地区
		String leader = request.getParameter("leader");// 部门领导
		String workTime = request.getParameter("workTime");// 到岗时间
		workTime += " 00:00:00";
		timeStamp = Timestamp.valueOf(workTime);
		PracticeRecord pr = new PracticeRecord();
		pr.setId(Common.returnUUID());// ID
		pr.setPractice_id(PracticeRecordId);
		pr.setApply_time(time);// 申请时间
		pr.setStu_id(stuId);
		pr.setCompany_id(companyId);
		pr.setPost_id(post);
		pr.setCheck_state("0");// 核对状态默认为0
		pr.setWork_time(timeStamp);
		pr.setLeader(leader);
		pr.setCom_teacher(" ");// 企业老师
		pr.setCom_phone(" ");// 企业老师电话
		pr.setCom_orgion(orgion);
		practiceRecordService.insert(pr);

		return "/mobileViews/mobileStudent/resignSucceed";
	}
}
