package com.sict.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sict.service.CoreService;
import com.sict.util.SignUtil;

/**
 * 请求处理的核心类
 * 
 * @author mengfanzhen
 * @date 2014-07-02
 */
@Controller
public class CoreServlet extends HttpServlet {
	// http://jngczx.jsp.jspee.cn/springmvc_mybatis/wx/index.do
	// http://www.dszweb.tk/springmvc_mybatis/wx/index.do
	Log log = LogFactory.getLog(this.getClass());

	private static final long serialVersionUID = 4440739483644821986L;
	@Resource
	CoreService coreService;

	/**
	 * 请求校验（确认请求来自微信服务器）
	 */
	@RequestMapping(value = "wx/index.do", method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("调用doGet方法");

		log.debug("调用doGet方法");
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();

	
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	// c校验时首先会判断是不是微信服务器发过来的东西
	/**
	 * 请求校验与处理
	 * @param session 
	 */

	@RequestMapping(value = "wx/index.do", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("调用doPost方法");

		log.debug("调用doPost方法");
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 接收参数微信加密签名、 时间戳、随机数
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		log.debug("!!!!!" + signature + "!!!!" + timestamp);

		PrintWriter out = response.getWriter();
		// 请求校验
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			// 调用核心服务类接收处理请求
			String respXml = coreService.processRequest(request);
			out.print(respXml);
			log.debug("!!!!!!!!!" + respXml);
		}
		out.close();
		out = null;
	}
}
