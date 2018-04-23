import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Message.Message;

public class SocketThread extends Thread {

	private Socket socket = null;
	private ObjectOutputStream oos;
	private int id;
	private Server server;

	public SocketThread(Socket socket, int id, Server server) {
		this.socket = socket;
		this.id = id;
		this.server = server;
	}
	
	public int getUserId() {
		return this.id;
	}

	@Override
	public void run() {
		try (
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		) {
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			Message newMessage = null;

			send(new Message("welcome", "Welcome, user: " + id, id));
			
			for(SocketThread socketThread : server.getSocketList()) {
				int uid = socketThread.getUserId();
				if(uid == id) continue;
				System.out.println("sent id: " + uid + "\n");
				send(new Message ("giveId", "", uid));
			}
			
			server.broadcast(new Message("giveId", "", id));

			while (true) {
				
				try {
					newMessage = (Message) ois.readObject();
					
					protocol(newMessage);
					
					System.out.println(newMessage.getContent());
				} catch (EOFException e) {
					break;
				}
				//server.broadcast(inputLine, id);
			}
			
			System.out.println("user: "+ id + " disconnected.");
			server.removeSocketThread(this);
			
			server.broadcast(new Message("userLeft", "", this.id));
			
			
			socket.close();
			oos.close();
			ois.close();
			
			server.removeSocketThread(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void protocol(Message message) {
		
		String type = message.getType();
		
		switch(type) {
			case "msg": {
				server.broadcast(message);
				break;
			}
			
		}
		
	}

	void send(Message message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




}
