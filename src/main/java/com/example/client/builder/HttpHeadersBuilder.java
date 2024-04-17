package com.example.client.builder;

import com.example.client.builder.interfaces.IHttpHeadersBuilder;
import com.example.client.dto.LoginRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class HttpHeadersBuilder implements IHttpHeadersBuilder {
    @Override
    public HttpHeaders buildHeaders() {
        return new HttpHeaders() {{
            setAccept(List.of(MediaType.APPLICATION_JSON));
        }};
    }

    @Override
    public HttpHeaders buildHeaders(LoginRequestDto loginRequest) {
        HttpHeaders httpHeaders = this.buildHeaders();

        if(loginRequest != null && loginRequest.username() != null && loginRequest.password() != null) {
            String authenticationCredentials = loginRequest.username() + ":" + loginRequest.password();
            byte[] encodedAuthenticationCredentials = Base64.getEncoder().encode(authenticationCredentials.getBytes(StandardCharsets.UTF_8));
            String authorizationHeaderValue = "Basic " + new String(encodedAuthenticationCredentials);

            httpHeaders.set("Authorization", authorizationHeaderValue);
        }

        return httpHeaders;
    }
}