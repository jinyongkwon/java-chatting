package site.metacogin.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Scanner sc;

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public MyClientSocket() {
        try {
            sc = new Scanner(System.in);
            socket = new Socket("localhost", 1077); // (IP주소, Port번호)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())); // 써서(Writer) 보내기(Out) 때문에 OutputStream을 사용

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 읽기위한 버퍼 생성.

            // 메인은 바쁘니 새로운 스레드에서 메세지를 읽음.
            new Thread(() -> {
                while (true) { // 메세지를 계속 받아야 하니 while 사용
                    try {
                        String text = reader.readLine(); // text에 받은 메세지 삽입
                        if (text.equals("종료")) { // 종료를 받으면 메세지 받는것 종료
                            break;
                        }
                        System.out.println("\n받은 메시지 : " + text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메세지를 반복해서 보내는 것은 메인 쓰레드를 사용
            while (true) {
                String text = sc.nextLine();
                writer.write(text + "\n"); // 버퍼에 담음. // \n이 있어야 읽을수있음.
                writer.flush(); // 다 담기지 않았으니 강제 전송
                if (text.equals("종료")) // 종료를 입력하면 메세지 보내기 종료.
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
