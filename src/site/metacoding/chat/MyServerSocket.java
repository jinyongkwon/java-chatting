package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    // ?���? ?��켓이 ?��?��?�� ?���??�� 받아 ?��결하�? ?��결되�? => ?���? ?��로운 ?��켓에 ?��?�� ?���?
    // ?��로운 ?��켓에?�� ?��?��?��?��?��?�� ?��결하�? ?��?��.
    ServerSocket serverSocket; // 리스?�� (?���? => ?��?��)
    Socket socket; // 메시�? ?��?��
    BufferedReader reader;

    // 추�? (?��?��?��?��?��?���? 메시�? 보내�?)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {
        try {
            // 1. ?��버소�? ?��?�� (리스?��)
            // ?��?��?���? ?��?��(Well Known Port)?�� ?��?�� x : 0~1023 => ?��?���? 충돌
            serverSocket = new ServerSocket(1077); // ?���??��?���? while?�� ?��?��.
            System.out.println("?���? ?���? ?��?��?��");
            socket = serverSocket.accept(); // while?�� ?��면서 ?��?��?�� ??�? => main?��?��?���? ?��몬이 ?��
            // ?��?��?�� ?���? accept()?��?�� ?��?��?��?���? ?��로운 ?��켓을 만듬
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())); // �??��???��(In) ?���?(Reader) ?��문에 InputStream?�� ?��?��

            // 메세�? 보내�?
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 메세�? �? 보내�? ?��?�� Writer�? 만듬.
            sc = new Scanner(System.in);

            // 메인 ?��?��?��?�� 메세�?�? 반복?��?�� 받고 ?��?���?�? 메인?? 바쁨
            // 메세�?�? 보내�? ?��?�� ?��로운 ?��?��?�� ?��?��
            new Thread(() -> { // ?��?��?��?��?��?�� class 공간?�� ?��르�?�? try-catch�? ?���? 걸어?��?��
                while (true) {
                    try {
                        String inputdata = sc.nextLine(); // �? ?��?��
                        writer.write(inputdata + "\n"); // 버퍼?�� ?���?
                        writer.flush(); // 버퍼 ?��?��.
                        if (inputdata.equals("종료"))
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메세�? 반복?��?�� 받는 ?���? ?���? - 메인 ?��?��?��
            while (true) {
                String inputData = reader.readLine(); // �??��?���? ?��?��. ?��?��?�� 길면 while?�� ?��?�� ?��?��?��?��
                if (inputData.equals("종료")) { // 종료�? ?��?��?���? 메세�? 보내�? 종료.
                    break;
                }
                System.out.println("\n받�? 메시�? : " + inputData);
            }
        } catch (Exception e) {
            System.out.println("?��?�� ?���? 발생 : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인 종료");
    }
}
