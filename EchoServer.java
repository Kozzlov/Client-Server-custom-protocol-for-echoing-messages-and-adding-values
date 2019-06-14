import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.util.*;
import java.io.IOException;

public class EchoServer {
	public static int DEFAULT_PORT = 7;

	public static void main(String[] args) {

		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(DEFAULT_PORT);
			ss.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		ByteBuffer buffer = ByteBuffer.allocate(100);
		
		while (true) {
			int selectednum = 0;
			try {
				selectednum = selector.select(); // blocks
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
			if (selectednum > 0) {
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = readyKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					try {
						if (key.isValid() == false) {
							key.cancel();
							key.channel().close();
							continue;
						}

						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel) key.channel();
							SocketChannel client = server.accept();
							System.out.println("Accepted from " + client);
							client.configureBlocking(false);
							SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
						}
						if (key.isReadable()) {
							SocketChannel client = (SocketChannel) key.channel();
							System.out.println("Echo responde" + client.socket().getInetAddress());
							buffer.clear();
							client.read(buffer);
							String request = Protocols.get_response(new String(buffer.array(), "ASCII"));
							if(request!=null) {
								client.write(ByteBuffer.wrap(request.getBytes()));
							}
							client.close();
						}
					} catch (IOException ex) {
						key.cancel();
						try {
							key.channel().close();
						} catch (IOException cex) {
							cex.printStackTrace();
						}
					}
				}
			}
		}
	}
}
