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

package net.cellcloud.storage.ehcache;

import net.cellcloud.exception.StorageException;
import net.cellcloud.storage.Storage;
import net.cellcloud.storage.StorageFactory;
import net.cellcloud.storage.StorageMetaData;

/** 缓存工厂。
 * 
 * @author Jiangwei Xu
 */
public final class CacheStorageFactory extends StorageFactory {

	public CacheStorageFactory() {
		super(new StorageMetaData(CacheStorage.TYPE_NAME, "Cache Storage"));
	}

	@Override
	public Storage create(String instanceName) {
		return new CacheStorage(instanceName);
	}

	@Override
	public void destroy(Storage instance) {
		try {
			instance.close();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
}
