package com.test.memory.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;


public class IoUtil {
	public static final char[] HEX_ARRAY = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String getExternalFilesDir(Context context) {
		String path = null;
		
		String mediaState = Environment.getExternalStorageState();
	
		if (mediaState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			int api = android.os.Build.VERSION.SDK_INT;
			
			if (api > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
				path = context.getExternalFilesDir(null).getPath();
			} else {
				path = String.format("%s/%s/%s", 
						Environment.getExternalStorageDirectory(),
						context.getApplicationInfo().packageName, 
						"files");
			}
		} else {
			path = context.getFilesDir().getPath();
		}
		
		return path;
	}

	
	public static void serialize(String filePath, Serializable object) throws IOException {
		ObjectOutputStream oos = 
				new ObjectOutputStream(
						new FileOutputStream(
								new File(filePath)));
		
		oos.writeObject(object);
		oos.close();
	}
	
	public static Object deserialize(String filePath) 
			throws IOException, ClassNotFoundException {
		
		Object obj = null;
		
		if (new File(filePath).exists()) {
			ObjectInputStream ois = 
					new ObjectInputStream(
							new FileInputStream(
									new File(filePath)));
			
			obj = ois.readObject();
			ois.close();
		}
		
		return obj;
	}

	public static void prepareFolder(String path) {
		File f = new File(path);
		
		if (f.isDirectory()) {
			if (!f.exists()) f.mkdirs();
		}
		else {
			File parent = f.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
		}
	}
	
	public static String encrypt(String message) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");
        return bytesToHex(md.digest(message.getBytes("utf-8")));
    }
    
	public static byte[] copyOf(byte[] original, int newLength) {
		byte[] copy = new byte[newLength];
		System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
		return copy;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static void copy(File source, File target) throws IOException {
	    InputStream in = new FileInputStream(source);
	    OutputStream out = new FileOutputStream(target);

	    byte[] buf = new byte[1024];
	    int length = 0;
	    
	    while ((length = in.read(buf)) > 0) {
	        out.write(buf, 0, length);
	    }
	    
	    in.close();
	    out.close();
	}

	/**
	 * Uncompress ZIP archive file (doesn't support RAR)
	 * The code is taken from StackOverflow. 
	 * http://stackoverflow.com/questions/5028421/android-unzip-a-folder
	 * @throws IOException 
	 * @throws ZipException 
	 */
	public static void unzipArchive(File archive, String outputDir) throws IOException {
		ZipFile zipfile = new ZipFile(archive);
		for (Enumeration<?> e = zipfile.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			unzipEntry(zipfile, entry, outputDir);
		}
	}

	private static void unzipEntry(
			ZipFile zipfile, ZipEntry entry, 
			String outputDir) throws IOException {

		if (entry.isDirectory()) {
			createDir(new File(outputDir, entry.getName()));
			return;
		}

		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			createDir(outputFile.getParentFile());
		}

		BufferedInputStream in = 
				new BufferedInputStream(
						zipfile.getInputStream(entry));
		
		BufferedOutputStream out = 
				new BufferedOutputStream(
						new FileOutputStream(outputFile));

		byte[] buf = new byte[1024];
	    int length = 0;
	    
	    while ((length = in.read(buf)) > 0) {
	        out.write(buf, 0, length);
	    }
	    
	    in.close();
	    out.close();
	}

	private static void createDir(File dir) {
		if (dir.isDirectory() && dir.exists()) return;
		
		if (!dir.mkdirs()) {
			throw new RuntimeException("Fail creating directory " + dir);
		}
	}
}