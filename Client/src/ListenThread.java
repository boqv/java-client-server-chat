import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import Message.Message;

public class ListenThread extends Thread {
	
	private ObjectInputStream ois;
	private Client client;
	private boolean running;
	
	public ListenThread(Client client, Socket socket) {
		
		
		this.client = client;
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			running = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	@Override
	public void run() {
		
		while(running) {
			Message message;
			try {
				message = (Message) ois.readObject();
				client.receivedMessage(message);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (SocketException e) {
				running = false;
				
			} catch (IOException e) {
				running = false;
				e.printStackTrace();
			}
			
		}
	}

}
