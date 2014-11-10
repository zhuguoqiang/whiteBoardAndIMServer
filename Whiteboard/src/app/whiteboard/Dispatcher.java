package app.whiteboard;

import net.cellcloud.core.Cellet;
import net.cellcloud.talk.dialect.ActionDelegate;
import net.cellcloud.talk.dialect.ActionDialect;

import org.json.JSONException;
import org.json.JSONObject;
//import java.io.*;

public class Dispatcher {

	public final static String REGISTER = "register";
	public final static String SHARE_META = "shareMeta";
	public final static String CLEAR_META = "clearMeta";
	public final static String LAUNCH_WB = "launchWB";
	public  final static String TERMINAL_WB = "terminalWB";

	public final static String IMAGE_DATA = "imageData";
	public final static String IMAGE_TRACK = "imageTrack";

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

						String tag = SessionManager.getInstance().getTagByName(peerName);
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
		else if (action.equals(CLEAR_META)) {
			System.out.println("clear_meta whiteboard");
			// 清空 Meta 数据
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					try {
						String stringData = dialect.getParamAsString("data");
						JSONObject data = new JSONObject(stringData);
						String peerName = data.getString("peerName");

						String tag = SessionManager.getInstance().getTagByName(peerName);
						if (null != tag) {
							ActionDialect ad = new ActionDialect();
							ad.setAction(CLEAR_META);
							JSONObject value = new JSONObject();
							value.put("peerName", peerName);
							ad.appendParam("data", value.toString());

							// 发送数据
							cellet.talk(tag, ad);
						}

						// 回送给发送者
						ActionDialect response = new ActionDialect();
						response.setAction(CLEAR_META);
						JSONObject value = new JSONObject();
						value.put("peerName", peerName);
						response.appendParam("data", value.toString());
						cellet.talk(dialect.getOwnerTag(), response);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		else if (action.equals(REGISTER)) {
			System.out.println("register whiteboard");
			// 注册用户
			try {
				String stringData = dialect.getParamAsString("data");
				JSONObject data = new JSONObject(stringData);
				String name = data.getString("name");
				SessionManager.getInstance().register(name.toString(), dialect.getOwnerTag().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else if (action.equals(IMAGE_DATA)) {
			System.out.println("image_data whiteboard");
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					String peerName = null;
					try {
						String stringData = dialect.getParamAsString("data");
						JSONObject data = new JSONObject(stringData);
						peerName = data.getString("peerName");
						int segmentNum = data.getInt("segmentNum");
						int segmentIndex = data.getInt("segmentIndex");

						String tag = SessionManager.getInstance().getTagByName(peerName);
						if (null != tag) {
							ActionDialect ad = new ActionDialect();
							ad.setAction(IMAGE_DATA);
							ad.appendParam("data", stringData);

							// 发送数据
							cellet.talk(tag, ad);
						}

						// 测试代码 - 开始
						ActionDialect response = new ActionDialect();
						response.setAction(IMAGE_TRACK);
						response.appendParam("data", "{\"peerName\":\""+ peerName +"\", \"segmentNum\":"+ segmentNum +", \"segmentIndex\":"+ segmentIndex +"}");
						cellet.talk(dialect.getOwnerTag(), response);

						System.out.println("Image tracker: " + peerName + " - " + segmentIndex + "/" + segmentNum);

						ActionDialect resData = new ActionDialect();
						resData.setAction(IMAGE_DATA);
						resData.appendParam("data", stringData);
						cellet.talk(dialect.getOwnerTag(), resData);
						// 测试代码 - 结束
					} catch (Exception e) {
						
					}
				}
			});
		}else if (action.equals(LAUNCH_WB)) {
			System.out.println("launch whiteboard");
			// 分享 Meta 数据
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					try {
						String stringData = dialect.getParamAsString("data");
						JSONObject data = new JSONObject(stringData);
						String token = data.getString("token");
						String myName = data.getString("myName");
						String peerName = data.getString("peerName");
						String tag = SessionManager.getInstance().getTagByName(peerName);
						if (null != tag) {
							ActionDialect ad = new ActionDialect();
							ad.setAction(LAUNCH_WB);

							JSONObject value = new JSONObject();
							value.put("launch", token);
							value.put("from", myName);
							
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
		else if (action.equals(TERMINAL_WB)) {
			System.out.println("terminal whiteboard");
			// 清空 Meta 数据
			dialect.act(new ActionDelegate() {
				@Override
				public void doAction(ActionDialect dialect) {
					try {
						String stringData = dialect.getParamAsString("data");
						JSONObject data = new JSONObject(stringData);
						String peerName = data.getString("peerName");

						String tag = SessionManager.getInstance().getTagByName(peerName);
						if (null != tag) {
							ActionDialect ad = new ActionDialect();
							ad.setAction(TERMINAL_WB);
							JSONObject value = new JSONObject();
							value.put("peerName", peerName);
							ad.appendParam("data", value.toString());

							// 发送数据
							cellet.talk(tag, ad);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		else {
			// Nothing
		}
	}

	protected GraphMeta parse(JSONObject data) {
		GraphMeta meta = null;
		return meta;
	}
}

