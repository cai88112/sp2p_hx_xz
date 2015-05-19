package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.ConnectionManager;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.dao.LinksDao;
import com.sp2p.dao.admin.AgreementDao;
import com.sp2p.dao.admin.ClauseDao;
import com.sp2p.dao.admin.MessageDao;
import com.sp2p.dao.admin.SuccessStoryDao;
import com.sp2p.dao.admin.TeamDao;

/**
 * 宣传管理模块（包括团队管理、信息管理、平台原理、法律政策的等）
 * 
 * @author Administrator
 * 
 */
public class PublicModelService extends BaseService {

	public static Log log = LogFactory.getLog(PublicModelService.class);
	private ConnectionManager connectionManager;
	private TeamDao teamDao;
	private MessageDao messageDao;
	private LinksDao linksDao;
	private AgreementDao agreementDao;
	private SuccessStoryDao successStoryDao;
	private ClauseDao clauseDao;

	public ClauseDao getClauseDao() {
		return clauseDao;
	}

	public void setClauseDao(ClauseDao clauseDao) {
		this.clauseDao = clauseDao;
	}

	public SuccessStoryDao getSuccessStoryDao() {
		return successStoryDao;
	}

	public void setSuccessStoryDao(SuccessStoryDao successStoryDao) {
		this.successStoryDao = successStoryDao;
	}

	public AgreementDao getAgreementDao() {
		return agreementDao;
	}

	public void setAgreementDao(AgreementDao agreementDao) {
		this.agreementDao = agreementDao;
	}

	public LinksDao getLinksDao() {
		return linksDao;
	}

