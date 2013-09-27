package com.test.memory.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BitmapUtil {

	private static final String TAG = "BitmapUtil";
	private static final boolean LOGD = Customization.DEBUG;
	private static final boolean LOGE = Customization.ERROR;

	private static final CompressFormat BITMAP_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int BITMAP_COMPRESS_QUALITY = 80;
	
	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
	
	/**
	 * Warning: This method may produce scaling problem in OS lower then 4.1
	 */
	public static Bitmap getBitmap(
			Resources res, int resId, int width, int height) {
	
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
	
		BitmapFactory.decodeResource(res, resId, options);
	
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap getBitmap(AssetManager asset, String fileName,
			int width, int height) {
	
		Bitmap bitmap = null;
	
		try {
			InputStream is = asset.open(fileName);
	
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
	
			BitmapFactory.decodeStream(is, null, options);
	
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			
			bitmap = BitmapFactory.decodeStream(is, null, options);
	
		} catch (IOException e) {
			if (LOGE)
				FLog.e(TAG, "getBitmap # Asset error: %s", e.toString());
		}
	
		return bitmap;
	}
	
	public static Bitmap getBitmap(InputStream input, int width, int height) {
		Bitmap bitmap = null;
		
		byte[] byteArr = new byte[0];
        byte[] buffer = new byte[1024];
        int len = 0;
        int count = 0;
		
	    try {
            while ((len = input.read(buffer)) > -1) {
                if (len != 0) {
                    if (count + len > byteArr.length) {
                        byte[] newbuf = new byte[(count + len) * 2];
                        System.arraycopy(byteArr, 0, newbuf, 0, count);
                        byteArr = newbuf;
                    }

                    System.arraycopy(buffer, 0, byteArr, count, len);
                    count += len;
                }
            }
	            
	        //Decode image size
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        
	        BitmapFactory.decodeByteArray(byteArr, 0, count, options);
	        
	        options.inSampleSize= calculateInSampleSize(options, width, height);
	        options.inJustDecodeBounds = false;
	        
	        bitmap = BitmapFactory.decodeByteArray(byteArr, 0, count, options);
	        
	    } catch (Exception e) {
	    	if(LOGE) FLog.e(TAG, "getBitmap # Error %s", e.toString());
	    }
	    
	    return bitmap;
	}

	@SuppressLint("SdCardPath")
	public static Bitmap getBitmap() {
		Bitmap bitmap = null;
		String filePath = "/mnt/sdcard/d1.jpg";
//		bitmap = getBitmap(filePath, 704, 974);
		bitmap =  BitmapFactory.decodeFile(filePath);
		return bitmap;
	}
	
	public static Bitmap getBitmap(String filePath, int width, int height){
		Bitmap bitmap = null;
		
	    try {
	        //Decode image size
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        
	        BitmapFactory.decodeFile(filePath, options);
	        
	        options.inSampleSize= calculateInSampleSize(options, width, height);
	        options.inJustDecodeBounds = false;
	        
	        bitmap = BitmapFactory.decodeFile(filePath, options);
	        
	    } catch (Exception e) {
	    	if(LOGE) FLog.e(TAG, "getBitmap # Error %s", e.toString());
	    }
	    
	    return bitmap;
	}

	public static Bitmap rotate(Bitmap bitmap, int rotation, int width, int height){
		if (bitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
	
			height = bitmap.getHeight() < height? bitmap.getHeight(): height;
			width = bitmap.getWidth() < width? bitmap.getWidth(): width;
			
			return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
		}
		return null;
	}
	
	public static void saveBitmap(Bitmap bitmap, String path, int quality) {
		if (bitmap != null) {
			try {
				IoUtil.prepareFolder(path);

				if (LOGD)
					FLog.d(TAG, "saveBitmap # Write: %s", path);

				FileOutputStream out = new FileOutputStream(path);
				bitmap.compress(BITMAP_COMPRESS_FORMAT,
						quality, out);

				out.close();
			} catch (Exception e) {
				if (LOGE)
					FLog.e(TAG, "saveBitmap # Error: %s", e.toString());
			}
		}
	}

	public static Bitmap shrinkBitmap(String file, int width, int height) {
		if( file == null){
			return null;
		}
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}
	
	public static String getFileNameTemp(){
		File cacheDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String dir = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
			
			cacheDir = new File(dir);
		} else {
			// it does not have an SD card
		   	cacheDir = new File(Environment.getDataDirectory().getAbsolutePath());
		}
		
		if(!cacheDir.exists())
	    	cacheDir.mkdirs();
		
		return cacheDir.getAbsolutePath() + "/ToonTellTemp.jpg";
	}
	
	

	public static void saveBitmap(Bitmap bitmap, String path) {
		saveBitmap(bitmap, path, BITMAP_COMPRESS_QUALITY);
	}


	public static Bitmap scaleDown(Bitmap realImage, int maxImageSize,
	        boolean filter) {
		if (realImage == null) {
			return null;
		}
		
	    float ratio = Math.min(
	            (float) maxImageSize / realImage.getWidth(),
	            (float) maxImageSize / realImage.getHeight());
	    int width = Math.round((float) ratio * realImage.getWidth());
	    int height = Math.round((float) ratio * realImage.getHeight());

	    Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
	            height, filter);
	    return newBitmap;
	}
	
	public static int getOrientation(File imageFile) {
		ExifInterface exif;
		int orientation = 0;
		int rotate = 0;
		try {
			exif = new ExifInterface(imageFile.getAbsolutePath());
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			 switch (orientation) {
		        case ExifInterface.ORIENTATION_ROTATE_270:
		            rotate = 270;
		            break;
		        case ExifInterface.ORIENTATION_ROTATE_180:
		            rotate = 180;
		            break;
		        case ExifInterface.ORIENTATION_ROTATE_90:
		            rotate = 90;
		            break;
		        }
		} catch (IOException e) {
			if (LOGE) FLog.e(TAG, 
					"onRemoteControlReceive # Error: %s", e.toString());
		}
		return rotate;
	}
	
	public static void recycleBitmap(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();

			imageView.setImageBitmap(null);
			imageView.setImageDrawable(null);

			try {
				recycleDrawable(drawable);
			} catch (Exception e) {
				if (LOGE)
					FLog.e(TAG, "recycleBitmap # Asset error: %s", e.toString());
			}
		}
	}

	public static void recycleDrawable(LinearLayout linearLayout) {
		Drawable drawable = linearLayout.getBackground();
		try {
			recycleDrawable(drawable);
		} catch (Exception e) {
			if (LOGE)
				FLog.e(TAG, "recycleDrawable # Asset error: %s", e.toString());
		}
		linearLayout.setBackgroundDrawable(null);
	}

	public static void recycleDrawable(RelativeLayout relativeLayout) {
		Drawable drawable = relativeLayout.getBackground();
		try {
			recycleDrawable(drawable);
		} catch (Exception e) {
			if (LOGE)
				FLog.e(TAG, "recycleDrawable # Asset error: %s", e.toString());
		}
		relativeLayout.setBackgroundDrawable(null);
	}

	public static void recycleDrawable(Drawable drawable) {
		if (drawable != null) {
			drawable.setCallback(null);
		}

		if (drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}

		} else if (drawable instanceof AnimationDrawable) {
			AnimationDrawable anime = (AnimationDrawable) drawable;

			if (anime.isRunning()) {
				anime.stop();
			}

			int frameCount = anime.getNumberOfFrames();

			Drawable frame = null;
			Bitmap bitmap = null;

			for (int i = 0; i < frameCount; i++) {
				frame = anime.getFrame(i);

				if (frame instanceof BitmapDrawable) {
					bitmap = ((BitmapDrawable) frame).getBitmap();

					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}
				}
			}
		}
	}

	public static byte[] getByteArray(Bitmap bitmap, CompressFormat format) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(format, 100, stream);
		return stream.toByteArray();
	}

	public static Bitmap addWaterMark(Bitmap image, Bitmap watermark,
			int width, int height) {

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(image, 0f, 0f, null);
		canvas.drawBitmap(watermark, 0f, 0f, null);

		return bitmap;
	}
	
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

	

}
