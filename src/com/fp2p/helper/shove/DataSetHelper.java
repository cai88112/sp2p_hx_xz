package com.fp2p.helper.shove;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fp2p.helper.BeanHelper;
import com.shove.data.ColumnCollection;
import com.shove.data.DataSet;
import com.shove.data.RowCollection;

/**
 * DataSet工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class DataSetHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(DataSetHelper.class
			.getName());

	/**
	 * DataSet工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private DataSetHelper() {
	}

	/**
	 * dataSet转为Map.
	 *
	 * @param dataSet
	 *            将数据库查询结果封装的dataset对象
	 * @return 与dataset对象对应的map
	 */
	public static Map<String, String> dataSetToMap(final DataSet dataSet) {
		logger.debug("进入dataSetToMap方法");
		Map<String, String> paramMap = null;
		try {
			if (dataSet == null) {
				throw new NullPointerException("dataSet为空指针");
			}
			RowCollection rowCollection = dataSet.tables.get(0).rows;
			ColumnCollection columnCollection = dataSet.tables.get(0).columns;
			paramMap = new HashMap<String, String>();
			for (int i = 0; i < rowCollection.getCount(); i++) {
				for (int j = 0; j < columnCollection.getCount(); j++) {
					paramMap.put(columnCollection.get(j).getName(),
							BeanHelper.parmToStr(rowCollection.get(i).get(j)));
				}
			}
			if (paramMap.isEmpty()) {
				paramMap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出dataSetToMap方法");
		return paramMap;
	}
}