package com.tomtom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Service
@Slf4j
public class TagExecutor {

    private final ExecutorService executor;

    @Autowired
    public TagExecutor() {
        final int processor = Math.max(1, Runtime.getRuntime().availableProcessors() - 2);
        this.executor = Executors.newFixedThreadPool(processor);
        log.info("Configured StateExecutor with {} threads", processor);
    }

    public <R> CompletableFuture<R> submit(Supplier<R> action) {
        return CompletableFuture.supplyAsync(action, executor);
    }

    public <R> void waitForCompletion(List<CompletableFuture<R>> allFutures) {
        var allFuturesResult = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
        allFuturesResult.join();
    }

}
