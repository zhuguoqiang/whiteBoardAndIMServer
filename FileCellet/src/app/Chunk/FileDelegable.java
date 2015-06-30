package app.Chunk;

public interface FileDelegable {

	public void onSendProgress(FileMessage fileMessage, long processed, long total);

	public void onReceiveProgress(FileMessage fileMessage, long processed, long total);

	public void onReceiveCompleted(FileMessage fileMessage);
}
