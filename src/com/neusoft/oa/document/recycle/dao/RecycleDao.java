package com.neusoft.oa.document.recycle.dao;

import java.util.List;

import com.neusoft.oa.core.dao.TemplateDao;
import com.neusoft.oa.document.recycle.entity.RecycleEntity;

public interface RecycleDao extends TemplateDao<RecycleEntity>{
	int selectsByKey(String key, int pageNo, int pageSize, List<RecycleEntity> pageData)throws Exception;
}
