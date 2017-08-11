package com.sict.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.eclipse.jdt.core.dom.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sict.authority.Login;
import com.sict.common.CommonSession;
import com.sict.entity.OnlineTestOption;
import com.sict.entity.OnlineTestQAnswer;
import com.sict.entity.OnlineTestQnAnswer;
import com.sict.entity.OnlineTestQuestion;
import com.sict.entity.OnlineTestQuestionnaire;
import com.sict.entity.Option;
import com.sict.entity.Org;
import com.sict.entity.OnlineTestOption;
import com.sict.entity.OnlineTestQuestion;
import com.sict.entity.OnlineTestQuestionnaire;
import com.sict.entity.Teacher;
import com.sict.entity.User;
import com.sict.service.OnlineTestOptionService;
import com.sict.service.OnlineTestQAnswerService;
import com.sict.service.OnlineTestQnAnswerService;
import com.sict.service.OnlineTestQuestionService;
import com.sict.service.OnlineTestQuestionnaireService;
import com.sict.service.OrgService;
import com.sict.service.UserService;
import com.sict.util.Common;

@Controller
@RequestMapping("/zxcy")
public class OnlinetTestController {

	@Resource(name = "onlineTestQuestionnaireService")
	private OnlineTestQuestionnaireService otQuestionnaireService;
	@Resource(name = "onlineTestQuestionService")
	private OnlineTestQuestionService otQuestionService;
	@Resource(name = "onlineTestOptionService")
	private OnlineTestOptionService otOptionService;
	@Resource(name = "onlineTestQnAnswerService")
	private OnlineTestQnAnswerService otQnAnswerService;
	@Resource(name = "onlineTestQAnswerService")
	private OnlineTestQAnswerService otQAnswerService;
	@Resource(name = "orgService")
	private OrgService orgService;
	@Resource(name = "userService")
	private UserService userService;

