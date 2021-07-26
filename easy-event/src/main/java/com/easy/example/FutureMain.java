package com.easy.example;

import com.easy.concuurent.ArchitectureThreadInvoker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yanghai
 * @since 2021/7/22 23:27
 */
public class FutureMain {

    public static void main(String[] args) throws Exception {

        ArchitectureThreadInvoker<List<String>> invoker = new ArchitectureThreadInvoker<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                list.add(i + "");
            }
            invoker.future.complete(list);
        });
        System.out.println(invoker.future.get());
        executorService.shutdown();
    }
}
