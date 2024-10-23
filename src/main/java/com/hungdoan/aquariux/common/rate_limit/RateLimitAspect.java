package com.hungdoan.aquariux.common.rate_limit;


import com.hungdoan.aquariux.dto.CustomUserDetails;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimited)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUser().getUserId();

        String methodName = joinPoint.getSignature().toShortString();
        String key = userId + ":" + methodName;

        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(rateLimited.requestAmount(), rateLimited.inSeconds()));
        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            return handleRateLimitExceeded(rateLimited.inSeconds());
        }
    }

    private Bucket createBucket(int requestAmount, long second) {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(requestAmount, Duration.ofSeconds(second)))
                .build();
    }

    private ResponseEntity<Object> handleRateLimitExceeded(long second) {
        // Create a response with status 429 (Too Many Requests)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(String.format("Rate limit exceeded. Try again in %d seconds.",
                        second));
    }
}
