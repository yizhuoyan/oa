package com.neusoft.oa.system.web.servlet.usermanage.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.oa.core.OAException;
import com.neusoft.oa.core.dto.AjaxResponse;
import com.neusoft.oa.core.service.FunctionFactory;
import com.neusoft.oa.core.web.servlet.CommonServlet;
import com.neusoft.oa.system.ao.SysUserAo;
import com.neusoft.oa.system.function.AdministratorFunction;
@WebServlet("/api/system/user/lock")
public class LockServlet extends CommonServlet {

	@Override
	protected AjaxResponse handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 1获取参数
		String id = req.getParameter("id");
		// 2调用业务方法
		AdministratorFunction fun=FunctionFactory.getFunction(AdministratorFunction.class);
		fun.lockUser(id);
		return AjaxResponse.ok();
	}
}
