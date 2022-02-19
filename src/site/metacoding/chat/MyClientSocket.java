package site.metacoding.chat;

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
                    new OutputStreamWriter(socket.getOutputStream())); // ?��?��(Writer) 보내�?(Out) ?��문에 OutputStream?�� ?��?��

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // ?��기위?�� 버퍼 ?��?��.

            // 메인?? 바쁘?�� ?��로운 ?��?��?��?��?�� 메세�?�? ?��?��.
            new Thread(() -> {
                while (true) { // 메세�?�? 계속 받아?�� ?��?�� while ?��?��
                    try {
                        String text = reader.readLine(); // text?�� 받�? 메세�? ?��?��
                        if (text.equals("종료")) { // 종료�? 받으�? 메세�? 받는�? 종료
                            break;
                        }
                        System.out.println("\n받�? 메시�? : " + text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메세�?�? 반복?��?�� 보내?�� 것�? 메인 ?��?��?���? ?��?��
            while (true) {
                String text = sc.nextLine();
                writer.write(text + "\n"); // 버퍼?�� ?��?��. // \n?�� ?��?��?�� ?��?��?��?��?��.
                writer.flush(); // ?�� ?��기�? ?��?��?��?�� 강제 ?��?��
                if (text.equals("종료")) // 종료�? ?��?��?���? 메세�? 보내�? 종료.
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
