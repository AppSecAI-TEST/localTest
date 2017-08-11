package com.sict.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.entity.Org;
import com.sict.entity.Param;
import com.sict.entity.Role;
import com.sict.entity.SysMenu;
import com.sict.entity.Teacher;
import com.sict.entity.UserRole;
import com.sict.service.CompanyService;
import com.sict.service.CourseService;
import com.sict.service.DictionaryService;
import com.sict.service.DirectRecordService;
import com.sict.service.EvalsIndexService;
import com.sict.service.EvaluateStandardService;
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

/**
 * 功能：系统管理员控制器 使用 @Controller 注释 by郑春光20140910 *
 * 
 */
@Controller
public class SysAdminController {
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
	 * 管理员首页.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 3.0
	 */
	@RequestMapping("sysadmin/index.do")
	public String index(HttpSession session) {
		// 默认年份
		String grade = Common.getDefaultYear();
		// 获取负责的学院,通过组织表的联系人id找到负责的学院，限制一个教师只负责一个学院
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String college_id = tea.getDept_id();
		
		// 设置当前权限为管理员 by桑博
		CommonSession.setUserRole(session, CommonSession.roleAdmin);
		return "sysadmin/index";
	}
	/**
	 * 菜单列表.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/menuList.do")
	public ModelAndView menuList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysMenu sm=new SysMenu();
		//sm.setSm_used("1");   wjg
		List<SysMenu> sysMenuList=sysMenuService.selectList(sm);
		map.put("sysMenuList", sysMenuList);
		return new ModelAndView("sysadmin/sysMenuList", map);
	}
	/**
	 * 添加菜单.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/addSysMenu.do")
	public ModelAndView MenuaddSys(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysMenu sm=new SysMenu();
		List<SysMenu> sysMenuList=sysMenuService.selectList(sm);
		map.put("sysMenuList", sysMenuList);
		return new ModelAndView("sysadmin/sysMenuAdd", map);
	}
	/**
	 * 添加菜单.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/doAddSysMenu.do")
	public String MenudoAddSys(HttpSession session,@ModelAttribute("sysMenu") SysMenu sysMenu,
			HttpServletRequest request)  {
		String sm_OneNum = request.getParameter("sm_OneNum");
		String sm_TwoNum = request.getParameter("sm_TwoNum");
		String sm_ThreeNum = request.getParameter("sm_ThreeNum");
		String sm_code ;
		System.out.println(sysMenu.getSm_code());
		/*if(sysMenu.getSm_code().equals("") || sysMenu.getSm_code()==null ){*/
			
