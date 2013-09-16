package com.example.mapdemo.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;

import com.example.mapdemo.mymap.MapsActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ImageService {

	String extStorageDirectory;
	Bitmap bm;
	String image_URL;
	ForlderService folderService;

	public ImageService() {

	}

	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// If we're running on Honeycomb or newer, try to use inBitmap

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	public static void SaveBitmapToSDCard(Bitmap bitmap, String path,
			String fileName, int reqWidth, int reqHeight) {
		OutputStream outStream = null;
		File file = new File(path, fileName);
		if (!file.exists()) {

			if (bitmap.getWidth() > 1024) {
				bitmap = Bitmap.createBitmap(bitmap, 128, 0, 1024, 752);
				if (reqWidth < bitmap.getWidth()
						|| reqHeight < bitmap.getHeight()) {
					bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth,
							reqHeight, false);
				}
			} else {
				if (reqWidth < bitmap.getWidth()
						|| reqHeight < bitmap.getHeight()) {
					bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth,
							reqHeight, false);
				}
				try {
					outStream = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
					outStream.flush();
					outStream.close();
					// bitmap.recycle();
					// bitmap = null;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Map<String, String> metaData = new HashMap<String, String>();
					Throwable th = new FileNotFoundException(e.getMessage());
					e.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					Map<String, String> metaData = new HashMap<String, String>();
					Throwable th = new IOException(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public void SaveImageFromStream(String path, FileOutputStream out,
			String name) {
		// extStorageDirectory =
		// Environment.getExternalStorageDirectory().toString();
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 4;
		// bm = BitmapFactory.de
		OutputStream outStream = null;
		File file = new File(path + "/thumb", name);
		try {
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new FileNotFoundException(e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new IOException(e.getMessage());
			e.printStackTrace();
		}
	}

	public void SaveImageFromUrlToSDCard(String path, String url, String name) {

		image_URL = url;
		// extStorageDirectory =
		// Environment.getExternalStorageDirectory().toString();
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		bm = LoadImage(image_URL, bmOptions);
		OutputStream outStream = null;
		File file = new File(path, name + ".PNG");
		try {
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new FileNotFoundException(e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new IOException(e.getMessage());
			e.printStackTrace();
		}
	}

	public static Bitmap getImageFromSDCard(String imageInSD) {

		Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		return bitmap;
	}

	public Bitmap LoadImage(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception ex) {
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(ex.getMessage());
		}
		return bitmap;
	}

	private InputStream OpenHttpConnection(String strURL) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(ex.getMessage());
		}
		return inputStream;
	}

	public String DownloadFromUrlOld(String path, String imageURL,
			String fileName) { // this
								// is
								// the
								// downloader
								// method
		try {
			URL url = new URL(imageURL); // you can write here any link
			File file = new File(path, fileName);

			long startTime = System.currentTimeMillis();
			Log.d("ImageManager", "download begining");
			Log.d("ImageManager", "download url:" + url);
			Log.d("ImageManager", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");
			return path;

		} catch (Exception e) {
			Log.d("ImageManager", "Error: " + e);
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception(e.getMessage());
			return null;
		}

	}

	public String DownloadFromUrl(Context context,String path, String imageURL, String fileName) {
		File file = new File(path, fileName);
		AndroidHttpClient client = AndroidHttpClient
				.newInstance(context.getPackageName());
		try {
			HttpGet get = new HttpGet(imageURL);
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 30000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 60000);
			final HttpParams httpParams = new BasicHttpParams();
			HttpClientParams.setRedirecting(httpParams, true);
			get.setParams(httpParams);
			HttpResponse resp = client.execute(get);
			int status = resp.getStatusLine().getStatusCode();
			if (status != HttpURLConnection.HTTP_OK) {
				// Log.i(LOGTAG, "Couldn't download image from Server: " + url +
				// " Reason: " + resp.getStatusLine().getReasonPhrase() + " / "
				// + status);
				return null;
			}
			HttpEntity entity = resp.getEntity();
			// Log.i(LOGTAG, url + " Image Content Length: " +
			// entity.getContentLength());
			InputStream is = entity.getContent();
			FileOutputStream fos = new FileOutputStream(file);
			copyStream(is, fos);
			fos.close();
			is.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new Exception("Download imageURL = " + imageURL
					+ "->exception: " + ex.getMessage());
			return null;
		} finally {
			client.close();
		}
		return path;
	}


	public static int copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] stuff = new byte[1024];
		int read = 0;
		int total = 0;
		while ((read = input.read(stuff)) != -1) {
			output.write(stuff, 0, read);
			total += read;
		}
		return total;
	}

	public static Drawable ImageOperations(String url, String saveFilename) {
		try {
			InputStream is = (InputStream) fetch(url);
			Drawable d = Drawable.createFromStream(is, saveFilename);
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Map<String, String> metaData = new HashMap<String, String>();
			Throwable th = new MalformedURLException(e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	public static Object GetImagesInSDCard(Context context,String folderName) {
		File images = new File(MapsActivity.getDataPath()
				+ "/" + folderName);
		File[] imagelist = images.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return ((filename.endsWith(".jpg")) || (filename
						.endsWith(".png")));
			}
		});
		return imagelist;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
