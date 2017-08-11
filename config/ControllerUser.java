package com.sict.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceView;

import com.sict.authority.Login;
import com.sict.common.CommonSession;
import com.sict.entity.Org;
import com.sict.service.OrgService;

enum userType {
	admin, teacher, student, teacherAnswer
}

@Controller
public class ControllerUser {

	
	@Login
	@RequestMapping("/wjdc/user/home.htm")
	public ModelAndView to_home(HttpServletRequest request,
			HttpSession session, String type) {

		int num = getUserType(session, type);
		// 获取用户信息
		switch (num) {
		case 1:
			return new ModelAndView(new InternalResourceView(
					"/wjdc/questionnaire/admin.htm"));
		case 2:
			return new ModelAndView(new InternalResourceView(
					"/wjdc/questionnaire/teacher_answer.htm"));
		case 3:
			return new ModelAndView(new InternalResourceView(
					"/wjdc/questionnaire/teacher.htm"));
		case 4:
			return new ModelAndView(new InternalResourceView(
					"/wjdc/questionnaire/student.htm"));
		default:
			return new ModelAndView("login");
		}

	}

	@Login
	@RequestMapping("/summary/user/home.htm")
	public static  ModelAndView to_summary_home(HttpServletRequest request,HttpSession session,
			String type, ModelMap modelMap,RedirectAttributes attr,String college_id) {
		if(college_id!=null){
			session.setAttribute("stu_college_id", college_id);
		}
		int num = getUserType(session, type);
		attr.addFlashAttribute("result", CommonSession.queryResultValue(request));
		String current_role_selected=(String) session.getAttribute("current_role_selected");
		try {
			if(current_role_selected.contains("ROLE_LEADER")){
				return new ModelAndView(new InternalResourceView("/summary/leader.htm"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 获取用户信息
		switch (num) {
		case 1:
			return new ModelAndView(new InternalResourceView("/summary/admin.htm"));
		case 2:
			return new ModelAndView(new InternalResourceView("/summary/teacher_answer.htm"));
		case 3:
			return new ModelAndView(new InternalResourceView("/summary/teacher.htm"));
		case 4:
			return new ModelAndView(new InternalResourceView("/summary/student.htm"));
		default:
			return new ModelAndView("login");
		}

	}

	@Login
	@RequestMapping("/zxcy/home.htm")
	public static ModelAndView to_zxcy_home(HttpServletRequest request,
			HttpSession session, ModelMap modelMap) {

		int num = getUserType(session, null);
		// 获取用户信息
		switch (num) {
		case 1:
			return new ModelAndView(new InternalResourceView("/zxcy/admin.htm"));
		case 2:
			return new ModelAndView(new InternalResourceView(
					"/zxcy/teacher_answer.htm"));
		case 3:
			return new ModelAndView(new InternalResourceView("/zxcy/admin.htm"));
		case 4:
			return new ModelAndView(new InternalResourceView(
					"/zxcy/student.htm"));
		default:
			return new ModelAndView("login");
		}

	}

	public static int getUserType(HttpSession session, String type) {

		String user_type = CommonSession.getUserType(session);

		int num = 0;
		if ("teacher".equals(user_type)) {
			if (CommonSession.isRoleAdmin(session)) {
				num = 1;
			} else if (CommonSession.isRoleTeacher(session)) {
				if ("t_answer".equals(type)) {
					num = 2;
				} else {
					num = 3;
				}
			} else if (CommonSession.isRoleLeader(session)){
				num = 5;
			}
		} else if ("student".equals(user_type)) {
			num = 4;
		} else {
			num = 0;
		}

		return num;

	}
	
	@Login
	@RequestMapping("/changeUserRole.do")
	@ResponseBody
	public void changeUserRole(HttpSession session, String roleName){
		CommonSession.setUserRole(session, roleName);
	}
	

}
