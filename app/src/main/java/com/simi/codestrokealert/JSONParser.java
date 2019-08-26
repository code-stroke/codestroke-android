package com.simi.codestrokealert;

import android.util.Log;

import com.simi.codestrokealert.httpsrequest.HTTPUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.List;



public class JSONParser
{

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArry = null;
    static String json = "";
    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    private static final String HTTPS_STRING = "https";
    StringBuilder result = new StringBuilder();
    URL urlObj;
    // JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;
    private static final String USER_AGENT = "Mozilla/5.0";

    public JSONParser()
    {
    }

    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {
        try {
            if (method == "POST") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "" + paramString;
                HttpGet httpGet = new HttpGet(url);
                // httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            json = sb.toString();
            json = json.trim();
            json = json.substring(1, json.length() - 1);
            json = json.replace("\\", "");

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            jObj = new JSONObject(json.toString());
            Log.e("json2", jObj + "");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;

    }

    // HTTP GET request
    public String sendGet(String url) {
        String res = "";
        //String url = "http://www.google.com/search?q=mkyong";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("User-Agent", USER_AGENT);
            //conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            if (in != null)
                in.close();
            res = response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            //print result

        }

        System.out.println(res);
        return res;
    }
    public String makeServiceCall(String reqUrl)
    {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e("ddddd", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e("dddd", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("Dddd", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ddd", "Exception: " + e.getMessage());
        }
        return response;
    }

    @SuppressWarnings({"deprecation", "deprecation"})
    public String sendJSONPost(String URL, JSONObject json1) {
        String res = "";
        String response = "";
        //String jsonstatic="";
        try {
            urlObj = new URL(URL);

            conn = (HttpURLConnection) urlObj.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // application/x-www-form-urlencoded
            //conn.setRequestProperty("Accept-Charset", "application/json;charset=utf-8");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setReadTimeout(90000);
            conn.setConnectTimeout(90000);

            conn.connect();

            // paramsString = sbParams.toString();

            wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json1.toString());
            wr.flush();
            wr.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {


            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            Log.e("Code>>", HttpResult + "");

            InputStream errorstream = conn.getErrorStream();

            BufferedReader br = null;
            if (errorstream == null) {
                InputStream inputstream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputstream));
            } else {
                br = new BufferedReader(new InputStreamReader(errorstream));
            }

            String nachricht;
            while ((nachricht = br.readLine()) != null) {
                response += nachricht;
            }
            Log.e("response>>", response.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        conn.disconnect();

        return response;
    }

    public String sendJSONPost1(String URL) {
        String res = "";
        String response = "";
        String jsonstatic = "";
        try {
            urlObj = new URL(URL);

            conn = (HttpURLConnection) urlObj.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // application/x-www-form-urlencoded
            //conn.setRequestProperty("Accept-Charset", "application/json;charset=utf-8");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setReadTimeout(90000);
            conn.setConnectTimeout(90000);

            conn.connect();

            // paramsString = sbParams.toString();

            wr = new DataOutputStream(conn.getOutputStream());
            //  wr.writeBytes(json1.toString());
            wr.flush();
            wr.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {


            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            Log.e("Code>>", HttpResult + "");

            InputStream errorstream = conn.getErrorStream();

            BufferedReader br = null;
            if (errorstream == null) {
                InputStream inputstream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputstream));
            } else {
                br = new BufferedReader(new InputStreamReader(errorstream));
            }

            String nachricht;
            while ((nachricht = br.readLine()) != null) {
                response += nachricht;
            }
            Log.e("response>>", response.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        conn.disconnect();

        return response;
    }

    public String postRequesthtpps(String url, String json) {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpPost postMethod = new HttpPost(newURI);
        try {


            postMethod.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
            postMethod.setHeader("Content-Type", "application/json");
            response = httpClient.execute(postMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }
    public String postRequestAuth(String url, String json) {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpPost postMethod = new HttpPost(newURI);
        try {


            postMethod.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
            postMethod.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(Constants.username, Constants.password+":"+Constants.otp), "UTF-8", false));
            postMethod.setHeader("Content-Type", "application/json");

            response = httpClient.execute(postMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }

    public String getRequestAuth(String url, String json) {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpGet postMethod = new HttpGet(newURI);
        try {


            //postMethod.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
            postMethod.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(Constants.username, Constants.password+":"+Constants.otp), "UTF-8", false));
            postMethod.setHeader("Content-Type", "application/json");

            response = httpClient.execute(postMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }

    /*public String postRequesthtpps_multy(String url, MultipartEntity json) {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpPost postMethod = new HttpPost(newURI);
        try {


            postMethod.setEntity(json);
            postMethod.setHeader("Content-Type", "application/json");
            response = httpClient.execute(postMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }*/

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();


        return sb.toString();
    }

    public String getRequesthtpps(String url)
    {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith(HTTPS_STRING));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpGet getMethod = new HttpGet(newURI);
        try {

            response = httpClient.execute(getMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }

}
