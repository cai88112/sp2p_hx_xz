package com.fp2p.helper;

import java.io.File;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件操作工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class FileHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(FileHelper.class
			.getName());

	/**
	 * 文件操作工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private FileHelper() {
	}

	/**
	 * 创建目录.
	 * 
	 * @param path
	 *            文件路径
	 */
	public static void mkdirs(final String path) {
		logger.debug("进入mkdirs方法");
		try {
			if (path == null) {
				throw new NullPointerException("path为空指针");
			}

			File uploadFilePath = new File(path);
			// 如果该目录不存在,则创建
			if (!uploadFilePath.exists()) {
				uploadFilePath.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出mkdirs方法");
	}

	/**
	 * 创建自定义文件名.
	 * 
	 * @return 创建的文件名
	 */
	public static String getFileName() {
		logger.debug("进入getFileName方法");
		String result = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
				+ new Random().nextInt(Integer.parseInt("10000"));
		logger.debug("退出getFileName方法");
		return result;
	}

	/**
	 * 返回指定路径下的所有文件.
	 * 
	 * @param path
	 *            路径
	 * @return 指定路径下的文件数组
	 */
	public static File[] getFiles(final String path) {
		logger.debug("进入getFiles方法");
		File[] files = null;
		try {
			if (path == null) {
				throw new NullPointerException("path为空指针");
			}
			File folder = new File(path);
			folder.mkdirs();
			// 得到当前文件和子文件
			files = folder.listFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getFiles方法");
		return files;
	}

	/**
	 * 删除文件.
	 * 
	 * @param file
	 *            待删除的文件
	 */
	public static void removeFile(final File file) {
		logger.debug("进入removeFile方法");
		// 判断一个文件是否存在
		if (file.exists()) {
			file.delete();
		}
		logger.debug("退出removeFile方法");
	}
}
