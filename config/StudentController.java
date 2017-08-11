package com.sict.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sict.common.CommonSession;
import com.sict.dao.FilesDao;
import com.sict.entity.Association;
import com.sict.entity.AssociationMembers;
import com.sict.entity.Files;
import com.sict.entity.GraduationMaterials;
import com.sict.entity.GraduationThesis;
import com.sict.entity.Notice;
import com.sict.entity.Org;
import com.sict.entity.PracticeTask;
import com.sict.entity.Student;
import com.sict.service.AssociationMembersService;
import com.sict.service.AssociationService;
import com.sict.service.DictionaryService;
import com.sict.service.ExcelService;
import com.sict.service.FilesService;
import com.sict.service.GraduationMaterialService;
import com.sict.service.GraduationThesisService;
import com.sict.service.GroupsService;
import com.sict.service.NoticeService;
import com.sict.service.OrgService;
import com.sict.service.PracticeTaskService;
import com.sict.service.StudentService;
import com.sict.service.TeaStuService;
import com.sict.util.Common;
import com.sict.util.Constants;
import com.sict.util.DateService;

/**
 * 功能：学生控制器 使用 @Controller 注释 by ccc 20140922*
 * 
 * */
@Controller
public class StudentController {
	@Resource(name = "graduationThesisService")
	private GraduationThesisService graduationThesisService;

	@Resource(name = "filesService")
	private FilesService filesService;

	@Resource(name = "studentService")
	private StudentService studentService;

	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;

	@Resource(name = "graduationMaterialService")
	private GraduationMaterialService graduationMaterialService;

	@Resource(name = "teaStuService")
	private TeaStuService teaStuService;

	@Resource(name = "orgService")
	private OrgService orgService;

	@Resource(name = "noticeService")
	private NoticeService noticeService;

	@Resource(name = "groupsService")
	private GroupsService groupsService;

	/*
	 * 注入：AssociationService 时间：2016-03-14 作者：张向杨
	 */
	@Resource
	private AssociationService associationService;
	/*
	 * 注入：AssociationService 时间：2016-03-14 作者：张向杨
	 */
	@Resource
	private AssociationMembersService associationMembersService;
	@Resource
	FilesDao filesDao;
	String ret = "";// 定义全局变量：返回类型 zcg 20141020

