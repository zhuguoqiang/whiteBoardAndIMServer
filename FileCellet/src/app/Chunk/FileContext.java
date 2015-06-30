package app.Chunk;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class FileContext {

	private ConcurrentHashMap<String, Vector<FileMessage>> fileMessages;

	public FileContext() {
		this.fileMessages = new ConcurrentHashMap<String, Vector<FileMessage>>();
	}

	public void addFileMessage(FileMessage fileMessage) {
		String receiver = fileMessage.getReceiver();
		Vector<FileMessage> list = this.fileMessages.get(receiver);
		if (null != list) {
			list.add(fileMessage);
		}
		else {
			list = new Vector<FileMessage>();
			list.add(fileMessage);
			this.fileMessages.put(receiver, list);
		}
	}

	public FileMessage getFileMessage(String receiverName, String fileName) {
		Vector<FileMessage> list = this.fileMessages.get(receiverName);
		if (null != list) {
			for (FileMessage message : list) {
				if (message.getFile().getName().equals(fileName)) {
					return message;
				}
			}
		}

		return null;
	}
}
