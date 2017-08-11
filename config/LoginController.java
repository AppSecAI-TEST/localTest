package com.sict.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import com.sict.common.CommonSession;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.entity.UserRole;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.service.UserRoleService;

/**
 * SpringMVC 控制器 用于登录以后，根据人员类型确定首页，并且构造用户对象并存入Session
 * 
 * @author Administrator
 * 
 */

enum User_type {
	student, teacher
};

@Controller
public class LoginController {

	private static final long serialVersionUID = 1L;

	@Resource(name = "studentService")
	private StudentService studentService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	@Resource(name = "userRoleService")
	private UserRoleService userRoleService;

	@RequestMapping("login.do")
	public ModelAndView login(HttpSession session, HttpServletRequest request) {

		try {
			if (CommonSession.isRoleTeacher(session)) {
				return new ModelAndView("redirect:/teacher/index.do");
			} else if (CommonSession.isRoleAdmin(session)) {
				return new ModelAndView("redirect:/admin/index.do");
			} else if (CommonSession.isRoleStudent(session)) {
				return new ModelAndView("redirect:/student/index.do");
			} else {
				return new ModelAndView("redirect:/login");
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/login");
		}

	}

	@RequestMapping("logout.do")
	public String logout(HttpSession session) {
		session.removeAttribute("current_user");

		return "redirect:/j_spring_security_logout";
	}

	public StringBuffer getUserRole(String name) {
		List<Map<String, String>> map_list = userRoleService
				.selectview_user_by_username(name);
		StringBuffer sb = new StringBuffer();
		for (Map<String, String> map : map_list) {
			sb.append(map.get("role_name") + ";");
		}
		return sb;
	}

}
