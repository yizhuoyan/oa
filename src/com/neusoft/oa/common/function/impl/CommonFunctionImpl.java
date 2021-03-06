package com.neusoft.oa.common.function.impl;

import static com.neusoft.oa.core.util.AssertThrowUtil.$;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.oa.common.function.CommonFunction;
import com.neusoft.oa.core.OAException;
import com.neusoft.oa.core.dao.DaoFactory;
import com.neusoft.oa.core.dto.UserContext;
import com.neusoft.oa.core.email.SendEmailUtil;
import com.neusoft.oa.core.email.TextEmail;
import com.neusoft.oa.core.service.TranscationManger;
import com.neusoft.oa.core.service.TranscationManger.Transcation;
import com.neusoft.oa.core.util.ThisSystemUtil;
import com.neusoft.oa.core.util.TokenUtil;
import com.neusoft.oa.organization.dao.EmployeeDao;
import com.neusoft.oa.system.dao.SysModuleDao;
import com.neusoft.oa.system.dao.SysUserDao;
import com.neusoft.oa.system.entity.SysModuleEntity;
import com.neusoft.oa.system.entity.SysUserEntity;

/**
 * 处理通用相关的业务逻辑
 * 
 * @author yizhuoyan@hotmail.com
 *
 */
public class CommonFunctionImpl implements CommonFunction {
	SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
	
	private void lockUser(String userId) {
		// 锁定账户（独立事务）
		Transcation newTranscation = TranscationManger.beginNewTranscation();
		try {
			udao.update(userId, "flag", SysUserEntity.FLAG_LOCK);
			newTranscation.commit();
		} catch (Exception e) {
			e.printStackTrace();
			newTranscation.rollback();
		}
	}
	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param password
	 * @return 用户上下文
	 * @throws Throwable
	 */
	public UserContext login(String account, String password, String loginIP) throws Exception {
		// 1参数清理和验证
		// 1.1清理前后空格
		// 1.2验证
		account = $("账号", account);
		password = $("密码", password);

		// 2业务逻辑判断

		// 2.1账号是否存在
		
		SysUserEntity u = udao.select("account", account);
		if (u == null) {
			EmployeeDao employeeDao = DaoFactory.getDao(EmployeeDao.class);
			u = employeeDao.select("workEmail", account);
			if (u == null) {
				throw new OAException("账号不存在！");
			}
		}
		// 2.2账号密码是否一致
		password = ThisSystemUtil.md5(password);
		if (!u.getPassword().equals(password)) {
			throw new OAException("账号和密码不匹配！");
		}
		// 2.3账户是否锁定
		if (u.getFlag() == SysUserEntity.FLAG_LOCK) {
			throw new OAException("账号已锁定，请联系管理员！");
		}
		
		// 2.4判断最后密码修改时间是否已经超过三个月
		Date lastModPasswordTime = u.getLastModPasswordTime();
		if (lastModPasswordTime != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastModPasswordTime);
			cal.add(Calendar.MONTH, 3);
			long threeMonthLater = cal.getTimeInMillis();
			// 当前时间大于最后一次修改密码的三个月后
			if (System.currentTimeMillis() > threeMonthLater) {
				this.lockUser(u.getId());
				// 2.3 账号是否已经锁定
				throw new OAException("由于您长时间未修改账号密码，您的账号已被管理员锁定，请联系管理员解除锁定");
			}
		}

