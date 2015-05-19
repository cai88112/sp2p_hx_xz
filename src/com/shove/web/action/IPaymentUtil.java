package com.shove.web.action;
import java.io.DataInputStream;



import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.ips.security.utility.IpsCrypto;
import com.shove.Convert;
import com.shove.config.IPayConfig;
import com.sp2p.service.IPaymentService;





public class IPaymentUtil {
	
	public static Log log = LogFactory.getLog(IPaymentUtil.class);
	//----------------------------------------测试接口-------------------------------------------------------------//
	public static void main(String[] args) throws JSONException {
		String pMerCode  =  "808805";
		String pErrCode = "0000";
		String pErrMsg = "充值成功";
		String p3DesXmlPara = "O75eJ5spQphliWkDyBTXItr9RH/lz3O0Qj08cPVAFYjmboeyvbx3itLYU80BmCwtvw469bjl4h4KhQOeyifPMTRgDsGq8wTmlacGRiARmR1VYu/jh+A6Z4jc+jyAvbK5FK6M7W/4RpxP4KT7HNVBmSrTY0usFj5Ybr/LfvlQYqx0Zbh13PcF4LE+1Y99ArpQY/poCzITB3vjpV1JenvYAdP4eTADCnJLUWMEjpT4Jd9N/gkIZdys4VN/WBvqP/8o/3mbcoCHM9gNDIoA4Tf/MsO2sQE8ErAbARox+tggPZ0Qyk7+sWro0o1d7JGYIq501CjbItmiLww5fsyiqGdoXA7uAYUyJKQtiHqSRf0KDV+u35Fv7KIlCyT4Vq1w+tozDbBUkEfeVJw5Nm1a5vlqEzLjcT7PJ5t9I6a4t0XwcBge8QAaIAnrE7U24ye3R3Hg3HcQnrYLUl6/j6sjPdegPU4rRo4uUQJWsoQ4CHQM7MQYZ8zQwKIGD6Tc8fcvB7YqU2z2vGhvEf9e8Vn3juS80DJ0cSORq3Qx2v+4CjWm+Ek=";
		String pSign = "202b577438a13b3505398af2a1ad023d4f75345d99fddcc41e4dd3298f1b6784b1be91f18f68d648b3f31c672f09c9240aef05d142154d5c1f94ea6bc92e3a4d500a8f59519688c642d0642d2d5c5842e0ecb147369a1b25b8bf831bbcc8e536f13a3c0fcbf94ae7337fbbc154282145542b0993a98f8bb1cefeb9c5a8d4eadc";
		String pubKey = "-----BEGIN PUBLIC KEY-----\n"+
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMWwKD0u90z1K8WvtG6cZ3SXHL"+
		"UqmQCWxbT6JURy5BVwgsTdsaGmr22HT4jfEBQHEjmTtyUWC5Ag9Cwgef0VFrDB7T"+
		"qyhWfVA7n8SvV6b1eDbQlY/qhUb50+3SCpN7HxdPzdMDkJjy6i6syh7RtH0QfoAp"+
		"HS6TLY4DjPvbGgdXhwIDAQAB\n"+
		"-----END PUBLIC KEY-----";
		String CERT_MD5 = "GPhKt7sh4dxQQZZkINGFtefRKNPyAj8S00cgAwtRyy0ufD7alNC28xCBKpa6IU7u54zzWSAv4PqUDKMgpOnM7fucO1wuwMi4RgPAnietmqYIhHXZ3TqTGKNzkxA55qYH";
		log.info(pMerCode);
		log.info(pErrCode);
		log.info(pErrMsg);
		log.info(p3DesXmlPara);
		log.info(pSign);
		log.info(pubKey);
		Boolean boo = com.ips.security.utility.IpsCrypto.md5WithRSAVerify
		(pMerCode+pErrCode+pErrMsg+p3DesXmlPara+CERT_MD5, pSign, pubKey);
		log.info("签名结果："+boo);
		
	}
	//-----------------------------------------测试接口------------------------------------------------------------//
	/**
	 * 将map转换成xml
	 * @param xmlMap
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked") 
	public static String parseMapToXml(LinkedHashMap<String, Object> xmlMap) throws JSONException{
		
		String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><pReq>";
		for(Map.Entry<String, Object> entry : xmlMap.entrySet()){
			
			String key = entry.getKey();
			String value = "";
			if(entry.getValue().getClass().isAssignableFrom(String.class)){
				value = entry.getValue().toString();
			}
			strxml = strxml + "<" + key + ">" + value + "</" + key + ">";
		}
		strxml = strxml + "</pReq>";
		log.info(strxml);
		return strxml;
	}	
	
	/**
	 * 将xml转化成Map字符串
	 * @param xmlStr
	 * @return
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, String> parseXmlToJson(String p3DesXmlPara) throws JSONException, UnsupportedEncodingException{
		String xml = IpsCrypto.triDesDecrypt(p3DesXmlPara, IPayConfig.des_key,
				IPayConfig.des_iv);
		Map<String, String> jsonMap = new HashMap<String, String>(); 
		JSONObject jsonObj = XML.toJSONObject(xml);
		JSONObject pReq = jsonObj.getJSONObject("pReq");
		java.util.Iterator<String> iterator = pReq.keys();
		while(iterator.hasNext()){
			String key = iterator.next();
			log.info(pReq.get(key).getClass());
			if(pReq.get(key).getClass().isAssignableFrom(String.class)){
				jsonMap.put(key, pReq.get(key).toString());
			}else{
				jsonMap.put(key, "");
			}
			;			
		}				
		return jsonMap;		
	}
	
	/**
	 * 发送数据
	 * 
	 * @param map
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String createHtml(String strxml, String url, String pMerCode, int i, String... obj) throws Exception {
		
		String htmlPMerCode = "argMerCode";
		String htmlP3DesXmlPara = "arg3DesXmlPara";
		String htmlPSign = "argSign";
		
		if(obj != null && obj.length == 3){
			
			htmlPMerCode = obj[0];
			htmlP3DesXmlPara = obj[1];
			htmlPSign = obj[2];
		}
		
		log.info(strxml);
		String str3DesXmlPana = strxml;
		  
    	log.info("DesXmlPana = "+str3DesXmlPana);
    	log.info(IPayConfig.des_key+"   "+IPayConfig.des_iv);
    	str3DesXmlPana = com.ips.security.utility.IpsCrypto.triDesEncrypt(strxml, IPayConfig.des_key, IPayConfig.des_iv);
    	str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
        String  strSign = com.ips.security.utility.IpsCrypto.md5Sign(pMerCode+ str3DesXmlPana + IPayConfig.cert_md5);
        log.info("签名："+strSign+"md5--"+IPayConfig.cert_md5+"---des_key--"+IPayConfig.des_key+"---"+IPayConfig.des_iv + "---");
		StringBuffer sb = new StringBuffer();
		
		sb.append("<!DOCTYPE html>");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Servlet AccountServlet</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<form action="+IPayConfig.ipay_gateway + url+" id=\"frm1\" method=\"post\">");
		sb.append("<input type=\"hidden\" name=" + htmlPMerCode + " value=" + pMerCode + ">");
		sb.append("<input type=\"hidden\" name=" + htmlP3DesXmlPara + " value=" + str3DesXmlPana + ">");
		sb.append("<input type=\"hidden\" name=" + htmlPSign + " value=" + strSign + ">");
		sb.append("</form>");
		sb.append("<script language=\"javascript\">");
		sb.append("document.getElementById(\"frm1\").submit();");
		sb.append("</script>");
		sb.append("</body>");
		sb.append("</html>");
	     System.out.print(sb+"}}}}}}}}}}}}}}}}}}}}}}}}}");
		return sb.toString();
	};
	
	/**
	 * 校验签名是否正确
	 * @param pMerCode商户号
	 * @param pErrCode错误代码
	 * @param pErrMsg错误信息
	 * @param p3DesXmlPara同步返回的xml数据(未经过解密的xml字符串)
	 * @param pSign 同步返回的签名
	 * @return
	 */
	public static boolean checkSign(String pMerCode, String pErrCode, String pErrMsg, String p3DesXmlPara, String pSign){
	    Boolean boo = com.ips.security.utility.IpsCrypto.md5WithRSAVerify(pMerCode + pErrCode + pErrMsg + p3DesXmlPara + IPayConfig.cert_md5, pSign, IPayConfig.pubKey);       
	    return boo;
	}
	
