package top.cellargalaxy.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class UrlDownloadUtil {
	public static final File httpDownload(String httpUrl, File folder, int connectTimeout, int readTimeout) throws IOException {
		if (httpUrl == null || httpUrl.length() == 0 || folder == null) {
			return null;
		}
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(httpUrl).openConnection();
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);
		String filename = httpURLConnection.getHeaderField("Content-Disposition");
		if (filename == null) {
			int index = httpUrl.lastIndexOf('#');
			if (index != -1) {
				httpUrl = httpUrl.substring(0, index);
			}
			index = httpUrl.lastIndexOf('?');
			if (index != -1) {
				httpUrl = httpUrl.substring(0, index);
			}
			index = -1;
			while (httpUrl.length() > 0 && (index = httpUrl.lastIndexOf('/')) == httpUrl.length() - 1) {
				httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
			}
			if (httpUrl.length() > 0) {
				if (index > 0) {
					filename = httpUrl.substring(index + 1);
				} else {
					filename = httpUrl;
				}
			}
		}
		if (filename == null || filename.length() == 0) {
			filename = UUID.randomUUID().toString();
		}
		File file = new File(folder.getAbsolutePath() + File.separator + filename);
		folder.mkdirs();
		try (InputStream inputStream = httpURLConnection.getInputStream();
		     OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			byte[] bytes = new byte[1024];
			int len;
			while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
				outputStream.write(bytes, 0, len);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return file;
	}
}
