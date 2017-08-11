package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.entity.Association;
import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.Company;
import com.sict.entity.Courses;
import com.sict.entity.DirectRecord;
import com.sict.entity.EvalsIndex;
import com.sict.entity.EvaluateStandard;
import com.sict.entity.Files;
import com.sict.entity.GroupMembers;
import com.sict.entity.Groups;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.Param;
import com.sict.entity.Position;
import com.sict.entity.PracticeTask;
import com.sict.entity.RecruitInfo;
import com.sict.entity.ReportCountGraduationMaterial;
import com.sict.entity.ReportStuCompany;
import com.sict.entity.ReportStuStateGrade;
import com.sict.entity.ReportTrainDetail;
import com.sict.entity.Role;
import com.sict.entity.StuGraStateCount;
import com.sict.entity.Student;
import com.sict.entity.SysMenu;
import com.sict.entity.SysRoleMenu;
import com.sict.entity.TeaCourses;
import com.sict.entity.TeaStu;
import com.sict.entity.Teacher;
import com.sict.entity.TrainDetail;
import com.sict.entity.TreeNode;
import com.sict.entity.UserRole;
import com.sict.service.AssociationService;
import com.sict.service.CompanyService;
import com.sict.service.CourseService;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateStandardService;
import com.sict.service.ExcelService;
import com.sict.service.FilesService;
import com.sict.service.GroupMembersService;
import com.sict.service.GroupsService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.ParamService;
import com.sict.service.PositionService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RecruitInfoService;
import com.sict.service.ReportDetailGraduationMaterialService;
import com.sict.service.ReportStudentStateService;
import com.sict.service.RoleService;
import com.sict.service.ScoreService;
import com.sict.service.StudentService;
import com.sict.service.SysMenuService;
import com.sict.service.SysRoleMenuService;
import com.sict.service.TeaCoursesService;
import com.sict.service.TeaStuService;
import com.sict.service.TeacherService;
import com.sict.service.TrainDetailService;
import com.sict.service.UserRoleService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.CustomMenuForm;
import com.sict.util.DateService;
import com.sict.util.Ichartjs_Color;

/**
 * 功能：管理员控制器 使用 @Controller 注释 by郑春光20140910 *
 * 
 */
@Controller
public class AdminController {
	/**
	 * 注入teacherService by郑春光20141105 *
	 * 
	 * */ 
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入ScoreService by邢志武20150405 *
	 * 
	 * */
	@Resource(name = "ScoreService")
	private ScoreService scoreService;
	/**
	 * 注入studentService by郑春光20141105 *
	 * 
	 * */
	@Resource(name = "studentService")
	private StudentService studentService;
	/**
	 * 注入directRecordService bY xzw 2015-03-23 *
	 */
	@Resource(name = "directRecordService")
	private DirectRecordService directRecordService;
	/**
	 * 注入teaStuService byccc20141105 *
	 * 
	 * */
	@Resource(name = "teaStuService")
	private TeaStuService teaStuService;
	/**
	 * 注入filesService byccc20141105 *
	 * 
	 * */
	@Resource(name = "filesService")
	private FilesService filesService;
	/**
	 * 注入trainDetailService by吴敬国20141105 *
	 * 
	 * */
	@Resource(name = "trainDetailService")
	private TrainDetailService trainDetailService;
	/**
	 * 注入orgService byccc20141105 *
	 * 
	 * */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * 注入roleService bywtt2014 1130 *
	 * 
	 * */
	@Resource(name = "roleService")
	private RoleService roleService;
	/**
	 * 注入evaluateStandardService by李瑶婷20141105 *
	 * 
	 * */
	@Resource(name = "evaluateStandardService")
	private EvaluateStandardService evaluateStandardService;
	/**
	 * 注入evalsIndexService by李瑶婷20141105 *
	 * 
	 * */
	@Resource(name = "evalsIndexService")
	private EvalsIndexService evalsIndexService;
	/**
	 * 注入userRoleService by吴敬国20141105 *
	 * 
	 * */
	@Resource(name = "userRoleService")
	private UserRoleService userRoleService;
	/**
	 * 注入companyService by李瑶婷20141105 *
	 * 
	 * */
	@Resource(name = "companyService")
	private CompanyService companyService;
	/**
	 * 注入positionService by李瑶婷20141105 *
	 * 
	 * */
	@Resource(name = "positionService")
	private PositionService positionService;
	/**
	 * 注入courseService bywtt2014 1130 *
	 * 
	 * */
	@Resource(name = "courseService")
	private CourseService courseService;
	/**
	 * 注入noticeService bywtt2014 1130 *
	 * 
	 * */
	@Resource(name = "noticeService")
	private NoticeService noticeService;
	/**
	 * 注入groupsService by吴敬国2014 1209 *
	 * 
	 * */
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	/**
	 * 注入practiceTaskService by孙家胜2014 1212 *
	 * 
	 * */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	/**
	 * 注入recruitInfoService by孙家胜2014 1212 *
	 * 
	 * */
	@Resource(name = "recruitInfoService")
	private RecruitInfoService recruitInfoService;
	/**
	 * 注入groupMembersService by吴敬国2014 1216 *
	 * 
	 * */
	@Resource(name = "reportStudentStateService")
	private ReportStudentStateService reportStudentStateService;
	/**
	 * 注入reportDetailGraduationMaterialService by张超 2015年1月28日 *
	 * 
	 * */
	@Resource(name = "reportDetailGraduationMaterialService")
	private ReportDetailGraduationMaterialService reportDetailGraduationMaterialService;
	/**
	 * 注入groupMembersService by吴敬国2014 1216 *
	 * 
	 * */
	@Resource(name = "groupMembersService")
	private GroupMembersService groupMembersService;
	/**
	 * 注入sysMenuService by吴敬国2015-12-21
	 * 
	 * */
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	/**
	 * 注入sysRoleMenuService by李泽2016-04-01
	 * 
	 * */
	@Resource(name = "sysRoleMenuService")
	private SysRoleMenuService sysRoleMenuService;
	/**
	 * 注入TeaCoursesService by 鲁雪艳2016-03-19 *
	 * 
	 * */
	@Resource(name = "teaCoursesService")
	private TeaCoursesService teaCoursesService;
	/**
	 * 注入sysMenuService by吴敬国2015-12-21
	 * 
	 * */
	@Resource(name = "paramService")
	private ParamService paramService;
	/**
	 * 注入sysMenuService by吴敬国2015-12-21
	 * 
	 * */
	@Resource(name = "associationService")
	private AssociationService associationService;
	// 定义返回类型
	String ret = "";

	private Object o;
	private final int pageSizeConstant = Constants.pageSize; // 获取常量分页页面大小
	private String org_id;

	/**
	 * 功能：教师管理 jokerGuo2015-12-29
	 * */
	@RequestMapping("admin/teacherList.do")
	public ModelAndView teacherList(HttpSession session, String type) {
		String teacher_dept_id = (String) session
				.getAttribute("teacher_dept_id");
		// 当前下拉框选中的值，不是当前老师的所在部门
		if (type != null) { // type 为标记，如果不为空，表示之前已经选择过院系，再次进入页面应该清空，重新选择下拉框
			session.removeAttribute("teacher_dept_id");
			teacher_dept_id = null;
		}
		;
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		// 得到该院所有的系和学院本身
		List<Org> collegeAndDepts = this.orgService
				.selectByXueYuanId(college_id);
		// 获取学院教师列表
		List<Teacher> teaList = this.teacherService
				.selectListByXuId(college_id);

		// 分页
		int pageSize = 15;
		int Currentpage = 1;// 初始值1
		/*
		 * if(teaList.size()<pageSizeConstant){//防止集合大小不满足每页记录，进行赋值。
		 * pageSize=teaList.size(); }
		 */
		// 获取当前页集合
		List<Teacher> listCurrentPage = Common.getListCurrentPage(teaList,
				pageSize, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(teaList, pageSize, Currentpage);
		// 放到session，方便分页时调用
		session.setAttribute("xinxi", teaList);
		map.put("count", pageCount);
		map.put("result", listCurrentPage);
		map.put("nowPage", Currentpage);
		map.put("xi", collegeAndDepts);// 下拉框
		map.put("teacher_dept_id", teacher_dept_id);
		return new ModelAndView("admin/teacherList", map);
	}

	/**
	 * 功能：导入成功列表 by 王磊 2015-5-18
	 * */
	@RequestMapping("admin/teacherImportSuccess.do")
	public ModelAndView teacherImportSuccess(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Teacher> teaSuccessList = (List<Teacher>) session
				.getAttribute("tc");// 导入时存的session
		map.put("teaSuccessList", teaSuccessList);
		return new ModelAndView("admin/teacherImportSuccess", map);
	}

	/**
	 * 实现分页功能
	 * 
	 * */
	@RequestMapping("admin/getTeacherByPage.do")
	public String teacherGetByPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		// 获取教师集合，增加条件：下拉框选中的组织id
		List<Teacher> result = (List<Teacher>) session.getAttribute("xinxi");
		String toPage = request.getParameter("toPage");
		List<Teacher> newResult;// 当前页的集合
		newResult = Common.getListCurrentPage(result, pageSizeConstant,
				Integer.parseInt(toPage));
		StringBuffer sb = new StringBuffer();
		for (Teacher ts : newResult) {
			String stateName = "有效";
			String esper = "无";
			if (ts.getState().equals("0")) {
				stateName = "无效";
			}
			if (ts.getExpertise() != null) {
				esper = ts.getExpertise();
			}
			sb.append("<tr>");
			sb.append("	<td align='center'>" + ts.getTea_code()
					+ "</td>");
			sb.append("<td align='center'>" + ts.getTrue_name()
					+ "</td>");
			sb.append("	<td align='center'>" + ts.getSex() + "</td>");
			sb.append("	<td align='center'>" + ts.getPhone() + "</td>");
			sb.append("	<td align='center'>"
					+ DictionaryService.findOrg(ts.getDept_id())
							.getOrg_name() + "</td>");
			sb.append("	<td align='center'>" + ts.getDuties() + "</td>");
			//新添加  by syj 20160406
			if(ts.getQqnum()==null){
				ts.setQqnum("");
			}
			sb.append("	<td align='center'>" + ts.getQqnum() + "</td>");
			if(ts.getEmail()==null){
				ts.setEmail("");
			}
			sb.append("	<td align='center'>" + ts.getEmail() + "</td>");
			
			sb.append("	<td align='center'>" + esper + "</td>");
			sb.append("	<td align='center'>" + stateName + "</td>");
			sb.append("<td align='center'><input type='button' value='重置密码' onclick=rePass('"
					+ ts.getId() + "');></td>");
			sb.append("	<td align='center'><input type='button' value='修改' onclick=doEdit('"
					+ ts.getId() + "');></td>");
			sb.append("<td align='center'> <input type='button' value='删除' onclick=doDel('"
					+ ts.getId() + "');></td>");
			sb.append("</tr>");
		}
		sb.append("FENGETEACHER" + toPage);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 功能：教师管理 -添加教师 王磊，敬国2015-12-29
	 * 
	 * */
	@RequestMapping("admin/addTeacher.do")
	public ModelAndView teacherAdd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> result = this.orgService.selectByXueYuanId(college_id);
		map.put("teachers", result);
		return new ModelAndView("admin/teacherAdd", map);
	}