		// 2.5 账号是否长时间未登录（超过3个月则自动锁定）
		Date lastLoginTime = u.getLastLoginTime();
		if (lastLoginTime != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastLoginTime);
			cal.add(Calendar.MONTH, 3);
			long threeMonthLater = cal.getTimeInMillis();
			// 当前时间大于最后一次修改密码的三个月后
			if (System.currentTimeMillis() > threeMonthLater) {
				this.lockUser(u.getId());
				throw new OAException("由于您长时间未登录，此账号已被管理员锁定，请联系管理员");
			}
		}
		// 2.6更新登陆时间和登陆ip
		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("LastLoginTime", new Date());
		updateMap.put("lastLoginIp", loginIP);
		udao.update(u.getId(), updateMap);

		// 3组装业务结果
		UserContext uc = new UserContext();
		uc.setAccount(account);
		uc.setId(u.getId());
		uc.setName(u.getName());
		uc.setAvatar(u.getAvatar());
		// 保存上一次登陆时间
		uc.setLastLoginTime(u.getLastLoginTime());
		// 保存上一次登陆ip
		uc.setLastLoginIp(u.getLastLoginIP());
		uc.setSecurityEmail(u.getSecurityEmail());
		uc.setLastModPasswordTime(u.getLastModPasswordTime());
		return uc;
	}

	/**
	 * 用户修改密码
	 * 
	 * @param 用户账号或用户id
	 * @param 旧密码
	 * @param 新密码
	 * @param 新密码确认
	 * 
	 */
	@Override
	public void modifyPassword(String userId, String oldPassword, String newPassword, String newPasswordConfirm)
			throws Exception {
		// 1验证清理参数
		userId = $("用户id", userId);
		oldPassword = $("原密码", oldPassword);
		newPassword = $("新密码", newPassword);

		// 2业务逻辑
		// 2.1 验证两次密码一致
		if (!newPassword.equals(newPasswordConfirm)) {
			throw new OAException("两次密码不一致");
		}
		oldPassword = ThisSystemUtil.md5(oldPassword);
		newPassword = ThisSystemUtil.md5(newPassword);
		// 2.2 userId
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
		SysUserEntity u = udao.select("id", userId);
		if (u == null) {
			OAException.throwWithMessage("用户【?】不存在", userId);
		}
		// 2.3 验证旧密码

		if (!u.getPassword().equals(oldPassword)) {
			throw new OAException("原密码不正确");
		}
		// 2.4 旧密码和新密码不能一样
		if (u.getPassword().equals(newPassword)) {
			throw new OAException("新密码不能和原密码一致");
		}

		// 2.4 验证新密码的合法性

		// 3进行密码更新
		udao.update(userId, "password", newPassword);

	}

	@Override
	public List<SysModuleEntity> loadUserModule(String userId) throws Exception {
		userId = $("userId", userId);
		SysModuleDao mdao = DaoFactory.getDao(SysModuleDao.class);
		List<SysModuleEntity> result = mdao.selectsByUserId(userId);
		return result;
	}

	@Override
	public int loadTotalAccount() throws Exception {
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
		return udao.countUser();
	}

	@Override
	public void modifySecurityEmail(String userId, String email) throws Exception {
		userId = $("用户id", userId);
		email = $("密保邮箱", email);
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
		udao.update(userId, "securityEmail", email);
	}

	@Override
	public void modifyUserName(String userId, String name) throws Exception {
		// 1验证清理参数
		userId = $("用户id", userId);
		name = $("新名称", name);

		// 2业务逻辑
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
		SysUserEntity u = udao.select("id", userId);
		if (u == null) {
			OAException.throwWithMessage("用户【?】不存在", userId);
		}
		// 2.3
		if (!u.getName().equals(name)) {
			udao.update(userId, "name", name);
		}

	}

	@Override
	public String sendModifySecurityEmailValidteCodeEmail(String userId, String email) throws Exception {
		userId = $("userId", userId);
		email = $("email", email);
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);
		SysUserEntity u = udao.select("id", userId);
		if (u == null) {
			OAException.throwWithMessage("用户【?】不存在", userId);
		}

		TextEmail m = new TextEmail();
		m.setFromName("东软OA-安全系统");
		m.setRecipientAccount(email);
		m.setSubject(String.format("%s申请修改密保邮箱确认码", u.getName()));
		String appPath = System.getProperty("app-path");
		String url = System.getProperty("email.modify-security-email-url", "/user/modifySecurityEmail.do");
		url = appPath + url;
		url += "?";
		String validateCode = TokenUtil.random(8);
		url += "code=" + validateCode;
		url += "&email";
		url += email;
		StringBuilder content = new StringBuilder();
		content.append("请在3分钟内点击下面链接进行确认修改密保邮箱。");

		content.append("<a href='");
		content.append(url);
		content.append("'>");
		content.append(url);
		content.append("</a>");
		m.setContent(content.toString());
		SendEmailUtil.sendTextEmail(m);
		return validateCode;
	}

	@Override
	public void modifyUserAvatar(String userId, String newAvatar) throws Exception {
		userId = $("用户id", userId);
		newAvatar = $("头像地址", newAvatar);
		SysUserDao udao = DaoFactory.getDao(SysUserDao.class);

		udao.update(userId, "avatar", newAvatar);

	}

}
