package site.metacoding.chatProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

// jyp= 진용 프로토콜
// 1. 최초메시지는 username으로 체킹
// 2. 구분자
// 3. ALL:메시지
// 4. CHAT:아이디:메시지

public class ServerProgram {

	// 리스너 (연결받기) - 메인스레드
	ServerSocket serverSocket;
	List<ClientSocket> SocketList; // socket를 저장하기 위한 리스트.

	// 서버는 메시지 받아서 보내기 (클라이언트 수마다)

	public ServerProgram() {
		try {
			serverSocket = new ServerSocket(2000); // 포트번호 2000을 가진 서버소켓 생성
			// 연결이 되더라도 다른 사용자와 또 연결되야하므로 while 돌리기
			SocketList = new Vector<>(); // Vector => 동기화가 처리된 ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main 스레드
				// 연결할때마다 socket을 생성해야하므로 전역 변수로 만들수 없음.
				System.out.println("클라이언트 연결됨");
				ClientSocket t = new ClientSocket(socket);
				// 스택이 끝나면 socket이 사라지게 됨 => 새로운 클래스에 socket을 넘김.
				SocketList.add(t); // 리스트에 클래스를 추가 = 리스트에다 클래스에 연결된 socket을 보관
				new Thread(t).start(); // 고객전담스레드에서 모든것이 이루어짐.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 내부 클래스 = 클래스 안에 클래스가 있음.
	class ClientSocket implements Runnable { // socket을 받는 클래스.

		String username; // 유저 이름 설정.
		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true; // 연결이 되있는지 확인.

		public ClientSocket(Socket socket) { // socket을 받아서 본인 socket에 저장
			this.socket = socket;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ALL:머시기 라고 메세지가 오면 동작
		public void chatPublic(String msg) {
			for (ClientSocket t : SocketList) {
				try {
					if (t != this) {
						t.writer.write(username + " : " + msg + "\n");
						t.writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// CHAT:권진용:안녕 라고 메세지가 오면 동작
		public void chatPrivate(String receiver, String msg) {
			for (ClientSocket t : SocketList) {
				try {
					if (t.username.equals(receiver))
						t.writer.write("[귓속말] " + username + " : " + msg + "\n");
					t.writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// 진용 프로토콜 검사기
		// ALL:안녕
		// CHAT:진용:안녕
		public void jyp(String inputData) {
			// 1. 프로토콜 분리
			String[] token = inputData.split(":"); // 받은 메세지를 :를 기준으로 분리해서 token에 순서대로 넣음
			String protocol = token[0]; // 0번지에는 무조건 프로토콜이 옴.
			if (protocol.equals("ALL")) { // 프로토콜이 ALL이면
				String msg = token[1]; // token의 1번지에는 msg가 들어있음.
				chatPublic(msg); // 전체보내기에 msg를 넘김.
			} else if (protocol.equals("CHAT")) { // 프로토콜이 CHAT이면
				String receiver = token[1]; // token의 1번지에는 username이
				String msg = token[2]; // token의 2번지에는 msg가 들어있음.
				chatPrivate(receiver, msg); // 귓속말에 username과,msg를 넘김
			} else { // 프로토콜이 통과 못함.
				System.out.println("프로토콜 없음.");
			}
		}

		@Override
		public void run() {

			// 최초 메세지는 username이다.
			// username만 날라오면 세션이 만들어짐.
			try {
				username = reader.readLine();
				for (ClientSocket c : SocketList) {
					c.writer.write(";" + username);
					c.writer.write("[" + username + "] 님이 입장하였습니다 .\n");
					c.writer.flush();
				}
			} catch (Exception e2) {
				isLogin = false;
				System.out.println("username을 받지 못했습니다.");
			}

			while (isLogin) {
				try { // 연결해제하면 reader가 끊겨서 readLine 부분이 오류가남.
					String inputdata = reader.readLine(); // 받은 메세지를 inpudata 저장
					jyp(inputdata);
				} catch (Exception e) {
					try {
						System.out.println("통신 실패" + e.getMessage());
						isLogin = false; // 어떤 오류가 나든 연결해제. => 반복문 종료 => 하지만 heap에는 떠있음.
						SocketList.remove(this); // 자기 자신을 heap에서 날려줌.
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
		new ServerProgram();
	}
}
