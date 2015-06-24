package com.epam.aem_training.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

@Component(metatype = true, label = "Google Auth Helper Service")
@Service(GoogleAuthHelperService.class)
public class GoogleAuthHelperService {
	
	private static final String CLIENT_ID = "984766868515-t14l41cu2r6n79aotj9qqhjsprkhu6mr.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "9m7PdCPE48_xJ4ptd1YNP93G";
	private static final String CALLBACK_URI = "http://localhost:4502/services/googlelogin";

	private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	private GoogleAuthorizationCodeFlow flow;
	private MemoryCredentialStore store = new MemoryCredentialStore();
	
	@Activate
    protected void activate(final Map<String, Object> config) {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).setCredentialStore(store).build();
    }
	
	public String buildLoginUrl() {
		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		return url.setRedirectUri(CALLBACK_URI).build();
	}

	public String getAuthToken(final String userId) throws IOException {
		final Credential credential = flow.loadCredential(userId);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();
		
		return jsonIdentity;
	}
	
	public String exchangeCodeForCredential(final String authCode) throws IOException {
		
		final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = new GoogleCredential();
		credential.setFromTokenResponse(response);
		
		Userinfo userinfo = getUserInfoByCredential(credential);		
		return String.valueOf(userinfo.getEmail().hashCode());
	}
		
	private Userinfo getUserInfoByCredential(Credential credential) throws IOException {	
		Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("AEM Training").build();
		Userinfo userinfo = oauth2.userinfo().get().execute();
		store.store(String.valueOf(userinfo.getEmail().hashCode()), credential);
		return userinfo;
	}
	
	public Userinfo getUserInfo(String userId) throws IOException {
		Credential credential = flow.loadCredential(userId);
		return getUserInfoByCredential(credential);
	}
	
	public String getUserName(String userId) throws IOException {
		return getUserInfo(userId).getName();
	}
	
	public String getUserEmail(String userId) throws IOException {
		return getUserInfo(userId).getEmail();
	}
	
	
}
