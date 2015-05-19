/**   
 * @Title: PropertyFile.java 
 * @Package com.shove.shuanquanUtil 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月4日 下午2:27:17 
 * @version V1.0   
 */
package com.shove.shuanquanUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Property文件读取
 * 
 * @ClassName: PropertyFile
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月4日 下午2:27:17
 * 
 */
public class PropertyFile {
	private Properties properties = null;

	/**
	 * 构造函数.
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param filePath
	 */
	public PropertyFile(String filePath) {
		InputStream isInputStream = PropertyFile.class.getClassLoader()
				.getResourceAsStream(filePath);
		this.properties = new Properties();
		try {
			this.properties.load(isInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (isInputStream != null) {
				try {
					isInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取配置文件参数.
	 * 
	 * @Title: read
	 * @param @param property
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String read(String property) {
		return properties.getProperty(property);
	}
}
