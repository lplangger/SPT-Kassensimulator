package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BlueCodeClient {

	final private Client client;
	private String bluecodeHost; // https://api.mailjet.com --> hhtps://merchant-api.bluecode.com

	public BlueCodeClient(String bluecodeHost, String user, String password) {
		this.client = ClientBuilder.newClient();
		client.register(new BasicAuthenticator(user, password));
		this.bluecodeHost = bluecodeHost;
	}

	void call(){
		 WebTarget webTarget = client.target(mailjetHost).path(path);

	        ObjectMapper mapper = new ObjectMapper();
	       
	        Response response
	                = getInvocationBuilder(webTarget).post(
	                Entity.entity(mapper.writeValueAsString(jsonMap),
	                        MediaType.APPLICATION_JSON_TYPE));

	        MailJetDataResponse mailJetResponse = null;
	        if (response.getStatus() == Response.Status.OK.getStatusCode() ||
	                response.getStatus() == Response.Status.CREATED.getStatusCode()) {
	            mailJetResponse = mapper.readValue(response.readEntity(String.class), MailJetDataResponse.class);
	        } else {
	            throw new MailjetClientException(
	                    "Error Mailjet request, status: " + response.getStatus());
	        }
	        return mailJetResponse;
	}
	
	
	
	public ArrayList<String> postRequestWithDataResponse(String path) throws IOException {

		WebTarget webTarget = client.target(mailjetHost).path(path);
		getInvocationBuilder(webTarget).post(Entity.entity(entity, mediaType)(name, type, data));

		Response response = getInvocationBuilder(webTarget)
				.post(Entity.entity(mapper.writeValueAsString(jsonMap), MediaType.APPLICATION_JSON_TYPE));

		MailJetDataResponse mailJetResponse = null;
		if (response.getStatus() == Response.Status.OK.getStatusCode()
				|| response.getStatus() == Response.Status.CREATED.getStatusCode()) {
			mailJetResponse = mapper.readValue(response.readEntity(String.class), MailJetDataResponse.class);
		} else {
			throw new MailjetClientException("Error Mailjet request, status: " + response.getStatus());
		}
		return mailJetResponse;
	}

	public MailJetDataResponse postRequestWithDataResponse(String path, String jsonString)
			throws IOException, MailjetClientException {

		WebTarget webTarget = client.target(mailjetHost).path(path);

		Response response = getInvocationBuilder(webTarget)
				.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));

		ObjectMapper mapper = new ObjectMapper();
		MailJetDataResponse mailJetResponse = null;
		if (response.getStatus() == Response.Status.OK.getStatusCode()
				|| response.getStatus() == Response.Status.CREATED.getStatusCode()) {
			mailJetResponse = mapper.readValue(response.readEntity(String.class), MailJetDataResponse.class);
		} else {
			throw new MailjetClientException("Error Mailjet request, status: " + response.getStatus());
		}

		return mailJetResponse;

	}

	public MailJetDataResponse postRequestWithDataResponse(String path, JsonObject jsonObject)
			throws IOException, MailjetClientException {

		return postRequestWithDataResponse(path, jsonObject.toString());

	}

	public Response postRequest(String path, Map<String, ? extends Object> jsonMap) throws JsonProcessingException {
		WebTarget target = client.target(mailjetHost).path(path);
		ObjectMapper mapper = new ObjectMapper();

		Response response = null;
		if (null != jsonMap) {
			response = getInvocationBuilder(target)
					.post(Entity.entity(mapper.writeValueAsString(jsonMap), MediaType.APPLICATION_JSON_TYPE));
		} else {
			response = getInvocationBuilder(target).post(Entity.json(""));
		}

		return response;

	}

	public Response postRequest(String path, JsonObject jsonObject) {

		WebTarget target = client.target(mailjetHost).path(path);
		Response response = null;
		if (null != jsonObject) {
			response = getInvocationBuilder(target)
					.post(Entity.entity(jsonObject.toString(), MediaType.APPLICATION_JSON_TYPE));
		} else {
			response = getInvocationBuilder(target).post(Entity.json(""));
		}

		return response;

	}

	private Invocation.Builder getInvocationBuilder(WebTarget target) {
		return target.request(MediaType.TEXT_PLAIN_TYPE);
	}

}
