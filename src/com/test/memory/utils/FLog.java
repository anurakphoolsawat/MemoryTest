package com.test.memory.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class FLog {
	
	private static final boolean VERBOSE = true;
	private static final boolean DEBUG = true;
	private static final boolean INFO = true;
	private static final boolean WARNING = true;
	private static final boolean ERROR = true;
	
	private static final int STACKTRACE_LIMIT = 50;
	
	private static String sLogFilePath;
	private static String sLogFileName;
	
	private static boolean sEnableSystemLog = false;
	private static boolean sEnableLogcat = true;
	private static boolean sEnableFileLog = false;
	
	private static final DateFormat sSimpleDateFormat = 
			new SimpleDateFormat("yyyy-dd-MM HH:mm:ss.SSS", Locale.ENGLISH);
	
	public static enum LogType {
		ERROR,
		WARNING,
		DEBUG,
		INFO,
		VERBOSE;
	}
	
	public static DateFormat getSimpleDateFormat() {
		return sSimpleDateFormat;
	}
	
	public static void setEnableLogCat(boolean enable) {
		sEnableLogcat = enable;
	}
	
	public static void setEnableSystemLog(boolean enable) {
		sEnableSystemLog = enable;
	}
	
	public static void setEnableFileLog(boolean enable, String path, String fileName){
		sEnableFileLog = enable;
		
		if (enable && path != null && fileName != null) {
			sLogFilePath = path.trim();
			sLogFileName = fileName.trim();
			
			if(!sLogFilePath.endsWith("/")) {
				sLogFilePath = sLogFilePath + "/";
			}
		}
	}
	
	public static String getLogFilePaht() {
		return sLogFilePath;
	}
	
	public static String getLogFileName() {
		return sLogFileName;
	}
	
	public static synchronized void v(String tag, String format, Object... args) {
		String msg = String.format(format, args);
		
		if(sEnableLogcat) Log.v(tag, msg);
		if(sEnableSystemLog) System.out.println(msg);
		if(sEnableFileLog && VERBOSE) writeLogToFile(LogType.VERBOSE, tag, msg);
	}
	public static synchronized void v(String tag, String msg, Throwable e) {
		if(sEnableLogcat) Log.v(tag, msg, e);
		if(sEnableFileLog && VERBOSE) writeLogToFile(LogType.VERBOSE, tag, msg, e);
		if(sEnableSystemLog) {
			System.out.println(msg);
			e.printStackTrace();
		}
	}

	public static synchronized void d(String tag, String format, Object... args) {
		String msg = String.format(format, args);
		
		if(sEnableLogcat) Log.d(tag, msg);
		if(sEnableSystemLog) System.out.println(msg);
		if(sEnableFileLog && DEBUG) writeLogToFile(LogType.DEBUG, tag, msg);
	}
	public static synchronized void d(String tag, String msg, Throwable e) {
		if(sEnableLogcat) Log.d(tag, msg, e);
		if(sEnableFileLog && DEBUG) writeLogToFile(LogType.DEBUG, tag, msg, e);
		if(sEnableSystemLog) {
			System.out.println(msg);
			e.printStackTrace();
		}
	}

	public static synchronized void i(String tag, String format, Object... args) {
		String msg = String.format(format, args);
		
		if(sEnableLogcat) Log.i(tag, msg);
		if(sEnableSystemLog) System.out.println(msg);
		if(sEnableFileLog && INFO) writeLogToFile(LogType.INFO, tag, msg);
	}
	public static synchronized void i(String tag, String msg, Throwable e) {
		if(sEnableLogcat) Log.i(tag, msg, e);
		if(sEnableFileLog && INFO) writeLogToFile(LogType.INFO, tag, msg, e);
		if(sEnableSystemLog) {
			System.out.println(msg);
			e.printStackTrace();
		}
	}

	public static synchronized void w(String tag, String format, Object... args) {
		String msg = String.format(format, args);
		
		if(sEnableLogcat) Log.w(tag, msg);
		if(sEnableSystemLog) System.out.println(msg);
		if(sEnableFileLog && WARNING) writeLogToFile(LogType.WARNING, tag, msg);
	}
	public static synchronized void w(String tag, String msg, Throwable e) {
		if(sEnableLogcat) Log.w(tag, msg, e);
		if(sEnableFileLog && WARNING) writeLogToFile(LogType.WARNING, tag, msg, e);
		if(sEnableSystemLog) {
			System.out.println(msg);
			e.printStackTrace();
		}
	}

	public static synchronized void e(String tag, String format, Object... args) {
		String msg = String.format(format, args);
		
		if(sEnableLogcat) Log.e(tag, msg);
		if(sEnableSystemLog) System.out.println(msg);
		if(sEnableFileLog && ERROR) writeLogToFile(LogType.ERROR, tag, msg);
	}
	
	public static synchronized void e(String tag, String msg, Throwable e) {
		if(sEnableLogcat) Log.e(tag, msg, e);
		if(sEnableFileLog && ERROR) writeLogToFile(LogType.ERROR, tag, msg, e);
		if(sEnableSystemLog) {
			System.out.println(msg);
			e.printStackTrace();
		}
	}
	
	private static synchronized void writeLogToFile(LogType level, String tag, String msg, Throwable e) {
		String stacktrace = getStackTraceLog(level, tag, msg, e);
		writeLogToFile(level, tag, stacktrace);
	}
	
	private static synchronized void writeLogToFile(LogType level, String tag, String msg) {
		if(sLogFilePath != null && !sLogFilePath.equals("") && 
				sLogFileName != null && !sLogFileName.equals("")) {
			
			File f = new File(sLogFilePath);
			
			if (!f.exists()) {
				try {
					f.mkdirs();
				}
				catch (SecurityException e) {
					/* e(TAG, String.format("writeLogToFile # Error: %s", e)); */
					/* Sometimes this can cause StackOverflow */
				}
			}
			
			if (f.canWrite()) {
				String log = getLogDisplay(level, tag, msg);
				
				f = new File(sLogFilePath + sLogFileName);
				try {
					BufferedReader reader = new BufferedReader(new StringReader(log), 256);
					BufferedWriter writer = new BufferedWriter(new FileWriter(f, true), 256);
					String line = null;
					while ((line = reader.readLine()) != null) {
						writer.append(line);
						writer.append("\r\n");
					}
					writer.flush();
					writer.close();
				}
				catch (IOException e) {
					/* e(TAG, String.format("writeLogToFile # Error: %s", e)); */
					/* Sometimes this can cause StackOverflow */
				}
			} // End if f.canWrite
		} // End first condition (file name & path must not be NULL)
	} // End method
	
	public static String getStackTraceLog(LogType level, String tag, String msg, Throwable e) {
		StringBuilder b = new StringBuilder();
		b.append(msg).append("\n");
		
		if (e != null) {
			b.append(e.toString()).append("\n");
			
			StackTraceElement[] elements = e.getStackTrace();
			StackTraceElement element = null;
			if (elements != null) {
				for (int i = 0; i < elements.length && i < STACKTRACE_LIMIT; i ++) {
					element = elements[i];
					if (element == null) continue;
					else {
						b.append("\tat ").append(element.toString()).append("\n");
					}
				}
				int more = elements.length - STACKTRACE_LIMIT;
				if (more > 0) {
					b.append("\t... ").append(more).append(" more").append("\n");
				}
			}
		}
		
		return b.toString();
	}
	
	public static  String getLogDisplay(LogType level, String tag, String msg) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new StringReader(msg), 256);
			
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			
			String time = sSimpleDateFormat.format(date);
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(String.format("%s: ", time));
				
				builder.append(String.format("%s/%s(%d): ", 
						level, tag, android.os.Process.myPid()));
				
				builder.append(line);
				builder.append("\r\n");
			}
		}
		catch (IOException e) { /* ignore */ }
		
		return builder.toString();
	}
}