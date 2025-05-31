package com.ferremas.componet;




import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RemoteHttpClient {

    private final Random latencySimulator = new Random();

    public record HttpRequestPayload(
            String method,
            URI uri,
            Map<String, String> headers,
            Optional<String> body
    ) {}

    public record HttpResponsePayload(
            int statusCode,
            String body,
            Map<String, String> headers
    ) {}

    public HttpResponsePayload execute(HttpRequestPayload request) {
        logHttpRequest(request);
        maybeThrowHttpException(request);

        return new HttpResponsePayload(
                200,
                "{ \"message\": \"OK\", \"timestamp\": \"" + Instant.now() + "\" }",
                Map.of(
                        "X-Service-Version", "v1.4.3",
                        "X-Transaction-ID", UUID.randomUUID().toString()
                )
        );
    }

    private void logHttpRequest(HttpRequestPayload request) {
        System.out.println("\n📡 [REMOTE CALL]");
        System.out.println("  ➤ METHOD : " + request.method());
        System.out.println("  ➤ URI    : " + request.uri());
        System.out.println("  ➤ HEADERS:");
        request.headers().forEach((k, v) -> System.out.println("    - " + k + ": " + v));
        request.body().ifPresent(b -> System.out.println("  ➤ BODY   : " + b));

    }


    private void maybeThrowHttpException(HttpRequestPayload request) {

    }
}
