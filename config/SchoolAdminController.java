package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.swing.JOptionPane;

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
import com.sict.entity.TeaStu;
import com.sict.entity.Teacher;
import com.sict.entity.TemporaryComplish;
import com.sict.entity.TrainDetail;
import com.sict.entity.TreeNode;
import com.sict.entity.UserRole;
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
import com.sict.service.TeaStuService;
import com.sict.service.TeacherService;
import com.sict.service.TrainDetailService;
import com.sict.service.UserRoleService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.DateService;
import com.sict.util.HttpRequest;
import com.sict.util.Ichartjs_Color;

/**
 * 功能：系统管理员控制器 使用 @Controller 注释 by郑春光20140910 *
 * 
 */
@Controller
public class SchoolAdminController {
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
	 * 注入sysRoleMenuService wjg
	 * 
	 * */
	@Resource(name = "sysRoleMenuService")
	private SysRoleMenuService sysRoleMenuService;
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
	 * 注入sysMenuService 
	 * by吴敬国2015-12-21
	 * 
	 * */
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	

	/**
	 * 注入ParamService by丁乐晓20160115 *
	 * 
	 * */
	@Resource(name = "paramService")
	private ParamService paramService;
	
	// 定义返回类型
	String ret = "";

	private Object o;
	private int pageSizeConstant = Constants.pageSize; // 获取常量分页页面大小

	
	/**
	 * 学校管理员首页.
	 * @author 吴敬国
	 * @createDate 2016-3-18
	 * @since 3.0
	 */
	@RequestMapping("schooladmin/index.do")
	public String index(HttpSession session) {
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院,通过组织表的联系人id找到负责的学院，限制一个教师只负责一个学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = tea.getDept_id();
		// 设置当前权限为管理员 by桑博
		CommonSession.setUserRole(session, CommonSession.roleAdmin);
		return "schooladmin/index";
	}
	
	/**
	 * 初始化学院.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/initCollege.do")
	public String initCollege(HttpSession session){
		Role role=new Role();
		List<Role> roleList=roleService.selectList(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleList", roleList);
		//return new ModelAndView("sysadmin/initCollege", map);
		return "sysadmin/initCollege";
	}*/
	
	
	
	/**
	 * 初始化学院.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/doinitCollege.do")
	public String initCollegeSave(HttpSession session,HttpServletRequest request)  {
		String college_name = request.getParameter("college_name");
		String college_code = request.getParameter("college_code");
		String tea_name = request.getParameter("tea_name");
		String tea_code = request.getParameter("tea_code");
		String tea_sex = request.getParameter("tea_sex");
		String tea_phone = request.getParameter("tea_phone");
		//String org_code = request.getParameter("org_code");
		String teaId=Common.returnUUID16();
		String collegeId=Common.returnUUID();
		//添加组织
		Org o = new Org();
		o.setOrg_level("2");
		o.setId(collegeId);
		o.setBegin_time(DateService.getNowTimeTimestamp());
		o.setContacts(teaId);
		o.setOrg_code(college_code);
		o.setOrg_name(college_name);
		o.setDirector("无");
		o.setPhone(tea_phone);
		o.setParent_id("szxy");//如果以后要是扩展到其他学校，需要修改
		orgService.insert(o);
		//添加管理员信息
		Teacher tea=new Teacher();
		tea.setId(teaId);
		tea.setTea_code(tea_code);
		tea.setTrue_name(tea_name);
		tea.setLogin_pass(tea_code);
		tea.setSex(tea_sex);
		tea.setPhone(tea_phone);
		tea.setDept_id(collegeId);
		tea.setTea_type("1");
		tea.setState("1");
		teacherService.insert(tea);
		//添加学院管理员角色
		String collegeAdminRole="ROLE_ADMIN_COLLEGE_SZ"+college_code;
		Role role=new Role();
		role.setId(collegeAdminRole);
		role.setRole_code(collegeAdminRole);
		role.setRole_name(college_name+"管理员");
		role.setRole_desc(college_name+"管理员");
		role.setRole_type("1");
		role.setCreate_time(DateService.getNowTimeTimestamp());
		role.setCreate_tea("1");
		role.setSchool_id("szxy");
		role.setCollege_id(collegeId);
		role.setTemp1("3");
		roleService.insert(role);
		//添加管理员权限
		UserRole userroleadmin=new UserRole();
		userroleadmin.setRole_id(collegeAdminRole);
		userroleadmin.setUser_id(teaId);
		userRoleService.insert(userroleadmin);
		sysRoleMenuService.saveMenuForRole(collegeAdminRole);//给学院管理员角色分配模板中的菜单
		//添加管理员权限
		UserRole userrole=new UserRole();
		userrole.setRole_id("ROLE_ADMIN");
		userrole.setUser_id(teaId);
		userRoleService.insert(userrole);
		UserRole userrole1=new UserRole();
		userrole1.setRole_id("ROLE_TEACHER");
		userrole1.setUser_id(teaId);
		userRoleService.insert(userrole1);
		return "redirect:index.do"; 
	}*/
	
