package com.sict.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sict.course.pojo.SNSUserInfo;
import com.sict.course.pojo.WeixinOauth2Token;
import com.sict.dao.LogDao;
import com.sict.dao.StudentDao;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.Company;
import com.sict.entity.InfoCheckRecord;
import com.sict.entity.Invitation;
import com.sict.entity.Knowledge;
import com.sict.entity.Log;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.PracticeRecord;
import com.sict.entity.PracticeTask;
import com.sict.entity.Region;
import com.sict.entity.StuGraState;
import com.sict.entity.StuGraStateCount;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.entity.User;
import com.sict.service.DictionaryService;
import com.sict.service.PracticeRecordService;
import com.sict.service.PracticeTaskService;
import com.sict.service.WeixinService;
import com.sict.util.AdvancedUtil;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.Ichartjs_Color;

/*
 * 功能：管理员控制器
 * 使用 @Controller 注释
 * byccc20140910	 * 
 * 
 * */
@Controller
public class WeixinController {
	@Resource
	private WeixinService weixinService;
	@Resource
	private PracticeTaskService practiceTaskService;
	@Resource
	PracticeRecordService practiceRecordService;
	@Resource
	LogDao logDao;
	@Resource
	StudentDao studentDao;

	/*
	 * 注解请求地址(映射)--添加实习申请页面 by ccc20140910 *
	 */
	@RequestMapping("weixin/addPracticeApply.do")
	public String addPracticeApply(ModelMap modelMap, String id, String praid) {
		System.out.println(id);
		List<PracticeRecord> practice_ids = weixinService
				.selectPrecordByCheck_time(id, praid);
		if (practice_ids.size() > 0) {
			for (int i = 0; i < practice_ids.size(); i++) {
				PracticeRecord pracRe = practice_ids.get(i);
				String des = "";
				String state = pracRe.getCheck_state();
				if (state.equals("0")) {
					des = "您的实习申请："
							+ DictionaryService.findPracticeTask(
									pracRe.getPractice_id()).getTask_name()
							+ "正在审核中，请勿再次申请。";
					modelMap.put("des", des);
					ret = "weixin/practiceRecordreState";
				} else if (state.equals("1")) {
					des = "您的实习申请："
							+ DictionaryService.findPracticeTask(
									pracRe.getPractice_id()).getTask_name()
							+ "已经审核成功，请勿再次申请。";
					modelMap.put("des", des);
					ret = "weixin/practiceRecordreState";
				} else if (state.equals("2")) {
					PracticeRecord practiceRecord = new PracticeRecord();
					practiceRecord.setStu_id(id);
					PracticeRecord pr = weixinService.selectPrecordBypraid(id,
							praid);
					modelMap.put("pr", pr);
					modelMap.put("practiceRecord", practiceRecord);
					List<Company> companys = weixinService
							.selectAllCompanysCheck();
					Map<String, String> mapIndustry = new HashMap<String, String>();
					for (Company company : companys) {
						mapIndustry.put(company.getIndustry(),
								DictionaryService
										.findIndustryClassificationName(company
												.getIndustry()));
					}
					modelMap.put("mapIndustry", mapIndustry);
					List<Org> college = weixinService.selectCollege();
					modelMap.put("college", college);
					// 根据选出来只属于自己的有效的实践任务id选出对应的实践任务
					PracticeTask pt = practiceTaskService.selectByID(praid);
					modelMap.put("pt", pt);
					List<Region> regions = weixinService.selectAllRegions();
					modelMap.put("regions", regions);
					ret = "weixin/PracticeApply";
				}
			}
		} else {
			PracticeRecord practiceRecord = new PracticeRecord();
			practiceRecord.setStu_id(id);
			PracticeRecord pr = weixinService.selectPrecordBypraid(id, praid);
			modelMap.put("pr", pr);
			modelMap.put("practiceRecord", practiceRecord);
			List<Company> companys = weixinService.selectAllCompanysCheck();
			Map<String, String> mapIndustry = new HashMap<String, String>();
			for (Company company : companys) {
				mapIndustry.put(company.getIndustry(), DictionaryService
						.findIndustryClassificationName(company.getIndustry()));
			}
			modelMap.put("mapIndustry", mapIndustry);
			List<Org> college = weixinService.selectCollege();
			modelMap.put("college", college);
			// 根据选出来只属于自己的有效的实践任务id选出对应的实践任务
			PracticeTask pt = practiceTaskService.selectByID(praid);
			modelMap.put("pt", pt);
			List<Region> regions = weixinService.selectAllRegions();
			modelMap.put("regions", regions);
			ret = "weixin/PracticeApply";
		}

		return ret;

	}

