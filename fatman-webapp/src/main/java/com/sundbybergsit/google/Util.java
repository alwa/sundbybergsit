package com.sundbybergsit.google;

/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.sundbybergsit.fatman.ConfigurationLoader;

import java.util.Arrays;

/**
 * @author Jennifer Murphy
 * @author Lee Denison
 */
public class Util {
	
  private static final String GOOGLE_OAUTH2_AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
  private static final String GOOGLE_OAUTH2_TOKEN_URI = "https://accounts.google.com/o/oauth2/token";

	
  public static final JsonFactory JSON_FACTORY = new GsonFactory();
  public static final HttpTransport TRANSPORT = new NetHttpTransport();
  
  private static AuthorizationCodeFlow authorizationCodeFlow = null;


  /**
   * A simple HTML tag stripper to prepare HTML for rendering. This is a
   * quick and dirty solution intended to improve presentation. Please do not
   * depend on it to prevent XSS attacks.
   *
   * @return The same string with all xml/html tags stripped.
   */
  public static String stripTags(String input) {
    return input.replaceAll("\\<[^>]*>","");
  }
  
  public static AuthorizationCodeFlow getFlow() {
	if (null == authorizationCodeFlow) {
		authorizationCodeFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
		        TRANSPORT,
		        JSON_FACTORY,
		        new GenericUrl(GOOGLE_OAUTH2_TOKEN_URI),
		        new ClientParametersAuthentication(ConfigurationLoader.CLIENT_ID, ConfigurationLoader.CLIENT_SECRET),
                ConfigurationLoader.CLIENT_ID,
		        GOOGLE_OAUTH2_AUTH_URI)
			.setCredentialStore(new MemoryCredentialStore())
			.setScopes(Arrays.asList(ConfigurationLoader.SCOPES))
		    .build();
	}

	return authorizationCodeFlow;
  }

    public static void resetFlow() {
        authorizationCodeFlow = null;
    }
}