	/**
	 * webService接口
	 * @param soap
	 * @param urls
	 * @return
	 */
	public static Map<String, String> webService(String soap, String urls) {
		try {
			URL url = new URL(IPayConfig.ipay_web_service);
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", Integer.toString(soap
					.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", urls);
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();

			java.io.InputStream is = conn.getInputStream();

			DataInputStream dis = new DataInputStream(is);
			byte d[] = new byte[dis.available()];
			dis.read(d);
			String data = new String(d, "UTF-8");
			data = data.replaceAll("&lt;", "<");
			data = data.replaceAll("&gt;", ">");
			log.info(data);
			
			data = data.substring(data.indexOf("<pReq>"), data.indexOf("</pReq>") + "</pReq>".length());
			JSONObject jsonObj = XML.toJSONObject(data);
			Gson gson = new Gson();
			
			Map<String, Map<String, String>> dataMap = gson.fromJson(jsonObj.toString(), new com.google.gson.reflect.TypeToken<Map<String, Map<String, String>>>(){}.getType());

			return dataMap.get("pReq");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static  String getSoapAuditTender(String str3DesXmlPana, String htmlKey, String argMerCode, String arg3DesXmlPara, String argSign, String strSign) throws JSONException {
	
		 String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
         "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
         "<soap:Body>"+
         "<" + htmlKey + " xmlns=\"http://tempuri.org/\">";
		 if(argMerCode != null && !argMerCode.equals("")){
			 
			 soap = soap + "<" + argMerCode + ">" + IPayConfig.terraceNoOne + "</" + argMerCode + ">";
		 }
		 if(arg3DesXmlPara != null && !arg3DesXmlPara.equals("")){
			 
			 soap = soap + "<" + arg3DesXmlPara + ">"+str3DesXmlPana+"</" + arg3DesXmlPara + ">";
		 }
		 if(argSign != null && !argSign.equals("")){
			 
			 soap = soap + "<" + argSign + ">" + strSign + "</" + argSign + ">";
		 }
		 soap = soap + "</" + htmlKey + ">"+
         "</soap:Body>"+
         "</soap:Envelope>";
		return soap;

	}
	/**
	 * httpClient式提交(自动投标接口)
	 * @param url
	 * @param str3DesXmlPana 未加密的xml字符串
	 * @return
	 */
	public static int http(String url, String str3DesXmlPana) {
		
		str3DesXmlPana = IpsCrypto.triDesEncrypt(str3DesXmlPana, IPayConfig.des_key, IPayConfig.des_iv);
		str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
		
		String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne
				+ str3DesXmlPana + IPayConfig.cert_md5);
		NameValuePair[] data = {
				new NameValuePair("pMerCode", IPayConfig.terraceNoOne),
				new NameValuePair("p3DesXmlPara", str3DesXmlPana),
				new NameValuePair("pSign", strSign) };
		
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
		// 301或者302
		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
				|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			// 从头中取出转向的地址
			org.apache.commons.httpclient.Header locationHeader = postMethod.getResponseHeader("location");
			String location = null;
			if (locationHeader != null) {
				location = locationHeader.getValue();
				log.info("The page was redirected to:" + location);
			} else {
				System.err.println("Location field value is null.");
			}
		}
		return statusCode;
	}		
	
	/**获取20位,唯一的极付合作合作伙伴网站唯一订单号**/
	public static String getIn_orderNo(){	
		String orderNo = "" ;
		try {
			orderNo =  IPaymentService.getOrderNo("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderNo;
	};
	
}