	/**
	 * 初始化学院角色及角色菜单.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/initCollegeRole.do")
	public ModelAndView initCollegeRole(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		Org o = new Org();
		o.setOrg_level("2");
		List<Org> orgList = orgService.selectList(o);
		map.put("orgList", orgList);
		return new ModelAndView("sysadmin/initCollegeRole", map);
	}*/
	
	/**
	 * 初始化学院角色及角色菜单.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/doinitCollegeRole.do")
	public String  initCollegeRoleSave(HttpSession session,HttpServletRequest request){
		String college_id = request.getParameter("college_id");
		roleService.saveBasicRoleByCollegeId(college_id);
		return "redirect:index.do"; 
	}*/
		/**
		 * 角色模板列表.
		 * @author 吴敬国
		 * @createDate 2015-12-21
		 * @since 1.0
		 */
		/*@RequestMapping("sysadmin/roleTemplateList.do")
		public ModelAndView roleTemplateList(HttpSession session)  {
			Role role=new Role();
			role.setTemp1("2");
			List<Role> roleTemplateList=roleService.selectList(role);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleTemplateList", roleTemplateList);
			return new ModelAndView("sysadmin/roleTemplateList", map);
		}*/
		
		/**
		 * 添加角色模板.
		 * @author 吴敬国
		 * @createDate 2015-12-21
		 * @since 1.0
		 */
		/*@RequestMapping("sysadmin/addRoleTemplate.do")
		public ModelAndView RoleTemplateadd(HttpSession session) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Org> collegeList=orgService.getAllColleges();
			map.put("collegeList", collegeList);
			return new ModelAndView("sysadmin/roleTemplateAdd", map);
		}*/
	/**
	 * 角色列表.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/roleList.do")
	public ModelAndView roleList(HttpSession session)  {
		Role role=new Role();
		List<Role> roleList=roleService.selectList(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleList", roleList);
		return new ModelAndView("sysadmin/roleList", map);
	}*/
	/**
	 * 添加角色.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/addRole.do")
	public ModelAndView Roleadd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> collegeList=orgService.getAllColleges();
		map.put("collegeList", collegeList);
		return new ModelAndView("sysadmin/roleAdd", map);
	}*/
	/**
	 * 保存角色.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/doAddRole.do")
	public String RoledoAdd(HttpSession session,@ModelAttribute("role") Role role)  {
		Role ro=(Role) roleService.addRoleCode(role);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		ro.setCreate_tea(tea.getId());
		ro.setCreate_time(DateService.getNowTimeTimestamp());
		roleService.insert(ro);
		return "redirect:roleList.do"; 
	}*/

