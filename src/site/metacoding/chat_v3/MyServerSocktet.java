package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class MyServerSocktet {

	// 리스너 (연결받기) - 메인스레드
	ServerSocket serverSocket;
	List<고객전담스레드> 고객리스트; // socket를 저장하기 위한 리스트.

	// 서버는 메시지 받아서 보내기 (클라이언트 수마다)

	public MyServerSocktet() {
		try {
			serverSocket = new ServerSocket(2000); // 포트번호 2000을 가진 서버소켓 생성
			// 연결이 되더라도 다른 사용자와 또 연결되야하므로 while 돌리기
			고객리스트 = new Vector<>(); // Vector => 동기화가 처리된 ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main 스레드
				// 연결할때마다 socket을 생성해야하므로 전역 변수로 만들수 없음.
				System.out.println("클라이언트 연결됨");
				고객전담스레드 t = new 고객전담스레드(socket);
				// 스택이 끝나면 socket이 사라지게 됨 => 새로운 클래스에 socket을 넘김.
				고객리스트.add(t); // 리스트에 클래스를 추가 = 리스트에다 클래스에 연결된 socket을 보관
				new Thread(t).start(); // 고객전담스레드에서 모든것이 이루어짐.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 내부 클래스 = 클래스 안에 클래스가 있음.
	class 고객전담스레드 implements Runnable { // socket을 받는 클래스.

		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true; // 연결이 되있는지 확인.

		public 고객전담스레드(Socket socket) { // socket을 받아서 본인 socket에 저장
			this.socket = socket;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (isLogin) {
				try { // 연결해제하면 reader가 끊겨서 readLine 부분이 오류가남.
					String inputdata = reader.readLine(); // 받은 메세지를 inpudata 저장
					System.out.println("from 클라이언트 : " + inputdata);

					// 메세지 받았으니까 List<고객전담스레드> 고객리스트 <== 여기에 담긴
					// 모든 클라이언트에게 메시지 전송 (for문 돌려서!!)
					for (고객전담스레드 t : 고객리스트) { // 왼쪽 : 컬렉션 타입, 오른쪽 : 컬렉션
						// for문을 시작할때마다 고객리스트에있는 리스트를 순서대로 고객전담스레드타입인 t에 넣는다.
						// 즉 시작할때마다 오른쪽에 있는 리스트의 값을 순서대로 왼쪽타입의 변수의 값을 넣음.
						// 리스트에 들어있는 타입과 왼쪽의 타입이 일치하지않으면 실행 불가.
						if (t != this) { // 나를 제외한 나머지에게 메세지를 보냄
							t.writer.write(inputdata + "\n");
							t.writer.flush();
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("클라이언트 연결 종료");
						isLogin = false; // 어떤 오류가 나든 연결해제. => 반복문 종료 => 하지만 heap에는 떠있음.
						고객리스트.remove(this); // 자기 자신을 heap에서 날려줌.
						reader.close(); // 어차피 가비지 컬렉션으로 날라가는 부분이지만
						writer.close(); // 가바지 컬렉션은 바로바로 버리는게 아니라 쌓이고 버림
						socket.close(); // 통신에 조금이라도 부하를 줄이기 위해 가비지 컬렉션 전에 먼저 날림.
					} catch (Exception e1) {
						System.out.println("연결해제 프로세스 실패 " + e1.getMessage());
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		new MyServerSocktet();
	}
}
