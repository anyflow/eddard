package com.lge.stark.eddard.mybatis;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.lge.stark.eddard.Settings;

public class SqlConnector {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SqlConnector.class);
	private static SqlSessionFactory factory = null;

	private SqlConnector() {
	}

	static {
		try {
			factory = (new SqlSessionFactoryBuilder())
					.build(Resources.getResourceAsStream(SqlConnector.class.getClassLoader(),
							Settings.SELF.getProperty("mybatis.configPath")), Settings.SELF);
		}
		catch (IOException e) {
			logger.error("Building SQL session factory failed.", e);
		}
	}

	public static SqlSessionFactory getSessionFactory() {
		return factory;
	}

	public static SqlSessionEx openSession(boolean readOnly) {
		SqlSession session = factory.openSession();
		try {
			if (session.getConnection().isClosed()) {
				session.close();
				session = factory.openSession();
			}
		}
		catch (SQLException e) {
			logger.error("Opening SQL session failed.", e);
		}
		catch (Exception e) {
			logger.error("SQL session failed.", e);
		}

		return new SqlSessionEx(session, readOnly);
	}
}