package com.shtydic.neo4j.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.sql.Driver;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.shtydic.neo4j.model.DsModel;

import sun.misc.BASE64Encoder;

/**
 * 
 * <p>Title: DriverManager</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午10:29:28
 */
public class DriverManager {
	
    public static  String url = "http://" + Constants.neo4j_ip_port + "/db/data/transaction/commit";
    
    private DriverManager() {
    }


    /**
     * 发送post请求
     *
     * @param url
     * @param sql
     * @return
     */
    public static String sendPost(String url, String sql) {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        String params = "{\"statements\":[{\"statement\": \"" + sql + "\",\"parameters\": {},\"resultDataContents\": [ \"row\",\"graph\"],\"includeStats\": true}]}";
//        String params = "{\"statements\":[{\"statement\": \"" + sql + "\"}]}";
        long start = System.currentTimeMillis();
        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            URLConnection httpConn = connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((Constants.neo4jUserName + ":" + Constants.neo4jPassword));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long usedTime = end - start;
        if (result.length() == 0) {
            return "";
        } else {
            if (result.lastIndexOf("}") == result.length()-1){
                StringBuffer sb  = new StringBuffer(result.substring(0,result.length()-1) + ",\"responseTime\":" + usedTime + "}");
                return sb.toString();
            } else {
                return result.toString();
            }
        }
    }
    
    public static String sendPost_too(String url, String sql,String user,String password) {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        String params = "{\"statements\":[{\"statement\": \"" + sql + "\",\"parameters\": {},\"resultDataContents\": [ \"row\",\"graph\"],\"includeStats\": true}]}";
//        String params = "{\"statements\":[{\"statement\": \"" + sql + "\"}]}";
        long start = System.currentTimeMillis();
        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            URLConnection httpConn = connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((user + ":" + password));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long usedTime = end - start;
        if (result.length() == 0) {
            return "";
        } else {
            if (result.lastIndexOf("}") == result.length()-1){
                StringBuffer sb  = new StringBuffer(result.substring(0,result.length()-1) + ",\"responseTime\":" + usedTime + "}");
                return sb.toString();
            } else {
                return result.toString();
            }
        }
    }
    
    public static String sendInsertPost(String url, String sql) {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
//        String params = "{\"statements\":[{\"statement\": \"" + sql + "\",\"parameters\": {},\"resultDataContents\": [ \"row\",\"graph\"],\"includeStats\": true}]}";
        String params = "{\"statements\":[{\"statement\": \"" + sql + "\"}]}";
        long start = System.currentTimeMillis();
        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            URLConnection httpConn = connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((Constants.neo4jUserName + ":" + Constants.neo4jPassword));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long usedTime = end - start;
        if (result.length() == 0) {
            return "";
        } else {
            if (result.lastIndexOf("}") == result.length()-1){
                StringBuffer sb  = new StringBuffer(result.substring(0,result.length()-1) + ",\"responseTime\":" + usedTime + "}");
                return sb.toString();
            } else {
                return result.toString();
            }
        }
    }
    
    public static String sendInsertPost(String url, String sql,String username,String password) {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
//        String params = "{\"statements\":[{\"statement\": \"" + sql + "\",\"parameters\": {},\"resultDataContents\": [ \"row\",\"graph\"],\"includeStats\": true}]}";
        String params = "{\"statements\":[{\"statement\": \"" + sql + "\"}]}";
        long start = System.currentTimeMillis();
        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            URLConnection httpConn = connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((username + ":" + password));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long usedTime = end - start;
        if (result.length() == 0) {
            return "";
        } else {
            if (result.lastIndexOf("}") == result.length()-1){
                StringBuffer sb  = new StringBuffer(result.substring(0,result.length()-1) + ",\"responseTime\":" + usedTime + "}");
                return sb.toString();
            } else {
                return result.toString();
            }
        }
    }
    /**
     * 发送post请求,自定义地址
     *
     * @param sql
     * @return
     */
    public static String sendPostWithAddress( String sql, DsModel dsModel) {
        String url = "http://" + dsModel.getDb_address() + "/db/data/transaction/commit";
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        String params = "{\"statements\":[{\"statement\": \"" + sql + "\"}]}";

        try {
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            URLConnection httpConn = connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((dsModel.getDb_username() + ":" + dsModel.getDb_password()));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (result.length() == 0) {
            return "";
        } else {
            return result.toString();
        }
    }

    /**
     * base64编码
     *
     * @param source
     * @return
     */
    public static String encode(String source) {
        BASE64Encoder enc = new sun.misc.BASE64Encoder();
        return (enc.encode(source.getBytes()));
    }
    public static boolean isPost(String url,String username,String password) throws IOException {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        Object is = null;
        try {
            java.net.URL connURL = new java.net.URL(url);
            URLConnection httpConn = connURL.openConnection();
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            String author = "Basic " + encode((username + ":" + password));
            httpConn.setRequestProperty("Authorization", author);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            //jackson解析
            JsonFactory jasonFactory = new JsonFactory();
            ObjectMapper objectMapper = new ObjectMapper();
            Map arr = objectMapper.readValue(result.toString(),Map.class);
            is = arr.get("neo4j_version");
//            http://localhost:7474/db/data/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                in.close();
            }
        }
        if(is == null){
            return false;
        }else{
            return true;
        }

    }


