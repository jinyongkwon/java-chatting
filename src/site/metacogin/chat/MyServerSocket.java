package site.metacogin.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    // 서버 소켓이 사용자 요청을 받아 연결하고 연결되면 => 끊고 새로운 소켓에 다시 연결
    // 새로운 소켓에서 클라이언트랑 연결하고 통신.
    ServerSocket serverSocket; // 리스너 (연결 => 세션)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    // 추가 (클라이언트에게 메시지 보내기)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {
        try {
            // 1. 서버소켓 생성 (리스너)
            // 잘알려진 포트(Well Known Port)는 사용 x : 0~1023 => 포트가 충돌
            serverSocket = new ServerSocket(1077); // 내부적으로 while이 돈다.
            System.out.println("서버 소켓 생성됨");
            socket = serverSocket.accept(); // while을 돌면서 접속을 대기 => main스레드가 데몬이 됨
            // 접속이 되면 accept()에서 랜덤포트로 새로운 소켓을 만듬
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())); // 가져와서(In) 읽기(Reader) 때문에 InputStream을 사용

            // 메세지 보내기
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 메세지 를 보내기 위해 Writer를 만듬.
            sc = new Scanner(System.in);

            // 메인 쓰레드는 메세지를 반복해서 받고 있으므로 메인은 바쁨
            // 메세지를 보내기 위해 새로운 쓰레드 사용
            new Thread(() -> { // 화살표함수는 class 공간이 다르므로 try-catch를 따로 걸어야함
                while (true) {
                    try {
                        String inputdata = sc.nextLine(); // 글 입력
                        writer.write(inputdata + "\n"); // 버퍼에 담고
                        writer.flush(); // 버퍼 전송.
                        if (inputdata.equals("종료"))
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메세지 반복해서 받는 서버 소켓 - 메인 쓰레드
            while (true) {
                String inputData = reader.readLine(); // 가져온걸 읽음. 내용이 길면 while을 써서 읽어야함
                if (inputData.equals("종료")) { // 종료를 입력하면 메세지 보내기 종료.
                    break;
                }
                System.out.println("\n받은 메시지 : " + inputData);
            }
        } catch (Exception e) {
            System.out.println("통신 오류 발생 : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인 종료");
    }
}
