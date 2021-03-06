package com.neusoft.oa.attendance.web.servlet.vacate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.oa.attendance.entity.VacateEntity;

import com.neusoft.oa.attendance.function.employess.VacateFunction;

import com.neusoft.oa.core.OAException;
import com.neusoft.oa.core.dto.PaginationQueryResult;
import com.neusoft.oa.core.service.FunctionFactory;
import com.neusoft.oa.core.util.ThisSystemUtil;
import com.neusoft.oa.core.web.servlet.CommonServlet;
/**
 * @author 田梦源
 *
 */

@WebServlet("/attendance/employess/vacate/list.do")
public class VacateListServlet extends CommonServlet{
	@Override
	protected String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 1获取查询
		String id=this.getCurrentUserId(req);
		String key = req.getParameter("key");
		String pageSize=req.getParameter("pageSize");
		int pageSizeInt=ThisSystemUtil.parseInt(pageSize, -1);
		String pageNo=req.getParameter("pageNo");
		int pageNoInt=ThisSystemUtil.parseInt(pageNo, -1);
		// 2调用业务方法
		try {
			VacateFunction fun=FunctionFactory.getFunction(VacateFunction.class);
			PaginationQueryResult<VacateEntity> result = fun.listVacate(id,key, pageNoInt, pageSizeInt);
			
			req.setAttribute("vacateresult", result);
			// 3确定视图
		} catch (OAException e) {
			req.setAttribute("message", e.getMessage());
		}
		return "/jsp/attendance/employess/vacateRecord.jsp";
	}
}