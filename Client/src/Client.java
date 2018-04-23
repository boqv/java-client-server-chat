import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Message.Message;

public class Client {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private boolean connected = false;

	private int id;

	private Room publicRoom;
	// private JTextArea textArea;
	private JList<User> userListView;
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	// private DefaultListModel<User> userList;

	public static void main(String[] args) {

		Client client = new Client();
		client.start();

	}

	public void sendMessage(Message message) {

		Object object = (Message) message;
		try {
			oos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {

		// userList = new DefaultListModel<User>();
		publicRoom = new Room();

		JFrame frame = new JFrame();

		JTextField textField = new JTextField("", 16);

		JButton button = new JButton("Send");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String text = textField.getText();
				if (text.trim().isEmpty())
					return;

				Message message = new Message("msg", text, id);
				sendMessage(message);

				textField.setText("");

			}

		});

		publicRoom.createTextArea();

		userListView = new JList<User>(publicRoom.getUserList());
		userListView.setLayoutOrientation(JList.VERTICAL);
		userListView.setPreferredSize(new Dimension(120, 240));

		String[] test = { "What", "is", "this" };
		JComboBox comboBox = new JComboBox(test);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(640, 320));
		panel.add(userListView);
		panel.add(publicRoom.getTextArea());
		panel.add(textField);
		panel.add(button);
		panel.add(comboBox);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.add(panel);
		frame.pack();

		frame.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				System.out.println("Window closed.");
				connected = false;

			}

		});
		frame.setVisible(true);

		try {
			socket = new Socket("localhost", 4888);

			oos = new ObjectOutputStream(socket.getOutputStream());

			connected = true;
			System.out.println("we are in.");

			ListenThread listen = new ListenThread(this, socket);
			listen.start();

			while (connected) {

			}

			listen.setRunning(false);

			System.out.println("we are out");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				System.out.println("closing socket");
				socket.close();

				if (oos != null)
					oos.close();
				if (ois != null)
					ois.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}

	}

	public void receivedMessage(Message message) {
		
		String content = message.getContent();
		String type = message.getType();
		int id = message.getId();
		LocalTime now = LocalTime.now();
		
		switch(type) {
		
			case "welcome": {
				this.id = id;
				publicRoom.addMsg("("+ now.format(timeFormatter) + ") Server: " + content + "\n");
				break;
			}
			case "msg": {
				publicRoom.addMsg("("+ now.format(timeFormatter) + ") ID "+ id + ": "+ content + "\n");
				break;
			}
			case "giveId" : {
				publicRoom.addUser(id);
				break;
			}
			case "userLeft" : {
				publicRoom.removeUser(id);
				break;
			}
		}
		
	}

}
