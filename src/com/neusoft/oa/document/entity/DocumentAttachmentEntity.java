package com.neusoft.oa.document.entity;

import java.time.Instant;

import com.neusoft.oa.organization.entity.DepartmentEntity;
import com.neusoft.oa.organization.entity.EmployeeEntity;
import com.neusoft.oa.system.entity.SysUserEntity;

public class DocumentAttachmentEntity {
	private String id;//附件编号
	private DocumentEntity documentId;//附件所属文档
	private String path;//附件位置
	private String name;//附件名称
	private String property;//附件属性说明
	private EmployeeEntity createUserId;//附件创建者
	private Instant createTime;//附件创建时间
	private String remark;//备注
	private int flag;//状态标志 0在回收站1正常2被占用
	private DepartmentEntity deptId;//所属部门id
	private String type;//附件类型
	private long size;//附件大小
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DocumentEntity getDocumentId() {
		return documentId;
	}
	public void setDocumentId(DocumentEntity documentId) {
		this.documentId = documentId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public EmployeeEntity getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(EmployeeEntity createUserId) {
		this.createUserId = createUserId;
	}
	public Instant getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public DepartmentEntity getDeptId() {
		return deptId;
	}
	public void setDeptId(DepartmentEntity deptId) {
		this.deptId = deptId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
	

}
