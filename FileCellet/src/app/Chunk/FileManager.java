package app.Chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import net.cellcloud.common.LogLevel;
import net.cellcloud.common.Logger;
import net.cellcloud.core.Cellet;
import net.cellcloud.talk.dialect.ChunkDialect;
import net.cellcloud.talk.dialect.ChunkListener;

public class FileManager implements ChunkListener, FileDelegable {

	private final static FileManager instance = new FileManager();

	private String receivePath;

	private LinkedList<String> subPathCache;

	// 绝对路劲关系映射
	private ConcurrentHashMap<String, FileContext> senderContextMap;

	private ConcurrentHashMap<String, File> fileMap;
	private FileDelegable delegate;

	private FileManager() {
		this.receivePath = System.getProperty("user.dir") + "/local/receive/";

		File file = new File(this.receivePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		this.senderContextMap = new ConcurrentHashMap<String, FileContext>();

		this.subPathCache = new LinkedList<String>();
		this.fileMap = new ConcurrentHashMap<String, File>();
	}

	public static FileManager getInstance() {
		return FileManager.instance;
	}

	public void setDelegate(FileDelegable delegate) {
		this.delegate = delegate;
	}

	public String getReceivePath() {
		return this.receivePath;
	}

	public synchronized String checkAndGetReceivePath(String subPath) {
		String path = this.receivePath + subPath + "/";

		if (this.subPathCache.contains(path)) {
			return path;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		this.subPathCache.add(path);

		return path;
	}

	public void offerDetectedFile(FileMessage fileMessage) {
		synchronized (this.senderContextMap) {
			String sender = fileMessage.getSender();
			FileContext ctx = this.senderContextMap.get(sender);
			if (null == ctx) {
				ctx = new FileContext();
				ctx.addFileMessage(fileMessage);
				this.senderContextMap.put(sender, ctx);
			}
			else {
				ctx.addFileMessage(fileMessage);
			}
		}
	}

	public void remove(String fileName, String senderName, String receiverName) {
		
	}

	public boolean send(Cellet cellet, String tag, String fileName, String senderName, String receiverName) {
		String targetTag = tag.toString();

		FileContext ctx = this.senderContextMap.get(senderName);
		if (null == ctx) {
			return false;
		}

		FileMessage fileMessage = ctx.getFileMessage(receiverName, fileName);
		if (null == fileMessage) {
			return false;
		}

		// 更新消息状态
		fileMessage.updateSending();

		File file = fileMessage.getFile();
		long fileLength = file.length();

		int chunkNum = (fileLength <= ChunkDialect.CHUNK_SIZE) ?
						1 : (int)Math.floor(fileLength / ChunkDialect.CHUNK_SIZE);
		if (fileLength > ChunkDialect.CHUNK_SIZE
			&& fileLength % ChunkDialect.CHUNK_SIZE != 0) {
			chunkNum += 1;
		}

		// TODO 断点续传

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[ChunkDialect.CHUNK_SIZE];
			int len = 0;
			int chunkIndex = 0;
			while ((len = fis.read(buf)) > 0) {
				int cIndex = chunkIndex;
				ChunkDialect chunk = new ChunkDialect(senderName, fileName, fileLength, cIndex, chunkNum, buf, len);
				chunk.setListener(this);

				// 更新索引
				++chunkIndex;

				// 发送 Chunk
				cellet.talk(targetTag, chunk);
			}
		} catch (FileNotFoundException e) {
			Logger.log(FileManager.class, e, LogLevel.ERROR);
			return false;
		} catch (IOException e) {
			Logger.log(FileManager.class, e, LogLevel.ERROR);
			return false;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					Logger.log(this.getClass(), e, LogLevel.WARNING);
				}
			}
		}

