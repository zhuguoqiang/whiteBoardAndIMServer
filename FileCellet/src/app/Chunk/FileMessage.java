package app.Chunk;

import java.io.File;

import net.cellcloud.common.LogLevel;
import net.cellcloud.common.Logger;
import net.cellcloud.util.Clock;

import org.json.JSONException;
import org.json.JSONObject;

public class FileMessage {
	private static final long serialVersionUID = -3390710554391293119L;

	private File file;
	private String sender;
	private String receiver;
	
	private long sendTimeStamp;

	private String fileName;
	private long fileLength;
	private long fileLastModified;

	private boolean sending = false;
	protected boolean ready = false;

	public FileMessage(String receiver, String sender, File file) {
		this.receiver = receiver;
		this.sender = sender;
		this.file = file;
	}

	protected void prepare(String name, long length, long lastModified) {
		this.fileName = name;
		this.fileLength = length;
		this.fileLastModified = lastModified;
	}

	public File getFile() {
		return this.file;
	}
	
	public String getReceiver() {
		return this.receiver;
	}
	
	public void setReceiver(String r) {
		this.receiver = r;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public void setSender(String s) {
		this.sender = s;
	}
	
	public void setSendTimestamp(long t) {
		this.sendTimeStamp = t;
	}

	public boolean isSending() {
		return this.sending;
	}

	protected void updateSending() {
		this.sending = true;
		this.setSendTimestamp(Clock.currentTimeMillis());
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			JSONObject file = new JSONObject();
			file.put("name", this.fileName);
			file.put("size", this.fileLength);
			file.put("modified", this.fileLastModified);

			json.put("file", file);
		} catch (JSONException e) {
			Logger.log(this.getClass(), e, LogLevel.WARNING);
		}
		return json;
	}
}
