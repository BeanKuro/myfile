package Multithread;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Mserver {
    public static void main(String[] args) {
        try {
            AtomicInteger clientCounter = new AtomicInteger(0);
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    int clientNumber = clientCounter.incrementAndGet();
                    System.out.println("New " + clientNumber + "-th client connected");

                    // 클라이언트가 연결될 때마다 새로운 스레드 생성
                    Thread clientThread = new Thread(() -> handleClient(clientSocket, clientNumber));
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, int clientNumber) {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // 파일에 쓰기 위한 FileWriter 및 BufferedWriter 설정
            FileWriter fileWriter = new FileWriter("clientMessages.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // 클라이언트로부터 메시지 받기
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                String logMessage = "Received message from " + clientNumber + "-th client: " + clientMessage;
                System.out.println(logMessage);

                // 파일에 로그 쓰기
                bufferedWriter.write(logMessage);
                bufferedWriter.newLine();

                // 클라이언트에게 응답 보내기 (가장 간단한 형태로 에코)
                String response = "Server received your message: " + clientMessage + "\n"; // 개행 문자 추가
                outputStream.write(response.getBytes());
                outputStream.flush();
            }

            // 파일과 소켓 닫기
            bufferedWriter.close();
            fileWriter.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}