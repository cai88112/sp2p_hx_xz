package com.fp2p.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Excel工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class ExcelHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(ExcelHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private ExcelHelper() {
	}

	/**
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行.
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @return 读出的Excel中数据的内容
	 */
	public static String[][] getData(final File file, final int ignoreRows) {
		logger.debug("进入getData方法");
		String[][] returnArray = null;
		BufferedInputStream in = null;
		try {
			if (file == null) {
				throw new NullPointerException("file为空指针");
			}

			in = new BufferedInputStream(new FileInputStream(file));
			// 打开HSSFWorkbook
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(in));
			HSSFCell cell = null;
			List<String[]> result = new ArrayList<String[]>();
			// 有效列数
			int rowSize = 0;
			for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
				HSSFSheet st = wb.getSheetAt(sheetIndex);
				// 第一行为标题，不取
				for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
					HSSFRow row = st.getRow(rowIndex);
					if (row == null) {
						continue;
					}
					int tempRowSize = row.getLastCellNum() + 1;
					if (tempRowSize > rowSize) {
						rowSize = tempRowSize;
					}
					String[] values = new String[rowSize];
					Arrays.fill(values, "");
					boolean hasValue = false;
					for (short columnIndex = 0; columnIndex <= row
							.getLastCellNum(); columnIndex++) {
						String value = "";
						cell = row.getCell(columnIndex);
						if (cell != null) {
							// 注意：一定要设成这个，否则可能会出现乱码
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
									Date date = cell.getDateCellValue();
									if (date != null) {
										value = new SimpleDateFormat(
												"yyyy-MM-dd").format(date);
									} else {
										value = "";
									}
								} else {
									value = new DecimalFormat("0").format(cell
											.getNumericCellValue());
								}
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								// 导入时如果为公式生成的数据则无值
								if (!cell.getStringCellValue().equals("")) {
									value = cell.getStringCellValue();
								} else {
									value = cell.getNumericCellValue() + "";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue() == true ? "Y"
										: "N");
								break;
							default:
								value = "";
							}
						}
						if (columnIndex == 0 && value.trim().equals("")) {
							break;
						}
						values[columnIndex] = value.trim();
						hasValue = true;
					}
					if (hasValue) {
						result.add(values);
					}
				}
			}
			returnArray = new String[result.size()][rowSize];
			for (int i = 0; i < returnArray.length; i++) {
				returnArray[i] = (String[]) result.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("退出getData方法");
		return returnArray;
	}

	/**
	 * 导出内容为Excel文件.
	 * 
	 * @param sheetName
	 *            表明
	 * @param list
	 *            数据内容
	 * @param titles
	 *            表头
	 * @param fieldNames
	 *            数据内容的key
	 * @return 导出的excel文件实体
	 */
	public static HSSFWorkbook exportExcel(final String sheetName,
			final List<Map<String, Object>> list, final String[] titles,
			final String[] fieldNames) {
		logger.debug("进入exportExcel方法");
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			if (list == null) {
				throw new NullPointerException("list为空指针");
			}
			if (titles == null) {
				throw new NullPointerException("titles为空指针");
			}
			if (fieldNames == null) {
				throw new NullPointerException("fieldNames为空指针");
			}
			String tempSheetName = sheetName;
			if (tempSheetName == null || "".equals(tempSheetName)) {
				tempSheetName = "sheet1";
			}

			// 对每个表生成一个新的sheet,并以表名命名
			HSSFSheet sheet = wb.createSheet(tempSheetName);
			// 设置表头的说明
			HSSFRow topRow = sheet.createRow(0);
			for (int i = 0; i < titles.length; i++) {
				setCellGBKValue(topRow.createCell((short) i), titles[i]);
			}
			int k = 1;
			for (Map<String, Object> map : list) {
				HSSFRow row = sheet.createRow(k);
				for (int i = 0; i < fieldNames.length; i++) {
					setCellGBKValue(row.createCell((short) i),
							map.get(fieldNames[i]) + "");
				}
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出exportExcel方法");
		return wb;
	}

	/**
	 * 设置Excel单元格的编码格式.
	 * 
	 * @param cell
	 *            Excel单元格
	 * @param value
	 *            单元格内容
	 */
	private static void setCellGBKValue(final HSSFCell cell, final String value) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		// 设置CELL的编码信息
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(value);
	}
}
