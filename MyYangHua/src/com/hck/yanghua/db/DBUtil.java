package com.hck.yanghua.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class DBUtil {

	private Context context;
	private Dao<MsgInviteBean, Integer> msgInviteDao;
	private DatabaseHelper helper;

	public DBUtil(Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			msgInviteDao = helper.getDao(MsgInviteBean.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private <T> RuntimeExceptionDao<T, Long> getDao(Class<T> clazz) {
		return helper.getRuntimeExceptionDao(clazz);
	}

	/**
	 * 增加邀请信息.
	 * 
	 */
	public void add(MsgInviteBean msgInviteBean) {
		try {
			msgInviteDao.createOrUpdate(msgInviteBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deleteMsgInviteMsg(MsgInviteBean msgInviteBean) {
		try {
			msgInviteDao.delete(msgInviteBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMsgInviteMsg(MsgInviteBean msgInviteBean) {
		try {
			msgInviteDao.update(msgInviteBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MsgInviteBean getOneMsgInfoByUid(String uid) {
		try {
			return msgInviteDao.queryBuilder().where().eq("userMsgId", uid).and()
					.eq("state", 0).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private <T> QueryBuilder<T, Long> getQueryBuilder(Class<T> clazz) {
		return getDao(clazz).queryBuilder();
	}

	public List<MsgInviteBean> getInviteBeans() {
		try {
			return getQueryBuilder(MsgInviteBean.class).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