	/**
	 * 功能：教师管理功能 根据部门id得到相关老师 ajax 王磊，敬国2015-12-29
	 * 
	 * */
	@RequestMapping("admin/ajaxTeacher.do")
	public String teacherajax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String xibu = request.getParameter("xibu");
		session.setAttribute("teacher_dept_id", xibu);
		List<Teacher> newResult;
		if (xibu != null) {
			List<Teacher> t = this.teacherService.getTeachersByDeptId(xibu);
			// 得到总页数
			int pageCount = Common.getPageCount(t, pageSizeConstant, 1);
			newResult = Common.getListCurrentPage(t, pageSizeConstant, 1);
			session.setAttribute("xinxi", t);
			StringBuffer sb = new StringBuffer();
			if (newResult == null) {
				sb.append("");
			} else {
				for (Teacher ts : newResult) {
					String stateName = "有效";
					String esper = "无";
					if (ts.getState().equals("0")) {
						stateName = "无效";
					}
					if (ts.getExpertise() != null) {
						esper = ts.getExpertise();
					}
					sb.append("<tr>");
					sb.append("	<td align='center'>" + ts.getTea_code()
							+ "</td>");
					sb.append("<td align='center'>" + ts.getTrue_name()
							+ "</td>");
					sb.append("	<td align='center'>" + ts.getSex() + "</td>");
					sb.append("	<td align='center'>" + ts.getPhone() + "</td>");
					sb.append("	<td align='center'>"
							+ DictionaryService.findOrg(ts.getDept_id())
									.getOrg_name() + "</td>");
					sb.append("	<td align='center'>" + ts.getDuties() + "</td>");
					//新添加  by syj 20160406
					if(ts.getQqnum()==null){
						ts.setQqnum("");
					}
					sb.append("	<td align='center'>" + ts.getQqnum() + "</td>");
					if(ts.getEmail()==null){
						ts.setEmail("");
					}
					sb.append("	<td align='center'>" + ts.getEmail() + "</td>");
					sb.append("	<td align='center'>" + esper + "</td>");
					sb.append("	<td align='center'>" + stateName + "</td>");
					sb.append("<td align='center'><input type='button' value='重置密码' onclick=rePass('"
							+ ts.getId() + "');></td>");
					sb.append("	<td align='center'><input type='button' value='修改' onclick=doEdit('"
							+ ts.getId() + "');></td>");
					sb.append("<td align='center'> <input type='button' value='删除' onclick=doDel('"
							+ ts.getId() + "');></td>");
					sb.append("</tr>");
				}
			}
			sb.append("FENGETEACHER" + pageCount + "FENGETEACHER" + "共 "
					+ pageCount + " 页");
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
	 * 功能：教师管理-添加教师 王磊
	 * */
	@RequestMapping("admin/doAddTeacher.do")
	public String teacherSave(HttpServletRequest request) throws IOException {
		String tea_id = Common.returnUUID16();
		userRoleService.saveBasicUserRole(tea_id, null, "ROLE_TEACHER", null,null,null);
		String tea_code = request.getParameter("tea_code");
		String true_name = request.getParameter("true_name");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String dept_id = request.getParameter("dept_id");
		String duties = request.getParameter("duties");
		String expertise = request.getParameter("expertise");
		String state = request.getParameter("state");
		Teacher teacher = new Teacher();
		teacher.setLogin_name(tea_code.trim());
		teacher.setLogin_pass(tea_code.trim());
		teacher.setTea_code(tea_code.trim());
		teacher.setTrue_name(true_name.trim());
		teacher.setSex(sex.trim());
		teacher.setPhone(phone.trim());
		teacher.setDept_id(dept_id.trim());
		teacher.setDuties(duties);
		teacher.setExpertise(expertise);
		teacher.setState(state);
		teacher.setId(tea_id.trim());
		teacherService.insert(teacher);
		return "redirect:teacherList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：教师管理-验证添加的教师编号是否与数据库重复 by 王磊 2015年3月19日
	 * 
	 * */
	@RequestMapping("admin/checkTeaCode.do")
	public String teaCodecheck(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		String tea_code = request.getParameter("tea_code");
		int b;
		String infor = "";
		b = this.teacherService.selectByTeaCode(tea_code);
		if (b != 0) {
			infor = "此编号已用";
		}
		try {
			response.getWriter().println(infor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：教师管理-修改教师信息 王磊，敬国2015-12-29
	 * */
	@RequestMapping("admin/editTeacher.do")
	public ModelAndView teacherEdit(String id, HttpSession session) {
		Teacher teacher = (Teacher) teacherService.selectByID(id);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		Map<String, Object> map = new HashMap<String, Object>();
		Org org = new Org();
		org = this.orgService.selectByID(teacher.getDept_id());
		String dept_id = tea.getDept_id();
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		List<Org> o = new ArrayList<Org>();
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
		}
		o = this.orgService.selectByXueYuanId(xy_id);
		for (int i = 0; i < o.size(); i++) {
			if (o.get(i).getId().equals(org.getId())) {
				o.remove(i);
			}
		}
		o.add(0, org);
		List<String> state = new ArrayList<String>();
		state.add(0, "无效");
		state.add(1, "有效");
		if (teacher.getState().equals("1")) {
			state.remove(1);
			state.add(0, "有效");
		}
		List<String> sex = new ArrayList<String>();
		sex.add(0, "男");
		sex.add(1, "女");
		if (teacher.getSex().equals("女")) {
			sex.remove(1);
			sex.add(0, "女");
		}
		List<String> duties = new ArrayList<String>();
		duties.add(0, "院长");
		duties.add(1, "副院长");
		duties.add(2, "系主任");
		duties.add(3, "系副主任");
		duties.add(4, "教师");
		if (teacher.getDuties().equals("院长")) {
			duties.add(0, "院长");
			duties.remove(1);
		} else if (teacher.getDuties().equals("副院长")) {
			duties.add(0, "副院长");
			duties.remove(2);

		} else if (teacher.getDuties().equals("系主任")) {
			duties.add(0, "系主任");
			duties.remove(3);

		} else if (teacher.getDuties().equals("系副主任")) {
			duties.add(0, "系副主任");
			duties.remove(4);

		} else if (teacher.getDuties().equals("教师")) {
			duties.add(0, "教师");
			duties.remove(5);

		}
		map.put("teacher", teacher);
		map.put("orgs", o);
		map.put("state", state);
		map.put("sex", sex);
		map.put("duties", duties);
		return new ModelAndView("admin/teacherEdit", map);
	}

	/**
	 * 功能：管理员编辑教师保存 by wtt 20141212
	 * 
	 * */
	@RequestMapping("admin/doEditTeacher.do")
	public String teacherEditSave(HttpServletRequest request, String state,
			String id) throws IOException {
		String sta;
		if (state.equals("有效")) {
			sta = "1";
		} else {
			sta = "0";
		}
		String true_name = request.getParameter("true_name");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String dept_id = request.getParameter("dept_id");
		String duties = request.getParameter("duties");
		Teacher teacher = DictionaryService.findTeacher(id);
		teacher.setState(sta);
		teacher.setTrue_name(true_name);
		teacher.setSex(sex);
		teacher.setPhone(phone);
		teacher.setDept_id(dept_id);
		teacher.setDuties(duties);
		teacherService.update(teacher);
		return "redirect:teacherList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：教师管理-删除教师 敬国2015-12-29
	 * */
	@RequestMapping("admin/deleteTeacher.do")
	public String teacherDelete(String id) {
		Teacher tea = DictionaryService.findTeacher(id);
		tea.setState("0");
		teacherService.delete(tea);
		return "redirect:teacherList.do";
	}

	/**
	 * 功能：指导教师 by ccc 20141009
	 * 
	 * @return
	 */

	@RequestMapping("admin/guidenceTeacherImportList.do")
	public ModelAndView teacherListGuidance() {
		Map<String, Object> map = new HashMap<String, Object>();
		TeaStu ts = new TeaStu();
		ts.setState("1");
		List<TeaStu> lists = this.teaStuService.selectList(ts);
		map.put("lists", lists);
		return new ModelAndView("admin/guidenceTeacherImportList", map);
	}

	/**
	 * 功能：管理员管理-删除教师
	 * 
	 * */
	@RequestMapping("admin/deleteGuidenceTeacher.do")
	public String teacherDeleteGuidence(TeaStu ts, String id) {
		ts.setState("0");
		teaStuService.update(ts);
		return "redirect:guidenceTeacherImportList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：管理员添加教师 注解请求地址(映射)--添加页面 郑春光20140910
	 * 
	 * */
	@RequestMapping("admin/addGuidenceTeacher.do")
	public String teacherAddGuidence() {
		return "admin/guidenceTeacherImport";
	}

	/**
	 * 功能：提交导入 指导教师分配表/学生表/教师表 /企业表 解析表格 王磊
	 */
	@RequestMapping(value = "admin/doGuidenceTeacherImport.do", method = RequestMethod.POST)
	public String teacherImport(MultipartHttpServletRequest request,
			ModelMap modelMap, HttpSession se) throws Exception {
		// 获取学院id为了验证部门编号和班级编号
		// 获取学院id为了验证部门编号和班级编号
		Teacher tea = (Teacher) se.getAttribute("current_user");
		String college_id = se.getAttribute("college_id").toString();
		// 导入类型

		String type = Common.NulltoBlank(request.getParameter("type"));

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = request;
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());

				if (file != null) {
					String file_type = "";
					if (type.equals("excelGuidenceTeacher")) {
						file_type = "excelGuidenceTeacher";
					} else if (type.equals("excelTeacher")) {
						file_type = "excelTeacher";
					} else if (type.equals("excelStudent")) {
						file_type = "excelStudent";
					} else if (type.equals("excelTrain")) {
						file_type = "excelTrain";
					} else if (type.equals("excelDept")) {
						file_type = "excelDept";
					} else if (type.equals("excelEvalsIndex")) {
						file_type = "excelEvalsIndex";
					} else if (type.equals("excelCourseArrangementImport")) {
						file_type = "excelCourseArrangementImport";
					}

					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					String fileName = file.getOriginalFilename();
					/*
					 * String filePosition = "WEB-INF/uploadedfiles/Import/" +
					 * fileName;
					 */
					String filepos = "Import" + "/" + file_type + "_";
					String filePosition = "Import" + "/" + file_type + "_"
							+ fileName;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					/*
					 * String file_pa = real_path +
					 * "WEB-INF/uploadedfiles/Import/";
					 */
					String file_pa = project_path + real_path + filepos;
					// float filesize = file.getSize();//
					// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件
					// 文件的属性
					/*
					 * String file_id = Common.returnUUID(); Files fil = new
					 * Files(); fil.setFile_size(filesize); Timestamp d = new
					 * Timestamp(System.currentTimeMillis());
					 * fil.setUpload_time(d); fil.setId(file_id);
					 * fil.setFile_type(file_type); fil.setFile_name(fileName);
					 * fil.setPosition(filePosition); filesService.insert(fil);
					 */
					File f = new File(file_pa + fileName);
					// 判断导入数据表类型
					if (type.equals("excelGuidenceTeacher")) {// 分配指导教师
						List<TeaStu> lists = ExcelService.AnalysisTeaStu(f);
						HttpSession session = request.getSession();
						session.setAttribute("lists", lists);
						modelMap.put("lists", lists);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} // 杨梦肖
					else if (type.equals("excelDept")) {
						List<Org> tcList = ExcelService.AnalysisOrg(f);

						// List<Teacher> tcL = ExcelService.AnalysisOrg(f);
						List<String> codeList = new ArrayList();
						HttpSession session = request.getSession();
						String grade = request.getParameter("grade");
						session.setAttribute("grade", grade);
						session.setAttribute("tc", tcList);
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						// int b;
						for (Org org : tcList) {
							if (org.getOrg_code() == null) {
								infor = infor + "组织编码不能为空";
							}
							if (org.getClass_stunum() == null) {
								infor = infor + "班级人数不能为空";
							}if (org.getTime() == null) {
								infor = infor + "班级不能为空";
							}
							if (org.getContacts() == null) {
								infor = infor + "联系人不能为空";
							}
							if (org.getContacts_code() == null) {
								infor = infor + "联系人编码不能为空";
							}
							if (org.getHead_code() == null) {
								infor = infor + "班主任编码不能为空";
							}
							if (org.getCounselor_code() == null) {
								infor = infor + "辅导员编码不能为空";
							}
							if (org.getHead_tea_id() == null) {
								infor = infor + "班主任不能为空";
							}

							if (org.getOrg_level() == null) {
								infor = infor + "联系人所在系别不能为空";
							}
							if (org.getPhone() == null) {
								infor = infor + "电话号码不能为空";
							} else if (org.getPhone().length() == 11
									&& org.getPhone().matches("[0-9]+")) {

							} else {
								infor = infor + "手机号只能为数字（长度为11位）！,";
							}
							if (org.getOrg_name() == null) {
								infor = infor + "组织名称不能为空";
							}
							if (org.getParent_id() == null) {
								infor = infor + "上级组织不能为空";
							}
							if (org.getCounselor_id() == null) {
								infor = infor + "辅导员不能为空";
							}
							if (infor.trim().equals("")) {
								infor = "无";
							}

							org.setTemp1(infor.trim());
							infor = "";
							if (org.getOrg_code() != null) {
								codeList.add(org.getOrg_code());
							}
						}
						String[] co = new String[codeList.size()];
						for (int a = 0; a < codeList.size(); a++) {
							co[a] = codeList.get(a);
							modelMap.put("tt", tcList);
							modelMap.put("grade", grade);
							modelMap.put("type", type);
							modelMap.put("fileName", fileName);
						}

					} else if (type.equals("excelStudent")) {
						HttpSession session = request.getSession();
						String class_id = request.getParameter("class_id");
						session.setAttribute("class_id", class_id);
						List<Student> s = ExcelService.AnalysisStudent(f);
						for (Student sts : s) {
							sts.setClass_id(class_id);
						}
						session.setAttribute("students", s);
						// 对学生表的数据验证编号是否重复
						List<String> codeList = new ArrayList();// 声明集合，存储学生编号，为了验证表格中的学生编号是否重复。
						List<String> idcardList = new ArrayList();// 声明集合，存储学生身份证号，为了验证表格中的学生身份证号是否重复。
						List<String> classidList = new ArrayList();// 声明集合，存储表格中班级编号，为得到每个班级的人数。
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						String information = "";// 声明information，得到各班级个数。
						/*
						 * String email=
						 * "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}"
						 * ;可以以后验证邮箱 if (email.matches(format)){return true;//
						 * 邮箱名合法，返回true }else{ return false;// 邮箱名不合法，返回fals }}
						 */int b;
						int c;
						// int d;
						// int cc;
						// 表格的数据验证
						for (Student student : s) {
							String stuCode = student.getStu_code();
							String id_card = student.getId_card();
							b = this.studentService.selectByStuCode(stuCode);
							c = this.studentService.selectByStuCard(id_card);
							if (student.getStu_code() == null) {
								infor = infor + "学号不能为空,";
							} else if (b != 0) {
								infor = "学号在数据库有重复,";
							} else if (student.getStu_code().substring(0, 4)
									.equals(student.getEntry_year())) {
							} else {
								infor = infor + "入学年份和学号不对应,";
							}
							;
							if (student.getTrue_name() == null) {
								infor = infor + "姓名不能为空";
							}
							if (student.getSex() == null) {
								infor = infor + "性别不能为空,";
							} else if (student.getSex().equals("男")
									|| student.getSex().equals("女")) {

							} else {
								infor = infor + "亲，人只有男女之分奥！,";
							}
							;
							if (student.getId_card() == null) {
								infor = infor + "身份证号不能为空,";
							} else if (student.getId_card().length() != 18) {
								infor = infor + "身份证号应为18位,";
							} else if (student
									.getId_card()
									.substring(
											student.getId_card().length() - 3,
											student.getId_card().length())
									.equals("000")) {
								infor = infor + "此身份证号有误,最后三位不能同时为'0'";
							} else if (c != 0) {
								infor = infor + "身份证号在数据库有重复,";
							}
							;
							if (student.getPhone() == null) {// boolean isNum =
																// str.matches("[0-9]+");
								infor = infor + "手机号不能为空,";
							} else if (student.getPhone().length() == 11
									&& student.getPhone().matches("[0-9]+")) {

							} else {
								infor = infor + "手机号只能为数字（长度为11位）！,";
							}
							if (codeList.size() != 0) {
								infor = infor
										+ isCodeExist(student.getStu_code(),
												codeList, "学生编号");
							}
							;
							if (idcardList.size() != 0) {
								infor = infor
										+ isCodeExist(student.getId_card(),
												idcardList, "学生身份证编号");
							}
							;
							if (infor.trim().equals("")) {
								infor = "无";
							}
							student.setTemp1(infor.trim());
							infor = "";
							classidList.add(class_id);
							if (student.getStu_code() != null) {
								codeList.add(student.getStu_code());
							}
							;
							if (student.getId_card() != null) {
								idcardList.add(student.getId_card());
							}
							;
							// 判断学生编号是否在excel中重复
							String[] co = new String[codeList.size()];
							for (int a = 0; a < codeList.size(); a++) {
								co[a] = codeList.get(a);
							}
							// 判断身份证号是否在excel中重复
							String[] ic = new String[idcardList.size()];
							for (int e = 0; e < idcardList.size(); e++) {
								ic[e] = idcardList.get(e);
							}

						}
						// 声明数组，存储班级id
						String[] ci = new String[classidList.size()];
						for (int g = 0; g < classidList.size(); g++) {
							ci[g] = classidList.get(g);
						}
						information = countArrary(ci);
						modelMap.put("information", information);
						modelMap.put("ss", s);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} else if (type.equals("excelTeacher")) {
						List<Teacher> tcList = ExcelService.AnalysisTeacher(f);
						HttpSession session = request.getSession();
						String xi_id = request.getParameter("xi_id");
						session.setAttribute("xi_id", xi_id);
						for (Teacher t : tcList) {
							t.setDept_id(xi_id);
						}
						session.setAttribute("tc", tcList);
						// 对教师表的数据验证编号是否重复
						List<String> codeList = new ArrayList();// 声明集合，存储教师编号，为了验证表格中的教师编号是否重复。
						List<String> duties = new ArrayList();// 声明集合，储存职能。
						duties.add("教师");
						duties.add("院长");
						duties.add("副院长");
						duties.add("系主任");
						duties.add("系副主任");
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						int b;
						// 表格的数据验证
						for (Teacher teacher : tcList) {
							String teaCode = teacher.getTea_code();
							b = this.teacherService.selectByTeaCode(teaCode);
							if (teacher.getPhone() == null) {
								infor = infor + "手机号不能为空,";
							} else if (teacher.getPhone().length() == 11
									&& teacher.getPhone().matches("[0-9]+")) {

							} else {
								infor = infor + "手机号只能为数字（长度为11位）！,";
							}
							;
							if (teaCode == null) {
								infor = "教工号不能为空,";
							} else {
								if (b != 0) {
									infor = "教工号在数据库有重复,";
								}
								;
							}
							;
							if (teacher.getSex() == null) {
								infor = infor + "性别不能为空,";
							} else if (teacher.getSex().equals("男")
									|| teacher.getSex().equals("女")) {

							} else {
								infor = infor + "亲，人只有男女之分奥！,";
							}
							;
							if (teacher.getCourse_id() != null) {
								if (teacher.getCourse_id().length() > 0) {
									String name[] = teacher.getCourse_id()
											.split(",");
									for (int i = 0; i < name.length; i++) {
										if (DictionaryService
												.findCoursesByCode(name[i]) == null) {
											infor = infor + "此编号" + name[i]
													+ "有误";
										}
									}
									;
								}
							}
							if (teacher.getTrue_name() == null) {
								infor = infor + "用户名不能为空";
							}
							if (codeList.size() != 0) {
								infor = infor
										+ isCodeExist(teaCode, codeList, "教师编号");
							}
							;
							if (teacher.getDuties() == null) {
								infor = infor + "职责不能为空";
							} else {
								if (duties.contains(teacher.getDuties()) == true) {
								} else {
									infor = infor + "没有此职责";
								}
							}
							if (infor.trim().equals("")) {
								infor = "无";
							}
							teacher.setTemp1(infor.trim());
							infor = "";
							if (teacher.getTea_code() != null) {
								codeList.add(teaCode);
							}
							;
						}
						// 判断是否在excel中重复
						String[] co = new String[codeList.size()];
						for (int a = 0; a < codeList.size(); a++) {
							co[a] = codeList.get(a);
						}
						modelMap.put("tt", tcList);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} else if (type.equals("excelTrain")) {
						List<TrainDetail> td = ExcelService
								.AnalysisTrainDetail(f);
						HttpSession session = request.getSession();
						session.setAttribute("td", td);
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						int count = 0;// 声明cuont变量，判断是否实训安排是否与数据库记录重复。
						List<String> checkRight = new ArrayList();// 声明集合，判断表格中的记录是否重复。
						int c;
						List<String> class_Numbers = new ArrayList();// 声明集合，储存职能。
						class_Numbers.add("一二节");
						class_Numbers.add("三四节");
						class_Numbers.add("五六节");
						class_Numbers.add("七八节");
						class_Numbers.add("九十节");
						List<String> termMonth = new ArrayList();// 声明集合，判断学期
						termMonth.add("08");
						termMonth.add("09");
						termMonth.add("10");
						termMonth.add("11");
						termMonth.add("12");
						termMonth.add("01");
						for (TrainDetail trainDetail : td) {
							PracticeTask pt = DictionaryService
									.findPracticeTask(trainDetail.getTask_id());
							Groups g = DictionaryService.findGroups(trainDetail
									.getGroup_id());
							if (pt == null) {
								infor = infor + "无此任务信息,";
							}
							;
							if (g == null) {
								infor = infor + "无此小组信息,";
							}
							;
							if (DictionaryService.findTeacher(trainDetail
									.getTea_id()) == null) {
								infor = infor + "无此老师信息,";
							} else {
								String deptId = DictionaryService.findTeacher(
										trainDetail.getTea_id()).getDept_id();
								c = this.orgService.checkDpetId(deptId,
										college_id);
								if (c == 0) {
									infor = infor + "教师所在部门不在当前学院中,";
								}
							}
							;
							if (DictionaryService.findCourses(trainDetail
									.getCourse_id()) == null) {
								infor = infor + "无此课程信息,";
							}
							;
							if (trainDetail.getTrain_time() == null) {
								infor = infor + "实训时间不能为空,";
							}
							if (trainDetail.getTrain_time().toString().length() >= 9) {
								String month = trainDetail.getTrain_time()
										.toString();
								String nowMonth = month.substring(5, 7);
								if (termMonth.contains(nowMonth) == true) {
									trainDetail.setTrain_term(1);
								} else {
									trainDetail.setTrain_term(2);
								}
							} else {
								infor = infor + "实训时间有问题,";
							}
							if (trainDetail.getWeek_num() == null) {
								infor = infor + "周次不能为空,";
							}
							;
							if (trainDetail.getClass_num() == null) {
								infor = infor + "节次不能为空,";
							} else {
								if (class_Numbers.contains(trainDetail
										.getClass_num()) == true) {
								} else {
									infor = infor + " 节次格式不正确（例如：一二节）";
								}
							}
							if (trainDetail.getTrain_place() == null) {
								infor = infor + "实训地点不能为空,";
							}
							;
							String pj = trainDetail.getTask_id()
									+ trainDetail.getGroup_id()
									+ trainDetail.getCourse_id()
									+ trainDetail.getTea_id()
									+ trainDetail.getTrain_time()
									+ trainDetail.getClass_num();
							if (checkRight.size() != 0) {

								infor = infor
										+ isCodeExist(pj, checkRight,
												"(任务，小组，课程，教师，时间，节次)");
							}
							;
							if (infor.trim().equals("")) {
								infor = "无";
							} else {
								String classNumber = trainDetail.getClass_num();
								String trainPlace = trainDetail
										.getTrain_place();
								int trainTerm = trainDetail.getTrain_term();
								trainDetail.setClass_num(null);
								trainDetail.setTrain_place(null);
								trainDetail.setTrain_term(null);
								count = this.trainDetailService
										.selectCount(trainDetail);
								if (count != 0) {
									infor = infor + "此实训安排重复,    ";
								}
								;
								trainDetail.setClass_num(classNumber);
								trainDetail.setTrain_term(trainTerm);
								trainDetail.setTrain_place(trainPlace);
							}
							trainDetail.setTemp1(infor.trim());
							infor = "";
							checkRight.add(pj);

						}
						modelMap.put("tds", td);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} else if (type.equals("excelCompany")) {
						List<Company> companys = ExcelService
								.AnalysisCompany(f);
						HttpSession session = request.getSession();
						session.setAttribute("companys", companys);
						List<Company> newCompanys = companyService
								.importCompany(companys, college_id);
						modelMap.put("c", newCompanys);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} else if (type.equals("excelEvalsIndex")) {
						List<EvalsIndex> evalsIndexs = ExcelService
								.AnalysisEvalsIndex(f);
						HttpSession session = request.getSession();
						String standard_id = request
								.getParameter("standard_id");
						session.setAttribute("standard_id", standard_id);
						session.setAttribute("evalsIndexs", evalsIndexs);
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						for (EvalsIndex ei : evalsIndexs) {
							String standId = (String) session
									.getAttribute("standard_id");
							String standName = DictionaryService
									.findEvaluateStandard(standId)
									.getStandard_name();
							if (ei.getIndex_code() == null) {
								infor = infor + "指标编码不能为空";
							}
							;
							if (ei.getIndex_name() == null) {
								infor = infor + "指标名称不能为空";
							}
							;
							if (ei.getDescription() == null) {
								infor = infor + "指标描述不能为空";
							}
							;
							if (ei.getScore() == null) {
								infor = infor + "分数不能为空";
							}
							;
							if (ei.getTemp2() == null) {
								infor = infor + "标准不能为空";
							}
							;
							if (!ei.getTemp2().equals(standName)) {
								infor = infor + "标准名称不一致";
							}
							;
							if (infor.trim().equals("")) {
								infor = "无";
							}
							ei.setTemp1(infor);
							infor = "";
						}
						modelMap.put("eis", evalsIndexs);
						modelMap.put("type", type);
						modelMap.put("fileName", fileName);
					} else if (type.equals("excelGroupMemberStuId")) {
						List<GroupMembers> groupMembers = ExcelService
								.AnalysisGroupMembers(f);
						HttpSession session = request.getSession();
						session.setAttribute("groupMembers", groupMembers);
						String groupId = (String) session
								.getAttribute("groupId");
						String infor = "";// 声明变量，存储表格中未按要求的信息存储。
						int count;// 声明count判断该学生是否已经有实习任务。
						List<String> codeList = new ArrayList();// 声明集合，存储学生编号，为了验证表格中的学生编号是否重复。
						for (GroupMembers gms : groupMembers) {
							String groupName = DictionaryService
									.findGroups(groupId).getGroup_name().trim();// 防止因为小组名称首尾有空格，致使导入数据不成功。
							if (gms.getTemp2() == null) {
								infor = infor + "小组名称不能为空，";
							}
							;
							if (gms.getUser_id() == null) {
								infor = infor + "学号不能为空，";
							} else {
								if (DictionaryService.findStudent(gms
										.getUser_id()) == null) {
									infor = infor + gms.getUser_id() + ",";
								} else {
									count = this.practiceTaskService
											.getCount(gms.getUser_id());
									if (count >= 1) {
										infor = infor + "此学生已有实习任务，";
									}
								}
							}
							;
							if (codeList.size() != 0) {
								infor = infor
										+ isCodeExist(gms.getUser_id(),
												codeList, "学号");
							}
							;
							if (!groupName.equals(gms.getTemp2())) {
								infor = infor + "表格中小组名称与导入实习小组名称不一致，";
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
					} else if (type.equals("excelCourseArrangementImport")) {
						List<Teacher> teaList = ExcelService
								.analysisTeaCourses(f);

						String year = (String) se
								.getAttribute("importTeaCourseYear");

						String semester = (String) se
								.getAttribute("importTeaCourseSemester");
						if (teaList != null) {
							// 提示信息
							String promptInfo = "";
							String courseId = null;
							String teaId = null;
							TeaCourses teaCourses = null;
							for (Teacher teacher : teaList) {
								// 避免出现null的问题
								if (teacher.getTemp1() == null)
									teacher.setTemp1("");
								promptInfo = teacher.getTemp1();
								teaCourses = new TeaCourses();
								teaCourses.setYear(year);
								teaCourses.setSemester(semester);
								// 验证老师是否具有已经分配了课程
								if (DictionaryService.findCoursesByCode(teacher
										.getTemp2()) != null) {
									courseId = DictionaryService
											.findCoursesByCode(
													teacher.getTemp2()).getId();
									teaCourses.setCourses_id(courseId);
								}
								if (DictionaryService.findTeacherByCode(teacher
										.getTea_code()) != null) {
									teaId = DictionaryService
											.findTeacherByCode(
													teacher.getTea_code())
											.getId();
									teaCourses.setTea_id(teaId);

									if (teaCoursesService
											.selectList(teaCourses).size() > 0) {
										promptInfo = promptInfo + ","
												+ teacher.getTrue_name()
												+ "老师在" + year + "第" + semester
												+ "学期已经安排了该课程";
										teacher.setTemp1(promptInfo);
									}
								}
								// 若都没有问题 则提示无
								if (teacher.getTemp1() == "")
									teacher.setTemp1("无");

							}
							se.setAttribute("teaCourseList", teaList);
							modelMap.put("teaList", teaList);
						}

					}
				}
			}
		}
		if (type.equals("excelGuidenceTeacher")) {
			ret = "admin/guidenceTeacherImport";
		} else if (type.equals("excelStudent")) {
			ret = "admin/studentImport";
		} else if (type.equals("excelTeacher")) {
			ret = "admin/teacherImport";
		} else if (type.equals("excelTrain")) {
			ret = "admin/trainDetailImport";
		} else if (type.equals("excelCompany")) {
			ret = "admin/companyImport";
		} else if (type.equals("excelEvalsIndex")) {
			ret = "admin/evalsIndexImport";
		} else if (type.equals("excelGroupMemberStuId")) {
			ret = "admin/groupMemberStuIdImport";
		} else if (type.equals("excelDept")) {
			ret = "admin/deptImport";
		} else if (type.equals("excelCourseArrangementImport")) {
			ret = "admin/courseArrangementImport";
		}
		return ret;
	}

	/**
	 * 功能：组织-导入班级 杨梦肖2016-3-23
	 * 2016/4/8  添加班级
	 * */
	@RequestMapping("admin/addDeptImport.do")
	public ModelAndView teacherAddImport(String dept_id, String grade) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("grade", grade);
		return new ModelAndView("admin/deptImport", map);
	}

	/**
	 * 
	 * 功能：得到excel表格中每个班级的人数信息 by王磊 20150120
	 * 
	 * @param excel中班级id数组
	 */
	public String countArrary(String a[]) {
		String classInformation = "";
		Map<String, Integer> m = new TreeMap<String, Integer>();
		for (int i = 0; i < a.length; i++) {
			Integer c = m.get(a[i]);
			if (c == null) {
				m.put(a[i], new Integer(1));
			} else {
				c = new Integer(c.intValue() + 1);
				m.put(a[i], new Integer(c));
			}
		}
		Set<String> s = m.keySet();
		for (String e : s) {
			Org o = DictionaryService.findOrg(e);
			String name = "错误班级(id:e)";
			if (o != null) {
				classInformation = classInformation + o.getOrg_name()
						+ "， 导入班级人数为：" + m.get(e) + " ";// .getOrg_name()
			} else {
				classInformation = classInformation + name + "， 导入班级人数为："
						+ m.get(e) + " ";
			}
		}
		return classInformation;
	}

	/*
	 * 功能：检验编号是否在表格中是否重复 by王磊 2014/1/6
	 */
	public static String isCodeExist(String currentCode, List d, String souce) {
		int count = 0;
		String temp = "";
		for (int i = 0; i < d.size(); i++) {
			temp = (String) d.get(i);
			if (temp.equals(currentCode)) {
				count++;
			}
		}
		if (count > 0) {
			return "表格中" + souce + "列不能重复。";
		} else {
			return "";
		}
	}

	/**
	 * 功能：管理员上传实训安排列表 注解请求地址(映射) 吴敬国20141028 修改：王磊 筛选和条件获得2015年1月29日
	 * */
	@RequestMapping("admin/trainDetailList.do")
	public ModelAndView trainDetailList(HttpSession session, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String year1 = (String) session.getAttribute("year1");
		String term1 = (String) session.getAttribute("term1");
		String dept_id1 = (String) session.getAttribute("dept_id1");
		String tea_id1 = (String) session.getAttribute("tea_id1");
		List<String> finalYears = new ArrayList<String>();
		if (type != null) {
			session.removeAttribute("term1");
			session.removeAttribute("year1");
			session.removeAttribute("dept_id1");
			session.removeAttribute("tea_id1");
			tea_id1 = null;
			dept_id1 = null;
			term1 = null;
			year1 = null;
		}
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> o = this.orgService.selectByXueYuanId(college_id);// 查出本学院的所有系
		// List<TrainDetail> lists =
		// this.trainDetailService.selectListByDeptid(college_id);
		List<String> years = this.trainDetailService.getYears();
		if (years.size() == 0 || years == null) {

		} else {
			int year = Integer.parseInt(years.get(years.size() - 1));// 加后一年
			int y = Integer.parseInt(years.get(0));// 加前一年
			years.add(Integer.toString((year + 1)));
			years.add(0, Integer.toString((y - 1)));
			for (int i = 0; i < (years.size() - 1); i++) {
				String updateYear = years.get(i) + "-" + years.get(i + 1);
				finalYears.add(updateYear);
			}
		}
		map.put("orgs", o);
		map.put("dept_id1", dept_id1);
		map.put("tea_id1", tea_id1);
		map.put("year1", year1);
		map.put("term1", term1);
		map.put("finalYears", finalYears);
		return new ModelAndView("admin/trainDetailList", map);
	}

	/**
	 * 功能：通过系部id得到系部老师 by王磊2015年1月28日
	 * 
	 * */
	@RequestMapping("admin/getTeacherByDeptId.do")
	public String getTeacherByDeptId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String dept_id = request.getParameter("dept_id");
		session.setAttribute("dept_id1", dept_id);
		List<Teacher> t = this.teacherService.getTeachersByDeptId(dept_id);
		Teacher teacher = new Teacher();
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='tea_id' name='tea_id' style='width:100px'> onChange=getPracticeTask()");
		String tea_id1 = (String) session.getAttribute("tea_id1");
		boolean b = false;
		if (DictionaryService.findTeacher(tea_id1) != null) {
			b = dept_id.equals(DictionaryService.findTeacher(tea_id1)
					.getDept_id());
		}
		if (b == false || t.size() == 0) {
			sb.append("<option value='0'>请选择老师</option>");
			for (Teacher teachers : t) {
				sb.append("<option " + "value=" + teachers.getId() + ">"
						+ teachers.getTrue_name() + "</option>");
			}
		} else {
			teacher = DictionaryService.findTeacher(tea_id1);
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i).getId().equals(tea_id1)) {
					t.add(0, teacher);
					t.remove(i + 1);
				}
			}
			for (Teacher teachers : t) {
				sb.append("<option value=" + teachers.getId() + ">"
						+ teachers.getTrue_name() + "</option>");
			}
		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 功能：通过老师id和入学年份查出老师所带的实践教学任务 by王磊 2015年1月28日
	 */
	/*
	 * @RequestMapping("admin/getPracticeTaskByYearAndTeacher.do") public String
	 * getPracticeTaskByYearAndTeacher(HttpServletRequest request,
	 * HttpServletResponse response,HttpSession session) {
	 * response.setCharacterEncoding("utf-8"); String tj =
	 * request.getParameter("tiaojian"); int a = tj.indexOf(","); String grade =
	 * tj.substring(0, a); String tea_id = tj.substring(a + 1, tj.length());
	 * PracticeTask pt = new PracticeTask(); pt.setGrade(grade);
	 * pt.setTea_id(tea_id); pt.setTask_type("2");
	 * session.setAttribute("tea_id1", tea_id); session.setAttribute("grade1",
	 * grade); List<PracticeTask> ptts =
	 * this.practiceTaskService.selectList(pt); PracticeTask practiceTask =new
	 * PracticeTask(); StringBuffer sb = new StringBuffer(); sb.append(
	 * "<select id='practiceTask_id' name='practiceTask_id' style='width:100px' onChange=getPracticeTask()>"
	 * ); String practiceTask_id1=(String)
	 * session.getAttribute("practiceTask_id1"); int
	 * counts=this.practiceTaskService.getCounts(practiceTask_id1, tea_id);
	 * boolean b=false; if(DictionaryService.findPracticeTask(practiceTask_id1)
	 * != null && counts !=0){ b=true; } if( b==false || ptts.size() ==0){
	 * sb.append("<option value='0'>请选择实践任务</option>"); for (PracticeTask pts :
	 * ptts) { sb.append("<option " + "value=" + pts.getId() + ">" +
	 * pts.getTask_name() + "</option>"); } }else{ practiceTask =
	 * DictionaryService.findPracticeTask(practiceTask_id1); for (int
	 * i=0;i<ptts.size();i++) {
	 * if(ptts.get(i).getId().equals(practiceTask_id1)){
	 * ptts.add(0,practiceTask); ptts.remove(i+1); } } for (PracticeTask pts :
	 * ptts) { sb.append("<option " + "value=" + pts.getId() + ">" +
	 * pts.getTask_name() + "</option>"); } } sb.append("</select>"); try {
	 * response.getWriter().println(sb.toString()); } catch (IOException e) {
	 * e.printStackTrace(); } return null; }
	 */

	/**
	 * 功能：通过实践教学任务id得到本任务的课表 by王磊 2015年1月29日
	 * 
	 * */
	@RequestMapping("admin/getTrainDetailByTaskId.do")
	public String trainDetailGetByTaskId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		List<TrainDetail> tds = null;
		String tiaojian = request.getParameter("tiaojian");
		String tj[] = tiaojian.split(",");
		String year = tj[0];
		String dept_id = tj[1];
		String tea_id = tj[2];
		String term = tj[3];
		session.setAttribute("year1", year);
		session.setAttribute("dept_id1", dept_id);
		session.setAttribute("term1", term);
		// 得到某学年某学期对应的起始和截止时间
		String[] years = year.split("-");
		String begin_time = "";
		String end_time = "";
		if (term.equals("1")) {
			begin_time = years[0] + Constants.TERM1_BEGIN;
			Integer i = Integer.parseInt(years[0]);
			Integer j = i + 1;
			end_time = j + Constants.TERM1_END;
		} else if (term.equals("2")) {
			begin_time = years[1] + Constants.TERM2_BEGIN;
			end_time = years[1] + Constants.TERM2_END;
		}
		if (!tea_id.equals("0")) {
			Map<String, String> mapYearAndTeaIdAndTerm = new HashMap<String, String>();
			mapYearAndTeaIdAndTerm.put("tea_id", tea_id);
			mapYearAndTeaIdAndTerm.put("begin_time", begin_time);
			mapYearAndTeaIdAndTerm.put("end_time", end_time);
			mapYearAndTeaIdAndTerm.put("term", term);
			session.setAttribute("tea_id1", tea_id);
			tds = this.trainDetailService
					.getTrainsByTermAndYearAndTeaId(mapYearAndTeaIdAndTerm);
		} else {
			Map<String, String> mapYearAndOrgIdAndTerm = new HashMap<String, String>();
			mapYearAndOrgIdAndTerm.put("dept_id", dept_id);
			mapYearAndOrgIdAndTerm.put("begin_time", begin_time);
			mapYearAndOrgIdAndTerm.put("end_time", end_time);
			mapYearAndOrgIdAndTerm.put("term", term);
			tds = this.trainDetailService
					.getTrainsByTermAndYearAndDeptId(mapYearAndOrgIdAndTerm);
		}
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='1200'>");
		sb.append("<tr> <th width='250' align='center'>任务名称</th><th width='200' align='center'>小组名称</th><th width='100' align='center'>课程名称</th><th width='60' align='center'>教师</th><th width='120' align='center'>实训时间</th><th width='150' align='center'>实训地点</th><th width='50' align='center'>周次</th><th width='100' align='center'>节次</th><th width='50' align='center'>修改</th><th width='50' align='center'>操作</th></tr>");
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
			sb.append("<td align='center'>"
					+ DictionaryService.findTeacher(trainDetail.getTea_id())
							.getTrue_name() + "</td>");
			sb.append("<td align='center'>" + createTime + "</td>");
			sb.append("<td align='center'>" + trainDetail.getTrain_place()
					+ "</td>");
			sb.append("<td align='center'>" + trainDetail.getWeek_num()
					+ "</td>");
			sb.append("<td align='center'>" + trainDetail.getClass_num()
					+ "</td>");
			sb.append("<td align='center'><input type='button' value='修改' onclick=doEdit('"
					+ trainDetail.getId() + "')></td>");
			sb.append("<td align='center'><input type='button' value='删除' onclick=doDel('"
					+ trainDetail.getId() + "')></td>");
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
	 * 功能：管理员转到实训安排添加界面 注解请求地址(映射) 吴敬国20141028
	 * 
	 * */
	@RequestMapping("admin/trainDetailImport.do")
	public String trainDetailImport() {
		return "admin/trainDetailImport";
	}

	/**
	 * 功能：管理员删除实训安排 注解请求地址(映射) 吴敬国20141028
	 * 
	 * */
	@RequestMapping("admin/deleteTrainDetail.do")
	public String trainDetailDelete(String id) {
		trainDetailService.delete(id);
		return "redirect:trainDetailList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：转到修改实训安排页面 注解请求地址(映射) 吴敬国 20141009
	 * 
	 * */
	@RequestMapping("admin/editTrainDetail.do")
	public ModelAndView trainDetailEdit(ModelMap modelMap, String id) {
		TrainDetail td = (TrainDetail) trainDetailService.selectByID(id);
		String class_num = td.getClass_num();
		List<String> class_nums = new ArrayList<String>();
		class_nums.add(0, "一二节");
		class_nums.add(1, "三四节");
		class_nums.add(2, "五六节");
		class_nums.add(3, "七八节");
		class_nums.add(4, "九十节");
		if (class_num.equals("一二节")) {
			class_nums.add(0, "一二节");
			class_nums.remove(1);
		} else if (class_num.equals("三四节")) {
			class_nums.add(0, "三四节");
			class_nums.remove(2);

		} else if (class_num.equals("五六节")) {
			class_nums.add(0, "五六节");
			class_nums.remove(3);

		} else if (class_num.equals("七八节")) {
			class_nums.add(0, "七八节");
			class_nums.remove(4);

		} else if (class_num.equals("九十节")) {
			class_nums.add(0, "九十节");
			class_nums.remove(5);

		}
		modelMap.put("td", td);
		modelMap.put("class_nums", class_nums);
		return new ModelAndView("admin/trainDetailEdit", modelMap);

	}

	/**
	 * 功能：管理员更新实训安排 注解请求地址(映射) 吴敬国20141028 修改：王磊 日期：2015-6-30
	 * */
	@RequestMapping("admin/doEditTrainDetail.do")
	public String trainDetailDoEdit(HttpServletRequest request, String id)
			throws IllegalStateException, IOException {
		String train_place = request.getParameter("train_place");
		String week_num = request.getParameter("week_num");
		String class_num = request.getParameter("class_num");
		int weekNumber = Integer.parseInt(week_num);
		String newTime = request.getParameter("new_time");
		TrainDetail trainDetail = (TrainDetail) this.trainDetailService
				.selectByID(id);
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp tst = new Timestamp(System.currentTimeMillis());
		try {
			tst = new Timestamp(format1.parse(newTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		trainDetail.setTrain_place(train_place);
		trainDetail.setWeek_num(weekNumber);
		trainDetail.setTrain_time(tst);
		trainDetail.setClass_num(class_num);
		trainDetailService.update(trainDetail);
		return "redirect:trainDetailList.do"; // 修改成功后重定向到列表页面
	}

	/**
	 * 功能：导入实训安排列表 by 王磊 2015-5-18
	 * */
	@RequestMapping("admin/trainImportSuccess.do")
	public ModelAndView trainImportSuccess(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TrainDetail> trainImportSuccess = (List<TrainDetail>) session
				.getAttribute("td");// 导入时存的session
		map.put("trainImportSuccess", trainImportSuccess);
		return new ModelAndView("admin/trainImportSuccess", map);
	}

	/**
	 * 功能：学生管理 王磊
	 * 
	 */
	@RequestMapping("admin/studentImportList.do")
	public ModelAndView studentList(HttpSession session, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String student_grade = (String) session.getAttribute("student_grade");
		String student_dept_id = (String) session
				.getAttribute("student_dept_id");
		String student_class_id = (String) session
				.getAttribute("student_class_id");
		if (type != null) {
			session.setAttribute("student_grade", null);
			session.setAttribute("student_dept_id", null);
			session.setAttribute("student_class_id", null);
			student_grade = null;
			student_dept_id = null;
			student_class_id = null;
		}
		List<String> Grade = this.orgService.getYears();
		// 获取管理的学院
		String college_id = session.getAttribute("college_id").toString();
		// 查出本学院的所有系
		List<Org> o = this.orgService.selectByXyId(college_id);
		map.put("o", o);
		map.put("grade", Grade);
		map.put("student_grade", student_grade);
		map.put("student_dept_id", student_dept_id);
		map.put("student_class_id", student_class_id);
		return new ModelAndView("admin/studentImportList", map);
	}

	/**
	 * 功能：学生管理-导入学生 王磊
	 * 
	 * */
	@RequestMapping("admin/studentImport.do")
	public ModelAndView studentImport(String class_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", class_id);
		return new ModelAndView("admin/studentImport", map);
	}

	/**
	 * 功能：导入学生成功列表 by 王磊 2015-5-18
	 * */
	@RequestMapping("admin/studentImportSuccess.do")
	public ModelAndView studentImportSuccess(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> studentImportList = (List<Student>) session
				.getAttribute("students");// 导入时存的session
		map.put("studentImportList", studentImportList);
		return new ModelAndView("admin/studentImportSuccess", map);// return new
	}

	/**
	 * 功能：学生管理-删除学生
	 * */
	@RequestMapping("admin/deleteStudentImport.do")
	public String studentDelete(String id) {
		Student s = DictionaryService.findStudent(id);
		s.setState("0");
		studentService.update(s);
		return "redirect:studentImportList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：导入教师 注解请求地址(映射) by ccc 20141028 修改：进行筛选，获得该院老师和该院所有的系 by王磊 20150118
	 * 
	 * @return
	 */

	@RequestMapping("admin/teacherImportList.do")
	public ModelAndView teacherImportList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> o = this.orgService.selectByXyId(college_id);
		List<Teacher> tc = this.teacherService.selectListByXuId(college_id);
		map.put("tc", tc);
		map.put("xi", o);
		return new ModelAndView("admin/teacherImportList", map);
	}

	/**
	 * 功能：查出系部所有老师 by:王磊 20150118
	 * 
	 * */
	@RequestMapping("admin/typeList.do")
	public String typeList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String Dept_id = request.getParameter("xId");
		List<Teacher> t = this.teacherService.getTeachersByDeptId(Dept_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='950'>");
		sb.append("<tr> <th width='150' align='center'>教师编号</th> <th width='150' align='center'>真实名字</th><th width='150' align='center'>性别</th><th width='150' align='center'>手机号</th><th width='150' align='center'>部门名称</th><th width='150' align='center'>职责</th><th width='150' align='center'>技能</th><th width='100' align='center'>状态</th><th width='50' align='center'>操作</th> </tr>");
		for (Teacher ts : t) {
			String stateName = "有效";
			if (ts.getState().equals("0"))
				stateName = "无效";
			sb.append("<tr><td align='center'>" + ts.getTea_code() + "</td>"
					+ "<td align='center'>" + ts.getTrue_name() + "</td>"
					+ "<td align='center'>" + ts.getSex() + "</td>"
					+ "<td align='center'>" + ts.getPhone() + "</td>"
					+ "<td align='center'>"
					+ DictionaryService.findOrg(ts.getDept_id()).getOrg_name()
					+ "</td>" + "<td align='center'>" + ts.getDuties()
					+ "</td>" + "<td align='center'>" + ts.getExpertise()
					+ "</td>" + "<td align='center'>" + stateName + "</td>"
					+ "<td align='center'>"
					+ "<a href='deleteTeacherImport.do?id=" + ts.getId() + "'>"
					+ " 删 除 " + "</a>" + "</td></tr>");
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
	 * 功能：查出系部所对应的班级信息 by:王磊 20150118
	 * 
	 * */
	@RequestMapping("admin/classList.do")
	public String classList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("utf-8");
		String tj = request.getParameter("tiaojian");
		int a = tj.indexOf(",");
		String student_grade = tj.substring(0, a);
		String student_dept_id = tj.substring(a + 1, tj.length());
		session.setAttribute("student_grade", student_grade);
		session.setAttribute("student_dept_id", student_dept_id);
		String student_class_id = (String) session
				.getAttribute("student_class_id");
		boolean b = false;
		Org org = new Org();
		if (DictionaryService.findOrg(student_class_id) != null) {
			b = student_class_id.equals(DictionaryService.findOrg(
					student_class_id).getId());
		}
		List<Org> o = this.orgService.selectBygradeAndXiId(student_dept_id,
				student_grade);// 通过系部查出班级
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='classId' name='classId' style='width:150px' onChange='changeSai()'>");
		if (b == false || o.size() == 0) {
			sb.append("<option>请选择班级 </option>");
			for (Org c : o) {
				sb.append("<option " + "value=" + c.getId() + ">"
						+ c.getOrg_name() + "</option>");
			}
		} else {
			org = DictionaryService.findOrg(student_class_id);
			for (int i = 0; i < o.size(); i++) {
				if (o.get(i).getId().equals(student_class_id)) {
					o.add(0, org);
					o.remove(i + 1);
				}
			}
			for (Org c : o) {
				sb.append("<option " + "value=" + c.getId() + ">"
						+ c.getOrg_name() + "</option>");
			}
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
	 * 功能：根据班级编号查出给班级的学生ajax wjg2015-12-29 修改：周睿 显示身份证号和空间主页
	 * */
	@RequestMapping("admin/studentList.do")
	public String studentListAjax(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String student_class_id = request.getParameter("ClassId");
		session.setAttribute("student_class_id", student_class_id);
		List<Student> s = this.studentService
				.getStudentsByClassId(student_class_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='1300'>");
		sb.append("<tr> <th width='60'>序号</th><th width='150'>学号</th> <th width='100'>姓名</th><th width='60'>性别</th><th width='100'>身份证号</th><th width='150'>手机号</th><th width='170'>班级名称</th><th width='100'>家庭电话</th><th width='300'>籍贯</th><th width='300'>空间主页</th><th width='50' align='center'>状态</th><th width='80'>操作</th> </tr>");
		for (int i = 0; i < s.size(); i++) {
			String stateName = "有效";
			if (s.get(i).getState().equals("0"))
				stateName = "无效";
			sb.append("<tr><td align='center'>"
					+ (i + 1)
					+ "</td>"
					+ "<td align='center'>"
					+ s.get(i).getStu_code()
					+ "</td>"
					+ "<td align='center'>"
					+ s.get(i).getTrue_name()
					+ "</td>"
					+ "<td align='center'>"
					+ s.get(i).getSex()
					+ "</td>"
					+ "<td align='center'>"
					+ s.get(i).getId_card()
					+ "</td>"
					+ "<td align='center'>"
					+ s.get(i).getPhone()
					+ "</td>"
					+ "<td align='center'>"
					+ DictionaryService.findOrg(s.get(i).getClass_id())
							.getOrg_name() + "</td>" + "<td align='center'>"
					+ s.get(i).getHome_phone() + "</td>"
					+ "<td align='center'>" + s.get(i).getBirthplace()
					+ "</td>" + "<td align='center'>" + s.get(i).getHomepage()
					+ "</td>" + "<td align='center'>" + stateName + "</td>"
					+ "<td align='center'>"
					+ "<a href='deleteStudentImport.do?id=" + s.get(i).getId()
					+ "'>" + " 删 除 " + "</a>" + "</td></tr>");
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
	 * 功能：根据学号姓名查找学生,李秋林，2016-1-16 修改：周睿 显示身份证号和空间主页
	 */

	@RequestMapping("admin/searchPerson.do")
	public String searchPerson(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String keyword = request.getParameter("keyword");
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Student> stus = new ArrayList();
		stus = studentService.getStudentByNameOrCode(keyword, college_id);
		StringBuffer sb = new StringBuffer();
		int row = 1;
		sb.append("<table border='1' width='1300'>");
		sb.append("<tr> <th width='60'>序号</th><th width='150'>学号</th> <th width='100'>姓名</th><th width='60'>性别</th><th width='100'>身份证号</th><th width='150'>手机号</th><th width='170'>班级名称</th><th width='100'>家庭电话</th><th width='300'>籍贯</th><th width='300'>空间主页</th><th width='50' align='center'>状态</th><th width='80'>操作</th> </tr>");
		if (stus.size() < 20) {
			for (Student s : stus) {
				String stateName = "有效";
				if (s.getState().equals("0"))
					stateName = "无效";
				sb.append("<tr><td align='center'>"
						+ (row++)
						+ "</td>"
						+ "<td align='center'>"
						+ s.getStu_code()
						+ "</td>"
						+ "<td align='center'>"
						+ s.getTrue_name()
						+ "</td>"
						+ "<td align='center'>"
						+ s.getSex()
						+ "</td>"
						+ "<td align='center'>"
						+ s.getId_card()
						+ "</td>"
						+ "<td align='center'>"
						+ s.getPhone()
						+ "</td>"
						+ "<td align='center'>"
						+ DictionaryService.findOrg(s.getClass_id())
								.getOrg_name() + "</td>"
						+ "<td align='center'>" + s.getHome_phone() + "</td>"
						+ "<td align='center'>" + s.getBirthplace() + "</td>"
						+ "<td align='center'>" + s.getHomepage() + "</td>"
						+ "<td align='center'>" + stateName + "</td>"
						+ "<td align='center'>"
						+ "<a href='deleteStudentImport.do?id=" + s.getId()
						+ "'>" + " 删 除 " + "</a>" + "</td></tr>");

				sb.append("</table>");
			}
		} else {
			sb.append("<div  style='color:red'>信息超过20条，请输入详细信息</div>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：教师管理-导入教师 王磊，敬国2015-12-29
	 * 
	 * */
	@RequestMapping("admin/addTeacherImport.do")
	public ModelAndView teacherAddImport(String xi_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xi_id", xi_id);
		return new ModelAndView("admin/teacherImport", map);
	}

	/**
	 * 功能：导入excel表格，保存到数据库 教师、学生、小组成员、企业 王磊，敬国2015-12-29 功能：导入excel表格，保存到数据库 班级
     * 杨梦肖 2016 3 24
	 * 2016/4/9  导入判断问题，判断数据库里面是否有对应的教师编码 改变时间
	 */
	@RequestMapping("admin/doSaveTeachers.do")
	public String teachersImportsave(MultipartHttpServletRequest request, ModelMap modelMap, String fileName,
			String type) {
		String project_path = request.getSession().getServletContext().getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String filepos = "Import" + "/" + type + "_";
		// 所要保存至数据库的excel的名称
		/*
		 * String file_pa = real_path + "WEB-INF/uploadedfiles/Import/" +
		 * fileName;
		 */
		String file_pa = project_path + real_path + filepos + fileName;
		File f = new File(file_pa);
		Teacher t = new Teacher();
		HttpSession session = request.getSession();
		List<Org> or = (List<Org>) session.getAttribute("tc");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		List<TrainDetail> td = (List<TrainDetail>) session.getAttribute("td");
		// 表格里的教师集合
		List<Teacher> tc = (List<Teacher>) session.getAttribute("tc");
		List<TeaStu> lists = (List<TeaStu>) session.getAttribute("lists");
		List<Student> s = (List<Student>) session.getAttribute("students");
		List<Company> c = (List<Company>) session.getAttribute("companys");
		List<EvalsIndex> ei = (List<EvalsIndex>) session.getAttribute("evalsIndexs");
		List<GroupMembers> gms = (List<GroupMembers>) session.getAttribute("groupMembers");
		String groupId = (String) session.getAttribute("groupId");
		String standard_id = (String) session.getAttribute("standard_id");
		String class_id = (String) session.getAttribute("class_id");
		
		if (type.equals("excelTeacher")) {
			UserRole ur = new UserRole();
			for (Teacher teacher : tc) {
				String teaId;
				teaId = Common.returnUUID16();
				userRoleService.saveBasicUserRole(teaId, null, "ROLE_TEACHER", null,null,null);
				if (teacher.getCourse_id() != null) {
					if (teacher.getCourse_id().length() > 0) {
						String code[] = teacher.getCourse_id().split(",");
						String coures_id = "";
						for (int i = 0; i < code.length; i++) {
							coures_id = coures_id + DictionaryService.findCoursesByCode(code[i]).getId() + ",";
						}
						teacher.setCourse_id(coures_id);
					}
				}
				teacher.setId(teaId);
				teacher.setState("1");
				teacher.setTemp1(null);
				teacher.setTea_type("1");
				teacher.setLogin_name(teacher.getTea_code());
				teacher.setLogin_pass(teacher.getTea_code());
				teacherService.insert(teacher);
				teacherService.selectList(t);
				modelMap.put("tc", tc);
			}
			ret = "redirect:teacherImportSuccess.do";
		}
		// 杨梦肖
		else if (type.equals("excelDept")) {
			
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			for (Org org : or) {
				String contacts = org.getContacts();
				String  head_code=org.getHead_code();
				String ti=org.getTime();
				String  head_id=((Teacher)teacherService.selectByCode(head_code)).getId();
				String  cou_code=org.getCounselor_code();
				String  cou_id=((Teacher)teacherService.selectByCode(cou_code)).getId();
				String  con_code=org.getContacts_code();
				String  con_id=((Teacher)teacherService.selectByCode(con_code)).getId();
				if (contacts == null) {
					contacts = "无";
				}
				Timestamp ts1 = Timestamp.valueOf(ti+"-09-01 0:0:0");
                org.setBegin_time(ts1);
				org.setId(Common.returnUUID());
				org.setHead_tea_id(head_id);
				org.setContacts(con_id);
				org.setCounselor_id(cou_id);
				org.setBegin_time(ts);
				org.setDirector("");
				org.setVice_director("");
				org.setState("1");
				org.setOrg_level("5");//班级对应的上级组织为5
				orgService.insert(org);
				modelMap.put("tc", or);// 保存到数据库
			}
			ret = "redirect:deptImportSuccess.do";
		} else if (type.equals("excelStudent")) {
			Student ss = new Student();
			String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
			Role schoolStuRole = roleService.getRoleByColegeIdAndRoleTemplateId(college_id,Constants.ROLE_STUDENT_SCHOOL);
			for (Student student : s) {
				String stuId = Common.returnUUID();
				userRoleService.saveBasicUserRole(stuId, null, null, "ROLE_STUDENT",null,null);
				if(schoolStuRole.getId()!=null){
					userRoleService.saveUserRole(stuId, schoolStuRole.getId());
				}
				student.setId(stuId);
				String year = student.getStu_code().substring(0, 4);
				String Current_notice_read = year + "-09-01 00:00:00";
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				String tsStr = Current_notice_read;
				ts = Timestamp.valueOf(tsStr);
				student.setCurrent_notice_read(ts);
				student.setCurrent_recruit_read(ts);
				student.setGroup_id("无");//
				student.setPractice_state("000");// 初始状态 add by zcg 2015-3-14
				student.setLogin_pass(student.getId_card().substring(student.getId_card().length() - 6,
						student.getId_card().length()));
				student.setTemp1(null);
				student.setClass_id(class_id);
				studentService.insert(student);
				modelMap.put("s", s);
			}
			studentService.selectList(ss);
			ret = "redirect:studentImportSuccess.do";// 跳到导入学生成功界面

		} else if (type.equals("excelGuidenceTeacher")) {
			TeaStu tst = new TeaStu();
			for (TeaStu teaStu : lists) {
				teaStu.setId(Common.returnUUID());
				teaStu.setState("1");
				teaStuService.insert(teaStu);
				teaStuService.selectList(tst);
				modelMap.put("lists", lists);
			}
			ret = "redirect:guidenceTeacherImportList.do";

		} else if (type.equals("excelTrain")) {
			TrainDetail trd = new TrainDetail();
			for (TrainDetail trainDetail : td) {
				trainDetail.setId(Common.returnUUID());
				trainDetail.setTemp1(null);
				trainDetailService.insert(trainDetail);
				trainDetailService.selectList(trd);
				modelMap.put("td", td);
			}
			ret = "redirect:trainImportSuccess.do";

		} else if (type.equals("excelCompany")) {
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
			for (Company company : c) {
				String[] coll_id = company.getApplicable_scope().split(",");
				String dept_id = "";
				for (int i = 0; i < coll_id.length; i++) {
					dept_id = dept_id + DictionaryService.findOrgName(coll_id[i]).getId() + ",";
				}
				company.setApplicable_scope(dept_id);
				company.setCheck_state("1");
				company.setCreate_time(time);
				company.setCheck_man(tea_id);
				company.setCheck_note("管理员添加");
				company.setTemp1(null);
				companyService.insert(company);

			}
			ret = "redirect:companyImportSuccess.do";
		} else if (type.equals("excelEvalsIndex")) {
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
			for (EvalsIndex evalsIndex : ei) {
				evalsIndex.setStandard_id(standard_id);
				evalsIndex.setCreate_time(time);
				evalsIndex.setId(Common.returnUUID());
				evalsIndex.setTemp2(null);
				evalsIndex.setTemp1(null);
				evalsIndexService.insert(evalsIndex);
			}
			ret = "redirect:evaluateStandardList.do";
		} else if (type.equals("excelGroupMemberStuId")) {// 导入小组成员
			String group_name = DictionaryService.findGroups(groupId).getGroup_name();
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
			UserRole ur = new UserRole();
			String college_id = (String) session.getAttribute("college_id");
			List<Role> roleList = roleService.selectByCollegeId(college_id, "ROLE_STUDENT_PRACTICE");
			String role_id = "";
			if (roleList.size() > 0) {
				Role role = roleList.get(0);
				role_id = role.getId();
			}
			for (GroupMembers groupmebers : gms) {
				Student st = DictionaryService.findStudent(groupmebers.getUser_id());
				st.setGroup_id(group_name);// 学生表小组id先存名称
				groupmebers.setDuty("学生");
				groupmebers.setState("1");
				groupmebers.setBegin_time(time);
				groupmebers.setGroup_id(groupId);
				groupmebers.setTemp2(null);
				groupmebers.setTemp1(null);
				groupmebers.setId(Common.returnUUID16());
				this.studentService.update(st);
				this.groupMembersService.insert(groupmebers);

				ur.setUser_id(st.getId());
				ur.setRole_id(role_id);
				ur.setTemp1("无");
				userRoleService.insert(ur);// 导入小组成员添加实习学生角色

			}
			ret = "redirect:editGroups.do?id=" + groupId;
		}

		return ret;
	}


	/**
	 * 功能：管理员删除教师信息 注解请求地址(映射) ccc20141028
	 * 
	 * */
	@RequestMapping("admin/deleteTeacherImport.do")
	public String deleteTeacherImport(Teacher tc, String id) {

		tc.setState("0");
		teacherService.update(tc);
		return "redirect:teacherImportList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：导入成功列表 by 杨梦肖 2016 3 24
	 * */
	@RequestMapping("admin/deptImportSuccess.do")
	public ModelAndView deptImportSuccess(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> teaSuccessList = (List<Org>) session.getAttribute("tc");// 导入时存的session
		
		//教师id转换为教师姓名  周睿
		List<Org> teaList=new ArrayList<Org>();
		for(Org o:teaSuccessList){
			String con_name=((Teacher)teacherService.selectByID(o.getContacts())).getTrue_name();
			o.setContacts(con_name);
			String hea_name=((Teacher)teacherService.selectByID(o.getHead_tea_id())).getTrue_name();
			o.setHead_tea_id(hea_name);
			String cou_name=((Teacher)teacherService.selectByID(o.getCounselor_id())).getTrue_name();
			o.setCounselor_id(cou_name);
			teaList.add(o);
		}
		map.put("teaSuccessList", teaList);
		return new ModelAndView("admin/deptImportSuccess", map);
	}
	

	/**
	 * 功能：管理员 组织管理 wtt 20141030 邢志武 20150119 吴敬国2015-3-30
	 * */
	@RequestMapping("admin/orgList.do")
	public ModelAndView orgList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String defaultdept = "0";
		String defaultgrade = "1";
		List<Org> departmentlist = null;
		session.setAttribute("org_grade", defaultgrade);
		session.setAttribute("org_collegeid", "0");
		map.put("departmentlist", departmentlist);
		map.put("defaultdept", defaultdept);
		map.put("defaultgrade", defaultgrade);
		return new ModelAndView("admin/orgList", map);
	}

	/**
	 * 功能：管理员 参数设置 20160330 李泽
	 * */
	@RequestMapping("admin/parameter.do")
	public ModelAndView parameter(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String defaultdept = "0";
		String defaultgrade = "1";
		List<Org> departmentlist = null;
		session.setAttribute("org_grade", defaultgrade);
		session.setAttribute("org_collegeid", "0");
		map.put("departmentlist", departmentlist);
		map.put("defaultdept", defaultdept);
		map.put("defaultgrade", defaultgrade);
		return new ModelAndView("admin/parameter", map);
	}

	/**
	 * 功能：管理员默认看到所在学院的所有系 wtt 20141030 20150119 邢志武修改 吴敬国2015-3-30
	 * */
	@RequestMapping("admin/backParameter.do")
	public ModelAndView parameterBack(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String defaultdept = (String) session.getAttribute("org_collegeid");
		String defaultgrade = (String) session.getAttribute("org_grade");
		String xy_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist;
		if (defaultdept.equalsIgnoreCase("0")) {
			departmentlist = null;
		} else {
			if (defaultdept.equalsIgnoreCase("院系")) {
				departmentlist = orgService.getOrgDeptByCollegeId(xy_id);
			} else {
				departmentlist = orgService.getClassByCollegesAndGrade(xy_id,
						defaultgrade);
			}
		}
		map.put("departmentlist", departmentlist);
		map.put("defaultdept", defaultdept);
		map.put("defaultgrade", defaultgrade);
		return new ModelAndView("admin/parameter", map);
	}
	/**
	 * 功能：管理员默认看到所在学院的所有系 wtt 20141030 20150119 邢志武修改 吴敬国2015-3-30
	 * */
	@RequestMapping("admin/backOrgList.do")
	public ModelAndView orgBackList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String defaultdept = (String) session.getAttribute("org_collegeid");
		String defaultgrade = (String) session.getAttribute("org_grade");
		String xy_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist;
		if (defaultdept.equalsIgnoreCase("0")) {
			departmentlist = null;
		} else {
			if (defaultdept.equalsIgnoreCase("院系")) {
				departmentlist = orgService.getOrgDeptByCollegeId(xy_id);
			} else {
				departmentlist = orgService.getClassByCollegesAndGrade(xy_id,
						defaultgrade);
			}
		}
		map.put("departmentlist", departmentlist);
		map.put("defaultdept", defaultdept);
		map.put("defaultgrade", defaultgrade);
		return new ModelAndView("admin/orgList", map);
	}

	/**
	 * 功能：根据组织是否关联学生或者教师 吴敬国2015年5月25日
	 * */
	@RequestMapping("admin/checkOrgConnStu.do")
	@ResponseBody
	public String orgCheckConnStu(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String org_id = request.getParameter("org_id");
		int b, c;
		String infor = "";
		b = this.teacherService.selectCountByOrgId(org_id);
		c = this.studentService.selectCountByOrgId(org_id);
		if (b != 0 || c != 0) {
			infor = "2";// 有老师，
		} else {
			infor = "1";
		}
		return infor;
	}

	/**
	 * 功能：对组织名称在数据库是否重复做验证 吴敬国2015-5-15
	 * */
	@RequestMapping("admin/ajaxOrgNameRepeat.do")
	public String orgAjaxNameRepeat(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		String content = request.getParameter("content");
		String cont = "";
		try {
			cont = URLDecoder.decode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int b;
		String infor = "";// 说明没有重复
		b = this.orgService.checkOrgNameRepeat(cont.trim());
		if (b != 0) {
			infor = "此组织名已用";
		}
		try {
			response.getWriter().println(infor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax传递 根据查询种类查询相应组织 by 吴敬国2015-3-30 * 2015年5月29日
	 * */
	@RequestMapping("admin/ajaxGetOrg.do")
	public String orgAjaxGet(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String gradeAndDept = request.getParameter("gradeAnddept");
		String cont = "";
		try {
			cont = URLDecoder.decode(gradeAndDept, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int a = cont.indexOf("-");
		String dept = cont.substring(0, a);
		String grade = cont.substring(a + 1, cont.length());
		String xy_id = (String) session.getAttribute("college_id");
		session.setAttribute("org_grade", grade);
		session.setAttribute("org_collegeid", dept);
		if (cont != null) {
			List<Org> orgList;
			if (dept.equalsIgnoreCase("院系")) {
				orgList = orgService.getOrgDeptByCollegeId(xy_id);
			} else {
				orgList = orgService.getClassByCollegesAndGrade(xy_id, grade);// 通过学院的id和年份得到这一年的所有班级
			}
			StringBuffer sb = new StringBuffer();
			for (Org org : orgList) {
				sb.append("<tr>");
				sb.append("<td>" + org.getOrg_code() + "</td>");
				sb.append("<td>" + org.getOrg_name() + "</td>");
				if (org.getOrg_level().equalsIgnoreCase("2")) {
					sb.append("	<td>" + "院级" + "</td>");
				} else if (org.getOrg_level().equalsIgnoreCase("3")) {
					sb.append("	<td>" + "系级" + "</td>");
				} else {
					sb.append("	<td>" + "班级" + "</td>");
				}
				try {
					sb.append("	<td>"
							+ DictionaryService.findTeacher(org.getContacts())
									.getTrue_name() + "</td>");
				} catch (Exception e) {
					// TODO: handle exception
					sb.append("	<td>" + "无" + "</td>");
				}

				sb.append("	<td>" + org.getPhone() + "</td>");
				sb.append("	<td>"
						+ DictionaryService.findOrg(org.getParent_id())
								.getOrg_name() + "</td>");
				if (org.getState().equalsIgnoreCase("1")) {
					sb.append("	<td>" + "有效" + "</td>");
				} else {
					sb.append("	<td>" + "无效" + "</td>");
				}
				/*
				 * <td><input type="button" value="修改"
				 * onclick="editOrg('${o.id}','${o.org_level}')">
				 */
				sb.append("	<td><input type='button' value='修改' onclick=\"editOrg('"
						+ org.getId()
						+ "','"
						+ org.getOrg_level()
						+ " ')\">&nbsp;<input type='button' value='删除' onclick=\"doDel('"
						+ org.getId() + " ')\"></td>");
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
	 * 功能：ajax传递 根据查询种类查询相应组织 by 吴敬国2015-3-30 * 2015年5月29日
	 * */
	@RequestMapping("admin/ajaxGetOrgParameter.do")
	public String orgAjaxGetParameter(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String gradeAndDept = request.getParameter("gradeAnddept");
		String cont = "";
		try {
			cont = URLDecoder.decode(gradeAndDept, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int a = cont.indexOf("-");
		String dept = cont.substring(0, a);
		String grade = cont.substring(a + 1, cont.length());
		String xy_id = (String) session.getAttribute("college_id");
		session.setAttribute("org_grade", grade);
		session.setAttribute("org_collegeid", dept);
		if (cont != null) {
			List<Org> orgList;
			if (dept.equalsIgnoreCase("院系")) {
				orgList = orgService.getOrgDeptByCollegeId(xy_id);
			} else {
				orgList = orgService.getClassByCollegesAndGrade(xy_id, grade);// 通过学院的id和年份得到这一年的所有班级
			}
			StringBuffer sb = new StringBuffer();
			for (Org org : orgList) {
				sb.append("<tr>");
				sb.append("<td>" + org.getOrg_code() + "</td>");
				sb.append("<td>" + org.getOrg_name() + "</td>");
				if (org.getOrg_level().equalsIgnoreCase("2")) {
					sb.append("	<td>" + "院级" + "</td>");
				} else if (org.getOrg_level().equalsIgnoreCase("3")) {
					sb.append("	<td>" + "系级" + "</td>");
				} else {
					sb.append("	<td>" + "班级" + "</td>");
				}
				try {
					sb.append("	<td>"
							+ DictionaryService.findTeacher(org.getContacts())
									.getTrue_name() + "</td>");
				} catch (Exception e) {
					// TODO: handle exception
					sb.append("	<td>" + "无" + "</td>");
				}

				sb.append("	<td>" + org.getPhone() + "</td>");
				sb.append("	<td>"
						+ DictionaryService.findOrg(org.getParent_id())
								.getOrg_name() + "</td>");
				if (org.getState().equalsIgnoreCase("1")) {
					sb.append("	<td>" + "有效" + "</td>");
				} else {
					sb.append("	<td>" + "无效" + "</td>");
				}
				/*
				 * <td><input type="button" value="修改"
				 * onclick="editOrg('${o.id}','${o.org_level}')">
				 */
				sb.append("	<td><input type='button' value='实习日期参数' onclick=\"editMonth('"
						+ org.getId() + " ')\"></td>");
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
	 * 功能：管理员添加组织 添加页面 wtt 20141030 吴敬国 2015年5月26日
	 * */
	@RequestMapping("admin/addOrg.do")
	public String orgAdd(HttpSession session, ModelMap modelMap) {
		String college_id = (String) session.getAttribute("college_id");
		List<Org> Org_NameList = orgService.selectByXueYuanId(college_id);
		List<Org> departmentlist;
		departmentlist = orgService.selectXiOrg_Name(college_id);// 只获取系的id
		modelMap.put("Org_Name", Org_NameList);
		modelMap.put("departmentlist", departmentlist);
		return "admin/orgAdd";
	}

	/**
	 * 功能：管理员添加组织 保存 wtt 20141030 吴敬国2015年5月26日
	 * */
	@RequestMapping("admin/doAddOrg.do")
	public String orgSave(HttpServletRequest request, HttpSession session)
			throws IOException {
		String org_code = request.getParameter("org_code");
		String org_name = request.getParameter("org_name");
		String org_level = request.getParameter("org_level");
		String contacts = request.getParameter("contacts");
		String contactss = request.getParameter("contactss");// 班主任
		String counsellorss = request.getParameter("counsellorss");// 辅导员
		String phone = request.getParameter("phone");
		String parent_id = request.getParameter("par_dept");
		if (org_level.equalsIgnoreCase("3")) {
			parent_id = (String) session.getAttribute("college_id");// 得到负责的学院
		}
		String begin_time = request.getParameter("begin_Time");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(begin_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Org org = new Org();
		org.setId(Common.returnUUID());
		org.setBegin_time(ts);
		org.setOrg_code(org_code);
		org.setOrg_name(org_name);
		org.setOrg_level(org_level);
		if (contacts == null) {
			org.setContacts(contactss);// 班主任的ID
		} else {
			org.setContacts(contacts);
		}
		org.setCounselor_id(counsellorss);// 辅导员的ID
		org.setHead_tea_id(contactss);
		org.setDirector("");
		org.setVice_director("");
		org.setPhone(phone);
		org.setParent_id(parent_id);
		org.setState("1");
		orgService.insert(org);
		return "redirect:backOrgList.do";
	}

	/**
	 * 功能：管理员修改组织 wtt 20141030 吴敬国 2015年5月29日
	 * */
	@RequestMapping("admin/editOrg.do")
	public String orgEdit(ModelMap modelMap, String id, String org_level,
			HttpSession session) {
		Org org = orgService.selectByID(id);
		String contacts = org.getContacts();
		String praent_id = org.getParent_id();
		String college_id = (String) session.getAttribute("college_id");
		List<Org> orgList = orgService.selectByXueYuanId(college_id);
		List<Org> departmentlist;
		departmentlist = orgService.selectXiOrg_Name(college_id);// 只获取系的id
		modelMap.put("orgList", orgList);
		modelMap.put("departmentlist", departmentlist);
		modelMap.put("org", org);
		modelMap.put("org_level", org_level);
		modelMap.put("praent_id", praent_id);
		return "admin/orgEdit";
	}

	/**
	 * 功能：修改实习期限 by 李泽 2016-03-18
	 * */
	@RequestMapping("admin/editMonth.do")
	public ModelAndView monthEdit(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Org org = (Org) orgService.selectByID(id);
		Param prStart = new Param();
		prStart.setDept_id(id);
		prStart.setParam_name("开始月份");
		prStart = paramService.selectParambyIdAndParam_name(prStart);
		if (prStart == null) {
			prStart = new Param();
			prStart.setParam_value("00");
			prStart.setYear("00");
			prStart.setTearm("00");
		}
		Param prEnd = new Param();
		prEnd.setDept_id(id);
		prEnd.setParam_name("结束月份");
		prEnd = paramService.selectParambyIdAndParam_name(prEnd);
		if (prEnd == null) {
			prEnd = new Param();
			prEnd.setParam_value("00");
			prEnd.setYear("00");
			prEnd.setTearm("00");
		}
		map.put("org", org);
		map.put("prStart", prStart);
		map.put("prEnd", prEnd);
		return new ModelAndView("admin/editMonth", map);

	}

	/**
	 * 功能：修改系统参数保存 by 李泽 2016-03-19
	 * */
	@RequestMapping("admin/doEditMonth.do")
	public String DoEditMonth(HttpServletRequest request) {
		Param prStart = new Param();
		String dept_id = request.getParameter("dept_id");
		String monthStart = request.getParameter("monthStart");
		String startYear = request.getParameter("startYear");
		String startTearm = request.getParameter("startTearm");
		String startState = request.getParameter("startState");
		prStart.setDept_id(dept_id);
		prStart.setParam_code("start");
		prStart.setParam_name("开始月份");
		prStart.setParam_value(monthStart);
		prStart.setState(startState);
		prStart.setYear(startYear);
		prStart.setTearm(startTearm);
		Param prEnd = new Param();
		String monthEnd = request.getParameter("monthEnd");
		String endYear = request.getParameter("endYear");
		String endTearm = request.getParameter("endTearm");
		String endState = request.getParameter("endState");
		prEnd.setDept_id(dept_id);
		prEnd.setParam_code("end");
		prEnd.setParam_name("结束月份");
		prEnd.setParam_value(monthEnd);
		prEnd.setState(endState);
		prEnd.setYear(endYear);
		prEnd.setTearm(endTearm);
		Param pr = paramService.selectParambyIdAndParam_name(prStart);
		if (pr != null) {
			int s1 = paramService.updateByParam(prStart);
			int s2 = paramService.updateByParam(prEnd);
			System.out.println("*********************更新记录s1：" + s1+"*********************更新记录s2：" + s2);
		} else {
			String idStart = Common.returnUUID();
			String idEnd= Common.returnUUID();
			prStart.setId(idStart);
			prEnd.setId(idEnd);
			int a1 = paramService.insertByParam(prStart);
			int a2 = paramService.insertByParam(prEnd);
			System.out.println("*********************插入记录a1：" + a1+"*********************插入记录a2：" + a2);
		}
		return "redirect:backParameter.do";
	}

	/**
	 * 功能：管理员删除组织 wtt 20141030 吴敬国 2015年5月25日
	 * */
	@RequestMapping("admin/deleteOrg.do")
	public String orgDelete(String id) {
		Org o = DictionaryService.findOrg(id);
		o.setState("2");
		orgService.update(o);
		return "redirect:backOrgList.do";
	}

	/**
	 * 管理员角色管理
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("admin/roleList.do")
	public ModelAndView roleList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Role o = new Role();
		o.setState("1");
		o.setCollege_id(orgService.getCollegeIdByTeaId(session));
		List<Role> rolelist = this.roleService.selectList(o);
		/*
		 * SysMenu sysMenu=new SysMenu(); sysMenu.s
		 */
		map.put("rolelist", rolelist);
		return new ModelAndView("admin/roleList", map);
	}

	/**
	 * 管理员角色管理
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("admin/addRole.do")
	public ModelAndView roleAdd(HttpSession session) {
		/*
		 * SysMenu sysmenu=new SysMenu(); sysmenu.setTemp1("1");//管理员
		 * sysmenu.setSm_is_top("1"); List<SysMenu>
		 * sysMenuList=sysMenuService.selectTopMenuListByRoleType(sysmenu);
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		String collegeId = orgService.getCollegeIdByTeaId(session);
		String collegeCode = DictionaryService.findOrg(collegeId).getOrg_code();
		map.put("collegeCode", collegeCode);
		return new ModelAndView("admin/roleAdd", map);
	}

	/**
	 * 管理员角色管理
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("admin/role_getSysMenuByRoletype.do")
	public String role_getSysMenuByRoletype(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String role_type = request.getParameter("role_type");
		SysMenu sysmenu = new SysMenu();
		sysmenu.setTemp1(role_type);
		sysmenu.setSm_is_top("1");
		sysmenu.setSm_used("1");
		List<SysMenu> sysMenuList = sysMenuService
				.selectTopMenuListByRoleType(sysmenu);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='100%'>");
		sb.append("<tr><td>用户角色资源</td><td><table border='1'  width='100%'>");
		for (SysMenu s : sysMenuList) {
			sb.append("<tr id='" + s.getId() + "'>");
			sb.append("<td>");
			sb.append("<div class='checkbox'>");
			sb.append("<label>");
			sb.append("<input type='checkbox' name='roleMenu' onclick=\"return allSelect('"
					+ s.getId()
					+ "',this.checked)\" value='"
					+ s.getId()
					+ "'>" + s.getSm_name() + "</label>");
			/*
			 * sb.append("<td><input type='button' value='删除' onclick=\"doDel('"
			 * + s.getId() + " ')\"></td>");
			 */
			sb.append("</div>");
			sb.append("</td>");
			if (s.isIsleaf()) {
				sb.append("<td></td>");
			}
			if (!s.isIsleaf()) {
				sb.append("<td><table border='1' width='100%'>");
				List<SysMenu> sysMenuTwo = s.getSysMenuList();
				for (SysMenu two : sysMenuTwo) {
					sb.append("<tr id='" + two.getId() + "'>");
					sb.append("<td>");
					sb.append("<div class='checkbox'>");
					sb.append("<label>");
					sb.append("<input type='checkbox' name='roleMenu' onclick=\"return allSelect('"
							+ two.getId()
							+ "',this.checked)\" value='"
							+ two.getId()
							+ "'>"
							+ two.getSm_name()
							+ "</label>");
					sb.append("</div>");
					sb.append("</td>");
					if (two.isIsleaf()) {
						sb.append("<td width='50%'></td>");
					}
					if (!two.isIsleaf()) {
						sb.append("<td>");
						sb.append("<table  width='100%'>");
						List<SysMenu> sysMenuThree = two.getSysMenuList();
						for (SysMenu three : sysMenuThree) {
							sb.append("<tr>");
							sb.append("<td>");
							sb.append("<div class='checkbox'>");
							sb.append("<label>");
							sb.append("<input type='checkbox' name='roleMenu' onclick=\"return allSelect('"
									+ three.getId()
									+ "',this.checked)\" value='"
									+ three.getId()
									+ "'>"
									+ three.getSm_name()
									+ "</label>");
							sb.append("</div>");
							sb.append("</td>");
							sb.append("</tr>");
						}
						sb.append("</table>");
						sb.append("</td>");
					}
					sb.append("</tr>");
				}
				sb.append("</table>");
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 管理员角色管理
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("admin/doAddRole.do")
	public String roleSave(HttpServletRequest request, HttpSession session)
			throws IOException {
		String role_type = request.getParameter("role_type");
		String basicRole = request.getParameter("basicRole");
		String role_code = request.getParameter("role_code");
		role_code = basicRole + role_code;
		String role_name = request.getParameter("role_name");
		String role_desc = request.getParameter("role_desc");
		String state = request.getParameter("state");

		Role role = new Role();
		role.setId(role_code);
		role.setRole_code(role_code);
		role.setRole_type(role_type);
		role.setRole_name(role_name);
		role.setRole_desc(role_desc);
		role.setSchool_id("szxy");
		role.setCreate_tea(Common.getOneTeaId(session));
		role.setCollege_id(orgService.getCollegeIdByTeaId(session));
		role.setCreate_time(DateService.getNowTimeTimestamp());
		role.setState(state);
		role.setTemp1("3");
		roleService.insert(role);

		String[] roleMenu = request.getParameterValues("roleMenu");
		SysRoleMenu srm = new SysRoleMenu();
		// 循环插入到数据库中
		if (roleMenu != null) {
			for (int i = 0; i < roleMenu.length; i++) {
				srm.setSrm_menu_id(roleMenu[i]);
				srm.setSrm_role_id(role_code);
				sysRoleMenuService.insert(srm);
			}
		}
		return "redirect:roleList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 角色管理-修改角色
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("admin/editRole.do")
	public String roleEdit(ModelMap modelMap, String role_id) {
		List<SysMenu> sysMenuList = sysMenuService
				.selectListByRoleTypeAndId(role_id);// 查询出该角色所有的菜单
		List<String> pass_name = new ArrayList<String>();// 过滤掉基本菜单。
		pass_name.add("首页");
		pass_name.add("个人信息");
		pass_name.add("帮助");
		pass_name.add("联系我们");
		pass_name.add("管理员首页");
		List<SysMenu> sysMenuLists = new ArrayList();
		for (SysMenu n : sysMenuList) {
			if (!pass_name.contains(n.getSm_name())) {
				sysMenuLists.add(n);
			}
		}
		Role role = (Role) roleService.selectByID(role_id);
		modelMap.put("role", role);
		modelMap.put("sysMenuLists", sysMenuLists);
		return "admin/roleEdit";
	}

	/**
	 * 角色管理-修改保存角色
	 * 
	 * @Description
	 * @author 丁乐晓
	 * @return
	 */
	@RequestMapping("admin/doEditRole.do")
	public String roleDoEdit(@ModelAttribute("role") Role role,
			HttpServletRequest request) throws IOException {
		String id = request.getParameter("id");// 获取该角色ID
		List<SysRoleMenu> menuIdList = sysRoleMenuService.MenuList(id);// 查询出该角色所对应的菜单ID
		for (SysRoleMenu l : menuIdList) {
			sysRoleMenuService.delete(l);
		}// 删除数据库原有的对应菜单
		String[] sysMenu = request.getParameterValues("sysMenu");
		SysRoleMenu srm = new SysRoleMenu();
		if (sysMenu != null) {
			for (int i = 0; i < sysMenu.length; i++) {
				srm.setSrm_menu_id(sysMenu[i]);
				srm.setSrm_role_id(id);
				sysRoleMenuService.insert(srm);// 循环添加新的对应菜单
			}
		}
		return "redirect:roleList.do";
	}

	/**
	 * 角色管理-删除角色
	 * 
	 * @Description
	 * @author 丁乐晓
	 * @return
	 */
	@RequestMapping("admin/deleteRole.do")
	public String roleDelete(String id, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8"); // 转码
		/*
		 * PrintWriter out = response.getWriter(); int user_num;//定义用户数量
		 * UserRole ur = new UserRole(); ur.setRole_id(id); user_num =
		 * userRoleService.selectCount(ur);//查询出用户数量 if (user_num > 0) {
		 * out.print("该角色已被占用，无法删除!"); } else {
		 * 
		 * }
		 */
		roleService.delete(id);
		return "redirect:roleList.do";
	}

	/**
	 * 功能：评分标准查询列表 注解请求地址(映射) 李瑶婷20141030 修改：学院id筛选得到本学院和系部的奖惩标准 by王磊2015-1-26
	 * 
	 * */
	@RequestMapping("admin/evaluateStandardList.do")
	public ModelAndView evaluateStandardList(HttpSession session, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String years1 = (String) session.getAttribute("years1");
		String org_id1 = (String) session.getAttribute("org_id1");
		if (type != null) {
			session.removeAttribute("years1");
			session.removeAttribute("org_id1");
			years1 = null;
			org_id1 = null;
		}
		;
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<EvaluateStandard> result = this.evaluateStandardService
				.ListById(college_id);
		List<String> years = this.evaluateStandardService.ListYears();
		List<Org> o = this.orgService.selectByXueYuanId(college_id);
		map.put("result", result);
		map.put("years", years);
		map.put("orgs", o);
		map.put("years1", years1);
		map.put("org_id1", org_id1);
		return new ModelAndView("admin/evaluateStandardList", map);
	}

	/*
	 * 根据年份和范围查出标准 by王磊2015-1-26
	 */
	@RequestMapping("admin/getEvalStand.do")
	public String getEvalStand(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("UTF-8");
		String tj = request.getParameter("tiaojian");
		int a = tj.indexOf(",");
		String year = tj.substring(0, a);
		String scope = tj.substring(a + 1, tj.length());
		session.setAttribute("years1", year);
		session.setAttribute("org_id1", scope);
		List<EvaluateStandard> es = this.evaluateStandardService
				.ListByYearAndScope(scope, year);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='1200'>");
		sb.append("<tr> <th width='200'>标准名称</th> <th width='150'>类型</th><th width='200'>描述</th><th width='150'>适用范围</th><th width='100'>创建时间</th><th width='180'>操作</th></tr>");
		for (EvaluateStandard ess : es) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
			String createTime = format.format(ess.getCreate_time());
			sb.append("<tr><td align='center'><a href='editEvaluateStandard.do?id="
					+ ess.getId()
					+ "'>"
					+ ess.getStandard_name()
					+ "</a></td>"
					+ "<td align='center'>"
					+ ess.getType()
					+ "</td>"
					+ "<td align='center'>"
					+ ess.getDescription()
					+ "</td>"
					+ "<td align='center'>"
					+ DictionaryService.findOrg(ess.getScope()).getOrg_name()
					+ "</td>"
					+ "<td align='center'>"
					+ createTime
					+ "</td>"
					+ "<td align='center'>"
					+ "<input type='button' value='导入指标' onClick=doImport('"
					+ ess.getId()
					+ "');>"
					+ "<input type='button' value='删除' onClick=doDel('"
					+ ess.getId()
					+ "');>"
					+ "<input type='button' value='修改' onClick=doEdit('"
					+ ess.getId() + "');></td></tr>");
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
	 * 功能：管理员添加评分标准 注解请求地址(映射)--添加页面 李瑶婷20141030 修改：王磊，添加适用范围，进行过滤，进行表与表之间的联系
	 * */
	@RequestMapping("admin/addEvaluateStandard.do")
	public ModelAndView addEvaluateStandard(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> os = this.orgService.selectByXueYuanId(college_id);
		Org o = new Org();
		o = DictionaryService.findOrg(college_id);
		map.put("xy", o);
		map.put("os", os);
		return new ModelAndView("admin/evaluateStandardAdd", map);
	}

	/**
	 * 功能：得到指标名称和指标编码 2015年4月1日 修改： by王磊
	 * */
	@RequestMapping("admin/getEvalStandName.do")
	public String getEvalStandName(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String tj = request.getParameter("tiaojian");
		String[] strs = tj.split(",");
		String scope = strs[0];
		String type = strs[1];
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd//
											// HH:mm:ss格式显示
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = ts.toString();
		String year = str.substring(0, 4);
		String org_code = DictionaryService.findOrg(scope).getOrg_code();
		String org_name = DictionaryService.findOrg(scope).getOrg_name();
		String standard_name;
		if (type.equals("XWSX")) {
			String part_code = year + org_code + "XWSX";
			String part_name = year + org_name + "校外实习";
			if (this.evaluateStandardService.getMaxEvaluateStandCode(part_code) == null) {
				standard_name = part_name + "001";
			} else {
				int maxCode = Integer.parseInt(this.evaluateStandardService
						.getMaxEvaluateStandCode(part_code)) + 1;
				if (String.valueOf(maxCode).length() == 1) {
					standard_name = part_name + "00" + maxCode;
				} else if (String.valueOf(maxCode).length() == 2) {
					standard_name = part_name + "0" + maxCode;
				} else {
					standard_name = part_name + maxCode;
				}
			}
		} else {
			String part_code = year + org_code + "ZZXS";
			String part_name = year + org_name + "整周实训";
			if (this.evaluateStandardService.getMaxEvaluateStandCode(part_code) == null) {
				standard_name = part_name + "001";
			} else {
				int maxCode = Integer.parseInt(this.evaluateStandardService
						.getMaxEvaluateStandCode(part_code)) + 1;
				if (String.valueOf(maxCode).length() == 1) {
					standard_name = part_name + "00" + maxCode;
				} else if (String.valueOf(maxCode).length() == 2) {
					standard_name = part_name + "0" + maxCode;
				} else {
					standard_name = part_name + maxCode;
				}
			}

		}
		StringBuffer sb = new StringBuffer();
		sb.append(standard_name);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：管理员添加评分标准保存 李瑶婷 20141030 修改： by王磊
	 * */
	@RequestMapping("admin/doAddEvaluateStandard.do")
	public String doAddEvaluateStandard(HttpServletRequest request)
			throws IOException {
		String type = request.getParameter("type");
		String description = request.getParameter("description");
		String scope = request.getParameter("scope");
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd//
											// HH:mm:ss格式显示
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = ts.toString();
		String year = str.substring(0, 4);
		String org_code = DictionaryService.findOrg(scope).getOrg_code();
		String org_name = DictionaryService.findOrg(scope).getOrg_name();
		String standard_code;
		String standard_name;
		if (type.equals("XWSX")) {
			String part_code = year + org_code + "XWSX";
			String part_name = year + org_name + "校外实习";
			if (this.evaluateStandardService.getMaxEvaluateStandCode(part_code) == null) {
				standard_code = part_code + "001";
				standard_name = part_name + "001";
			} else {
				int maxCode = Integer.parseInt(this.evaluateStandardService
						.getMaxEvaluateStandCode(part_code)) + 1;
				if (String.valueOf(maxCode).length() == 1) {
					standard_code = part_code + "00" + maxCode;
					standard_name = part_name + "00" + maxCode;
				} else if (String.valueOf(maxCode).length() == 2) {
					standard_code = part_code + "0" + maxCode;
					standard_name = part_name + "0" + maxCode;
				} else {
					standard_code = part_code + maxCode;
					standard_name = part_name + maxCode;
				}
			}
		} else {
			String part_code = year + org_code + "ZZXS";
			String part_name = year + org_name + "整周实训";
			if (this.evaluateStandardService.getMaxEvaluateStandCode(part_code) == null) {
				standard_code = part_code + "001";
				standard_name = part_name + "001";
			} else {
				int maxCode = Integer.parseInt(this.evaluateStandardService
						.getMaxEvaluateStandCode(part_code)) + 1;
				if (String.valueOf(maxCode).length() == 1) {
					standard_code = part_code + "00" + maxCode;
					standard_name = part_name + "00" + maxCode;
				} else if (String.valueOf(maxCode).length() == 2) {
					standard_code = part_code + "0" + maxCode;
					standard_name = part_name + "0" + maxCode;
				} else {
					standard_code = part_code + maxCode;
					standard_name = part_name + maxCode;
				}
			}

		}

		EvaluateStandard evaluateStandard = new EvaluateStandard();
		evaluateStandard.setId(Common.returnUUID());
		evaluateStandard.setStandard_code(standard_code);
		evaluateStandard.setStandard_name(standard_name);
		if (type.equals("XWSX")) {
			type = "实习";
		} else {
			type = "实训";
		}
		evaluateStandard.setType(type);
		evaluateStandard.setDescription(description);
		evaluateStandard.setScope(scope);
		evaluateStandard.setCreate_time(ts);
		evaluateStandardService.insert(evaluateStandard);

		return "redirect:evaluateStandardList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员修改评分标准注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * */
	@RequestMapping("admin/editEvaluateStandard.do")
	public ModelAndView editEvaluateStandard(ModelMap modelMap, String id,
			HttpSession session) {
		EvaluateStandard evaluatestandard = DictionaryService
				.findEvaluateStandard(id);
		modelMap.put("e", evaluatestandard);
		return new ModelAndView("admin/evaluateStandardEdit", modelMap);

	}

	/**
	 * 功能：查看标准和指标列表
	 * 
	 * */
	@RequestMapping("admin/getStandardAndevalsIndex.do")
	public ModelAndView getStandardAndevalsIndex(ModelMap modelMap, String id,
			HttpSession session) {
		EvaluateStandard evaluatestandard = (EvaluateStandard) evaluateStandardService
				.selectByID(id);
		EvalsIndex evalsIndex = new EvalsIndex();
		evalsIndex.setStandard_id(id);
		List<EvalsIndex> result = this.evalsIndexService.selectList(evalsIndex);
		modelMap.put("e", evaluatestandard);
		modelMap.put("result", result);
		return new ModelAndView("admin/evaluateStandardDetail", modelMap);

	}

	/**
	 * 功能：管理员编辑评分标准保存 注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * @ModelAttribute1.注释方法 2.注释一个方法的参数 参考：spring学习之@ModelAttribute运用详解
	 *                       http://blog
	 *                       .csdn.net/li_xiao_ming/article/details/8349115
	 * */
	@RequestMapping("admin/doEditEvaluateStandard.do")
	public String doEditEvaluateStandard(String id, HttpServletRequest request)
			throws IOException {
		EvaluateStandard evaluatestandard = DictionaryService
				.findEvaluateStandard(id);
		String description = request.getParameter("description");
		evaluatestandard.setDescription(description);
		this.evaluateStandardService.update(evaluatestandard);
		return "redirect:evaluateStandardList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员删除评分标准 注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * */
	@RequestMapping("admin/deleteEvaluateStandard.do")
	public String deleteEvaluateStandard(String id) {
		evalsIndexService.deleteByStandId(id);
		evaluateStandardService.delete(id);
		return "redirect:evaluateStandardList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：根据标准id查出指标列表
	 * */
	@RequestMapping("admin/evalsIndexListByStandId.do")
	public ModelAndView evalsIndexListByStandId(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		EvalsIndex evalsIndex = new EvalsIndex();
		evalsIndex.setStandard_id(id);
		List<EvalsIndex> result = this.evalsIndexService.selectList(evalsIndex);
		map.put("result", result);
		return new ModelAndView("admin/evalsIndexListByStandId", map);
	}

	/**
	 * 功能：评分标准指标查询列表 注解请求地址(映射) 李瑶婷20141030 修改：过滤，联系，完善王磊2015年1月24日
	 * */
	@RequestMapping("admin/evalsIndexList.do")
	public ModelAndView evalsIndexList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<EvalsIndex> result = this.evalsIndexService.ListById(college_id);
		List<String> years = this.evaluateStandardService.ListYears();
		List<Org> o = this.orgService.selectByXueYuanId(college_id);
		map.put("years", years);
		map.put("result", result);
		map.put("orgs", o);
		return new ModelAndView("admin/evalsIndexList", map);
	}

	/*
	 * 根据年份和范围查出标准指标 by王磊2015-1-26
	 */
	@RequestMapping("admin/getEvalIndex.do")
	public String getEvalIndex(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String tj = request.getParameter("tiaojian");
		int a = tj.indexOf(",");
		String year = tj.substring(0, a);
		String scope = tj.substring(a + 1, tj.length());
		List<EvaluateStandard> es = this.evaluateStandardService
				.ListByYearAndScope(scope, year);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='id1' name='id1'>");
		sb.append("<option>请选择</option>");
		for (EvaluateStandard ess : es) {

			sb.append("<option value=" + ess.getId() + ">"
					+ ess.getStandard_name() + "</option>");

		}
		sb.append("</select>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	/*
	 * 根据标准id得到标准指标by王磊2015-1-28
	 */
	@RequestMapping("admin/getEvaluList.do")
	public String getEvaluList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String stand_id = request.getParameter("stand_id");
		EvalsIndex ei = new EvalsIndex();
		ei.setStandard_id(stand_id);
		List<EvalsIndex> result = this.evalsIndexService.selectList(ei);
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' width='750'>");
		sb.append("<tr> <th width='200' align='center'>标准名称</th> <th width='200' align='center'>指标名称</th><th width='300'>描述</th><th width='50' align='center'>分值</th><th width='150' align='center'>创建时间</th><th width='50' align='center'>操作</th> </tr>");
		for (EvalsIndex eis : result) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 设置显示格式
			String createTime = format.format(eis.getCreate_time());
			sb.append("<tr><td align='center'>"
					+ DictionaryService.findEvaluateStandard(
							eis.getStandard_id()).getStandard_name() + "</td>"
					+ "<td align='center'><a href='editEvalsIndex.do?id="
					+ eis.getId() + "'>" + eis.getIndex_name() + "</a></td>"
					+ "<td align='center'>" + eis.getDescription() + "</td>"
					+ "<td align='center'>" + eis.getScore() + "</td>"
					+ "<td align='center'>" + createTime + "</td>"
					+ "<td align='center'>"
					+ "<input type='button' value='修改' onClick=doEdit('"
					+ eis.getId() + "');>"
					+ "<input type='button' value='删除' onClick=doDel('"
					+ eis.getId() + "');></td></tr>");
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
	 * 功能：管理员添加评分标准指标 注解请求地址(映射)--添加页面 李瑶婷20141030 修改：过滤，联系，完善王磊2015年1月24日
	 * */
	@RequestMapping("admin/addEvalsIndex.do")
	public ModelAndView addEvalsIndex(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<EvaluateStandard> result = this.evaluateStandardService
				.ListById(college_id);
		List<String> years = this.evaluateStandardService.ListYears();
		map.put("years", years);
		map.put("es", result);
		return new ModelAndView("admin/evalsIndexAdd", map);
	}

	/*
	 * 根据年份和学院id查出标准信息 by王磊2015-1-26
	 */
	@RequestMapping("admin/getStandName.do")
	public String getEvalName(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setCharacterEncoding("UTF-8");
		String year = request.getParameter("year");
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<EvaluateStandard> result = this.evaluateStandardService
				.selectByYearAndId(college_id, year);
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='standard_id' name='standard_id'>");
		sb.append("<option>请选择标准</option>");
		for (EvaluateStandard rs : result) {
			sb.append("<option value=" + rs.getId() + ">"
					+ rs.getStandard_name() + "</option>");
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
	 * 功能：管理员添加评分标准指标保存 注解请求地址(映射) 李瑶婷 20141030 修改：过滤，联系，完善王磊2015年1月24日
	 * */
	/*
	 * @RequestMapping("admin/doAddEvalsIndex.do") public String
	 * saveEvalsIndex(HttpServletRequest request) throws IOException {
	 * 
	 * Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间 DateFormat format = new
	 * SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式 String nowTime =
	 * format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd //
	 * HH:mm:ss格式显示
	 * 
	 * Timestamp ts = new Timestamp(System.currentTimeMillis()); try { ts = new
	 * Timestamp(format.parse(nowTime).getTime()); } catch (ParseException e) {
	 * e.printStackTrace(); }
	 * 
	 * String standard_id = request.getParameter("standard_id"); String
	 * index_code = request.getParameter("index_code"); String index_name =
	 * request.getParameter("index_name"); String description =
	 * request.getParameter("description"); Double score =
	 * Double.parseDouble(request.getParameter("score"));
	 * 
	 * EvalsIndex evalsIndex = new EvalsIndex();
	 * evalsIndex.setId(Common.returnUUID());
	 * evalsIndex.setStandard_id(standard_id);
	 * evalsIndex.setIndex_code(index_code);
	 * evalsIndex.setIndex_name(index_name);
	 * evalsIndex.setDescription(description); evalsIndex.setScore(score);
	 * evalsIndex.setCreate_time(ts); evalsIndexService.insert(evalsIndex);
	 * 
	 * return "redirect:evalsIndexList.do"; // 添加成功后重定向到列表页面 }
	 */

	/**
	 * 功能：管理员修改评分标准指标注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * */
	@RequestMapping("admin/editEvalsIndex.do")
	public ModelAndView editEvalsIndex(ModelMap modelMap, String id,
			HttpSession session) {
		EvalsIndex evalsIndex = (EvalsIndex) evalsIndexService.selectByID(id);
		String name = DictionaryService.findEvaluateStandard(
				evalsIndex.getStandard_id()).getStandard_name();
		modelMap.put("e", evalsIndex);
		modelMap.put("name", name);
		return new ModelAndView("admin/evalsIndexEdit", modelMap);
	}

	/*	*//**
	 * 功能：管理员编辑评分标准指标保存 注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * @ModelAttribute1.注释方法 2.注释一个方法的参数 参考：spring学习之@ModelAttribute运用详解
	 *                       http://blog
	 *                       .csdn.net/li_xiao_ming/article/details/8349115
	 * */
	/*
	 * @RequestMapping("admin/doEditEvalsIndex.do") public String
	 * editEvalsIndex(
	 * 
	 * @ModelAttribute("EvalsIndex") EvalsIndex evalsIndex) throws IOException {
	 * evalsIndexService.update(evalsIndex); return
	 * "redirect:evalsIndexList.do"; // 添加成功后重定向到列表页面
	 * 
	 * }
	 */
	/**
	 * 功能：管理员编辑评分标准指标保存 注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * @ModelAttribute1.注释方法 2.注释一个方法的参数 参考：spring学习之@ModelAttribute运用详解
	 *                       http://blog
	 *                       .csdn.net/li_xiao_ming/article/details/8349115
	 * */
	@RequestMapping("admin/doEditEvalsIndex.do")
	public String editEvalsIndex(HttpServletRequest request) throws IOException {
		String evals_id = request.getParameter("id");
		EvalsIndex evalsIndex = (EvalsIndex) this.evalsIndexService
				.selectByID(evals_id);
		String index_code = request.getParameter("index_code");
		String index_name = request.getParameter("index_name");
		String description = request.getParameter("description");
		String id = evalsIndex.getStandard_id();
		Double score = Double.parseDouble(request.getParameter("score"));
		evalsIndex.setIndex_code(index_code);
		evalsIndex.setIndex_name(index_name);
		evalsIndex.setDescription(description);
		evalsIndex.setScore(score);
		evalsIndexService.update(evalsIndex);
		return "redirect:evalsIndexListByStandId.do?id=" + id;
	}

	/**
	 * 功能：管理员删除评分标准指标 注解请求地址(映射) 李瑶婷 20141030
	 * 
	 * */
	@RequestMapping("admin/deleteEvalsIndex.do")
	public String deleteEvalsIndex(String id) {
		String si = DictionaryService.findEvalsIndex(id).getStandard_id();
		evalsIndexService.delete(id);
		return "redirect:evalsIndexListByStandId.do?id=" + si; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：管理员添家指标(映射)--添加页面王磊2015-1-24
	 * 
	 * */
	@RequestMapping("admin/importEvalsIndex.do")
	public ModelAndView addEvalsIndexImport(String standard_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("standard_id", standard_id);
		return new ModelAndView("admin/evalsIndexImport", map);
	}

	/**
	 * 功能：用户权限查询列表 权限管理 吴敬国 2015-8-31
	 * */
	@RequestMapping("admin/userRoleList.do")
	public ModelAndView userRoleList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> departmentlist;
		departmentlist = orgService.getOrgDeptByCollegeId(college_id);// 获取系及学院自身
		List<UserRole> result = userRoleService
				.selectUserRoleByDeptId(college_id);
		session.setAttribute("userrole_department_id", college_id);
		map.put("result", result);
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/userRoleList", map);
	}

	/**
	 * 返回上一个用户权限列表 by吴敬国20150312
	 * 
	 * 
	 * */
	@RequestMapping("admin/backuserRoleList.do")
	public ModelAndView userRoleBackList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");// 得到负责的学院
		List<Org> departmentlist;
		departmentlist = orgService.getOrgDeptByCollegeId(college_id);// 获取系及学院自身
		String dept_id = (String) session
				.getAttribute("userrole_department_id");
		String dept_name = DictionaryService.findOrg(dept_id).getOrg_name();
		List<UserRole> userRoleList = userRoleService
				.selectUserRoleByDeptId(dept_id);
		map.put("dept_name", dept_name);
		map.put("result", userRoleList);
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/userRoleList", map);
	}

	/**
	 * 功能：ajax传递 用户权限 by 吴敬国20150117 *
	 * 
	 * */
	@RequestMapping("admin/ajaxUserRole.do")
	public String userRoleAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String department_id = request.getParameter("department_idAndrole_id");
		session.setAttribute("userrole_department_id", department_id);
		if (department_id != null) {
			List<UserRole> userrole = userRoleService
					.selectUserRoleByDeptId(department_id);
			session.setAttribute("userrole", userrole);
			StringBuffer sb = new StringBuffer();
			for (UserRole ur : userrole) {
				sb.append("<tr>");
				/*
				 * sb.append("<td><a href='getThisTeacherUserRole.do?id="+
				 * ur.getUser_id()+ "'>"+
				 * DictionaryService.findTeacher(ur.getUser_id()).getTrue_name()
				 * + "</a></td>");
				 */
				sb.append("<td>"
						+ DictionaryService.findTeacher(ur.getUser_id())
								.getTrue_name() + "</a></td>");
				sb.append("<td>"
						+ DictionaryService.findRole(ur.getRole_id())
								.getRole_name() + "</td>");
				if (ur.getState().equalsIgnoreCase("1")) {
					sb.append("<td>" + "有效" + "</td>");
				} else {
					sb.append("<td>" + "无效" + "</td>");
				}
				if (ur.getTemp1() == null) {
					sb.append("<td>" + "无" + "</td>");
				} else {
					sb.append("<td>" + ur.getTemp1() + "</td>");
				}
				sb.append("<td><input type='button' value='删除' onclick=\"doDel('"
						+ ur.getId() + " ')\"></td>");
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
	 * 功能：添加用户权限界面 从数据库得到当前的用户角色 by吴敬国20141110
	 * 
	 * */
	@RequestMapping("admin/addUserRole.do")
	public ModelAndView userRoleAdd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Role rl = new Role();
		rl.setCollege_id(orgService.getCollegeIdByTeaId(session));
		List<Role> roleList = userRoleService.selectRole(rl);
		List<Org> orgList = orgService.selectByXueYuanId((String) session
				.getAttribute("college_id"));
		map.put("orgList", orgList);
		map.put("roleList", roleList);
		return new ModelAndView("admin/userRoleAdd", map);
	}

	/**
	 * 功能：用户权限管理 ajax得到没有这种角色的用户 by 吴敬国20141110
	 * 
	 * @param role_id
	 * 
	 */
	@RequestMapping("admin/getTeaBydeptIdAndRoleName.do")
	public String userRoleAjaxGetTeaByRoleId(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String role_id = request.getParameter("role_name");
		String dept_id = request.getParameter("dept_id");
		// 将role_id保存到session中以便保存用户权限的时候取出
		session.setAttribute("role_id", role_id);
		// 获取这个学院下没有该权限的教师的姓名
		List<Teacher> teaList = this.userRoleService.selectTeacherByRoleId(
				dept_id, role_id);
		StringBuffer sb = new StringBuffer();

		for (Teacher ts : teaList) {
			sb.append("<tr id = 'select'>");
			sb.append("<td>" + ts.getTrue_name() + "</td>");
			if (ts.getPhone().equalsIgnoreCase("")
					|| ts.getPhone().equals(null)) {
				sb.append("<td>" + "无" + "</td>");
			} else {
				sb.append("<td>" + ts.getPhone() + "</td>");
			}
			/*
			 * if(ts.getQqnum().equalsIgnoreCase("")||ts.getQqnum().equals(null))
			 * { sb.append("<td>" + "无" + "</td>"); }else{ sb.append("<td>" +
			 * ts.getQqnum() + "</td>"); }
			 */
			sb.append("<td ><input type='checkbox'  name='teacher' id='"
					+ ts.getId() + "' value='" + ts.getId() + "'  ></td>");
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
	 * 功能：保存用户权限 by吴敬国20141110
	 * 
	 * */
	@RequestMapping("admin/doAdduserRole.do")
	public String userRoleSave(HttpServletRequest request, HttpSession session)
			throws IOException {
		// 获取修改权限人的姓名
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String true_name = tea.getTrue_name();
		// 获取当前时间
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = "";
		nowTime = df.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		// String role_id = (String) session.getAttribute("role_id");//
		// 从session获取角色编号
		String role_id = request.getParameter("role_name");
		UserRole ur = new UserRole();
		ur.setRole_id(role_id);
		ur.setState("1");
		ur.setTemp1(true_name + "" + nowTime);
		// 获取复选框数组
		String[] teacherSelected;
		teacherSelected = request.getParameterValues("teacher");
		// 循环插入到数据库中
		if (teacherSelected != null) {
			for (int i = 0; i < teacherSelected.length; i++) {
				ur.setUser_id(teacherSelected[i]);
				userRoleService.insert(ur);
			}
		}
		String roleType = DictionaryService.findRole(role_id).getRole_type();
		UserRole userrole = new UserRole();
		if (roleType.equals("1")) {
			userrole.setRole_id("ROLE_ADMIN");
		} else if (roleType.equals("2")) {
			userrole.setRole_id("ROLE_TEACHER");// 这地方以后可以去掉，既然是用户分配了，说明这个用户已经有基本的教师或者学生角色2016-1-10
		} else if (roleType.equals("3")) {
			userrole.setRole_id("ROLE_STUDENT");
		} else if (roleType.equals("4")) {
			userrole.setRole_id("ROLE_LEADER");
		} else {
			userrole.setRole_id("ROLE_COMPANY");
		}
		userrole.setState("1");
		userrole.setTemp1(true_name + "" + nowTime);
		if (teacherSelected != null) {
			for (int i = 0; i < teacherSelected.length; i++) {
				userrole.setUser_id(teacherSelected[i]);
				userRoleService.insert(userrole);
			}
		}
		return "redirect:backuserRoleList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员删除用户权限 吴敬国20141110
	 * 
	 * */
	@RequestMapping("admin/deleteUserRole.do")
	public String userRoleDelete(String id) {
		UserRole userRole = DictionaryService.findUserRole(id);
		userRoleService.delete(userRole);
		return "redirect:backuserRoleList.do";
	}

	/**
	 * 功能：维护教师 注解请求地址(映射) wtt20141106
	 * 
	 * */

	@RequestMapping("admin/editmyInfo.do")
	public ModelAndView editmyInfo(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_code = tea.getTea_code();
		List<Teacher> teaList = this.teacherService
				.selectListByTeaCode(tea_code);// selectByCode(tea_code);
		map.put("teaList", teaList);
		return new ModelAndView("admin/editmyInfo", map);

	}

	/**
	 * 功能：维护教师 注解请求地址(映射) wtt20141106
	 * 
	 * */
	@RequestMapping("admin/doeditmyInfo.do")
	public String editTeacher1(HttpServletRequest request, HttpSession session)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String true_name = request.getParameter("true_name");
		String phone = request.getParameter("phone");
		String qqnum = request.getParameter("qqnum");
		String email = request.getParameter("email");
		String homepage = request.getParameter("homepage");
		String expertise = request.getParameter("expertise");
		String duties = request.getParameter("duties");
		tea.setTrue_name(true_name);
		tea.setPhone(phone);
		tea.setQqnum(qqnum);
		tea.setEmail(email);
		tea.setHomepage(homepage);
		tea.setExpertise(expertise);
		tea.setDuties(duties);
		teacherService.update(tea);
		return "redirect:index.do"; // 添加成功后重定向到列表页面
	}

	@RequestMapping("admin/myInfo.do")
	public ModelAndView myInfo(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");

		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());// selectByCode(tea_code);
		map.put("List", teaList);
		return new ModelAndView("admin/index", map);

	}

	/**
	 * 功能：修改密码 注解请求地址(映射) wtt20141106 修改： 王磊2015-7-1 原因：错误混乱。
	 * 
	 * */

	@RequestMapping("admin/passwordEdit.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		Teacher teacher = (Teacher) this.teacherService.selectByID(tea_id);
		map.put("t", teacher);
		return new ModelAndView("admin/passwordEdit", map);

	}

	/**
	 * 功能：修改密码 注解请求地址(映射) 郑春光20140910
	 * 
	 * */

	@RequestMapping("admin/doPasswordEdit.do")
	public String doPasswordEdit(String id, HttpServletRequest request)
			throws IOException {
		String newPassWord = request.getParameter("login_pass");
		Teacher teacher = DictionaryService.findTeacher(id);
		teacher.setLogin_pass(newPassWord);
		teacherService.update(teacher);
		return "redirect:../login.jsp"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：修改密码 注解请求地址(映射) 郑春光20140910
	 * 
	 * */

	@RequestMapping("admin/password.do")
	public ModelAndView password(HttpSession session, HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		List<Teacher> teaList = this.teacherService.selectListByTeaCode(tea
				.getTea_code());
		map.put("List", teaList);
		return new ModelAndView("admin/index", map);

	}

	/**
	 * 功能：企业管理 王磊 2015-7-6
	 * 
	 * */
	@RequestMapping("admin/companyList.do")
	public ModelAndView companyList(HttpSession session,
			HttpServletRequest request, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		String industry1 = (String) session.getAttribute("industry");
		String company_part_id = (String) session
				.getAttribute("company_part_id");
		String companyContent = (String) session.getAttribute("companyContent");
		if (type != null) {
			session.removeAttribute("industry");
			session.removeAttribute("company_part_id");
			session.setAttribute("companyContent", "地址/名称/时间/编码/联系人");
			industry1 = null;
			company_part_id = null;
			companyContent = "地址/名称/时间/编码/联系人";
		} else if (industry1 == null && company_part_id == null
				&& companyContent == null) {
			companyContent = "地址/名称/时间/编码/联系人";
		}
		/*
		 * String xy_id; String dept_id = tea.getDept_id(); String dept_level =
		 * DictionaryService.findOrg(dept_id).getOrg_level();
		 */
		/*
		 * if (dept_level.equals("2")) { xy_id = dept_id;//
		 * 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id } else { xy_id =
		 * DictionaryService.findOrg(dept_id).getParent_id();//
		 * 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id }
		 */
		String college_id = (String) session.getAttribute("college_id");
		List<Company> companyList = this.companyService
				.getCompanysById(college_id);
		// 进行分页操作
		int Currentpage = 1;// 初始值1
		// 获取当前页集合
		List<Teacher> listCurrentPage = Common.getListCurrentPage(companyList,
				pageSizeConstant, Currentpage);
		// 得到总页数
		int pageCount = Common.getPageCount(companyList, pageSizeConstant,
				Currentpage);
		Org o = new Org();
		o.setOrg_level("2");
		List<Org> orgs = this.orgService.selectList(o);
		session.setAttribute("companys", companyList);// 后.....分页再次调用
		// map.put("result", result);
		map.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());// 行业
		map.put("result", listCurrentPage);// 获取当前页集合
		map.put("orgs", orgs);// 所有院级组织的集合
		map.put("count", pageCount);// 总页数
		map.put("nowPage", Currentpage);// 当前页
		map.put("industry1", industry1);
		map.put("companyContent", companyContent);
		map.put("company_part_id", company_part_id);
		map.put("tea_id", Common.getOneTeaId(session));
		return new ModelAndView("admin/companyList", map);
	}

	/**
	 * by王磊 2015年4月4日 功能：实现分页功能
	 * 
	 * */
	@RequestMapping("admin/getCompanyByPage.do")
	public String getCompanyByPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		// 获取教师集合，增加条件：下拉框选中的组织id
		List<Company> result = (List<Company>) session.getAttribute("companys");
		String toPage = request.getParameter("toPage");
		List<Company> newResult;// 当前页的集合
		newResult = Common.getListCurrentPage(result, pageSizeConstant,
				Integer.parseInt(toPage));
		String state = "有效";
		StringBuffer sb = new StringBuffer();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		for (Company company : newResult) {
			String[] coll_id = company.getApplicable_scope().split(",");
			String coll_name = "";
			String Industry = DictionaryService
					.getmapIndustryClassificationCode().get(
							company.getIndustry());
			String checkMan;
			if (Industry == null) {
				Industry = "无此行业";
			}
			if (DictionaryService.findTeacher(company.getCheck_man()) != null) {
				checkMan = DictionaryService
						.findTeacher(company.getCheck_man()).getTrue_name();
			} else {
				checkMan = "<span><font color='red'>暂无审核</font></span>";
			}
			for (int i = 0; i < coll_id.length; i++) {
				coll_name = coll_name
						+ DictionaryService.findOrg(coll_id[i]).getOrg_name()
						+ " ";
			}
			String createTime = format.format(company.getCreate_time());
			if (company.getState().equals("0")) {
				state = "无效";
			}
			sb.append("<tr>");
			sb.append("<td align='center'>" + company.getCom_name() + "</td>");
			sb.append("<td align='center'>" + company.getContacts() + "</td>");
			sb.append("<td align='center'>" + company.getPhone() + "</td>");
			sb.append("<td align='center'>" + company.getAddress() + "</td>");
			sb.append("<td align='center'>" + coll_name + "</td>");
			sb.append("<td align='center'>" + Industry + "</td>");
			sb.append("<td align='center'>" + checkMan + "</td>");
			sb.append("<td align='center'>" + state + "</td>");
			sb.append("<td align='center'>" + createTime + "</td>");
			sb.append("<td align='center'>"
					+ "<input type='button' value='修改' onclick=editCompany('"
					+ company.getId() + "','" + company.getCheck_man() + "')>"
					+ "</td>");
			sb.append("</tr>");
		}
		sb.append("FENGECOMPANY" + toPage);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * by 王磊 功能：根据行业和学院id和关键字查出对应的企业 2015年3月17日
	 * */
	@RequestMapping("admin/getCompany.do")
	public String companyList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		List<Company> cs = new ArrayList<Company>();
		String tj = request.getParameter("tiaojian");
		String state = "有效";
		String[] strs = tj.split(",");
		String industry = strs[0];
		String company_part_id = strs[1];
		String companyContent = strs[2];
		companyContent = URLDecoder.decode(companyContent, "utf-8");
		session.setAttribute("industry", industry);
		session.setAttribute("company_part_id", company_part_id);
		session.setAttribute("companyContent", companyContent);
		if (industry.equals("0")) {
			industry = null;
		}
		;
		if (company_part_id.equals("0")) {
			company_part_id = null;
		}
		;
		if (companyContent.equals("地址/名称/时间/编码/联系人")) {
			companyContent = null;
		}
		cs = this.companyService.selectCompanys(industry, company_part_id,
				companyContent);
		StringBuffer sb = new StringBuffer();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		List<Company> newResult;
		// 得到总页数
		int pageCount = Common.getPageCount(cs, pageSizeConstant, 1);
		newResult = Common.getListCurrentPage(cs, pageSizeConstant, 1);
		session.setAttribute("companys", cs);
		if (newResult == null) {
			sb.append("");
		} else {
			for (Company company : cs) {
				String[] coll_id = company.getApplicable_scope().split(",");
				String coll_name = "";
				String Industry = DictionaryService
						.getmapIndustryClassificationCode().get(
								company.getIndustry());
				String checkMan;
				if (Industry == null) {
					Industry = "无此行业";
				}
				if (DictionaryService.findTeacher(company.getCheck_man()) != null) {
					checkMan = DictionaryService.findTeacher(
							company.getCheck_man()).getTrue_name();
				} else {
					checkMan = "<span><font color='red'>暂无审核</font></span>";
				}
				for (int i = 0; i < coll_id.length; i++) {
					coll_name = coll_name
							+ DictionaryService.findOrg(coll_id[i])
									.getOrg_name() + " ";
				}
				String createTime = format.format(company.getCreate_time());
				if (company.getState().equals("0")) {
					state = "无效";
				}
				sb.append("<tr>");
				sb.append("<td align='center'>" + company.getCom_name()
						+ "</td>");
				sb.append("<td align='center'>" + company.getContacts()
						+ "</td>");
				sb.append("<td align='center'>" + company.getPhone() + "</td>");
				sb.append("<td align='center'>" + company.getAddress()
						+ "</td>");
				sb.append("<td align='center'>" + coll_name + "</td>");
				sb.append("<td align='center'>" + Industry + "</td>");
				sb.append("<td align='center'>" + checkMan + "</td>");
				sb.append("<td align='center'>" + state + "</td>");
				sb.append("<td align='center'>" + createTime + "</td>");
				sb.append("<td align='center'><input type='button' value='修改' onclick=editCompany('"
						+ company.getId()
						+ "','"
						+ company.getCheck_man()
						+ "')>" + "</td>");
				sb.append("</tr>");
			}
		}
		sb.append("FENGECOMPANY" + pageCount + "FENGECOMPANY" + "共 "
				+ pageCount + " 页");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 功能：企业管理 by 王磊 2015年3月25日
	 * */
	@RequestMapping("admin/addCompany.do")
	public String companyAdd(ModelMap modelMap) {
		modelMap.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		Org o = new Org();
		o.setOrg_level("2");
		List<Org> orgs = this.orgService.selectList(o);
		modelMap.put("orgs", orgs);
		return "admin/companyAdd";
	}

	/**
	 * 功能：企业管理-对企业编号在数据库是否重复做验证 吴敬国2016-1-12
	 * */
	@RequestMapping("admin/checkCompanyCode.do")
	public String companyCodeCheck(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		String com_code = request.getParameter("com_code");
		int b;
		String infor = "";
		b = this.companyService.selectByTeaCode(com_code);
		if (b != 0) {
			infor = "此企业编号已用";
		}
		try {
			response.getWriter().println(infor);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：企业管理-企业 保存 王磊 2015年3月30日 吴敬国2016-1-12
	 * */
	@RequestMapping("admin/doAddCompany.do")
	public String companySave(HttpServletRequest request, HttpSession session)
			throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String com_name = request.getParameter("com_name");
		String com_code = request.getParameter("com_code");
		String short_name = request.getParameter("short_name");
		String contacts = request.getParameter("contacts");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String industry = request.getParameter("industry");
		String[] orgs = request.getParameterValues("orgs");
		String Applicable_scope = "";
		for (int i = 0; i < orgs.length; i++) {
			Applicable_scope = orgs[i] + "," + Applicable_scope;
		}
		Company company = new Company();
		company.setId(Common.returnUUID());
		company.setCreate_time(ts);
		company.setCom_name(com_name.trim());// 去掉首位空格，为了以后对导入时企业名称的验证。
		company.setCom_code(com_code);
		company.setShort_name(short_name);
		company.setContacts(contacts);
		company.setPhone(phone);
		company.setAddress(address);
		company.setCheck_man(tea_id);
		company.setApplicable_scope(Applicable_scope);
		company.setEmail(email);
		company.setCheck_state("1");
		company.setCheck_note("管理员添加");
		company.setIndustry(industry);
		companyService.insert(company);

		return "redirect:companyList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员添加企业信息 王磊2015-1-24
	 * 
	 * */
	@RequestMapping("admin/CompanyImport.do")
	public String companyImport() {
		return "admin/companyImport";
	}

	/**
	 * 功能：导入企业成功列表 by 王磊 2015-5-18
	 * */
	@RequestMapping("admin/companyImportSuccess.do")
	public ModelAndView companyImportSuccess(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Company> companyImportList = (List<Company>) session
				.getAttribute("companys");// 导入时存的session
		map.put("companyImportList", companyImportList);
		return new ModelAndView("admin/companyImportSuccess", map);// return new
																	// ModelAndView("admin/teacherImportSuccess",
																	// map);
	}

	/**
	 * 功能：管理员修改企业注解请求地址(映射) 李瑶婷20141105* 修改 by王磊
	 * */
	@RequestMapping("admin/editCompany.do")
	public ModelAndView editCompany(ModelMap modelMap, String id) {
		Company company = (Company) companyService.selectByID(id);
		modelMap.put("c", company);
		List<String> state = new ArrayList<String>();
		state.add(0, "无效");
		state.add(1, "有效");
		if (company.getState().equals("1")) {
			state.remove(1);
			state.add(0, "有效");
		}
		modelMap.put("state", state);
		return new ModelAndView("admin/companyEdit", modelMap);
	}

	/**
	 * 功能：管理员编辑企业 保存 注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * @ModelAttribute1.注释方法 2.注释一个方法的参数 参考：spring学习之@ModelAttribute运用详解
	 *                       http://blog
	 *                       .csdn.net/li_xiao_ming/article/details/8349115
	 * */
	@RequestMapping("admin/doEditCompany.do")
	public String editCompany(HttpServletRequest request, String id)
			throws IOException {
		String com_name = request.getParameter("com_name");
		String short_name = request.getParameter("short_name");
		String contacts = request.getParameter("contacts");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String check_note = request.getParameter("check_note");
		String state = request.getParameter("state");
		Company company = DictionaryService.findCompany(id);
		company.setAddress(address);
		company.setCheck_note(check_note);
		company.setCom_name(com_name);
		company.setShort_name(short_name);
		company.setContacts(contacts);
		company.setPhone(phone);
		company.setEmail(email);
		if (state.equals("有效")) {
			company.setState("1");
		} else {
			company.setState("0");
		}
		companyService.update(company);
		return "redirect:companyList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员删除企业 注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * */
	@RequestMapping("admin/deleteCompany.do")
	public String deleteCompany(String id) {
		// Company company = new Company();
		Company company = DictionaryService.findCompany(id);
		company.setId(id);
		company.setState("0");
		companyService.update(company);
		return "redirect:companyList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：审核 注解请求地址(映射) by李瑶婷 20141105*
	 * 
	 * @throws IOException
	 * @throws ServletException
	 *             *
	 * 
	 * */
	@RequestMapping("admin/check.do")
	public String check(HttpServletRequest request, String check_note)
			throws IOException {
		String check_state = request.getParameter("check_state");
		String id = request.getParameter("id");
		// Company c = new Company();
		Company c = DictionaryService.findCompany(id);
		c.setId(id);
		c.setCheck_state(check_state);
		this.companyService.update(c);
		return "redirect:companyList.do";
	}

	/**
	 * 功能：管理员查询企业岗位列表 注解请求地址(映射) 李瑶婷20141105* 遍历审核记录 使用@RequestMapping
	 * */

	@SuppressWarnings("unchecked")
	@RequestMapping("admin/positionList.do")
	public ModelAndView positionList(HttpSession session,
			HttpServletRequest request) {

		String check_state = request.getParameter("check_state");
		Map<String, Object> map = new HashMap<String, Object>();
		Position p = new Position();

		/*
		 * //获得企业记录 Company com = (Company) session.getAttribute("com_name");
		 * List<Company> cList = this.companyService.selectList(com);
		 */

		// 按参数获得申请记录
		p.setCheck_state(check_state);
		List<Position> result = this.positionService.selectList(p);
		// 获得未审核的数量
		p.setCheck_state("0");
		int check_count_0 = this.positionService.selectCount(p);
		// 获得已通过的数量
		p.setCheck_state("1");
		int check_count_1 = this.positionService.selectCount(p);
		// 获得未通过数量
		p.setCheck_state("2");
		int check_count_2 = this.positionService.selectCount(p);

		map.put("result", result);

		map.put("check_count_0", check_count_0);
		map.put("check_count_1", check_count_1);
		map.put("check_count_2", check_count_2);
		return new ModelAndView("admin/positionList", map);
	}

	/**
	 * 功能：管理员添加企业岗位注解请求地址(映射)--添加页面 李瑶婷20141105*
	 * 
	 * */
	@RequestMapping("admin/addPosition.do")
	public String addPosition() {

		return "admin/positionAdd";
	}

	/**
	 * 功能：获取该岗位的父id by 王磊 2015年4月2日
	 * */
	@RequestMapping("admin/getParentId.do")
	public String getParentId(HttpServletRequest request,
			HttpServletResponse response) {
		Position p = new Position();
		List<Position> position = null;
		int type;
		String PostType;
		String infor = null;
		response.setCharacterEncoding("utf-8");
		String post_type = request.getParameter("post_type");
		if (post_type.equals("1")) {
			infor = "无父ID";
		} else {
			type = Integer.parseInt(post_type) - 1;
			PostType = String.valueOf(type);
			p.setPost_type(PostType);
			position = this.positionService.selectList(p);
		}
		if (post_type.equals("1")) {
			try {
				response.getWriter().println(infor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<select name='parent_id' id='parent_id' Style='width:100px;'>");
			sb.append("<option value='1'>请选择父ID</option>");
			for (Position pn : position) {
				sb.append("<option value='" + pn.getId() + "'>"
						+ pn.getPost_name() + "</option>");

			}
			sb.append("</select>");
			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 功能：管理员添加企业岗位保存 注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * */
	@RequestMapping("admin/doAddPosition.do")
	public String savePosition(HttpServletRequest request) throws IOException {

		String post_type = request.getParameter("post_type");
		String post_code = request.getParameter("post_code");
		String post_name = request.getParameter("post_name");
		String post_duties = request.getParameter("post_duties");
		String parent_id = request.getParameter("parent_id");
		String check_state = request.getParameter("check_state");
		String check_note = request.getParameter("check_note");
		String state = request.getParameter("state");

		Position position = new Position();
		position.setId(Common.returnUUID());
		position.setPost_type(post_type);
		position.setPost_code(post_code);
		position.setPost_name(post_name);
		position.setPost_duties(post_duties);
		position.setParent_id(parent_id);
		position.setCheck_state(check_state);
		position.setCheck_note(check_note);
		position.setState(state);
		positionService.insert(position);

		return "redirect:positionList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员修改企业岗位注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * */
	@RequestMapping("admin/editPosition.do")
	public ModelAndView editPosition(ModelMap modelMap, String id) {
		Position position = (Position) positionService.selectByID(id);
		modelMap.put("p", position);
		return new ModelAndView("admin/positionEdit", modelMap);
	}

	/**
	 * 功能：管理员编辑企业岗位保存 注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * @ModelAttribute1.注释方法 2.注释一个方法的参数 参考：spring学习之@ModelAttribute运用详解
	 *                       http://blog
	 *                       .csdn.net/li_xiao_ming/article/details/8349115
	 * */
	@RequestMapping("admin/doEditPosition.do")
	public String editPosition(@ModelAttribute("Position") Position position)
			throws IOException {
		positionService.update(position);
		return "redirect:positionList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员删除企业岗位注解请求地址(映射) 李瑶婷20141105*
	 * 
	 * */
	@RequestMapping("admin/deletePosition.do")
	public String deletePosition(String id) {
		Position position = new Position();
		position.setId(id);
		positionService.delete(position);
		return "redirect:positionList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：查看企业岗位 注解请求地址(映射) by李瑶婷 20141105 *
	 * 
	 * */
	@RequestMapping("admin/checkPosition.do")
	public String checkPosition(ModelMap modelMap, String id) {
		Position position = (Position) positionService.selectByID(id);
		modelMap.put("position", position);

		return "admin/checkPosition";
	}

	/**
	 * 功能：审核 注解请求地址(映射) by李瑶婷 20141105*
	 * 
	 * @throws IOException
	 * @throws ServletException
	 *             *
	 * 
	 * */
	@RequestMapping("admin/check11.do")
	public String check11(HttpServletRequest request, String check_note)
			throws IOException {
		String check_state = request.getParameter("check_state");
		String id = request.getParameter("id");
		Position p = new Position();
		p.setId(id);
		p.setCheck_state(check_state);
		this.positionService.update(p);
		return "redirect:positionList.do";
	}

	/**
	 * 功能：管理员查询课程信息 by wtt 20141130 吴敬国2015-6-29 修改 张向杨 2015-12-28
	 * */
	@RequestMapping("admin/courseList.do")
	public ModelAndView courseList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 得到当前用户
		Teacher tea = (Teacher) session.getAttribute("current_user");
		// 得到当前用户的部门id
		String dept_id = tea.getDept_id();
		// 根据部门id查询到部门的 级别（水平 ）
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别

		String xy_id;
		// 判断得到的当前用户的 部门水平
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
		}

		List<String> Grade = this.studentService.getYears();
		map.put("Grade", Grade);

		List<Org> o = this.orgService.selectByXueYuanId(xy_id);

		/*
		 * Courses c = new Courses(); c.setState("1"); c.setOrg_id(xy_id);
		 */
		// 根据课程 查询到
		List<Courses> result = this.courseService.getCourse(xy_id);

		map.put("result", result);
		map.put("orgs", o);
		return new ModelAndView("admin/courseList", map);
	}

	/**
	 * 功能：管理员添加课程注解请求地址(映射)--添加页面 wtt 20141130 邢志武 2015-01-30
	 * 
	 * 修改： 管理员有可能不是本院的的老师，修复了显示全部学院的问题 张向杨 2015-12-28
	 * */
	@RequestMapping("admin/addCourses.do")
	public ModelAndView addCourse(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();

		// 查询数据字典 --根据部门id找到对应部门 然后得到部门的级别
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		// 对部门级别判断
		if (org_level.equals("3")) {
			// 级别为3（系别） 就得到上级院级2 的id
			String org_id = DictionaryService.findOrg(dept_id).getParent_id();

			// 根据学院组织id
			List<Org> result = this.orgService.selectByXueYuanId(org_id);
			map.put("teachers", result);
		} else if (org_level.equals("2")) {
			String org_id = DictionaryService.findOrg(dept_id).getId();

			List<Org> result = this.orgService.selectByXueYuanId(org_id);
			map.put("teachers", result);
		}

		return new ModelAndView("admin/courseAdd", map);
	}

	/**
	 * 功能：管理员添加课程信息 保存 注解请求地址(映射) wtt 20141130
	 * 
	 * */
	@RequestMapping("admin/doAddCourses.do")
	public String saveCourse(HttpServletRequest request) throws IOException {
		String course_code = request.getParameter("course_code");
		String course_name = request.getParameter("course_name");
		String org_id = request.getParameter("org_id");
		String class_object = request.getParameter("class_object");
		int class_hour = Integer.parseInt(request.getParameter("class_hour"));
		String description = request.getParameter("description");

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

		String state = request.getParameter("state");

		Courses c = new Courses();
		c.setId(Common.returnUUID16());
		c.setCourse_code(course_code);
		c.setCourse_name(course_name);
		c.setOrg_id(org_id);
		c.setClass_object(class_object);
		c.setClass_hour(class_hour);
		c.setDescription(description);
		c.setCreate_time(ts);
		c.setState(state);
		courseService.insert(c);

		return "redirect:courseList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员修改课程信息 wtt 20141130 吴敬国 2015-7-6
	 * */
	@RequestMapping("admin/editCourses.do")
	public String editCourse(ModelMap modelMap, String id) {
		Courses course = (Courses) courseService.selectByID(id);
		modelMap.put("course", course);
		return "admin/courseEdit";
	}

	/**
	 * 功能：管理员编辑课程信息 保存 wtt 20141130 吴敬国 2015-7-6
	 * */
	@RequestMapping("admin/doEditCourses.do")
	public String editCourses(@ModelAttribute("course") Courses course)
			throws IOException {
		courseService.update(course);
		return "redirect:courseList.do";
	}

	/**
	 * 功能：管理员删除课程注解请求地址(映射) wtt 20141130 修改：王磊2015年6月22日，删除方法错误修改
	 * 
	 * */
	@RequestMapping("admin/deleteCourses.do")
	public String deleteCourses(String id) {
		Courses c = new Courses();
		c = DictionaryService.findCourses(id);
		courseService.delete(c);
		return "redirect:courseList.do";
	}

	/**
	 * 功能：目录树操作 李瑶婷20141129 // 组织树示例 响应AJAX请求，提供树枝数据
	 * */
	@RequestMapping("*/getBranchData.do")
	@ResponseBody
	public List<TreeNode> getBranchData(HttpServletRequest request,
			HttpServletResponse response) {
		String key = request.getParameter("key"); // 获取当前点击需要展开的节点的key值
		// Integer k = Integer.parseInt(key); //noted byzcg 传int改为传字符串
		List<TreeNode> nodes;
		/*
		 * if (k == 0) { nodes = orgService.getPresidentOrgs(); } else {
		 */
		nodes = orgService.getOrgsByParent(key);
		// }
		for (TreeNode n : nodes) {
			n.setFolder(n.getChildNum() > 0);
			n.setLazy(true);
		}
		return nodes;
	}

	/**
	 * 功能：通告查询列表 注解请求地址(映射) 王磊20141124
	 * 
	 * */
	@RequestMapping("admin/noticeList.do")
	public ModelAndView noticeList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Notice> result = this.noticeService
				.getCollegeNoticeListByteaid(session);
		map.put("result", result);
		return new ModelAndView("admin/noticeList", map);
	}

	/**
	 * 功能：管理员查看详细通告 注解请求地址(映射) by 王磊20141124
	 * 
	 * */
	@RequestMapping("admin/checkNotice.do")
	public String checkPracticeRecord(ModelMap modelMap, String id) {
		Notice notice = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", notice);
		return "admin/noticeDetail";
	}

	/**
	 * 功能：管理员查看修改通告公告 注解请求地址(映射) by 王磊2015年4月1日
	 * 
	 * */
	@RequestMapping("admin/editNotice.do")
	public String editNotice(ModelMap modelMap, String id) {
		Notice notice = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", notice);
		return "admin/noticeEdit";
	}

	/**
	 * 功能：管理员查看修改通告公告 注解请求地址(映射) by 王磊2015年4月1日
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * 
	 * */
	@RequestMapping("admin/doEditNotice.do")
	public String doEditNotice(String id, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Notice notice = (Notice) noticeService.selectByID(id);

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
					fil.setStu_id("");//
					filesService.insert(fil);
					notice.setTemp2(file_id);
				}
			}
		}
		notice.setContent(content);
		notice.setTitle(title);
		this.noticeService.update(notice);
		return "redirect:noticeList.do";
	}

	/**
	 * 功能：管理员添加通告 注解请求地址(映射)-- 添加页面 王磊20141125
	 * 
	 * */
	@RequestMapping("admin/addNotice.do")
	public String addNotice(HttpSession session, ModelMap modelMap) {
		return "admin/noticeAdd";
	}

	/**
	 * 功能：管理员添加通知公告 by王磊 2014
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * 
	 * */
	@RequestMapping("admin/doAddNotice.do")
	public String savaNotice(HttpServletRequest request, HttpSession session)
			throws IllegalStateException, IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String tea_id = tea.getId();
		String tea_code = tea.getTea_code();
		String dept_id = tea.getDept_id();
		String notice_code = "";
		String dept_level = DictionaryService.findOrg(dept_id).getOrg_level();// 得到该管理员是的组织id在系级别还是学院级别
		String xy_id;
		String xy_code;
		if (dept_level.equals("2")) {
			xy_id = dept_id;// 如果组织的等级为2，说明是学院级别，直接把改id传给xy_id
		} else {
			xy_id = DictionaryService.findOrg(dept_id).getParent_id();// 如果级别不为2，说明是系级别，查询该系的parent_id得到学院id
		}
		xy_code = DictionaryService.findOrg(xy_id).getOrg_code();
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String year = nowTime.substring(0, 4);
		// 生成通知编码 王磊 2015年1月30日
		String notice_cur_code = year + xy_code + tea_code;
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
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Notice n = new Notice();

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
					fil.setStu_id("");// 保存创建实践任务的id
					filesService.insert(fil);
					n.setTemp2(file_id);
				}
			}
		}

		n.setContent(content);
		n.setNotice_code(notice_code);
		n.setNotice_type("1");
		n.setOrg_id(xy_id);
		n.setStu_id("");
		n.setTitle(title);
		n.setTea_id(tea_id);
		n.setCreate_time(ts);
		noticeService.insert(n);
		return "redirect:noticeList.do";// 添加成功后重定向到列表页面

	}

	/**
	 * 功能：管理员删除通知公告 王磊 20141210
	 * 
	 * */
	@RequestMapping("admin/deleteNotice.do")
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
	 * 遍历列表 注解请求地址(映射) 吴敬国20140927 邢志武修改 20141225
	 * */
	@RequestMapping("admin/groupsList.do")
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
		if (org_id1 != null) {
			map.put("org_id1", org_id1);
		} else {
			map.put("org_id1", "2");
		}
		map.put("org", org);
		map.put("grade1", grade1);
		// map.put("result", result);
		return new ModelAndView("admin/groupsList", map);
	}

	/**
	 * 功能： 分组管理-导入小组成员 by：王磊 2015年4月17日
	 * 
	 * */
	@RequestMapping("admin/importStudentIds.do")
	public String importStudentIds(String id, HttpSession session) {
		session.setAttribute("groupId", id);
		return "admin/groupMemberStuIdImport";
	}

	/**
	 * 功能：ajax传递小组信息 邢志武2015-01-29 *
	 * 
	 * */
	@RequestMapping("admin/ajaxSerchGroups.do")
	public String ajaxSerchStus(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String org_id = request.getParameter("org_id");
		String grade = request.getParameter("grade");
		// 将 组织id 创建年份放进session 供其他页面调用
		session.setAttribute("org_id", org_id);
		session.setAttribute("grade", grade);
		StringBuffer allGroups = new StringBuffer();
		List<Groups> groups = this.groupsService.selectGroupsBydept_id(org_id,
				grade);
		for (Groups g : groups) {
			String practice_id = DictionaryService.findGroups(g.getId())
					.getPractice_id();
			String state = DictionaryService.findPracticeTask(practice_id)
					.getTask_type();
			if (state.equals("1")) {
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
						.append("<td><input type='button' value='修改成员' onclick=\"updateGroupMembers('"
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
	 * 功能：添加页面 注解请求地址(映射) by 吴敬国20140927 邢志武修改 20141225*
	 * 
	 * */
	@RequestMapping("admin/addGroups.do")
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
		return new ModelAndView("admin/groupsAdd", modelMap);
	}

	/**
	 * 功能：ajax传递 系by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("admin/ajaxXi.do")
	@ResponseBody
	public List ajaxXi(HttpServletRequest request) throws IOException {
		String c = request.getParameter("xueyuan");
		if (c != null) {
			List<Org> XiOrg_Name = orgService.selectXiOrg_Name(c);
			return XiOrg_Name;
		}
		return null;
	}

	/**
	 * 功能：ajax传递 实践任务by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("admin/ajaxShiJian.do")
	@ResponseBody
	public List ajaxShiJian(HttpServletRequest request) throws IOException {
		String data = request.getParameter("data");
		int a = data.indexOf("-");
		String xi = data.substring(0, a);
		String nianji = data.substring(a + 1, data.length());
		if (data != null) {
			List<PracticeTask> ren = practiceTaskService
					.getPracticeTasksGradeByGradeAndDept_id(nianji, xi);
			List<PracticeTask> renwu = new ArrayList<PracticeTask>();
			for (PracticeTask p : ren) {
				if (p.getTask_type().equals("1")) {
					renwu.add(p);
				}
			}

			return renwu;
		}
		return null;
	}

	/**
	 * 功能：ajax查询该组织的成员(映射) by 邢志武20140927 *
	 * 
	 * */
	@RequestMapping("*/ajaxSearchPerson.do")
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
	 * 功能：ajax传递org_level by 邢志武20141211 *
	 * 
	 * */
	@RequestMapping("*/ajaxOrg_level.do")
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
	 * 功能：创建分组信息 by 邢志武
	 * 
	 * */
	@RequestMapping("admin/doAddGroups.do")
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
		group.setGroup_name(group_name);
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

		// 插入小组负责老师
		GroupMembers gm = new GroupMembers();
		gm.setId(Common.returnUUID16());
		String group_id = group.getId();
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
		 * + 1; int x = a.lastIndexOf("+"); String stu_id = a.substring(f, x);
		 * groupMember.setUser_id(stu_id); groupMember.setDuty("学生");
		 * System.out.println("+++++++++++++++++stu_id:" + stu_id);
		 * groupMembersService.insert(groupMember); Student
		 * student=DictionaryService.findStudent(stu_id); student.setId(stu_id);
		 * student.setGroup_id(group_name); studentService.update(student); }
		 * else { // 执行插入教师sql int t = a.lastIndexOf("-") + 1; String tea_id =
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
	@RequestMapping("admin/editGroups.do")
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
		return new ModelAndView("admin/groupsEdit", modelMap);
	}

	/**
	 * 保存修改的分组信息任务 功能： 吴敬国20141123
	 * 
	 * */
	@RequestMapping("admin/doEditGroups.do")
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
	 * 小组管理-修改小组信息及组员信息 邢志武 20150118
	 * 
	 * */
	@RequestMapping("admin/updateGroupMembers.do")
	public ModelAndView groupMembersUpdate(ModelMap modelMap, String id,
			HttpSession session) {
		// 获取负责的学院,通过组织表的联系人id找到负责的学院，限制一个教师只负责一个学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = orgService.selectOrgByTeaId(tea.getId());
		List<Org> xiList = orgService.selectByXyId(college_id);
		List<GroupMembers> groups = this.groupMembersService
				.selectGroupMembersByGroupId(id);
		modelMap.put("xiliebiao", xiList);
		modelMap.put("groups", groups);
		return new ModelAndView("admin/updateGroupMembers", modelMap);
	}

	/**
	 * 小组管理-查看并修改小组 功能 邢志武 20150118
	 * 
	 * */
	@RequestMapping("admin/updateGroups.do")
	public String groupsUpdate(HttpServletRequest request, HttpSession session) {
		String group_id = request.getParameter("group_id");
		Groups g = (Groups) groupsService.selectByID(group_id);
		String groupName = g.getGroup_name();
		// 1获得修改前的组员id
		UserRole userrole = new UserRole();
		String college_id = (String) session.getAttribute("college_id");
		List<Role> roleList = roleService.selectByCollegeId(college_id,
				"ROLE_STUDENT_PRACTICE");
		String role_id = "";
		if (roleList.size() > 0) {
			Role role = roleList.get(0);
			role_id = role.getId();
		}
		List<String> s_id = groupMembersService
				.selectGroupMembersIdByGroupId(group_id);
		for (int i = 0; i < s_id.size(); i++) {
			String groupMemberId = s_id.get(i);
			String sid = DictionaryService.findGroupMembers(groupMemberId)
					.getUser_id();
			// 避免因教师id字典查询出现空指针
			if (sid.length() > 16) {
				Student student = DictionaryService.findStudent(sid);
				student.setGroup_id("无");
				studentService.update(student);

				userrole.setUser_id(sid);
				userrole.setRole_id(role_id);
				userRoleService.delete(userrole);// 删除原先小组中组员的实习学生的角色wjg2015-12-29
			}
		}
		// 2功能：根据group_id 删除分组成员
		groupMembersService.deleteByGroup_id(group_id);

		// 3获取前台修改后的组员
		String groupMembersID = request.getParameter("shuZu");
		String[] allID = groupMembersID.split(",");
		// 4获取修改时间，如果不需要格式,可直接用dt,dt就是当前系统时间
		Date dt = new Date();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
											// // HH:mm:ss格式显示
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		UserRole ur = new UserRole();

		// 5插入组员
		for (int i = 0; i < allID.length; i++) {
			// 创建成员表（分组成员表）
			GroupMembers groupMember = new GroupMembers();
			groupMember.setId(Common.returnUUID16());
			groupMember.setGroup_id(group_id);// 用原来的分组id
			groupMember.setBegin_time(ts);
			groupMember.setState("1");
			groupMember.setEnd_time(null);
			// 获取成员的id
			String oneMemberId = allID[i];// 包含id的字符串
			int f = oneMemberId.lastIndexOf("-");
			String trueMemberId = "";
			if (f == -1) {// 字符串即为id
				trueMemberId = oneMemberId;
			} else {// -的前部分为id
				trueMemberId = oneMemberId.substring(0, f);
			}
			groupMember.setUser_id(trueMemberId);
			// 确定组员的类型，长度16为老师，长度32为学生
			int idLength = trueMemberId.length();

			if (idLength > 16) {
				// 执行插入学生sql
				groupMember.setDuty("学生");
				// System.out.println("+++++++++++++++++stu_id:" + stu_id);
				// 更新学生表的groupid字段
				Student student = DictionaryService.findStudent(trueMemberId);
				student.setGroup_id(groupName);
				studentService.update(student);

				ur.setRole_id(role_id);
				ur.setUser_id(student.getId());
				ur.setTemp1("无");
				userRoleService.insert(ur);// 修改小组成员保存实习学生角色
			} else {
				// 执行插入教师sql

				groupMember.setDuty("老师");
				// System.out.println("+++++++++++++++++tea_id:" + tea_id);
			}
			groupMembersService.insert(groupMember);
		}

		return "redirect:editGroups.do?id=" + group_id;
	}

	// ajax 查询系老师 20150119

	@RequestMapping("admin/ajaxTeacher2.do")
	@ResponseBody
	public List ajaxTeacher2(HttpServletRequest request) throws IOException {
		String c = request.getParameter("xibu");
		if (c != null) {
			List<Teacher> t = teacherService.getTeachersByDeptId(c);
			return t;
		}
		return null;
	}

	// ajax 查询系老师 20150119

	@RequestMapping("admin/ajaxPk_teacher.do")
	public String ajaxPk_teacher(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		String c = request.getParameter("xibu");
		StringBuffer s = new StringBuffer();
		s.append("<option  selected='selected'>请选择</option>");
		if (c != null) {
			List<Teacher> t = teacherService.getTeachersByDeptId(c);
			for (Teacher teacher : t) {
				s.append("<option " + "value=" + teacher.getId() + ">"
						+ teacher.getTrue_name() + "</option>");
			}
			try {
				response.getWriter().println(s.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	// ajax 查询班级 20150119 xzw

	@RequestMapping("admin/ajaxClass.do")
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

	// ajax 查询班级 20150121 xzw

	@RequestMapping("admin/ajaxClass2.do")
	@ResponseBody
	public List ajaxClass2(HttpServletRequest request) throws IOException {
		String tj = request.getParameter("tiaojian");
		int a = tj.indexOf(",");
		String xi = tj.substring(0, a);
		String grade = tj.substring(a + 1, tj.length());
		String org_lev = DictionaryService.findOrg(xi).getOrg_level();
		if (org_lev.equals("2")) {
			List<Org> xueyuan = orgService.selectByXyId(xi);
			return xueyuan;
		} else {
			if (grade != null && xi != null) {

				List<Org> cla = orgService.selectBygradeAndXiId(xi, grade);
				return cla;
			}
		}
		return null;
	}

	// ajax 查询班级成员 20150119 xzw

	@RequestMapping("admin/ajaxPersons.do")
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
	 * ajax 删除小组成员 功能：邢志武 20150118
	 * 
	 * */
	@RequestMapping("admin/deleteGroupMembers.do")
	public String deleteGroupMembers(HttpServletRequest request) {
		String id = request.getParameter("id");
		groupMembersService.delete(id);

		return "";
	}

	/**
	 * 删除小组，小组成员 邢志武20150118
	 * 
	 * */
	@RequestMapping("admin/deleteGroups.do")
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
		Groups g = DictionaryService.findGroups(id);
		g.setId(id);
		g.setDismiss_time(ts);
		groupsService.update(g);

		List<String> gmId = groupMembersService
				.selectGroupMembersIdByGroupId(id);
		for (int i = 0; i < gmId.size(); i++) {
			String gId = gmId.get(i);
			GroupMembers gm = DictionaryService.findGroupMembers(gId);
			gm.setState("2");
			gm.setId(gId);
			groupMembersService.update(gm);
			// 更新学生表
			String stu_id = DictionaryService.findGroupMembers(gId)
					.getUser_id();
			if (stu_id.length() > 16) {
				Student student = DictionaryService.findStudent(stu_id);
				student.setGroup_id("无");
				studentService.update(student);
			}
		}
		String org_id = (String) session.getAttribute("org_id");
		String grade = (String) session.getAttribute("grade");
		return "redirect:groupsList.do";
	}

	/**
	 * 功能：管理员查看实训任务列表 注解请求地址(映射) 孙家胜20141212
	 * 
	 * */
	@RequestMapping("admin/practiceTask.do")
	public ModelAndView practiceTask() {
		Map<String, Object> map = new HashMap<String, Object>();
		PracticeTask p = new PracticeTask();
		List<PracticeTask> lists = this.practiceTaskService.selectList(p);
		map.put("lists", lists);
		return new ModelAndView("admin/practiceTask", map);
	}

	/**
	 * 功能：管理员查询招聘信息 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * 
	 * 邢志武修改 2015-01-22 张向杨 修改 2015-12-27
	 * */
	@RequestMapping("admin/recruitInfoList.do")
	public ModelAndView recruitInfoList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 新建一个招聘对应
		RecruitInfo ri = new RecruitInfo();
		// 根据老师id 得到老师所在的学院id
		String college_id = orgService.getCollegeIdByTeaId(session);
		// 给招聘通知对象的进行 set赋值
		ri.setCollege_id(college_id);
		// 给招聘通知对象的进行 set赋值
		ri.setState("1");
		// 将招聘通知对象作为参数，传递给select 方法 进行过滤
		List<RecruitInfo> result = this.recruitInfoService.select(ri);
		map.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		map.put("result", result);
		return new ModelAndView("admin/recruitInfoList", map);
	}

	/**
	 * 功能：管理员添加招聘信息注解请求地址(映射)--添加页面孙家胜 20141212
	 * 
	 * */
	@RequestMapping("admin/addRecruitInfo.do")
	public ModelAndView addRecruitInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		Company c = new Company();
		List<Company> result1 = this.companyService.selectList(c);
		Position p = new Position();
		List<Position> result2 = this.positionService.selectList(p);
		map.put("rs1", result1);
		map.put("rs2", result2);
		map.put("mapIndustry",
				DictionaryService.getmapIndustryClassificationCode());
		return new ModelAndView("admin/recruitInfoAdd", map);
	}

	/**
	 * 功能：ajax获取招聘信息 邢志武 2015年4月10日
	 * */
	@RequestMapping("admin/serchRec.do")
	public String serchRec(HttpSession session, HttpServletRequest request,
			ModelMap modelMap, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String industry = request.getParameter("industry");
		String org_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(org_id).getOrg_level();
		if (org_level.equals("2")) {
			org_id = tea.getDept_id();
		} else if (org_level.equals("3")) {
			org_id = DictionaryService.findOrg(tea.getDept_id()).getParent_id();
		}
		StringBuffer sb = new StringBuffer();
		List<RecruitInfo> result = companyService.getRecByIndustryAndDept_id(
				industry, org_id);
		for (RecruitInfo d : result) {

			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findCompany(d.getCom_id())
							.getCom_name() + "</td>");
			sb.append("<td>" + d.getPost_id() + "</td>");
			sb.append("<td>" + d.getRecruit_prof() + "</td>");
			sb.append("<td>" + d.getRecruit_desc() + "</td>");
			sb.append("<td>" + d.getRecruit_num() + "</td>");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sb.append("<td>" + dateFormat.format(d.getEffect_time()) + "</td>");
			sb.append("<td>" + dateFormat.format(d.getEnd_time()) + "</td>");
			sb.append("<td>" + dateFormat.format(d.getCreate_time()) + "</td>");
			String state = d.getState();
			String Strstate;
			if (state.equals("1")) {
				Strstate = "有效";
			} else {
				Strstate = "无效";
			}
			sb.append("<td>" + Strstate + "</td>");
			sb.append("	<td><input type='button' value='修改' onclick=\"location.href='editRecruitInfo.do?id="
					+ d.getId() + " \'\">");
			sb.append("	<td><input type='button' value='删除' onclick=\"doDel('"
					+ d.getId() + " ')\">");
			sb.append("</td></tr>");
		}

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：ajax获取招聘信息 邢志武 2015年4月10日
	 * 
	 * @throws UnsupportedEncodingException
	 * */
	@RequestMapping("admin/dimQuery.do")
	public String dimQuery(HttpSession session, HttpServletRequest request,
			ModelMap modelMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String queryType = request.getParameter("queryType");
		String content = "";
		String fcontent = request.getParameter("content");
		content = new String(fcontent.getBytes("iso-8859-1"), "utf-8");
		String org_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(org_id).getOrg_level();
		if (org_level.equals("2")) {
			org_id = tea.getDept_id();
		} else if (org_level.equals("3")) {
			org_id = DictionaryService.findOrg(tea.getDept_id()).getParent_id();
		}
		StringBuffer sb = new StringBuffer();
		List<RecruitInfo> result = new ArrayList<RecruitInfo>();
		if (queryType.equals("1")) {
			result = this.companyService.getRecByPostID(content);
		} else if (queryType.equals("2")) {
			result = this.companyService.getRecByComName(content);
		} else {
			result = this.companyService.getRecByRecrut_prof(content);
		}

		for (RecruitInfo d : result) {

			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findCompany(d.getCom_id())
							.getCom_name() + "</td>");
			sb.append("<td>" + d.getPost_id() + "</td>");
			sb.append("<td>" + d.getRecruit_prof() + "</td>");
			sb.append("<td>" + d.getRecruit_desc() + "</td>");
			sb.append("<td>" + d.getRecruit_num() + "</td>");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sb.append("<td>" + dateFormat.format(d.getEffect_time()) + "</td>");
			sb.append("<td>" + dateFormat.format(d.getEnd_time()) + "</td>");
			sb.append("<td>" + dateFormat.format(d.getCreate_time()) + "</td>");
			String state = d.getState();
			String Strstate;
			if (state.equals("1")) {
				Strstate = "有效";
			} else {
				Strstate = "无效";
			}
			sb.append("<td>" + Strstate + "</td>");
			sb.append("	<td><input type='button' value='修改' onclick=\"location.href='editRecruitInfo.do?id="
					+ d.getId() + " \'\">");
			sb.append("	<td><input type='button' value='删除' onclick=\"doDel('"
					+ d.getId() + " ')\">");
			sb.append("</td></tr>");
		}

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param industry
	 * @return 根据INDUSTRY 显示企业信息 by 邢志武 2015年4月8日
	 */
	@RequestMapping("admin/ajaxgetCompanyByIndustry.do")
	@ResponseBody
	public List<Company> getCompanyByIndustry(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		String industry = request.getParameter("industry");
		List<Company> result = companyService.getCompanyByIndustry(industry);
		return result;
	}

	/**
	 * 功能：管理员添加招聘信息 保存 注解请求地址(映射) 添加页面孙家胜 20141212
	 * 
	 * 邢志武修改 20150118 COLLEGE_ID
	 * */
	@RequestMapping("admin/doAddRecruitInfo.do")
	public String recruitInfoSave(HttpServletRequest request,
			HttpSession session) throws IOException {
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String org_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(org_id).getOrg_level();
		if (org_level.equals("2")) {
			org_id = tea.getDept_id();
		} else if (org_level.equals("3")) {
			org_id = DictionaryService.findOrg(tea.getDept_id()).getParent_id();
		}
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 设置显示格式
		String nowTime = format.format(dt);// 用DateFormat的format()方法在dt中获取并以yyyy/MM/dd
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(nowTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String com_id = request.getParameter("com_id");
		String type1 = "1";
		String type2 = "1";
		String type3 = "1";
		String post_id = request.getParameter("post_id");
		String recruit_prof = request.getParameter("recruit_prof");
		String recruit_desc = request.getParameter("recruit_desc");
		Integer recruit_num = Integer.parseInt(request
				.getParameter("recruit_num"));
		String state = request.getParameter("state");
		String effect_time = request.getParameter("recruit_eff");
		String end_time = request.getParameter("recruit_end");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(effect_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		try {
			ts2 = new Timestamp(format3.parse(end_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		RecruitInfo r = new RecruitInfo();

		r.setEffect_time(ts1);
		r.setEnd_time(ts2);
		r.setId(Common.returnUUID());
		r.setCreate_time(ts);
		r.setCom_id(com_id);
		r.setType1(type1);
		r.setType2(type2);
		r.setType3(type3);
		r.setCollege_id(org_id);
		r.setPost_id(post_id);
		r.setRecruit_prof(recruit_prof);
		r.setRecruit_desc(recruit_desc);
		r.setRecruit_num(recruit_num);
		r.setState(state);
		recruitInfoService.insert(r);

		return "redirect:recruitInfoList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员修改招聘信息 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * 
	 * 邢志武修改20150121
	 * */
	@RequestMapping("admin/editRecruitInfo.do")
	public String recruitInfoEdit(ModelMap modelMap, String id) {
		RecruitInfo r = (RecruitInfo) recruitInfoService.selectByID(id);
		Position p = new Position();
		List<Position> result2 = this.positionService.selectList(p);
		modelMap.put("r2", result2);
		modelMap.put("recruitInfo", r);
		return "admin/recruitInfoEdit";
	}

	/**
	 * 功能：管理员编辑招聘信息保存 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * 邢志武修改
	 * 
	 * */
	@RequestMapping("admin/doEditRecruitInfo.do")
	public String recruitInfoedit(HttpServletRequest request)
			throws IOException {
		String id = request.getParameter("id");
		String com_id = request.getParameter("com_id");
		String type1 = request.getParameter("1");
		String type2 = request.getParameter("1");
		String type3 = request.getParameter("1");
		String post_id = request.getParameter("post_id");
		String recruit_prof = request.getParameter("recruit_prof");
		String recruit_desc = request.getParameter("recruit_desc");
		Integer recruit_num = Integer.parseInt(request
				.getParameter("recruit_num"));

		String state = request.getParameter("state");
		String effect_time = request.getParameter("effect_time");
		String end_time = request.getParameter("end_time");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = new Timestamp(format2.parse(effect_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		try {
			ts2 = new Timestamp(format3.parse(end_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		RecruitInfo r = new RecruitInfo();
		r.setId(id);
		r.setEffect_time(ts1);
		r.setEnd_time(ts2);
		r.setCom_id(com_id);
		r.setType1(type1);
		r.setType2(type2);
		r.setType3(type3);
		r.setPost_id(post_id);
		r.setRecruit_prof(recruit_prof);
		r.setRecruit_desc(recruit_desc);
		r.setRecruit_num(recruit_num);
		r.setState(state);
		recruitInfoService.update(r);
		return "redirect:recruitInfoList.do"; // 添加成功后重定向到列表页面
	}

	/**
	 * 功能：管理员删除招聘信息 注解请求地址(映射) 孙家胜 20141212
	 * 
	 * */
	@RequestMapping("admin/deleteRecruitInfo.do")
	public String recruitInfoDelete(String id) {
		recruitInfoService.delete(id);
		return "redirect:recruitInfoList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 实习任务管理 by吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/practicetaskList.do")
	public ModelAndView practiceTaskList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 先判断是实习还是实训
		String type = request.getParameter("type");
		if (type == null) {
			type = (String) session.getAttribute("type");
		}
		String task_type;
		if (type.equalsIgnoreCase("shixi")) {
			task_type = "1";
		} else {
			task_type = "2";
		}
		session.setAttribute("type", type);
		session.setAttribute("task_type", task_type);
		// 取得一些默认值

		List<Org> departmentlist;
		List<PracticeTask> practicetaskList;
		departmentlist = orgService.getOrgDeptByCollegeId((String) session
				.getAttribute("college_id"));// 获取系及学院自身
		practicetaskList = this.practiceTaskService
				.selectPracticeTasksByDeptIdAndGrade(
						(String) session.getAttribute("college_id"),
						Common.getDefaultYear(), task_type);
		String dept_name = departmentlist.get(0).getOrg_name();// 第一次得到第一个部门的部门名
		session.setAttribute("grade", Common.getDefaultYear());
		session.setAttribute("dept_id",
				(String) session.getAttribute("college_id"));

		session.setAttribute("dept_id", session.getAttribute("college_id"));

		map.put("type", type);
		map.put("dept_name", dept_name);
		map.put("practicetaskList", practicetaskList);
		map.put("grade", Common.getDefaultYear());
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/practiceTaskList", map);
	}

	/**
	 * 实习任务管理-学院管理员查询该学院每个系的老师负责的实践任务 by吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/backpracticetaskList.do")
	public ModelAndView practicetaskListBack(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String task_type = (String) session.getAttribute("task_type");
		String grade = (String) session.getAttribute("grade");
		String dept_id = (String) session.getAttribute("dept_id");
		String dept_name = DictionaryService.findOrg(dept_id).getOrg_name();
		String xy_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist;
		List<PracticeTask> practicetaskList;
		departmentlist = orgService.getOrgDeptByCollegeId(xy_id);// 获取系及学院自身
		practicetaskList = this.practiceTaskService
				.selectPracticeTasksByDeptIdAndGrade(dept_id, grade, task_type);
		map.put("dept_name", dept_name);
		map.put("practicetaskList", practicetaskList);
		map.put("grade", grade);
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/practiceTaskList", map);
	}

	/**
	 * 功能：实习任务管理-ajax传递 根据年级和系部得到实习 实训任务 by 吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/ajaxPracticeTask.do")
	public String practiceTaskAjax(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String task_type = (String) session.getAttribute("task_type");
		String gradeAndDept = request.getParameter("gradeAndDeptid");
		int a = gradeAndDept.indexOf("-");
		int grade1 = Integer.parseInt(gradeAndDept.substring(0, a));
		String grade2 = String.valueOf(grade1);
		String dept_id = gradeAndDept.substring(a + 1, gradeAndDept.length());
		session.setAttribute("grade", grade2);
		session.setAttribute("dept_id", dept_id);
		if (gradeAndDept != null) {
			List<PracticeTask> pa = this.practiceTaskService
					.selectPracticeTasksByDeptIdAndGrade(dept_id, grade2,
							task_type);
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy/MM/dd");
			for (PracticeTask ts : pa) {
				sb.append("<tr>");
				Date d = ts.getBegin_time();
				Date e = ts.getEnd_time();
				Date f = ts.getCreate_time();
				sb.append("<td>" + ts.getTask_name() + "</td>");
				sb.append("<td>" + dateFm.format(f) + "</td>");
				sb.append("<td>" + dateFm.format(d) + "</td>");// 开始时间
				sb.append("<td>" + dateFm.format(e) + "</td>");
				sb.append("<td>" + ts.getTask_desc() + "</td>");
				sb.append("<td>" + ts.getTask_place() + "</td>");
				sb.append("<td><input type='button' value='任务小组详情' onclick=\"seeGroup('"
						+ ts.getId() + "')\"></td>");
				sb.append("<td><input type='button' value='修改' onclick=\"doEdit('"
						+ ts.getId()
						+ " ')\">&nbsp;&nbsp;<input type='button' value='删除' onclick=\"checkGroups('"
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
	 * 功能：实习任务管理-添加实践任务页面 by 吴敬国2015-5-15 增加获取老师的部门id
	 * */
	@RequestMapping("admin/addPracticeTask.do")
	public ModelAndView practiceTaskAdd(HttpSession session, ModelMap modelMap) {
		String task_type = (String) session.getAttribute("task_type");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_name = DictionaryService.findOrg(dept_id).getOrg_name();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		List<Org> practiceTask_orgList = orgService.selectByXueYuanId(dept_id);
		modelMap.put("practiceTask_orgList", practiceTask_orgList);
		modelMap.put("org_name", org_name);
		modelMap.put("dept_id", dept_id);
		modelMap.put("task_type", task_type);
		if (task_type.equalsIgnoreCase("1")) {
			return new ModelAndView("admin/practiceTaskAdd", modelMap);
		} else {
			return new ModelAndView("admin/practiceTrainAdd", modelMap);
		}

	}

	/**
	 * 功能：实习任务管理-保存实践教学任务 by吴敬国2015-5-15 丁乐晓 2015-11-16
	 * */
	@RequestMapping("admin/doAddPracticeTask.do")
	public String practiceTaskSave(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute("PracticeTask") PracticeTask practiceTask)
			throws IllegalStateException, IOException {
		String task_name1 = request.getParameter("task_name");
		String task_namereplenish = request.getParameter("task_namereplenish");
		String task_namereplenish1 = request
				.getParameter("task_namereplenish1");
		String task_name2 = task_name1.concat(task_namereplenish);
		String task_name;
		if (task_namereplenish1 != null) {
			task_name = task_name2.concat(task_namereplenish1);
		} else {
			task_name = task_name2;
		}
		PracticeTask pra = new PracticeTask();
		String tea_id = request.getParameter("tea_id");// 获得负责教师的id
		String tea_code1 = DictionaryService.findTeacher(tea_id).getTea_code();
		String task_dept = DictionaryService.findTeacher(tea_id).getDept_id();
		session.setAttribute("dept_id", task_dept);
		String grade = request.getParameter("grade");
		session.setAttribute("grade", grade);
		String begin_Time = request.getParameter("begin_Time");
		String end_Time = request.getParameter("end_Time");
		String task_Place = request.getParameter("task_Place");
		if (task_Place == null) {
			task_Place = "校外";
		}
		String task_Desc = request.getParameter("task_Desc");
		String task_Type = (String) session.getAttribute("task_type");
		String task_type;
		String task_Name;
		if (task_Type.equalsIgnoreCase("1")) {
			task_type = "xwsx";
			task_Name = "实习";
		} else {
			task_type = "zzsx";
			task_Name = "实训";
		}
		session.setAttribute("task_name", task_name);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String createTeaId = tea.getId();// 保存创建实践任务的教师的id

		String new_practice_code = practiceTaskService.getPracticeCode(grade,
				tea_id, task_type, tea_code1);
		String time = DateService.formatNowTimeforUpload();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file.getSize() != 0) {
					String file_type = "Task";
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					// String suffix =
					// file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					String file_name = file.getOriginalFilename();
					// String filePosition = "WEB-INF/uploadedfiles/" +
					// file_type+ "/" + file_type+"_"+new_practice_code +
					// suffix;
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + new_practice_code + "_" + file_name;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					// String file_path = project_path + filePosition;
					// String file_Position =file_type+"_"+new_practice_code +
					// suffix;
					/* String file_name = file.getOriginalFilename(); */
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
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
					fil.setStu_id(createTeaId);// 保存创建实践任务的教师的id
					filesService.insert(fil);
					pra.setFile_id(file_id);
				}
			}
		}

		String pra_id = Common.returnUUID();
		// 实习任务属性
		pra.setPractice_code(new_practice_code);
		pra.setTask_name(task_name.trim());
		pra.setTea_id(tea_id);
		pra.setId(pra_id);
		Map<String, Timestamp> map = DateService.StringTimeTurnTimestamp1(
				begin_Time, end_Time);
		pra.setBegin_time(map.get("begin"));
		pra.setCreate_time(DateService.getNowTimeTimestamp());
		pra.setEnd_time(map.get("end"));
		pra.setTask_desc(task_Desc);
		pra.setTask_place(task_Place);
		pra.setTask_type(task_Type);
		pra.setScope(task_dept);
		pra.setGrade(grade);
		practiceTaskService.insert(pra);
		UserRole ur = new UserRole();
		String college_id = (String) session.getAttribute("college_id");
		List<Role> roleList = roleService.selectByCollegeId(college_id,
				"ROLE_TEACHER_PRACTICE");
		if (roleList.size() > 0) {
			Role role = roleList.get(0);
			ur.setRole_id(role.getId());
		}
		ur.setUser_id(tea_id);
		userRoleService.insert(ur);// 给教师添加实习教师角色

		// 如果是实训任务，也一并创建分组 2015-6-25
		if (task_Type.equalsIgnoreCase("2")) {
			Groups group = new Groups();
			group.setId(Common.returnUUID16());
			group.setGroup_name(task_name.trim());
			group.setPurpose("无");
			group.setDescription("无");
			group.setCreate_time(DateService.getNowTimeTimestamp());
			group.setTea_id(Common.getOneTeaId(session));// 小组的创建老师，即管理员的id
			group.setPractice_id(pra_id);
			groupsService.insert(group);
			String group_id = group.getId();
			// 插入小组负责老师
			GroupMembers gm = new GroupMembers();
			gm.setId(Common.returnUUID16());
			gm.setGroup_id(group_id);
			gm.setBegin_time(DateService.getNowTimeTimestamp());
			gm.setState("1");
			gm.setEnd_time(null);
			gm.setUser_id(tea_id);// 这里填写的老师是实践任务负责的老师
			gm.setDuty("老师");
			groupMembersService.insert(gm);
		}
		String re = "";
		if (task_type.equalsIgnoreCase("xwsx")) {
			re = "shixi";
		} else if (task_type.equals("zzsx")) {
			re = "shixun";
		}
		return "redirect:backpracticetaskList.do";
	}

	/**
	 * 功能：对实践任务名称在数据库是否重复做验证 吴敬国2015-5-15
	 * */
	@RequestMapping("admin/checkTaskNameRepeat.do")
	public String practiceCheckTaskNameRepeat(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		String content = request.getParameter("content");
		String cont = "";
		try {
			cont = URLDecoder.decode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int b;
		String infor = "";// 说明没有重复
		b = this.practiceTaskService.selectByTaskName(cont.trim());
		if (b != 0) {
			infor = "此任务名已用";
		}
		try {
			response.getWriter().println(infor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 功能：根据实践任务id查询是否有分组 吴敬国2015-5-15
	 * */
	@RequestMapping("admin/checkGroups.do")
	@ResponseBody
	public String practiceCheckGroups(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String pra_id = request.getParameter("pra_id");
		int b;
		String infor = "";
		b = this.groupsService.selectCountByPraid(pra_id);
		if (b != 0) {
			Groups delGroups = groupsService.selectGroupsByPraid(pra_id);
			String group_name = delGroups.getGroup_name();
			infor = "2";
		} else {
			infor = "1";
		}
		return infor;
	}

	/**
	 * 功能：教师修改实践任务 吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/editPracticeTask.do")
	public ModelAndView practiceTaskEdit(ModelMap modelMap, String id,
			HttpSession session) {
		String task_type = (String) session.getAttribute("task_type");
		PracticeTask pt = practiceTaskService.selectByID(id);
		String practicefileid = pt.getFile_id();
		String practice_code = pt.getPractice_code();
		session.setAttribute("practicefileid", practicefileid);
		session.setAttribute("practice_code", practice_code);
		if (practicefileid != null) {
			Files file = filesService.selectByID(practicefileid);
			modelMap.put("file", file);
		}
		modelMap.put("task_type", task_type);
		modelMap.put("pt", pt);
		return new ModelAndView("admin/practiceTaskEdit", modelMap);
	}

	/**
	 * 功能：教师编实践任务保存 by 吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/doEditPracticeTask.do")
	public String practiceTaskedit(HttpServletRequest request,
			HttpSession session) throws IllegalStateException, IOException {
		String practicefileid = (String) session.getAttribute("practicefileid");// 取到实践任务文件的id
		String practice_code = (String) session.getAttribute("practice_code");// 取到实践任务文件的编码
		String task_type = (String) session.getAttribute("task_type");
		String id = request.getParameter("id");
		PracticeTask practicetask = practiceTaskService.selectByID(id);
		String task_name = request.getParameter("task_name");
		/* String task_name = (String) session.getAttribute("task_name"); */
		/* String task_place = request.getParameter("task_place"); */
		/* String task_type=request.getParameter("task_type"); */
		// String grade = request.getParameter("grade");年级不能修改
		// session.setAttribute("grade", grade);
		String task_desc = request.getParameter("task_desc");
		String begin_time = request.getParameter("begin_time");
		String end_time = request.getParameter("end_time");
		String create_time = request.getParameter("create_time");

		Map<String, Timestamp> map = DateService.StringTimeTurnTimestamp1(
				begin_time, end_time);
		practicetask.setBegin_time(map.get("begin"));
		practicetask.setEnd_time(map.get("end"));
		practicetask.setCreate_time(DateService.getNowTimeTimestamp());
		practicetask.setTask_desc(task_desc);
		practicetask.setTask_name(task_name.trim());
		practicetask.setTask_type(task_type);
		// practicetask.setId(id);
		// practicetask.setGrade(grade);
		String time = DateService.formatNowTimeforUpload();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file.getSize() != 0) {
					String file_type = "Task";
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					// String suffix =
					// file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					// String file_Position = file_type+"_" +
					// file_type+"_"+practice_code + suffix;
					// String filePosition = "WEB-INF/uploadedfiles/" +
					// file_type+ "/" + file_type+"_"+practice_code + suffix;
					String file_name = file.getOriginalFilename();
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + practice_code + "_" + file_name;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					Timestamp d = new Timestamp(System.currentTimeMillis());
					if (practicefileid != null) {
						Files f = filesService.selectByID(practicefileid);
						f.setUpload_time(d);
						f.setFile_size(filesize);
						f.setFile_name(file_name);
						f.setPosition(filePosition);
						filesService.update(f);
						practicetask.setFile_id(f.getId());
					} else {
						Files files = new Files();
						String fid = Common.returnUUID();
						files.setId(fid);
						files.setFile_name(file_name);
						files.setFile_size(filesize);
						files.setFile_type(file_type);
						files.setUpload_time(d);
						files.setStu_id(" ");
						files.setPosition(filePosition);
						filesService.insert(files);
						practicetask.setFile_id(fid);
					}
					file.transferTo(new File(file_path));
				}
			}
		}
		practiceTaskService.update(practicetask);
		String re = "";
		if (task_type.equalsIgnoreCase("XWSX")) {
			re = "shixi";
		} else if (task_type.equals("ZZSX")) {
			re = "shixun";
		}
		return "redirect:backpracticetaskList.do"; // 修改成功后重定向到列表页面
	}

	/**
	 * 功能：教师删除实践任务更新状态 吴敬国2015-5-15
	 * 
	 * */
	@RequestMapping("admin/deletePracticeTask.do")
	public String practiceTaskDelete(String id, HttpServletRequest request,
			HttpSession session) {
		if (id != null) {// 删除数据库文件表中的信息，并且把路径下的文件一并删除
			String file_id = DictionaryService.findPracticeTask(id)
					.getFile_id();
			if (file_id != null) {
				Files file = DictionaryService.findFiles(file_id);
				filesService.delete(file);
				String position = file.getPosition();
				String real_path = Constants.FILE_ROUTE;
				String file_path = real_path + position;
				File fie = new File(file_path); // 路径为文件且不为空则进行删除
				if (fie.isFile() && fie.exists()) {
					fie.delete();
				}
			}
		}
		PracticeTask pt = practiceTaskService.selectByID(id);
		String task_name = pt.getTask_name();
		String newtask_name = task_name + "-无效";
		pt.setTask_name(newtask_name);
		pt.setState("2");
		practiceTaskService.update(pt);
		/*
		 * Date dat=new Date(); DateFormat format1 = new
		 * SimpleDateFormat("yyyy-MM-dd"); Timestamp ts = new
		 * Timestamp(System.currentTimeMillis());
		 */
		Timestamp dismiss_time = new Timestamp(System.currentTimeMillis());
		int b = this.groupsService.selectCountByPraid(id);
		if (b != 0) {
			Groups delGroups = groupsService.selectGroupsByPraid(id);
			String delGroup_id = delGroups.getId();
			List<Student> stuList = studentService
					.getStuListByGroupID(delGroup_id);
			for (Student s : stuList) {
				s.setGroup_id("无");// 这个地方貌似有问题
				studentService.update(s);
			}// 没有删除小组表中的信息，看是否应该删除
			delGroups.setDismiss_time(dismiss_time);
			groupsService.update(delGroups);

			List<GroupMembers> gmList = groupMembersService
					.selectGroupMembersByGroupId(delGroup_id);
			for (GroupMembers gm : gmList) {
				gm.setState("2");
				groupMembersService.update(gm);
			}
		}
		return "redirect:backpracticetaskList.do";
	}

	/**
	 * 功能：添加实践任务页面 by 吴敬国2015-5-15 增加获取老师的部门id 2015-03-14 邢志武 获该老师部门及下级系部
	 * */
	@RequestMapping("admin/seeGroup.do")
	public ModelAndView seeGroup(ModelMap modelMap, String id) {
		Groups group = new Groups();
		group.setPractice_id(id);
		List<Groups> groupList = groupsService.selectList(group);
		if (groupList.size() != 0) {
			Groups groups = groupList.get(0);
			List<GroupMembers> groupMember = this.groupMembersService
					.selectGroupMembersByGroupId(groups.getId());
			int teaSize = this.groupMembersService.getTeachersSize(groups
					.getId());
			int stusSize = this.groupMembersService.getStudentsSize(groups
					.getId());
			modelMap.put("teaSize", teaSize);
			modelMap.put("stusSize", stusSize);
			modelMap.put("groups", groups);
			modelMap.put("groupMember", groupMember);
		}
		return new ModelAndView("admin/groupsDetails", modelMap);
	}

	@RequestMapping("admin/downloadFile.do")
	public String download(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, String file_id) throws Exception {
		String project_path = session.getServletContext().getRealPath("/")
				+ "/";
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		// String ctxPath = "D:"+"\\"+"uploadedfiles\\";
		Files file1 = filesService.selectByID(file_id);
		String thesisPosition = file1.getPosition();
		filesService.downloadfile(thesisPosition, ctxPath, request, response);
		return null;
	}

	/*
	 * //ajax 获取系班级 邢志武 2015-01-21
	 * 
	 * @RequestMapping("admin/getClassList.do") public String
	 * getClassList(HttpServletResponse response, String org_id) throws
	 * IOException { String s = "";
	 * response.setContentType("text/html;charset=UTF-8"); if (org_id == null) {
	 * s = "org_id == null"; } else { //写一个查询班级id的方法 List<String>
	 * claIdList=orgService.selectClassIdByXiId(org_id); for(int
	 * i=0;i<claIdList.size();i++){ String claId=claIdList.get(i); Org o =
	 * DictionaryService.findOrg(claId); if (claId != null) { s +=
	 * "<table border='1' width='1000'><tr><th width='100'>组织编码</th><th width='100'>组织名称</th><th width='110'>组织级别</th><th width='100'>联系人</th><th width='100'>主管</th><th width='100'>副主管</th><th width='100'>联系电话</th><th width='80'>上级部门</th><th width='50'>状态</th><th width='50'>操作</th></tr>"
	 * ; s += "<tr><td>"+ o.getOrg_code()+"</td>"; s +=
	 * "<a href='editOrg.do?id='"+o.getId()+">"+o.getOrg_name()+"</a>"; s
	 * +="<td>"+ o.getOrg_level()+"</td>"; s += "<td>"+ o.getContacts()+"</td>";
	 * s += "<td>"+ o.getDirector()+"</td>"; s += "<td>"+
	 * o.getVice_director()+"</td>"; s +="<td>"+ o.getParent_id()+"</td>"; s
	 * +="<td>"+ o.getParent_id()+"</td>"; s +=
	 * "<td><input type='button' value='删除' onclick='doDel('"
	 * +o.getId()+"');></td></tr>"; } else { s = "Class can not found!"; }
	 * response.getWriter().print(s); } }
	 * 
	 * return null; }
	 */

	/**
	 * 就业材料汇总报表类.
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("admin/getA_Graduationmaterials_0.do")
	public ModelAndView ReportGraduationmaterials(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist;
		departmentlist = orgService.getOrgDeptByCollegeId(college_id);// 获取系及学院自身
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/graduationMaterialReport", map);
	}

	/**
	 * 就业材料汇总报表类.
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/getA_Graduationmaterials.do", method = RequestMethod.POST)
	public ModelAndView ReportGraduationmaterialsDo(ModelAndView modelAndView,
			HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String dept_id = request.getParameter("dept_id");
		String entry_year = request.getParameter("entry_year");
		String task_type = "1";
		Map<String, String> param = new HashMap<String, String>();
		param.put("dept_id", dept_id);
		param.put("entry_year", entry_year);
		param.put("task_type", task_type);
		// model 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportCountGraduationMaterial> data = reportDetailGraduationMaterialService
				.getReportData(param);
		model.put("data", data);
		// A_Graduationmaterials 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("graduationmaterialsReport", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 就业材料汇总报表 导出ecxel.
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/getA_GraduationmaterialsExcel.do", method = RequestMethod.POST)
	public ModelAndView doA_GraduationmaterialsExcel(ModelAndView modelAndView,
			HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dept_id = request.getParameter("dept_id");
		String entry_year = request.getParameter("entry_year");
		String task_type = "1";
		Map<String, String> param = new HashMap<String, String>();
		param.put("dept_id", dept_id);
		param.put("entry_year", entry_year);
		param.put("task_type", task_type);
		// model 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportCountGraduationMaterial> gradu = reportDetailGraduationMaterialService
				.getReportData(param);
		model.put("data", gradu);
		// A_Graduationmaterials 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("graduationmaterialsReportExcel", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 实训安排报表管理 .
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("admin/getTrainDetailReport.do")
	public ModelAndView getTrainDetailReport(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist;
		departmentlist = orgService.getOrgDeptByCollegeId(college_id);// 获取系及学院自身
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/trainDetailReport", map);
	}

	/**
	 * 实训安排报表管理 .
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/doGetTrainDetail.do", method = RequestMethod.POST)
	public ModelAndView doA_TrainDetail(ModelAndView modelAndView,
			HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String org_id = request.getParameter("org_id");// 部门id
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		String term = request.getParameter("term");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");

		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getOrgReportData(
				org_id, term, begin_time, end_time);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		dataAndReportTitle.put("yearAndTerm", yearAndTerm);
		dataAndReportTitle.put("data", gradu);
		dataAndReportTitle.put("report_title", report_title);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("trainDetail_adminAndLeader",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 实训安排报表管理，导出ecxel.
	 * 
	 * @author 张超2015年1月28日
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/doGetTrainDetailExcel.do", method = RequestMethod.POST)
	public ModelAndView doA_TrainDetail_Excel(ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String org_id = request.getParameter("org_id");
		// 得到某学年某学期对应的起始和截止时间
		String year = request.getParameter("year");
		String term = request.getParameter("term");
		Map<String, String> BeginTimeAndEndTime = Common
				.getBeginTimeAndEndTime(year, term);
		String begin_time = BeginTimeAndEndTime.get("begin_time");
		String end_time = BeginTimeAndEndTime.get("end_time");
		/*
		 * Map<String, String> mapYearAndOrgIdAndTerm = new HashMap<String,
		 * String>(); mapYearAndOrgIdAndTerm.put("org_id", org_id);
		 * mapYearAndOrgIdAndTerm.put("begin_time", begin_time);
		 * mapYearAndOrgIdAndTerm.put("end_time", end_time);
		 * mapYearAndOrgIdAndTerm.put("term", term);
		 */
		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		List<ReportTrainDetail> gradu = trainDetailService.getOrgReportData(
				org_id, term, begin_time, end_time);
		String org_name = DictionaryService.findOrg(org_id).getOrg_name();
		System.out.print(org_name);
		String report_title = org_name + "实训安排";
		String yearAndTerm = year + "学年第" + term + "学期";
		dataAndReportTitle.put("yearAndTerm", yearAndTerm);
		dataAndReportTitle.put("data", gradu);
		dataAndReportTitle.put("report_title", report_title);
		// trainDetail_adminAndLeader_excel 是视图逻辑名，在 /WEB-INF/jasper-views.xml
		// 中定义
		response.setContentType("APPLICATION/vnd.ms-excel");
		modelAndView = new ModelAndView("trainDetail_adminAndLeader_excel",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 管理员首页.
	 * 
	 * @author 郑春光 2015-2-9
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("admin/index.do")
	public ModelAndView index(HttpSession session, String current_role_selected) {
		if (current_role_selected != null) {
			session.setAttribute("current_role_selected", current_role_selected);
		}
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院,通过组织表的联系人id找到负责的学院，限制一个教师只负责一个学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(college_id).getOrg_level();
		if (org_level.equals("3")) {
			college_id = DictionaryService.findOrg(college_id).getParent_id();
		}
		// 存入session 供其他模块使用 保存管理员模块的登陆教师所在学院的id号
		session.setAttribute("college_id", college_id);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		// 获取单个学院各项状态人数
		mapParam.put("grade", grade);
		mapParam.put("deptId", college_id);
		List<StuGraStateCount> listStuStateCount = studentService
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cm", jsonData);
		map.put("grade", grade);
		map.put("college_name", college_name);

		// 设置当前权限为管理员 by桑博
		CommonSession.setUserRole(session, CommonSession.roleAdmin);
		return new ModelAndView("admin/index", map);
	}

	/**
	 * 功能：转到系部实习状态图表
	 * 
	 * */
	@RequestMapping("admin/toPracticeStateChartDept.do")
	public ModelAndView toPracticeStateChartDept(HttpSession session,
			HttpServletRequest request) {
		// 获取状态编码
		String curStateCode = request.getParameter("stateCode");
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String college_id = Common.getCollegeId(dept_id);
		// 各系各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = orgService.selectByXyId(college_id);
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = studentService
					.getXiStuStateCountByGradeAndOrg(mapParam);
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
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		String college_Name = DictionaryService.findOrg(college_id)
				.getOrg_name();
		map.put("stateName", stateName);
		map.put("college_name", college_Name);
		map.put("cm", jsonData);
		map.put("grade", grade);
		return new ModelAndView("admin/practiceStateChartDept", map);
	}

	// 管理员查看教师指导记录功能 邢志武 by 2015-03-23
	@RequestMapping("admin/diectRecordList.do")
	public ModelAndView directrecordList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher t = (Teacher) session.getAttribute("current_user");
		String dept_id = t.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		List<Org> org = this.orgService.selectByXueYuanId(dept_id);
		map.put("org", org);

		return new ModelAndView("admin/diectRecordList", map);
	}

	@RequestMapping("admin/editdirectrecord.do")
	public String editdirectrecord(ModelMap modelMap, String id) {

		DirectRecord d = (DirectRecord) directRecordService.selectByID(id);

		modelMap.put("directrecord", d);

		return "admin/directrecordEdit";
	}

	/**
	 * 功能：ajax获取教师指导记录 邢志武2015-03-24
	 * 
	 * */
	@RequestMapping("admin/ajaxTeasDieRecs.do")
	public String ajaxTeasDieRecs(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String tea_id = request.getParameter("tea_id");
		StringBuffer sb = new StringBuffer();
		List<DirectRecord> result = this.directRecordService
				.getTeaDirecRecsByTeaId(tea_id);
		for (DirectRecord d : result) {
			sb.append("<tr>");
			sb.append("<td>"
					+ DictionaryService.findPracticeTask(d.getPractice_id())
							.getTask_name() + "</td>");
			sb.append("<td><a href='editdirectrecord.do?id=" + d.getId() + "'>"
					+ d.getTitle() + "</a></td>");
			sb.append("<td>" + d.getDirect_time() + "</td>");
			sb.append("<td>" + d.getDirect_place() + "</td>");
			sb.append("<td>"
					+ DictionaryService.findGroups(d.getOrg_id())
							.getGroup_name() + "</td>");
			String stuts = d.getStu_id();
			StringBuffer bf = new StringBuffer();
			String[] sts = stuts.split(",");
			for (int i = 0; i < sts.length; i++) {
				String stuName = DictionaryService.findStudent(sts[i])
						.getTrue_name();
				bf.append(stuName + "、");
			}
			bf.deleteCharAt(bf.length() - 1);
			sb.append("<td>" + bf + "</td>");
			sb.append("<td>" + d.getDirect_desc() + "</td>");
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
	 * 
	 * @param session
	 * @return 管理员查看签到情况 by 邢志武 2015/4/1
	 */
	@RequestMapping("admin/BmapPie.do")
	public ModelAndView ceshi(HttpSession session) {
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		// 获取本周该学院签到学生的比例
		double SignPro = practiceTaskService.getCollegeDaySignPro(dept_id,
				grade);
		// 处理小数点保留位数问题
		String falseRate = new java.text.DecimalFormat("#.00").format(SignPro);
		SignPro = Double.parseDouble(falseRate);
		// 获取本周该学院未签到学生的比例
		double unSignPro = 1 - SignPro;
		String SignProString = Double.toString(SignPro);
		String unSignProString = Double.toString(unSignPro);
		ChartData BmapIchar = new ChartData();
		BmapIchar.setName("1");
		BmapIchar.setText("本周签到的人");
		BmapIchar.setValue(SignProString);
		BmapIchar.setColor("#CCFFFF");
		ChartData BmapIchar2 = new ChartData();
		BmapIchar2.setName("2");
		BmapIchar2.setValue(unSignProString);
		BmapIchar2.setColor("#FFFF99");
		BmapIchar2.setText("本周未签到的人");
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		BmapIcharList.add(BmapIchar2);
		BmapIcharList.add(BmapIchar);
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonDataNew();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptName", deptName);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/BmapPie", map);

	}

	/**
	 * 功能：转到系部签到状态图表 邢志武 2015/4/1
	 * 
	 * */
	@RequestMapping("admin/bMapSignState.do")
	public ModelAndView tobMapSignState(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = Common.getDefaultYear();
		String stateName = request.getParameter("stateName");
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
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
		List<Org> departments = orgService.selectByXyId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			double scale = practiceTaskService.getDepartmentDaySignPro(org_id,
					grade);
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - scale;
				sb.append(deptName + grade + "学生本周未签到情况");
			}
			// 处理小数点保留位数问题
			String falseRate = new java.text.DecimalFormat("#.00")
					.format(scale);
			scale = Double.parseDouble(falseRate);
			sb.append(deptName + grade + "学生本周签到情况 ");
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
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/bMapSignState", map);
	}

	/**
	 * 
	 * @param session
	 * @return 管理员查看成绩情况 by 邢志武 2015/4/1
	 */
	@RequestMapping("admin/scoreSearch.do")
	public ModelAndView toScoreSearch(HttpSession session) {
		// 默认年份
		String grade = Common.getDefaultYear();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(dept_id).getOrg_level();
		if (org_level.equals("3")) {
			dept_id = DictionaryService.findOrg(dept_id).getParent_id();
		}
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		// 计算某系校外实习成绩优秀率
		Map<String, Double> collegeStusSizeStuScoreProportion = scoreService
				.getCollegeStusSizeStuScoreProportion(dept_id, grade);
		double notPassPro = collegeStusSizeStuScoreProportion
				.get("notPassProportion");
		double passPro = collegeStusSizeStuScoreProportion
				.get("passProportion");
		double wellPro = collegeStusSizeStuScoreProportion
				.get("wellProportion");
		double excellentPro = collegeStusSizeStuScoreProportion
				.get("excellentProportion");
		String notPassProp = Double.toString(notPassPro);
		String passProp = Double.toString(passPro);
		String wellProp = Double.toString(wellPro);
		String excellentProp = Double.toString(excellentPro);
		ChartData BmapIchar = new ChartData();
		BmapIchar.setName("1");
		BmapIchar.setText("未及格");
		BmapIchar.setValue(notPassProp);
		BmapIchar.setColor("#FFFFCC");
		ChartData BmapIchar2 = new ChartData();
		BmapIchar2.setName("2");
		BmapIchar2.setText("及格");
		BmapIchar2.setValue(passProp);
		BmapIchar2.setColor("#CCFFFF");
		ChartData BmapIchar3 = new ChartData();
		BmapIchar3.setName("3");
		BmapIchar3.setText("良好");
		BmapIchar3.setValue(wellProp);
		BmapIchar3.setColor("#99CCCC");
		ChartData BmapIchar4 = new ChartData();
		BmapIchar4.setName("4");
		BmapIchar4.setText("优秀");
		BmapIchar4.setValue(excellentProp);
		BmapIchar4.setColor("#FFFF99");
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		BmapIcharList.add(BmapIchar);
		BmapIcharList.add(BmapIchar2);
		BmapIcharList.add(BmapIchar3);
		BmapIcharList.add(BmapIchar4);
		ChartModel mode = new ChartModel();
		mode.setListData(BmapIcharList);
		String json = mode.getJsonDataNew();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptName", deptName);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/scoreSearch", map);

	}

	/**
	 * 功能：转到系部成绩比例状态图表 邢志武 2015年4月21日
	 * */
	@RequestMapping("admin/toScoreSeach.do")
	public ModelAndView toScoreSeach(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = Common.getDefaultYear();
		String stateName = request.getParameter("stateName");
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
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
		List<Org> departments = orgService.selectByXyId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			// 某系校外实习成绩优秀率
			Map<String, Double> departmentStuScoreProportion = this.scoreService
					.getDepartmentStuScoreProportion(org_id, grade);
			// 判断点击的那部分饼图
			double doublePro = 0;
			if (stateName.equals("1")) {
				doublePro = departmentStuScoreProportion
						.get("notPassProportion") * 100;
				sb.append(deptName + grade + "学生成绩未及格比例");
			} else if (stateName.equals("2")) {
				doublePro = departmentStuScoreProportion.get("passProportion") * 100;
				sb.append(deptName + grade + "学生成绩及格比例");
			} else if (stateName.equals("3")) {
				doublePro = departmentStuScoreProportion.get("wellProportion") * 100;
				sb.append(deptName + grade + "学生成绩良好比例");
			} else {
				doublePro = departmentStuScoreProportion
						.get("excellentProportion") * 100;
				sb.append(deptName + grade + "学生成绩优秀比例");
			}
			String SignProString = Double.toString(doublePro);
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
		String title = sb.toString().substring(0, 19);
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/deparScoreSeach", map);
	}

	/*
	 * 功能：判断org表组织编码是否重复 孙磊 2015/4/21
	 */
	@RequestMapping("admin/checkOrgCode.do")
	public String ajaxCheckOrgCode(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String org_code = request.getParameter("org_code");
		String checkCode = orgService.checkOrgCode(org_code);
		if (checkCode != null) {
			response.getWriter().print("1");
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return 验证小组是否重复
	 * @throws IOException
	 *             2015年5月10日 14:37:50 邢志武
	 */
	@RequestMapping("admin/checkGroupName.do")
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
	 * 
	 * @param request
	 * @param session
	 * @param response
	 * @return
	 * @throws IOException
	 *             判断课程名是否重复 邢志武 2015年5月28日 08:56:38
	 */
	@RequestMapping("admin/checkCourseName.do")
	public String checkCourseName(HttpServletRequest request,
			HttpSession session, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String course_name = request.getParameter("course_name");
		// 解决乱码问题
		course_name = new String(course_name.getBytes("iso-8859-1"), "UTF-8");
		Courses c = new Courses();
		c.setCourse_name(course_name);
		int trueOrFlase = courseService.selectCount(c);
		StringBuffer sb = new StringBuffer();
		if (trueOrFlase == 1) {
			sb.append("<font color='red'size='2'>课程名已重复不可用! X</font>");
		} else if (course_name.equals("")) {
			sb.append("<font color='red'size='2'>课程名不能为空! X</font>");
		} else {
			sb.append("<font color='green'size='2'>课程名可用! √</font>");
		}

		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * 功能：判断org表组织名称是否重复 孙磊 2015/5/21
	 * 
	 * @RequestMapping("admin/checkOrgName.do") public String
	 * ajaxCheckOrgName(HttpSession session, HttpServletRequest
	 * request,HttpServletResponse response) throws
	 * UnsupportedEncodingException{ String org_name =
	 * (String)request.getParameter("org_name"); org_name = new
	 * String(org_name.getBytes("iso-8859-1"),"utf-8"); String checkName =
	 * orgService.CheckOrgName(org_name); if(checkName != ""){
	 * response.getWriter().print("1"); } return null; }
	 */
	/**
	 * 功能：实习状态成绩报表 by 吴敬国 2015年5月31日
	 * 
	 * @return
	 */
	@RequestMapping("admin/getAdminStateReport.do")
	public ModelAndView getAdminStateReport(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");
		List<Org> departmentlist = orgService.getOrgDeptByCollegeId(college_id);
		map.put("departmentlist", departmentlist);
		return new ModelAndView("admin/studentStateReport", map);
	}

	/**
	 * 功能： 获取报表数据并显示在报表中 by 吴敬国 2015年5月31日
	 * 
	 * @return
	 */
	@RequestMapping(value = "admin/doGetstudentStateReport.do", method = RequestMethod.POST)
	public ModelAndView doGetstudentStateReport(ModelAndView modelAndView,
			HttpServletRequest request, HttpSession session) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String year = request.getParameter("year");
		String dept_id = request.getParameter("dept_id");
		List<ReportStuStateGrade> data = new ArrayList();
		// 写时间的循环，造测试数据，报表
		List<String> timelist = new ArrayList<String>();
		timelist.add("2015-07-01 22:12:52");
		timelist.add("2015-07-30 11:49:45");
		timelist.add("2015-08-30 11:49:45");
		timelist.add("2015-09-30 11:49:45");
		/*
		 * timelist.add("2015-07-30 11:49:45");
		 * timelist.add("2015-09-01 11:49:45");
		 * timelist.add("2015-09-30 11:49:45");
		 * timelist.add("2015-10-30 11:49:45");
		 * timelist.add("2015-11-30 11:49:45");
		 * timelist.add("2015-12-30 11:49:45");
		 */

		if (dept_id.equalsIgnoreCase("全院")) {
			String xy_id = (String) session.getAttribute("college_id");
			List<Org> departmentlist = orgService.getOrgDeptByCollegeId(xy_id);
			for (String time : timelist) {
				for (Org org : departmentlist) {
					ReportStuStateGrade reportStuStateGrade1 = new ReportStuStateGrade();
					String deptid = org.getId();
					String deptName = org.getOrg_name();
					Timestamp d = new Timestamp(System.currentTimeMillis());
					d = Timestamp.valueOf(time);
					String tsStr = "";
					DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					tsStr = sdf.format(d);
					int avgScore = reportStudentStateService.getOnedeptGrade(
							deptid, tsStr);
					reportStuStateGrade1.setTime(tsStr);
					reportStuStateGrade1.setDeptortea_name(deptName);
					reportStuStateGrade1.setAvg_score(avgScore);
					data.add(reportStuStateGrade1);
				}
			}
		} else {
			List<Teacher> tea = teacherService.getTeachersByDeptId(dept_id);
			for (String time : timelist) {
				for (Teacher t : tea) {
					ReportStuStateGrade reportStuStateGrade1 = new ReportStuStateGrade();
					String tea_id = t.getId();
					String tea_name = t.getTrue_name();
					Timestamp d = new Timestamp(System.currentTimeMillis());
					d = Timestamp.valueOf(time);
					String tsStr = "";
					DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					tsStr = sdf.format(d);
					int avgScore = reportStudentStateService.getTeaGrade(
							tea_id, tsStr);
					reportStuStateGrade1.setTime(tsStr);
					reportStuStateGrade1.setDeptortea_name(tea_name);
					reportStuStateGrade1.setAvg_score(avgScore);
					data.add(reportStuStateGrade1);
				}
			}
		}
		// parameterMap 是应用程序的数据模型
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("data", data);
		// pdfReport 是视图逻辑名，在 /WEB-INF/jasper-views.xml 中定义
		modelAndView = new ModelAndView("stugrade_state", model);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能：导入模板下载 注解请求地址(映射) sl
	 * 
	 * */
	@RequestMapping("admin/templetdownload.do")
	public String templetdownload(HttpServletRequest request,
			HttpSession session, HttpServletResponse response) throws Exception {
		String project_path = request.getSession().getServletContext()
				.getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		String file_type = "Helps";
		String file_type1 = "AdminUser";
		String a = file_type + "/" + file_type1 + "/" + "导入模板.rar";
		filesService.downloadfile(a, ctxPath, request, response);
		return null;
	}

	/**
	 * 
	 * @param session
	 * @param request
	 * @return 查看学院任务完成度 xzw 2015年7月22日 09:55:14
	 */
	@RequestMapping("admin/taskAccomplishAll.do")
	public ModelAndView toTaskAccomplishAll(HttpSession session,
			HttpServletRequest request) {
		/* String notPassProp = Double.toString(notPassPro); */
		// 默认年份
		String grade = Common.getDefaultYear();
		// String grade ="2013";
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
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
		Map<String, Double> CollegeTaskAccomplish = scoreService
				.getCollegeTaskAccomplish(dept_id, grade);
		// Map<String, Double>
		// CollegeTaskAccomplish=scoreService.getDepartementTaskAccomplish("rjx",
		// grade);
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
		map.put("title", deptName + grade + "级" + "任务完成情况");
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/taskAccomplishAll", map);
	}

	/**
	 * 查看各系任务完成度 邢志武2015年7月22日 09:55:02
	 */
	@RequestMapping("admin/taskAccomplishDepartments.do")
	public ModelAndView taskAccomplishDepartments(HttpSession session,
			HttpServletRequest request) {
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
		Teacher tea = (Teacher) session.getAttribute("current_user");
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
		List<Org> departments = orgService.selectByXyId(dept_id);
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			// 某系校外实习成绩优秀率
			Map<String, Double> departementTaskAccomplish = this.scoreService
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
		map.put("title", title);
		map.put("grade", grade);
		map.put("cm", json);
		return new ModelAndView("admin/taskAccomplishDepartment", map);
	}

	/**
	 * 查看学生所在公司的情况 吴敬国 2015-9-25
	 */
	@RequestMapping("admin/getstuCompanyReport.do")
	public ModelAndView getstuCompanyReport(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * String college_id = (String) session.getAttribute("college_id");
		 * List<Org> departmentlist; departmentlist =
		 * orgService.getOrgDeptByCollegeId(college_id);// 获取系及学院自身
		 * map.put("departmentlist", departmentlist);
		 */
		return new ModelAndView("admin/stuCompanyReport", map);
	}

	/**
	 * 实习学生就业情况，导出ecxel.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-9-26
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/getstuCompanyReportExcel.do", method = RequestMethod.POST)
	public ModelAndView getstuCompanyReportExcel(ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		String entry_year = request.getParameter("entry_year");
		List<ReportStuCompany> reportStuCompanyList = studentService
				.getStuCompanyReport(entry_year);
		dataAndReportTitle.put("title", "2013级学生实习公司统计");
		dataAndReportTitle.put("reportStuCompanyList", reportStuCompanyList);
		/*
		 * trainDetail_adminAndLeader_excel 是视图逻辑名，在
		 * /WEB-INF/jasper-views.xml中定义
		 */
		response.setContentType("APPLICATION/vnd.ms-excel");
		modelAndView = new ModelAndView("reportStuCompany_excel",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 实习学生就业情况，导出pdf.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-9-26
	 * @since 3.0
	 * 
	 */
	@RequestMapping(value = "admin/getstuCompanyReportPdf.do", method = RequestMethod.POST)
	public ModelAndView getstuCompanyReportPdf(ModelAndView modelAndView,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Object> dataAndReportTitle = new HashMap<String, Object>();
		String entry_year = request.getParameter("entry_year");
		List<ReportStuCompany> reportStuCompanyList = studentService
				.getStuCompanyReport(entry_year);
		dataAndReportTitle.put("title", "2013级学生实习公司统计");
		dataAndReportTitle.put("reportStuCompanyList", reportStuCompanyList);
		/*
		 * trainDetail_adminAndLeader_excel 是视图逻辑名，在
		 * /WEB-INF/jasper-views.xml中定义
		 */
		/* response.setContentType("APPLICATION/vnd.ms-excel"); */
		modelAndView = new ModelAndView("reportStuCompany_pdf",
				dataAndReportTitle);
		// 返回视图和模型的混合对象
		return modelAndView;
	}

	/**
	 * 功能：修改组织管理的保存按钮!!!!!!!!!!
	 * 
	 * @param org
	 * @return 列表页面
	 */
	@RequestMapping(value = "admin/doEditOrg.do")
	public String DoEditOrg(HttpServletRequest request, HttpSession session)
			throws IOException {
		String id = request.getParameter("id");
		String org_name = request.getParameter("org_name");
		String contacts = request.getParameter("contacts");
		String contactss = request.getParameter("contactss");// 班主任
		String counsellorss = request.getParameter("counsellorss");// 辅导员
		String phone = request.getParameter("phone");
		String parent_id = request.getParameter("par_dept");
		String begin_time = request.getParameter("begin_Time");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = new Timestamp(format.parse(begin_time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Org org = new Org();
		org = orgService.selectByID(id);
		org.setBegin_time(ts);
		org.setOrg_name(org_name);
		if (contacts == null) {
			org.setContacts(contactss);// 班主任的ID
		} else {
			org.setContacts(contacts);
		}
		org.setCounselor_id(counsellorss);// 辅导员的ID
		org.setHead_tea_id(contactss);
		org.setDirector("");
		org.setVice_director("");
		org.setPhone(phone);
		org.setParent_id(parent_id);
		org.setState("1");

		orgService.update(org);
		
		

		return "redirect:backOrgList.do";// 修改成功后重定向到列表页面
	}

	@RequestMapping("admin/closeRecruitInfo.do")
	public String delNotice(ModelMap modelMap, String id) {
		// 根据id获取到选中的招聘信息
		RecruitInfo r = (RecruitInfo) recruitInfoService.selectByID(id);
		// 设置招聘信息的状态为无效 2----代表无效
		r.setState("2");
		// 更新数据库
		recruitInfoService.update(r);
		// 通过return 后面加上链接 跳转到指定页面
		return "redirect:recruitInfoList.do";
	}

	// 微信修改服务器地址
	@RequestMapping("admin/editwxurl.do")
	public ModelAndView editwxurl() {
		String url = Constants.ServerURL;
		return new ModelAndView("admin/weixinUrlEdit", "url", url);
	}

	@RequestMapping("admin/changewxurl.do")
	public ModelAndView savewxurl(HttpServletRequest request) {
		String url = request.getParameter("url");
		Constants.ServerURL = url;
		return new ModelAndView("admin/weixinUrlEdit", "url",
				Constants.ServerURL);
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
	@RequestMapping("admin/ajaxIndex.do")
	public String ajaxIndex2(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		// 默认年份
		String grade = request.getParameter("year");
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = tea.getDept_id();
		String org_level = DictionaryService.findOrg(college_id).getOrg_level();
		if (org_level.equals("3")) {
			college_id = DictionaryService.findOrg(college_id).getParent_id();
		}
		// 存入session 供其他模块使用 保存管理员模块的登陆教师所在学院的id号
		session.setAttribute("college_id", college_id);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		// 获取单个学院各项状态人数
		mapParam.put("grade", grade);
		mapParam.put("deptId", college_id);
		List<StuGraStateCount> listStuStateCount = studentService
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
		try {
			response.getWriter().println(jsonData);
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

	@RequestMapping("admin/menuByUserAjax.do")
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

	@RequestMapping("admin/updateCustomMenuAjax.do")
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
		int i = userRoleService.updatecustomMenuIds(userRole);

	}

	/**
	 * 显示用户自定义菜单
	 * 
	 * @Description
	 * @author 吴宝
	 */

	@RequestMapping("admin/updateCustomMenuShow.do")
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
	 * 功能：查看老师的课程 时间：２０１６－０３－1９ 作者：鲁雪艳
	 */
	@RequestMapping("admin/showTeaCourses.do")
	public String showTeaCourses(ModelMap modelMap) {

		return "admin/showTeaCourses";
	}

	/**
	 * 功能：导入老师课程 时间：２０１６－０３－1９ 作者：鲁雪艳
	 */
	@RequestMapping("admin/courseArrangementImport.do")
	public String courseArrangementImport(ModelMap modelMap, String year,
			String semester, HttpSession session) {
		if (year != null)
			session.setAttribute("importTeaCourseYear", year);
		if (semester != null)
			session.setAttribute("importTeaCourseSemester", semester);
		return "admin/courseArrangementImport";
	}

	/**
	 * 功能：添加老师课程 老师的课程 时间：２０１６－０３－1９ 作者：鲁雪艳
	 */
	@RequestMapping("admin/courseArrangementAdd.do")
	public String courseArrangementAdd(ModelMap modelMap, HttpSession session) {

		// 想得到 学院的系部 或者学院的 对象
		String college_id = (String) session.getAttribute("college_id");

		List<Org> orgList = orgService.selectByXueYuanId(college_id);

		modelMap.put("orgList", orgList);
		return "admin/courseArrangeAdd";
	}

	/**
	 * 功能：保存单个添加老师的课程 时间：２０１６－０３－1９ 作者：鲁雪艳
	 */
	@RequestMapping("admin/saveCourseArrangement.do")
	public String saveCourseArrangement(ModelMap modelMap,
			HttpServletRequest request, HttpSession session) {
		String year = request.getParameter("year");
		String semester = request.getParameter("semester");
		String courseId = request.getParameter("courseId");
		String teaId = request.getParameter("teaId");
		Teacher tea = (Teacher) session.getAttribute("current_user");// 获得当前用户
		// 如不为空 则保存数据库
		if (year != null && semester != null && courseId != null
				&& teaId != null) {
			TeaCourses teaCourses = new TeaCourses();
			teaCourses.setYear(year);
			teaCourses.setSemester(semester);
			teaCourses.setCourses_id(courseId);
			teaCourses.setTea_id(teaId);
			teaCourses.setCreate_people(tea.getId());
			teaCoursesService.insert(teaCourses);
		}

		return "redirect:showTeaCourses.do";
	}

	/**
	 * 功能：ajax得到老师的课程 时间：２０１６－０３－1９ 作者：鲁雪艳
	 */

	@RequestMapping("admin/getTeaCourse.do")
	public String ajaxGetTeaCourse(String year, String semester,
			HttpServletResponse response, HttpServletRequest request)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		TeaCourses teaCourses = new TeaCourses();
		teaCourses.setSemester(semester);
		teaCourses.setYear(year);
		// 查询得到 过滤条件的课程老师
		List<TeaCourses> teaCoursesList = teaCoursesService
				.selectList(teaCourses);
		// 创建 stringbuild对象 用于存放 向前台传动的数据
		StringBuilder stringBuilder = new StringBuilder();
		// 若得到的数据不为空 则要遍历得到想要的 数据
		if (teaCoursesList != null) {
			// 用于 显示序号
			int i = 0;
			for (TeaCourses teaCourses2 : teaCoursesList) {

				stringBuilder.append("<tr>");
				stringBuilder.append("<td>" + (++i) + "</td>");
				Courses course = (Courses) courseService.selectById(teaCourses2
						.getCourses_id());
				if (course != null) {
					stringBuilder.append("<td>" + course.getCourse_code()
							+ "</td>");
					stringBuilder.append("<td>" + course.getCourse_name()
							+ "</td>");
				} else {
					stringBuilder.append("<td>" + "无" + "</td>");
					stringBuilder.append("<td>" + "无" + "</td>");

				}
				if (teaCourses2.getCourses_type() == null)
					stringBuilder.append("<td>" + "无" + "</td>");
				else
					stringBuilder.append("<td>" + teaCourses2.getCourses_type()
							+ "</td>");
				Teacher teacher = DictionaryService.findTeacher(teaCourses2
						.getTea_id());
				if (teacher != null) {
					stringBuilder.append("<td>" + teacher.getTrue_name()
							+ "</td>");
					stringBuilder.append("<td>" + teacher.getTea_code()
							+ "</td>");

				} else {
					stringBuilder.append("<td>" + "无" + "</td>");
					stringBuilder.append("<td>" + "无" + "</td>");
				}
				stringBuilder.append("</tr>");

			}

			try {
				PrintWriter printWriter = response.getWriter();
				printWriter.println(stringBuilder.toString());
				printWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 功能：ajax传递小组信息 邢志武2015-01-29 *
	 * 
	 * */
	@RequestMapping("admin/ajaxCourseByOrg.do")
	public String ajaxCourseByOrg(HttpSession session,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String org_id = request.getParameter("org_id");
		String flag = request.getParameter("flag");
		Courses cour = new Courses();
		cour.setOrg_id(org_id);
		StringBuffer allCourse = new StringBuffer();
		List<Courses> courses = this.courseService.selectList(cour);
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置显示格式
		if (flag != null || "flag".equals(flag)) {
			allCourse.append("<option  selected='selected'>请选择</option>");
			for (Courses c : courses) {
				allCourse.append("<option " + "value=" + c.getId() + ">"
						+ c.getCourse_name() + "</option>");
			}
		} else {
			for (Courses c : courses) {
				String createTime = format.format(c.getCreate_time());
				allCourse.append("<tr>");
				allCourse.append("<td>" + c.getCourse_code() + "</td>");
				allCourse.append("<td>" + c.getCourse_name() + "</td>");
				allCourse.append("<td>"
						+ DictionaryService.findOrg(c.getOrg_id())
								.getOrg_name() + "</td>");
				allCourse.append("<td>" + c.getClass_object() + "</td>");
				allCourse.append("<td>" + c.getClass_hour() + "</td>");
				allCourse.append("<td>" + c.getDescription() + "</td>");
				allCourse.append("<td>" + createTime + "</td>");
				String state = c.getState();
				String stateName = "有效";
				if (state.equals("2")) {
					stateName = "无效";
				}
				allCourse.append("<td>" + stateName + "</td>");
				allCourse
						.append("<td><input type='button' value='修改' onclick=\"doEdit('"
								+ c.getId() + "')\"></td>");
				allCourse
						.append("<td><input type='button' value='删除' onclick=\"doDel('"
								+ c.getId() + "')\"></td>");
			}
		}
		try {
			response.getWriter().println(allCourse.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 学生会社团管理 列表 李秋林 师杰 2016-3-12
	 */
	@RequestMapping("admin/associationList.do")
	public ModelAndView associationList(HttpSession session,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		return new ModelAndView("/admin/associationList", map);
	}

	/**
	 * 功能：查询所有的学生会社团列表 by 师杰 李秋林 2016-3-12
	 * */
	@RequestMapping("admin/ajaxGetAssociation.do")
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
	@RequestMapping("admin/addAssociation.do")
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
	@RequestMapping("admin/doAddAss.do")
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
		ass.setSa_stu_id("0024445e62f7b5100554c6281586a569");
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
	@RequestMapping("admin/editAsso.do")
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

	@RequestMapping("admin/doEditSubmit.do")
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

	/**
	 * 功能：管理员重置教师密码 周睿20160329
	 * */

	@RequestMapping("admin/resetPassword.do")
	public String resetPassword(HttpServletRequest request, HttpSession session)
			throws IOException {
		String tea_id = request.getParameter("id");
		Teacher tea = (Teacher) teacherService.selectByID(tea_id);
		tea.setLogin_pass(tea.getLogin_name());
		teacherService.update(tea);
		return "redirect:teacherList.do";
	}
	
	/**
	 * 功能：学生管理-删除所有学生 by syj 20160330
	 * */
	@RequestMapping("admin/deleteAllStudentImport.do")
	public String studentAllDelete(HttpServletRequest request,HttpSession session,String class_id1) {
		Map<String, Object> map = new HashMap<String, Object>();
		String student_class_id =request.getParameter("id") ;//获取班级id
		Org org = DictionaryService.findOrg(student_class_id);//通过班级id获得对应的班级
		List<Student> stulist = studentService.getStudentsByClassId(student_class_id);//通过班级id得到相应的学生
		//遍历
		for (Student stu :stulist) {
			stu.setState("0");
			studentService.update(stu);
		}
		
		return "redirect:studentImportList.do"; // 删除成功后重定向到列表页面
	}

	/**
	 * 功能：管理员设置参数  周睿20160415
	 * */

	@RequestMapping("admin/setWorkload.do")
	public String setWorkload(HttpServletRequest request, HttpSession session)
			throws IOException {
		String tea_id = request.getParameter("id");
		Teacher tea = (Teacher) teacherService.selectByID(tea_id);
		tea.setLogin_pass(tea.getLogin_name());
		teacherService.update(tea);
		return "admin/setWorkload";
	}
	
}
