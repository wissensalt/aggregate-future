package com.wissensalt.aggregatefuture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SequentialAggregationWithThenApply {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CompletableFuture<AllContents> completableFuture = completableFutureContentA()
                .thenApplyAsync(a -> completableFutureContentB(a).join())
                .thenApplyAsync(b -> completableFutureContentC(b).join());

        AllContents allContents = completableFuture.get();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        System.out.println("All Contents : "+allContents);
        System.out.println("Main Thread Time Elapse : "+timeElapsed + " ms");
    }

    private static CompletableFuture<AllContents> completableFutureContentA() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                return AllContents
                        .builder()
                        .contentA(getContentA())
                        .build();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static CompletableFuture<AllContents> completableFutureContentB(AllContents allContents) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                allContents.contentB = getContentB();

                return allContents;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static CompletableFuture<AllContents> completableFutureContentC(AllContents allContents) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                allContents.contentC = getContentC();

                return allContents;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static ContentA getContentA() throws InterruptedException {
        long startContentA = System.currentTimeMillis();
        Thread.sleep(100);

        ContentA result = ContentA.builder()
                .a1("A1")
                .a2("A2")
                .build();
        long endContentA = System.currentTimeMillis();
        long timeElapsedContentA = endContentA - startContentA;
        System.out.println("Content A Time Elapse : "+timeElapsedContentA + " ms");

        return result;
    }

    private static ContentB getContentB() throws InterruptedException {
        long startContentB = System.currentTimeMillis();
        Thread.sleep(100);
        ContentB result = ContentB.builder()
                .b1("B1")
                .b2("B2")
                .build();
        long endContentB = System.currentTimeMillis();
        long timeElapsedContentB = endContentB - startContentB;
        System.out.println("Content B Time Elapse : "+timeElapsedContentB + " ms");

        return result;
    }

    private static ContentC getContentC() throws InterruptedException {
        long startContentC = System.currentTimeMillis();
        Thread.sleep(100);

        long endContentC = System.currentTimeMillis();
        long timeElapsedContentC = endContentC - startContentC;
        ContentC result = ContentC.builder()
                .c1("C1")
                .c2("C2")
                .build();

        System.out.println("Content C Time Elapse : "+timeElapsedContentC + " ms");
        return result;
    }

    @AllArgsConstructor
    @Builder
    @Data
    private static class AllContents {
        private ContentA contentA;
        private ContentB contentB;
        private ContentC contentC;
    }

    @Builder
    @Data
    private static class ContentA {
        private String a1;
        private String a2;
    }

    @Builder
    @Data
    private static class ContentB {
        private String b1;
        private String b2;
    }

    @Builder
    @Data
    private static class ContentC {
        private String c1;
        private String c2;
    }
}