	/**
	 * 学院列表
	 * 
	 * @author 周睿
	 * @createDate 2015-12-26
	 * @since 1.0
	 */
	/*@RequestMapping("sysadmin/orgList.do")
	public ModelAndView orgList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Org o = new Org();
		o.setOrg_level("2");
		List<Org> orgList = orgService.selectList(o);
		map.put("orgList", orgList);
		return new ModelAndView("sysadmin/sysOrgList", map);
	}*/


/**
	 * 功能：系统管理员——参数管理by丁乐晓2016o115
	 * 
	 * @param
	 * @return 列表页面
	 */
/*	@RequestMapping("sysadmin/paramList.do")
	public ModelAndView paramList(HttpServletRequest request,
			HttpServletResponse response, String xi_id, HttpSession session,
			String type) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String student_grade = (String) session.getAttribute("student_grade");
		String student_dept_id = (String) session
				.getAttribute("student_dept_id");
		
       //动态获取学年范围
		List<String> grade = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();  
        int find_year = calendar.get(Calendar.YEAR);
        for(int i=2011;i<=find_year;i++){
        	String b=i+"-"+(i+1);
        	grade.add(b);
        }
        
		// 得到所有的学院
		Org o = new Org();
		o.setOrg_level("2");
		List<Org> orgList = orgService.selectList(o);
		map.put("orgList", orgList);
		map.put("grade", grade);
		map.put("student_grade", student_grade);
		map.put("student_dept_id", student_dept_id);
		return new ModelAndView("sysadmin/paramList", map);
	}*/

	/**
	 * 功能：管理员——参数管理 查询出学院所对应的系部名称信息By丁乐晓20160115
	 * 
	 * @param org
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/xibuList.do")
	public ModelAndView paramList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String Org_Name)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf-8");
		List<Org> xibuList = this.orgService.selectXiOrg_Name(Org_Name);// 查询出该学院对应的系部
		StringBuffer xb = new StringBuffer();
		xb.append("<option "+"请选择系部"+"</option>");
		for (Org x : xibuList) {
			xb.append("<option " + "value=" + x.getId() + ">" + x.getOrg_name()
					+ "</option>");
		}
		response.getWriter().println(xb.toString());
		return null;
	}*/

	/**
	 * 功能：管理员——参数管理 查询出不同学年不同学院的参数信息By丁乐晓20160115
	 * 
	 * @param org
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/findList.do")
	public String findList(HttpServletRequest request,
			HttpServletResponse response, String year, HttpSession session,
			String type, String org_id, String xibu) throws IOException {
		response.setCharacterEncoding("utf-8");
		String years = year.substring(0,4);
		Param p = new Param();
		p.setYear(years);
		if(xibu!=""){
			p.setDept_id(xibu);
		}else{
			p.setDept_id(org_id);
		}
		List<Param> s = this.paramService.selectList(p);
		StringBuffer f = new StringBuffer();
		int i = 0;
		for (Param l : s) {
			i++;
			String stateName = "有效";
			if (l.getState().equals("2")) {
				stateName = "无效";
			}
			String tearm="第一学期";
			if(l.getTearm().equals("2")){
				tearm="第二学期";
			}
			f.append("<tr id='nei'>");
			f.append("	<td align='center'>" + i + "</td>");
			f.append("	<td align='center'>" + l.getParam_code() + "</td>");
			f.append("	<td align='center'>" + l.getParam_name() + "</td>");
			f.append("	<td align='center'>" + l.getParam_value() + "</td>");
			f.append("	<td align='center'>"
					+ DictionaryService.findOrg(l.getDept_id()).getOrg_name()
					+ "</td>");
			f.append("	<td align='center'>" + tearm + "</td>");
			f.append("	<td align='center'>" + stateName + "</td>");
			f.append("	<td align='center'><input type='button' value='修改' onclick=doEdit('"
					+ l.getId() + "');></td>");
			f.append("<td align='center'> <input type='button' value='删除' onclick=doDel('"
					+ l.getId() + "');></td>");
			f.append("</tr>");

		}
		response.getWriter().println(f.toString());
		return null;
	}*/

	/**
	 * 功能：管理员——参数管理 删除参数By丁乐晓20160115
	 * 
	 * @param
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/deleteParam.do")
	public String deleteParam(HttpServletRequest request,
			HttpServletResponse response, String delete_id) throws IOException {
		
		功能不全面，删除一条数据之后页面发生了跳转，代码需要优化
		 * Param delete=(Param) paramService.selectByID(delete_id);
		String year=delete.getYear();
		String org_id=delete.getDept_id();
		String xibu=delete.getDept_id();
		paramService.delete(delete_id);
		return "redirect:paramList.do";
		return "redirect:findList.do?year="+year+"&org_id="+org_id;
	}*/

