package com.coreweb.extras.log;

import java.util.logging.*;
import java.io.*;

public class LOG {

	public static String file = "./log-file.log";

	/* Si imprime por pantalla */
	private static boolean ifOut = true;

	public static Logger logger;

	static {
		try {

			boolean append = true;
			FileHandler fh = new FileHandler(file, append);

/*			
			fh.setFormatter(new Formatter() {
		         public String format(LogRecord rec) {
		            StringBuffer buf = new StringBuffer(1000);
		            buf.append(new java.util.Date());
		            buf.append(' ');
		            buf.append(rec.getLevel());
		            buf.append(' ');
		            buf.append(formatMessage(rec));
		            buf.append('\n');
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getSourceClassName:"+rec.getSourceClassName()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getSourceMethodName: "+rec.getSourceMethodName()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getLoggerName: "+rec.getLoggerName()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getgetResourceBundleName: "+rec.getResourceBundleName()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getSequenceNumber: "+rec.getSequenceNumber()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getThreadID: "+rec.getThreadID()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getParameters: "+rec.getParameters()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("getThrown: "+rec.getThrown()+"\n");
		            buf.append("---------------------------------------------------------\n");
		            buf.append("\n---------\n");
		            return buf.toString();
		            }

		          });
		          */
			//fh.setFormatter(new XMLFormatter());
			 fh.setFormatter(new SimpleFormatter());
			logger =  Logger.getLogger("TestLog");
			logger.addHandler(fh);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
