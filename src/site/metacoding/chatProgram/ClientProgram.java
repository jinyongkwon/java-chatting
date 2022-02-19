package site.metacoding.chatProgram;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientProgram extends JFrame {

	boolean session;

	JPanel panel, southpanel;
	JTextField text;
	JTextArea msgList;
	JScrollPane scrollPane;
	JButton btnConnect, btnSend;

	String username, sendMsg, receiveMsg;
	String ip;

	Socket socket;
	BufferedReader reader;
	BufferedWriter writer;
	Scanner sc;

	public ClientProgram() {
		GUI();

		Client();
		GUIListener();
	}

	public void GUI() {
		setSize(600, 600); // w,h
		setLocationRelativeTo(null); // ������ ȭ�� �߾� ��ġ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x��ư Ŭ���� main ����

		panel = (JPanel) getContentPane();
		southpanel = new JPanel();
		text = new JTextField(30);
		msgList = new JTextArea(5, 20);
		scrollPane = new JScrollPane(msgList);
		southpanel.setLayout(new FlowLayout());

		panel.add(scrollPane);

		btnSend = new JButton("������");
		southpanel.add(text);
		southpanel.add(btnSend);
		panel.add(southpanel, BorderLayout.SOUTH);

		ip = JOptionPane.showInputDialog("������ ip�� �Է��ϼ���.");
		username = JOptionPane.showInputDialog("�г����� �Է��ϼ���.");

		setVisible(true); // �׸� �׸���
	}

	public void GUIListener() {

//		btnSend.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					sendMsg = text.getText();
//					msgList.append(sendMsg + "\n");
//					text.setText("");
//					text.requestFocus();
//				}
//			}
//		});

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendMsg = text.getText();
					if (!sendMsg.equals("")) {
						writer.write("ALL:" + sendMsg + "\n");
						writer.flush();
					}
					text.setText("");
					text.requestFocus();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// ���ۿ� ��� ���� stream���� ���������
			}
		});
	}

	public void Client() {
		try {
			socket = new Socket(ip, 2000);
			sc = new Scanner(System.in);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// ���ο� ������ (�б� ����)
			new Thread(() -> {
				try {
					while (true) {
						receiveMsg = reader.readLine();
						msgList.append(receiveMsg + "\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

			// ���� username ���� ��������
			writer.write(username + "\n");
			writer.flush(); // ���ۿ� ��� ���� stream���� ���������

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientProgram();
	}
}