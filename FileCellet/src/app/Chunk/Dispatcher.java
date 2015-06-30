package app.Chunk;

import net.cellcloud.core.Cellet;
import net.cellcloud.talk.dialect.ActionDelegate;
import net.cellcloud.talk.dialect.ActionDialect;

import org.json.JSONException;
import org.json.JSONObject;
//import java.io.*;

public class Dispatcher {

	public final static String SHARE_META = "shareFile";

	private Cellet cellet;

	public Dispatcher(Cellet cellet) {
		this.cellet = cellet;
	}

	public void dispatch(ActionDialect dialect) {
		String action = dialect.getAction();
		
		if (action.equals(SHARE_META)) {
			System.out.println("share_meta whiteboard");
			// 分享 Meta 数据
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					try {
						String stringData = dialect.getParamAsString("data");
						JSONObject data = new JSONObject(stringData);
						String myName = data.getString("myName");
						String peerName = data.getString("peerName");
						int lineARGB = data.getInt("lineARGB");

						// 解析 Meta
						JSONObject metaJson = data.getJSONObject("meta");

						String tag = dialect.getOwnerTag();
						if (null != tag) {
							ActionDialect ad = new ActionDialect();
							ad.setAction(SHARE_META);

							JSONObject value = new JSONObject();
							value.put("from", myName);
							value.put("lineARGB", lineARGB);
							value.put("meta", metaJson);
							ad.appendParam("data", value.toString());

							// 发送数据
							cellet.talk(tag, dialect);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		else if (action.equals("iOS")) {
			System.out.println("iOS");
			// 分享 Meta 数据
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					try {
						String name = dialect.getParamAsString("name");
						String project = dialect.getParamAsString("project");
						System.out.println("name: "+ name + ", project: " + project);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}

