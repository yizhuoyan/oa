package com.neusoft.oa.organization.web.empmanage.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.oa.core.service.FunctionFactory;
import com.neusoft.oa.core.web.servlet.CommonServlet;
import com.neusoft.oa.organization.function.OrganizationFunction;
@WebServlet("/api/organization/employee/nextAccount")
public class GenerateEmployeeAccountServlet extends CommonServlet{

	@Override
	protected Object handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		String departmentId=req.getParameter("departmentId");
		String hiredate=req.getParameter("hiredate");
		
		OrganizationFunction function=FunctionFactory.getFunction(OrganizationFunction.class);
		String result=function.generateNextEmployeeAccount(departmentId, hiredate);
		return result;
	}
	
}
