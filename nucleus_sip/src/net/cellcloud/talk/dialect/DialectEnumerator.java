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

import java.util.concurrent.ConcurrentHashMap;

/** 方言枚举器。
 * 
 * @author Jiangwei Xu
 */
public final class DialectEnumerator {

	private static final DialectEnumerator instance = new DialectEnumerator();

	private ConcurrentHashMap<String, DialectFactory> factories;

	private DialectEnumerator() {
		this.factories = new ConcurrentHashMap<String, DialectFactory>();
	}

	/** 返回对象实例。
	 */
	public static DialectEnumerator getInstance() {
		return instance;
	}

	/** 创建方言。
	 */
	public Dialect createDialect(final String name, final String tracker) {
		DialectFactory fact = this.factories.get(name);
		if (null != fact) {
			return fact.create(tracker);
		}

		return null;
	}

	/** 添加方言工厂。
	 */
	public void addFactory(DialectFactory fact) {
		this.factories.put(fact.getMetaData().name, fact);
	}

	/** 删除方言工厂。
	 */
	public void removeFactory(DialectFactory fact) {
		if (this.factories.containsKey(fact.getMetaData().name)) {
			this.factories.remove(fact.getMetaData().name);
		}
	}

	/** 获取指定名称的方言工厂。
	 */
	public DialectFactory getFactory(String name) {
		return this.factories.get(name);
	}

	/** 关闭所有方言工厂。
	 */
	public void shutdownAll() {
		for (DialectFactory fact : this.factories.values()) {
			fact.shutdown();
		}
	}
}
