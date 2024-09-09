package com.trav.cms.banner.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.trav.cms.banner.dao.BanOperator;
import com.trav.cms.banner.domain.Banner;

/**
 * Servlet implementation class BannerAdd
 */
@WebServlet("/admin/BannerAdd")
public class BannerAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	BanOperator banOperator = new BanOperator();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BannerAdd() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// 上下文
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true,
				8 * 1024, true);

		// 上传到目录个数
		int count = 0;

		String banname = null;
		String bantitle = null;
		String banpath = null;
		Banner banner = null;

		System.out.println("轮播图片上传");
		// 新建一个SmartUpload对象
		SmartUpload su = new SmartUpload();
		// 上传初始化
		su.initialize(pageContext);

		// 设置上传限制
		// 1.限制每个上传文件的最大长度为10MB
		su.setMaxFileSize(10 * 1024 * 1024);
		// 2.限制总上传文件的长度
		su.setTotalMaxFileSize(30 * 1024 * 1024);
		// 3.设定允许上传的文件
		su.setAllowedFilesList("gif,jpg,png,jpeg,bmp,ico");
		// 4.设定禁止上传的文件
		try {
			su.setDeniedFilesList("exe,bat,jsp,htm,html,,");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("不允许上传此类文件");
			e.printStackTrace();
		}

		// 上传文件
		try {
			su.upload();
		} catch (SmartUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 将上传的文件全部保存到指定目录
		// 图片轮播的路径

		try {
			count = su.save("/image/banner");

		} catch (SmartUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(count + "个文件上传成功！");

		// 利用Request对象获取参数之值

		// 逐一提取上传文件信息，同时可保存文件
		for (int i = 0; i < su.getFiles().getCount(); i++) {
			File file = su.getFiles().getFile(i);
			// 若文件不存在则继续
			if (file.isMissing()) {
				continue;
			}
			banname = su.getRequest().getParameter("ban-name");
			bantitle = su.getRequest().getParameter("ban-title");
			banpath = "image/banner/" + file.getFileName();
			banner = new Banner(banname, bantitle, banpath);
			if (banOperator.addBan(banner)) {
				System.out.println("getFileName***--" + file.getFilePathName());
				out.flush();// 清空缓存
				out.println("<script>");// 输出script标签
				out.println("alert('轮播图图片上传成功!');");// js语句：输出alert语句
				out.println("history.back();");// js语句：输出网页回退语句
				out.println("</script>");// 输出script结尾标签

				// 显示当前文件信息
				/*out.println("轮播图图片上传成功");
				out.println("<br/>图片名：" + su.getRequest().getParameter("ban-name") + "<br/><br/>");
				out.println("<br/>标题名：" + su.getRequest().getParameter("ban-title") + "<br/><br/>");
				out.println("<br/>标题名：" + "image/banner/" + file.getFileName() + "<br/><br/>");
				

				out.println("<TABLE BORDER=1>");
				out.println("<TR><TD>表单项名(FieldName)</TD><TD>" + file.getFieldName() + "</TD></TR>");
				out.println("<TR><TD>文件长度(Size)</TD><TD>" + file.getSize() + "</TD></TR>");
				out.println("<TR><TD>文件名(FileName)</TD><TD>" + file.getFileName() + "</TD></TR>");
				out.println("<TR><TD>文件扩展名(FileExt)</TD><TD>" + file.getFileExt() + "</TD></TR>");
				out.println("<TR><TD>文件全名(FilePathName)</TD><TD>" + file.getFilePathName() + "</TD></TR>");
				out.println("</TABLE><BR/>");*/
			}

			/*
			 * // 将文件另存为 try { file.saveAs("/image/banner" + file.getFileName(),
			 * SmartUpload.SAVE_VIRTUAL); file.saveAs("E:/Web//" +
			 * file.getFileName(), SmartUpload.SAVE_PHYSICAL); } catch
			 * (SmartUploadException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */

		}

	}

}
