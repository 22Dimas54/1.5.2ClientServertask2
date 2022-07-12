package ru.netology.honeybadger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private final static int PORT = 56432;
    private final static String HOST = "netology.homework";

    public static void main(String[] args) {
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        try (final SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(socketAddress);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while (true) {
                System.out.println("(end-exit)Введите строки с пробелами и сервер вернет их без пробелов:");
                msg = scanner.nextLine();
                if (msg.equals("end")) break;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(2000);
                int byreCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, byreCount, StandardCharsets.UTF_8));
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
