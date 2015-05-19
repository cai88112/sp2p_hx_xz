/**   
 * @Title: CoinDao.java 
 * @Package com.sp2p.dao 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月19日 上午9:41:46 
 * @version V1.0   
 */
package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;

/**
 * 金币系统Dao.
 * 
 * @ClassName: CoinDao
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月19日 上午9:41:46
 * 
 */
public class CoinDao {

	/**
	 * 初始化金币记录.
	 * 
	 * @param conn
	 * @param userid
	 *            用户id.
	 * @return
	 * @throws SQLException
	 */
	public Long addCoin(Connection conn, Long userid) throws SQLException {
		Dao.Tables.t_coin t_coin = new Dao().new Tables().new t_coin();
		t_coin.userid.setValue(userid);
		t_coin.sumcoin.setValue(0.00);
		t_coin.usedcoin.setValue(0.00);
		return t_coin.insert(conn);
	}

	/**
	 * 更新金币
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param addCoin
	 *            增加的金币.
	 * @param usedCoin
	 *            减少的金币.
	 * @return
	 */
	public Long updateCoin(Connection conn, Long userId, Double addCoin,
			Double usedCoin) throws SQLException {
		long returnId = -1;
		String command = "update t_coin set sumcoin = sumcoin + " + addCoin
				+ ",usedcoin = usedcoin +" + usedCoin + " where userid ="
				+ userId;
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 添加金币记录.
	 * 
	 * @param conn
	 * @param userid
	 *            用户id.
	 * @param addcoin
	 *            增加的金币.
	 * @param reducecoin
	 *            消费的金币.
	 * @param remark
	 *            备注.
	 * @return
	 */
	public Long addCoinDetail(Connection conn, Long userid, Double addcoin,
			Double reducecoin, String remark) throws SQLException {
		Dao.Tables.t_coin_detail t_coin_detail = new Dao().new Tables().new t_coin_detail();
		t_coin_detail.userid.setValue(userid);
		t_coin_detail.addcoin.setValue(addcoin);
		t_coin_detail.reducecoin.setValue(reducecoin);
		t_coin_detail.addtime.setValue(new Date());
		t_coin_detail.remark.setValue(remark);
		return t_coin_detail.insert(conn);
	}

}