		return true;
	}

	public synchronized void receiveChunk(String senderName, String receiverName, ChunkDialect chunkDialect) {

		 String filePath = this.checkAndGetReceivePath(senderName);
		String fileName = chunkDialect.getSign();
		String fullPath = filePath + fileName;

		File file = this.fileMap.get(fullPath);
		if (null == file) {
			file = new File(filePath, fileName);
			this.fileMap.put(fullPath, file);
		}

		long processed = 0;
		long total = chunkDialect.getTotalLength();
		int index = chunkDialect.getChunkIndex();
		if (index + 1 == chunkDialect.getChunkNum()) {
			processed = total;
		}
		else {
			processed = (index + 1) * ChunkDialect.CHUNK_SIZE;
		}

		//监控接收进度
//		if (null != this.delegate) {
//			FileContext ctx = this.senderContextMap.get(senderName);
//			if (null != ctx) {
//				FileMessage fileMessage = ctx.getFileMessage(receiverName, fileName);
//				this.delegate.onReceiveProgress(fileMessage, processed, total);
//			}
//		}

		if (chunkDialect.hasCompleted()) {
			if (Logger.isDebugLevel()) {
				Logger.d(FileManager.class, "File '" + chunkDialect.getSign() + "' receive completed.");
			}

			if (file.exists()) {
				file.delete();
			}

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				byte[] buf = new byte[ChunkDialect.CHUNK_SIZE];
				int len = 0;
				while ((len = chunkDialect.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				fos.flush();
			} catch (FileNotFoundException e) {
				Logger.log(this.getClass(), e, LogLevel.ERROR);
			} catch (IOException e) {
				Logger.log(this.getClass(), e, LogLevel.ERROR);
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException e) {
						Logger.log(this.getClass(), e, LogLevel.WARNING);
					}
				}
			}

			// 文件接收完毕
//			synchronized (this.senderContextMap) {
//				FileContext ctx = this.senderContextMap.get(senderName);
//				if (null != ctx) {
//					FileMessage fileMessage = ctx.getFileMessage(receiverName, fileName);
//					fileMessage.ready = true;
//
//					if (null != this.delegate) {
//						this.delegate.onReceiveCompleted(fileMessage);
//					}
//				}
//			}
		}
		  
		
	/*	String filePath = this.checkAndGetReceivePath(senderName);
		String fileName = chunkDialect.getSign();
		String fullPath = filePath + fileName;

		File file = this.fileMap.get(fullPath);
		if (null == file) {
			file = new File(filePath, fileName);
			this.fileMap.put(fullPath, file);
		}

		long processed = 0;
		long total = chunkDialect.getTotalLength();
		int index = chunkDialect.getChunkIndex();
		if (index + 1 == chunkDialect.getChunkNum()) {
			processed = total;
		}
		else {
			processed = (index + 1) * ChunkDialect.CHUNK_SIZE;
		}

		if (null != this.delegate) {
			FileContext ctx = this.senderContextMap.get(senderName);
			if (null != ctx) {
				FileMessage fileMessage = ctx.getFileMessage(receiverName, fileName);
				this.delegate.onReceiveProgress(fileMessage, processed, total);
			}
		}

		if (chunkDialect.hasCompleted()) {
			if (Logger.isDebugLevel()) {
				Logger.d(FileManager.class, "File '" + chunkDialect.getSign() + "' receive completed.");
			}

			if (file.exists()) {
				file.delete();
			}

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				byte[] buf = new byte[ChunkDialect.CHUNK_SIZE];
				int len = 0;
				while ((len = chunkDialect.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				fos.flush();
			} catch (FileNotFoundException e) {
				Logger.log(this.getClass(), e, LogLevel.ERROR);
			} catch (IOException e) {
				Logger.log(this.getClass(), e, LogLevel.ERROR);
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException e) {
						Logger.log(this.getClass(), e, LogLevel.WARNING);
					}
				}
			}

			// 文件接收完毕
			synchronized (this.senderContextMap) {
				FileContext ctx = this.senderContextMap.get(senderName);
				if (null != ctx) {
					FileMessage fileMessage = ctx.getFileMessage(receiverName, fileName);
					fileMessage.ready = true;

					if (null != this.delegate) {
						this.delegate.onReceiveCompleted(fileMessage);
					}
				}
			}
		}
		*/
	}

	@Override
	public void onProgress(String target, ChunkDialect chunkDialect) {
//		if (null != this.delegate) {
//			long total = chunkDialect.getTotalLength();
//			int index = chunkDialect.getChunkIndex();
//			long processed = ((index + 1) == chunkDialect.getChunkNum()) ?
//					total : (index + 1) * ChunkDialect.CHUNK_SIZE;
//		}
	}
	
	public void onSendProgress(FileMessage fileMessage, long processed, long total)
	{
		//Nothing
	}

	public void onReceiveProgress(FileMessage fileMessage, long processed, long total)
	{
		//Nothing
	}

	public void onReceiveCompleted(FileMessage fileMessage)
	{

		//Nothing
	}
}
