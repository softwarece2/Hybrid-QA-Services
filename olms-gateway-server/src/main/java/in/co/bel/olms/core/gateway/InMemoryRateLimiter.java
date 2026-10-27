package in.co.bel.olms.core.gateway;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to implement the in-memory rate limiter using ConcurrentHashMap
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2024-11-08
 */

public class InMemoryRateLimiter {

    private final Map<String, RateLimitInfo> rateLimiters = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long timeWindowMillis;
    private final Set<String> excludedPatterns = Set.of("/olms-websocket");

    public InMemoryRateLimiter(int maxRequests, long timeWindowSeconds) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowSeconds * 1000L;
    }

    public boolean isAllowed(String clientId, String requestPath) {

        // Skip rate limiting if the request URL is of web socket
        if (shouldExclude(requestPath)) {
            return true;
        }
        RateLimitInfo rateLimitInfo = rateLimiters.computeIfAbsent(clientId, k -> new RateLimitInfo());

        synchronized (rateLimitInfo) {
            long currentTime = Instant.now().toEpochMilli();

            // Reset count if time window has elapsed
            if (currentTime - rateLimitInfo.startTime >= timeWindowMillis) {
                rateLimitInfo.startTime = currentTime;
                rateLimitInfo.requestCount.set(0);
            }

            // Allow request if within rate limit
            if (rateLimitInfo.requestCount.incrementAndGet() <= maxRequests) {
                return true;
            }

            return false; // Rate limit exceeded
        }
    }

    private boolean shouldExclude(String path) {
        // Checking if path contains websocket request
        return excludedPatterns.stream().anyMatch(path::contains);
    }

    private static class RateLimitInfo {
        long startTime = Instant.now().toEpochMilli();
        AtomicInteger requestCount = new AtomicInteger(0);
    }
}