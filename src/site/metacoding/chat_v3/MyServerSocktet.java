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

	// ������ (����ޱ�) - ���ν�����
	ServerSocket serverSocket;
	List<�����㽺����> ������Ʈ; // socket�� �����ϱ� ���� ����Ʈ.

	// ������ �޽��� �޾Ƽ� ������ (Ŭ���̾�Ʈ ������)

	public MyServerSocktet() {
		try {
			serverSocket = new ServerSocket(2000); // ��Ʈ��ȣ 2000�� ���� �������� ����
			// ������ �Ǵ��� �ٸ� ����ڿ� �� ����Ǿ��ϹǷ� while ������
			������Ʈ = new Vector<>(); // Vector => ����ȭ�� ó���� ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main ������
				// �����Ҷ����� socket�� �����ؾ��ϹǷ� ���� ������ ����� ����.
				System.out.println("Ŭ���̾�Ʈ �����");
				�����㽺���� t = new �����㽺����(socket);
				// ������ ������ socket�� ������� �� => ���ο� Ŭ������ socket�� �ѱ�.
				������Ʈ.add(t); // ����Ʈ�� Ŭ������ �߰� = ����Ʈ���� Ŭ������ ����� socket�� ����
				new Thread(t).start(); // �����㽺���忡�� ������ �̷����.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���� Ŭ���� = Ŭ���� �ȿ� Ŭ������ ����.
	class �����㽺���� implements Runnable { // socket�� �޴� Ŭ����.

		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true; // ������ ���ִ��� Ȯ��.

		public �����㽺����(Socket socket) { // socket�� �޾Ƽ� ���� socket�� ����
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
				try { // ���������ϸ� reader�� ���ܼ� readLine �κ��� ��������.
					String inputdata = reader.readLine(); // ���� �޼����� inpudata ����
					System.out.println("from Ŭ���̾�Ʈ : " + inputdata);

					// �޼��� �޾����ϱ� List<�����㽺����> ������Ʈ <== ���⿡ ���
					// ��� Ŭ���̾�Ʈ���� �޽��� ���� (for�� ������!!)
					for (�����㽺���� t : ������Ʈ) { // ���� : �÷��� Ÿ��, ������ : �÷���
						// for���� �����Ҷ����� ������Ʈ���ִ� ����Ʈ�� ������� �����㽺����Ÿ���� t�� �ִ´�.
						// �� �����Ҷ����� �����ʿ� �ִ� ����Ʈ�� ���� ������� ����Ÿ���� ������ ���� ����.
						// ����Ʈ�� ����ִ� Ÿ�԰� ������ Ÿ���� ��ġ���������� ���� �Ұ�.
						if (t != this) { // ���� ������ ���������� �޼����� ����
							t.writer.write(inputdata + "\n");
							t.writer.flush();
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("Ŭ���̾�Ʈ ���� ����");
						isLogin = false; // � ������ ���� ��������. => �ݺ��� ���� => ������ heap���� ������.
						������Ʈ.remove(this); // �ڱ� �ڽ��� heap���� ������.
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
		new MyServerSocktet();
	}
}
