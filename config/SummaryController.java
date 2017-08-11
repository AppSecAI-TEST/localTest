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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sict.authority.Login;
import com.sict.common.CommonSession;
import com.sict.dao.LogDao;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.GroupMembers;
import com.sict.entity.Groups;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.Student;
import com.sict.entity.SummaryOption;
import com.sict.entity.SummaryQAnswer;
import com.sict.entity.SummaryQnAnswer;
import com.sict.entity.SummaryQuestion;
import com.sict.entity.SummaryQuestionnaire;
import com.sict.entity.Teacher;
import com.sict.entity.User;
import com.sict.entity.UserRole;
import com.sict.service.DictionaryService;
import com.sict.service.ExcelService;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.QAnswerService;
import com.sict.service.StudentService;
import com.sict.service.SummaryOptionService;
import com.sict.service.SummaryQAnswerService;
import com.sict.service.SummaryQnAnswerService;
import com.sict.service.SummaryQuestionService;
import com.sict.service.SummaryQuestionnaireService;
import com.sict.service.TeaStuService;
import com.sict.service.TeacherService;
import com.sict.service.TrainDetailService;
import com.sict.service.UserRoleService;
import com.sict.service.UserService;
import com.sict.util.Common;
import com.sict.util.DateService;
import com.sict.util.Ichartjs_Color;

@Controller
public class SummaryController {
	@Resource(name = "summaryQuestionnaireService")
	private SummaryQuestionnaireService summaryQuestionnaireService;
	@Resource(name = "summaryQuestionService")
	private SummaryQuestionService summaryQuestionService;
	@Resource(name = "summaryOptionService")
	private SummaryOptionService summaryOptionService;
	@Resource(name = "summaryQnAnswerService")
	private SummaryQnAnswerService summaryQnAnswerService;
	@Resource(name = "summaryQAnswerService")
	private SummaryQAnswerService summaryQAnswerService;

	@Resource(name = "trainDetailService")
	private TrainDetailService trainDetailService;
	@Resource(name = "practiceRecordService")
	private PracticeRecordService practiceRecordService;
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	@Resource(name = "groupMembersService")
	private GroupMembersService groupMembersService;
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	@Resource(name = "orgService")
	private OrgService orgService;
	@Resource(name = "userRoleService")
	private UserRoleService userRoleService;

