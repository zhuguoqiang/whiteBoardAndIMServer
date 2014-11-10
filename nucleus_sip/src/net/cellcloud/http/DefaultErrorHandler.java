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

package net.cellcloud.http;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cellcloud.common.Logger;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

/** 默认错误处理句柄。
 * 
 * @author Jiangwei Xu
 *
 */
public class DefaultErrorHandler extends ErrorHandler {

	public DefaultErrorHandler() {
		super();
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
		if (Logger.isDebugLevel()) {
			Logger.d(DefaultErrorHandler.class, "Error http request: " + request.getRequestURI());
		}

		response.setHeader("Server", "Cell Cloud");
		baseRequest.setHandled(true);
	}

	@Override
	protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
		// Nothing
	}

	@Override
	protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
		// Nothing
	}

	@Override
	protected void writeErrorPageHead(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
		// Nothing
	}

	@Override
	protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
		// Nothing
	}

	@Override
	protected void writeErrorPageMessage(HttpServletRequest request, Writer writer, int code, String message, String uri) throws IOException {
		// Nothing
	}

	@Override
	protected void writeErrorPageStacks(HttpServletRequest request, Writer writer) throws IOException {
		// Nothing
	}
}
