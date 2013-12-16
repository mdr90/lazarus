package com.android.lazarus.serviceadapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.android.lazarus.helpers.ConstantsHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ObstacleReportingServiceAdapterImpl implements ObstacleReportingServiceAdapter {

	@Override
	public boolean reportObstacle(String token, String coordinates,
			String radius, String description) {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(ConstantsHelper.REST_API_URL
				+ "/obstacles");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("coordinates",
					coordinates));
			nameValuePairs.add(new BasicNameValuePair("radius", radius));
			nameValuePairs.add(new BasicNameValuePair("description",
					description));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			request.addHeader("Authorization", token);
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			JsonObject jsonResponse = new JsonParser().parse(rd.readLine())
					.getAsJsonObject();
			if (jsonResponse.get("result").getAsString().equals("OK"))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deactivateObstacle(String token, String coordinates) {
		HttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(ConstantsHelper.REST_API_URL
				+ "/obstacles/" + coordinates);
		try {
			request.addHeader("Authorization", token);
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			JsonObject jsonResponse = new JsonParser().parse(rd.readLine())
					.getAsJsonObject();
			if (jsonResponse.get("result").getAsString().equals("OK"))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
