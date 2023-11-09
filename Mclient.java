package Multithread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class Mclient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);

            // 서버와의 통신을 위한 입출력 스트림 설정
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();
            int i = 0;
            while (i == 0) {
                // 사용자 입력 받기
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter the two number what you want to calculate.(ex: 12 * 12): ");
                String message = userInput.readLine() + ""; // 개행 문자 추가
                if ("bye".equals(message.trim())) { // "bye"를 입력하면 반복문 탈출
                    i = 1;
                } else {
                    // 서버로 메시지 전송
                    outputStream.write(message.getBytes());
                    outputStream.flush();

                    // 서버에서 받은 응답 출력
                    String res = calc(message);
                    System.out.println("Server respons: " + message + " = " + res);
                }
            }
            // 소켓 닫기(반복문 탈출)
            socket.close();
            reader.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String calc(String exp) { //계산식
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() != 3)
            return "error";
        String res = "";
        float op1 = Float.parseFloat(st.nextToken());
        String opcode = st.nextToken();
        float op2 = Float.parseFloat(st.nextToken());
        switch (opcode) {
            case "+":
                res = Float.toString(op1 + op2);
                break;
            case "-":
                res = Float.toString(op1 - op2);
                break;
            case "*":
                res = Float.toString(op1 * op2);
                break;
            case "/":
                if(op2 == 0){
                    res = "Error: The denominator cannot be zero."; //분모가 0이 될 수 없습니다.
                    break;
                }
                else{
                    res = Float.toString(op1 / op2);
                    break;
                }
            default:
                res = "Error";
        }
        return res;
    }
}