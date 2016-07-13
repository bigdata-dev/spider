package com.ryxc.spider;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TestLogin {
	@Test
	public void test1() throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		
		HttpPost httpPost = new HttpPost("http://svn.jundie.net/user/login");
		//组装参数
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("uid", "crxy"));
		parameters.add(new BasicNameValuePair("pwd", "www.crxy.cn"));
		
		HttpEntity entity = new UrlEncodedFormEntity(parameters);
		httpPost.setEntity(entity );
		CloseableHttpResponse response = client.execute(httpPost);
		//获取请求的状态码
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		if(statusCode==302){
			Header[] headers = response.getHeaders("Location");
			String redrectUrl = "";
			if(headers.length>0){
				String value = headers[0].getValue();
				redrectUrl = value;
			}
			httpPost.setURI(new URI("http://svn.jundie.net"+redrectUrl));
			response = client.execute(httpPost);
			HttpEntity entity2 = response.getEntity();
			System.out.println(EntityUtils.toString(entity2));
		}
		
		
		
		
		
		
	}

}
