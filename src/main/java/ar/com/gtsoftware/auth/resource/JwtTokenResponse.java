package ar.com.gtsoftware.auth.resource;

import java.io.Serializable;

public record JwtTokenResponse(String token) implements Serializable {}