	@Resource(name = "QAnswerService")
	private QAnswerService qanswerService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	@Resource(name = "studentService")
	private StudentService studentService;
	@Resource(name = "teaStuService")
	private TeaStuService teaStuService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "noticeService")
	private NoticeService noticeService;
	@Resource(name = "excelService")
	private ExcelService excelService;
	@Resource
	LogDao logDao;

	/**
	 * 管理员界面，根据用户加载总结列表
	 * 
	 * @param session
	 * @param modelMap
	 * @return
	 */
	/* @Login */// 2016年1月8日
	@RequestMapping("/summary/admin.htm")
	public ModelAndView summary_admin(HttpSession session, ModelMap modelMap,
			HttpServletRequest request) {
		User user = CommonSession.getUser(session, userService);
		UserRole ur = new UserRole();
		ur.setUser_id(user.getId());
		List<UserRole> urlist = userRoleService.selectList(ur);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < urlist.size(); i++) {
			list.add(urlist.get(i).getRole_id());
		}
		/*
		 * if (list.contains("ROLE_LEADER_")){ SummaryQuestionnaire sn=new
		 * SummaryQuestionnaire(); List<SummaryQuestionnaire>
		 * summaryQuestionnaires = summaryQuestionnaireService.selectList(sn);
		 * List<SummaryQuestionnaire> summaryQuestionnairesList=new
		 * ArrayList<SummaryQuestionnaire>(); for(SummaryQuestionnaire sQ
		 * :summaryQuestionnaires){ SummaryQnAnswer summaryQnAnswer=new
		 * SummaryQnAnswer(); summaryQnAnswer.setQuestionnaire_id(sQ.getId());
		 * int count=summaryQnAnswerService.selectCount(summaryQnAnswer);
		 * if(count>0){ sQ.setIsHaveSubmit("1"); }else{ sQ.setIsHaveSubmit("2");
		 * } summaryQuestionnairesList.add(sQ); } modelMap.put("wenjuan",
		 * summaryQuestionnairesList); return new
		 * ModelAndView("summary/users/leader"); }else{
		 */

		List<SummaryQuestionnaire> summaryQuestionnaires = summaryQuestionnaireService
				.selectByUserID(user.getId());
		// start 吴敬国 2015年6月15日 看每个月总结是否有人回答
		List<SummaryQuestionnaire> summaryQuestionnairesList = new ArrayList<SummaryQuestionnaire>();
		for (SummaryQuestionnaire sQ : summaryQuestionnaires) {
			SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
			summaryQnAnswer.setQuestionnaire_id(sQ.getId());
			int count = summaryQnAnswerService.selectCount(summaryQnAnswer);
			if (count > 0) {
				sQ.setIsHaveSubmit("1");
			} else {
				sQ.setIsHaveSubmit("2");
			}
			summaryQuestionnairesList.add(sQ);
		}
		// end wjg 2015年6月15日 看每个月总结是否有人回答
		modelMap.put("wenjuan", summaryQuestionnairesList);
		modelMap.put("result", CommonSession.queryResultValue(request));

		// 获取提交日期列表
		List<Date[]> dateList = new ArrayList<Date[]>();
		for (SummaryQuestionnaire questionnaire : summaryQuestionnaires) {
			try {
				Date[] dateArr = this.queryAnswerDate(
						questionnaire.getStartDate(),
						questionnaire.getEndDate(),
						SummaryQuestionnaire.TYPE_MONTH_SUMMARY);
				dateList.add(dateArr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		modelMap.put("dateList", dateList);
		modelMap.put("nowDate", Common.getNowTime());
		return new ModelAndView("summary/users/admin");
		/* } */
	}

	@RequestMapping("/summary/leader.htm")
	public ModelAndView summary_leader(HttpSession session, ModelMap modelMap,
			HttpServletRequest request) {

		User user = CommonSession.getUser(session, userService);
		UserRole ur = new UserRole();
		ur.setUser_id(user.getId());
		List<UserRole> urlist = userRoleService.selectList(ur);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < urlist.size(); i++) {
			list.add(urlist.get(i).getRole_id());
		}

		SummaryQuestionnaire sn = new SummaryQuestionnaire();
		List<SummaryQuestionnaire> summaryQuestionnaires = summaryQuestionnaireService
				.selectList(sn);
		List<SummaryQuestionnaire> summaryQuestionnairesList = new ArrayList<SummaryQuestionnaire>();
		for (SummaryQuestionnaire sQ : summaryQuestionnaires) {
			SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
			summaryQnAnswer.setQuestionnaire_id(sQ.getId());
			int count = summaryQnAnswerService.selectCount(summaryQnAnswer);
			if (count > 0) {
				sQ.setIsHaveSubmit("1");
			} else {
				sQ.setIsHaveSubmit("2");
			}
			summaryQuestionnairesList.add(sQ);
		}
		modelMap.put("wenjuan", summaryQuestionnairesList);
		return new ModelAndView("summary/users/leader");

	}

	/**
	 * @function 教师端----学生总结管理
	 * 修改 by syj 2016-04-09
	 * @param session、modelMap、request
	 *
	 */
	@Login
	@RequestMapping("/summary/teacher.htm")
	public ModelAndView summary_teacher(HttpSession session, ModelMap modelMap,
			HttpServletRequest request) {
		User user = CommonSession.getUser(session, userService);
		//新添加代码  by syj
		String tea_id = user.getId();
		//最近的一个实习任务（比如说是2013级和2012级，前者比后者近）
		String lastGroupName = new String();
		String practiceId = new String();
		String grade = new String();
		//得到该老师带的所有的任务（包括实习和实训）
		List<Groups> groups = this.groupsService.getGroupsByTeaId(tea_id);
		//通过老师id得到老师所带的"校外实习"的任务
		List<PracticeTask> praList = this.practiceTaskService.selectOutSchoolPracticeTasks(tea_id);
		
		for(PracticeTask p : praList){//遍历集合将每个任务的名称赋给lastGroupName
			lastGroupName = p.getTask_name();
			practiceId = p.getId();//获得每个实习任务的id
			grade = p.getGrade();
			modelMap.put("grade", grade);
		}
		if(praList.size() == 0){
			lastGroupName = "";
		}
		
		modelMap.put("praList", praList);//将得到的实习任务列表（在这里共有两个）传到前台
		/*String defaultYear = Common.getDefaultYear();
		PracticeTask pra = new PracticeTask();
		pra.setTea_id(user.getId());
		pra.setState("1");
		pra.setTask_type("1");
		pra.setGrade(defaultYear);
		List<PracticeTask> practiceList = practiceTaskService.selectList(pra);
		String practiceId = null;

		for (PracticeTask p : practiceList) {
			if (p.getGrade().equalsIgnoreCase(defaultYear)) {
				practiceId = p.getId();
			}
		}*/
		modelMap.put("practiceId", practiceId);
		String orgAll = "";
		List<String> list = Common.getOrgsByUser_org_id(orgService,
				user.getOrg_id());
		List<SummaryQuestionnaire> summaryQuestionnaires = new ArrayList<SummaryQuestionnaire>();
		for (String str : list) {
			SummaryQuestionnaire summaryQuestionnarie = new SummaryQuestionnaire();
			summaryQuestionnarie.setState("1");
			summaryQuestionnarie.setDepartment(str);
			summaryQuestionnaires
					.addAll(summaryQuestionnaireService
							.selectListAndAddCommitNum(summaryQuestionnarie,
									practiceId));
		}
		modelMap.put("summaryQuestionnaires", summaryQuestionnaires);
		modelMap.put("result", CommonSession.queryResultValue(request));

		// 获取提交日期列表
		List<Date[]> dateList = new ArrayList<Date[]>();
		for (SummaryQuestionnaire questionnaire : summaryQuestionnaires) {
			try {
				Date[] dateArr = this.queryAnswerDate(
						questionnaire.getStartDate(),
						questionnaire.getEndDate(),
						SummaryQuestionnaire.TYPE_MONTH_SUMMARY);
				dateList.add(dateArr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		modelMap.put("dateList", dateList);
		modelMap.put("nowDate", Common.getNowTime());
//		modelMap.put("defaultYear", defaultYear);
		return new ModelAndView("summary/users/teacher");
	}

	/*
	 * 学生用户
	 */
	@Login
	@RequestMapping("/summary/student.htm")
	public ModelAndView summary_student(HttpSession session, ModelMap modelMap,
			HttpServletRequest request) {
		SummaryQuestionnaire summaryQuestionnaire = new SummaryQuestionnaire();
		summaryQuestionnaire.setState("1");
		String collegeId = (String) session.getAttribute("stu_college_id");
		summaryQuestionnaire.setDepartment(collegeId);
		List<SummaryQuestionnaire> summaryQuestionnaires = summaryQuestionnaireService
				.selectList(summaryQuestionnaire);

		List<Date[]> dateList = new ArrayList<Date[]>();
		for (SummaryQuestionnaire questionnaire : summaryQuestionnaires) {
			// 获取提交日期列表
			try {
				Date[] dateArr = this.queryAnswerDate(
						questionnaire.getStartDate(),
						questionnaire.getEndDate(),
						SummaryQuestionnaire.TYPE_MONTH_SUMMARY);
				dateList.add(dateArr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		User user = CommonSession.getUser(session, userService);
		GroupMembers groupMember = new GroupMembers();
		groupMember.setUser_id(user.getId());
		List<GroupMembers> groupMembers = groupMembersService
				.selectList(groupMember);
		List<Groups> groups = new ArrayList<Groups>();
		for (GroupMembers gm : groupMembers) {
			groups.add((Groups) groupsService.selectByID(gm.getGroup_id()));
		}

		List<PracticeTask> practiceTasks = new ArrayList<PracticeTask>();
		for (Groups group : groups) {
			PracticeTask practiceTask = (PracticeTask) practiceTaskService
					.selectByID(group.getPractice_id());
			// 吴敬国 2015-6-9 过滤掉实训任务
			if (practiceTask.getTask_type().equalsIgnoreCase("1")) {
				practiceTasks.add(practiceTask);
			}
		}

		// 吴敬国 2015-7-23 显示当前的实习状态
		/*
		 * TreeMap<String, StuGraState>
		 * stustate=DictionaryService.getStuGraStateName(); Iterator<String>
		 * iterator_2 = stustate.keySet().iterator(); List<StuGraState>
		 * statelist = new ArrayList<StuGraState>(); while
		 * (iterator_2.hasNext()) { Object key = iterator_2.next();
		 * System.out.println("tmp.get(key) is :" + stustate.get(key));
		 * statelist.add(stustate.get(key)); } modelMap.put("statelist",
		 * statelist);
		 */
		String nowpractice_state = DictionaryService.findStuGraStateName(
				user.getPractice_state()).getStateDesc();
		modelMap.put("practice_state", user.getPractice_state());
		modelMap.put("nowpractice_state", nowpractice_state);
		// 吴敬国 2015-7-23

		modelMap.put("practiceTasks", practiceTasks);
		modelMap.put("summarys", summaryQuestionnaires);
		modelMap.put("dateList", dateList);
		modelMap.put("nowDate", Common.getNowTime());
		return new ModelAndView("summary/users/student");

	}

	/**
	 * 新建总结页面
	 * 
	 * @param modelMap
	 * @param qn_id
	 * @return
	 */
	@Login
	@RequestMapping("/summary/newSummary.htm")
	public ModelAndView summary_newSummary(HttpServletRequest request,
			ModelMap modelMap, String qn_id) {

		HttpSession session = request.getSession();
		User user = CommonSession.getUser(session, userService);

		// 只获取本院系
		String orgID = orgService.selectOrgByTeaId(user.getId());
		Org org = new Org();
		org.setId(orgID);
		List<Org> org_list = orgService.selectList(org);
		modelMap.put("org", org_list);

		SummaryQuestionnaire summaryQuestionnaire = new SummaryQuestionnaire();
		summaryQuestionnaire = summaryQuestionnaireService.selectByID(qn_id);

		modelMap.put("summary", summaryQuestionnaire);
		return new ModelAndView("summary/newSummary");
	}

	/**
	 * 新建总结post提交
	 * 
	 * @param summaryQuestionnaire
	 * @param session
	 * @param modelMap
	 * @param qn_id
	 * @return
	 */
	@Login
	@RequestMapping("/summary/newSummary.do")
	public ModelAndView summary_newid(
			SummaryQuestionnaire summaryQuestionnaire, HttpSession session,
			ModelMap modelMap, String qn_id, RedirectAttributes attr) {
		User user = CommonSession.getUser(session, userService);
		summaryQuestionnaire.setUser_id(user.getId());
		// 去除日期插件带来的多余字符
		summaryQuestionnaire.setStartDate(summaryQuestionnaire.getStartDate()
				.replace(",", ""));
		summaryQuestionnaire.setEndDate(summaryQuestionnaire.getEndDate()
				.replace(",", ""));
		summaryQuestionnaire = summaryQuestionnaireService
				.insertOrUpdate(summaryQuestionnaire);
		if (summaryQuestionnaire != null) {
			attr.addFlashAttribute("result", "创建或修改总结成功");
		} else {
			attr.addFlashAttribute("result", "创建或修改创建总结失败");
		}
		return new ModelAndView("redirect:/summary/user/home.htm");
	}

	// TODO: 删除总结
	@Login
	@RequestMapping("/summary/delete.do")
	public ModelAndView summary_delete(String id, ModelMap modelMap,
			HttpSession session, RedirectAttributes attr) {

		// 判断这套总结是否有人填写，没有人填写的时候才能删除

		if (summaryQuestionnaireService.deleteByID(id) > 0) {
			attr.addFlashAttribute("result", "删除成功");
		} else {
			attr.addFlashAttribute("result", "删除失败");
		}
		return new ModelAndView("redirect:/summary/user/home.htm");

	}

	/*
	 * 总结问题列表
	 */
	@Login
	@RequestMapping(value = "/summary/{summary_id}/question.htm")
	public ModelAndView summary_showhtm(@PathVariable String summary_id,
			ModelMap modelMap, HttpServletRequest request) {

		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.getSummaryQuestionnaireAndQuestions(summary_id);
		modelMap.put("summary", summaryQuestionnaire);
		modelMap.put("result", CommonSession.queryResultValue(request));
		return new ModelAndView("summary/summaryQuestion");

	}

	// TODO: 加载新建总结问题页面
	@Login
	@RequestMapping(value = "/summary/question/{summary_id}/new.htm")
	public ModelAndView summary_newhtm(@PathVariable String summary_id,
			String questionID, ModelMap modelMap) {

		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectByID(summary_id);
		SummaryQuestion summaryQuestion = new SummaryQuestion();
		if ("".equals(questionID) == false && questionID != null) {
			summaryQuestion = summaryQuestionService.selectByID(questionID);
		}
		summaryQuestionnaire.setSummaryQuestion(summaryQuestion);
		modelMap.put("summary", summaryQuestionnaire);
		return new ModelAndView("summary/newSummaryQuestion");

	}

	// TODO : 新建总结问题post请求
	@RequestMapping(value = "/summary/question/insert.do")
	public ModelAndView summary_question_insert(
			SummaryQuestion summaryQuestion, ModelMap modelMap,
			RedirectAttributes attr) {
		String summaryID = summaryQuestion.getQuestionnaire_id();
		summaryQuestion = summaryQuestionService
				.insertOrUpdate(summaryQuestion);
		if (summaryQuestion != null) {
			attr.addFlashAttribute("result",
					"新建或修改总结问题——“" + summaryQuestion.getTitle() + "”——成功");
		} else {
			attr.addFlashAttribute("result", "新建或修改总结问题失败，请重新尝试");
		}
		return new ModelAndView("redirect:/summary/" + summaryID
				+ "/question.htm");

	}

	// TODO : 删除总结问题
	@Login
	@RequestMapping(value = "/summary/question/{questionID}/delete.do")
	public ModelAndView summary_question_delete(
			@PathVariable String questionID, String summaryID,
			RedirectAttributes attr) {

		if (summaryQuestionService.deleteByID(questionID) > 0) {
			attr.addFlashAttribute("result", "删除总结问题成功");
		} else {
			attr.addFlashAttribute("result", "删除总结问题失败，请重新尝试");
		}
		return new ModelAndView("redirect:/summary/" + summaryID
				+ "/question.htm");

	}

	// TODO : 根据问题查看所有选项
	@Login
	@RequestMapping(value = "/summary/{questionID}/option.htm")
	public ModelAndView zxcy_option(@PathVariable String questionID,
			ModelMap modelMap, HttpServletRequest request) {

		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectSummaryAndQuestionByQuestionID(questionID);
		List<SummaryOption> summaryOptions = summaryOptionService
				.selectByQuestionID(questionID);
		SummaryQuestion summaryQuestion = summaryQuestionnaire
				.getSummaryQuestion();
		summaryQuestion.setSummaryOptions(summaryOptions);
		summaryQuestionnaire.setSummaryQuestion(summaryQuestion);

		modelMap.put("summary", summaryQuestionnaire);
		modelMap.put("result", CommonSession.queryResultValue(request));

		return new ModelAndView("/summary/SummaryOption");

	}

	// TODO : 新建选项的页面
	@Login
	@RequestMapping(value = "/summary/option/{questionID}/new.htm")
	public ModelAndView summary_new_Option(@PathVariable String questionID,
			String optionID, ModelMap modelMap) {
		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectSummaryAndQuestionByQuestionID(questionID);
		SummaryQuestion summaryQuestion = summaryQuestionnaire
				.getSummaryQuestion();
		SummaryOption summaryOption = summaryOptionService.selectByID(optionID);
		summaryQuestion.setSummaryOption(summaryOption);
		summaryQuestionnaire.setSummaryQuestion(summaryQuestion);

		modelMap.put("summary", summaryQuestionnaire);

		return new ModelAndView("/summary/newSummaryOption");

	}

	// TODO : 提交新建问题选项
	@Login
	@RequestMapping(value = "/summary/option/insert.do")
	public ModelAndView summary_option_insert(SummaryOption summaryOption,
			RedirectAttributes attr) {

		String questionID = summaryOption.getQuestion_id();
		summaryOption = summaryOptionService.insertOrUpdate(summaryOption);
		if (summaryOption != null) {
			attr.addFlashAttribute("result",
					"新建或修改总结问题选项——“" + summaryOption.getTitle() + "”——成功");
		} else {
			attr.addFlashAttribute("result", "新建或修改总结问题失败，请重新尝试");
		}
		return new ModelAndView("redirect:/summary/" + questionID
				+ "/option.htm");
	}

	// TODO : 删除总结问题选项
	@Login
	@RequestMapping(value = "/summary/option/{optionID}/delete.do")
	public ModelAndView option_delete(@PathVariable String optionID,
			String questionID, RedirectAttributes attr) {

		if (summaryOptionService.deleteByID(optionID) > 0) {
			attr.addFlashAttribute("result", "删除选项成功");
		} else {
			attr.addFlashAttribute("result", "删除选项失败，请重新尝试");
		}
		return new ModelAndView("redirect:/summary/" + questionID
				+ "/option.htm");
	}

	/*
	 * 开始填写总结填写前的准备
	 */
	@Login
	@RequestMapping(value = "/summary/{summaryID}/summary.htm")
	public ModelAndView SummaryAnswer(HttpSession session, ModelMap modelMap,
			@PathVariable String summaryID, String answerDate,
			String practiceTasksID, RedirectAttributes attr, String StuState) {

		User user = CommonSession.getUser(session, userService);
		if (user == null) {
			return new ModelAndView("login");
		}
		// 吴敬国2015-7-23 保存实习状态
		String oldState = user.getPractice_state();
		session.setAttribute("StuState", StuState);
		// 如果学生的状态和数据库中的不一致，就需要在log表里结束上一条记录，开启一条新纪录
		/*
		 * if(!oldState.equalsIgnoreCase(StuState)){ Student
		 * s=DictionaryService.findStudent(user.getId());
		 * s.setPractice_state(StuState); studentService.update(s); Log oldLog =
		 * this.logDao.getOldLog(user.getId());
		 * oldLog.setEnd_time(Common.getNowTime()); this.logDao.update(oldLog);
		 * Log newLog = new Log(); newLog.setStu_id(user.getId());
		 * newLog.setPractice_id(practiceTasksID); newLog.setLog_type("1");
		 * newLog.setPractice_state(StuState);
		 * newLog.setUser_id(user.getTrue_name());
		 * newLog.setOperate_time(Common.getNowTime());
		 * this.logDao.insert(newLog); }
		 */
		// 吴敬国2015-7-23

		// 获取总结和总结的问题及选项
		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.getSummaryQuestionnaireAndQuestionAndOptionBySummaryIDAndStuState(
						summaryID, StuState);

		if (SummaryQuestionnaire.STATE_CLOSE.equals(summaryQuestionnaire
				.getState())) {
			modelMap.put("result", "总结已被关闭，无法填写");
			return new ModelAndView("summary/result");
		}

		// 将问题放到session中
		session.setAttribute("summaryQuestionnaire", summaryQuestionnaire);

		SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
		summaryQnAnswer.setQuestionnaire_id(summaryID);
		summaryQnAnswer.setUser_id(user.getId());
		summaryQnAnswer.setAnswerDate(answerDate);
		summaryQnAnswer.setPracticeTasksID(practiceTasksID);
		summaryQnAnswer = summaryQnAnswerService
				.selectByQnAnswer(summaryQnAnswer);

		if (summaryQnAnswer == null) {
			summaryQnAnswer = new SummaryQnAnswer();
		}

		// 如果提交过则不能再次提交
		if (summaryQnAnswer.getEnddate() != null
				&& !"".equals(summaryQnAnswer.getEnddate())) {

			if (summaryQnAnswer.getScore() != null) {// 如果提交过并且有老师评分，不能修改
				attr.addFlashAttribute("result",
						summaryQuestionnaire.getTitle() + "总结中" + answerDate
								+ "的总结已经提交完成，并且教师已评分，不允许重复填写");
				return new ModelAndView("redirect:/summary/user/home.htm");
			}

		}

		if (summaryQnAnswer.getId() == null) {
			Timestamp time = Common.getNowTime();
			summaryQnAnswer.setStartdate(time);
			summaryQnAnswer.setQuestionnaire_id(summaryID);
			summaryQnAnswer.setUser_id(user.getId());
			summaryQnAnswer.setAnswerDate(answerDate);
			summaryQnAnswer.setPracticeTasksID(practiceTasksID);
			summaryQnAnswerService.insert(summaryQnAnswer);
		}

		session.setAttribute("summaryQnAnswer", summaryQnAnswer);

		return new ModelAndView("redirect:/summary/summary.htm?page=0");

	}

	/*
	 * 每一道总结的准备
	 */
	@Login
	@RequestMapping(value = "/summary/summary.htm")
	public ModelAndView SummaryShow(String page, HttpSession session,
			ModelMap modelMap) {
		int num = 0;
		try {
			num = Integer.parseInt(page);
		} catch (Exception e) {
			return null;
		}

		try {
			SummaryQuestionnaire summaryQuestionnaire = (SummaryQuestionnaire) session
					.getAttribute("summaryQuestionnaire");

			List<SummaryQuestion> summaryQuestions = summaryQuestionnaire
					.getSummaryQuestions();
			SummaryQuestion summaryQuestion = summaryQuestions.get(num);
			summaryQuestionnaire.setSummaryQuestion(summaryQuestion);
			String summaryQnAnswerID = ((SummaryQnAnswer) session
					.getAttribute("summaryQnAnswer")).getId();
			SummaryQAnswer summaryQAnswer = new SummaryQAnswer();
			summaryQAnswer.setQuestion_id(summaryQuestion.getId());
			summaryQAnswer.setQnanswer_id(summaryQnAnswerID);
			List<SummaryQAnswer> summaryQAnswers = summaryQAnswerService
					.selectList(summaryQAnswer);
			if (summaryQAnswers.size() > 0) {
				summaryQAnswer = summaryQAnswers.get(0);
				if (summaryQAnswer.getAnswertext() != null) {
					for (SummaryOption option : summaryQuestion
							.getSummaryOptions()) {
						if (summaryQAnswer.getAnswertext().contains(
								option.getId())) {
							option.setChecked("checked");
						}
					}
				}
			}
			List<SummaryOption> optinonList = summaryQuestions.get(0)
					.getSummaryOptions();
			String StuState = (String) session.getAttribute("StuState");
			String ABCD = Common.getABCD(StuState, optinonList);
			modelMap.put("StuState", StuState);
			modelMap.put("ABCD", ABCD);
			modelMap.put("summary", summaryQuestionnaire);
			modelMap.put("page", page);
			modelMap.put("summaryQAnswer", summaryQAnswer);

			return new ModelAndView("/summary/summary");
		} catch (Exception e) {
			// TODO: handle exception
			return new ModelAndView("/summary/erroy");
		}

	}

	/*
	 * 总结提交的post
	 */
	@Login
	@RequestMapping(value = "/summary/summary.do")
	public ModelAndView SummarySumbit(String page,
			SummaryQAnswer summaryQAnswer, HttpSession session,
			ModelMap modelMap, HttpServletRequest request) {
		String test = (String) request.getAttribute("answertext");
		summaryQAnswer.setAnswertext(test);
		SummaryQuestionnaire summaryQuestionnaire = (SummaryQuestionnaire) session
				.getAttribute("summaryQuestionnaire");
		summaryQAnswerService.insertOrUpdate(summaryQAnswer);

		// 如果完成测验最后提交
		if ("sumbit".equals(page)) {

			SummaryQnAnswer summaryQnAnswer = (SummaryQnAnswer) session
					.getAttribute("summaryQnAnswer");
			summaryQnAnswer.setEnddate(Common.getNowTime());
			summaryQnAnswerService.update(summaryQnAnswer);

			session.removeAttribute("summaryQuestionnaire");
			session.removeAttribute("summaryQnAnswer");

			modelMap.put("cy", summaryQuestionnaire);
			modelMap.put("result", "总结提交成功");

			return new ModelAndView("summary/result");
		} else {
			return new ModelAndView("redirect:/summary/summary.htm?page="
					+ page);
		}

	}

	/*
	 * 预览总结前的准备
	 */
	@Login
	@RequestMapping(value = "/summary/{summaryID}/summaryPreview.htm")
	public ModelAndView SummaryPreview(HttpSession session, ModelMap modelMap,
			@PathVariable String summaryID, String answerDate) {

		// 获取总结
		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.getSummaryQuestionnaireAndQuestionAndOptionBySummaryID(summaryID);

		// 将问题放到session中
		session.setAttribute("summaryQuestionnaire", summaryQuestionnaire);

		return new ModelAndView("redirect:/summary/summaryPreview.htm?page=0");

	}

	/*
	 * 预览一道总结题目
	 */
	@Login
	@RequestMapping(value = "/summary/summaryPreview.htm")
	public ModelAndView SummaryShowPreview(String page, HttpSession session,
			ModelMap modelMap) {
		int num = 0;
		try {
			num = Integer.parseInt(page);
		} catch (Exception e) {
			return null;
		}

		SummaryQuestionnaire summaryQuestionnaire = (SummaryQuestionnaire) session
				.getAttribute("summaryQuestionnaire");
		List<SummaryQuestion> summaryQuestions = summaryQuestionnaire
				.getSummaryQuestions();

		if (num == summaryQuestions.size()) {
			modelMap.put("result", "预览问卷并不计入结果，不生成结果，请返回首页");
			return new ModelAndView("summary/result");
		}

		SummaryQuestion summaryQuestion = summaryQuestions.get(num);
		summaryQuestionnaire.setSummaryQuestion(summaryQuestion);

		modelMap.put("summary", summaryQuestionnaire);
		modelMap.put("page", page);

		return new ModelAndView("/summary/summaryPreview");

	}

	/*
	 * 开启关闭测验
	 */

	@Login
	@RequestMapping(value = "/summary/{summaryID}/summaryState.do")
	public ModelAndView change_questionnaire_state(
			@PathVariable String summaryID, ModelMap modelMap,
			HttpSession session, RedirectAttributes attr) {

		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectByID(summaryID);
		if (SummaryQuestionnaire.STATE_OPEN.equals(summaryQuestionnaire
				.getState())) {
			summaryQuestionnaire.setState(SummaryQuestionnaire.STATE_CLOSE);
			attr.addFlashAttribute("result", "成功关闭总结");
		} else {
			summaryQuestionnaire.setState(SummaryQuestionnaire.STATE_OPEN);
			attr.addFlashAttribute("result", "成功开启总结");
		}
		summaryQuestionnaireService.update(summaryQuestionnaire);
		return new ModelAndView("redirect:/summary/user/home.htm");
	}

	// TODO: 显示上传月总结页面
	@Login
	@RequestMapping(value = "/summary/uploadSummary.htm")
	public String summary_upload_summary_htm(ModelMap modelMap,
			HttpServletRequest request, HttpSession session) {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 只获取1，2级单位,只取自己负责的学院
		Org org = new Org();
		org.setOrg_level("1");
		org.setContacts(tea.getId());
		List<Org> org_list = orgService.selectList(org);
		org.setOrg_level("2");
		org_list.addAll(orgService.selectList(org));
		modelMap.put("org", org_list);
		modelMap.put("result", CommonSession.queryResultValue(request));

		return "summary/upload/uploadSummary";
	}

	// TODO : 上传总结Post请求
	@Login
	@RequestMapping(value = "/summary/uploadSummary.do")
	public String summary_upload_summary_do(HttpServletRequest request,
			RedirectAttributes attr, SummaryQuestionnaire summaryQuestionnaire) {

		// 设置总结的ID
		summaryQuestionnaire.setId(Common.returnUUID());
		// 设置创建用户ID
		User user = CommonSession.getUser(request.getSession(), userService);
		summaryQuestionnaire.setUser_id(user.getId());

		MultipartFile file = null;
		try {
			file = Common.getUpliadFile(request);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			attr.addFlashAttribute("result", "上传总结错误，信息：" + e.getMessage());
			return "redirect:/summary/uploadSummary.htm";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			attr.addFlashAttribute("result", "上传总结错误，信息：" + e.getMessage());
			return "redirect:/summary/uploadSummary.htm";
		}
		if (!file.isEmpty()) {

			try {
				Workbook book = Workbook.getWorkbook(file.getInputStream());
				// 获得第一个工作表对象
				Sheet sheet = book.getSheet(0);

				List<SummaryQuestion> summaryQuestions = new ArrayList<SummaryQuestion>();

				int i = 2;
				while (true) {
					int j = 0;
					// 获取第1列第3行
					Cell cell = sheet.getCell(j, i);
					// 如果值不为空
					if ("".equals(cell.getContents()) == false) {

						SummaryQuestion summaryQuestion = new SummaryQuestion();
						// 设置问题标题
						summaryQuestion.setTitle(cell.getContents());
						// 设置问题的ID
						summaryQuestion.setId(Common.returnUUID());
						// 设置问题的questionnaireID
						summaryQuestion
								.setQuestionnaire_id(summaryQuestionnaire
										.getId());

						// 设置问题类型
						++j;
						cell = sheet.getCell(j, i);
						String type = "";
						if ("单选".equals(cell.getContents())) {
							type = SummaryQuestion.TYPE_SINGLECHOICE;
						} else if ("多选".equals(cell.getContents())) {
							type = SummaryQuestion.TYPE_MULTIPLECHOICE;
						} else if ("文本".equals(cell.getContents())) {
							type = SummaryQuestion.TYPE_TEXT;
						} else if ("说明".equals(cell.getContents())) {
							type = SummaryQuestion.TYPE_ILLUSTRATION;
						} else {
							attr.addFlashAttribute("result", "上传总结错误，信息："
									+ summaryQuestion.getTitle()
									+ "--中的问题类型不能为空，请检查表，修改后再尝试");
							return "redirect:/summary/uploadSummary.htm";
						}
						summaryQuestion.setType(type);

						// 设置问题是否必答
						++j;
						cell = sheet.getCell(j, i);
						String isT;
						if ("是".equals(cell.getContents())) {
							isT = "1";
						} else if ("否".equals(cell.getContents())) {
							isT = "0";
						} else {
							if (type.equals(SummaryQuestion.TYPE_ILLUSTRATION)) {
								isT = "0";
							} else {
								attr.addFlashAttribute("result", "上传总结错误，信息："
										+ summaryQuestion.getTitle()
										+ "--中的问题是否必答选项不能为空，请检查表，修改后再尝试");
								return "redirect:/summary/uploadSummary.htm";
							}
						}
						summaryQuestion.setAnswer(type);

						// 设置问题是否有其他选项
						++j;
						cell = sheet.getCell(j, i);
						if ("是".equals(cell.getContents())) {
							isT = "1";
						} else if ("否".equals(cell.getContents())) {
							isT = "0";
						} else {
							if (type.equals(SummaryQuestion.TYPE_ILLUSTRATION)
									|| type.equals(SummaryQuestion.TYPE_TEXT)) {
								isT = "0";
							} else {
								attr.addFlashAttribute("result", "上传总结错误，信息："
										+ summaryQuestion.getTitle()
										+ "--中的问题是否有其他选项不能为空，请检查表，修改后再尝试");
								return "redirect:/summary/uploadSummary.htm";
							}

						}
						summaryQuestion.setOther(isT);

						// 设置总结问题样式
						++j;
						cell = sheet.getCell(j, i);
						String style = "";
						if ("默认".equals(cell.getContents())) {
							style = SummaryQuestion.STYLE_DEFAULT;
						} else {
							style = SummaryQuestion.STYLE_DEFAULT;
						}
						summaryQuestion.setStyle(style);

						// 设置回答学生的类型
						++j;
						cell = sheet.getCell(j, i);
						String type_student = "";
						if ("没有工作".equals(cell.getContents())) {
							type_student = SummaryQuestion.TYPE_STUDENT_NOWORK;
						} else if ("有工作".equals(cell.getContents())) {
							type_student = SummaryQuestion.TYPE_STUDENT_HAVEWORK;
						} else if ("通用问题".equals(cell.getContents())) {
							type_student = SummaryQuestion.TYPE_STUDENT_COMMEN;
						} else if ("升学考试".equals(cell.getContents())) {
							type_student = SummaryQuestion.TYPE_STUDENT_ENTRANCE;
						} else {
							attr.addFlashAttribute("result", "上传总结错误，信息："
									+ summaryQuestion.getTitle()
									+ "--中的问题类型不能为空，请检查表，修改后再尝试");
							return "redirect:/summary/uploadSummary.htm";
						}
						summaryQuestion.setType_student(type_student);
						List<SummaryOption> summaryOptions = new ArrayList<SummaryOption>();
						// 如果是单选题或者是多选题，添加选项
						if (SummaryQuestion.TYPE_CHOICE
								.contains(summaryQuestion.getType())) {
							while (true) {
								++j;
								cell = sheet.getCell(j, i);
								if ("".equals(cell.getContents()) == false) {
									SummaryOption option = new SummaryOption();
									option.setQuestion_id(summaryQuestion
											.getId());
									option.setTitle(cell.getContents());
									summaryOptions.add(option);
								} else {
									break;
								}
							}

							if (summaryOptions.size() < 2) {
								attr.addFlashAttribute("result", "上传总结错误，信息："
										+ summaryQuestion.getTitle()
										+ "--是以个选择题，那么它的选项不能少于2个，请检查表，修改后再尝试");
								return "redirect:/summary/uploadSummary.htm";
							}
						}

						// 将选项添加到问题里
						summaryQuestion.setSummaryOptions(summaryOptions);
						summaryQuestions.add(summaryQuestion);
						i++;
					} else {
						break;
					}
					summaryQuestionnaire.setSummaryQuestions(summaryQuestions);
				}
				book.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {
			attr.addFlashAttribute("result", "上传总结错误，信息：" + "文件异常，文件是空的");
			return "redirect:/summary/uploadSummary.htm";
		}

		// 开始上传总结
		summaryQuestionnaireService.insertManualID(summaryQuestionnaire);

		List<SummaryQuestion> summaryQuestions = summaryQuestionnaire
				.getSummaryQuestions();
		for (SummaryQuestion summaryQuestion : summaryQuestions) {

			summaryQuestionService.insertManualID(summaryQuestion);

			List<SummaryOption> summaryOptions = summaryQuestion
					.getSummaryOptions();
			for (SummaryOption summaryOption : summaryOptions) {

				summaryOptionService.insert(summaryOption);

			}
		}

		attr.addFlashAttribute("result", summaryQuestionnaire.getTitle()
				+ "，总结上传并生成成功。但是总结的状态是关闭的，请手动开启总结，学生/老师才能访问总结");
		return "redirect:/summary/user/home.htm";

	}

	// TODO : 查看提交月总结的用户列表
	@Login
	@RequestMapping(value = "/summary/{summaryID}/queryUserSummary/sumbitSummaryUserList.htm")
	public String summary_queryUserList(@PathVariable String summaryID,
			HttpServletRequest request, HttpSession session, ModelMap modelMap,
			String answerDate, String practiceTasksID) {
		session.setAttribute("summaryID", summaryID);// 保存查看的月总结id 吴敬国 2015-6-25
		List<Student> studentList = groupMembersService
				.getStuListByPraId(practiceTasksID);
		SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
		summaryQnAnswer.setQuestionnaire_id(summaryID);
		SummaryQuestionnaire sq = summaryQuestionnaireService
				.selectByID(summaryID);
		String anDate = sq.getStartDate();
		for (Student s : studentList) {
			String pk_State = s.getPractice_state();
			summaryQnAnswer.setUser_id(s.getId());
			// summaryQnAnswer.setAnswerDate(answerDate);
			List<SummaryQnAnswer> summaryqnAnswer = summaryQnAnswerService
					.selectList(summaryQnAnswer);
			/*
			 * List<User> users =
			 * userService.selectBySummaryQnAnswer(summaryQnAnswer);
			 */
			if (!summaryqnAnswer.isEmpty()) {// 已经提交
				if (pk_State.equals("180")) {
					s.setSummaryScore("已征兵");
					s.setSummarystartDate("不需要提交");
					SummaryQnAnswer sumQnAnswer = summaryqnAnswer.get(0);
					sumQnAnswer.setScore("70");
					sumQnAnswer.setStartdate(Common
							.getTimeStampTime("2013-09-01"));
					summaryQnAnswerService.update(sumQnAnswer);
				} else {
					if (summaryqnAnswer.get(0).getEnddate() != null) {
						if (summaryqnAnswer.get(0).getEnddate() != null
								& summaryqnAnswer.get(0).getScore() == null
								& summaryqnAnswer.get(0).getTemp1() == null) {// 提交完没有审核
							s.setSummaryScore("未审阅");
							s.setSummarystartDate(DateService
									.TimestampTimeTurnString(summaryqnAnswer
											.get(0).getStartdate()));
						} else if (summaryqnAnswer.get(0).getEnddate() != null
								& summaryqnAnswer.get(0).getScore() == null) {
							s.setSummaryScore("已审阅，未评分");
							s.setSummarystartDate(DateService
									.TimestampTimeTurnString(summaryqnAnswer
											.get(0).getStartdate()));
						} else {
							s.setSummaryScore(summaryqnAnswer.get(0).getScore());
							s.setSummarystartDate(DateService
									.TimestampTimeTurnString(summaryqnAnswer
											.get(0).getStartdate()));
						}
					} else {
						s.setSummaryScore("0");
						s.setSummarystartDate("未提交");
					}
				}
			} else {
				if (pk_State.equals("180")) {
					s.setSummaryScore("已征兵");
					s.setSummarystartDate("不需要提交");
					SummaryQnAnswer sumQnAnswer = new SummaryQnAnswer();
					sumQnAnswer.setUser_id(s.getId());
					sumQnAnswer.setQuestionnaire_id(summaryID);
					sumQnAnswer.setPracticeTasksID(practiceTasksID);
					sumQnAnswer.setScore("70");
					sumQnAnswer.setStartdate(Common
							.getTimeStampTime("2013-09-01"));
					sumQnAnswer.setEnddate(Common
							.getTimeStampTime("2013-09-01"));
					sumQnAnswer.setAnswerDate(anDate);
					summaryQnAnswerService.insert(sumQnAnswer);
				} else {
					s.setSummaryScore("0");
					s.setSummarystartDate("未提交");
				}
			}
		}

		/*
		 * 2015年5月7日 14:01:05 邢志武修改 处理只显示该老师任务的学生月总结
		 */
		/*
		 * Teacher tea = (Teacher) session.getAttribute("current_user"); String
		 * tea_id=tea.getId(); List<User>
		 * trueUsers=userService.getTrueUsers(tea_id, users);
		 */
		modelMap.put("summaryQnAnswer", summaryQnAnswer);
		modelMap.put("users", studentList);
		return "/summary/queryUserSummary/sumbitSummaryUserList";
	}

	// TODO : 查看一个用户提交的月总结
	@Login
	@RequestMapping(value = "/summary/queryUserSummary/userSumbitSummary.htm")
	public String summary_userSumbitSummary(String questionnaire_id,
			String answerDate, HttpServletRequest request, ModelMap modelMap,
			String user_id, HttpSession session) {
		SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
		summaryQnAnswer.setStartdate(DateService
				.StringTimeTurnTimestamp(answerDate));
		summaryQnAnswer.setQuestionnaire_id(questionnaire_id);
		// 吴敬国 start 2015-6-24
		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectByID((String) session.getAttribute("summaryID"));
		String startDate = summaryQuestionnaire.getStartDate();
		int a = startDate.indexOf("-");
		String year = startDate.substring(0, a);
		String month = startDate.substring(a + 1, startDate.length());

		Student student = studentService.selectByID(user_id);
		String class_name = DictionaryService.findOrg(student.getClass_id())
				.getOrg_name();
		String com_teacher_id = student.getCom_teacher_id();
		Teacher com_teacher = (Teacher) teacherService
				.selectByID(com_teacher_id);
		String com_teacher_name = "无";
		String com_teacher_phone = "无";
		if (com_teacher != null) {
			com_teacher_name = com_teacher.getTrue_name();
			com_teacher_phone = com_teacher.getPhone();
		}
		PracticeRecord practiceRecord = practiceRecordService
				.selectPracticeRecordByStuId(user_id);
		String com_name = "无";
		String com_phone = "无";
		String post = "无";
		if (practiceRecord != null) {
			com_phone = DictionaryService.findCompany(
					practiceRecord.getCompany_id()).getPhone();
			com_name = DictionaryService.findCompany(
					practiceRecord.getCompany_id()).getCom_name();
			post = practiceRecord.getPost_id();
		}
		// 吴敬国 end 2015-6-24
		summaryQnAnswer = summaryQnAnswerService
				.selectByQnAnswer(summaryQnAnswer);
		List<Map<String, Object>> list = summaryQnAnswerService
				.selectQuestionAndAnanswerBySummaryQnAnswerID(summaryQnAnswer
						.getId());

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (SummaryQuestion.TYPE_CHOICE
					.contains(map.get("type").toString())) {
				/*
				 * 2015年5月6日 19:33:29 邢志武 修改内容： answertext为空未处理 try catch
				 * 捕捉空指针异常，空指针既未答题
				 */
				String summaryOptionID = "";
				try {
					summaryOptionID = map.get("answertext").toString();
				} catch (NullPointerException e) {
					System.out.println("空指针");
					map.put("answertext", "未答题");
				}
				// 以上未修改内容
				if (summaryOptionID != null
						&& "".equals(summaryOptionID) == false) {
					// 2015年5月6日 邢志武修改 处理多选问题（多选用“/”分隔开了）
					if (summaryOptionID.length() > 32) {
						StringBuffer bf = new StringBuffer();
						String[] summaryOptionIDArray = summaryOptionID
								.split(",");
						for (int j = 0; j < summaryOptionIDArray.length; j++) {
							String summaryTrueOptionID = summaryOptionIDArray[j];
							String title = summaryOptionService.selectByID(
									summaryTrueOptionID).getTitle();
							String trueTitle = bf.append("/" + title)
									.toString();
							map.put("answertext",
									trueTitle.substring(1, trueTitle.length()));
						}
					} else {
						String title = summaryOptionService.selectByID(
								summaryOptionID).getTitle();
						map.put("answertext", title);
					}
					// 2015年5月6日 以上是修改

				}
			} else if (SummaryQuestion.TYPE_ILLUSTRATION.equals(map.get("type")
					.toString())) {
				list.remove(i);
				i = i - 1;
				// 2015年5月6日 19:52:52 处理了其他的情况，如summaryOptionID=,这种情况
			} else if (SummaryQuestion.TYPE_TEXT.equals(map.get("type")
					.toString())) {
				// 吴敬国 2015-6-9 处理文本类型
				String summaryOptionID = "";
				try {
					summaryOptionID = map.get("answertext").toString();
				} catch (NullPointerException e) {
					System.out.println("空指针");
					map.put("answertext", "未答题");
				}
				// 以上未修改内容
				if (summaryOptionID != null
						&& "".equals(summaryOptionID) == false) {
					int wordCount = summaryOptionID.length();
					map.put("wordCount", wordCount); // 字数统计
				}

				// 吴敬国 2015-6-9 处理文本类型 end
			} else {
				map.put("answertext", "未答题");
			}
			// 以上为修改内容
		}
		modelMap.put("summaryQnAnswer", summaryQnAnswer);
		modelMap.put("list", list);
		// 吴敬国 start 2015-6-24
		modelMap.put("student", student);
		modelMap.put("com_teacher_name", com_teacher_name);
		modelMap.put("com_teacher_phone", com_teacher_phone);
		modelMap.put("class_name", class_name);
		modelMap.put("com_name", com_name);
		modelMap.put("com_phone", com_phone);
		modelMap.put("post", post);
		modelMap.put("year", year);
		modelMap.put("month", month);
		// //吴敬国 end 2015-6-24
		return "/summary/queryUserSummary/userSumbitSummary";
	}

	// TODO : 给学生打分 并且保存教师评价
	@Login
	@RequestMapping("/summary/queryUserSummary/summaryGrade.do")
	public void summaryGrade(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, String score,
			String summaryQnAnswerID, String content) throws IOException {

		SummaryQnAnswer summaryQnAnswer = summaryQnAnswerService
				.selectByID(summaryQnAnswerID);
		if (!score.equalsIgnoreCase("")) {
			summaryQnAnswer.setScore(score);
		}
		summaryQnAnswer.setTemp1(content);
		Notice notice = new Notice();

		Teacher tea = DictionaryService.findTeacher(DictionaryService
				.findPracticeTask(summaryQnAnswer.getPracticeTasksID())
				.getTea_id());
		String notice_code = noticeService.getNoticeCode(
				summaryQnAnswer.getPracticeTasksID(),
				summaryQnAnswer.getUser_id());
		notice.setTitle("实习总结已审阅的通知");
		String noticeContent = "你的" + summaryQnAnswer.getAnswerDate()
				+ "实习总结已被指导教师审阅过,得分:" + score + ",教师评价:" + content + "。";
		notice.setContent(noticeContent);
		notice.setNotice_type("8");
		notice.setCreate_time(DateService.getNowTimeTimestamp());
		notice.setNotice_code(notice_code);
		notice.setStu_id(summaryQnAnswer.getUser_id());
		notice.setTea_id(tea.getId());
		noticeService.insert(notice);
		response.getWriter().print(
				(Integer) summaryQnAnswerService.update(summaryQnAnswer));

	}

	// TODO : 判断学生是否回答了该总结
	@Login
	@RequestMapping("/summary/isAnswerSummary.do")
	public void isAnswerSummary(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, String summaryId)
			throws IOException {
		User user = CommonSession.getUser(request.getSession(), userService);
		SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
		summaryQnAnswer.setQuestionnaire_id(summaryId);
		summaryQnAnswer.setUser_id(user.getId());
		response.getWriter().print(
				(Integer) summaryQnAnswerService.selectCount(summaryQnAnswer));
	}

	/**
	 * 获取提交日期列表
	 * 
	 * @param start
	 * @param end
	 * @param type
	 *            问卷类型 年总结 月总结 周总结 日总结
	 * @return
	 * @throws ParseException
	 */
	public Date[] queryAnswerDate(String start, String end, String type)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (SummaryQuestionnaire.TYPE_MONTH_SUMMARY.equals(type)) {
			sdf = new SimpleDateFormat("yyyy-MM");
		}
		Date dBegin = sdf.parse(start);
		Date dEnd = sdf.parse(end);

		List<Date> lDate = new ArrayList<Date>();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		Date[] dateArr = new Date[lDate.size()];
		for (int i = 0; i < lDate.size(); i++) {
			Date date = lDate.get(i);
			dateArr[i] = date;
		}
		return dateArr;
	}

	/**
	 * 功能：验证时间区间时候交叉 吴敬国2015年6月16日
	 * */
	@RequestMapping("/summary/checkTimeRepeat.do")
	@ResponseBody
	public String checkTimeRepeat(HttpServletRequest request,
			HttpServletResponse response, String startDate, String endDate)
			throws IOException {
		response.setCharacterEncoding("UTF-8");

		SummaryQuestionnaire summaryQuestionnaire = new SummaryQuestionnaire();
		summaryQuestionnaire.setState("1");
		List<SummaryQuestionnaire> questionnaires = summaryQuestionnaireService
				.selectList(summaryQuestionnaire);

		List<String> beginList = new ArrayList<String>();
		List<String> endList = new ArrayList<String>();
		for (SummaryQuestionnaire sq : questionnaires) {
			beginList.add(sq.getStartDate());// 开始时间的list
			endList.add(sq.getEndDate());// 结束时间的list
		}
		String infor = "1";// 说明没有重叠时间
		// 时间有重叠有三种情况:
		// 1.插入时间的开始时间小于已经存在的一段时间的开始时间，但结束时间位于开始时间和结束时间之间(重叠)
		// 2.插入时间的开始时间小于已经存在的一段时间的开始时间，但结束时间大于结束时间(全包含)
		// 3.插入时间的开始时间大于一段已经存在的时间的开始时间，但是却小于这段时间的结束时间,且结束时间大于这段时间的结束时间
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Date dt1 = null;
		try {
			dt1 = df.parse(startDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Date dt2 = null;
		try {
			dt2 = df.parse(endDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < endList.size(); i++) {
			try {
				if (dt1.getTime() <= df.parse(endList.get(i)).getTime()
						&& dt2.getTime() >= df.parse(beginList.get(i))
								.getTime()) {
					infor = "2";
					break;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return infor;
	}

	// TODO : 学生查看自己提交的月总结
	@Login
	@RequestMapping(value = "/summary/queryUserSummary/seeMySumbitSummary.htm")
	public String summary_seeMySumbitSummary(String summaryId,
			String startDate, HttpServletRequest request, ModelMap modelMap,
			HttpSession session) {
		User user = CommonSession.getUser(session, userService);
		// 吴敬国 start 2015-6-24
		SummaryQuestionnaire summaryQuestionnaire = summaryQuestionnaireService
				.selectByID((String) session.getAttribute("summaryID"));
		int a = startDate.indexOf("-");
		String year = startDate.substring(0, a);
		String month = startDate.substring(a + 1, startDate.length());
		Student student = studentService.selectByID(user.getId());
		String com_teacher_id = student.getCom_teacher_id();
		Teacher com_teacher = (Teacher) teacherService
				.selectByID(com_teacher_id);
		String com_teacher_name = "无";
		String com_teacher_phone = "无";
		String com_name = "无";
		String com_phone = "无";
		String post = "无";
		if (com_teacher != null) {
			com_teacher_name = com_teacher.getTrue_name();
			com_teacher_phone = com_teacher.getPhone();
		}
		PracticeRecord practiceRecord = practiceRecordService
				.selectPracticeRecordByStuId(user.getId());
		if (practiceRecord != null) {
			com_phone = DictionaryService.findCompany(
					practiceRecord.getCompany_id()).getPhone();
			com_name = DictionaryService.findCompany(
					practiceRecord.getCompany_id()).getCom_name();
			post = practiceRecord.getPost_id();
		}
		// 吴敬国 end 2015-6-24
		SummaryQnAnswer summaryQnAnswer = new SummaryQnAnswer();
		summaryQnAnswer.setUser_id(user.getId());
		summaryQnAnswer.setQuestionnaire_id(summaryId);
		summaryQnAnswer = summaryQnAnswerService
				.selectByQnAnswer(summaryQnAnswer);
		if (summaryQnAnswer.getId() != null) {
			List<Map<String, Object>> list = summaryQnAnswerService
					.selectQuestionAndAnanswerBySummaryQnAnswerID(summaryQnAnswer
							.getId());
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (SummaryQuestion.TYPE_CHOICE.contains(map.get("type")
						.toString())) {
					/*
					 * 2015年5月6日 19:33:29 邢志武 修改内容： answertext为空未处理 try catch
					 * 捕捉空指针异常，空指针既未答题
					 */
					String summaryOptionID = "";
					try {
						summaryOptionID = map.get("answertext").toString();
					} catch (NullPointerException e) {
						System.out.println("空指针");
						map.put("answertext", "未答题");
					}
					// 以上未修改内容
					if (summaryOptionID != null
							&& "".equals(summaryOptionID) == false) {
						// 2015年5月6日 邢志武修改 处理多选问题（多选用“/”分隔开了）
						if (summaryOptionID.length() > 32) {
							StringBuffer bf = new StringBuffer();
							String[] summaryOptionIDArray = summaryOptionID
									.split(",");
							for (int j = 0; j < summaryOptionIDArray.length; j++) {
								String summaryTrueOptionID = summaryOptionIDArray[j];
								String title = summaryOptionService.selectByID(
										summaryTrueOptionID).getTitle();
								String trueTitle = bf.append("/" + title)
										.toString();
								map.put("answertext",
										trueTitle.substring(1,
												trueTitle.length()));
							}
						} else {
							String title = summaryOptionService.selectByID(
									summaryOptionID).getTitle();
							map.put("answertext", title);
						}
						// 2015年5月6日 以上是修改

					}
				} else if (SummaryQuestion.TYPE_ILLUSTRATION.equals(map.get(
						"type").toString())) {
					list.remove(i);
					i = i - 1;
					// 2015年5月6日 19:52:52 处理了其他的情况，如summaryOptionID=,这种情况
				} else if (SummaryQuestion.TYPE_TEXT.equals(map.get("type")
						.toString())) {
					// 吴敬国 2015-6-9 处理文本类型
					String summaryOptionID = "";
					try {
						summaryOptionID = map.get("answertext").toString();
					} catch (NullPointerException e) {
						System.out.println("空指针");
						map.put("answertext", "未答题");
					}
					// 吴敬国 2015-6-9 处理文本类型 end
				} else {
					map.put("answertext", "未答题");
				}
				// 以上为修改内容
			}
			modelMap.put("list", list);
		}

		// 获得学生的id
		String stu_id = student.getId();
		// 通过学生id和所在的学院得到总结的一条记录
		SummaryQnAnswer summaryQnAnswer1 = summaryQnAnswerService
				.selectByQuestionnaireIDAndUserID(summaryId, stu_id);
		// 根据记录得到实习任务的id
		String practiceId = summaryQnAnswer1.getPracticeTasksID();
		// 通过实习任务的id得到实习任务
		PracticeTask practiceTask = DictionaryService
				.findPracticeTask(practiceId);
		// 获得实习任务中的所属部门（系或者是学院）
		String scope = practiceTask.getScope();
		// 通过所属部门得到组织表
		Org org = DictionaryService.findOrg(scope);
		// 获得组织表的名称
		String orgName = org.getOrg_name();
		String orgParent_id;// 组织表的父id
		// 获得组织表的组织级别并判断等级是否为学院级别（2）
		String orgLevel = org.getOrg_level();//
		if (orgLevel.equals("3") || orgLevel.equals("4")
				|| orgLevel.equals("5")) {
			orgParent_id = org.getParent_id();
			orgName = DictionaryService.findOrg(orgParent_id).getOrg_name();
		}

		modelMap.put("orgName", orgName);
		modelMap.put("summaryQnAnswer", summaryQnAnswer);
		// 吴敬国 start 2015-6-24
		modelMap.put("student", user);
		modelMap.put("com_teacher_name", com_teacher_name);
		modelMap.put("com_teacher_phone", com_teacher_phone);
		modelMap.put("com_name", com_name);
		modelMap.put("com_phone", com_phone);
		modelMap.put("post", post);
		modelMap.put("year", year);
		modelMap.put("month", month);
		// //吴敬国 end 2015-6-24
		return "/summary/queryUserSummary/mySumbitSummary";
	}

	/**
	 * 查看月总结分析图
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/summary/analysis.htm")
	public String toAnalysis(ModelMap modelMap, String id, String title)
			throws UnsupportedEncodingException {

		// 显示问卷，问题，选项
		SummaryQuestionnaire qn = this.summaryQuestionnaireService
				.getByQuestionAndOption(id);
		SummaryQuestion q;
		SummaryQAnswer qa;
		SummaryOption o;
		List qList; // 问题集合
		List oList; // 选项集合

		// 查看问卷有多少人提交了
		SummaryQnAnswer qna = new SummaryQnAnswer();
		qna.setQuestionnaire_id(id);
		List<SummaryQnAnswer> qnaList = this.summaryQnAnswerService
				.selectList(qna);
		int qnaNum = qnaList.size(); // 问卷提交数量
		int qnaOver = 0;
		for (int i = 0; i < qnaList.size(); i++) {
			try {
				if (qnaList.get(i).getEnddate().equals("") == false
						|| qnaList.get(i).getEnddate().equals("null") == false
						|| qnaList.get(i).getEnddate() != null)
					qnaOver++;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		/*
		 * User user = (User) this.userService.selectByID(qn.getUser_id());
		 * String department = user.getDepartment(); user = new User();
		 * user.setDepartment(department); int qnaNoNum =
		 * this.userService.selectCount(user); modelMap.put("qnaNoNum",
		 * qnaNoNum);// 问卷需填数量
		 */modelMap.put("qnaNum", qnaNum); // 问卷提交数量
		/*
		 * modelMap.put("qnaoverNum", qnaOver); // 问卷提交数量
		 * modelMap.put("qnaNo-OverNum", qnaNoNum - qnaNum); // 问卷提交数量
		 */// 问题有多少人做了
		qList = qn.getSummaryQuestions();
		for (int i = 0; i < qList.size(); i++) {

			q = new SummaryQuestion();
			q = (SummaryQuestion) qList.get(i);
			qa = new SummaryQAnswer();
			qa.setQuestion_id(q.getId());
			q.setTemp5(String.valueOf(this.summaryQAnswerService
					.selectCount(qa)));
			qa = null;
			// 选项有多少人选了
			oList = q.getSummaryOptions();

			ChartModel m = new ChartModel();
			List<ChartData> listData = new ArrayList<ChartData>();
			if (oList != null) {
				for (int j = 0; j < oList.size(); j++) {
					o = new SummaryOption();
					o = (SummaryOption) oList.get(j);
					qa = new SummaryQAnswer();
					qa.setAnswertext(o.getId());
					o.setTemp5(String.valueOf(this.summaryQAnswerService
							.selectCount(qa)));
					qa = null;
					oList.set(j, o);
					if (q.getType().equals("2")) {
						Double oNum = Double.parseDouble(o.getTemp5());
						Double pNum = Double.parseDouble(q.getTemp5());
						DecimalFormat df = new DecimalFormat("#.00");
						listData.add(new ChartData(o.getTitle(), df.format(oNum
								/ pNum * 100),
								Ichartjs_Color.Ichartjs_Color_List.get(j)));
					} else if (q.getType().equals("1")) {
						listData.add(new ChartData(o.getTitle(), o.getTemp5(),
								Ichartjs_Color.Ichartjs_Color_List.get(j)));
					}

				}
				m.setListData(listData);
				q.setSummaryOptions(oList);
				q.setChartModel(m);
				qList.set(i, q);
			}

		}
		qn.setSummaryQuestions(qList);
		modelMap.put("qn", qn);
		String trueTitle = new String(title.getBytes("iso-8859-1"), "utf-8");
		modelMap.put("title", trueTitle);

		return "/summary/analysis";
	}

	/**
	 * 导出月总结文本题excle
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * @RequestMapping("/summary/textexcle.htm") public void textexcle( String
	 * id,HttpServletRequest request, HttpServletResponse response) {
	 * List<SummaryQuestion>
	 * summaryQuestionList=this.summaryQuestionService.getQuestionList(id);
	 * excelService.writeExcel(summaryQuestionList,request,response); }
	 */

	/**
	 * 导出月总结文本题excle(按班级)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/summary/textexcle.htm")
	public void textexcle(String id, HttpServletRequest request,
			HttpServletResponse response) {
		SummaryQuestion sumQuestion = new SummaryQuestion();
		sumQuestion.setQuestionnaire_id(id);
		sumQuestion.setType("3");
		List<SummaryQuestion> sumQuesList = this.summaryQuestionService
				.selectList(sumQuestion);
		List<Map<String, String>> anwserList = this.summaryQAnswerService
				.selectAnswerbyQuestionId(id);
		excelService.writeExcel(sumQuesList, anwserList, request, response);
	}

	/**
	 * 功能：ajax传递 根据年级得到实习任务 共用方法： 审批实习申请 ，信息核对 by 宋浩20160121
	 * */
	@RequestMapping("summary/user/ajaxGetPraByChangeGrade.do")
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

}
