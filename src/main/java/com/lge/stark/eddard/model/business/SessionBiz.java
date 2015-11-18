package com.lge.stark.eddard.model.business;

import com.lge.stark.eddard.model.Session;
import com.lge.stark.eddard.mybatis.SessionMapper;
import com.lge.stark.eddard.mybatis.SqlConnector;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

public class SessionBiz {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SessionBiz.class);

	private static SessionBiz instance;

	public static SessionBiz instance() {
		if (instance == null) {
			instance = new SessionBiz();
		}

		return instance;
	}

	public Session getSession(String id) {
		SqlSessionEx sqlSession = SqlConnector.openSession(false);

		try {
			Session ret = sqlSession.getMapper(SessionMapper.class).selectByPrimaryKey(id);

			sqlSession.commit();

			return ret;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		finally {
			sqlSession.close();
		}
	}
}