	/*
	 * 注解请求地址(映射)--保存实习申请页面 ccc20140910 *
	 */
	@RequestMapping("weixin/savePracticeApply.do")
	public String savePracticeApply(ModelMap modelMap,
			HttpServletRequest request) throws IOException {
		String stu_id = request.getParameter("stu_id");
		String com_name = request.getParameter("com_name").trim();
		String post_id = request.getParameter("post_id");
		String com_orgion = request.getParameter("com_orgion").trim();
		String work_orgion = request.getParameter("work_orgion").trim();
		String com_phone = "";
		String com_teacher = "";
		String leader = request.getParameter("leader").trim();
		String is_netsign = request.getParameter("is_netsign").trim();
		String is_contract = request.getParameter("is_contract").trim();
		String pracktask_id = request.getParameter("pracktask_id");
		List<PracticeRecord> practice_ids = weixinService
				.selectPrecordByCheck_time(stu_id, pracktask_id);
		PracticeRecord prRecord = new PracticeRecord();
		Student student = DictionaryService.findStudent(stu_id);
		if (practice_ids.size() > 0) {
			prRecord = practice_ids.get(0);
		}

		// 防止重复提交表单 邢志武
		// 若有1条以上实习申请，且实习申请的状态不为2则不能再次创建申请
		if (practice_ids.size() >= 1
				&& !(prRecord.getCheck_state().equals("2"))) {
			String des = "已有实习申请，请勿重复提交！";
			modelMap.put("des", des);
			ret = "weixin/practiceRecordreState";
		} else {
			// 若实习申请未填写单位名称，则不能创建
			if (com_name.equals("1")) {
				String des = "未填写实习单位申请失败！请重新填写";
				modelMap.put("des", des);
				ret = "weixin/practiceRecordreState";
			} else {
				if (is_netsign == null) {
					is_netsign = "不确定";
				}
				if (is_contract == null) {
					is_contract = "不确定";
				}
				String practice_id = request.getParameter("praid");
				PracticeRecord pr = weixinService.selectPrecordBypraid(stu_id,
						practice_id);
				if (null == pr) {
					PracticeRecord practiceRecord = new PracticeRecord();
					practiceRecord.setCom_orgion(com_orgion);
					practiceRecord.setCom_phone(com_phone);
					practiceRecord.setCom_teacher(com_teacher);
					practiceRecord.setIs_netsign(is_netsign);
					practiceRecord.setIs_contract(is_contract);
					practiceRecord.setPractice_id(practice_id);
					practiceRecord.setCompany_id(com_name);
					practiceRecord.setPost_id(post_id);
					practiceRecord.setLeader(leader);
					practiceRecord.setWork_orgion(work_orgion);
					practiceRecord.setCheck_state("0");
					practiceRecord.setStu_id(stu_id);
					DictionaryService.updatePracticeRecord(practiceRecord);
					// 实习申请后，直接修改相应的状态
					String studentState = DictionaryService.findCompany(com_name).getTemp1();
					student.setPractice_state(studentState);
					weixinService.updatePersonal(student);
					weixinService.insertPracticeRecord(practiceRecord);
					ret = "weixin/PracticeApplySuccess";
					// 添加成功后重定向到列表页面
				} else {
					PracticeRecord practiceRecord = DictionaryService
							.findPracticeRecord(pr.getId());
					practiceRecord.setCom_orgion(com_orgion);
					practiceRecord.setCom_phone(com_phone);
					practiceRecord.setCom_teacher(com_teacher);
					practiceRecord.setIs_netsign(is_netsign);
					practiceRecord.setIs_contract(is_contract);
					practiceRecord.setPractice_id(practice_id);
					practiceRecord.setCompany_id(com_name);
					practiceRecord.setPost_id(post_id);
					practiceRecord.setLeader(leader);
					practiceRecord.setWork_orgion(work_orgion);
					practiceRecord.setCheck_state("0");
					practiceRecord.setStu_id(stu_id);
					DictionaryService.updatePracticeRecord(practiceRecord);
					weixinService.updatePracticeRecord(practiceRecord);
					// 实习申请后，直接修改相应的状态
					String studentState = DictionaryService.findCompany(
							com_name).getTemp1();
					student.setPractice_state(studentState);
					weixinService.updatePersonal(student);
					ret = "weixin/PracticeApplySuccess";
				}
			}
		}

		return ret; // 添加成功后重定向到列表页面

	}

	/*
	 * 注解请求地址(映射)--新增实习单位页面 by ccc 20141107 *
	 */
	@RequestMapping("weixin/addCompany.do")
	public String addCompany(ModelMap modelMap, String stu_id, String infos) {
		System.out.println(stu_id);
		DictionaryService.findStudent(stu_id).getClass_id();
		Company company = new Company();
		String org_id = weixinService.selectCollegeBystu_id(DictionaryService
				.findStudent(stu_id).getClass_id());
		// 学院id存进使用范围
		company.setApplicable_scope(org_id);
		company.setCheck_state("0");
		// 行业范围直接在前台取key值
		modelMap.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		modelMap.put("company", company);
		return "weixin/CompanyAdd";
	}

	String ret;

	/*
	 * 注解请求地址(映射)--保存新增实习单位页面 by ccc20141107 *
	 */
	@RequestMapping("weixin/saveCompany.do")
	public String saveCompany(@ModelAttribute("company") Company company)
			throws IOException {
		DictionaryService.updateCompany(company);
		int i = weixinService.insertCompany(company);

		if (i == 1) {
			ret = "weixin/CompanySuccess"; // 添加成功后重定向到列表页面
		} else {
			ret = "";
		}
		return ret;
	}

