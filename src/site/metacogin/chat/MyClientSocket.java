package site.metacogin.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077); // (IP주소, Port번호)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())); // 써서(Writer) 보내기(Out) 때문에 OutputStream을 사용
            writer.write("안녕 \n"); // 버퍼에 담음.
            writer.flush(); // 다 담기지 않았으니 강제 전송
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
