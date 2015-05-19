package com.fp2p.deprecated;

import java.io.IOException;

public class MySQLTool {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static boolean isLinux(){  
        return OS.indexOf("linux")>=0;  
    }  
      
    public static boolean isMacOS(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0;  
    }  
      
    public static boolean isMacOSX(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")>0;  
    }  
      
    public static boolean isWindows(){  
        return OS.indexOf("windows")>=0;  
    }  
    
    public static String execMySQL(String action,String user,String password,String host,String port,String database,String path){
    	String backUpStmt = "mysqldump -u"+user+" -p"+password+" -h"+host+" -P"+port +
		" --set-charset --single-transaction --default-character-set=utf8" +
		" --disable-keys -c --no-autocommit --triggers -R "+database+" > "+path;
		String recoveStmt = "cmd /c mysql  -u"+user+" -p"+password+" -h"+host+" -P"+port+" "+database+" <"+path;
		Process p;
		try {
			if(isWindows()){
				p = Runtime.getRuntime().exec(new String[]{"cmd","/c",action == "backup"?backUpStmt:recoveStmt});
			}else{
				p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",action == "backup"?backUpStmt:recoveStmt});
			}
			
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "数据库"+(action == "backup"?"备份":"还原")+"中断!";
		} catch (IOException e) {
			e.printStackTrace();
			return "数据库连接异常";
		}
		return "数据库"+(action == "backup"?"备份":"还原")+"处理成功!";
    }
	public static void main(String[] args) throws IOException {
		String str =System.getProperty("user.dir");
		System.out.println(str);
	}
	
} 