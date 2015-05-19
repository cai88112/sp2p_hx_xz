package com.fp2p.helper;

import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HtmlHelper工具类. 支付表单
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class HtmlHelper {

	/**
	 * 私有的构造方法.
	 */
	private HtmlHelper() {
	}

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(HtmlHelper.class
			.getName());

	/**
	 * 构造提交表单HTML数据.
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param gateway
	 *            网关地址
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @param formName
	 *            表单名字
	 * @return 提交表单HTML文本
	 */
	public static String buildForm(final Map<String, String> sParaTemp,
			final String gateway, final String strMethod,
			final String strButtonName, final String formName) {
		logger.debug("进入buildForm方法.");
		String result = null;
		try {
			if (sParaTemp == null) {
				throw new NullPointerException("sParaTemp为空指针.");
			}
			if (gateway == null) {
				throw new NullPointerException("gateway为空指针.");
			}
			if (strMethod == null) {
				throw new NullPointerException("strMethod为空指针.");
			}
			if (strMethod != "get" && strMethod != "post") {
				throw new Exception("strMethod不是get和post的类型.strMethod:"
						+ strMethod);
			}
			if (strButtonName == null) {
				throw new NullPointerException("strButtonName为空指针.");
			}
			if (formName == null) {
				throw new NullPointerException("formName为空指针.");
			}

			if (sParaTemp.size() > 0) {
				StringBuffer sbHtml = new StringBuffer();
				// 待请求参数数组
				Set<String> keys = sParaTemp.keySet();
				// sbHtml.append("<form id=\"editForm\" name=\"editForm\" action=\"");
				sbHtml.append("<form id=\"").append(formName)
						.append("\" name=\"").append(formName)
						.append("\" action=\"");
				sbHtml.append(gateway);
				sbHtml.append("\" method=\"");
				sbHtml.append(strMethod);
				sbHtml.append("\">");
				for (String name : keys) {
					String value = sParaTemp.get(name);
					sbHtml.append("<input type=\"hidden\" name=\"");
					sbHtml.append(name);
					sbHtml.append("\" value=\"");
					sbHtml.append(value);
					sbHtml.append("\"/>");
					// log.info(name+"============="+value);
				}
				// submit按钮控件请不要含有name属性
				sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
						+ "\" style=\"display:none;\"></form>");
				sbHtml.append("<script>document.forms['").append(formName)
						.append("'].submit();</script>");
				result = sbHtml.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("退出buildForm方法.");
		return result;
	}

	/**
	 * buildHtmlForm方法.
	 * 
	 * @param sParaTemp
	 *            Map<String, String>
	 * @param gateway
	 *            String
	 * @param method
	 *            String
	 * @return buildHtmlForm.
	 */
	public static String buildHtmlForm(final Map<String, String> sParaTemp,
			final String gateway, final String method) {
		logger.debug("进入buildHtmlForm方法.");
		StringBuffer htmlBuf = new StringBuffer();
		try {
			if (sParaTemp == null) {
				throw new NullPointerException("sParaTemp为空指针.");
			}
			if (gateway == null) {
				throw new NullPointerException("gateway为空指针.");
			}
			if (method == null) {
				throw new NullPointerException("method为空指针.");
			}
			htmlBuf.append("<html>");
			htmlBuf.append(" <head><title>sender</title></head>");
			htmlBuf.append(" <body>");
			htmlBuf.append(buildForm(sParaTemp, gateway, method, "提交",
					"editForm"));
			htmlBuf.append(" </body>");
			htmlBuf.append("</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出buildHtmlForm方法.");
		return htmlBuf.toString();
	}

}
