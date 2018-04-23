import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

public class Room {
	
	private DefaultListModel<User> userList;
	private JTextArea textArea;
	
	public Room() {
		userList = new DefaultListModel<User>();
	}

	public DefaultListModel<User> getUserList() {
		return userList;
	}

	public void setUserList(DefaultListModel<User> userList) {
		this.userList = userList;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public void createTextArea() {
		textArea = new JTextArea("");
		textArea.setEditable(false);

		textArea.setPreferredSize(new Dimension(460, 240));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
	}

	public void addMsg(String msg) {
		textArea.append(msg);
	}

	public void addUser(int id) {
		User user = new User(id, "");
		userList.addElement(user);
	}

	public void removeUser(int id) {
		for(Object obj : userList.toArray()) {
			User user = (User) obj;
			if (user.getId() == id) {
				userList.removeElement(user);
			}
		}
	}
	
	
}
