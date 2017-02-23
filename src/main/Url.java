package main;

public class Url {
	private String protokoll, authName, authPasswd, domain, topLevelDomain, ending;

	public Url(String protokoll, String authName, String authPasswd, String domain, String topLevelDomain,
			String ending) {
		this.protokoll = protokoll;
		this.authName = authName;
		this.authPasswd = authPasswd;
		this.domain = domain;
		this.topLevelDomain = topLevelDomain;
		this.ending = ending;
	}

	public String toString(boolean withAuth) {
		if (withAuth)
			return protokoll + authName + ":" + authPasswd + "@" + domain + topLevelDomain + ending;
		return protokoll + domain + topLevelDomain + ending;
	}

	public String getProtokoll() {
		return protokoll;
	}

	public void setProtokoll(String protokoll) {
		this.protokoll = protokoll;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthPasswd() {
		return authPasswd;
	}

	public void setAuthPasswd(String authPasswd) {
		this.authPasswd = authPasswd;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTopLevelDomain() {
		return topLevelDomain;
	}

	public void setTopLevelDomain(String topLevelDomain) {
		this.topLevelDomain = topLevelDomain;
	}

	public String getEnding() {
		return ending;
	}

	public void setEnding(String ending) {
		this.ending = ending;
	}

}