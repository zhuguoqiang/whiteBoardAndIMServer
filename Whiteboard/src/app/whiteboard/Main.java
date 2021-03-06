package app.whiteboard;

import net.cellcloud.cell.Cell;
import net.cellcloud.core.Nucleus;
import net.cellcloud.core.NucleusConfig;
import net.cellcloud.exception.SingletonException;

/**
 * 主入口。
 */
public final class Main {

	public static void main(String[] args) {
		try {
			NucleusConfig config = new NucleusConfig();
			// 禁用 HTTP
			config.httpd = false;
			// 设置缓存大小
			config.talk.block = 20480;
			
			config.talk.port = 7010;

			// 实例化内核
			Nucleus nucleus = Nucleus.createInstance(config);

			// 注册 Cellet
			nucleus.registerCellet(new Whiteboard());

		} catch (SingletonException e) {
			e.printStackTrace();
		}

		// 调用入口函数
		Cell.main(new String[]{"start", "-console=false"});
	}
}
