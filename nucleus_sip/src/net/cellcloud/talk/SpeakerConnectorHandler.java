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

import net.cellcloud.common.Logger;
import net.cellcloud.common.Message;
import net.cellcloud.common.MessageErrorCode;
import net.cellcloud.common.MessageHandler;
import net.cellcloud.common.Packet;
import net.cellcloud.common.Session;
import net.cellcloud.util.Utils;

/** Speaker 连接处理器。
 * 
 * @author Jiangwei Xu
 */
public final class SpeakerConnectorHandler implements MessageHandler {

	private Speaker speaker;

	/** 构造函数。
	 */
	public SpeakerConnectorHandler(Speaker speaker) {
		this.speaker = speaker;
	}

	/**
	 * @copydoc MessageHandler::sessionCreated(Session)
	 */
	@Override
	public void sessionCreated(Session session) {
		// Nothing
	}

	/**
	 * @copydoc MessageHandler::sessionDestroyed(Session)
	 */
	@Override
	public void sessionDestroyed(Session session) {
		// Nothing
	}

	/**
	 * @copydoc MessageHandler::sessionOpened(Session)
	 */
	@Override
	public void sessionOpened(Session session) {
		// Nothing
	}

	/**
	 * @copydoc MessageHandler::sessionClosed(Session)
	 */
	@Override
	public void sessionClosed(Session session) {
		this.speaker.notifySessionClosed();
	}

	/**
	 * @copydoc MessageHandler::messageReceived(Session, Message)
	 */
	@Override
	public void messageReceived(Session session, Message message) {
		// 解包
		Packet packet = Packet.unpack(message.get());
		if (null != packet) {
			// 解析数据包
			interpret(session, packet);
		}
	}

	/**
	 * @copydoc MessageHandler::messageSent(Session, Message)
	 */
	@Override
	public void messageSent(Session session, Message message) {
		// Nothing
	}

	/**
	 * @copydoc MessageHandler::errorOccurred(int, Session)
	 */
	@Override
	public void errorOccurred(int errorCode, Session session) {
		if (Logger.isDebugLevel()) {
			Logger.d(SpeakerConnectorHandler.class, "errorOccurred : " + errorCode);
		}

		if (errorCode == MessageErrorCode.CONNECT_TIMEOUT
			|| errorCode == MessageErrorCode.CONNECT_FAILED) {

			TalkServiceFailure failure = new TalkServiceFailure(TalkFailureCode.CALL_FAILED
				, this.getClass());
			failure.setSourceDescription("Attempt to connect to host timed out");
			failure.setSourceCelletIdentifier(this.speaker.getIdentifier());
			this.speaker.fireFailed(failure);

			// 标记为丢失
			this.speaker.lost = true;
		}
	}

	private void interpret(Session session, Packet packet) {
		// 处理包

		byte[] tag = packet.getTag();

		if (TalkDefinition.TPT_DIALOGUE[2] == tag[2]
			&& TalkDefinition.TPT_DIALOGUE[3] == tag[3]) {
			this.speaker.doDialogue(packet, session);
		}
		else if (TalkDefinition.TPT_RESUME[2] == tag[2]
			&& TalkDefinition.TPT_RESUME[3] == tag[3]) {
			this.speaker.doResume(packet, session);
		}
		else if (TalkDefinition.TPT_SUSPEND[2] == tag[2]
				&& TalkDefinition.TPT_SUSPEND[3] == tag[3]) {
			this.speaker.doSuspend(packet, session);
		}
		else if (TalkDefinition.TPT_CONSULT[2] == tag[2]
			&& TalkDefinition.TPT_CONSULT[3] == tag[3]) {
			this.speaker.doConsult(packet, session);
		}
		else if (TalkDefinition.TPT_REQUEST[2] == tag[2]
			&& TalkDefinition.TPT_REQUEST[3] == tag[3]) {
			this.speaker.doReply(packet, session);
		}
		else if (TalkDefinition.TPT_CHECK[2] == tag[2]
			&& TalkDefinition.TPT_CHECK[3] == tag[3]) {

			// 记录标签
			byte[] rtag = packet.getSubsegment(1);
			this.speaker.recordTag(Utils.bytes2String(rtag));

			// 请求 Cellet
			this.speaker.requestCellet(session);
		}
		else if (TalkDefinition.TPT_INTERROGATE[2] == tag[2]
			&& TalkDefinition.TPT_INTERROGATE[3] == tag[3]) {
			this.speaker.requestCheck(packet, session);

			// 重置重试参数
			if (null != this.speaker.capacity) {
				this.speaker.retryTimestamp = 0;
				this.speaker.retryCounts = 0;
				this.speaker.retryEnd = false;
			}
		}
	}
}
