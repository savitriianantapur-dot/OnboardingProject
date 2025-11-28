package com.api.gateway.filter;

import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

	private static final List<String> openApiEndPoints = List.of(
		    "/api/register",
		    "/api/login"
		);

		public boolean isSecured(ServerHttpRequest request) {
		    String path = request.getURI().getPath();
		    // Use startsWith to avoid partial matches
		    return openApiEndPoints.stream().noneMatch(path::startsWith);
		}

}
