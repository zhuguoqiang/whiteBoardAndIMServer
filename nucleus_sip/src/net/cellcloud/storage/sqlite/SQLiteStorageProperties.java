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

package net.cellcloud.storage.sqlite;

import net.cellcloud.util.Properties;
import net.cellcloud.util.StringProperty;

/** SQLite 存储器属性配置。
 * 
 * @author Jiangwei Xu
 */
public final class SQLiteStorageProperties extends Properties {

	/// 数据文件名
	protected final static String DB_FILE = "db_file";

	/** 指定数据库文件创建属性集。
	 */
	public SQLiteStorageProperties(String dbFile) {
		StringProperty file = new StringProperty(DB_FILE, dbFile);
		this.addProperty(file);
	}

	public String getDBFile() {
		return (String)this.getProperty(DB_FILE).getValue();
	}
}
