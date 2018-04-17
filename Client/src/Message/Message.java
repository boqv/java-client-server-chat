package Message;

import java.io.Serializable;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String content;
	private int id;
	
	public Message(String type, String content, int id) {
		this.type = type;
		this.content = content;
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}
}
