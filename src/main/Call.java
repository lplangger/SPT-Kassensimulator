package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

@SuppressWarnings({ "deprecation" })
public class Call {

	private String PATH0 = "https://";
	private String PATH1 = "merchant-api.bluecode.";
	private String PATH2;
	private String PATH3 = "/v3/transactions";
	private String PATH4;
	private SPTKassensimulator frame;
	CloseableHttpClient client;

	public Call(SPTKassensimulator f) {
		frame = f;
		SSLContextBuilder builder = new SSLContextBuilder();
		try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			frame.addToArea("Error - no request", 1);
			frame.addToArea(SPTKassensimulator.getStringFromException(e), 2);
		}
	}

	// returns: 2 JSONObjects: the first is the https response code and the
	// second is the response of BC
	@SuppressWarnings("unchecked")
	public ArrayList<String> pay(Merchant merch, ArrayList<BasicNameValuePair> urlParams, boolean cusConf) {
		PATH2 = merch.getRealm();
		PATH4 = cusConf ? "/listen" : "/pay";
		ArrayList<String> retList = null;
		try {
			// config
			HttpPost post = new HttpPost(getFullPath(merch));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// TODO auth
			post.setEntity(new UrlEncodedFormEntity(urlParams));
			CloseableHttpResponse response = client.execute(post);
			// out
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sbContent = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sbContent.append(line);
			}
			rd.close();
			// JSONObject
			retList = new ArrayList<String>();
			JSONObject o1 = new JSONObject();
			o1.put("code", response.getStatusLine().getStatusCode());
			o1.put("text", response.getStatusLine().getReasonPhrase());
			retList.add(o1.toJSONString());
			retList.add(sbContent.toString());
		} catch (Exception e) {
			frame.addToArea("Error - no request", 1);
			frame.addToArea(SPTKassensimulator.getStringFromException(e), 2);
			retList.add(" ");
			retList.add(" ");
			return retList;
		}
		return retList;
	}

	private String getFullPath(Merchant m) {
		return PATH0 + m.getName() + ":" + m.getApiKey() + "@" + PATH1 + PATH2 + PATH3 + PATH4;
	}
}