package tech.bgdigital.online.payment.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String first_name;
	private String email;

	public JwtResponse(String accessToken, Long id, String first_name, String email) {
		this.token = accessToken;
		this.id = id;
		this.first_name = first_name;
		this.email = email;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return first_name;
	}

	public void setUsername(String username) {
		this.first_name = username;
	}
}
