package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {
    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.out.println("Exception is : " + e );
                    return Flux.just("default", "default1");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }
}
