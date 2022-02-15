package site.metacogin.chat_v2;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServerSocktet {

    // 리스너 (연결받기) - 메인스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;

    // 서버는 메시지 받아서 보내기 (클라이언트 수마다)

    public MyServerSocktet() {
        try {
            serverSocket = new ServerSocket(2000); // 포트번호 2000을 가진 서버소켓 생성
            // 연결이 되더라도 다른 사용자와 또 연결되야하므로 while 돌리기
            고객리스트 = new ArrayList<>();
            while (true) {
                Socket socket = serverSocket.accept(); // main 스레드
                // 연결할때마다 socket을 생성해야하므로 전역 변수로 만들수 없음.
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket);
                // 스택이 끝나면 socket이 사라지게 됨 => 새로운 클래스에 socket을 넘김.
                고객리스트.add(t); // 리스트에 클래스를 추가 = 리스트에다 클래스에 연결된 socket을 보관
                new Thread(t).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스 = 클래스 안에 클래스가 있음.
    class 고객전담스레드 implements Runnable {

        Socket socket;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

        }

    }

    public static void main(String[] args) {
        new MyServerSocktet();
    }
}
