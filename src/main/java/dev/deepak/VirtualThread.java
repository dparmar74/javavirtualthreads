package dev.deepak;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class VirtualThread {
  public static void main(String[] args) {
    VirtualThread var = new VirtualThread();
    var.fetchURLInParallel(false);
    var.fetchURLInParallel(true);

  }

  private void fetchURLInParallel(boolean useVirtualThread) {
    int cores = Runtime.getRuntime().availableProcessors();
    System.out.println("No of Cores:" + cores);

    try (var executor = useVirtualThread ? Executors.newVirtualThreadPerTaskExecutor() : Executors.newFixedThreadPool(cores)) {


      List<CompletableFuture<String>> tasks = new ArrayList<>();

      System.out.println("Fetching URL tasks started using " + (useVirtualThread ? " virtual threads" : " platform threads"));
      long start = System.currentTimeMillis();
      IntStream.range(0, 100).forEach(i -> {
        var fetchTask = CompletableFuture.supplyAsync(() -> {
          try {
            return fetchURL();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }, executor);
        tasks.add(fetchTask);
      });

      tasks.stream().forEach(task -> {
        try {
          task.get();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

      long end = System.currentTimeMillis();
      System.out.println("Fetching URL tasks completed in " + (end - start) + " ms\n");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  private String fetchURL() throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder(URI.create("https://yahoo.com/")).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

}