/*
-----------------------------------------------------------------------------
This source file is part of Cell Cloud.

Copyright (c) 2009-2013 Cell Cloud Team (www.cellcloud.net)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
-----------------------------------------------------------------------------
*/

package net.cellcloud.cluster;

import java.util.Date;
import java.util.Map;

import net.cellcloud.common.Message;
import net.cellcloud.common.Session;
import net.cellcloud.util.Utils;

/** 集群协议。
 * 
 * @author Jiangwei Xu
 */
public abstract class ClusterProtocol {

	// 协议名
	public final static String KEY_PROTOCOL = "Protocol";
	// 内核标签
	public final static String KEY_TAG = "Tag";
	// 本地时间
	public final static String KEY_DATE = "Date";
	// 状态
	public final static String KEY_STATE = "State";
	// 节点散列码
	public final static String KEY_HASH = "Hash";

	private String name;

	// 上下文会话
	protected Session contextSession;

	// 属性
	protected Map<String, String> prop;

	/** 指定协议名构建协议。
	 */
	public ClusterProtocol(String name) {
		this.name = name;
		this.prop = null;
	}

	/** 指定协议属性值构建协议。
	 */
	public ClusterProtocol(String name, Map<String, String> prop) {
		this.name = name;
		this.prop = prop;
	}

	/** 返回协议名。
	 */
	public final String getName() {
		return this.name;
	}

	/** 返回标准日期。
	 */
	public final String getStandardDate() {
		return Utils.sDateFormat.format(new Date());
	}

	/** 返回协议内传输的标签。
	 */
	public final String getTag() {
		return this.prop.get(KEY_TAG);
	}

	/** 返回协议状态。
	 */
	public int getStateCode() {
		String szState = this.prop.get(KEY_STATE);
		return (null != szState) ? Integer.parseInt(szState) : -1;
	}

	/** 返回物理节点 Hash 。
	 */
	public long getHash() {
		String szHash = this.prop.get(KEY_HASH);
		return (null != szHash) ? Long.parseLong(szHash) : 0;
	}

	/** 返回指定键对应的属性值。
	 */
	public String getProp(String key) {
		return (null != this.prop) ? this.prop.get(key) : null;
	}

	/** 启动协议。
	 */
	abstract public void launch(Session session);

	/** 向指定 Session 回送执行结果。
	 */
	abstract public void respond(ClusterNode node, StateCode state);

	/** 协议收尾处理并发送。
	 */
	protected void touch(Session session, StringBuilder buffer) {
		buffer.append("\r\n\r\n");
		Message message = new Message(buffer.toString());
		session.write(message);
	}

	/** 协议状态。
	 */
	public enum StateCode {

		// 成功
		SUCCESS(200),

		// 操作被拒绝
		REJECT(201),

		// 操作失败
		FAILURE(209);

		private int code;

		private StateCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}
	}
}
