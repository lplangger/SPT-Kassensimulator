package main;

import java.io.BufferedReader;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.JSONParser;

public class Calls_NOT_IN_USE {

	private static String PATH0 = "https://";
	private static String PATH1 = "merchant-api.bluecode.";
	private static String PATH2 = "mobi";
	private static String PATH3 = "/v3/transactions";
	private static String PATH4 = "/pay";

	public static void main(String[] args) {
		
	}
	
	@SuppressWarnings("unused")
	private static ArrayList<String> reqHTTPS(Merchant m, ArrayList<BasicNameValuePair> urlParameters) {
		//ClientBuilder c; import javax.ws.rs.client.ClientBuilder;
		URL url;
		try {
			trustAllHosts();
			url = new URL(PATH0 + m.getName() + ":" + m.getApiKey() + "@" + PATH1 + PATH2 + PATH3 + PATH4);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setHostnameVerifier(DO_NOT_VERIFY);
			con.setDoOutput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Authorization", m.getName() + "," + m.getApiKey());
			// SSLSocketFactory x = con.getSSLSocketFactory();
			// x.createSocket(PATH0 + PATH1 + PATH2, 443);
			// con.setSSLSocketFactory(x);
			for (BasicNameValuePair b : urlParameters) {
				con.addRequestProperty(b.getName(), b.getValue());
			}
			System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String input;

			while ((input = br.readLine()) != null) {
				System.out.println(input);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String request(Url url, String branch, String terminal, String operator, String slip,
			String slipDateTime, String currency, String amount, String barcode) throws Exception {
		// build urlParameters for request
		ArrayList<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("branch", URLEncoder.encode(branch, "UTF-8")));
		urlParameters.add(new BasicNameValuePair("terminal", URLEncoder.encode(terminal, "UTF-8")));
		urlParameters.add(new BasicNameValuePair("operator", URLEncoder.encode(operator, "UTF-8")));
		urlParameters.add(new BasicNameValuePair("slip", URLEncoder.encode(slip, "UTF-8")));
		// urlParameters.add(new BasicNameValuePair("slipDateTime",
		// URLEncoder.encode(slipDateTime, "UTF-8")));
		urlParameters.add(new BasicNameValuePair("slipDateTime", slipDateTime));
		urlParameters.add(new BasicNameValuePair("currency", currency));
		urlParameters.add(new BasicNameValuePair("amount", amount));
		urlParameters.add(new BasicNameValuePair("barcode", barcode));
		// request
		// ArrayList<String> responseListDefault = reqTest(urlParameters);

		JSONParser parser = new JSONParser();
		// JSONObject topObj = (JSONObject)
		// parser.parse(responseListDefault.get(1));
		// // pending
		// if (topObj.containsKey("state") &&
		// topObj.get("state").equals("PENDING")) {
		// ArrayList<BasicNameValuePair> urlParametersPending = new
		// ArrayList<BasicNameValuePair>();
		// String uuid = (String) ((JSONObject) topObj.get("data")).get("uuid");
		// urlParametersPending.add(new BasicNameValuePair("uuid", uuid));
		// url.setEnding("/v3/transactions/listen");
		// ArrayList<String> responseListPending = reqTest(url,
		// urlParametersPending);
		// FileHandler.createInvoice(responseListPending.get(1));
		// return responseListDefault.get(0) + "\n" + responseListDefault.get(1)
		// + "\n\n" + responseListPending.get(0)
		// + "\n" + responseListPending.get(1);
		// }
		// Invoice.createInvoice(responseListDefault.get(1));
		return null;// responseListDefault.get(0) + "\n" +
					// responseListDefault.get(1);
	}

	// private static ArrayList<String> reqTest(ArrayList<BasicNameValuePair>
	// urlParameters) throws Exception {
	// SSLContextBuilder builder = new SSLContextBuilder();
	// builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	// SSLConnectionSocketFactory sslsf = new
	// SSLConnectionSocketFactory(builder.build());
	// // CloseableHttpClient client =
	// // HttpClients.custom().setSSLSocketFactory(sslsf).build();
	//
	// CredentialsProvider credsProvider = new BasicCredentialsProvider();
	// credsProvider.setCredentials(new
	// AuthScope("https://merchant-api.bluecode.biz", 443),
	// new UsernamePasswordCredentials(url.getAuthName(), url.getAuthPasswd()));
	// // CloseableHttpClient httpclient =
	// //
	// HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
	// CloseableHttpClient httpclient =
	// HttpClients.custom().setSSLSocketFactory(sslsf)
	// .setDefaultCredentialsProvider(credsProvider).build();
	//
	// HttpPost post = new HttpPost(url.toString(false));
	// new org.apache.http.entity.mime.Header().addField(new MinimalField("",
	// ""));
	// post.setHeader("Content-Type", "application/x-www-form-urlencoded");
	// // post.setHeaders(arg0);
	// post.setEntity(new UrlEncodedFormEntity(urlParameters));
	// CloseableHttpResponse response = httpclient.execute(post);
	//
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(response.getEntity().getContent()));
	// StringBuffer sbContent = new StringBuffer();
	// String line = "";
	// while ((line = rd.readLine()) != null) {
	// sbContent.append(line);
	// }
	// rd.close();
	// ArrayList<String> retList = new ArrayList<String>();
	// retList.add(response.getStatusLine().getStatusCode() + " " +
	// response.getStatusLine().getReasonPhrase());
	// retList.add(sbContent.toString());
	// return retList;
	// }

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	// Trust every server - dont check for any certificate
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
