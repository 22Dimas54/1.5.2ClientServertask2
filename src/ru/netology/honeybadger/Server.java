package ru.netology.honeybadger;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/*
В данной задаче выбрал NonBlocking, так как пользователь может продолжительное время набирать большой
текст и передавать его каналу, а тот складывать данные в буфер, в это время, чтобы не простаивать сервер может заниматься
другими задачами, после обратиться к каналу, чтобы тот проверил если там какие-то данные для обрботки в буфере, если есть
сервер получает, обратывает и передает каналу, который отправляет их в буфер для клиента. В свою очередь клиент может добавлять данные
пока сервер обрабатывает ранее взятую порцию из буфера
 */

public class Server {
    private final static int PORT = 56432;
    private final static String HOST = "netology.homework";

    public static void main(String[] args) {
        try {
            final ServerSocketChannel SOCKET_CHANNEL = ServerSocketChannel.open();
            SOCKET_CHANNEL.bind(new InetSocketAddress(HOST, PORT));
            while (true) {
                try (SocketChannel socketChannel = SOCKET_CHANNEL.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                    while (socketChannel.isConnected()) {
                        int byteCount = socketChannel.read(inputBuffer);
                        if (byteCount == -1) break;
                        final String msg = new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);
                        if (msg.equals("end")) break;
                        inputBuffer.clear();
                        System.out.println("Сообщение пользователя : " + msg);
                        socketChannel.write(ByteBuffer.wrap(("Сообщение пользователя без пробелов: " + msg.replace(" ", "")).getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
