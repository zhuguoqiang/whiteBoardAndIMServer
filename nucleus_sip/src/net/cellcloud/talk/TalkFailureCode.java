/*
-----------------------------------------------------------------------------
This source file is part of Cell Cloud.

Copyright (c) 2009-2014 Cell Cloud Team (www.cellcloud.net)

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

package net.cellcloud.talk;

/** 会话故障码。
 * 
 * @author Jiangwei Xu
 */
public enum TalkFailureCode {

	/** 未找到指定的 Cellet 。
	 * @note 此错误不触发自动重连。
	 */
	NOTFOUND_CELLET(1000),

	/** Call 请求失败。 */
	CALL_FAILED(2000),

	/** 会话连接被断开。 */
	TALK_LOST(3000),

	/** 重试次数达到上限，重试结束。
	 * @note 此错误不触发自动重连。
	 */
	RETRY_END(4000);

	private int code;

	private TalkFailureCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
