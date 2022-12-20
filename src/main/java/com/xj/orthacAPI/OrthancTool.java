package com.xj.orthacAPI;

import com.alibaba.fastjson.JSONObject;
import com.util.HttpConnectionUtil;
import com.util.RegexUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * orthanc  java tool  for  restful api
 * @author xj
  */
public class OrthancTool {


    public static void main( String[] args )
    {
        OrthancTool t=new OrthancTool();
        String filePath="D:\\temp\\2-32_1_00000C98.IMG";
        String orthancUrl="http://localhost:8042/instances";
        try {
            t.postDcm(filePath,orthancUrl,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * orthanc post dicom file
     * @param filePath
     * @param orthancUrl
     * @param token
     * @throws IOException
     */
    public void postDcm(String filePath, String orthancUrl, String token) throws IOException {
        URL url = new URL(orthancUrl);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");

        // Optional: 如果需要，可以加上token等其它header信息
        //httpCon.setRequestProperty ("Authorization", "Bearer " + token);

        OutputStream os = httpCon.getOutputStream();
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        os.write(bytes);
        os.flush();
        os.close();
        httpCon.connect();


        // 打印出response
        String result;
        BufferedInputStream bis = new BufferedInputStream(httpCon.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result2 = bis.read();
        while(result2 != -1) {
            buf.write((byte) result2);
            result2 = bis.read();
        }
        result = buf.toString();
        //System.out.println(result);
    }

    //列出所有患者信息
    public     List<String> getPatientInfos(){
        String urlString = "http://localhost:8042/patients";

        return getInfoFromOrthanc(urlString);
    }



    //列出所有Studies
    public  List<String> getStudies(){
        String urlString = "http://localhost:8042/studies";
        return getInfoFromOrthanc(urlString);
    }


    //列出所有Studies
    public  List<String> getSeries(){
        String urlString = "http://localhost:8042/series";
        return getInfoFromOrthanc(urlString);
    }
    //列出所有Instance
    public  List<String> getInstances(){
        String urlString = "http://localhost:8042/instances";
        return getInfoFromOrthanc(urlString);
    }

    public  void   getPatient(String patientId){
        String urlString = "http://localhost:8042/patients/"+patientId;
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }

    public  void   getStudy(String StudyId){
        String urlString = "http://localhost:8042/studies/"+StudyId;
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }

    public  void   getSeries(String seriesId){
        String urlString = "http://localhost:8042/series/"+seriesId;
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }


    public  void   getInstance(String instanceId){
        String urlString = "http://localhost:8042/instances/"+instanceId;
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }



    public  void   getSimpleTag(String instanceId){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/simplified-tags";
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }



    public  void   getTag(String instanceId){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/tags";
        JSONObject  o=getJsonFromOrthanc(urlString);
        System.out.println(JSONObject.toJSONString(o));
    }



    //    访问实例的原始DICOM字段
    //curl http://localhost:8042/instances/e668dcbf-8829a100-c0bd203b-41e404d9-c533f3d4/content/0010-0010
    //  递归查询 ：http://localhost:8042/instances/e668dcbf-8829a100-c0bd203b-41e404d9-c533f3d4/content/0008-1250/0/0040-a170/0/0008-0104
    public void getDicomField(String instanceId,String tagStr){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/content/"+tagStr;
        String  r=getValueFromOrthanc(urlString);
        System.out.println(r);
    }


    public List<String>  getAllTag(String instanceId){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/content";
        List<String>  r=getInfoFromOrthanc(urlString);
        for (String s : r) {
            System.out.println(s);
        }
        return r;
    }



    /**
     * 下载dicom文件
     * @param instanceId
     * @param filePathStr  保存到本地的文件全路径
     */
    public  void   downInstance(String instanceId,String  filePathStr){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/file";
        downInstanceFromOrthanc(urlString,filePathStr);
    }



    /**
     * 下载png文件
     * @param instanceId
     * @param filePathStr  保存到本地的文件全路径 (Preview.png) 注意文件类型是png
     */
    public  void   downInstancePng(String instanceId,String  filePathStr){
        String urlString = "http://localhost:8042/instances/"+instanceId+"/preview";
        downInstanceFromOrthanc(urlString,filePathStr);
    }




    //列出所有Studies
    public  List<String> getInfoFromOrthanc(String urlString){
        List<String> strArray = new ArrayList<String> ();
        String rgex = "\\[(.*?)\\]";
        try {
            URL url = new URL(urlString);
            //得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
//                String result = is2String(inputStream);//将流转换为字符串。
//                Log.d("kwwl","result============="+result);

                BufferedInputStream bis = new BufferedInputStream(inputStream);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                int result2 = bis.read();
                while(result2 != -1) {
                    buf.write((byte) result2);
                    result2 = bis.read();
                }
                String result;
                result = buf.toString();
                //System.out.println(result);
                if(result!=null  && result.length()>0){
                    result= result.replace("\n", "");
                    result=result.replace("\"", "");
                }
                String innerStr= RegexUtil.getSubUtilSimple(result,rgex);
                String[] arr=innerStr.split(",");
                if(arr!=null  && arr.length>0){
                    strArray = Arrays.asList(arr);
                }
                return strArray;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strArray;
    }

    public JSONObject getJsonFromOrthanc(String urlString){
        try {
            URL url = new URL(urlString);
            //得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                int result2 = bis.read();
                while(result2 != -1) {
                    buf.write((byte) result2);
                    result2 = bis.read();
                }
                String result;
                result = buf.toString();
//                System.out.println(result);
//                if(result!=null  && result.length()>0){
//                    result= result.replace("\n", "");
//                    result=result.replace("\"", "");
//                }
                JSONObject jSONObject = JSONObject
                        .parseObject(result);
//                System.out.println("--------------------------------");
//                System.out.println(jSONObject);
                return jSONObject;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }



    //列出所有Studies
    public  String getValueFromOrthanc(String urlString){
        String value=null;
        try {
            URL url = new URL(urlString);
            //得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();

                BufferedInputStream bis = new BufferedInputStream(inputStream);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                int result2 = bis.read();
                while(result2 != -1) {
                    buf.write((byte) result2);
                    result2 = bis.read();
                }
                String result=null;
                result = buf.toString();
                //System.out.println(result);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    //下载文件
    public void downInstanceFromOrthanc(String urlString,String fiesavePath){
        HttpConnectionUtil.downLoadFileFromOrthanc(urlString,fiesavePath);
    }




}