	public void setLinksDao(LinksDao linksDao) {
		this.linksDao = linksDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public TeamDao getTeamDao() {
		return teamDao;
	}

	public void setTeamDao(TeamDao teamDao) {
		this.teamDao = teamDao;
	}

	/**
	 * 添加团队信息
	 * 
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addTeam(Integer sort, String userName, String imgPath,
			String intro, String publishTime) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = teamDao.addTeam(conn, sort, userName, imgPath, intro,
					publishTime);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 根据团队信息
	 * 
	 * @param id
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateTeam(Long id, Integer sort, String userName,
			String imgPath, String intro, String publishTime) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = teamDao.updateTeam(conn, id, sort, userName, imgPath,
					intro, publishTime);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 删除团队信息
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long deleteTeam(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = teamDao.deleteTeam(conn, id);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 删除团队介绍的数据
	 * 
	 * @param commonIds
	 *            id拼接字符串
	 * @param delimiter
	 *            分割符
	 * @throws DataException
	 * @throws SQLException
	 * @return int
	 */
	public int deleteTeam(String commonIds, String delimiter)
			throws Exception {
		Connection conn = MySQL.getConnection();
		int result = -1;
		try {
			result = teamDao.deleteTeam(conn, commonIds, delimiter);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 根据Id获取团队信息详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getTeamById(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = teamDao.getTeamById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public void queryTeamPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_team", "*", "order by sort asc",
					"AND state=1");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询团队信息列表
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> findListTeam() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = null;
		try {
			list = teamDao.queryTeamList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * add by houli 查找表里最大的排列序号
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getTeamMaxSerial() throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return teamDao.getMaxSerial(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * add by houli 查看sortid是否存在
	 * 
	 * @param sortId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long isExistSortId(int sortId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			Map<String, String> map = teamDao.isExistSortId(conn, sortId);
			if (map == null || map.get("sort") == null)
				return 1L;
			else
				return -1L;

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	public Long isExistToUpdate(int sortId, int originalSortId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			Map<String, String> map = teamDao.isExistToUpdate(conn, sortId,
					originalSortId);
			if (map == null || map.get("sort") == null)
				return 1L;
			else
				return -1L;

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	// 以上为团队管理模块
	// 下为信息管理模块
	/**
	 * 添加信息管理
	 * 
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addMessage(Integer sort, String columName, String content,
			String publishTime) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = messageDao.addMessage(conn, sort, columName, content,
					publishTime);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 更新信息管理
	 * 
	 * @param id
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateMessage(Long id, Integer sort, String columName,
			String content, String publishTime) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = messageDao.updateMessage(conn, id, sort, columName,
					content, publishTime);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 删除信息管理
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long deleteMessage(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = messageDao.deleteMessage(conn, id);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 根据Id获取团队信息详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getMessageById(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = messageDao.getMessageById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 根据信息管理类型查询信息详情
	 * 
	 * @param conn
	 * @param typeId
	 *            类型
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getMessageByTypeId(Integer typeId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = messageDao.getMessageByTypeId(conn, typeId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 分页获取信息管理列表
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryMessagePage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_message", "*", "order by sort asc",
					"typeId=2");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 获取信息管理列表
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> findMessageList() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = messageDao.queryMessageList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	// 以上是信息管理
	// 以下是友情链接
	/**
	 * 添加友情链接
	 */
	public Long addLinks(String companyName, String companyImg,
			String companyURL, long serialCount, String dateTime)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long commonId = -1L;
		Long newsId = -1L;
		try {
			commonId = linksDao.addLinks(conn, companyName, companyImg,
					companyURL, serialCount, dateTime);
			if (commonId < 0) {// 如果小于0，添加失败，回滚
				conn.rollback();
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return newsId;
	}

	/**
	 * 友情链接列表
	 * 
	 * @param pageBean
	 * @throws DataException
	 * @throws SQLException
	 */
	public void queryLinksPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_links", "*", "", "   and type = 1 ");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 投资广告
	 * 
	 * @param pageBean
	 * @throws DataException
	 * @throws SQLException
	 */
	public void queryLinksGGPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " t_links ", "*",
					"  order by ordershort asc ", " and type = 2 ");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 首页图片滚动
	 * 
	 * @param pageBean
	 * @throws DataException
	 * @throws SQLException
	 */
	public void queryBannerListPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " t_links ", "*",
					"  order by ordershort asc ", " and type = 3 ");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 删除友情链接
	 * 
	 * @param commonIds
	 * @throws SQLException
	 * @throws DataException
	 */
	public void deleteLinkss(String commonIds) throws Exception,
			DataException {
		Connection conn = MySQL.getConnection();
		try {
			linksDao.deleteLinks(conn, commonIds);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 友情链接信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queyLinksInfoList() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		try {
			maplist = linksDao.queyLinksInfoList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return maplist;
	}

	/**
	 * 查询链接
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryLinksInfoByid(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = linksDao.queyLinksInfoById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 更新连接信息
	 * 
	 * @param companyName
	 * @param companyImg
	 * @param companyURL
	 * @param serialCount
	 * @param dateTime
	 * @return
	 * @throws SQLException
	 */
	public Long updateLinks(String companyName, String companyImg,
			String companyURL, long serialCount, String dateTime)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			linksDao.updateLinks(conn, companyName, companyImg, companyURL,
					serialCount, dateTime);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return 1L;
	}

	/**
	 * 添加首页滚动图片
	 * 
	 * @return
	 * @author Administrator
	 */
	public Long addIndexRollImg(String companyImg, long serialCount,
			String dateTime, long ordershort, int cardStatus,String companyURL)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long commonId = -1L;
		Long newsId = -1L;
		try {
			commonId = linksDao.addIndexRollImg(conn, companyImg, serialCount,
					dateTime, ordershort, cardStatus, companyURL);
			if (commonId < 0) {// 如果小于0，添加失败，回滚
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return newsId;
	}

	/**
	 * 修改首页滚动图片
	 * 
	 * @return
	 * @author Administrator
	 */
	public Long updateIndexRollImg(String companyImg, long serialCount,
			String dateTime, long ordershort,String companyURL) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			linksDao.updateIndexRollImg(conn, companyImg, serialCount,
					ordershort, dateTime, companyURL);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return 1L;
	}

	public void queryIndexRollImgPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_links", "*",
					" order by type desc,ordershort asc ", "   AND type !=1");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	// end

	/**
	 * 添加新问题列表的时候，首先获得序列号
	 * 
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> getMaxIndexRollImgSerial() throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return linksDao.getMaxIndexRollImgSerial(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 添加新问题列表的时候，首先获得序列号
	 * 
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> getLinkMaxSerial() throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return linksDao.getMaxSerial(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	// 以下为后台协议
	/**
	 * 更新协议
	 * 
	 * @param id
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateAgreement(Integer agreementType, Integer provisionType,
			String content, String title) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = agreementDao.updateAgreement(conn, agreementType,
					provisionType, content, title);
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 获取协议详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getAgreementDetail(Integer agreementType,
			Integer provisionType) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = agreementDao.getAgreementDetail(conn, agreementType,
					provisionType);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	// 以下为成功故事管理
	/**
	 * 添加成功故事信息
	 * 
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addSuccessStory(String sort, String title, String content,
			String publishTime, String publisher, String browseNum,
			String imgPath) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = successStoryDao.addSuccessStory(conn, sort, title,
					content, publishTime, publisher, browseNum, imgPath);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 更新成功故事信息
	 * 
	 * @param id
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateSuccessStory(Long id, String sort, String title,
			String content, String publishTime, String publisher,
			Long browseNum, String imgPath) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = successStoryDao.updateSuccessStory(conn, id, sort, title,
					content, publishTime, publisher, browseNum, imgPath);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 删除成功故事
	 * 
	 * @param conn
	 * @param ids
	 *            id字符串拼接
	 * @param delimiter
	 *            拼接符号
	 * @return long
	 * @throws DataException
	 * @throws SQLException
	 */
	public int deleteSuccessStory(String commonIds, String delimiter)
			throws Exception {
		Connection conn = MySQL.getConnection();
		int result = -1;
		try {
			result = successStoryDao.deleteSuccessStory(conn, commonIds,
					delimiter);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 根据Id获取团队信息详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getSuccessStoryById(Long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = successStoryDao.getSuccessStoryById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 成功故事页面
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void querySuccessStoryPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_successstory", "*",
					"order by publishTime desc", "");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 前台获取成功故事信息页面
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void frontQuerySuccessStoryPage(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			dataPage(conn, pageBean, "t_successstory", "*",
					"order by publishTime desc", "");
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	// 以下为条款信息编辑

	/**
	 * 添加协议条款
	 */
	public Long addClause(String columnName, String content, int typeId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long commonId = -1L;
		Long newsId = -1L;
		try {
			commonId = clauseDao.addClause(conn, columnName, content, typeId);
			if (commonId < 0) {// 如果小于0，添加失败，回滚
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return newsId;
	}

	/**
	 * 查询协议条款
	 * 
	 * @param pageBean
	 * @param id
	 * @throws DataException
	 * @throws SQLException
	 */
	public void queryClausePage(PageBean<Map<String, Object>> pageBean, int id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_message", "*", "", " and typeId=" + id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	public void deleteClauses(String commonIds) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			clauseDao.deleteClause(conn, commonIds);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryClauseInfoByid(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = clauseDao.queyClauseInfoById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public Long updateClause(String columnName, String content, int id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			clauseDao.updateClause(conn, columnName, content, id);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return 1L;
	}

	/**
	 * 添加新问题列表的时候，首先获得序列号
	 * 
	 * @throws DataException
	 * @throws SQLException
	 */
	// public Map<String,String> getMaxSerial() throws SQLException,
	// DataException{
	// Connection conn = MySQL.getConnection();
	// try{
	// return clauseDao.getMaxSerial(conn);
	// }finally {
	// conn.close();
	// }
	// }
	/**
	 * 查找借款协议范本
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryBorrowClause(String title, int typeId)
			throws Exception {
		StringBuffer where = new StringBuffer(" 1=1 ");
		Map<String, String> map = null;
		if (typeId >= 0) {
			where.append(" AND typeId = " + typeId);
		}
		if (title != null && StringUtils.isNotBlank(title)) {// title代表问题内容的部分信息
			where.append(" AND columName LIKE \'%"
					+ StringEscapeUtils.escapeSql(title) + "%\'");
		}
		Connection conn = MySQL.getConnection();
		try {
			map = clauseDao.queryBorrowClause(conn, where.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

}