	/**
	 * 学生主页，显示行政班的通讯录类.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/index.do")
	public ModelAndView index(HttpSession session, String current_role_selected) {
		if (current_role_selected != null) {
			session.setAttribute("current_role_selected", current_role_selected);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		List<Org> orgList = orgService
				.selectCollegeByclassId(stu.getClass_id());
		session.setAttribute("stu_college_id", orgList.get(0).getId());

		/* String class_id=stu.getClass_id(); */
		Student s = new Student();
		s.setClass_id(stu.getClass_id());
		s.setState("1");
		List<Student> stulist = studentService.selectList(s);
		map.put("stulist", stulist);
		CommonSession.setUserRole(session, CommonSession.roleStudent);
		return new ModelAndView("student/index", map);
	}

	/**
	 * 论文管理类.
	 * 
	 * @author 楚晨晨、吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/graduationThesisList.do")
	public ModelAndView studentList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		/* String stu_id = stu.getId(); */
		String type = "毕业论文";
		List<GraduationThesis> modelList = this.graduationThesisService
				.selectByStuIdAndType(stu.getId(), type);
		int max_version = 0;
		String check_state;// 得到版本号最大的论文 的审核状态
		if (modelList != null) {
			for (GraduationThesis g : modelList) {
				int version = Integer.parseInt(g.getVersion());
				String progrsss = g.getProgress();
				if (version > max_version) {
					max_version = version;
					check_state = progrsss;
					map.put("check_state", check_state);
				}
			}
		}
		map.put("modelList", modelList);
		return new ModelAndView("student/graduationThesisList", map);
	}

	/**
	 * 转到自己添加毕业论文的页面类.
	 * 
	 * @author 楚晨晨、吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/addGraduationThesis.do")
	public ModelAndView addGraduationThesis(HttpSession session,
			ModelMap modelMap) {
		// 获取当前学生
		Student stu = (Student) session.getAttribute("current_user");
		// 根据学生id得到这个学生的实习任务
		String task_type = "1";
		List<PracticeTask> practice_idList = practiceTaskService
				.getTaskByStuIdAndType(stu.getId(), task_type);
		// 获取当前论文版本+1
		Integer currentVersion = Integer.parseInt(graduationThesisService
				.getCurrentVersion(stu.getId())) + 1;
		for (int i = 0; i < practice_idList.size(); i++) {// 此方法是判断得到的任务能在字典表中能找到，不过如果数据都没错，就不需要用这个方法。
			System.out.println(practice_idList.get(i).getId());
			if (DictionaryService.findPracticeTask(practice_idList.get(i)
					.getId()) == null) {
				practice_idList.remove(i);
			}
		}
		modelMap.put("currentVersion", currentVersion);
		modelMap.put("practice_idList", practice_idList);
		return new ModelAndView("student/graduationThesissAdd", modelMap);
	}

	/**
	 * 保存此版本的毕业论文信息 类.
	 * 
	 * @author 楚晨晨、吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/doAddGraduationThesis.do")
	public String saveGraduationThesis(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute("graduationThesis") GraduationThesis gThesis)
			throws IllegalStateException, IOException {
		Student stu = (Student) session.getAttribute("current_user");
		String version = Common.NulltoBlank(request.getParameter("version"));
		String practice_id = request.getParameter("practice_id");
		String practice_code = DictionaryService.findPracticeTask(practice_id)
				.getPractice_code();
		String thesis_name = request.getParameter("thesis_name");
		String thesis_desc = request.getParameter("content");
		String type = Common.NulltoBlank(request.getParameter("type"));// 获取保存的类型：论文或实训作品
		if (type.equals("")) {
			type = "毕业论文";
		}
		// 获取系统时间
		String time = DateService.formatNowTimeforUpload();
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）,创建一个通用的多部分解析器
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
					String file_type = "Thesis";// file.getContentType();//使用getContentType()方法获得文件类型，以此决定允许上传的文件类型
					if (type.equals("实训作品")) {
						file_type = "Train";
					}
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					String suffix = file.getOriginalFilename().substring(
							file.getOriginalFilename().lastIndexOf("."));
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + practice_code + suffix;
					String real_path = Constants.FILE_ROUTE;
					// String file_path = project_path +
					// filePosition;//文件名称中包含实习任务的code
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();
					String file_name = file.getOriginalFilename();
					file.transferTo(new File(file_path));
					// 文件的属性
					String file_id = Common.returnUUID();
					Files fil = new Files();
					fil.setFile_size(filesize);
					Timestamp d = new Timestamp(System.currentTimeMillis());
					fil.setUpload_time(d);
					fil.setId(file_id);
					fil.setFile_type(file_type);
					fil.setFile_name(file_name);
					fil.setPosition(filePosition);
					fil.setStu_id(stu.getId());
					filesService.insert(fil);
					// 论文属性
					gThesis.setFile_id(file_id);
					gThesis.setStu_id(stu.getId());
					if (type.equals("实训作品")) {
						version = "0";
					}
					gThesis.setVersion(version);
					if (version.equalsIgnoreCase("1")) {
						gThesis.setProgress("初次提交");
					} else {
						gThesis.setProgress("新提交");
					}
					gThesis.setType(type);
					gThesis.setPractice_id(practice_id);
					gThesis.setThesis_name(thesis_name);
					gThesis.setThesis_desc(thesis_desc);
					gThesis.setCreate_time(time);
					gThesis.setComment("暂无");
					gThesis.setScore_type("暂无");
					gThesis.setScore("暂无");
					graduationThesisService.insert(gThesis);
				}
			}
		} else {
			System.out.println("测试测试测试测试测试");
		}
		long endTime = System.currentTimeMillis();
		System.out
				.println("运行时间：" + String.valueOf(endTime - startTime) + "ms");
		if (type.equals("毕业论文")) {
			ret = "redirect:graduationThesisList.do";
		} else if (type.equals("实训作品")) {
			ret = "redirect:trainList.do";
		}
		return ret;
	}

	/**
	 * 转到修改毕业论文信息的页面类.
	 * 
	 * @author 楚晨晨、吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/editGraduationThesis.do")
	public String toEdit(ModelMap modelMap, String id, HttpSession session) {
		GraduationThesis gthesis = (GraduationThesis) graduationThesisService
				.selectByID(id);
		String gthesis_id = gthesis.getId();
		String gthesis_fileid = gthesis.getFile_id();
		String pracode = DictionaryService.findPracticeTask(
				gthesis.getPractice_id()).getPractice_code();
		session.setAttribute("gthesis_id", gthesis_id);
		session.setAttribute("gthesis_fileid", gthesis_fileid);
		session.setAttribute("pracode", pracode);
		modelMap.put("gthesis", gthesis);
		return "student/graduationThesisEdit";
	}

	/**
	 * 毕业论文/实训作品 修改之后保存的操作.
	 * 
	 * @author 楚晨晨、吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/doEditGraduationThesis.do")
	public String doEditGraduation(HttpServletRequest request,
			HttpSession session) throws IllegalStateException, IOException {
		String gthesis_id = (String) session.getAttribute("gthesis_id");
		GraduationThesis graduationThesis = DictionaryService
				.findGraduationThesis(gthesis_id);
		String type = graduationThesis.getType();
		String gthesis_name = request.getParameter("thesis_name");
		String gthesis_desc = request.getParameter("content");
		String gthesis_fileid = (String) session.getAttribute("gthesis_fileid");
		String pracode = (String) session.getAttribute("pracode");
		graduationThesis.setId(gthesis_id);
		graduationThesis.setThesis_name(gthesis_name);
		graduationThesis.setThesis_desc(gthesis_desc);
		String time = DateService.formatNowTimeforUpload();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;// 转换成多部分request
			Iterator iter = multiRequest.getFileNames();// 获取multiRequest
			while (iter.hasNext()) {// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file.getSize() != 0) {// 有文件上传
					String file_type = "Thesis";
					if (type.equals("实训作品")) {
						file_type = "Train";
					}
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					// String filePosition = "WEB-INF/uploadedfiles/" +
					// file_type+ "/" + pracode + suffix;
					String originalFilename = file.getOriginalFilename();
					String suffix = file.getOriginalFilename().substring(
							file.getOriginalFilename().lastIndexOf("."));
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + pracode + suffix;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					Timestamp d = new Timestamp(System.currentTimeMillis());
					if (gthesis_fileid != null) {
						Files f = (Files) filesService
								.selectByID(gthesis_fileid);
						f.setUpload_time(d);
						f.setFile_size(filesize);
						f.setFile_name(originalFilename);
						f.setPosition(filePosition);
						filesService.update(f);
						graduationThesis.setFile_id(f.getId());
					}
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件
				}
			}
		}
		graduationThesisService.update(graduationThesis);
		if (type.equals("毕业论文")) {
			ret = "redirect:graduationThesisList.do";
		} else if (type.equals("实训作品")) {
			ret = "redirect:trainList.do";
		}
		return ret;
	}

	/**
	 * 功能：学生删除此版本的毕业论文 ccc2014-09-17 吴敬国 2015-5-22
	 * */
	@RequestMapping("student/deleteGraduationThesis.do")
	public String deleteGraduationThesis(String id, HttpServletRequest request) {
		GraduationThesis gt = DictionaryService.findGraduationThesis(id);
		String file_id = gt.getFile_id();
		deleteFile(file_id, request);
		graduationThesisService.delete(gt);
		return "redirect:graduationThesisList.do";
	}

	/**
	 * 功能：删除文件表的数据，删除磁盘中的文件 吴敬国 2015-5-22
	 * */
	@RequestMapping("student/deleteFile.do")
	public void deleteFile(String file_id, HttpServletRequest request) {
		if (file_id != null) {// try catch
			Files file = DictionaryService.findFiles(file_id);
			filesService.delete(file);// 删除数据库中表的数据
			String position = file.getPosition();
			String project_path = request.getSession().getServletContext()
					.getRealPath("/");
			String real_path = Constants.FILE_ROUTE;
			String file_path = project_path + real_path + position;
			File fie = new File(file_path); // 路径为文件且不为空则进行删除
			if (fie.isFile() && fie.exists()) {
				fie.delete();
			}
		}
	}

	/**
	 * 功能：就业材料列表 byccc20140918 吴敬国 2015-5-22
	 * */
	@RequestMapping("student/graduationMaterialsList.do")
	public ModelAndView graduationMaterialsList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		List<GraduationMaterials> gm = this.graduationMaterialService
				.selectGraduationMaterialsByStuId(stu_id);
		for (int i = 0; i < gm.size(); i++) {
			if (DictionaryService.findPracticeTask(gm.get(i).getPractice_id()) == null) {
				gm.remove(i);
			}
		}
		map.put("gm", gm);
		return new ModelAndView("student/graduationMaterialsList", map);
	}

	/**
	 * 功能：转到就业材料添加页面 吴敬国 2015-5-22 by ccc20141013 *
	 * 
	 * */
	@RequestMapping("student/addGraduationMaterials.do")
	public ModelAndView addGraduationMaterials(HttpSession session,
			ModelMap modelMap) {
		Student stu = (Student) session.getAttribute("current_user");
		String user_id = stu.getId();
		String task_type = "1";
		// 根据学生id找出了分组成员表再找出用户分组表，得到这个学生有多少个实习任务
		List<PracticeTask> ptList = practiceTaskService.getTaskByStuIdAndType(
				user_id, task_type);
		for (int i = 0; i < ptList.size(); i++) {
			if (DictionaryService.findPracticeTask(ptList.get(i).getId()) == null) {
				ptList.remove(i);
			}
		}
		modelMap.addAttribute("ptList", ptList);
		return new ModelAndView("student/graduationMaterialAdd", modelMap);
	}

	/**
	 * 功能：保存就业材料信息 byccc20141030 吴敬国 2015-5-22
	 * 
	 * @param graduationMaterials
	 * 
	 * */
	@RequestMapping("student/doAddGraduationMaterials.do")
	public String doAddGraduationMaterials(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute("graduationMaterials") GraduationMaterials gm)
			throws IllegalStateException, IOException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String practice_id = request.getParameter("practice_id");
		String materials_type = request.getParameter("materials_type");
		String materials_name = request.getParameter("materials_name");
		// 获取系统时间
		String time = DateService.formatNowTimeforUpload();
		MultipartFile file = null;
		try {
			file = Common.getUpliadFile(request);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch bloc
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		if (!file.isEmpty()) {
			String file_type = "Materials";
			String project_path = request.getSession().getServletContext()
					.getRealPath("/");
			String real_path = Constants.FILE_ROUTE;
			String filePosition = file_type + "/" + file_type + "_" + time
					+ "_" + file.getOriginalFilename();
			String file_path = project_path + real_path + filePosition;
			float filesize = file.getSize();
			String file_name = file.getOriginalFilename();
			file.transferTo(new File(file_path));
			// 文件的属性
			String file_id = Common.returnUUID();
			Files fil = new Files();
			fil.setId(file_id);
			fil.setFile_size(filesize);
			fil.setStu_id(stu_id);
			fil.setUpload_time(DateService.getNowTimeTimestamp());
			fil.setFile_type(file_type);
			fil.setFile_name(file_name);
			fil.setPosition(filePosition);
			filesService.insert(fil);
			// 就业材料属性
			gm.setFile_id(file_id);
			gm.setId(Common.returnUUID());
			gm.setStu_id(stu.getId());
			gm.setCheck_state("0");
			gm.setMaterials_name(materials_name);
			gm.setMaterials_type(materials_type);
			gm.setCreate_time(DateService.getNowTimeTimestamp());
			gm.setPractice_id(practice_id);
			gm.setNote("");
			graduationMaterialService.insert(gm);
		}
		return "redirect:graduationMaterialsList.do";
	}

	/**
	 * 功能：转到学生修改就业材料的页面 by 吴敬国 2015-5-22
	 * 
	 * */
	@RequestMapping("student/editGraduationMaterials.do")
	public String editGraduationMaterials(ModelMap modelMap, String id,
			HttpSession session) {
		GraduationMaterials graduationMaterials = (GraduationMaterials) graduationMaterialService
				.selectByID(id);
		String materials_type = graduationMaterials.getMaterials_type();
		String gthesis_fileid = graduationMaterials.getFile_id();
		session.setAttribute("Materials_fileid", gthesis_fileid);
		modelMap.put("graduationMaterials", graduationMaterials);
		modelMap.put("materials_type", materials_type);
		return "student/graduationMaterialsEdit";
	}

	/**
	 * 就业材料修改之后保存的操作 吴敬国 2015-5-22
	 * 
	 */
	@RequestMapping("student/doEditGraduationMaterials.do")
	public String doEditGraduationMaterials(
			HttpServletRequest request,
			@ModelAttribute("GraduationMaterials") GraduationMaterials graduationMaterials,
			HttpSession session) throws IllegalStateException, IOException {
		String materialsId = request.getParameter("materialsId");
		String materials_type = request.getParameter("materials_type");
		String materials_name = request.getParameter("materials_name");
		String materials_fileid = (String) session
				.getAttribute("Materials_fileid");
		graduationMaterials.setId(materialsId);
		graduationMaterials.setMaterials_name(materials_name);
		graduationMaterials.setMaterials_type(materials_type);
		String time = DateService.formatNowTimeforUpload();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file.getSize() != 0) {
					String file_type = "Materials";
					String originalFilename = file.getOriginalFilename();
					String project_path = request.getSession()
							.getServletContext().getRealPath("/");
					/*
					 * String suffix = file.getOriginalFilename().substring(
					 * file.getOriginalFilename().lastIndexOf("."));
					 */
					/*
					 * String filePosition = "WEB-INF/uploadedfiles/" +
					 * file_type + file.getOriginalFilename();
					 */
					// String real_path ="D:"+"\\"+"uploadedfiles\\";
					String real_path = Constants.FILE_ROUTE;
					String filePosition = file_type + "/" + file_type + "_"
							+ time + "_" + file.getOriginalFilename();
					String file_path = project_path + real_path + filePosition;
					float filesize = file.getSize();

					if (materialsId != null) {
						Files f = (Files) filesService
								.selectByID(materials_fileid);
						f.setUpload_time(DateService.getNowTimeTimestamp());
						f.setFile_size(filesize);
						f.setFile_name(originalFilename);
						f.setPosition(filePosition);
						filesService.update(f);
						graduationMaterials.setFile_id(f.getId());
					}
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件
				}
			}
		}
		Object o = graduationMaterialService.update(graduationMaterials);
		if (o.equals(null)) {
			return null;
		} else {
			return "redirect:graduationMaterialsList.do";
		}
	}

	/**
	 * 学生删除就业材料.
	 * 
	 * @author 楚晨晨、吴敬国 2015-5-22
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/deleteGraduationMaterials.do")
	public String deleteGraduationMaterials(String id,
			HttpServletRequest request) {
		GraduationMaterials gm = DictionaryService.findGraduationMaterials(id);
		String file_id = gm.getFile_id();
		deleteFile(file_id, request);
		graduationMaterialService.delete(gm);
		return "redirect:graduationMaterialsList.do";
	}

	/**
	 * 学生下载文件.
	 * 
	 * @author 吴敬国 2015-5-22
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/downloadFile.do")
	public String download(HttpSession session, HttpServletRequest request,
			HttpServletResponse response, String file_id) throws Exception {
		String project_path = session.getServletContext().getRealPath("/")
				+ "/";
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		Files file1 = (Files) filesService.selectByID(file_id);
		String thesisPosition = file1.getPosition();
		filesService.downloadfile(thesisPosition, ctxPath, request, response);
		return null;
	}

	/**
	 * 学生删除实训作品 .
	 * 
	 * @author 吴敬国 2015-5-22
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/deleteTrain.do")
	public String deleteTrain(String id, HttpServletRequest request) {
		GraduationThesis gt = DictionaryService.findGraduationThesis(id);
		String file_id = gt.getFile_id();
		deleteFile(file_id, request);
		graduationThesisService.delete(gt);
		return "redirect:trainList.do";
	}

	/**
	 * 添加实训作品 .
	 * 
	 * @author 吴敬国 2015-5-22
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/trainList.do")
	public ModelAndView trainList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String type = "实训作品";
		// 根据学生id获得论文和实训作品（论文和实训作品一个表）
		List<GraduationThesis> modelList = this.graduationThesisService
				.selectByStuIdAndType(stu_id, type);
		map.put("modelList", modelList);
		return new ModelAndView("student/trainList", map);
	}

	/**
	 * 添加实训作品 .
	 * 
	 * @author 吴敬国 2014.12.02
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/addTrainWorks.do")
	public ModelAndView addPracticeWorks(HttpSession session, ModelMap modelMap) {
		Student stu = (Student) session.getAttribute("current_user");
		String user_id = stu.getId();
		String task_type = "2";
		List<PracticeTask> practice_idList = practiceTaskService
				.getTaskByStuIdAndType(user_id, task_type);
		for (int i = 0; i < practice_idList.size(); i++) {
			if (DictionaryService.findPracticeTask(practice_idList.get(i)
					.getId()) == null) {
				practice_idList.remove(i);
			}
		}
		modelMap.put("practice_idList", practice_idList);
		return new ModelAndView("student/trainAdd", modelMap);
	}

	/**
	 * 修改实训作品 .
	 * 
	 * @author 吴敬国 2014.12.02
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/editTrainWorks.do")
	public String toEdits(ModelMap modelMap, String id, HttpSession session) {
		GraduationThesis gthesis = (GraduationThesis) graduationThesisService
				.selectByID(id);
		String train_fileid = gthesis.getFile_id();
		String train_id = gthesis.getId();
		String pracode = DictionaryService.findPracticeTask(
				gthesis.getPractice_id()).getPractice_code();
		session.setAttribute("gthesis_fileid", train_fileid);
		session.setAttribute("pracode", pracode);
		session.setAttribute("gthesis_id", train_id);
		modelMap.put("gthesis", gthesis);
		return "student/trainEdit";
	}

	/**
	 * 实践教学资料 下载 李瑶婷 2014.12.19* wjg 2015-4-6
	 * */
	@RequestMapping("student/practiceTaskList.do")
	public ModelAndView practiceTaskList(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String user_id = stu.getId();
		String task_type = "1";
		List<PracticeTask> result = this.practiceTaskService
				.getTaskByStuIdAndType(user_id, task_type);
		map.put("result", result);
		return new ModelAndView("student/practiceTaskList", map);
	}

	/**
	 * 功能：修改密码 by wtt20141106 wjg2015-6-20
	 * */
	@RequestMapping("student/passwordEdit.do")
	public ModelAndView passwordEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Student student = (Student) session.getAttribute("current_user");
		map.put("student", student);
		return new ModelAndView("student/passwordEdit", map);
	}

	/**
	 * 功能：保存修改的密码 by 郑春光20140910 吴敬国2015-6-20 王磊2015年6月23日
	 * */
	@RequestMapping("student/doPasswordEdit.do")
	public String doPasswordEdit(HttpSession session, String id,
			HttpServletRequest request) throws IOException {
		String login_pass2 = request.getParameter("login_pass2");
		Student student = DictionaryService.findStudent(id);
		student.setLogin_pass(login_pass2);
		studentService.update(student);
		session.removeAttribute("current_user");
		return "redirect:../login.jsp"; // 添加成功后重定向到列表页面
	}

	/**
	 * 用户手册下载
	 * 
	 * @author 孙磊、吴敬国2015-6-30 type类型：cjwt 学生常见问题 stuWX 学生微信 stuWeb 学生web sxjd
	 *         实习鉴定表
	 * */

	@RequestMapping("student/helpsDownload.do")
	public String WenJuanDownload(HttpServletRequest request,
			HttpSession session, HttpServletResponse response, String type)
			throws Exception {
		String project_path = request.getSession().getServletContext()
				.getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String ctxPath = project_path + real_path;
		String file_type = "Helps";
		String posistion = "";
		if (type.equalsIgnoreCase("cjwt")) {
			posistion = file_type + "/" + "就业问题解答.doc";
		} else if (type.equalsIgnoreCase("stuWX")) {
			posistion = file_type + "/" + "学生微信端用户手册.doc";
		} else if (type.equalsIgnoreCase("stuWeb")) {
			posistion = file_type + "/" + "学生web端用户手册.doc";
		} else {
			posistion = file_type + "/" + "山东商业职业技术学院实习鉴定表.docx";
		}
		filesService.downloadfile(posistion, ctxPath, request, response);
		return null;
	}

	/**
	 * 学生信息修改.
	 * 
	 * @author 王磊 2015年6月27日
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/personInforEdit.do")
	public ModelAndView personInforEdit(HttpSession session,
			HttpServletRequest request) {
		// 获取当前用户
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		Student student = this.studentService.selectByID(stu_id);
		map.put("student", student);
		return new ModelAndView("student/studentEdit", map);
	}

	/**
	 * 功能保存学生信息修改界面.
	 * 
	 * @author 王磊 2014.12.02
	 * @createDate 2015-7-20
	 * @since 3.0
	 */
	@RequestMapping("student/doEditStudent.do")
	public String editStudent(HttpServletRequest request) throws IOException {
		String studentId = request.getParameter("id");
		Student student = DictionaryService.findStudent(studentId);
		student.setQqnum(request.getParameter("qqnum"));
		student.setId_card(request.getParameter("id_card").trim());
		student.setPhone(request.getParameter("phone"));
		student.setHome_phone(request.getParameter("home_phone"));
		student.setHomepage(request.getParameter("homepage"));
		student.setEmail(request.getParameter("email"));
		student.setHome_addr(request.getParameter("home_addr"));
		studentService.update(student);
		return "redirect:index.do";
	}

	/**
	 * 功能：联系我们 by：吴敬国 2015-7-28
	 * 
	 * */
	@RequestMapping("student/contactUS.do")
	public String contactUS(HttpSession session) {
		return "student/contactUS";
	}

	/**
	 * 照片上传.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-8-7
	 * @since 1.0
	 */
	@RequestMapping("student/photoUpload.do")
	public ModelAndView photoUpload(HttpSession session,
			HttpServletRequest request) {
		Student stu = (Student) session.getAttribute("current_user");
		List<Files> myPhotos = this.filesService.selectStuPhotoByStu_id(stu
				.getId());
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String month1 = "";
		if (month < 10) {
			month1 = "0" + String.valueOf(month);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myPhotos", myPhotos);
		map.put("month", month1);
		return new ModelAndView("student/showPhoto", map);
	}

	/**
	 * 保存照片上传.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-8-7
	 * @since 1.0
	 */
	@RequestMapping("student/doAddPhoto.do")
	public String doAddPhoto(HttpSession session, HttpServletRequest request)
			throws IllegalStateException, IOException {
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String photoMonth = request.getParameter("photoMonth");
		MultipartFile file = null;
		try {
			file = Common.getUpliadFile(request);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch bloc
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		if (!file.isEmpty()) {
			int countFiles = 1;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			Timestamp time1 = new Timestamp(System.currentTimeMillis());
			List<Files> filess = filesDao.selectStuStu_id(stu_id, time1);
			String fromUserName = "";
			if (stu.getWx_code() != null) {
				fromUserName = stu.getWx_code();
			} else {
				fromUserName = stu.getStu_code();
			}
			if (filess.size() != 0) {
				for (int i = 0; i < filess.size(); i++) {
					if (filess.get(i).getFile_name().substring(0, 7)
							.equalsIgnoreCase((year + "-" + photoMonth))) {
						countFiles++;
					}
				}
			}
			String fileNames = "/" + year + "-" + photoMonth + "00"
					+ countFiles + fromUserName + ".jpg";
			String project_path = request.getSession().getServletContext()
					.getRealPath("/");
			String fileurl = project_path + "/uploadedfiles/Photos";
			String postion = fileurl + fileNames;
			float filesize = file.getSize();
			String fileName = year + "-" + photoMonth + "00" + countFiles
					+ fromUserName + ".jpg";
			file.transferTo(new File(postion));
			// 文件的属性
			String file_id = Common.returnUUID();
			Files fil = new Files();
			fil.setId(file_id);
			fil.setFile_size(filesize);
			fil.setStu_id(stu_id);
			fil.setUpload_time(DateService.getNowTimeTimestamp());
			fil.setFile_type("4");
			fil.setFile_name(fileName);
			fil.setPosition(postion);
			filesService.insert(fil);

		}
		return "redirect:photoUpload.do";
	}

	/**
	 * 通知管理 类.
	 * 
	 * @author 王磊 2014.12.02
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/seeMyNotice.do")
	public ModelAndView seeMyNotice(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		String stu_id = stu.getId();
		String college_id = Common.getCollegeByStuID(stu_id);
		List<Notice> myNoticeList = noticeService.stuGetMyNotice(college_id,
				stu_id);
		map.put("myNoticeList", myNoticeList);
		return new ModelAndView("student/myNoticeList", map);
	}

	/**
	 * 教师查看通知公告 类.
	 * 
	 * @author 吴敬国
	 * @createDate 2015-7-16
	 * @since 3.0
	 * 
	 */
	@RequestMapping("student/detailNotice.do")
	public String detailNotice(ModelMap modelMap, String id) {
		Notice n = (Notice) noticeService.selectByID(id);
		modelMap.put("notice", n);
		return "student/noticeDetail";
	}

	/**
	 * 查看学生会 或者社团干部 查看本部门的信息.
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping("student/seeStuUnonOrAssociation.do")
	public String seeStuUnonOrAssociation(ModelMap modelMap, String deptId) {

		Association association = new Association();
		association.setSa_category("1");
		association.setSa_college_id("1");
		association.setState("1");
		List<Association> associationList = associationService
				.selectList(association);

		modelMap.put("associationList", associationList);

		return "student/seeStuUnonOrAssociation";
	}

	/**
	 * 查看学生会 或者社团干部 具体部门的成员信息.
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping("student/seeStuUnionOrAssNumberdetail.do")
	public String seeStuUnionOrAssNumberdetail(ModelMap modelMap, String id) {
		String sam_association_id = null;
		if (id != null) {
			sam_association_id = id;
			AssociationMembers associationMembers = new AssociationMembers();
			associationMembers.setSam_association_id(sam_association_id);
			List<AssociationMembers> associationMembersList = associationMembersService
					.selectList(associationMembers);

			if (associationMembersList != null) {
				modelMap.put("associationMembersList", associationMembersList);
			}
		}

		modelMap.put("id", sam_association_id);
		return "student/showStuUnionOrAssNumber";
	}

	/**
	 * 添加 学生会 或者社团干部成员.
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping("student/stuUnionOrAssAdd.do")
	public String stuUnionOrAssAdd(ModelMap modelMap, String id) {
		modelMap.put("id", id);

		return "student/stuUnionOrAssAdd";
	}

	/**
	 * 导入学生会 或者社团干部的成员信息.
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping("student/importStuUnionOrAssNumber.do")
	public String importStuUnionOrAssNumber(ModelMap modelMap, String id) {
		String type = null;

		if (id != null) {
			AssociationMembers associationMembers = associationMembersService
					.selectByID(id);
			if (associationMembers.getSam_association_id().equals("1")) {
				type = "StudentUnionExcel";
			} else if (associationMembers.getSam_association_id().equals("2")) {
				type = "AssociationExcel";
			} else {// 没有该类型 返回null
				return null;
			}
			modelMap.put("id", id);
			modelMap.put("type", type);
		}
		return "student/importStuUnionOrAssNumber";
	}

	/**
	 * 验证导入的学生会 或者社团干部的成员信息.
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping(value = "student/checkStuUnionOrAssNumber.do", method = RequestMethod.POST)
	public String checkStuUnionOrAssNumber(MultipartHttpServletRequest request,
			ModelMap modelMap, HttpSession sesson) throws Exception {

		String type = Common.NulltoBlank(request.getParameter("type"));

		// 取到用户提交来的excel文件
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 不为空就要操作文件、
		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = request;
			// 遍历得到用户提交的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next()
						.toString());
				if (file != null) {

					String file_type = "";
					if (type.equals("StudentUnionExcel")) {
						file_type = "StudentUnionExcel";
					} else if (type.equals("AssociationExcel")) {
						file_type = "AssociationExcel";
					}

					String project_path = request.getSession()
							.getServletContext().getRealPath("/");

					String fileName = file.getOriginalFilename();
					String filepos = "Import" + "/" + file_type + "_";
					String filePosition = "Import" + "/" + file_type + "_"
							+ fileName;
					String real_path = Constants.FILE_ROUTE;
					String file_path = project_path + real_path + filePosition;
					String file_pa = project_path + real_path + filepos;
					// float filesize = file.getSize();//
					// 使用getSize()方法获得文件长度，以此决定允许上传的文件大小。
					file.transferTo(new File(file_path));// 使用transferTo（dest）方法将上传文件写到服务器上指定的文件
					// 文件的属性
					File f = new File(file_pa + fileName);
					// 判断导入数据表类型

					HttpSession session = request.getSession();

					String StuUnionOrAsso_id = request
							.getParameter("StuUnionOrAsso_id");
					session.setAttribute("StuUnionOrAsso_id", StuUnionOrAsso_id);

					// 获得excel文件中的数据封装成java对象放进集合
					List<Student> stuList = ExcelService
							.analysisStrUnionOrAssciationSutdent(f);

					session.setAttribute("students", stuList);
					// 对学生表的数据验证编号是否重复
					List<String> codeList = new ArrayList<String>();// 声明集合，存储学生编号，为了验证表格中的学生编号是否重复。
					List<String> classidList = new ArrayList<String>();// 声明集合，存储表格中班级编号，为得到每个班级的人数。
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
					for (Student student : stuList) {
						String stuCode = student.getStu_code();
						b = this.studentService.selectByStuCode(stuCode);
						if (student.getStu_code() == null) {
							infor = infor + "学号不能为空,";
						} else if (b == 0) {
							infor = "请检查您的学号 数据库没有该学生学号,";
						} else if (student.getStu_code().substring(0, 4)
								.equals(student.getEntry_year())) {
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

						if (codeList.size() != 0) {
							infor = infor
									+ isCodeExist(student.getStu_code(),
											codeList, "学生编号");
						}
						;
						if (infor.trim().equals("")) {
							infor = "无";
						}
						student.setTemp1(infor.trim());
						infor = "";
						if (student.getStu_code() != null) {
							codeList.add(student.getStu_code());
						}
						;
						// 判断学生编号是否在excel中重复
						String[] co = new String[codeList.size()];
						for (int a = 0; a < codeList.size(); a++) {
							co[a] = codeList.get(a);
						}

					}

					modelMap.put("ss", stuList);
					modelMap.put("type", type);
					modelMap.put("fileName", fileName);
				}
			}
		}
		return "student/importStuUnionOrAssNumber";
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
	 * 
	 * 
	 * @author 张向杨
	 * @createDate 2016-03-14
	 * @since 3.0
	 * 
	 */

	@RequestMapping("student/saveStuUnionOrAss.do")
	public String importStuUnionOrAssSuccess(HttpSession session,
			ModelMap modelMap, String id, String fileName, String type) {
		String project_path = session.getServletContext().getRealPath("/");
		String real_path = Constants.FILE_ROUTE;
		String filepos = "Import" + "/" + type + "_";
		// 所要保存至数据库的excel的名称
		/*
		 * String file_pa = real_path + "WEB-INF/uploadedfiles/Import/" +
		 * fileName;
		 */
		String file_pa = project_path + real_path + filepos + fileName;
		File f = new File(file_pa);

		List<Student> stuList = (List<Student>) session
				.getAttribute("students");
		// 获得系统当前时间
		Date date = new Date();
		Timestamp nousedate = new Timestamp(date.getTime());
		// 获得用户导入的类型的id 学生会 或者 社团的id
		String StuUnionOrAsso_id = (String) session
				.getAttribute("StuUnionOrAsso_id");

		for (Student student : stuList) {
			String associationMembersID = Common.returnUUID();
			Student student2 = (Student) studentService.selectByCode(student
					.getStu_code());
			AssociationMembers associationMembers = new AssociationMembers();

			associationMembers.setState("1");
			associationMembers.setSam_stu_id(student2.getId());
			associationMembers.setSam_duty(student.getTemp2());
			associationMembers.setBegin_time(nousedate);
			associationMembers.setSam_association_id(StuUnionOrAsso_id);
			associationMembersService.insert(associationMembers);
		}
		modelMap.put("s", stuList);
		return "student/importStuUnionOrAssSuccess";
	}

	/*
	 * 功能： 保存添加的学生会或者社团人员 时间：　２０１６－０３－１８
	 */
	@RequestMapping("student/doSaveStuUnionOrAssMumber.do")
	public String doSaveStuUnionOrAssMumber(HttpServletRequest request) {
		// 获得前台表单数据
		String stu_code = request.getParameter("stu_code");
		String sex = request.getParameter("sex");
		String duties = request.getParameter("duties");
		String association_id = request.getParameter("id");
		String stu_id = studentService.getStudentIdByCode(stu_code);
		// 获得当天系统时间
		Date date = new Date();
		Timestamp nousedate = new Timestamp(date.getTime());
		// 新建对象 用于插入数据库
		AssociationMembers associationMembers = new AssociationMembers();

		associationMembers.setState("1");
		associationMembers.setSam_duty(duties);
		associationMembers.setBegin_time(nousedate);
		associationMembers.setSam_stu_id(stu_id);
		associationMembers.setSam_association_id(association_id);
		// 插入到数据库
		associationMembersService.insert(associationMembers);
		return "redirect:seeStuUnionOrAssNumberdetail.do?id=1";
	}

	/**
	 * 功能： 保存添加的学生会或者社团人员 时间：　２０１６－０３－１８
	 */
	@RequestMapping("student/checkStuCode.do")
	public String checkStuCode(String stu_code, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		StringBuilder stringBuilder = new StringBuilder();
		String info = "";
		// 根据学生的学号 得到学生的id
		String stu_id = studentService.getStudentIdByCode(stu_code);
		// 判断该学生 是否在数据库中
		if (stu_id != null) {
			AssociationMembers associationMembers = new AssociationMembers();
			associationMembers.setSam_stu_id(stu_id);

			// 判断社团或者学生还没有添加 该学生
			if (associationMembersService.selectList(associationMembers).size() == 0) {
				info = "学号可以使用";
			} else {
				info = "学号已经在该组织内";
			}
		} else {
			info = "系统没有该学号";
		}
		stringBuilder.append(info);

		try {
			response.getWriter().println(stringBuilder.toString());
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 功能： 删除照片
	 * by周睿20160329
	 */
	@RequestMapping("student/photoDelete.do")
	public String photoDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		Files files = filesService.selectByID(id);
		File file = new File(files.getPosition());
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		filesService.delete(files);
		return "redirect:photoUpload.do";
	}
	/**
	 * 功能： 修改实习状态
	 * by周睿20160330
	 */
	@RequestMapping("student/stateEdit.do")
	public ModelAndView stateEdit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Student stu = (Student) session.getAttribute("current_user");
		map.put("stu", stu);
		
		return new ModelAndView("student/stateEdit",map);
	}
	/**
	 * 功能： 修改实习状态
	 * by周睿20160330
	 */
	@RequestMapping("student/doStateEdit.do")
	public String doStateEdit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) {
		Student stu = (Student) session.getAttribute("current_user");
		String practice_state=request.getParameter("state");
		stu.setPractice_state(practice_state);
		studentService.update(stu);
		return "redirect:index.do";
	}


}