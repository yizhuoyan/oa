package com.neusoft.oa.message.web.email;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.oa.core.OAException;
import com.neusoft.oa.core.dto.PaginationQueryResult;
import com.neusoft.oa.core.dto.UserContext;
import com.neusoft.oa.core.service.FunctionFactory;
import com.neusoft.oa.core.web.servlet.CommonServlet;
import com.neusoft.oa.message.entity.EmailEntity;
import com.neusoft.oa.message.function.EmailFunction;

@WebServlet("/email/ListAllEmailServlet.do")
public class ListAllEmailServlet extends CommonServlet {

	private static final long serialVersionUID = -1214810556886291457L;

	@Override
	protected String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 1获取查询
		String key = req.getParameter("key");
		System.out.println(key);
		String pageSize = req.getParameter("pageSize");
		System.out.println(pageSize);
		String pageNo = req.getParameter("pageNo");
		System.out.println(pageNo);
		// 2调用业务方法
		try {
			EmailFunction fun = FunctionFactory.getFunction(EmailFunction.class);
			PaginationQueryResult<EmailEntity> result = fun.queryAllEmailByKey(key, pageNo, pageSize);
			req.setAttribute("result", result);
//			req.setAttribute("total", result.getTotalRows());
//			req.setAttribute("user", uc.getName());
			// 3确定视图
		} catch (OAException e) {
			req.setAttribute("message", e.getMessage());
		}
		return "/jsp/message/email/listAllEmail.jsp";
	}
}
