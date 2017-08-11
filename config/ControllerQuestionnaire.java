package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import com.sict.authority.AuthorityType;
import com.sict.authority.FireAuthority;
import com.sict.authority.Login;
import com.sict.common.CommonSession;
import com.sict.common.Constant;
import com.sict.dao.QnAnswerDao;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.GroupMembers;
import com.sict.entity.Option;
import com.sict.entity.Org;
import com.sict.entity.QAnswer;
import com.sict.entity.QnAnswer;
import com.sict.entity.Question;
import com.sict.entity.QuestionID;
import com.sict.entity.Questionnaire;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.entity.User;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.OptionService;
import com.sict.service.OrgService;
import com.sict.service.QAnswerService;
import com.sict.service.QnAnswerService;
import com.sict.service.QuestionService;
import com.sict.service.QuestionnaireService;
import com.sict.service.StudentService;
import com.sict.service.TeaStuService;
import com.sict.service.UserService;
import com.sict.util.Common;
import com.sict.util.HttpRequest;
import com.sict.util.Ichartjs_Color;

@Controller
public class ControllerQuestionnaire {
	@Resource(name = "QuestionnaireService")
	private QuestionnaireService questionnaireService;
	@Resource(name = "QnAnswerService")
	private QnAnswerService qnaService;
	@Resource(name = "QAnswerService")
	private QAnswerService qaService;
	@Resource(name = "QuestionService")
	private QuestionService questionService;
	@Resource(name = "OptionService")
	private OptionService optionService;
	@Resource(name = "orgService")
	private OrgService orgService;
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	@Resource(name = "QAnswerService")
	private QAnswerService qanswerService;
	@Resource(name = "studentService")
	private StudentService studentService;
	@Resource(name = "teaStuService")
	private TeaStuService teaStuService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "groupMembersService")
	private GroupMembersService groupMembersService;

	@RequestMapping(value = "/wjdc/questionnaire/{id}/text.htm")
	public ModelAndView to_text(HttpSession session, ModelMap modelMap,
			@PathVariable String id, String page) {

		Questionnaire qn = (Questionnaire) this.questionnaireService
				.selectByID(id);
		QuestionID qid = new QuestionID();
		qid.setId(this.questionService.selectAllIdByQn_id(qn.getId()));
		qid.setNum(0);
		List<QuestionID> list_qid = new ArrayList<QuestionID>();
		list_qid.add(qid);

		session.setAttribute("list_qid", list_qid);
		session.setAttribute("qn", qn);

		String user_id = "";
		String user_type = "";
		// 获取用户信息
		user_type = (String) session.getAttribute("current_user_type");
		// 判断用户是否存在
		if ("teacher".equals(user_type)) {
			Teacher t = (Teacher) session.getAttribute("current_user");
			user_id = t.getId();
		} else if ("student".equals(user_type)) {
			Student s = (Student) session.getAttribute("current_user");
			user_id = s.getId();
		} else {
			modelMap.put("result", "您还没有登录");
			return new ModelAndView("welcome");
		}
		System.out.println(qn.getTemp2());
		if (user_type.equals(qn.getTemp2()) == false) {
			modelMap.put("result", "很抱歉，你不符合此问卷答题的条件");
			return new ModelAndView("welcome");
		}

		// 判断判断问卷是否公开，并是否可以填写问卷的条件
		// temp1存储问卷可以填写的单位
		if (null != qn.getTemp1() || "".equals(qn.getTemp1()) == true) {
			// User dp_user = (User)
			// this.userService.selectByID(qn.getUser_id());
			// if (user.getDepartment().equals(dp_user.getDepartment()) ==
			// false) {
			// modelMap.put("result", "你不符合本问卷调查的条件,谢谢");
			// return new ModelAndView("welcome");
			// }
		}

		// 从数据库中查询提交结果
		QnAnswer qna = new QnAnswer();
		qna.setQuestionnaire_id(id);
		qna.setUser_id(user_id);

		// 查看问卷是否已经，根据时间判断
		if (this.qnaService.selectQnAnswerCount(qna) == 1) {
			qna = this.qnaService.getSelectQnAnswer(qna).get(0);
			if (null == qna.getEnddate() || "".equals(qna.getEnddate())) {
				// 删除问卷答过的问题
				// QAnswer qa = new QAnswer();
				// qa.setQnanswer_id(qna.getId());
				// this.qaService.deleteQAnswerByAll(qa);

			} else {
				modelMap.put("result", "问卷已经被完成,谢谢");
				return new ModelAndView("welcome");
			}
		}
		// 如果不存在，则创建一份提交结果
		else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			qna.setStartdate(df.format(new Date()).toString());
			qna = this.qnaService.insertQnAnswer(qna);
		}

		session.setAttribute("qna", qna);

		String q_id = null;
		for (int i = list_qid.size() - 1; i <= 0; i--) {

			qid = new QuestionID();
			qid = list_qid.get(i);
			q_id = qid.getId().get(qid.getNum());
			qn.setqNum(qid.getId().size());
			break;

		}
		modelMap.put("qn", qn);
		return new ModelAndView("redirect:/wjdc/question/" + qn.getId() + "/"
				+ q_id + ".htm");

	}

	

	@Login
	@RequestMapping("/wjdc/questionnaire/admin.htm")
	public ModelAndView admin(HttpSession session,ModelMap modelMap) {

		User user = CommonSession.getUser(session, userService);
		Questionnaire qn = new Questionnaire();
		qn.setUser_id(user.getId());
		qn.setTemp3(Questionnaire.qn_wjdc);
		List<Questionnaire> list = questionnaireService.selectList(qn);
		modelMap.put("wenjuan", list);
		return new ModelAndView("wjdc/users/admin", "wenjuan", list);

	}

	
	


	@Login
	@RequestMapping("/wjdc/questionnaire/teacher_answer.htm")
	public ModelAndView teacher_admin(HttpSession session) {

		Teacher t = (Teacher) session.getAttribute("current_user");
		String user_id = t.getId();
		String user_org_id = t.getDept_id();
		Questionnaire qn_temp = new Questionnaire();
		qn_temp.setTemp2("teacher");
		qn_temp.setTemp3(Questionnaire.qn_wjdc);
		List<Questionnaire> qn_list = this.questionnaireService
				.selectList(qn_temp);
		for (int i = 0; i < qn_list.size(); i++) {
			Questionnaire qn = new Questionnaire();
			qn = qn_list.get(i);
			if ("teacher".equals(qn.getTemp2()) != true) {
				continue;
			}
			String org_id = qn.getTemp1();
			if (Common.isOrgTure(orgService, user_org_id, org_id)==false) {
				qn_list.remove(i);
			}
		}
		return new ModelAndView("wjdc/users/student", "wenjuan", qn_list);
	}


	@Login
	@RequestMapping("/wjdc/questionnaire/teacher.htm")
	public ModelAndView teacher(HttpSession session, ModelMap modelMap) {

		Teacher t = (Teacher) session.getAttribute("current_user");
		String user_id = t.getId();
		Questionnaire qn = new Questionnaire();
		qn.setUser_id(user_id);
		qn.setTemp3(Questionnaire.qn_wjdc);
		List list = questionnaireService.selectList(qn);
		modelMap.put("wenjuan", list);

		return new ModelAndView("wjdc/users/admin");

	}

	

	@Login
	@RequestMapping("/wjdc/questionnaire/student.htm")
	public ModelAndView student(HttpSession session) {

		Student s = (Student) session.getAttribute("current_user");
		String user_org_id = s.getClass_id();
		Questionnaire qn_temp = new Questionnaire();
		qn_temp.setTemp2("student");
		qn_temp.setTemp3(Questionnaire.qn_wjdc);
		List<Questionnaire> qn_list = this.questionnaireService
				.selectList(qn_temp);
		for (int i = 0; i < qn_list.size(); i++) {
			Questionnaire qn = new Questionnaire();
			qn = qn_list.get(i);
			if ("student".equals(qn.getTemp2()) != true) {
				continue;
			}
			String org_id = qn.getTemp1();
			if (Common.isOrgTure(orgService, user_org_id, org_id)==false && org_id != null) {
				qn_list.remove(i);
			}
		}

		return new ModelAndView("wjdc/users/student", "wenjuan", qn_list);

	}

	
	
	@Login
	@RequestMapping("/wjdc/questionnaire/upload.htm")
	public ModelAndView wjdc_upload(ModelMap modelMap) {
		List<Org> org_list = orgService.selectList(null);
		return new ModelAndView("wjdc/up_wj_a", "org", org_list);
	}


	@Login
	@RequestMapping("/wjdc/questionnaire/upload.do")
	public ModelAndView doupLoad(HttpServletRequest request, String publicQn,
			String department, String t_s, HttpSession session)
			throws IllegalStateException, IOException {
		Teacher t = (Teacher) session.getAttribute("current_user");
		Questionnaire qn = new Questionnaire();

		if ("否".equals(publicQn)) {
			qn.setTemp1(department);
		}
		qn.setTemp2(t_s);
		qn.setTemp3(Questionnaire.qn_wjdc);
		qn.setUser_id(t.getId());

		upLoad(qn, t, request);
		return new ModelAndView("redirect:/wjdc/questionnaire/admin.htm");
	}

	public void upLoad(Questionnaire qn, Teacher t, HttpServletRequest request) {

		int i;
		Sheet sheet;
		Workbook book;
		Cell[] cell = new Cell[12];
		Cell cell1;
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {
					try {
						book = Workbook.getWorkbook(file.getInputStream());
						sheet = book.getSheet(0);
						// 获取左上角的单元格
						cell1 = sheet.getCell(0, 0);
						System.out.println("标题：" + cell1.getContents());
						qn.setTitle(cell1.getContents());
						qn = (Questionnaire) this.questionnaireService
								.insert(qn);
						String qn_id = qn.getId();
						i = 1;
						while (true) {

							if (i > 1) {
								// 获取每一行的单元格
								for (int j = 0; j < cell.length; j++) {
									cell[j] = sheet.getCell(j, i);
								}
								if ("".equals(cell[0].getContents()) == true) {
									break;
								}
								Question q = new Question();
								q.setTitle(cell[0].getContents());
								q.setType(cell[1].getContents());
								q.setAnswer(cell[2].getContents());
								q.setOther(cell[3].getContents());
								q.setStyle(cell[4].getContents());
								q.setQuestionnaire_id(qn_id);
								q = (Question) this.questionService.insert(q);
								String q_id = q.getId();
								System.out.println(q_id);
								for (int j = 5; j < cell.length; j++) {

									if ("".equals(cell[j].getContents()) == true
											|| null == cell[j].getContents()) {
										break;
									}
									System.out.println(cell[j].getContents());
									Option o = new Option();
									o.setQuestion_id(q_id);
									o.setTitle(cell[j].getContents());
									o.setId("");
									this.optionService.insertOption(o);
								}
							}
							i++;
						}
						book.close();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("方法三的运行时间：" + String.valueOf(endTime - startTime)
				+ "ms");
	}

	@Login
	@RequestMapping("/wjdc/questionnaire/new.htm")
	public ModelAndView newhtm(ModelMap modelMap, String qn_id,HttpSession session) {

		
		List<Org> org_list = orgService.selectList(null);
		modelMap.put("org", org_list);
		
		Questionnaire questionnaire = new Questionnaire();
		if ("".equals(qn_id)==false && qn_id!=null) {
			questionnaire = questionnaireService.selectByID(qn_id);
		}
		modelMap.put("questionnaire", questionnaire);
		modelMap.put("userRole",CommonSession.getUserRole(session));
		return new ModelAndView("wjdc/newQuestionnaire");
	}

	
	@Login
	@RequestMapping("/wjdc/questionnaire/analysis.htm")
	public String toAnalysis(ModelMap modelMap, String id) {

		// 显示问卷，问题，选项
		Questionnaire qn = this.questionnaireService.getByQuestionAndOption(id);
		Question q;
		QAnswer qa;
		Option o;
		List qList; // 问题集合
		List oList; // 选项集合

		// 查看问卷有多少人提交了
		QnAnswer qna = new QnAnswer();
		qna.setQuestionnaire_id(id);
		List<QnAnswer> qnaList = this.qnaService.getSelectQnAnswer(qna);
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

		// User user = (User) this.userService.selectByID(qn.getUser_id());
		// String department = user.getDepartment();
		// user = new User();
		// user.setDepartment(department);
		// int qnaNoNum = this.userService.selectCount(user);
		// modelMap.put("qnaNoNum", qnaNoNum);// 问卷需填数量
		// modelMap.put("qnaNum", qnaNum); // 问卷提交数量
		// modelMap.put("qnaoverNum", qnaOver); // 问卷提交数量
		// modelMap.put("qnaNo-OverNum", qnaNoNum - qnaNum); // 问卷提交数量
		// 问题有多少人做了
		qList = qn.getQuestion();
		for (int i = 0; i < qList.size(); i++) {

			q = new Question();
			q = (Question) qList.get(i);
			qa = new QAnswer();
			qa.setQuestion_id(q.getId());
			q.setTemp5(String.valueOf(this.qaService.distinctQAnswerCount(qa)));
			qa = null;
			// 选项有多少人选了
			oList = q.getOption();

			ChartModel m = new ChartModel();
			List<ChartData> listData = new ArrayList<ChartData>();

			for (int j = 0; j < oList.size(); j++) {
				o = new Option();
				o = (Option) oList.get(j);
				qa = new QAnswer();
				qa.setAnswertext(o.getId());
				o.setTemp5(String.valueOf(this.qaService
						.distinctQAnswerCount(qa)));
				qa = null;
				oList.set(j, o);

				if (q.getType().equals("单选")) {
					Double oNum = Double.parseDouble(o.getTemp5());
					Double pNum = Double.parseDouble(q.getTemp5());
					DecimalFormat df = new DecimalFormat("#.00");
					listData.add(new ChartData(o.getTitle(), df.format(oNum
							/ pNum * 100), Ichartjs_Color.Ichartjs_Color_List
							.get(j)));
				} else if (q.getType().equals("多选")) {
					listData.add(new ChartData(o.getTitle(), o.getTemp5(),
							Ichartjs_Color.Ichartjs_Color_List.get(j)));
				}

			}
			m.setListData(listData);
			q.setOption(oList);
			q.setChartModel(m);
			qList.set(i, q);
		}
		qn.setQuestion(qList);
		modelMap.put("qn", qn);

		return "wjdc/questionnaire/analysis";
	}

	//
	// /**
	// *
	// * 生成一个id新建一个问卷表单
	// *
	// * @return
	// */
	@Login
	@RequestMapping("/wjdc/questionnaire/newid.do")
	public ModelAndView wjdc_newid(String temp1, Questionnaire qn,
			String qn_id, String publicQn, String department, String t_s,
			HttpSession session) {
		Teacher t = (Teacher) session.getAttribute("current_user");
		qn.setId(qn_id);
		if ("否".equals(publicQn)) {
			qn.setTemp1(department);
		}
		qn.setTemp2(t_s);
		qn.setTemp3(Questionnaire.qn_wjdc);
		qn.setUser_id(t.getId());
		this.questionnaireService.insertOrUpdate(qn);

		return new ModelAndView("redirect:/wjdc/user/home.htm");
	}

	@Login
	@RequestMapping("/wjdc/questionnaire/delete.do")
	public ModelAndView wjdc_delete(String id) {

		deleteQuestionnaireByid(id);
		return new ModelAndView("redirect:/wjdc/user/home.htm");
	}

	

	public int deleteQuestionnaireByid(String id) {

		Questionnaire qn = new Questionnaire();
		qn.setId(id);
		return this.questionnaireService.delete(qn);

	}

	@RequestMapping("/wjdc/questionnaire/{id}/qr.htm")
	public String wjdc_qr(@PathVariable String id, ModelMap modelMap,
			HttpServletRequest request) {

		String basePath = Constant.getBasePath(request);
		String url = basePath + "wjdc/questionnaire/" + id + "/text.htm";
		modelMap = Common.get_qr(url, modelMap);

		return "wjdc/questionnaire/qr";
	}
	
	
	
	// TODO 原question
	
	
	
	
	
	//原question
	@RequestMapping(value = "/wjdc/question/{qn_id}/{q_id}.htm")
	public ModelAndView wjdc_showById(@PathVariable String qn_id,
			@PathVariable String q_id, ModelMap modelMap, HttpSession session,
			HttpServletRequest request) {

		Question q = (Question) this.questionService.selectByID(q_id);

		List oList = new ArrayList<Option>();
		Option o = new Option();
		o.setQuestion_id(q.getId());
		oList = this.optionService.getSelectOption(o);
		q.setOption(oList);

		List<QuestionID> list_qid = (List<QuestionID>) session
				.getAttribute("list_qid");
		int now_num = list_qid.get(0).getNum();
		modelMap.put("now_num", now_num);

		Questionnaire qn = (Questionnaire) session.getAttribute("qn");
		modelMap.put("qn", qn);
		String result = request.getParameter("result");
		if ("cxxbt".equals(result)) {
			result = "此选项为必填选项";
		} else if ("qtzbwk".equals(result)) {
			result = "你选择了其他选项，请填写其他选项的内容";
		}
		modelMap.put("result", result);
		QnAnswer qna = (QnAnswer) session.getAttribute("qna");
		if(qna == null){
			result = "问卷问题错误，请重新登录打开问卷。";
			modelMap.put("result", result);
			return new ModelAndView("welcome");
		}
		
		QAnswer qa = new QAnswer();
		qa.setQnanswer_id(qna.getId());
		qa.setQuestion_id(q.getId());
		List<QAnswer> qanList = this.qanswerService.getSelectQAnswer(qa);
		String textAnswer = "";
		String otherAnswer = "";
		String otherChecked = "";
		String otherNone = "none";
		if ("文本".equals(q.getType())) {
			for (QAnswer qAnswer : qanList) {
				textAnswer = qAnswer.getAnswertext();
			}
			modelMap.put("textAnswer", textAnswer);
		} else {
			for (QAnswer qAnswer : qanList) {
				for (int i = 0; i < oList.size(); i++) {
					o = (Option) oList.get(i);
					if (qAnswer.getAnswertext().equals(o.getId())) {
						o.setChecked("checked");
						oList.set(i, o);
					}
				}
				if("是".equals(q.getOther())){
					if(qAnswer.getAnswertext().contains("other:")){
						otherAnswer = qAnswer.getAnswertext().substring("other:".length());
						otherChecked = "checked";
						otherNone = "block";
						modelMap.put("otherAnswer", otherAnswer);
						modelMap.put("otherChecked", otherChecked);
						
					}
				}
			}
			q.setOption(oList);
		}

		modelMap.put("q", q);
		modelMap.put("otherNone", otherNone);
		return new ModelAndView("wjdc/questionnaire/show");

	}

	
	
	
	
	@RequestMapping(value = "/wjdc/question/{id}/show.htm")
	public ModelAndView wjdc_showhtm(@PathVariable String id,ModelMap modelMap) {
		Questionnaire questionnaire = this.questionService.getOneQurestionnaire_question(id);
		modelMap.put("questionnaire", questionnaire);
		return new ModelAndView("wjdc/QuestionnaireQuestion");
	}
	
	

//	@Login
//	@FireAuthority(authorityTypes = { AuthorityType.ADMIN,
//			AuthorityType.DEPARTMENT })
	@RequestMapping(value = "/wjdc/question/{id}/{type}/show.htm")
	public ModelAndView wjdc_quesiton_questionnaire_show(@PathVariable String id,
			@PathVariable String type) {
		Questionnaire qn = new Questionnaire();
		qn.setId(id);
		qn = this.questionService.getOneQurestionnaire_question(id);
		if (type.equals("oneallquestion")) {
			return new ModelAndView("wjdc/questionnaire/question/one_all_question", "qn", qn);
		} else {
			return new ModelAndView(new InternalResourceView("/index.jsp"));
		}

	}
	


	@RequestMapping(value = "/wjdc/question/{qn_id}/new.htm")
	public ModelAndView wjdc_newhtm(@PathVariable String qn_id, String q_id,ModelMap modelMap) {
		
		Question q = new Question();
		q.setQuestionnaire_id(qn_id);
		List<Question> qList = questionService.getSelectQuestion(q);
		for (int i = 0; i < qList.size(); i++) {
			q = (Question) qList.get(i);
			Option o = new Option();
			o.setQuestion_id(q.getId());
			List oList = this.optionService.getSelectOption(o);
			q.setOption(oList);
			qList.set(i, q);
		}
		for (int i = 0; i < qList.size(); i++) {
			q = (Question) qList.get(i);
			if (q.getId().equals(q_id)) {
				qList.remove(i);
			}
		}

		modelMap.put("qlist", qList);
		q = new Question();

		if ("".equals(q_id) || null == q_id) {
			q.setQuestionnaire_id(qn_id);
		} else {
			q = (Question) this.questionService.selectByID(q_id);
			q.setQuestionnaire_id(qn_id);
		}
		modelMap.put("q", q);
		return new ModelAndView("wjdc/newQuestionnaireQuestion");

	}
	
	

	@RequestMapping(value = "/wjdc/question/{qn_id}/insert.do")
	public ModelAndView wjdc_insert(@PathVariable String qn_id, Question q,
			String q_id) {

		System.out.println(q_id);
		q.setQuestionnaire_id(qn_id);
		q.setId(q_id);
		q = (Question) this.questionService.insertOrUpdate(q);
		return new ModelAndView(new InternalResourceView("/wjdc/question/" + qn_id
				+ "/show.htm"));
	}
	
	

	@RequestMapping(value = "/wjdc/question/{qn_id}/delete.do")
	public ModelAndView wjdc_delete(@PathVariable String qn_id, String q_id) {

		deleteQuestionById(q_id);
		return new ModelAndView("redirect:/wjdc/question/" + qn_id + "/show.htm");
	}
	
	
	
	public int deleteQuestionById(String id){
		Question q = new Question();
		q.setId(id);
		return this.questionService.deleteQuestion(q);
	}	
	
	
	
	
	
	//TODO 原 QnAnswer
	
	@RequestMapping(value = "/qna/ioru.do")
	public ModelAndView insertOrUpdateQnAnswer(String other, String qna_id,
			String[] xx, HttpSession session, ModelMap modelMap, String num,
			String qn_id, String q_id, HttpServletRequest request,
			String depend, String show) throws UnsupportedEncodingException {
		modelMap.put("show", show);
		String context = "";
		// 捕获选项值是否为空

		int page;
		try {
			page = Integer.parseInt(num);
		} catch (Exception e) {
			page = 0;
		}
		try {
			int xxlength = xx.length;
		} catch (Exception e) {
			// 如果为空，查看选项是否允许为空
			Questionnaire qn = this.questionnaireService
					.getByQuestionAndOption(qn_id);
			Question q = (Question) qn.getQuestion().get(page);
			// 如果必须是必选项，则提示不能为空，返回此题
			if ("是".equals(q.getAnswer())) {
				modelMap.put("qn", qn);
				modelMap.put("q", q);
				modelMap.put("num", page);
				modelMap.put("qna_id", qna_id);
				modelMap.put("depend", depend);
				modelMap.put("result", "此选项为必选项");
				return new ModelAndView("wjdc/questionnaire/show");
			} else {
				// 如果不是必选项，则值为空。
				xx = new String[1];
				xx[0] = "";
			}

		}
		// 其他选项不能为空
		for (int i = 0; i < xx.length; i++) {
			// 先判断选项是否是其他
			if ("other".equals(xx[i])) {
				if ("".equals(other)) {
					Questionnaire qn = this.questionnaireService
							.getByQuestionAndOption(qn_id);
					Question q = (Question) qn.getQuestion().get(page);
					modelMap.put("qn", qn);
					modelMap.put("q", q);
					modelMap.put("num", page);
					modelMap.put("qna_id", qna_id);
					modelMap.put("depend", depend);
					modelMap.put("result", "其他选项的值不能为空");
					return new ModelAndView("wjdc/questionnaire/show");
				}
			}
		}

		// 添加提交数据
		for (int i = 0; i < xx.length; i++) {
			QAnswer qa = new QAnswer();
			qa.setQnanswer_id(qna_id);
			qa.setQuestion_id(q_id);
			qa.setAnswertext(xx[i]);
			context = xx[i];
			this.qaService.insertQAnswer(qa);
		}
		Questionnaire qn = this.questionnaireService
				.getByQuestionAndOption(qn_id);
		Question q = new Question();

		// 判断问题中是否有此选项的依赖值
		if ("".equals(context) == false && null != context) {
			// 选项有依赖值,则查询依赖的选项
			q = new Question();
			q.setDepend(context);
			List<Question> qList = this.questionService.getSelectQuestion(q);
			if (qList.size() > 0) {
				// 如果填完了，那么被告知已经被完成
				if (page >= qList.size()) {
					QnAnswer qna = new QnAnswer();
					qna = this.qnaService.getQnAnswerById(qna_id);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					qna.setEnddate(df.format(new Date()).toString());
					this.qnaService.insertOrUpdate(qna);
					modelMap.put("result", "问卷已经被完成,谢谢");
					return new ModelAndView("welcome");
				}
				if ("".equals(depend) || null == depend) {
					page = 0;
				}
				q = (Question) qList.get(page);
				Option o = new Option();
				o.setQuestion_id(q.getId());
				List<Object> oList = this.optionService.getSelectOption(o);
				q.setOption(oList);
				modelMap.put("qn", qn);
				modelMap.put("q", q);
				modelMap.put("num", page + 1);
				modelMap.put("qna_id", qna_id);
				System.out.println(context);
				modelMap.put("depend", context);
				return new ModelAndView("wjdc/questionnaire/show");
			}
		}

		// 如果依赖了，那么继续做依赖的内容
		if ("".equals(depend) == false && null != depend) {
			// 选项有依赖值,则查询依赖的选项
			q = new Question();
			q.setDepend(depend);
			List<Question> qList = this.questionService.getSelectQuestion(q);
			// 如果填完了，那么被告知已经被完成
			if (page >= qList.size()) {
				QnAnswer qna = new QnAnswer();
				qna = this.qnaService.getQnAnswerById(qna_id);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				qna.setEnddate(df.format(new Date()).toString());
				this.qnaService.insertOrUpdate(qna);
				modelMap.put("result", "问卷已经被完成,谢谢");
				return new ModelAndView("welcome");
			}
			q = (Question) qList.get(page);
			Option o = new Option();
			o.setQuestion_id(q.getId());
			List<Object> oList = this.optionService.getSelectOption(o);
			q.setOption(oList);
			modelMap.put("qn", qn);
			modelMap.put("q", q);
			modelMap.put("num", page + 1);
			modelMap.put("qna_id", qna_id);
			modelMap.put("depend", depend);
			return new ModelAndView("wjdc/questionnaire/show");
		}

		qn = this.questionnaireService.getByQuestionAndOption(qn_id);
		for (int i = 0; i < qn.getQuestion().size(); i++) {
			Question qtemp = new Question();
			qtemp = (Question) qn.getQuestion().get(i);
			if ("".equals(qtemp.getDepend()) == false
					&& null != qtemp.getDepend()) {
				qn.getQuestion().remove(i);
			}
		}
		q = (Question) qn.getQuestion().get(page);

		if (page + 1 == qn.getQuestion().size()) {
			QnAnswer qna = new QnAnswer();
			qna = this.qnaService.getQnAnswerById(qna_id);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			qna.setEnddate(df.format(new Date()).toString());
			this.qnaService.insertOrUpdate(qna);
			modelMap.put("result", "问卷已经被完成,谢谢");
			return new ModelAndView("welcome");
		}
		q = (Question) qn.getQuestion().get(page + 1);
		modelMap.put("qn", qn);
		modelMap.put("q", q);
		modelMap.put("num", page + 1);
		modelMap.put("qna_id", qna_id);
		modelMap.put("depend", depend);
		return new ModelAndView("wjdc/questionnaire/show");

		// }

	}

	

	// @Login
	// @FireAuthority(authorityTypes = AuthorityType.ADMIN)
	@RequestMapping(value = "/qna/{id}/list.htm")
	public ModelAndView listhtm(@PathVariable String id) {

		QnAnswer qna = new QnAnswer();
		qna.setQuestionnaire_id(id);
		List<QnAnswer> qnaList = this.qnaService.getSelectQnAnswer(qna);
		return new ModelAndView("wjdc/questionnaire/submitJieguoList",
				"qnaList", qnaList);
	}

	

	// @Login
	// @FireAuthority(authorityTypes = { AuthorityType.ADMIN,
	// AuthorityType.DEPARTMENT })
	@RequestMapping("/qna/{qn_id}/{qna_id}/one_user_qna.htm")
	public String select_one_user_qna(@PathVariable String qn_id,
			@PathVariable String qna_id, ModelMap modelMap) {

		List<Question> question_list = this.get_question(qn_id);
		List<String> answer_list = new ArrayList<String>();
		for (Question question : question_list) {
			String answer_text = this.get_question_one_qna(qna_id,
					question.getId());
			answer_list.add(answer_text);
		}
		modelMap.put("question_list", question_list);
		modelMap.put("answer_list", answer_list);
		return "/wjdc/questionnaire/qn_qna_user";
	}

	

	// @Login
	// @FireAuthority(authorityTypes = { AuthorityType.ADMIN,
	// AuthorityType.DEPARTMENT })
	@RequestMapping("/qna/{qn_id}/qn_qna_all_user.htm")
	public String select_all_user_qna(@PathVariable String qn_id,
			ModelMap modelMap, HttpServletRequest request) {

		modelMap = getAllUserQna(qn_id, modelMap, request);
		return "/wjdc/questionnaire/qn_qna_all_user";
	}

	

	public ModelMap getAllUserQna(String qn_id, ModelMap modelMap,
			HttpServletRequest request) {
		QnAnswer qna = new QnAnswer();
		qna.setQuestionnaire_id(qn_id);
		qna.setEnddate("isnotnull");
		List<QnAnswer> qn_list = this.qnaService.getSelectQnAnswer(qna);
		List<Question> question_list = this.get_question(qn_id);
		List<List<String>> all_answer_list = new ArrayList<List<String>>();
		List<QnAnswer> all_qn_list = new ArrayList<QnAnswer>();

		String temp = request.getParameter("page");
		int start = 0;
		int end = 0;
		int sum_page = 0;
		int page = 0;
		if (qn_list.size() % 10 == 0) {
			sum_page = qn_list.size() / 10;
		} else {
			sum_page = qn_list.size() / 10 + 1;
		}

		modelMap.put("sum_page", sum_page);
		modelMap.put("qn_id", qn_id);
		if (temp == null || temp.equals("") || temp.equals("1")) {
			start = 0;
			page = 1;
			if (qn_list.size() <= 9) {
				end = qn_list.size() - 1;
			} else {
				end = 9;
			}
		} else {
			end = Integer.parseInt(temp);
			start = Integer.parseInt(temp);
			page = Integer.parseInt(temp);
			start = (start - 1) * 10;
			if (qn_list.size() / 10 + 1 == end) {
				end = qn_list.size() - 1;
			} else {
				end = end * 10 - 1;
			}
		}
		modelMap.put("page", page);
		for (int i = start; i <= end; i++) {
			QnAnswer qnAnswer = qn_list.get(i);
			List<String> answer_list = new ArrayList<String>();
			for (Question question : question_list) {

				String answer_text = this.get_question_one_qna(
						qnAnswer.getId(), question.getId());
				answer_list.add(answer_text);
			}
			all_answer_list.add(answer_list);
			all_qn_list.add(qnAnswer);
		}

		modelMap.put("question_list", question_list);
		modelMap.put("all_qn_list", all_qn_list);
		modelMap.put("all_answer_list", all_answer_list);
		return modelMap;
	}

	private List<Question> get_question(String qn_id) {

		Question q = new Question();
		q.setQuestionnaire_id(qn_id);
		List<Question> question_list = this.questionService.selectList(q);
		return question_list;
	}

	private String get_question_one_qna(String qna_id, String question_id) {

		StringBuffer answer_text = new StringBuffer();
		QAnswer qa = new QAnswer();
		qa.setQnanswer_id(qna_id);
		qa.setQuestion_id(question_id);
		List<QAnswer> qa_list = this.qaService.getSelectQAnswer(qa);
		for (QAnswer qAnswer : qa_list) {

			String context = null;
			Option o = null;
			try {
				o = this.optionService.getOptionById(qAnswer.getAnswertext());
			} catch (Exception e) {
				o = null;
			}

			if (o != null) {
				context = o.getTitle();
			} else {
				context = qAnswer.getAnswertext();
			}
			if (qa_list.size() - 1 == 0) {
				answer_text.append(context);
			} else {
				answer_text.append(context + "<br>");
			}
		}
		return answer_text.toString();
	}
	
	
	
	//TODO 原Qanswer
	
	@RequestMapping(value = "/wjdc/qa/ioru.do")
	public ModelAndView wjdc_insertOrUpdateQAnswer(String sumbit_btn, String other,
			String[] xx, HttpSession session, ModelMap modelMap,
			HttpServletRequest request,String page_sumbit) {

		String context = "";
		QnAnswer qna = (QnAnswer) session.getAttribute("qna");
		Questionnaire qn = (Questionnaire) session.getAttribute("qn");
		@SuppressWarnings("unchecked")
		List<QuestionID> list_qid = (List<QuestionID>) session
				.getAttribute("list_qid");
		QuestionID qid = new QuestionID();


		if ("previous".equals(page_sumbit)) {
			qid = list_qid.get(list_qid.size() - 1);
			int num = qid.getNum();
			num = num - 1;
			if (num == -1) {
				num = 0;
			}
			qid.setNum(num);
			String q_id = qid.getId().get(num);
			session.setAttribute("list_qid", list_qid);
			return new ModelAndView("redirect:/wjdc/question/" + qn.getId()
					+ "/" + q_id + ".htm");
		}

		qid = new QuestionID();
		qid = list_qid.get(list_qid.size() - 1);
		String q_id = qid.getId().get(qid.getNum());

		// 判断用户是否多次提交
		String request_q_id = request.getParameter("q_id");
		if (q_id.equals(request_q_id) == false) {
			return new ModelAndView("redirect:/question/" + qn.getId() + "/"
					+ q_id + ".htm");
		}
		// ----------------------------------

		Question q = (Question) this.questionService.selectByID(q_id);

		int num = 0;
		try {
			num = xx.length;
			if (num == 0) {
				xx = new String[1];
				xx[0] = "";
			}
		} catch (Exception e) {
			xx = new String[1];
			xx[0] = "";
			num = xx.length;
		}

		if ("".equals(xx[0])) {
			if ("是".equals(q.getAnswer())) {

				return new ModelAndView("redirect:/wjdc/question/" + qn.getId()
						+ "/" + q_id + ".htm?result=cxxbt");
			}
		}

		for (int i = 0; i < xx.length; i++) {
			// 先判断选项是否是其他
			if ("other".equals(xx[i])) {
				if ("".equals(other)) {
					return new ModelAndView("redirect:/wjdc/question/"
							+ qn.getId() + "/" + q_id + ".htm?result=qtzbwk");
				} else {
					xx[i] = "other:" + other;
				}
			}
		}
		// 如果之前提交过数据，那么删除这些数据
		QAnswer qa = new QAnswer();
		qa.setQnanswer_id(qna.getId());
		qa.setQuestion_id(q_id);
		List<QAnswer> qaList = this.qaService.getSelectQAnswer(qa);
		for (QAnswer qAnswer : qaList) {
			this.qaService.deleteQAnswerById(qAnswer.getId());
		}
		// 添加提交数据
		for (int i = 0; i < xx.length; i++) {
			qa = new QAnswer();
			qa.setQnanswer_id(qna.getId());
			qa.setQuestion_id(q_id);
			qa.setAnswertext(xx[i]);
			context = xx[i];
			this.qaService.insertQAnswer(qa);
		}

		qid = new QuestionID();
		qid = list_qid.get(list_qid.size() - 1);
		qid.setNum(qid.getNum() + 1);
		if (qid.getNum() == qid.getId().size()) {
			list_qid.remove(list_qid.size() - 1);
		}
		if (list_qid.size() == 0) {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			qna.setEnddate(df.format(new Date()).toString());
			this.qnaService.insertOrUpdate(qna);
			modelMap.put("result", "问卷已提交，谢谢参与");
			return new ModelAndView("welcome");
		}

		q = new Question();
		q.setDepend(context);
		List<Question> qList = this.questionService.getSelectQuestion(q);
		if (qList.size() > 0) {
			qid = new QuestionID();

			List<String> id = new ArrayList<String>();

			for (Question question : qList) {
				id.add(question.getId());
			}

			qid.setId(id);
			qid.setNum(0);
			list_qid.add(qid);
		}

		qid = list_qid.get(list_qid.size() - 1);
		q_id = qid.getId().get(qid.getNum());

		session.setAttribute("list_qid", list_qid);

		return new ModelAndView("redirect:/wjdc/question/" + qn.getId() + "/"
				+ q_id + ".htm");
	}
	
	
	

	@RequestMapping(value = "/wjdc/qa/{id}/show.htm")
	public ModelAndView wjdc_show(@PathVariable String id)
			throws UnsupportedEncodingException {

		QAnswer qa = new QAnswer();
		qa.setQnanswer_id(id);
		List<QAnswer> qaList = this.qaService.getSelectQAnswer(qa);
		for (int i = 0; i < qaList.size(); i++) {

			Question q = (Question) this.questionService.selectByID(qaList.get(
					i).getQuestion_id());
			qaList.get(i).setQ(q);
			Option o = this.optionService.getOptionById(qaList.get(i)
					.getAnswertext());
			qaList.get(i).setO(o);
		}
		return new ModelAndView("wjdc/questionnaire/submitJieguo", "qaList", qaList);
	}
	
	
	
	
//	@Login
//	@FireAuthority(authorityTypes = { AuthorityType.ADMIN,
//			AuthorityType.DEPARTMENT })
	@RequestMapping(value = "/wjdc/qa/{qn_id}/{q_id}/other.htm")
	public ModelAndView wjdc_showhtm(@PathVariable String qn_id,@PathVariable String q_id,ModelMap modelMap) {
		QAnswer qa = new QAnswer();
		qa.setQuestion_id(q_id);
//		List<QAnswer> qa_list = this.qaService.selectOtherQAnswer(qa);
//		List<User> user_list = new ArrayList<User>();
//		for (QAnswer qAnswer : qa_list) {
//			QnAnswer qn = new QnAnswer();
//			qn = this.qnaService.getQnAnswerById(qAnswer.getQnanswer_id());
//			User user = new User();
//			user = (User) this.userService.selectByID(qn.getUser_id());
//			user_list.add(user);
//		}
//		
//		modelMap.put("qa_list", qa_list);
//		modelMap.put("user_list", user_list);
		
		return new ModelAndView("wjdc/questionnaire/answer/question_other");
	}
	
	
	
	
	

	
	//TODO 原Option
	@RequestMapping(value = "/wjdc/option/{qn_id}/{q_id}/show.htm")
	public ModelAndView wjdc_option_show(@PathVariable String qn_id,
			@PathVariable String q_id) {
		
		Questionnaire qn = new Questionnaire();
		qn.setId(qn_id);
		Question q = new Question();
		q.setId(q_id);
		qn.setQ(q);
		qn = this.optionService.getOneQurestionnaire_question_option(qn);
		return new ModelAndView("wjdc/QuestionnaireOption", "qn", qn);

	}
	
//	@Login
//	@FireAuthority(authorityTypes = { AuthorityType.ADMIN,
//			AuthorityType.DEPARTMENT })
	@RequestMapping(value = "/wjdc/option/{qn_id}/{q_id}/{type}/show.htm")
	public ModelAndView wjdc_showhtm(@PathVariable String qn_id,
			@PathVariable String q_id,@PathVariable String type) {

		Questionnaire qn = new Questionnaire();
		qn.setId(qn_id);
		Question q = new Question();
		q.setId(q_id);
		qn.setQ(q);
		qn = this.optionService.getOneQurestionnaire_question_option(qn);
		if (type.equals("oneallquestion")) {
			return new ModelAndView("wjdc/questionnaire/option/one_all_option", "qn", qn);
		} else {
			return new ModelAndView(new InternalResourceView("/index.jsp"));
		}
		

	}
	
	
	
	

//	@Login
//	@FireAuthority(authorityTypes = AuthorityType.ADMIN)
	@RequestMapping(value = "/wjdc/option/{qn_id}/{q_id}/new.htm")
	public ModelAndView wjdc_newhtm(@PathVariable String qn_id,
			@PathVariable String q_id, String o_id) {

		Questionnaire qn = this.getNewHtm(qn_id, q_id, o_id);
		return new ModelAndView("wjdc/newQuestionnaireOption", "qn", qn);

	}
	
	
	
	public Questionnaire getNewHtm(String qn_id,String q_id, String o_id){
		Questionnaire qn = new Questionnaire();
		Question q = new Question();
		Option o = new Option();
		if ("".equals(o_id) || null == o_id || "null".equals(o_id)) {

		} else {
			o = this.optionService.getOptionById(o_id);
			q.setO(o);
		}

		qn.setId(qn_id);
		q.setId(q_id);
		qn.setQ(q);

		q = new Question();
		q.setQuestionnaire_id(qn.getId());
		List<Question> qList = this.questionService.getSelectQuestion(q);
		for (int i = 0; i < qList.size(); i++) {
			q = new Question();
			q = (Question) qList.get(i);
			if (q.getId().equals(q_id)) {
				qList.remove(i);
			}

		}
		qn.setQuestion(qList);
		return qn;
	}

//	@Login
//	@FireAuthority(authorityTypes = AuthorityType.ADMIN)
	@RequestMapping(value = "/wjdc/option/{qn_id}/{q_id}/insert.do")
	public ModelAndView wjdc_insert(@PathVariable String qn_id,
			@PathVariable String q_id, Option o, String o_id) {

		o.setId(o_id);
		o.setQuestion_id(q_id);
		int i = this.optionService.insertOption(o);
		return new ModelAndView("redirect:/wjdc/option/" + qn_id + "/" + q_id
				+ "/show.htm");
	}

	

	
//	@Login
//	@FireAuthority(authorityTypes = AuthorityType.ADMIN)
	@RequestMapping(value = "/wjdc/option/{qn_id}/{q_id}/{o_id}/delete.do")
	public ModelAndView wjdc_delete(@PathVariable String qn_id,
			@PathVariable String q_id, @PathVariable String o_id) {

		Option o = new Option();
		o.setId(o_id);
		optionService.deleteOption(o);
		return new ModelAndView("redirect:/wjdc/option/" + qn_id + "/" + q_id
				+ "/show.htm");
	}
	

	
	
	
}
