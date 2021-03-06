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
@WebServlet("/api/system/user/mod")
public class ModServlet extends CommonServlet {

	@Override
	protected AjaxResponse handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 1获取参数
		String id = req.getParameter("id");
		String account = req.getParameter("account");
		String name = req.getParameter("name");
		String remark = req.getParameter("remark");
		String flag = req.getParameter("flag");
		// 2调用业务方法
		SysUserAo ao = new SysUserAo();
		ao.setAccount(account);
		ao.setName(name);
		ao.setRemark(remark);
		ao.setFlag(flag);
		AdministratorFunction fun=FunctionFactory.getFunction(AdministratorFunction.class);
		fun.modifySysUser(id, ao);
		return AjaxResponse.ok();
	}
}
