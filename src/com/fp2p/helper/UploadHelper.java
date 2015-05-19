package com.fp2p.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.vo.FileCommon;
import com.shove.vo.Files;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 处理上传功能的工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class UploadHelper {
	/**
	 * 1kb.
	 */
	public static final int ONE_K_BYTE = 1024;

	/**
	 * 四分之一kb.
	 */
	public static final int ONE_QUARTER_K_BYTE = 256;

	/**
	 * 私有的构造方法.
	 */
	private UploadHelper() {
	}

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(UploadHelper.class
			.getName());

	/**
	 * 上传文件公共方法.
	 * 
	 * @param file
	 *            文件
	 * @param source
	 *            路径
	 * @param fileName
	 *            文件名
	 */
	private static void uploadByFile(final File file, final String source,
			final String fileName) {
		logger.debug("进入uploadByFile方法");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (file == null) {
				throw new NullPointerException("file为空指针.");
			}
			if (source == null) {
				throw new NullPointerException("source为空指针.");
			}
			if (fileName == null) {
				throw new NullPointerException("fileName为空指针.");
			}

			if (file.length() == 0) {
				new File(source + fileName).createNewFile();
			} else {
				fis = new FileInputStream(file);
				fos = new FileOutputStream(source + "/" + fileName);
				// 读取字节流
				byte[] bt = new byte[ONE_K_BYTE];
				int real = fis.read(bt);
				while (real > 0) {
					fos.write(bt, 0, real);
					real = fis.read(bt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("退出uploadByFile方法");
	}

	/**
	 * 使用完整参数上传文件.
	 * 
	 * @param file
	 *            文件
	 * @param fileCommon
	 *            文件参数
	 * @param realpath
	 *            文件路径
	 * @return 上传结果
	 */
	public static String getByAllParams(final Files file,
			final FileCommon fileCommon, final String realpath) {
		logger.debug("进入getByAllParams方法");
		String result = null;
		try {
			if (file == null) {
				throw new NullPointerException("file为空指针.");
			}
			if (fileCommon == null) {
				throw new NullPointerException("fileCommon为空指针.");
			}
			if (realpath == null) {
				throw new NullPointerException("realpath为空指针.");
			}
			// 取真实文件名
			String fileName = file.getFilesFileName();
			String getExt = null;
			if (fileName == null || "".equals(fileName)) {
				getExt = null;
			}
			getExt = fileName.substring(fileName.lastIndexOf(".") + 1);

			fileName = FileHelper.getFileName() + "." + getExt;

			// 传回前台页面使用
			fileCommon.setFileName(fileName);
			if (StringUtils.isBlank(fileCommon.getFileSource())) {
				result = "请选择上传文件!";
			} else {
				// 若指定了类型，则进行检查
				if (StringUtils.isNotBlank(fileCommon.getFileType())
						&& !fileCommon.getFileType().toUpperCase()
								.contains(getExt.toUpperCase())) {
					result = "文件类型不对!";
				} else {
					// 若禁止类型，则进行检查
					String notAllowFileType = fileCommon.getNotAllowFileType();
					if (StringUtils.isNotBlank(notAllowFileType)
							&& notAllowFileType.toUpperCase().contains(
									getExt.toUpperCase())) {
						result = "禁止上传" + notAllowFileType + "类型的文件!";
					} else {
						// 若指定了大小限制，则进行检查
						double fileLimitSize = Double.parseDouble(fileCommon
								.getFileLimitSize());
						long getFileSize = 0L;
						if (fileLimitSize > 0) {
							if (fileCommon.getSizeUnit().equalsIgnoreCase("M")) {
								getFileSize = new Double(fileLimitSize
										* ONE_K_BYTE * ONE_K_BYTE).longValue();
							}
							if (fileCommon.getSizeUnit().equalsIgnoreCase("K")) {
								getFileSize = new Double(fileLimitSize
										* ONE_K_BYTE).longValue();
							}
						}
						if (file.getFiles().length() > getFileSize) {
							result = "文件超过上传限制!";
						} else {
							// 若已存在同名文件，就先删除
							String parent = realpath;
							parent = parent.replace("/", File.separator);
							File f = new File(parent, fileName);
							if (f != null && f.exists() && f.isFile()) {
								f.delete();
							}

							try {
								// 上传文件
								UploadHelper.uploadByFile(file.getFiles(),
										realpath, fileName);
							} catch (Exception e) {
								result = "上传路径不存在!";
								logger.debug("退出getByAllParams方法");
								return result;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getByAllParams方法");
		return result;
	}

	/**
	 * 从app上传文件.
	 * 
	 * @param file
	 *            文件
	 * @param source
	 *            路径
	 * @return 上传结果
	 * @throws Exception
	 */
	public static String uploadByFileapp(final Files file, final String source) {
		logger.debug("进入uploadByFileapp方法");
		FileInputStream fis = null;
		OutputStream out = null;
		String result = null;
		try {
			if (file == null) {
				throw new NullPointerException("file为空指针.");
			}
			if (source == null) {
				throw new NullPointerException("source为空指针.");
			}
			String fileName = file.getFilesFileName();
			String getExt = null;
			if (fileName == null || "".equals(fileName)) {
				getExt = null;
			}
			getExt = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName = FileHelper.getFileName() + "." + getExt;
			fis = new FileInputStream(source + "/" + "2.TXT");
			file.setFilesFileName(fileName);
			// 读取字节流
			byte[] bytes = Base64.decode(new BufferedReader(
					new InputStreamReader(fis)));
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) { // 调整异常数据
					bytes[i] += ONE_QUARTER_K_BYTE;
				}
			}
			out = new FileOutputStream(source + "/" + fileName);
			out.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("退出uploadByFileapp方法");
		return result;
	}
}
