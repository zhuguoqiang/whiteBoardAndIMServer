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

package net.cellcloud.talk.stuff;

/** 变量字面意义。
 * 
 * @author Jiangwei Xu
 */
public enum LiteralBase {

	/** 字符串型。
	 */
	STRING,

	/** 整数型。
	 */
	INT,

	/** 无符号整数型。
	 */
	UINT,

	/** 长整数型。
	 */
	LONG,

	/** 无符号长整型。
	 */
	ULONG,

	/** 浮点型。
	 */
	FLOAT,

	/** 双精浮点型。
	 */
	DOUBLE,

	/** 布尔型。
	 */
	BOOL,

	/** JSON 类型。
	 */
	JSON,

	/** XML 类型。
	 */
	XML
}