	/*
	 * 转发到专家提问页面 邢志武 2015年6月6日 15:34:35
	 */
	@RequestMapping("weixin/expertQuiz.do")
	public String toExpertQuiz(ModelMap modelMap, HttpServletRequest request) {
		String stu_id = request.getParameter("stu_id");
		List<Org> college = weixinService.selectCollege();
		modelMap.put("college", college);
		modelMap.put("stu_id", stu_id);
		return "weixin/expertQuiz";
	}

	/*
	 * 注解请求地址(映射)--信息 核对页面 ccc20141121 *
	 */
	@RequestMapping("weixin/checkInformation.do")
	public String checkInformation(ModelMap modelMap, String stu_id,
			String infos, String infoid) {
		System.out.println(stu_id);
		System.out.println(infos);
		modelMap.put("stu_id", stu_id);
		modelMap.put("checktask_id", infos);
		modelMap.put("infoid", infoid);
		return "weixin/InfoEdit";
	}

	/*
	 * 注解请求地址(映射)--保存信息核对页面 ccc20141107 *
	 */
	@RequestMapping("weixin/saveInfo.do")
	public String saveInfo(HttpServletRequest request) throws IOException {
		String checktask_id = request.getParameter("checktask_id");
		System.out.println("ccccccc" + checktask_id);
		String note = request.getParameter("note");
		String check_result = request.getParameter("check_result");
		String stu_id = request.getParameter("stu_id");
		String infoid = request.getParameter("infoid");
		InfoCheckRecord info = DictionaryService.findInfoCheckRecord(infoid);
		info.setChecktask_id(infoid);
		info.setNote(note);
		info.setCheck_result(check_result);
		info.setStu_id(stu_id);
		weixinService.updateInfo(info);
		DictionaryService.updateInfoCheckRecord(info);
		return "weixin/checksuccess"; // 保存成功后关闭窗口
	}

	/*
	 * 注解请求地址(映射)--实习记录页面 by ccc20141208 *
	 */
	@RequestMapping("weixin/practiceState.do")
	public String employmentIntention(ModelMap modelMap, String stu_id,
			String praid, String pd) {
		System.out.println(stu_id);
		modelMap.put("practicerecord", pd);
		System.out.println("cvcxvv" + pd);
		PracticeTask pt = practiceTaskService.selectByID(praid);
		modelMap.put("pt", pt);
		modelMap.put("stu_id", stu_id);
		return "weixin/PracticeState";
	}

	/*
	 * 注解请求地址(映射)--修改实习记录by ccc 20141107 *
	 */
	@RequestMapping("weixin/savePracticeState.do")
	public String savePracticeState(HttpServletRequest request)
			throws IOException {
		String practicerecord = request.getParameter("practicerecord");
		String stu_id = request.getParameter("stu_id");
		String work_time = request.getParameter("work_time");
		String prct_contract_time = request.getParameter("prct_contract_time");
		String netsign_time = request.getParameter("starttime");
		String contract_time = request.getParameter("contract_time");
		String dimission_time = request.getParameter("dimission_time");
		// 将字符串的时间格式化Timestamp类型
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PracticeRecord practicerecords = DictionaryService
				.findPracticeRecord(practicerecord);
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
				pct = new Timestamp(format.parse(prct_contract_time).getTime());
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
		if (i == 1) {
			ret = "weixin/PracticeStatesuccess"; // 修改成功后
		}
		return ret;
	}

	/*
	 * 注解请求地址(映射)--修改个人资料页面 by ccc20141221 *
	 */
	@RequestMapping("weixin/updatePersonal.do")
	public String updatePersonal(ModelMap modelMap, String stu_id) {
		System.out.println(stu_id);
		String id = stu_id;
		Student stu = (Student) weixinService.selectStuByStu_id(id);
		modelMap.put("stu", stu);
		return "weixin/updatePersonal";
	}

	/*
	 * 注解请求地址(映射)--保存信息核对页面 ccc20141107 *
	 */
	@RequestMapping("weixin/savePersonal.do")
	public String savePersonal(HttpServletRequest request) throws IOException {
		String stu_code = request.getParameter("stu_code");
		String qqnum = request.getParameter("qqnum");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String home_addr = request.getParameter("home_addr");
		String home_phone = request.getParameter("home_phone");
		String login_pass = request.getParameter("login_pass");
		Student stu = DictionaryService.findStudentByCode(stu_code);// 通过学号查询学生对象，修改王磊
		stu.setQqnum(qqnum);
		stu.setEmail(email);
		stu.setPhone(phone);
		stu.setStu_code(stu_code);
		stu.setLogin_pass(login_pass);
		stu.setHome_addr(home_addr);
		stu.setHome_phone(home_phone);
		weixinService.updatePersonal(stu);
		return "weixin/updatePersonsuccess"; // 保存成功后关闭窗口
	}

