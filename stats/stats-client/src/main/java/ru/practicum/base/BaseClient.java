package ru.practicum.base;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ViewStatsDto;

import java.util.List;
import java.util.Map;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendPostRequest(HttpMethod.POST, path, parameters, body, null);
    }

    protected ResponseEntity<List<ViewStatsDto>> getListStats(String s, @Nullable Map<String, Object> parameters) {
        return makeAndSendGetRequest(s, parameters);
    }

    private static ResponseEntity<List<ViewStatsDto>> prepareClientGetResponse(ResponseEntity<List<ViewStatsDto>> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    private static ResponseEntity<Object> prepareClientPostResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    private ResponseEntity<List<ViewStatsDto>> makeAndSendGetRequest(String path, @Nullable Map<String, Object> parameters) {
        HttpEntity<List<ViewStatsDto>> requestEntity = new HttpEntity<>(null, defaultHeaders(null));
        ResponseEntity<List<ViewStatsDto>> statsServiceResponse;
        try {
            if (parameters != null) {
                statsServiceResponse = rest.exchange(path, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ViewStatsDto>>() {
                }, parameters);
            } else {
                statsServiceResponse = rest.exchange(path, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ViewStatsDto>>() {
                });
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return prepareClientGetResponse(statsServiceResponse);
    }

    private <T> ResponseEntity<Object> makeAndSendPostRequest(HttpMethod method,
                                                              String path,
                                                              @Nullable Map<String, Object> parameters,
                                                              @Nullable T body,
                                                              Long userId) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));
        ResponseEntity<Object> statsServiceResponse;
        try {
            if (parameters != null) {
                statsServiceResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServiceResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareClientPostResponse(statsServiceResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }
}
