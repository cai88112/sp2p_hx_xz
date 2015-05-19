package com.sp2p.action.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.shuangqian.ShuangqianSignHelper;
import com.shove.web.action.BasePageAction;


public class BaseAppAction extends BasePageAction { 

	public static Log log = LogFactory.getLog(BaseAppAction.class);
	protected Map<String, String> getAppAuthMap() {
		return getRequestMap("auth");

	}

	protected Map<String, String> getAppInfoMap() {
		return getRequestMap("info");
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getRequestMap(String requestAttr) {
		HttpServletRequest request =  request();
		Map<String, String> jsonMap = new HashMap<String, String>();
		log.info("request url==>"+request.getContextPath());
		Map<String,Object> paraMap = request.getParameterMap();
		log.info("session==>"+session());
		Set<String> keySet = paraMap.keySet();
		log.info("=============request value start===============");
		for(String key : keySet){
			Object val = paraMap.get(key);
			if(val instanceof String[]){
				log.info(key+"==>"+Arrays.toString((String[])val));
			}else{
				log.info(key+"==>"+val);
			}
		}
		log.info("=============request value end===============");
		String json = request(requestAttr);
		Map<String, String> map = (Map<String, String>) JSONObject.toBean(
				JSONObject.fromObject(json), HashMap.class);
		if(map == null){
			map = new HashMap<String, String>();
		}
		return map;
		
	}
	
	public void  getStream(String source){
		try{
			HttpServletRequest request =  request();
			log.info("request url==>"+request.getContextPath());
			Map<String,Object> paraMap = request.getParameterMap();
			log.info(paraMap);
			log.info("session==>"+session());
			Set<String> keySet = paraMap.keySet();
			log.info("=============request value start===============");
			for(String key : keySet){
				Object val = paraMap.get(key);
				String conten = "";
				if(val instanceof String[]){
					log.info(key+"==>"+Arrays.toString((String[])val));
				}else{
					log.info(key+"==>"+val);
				}
				if(key.equals("str")){
					if(val instanceof String[]){
						conten = ((String[])val)[0];
					}else{
						conten = val+"";
					}
					 byte[] bytes = null;     
				        ByteArrayOutputStream bos = new ByteArrayOutputStream();     
				        try {       
				            ObjectOutputStream oos = new ObjectOutputStream(bos);        
				            oos.writeObject(Base64.encodeBase64((val+"").getBytes()));       
				            oos.flush();      
				            bytes = bos.toByteArray ();     
				            oos.close();        
				            bos.close();       
				        } catch (IOException ex) {       
				            ex.printStackTrace();  
				        }  
				    FileOutputStream out = new FileOutputStream( new File(source + "/" +"2.TXT") ) ;  
				    Writer writer = new OutputStreamWriter(out,"UTF-8") ;
				    
				    writer.write(conten);
				    writer.flush();
				    writer.close();
				    out.close();
				}
			}
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	
	
	
}
