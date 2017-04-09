package com.catcha.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonObject;

public class CaptchaValidator {

	private static final String WEBSERVICE_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String SECRET_KEY = "6LdSFxwUAAAAAHMtUlsI1ogZqVWGcx04ygaOHpqQ";

	public static Boolean validate(String captchaResponse) {

		if (isNullOrEmpty(captchaResponse)) {
			return Boolean.FALSE;
		}

		String response = sendRequest(captchaResponse);
		
		if(isNullOrEmpty(response)) {
			return Boolean.FALSE;
		}

		JsonObject jsonObject = Json.createReader(new StringReader(response)).readObject();
		return getBooleanFromJson(jsonObject, "success");
	}

	private static String sendRequest(String catchaResponse) {
		StringBuilder responseJson = new StringBuilder();
		try {

			URL url = new URL(WEBSERVICE_URL);
			URLConnection urlConnection = url.openConnection();

			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			httpURLConnection.setDoOutput(true);

			String parameters = "secret=" + SECRET_KEY + "&response=" + catchaResponse;

			DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			InputStream inputStream = httpURLConnection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				responseJson.append(line);
			}

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();

			String responseJsonString = responseJson.toString();

			return responseJsonString;

		} catch (Exception e) {
			System.err.println(e);
		}

		return null;
	}

	private static Boolean getBooleanFromJson(JsonObject jsonObject, String key) {

		if (jsonObject.containsKey(key)) {
			return jsonObject.getBoolean(key);
		}

		return Boolean.FALSE;
	}
	
	private static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

}
