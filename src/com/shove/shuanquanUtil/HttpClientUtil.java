package com.shove.shuanquanUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil
{
	/**
	 * 发送post请求
	 * 
	 * @param strURL
	 * @param req
	 * @return
	 */
	public static String[] doPostQueryCmd(String strURL, Map<String, String> req)
	{
		String returncode = null;
		String result = null;
		String[] resultarr = new String[2];
		try
		{
			// 构造HttpClient的实例
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(100000);
			// 创建GET方法的实例
			PostMethod postMethod = new PostMethod(strURL);
			// 使用系统提供的默认的恢复策略
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,50000);
			NameValuePair[] param = new NameValuePair[req.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : req.entrySet())
			{
				param[i] = new NameValuePair(entry.getKey(), entry.getValue());
				i++;
			}
			postMethod.setRequestBody(param);
			try
			{
				// 执行getMethod
				int statusCode = httpClient.executeMethod(postMethod);
				returncode = Integer.toString(statusCode);
				// if (statusCode != HttpStatus.SC_OK)
				// {
				// System.err.println("Method failed: " + postMethod.getStatusLine());
				// }
				// else
				// {
				//					
				// }
				// 读取内容
				// byte[] responseBody = postMethod.getResponseBody();
				// result = postMethod.getResponseBodyAsString();
				// 处理内容
				// result = new String(responseBody, "UTF-8");
				// System.out.println(result);
				
				StringBuffer sb = new StringBuffer();
				InputStream in = postMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String line;
				while ((line = br.readLine()) != null)
				{
					sb.append(line);
				}
				result = sb.toString();
				if (br != null)
				{
					br.close();
				}
			}
			catch (HttpException e)
			{
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				// System.out.println("Please check your provided http address!");
				// e.printStackTrace();
			}
			catch (IOException e)
			{
				// 发生网络异常
				// e.printStackTrace();
			}
			finally
			{
				// 释放连接
				postMethod.releaseConnection();
			}
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		if (result == null)
		{
			result = "";
		}
		
		resultarr[0] = returncode;
		resultarr[1] = result;
		
		return resultarr;
	}
}