	/**
	 * 功能：管理员——添加管理 添加参数--学院信息By丁乐晓20160115
	 * 
	 * @param
	 * @return
	 */
/*	@RequestMapping("sysadmin/paramAdd.do")
	public ModelAndView paramAdd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		//动态获取学年
		Calendar calendar = Calendar.getInstance();  
        int add_year = calendar.get(Calendar.YEAR);
    	ArrayList add_years=new ArrayList();
        for(int i=2011;i<=add_year;i++){
        	add_years.add(i);
        }
		List<Org> collegeList = orgService.getAllColleges();
		map.put("add_years", add_years);
		map.put("collegeList", collegeList);
		return new ModelAndView("sysadmin/paramAdd", map);
	}*/

	/**
	 * 功能：管理员——添加管理 添加参数By丁乐晓20160309
	 * 
	 * @param
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/doAddParam.do")
	public String doAddParam(HttpServletRequest Request,
			HttpServletResponse response) throws IOException {
		Param pr = new Param();
		String xi_id = Request.getParameter("xi_id");
		String collegeList_name = Request.getParameter("collegeList_name");
		if(collegeList_name.equals("请选择学院")){
			JOptionPane.showMessageDialog(null, "请选择部门");
		}else{
        if(xi_id!=""&&!xi_id.equals("请选择系部")){
    		pr.setDept_id(xi_id);
		}else{
			String collegeList_id = Request.getParameter("collegeList_name");
			pr.setDept_id(collegeList_id);
		}
		}
        //获取参数值
		String param_name = Request.getParameter("param_name");
		String param_value = Request.getParameter("param_value");
		String state = Request.getParameter("state");
		String param_code = Request.getParameter("param_code");
		String year = Request.getParameter("year");
		String tearm = Request.getParameter("tearm");
		//将参数保存到pr中
		pr.setTearm(tearm);
		pr.setTask_id(null);
		pr.setYear(year);
		pr.setParam_name(param_name);
		pr.setParam_value(param_value);
		pr.setState(state);
		pr.setParam_code(param_code);
		pr.setId(Common.returnUUID());
		//添加到数据库
		paramService.insert(pr);
		return "redirect:paramList.do";
	}*/

	/**
	 * 功能：管理员——参数管理 修改参数--显示原始数据By丁乐晓20160309
	 * 
	 * @param
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/paramEdit.do")*/
	/*public ModelAndView paramEdit(HttpServletRequest request,
			HttpServletResponse response,String param_id) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Param edit=(Param) paramService.selectByID(param_id);
		//获取需要修改的数据
		String edit_code=edit.getParam_code();
		String edit_name=edit.getParam_name();
		String edit_value=edit.getParam_value();
		String id=edit.getId();
		
		map.put("id", id);
		map.put("edit_code", edit_code);
		map.put("edit_name", edit_name);
		map.put("edit_value", edit_value);
		return new ModelAndView ("sysadmin/paramEdit",map);

	}*/
	/**
	 * 功能：管理员——参数管理 修改参数By丁乐晓20160310
	 * 
	 * @param
	 * @return 列表页面
	 */
	/*@RequestMapping("sysadmin/doEdit.do")
	public String doEdit(HttpServletRequest request,HttpServletResponse response,String id) throws IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Param ed=new Param();
		ed=(Param) paramService.selectByID(id);
		String edit_name = request.getParameter("edit_name");
		String edit_value = request.getParameter("edit_value");
		String edit_code = request.getParameter("edit_code");
		//保存修改的数据
		ed.setParam_code(edit_code);
		ed.setParam_name(edit_name);
		ed.setParam_value(edit_value);
		
		paramService.update(ed);
		
		return "redirect:paramList.do";

	}
	*/
	/**
	 * 学校领导列表.
	 * @author 吴敬国
	 * @createDate 2016-3-11
	 * @since 1.0
	 */
//	@RequestMapping("sysadmin/schoolLeader.do")
//	public ModelAndView schoolLeaderList(HttpSession session) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		
//		
//		map.put("collegeList", collegeList);
//		return new ModelAndView("sysadmin/roleAdd", map);
//	}
	
	
	
	
}
