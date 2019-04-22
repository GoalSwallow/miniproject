package com.swallow.miniproject.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import com.swallow.miniproject.bean.AccessToken;
import com.swallow.miniproject.bean.JsapiTicket;
import com.swallow.miniproject.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;


@Slf4j
public class WeChatUtil {

	/**
	 * 处理Get请求
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doGetUrl(String url) throws ClientProtocolException, IOException {
		JSONObject jsonObject = new JSONObject();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();
		if(httpEntity != null) {
			String result = EntityUtils.toString(httpEntity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * 处理POST请求
	 * @param url
	 * @param ourStr
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doPoatUrl(String url, String ourStr) throws ClientProtocolException, IOException {
		JSONObject jsonObject = new JSONObject();
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(ourStr,"UTF-8"));
		HttpResponse httpResponse = client.execute(httpPost);
		HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();
		if(httpEntity != null) {
			String result = EntityUtils.toString((org.apache.http.HttpEntity) httpEntity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	/**
	 * 获取access_token凭证
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ParseException, IOException{
		AccessToken token = new AccessToken();
		String url = Constant.ACCESS_TOKEN_URL.replace("APPID", Constant.APPID).replace("APPSECRET", Constant.APPSECRET);
		JSONObject jsonObject = doGetUrl(url);
		if(jsonObject!=null){
			token.setAccess_token(jsonObject.getString("access_token"));
			token.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    //UUID
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    
    //时间戳
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    //获取JsapiTicket(jsksdk)
    public static JsapiTicket getJsapiTicket(String token){
		JsapiTicket ticket=new JsapiTicket();
		String url = Constant.JS_API_TICKET_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = null;
		try {
			jsonObject = WeChatUtil.doGetUrl(url);
			if(jsonObject.getString("errcode").equals("0")){
				ticket.setTicket(jsonObject.getString("ticket"));
				ticket.setExpiresIn(jsonObject.getString("expires_in"));
			}else{
				log.error("error");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ticket;
	}

    //获取jsksdk接口参数
    public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		//注意这里 要加上自己的appId
		ret.put("appId", Constant.APPID);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}

	//改变url参数
	public static String replaceUrl(String url, String name, String accessToken) {
		if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(accessToken)) {
			url = url.replaceAll("(" + name +"=[^&]*)", name + "=" + accessToken);
		}
		return url;

	}
}
