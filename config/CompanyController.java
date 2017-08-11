package com.sict.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.EvalStu;
import com.sict.entity.PracticeRecord;
import com.sict.entity.RightRegion;
import com.sict.entity.Teacher;
import com.sict.service.EvalStuService;
import com.sict.service.PracticeRecordService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.util.Common;
import com.sict.util.Ichartjs_Color;

@Controller
public class CompanyController {
	/**
	 * 注入practiceRecordService by王磊20140925 *
	 * 
	 * */
	@Resource(name = "practiceRecordService")
	private PracticeRecordService practiceRecordService;
	
	/**
	 * 注入studentService
	 * @author zcg 2015-2-3
	 */
	@Resource(name = "studentService")
	private StudentService studentService;
	
	/**
	 * 注入teacherService wtt20140910 
	 */
	@Resource(name = "evalstuService")
	private EvalStuService evalstuService;

	/**
	 * 注入EvalStuService
	 * @author zcg 2015-2-3
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 功能：主页显示
	 * by：孙磊
	 * 2015年5月26日
	 * 
	 */
	@RequestMapping("company/index.do")
  	public ModelAndView index() throws IOException {
		
		return new ModelAndView("company/index");
		}

	/**
	 * 功能：根据企业Id获得在企业实习的学生
	 * by：孙磊
	 * 2015年5月26日
	 * 
	 */
  @RequestMapping("company/evalStuList.do")
	public ModelAndView selectByComTeacherId(HttpServletRequest request,
		HttpServletResponse response,HttpSession session) {
	//根据登录名获得企业指导老师的Id
	Teacher tea = (Teacher) session.getAttribute("current_user");
	//获得infor参数，进行保存成功提示
	String infor = request.getParameter("infor");
	if(infor==null){
		infor="error";
	}
		//根据企业指导老师id获得所带的的学生
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("DeptId", tea.getDept_id());
	map.put("ComTeacherId", tea.getId());
	List<PracticeRecord> stu = this.practiceRecordService.selectByComTeacherId(map);
	Map<String, Object> map1 = new HashMap<String, Object>();
	map1.put("stu", stu);
	map1.put("infor", infor);
	return new ModelAndView("company/evalStuList",map1);
	}
  /**
	 * 功能：评价学生
	 * by：孙磊
	 * 2015年5月26日
 * @throws IOException 
	 * 
	 */
  @RequestMapping("company/submitForm.do") 
  public String saveForm(HttpServletRequest request,
		  HttpServletResponse response,HttpSession session) {
	    //获取前台传回的信息
	  String id = Common.returnUUID();
		String eval_type = "1";
		String eval_title = request.getParameter("evaltitle");
		int eval_score =Integer.parseInt(request.getParameter("score"));
		String eval_content = request.getParameter("task_Desc");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id =  tea.getId();
		String stu_id = request.getParameter("stu_id");
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(date);
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//进行数据库数据的更新
		EvalStu evalstu = new EvalStu();
		evalstu.setId(id);
		evalstu.setEval_type(eval_type);
		evalstu.setEval_title(eval_title);
		evalstu.setEval_score(eval_score);
		evalstu.setEval_content(eval_content);
		evalstu.setTea_id(tea_id);
		evalstu.setStu_id(stu_id);
		evalstu.setEval_time(ts1);
		evalstuService.insert(evalstu);
		return "redirect:evalStuList.do?infor=success";
	     
  }
  /**
	 * 功能：查看历史评价   注解请求地址(映射)  sl
	 * 
	 * */

  @RequestMapping("company/historyEval.do") 
  public ModelAndView usestuId(HttpServletRequest request,
		  HttpServletResponse response,HttpSession session,String stu_id) {
		List<EvalStu> eval = this.evalstuService.selectByStuid(stu_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("eval", eval);
	  return new ModelAndView("company/historyEval" ,map);
  
  }
  /**
	 * 功能：维护教师 注解请求地址(映射) sl
	 * 
	 * */

	@RequestMapping("company/editmyInfo.do")
	public ModelAndView editmyInfo(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		List<Teacher> teaList = this.teacherService
				.selectListByTeaCode(tea_code);// selectByCode(tea_code);
		map.put("teaList", teaList);
		return new ModelAndView("company/editmyInfo", map);

	}
	@RequestMapping("company/PasswordEdit.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		List<Teacher> teaList = this.teacherService
				.selectListByTeaCode(tea_code);// selectByCode(tea_code);
		map.put("teaList", teaList);
		return new ModelAndView("company/passwordEdit", map);

	}

	/**
	 * 功能：修改密码 注解请求地址(映射) sl
	 * 
	 * */

	@RequestMapping("company/doPasswordEdit.do")
	public String doPasswordEdit(@ModelAttribute("teacher") Teacher teacher)
			throws IOException {
		teacherService.update(teacher);
		return "redirect:../login.jsp"; // 添加成功后重定向到列表页面
	}
}
