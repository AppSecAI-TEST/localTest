package com.sict.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sict.biz.GetImage;
import com.sict.entity.Files;
import com.sict.entity.Invitation;
import com.sict.entity.PracticeTask;
import com.sict.entity.ReplyNotice;
import com.sict.entity.Student;
import com.sict.entity.Teacher;
import com.sict.entity.User;
import com.sict.service.DictionaryService;
import com.sict.service.FilesService;
import com.sict.service.InvitationService;
import com.sict.service.PracticeTaskService;
import com.sict.service.ReplyNoticeService;
import com.sict.service.StudentService;
import com.sict.service.TeacherService;
import com.sict.service.UserService;
import com.sict.util.Common;
import com.sict.util.Constants;

/*
 * 功能：手机端bbs论坛
 * 使用 @Controller 注释
 * byxzw 2015年11月17日10:36:58	 * 
 * 
 * */
@Controller
public class MobileBBSUserController {

	/**
	 * 注入userService
	 * 
	 * */
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "invitationService")
	private InvitationService invitationService;

	@Resource(name = "teacherService")
	private TeacherService teacherService;

	@Resource(name = "studentService")
	private StudentService studentService;
	
	@Resource
	FilesService filesService;

	@Resource(name = "replyNoticeService")
	private ReplyNoticeService replyNoticeService;
	
	@Resource(name = "practiceTaskService")
	private PracticeTaskService practiceTaskService;
	//获取常量分页页大小
	private int pageSizeConstant=Constants.pageSize; 
	
	@RequestMapping("weixin/getInvitations.do")
	public String getTeacherByPage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		//置顶帖
		List<Invitation> topInvitationList = this.invitationService
				.selectTapInvititons(type);
		//类别贴
		//List<Invitation> invitationList = this.invitationService.selectParentInvititons(type);
		session.setAttribute("bbs_endPage",10);
		session.setAttribute("bbs_startPage",0);
		int endpage=(Integer)session.getAttribute("bbs_endPage");
		List<Invitation> invitationList = this.invitationService.getPageDate(0,endpage,type);
		StringBuffer sb = new StringBuffer();
		if(topInvitationList.size()>0){
			for (Invitation in : topInvitationList) {
				sb.append("<li onclick=\"toParticulars('" + in.getId() + "')\">");
				sb.append("<span class='badge bg-pomegranate white'>置顶</span>&nbsp;&nbsp;");
				// 发帖主题
				sb.append("<B>" + in.getTitle() + "</B>");
				sb.append("</li>");
			}
			
		}
		//分割线
		//sb.append("<div style='background-color: #EBEDF0;height: 8px;'></div>");
		for (Invitation in : invitationList) {
			sb.append("<li onclick=\"toParticulars('" + in.getId() + "')\">");
			//头像地址
			//String url= request.getContextPath()+ "/AgileLite/assets/app/img/logo-exmobi.png";
			String url=Common.getUserPhotoUrl(in.getUser_id());
;			sb.append("<img " + "src='"+url+ " 'class='img appimg' style='width: 45px;height: 45px;'/>");
			sb.append("<div class='text'>");
			// 发帖人
			String posUserId = in.getUser_id();
			User user = Common.getUser(posUserId);
			sb.append(user.getTrue_name());
			sb.append("(" + user.getClass_name() + ")");
			sb.append("<i class='right iconfont iconline-chat' style='size: 1em;'>&nbsp;");
			// 回帖数量
			int count = this.invitationService.selcetPerCount(in.getId());
			sb.append(count);
			sb.append("</i> <small>");
			// 发帖时间
			sb.append(in.getCreate_time());
			sb.append("</small></div><div style='margin-top: 15px'>");
			// 发帖主题
			sb.append("<B>" + in.getTitle() + "</B>");
			// 发帖内容
			String content = in.getContent();
			if (content.length() > 100) {
				content = content.substring(0, 100);
				content = content + "......";
			}
			sb.append("<br />" + content + "</div></li>");
			//sb.append("<div style='background-color: #EBEDF0;height: 8px;'></div>");
		}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 上拉分页ajax
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/getUpPageInvitations.do")
	public String getUpPageInvitations(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		//类别贴
		//List<Invitation> invitationList = this.invitationService.selectParentInvititons(type);
		int endpage=(Integer) session.getAttribute("bbs_endPage");
		int startpage=(Integer) session.getAttribute("bbs_startPage");
		Map<String, Integer> pageMap=Common.getPage(startpage, endpage,session);
		int start=pageMap.get("startPage");
		int end=pageMap.get("endPage");
		List<Invitation> invitationList = this.invitationService.getPageDate(start,end,type);
		StringBuffer sb = new StringBuffer();
		//分割线
		//sb.append("<div style='background-color: #EBEDF0;height: 8px;'></div>");
		for (Invitation in : invitationList) {
			sb.append("<li onclick=\"toParticulars('" + in.getId() + "')\">");
			//头像地址
			//String url= request.getContextPath()+ "/AgileLite/assets/app/img/logo-exmobi.png";
			String url=Common.getUserPhotoUrl(in.getUser_id());
;			sb.append("<img " + "src='"+url+ " 'class='img appimg' style='width: 45px;height: 45px;'/>");
			sb.append("<div class='text'>");
			// 发帖人
			String posUserId = in.getUser_id();
			User user = Common.getUser(posUserId);
			sb.append(user.getTrue_name());
			sb.append("(" + user.getClass_name() + ")");
			sb.append("<i class='right iconfont iconline-chat' style='size: 1em;'>&nbsp;");
			// 回帖数量
			int count = this.invitationService.selcetPerCount(in.getId());
			sb.append(count);
			sb.append("</i> <small>");
			// 发帖时间
			sb.append(in.getCreate_time());
			sb.append("</small></div><div style='margin-top: 15px'>");
			// 发帖主题
			sb.append("<B>" + in.getTitle() + "</B>");
			// 发帖内容
			String content = in.getContent();
			if (content.length() > 100) {
				content = content.substring(0, 100);
				content = content + "......";
			}
			sb.append("<br />" + content + "</div></li>");
			//sb.append("<div style='background-color: #EBEDF0;height: 8px;'></div>");
		}
			if (invitationList.size() == 0) {
			}else if(invitationList.size()<=15){
				sb.append("<li class='sliver'style='text-align:center'>暂无更新</li>");
			}
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 返回主页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/index.do")
	public ModelAndView index(HttpServletRequest request, HttpSession session)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("bbs/user/index", map);
	}

	/**
	 * 发帖
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toPosting.do")
	public ModelAndView toPosting(HttpServletRequest request,
			HttpSession session) throws IOException {
		String url= Constants.ServerURL + "/weixin/toPosting.do";
		Map<String, String> wxMap=Common.getWxMap(url);
		wxMap.put("appId", Constants.appid);
		return new ModelAndView("bbs/user/posting", wxMap);
	}

	/**
	 * 测试wxjs
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/test.do")
	public ModelAndView test(HttpServletRequest request,
			HttpSession session) throws IOException {
		String url= Constants.ServerURL + "/weixin/test.do";
		Map<String, String> wxMap=Common.getWxMap(url);
		wxMap.put("appId", Constants.appid);
		return new ModelAndView("bbs/user/test", wxMap);
	}

	/**
	 * 个人中心
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toPerson.do")
	public ModelAndView toPerson(HttpServletRequest request, HttpSession session)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = Common.getUser(session);
		map.put("user", user);
		//查询本人的通知
		List<ReplyNotice> replyList=replyNoticeService.selectNotReadNotice(user.getId());
		int notReadcount=replyList.size();
		map.put("notReadcount", notReadcount);
		return new ModelAndView("bbs/user/person", map);
	}

	/**
	 * 个人详细
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toPersonDetails.do")
	public ModelAndView PersonDetails(HttpServletRequest request,
			HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = Common.getUser(session);
		map.put("user", user);
		return new ModelAndView("bbs/user/personDetails", map);
	}

	/**
	 * 个人主题
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toPersonTheme.do")
	public ModelAndView personTheme(HttpServletRequest request,
			HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String stu_id=request.getParameter("stu_id");
		User user;
		//个人主题
		if(stu_id==null){
			user = Common.getUser(session);
		}else{
			//教师查看学生主题
			user=Common.getUser(stu_id);
		}
		List<Invitation> inlist = this.invitationService.selcetPersonInvititons(user.getId());
		map.put("inlist", inlist);
		map.put("user", user);
		return new ModelAndView("bbs/user/personTheme", map);
	}

	/**
	 * 看帖
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toParticulars.do")
	public ModelAndView toParticulars(HttpServletRequest request,
			HttpSession session) throws IOException {
		//用于转发到不同也面
		String type=request.getParameter("type");
		Map<String, Object> map = new HashMap<String, Object>();
		String inId = request.getParameter("inId");
		Invitation in = (Invitation) invitationService.selectByID(inId);
		User user=null;
		try {
			 user = Common.getUser(in.getUser_id());
		} catch (Exception e) {
			return new ModelAndView("bbs/user/deleteErroy");
		}
		List<Invitation> sonInList = new ArrayList<Invitation>();
		List<Invitation> tsonInList = invitationService
				.selectSonInvititons(inId);
		// 获取子回复者的姓名
		if (tsonInList.size() > 0) {
			for (Invitation invi : tsonInList) {
				String user_id = invi.getUser_id();
				String name;
				if (user_id.length() > 16) {
					Student stu = DictionaryService.findStudent(user_id);
					name = stu.getTrue_name()
							+ "("
							+ DictionaryService.findOrg(stu.getClass_id())
									.getOrg_name() + ")";
				} else {
					Teacher tea = DictionaryService.findTeacher(user_id);
					name = tea.getTrue_name()
							+ "("
							+ DictionaryService.findOrg(tea.getDept_id())
									.getOrg_name() + ")";
				}
				invi.setUseName(name);
				sonInList.add(invi);
			}
			map.put("sonInList", sonInList);
		}
		List<Files> files=filesService.getFileByInid(in.getId());
		if(files.size()>0){
			map.put("files", files);
		}
		map.put("in", in);
		map.put("type", user.getType());
		if(type.equals("1")){
			return new ModelAndView("bbs/user/particulars", map);
		}else {
			return new ModelAndView("bbs/user/replyNotice", map);
		}
		
	}
	
	/**
	 * 查看回复
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/replyNotice.do")
	public ModelAndView replyNoticec(HttpServletRequest request,
			HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String reId=request.getParameter("reId");
		ReplyNotice reply=(ReplyNotice) this.replyNoticeService.selectByID(reId);
		reply.setIsRead('1');
		//更新为已读
		this.replyNoticeService.update(reply);
		String inId=reply.getInvitation_id();
		Invitation in=(Invitation) this.invitationService.selectByID(inId);
		map.put("reply", reply);
		map.put("in", in);
		return new ModelAndView("redirect:toParticulars.do?inId="+reply.getInvitation_id()+"&&type=3");
	
	}
	
	/**
	 * 保存帖子
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/savePost.do")
	public ModelAndView savePost(HttpServletRequest request, HttpSession session)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = Common.getUser(session);
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String media_ids=request.getParameter("media_ids");
		String type = request.getParameter("type");
		if(type==null){	
			type="1";
		}
		Invitation in = new Invitation();
		String id=Common.returnUUID16();
		in.setId(id);
		in.setType_id(type);
		in.setContent(content);
		in.setTitle(title);
		in.setUser_id(user.getId());
		in.setTop("2");
		in.setCreate_time(Common.getNowTime());
		in.setUpdate_time(Common.getNowTime());
		this.invitationService.insert(in);
		
		String [] media_id=null;
		if(media_ids.length()>0){
			media_id=media_ids.split("---");
			String project_path = request.getSession().getServletContext().getRealPath("/");
			String photoUrl = project_path+ "uploadedfiles/BBSPhotos";
			String filePath="";
			for(int i=0;i<media_id.length;i++){
				filePath=Common.downloadMedia( media_id[i], photoUrl);
				Files files = new Files();
				files.setFile_type("25");
				files.setFile_name(id);
				files.setPosition(filePath);
				files.setFile_size(10);
				files.setId(Common.returnUUID());
				files.setStu_id(user.getId());
				filesService.insertFiles(files);
			}
		}
		
		/*List<String> urllist=Common.getImgUrlList(media_id);
		String url=urllist.get(0);
		byte[] btImg = GetImage.getImageFromNetByUrl(url);*/
		// String trueTitle = new String(title.getBytes("iso-8859-1"),"utf-8");
		// String truecontent = new
		// String(content.getBytes("iso-8859-1"),"utf-8");
		return new ModelAndView("bbs/user/index", map);
	}

	/**
	 * 回复帖子
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/postParticulars.do")
	public String postParticulars(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String content = request.getParameter("content");
		String truecontent = new String(content.getBytes("iso-8859-1"), "utf-8");
		String inId = request.getParameter("inId");
		User user = Common.getUser(session);
		Invitation Parintion = (Invitation) this.invitationService
				.selectByID(inId);
		Invitation invitation = new Invitation();
		invitation.setContent(truecontent);
		invitation.setCreate_time(Common.getNowTime());
		invitation.setParents_id(inId);
		String id=Common.returnUUID16();
		invitation.setId(id);
		invitation.setType_id(Parintion.getType_id());
		invitation.setUser_id(user.getId());
		invitation.setTop("2");
		//更新父贴时间 （新回复置顶）
		Invitation pIn = (Invitation) this.invitationService.selectByID(inId);
		pIn.setUpdate_time(Common.getNowTime());
		this.invitationService.update(pIn);
		this.invitationService.insert(invitation);
		//所有参与此贴的用户
		List<String> inlist=this.invitationService.getUserIdByinId(inId);
		for(int i=0;i<inlist.size();i++){
			String  us_id=inlist.get(i);
			//发送消息
			ReplyNotice rn=new ReplyNotice();
			rn.setId(Common.returnUUID16());
			rn.setContent(truecontent);
			rn.setCreate_time(Common.getNowTime());
			rn.setInvitation_id(inId);
			rn.setIsRead('2');
			rn.setState('1');
			rn.setIssuer_id(us_id);
			//回复人id暂时未启用
			rn.setReply_id(us_id);
			this.replyNoticeService.insert(rn);
		}
		//ajax返回数据
		StringBuffer sb = new StringBuffer();
		sb.append("<li>");
		//头像
		String url=Common.getUserPhotoUrl(user.getId());
		sb.append("<img src='" +url+"class='img appimg' style='width: 35px;height: 35px;'/>");
		sb.append("<div class='text'>");
		sb.append(user.getTrue_name());
		sb.append("(" + user.getClass_name() + ")");
		sb.append("<small>" + invitation.getCreate_time() + "</small>");
		sb.append("</div>");
		sb.append("<div style='margin-top: 15px'>");
		sb.append(invitation.getContent());
		sb.append("</div>");
		sb.append("</li>");
		try {
			response.getWriter().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除帖子
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/deleteTheme.do")
	public String deleteTheme(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		Invitation in = new Invitation();
		in.setId(id);
		this.invitationService.delete(in);
		try {
			response.getWriter().println("1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除帖子
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/toStuList")
	public ModelAndView stulist(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		User user = Common.getUser(session);
		String grade=Common.getDefaultYear();
		List<PracticeTask> pklist=(List<PracticeTask>) this.practiceTaskService.getOutPracticeTasksBytea_id(user.getId(), grade);
		Map<String, Object> map = new HashMap<String, Object>();
		if(pklist.size()>0){
			PracticeTask pk=pklist.get(0);
			List<Student> stulist=this.studentService.getStuByPracticeTaskId(pk.getId());
			map.put("stulist", stulist);
			map.put("pk", pk);
		}
		return new ModelAndView("bbs/user/stuList", map);

	}
	
	
	/**
	 * 消息列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("weixin/noticeList")
	public ModelAndView NoticeList(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		User user = Common.getUser(session);
		//查询本人的通知
		List<ReplyNotice> replyList=replyNoticeService.selectNotReadNotice(user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("replyList", replyList);
		return new ModelAndView("bbs/user/replyNoticeList", map);

	}
}