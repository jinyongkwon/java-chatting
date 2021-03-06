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
            socket = new Socket("localhost", 1077); // (IPμ£Όμ, Portλ²νΈ)
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())); // ?¨?(Writer) λ³΄λ΄κΈ?(Out) ?λ¬Έμ OutputStream? ?¬?©

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // ?½κΈ°μ? λ²νΌ ??±.

            // λ©μΈ?? λ°μ? ?λ‘μ΄ ?€? ??? λ©μΈμ§?λ₯? ?½?.
            new Thread(() -> {
                while (true) { // λ©μΈμ§?λ₯? κ³μ λ°μ?Ό ?? while ?¬?©
                    try {
                        String text = reader.readLine(); // text? λ°μ? λ©μΈμ§? ?½?
                        if (text.equals("μ’λ£")) { // μ’λ£λ₯? λ°μΌλ©? λ©μΈμ§? λ°λκ²? μ’λ£
                            break;
                        }
                        System.out.println("\nλ°μ? λ©μμ§? : " + text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // λ©μΈμ§?λ₯? λ°λ³΅?΄? λ³΄λ΄? κ²μ? λ©μΈ ?°? ?λ₯? ?¬?©
            while (true) {
                String text = sc.nextLine();
                writer.write(text + "\n"); // λ²νΌ? ?΄?. // \n?΄ ??΄?Ό ?½????.
                writer.flush(); // ?€ ?΄κΈ°μ? ???Ό? κ°μ  ? ?‘
                if (text.equals("μ’λ£")) // μ’λ£λ₯? ?? ₯?λ©? λ©μΈμ§? λ³΄λ΄κΈ? μ’λ£.
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
