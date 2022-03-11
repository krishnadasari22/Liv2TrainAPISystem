package com.pcompany.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.pcompany.dao.FactSetOnDemandDao;
import com.pcompany.entity.CompanyDocumentMetadata;
import com.pcompany.entity.FactSetOnDemandSessionInfo;
import com.pcompany.util.FactSetOnDemandServiceUtil;
import com.televisory.capitalmarket.util.CMStatic;

/**
 * 
 * @author vinay
 *
 */
@Service
public class FactSetOnDemandService {
	
	private static final Logger _log = Logger.getLogger(FactSetOnDemandService.class);
	
	@Autowired
	FactSetOnDemandDao factSetOnDemandDao;
	
	@Autowired
	FactSetOnDemandServiceUtil factSetOnDemandServiceUtil;
	
	@Value("${CM.FACTSET.ONDEMAND.FILE.DOWNLOAD.PATH}")
	private String FILE_PATH;
	
	@Value("${CM.FACTSET.ENVIRONMENT}")
	private String environment;

	public File downloadFileOnDemand(CompanyDocumentMetadata metadata) throws IOException, Exception {
		
		//get session Info from database
		FactSetOnDemandSessionInfo factSetOnDemandSessionInfo = factSetOnDemandDao.getFactSetOndemandSesssionInfo(environment, CMStatic.FACTSET_ACCESS_LEVEL_DOD);
		
		String sessionToken =factSetOnDemandSessionInfo.getSessionToken();
		
		//Check if Session Token is valid
		String authCheckStatus = factSetOnDemandServiceUtil.validateSessionToken(sessionToken);
		
		//if session is not valid then increment the counter and get fresh token and update that in database
		if(authCheckStatus.isEmpty()){
			sessionToken = getAndUpdateSessionToken(factSetOnDemandSessionInfo,1);
		}
		
		Map<String,String> requestProps =new HashMap<>();
		File outputFile = null;
		
		requestProps.put("URL", metadata.getLink1());
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			
			HttpGet request = factSetOnDemandServiceUtil.generateHttpGet(requestProps);
			request.setHeader("X-Fds-Auth-Token", sessionToken);
			
			_log.debug("Adding auth token: " + sessionToken);
			_log.info("HTTP request:" + request);
			
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				// read response from server
				if (response.getStatusLine().getStatusCode() != 200) {
					_log.debug("Did not get an OK status");
					_log.debug("Status:" + response.getStatusLine());
					String req_key = response.getFirstHeader("X-DataDirect-Request-Key").getValue();
					_log.debug("X-DataDirect-Request-Key:" + req_key);
				} else {
					_log.debug("Got an OK status message");
					String req_key = response.getFirstHeader("X-DataDirect-Request-Key").getValue();
					_log.debug("X-DataDirect-Request-Key:" + req_key);
					_log.debug(response.getEntity().getContent().available());
					_log.info(response.getEntity().getContentType());
					
					InputStream responseStream = response.getEntity().getContent();
					String responseContentType = response.getEntity().getContentType().getValue().split(";")[0].trim();
					
					String outputFilePath;
					if(responseContentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
						outputFilePath = FILE_PATH + File.separator + metadata.getKey()+".pdf";
					} else {
						outputFilePath = FILE_PATH + File.separator + metadata.getKey()+".html";
					}
					outputFile = new File(outputFilePath);
					//create parent DIR if not exists
					Files.createDirectories(Paths.get(outputFile.getParentFile().getAbsolutePath()));
					
					BufferedInputStream bis = new BufferedInputStream(responseStream);
		            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
		           
		            int inByte;
		           
		            while((inByte = bis.read()) != -1) bos.write(inByte);
		          
		            bis.close();
		            bos.close();
		            responseStream.close();
		           _log.info("File Download Completed !");
		        }
			}
	
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return outputFile;
		
	}
	
	
	public String searchRequestXML(String url) throws IOException, Exception {

		
		//get session Info from database
		FactSetOnDemandSessionInfo factSetOnDemandSessionInfo = factSetOnDemandDao.getFactSetOndemandSesssionInfo(environment, CMStatic.FACTSET_ACCESS_LEVEL_SDF);
		
		String sessionToken = factSetOnDemandSessionInfo.getSessionToken();
		
		//Check if Session Token is valid
		String authCheckStatus = factSetOnDemandServiceUtil.validateSessionToken(sessionToken);
		
		//if session is not valid then increment the counter and get fresh token and update that in database
		if(authCheckStatus.isEmpty()){
			sessionToken = getAndUpdateSessionToken(factSetOnDemandSessionInfo,1);
		}
		
		Map<String,String> requestProps =new HashMap<>();
		String xmltext = null;
		
		requestProps.put("URL", url);
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			
			HttpGet request = factSetOnDemandServiceUtil.generateHttpGet(requestProps);
			request.setHeader("Content-Type", "text/xml");
			request.setHeader("X-Fds-Auth-Token", sessionToken);
			
			_log.info("Adding auth token: " + sessionToken);
			_log.info("HTTP request:" + request);
			
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				// read response from server
				if (response.getStatusLine().getStatusCode() != 200) {
					_log.info("Did not get an OK status");
					_log.info("Status:" + response.getStatusLine());
					String req_key = response.getFirstHeader("X-DataDirect-Request-Key").getValue();
					_log.info("X-DataDirect-Request-Key:" + req_key);
				} else {
					_log.info("Got an OK status message");
					String req_key = response.getFirstHeader("X-DataDirect-Request-Key").getValue();
					_log.info("X-DataDirect-Request-Key:" + req_key);
					_log.info(response.getEntity().getContent().available());
					
					xmltext = IOUtils.toString(response.getEntity().getContent(),StandardCharsets.UTF_8.name());
		            
					_log.info("File Download Completed !");
		        }
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}	
		return xmltext;
	}
	
	/**
	 * Recursively get the session Token
	 * @param factSetOnDemandSessionInfo
	 * @param counterIncrement
	 * @return
	 */
	private String getAndUpdateSessionToken(FactSetOnDemandSessionInfo factSetOnDemandSessionInfo ,Integer counterIncrement) {
		
		long currentCounter = Long.parseLong(factSetOnDemandSessionInfo.getCounter());
		long nextCounter= currentCounter+counterIncrement;
		factSetOnDemandSessionInfo.setCounter(nextCounter+"");
		String sessionToken = factSetOnDemandServiceUtil.getToken(factSetOnDemandSessionInfo);

		if(sessionToken!=null){
			_log.info("New Session Token is "+sessionToken +" counter is "+nextCounter);
			factSetOnDemandSessionInfo.setSessionToken(sessionToken);
			updateSessionInfo(factSetOnDemandSessionInfo);
			return sessionToken;
		}
		
		return getAndUpdateSessionToken(factSetOnDemandSessionInfo,1);
	}

	/**
	 * update new Session in database
	 * @param factSetOnDemandSessionInfo
	 */
	private void updateSessionInfo(FactSetOnDemandSessionInfo factSetOnDemandSessionInfo) {
		 factSetOnDemandDao.updateSessionInfo(factSetOnDemandSessionInfo);
	}
	
}
