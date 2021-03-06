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

    // ?λ²? ?μΌμ΄ ?¬?©? ?μ²?? λ°μ ?°κ²°νκ³? ?°κ²°λλ©? => ?κ³? ?λ‘μ΄ ?μΌμ ?€? ?°κ²?
    // ?λ‘μ΄ ?μΌμ? ?΄?Ό?΄?Έ?Έ? ?°κ²°νκ³? ?΅? .
    ServerSocket serverSocket; // λ¦¬μ€? (?°κ²? => ?Έ?)
    Socket socket; // λ©μμ§? ?΅? 
    BufferedReader reader;

    // μΆκ? (?΄?Ό?΄?Έ?Έ?κ²? λ©μμ§? λ³΄λ΄κΈ?)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {
        try {
            // 1. ?λ²μμΌ? ??± (λ¦¬μ€?)
            // ??? €μ§? ?¬?Έ(Well Known Port)? ?¬?© x : 0~1023 => ?¬?Έκ°? μΆ©λ
            serverSocket = new ServerSocket(1077); // ?΄λΆ?? ?Όλ‘? while?΄ ??€.
            System.out.println("?λ²? ?μΌ? ??±?¨");
            socket = serverSocket.accept(); // while? ?λ©΄μ ? ?? ??κΈ? => main?€? ?κ°? ?°λͺ¬μ΄ ?¨
            // ? ??΄ ?λ©? accept()?? ??€?¬?Έλ‘? ?λ‘μ΄ ?μΌμ λ§λ¬
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())); // κ°?? Έ???(In) ?½κΈ?(Reader) ?λ¬Έμ InputStream? ?¬?©

            // λ©μΈμ§? λ³΄λ΄κΈ?
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // λ©μΈμ§? λ₯? λ³΄λ΄κΈ? ??΄ Writerλ₯? λ§λ¬.
            sc = new Scanner(System.in);

            // λ©μΈ ?°? ?? λ©μΈμ§?λ₯? λ°λ³΅?΄? λ°κ³  ??Όλ―?λ‘? λ©μΈ?? λ°μ¨
            // λ©μΈμ§?λ₯? λ³΄λ΄κΈ? ??΄ ?λ‘μ΄ ?°? ? ?¬?©
            new Thread(() -> { // ??΄??¨?? class κ³΅κ°?΄ ?€λ₯΄λ?λ‘? try-catchλ₯? ?°λ‘? κ±Έμ΄?Ό?¨
                while (true) {
                    try {
                        String inputdata = sc.nextLine(); // κΈ? ?? ₯
                        writer.write(inputdata + "\n"); // λ²νΌ? ?΄κ³?
                        writer.flush(); // λ²νΌ ? ?‘.
                        if (inputdata.equals("μ’λ£"))
                            break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // λ©μΈμ§? λ°λ³΅?΄? λ°λ ?λ²? ?μΌ? - λ©μΈ ?°? ?
            while (true) {
                String inputData = reader.readLine(); // κ°?? Έ?¨κ±? ?½?. ?΄?©?΄ κΈΈλ©΄ while? ?¨? ?½?΄?Ό?¨
                if (inputData.equals("μ’λ£")) { // μ’λ£λ₯? ?? ₯?λ©? λ©μΈμ§? λ³΄λ΄κΈ? μ’λ£.
                    break;
                }
                System.out.println("\nλ°μ? λ©μμ§? : " + inputData);
            }
        } catch (Exception e) {
            System.out.println("?΅?  ?€λ₯? λ°μ : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("λ©μΈ μ’λ£");
    }
}
