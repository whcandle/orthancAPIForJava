 package com.util;

         import java.io.*;
         import java.net.*;
         import java.util.Iterator;
  import java.util.Map;

   public class HttpConnectionUtil {

       public static  void downloadFile(String downloadUrl,String fileSavePath) {
           File downloadFile = null;

           HttpURLConnection connection = null;
           try {
               //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
               URL url = new URL(downloadUrl);
               connection = (HttpURLConnection) url.openConnection();
               System.out.println(connection.getResponseCode());
               System.out.println(connection.getContentLength());
//               connection.setConnectTimeout(5000);
//               connection.setReadTimeout(60000);
               //connection.setDoInput(true);
               InputStream is = connection.getInputStream();
               final File temp = new File(fileSavePath);
               if (temp.exists()){
                   temp.delete();
               }

               temp.createNewFile();
               temp.setReadable(true, false);
               temp.setWritable(true, false);
               downloadFile = temp;
               //Log.d("downloadAPK", "downloadUrl " + downloadUrl + "\n save to " + temp);
               OutputStream os = new FileOutputStream(temp);
               byte[] buf = new byte[8 * 1024];
               int len;
               try {
                   while ((len = is.read(buf)) != -1) {
                       os.write(buf, 0, len);
                   }
                   os.flush();
                   if (os instanceof FileOutputStream) {
                       ((FileOutputStream) os).getFD().sync();
                   }
               } finally {
                   closeSilently(os);
                   closeSilently(is);
               }
               //Log.d("downloadAPK", "download complete url=" + downloadUrl + ", fileSize= " + temp.length());
//			installPkg(this, temp, pkg);
           } catch (Exception e) {
               //Log.w("downloadAPK", e);
               if (downloadFile != null)
                   downloadFile.delete();

           } finally {
               if (connection != null)
                   connection.disconnect();
           }
       }

       public static final void closeSilently(Object closeable) {
           try {
               if (closeable != null) {
                   if (closeable instanceof Closeable) {
                       ((Closeable) closeable).close();
                   } else if (closeable instanceof Socket) {
                       ((Socket) closeable).close();
                   } else if (closeable instanceof ServerSocket) {
                       ((ServerSocket) closeable).close();
                   } else {
                       throw new IllegalArgumentException("Unknown object to close");
                   }
               }
           } catch (IOException e) {
               // ignore
           }
       }



       public static HttpURLConnection getConnection(String httpUrl) throws Exception {
           URL url = new URL(httpUrl);
           HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setRequestProperty("Content-Type", "application/octet-stream");
           connection.setDoOutput(true);
           connection.setDoInput(true);
           connection.setRequestProperty("Connection", "Keep-Alive");
           connection.connect();
           return connection;

       }

       public static void downLoadFileFromOrthanc(String downloadUrl,String fileSavePath){
           File downloadFile = null;
           BufferedInputStream  bis =null;
           BufferedOutputStream bos=null;
           //String HTTP_URL="http://localhost:8042/instances/7a2bd226-d679ea16-06f25ba2-e5dbcbcd-a4d6b6e8/file";
           String HTTP_URL=downloadUrl;
           try {
               int contentLength = getConnection(HTTP_URL).getContentLength();
               System.out.println("文件的大小是:"+contentLength);
               if (contentLength>32) {
                   InputStream is= getConnection(HTTP_URL).getInputStream();
                   bis = new BufferedInputStream(is);

                   final File temp = new File(fileSavePath);
                   if (temp.exists()){
                       temp.delete();
                   }

                   temp.createNewFile();
                   temp.setReadable(true, false);
                   temp.setWritable(true, false);
                   downloadFile = temp;
                   OutputStream fos = new FileOutputStream(temp);

                   //FileOutputStream fos = new FileOutputStream("d:/test777.dcm");
                   bos= new BufferedOutputStream(fos);
                   int b = 0;
                   byte[] byArr = new byte[1024];
                   while((b= bis.read(byArr))!=-1){
                       bos.write(byArr, 0, b);
                   }
                 bos.flush();
                   System.out.println("下载的文件的大小是----------------------------------------------:"+contentLength);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }finally{
               try {
                   if(bis !=null){
                       bis.close();
                   }
                   if(bos !=null){
                       bos.close();
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }


       public static void main(String[] args) {

       }




   }

