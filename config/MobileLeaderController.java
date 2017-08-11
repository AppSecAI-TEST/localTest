package com.sict.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sict.entity.ChartData;
import com.sict.entity.ChartModel;
import com.sict.entity.Org;
import com.sict.entity.StuGraState;
import com.sict.entity.StuGraStateCount;
import com.sict.entity.Teacher;
import com.sict.service.DictionaryService;
import com.sict.service.OrgService;
import com.sict.service.PracticeTaskService;
import com.sict.service.RegionalDistributionService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.util.Common;
import com.sict.util.Ichartjs_Color;

/*
 * 功能：手机端领导控制器
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class MobileLeaderController {
	/**
	 * 注入practiceTaskService by杨梦肖20160419 *
	 * 
	 * */
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	@Resource(name = "regionalDistributionService")
	private RegionalDistributionService regionalDistributionService;
	/**
	 * 注入teacherService by杨梦肖20160412 *
	 */
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	/**
	 * 注入studentService
	 * 
	 * @author杨梦肖 2016-4-12
	 */
	@Resource(name = "studentService")
	private StudentService studentService;
	/**
	 * 注入orgService
	 * 
	 * @author  杨梦肖 2016-4-14
	 */
	@Resource(name = "orgService")
	private OrgService orgService;
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至二级学院签到
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 19
	 */
	@RequestMapping("MobileLeader/signUse.do")
	public ModelAndView test(HttpServletRequest request,HttpSession session, HttpServletResponse response) throws Exception {
		/*Map<String, Object> map = new HashMap<String, Object>();
		List<EchartSeries> liste=new ArrayList<EchartSeries>();
		List<String> orgNameList=new ArrayList<String>();
		List<Org> orgList=this.orgService.selectOrg_Name();
		String x="";
		Map<String, List<String>> result=practiceTaskService.getSinStuSizePro("2013", "dzxxxy", "2015");
		List<String> mouthList=result.get("mouthList");
		for(Org o:orgList){
			orgNameList.add(o.getOrg_name());
			String id=o.getId();
			Map<String, List<String>> resultl=practiceTaskService.getSinStuSizePro("2013", id, "2015");
			List<String> pro=resultl.get("spro");
			EchartSeries e=new EchartSeries();
			e.setName(o.getOrg_name());
			e.setType("line");
			e.setData(pro, mouthList.size());
			liste.add(e);
		}
		x=Common.getXzhou(mouthList);
		String es=Common.getJsonSeries(liste);
		String orgNameJson=Common.getXzhou(orgNameList);
		map.put("e", es);
		map.put("d", x);
		map.put("orgNameJson", orgNameJson);*/
		return new ModelAndView("/mobileViews/mobileLeader/signUsePro");
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至学校实习学生分布
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 19
	 */
	// 学校实习学生分布（学校领导的权限）
		@RequestMapping("MobileLeader/getCollegeStudentCityStatistics.do")
		public ModelAndView getCollegeStudentCityStatistics(
				HttpServletRequest request, HttpServletResponse response,
				HttpSession session, ModelMap modelMap) throws Exception {
			response.setCharacterEncoding("utf-8");
			response.setContentType("utf-8");
			List<ChartData> BmapIcharList = new ArrayList<ChartData>();
			Teacher tea = (Teacher) session.getAttribute("current_user");
			String dept_id = tea.getDept_id();
			String provence1=request.getParameter("code");
			String provence=null;
			if(provence1==null){
				provence1="山东省";//1210
				provence=provence1;
			}else{
				 try 
				  {
					 provence = new String(provence1.getBytes("ISO-8859-1"),"UTF-8");
				  } 
				  catch (UnsupportedEncodingException e) 
				  {
				          e.printStackTrace();
				  }
			}
						/*//乱码处理
			String provence = new String(provence1.getBytes("iso-8859-1"), "utf-8");*/
			
			String grade = request.getParameter("year");
			if(grade==null){
				grade = Common.getDefaultYear();
			}
			//provence = new String(provence.getBytes("iso-8859-1"), "utf-8");
			List<String> list = this.orgService.getColleges();
			for (int i = 0; i < list.size(); i++) {
				String id = list.get(i);
				double pet = this.regionalDistributionService.getPent(grade, provence,id);
				String SignProString = Double.toString(pet);
				ChartData BmapIchar = new ChartData();
				String org_name = DictionaryService.findOrg(id).getOrg_name();
				BmapIchar.setName(org_name);
				BmapIchar.setValue(SignProString);
				BmapIchar.setColor(Ichartjs_Color.Ichartjs_Color_List.get(i));
				BmapIcharList.add(i, BmapIchar);
			}
			//获得城市
			Map<String, Integer> map1 = this.practiceTaskService.getSchoolStudent(
					dept_id, grade);
			Map<String, Integer> map = Common.mapSort(map1);
			
			ChartModel mode = new ChartModel();
			mode.setListData(BmapIcharList);
			String json = mode.getJsonData();
			modelMap.put("map", map);
			modelMap.put("title", grade+"级各二级学院学生分布");
			modelMap.put("grade", grade);
			modelMap.put("provence", provence);
			modelMap.put("calculateRules", "计算规则：各二级学院"+provence+"人数/全校"+provence+"总人数");
			modelMap.put("cm", json);
			return new ModelAndView("mobileViews/mobileLeader/StudentCityStatistics", modelMap);
		}

	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至学校签到
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 18
	 */
	@RequestMapping("MobileLeader/schoolBmapPie.do")
	public ModelAndView tobMapCollegeSignState(HttpSession session,
			HttpServletRequest request) {
		// 默认年份
		String grade = request.getParameter("year");
		if(grade==null){
			grade = Common.getDefaultYear();
		}
		String stateName = request.getParameter("code");
		if(stateName==null){
			stateName="2" ;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String dept_id = "szxy";
		// 获取当前学院的名字
		String deptName = DictionaryService.findOrg(dept_id).getOrg_name();
		List<ChartData> BmapIcharList = new ArrayList<ChartData>();
		// 标题
		String signState="";
		StringBuffer sb = new StringBuffer();
		if (stateName.equals("2")) {
			signState="未签到";
			sb.append(deptName + grade + "学生本周未签到情况");
		} else {
			signState="签到";
			sb.append(deptName + grade + "学生本周签到情况 ");
		}
		// 获取该学院管理下的系
		// List<Org> departments = orgService.selectByXyId(dept_id);
		List<Org> departments = orgService.selectOrg_Name();
		for (int i = 0; i < departments.size(); i++) {
			String org_id = departments.get(i).getId();
			double scale = practiceTaskService.getCollegeDaySignPro(org_id,
					grade);
			// 判断点击的那部分饼图
			if (stateName.equals("2")) {
				scale = 100 - (scale * 100);
			} else {
				scale = scale * 100;
			}
			// 处理小数点保留位数问题
			String falseRate = new java.text.DecimalFormat("#.00")
					.format(scale);
			scale = Double.parseDouble(falseRate);

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
		String title = sb.toString();
		List  <String>  staname=new  ArrayList<String>();
		staname.add("1");
		staname.add("2");
		map.put("title", title);
		map.put("staname", staname);
		map.put("grade", grade);
		map.put("cm", json);
		map.put("calculateRules", "计算规则：各二级学院"+signState+"人数/二级学院实习总人数");
		return new ModelAndView("mobileViews/mobileLeader/bMapSignState", map);
	}
	

	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至校外实习的页面
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/index.do")
	public ModelAndView toPracticeStateChartCollege(HttpServletRequest request,ModelMap modelMap) {
		// 获取状态编码
		String curStateCode = request.getParameter("code");//问题：从前台获得不了curStateCode
		if(curStateCode==null){
			curStateCode = "000";
		}
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list  柱形图上面显示数据
		List<String> list=new ArrayList<String>();
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		String grade = request.getParameter("date");//问题：从前台获得不了年份
		if(grade==null){
			grade = Common.getDefaultYear();
		}
		// 各学院各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = orgService.selectOrg_Name();
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = studentService
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
						list.add(stuCount.getDeptId());	
					}
				}
			else//其他学院显示0 2016年2月25日 wugee
			{
				mapStateCount = new HashMap<String, Object>();
				// 生成新的list listDeptStuState
				mapStateCount.put("deptId", o.getId());
				mapStateCount.put("percent", "0");
				listMapDeptStuState.add(mapStateCount);
			}
		}
		
		ChartModel cm = new ChartModel();
		// 将新生成的数据转成json，在图表在显示
		cm = Ichartjs_Color.getChartModelByPercent(listMapDeptStuState);
		String jsonData = cm.getJsonData();
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		TreeMap<String, StuGraState> mapState = new TreeMap<String, StuGraState>();
		mapState = DictionaryService.getStuGraStateName();
		for (String key : mapState.keySet()) {
			String val = key + " " + mapState.get(key).getStateDesc();
			map.put(key, val);
		}
		modelMap.put("mapState", map);
		modelMap.put("stateName", stateName);
		modelMap.put("cm", jsonData);
		modelMap.put("grade", grade);
		modelMap.put("calculateRules", "计算规则：各二级学院"+ stateName+"人数/各二级学院总人数");
		return new ModelAndView("mobileViews/mobileLeader/index", map);
	}
	

	
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至实习状态
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 18
	 */
	@RequestMapping("MobileLeader/InternshipStudentList.do")
	public ModelAndView toInternshipStudentList
		(HttpServletRequest request,ModelMap modelMap) {
		// 获取状态编码
		String curStateCode = request.getParameter("code");//问题：从前台获得不了curStateCode
		if(curStateCode==null){
			curStateCode = "000";
		}
		List<Map<String, Object>> listMapDeptStuState = new ArrayList<Map<String, Object>>();// 新的list  柱形图上面显示数据
		List<String> list=new ArrayList<String>();
		List<StuGraStateCount> listStuStateCount;// 单个学院各项状态人数
		String grade = request.getParameter("date");//问题：从前台获得不了年份
		if(grade==null){
			grade = Common.getDefaultYear();
		}
		// 各学院各项状态百分比
		Map<String, Object> mapParam = new HashMap<String, Object>();
		Map<String, Object> mapStateCount = new HashMap<String, Object>();
		List<Org> orgList = orgService.selectOrg_Name();
		for (Org o : orgList) {
			// 获取单个学院各项状态人数
			mapParam.put("grade", grade);
			mapParam.put("deptId", o.getId());
			listStuStateCount = studentService
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
						list.add(stuCount.getDeptId());	
					}
				}
			else//其他学院显示0 2016年2月25日 wugee
			{
				mapStateCount = new HashMap<String, Object>();
				// 生成新的list listDeptStuState
				mapStateCount.put("deptId", o.getId());
				mapStateCount.put("percent", "0");
				listMapDeptStuState.add(mapStateCount);
			}
		}
		
		ChartModel cm = new ChartModel();
		// 将新生成的数据转成json，在图表在显示
		cm = Ichartjs_Color.getChartModelByPercent(listMapDeptStuState);
		String jsonData = cm.getJsonData();
		Map<String, Object> map = new HashMap<String, Object>();
		String stateName = DictionaryService.findStuGraStateName(curStateCode)
				.getStateDesc();
		TreeMap<String, StuGraState> mapState = new TreeMap<String, StuGraState>();
		mapState = DictionaryService.getStuGraStateName();
		for (String key : mapState.keySet()) {
			String val = key + " " + mapState.get(key).getStateDesc();
			map.put(key, val);
		}
		modelMap.put("mapState", map);
		modelMap.put("stateName", stateName);
		modelMap.put("cm", jsonData);
		modelMap.put("grade", grade);
		modelMap.put("calculateRules", "计算规则：各二级学院"+ stateName+"人数/各二级学院总人数");
		return new ModelAndView("mobileViews/mobileLeader/index01", map);
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至个人信息
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/PersonalInformation.do")
	public String to_PersonalInformation(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileLeader/PersonalInformation";
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至帮助
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/help.do")
	public String help(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException, ServletException {
		return "/mobileViews/mobileLeader/Help";
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至个人信息维护
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/PersonalInformationMaintenance.do")
	public ModelAndView toPersonalInformationMaintenance(ModelMap modelMap,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// 获取当前用户!!!!!
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.selectByID(Common
				.getOneTeaId(session));// 通过session获得当前老师的ID
		map.put("teacher", teacher);
		/* return "/mobileViews/mobileTeacher/PersonalInformationMaintenance"; */
		return new ModelAndView(
				"/mobileViews/mobileLeader/PersonalInformationMaintenance",
				map);
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至修改密码
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/ChangePassword.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Teacher teacher = (Teacher) session.getAttribute("current_user");
		map.put("teacher", teacher);
		return new ModelAndView("/mobileViews/mobileLeader/ChangePassword",
				map);
	}
	/**
	 * @author 杨梦肖
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return 跳转至校外实习的页面
	 * @throws IOException
	 * @throws ServletException
	 * 2016 4 12
	 */
	@RequestMapping("MobileLeader/practiceManagementList.do")
	public String topracticeManagementList(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		return "/mobileViews/mobileLeader/practiceManagementList";
	}

}
