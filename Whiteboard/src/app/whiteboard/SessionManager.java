package app.whiteboard;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionManager {

	private static SessionManager instance = new SessionManager();

	private Timer timer;

	private ConcurrentHashMap<String, String> nameTagMap;
	private ConcurrentHashMap<String, Long> nameTimestampMap;

	public SessionManager() {
		this.nameTagMap = new ConcurrentHashMap<String, String>();
		this.nameTimestampMap = new ConcurrentHashMap<String, Long>();

		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new Daemon(), 10000, 5 * 60 * 1000);
	}

	public static SessionManager getInstance() {
		return SessionManager.instance;
	}

	public void stop() {
		this.timer.cancel();
	}

	public void register(String name, String tag) {
		this.nameTagMap.put(name, tag);
		this.nameTimestampMap.put(name, System.currentTimeMillis());
	}

	public synchronized String getTagByName(String name) {
		if (this.nameTimestampMap.containsKey(name)) {
			this.nameTimestampMap.put(name, System.currentTimeMillis());
		}

		return this.nameTagMap.get(name);
	}

	protected class Daemon extends TimerTask {
		protected Daemon() {
		}

		@Override
		public void run() {
			long time = System.currentTimeMillis();

			LinkedList<String> list = new LinkedList<String>();
			Iterator<Map.Entry<String, Long>> iter = nameTimestampMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Long> entry = iter.next();
				long t = entry.getValue().longValue();
				// 删除超时的标签
				if (time - t >= 300000) {
					list.add(entry.getKey());
				}
			}

			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); ++i) {
					String name = list.get(i);
					nameTagMap.remove(name);
					nameTimestampMap.remove(name);
				}

				list.clear();
			}

			list = null;
		}
	}
}
