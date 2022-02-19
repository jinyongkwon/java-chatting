package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    // ?„œë²? ?†Œì¼“ì´ ?‚¬?š©? ?š”ì²??„ ë°›ì•„ ?—°ê²°í•˜ê³? ?—°ê²°ë˜ë©? => ?Šê³? ?ƒˆë¡œìš´ ?†Œì¼“ì— ?‹¤?‹œ ?—°ê²?
    // ?ƒˆë¡œìš´ ?†Œì¼“ì—?„œ ?´?¼?´?–¸?Š¸?‘ ?—°ê²°í•˜ê³? ?†µ?‹ .
    ServerSocket serverSocket; // ë¦¬ìŠ¤?„ˆ (?—°ê²? => ?„¸?…˜)
    Socket socket; // ë©”ì‹œì§? ?†µ?‹ 
    BufferedReader reader;

    // ì¶”ê? (?´?¼?´?–¸?Š¸?—ê²? ë©”ì‹œì§? ë³´ë‚´ê¸?)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {
        try {
            // 1. ?„œë²„ì†Œì¼? ?ƒ?„± (ë¦¬ìŠ¤?„ˆ)
            // ?˜?•Œ? ¤ì§? ?¬?Š¸(Well Known Port)?Š” ?‚¬?š© x : 0~1023 => ?¬?Š¸ê°? ì¶©ëŒ
            serverSocket = new ServerSocket(1077); // ?‚´ë¶?? ?œ¼ë¡? while?´ ?ˆ?‹¤.
            System.out.println("?„œë²? ?†Œì¼? ?ƒ?„±?¨");
            socket = serverSocket.accept(); // while?„ ?Œë©´ì„œ ? ‘?†?„ ??ê¸? => main?Š¤? ˆ?“œê°? ?°ëª¬ì´ ?¨
            // ? ‘?†?´ ?˜ë©? accept()?—?„œ ?œ?¤?¬?Š¸ë¡? ?ƒˆë¡œìš´ ?†Œì¼“ì„ ë§Œë“¬
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())); // ê°?? ¸???„œ(In) ?½ê¸?(Reader) ?•Œë¬¸ì— InputStream?„ ?‚¬?š©

            // ë©”ì„¸ì§? ë³´ë‚´ê¸?
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // ë©”ì„¸ì§? ë¥? ë³´ë‚´ê¸? ?œ„?•´ Writerë¥? ë§Œë“¬.
            sc = new Scanner(System.in);

            // ë©”ì¸ ?“°? ˆ?“œ?Š” ë©”ì„¸ì§?ë¥? ë°˜ë³µ?•´?„œ ë°›ê³  ?ˆ?œ¼ë¯?ë¡? ë©”ì¸?? ë°”ì¨
            // ë©”ì„¸ì§?ë¥? ë³´ë‚´ê¸? ?œ„?•´ ?ƒˆë¡œìš´ ?“°? ˆ?“œ ?‚¬?š©
            new Thread(() -> { // ?™”?‚´?‘œ?•¨?ˆ˜?Š” class ê³µê°„?´ ?‹¤ë¥´ë?ë¡? try-catchë¥? ?”°ë¡? ê±¸ì–´?•¼?•¨
                while (true) {
                    try {
                        String inputdata = sc.nextLine(); // ê¸? ?…? ¥
                        writer.write(inputdata + "\n"); // ë²„í¼?— ?‹´ê³?
                        writer.flush(); // ë²„í¼ ? „?†¡.
                        if (inputdata.equals("ì¢…ë£Œ"))
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // ë©”ì„¸ì§? ë°˜ë³µ?•´?„œ ë°›ëŠ” ?„œë²? ?†Œì¼? - ë©”ì¸ ?“°? ˆ?“œ
            while (true) {
                String inputData = reader.readLine(); // ê°?? ¸?˜¨ê±? ?½?Œ. ?‚´?š©?´ ê¸¸ë©´ while?„ ?¨?„œ ?½?–´?•¼?•¨
                if (inputData.equals("ì¢…ë£Œ")) { // ì¢…ë£Œë¥? ?…? ¥?•˜ë©? ë©”ì„¸ì§? ë³´ë‚´ê¸? ì¢…ë£Œ.
                    break;
                }
                System.out.println("\në°›ì? ë©”ì‹œì§? : " + inputData);
            }
        } catch (Exception e) {
            System.out.println("?†µ?‹  ?˜¤ë¥? ë°œìƒ : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("ë©”ì¸ ì¢…ë£Œ");
    }
}
