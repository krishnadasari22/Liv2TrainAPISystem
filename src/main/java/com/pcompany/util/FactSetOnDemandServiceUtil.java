package com.pcompany.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pcompany.entity.FactSetOnDemandSessionInfo;
import com.televisory.capitalmarket.util.CMStatic;

/**
 * 
 * @author vinay
 *
 */
@Component
public class FactSetOnDemandServiceUtil {

	private static final Logger _log = Logger.getLogger(FactSetOnDemandServiceUtil.class);
	
	/**
	 * Method to get token  
	 * @param sessionInfo
	 * @return
	 */
	@Value("${CM.FACTSET.ENVIRONMENT}")
	private String environment;

	@Value("${CM.FACTSET.BETA.ONDEMAND.SERVICE.HOST}")
	private String betaOnDemandServiceHost;
	
	@Value("${CM.FACTSET.BETA.ONDEMAND.AUTH.CHECK.PATH}")
	private String betaAuthCheckPath;

	@Value("${CM.FACTSET.BETA.ONDEMAND.FETCH.TOKEN.URL}")
	private String betaTokenURL;
	
	@Value("${CM.FACTSET.PROD.ONDEMAND.SERVICE.HOST}")
	private String prodOnDemandServiceHost;
	
	@Value("${CM.FACTSET.PROD.ONDEMAND.AUTH.CHECK.PATH}")
	private String prodAuthCheckPath;

