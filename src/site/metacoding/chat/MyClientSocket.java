package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Scanner sc;

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public MyClientSocket() {
        try {
            sc = new Scanner(System.in);
            socket = new Socket("localhost", 1077); // (IPì£¼ì†Œ, Portë²ˆí˜¸)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())); // ?¨?„œ(Writer) ë³´ë‚´ê¸?(Out) ?•Œë¬¸ì— OutputStream?„ ?‚¬?š©

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // ?½ê¸°ìœ„?•œ ë²„í¼ ?ƒ?„±.

            // ë©”ì¸?? ë°”ì˜?‹ˆ ?ƒˆë¡œìš´ ?Š¤? ˆ?“œ?—?„œ ë©”ì„¸ì§?ë¥? ?½?Œ.
            new Thread(() -> {
                while (true) { // ë©”ì„¸ì§?ë¥? ê³„ì† ë°›ì•„?•¼ ?•˜?‹ˆ while ?‚¬?š©
                    try {
                        String text = reader.readLine(); // text?— ë°›ì? ë©”ì„¸ì§? ?‚½?…
                        if (text.equals("ì¢…ë£Œ")) { // ì¢…ë£Œë¥? ë°›ìœ¼ë©? ë©”ì„¸ì§? ë°›ëŠ”ê²? ì¢…ë£Œ
                            break;
                        }
                        System.out.println("\në°›ì? ë©”ì‹œì§? : " + text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // ë©”ì„¸ì§?ë¥? ë°˜ë³µ?•´?„œ ë³´ë‚´?Š” ê²ƒì? ë©”ì¸ ?“°? ˆ?“œë¥? ?‚¬?š©
            while (true) {
                String text = sc.nextLine();
                writer.write(text + "\n"); // ë²„í¼?— ?‹´?Œ. // \n?´ ?ˆ?–´?•¼ ?½?„?ˆ˜?ˆ?Œ.
                writer.flush(); // ?‹¤ ?‹´ê¸°ì? ?•Š?•˜?œ¼?‹ˆ ê°•ì œ ? „?†¡
                if (text.equals("ì¢…ë£Œ")) // ì¢…ë£Œë¥? ?…? ¥?•˜ë©? ë©”ì„¸ì§? ë³´ë‚´ê¸? ì¢…ë£Œ.
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