	@Login
	@RequestMapping("/admin.htm")
	public ModelAndView admin(HttpSession session, ModelMap modelMap,
			String orgName, String userName, String startDate, String endDate) {

		Teacher t = (Teacher) session.getAttribute("current_user");
		String user_id = t.getId();
		OnlineTestQuestionnaire otqn = new OnlineTestQuestionnaire();
		otqn.setUser_id(user_id);
		List<OnlineTestQuestionnaire> list = otQuestionnaireService
				.selectListAndQuestionCount(otqn);

		// 组合条件
		List<Org> orgs = null;
		if (orgName != null && "".equals(orgName) == false) {
			Org org = new Org();
			org.setOrg_name("%" + orgName + "%");
			orgs = orgService.selectList(org);
		} else {
			orgName = null;
		}

		List<User> users = null;
		if (userName != null && "".equals(userName) == false) {
			User user = new User();
			user.setTrue_name("%" + userName + "%");
			users = userService.selectList(user);
		} else {
			users = null;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if (startDate != null && "".equals(startDate) == false) {
			try {
				start = df.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		} else {
			start = null;
		}
		if (endDate != null && "".equals(endDate) == false) {
			try {
				end = df.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		} else {
			end = null;
		}

		// 查询用户姓名
		List<String> userNames = new ArrayList<String>();
		// 查询测试对象
		List<String> orgNames = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			OnlineTestQuestionnaire otQuestionnaire = list.get(i);

			// 判断条件
			if (orgs != null) {
				if (orgs.toString().contains(otQuestionnaire.getOrg_id()) == false) {
					list.remove(i);
					i--;
					continue;
				}
			}
			if (users != null) {
				if (users.toString().contains(otQuestionnaire.getUser_id()) == false) {
					list.remove(i);
					i--;
					continue;
				}
			}

			if (start != null) {
				if (start.after(otQuestionnaire.getCreateDate())) {
					list.remove(i);
					i--;
					continue;
				}
			}

			if (end != null) {
				if (end.before(otQuestionnaire.getCreateDate())) {
					list.remove(i);
					i--;
					continue;
				}
			}

			userNames.add(userService.selectByID(user_id).getTrue_name());

			orgNames.add(orgService.selectByID(t.getDept_id()).getOrg_name());
		}

		modelMap.put("cy", list);
		modelMap.put("userNames", userNames);
		modelMap.put("orgNames", orgNames);
		return new ModelAndView("zxcy/users/admin");

	}

	/*
	 * 管理员查看提交的学生测验
	 */
	@Login
	@RequestMapping("/{cy_id}/queryStudentOnlineTestList.htm")
	public ModelAndView queryStudentOnlineTestList(@PathVariable String cy_id,
			String orgID, ModelMap modelMap) {

		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.selectByID(cy_id);
		OnlineTestQnAnswer otQnAnswer = new OnlineTestQnAnswer();
		otQnAnswer.setQuestionnaire_id(cy_id);
		List<OnlineTestQnAnswer> otQnAnswers = otQnAnswerService
				.selectList(otQnAnswer);

		// 初始化orgID
		if ("".equals(orgID))
			orgID = null;
		List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < otQnAnswers.size(); i++) {

			OnlineTestQnAnswer onlineTestQnAnswer = otQnAnswers.get(i);
			User user = new User();
			user = userService.selectByID(onlineTestQnAnswer.getUser_id());
			String score = otQAnswerService
					.sumScoreByQnAnswerID(onlineTestQnAnswer.getId());
			otQnAnswers.get(i).setScore(score);
			// 如果用户不存在删除用户
			if (user == null) {
				otQnAnswers.remove(i);
				i--;
				continue;
			} else {

				// 列出用户的班级并去重
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("className", user.getClass_name());
				map.put("orgID", user.getOrg_id());
				if (!classList.contains(map))
					classList.add(map);

				// 显示一个班级，否则显示全部
				if (orgID != null) {
					if (orgID.equals(user.getOrg_id()) == false) {
						otQnAnswers.remove(i);
						i--;
						continue;
					}
				}
				onlineTestQnAnswer.setUser(user);
			}
		}

		// 初始化成绩(主观成绩3,客观成绩1,2)
		List<Integer> scoreByTypeText = new ArrayList<Integer>();
		List<Integer> scoreByTypeChoice = new ArrayList<Integer>();
		for (int i = 0; i < otQnAnswers.size(); i++) {
			OnlineTestQnAnswer onlineTestQnAnswer = otQnAnswers.get(i);
			int score = 0;
			score = otQAnswerService.sumScoreByQuestionType(
					OnlineTestQuestion.type_text, onlineTestQnAnswer.getId());
			scoreByTypeText.add(score);
			score = 0;
			score = otQAnswerService.sumScoreByQuestionType(
					OnlineTestQuestion.type_singleChoice,
					onlineTestQnAnswer.getId());
			score = score
					+ otQAnswerService.sumScoreByQuestionType(
							OnlineTestQuestion.type_multipleChoice,
							onlineTestQnAnswer.getId());
			scoreByTypeChoice.add(score);

		}

		modelMap.put("cy", otQuestionnaire);
		modelMap.put("otQnAnswers", otQnAnswers);
		modelMap.put("classList", classList);
		modelMap.put("orgID", orgID);
		modelMap.put("scoreByTypeText", scoreByTypeText);
		modelMap.put("scoreByTypeChoice", scoreByTypeChoice);
		return new ModelAndView("/zxcy/correctOnlineTest/OnlineTestList");

	}

	/*
	 * 批改单个学生的测验
	 */
	@Login
	@RequestMapping("/{qnan_id}/onlineTestCorrect.htm")
	public ModelAndView toOnlineTestCorrect(@PathVariable String qnan_id,
			ModelMap modelMap, HttpSession session) {

		OnlineTestQnAnswer otQnAnswer = otQnAnswerService.selectByID(qnan_id);

		if (this.queryOnlineTestIsSumbit(otQnAnswer)) {

			if (!OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT.equals(otQnAnswer
					.getTemp1())) {
				otQnAnswer.setTemp1(OnlineTestQnAnswer.SUMBIT_STATE_CLOSE);
				otQnAnswerService.update(otQnAnswer);
			}

			OnlineTestQuestion otQuestion = new OnlineTestQuestion();
			otQuestion.setType(OnlineTestQuestion.type_text);
			otQuestion.setQuestionnaire_id(otQnAnswer.getQuestionnaire_id());
			List<OnlineTestQuestion> otQuestions = otQuestionService
					.selectList(otQuestion);
			List<OnlineTestQAnswer> otQAnswers = new ArrayList<OnlineTestQAnswer>();

			for (int i = 0; i < otQuestions.size(); i++) {
				OnlineTestQuestion onlineTestQuestion = otQuestions.get(i);
				OnlineTestQAnswer otQAnswer = new OnlineTestQAnswer();
				otQAnswer.setQnanswer_id(qnan_id);
				otQAnswer.setQuestion_id(onlineTestQuestion.getId());
				otQAnswer = otQAnswerService.selectOTQAnswer(otQAnswer);
				if (otQAnswer != null) {
					otQAnswers.add(otQAnswer);
				} else {
					otQuestions.remove(i);
				}
			}
			modelMap.put("otQuestions", otQuestions);
			modelMap.put("otQAnswers", otQAnswers);
			modelMap.put("otQnAnswer", otQnAnswer);
			return new ModelAndView("/zxcy/correctOnlineTest/OnlineTestCorrect");
		} else {
			modelMap.put("result", "学生还未提交测验，无法批改");
			return this.queryStudentOnlineTestList(
					otQnAnswer.getQuestionnaire_id(), null, modelMap);
		}
	}

	/*
	 * 保存测验结果
	 */
	@Login
	@RequestMapping("/onlineTestCorrect.do")
	public void onlineTestCorrect(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws IOException {

		OnlineTestQAnswer otQAnswer = new OnlineTestQAnswer();
		int score = Integer.parseInt(request.getParameter("score"));
		otQAnswer.setScore(request.getParameter("score"));
		otQAnswer.setId(request.getParameter("otid"));
		if (score <= 0) {
			otQAnswer.setValidity(OnlineTestQAnswer.validity_false);
		} else {
			otQAnswer.setValidity(OnlineTestQAnswer.validity_true);
		}
		response.getWriter()
				.print((Integer) otQAnswerService.update(otQAnswer));

	}

	/*
	 * 提交测验结果
	 */
	@Login
	@RequestMapping("/sumbitOnlineTestCorrect.do")
	public void sumbitOnlineTestCorrect(HttpServletRequest request,
			HttpServletResponse response, String otQnAnswer_id)
			throws IOException {

		OnlineTestQnAnswer otQnAnswer = otQnAnswerService
				.selectByID(otQnAnswer_id);
		if (!OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT.equals(otQnAnswer
				.getTemp1())) {
			otQnAnswer.setTemp1(OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT);
			response.getWriter().print(
					(Integer) otQnAnswerService.update(otQnAnswer));
		} else {
			response.getWriter().print(OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT);
		}

	}

	@Login
	@RequestMapping("/student.htm")
	public ModelAndView student(HttpSession session, ModelMap modelMap) {

		OnlineTestQuestionnaire otqn = new OnlineTestQuestionnaire();
		otqn.setState(OnlineTestQuestionnaire.open);
		List<OnlineTestQuestionnaire> list = otQuestionnaireService
				.selectListAndQuestionCount(otqn);

		User user = CommonSession.getUser(session, userService);

		// 查询测验是否提交
		List<Boolean> isSumbit = new ArrayList<Boolean>();
		// 测验是否必做
		List<Boolean> isAnswer = new ArrayList<Boolean>();
		for (OnlineTestQuestionnaire otQuestionnaire : list) {
			isSumbit.add(queryOnlineTestQnAnswerIsSumbit(
					otQuestionnaire.getId(), session));
			isAnswer.add(Common.isOrgTure(orgService, user.getOrg_id(),
					otQuestionnaire.getOrg_id()));

		}
		modelMap.put("cy", list);
		modelMap.put("isSumbit", isSumbit);
		modelMap.put("isAnswer", isAnswer);
		return new ModelAndView("zxcy/users/student");

	}

	/*
	 * 学生查看测验结果
	 */
	@Login
	@RequestMapping("/studentOnlintestFruit.htm")
	public ModelAndView studentOnlintestFruit(String cy_id,
			HttpSession session, ModelMap modelMap) {

		// 判断测验是否完成
		User user = CommonSession.getUser(session, userService);
		OnlineTestQnAnswer otQnAnswer = new OnlineTestQnAnswer();
		otQnAnswer.setQuestionnaire_id(cy_id);
		otQnAnswer.setUser_id(user.getId());
		List<OnlineTestQnAnswer> otQnAnswers = otQnAnswerService
				.selectList(otQnAnswer);
		if (otQnAnswers.size() > 0) {
			otQnAnswer = otQnAnswers.get(0);
			if (!this.queryOnlineTestIsSumbit(otQnAnswer)) {
				modelMap.put("result", "你的测验未提交，请先提交测验，再进行查询");
				return ControllerUser.to_zxcy_home(null, session, modelMap);
			}

		} else {
			modelMap.put("result", "未查询到你的测验记录，请先回答测验");
			return ControllerUser.to_zxcy_home(null, session, modelMap);
		}

		// 判断测验是否有客观题
		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.getOnlineTestQuestionnaireAndQuestionByQn_id(cy_id);
		// if (onlineTestQuestionTypeIsChooice(otQuestionnaire)
		// || OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT.equals(otQnAnswer
		// .getTemp1())) {
		modelMap = this.queryOnlineTestFruit(modelMap, otQuestionnaire,
				otQnAnswer);
		return new ModelAndView("zxcy/OnlineTestFruit");
		// } else {
		// modelMap.put("result", "因为包含客观题，作业待老师批改后，在个人主页进行查看。");
		// return new ModelAndView("/zxcy/result");
		// }

	}

	/*
	 * 加载新建测验的界面
	 */
	@Login
	@RequestMapping("/newOnlineTest.htm")
	public ModelAndView newOnlineTest_htm(HttpServletRequest request,
			ModelMap modelMap, String qn_id) {

		HttpSession session = request.getSession();
		String userRole = CommonSession.getUserRole(session);
		List<Org> org_list = orgService.selectList(null);
		modelMap.put("org", org_list);
		OnlineTestQuestionnaire otqn = new OnlineTestQuestionnaire();
		if ("".equals(qn_id) == false) {
			otqn = (OnlineTestQuestionnaire) this.otQuestionnaireService
					.selectByID(qn_id);
		}
		modelMap.put("userRole", userRole);
		modelMap.put("qn", otqn);
		return new ModelAndView("zxcy/newOnlineTest");
	}

	/*
	 * 新建测验提交
	 */
	@Login
	@RequestMapping("/newOnlineTest.do")
	public ModelAndView newOnlineTest_do(OnlineTestQuestionnaire otqn,
			HttpSession session) {

		Teacher t = (Teacher) session.getAttribute("current_user");
		otqn.setUser_id(t.getId());
		this.otQuestionnaireService.insertOrUpdate(otqn);
		return new ModelAndView("redirect:/zxcy/home.htm");
	}

	/*
	 * 删除测验
	 */
	@Login
	@RequestMapping("/deleteOnlineTest.do")
	public ModelAndView deleteOnlineTest(String id) {

		OnlineTestQuestionnaire otqn = new OnlineTestQuestionnaire();
		otqn.setId(id);
		this.otQuestionnaireService.delete(otqn);
		return new ModelAndView("redirect:/zxcy/home.htm");

	}

	/*
	 * 根据一个测验ID加载所有的测验问题
	 */
	@Login
	@RequestMapping(value = "/{cy_id}/question.htm")
	public ModelAndView zxcy_question(@PathVariable String cy_id,
			ModelMap modelMap) {

		OnlineTestQuestionnaire otqn = (OnlineTestQuestionnaire) otQuestionnaireService
				.selectByID(cy_id);
		if (otqn != null) {
			otqn = this.otQuestionnaireService
					.getOnlineTestQuestionnaireAndQuestionByQn_id(otqn.getId());
		} else {
			return new ModelAndView("redirect:/zxcy/home.htm");
		}

		modelMap.put("cy", otqn);
		return new ModelAndView("zxcy/OnlineTestQuestion");

	}

	/*
	 * 加载新建测验问题的界面
	 */
	@RequestMapping(value = "/question/{cy_id}/new.htm")
	public ModelAndView zxcy_newhtm(HttpServletRequest request,
			@PathVariable String cy_id, String q_id, ModelMap modelMap) {

		OnlineTestQuestion otq = new OnlineTestQuestion();
		if ("".equals(q_id) == false && q_id != null) {
			otq = (OnlineTestQuestion) otQuestionService.selectByID(q_id);
		} else {
			otq.setQuestionnaire_id(cy_id);
		}
		modelMap.put("q", otq);
		return new ModelAndView("zxcy/newOnlineTestQuestion");

	}

	/*
	 * 提交新建测验的问题
	 */
	@RequestMapping(value = "/question/insert.do")
	public ModelAndView zxcy_question_insert(OnlineTestQuestion otq) {

		otq = (OnlineTestQuestion) otQuestionService.insertOrUpdate(otq);
		return new ModelAndView("redirect:/zxcy/" + otq.getQuestionnaire_id()
				+ "/question.htm");
	}

	/*
	 * 开启关闭测验
	 */

	@Login
	@RequestMapping(value = "/{cy_id}/onlineTestState.do")
	public ModelAndView change_questionnaire_state(@PathVariable String cy_id,
			ModelMap modelMap, HttpSession session) {

		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.selectByID(cy_id);
		if (OnlineTestQuestionnaire.close.equals(otQuestionnaire.getState())) {

			boolean isT = true;

			OnlineTestQuestion otQuestion = new OnlineTestQuestion();
			otQuestion.setQuestionnaire_id(cy_id);
			// 判断测验是否有问题
			if (otQuestionService.selectCount(otQuestion) <= 0) {
				modelMap.put("result", "测验的问题少于1个，不能发布测验，请修改后再试");
				return this.zxcy_question(cy_id, modelMap);
			}

			// 判断是问题否有少于两个的选项，或者是否有正确选项
			otQuestion = null;
			otQuestion = otQuestionnaireService
					.getNotTrueAnswerOption(otQuestionnaire);
			if (otQuestion != null
					&& OnlineTestQuestion.type_choice.contains(otQuestion
							.getType())) {
				modelMap.put("result", "\"" + otQuestion.getTitle()
						+ "\"，这个问题的选项少于两个，或者问题的选项没有正确的答案，请修改后在试。");
				return this.zxcy_option(otQuestion.getId(), modelMap);
			}

			if (isT) {
				modelMap.put("result", "测验发布成功");
				otQuestionnaire.setState(OnlineTestQuestionnaire.open);
				otQuestionnaireService.update(otQuestionnaire);
			}

		} else {
			modelMap.put("result", "测验关闭成功");
			otQuestionnaire.setState(OnlineTestQuestionnaire.close);
			otQuestionnaireService.update(otQuestionnaire);
		}

		return ControllerUser.to_zxcy_home(null, session, modelMap);
	}

	/*
	 * 删除测验的问题
	 */
	@Login
	@RequestMapping(value = "/question/{q_id}/delete.do")
	public ModelAndView question_delete(@PathVariable String q_id, String cy_id) {
		OnlineTestQuestion otq = new OnlineTestQuestion();
		otq.setId(q_id);
		otQuestionService.delete(otq);
		return new ModelAndView("redirect:/zxcy/" + cy_id + "/question.htm");
	}

	/*
	 * 根据问题查看所有选项
	 */
	@Login
	@RequestMapping(value = "/{q_id}/option.htm")
	public ModelAndView zxcy_option(@PathVariable String q_id, ModelMap modelMap) {

		List<OnlineTestOption> otOptions = otOptionService
				.getOptionListByQuestion_ID(q_id);
		OnlineTestQuestion otq = otQuestionService.selectByID(q_id);
		otq.setOnlineTestOptions(otOptions);
		OnlineTestQuestionnaire otqn = otQuestionnaireService.selectByID(otq
				.getQuestionnaire_id());
		otqn.setOnlineTestQuestion(otq);
		modelMap.put("cy", otqn);
		return new ModelAndView("zxcy/OnlineTestOption");

	}

	/*
	 * 新建选项的页面
	 */
	@Login
	@RequestMapping(value = "/option/{q_id}/new.htm")
	public ModelAndView zxcy_newOption(@PathVariable String q_id, String o_id,
			ModelMap modelMap) {

		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.getOnlineTestQuestionnaireAndQuestionByQuestion_ID(q_id);
		OnlineTestOption otOption = otOptionService.selectByID(o_id);
		otQuestionnaire.getOnlineTestQuestion().setOnlineTestOption(otOption);
		if ("".equals(o_id) == false && o_id != null) {
			otOption = otOptionService.selectByID(o_id);
		} else {
			otOption = new OnlineTestOption();
			otOption.setQuestion_id(q_id);
		}

		modelMap.put("cy", otQuestionnaire);
		return new ModelAndView("zxcy/newOnlineTestOption");

	}

	/*
	 * 提交新建测验的问题的选项
	 */
	@Login
	@RequestMapping(value = "/option/insert.do")
	public ModelAndView zxcy_option_insert(OnlineTestOption otOption) {

		otOption = otOptionService.insertOrUpdate(otOption);
		return new ModelAndView("redirect:/zxcy/" + otOption.getQuestion_id()
				+ "/option.htm");
	}

	/*
	 * 删除测验的问题的选项
	 */
	@Login
	@RequestMapping(value = "/option/{o_id}/delete.do")
	public ModelAndView option_delete(@PathVariable String o_id, String q_id) {

		OnlineTestOption otOption = new OnlineTestOption();
		otOption.setId(o_id);
		otOptionService.delete(otOption);

		return new ModelAndView("redirect:/zxcy/" + q_id + "/option.htm");
	}

	/*
	 * 改变问题的正确答案
	 */
	@Login
	@RequestMapping(value = "/option/{option_id}/optionAnswer.do")
	public ModelAndView onlineTestOptionAnswerState(ModelMap modelMap,
			@PathVariable String option_id) {

		OnlineTestOption otOption = otOptionService.selectByID(option_id);
		OnlineTestQuestion otQuestion = otQuestionService.selectByID(otOption
				.getQuestion_id());
		if (OnlineTestOption.answer_true.equals(otOption.getAnswer())) {
			otOption.setAnswer(OnlineTestOption.answer_false);
		} else {
			otOption.setAnswer(OnlineTestOption.answer_true);
		}
		// 如果是单选，将其他问题设置为错误选项
		if (OnlineTestQuestion.type_singleChoice.equals(otQuestion.getType())) {

			OnlineTestOption singOption = new OnlineTestOption();
			singOption.setQuestion_id(otQuestion.getId());
			List<OnlineTestOption> options = otOptionService
					.selectList(singOption);
			for (OnlineTestOption onlineTestOption : options) {
				if (OnlineTestOption.answer_false.equals(onlineTestOption
						.getAnswer()) == false) {
					onlineTestOption.setAnswer(OnlineTestOption.answer_false);
					otOptionService.update(onlineTestOption);
				}
			}
		}

		otOptionService.update(otOption);
		return this.zxcy_option(otOption.getQuestion_id(), modelMap);

	}

	/*
	 * 预览测验
	 */

	@Login
	@RequestMapping(value = "/{cy_id}/onlineTestPreview.htm")
	public ModelAndView onlineTestPreview(HttpSession session,
			ModelMap modelMap, @PathVariable String cy_id) {

		// 获取测验
		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.getOnlineTestQuestionnaireAndQuestionAndOptionByQn_id(cy_id);

		// 将问题放到session中
		session.setAttribute("otQuestionnairePreview", otQuestionnaire);

		return new ModelAndView("redirect:/zxcy/onlineTestPreview.htm?page=0");

	}

	/*
	 * 预览测验准备一道题
	 */
	@Login
	@RequestMapping(value = "/onlineTestPreview.htm")
	public ModelAndView onLineTestPreviewShow(String page, HttpSession session,
			ModelMap modelMap) {
		int num = 0;
		try {
			num = Integer.parseInt(page);
		} catch (Exception e) {
			return null;
		}

		OnlineTestQuestionnaire otQuestionnaire = (OnlineTestQuestionnaire) session
				.getAttribute("otQuestionnairePreview");
		List<OnlineTestQuestion> list_question = otQuestionnaire
				.getOnlineTestQuestions();

		if (num == list_question.size()) {
			modelMap.put("result", "预览问卷并不计入结果，不生成结果，请返回首页");
			return new ModelAndView("zxcy/result");
		}

		OnlineTestQuestion otQuestion = list_question.get(num);
		otQuestionnaire.setOnlineTestQuestion(otQuestion);

		modelMap.put("cy", otQuestionnaire);
		modelMap.put("page", page);

		return new ModelAndView("zxcy/OnlineTestPreview");

	}

	/*
	 * 开始填写测验填写前的准备
	 */
	@Login
	@RequestMapping(value = "/{cy_id}/onlineTest.htm")
	public ModelAndView onlineTestAnswer(HttpSession session,
			ModelMap modelMap, @PathVariable String cy_id) {

		User user = CommonSession.getUser(session, userService);
		if (user == null) {
			return new ModelAndView("login");
		}

		// 获取测验
		OnlineTestQuestionnaire otQuestionnaire = otQuestionnaireService
				.getOnlineTestQuestionnaireAndQuestionAndOptionByQn_id(cy_id);

		if (OnlineTestQuestionnaire.close.equals(otQuestionnaire.getState())) {
			modelMap.put("result", "测验已被关闭，无法填写");
			return new ModelAndView("zxcy/result");
		}

		// 将问题放到session中
		session.setAttribute("otQuestionnaire", otQuestionnaire);

		OnlineTestQnAnswer otQnAnswer = otQnAnswerService
				.selectByQuestionnaireIDAndUserID(cy_id, user.getId());

		// 如果测验已经在批改了就不能再答了
		if (!OnlineTestQnAnswer.SUMBIT_STATE_OPEN.equals(otQnAnswer.getTemp1())) {

			if (otQnAnswer.getTemp1() != null) {
				modelMap.put("result", "测验已被批改，无法重新答题");
				return new ModelAndView("zxcy/result");
			}

		}

		// 如果提交过测验，则删除提交的测验
		if (otQnAnswer.getEnddate() != null
				&& !"".equals(otQnAnswer.getEnddate())) {
			otQnAnswerService.delete(otQnAnswer);
			otQnAnswer = new OnlineTestQnAnswer();
		}

		if (otQnAnswer.getId() == null) {
			Timestamp time = Common.getNowTime();
			otQnAnswer.setStartdate(time);
			otQnAnswer.setQuestionnaire_id(cy_id);
			otQnAnswer.setUser_id(user.getId());
			otQnAnswerService.insert(otQnAnswer);
		}

		session.setAttribute("otQnAnswer", otQnAnswer);

		return new ModelAndView("redirect:/zxcy/onlineTest.htm?page=0");

	}

	/*
	 * 每一道测验的准备
	 */
	@Login
	@RequestMapping(value = "/onlineTest.htm")
	public ModelAndView onLineTestShow(String page, HttpSession session,
			ModelMap modelMap) {
		int num = 0;
		try {
			num = Integer.parseInt(page);
		} catch (Exception e) {
			return null;
		}

		OnlineTestQuestionnaire otQuestionnaire = CommonSession
				.getOTestQuestionnaire(session);
		List<OnlineTestQuestion> list_question = otQuestionnaire
				.getOnlineTestQuestions();
		OnlineTestQuestion otQuestion = list_question.get(num);
		otQuestionnaire.setOnlineTestQuestion(otQuestion);

		String otQnAnswer_id = ((OnlineTestQnAnswer) session
				.getAttribute("otQnAnswer")).getId();
		OnlineTestQAnswer otQAnswer = new OnlineTestQAnswer();
		otQAnswer.setQuestion_id(otQuestion.getId());
		otQAnswer.setQnanswer_id(otQnAnswer_id);
		List<OnlineTestQAnswer> otQAnswers = otQAnswerService
				.selectList(otQAnswer);
		if (otQAnswers.size() > 0) {
			otQAnswer = otQAnswers.get(0);
			for (OnlineTestOption option : otQuestion.getOnlineTestOptions()) {
				if (otQAnswer.getAnswertext().contains(option.getId())) {
					option.setChecked("checked");
				} else {
					option.setChecked("");
				}
			}
		}

		modelMap.put("cy", otQuestionnaire);
		modelMap.put("page", page);
		modelMap.put("otQAnswer", otQAnswer);

		return new ModelAndView("zxcy/OnlineTest");

	}

	/*
	 * 测验提交的post
	 */
	@Login
	@RequestMapping(value = "/onlineTest.do")
	public ModelAndView onLineTestSumbit(String page,
			OnlineTestQAnswer otQAnswer, HttpSession session,
			ModelMap modelMap, RedirectAttributes attr) {

		// 获取测验
		OnlineTestQuestionnaire otQuestionnaire = CommonSession
				.getOTestQuestionnaire(session);

		// 判断测验是否重复提交
		if (otQuestionnaire == null) {
			attr.addFlashAttribute("result", "请勿重复提交页面，请重新开始测验。");
			return new ModelAndView("redirect:/zxcy/home.htm");
		}

		// 获取测验问题
		OnlineTestQuestion otQuestion = otQuestionnaire.getOnlineTestQuestion();
		List<OnlineTestOption> options = otQuestion.getOnlineTestOptions();

		// 1、多选 2、单选
		if (otQAnswer.getAnswertext() != null
				&& OnlineTestQuestion.type_choice
						.contains(otQuestion.getType())) {
			for (OnlineTestOption otOption : options) {
				if (otQAnswer.getAnswertext().contains(otOption.getId())) {
					if (OnlineTestQAnswer.validity_true.equals(otOption
							.getAnswer())) {
						otQAnswer.setValidity(OnlineTestQAnswer.validity_true);
					} else {
						otQAnswer.setValidity(OnlineTestQAnswer.validity_false);
						break;
					}
				}
				if ("1".equals(otOption.getAnswer())) {
					if (!otQAnswer.getAnswertext().contains(otOption.getId())) {
						otQAnswer.setValidity(OnlineTestQAnswer.validity_false);
						break;
					}
				}
			}
		} else if (otQAnswer.getAnswertext() != null
				&& !OnlineTestQuestion.type_choice.contains(otQuestion
						.getType())) {
			otQAnswer.setValidity(OnlineTestQAnswer.validity_false);
		} else {
			otQAnswer.setValidity(OnlineTestQAnswer.validity_false);
			otQAnswer.setAnswertext("");
		}
		// 如果回答正确，那么获得分数，否则0分
		if (otQuestion.getScore() != null
				&& OnlineTestQAnswer.validity_true.equals(otQAnswer
						.getValidity())) {
			otQAnswer.setScore(otQuestion.getScore());
		} else {
			otQAnswer.setScore("0");
		}
		otQAnswerService.insertOrUpdate(otQAnswer);

		// 如果完成测验最后提交
		if ("sumbit".equals(page)) {

			OnlineTestQnAnswer otQnAnswer = (OnlineTestQnAnswer) session
					.getAttribute("otQnAnswer");
			otQnAnswer.setEnddate(Common.getNowTime());
			otQnAnswerService.update(otQnAnswer);

			session.removeAttribute("otQuestionnaire");
			session.removeAttribute("otQnAnswer");

			modelMap = queryOnlineTestFruit(modelMap, otQuestionnaire,
					otQnAnswer);
			modelMap.put("cy", otQuestionnaire);

			return new ModelAndView("zxcy/OnlineTestFruit");
		} else {
			return new ModelAndView("redirect:/zxcy/onlineTest.htm?page="
					+ page);
		}

	}

	// TODO: 显示上传测验页面
	@Login
	@RequestMapping(value = "/uploadOnlineTest.htm")
	public String zxcy_upload_zxcy_htm(ModelMap modelMap,
			HttpServletRequest request) {

		List<Org> org_list = orgService.selectList(null);
		modelMap.put("org", org_list);
		modelMap.put("result", CommonSession.queryResultValue(request));
		System.out.println(CommonSession.queryResultValue(request));
		return "zxcy/upload/uploadOnlineTest";
	}

	// TODO : 导入测验Post请求
	@Login
	@RequestMapping(value = "/uploadOnlineTest.do")
	public String onlineTest_upload_onlineTest_do(HttpServletRequest request,
			RedirectAttributes attr,
			OnlineTestQuestionnaire onlineTestQuestionnaire) {

		// 设置测验的ID
		onlineTestQuestionnaire.setId(Common.returnUUID());
		// 设置用户ID
		User user = CommonSession.getUser(request.getSession(), userService);
		onlineTestQuestionnaire.setUser_id(user.getId());

		MultipartFile file = null;
		try {
			file = Common.getUpliadFile(request);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			attr.addFlashAttribute("result", "上传测验错误，信息：" + e.getMessage());
			return "redirect:/zxcy/uploadOnlineTest.htm";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			attr.addFlashAttribute("result", "上传测验错误，信息：" + e.getMessage());
			return "redirect:/zxcy/uploadOnlineTest.htm";
		}
		if (file != null) {

			try {
				Workbook book = Workbook.getWorkbook(file.getInputStream());
				// 获得第一个工作表对象
				Sheet sheet = book.getSheet(0);

				List<OnlineTestQuestion> onlineTestQuestions = new ArrayList<OnlineTestQuestion>();

				int i = 1;
				int trueAnswerTitleNum = 0;
				while (true) {

					trueAnswerTitleNum++;
					Cell cell = sheet.getCell(trueAnswerTitleNum, i);
					if (cell.getContents().contains("正确答案")) {
						break;
					}
				}
				i++;
				while (true) {
					int j = 0;
					// 获取第1列第3行
					Cell cell = sheet.getCell(j, i);
					// 如果值不为空
					if ("".equals(cell.getContents()) == false) {

						OnlineTestQuestion onlineTestQuestion = new OnlineTestQuestion();
						// 设置问题标题
						onlineTestQuestion.setTitle(cell.getContents());
						// 设置问题的ID
						onlineTestQuestion.setId(Common.returnUUID());
						// 设置问题的questionnaireID
						onlineTestQuestion
								.setQuestionnaire_id(onlineTestQuestionnaire
										.getId());

						// 设置问题类型
						++j;
						cell = sheet.getCell(j, i);
						String type = null;

						/*
						 * 原程序 if ("单选".equals(cell.getContents())) { type =
						 * OnlineTestQuestion.type_singleChoice; } else if
						 * ("多选".equals(cell.getContents())) { type =
						 * OnlineTestQuestion.type_multipleChoice; } else if
						 * ("文本".equals(cell.getContents())) { type =
						 * OnlineTestQuestion.type_text; } else if
						 * ("说明".equals(cell.getContents())) { type =
						 * OnlineTestQuestion.type_illustration; } else {
						 * attr.addFlashAttribute("result", "上传测验错误，信息：" +
						 * onlineTestQuestion.getTitle() +
						 * "--中的问题类型不能为空，请检查表，修改后再尝试"); return
						 * "redirect:/zxcy/uploadOnlineTest.htm"; }
						 */

						// 使用表驱动的方法
						type = OnlineTestQuestion
								.getTypeNum(cell.getContents());
						if (type == null) {
							attr.addFlashAttribute("result", "上传测验错误，信息："
									+ onlineTestQuestion.getTitle()
									+ "--中的问题类型不能为空，请检查表，修改后再尝试");
							return "redirect:/zxcy/uploadOnlineTest.htm";
						}

						onlineTestQuestion.setType(type);

						// 设置问题是否必答
						++j;
						cell = sheet.getCell(j, i);
						String isT;
						if ("是".equals(cell.getContents())) {
							isT = "1";
						} else if ("否".equals(cell.getContents())) {
							isT = "0";
						} else {
							attr.addFlashAttribute("result", "上传测验错误，信息："
									+ onlineTestQuestion.getTitle()
									+ "--中的问题是否必答选项不能为空，请检查表，修改后再尝试");
							return "redirect:/zxcy/uploadOnlineTest.htm";
						}
						onlineTestQuestion.setAnswer(type);

						// 设置问题是否有其他选项
						++j;
						cell = sheet.getCell(j, i);
						if ("是".equals(cell.getContents())) {
							isT = "1";
						} else if ("否".equals(cell.getContents())) {
							isT = "0";
						} else {
							attr.addFlashAttribute("result", "上传测验错误，信息："
									+ onlineTestQuestion.getTitle()
									+ "--中的问题是否有其他选项不能为空，请检查表，修改后再尝试");
							return "redirect:/zxcy/uploadOnlineTest.htm";
						}
						onlineTestQuestion.setOther(isT);

						// 设置测验问题样式
						++j;
						cell = sheet.getCell(j, i);
						String style = "";
						if ("默认".equals(cell.getContents())) {
							style = OnlineTestQuestion.style_default;
						} else {
							attr.addFlashAttribute("result", "上传测验错误，信息："
									+ onlineTestQuestion.getTitle()
									+ "--中的问题样式选项不能为空，请检查表，修改后再尝试");
							return "redirect:/zxcy/uploadOnlineTest.htm";
						}
						onlineTestQuestion.setStyle(style);
						List<OnlineTestOption> onlineTestOptions = new ArrayList<OnlineTestOption>();

						// 多选或者是单选设置问题的选项
						if (OnlineTestQuestion.type_choice
								.contains(onlineTestQuestion.getType())) {
							while (true) {
								++j;
								cell = sheet.getCell(j, i);
								if ("".equals(cell.getContents()) == false) {
									OnlineTestOption option = new OnlineTestOption();
									option.setQuestion_id(onlineTestQuestion
											.getId());
									option.setTitle(cell.getContents());
									onlineTestOptions.add(option);
								} else {
									cell = sheet.getCell(trueAnswerTitleNum, i);
									String[] trueAnswer = cell.getContents()
											.split(";");
									int trueNum = 0;
									for (String str : trueAnswer) {

										// 如果是单选题，那么正确答案只能有一个
										if (OnlineTestQuestion.type_singleChoice
												.equals(onlineTestQuestion
														.getType())
												&& trueNum >= 1) {
											attr.addFlashAttribute(
													"result",
													"上传测验错误，信息："
															+ onlineTestQuestion
																	.getTitle()
															+ "--是一个单选题，那么它的正确答案不应该大于1个，请检查后重新提交");
											return "redirect:/zxcy/uploadOnlineTest.htm";
										}
										int num = Integer.parseInt(str);

										// 判断正确数值是否正确
										try {
											onlineTestOptions
													.get(num - 1)
													.setAnswer(
															OnlineTestOption.answer_true);
										} catch (Exception e) {
											attr.addFlashAttribute(
													"result",
													"上传测验错误，信息："
															+ onlineTestQuestion
																	.getTitle()
															+ "--正确选项的数值超过了选项的数量，请检查正确选项的数值后重新提交");
											return "redirect:/zxcy/uploadOnlineTest.htm";
										}
										trueNum++;
									}
									break;
								}
							}

							if (onlineTestOptions.size() < 3) {
								attr.addFlashAttribute("result", "上传测验错误，信息："
										+ onlineTestQuestion.getTitle()
										+ "--是以个选择题，那么它的选项不能少于2个，请检查表，修改后再尝试");
								return "redirect:/zxcy/uploadOnlineTest.htm";
							}
						}

						// 将选项添加到问题里
						onlineTestQuestion
								.setOnlineTestOptions(onlineTestOptions);
						onlineTestQuestions.add(onlineTestQuestion);
						i++;
					} else {
						break;
					}
					onlineTestQuestionnaire
							.setOnlineTestQuestions(onlineTestQuestions);
				}
				book.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {
			attr.addFlashAttribute("result", "上传测验错误，信息：" + "文件异常，文件是空的");
			return "redirect:/zxcy/uploadOnlineTest.htm";
		}

		// 开始上传测验

		otQuestionnaireService.insertManualID(onlineTestQuestionnaire);

		List<OnlineTestQuestion> onlineTestQuestions = onlineTestQuestionnaire
				.getOnlineTestQuestions();
		for (OnlineTestQuestion onlineTestQuestion : onlineTestQuestions) {

			otQuestionService.insertManualID(onlineTestQuestion);

			List<OnlineTestOption> onlineTestOptions = onlineTestQuestion
					.getOnlineTestOptions();
			for (OnlineTestOption onlineTestOption : onlineTestOptions) {

				otOptionService.insert(onlineTestOption);

			}
		}

		attr.addFlashAttribute(onlineTestQuestionnaire.getTitle()
				+ "，测验上传并生成成功。");
		return "redirect:/zxcy/home.htm";

	}

	/**
	 * 算出答题结果
	 * 
	 * @param modelMap
	 * @param otQuestionnaire
	 * @param otQnAnswer
	 * @return
	 */
	public ModelMap queryOnlineTestFruit(ModelMap modelMap,
			OnlineTestQuestionnaire otQuestionnaire,
			OnlineTestQnAnswer otQnAnswer) {
		//吴敬国 2015-6-15 start 修改所要显示的答题结果，加上主观题个数，总分，正确率等都改为客观题的
		OnlineTestQuestion otQuestion = new OnlineTestQuestion();
		otQuestion.setType(OnlineTestQuestion.type_singleChoice);
		otQuestion.setQuestionnaire_id(otQuestionnaire.getId());
		float count = otQuestionService.selectCount(otQuestion);
		otQuestion.setType(OnlineTestQuestion.type_multipleChoice);
		count = count + otQuestionService.selectCount(otQuestion);//客观题的个数
		float count_subjective=0;
		if (OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT
				.equals(otQnAnswer.getTemp1())) {
			otQuestion.setType(OnlineTestQuestion.type_text);
			//count = count + otQuestionService.selectCount(otQuestion);
			count_subjective =  otQuestionService.selectCount(otQuestion);//主观题的个数
			modelMap.put("typeText", "主观题成绩已批改");
		} else {
			modelMap.put("typeText", "主观题成绩未批改，待老师修改后显示，只显示了选择题的成绩");
		}
		float sumValidity = Float.parseFloat(otQAnswerService
				.sumValidityByQnAnswerID(otQnAnswer.getId()));//所有题答对的个数
		
		/*float sumScore = Float.parseFloat(otQAnswerService
				.sumScoreByQnAnswerID(otQnAnswer.getId()));*///所有题的总分
		float sumScore_singleChoice=otQAnswerService.sumScoreByQuestionType(OnlineTestQuestion.type_singleChoice, otQnAnswer.getId());//单选题的总分
		float sumScore_multipleChoice=otQAnswerService.sumScoreByQuestionType(OnlineTestQuestion.type_multipleChoice, otQnAnswer.getId());//多选题的总分
		int percentage = (int) ((sumValidity-count_subjective) / count * 100);//客观题正确率
		modelMap.put("count_subjective", count_subjective);//主观题的个数
		modelMap.put("count", count);//客观题的个数
		modelMap.put("sumValidity", sumValidity-count_subjective);//客观题答对的题数
		modelMap.put("sumScore", sumScore_singleChoice+sumScore_multipleChoice);//客观题的总分
		modelMap.put("percentage", percentage);//客观题正确率
		//吴敬国 2015-6-15 end 修改所要显示的答题结果，加上主观题个数，总分，正确率等都改为客观题的 原方法在下面注释，方便恢复
		return modelMap;
	}
	/**
	 * 算出答题结果
	 * 
	 * @param modelMap
	 * @param otQuestionnaire
	 * @param otQnAnswer
	 * @return
	 */
	/*public ModelMap queryOnlineTestFruit(ModelMap modelMap,
			OnlineTestQuestionnaire otQuestionnaire,
			OnlineTestQnAnswer otQnAnswer) {

		OnlineTestQuestion otQuestion = new OnlineTestQuestion();
		otQuestion.setType(OnlineTestQuestion.type_singleChoice);
		otQuestion.setQuestionnaire_id(otQuestionnaire.getId());
		float count = otQuestionService.selectCount(otQuestion);
		otQuestion.setType(OnlineTestQuestion.type_multipleChoice);
		count = count + otQuestionService.selectCount(otQuestion);
		if (OnlineTestQnAnswer.SUMBIT_STATE_SUMBIT
				.equals(otQnAnswer.getTemp1())) {
			otQuestion.setType(OnlineTestQuestion.type_text);
			count = count + otQuestionService.selectCount(otQuestion);
			modelMap.put("typeText", "主观题成绩已批改");
		} else {
			modelMap.put("typeText", "主观题成绩未批改，待老师修改后显示，只显示了选择题的成绩");
		}
		float sumValidity = Float.parseFloat(otQAnswerService
				.sumValidityByQnAnswerID(otQnAnswer.getId()));
		float sumScore = Float.parseFloat(otQAnswerService
				.sumScoreByQnAnswerID(otQnAnswer.getId()));
		int percentage = (int) (sumValidity / count * 100);

		modelMap.put("count", count);
		modelMap.put("sumValidity", sumValidity);
		modelMap.put("sumScore", sumScore);
		modelMap.put("percentage", percentage);

		return modelMap;
	}*/
	

	/**
	 * 判断测验的问题是否全部是选择题
	 * 
	 * @param otQuestionnaire
	 * @return
	 */
	public boolean onlineTestQuestionTypeIsChooice(
			OnlineTestQuestionnaire otQuestionnaire) {
		List<OnlineTestQuestion> olQuestions = otQuestionnaire
				.getOnlineTestQuestions();
		for (OnlineTestQuestion otQuestion : olQuestions) {
			if (!OnlineTestQuestion.type_choice.contains(otQuestion.getType())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 查询用户是否填过测验
	 * 
	 * @param otQuestionnaire
	 * @param session
	 * @return
	 */
	public boolean queryOnlineTestQnAnswerIsSumbit(String cy_id,
			HttpSession session) {

		User user = CommonSession.getUser(session, userService);
		OnlineTestQnAnswer otAnswer = new OnlineTestQnAnswer();
		otAnswer.setQuestionnaire_id(cy_id);
		otAnswer.setUser_id(user.getId());
		List<OnlineTestQnAnswer> otAnswers = otQnAnswerService
				.selectList(otAnswer);
		if (otAnswers.size() > 0) {
			otAnswer = otAnswers.get(0);
		} else {
			return false;
		}

		if (otAnswer.getEnddate() != null && !"".equals(otAnswer)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 查看回答的测验是否提交 已提交 true 未提交 false
	 * 
	 * @param otQnAnswer
	 * @return
	 */
	public boolean queryOnlineTestIsSumbit(OnlineTestQnAnswer otQnAnswer) {
		if (otQnAnswer.getEnddate() == null) {
			return false;
		} else {
			return true;
		}
	}

}
