/*
-----------------------------------------------------------------------------
This source file is part of Cell Cloud.

Copyright (c) 2009-2012 Cell Cloud Team (www.cellcloud.net)

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

package net.cellcloud.talk.dialect;

import net.cellcloud.core.Cellet;

/*! 方言工厂。
 * 
 * \author Jiangwei Xu
 */
public abstract class DialectFactory {

	/*! 返回元数据。
	 */
	abstract public DialectMetaData getMetaData();

	/*! 创建方言。
	 */
	abstract public Dialect create(final String tracker);

	/*! 关闭。
	 */
	abstract public void shutdown();

	/*! 发送回调。
	 * \param identifier
	 * \param dialect
	 * \return
	 */
	abstract protected boolean onTalk(String identifier, Dialect dialect);

	/*! 接收回调。
	 * \param identifier
	 * \param dialect
	 * \return
	 */
	abstract protected boolean onDialogue(String identifier, Dialect dialect);

	/*!
	 * \param cellet
	 * \param targetTag
	 * \param dialect
	 * \return
	 */
	abstract protected boolean onTalk(Cellet cellet, String targetTag, Dialect dialect);

	/*!
	 * \param cellet
	 * \param sourceTag
	 * \param dialect
	 * \return
	 */
	abstract protected boolean onDialogue(Cellet cellet, String sourceTag, Dialect dialect);
}
