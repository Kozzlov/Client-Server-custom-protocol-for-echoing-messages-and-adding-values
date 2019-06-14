import java.net.*;
import java.nio.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;


public class EchoClient
{
	
	EchoClient(){}

    public static void main(String [] args)
    {	        
        try{
        	String msg = "MSG;    string   ";
        	SocketChannel socket = SocketChannel.open();
        	socket.configureBlocking(false);
        	socket.connect(new InetSocketAddress("localhost",7));
        	ByteBuffer bb = ByteBuffer.allocate(100);
        	while(true) {
        		if(socket.finishConnect()) {
        			socket.write(ByteBuffer.wrap(msg.getBytes()));
        			try {
    					Thread.sleep(1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
                    System.out.println("Echo responde " + msg);
                    //read data
                    socket.read(bb);
                    String receive = new String(bb.array(), "ASCII");                   
                    System.out.println(receive);
                    bb.clear();
                    break;
        		}
        	}
        	/*
        	//OutputStream out = socket.getOutputStream();
        	//InputStream in = socket.getInputStream();
            Thread.sleep(1000);
            out.write(msg.getBytes("US-ASCII"));
            String temp = "";
            int c;
            
        do {
        	   c = in.read();
        	   temp+=(char)c;
           }while (in.available() > 0);
           */
           //socket.shutdownInput();
           //socket.shutdownOutput();
           //socket.close();
        }
        catch (IOException ex) {
        	ex.printStackTrace();
        }
        catch (RuntimeException ex1) {
        	ex1.printStackTrace();
        }
    }
}