package cn.hudp.loader.download;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public class DownLoad {
    public static InputStream getInputStreamFromUrl(String strUrl) {
        InputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                if (is != null)
//                    is.close();
//                if (connection != null)
//                    connection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }
}
