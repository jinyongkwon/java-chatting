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

// jyp= ���� ��������
// 1. ���ʸ޽����� username���� üŷ
// 2. ������
// 3. ALL:�޽���
// 4. CHAT:���̵�:�޽���

public class ServerProgram {

	// ������ (����ޱ�) - ���ν�����
	ServerSocket serverSocket;
	List<ClientSocket> SocketList; // socket�� �����ϱ� ���� ����Ʈ.

	// ������ �޽��� �޾Ƽ� ������ (Ŭ���̾�Ʈ ������)

	public ServerProgram() {
		try {
			serverSocket = new ServerSocket(2000); // ��Ʈ��ȣ 2000�� ���� �������� ����
			// ������ �Ǵ��� �ٸ� ����ڿ� �� ����Ǿ��ϹǷ� while ������
			SocketList = new Vector<>(); // Vector => ����ȭ�� ó���� ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main ������
				// �����Ҷ����� socket�� �����ؾ��ϹǷ� ���� ������ ����� ����.
				System.out.println("Ŭ���̾�Ʈ �����");
				ClientSocket t = new ClientSocket(socket);
				// ������ ������ socket�� ������� �� => ���ο� Ŭ������ socket�� �ѱ�.
				SocketList.add(t); // ����Ʈ�� Ŭ������ �߰� = ����Ʈ���� Ŭ������ ����� socket�� ����
				new Thread(t).start(); // �����㽺���忡�� ������ �̷����.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���� Ŭ���� = Ŭ���� �ȿ� Ŭ������ ����.
	class ClientSocket implements Runnable { // socket�� �޴� Ŭ����.

		String username; // ���� �̸� ����.
		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true; // ������ ���ִ��� Ȯ��.

		public ClientSocket(Socket socket) { // socket�� �޾Ƽ� ���� socket�� ����
			this.socket = socket;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ALL:�ӽñ� ��� �޼����� ���� ����
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

		// CHAT:������:�ȳ� ��� �޼����� ���� ����
		public void chatPrivate(String receiver, String msg) {
			for (ClientSocket t : SocketList) {
				try {
					if (t.username.equals(receiver))
						t.writer.write("[�ӼӸ�] " + username + " : " + msg + "\n");
					t.writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// ���� �������� �˻��
		// ALL:�ȳ�
		// CHAT:����:�ȳ�
		public void jyp(String inputData) {
			// 1. �������� �и�
			String[] token = inputData.split(":"); // ���� �޼����� :�� �������� �и��ؼ� token�� ������� ����
			String protocol = token[0]; // 0�������� ������ ���������� ��.
			if (protocol.equals("ALL")) { // ���������� ALL�̸�
				String msg = token[1]; // token�� 1�������� msg�� �������.
				chatPublic(msg); // ��ü�����⿡ msg�� �ѱ�.
			} else if (protocol.equals("CHAT")) { // ���������� CHAT�̸�
				String receiver = token[1]; // token�� 1�������� username��
				String msg = token[2]; // token�� 2�������� msg�� �������.
				chatPrivate(receiver, msg); // �ӼӸ��� username��,msg�� �ѱ�
			} else { // ���������� ��� ����.
				System.out.println("�������� ����.");
			}
		}

		@Override
		public void run() {

			// ���� �޼����� username�̴�.
			// username�� ������� ������ �������.
			try {
				username = reader.readLine();
				for (ClientSocket c : SocketList) {
					c.writer.write(";" + username);
					c.writer.write("[" + username + "] ���� �����Ͽ����ϴ� .\n");
					c.writer.flush();
				}
			} catch (Exception e2) {
				isLogin = false;
				System.out.println("username�� ���� ���߽��ϴ�.");
			}

			while (isLogin) {
				try { // ���������ϸ� reader�� ���ܼ� readLine �κ��� ��������.
					String inputdata = reader.readLine(); // ���� �޼����� inpudata ����
					jyp(inputdata);
				} catch (Exception e) {
					try {
						System.out.println("��� ����" + e.getMessage());
						isLogin = false; // � ������ ���� ��������. => �ݺ��� ���� => ������ heap���� ������.
						SocketList.remove(this); // �ڱ� �ڽ��� heap���� ������.
						reader.close(); // ������ ������ �÷������� ���󰡴� �κ�������
						writer.close(); // ������ �÷����� �ٷιٷ� �����°� �ƴ϶� ���̰� ����
						socket.close(); // ��ſ� �����̶� ���ϸ� ���̱� ���� ������ �÷��� ���� ���� ����.
					} catch (Exception e1) {
						System.out.println("�������� ���μ��� ���� " + e1.getMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new ServerProgram();
	}
}