			if(sysMenu.getTemp1().equalsIgnoreCase("1")){
				sm_code="admin";
			}else if(sysMenu.getTemp1().equalsIgnoreCase("2")){
				sm_code="tea";
			}else if(sysMenu.getTemp1().equalsIgnoreCase("3")){
				sm_code="stu";
			}else if(sysMenu.getTemp1().equalsIgnoreCase("4")){
				sm_code="leader";
			}else if(sysMenu.getTemp1().equalsIgnoreCase("5")){
				sm_code="com";
			}else {
				sm_code="other";
			}
			if(!(sm_OneNum.equals("")||sm_OneNum==null)){
				sm_code=sm_code+sm_OneNum;
			}
			if(!(sm_TwoNum.equals("")||sm_TwoNum==null)){
				sm_code=sm_code+sm_TwoNum;
			}
			if(!(sm_ThreeNum.equals("")||sm_ThreeNum==null)){
				sm_code=sm_code+sm_ThreeNum;
			}
			sysMenu.setSm_code(sm_code);
		/*}*/
		sysMenuService.insert(sysMenu);
		return "redirect:menuList.do"; 
	}
	
	/**
	 * 删除菜单
	 * 
	 * @author 周睿
	 * @createDate 2015-12-26
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/deleteSysMenu.do")
	public String MenudeleteSys(HttpSession sessio, HttpServletRequest request) {
		String sm_ID = request.getParameter("menuID");
		SysMenu sm = (SysMenu) sysMenuService.selectByID(sm_ID);
		sm.setSm_used("2");
		sysMenuService.update(sm);
		return "redirect:menuList.do";
	}

	/**
	 * 修改菜单
	 * 
	 * @author 周睿
	 * @createDate 2015-12-26
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/sysMenuEdit.do")
	public ModelAndView MenuEdit(HttpSession sessio,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sm_ID = request.getParameter("menuID");
		SysMenu sm = (SysMenu) sysMenuService.selectByID(sm_ID);
		SysMenu sm1 = new SysMenu();
		sm.setSm_used("1");
		List<SysMenu> sysMenuList = sysMenuService.selectList(sm1);
		map.put("sysmenu", sm);
		map.put("sysMenuList", sysMenuList);
		return new ModelAndView("sysadmin/sysMenuEdit", map);
	}

	/**
	 * 修改菜单
	 * 
	 * @author 周睿
	 * @createDate 2015-12-26
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/dosysMenuEdit.do")
	public String MenudoEdit(HttpSession sessio, HttpServletRequest request,
			@ModelAttribute("sysMenu") SysMenu sysMenu) {
		sysMenuService.update(sysMenu);
		return "redirect:menuList.do";
	}
	/**
	 * 顶级组织列表.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/schoolList.do")
	public ModelAndView schoolList(HttpSession session){
		List<Map<String, String>> schoolList=orgService.getTopOrgListAndContacts();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolList", schoolList);
		return new ModelAndView("sysadmin/schoolList", map);
	}
	/**
	 * 初始化学校.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/initSchool.do")
	public ModelAndView initCollege(HttpSession session){
		Role role=new Role();
		role.setTemp1("2");
		List<Role> roleList=roleService.selectList(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleList", roleList);
		return new ModelAndView("sysadmin/initSchool", map);
	}
	
	/**
	 * 初始化学校.
	 * @author 吴敬国
	 * @createDate 2016-1-5
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/doinitSchool.do")
	public String initCollegeSave(HttpSession session,HttpServletRequest request)  {
		String school_name = request.getParameter("school_name");
		String school_code = request.getParameter("school_code");
		String tea_name = request.getParameter("tea_name");
		String tea_code = request.getParameter("tea_code");
		String tea_sex = request.getParameter("tea_sex");
		String tea_phone = request.getParameter("tea_phone");
		String schoolAdmin = request.getParameter("schoolAdmin");
		
		String teaId=Common.returnUUID16();//学校管理员教师的id
		//String collegeId=Common.returnUUID();//学院id
		//添加组织
		Org o = new Org();
		o.setOrg_level("1");
		o.setId(school_code);
		o.setBegin_time(DateService.getNowTimeTimestamp());
		o.setContacts(teaId);
		o.setOrg_code(school_code);
		o.setOrg_name(school_name);
		o.setDirector("无");
		o.setPhone(tea_phone);
		o.setParent_id(Constants.TOP_ORG_ID);//学校级别的组织上级组织为顶级组织-系统
		orgService.insert(o);
		//添加管理员信息
		Teacher tea=new Teacher();
		tea.setId(teaId);
		tea.setTea_code(tea_code);
		tea.setTrue_name(tea_name);
		tea.setLogin_pass(tea_code);
		tea.setSex(tea_sex);
		tea.setPhone(tea_phone);
		tea.setDept_id(school_code);
		tea.setTea_type("1");
		tea.setState("1");
		teacherService.insert(tea);
		//添加学校管理员角色
		String schoolAdminRole=schoolAdmin+school_code;
		Role role=new Role();
		role.setId(schoolAdminRole);
		role.setRole_code(schoolAdminRole);
		role.setRole_name(school_name+"管理员");
		role.setRole_desc(school_name+"管理员");
		role.setRole_type("1");//管理员类型
		role.setCreate_time(DateService.getNowTimeTimestamp());
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		role.setCreate_tea(teacher.getId());
		role.setSchool_id(school_code);
		role.setCollege_id("");
		role.setTemp1("4");//学校级别的角色为4
		roleService.insert(role);
		//添加管理员权限
		UserRole userroleadmin=new UserRole();
		userroleadmin.setRole_id(schoolAdminRole);
		userroleadmin.setUser_id(teaId);
		userRoleService.insert(userroleadmin);
		//sysRoleMenuService.saveMenuForRole(collegeAdminRole);//给学院管理员角色分配模板中的菜单
		//添加管理员权限和教师基本角色
		userRoleService.saveBasicUserRole(teaId, "ROLE_ADMIN", "ROLE_TEACHER", null,null,null);
		return "redirect:index.do"; 
	}
	
	
	/**
	 * 角色模板列表.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/roleTemplateList.do")
	public ModelAndView roleTemplateList(HttpSession session)  {
		//得到角色模板的列表
		List<Role> roleTemplateList=roleService.getRoleByRoleLevel(Constants.ROLE_LEVEL_ONE,Constants.ROLE_LEVEL_TWO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleTemplateList", roleTemplateList);
		return new ModelAndView("sysadmin/roleTemplateList", map);
	}
	
	/**
	 * 添加角色模板.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/addRoleTemplate.do")
	public String  roleTemplateadd(HttpSession session) {
		/*Map<String, Object> map = new HashMap<String, Object>();
		List<Org> collegeList=orgService.getAllColleges();//没用
		map.put("collegeList", collegeList);*/
		//return new ModelAndView("sysadmin/roleTemplateAdd", map);
		return "sysadmin/roleTemplateAdd";
	}
	
	/**
	 * 根据角色类型得到对应的菜单
	 * 
	 * @Description
	 * @author 吴敬国 2015-12-23
	 * @return
	 */
	@RequestMapping("sysadmin/getSysMenuByRoletype.do")
	public String roleAjaxGetSysMenuByRoletype(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String role_type = request.getParameter("role_type");
		SysMenu sysmenu = new SysMenu();
		sysmenu.setTemp1(role_type);
		sysmenu.setSm_is_top(Constants.SYSMENU_LEVEL_ONE);
		sysmenu.setSm_used(Constants.SYSMENU_USERD);
		List<SysMenu> sysMenuList = sysMenuService.selectTopMenuListByRoleType(sysmenu);
		StringBuffer sb=sysMenuService.StringBufferWithSysMenu(sysMenuList);
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 添加角色模板.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/doAddRoleTemplate.do")
	public String roleTemplateSave(HttpServletRequest request, HttpSession session)
			throws IOException {
		String role_type = request.getParameter("role_type");
		String role_code = request.getParameter("role_code");
		String role_name = request.getParameter("role_name");
		String role_desc = request.getParameter("role_desc");
		String state = request.getParameter("state");

		Role role = new Role();
		role.setId(role_code);
		role.setRole_code(role_code);
		role.setRole_type(role_type);
		role.setRole_name(role_name);
		role.setRole_desc(role_desc);
		role.setSchool_id(Constants.TOP_ORG_ID);
		Teacher tea = (Teacher) session.getAttribute("current_user");
		role.setCreate_tea(tea.getId());
		role.setCollege_id(Constants.TOP_ORG_ID);
		role.setCreate_time(DateService.getNowTimeTimestamp());
		role.setState(state);
		role.setTemp1(Constants.ROLE_LEVEL_TWO);
		roleService.insert(role);

		String[] sysMenu = request.getParameterValues("roleMenu");
		sysRoleMenuService.insertSysRoleMenuBySrmRoleId(role_code, sysMenu);//根据角色id和这个角色对应的菜单数组保存到角色菜单对应表
		
		/*SysRoleMenu srm = new SysRoleMenu();
		// 循环插入到数据库中
		if (roleMenu != null) {
			for (int i = 0; i < roleMenu.length; i++) {
				srm.setSrm_menu_id(roleMenu[i]);
				srm.setSrm_role_id(role_code);
				sysRoleMenuService.insert(srm);
			}
		}*/
		return "redirect:roleTemplateList.do"; // 添加成功后重定向到列表页面
	}
	
	/**
	 * 角色模板管理-修改角色
	 * @Description
	 * @author 
	 * @return
	 */
	@RequestMapping("sysadmin/editRoleTemplate.do")
	public String roleTemplateEdit(ModelMap modelMap, String role_id) {
		List<SysMenu> sysMenuLists = sysMenuService.getSysMenuByRoleId(role_id);
		Role role = (Role) roleService.selectByID(role_id);
		modelMap.put("role", role);
		modelMap.put("sysMenuLists", sysMenuLists);
		return "sysadmin/roleTemplateEdit";
	}
	
	/**
	 * 角色模板管理-修改保存角色
	 * @Description
	 * @author 
	 * @return
	 */
	@RequestMapping("sysadmin/doEditTemplateRole.do")
	public String roleTemplateDoEdit(@ModelAttribute("role") Role role,
			HttpServletRequest request) throws IOException {
		String role_id = request.getParameter("id");// 获取该角色ID
		String[] sysMenu = request.getParameterValues("sysMenu");
		roleService.update(role);
		sysRoleMenuService.updateSysRoleMenuBySrmRoleId(role_id, sysMenu);
		return "redirect:roleTemplateList.do";
	}
	
	/**
	 * 角色列表.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/roleList.do")
	public ModelAndView roleList(HttpSession session)  {
		List<Role> roleList=roleService.getRoleByRoleLevel(Constants.ROLE_LEVEL_THREE,Constants.ROLE_LEVEL_FOUT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleList", roleList);
		return new ModelAndView("sysadmin/roleList", map);
	}
	/**
	 * 添加角色.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/addRole.do")
	public ModelAndView roleAdd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> collegeList=orgService.getAllColleges();
		List<Org> schoolList=orgService.getAllSchool();
		map.put("collegeList", collegeList);
		map.put("schoolList", schoolList);
		return new ModelAndView("sysadmin/roleAdd", map);
	}
	
	/**
	 * 添加角色通过角色模板.
	 * @author 吴敬国
	 * @createDate 
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/addRoleByRoleTemplate.do")
	public ModelAndView addRoleByRoleTemplate(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Role role=new Role();
		role.setTemp1("2");
		List<Role> roleTemplateList=roleService.selectList(role);
		List<Org> collegeList=orgService.getAllColleges();
		List<Org> schoolList=orgService.getAllSchool();
		map.put("collegeList", collegeList);
		map.put("schoolList", schoolList);
		map.put("roleTemplateList", roleTemplateList);
		return new ModelAndView("sysadmin/roleAddByRoleTemplate", map);
	}
	
	
	/**
	 * 通过角色模板保存角色.
	 * @author 吴敬国
	 * @createDate 
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/doAddRoleByRoleTemplate.do")
	public String  doAddRoleByRoleTemplate(HttpSession session,HttpServletRequest request) {
		String roleTemplate_id=request.getParameter("roleTemplate_id");
		String roleType=request.getParameter("roleType");
		String school_id=request.getParameter("school_id");
		String college_id=request.getParameter("college_id");
		//根据角色模板建立一个的角色及菜单
		if(roleType.equalsIgnoreCase("学校角色")){
			roleService.saveSchoolRoleByRoleTemplate(roleTemplate_id, school_id, session);
		}else{
			roleService.saveRoleByRoleTemplate(roleTemplate_id, school_id, college_id, session);
		}
		
		return "redirect:roleList.do";
	}
	
	
	/**
	 * 保存角色.
	 * @author 吴敬国
	 * @createDate 2015-12-21
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/doAddRole.do")
	public String roledoSave(HttpServletRequest request,HttpSession session)  {
		String role_type = request.getParameter("role_type");
		String school_id = request.getParameter("school_id");
		String college_id = request.getParameter("college_id");
		String role_code = request.getParameter("role_code");
		String role_name = request.getParameter("role_name");
		String role_desc = request.getParameter("role_desc");
		String state = request.getParameter("state");

		Role role = new Role();
		role.setId(role_code);
		role.setRole_code(role_code);
		role.setRole_type(role_type);
		role.setRole_name(role_name);
		role.setRole_desc(role_desc);
		role.setSchool_id(school_id);
		role.setCreate_tea(Common.getOneTeaId(session));
		role.setCollege_id(college_id);
		role.setCreate_time(DateService.getNowTimeTimestamp());
		role.setState(state);
		role.setTemp1("3");
		roleService.insert(role);
		
		String[] sysMenu = request.getParameterValues("roleMenu");
		sysRoleMenuService.insertSysRoleMenuBySrmRoleId(role_code, sysMenu);
		
		return "redirect:roleList.do"; 
	}
	
	
	/**
	 * 角色管理-修改角色
	 * @Description
	 * @author 
	 * @return
	 */
	@RequestMapping("sysadmin/editRole.do")
	public String roleEdit(ModelMap modelMap, String role_id) {
		List<SysMenu> sysMenuLists = sysMenuService.getSysMenuByRoleId(role_id);
		Role role = (Role) roleService.selectByID(role_id);
		modelMap.put("role", role);
		modelMap.put("sysMenuLists", sysMenuLists);
		return "sysadmin/roleEdit";
	}
	
	
	/**
	 * 角色管理-修改保存角色
	 * @Description
	 * @author 
	 * @return
	 */
	@RequestMapping("sysadmin/doEditRole.do")
	public String roleDoEdit(@ModelAttribute("role") Role role,
			HttpServletRequest request) throws IOException {
		String role_id = request.getParameter("id");// 获取该角色ID
		String[] sysMenu = request.getParameterValues("sysMenu");
		roleService.update(role);
		sysRoleMenuService.updateSysRoleMenuBySrmRoleId(role_id, sysMenu);
		return "redirect:roleList.do";
	}
	
	/**
	 * 组织管理列表
	 * by吴敬国
	 */
	/*@RequestMapping("sysadmin/orgList.do")功能还没写2016-4-7
	public ModelAndView orgList(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		Org org =new Org();
		org.setState("1");
		orgService.
		
		
		return new ModelAndView("sysadmin/schOrgList", map);
	}*/
	
	/**
	 * 教师用户列表.
	 * @author 吴敬国
	 * @createDate 2016-3-11
	 * @since 1.0
	 */
	@RequestMapping("sysadmin/teacherList.do")
	public ModelAndView schoolLeaderList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea=new Teacher();
		List<Teacher> teaList=teacherService.selectList(tea);
		map.put("teaList", teaList);
		return new ModelAndView("sysadmin/teacherList", map);
	}
	
	/**
	 * 功能：重置教师密码
	 *  
	 */
	@RequestMapping("sysadmin/resetPassword.do")
	public String resetPassword(HttpServletRequest request, HttpSession session) throws IOException {
		String tea_id = request.getParameter("tea_id");
		teacherService.resetPassword(tea_id);
		return "redirect:teacherList.do";
	}
	
	/**
	 * 
	 * @Description
	 * @author 
	 * @return
	 */
	@RequestMapping("sysadmin/addTeacher.do")
	public String  teacherAdd(HttpSession session) {
		/*功能还没写2016-4-7
		 * 
		 * Map<String, Object> map = new HashMap<String, Object>();
		String college_id = (String) session.getAttribute("college_id");
		List<Org> result = this.orgService.selectByXueYuanId(college_id);
		map.put("teachers", result);
		
		return new ModelAndView("admin/teacherAdd", map);*/
		return "";
	}
	
	
	
	
	
	
	/**
	 * 组织管理列表by宋浩20160315
	 */
	@RequestMapping("sysadmin/schOrgList.do")
	public ModelAndView orgManager(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String dept_id=tea.getDept_id();
		Org org=orgService.selectByID(dept_id);
		List<Org> orgList=orgService.getOrgSon();
		map.put("orgList", orgList);
		map.put("org", org);
		return new ModelAndView("sysadmin/schOrgList", map);
	}
	/**
	 * 校级组织管理添加by宋浩20160319
	 */
	@RequestMapping("sysadmin/schOrgAdd.do")
	public ModelAndView orgAdd(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher tea = (Teacher) session.getAttribute("current_user");
		String teaDept=tea.getDept_id();
		Org org=orgService.selectByID(teaDept);
		List<Org> orgList=orgService.getOrgSon();

		map.put("org", org);
		map.put("orgList", orgList);
		return new ModelAndView("sysadmin/schOrgAdd", map);
	}
	
		/**
		 * ajax查询学院老师
		 * 宋浩 20160324
		 */
		@RequestMapping("sysadmin/ajaxXY_teacher.do")
		public String ajaxXY_teacher(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			response.setCharacterEncoding("utf-8");
			String c = request.getParameter("contactsDept");
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
	/**
	 * 保存组织管理by宋浩20160321
	 */
	@RequestMapping("sysadmin/dosysOrgAdd.do")
	public String schOrgdoAdd(HttpServletRequest request, HttpSession session)  {
		String org_code = request.getParameter("org_code");
		String org_name = request.getParameter("org_name");
		String contacts = request.getParameter("contacts");
		String org_level = "1";
		String phone = request.getParameter("phone");
		String parent_id = request.getParameter("par_dept");//上级组织id
		if (contacts == null) {
			contacts = "无";
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
		org.setContacts(contacts);
		org.setOrg_level("1");
		org.setDirector("");
		org.setVice_director("");
		org.setPhone(phone);
		org.setParent_id(parent_id);
		org.setState("1");
		orgService.insert(org);
		return "redirect:schOrgList.do"; 
	}
	
	/**
	 * 添加校级领导 by 师杰 20160321
	 */
	@RequestMapping("sysadmin/schoolLeadersAdd.do")
	public ModelAndView schoolLeadersAdd(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> org = orgService.selectSchoolOrg();// 获取所有校级部门

		map.put("collegeList", org);
		return new ModelAndView("sysadmin/schoolLeadersAdd", map);
	}

	/**
	 * 保存校级领导信息 by 师杰 20160321
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("sysadmin/doSchoolLeadersAdd.do")
	public String doSchoolLeadersAdd(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String tea_code = request.getParameter("tea_code");
		String tea_name1 = request.getParameter("tea_name");
		String tea_name = new String(tea_name1.getBytes("ISO-8859-1"), "UTF-8");
		String sex1 = request.getParameter("sex");
		String sex = new String(sex1.getBytes("ISO-8859-1"), "UTF-8");
		String phone = request.getParameter("phone");
		String dept_id = request.getParameter("dept_id");
		String duties1 = request.getParameter("duties");
		String duties = new String(duties1.getBytes("ISO-8859-1"), "UTF-8");

		Teacher tea = new Teacher();
		tea.setId(Common.returnUUID());
		tea.setTea_code(tea_code);
		tea.setLogin_pass(tea_code);
		tea.setTrue_name(tea_name);
		tea.setSex(sex);
		tea.setPhone(phone);
		tea.setDept_id(dept_id);
		tea.setCourse_id("");
		tea.setExpertise("");
		tea.setState("1");
		tea.setTea_type("1");
		tea.setDuties(duties);
		teacherService.insert(tea);

		return "redirect:schoolLeadersList.do";
	}

	/**
	 * 显示校级领导 by 师杰 20160321
	 */
	@RequestMapping("sysadmin/schoolLeadersList.do")
	public ModelAndView schoolLeadersList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Org> org = orgService.selectSchoolOrg();
		List<Teacher> li = new ArrayList<Teacher>();
		for (Org o : org) {
			List<Teacher> tea = teacherService.getTeachersByDeptId(o.getId());
			for (Teacher t : tea) {
				Org i = orgService.selectByID(t.getDept_id());
				String org_name = i.getOrg_name();
				t.setTemp1(org_name);
			}
			li.addAll(tea);
		}
		map.put("org", org);
		map.put("teaList", li);
		return new ModelAndView("sysadmin/schoolLeadersList", map);
	}

	/**
	 * ajax传递 师杰 2016-03-24 ajaxGetSchoolLeaders.do
	 */
	@RequestMapping("sysadmin/ajaxGetSchoolLeaders.do")
	public String schoolLeadersAjaxGet(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {

		response.setCharacterEncoding("UTF-8");// 设置编码格式为utf-8
		String dept = request.getParameter("dept");
		List<Teacher> tea = teacherService.getTeachersByDeptId(dept);
		
			StringBuffer sb = new StringBuffer();// 数据缓冲池
			
				for (Teacher t : tea) {
					int i = tea.indexOf(t) +1;
					sb.append("<tr id='tbody'>");
					sb.append("<td>" + i + "</td>");
					sb.append("<td>" + t.getTea_code() + "</td>");
					sb.append("<td>" + t.getTrue_name() + "</td>");
					sb.append("<td>" + t.getSex() + "</td>");
					sb.append("<td>" + t.getPhone() + "</td>");
					sb.append("<td>" + DictionaryService.findOrg(t.getDept_id()).getOrg_name() + "</td>");
					sb.append("<td>" + t.getDuties() + "</td>");
					if (t.getState().equalsIgnoreCase("1")) {
						sb.append("	<td>" + "有效" + "</td>");
					} else if (t.getState().equalsIgnoreCase("2")) {
						sb.append("	<td>" + "无效" + "</td>");
					} else {
						sb.append("	<td>" + "待审核" + "</td>");
					}
					sb.append("<td><input id='"
							+ t.getId()
							+ "' type='button' onclick='doEdit(id)' value='修改'/><button>删除</button></td>");
				}
				

			try {
				response.getWriter().println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		
	}

	/**
	 * 修改校级领导 by 师杰 20160321
	 */
	@RequestMapping("sysadmin/schoolLeadersEdit.do")
	public ModelAndView schoolLeadersEdit(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String teaId = request.getParameter("teaId");
		Teacher tea = (Teacher) teacherService.selectByID(teaId);
		List<Org> org = orgService.selectSchoolOrg();// 获取所有校级部门
		String se = tea.getSex();

		map.put("collegeList", org);
		map.put("tea", tea);
		map.put("teaId", teaId);
		map.put("se", se);
		return new ModelAndView("sysadmin/schoolLeadersEdit", map);
	}

	/**
	 * 保存修改后校级领导信息 by 师杰 20160321
	 * 
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping("sysadmin/doSchoolLeadersEdit.do")
	public String doSchoolLeadersEdit(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String teaId = request.getParameter("teaId");
		String tea_code = request.getParameter("tea_code");
		String tea_name1 = request.getParameter("tea_name");
		String tea_name = new String(tea_name1.getBytes("ISO-8859-1"), "UTF-8");
		String sex1 = request.getParameter("sex");
		String sex = new String(sex1.getBytes("ISO-8859-1"), "UTF-8");
		String phone = request.getParameter("phone");
		String dept_id = request.getParameter("dept_id");
		String duties1 = request.getParameter("duties");
		String duties = new String(duties1.getBytes("ISO-8859-1"), "UTF-8");

		Teacher tea = (Teacher) teacherService.selectByID(teaId);
		String s = tea.getSex();
		tea.setTea_code(tea_code);
		tea.setTrue_name(tea_name);
		tea.setSex(sex);
		tea.setPhone(phone);
		tea.setDept_id(dept_id);
		tea.setDuties(duties);
		teacherService.update(tea);

		return "redirect:schoolLeadersList.do";
	}
	
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
	 * 功能：系统管理员——参数管理by丁乐晓2016o115
	 * 
	 * @param
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/paramList.do")
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
	}

	/**
	 * 功能：管理员——参数管理 查询出学院所对应的系部名称信息By丁乐晓20160115
	 * 
	 * @param org
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/xibuList.do")
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
	}

	/**
	 * 功能：管理员——参数管理 查询出不同学年不同学院的参数信息By丁乐晓20160115
	 * 
	 * @param org
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/findList.do")
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
	}

	/**
	 * 功能：管理员——参数管理 删除参数By丁乐晓20160115
	 * 
	 * @param
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/deleteParam.do")
	public String deleteParam(HttpServletRequest request,
			HttpServletResponse response, String delete_id) throws IOException {
		
		/*功能不全面，删除一条数据之后页面发生了跳转，代码需要优化
		 * Param delete=(Param) paramService.selectByID(delete_id);
		String year=delete.getYear();
		String org_id=delete.getDept_id();
		String xibu=delete.getDept_id();*/
		paramService.delete(delete_id);
		return "redirect:paramList.do";
		/*return "redirect:findList.do?year="+year+"&org_id="+org_id;*/
	}

	/**
	 * 功能：管理员——添加管理 添加参数--学院信息By丁乐晓20160115
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("sysadmin/paramAdd.do")
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
	}

	/**
	 * 功能：管理员——添加管理 添加参数By丁乐晓20160309
	 * 
	 * @param
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/doAddParam.do")
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
	}

	/**
	 * 功能：管理员——参数管理 修改参数--显示原始数据By丁乐晓20160309
	 * 
	 * @param
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/paramEdit.do")
	public ModelAndView paramEdit(HttpServletRequest request,
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

	}
	/**
	 * 功能：管理员——参数管理 修改参数By丁乐晓20160310
	 * 
	 * @param
	 * @return 列表页面
	 */
	@RequestMapping("sysadmin/doEdit.do")
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
	/**
	 * 功能：设置二级学院是否需交工作总结 周睿20160415
	 * */

	@RequestMapping("sysadmin/setWorkload.do")
	public ModelAndView setWorkload(HttpServletRequest request,
			HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List <Org> orgs=orgService.getAllColleges();
		List <Org> orgList=new ArrayList<Org>();
		for(Org o:orgs){
			Param p1 = new Param();
			p1.setParam_name("教师总结");
			p1.setDept_id(o.getOrg_code());
			Param param = new Param();
			param = paramService.selectParambyIdAndParam_name(p1);//取出学院是否有总结参数
			if(param!=null){
			o.setHead_tea_id(param.getParam_code());}
			orgList.add(o);
		}
		map.put("orgList", orgList);
		return new ModelAndView("sysadmin/setWorkload", map);
	}

	/**
	 * 功能：管理员设置参数 周睿20160415
	 * */
	@RequestMapping("sysadmin/doSetWorkload.do")
	public String doSetWorkload(HttpServletRequest request,
			HttpSession session) throws IOException {
		String orgId=request.getParameter("org_code");
		Param p1 = new Param();
		p1.setParam_name("教师总结");
		p1.setDept_id(orgId);
		Param param = new Param();
		param = paramService.selectParambyIdAndParam_name(p1);//取出学院是否有总结参数
		if(param!=null){
			if(param.getParam_code().equals("true")){
				param.setParam_code("false");
				paramService.update(param);
			}else{
				param.setParam_code("true");
				paramService.update(param);
			}
		}
		else{//若无设置总结参数设置总结参数
			Param newParam = new Param();
			String id=Common.returnUUID();
			newParam.setId(id); 
			newParam.setDept_id(orgId);
			newParam.setParam_name("教师总结");
			newParam.setParam_code("false");
			newParam.setParam_value("01");
			newParam.setState("1");
			newParam.setYear("0000");
			newParam.setTearm("长期有效");
			paramService.insert(newParam);
			
		}
		return "redirect:setWorkload.do";
		
	}

	
}
