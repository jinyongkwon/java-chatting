package site.metacoding.chatProgram;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
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
	JList userList;
	DefaultListModel<String> userListModel;

	String username, sendMsg, receiveMsg;
	String ip;

	Socket socket;
	BufferedReader reader;
	BufferedWriter writer;

	public ClientProgram() {
		GUI();

		Client();
		GUIListener();
	}

	public void GUI() {
		setSize(600, 600); // w,h
		setLocationRelativeTo(null); // 프레임 화면 중앙 배치
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼 클릭시 main 종료

		panel = (JPanel) getContentPane();
		southpanel = new JPanel();
		text = new JTextField(30);
		msgList = new JTextArea(5, 20);
		scrollPane = new JScrollPane(msgList);

		southpanel.setLayout(new FlowLayout());
		userListModel = new DefaultListModel<String>();
		userList = new JList<>(userListModel);

		panel.add(scrollPane);
		panel.add(userList, BorderLayout.EAST);

		btnSend = new JButton("보내기");
		southpanel.add(text);
		southpanel.add(btnSend);
		panel.add(southpanel, BorderLayout.SOUTH);

		ip = JOptionPane.showInputDialog("연결할 ip를 입력하세요.");
		username = JOptionPane.showInputDialog("닉네임을 입력하세요.");

		setVisible(true); // 그림 그리기
	}

	public void GUIListener() {

		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
				}
			}
		});

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
				// 버퍼에 담긴 것을 stream으로 흘려보내기
			}
		});
	}

	public void Client() {
		try {
			socket = new Socket(ip, 2000);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 최초 username 전송 프로토콜
			writer.write(username + "\n");
			writer.flush(); // 버퍼에 담긴 것을 stream으로 흘려보내기

			// 새로운 스레드 (읽기 전용)
			new Thread(() -> {
				try {
					while (true) {
						receiveMsg = reader.readLine();
						msgList.append(receiveMsg + "\n");
						msgList.setCaretPosition(msgList.getDocument().getLength());
						if (receiveMsg.charAt(0) == '[') {
							String[] token = receiveMsg.split("]");
							String name = token[0].substring(1, token.length - 1);
							userListModel.addElement(name);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientProgram();
	}
}