	@Value("${CM.FACTSET.PROD.ONDEMAND.FETCH.TOKEN.URL}")
	private String prodTokenURL;
	
		
	public String getToken(FactSetOnDemandSessionInfo sessionInfo) {
		
		String session_token = null;
			
		String keyId = sessionInfo.getKeyId();
		String key = sessionInfo.getFactSetKey();
		long counter = Long.parseLong(sessionInfo.getCounter());
		
		String username = sessionInfo.getUserName();
		String serial = sessionInfo.getSerial();
		
		try {
			String Otp = constructOTP(counter, key);
			session_token = sendOtpAndGetToken(username,serial, keyId, Otp);
			_log.info("Session token: " + session_token);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (DecoderException d1) {
			d1.printStackTrace();
		} catch (InvalidKeyException ik) {
			ik.printStackTrace();
		}
		return session_token;
	}
	
	
	public String constructOTP(final Long counter, final String key)
			throws NoSuchAlgorithmException, DecoderException, InvalidKeyException {
		
		// setup the HMAC algorithm, setting the key to use
		final Mac mac = Mac.getInstance("HmacSHA512");
		
		// convert the key from a hex string to a byte array
		final byte[] binaryKey = Hex.decodeHex(key.toCharArray());
		
		// initialize the HMAC with a key spec created from the key
		mac.init(new SecretKeySpec(binaryKey, "HmacSHA512"));
		
		// compute the OTP using the bytes of the counter
		byte[] computedOtp = mac.doFinal(ByteBuffer.allocate(8).putLong(counter).array());
		
		//
		// increment the counter and store the new value
		//
		// return the value as a hex encoded string
		return new String(Hex.encodeHex(computedOtp));
	}

	public String sendOtpAndGetToken(final String username,
	   
		final String serial, final String keyId, final String otp) {
		
		try {
			// build request JSON
			JSONObject json = new JSONObject();
			json.put("username", username);
			json.put("serial", serial);
			json.put("keyId", keyId);
			json.put("otp", otp); 
		
			String json_req = json.toString();
			
			_log.info("JSon Req: " + json_req);
			
			HttpClient client = HttpClientBuilder.create().build();
	
			String tokenURL;
			if(CMStatic.FACTSET_ENVIRONMENT_BETA.equals(environment))
				tokenURL = betaTokenURL;
			else
				tokenURL = prodTokenURL;
				
			
			// make HTTP request and get response
			HttpPost request = new HttpPost(tokenURL);
			
			// use code below instead if you are using the Production
			// environment
			// HttpPost request = new HttpPost
			// ("https://auth.factset.com/fetchotpv1");
			request.setHeader("Content-Type", "application/json");
			request.setEntity(new StringEntity(json_req));
			
			HttpResponse response = client.execute(request);
			
			String tokenHeader = null;
			
			// read the auth token header from the response
			if (response.getStatusLine().getStatusCode() != 200) {
				_log.info("Did not get an OK status");
				_log.info("Status: " + response.getStatusLine());
				String req_key = response.getFirstHeader(
						"X-DataDirect-Request-Key").getValue();
				_log.info("Req key:" + req_key);
			} else {
				tokenHeader = response.getFirstHeader("X-Fds-Auth-Token")
						.getValue();
			}
			
			// return the value of the auth header if found,
			// or null otherwise
			return tokenHeader;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	/* This function will return HttpGet object with URL information */
	public HttpGet generateHttpGet(Map<String, String> requestProps) throws URISyntaxException {

		// use code below instead if you are using the Production
		// environment
		// builder.setScheme("https").setHost("datadirect.factset.com")

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https");

		Set<String> propertyList = requestProps.keySet();
		
		for (String property : propertyList) {
			if (property.equalsIgnoreCase("URL")) {
				builder = new URIBuilder(requestProps.get(property));
				break;
			} else if (property.equalsIgnoreCase("host")) {
				builder.setHost(requestProps.get(property));
			} else if (property.equalsIgnoreCase("path")) {
				builder.setPath(requestProps.get(property));
			} else 
				builder.setParameter(property, requestProps.get(property));
			
		}
		
		/*requestProps.forEach((property,value) -> {
			if (property.equalsIgnoreCase("host")) {
				builder.setHost(value);
			} else if (property.equalsIgnoreCase("path")) {
				builder.setPath(value);
			} else 
				builder.setParameter(property, value);
		});*/

		URI uri;
		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		_log.info("http request "+uri.toString());
		
		HttpGet request = new HttpGet(uri);
		return request;
	}
	
	
	/**
	 * Function to validate token 
	 * @param sessionToken
	 * @return
	 * @throws Exception 
	 */
	public String validateSessionToken(String sessionToken) throws Exception{
		
		InputStreamReader isr;
		BufferedReader in;
		String sResponse = "";
		
		Map<String,String> requestProps =new HashMap<String, String>();
		
		
		String onDemandServiceHost;
		String onDemandAuthCheck;
		
		if(CMStatic.FACTSET_ENVIRONMENT_BETA.equals(environment)) {
			onDemandServiceHost = betaOnDemandServiceHost;
			onDemandAuthCheck = betaAuthCheckPath;
		} else {
			onDemandServiceHost = prodOnDemandServiceHost;
			onDemandAuthCheck = prodAuthCheckPath;
		}
		
		requestProps.put("host", onDemandServiceHost);
		requestProps.put("path", onDemandAuthCheck);
	
		// Call this function to use an existing token
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			
			HttpGet request = generateHttpGet(requestProps);
			
			request.setHeader("X-Fds-Auth-Token", sessionToken);
			
			_log.info("Adding auth token: " + sessionToken);
			_log.info("Http request:" + request);
			
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				
				// read response from server
				if (response.getStatusLine().getStatusCode() != 200) {
					
					_log.info("Did not get an OK status");
					_log.info("Status:" + response.getStatusLine());
					
					String req_key = response.getFirstHeader(
							"X-DataDirect-Request-Key").getValue();
					_log.info("X-DataDirect-Request-Key:" + req_key);
				
				} else {
					_log.info("Got an OK status message");
					String req_key = response.getFirstHeader(
							"X-DataDirect-Request-Key").getValue();
					_log.info("X-DataDirect-Request-Key:" + req_key);
					isr = new InputStreamReader(response.getEntity()
							.getContent());
					in = new BufferedReader(isr);
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						sResponse = sResponse.concat(inputLine + "\n");
					_log.info(sResponse);
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sResponse;
	}
	
}
