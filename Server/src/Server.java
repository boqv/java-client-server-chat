
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import Message.Message;

public class Server {
	
	private List<SocketThread> socketList;
	
	public List<SocketThread> getSocketList() {
		return socketList;
	}

	public void setSocketList(List<SocketThread> socketList) {
		this.socketList = socketList;
	}

	public Server() {
		socketList = new ArrayList<SocketThread>();
	}
	
	public void removeSocketThread(SocketThread socketThread) {
		socketList.remove(socketThread);
	}
	
	public static void main(String[] args) {

		Server server = new Server();
		server.start();

	}

	private void start() {
		
		int idCounter = 0;

		try (ServerSocket serverSocket = new ServerSocket(4888)) {
			
			boolean listening = true;
			
			while (listening) {
				SocketThread socketThread = new SocketThread(serverSocket.accept(), ++idCounter, this);
				socketList.add(socketThread);
				socketThread.start();
				System.out.println("client " + idCounter + " connected.");
			}
			
			System.out.println("exited");
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	public void broadcast(Message message) {
		
		for(SocketThread socketThread : socketList) {
			socketThread.send(message);
		}
		
	}

}