    public static boolean isPost(String url) throws IOException {
        StringBuilder result = new StringBuilder();// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        Object is = null;
        try {
            java.net.URL connURL = new java.net.URL(url);
            URLConnection httpConn = connURL.openConnection();
            httpConn.setRequestProperty("Accept", "application/json;charset=UTF-8 ");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            //jackson解析
            JsonFactory jasonFactory = new JsonFactory();
            ObjectMapper objectMapper = new ObjectMapper();
            Map arr = objectMapper.readValue(result.toString(),Map.class);
            is = arr.get("cluster_name");
//            http://localhost:7474/db/data/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                in.close();
            }
        }
        if(is == null){
            return false;
        }else{
            return true;
        }

    }
    public static void main(String[] args) throws IOException {
       /* DsModel ds = new DsModel();
        ds.setDb_username("neo4j");
        ds.setDb_password("tydic");
        String s = DriverManager.sendPost(DriverManager.url, "CALL db.labels()");
        ObjectMapper obj = new ObjectMapper();
        Map map = obj.readValue(s, Map.class);
        ArrayList<Map> list =  (ArrayList) map.get("results");
        Map map1 = list.get(0);
        List<String> lables = new ArrayList<>();
        ArrayList<Map> dataArr = (ArrayList)map1.get("data");
        for(int i = 0;i < dataArr.size() ; i++){
            String str = dataArr.get(i).get("row").toString();
            str = str.substring(1,str.length()-1);
            lables.add(str);
        }*/
//        dataArr.get("row");
//        LinkedList row = (LinkedList)dataMap.get("row");
//        System.out.println(row);
//        JsonFactory jasonFactory = new JsonFactory();
//        ObjectMapper objectMapper = new ObjectMapper();
////        String neo4j_url = "http://192.168.128.59:7474/db/data/";
////        System.out.println("Neo4j:"+DriverManager.isPost(neo4j_url,"neo4j","tydic"));
//        String es_url = "http://192.168.128.59:9200/";
//        System.out.println("Elasticsearch:"+DriverManager.isPost(es_url));

//        Map arr = objectMapper.readValue(result,Map.class);
//        System.out.println(arr.get("neo4j_version"));

//        JsonParser jsonParser = jasonFactory.createJsonParser(result);
//        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
//            //get the current token
//            String fieldname = jsonParser.getCurrentName();
//            if ("name".equals(fieldname)) {
//                //move to next token
//                jsonParser.nextToken();
//                System.out.println(jsonParser.getText());
//            }
//        }
//        System.out.println(result);
        //String sql2 = "{\"statements\": [{\"statement\": \"WITH ['747574198608049222','740783198205031917','746365200603186205','740793197207011279','713409195803217092','713409195408063028','610909090916359916','610909090969544346','342622195310307128','342622199105227091','713409195408063028'] AS x ,[ '747574198608049222','740783198205031917','746365200603186205','740793197207011279','713409195803217092','713409195408063028','610909090916359916','610909090969544346','342622195310307128','342622199105227091','713409195408063028'] AS y  MATCH (a:People) WHERE a.ZJHM IN x MATCH (b:People) WHERE b.ZJHM IN y MATCH p=(a)-[r*1..1]-(b) WHERE a<>b RETURN r\",\"parameters\": {},\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true}]}";
    	String sql2 = "WITH ['747574198608049222','740783198205031917','746365200603186205','740793197207011279','713409195803217092','713409195408063028','610909090916359916','610909090969544346','342622195310307128','342622199105227091','713409195408063028'] AS x ,[ '747574198608049222','740783198205031917','746365200603186205','740793197207011279','713409195803217092','713409195408063028','610909090916359916','610909090969544346','342622195310307128','342622199105227091','713409195408063028'] AS y  MATCH (a:People) WHERE a.ZJHM IN x MATCH (b:People) WHERE b.ZJHM IN y MATCH p=(a)-[r*1..1]-(b) WHERE a<>b RETURN r";
        System.out.println(sql2);
       String out =  DriverManager.sendPost("http://192.168.128.59:7474/db/data/transaction/commit", sql2);
       System.out.println(out);
    }
}

