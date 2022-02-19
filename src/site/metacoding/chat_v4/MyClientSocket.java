package site.metacoding.chat_v4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

	String username; // 접속할 이름.

	Socket socket;

	// 데이터를 받아서 읽을 스레드
	BufferedReader reader;

	// 키보드로 부터 받아서 바로 쓸 스레드
	BufferedWriter writer;
	Scanner sc;

	public MyClientSocket() {
		try {
			socket = new Socket("localhost", 2000);
			sc = new Scanner(System.in);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 새로운 스레드 (읽기 전용)
			new Thread(new 읽기전담스레드()).start();

			// 최초 username 전송 프로토콜
			System.out.println("아이디를 입력하세요");
			username = sc.nextLine();
			writer.write(username + "\n");
			writer.flush(); // 버퍼에 담긴 것을 stream으로 흘려보내기
			System.out.println(username + "이 서버로 전송되었습니다.");

			// 메인 스레드 (쓰기 전용)
			while (true) {
				String keyboardInputData = sc.nextLine();
				writer.write(keyboardInputData + "\n");
				writer.flush(); // 버퍼에 담긴 것을 stream으로 흘려보내기
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class 읽기전담스레드 implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					String inputdata = reader.readLine();
					System.out.println(inputdata);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new MyClientSocket();
	}
}