	/**
	 * 功能：ajax传递 by ccc20150117
	 * 
	 * @throws UnsupportedEncodingException
	 *             *
	 * */
	@RequestMapping("weixin/ajaxCompany.do")
	public String ajaxCompany(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		/*
		 * String tj = request.getParameter("tiaojian"); int a =
		 * tj.indexOf(","); String applicable_scope = tj.substring(0, a); String
		 * industry = tj.substring(a + 1, tj.length()); List<Company> pt =
		 * this.weixinService.getCompany(applicable_scope, industry);
		 * StringBuffer sb = new StringBuffer();
		 * sb.append("<select id='com_name' name='com_name'>");
		 * sb.append("<option >请选择实习单位</option>"); for (Company com : pt) {
		 * sb.append("<option " + "value=" + com.getId() + ">" +
		 * com.getCom_name() + "</option>"); } try {
		 * response.getWriter().println(sb.toString()); } catch (IOException e)
		 * { e.printStackTrace(); } return null;
		 */
		String companyName = request.getParameter("companyName");
		String trueCompanyName = new String(companyName.getBytes("iso-8859-1"),
				"utf-8");
		List<Company> pt = this.weixinService.getSomeCompanys(trueCompanyName);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='com_name' name='com_name'>");
		sb.append("<option value='1' >请选择实习单位</option>");
		for (Company com : pt) {
			sb.append("<option " + "value=" + com.getId() + ">"
					+ com.getCom_name() + "</option>");
		}
		if (pt.size() > 29) {
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
	 * 功能：ajax传递院系 邢志武 2015年5月27日 10:26:47
	 * */
	@RequestMapping("weixin/ajaxDepartment.do")
	public String ajaxDepartment(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String college_id = request.getParameter("college");
		List<Org> department = this.weixinService
				.getDepartmentBycollege(college_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='Department' name='Department'>");
		sb.append("<option >请选择院系</option>");
		for (Org dep : department) {
			sb.append("<option " + "value=" + dep.getId() + ">"
					+ dep.getOrg_name() + "</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax传递 特长 邢志武 2015年5月27日 10:26:47
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("weixin/ajaxMajor.do")
	public String ajaxMajor(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		String college_id = request.getParameter("Department");
		String trueCollege_id = new String(college_id.getBytes("iso-8859-1"),
				"utf-8");
		List<String> speciality = this.weixinService
				.getSpecialityBycollege(trueCollege_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='major' name='major'>");
		sb.append("<option >请选择领域</option>");
		for (int i = 0; i < speciality.size(); i++) {
			sb.append("<option " + "value=" + speciality.get(i) + ">"
					+ speciality.get(i) + "</option>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax传递 专家 邢志武 2015年5月27日 10:26:47
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("weixin/ajaxExpert.do")
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

	// 保存问题 专家指导问题 邢志武 2015年6月9日 10:51:35
	@RequestMapping("weixin/saveExpertQuiz.do")
	public String saveExpertQuiz(HttpServletRequest request) {
		String stu_id = request.getParameter("stu_id");
		String expert = request.getParameter("expert");
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
		return "weixin/expertQuizSuccess"; // 保存成功后关闭窗口
	}

	// 查询某个学生专家指导问题 邢志武 2015年6月9日 10:51:35
	@RequestMapping("weixin/toQueryExpertQuiz.do")
	public String toQueryExpertQuiz(HttpServletRequest request,
			ModelMap modelMap) {
		String question_id = request.getParameter("question_id");
		Knowledge knowledge = this.weixinService
				.getQustionAnswerByID(question_id);
		modelMap.put("knowledge", knowledge);
		return "weixin/expertQuizAnswer"; // 保存成功后关闭窗口
	}

	// 保存学生对专家回复的评价 邢志武 2015年6月9日 10:51:35
	@RequestMapping("weixin/saveEvaluateQuiz.do")
	public String saveEvaluateQuiz(HttpServletRequest request, ModelMap modelMap) {
		String qusertion_id = request.getParameter("qusertion_id");
		String evaluate = request.getParameter("evaluate");
		int answer_score = Integer.parseInt(evaluate);
		Knowledge knowledge = this.weixinService
				.getQustionAnswerByID(qusertion_id);
		knowledge.setAnswer_score(answer_score);
		this.weixinService.updateQustionAnswerByID(knowledge);
		return "weixin/expertQuizAnswerSuccess"; // 保存成功后关闭窗口
	}

	// 跳转到企业老师维护页面 邢志武 2015年6月14日 11:21:25
	@RequestMapping("weixin/toComTeacherMaintain.do")
	public String toComTeacherMaintain(HttpServletRequest request,
			ModelMap modelMap) {
		String stu_id = request.getParameter("stu_id");
		String stuName = DictionaryService.findStudent(stu_id).getTrue_name();
		PracticeRecord practiceRecord = this.weixinService
				.getPrecordUndimission_time(stu_id);
		String com_id = practiceRecord.getCompany_id();
		String comName = DictionaryService.findCompany(com_id).getCom_name();
		Teacher t = new Teacher();
		t.setDept_id(com_id);
		List<Teacher> comTeachers = this.weixinService.getTeachersByCom_id(t);
		modelMap.put("comTeachers", comTeachers);
		modelMap.put("stuName", stuName);
		modelMap.put("com_id", com_id);
		modelMap.put("stu_id", stu_id);
		modelMap.put("comName", comName);
		return "weixin/comTeacherMaintain"; // 保存成功后关闭窗口
	}

	// 企业老师邢志武 2015年6月14日 11:21:25
	@RequestMapping("weixin/saveComTeaccher.do")
	public String saveComTeaccher(HttpServletRequest request) {
		String comTeacher = request.getParameter("comTeacher");
		String stu_id = request.getParameter("stu_id");
		String com_id = request.getParameter("com_id");
		String com_code = "com_code";
		String comTeaherName = request.getParameter("comTeaherName");
		String comTeacherSex = request.getParameter("comTeacherSex");
		String comTeaherPhone = request.getParameter("comTeaherPhone");
		Student student = DictionaryService.findStudent(stu_id);
		// 如果两种方式都不选择，默认什么也不操作，解决存储空的现象
		if (comTeacher.equals("1") && comTeaherName == "") {
			System.out.println("什么也不操作！");
		}
		/* 判断是新增还是选择企业老师 */
		else if (comTeacher.equals("1") && comTeaherName != "") {
			// 教师id更新到学生表企业教师字段
			/* 创建新的企业老师 */
			Teacher newComTeacher = new Teacher();
			String tea_id = Common.returnUUID16();
			newComTeacher.setId(tea_id);
			newComTeacher.setTrue_name(comTeaherName);
			newComTeacher.setDept_id(com_id);
			newComTeacher.setState("3");
			newComTeacher.setSex(comTeacherSex);
			newComTeacher.setPhone(comTeaherPhone);
			// teacode暂时用公司code
			newComTeacher.setTea_code(com_code);
			newComTeacher.setTea_type("2");
			this.weixinService.insertComTeacher(newComTeacher);
			student.setCom_teacher_id(tea_id);
			this.weixinService.updatePersonal(student);
		} else {
			// 已有的企业老师
			student.setCom_teacher_id(comTeacher);
			this.weixinService.updatePersonal(student);
		}

		return "weixin/comTeacherMaintainSuccess"; // 保存成功后关闭窗口
	}

	// 跳转到查看教师所带学生页面 邢志武 2015年6月25日 10:34:11
	@RequestMapping("weixin/selectTeaGroupStus.do")
	public String selectTeaGroupStus(HttpServletRequest request,
			ModelMap modelMap) {
		String tea_id = request.getParameter("tea_id");
		List<PracticeTask> outSchollPracticeTask = this.practiceTaskService
				.selectOutSchoolPracticeTasks(tea_id);
		modelMap.put("outSchollPracticeTask", outSchollPracticeTask);
		return "weixin/TeaGroupStus"; // 保存成功后关闭窗口
	}

	/**
	 * 功能：ajax 获取校外实习学生信息 邢志武 2015年6月25日 11:09:14
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("weixin/ajaxGetStudents.do")
	public String ajaxGetStudents(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		String practiceTask_id = request.getParameter("practiceTask_id");
		String truePracticeTask_id = new String(
				practiceTask_id.getBytes("iso-8859-1"), "utf-8");
		List<String> stusId = this.practiceTaskService
				.selectStusId(truePracticeTask_id);
		StringBuffer sb = new StringBuffer();
		if (stusId.size() > 0) {

			for (int i = 0; i < stusId.size(); i++) {
				String stu_id = stusId.get(i);
				String class_id = DictionaryService.findStudent(stu_id)
						.getClass_id();
				String stu_name = DictionaryService.findStudent(stu_id)
						.getTrue_name();
				String className = DictionaryService.findOrg(class_id)
						.getOrg_name();
				String phone = DictionaryService.findStudent(stu_id).getPhone();
				sb.append("<h4>学生信息" + (i + 1) + "</h4><p>班级：" + className
						+ "</p><p>姓名：" + stu_name + "</p><p>电话：" + phone
						+ "</p>");
			}
		} else {
			sb.append("	该任务没有成员");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 跳转到查看公告页面 邢志武 2015年6月30日
	@RequestMapping("weixin/selectNotices.do")
	public String selectNotices(HttpServletRequest request, ModelMap modelMap) {
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
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
		List<Notice> noticeList = this.weixinService.getNoticesList(n);
		modelMap.put("noticeList", noticeList);
		return "weixin/noticeList"; // 保存成功后关闭窗口
	}

	// 跳转到查看公告详情页面 邢志武 2015年6月30日
	@RequestMapping("weixin/toOneNotice.do")
	public String toOneNotice(HttpServletRequest request, ModelMap modelMap) {
		String notice_id = request.getParameter("notice_id");
		Notice notice = this.weixinService.getNotice(notice_id);
		modelMap.put("notice", notice);
		return "weixin/oneNotice"; // 保存成功后关闭窗口
	}

	// 跳转到指导回复页面 邢志武 2015年6月30日
	@RequestMapping("weixin/answerQuestion.do")
	public String answerQuestion(HttpServletRequest request,
			HttpSession session, ModelMap modelMap) {
		String tea_id = request.getParameter("tea_id");
		List<Knowledge> knowledges = this.weixinService
				.getKnowledgeList(tea_id);
		modelMap.put("knowledges", knowledges);
		return "weixin/answerQuestion"; // 保存成功后关闭窗口
	}

	// 跳转到查看公告详情页面 邢志武 2015年6月30日
	@RequestMapping("weixin/toOneAnswerQuersiton.do")
	public String toOneAnswerQuersiton(HttpServletRequest request,
			ModelMap modelMap) {
		String knowledge_id = request.getParameter("knowledge_id");
		Knowledge knowledge = this.weixinService.getKnowledge(knowledge_id);
		modelMap.put("knowledge", knowledge);
		return "weixin/oneknowledge"; // 保存成功后关闭窗口
	}

	// 转发回答问题页面，专家指导，邢志武 2015年6月9日 10:14:32
	@RequestMapping("weixin/doSavaShortQuestion.do")
	public String doSavaShortQuestion(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String answer = request.getParameter("answer");
		String question_id = request.getParameter("question_id");
		String tea_id = request.getParameter("tea_id");
		Date now = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp answerQuestionTime = new Timestamp(System.currentTimeMillis());
		answerQuestionTime = new Timestamp(now.getTime());
		Knowledge knowledge = this.weixinService.selectById(question_id);
		knowledge.setAnswer_time(answerQuestionTime);
		knowledge.setAnswer(answer);
		this.weixinService.updateQustionAnswerByID(knowledge);
		return "redirect:answerQuestion.do?tea_id=" + tea_id;

	}

	// 转发到签到情况，邢志武 2015年6月9日 10:14:32
	@RequestMapping("weixin/BmapPie.do")
	public String BmapPie(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		// 默认年份
		String grade = Common.getDefaultYear();
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		// 获取当天该学院签到学生的比例
		double SignPro = practiceTaskService.getCollegeDaySignPro(dept_id,
				grade);
		// 获取当天该学院未签到学生的比例
		double unSignPro = 1 - SignPro;
		String SignProString = Double.toString(SignPro);
		String unSignProString = Double.toString(unSignPro);
		ChartData BmapIchar = new ChartData();
		BmapIchar.setName("1");
		BmapIchar.setText("当天签到的人");
		BmapIchar.setValue(SignProString);
		BmapIchar.setColor("#CCFFFF");
		ChartData BmapIchar2 = new ChartData();
		BmapIchar2.setName("2");
		BmapIchar2.setValue(unSignProString);
		BmapIchar2.setColor("#FFFF99");
		BmapIchar2.setText("当天未签到的人");
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		BmapIcharList.add(BmapIchar2);
		BmapIcharList.add(BmapIchar);
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonDataNew();
		modelMap.put("deptName", deptName);
		modelMap.put("grade", grade);
		modelMap.put("cm", json);
		modelMap.put("tea_id", tea_id);
		return "weixin/BmapPie"; // 保存成功后关闭窗口
	}

	// 转发到就业情况，邢志武 2015年6月9日 10:14:32
	@RequestMapping("weixin/bMapSignState.do")
	public String tobMapSignState(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		// 默认年份
		String grade = Common.getDefaultYear();
		String stateName = request.getParameter("stateName");
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
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
		// List<Org> departments = orgService.selectByXyId(dept_id);
		List<Org> departments = this.weixinService.selectByXueYuanId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			double scale = practiceTaskService.getDepartmentDaySignPro(org_id,
					grade);
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - scale;
				sb.append(deptName + grade + "学生今日未签到情况");
			}
			sb.append(deptName + grade + "学生今日签到情况 ");
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
		modelMap.put("title", title);
		modelMap.put("grade", grade);
		modelMap.put("cm", json);
		return "weixin/bMapSignState"; // 保存成功后关闭窗口
	}

	// 转发到签到情况，邢志武 2015年6月9日 10:14:32
	@RequestMapping("weixin/employmentState.do")
	public String employmentState(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院,通过组织表的联系人id找到负责的学院，限制一个教师只负责一个学院
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
		String college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(college_id).getOrg_level();
		if (org_level.equals("3")) {
			college_id = DictionaryService.findOrg(college_id).getParent_id();
		}
		// 存入session 供其他模块使用
		session.setAttribute("college_id", college_id);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		// 获取单个学院各项状态人数
		mapParam.put("grade", grade);
		mapParam.put("deptId", college_id);
		List<StuGraStateCount> listStuStateCount = weixinService
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
		String college_name = DictionaryService.findOrg(college_id)
				.getOrg_name();
		modelMap.put("cm", jsonData);
		modelMap.put("grade", grade);
		modelMap.put("tea_id", tea_id);
		modelMap.put("college_name", college_name);
		return "weixin/index"; // 保存成功后关闭窗口
	}

	/**
	 * 功能：转到系部实习状态图表
	 * 
	 * */
	@RequestMapping("weixin/toPracticeStateChartDept.do")
	public String toPracticeStateChartDept(ModelMap modelMap,
			HttpSession session, HttpServletRequest request) {
		// 获取状态编码
		String curStateCode = request.getParameter("stateCode");
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
		String dept_id = tea.getDept_id();
		String college_id = Common.getCollegeId(dept_id);
		// 各系各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = weixinService.getOrgDeptByCollegeId(college_id);
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = weixinService
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
					}
				}
		}
		ChartModel cm = new ChartModel();
		// 将新生成的数据转成json，在图表在显示
		cm = Ichartjs_Color.getChartModelByPercent(listMapDeptStuState);
		String jsonData = cm.getJsonData();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		String college_Name = DictionaryService.findOrg(college_id)
				.getOrg_name();
		modelMap.put("stateName", stateName);
		modelMap.put("college_name", college_Name);
		modelMap.put("cm", jsonData);
		modelMap.put("grade", grade);
		return "weixin/practiceStateChartDept"; // 保存成功后关闭窗口
	}

	/**
	 * 
	 * @param session
	 * @param request
	 * @return 查看学院任务完成度 xzw 2015年7月22日 09:55:14
	 */
	@RequestMapping("weixin/taskAccomplishAll.do")
	public String toTaskAccomplishAll(ModelMap modelMap, HttpSession session,
			HttpServletRequest request) {
		String grade = Common.getDefaultYear();
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
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
		Map<String, Double> CollegeTaskAccomplish = this.weixinService
				.getCollegeTaskAccomplish(dept_id, grade);
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
		modelMap.put("title", deptName + grade + "级" + "任务完成情况");
		modelMap.put("grade", grade);
		modelMap.put("cm", json);
		modelMap.put("tea_id", tea_id);
		return "weixin/taskAccomplishAll"; // 保存成功后关闭窗口
	}

	/**
	 * 查看各系任务完成度 邢志武2015年7月22日 09:55:02
	 */
	@RequestMapping("weixin/taskAccomplishDepartments.do")
	public String taskAccomplishDepartments(ModelMap modelMap,
			HttpSession session, HttpServletRequest request) {
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
		String tea_id = request.getParameter("tea_id");
		Teacher tea = DictionaryService.findTeacher(tea_id);
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
		List<Org> departments = this.weixinService.selectByXyId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			// 某系校外实习成绩优秀率
			Map<String, Double> departementTaskAccomplish = this.weixinService
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
		modelMap.put("title", title);
		modelMap.put("grade", grade);
		modelMap.put("cm", json);
		return "weixin/taskAccomplishDepartment";
	}

	// bbs论坛入口
	@RequestMapping("weixin/szPostBar.do")
	public String OauthTest(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap)
			throws IOException, ServletException {
		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil
					.getOauth2AccessToken(Constants.appid, Constants.AppSecret,
							code);
			// 网页授权接口访问凭证
			String accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken,
					openId);
			String phoneUrl = snsUserInfo.getHeadImgUrl();

			List<Teacher> teaList = this.weixinService.getTeacherByWX(openId);
			Teacher tea;
			Student stu;
			// 是否是教师用户
			if (teaList.size() != 0) {
				tea = teaList.get(0);
				session.setAttribute("current_user", tea);
				session.setAttribute("type", "tea");
				session.setAttribute("phoneUrl", phoneUrl);
			} else {
				List<Student> stuList = this.weixinService
						.getStudentByWX(openId);
				// 学生绑定用户
				if (stuList.size() != 0) {
					stu = stuList.get(0);
					session.setAttribute("current_user", stu);
					session.setAttribute("type", "stu");
					stu.setPhoto_id(phoneUrl);
					this.weixinService.updateStudentPhoto(stu);
				}
				// 没有绑定的用户
				else {
					session.setAttribute("current_user", null);
					modelMap.put("user", null);
					session.setAttribute("type", "null");
					return "bbs/user/bbsbdErroy";
				}
			}
		}
		return "bbs/user/index";
	}

	// 测试教师端手机页面
	@RequestMapping("weixin/teacherSjjx.do")
	public String teacherSjjx(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");
		String openId = Common.getOpenId(code);
		if (openId.length() > 0) {
			List<Teacher> teaList = this.weixinService.getTeacherByWX(openId);
			if (teaList.size() > 0) {
				Teacher tea = teaList.get(0);
				session.setAttribute("current_user", tea);
				session.setAttribute("current_user_type", "teacher");
				session.setAttribute("nowUserRole", "ROLE_TEACHER");
				return "redirect:../MobileTeacher/index.do";
			} else {
				return "redirect:../MobileTeacher/erroy.do";
			}

		} else {
			return "redirect:../MobileTeacher/erroy.do";
		}
	}

	@RequestMapping("weixin/bd_student.do")
	public String bd_student(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String wx_code = request.getParameter("wx_code");
		session.setAttribute("bd_wx_code", wx_code);
		return "weixin/stuBind";
	}

	@RequestMapping("weixin/bd_teacher.do")
	public String bd_teacher(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String wx_code = request.getParameter("wx_code");
		session.setAttribute("bd_wx_code", wx_code);
		return "weixin/teaBind";
	}

	@RequestMapping("weixin/sxzt.do")
	public String sxzt(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String wx_code = request.getParameter("wx_code");
		TreeMap<String, StuGraState> mapState = new TreeMap<String, StuGraState>();
		mapState = DictionaryService.getStuGraStateName();
		List<Student> stulist = weixinService.getStudentByWX(wx_code);
		Map<String, String> map = new HashMap<String, String>();
		Student stu = null;
		if (stulist.size() > 0) {
			stu = stulist.get(0);
		}
		for (String key : mapState.keySet()) {
			String val = key + " " + mapState.get(key).getStateDesc();
			map.put(key, val);
		}
		String state = DictionaryService.findStuGraStateName(
				stu.getPractice_state()).getStateDesc();
		modelMap.put("stu", stu);
		modelMap.put("state", state);
		modelMap.put("mapState", map);
		return "weixin/sxzt";
	}

	// 修改实习状态
	@RequestMapping("weixin/updateSxzt.do")
	public String updateSxzt(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		//实习状态编码 
		String code = request.getParameter("code");
		Student student = DictionaryService.findStudent(id);
		PracticeRecord pracRec = this.practiceRecordService
				.getValidPracticeTask(id);
		if (code.equals("130") && pracRec == null) {
			modelMap.put("result", "您还没有实习申请，不能修改实习状态");
			return "weixin/sxztResult";
		} else {
			//1 默认没有修改
			if (code.equals("1")) {
				modelMap.put("result", "实习状态修改成功");
				return "weixin/sxztResult";
			} else {
				student.setPractice_state(code);	
				// 邢志武修改 更新日志表(记录学生修改实习状态过程，实习记录表里是直接更新，没有保存状态修改过程) 2015年6月17日 19:58:55
			/*	String stu_trueName = DictionaryService.findStudent(id).getTrue_name();
			 
				List<PracticeTask> practiceTask = this.practiceTaskService.getTaskByStuIdAndType(id, "1");
				String practiceTask_id = practiceTask.get(0).getId();
				int logCount = logDao.getStuLogCont(id);
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
				// 生成新的日志信息
				Log newLog = new Log();
				newLog.setStu_id(id);
				newLog.setPractice_id(practiceTask_id);
				newLog.setLog_type("1");
				newLog.setPractice_state(code);
				newLog.setUser_id(stu_trueName);
				newLog.setOperate_time(time);
				if (logCount != 0) {
					// 存在以前的日志信息，现更新，后插入
					Log oldLog = this.logDao.getOldLog(id);
					oldLog.setEnd_time(time);
					this.logDao.update(oldLog);
					this.logDao.insert(newLog);
				} else {
					// 不存在日志信息，直接插入新的数据
					this.logDao.insert(newLog);
				}*/
				int i = studentDao.updatePractice(student);
				if (i> 0) {
					//student.setPractice_state(code);
					DictionaryService.updateStudent(student);
					//DictionaryService.findStuGraStateName(code).getStateDesc();
				} else {
					// respContent = "请输入正确的数字序号";
					modelMap.put("result", "实习状态修改失败");
					return "weixin/sxztResult";
				}
			}
		}
		modelMap.put("result", "实习状态修改成功");
		return "weixin/sxztResult";
	}

	/**
	 * 绑定微信
	 * 
	 * @param modelMap
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("weixin/bd.do")
	public String bd(ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		HashMap<String, String> map = new HashMap<String, String>();
		String type = request.getParameter("type");
		map.put("wx_code", (String) session.getAttribute("bd_wx_code"));
		map.put("type", type);
		String result = "";
		if (type.equals("1")) {
			//服务器部署用这个，编码转为utf-8
			String stu_name = Common.xxToUTF_8(request.getParameter("stu_name"));
			/*String stu_name = request.getParameter("stu_name");*/
			String stu_code = Common.xxToUTF_8(request.getParameter("stu_code"));
			String stu_id = Common.xxToUTF_8(request.getParameter("stu_id"));
			String stu_password = Common.xxToUTF_8(request.getParameter("stu_password"));
			map.put("stu_name", stu_name);
			map.put("stu_code", stu_code);
			map.put("stu_id", stu_id);
			map.put("stu_password", stu_password);
			result = weixinService.bdUser(map);
		} else {
			String tea_name = Common
					.xxToUTF_8(request.getParameter("tea_name"));
			String tea_code = Common
					.xxToUTF_8(request.getParameter("tea_code"));
			String tea_password = Common.xxToUTF_8(request
					.getParameter("tea_password"));
			map.put("tea_name", tea_name);
			map.put("tea_code", tea_code);
			map.put("tea_password", tea_password);
			result = weixinService.bdUser(map);
		}
		if (result.equals("true")) {
			return "weixin/bdSuccess";
		} else {
			return "weixin/bdErrroy";
		}
	}
}
