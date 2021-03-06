package com.neusoft.oa.document.recycle.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.oa.core.OAException;
import com.neusoft.oa.core.dto.AjaxResponse;
import com.neusoft.oa.core.service.FunctionFactory;
import com.neusoft.oa.core.web.servlet.CommonServlet;
import com.neusoft.oa.document.recycle.function.RecycleBinFunction;

import static com.neusoft.oa.core.util.AssertThrowUtil.*;
/**
 * 文档还原
 * @author zhoujingmeng
 *
 */
@WebServlet("/document/recycle/documentmod.do")
public class RestoreDocumentServlet extends CommonServlet {

	
	private static final long serialVersionUID = -3183005778809700151L;
	
	@Override
	protected String handleRequest(HttpServletRequest req, HttpServletResponse resp)throws Throwable {
		//1验证参数
		String id=$("获取文档编号",req.getParameter("id"));
		resp.setContentType("text/html");
			
		//2调用业务方法
		RecycleBinFunction rFun=FunctionFactory.getFunction(RecycleBinFunction.class);
		try {	
			rFun.restoreDocument(id);
			req.setAttribute("message", "数据已还原!");
		} catch (OAException e) {
			req.setAttribute("message", e.getMessage());
		}
		return "/jsp/base/document/recyclemanage/list.jsp";
	}
	
}
