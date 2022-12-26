package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    /*
    threadCount - количество клиентских потоков (>= 1)
readQuota   - доля запросов getBalance (>= 0)
writeQuota  - доля запросов changeBalance (>= 0)
readIdList  - список идентификаторов для getBalance
writeIdList - список идентификаторов для changeBalance
     */

    private static final int threadCount = 5;

    private static final int readQuota = 10;

    private static final int writeQuota = 10;

    private static final List<Integer> readIdList = List.of(1, 15, 38, 82, 357, 458, 660, 3, 2);

    private static final List<Integer> writeIdList = List.of(1, 15, 38, 82, 357, 458, 660, 3, 2);


    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(threadCount);

        service.submit((Runnable) () -> {
            while (true) {
                // вероятность вызова метода getBalance
                double readProbability = (double) readQuota / (double) (readQuota + writeQuota);

                if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                    try {
                        getBalance(randomFromList(readIdList));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        changeBalance(randomFromList(writeIdList), 1L);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private static void changeBalance(int randomFromList, long l) throws IOException {
        URL url = new URL("http://localhost:8091/v1/account/changeBalance/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{  \n" +
                "        \"id\": " + randomFromList + ",   \n" +
                "        \"amount\": " + l + "\n" +
                "}  ";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
        }

    }

    private static void getBalance(int randomFromList) throws IOException {
        URLConnection connection =
                new URL("http://localhost:8091/v1/account/getBalance/" + randomFromList).openConnection();
        InputStream is = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        reader.close();
    }

    private static int randomFromList(List<Integer> readIdList) {
        Random rand = new Random();
        return readIdList.get(rand.nextInt(readIdList.size()));
    }
}