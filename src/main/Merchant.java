package main;

import java.io.Serializable;

public class Merchant implements Serializable {
	private static final long serialVersionUID = -8536944820416544135L;
	String name;
	String apiKey;
	String description;
	String realm;

	public Merchant(String name, String apiKey, String description, String realm) {
		this.name = name;
		this.apiKey = apiKey;
		this.description = description;
		this.realm = realm;
	}

	public String getName() {
		return name;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getDescription() {
		return description;
	}

	public String getRealm() {
		return realm;
	}

	public String toShowableString() {
		int l = name.length();
		String showableName = l > 23 ? name.substring(0, 13) + "..." + name.substring(l - 7, l) : name;
		String showableRealm = realm.length() == 3 ? realm + " " : realm;
		String showableDesc = description.trim().length() <= 0 ? "" : " (" + description + ")";
		return showableRealm + " - " + showableName + showableDesc;
	}

}