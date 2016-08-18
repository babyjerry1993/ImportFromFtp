/*
 * (C) Copyright 2009 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Thierry Delprat
 */
package com.shawn.controller;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.shawn.base.ImporterRunner;
import com.shawn.ftpUtil.FtpInfo;
import com.shawn.ftpUtil.FtpUtil;
import com.shawn.service.DefaultImporterService;
import org.nuxeo.runtime.api.Framework;

@Path("myImporter")
public class HttpFileImporterExecutor extends AbstractJaxRSImporterExecutor {

	private static final Log log = LogFactory.getLog(HttpFileImporterExecutor.class);

	protected DefaultImporterService importerService;

	@Override
	protected Log getJavaLogger() {
		return log;
	}

	@GET
	@Path("run")
	@Produces("text/plain; charset=UTF-8")
	public String run(@QueryParam("leafType") String leafType, @QueryParam("folderishType") String folderishType,
			@QueryParam("url") String url, @QueryParam("port") String port, @QueryParam("username") String username,
			@QueryParam("password") String password, @QueryParam("path") String path,
			@QueryParam("targetPath") String targetPath,
			@QueryParam("skipRootContainerCreation") Boolean skipRootContainerCreation,
			@QueryParam("batchSize") Integer batchSize, @QueryParam("nbThreads") Integer nbThreads,
			@QueryParam("interactive") Boolean interactive,
			@QueryParam("transactionTimeout") Integer transactionTimeout) {

		if (port == null) {
			port = "21";
		}

		if (url == null || username == null || password == null) {
			return "Can not import, missing info of FTP";
		}
		if (path == null || targetPath == null) {
			return "Can not import, missing " + (path == null ? "path" : "targetPath");
		}
		if (skipRootContainerCreation == null) {
			skipRootContainerCreation = false;
		}
		if (batchSize == null) {
			batchSize = 5;
		}
		if (nbThreads == null) {
			nbThreads = 5;
		}
		if (interactive == null) {
			interactive = false;
		}
		if (transactionTimeout == null) {
			transactionTimeout = 0;
		}
		getImporterService().setTransactionTimeout(transactionTimeout);

		// 改变了html填入的参数url(IP)+相对路径，以适应FTP上传批量文件
		// 本地缓存目录，若不存在则建立
		String tempPath = "C:\\temp";
		File file = new File(tempPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//做下载到缓存目录的操作
		FtpUtil fu = new FtpUtil();
		FtpInfo info = new FtpInfo(url, Integer.parseInt(port.trim()), username, password, path, null, tempPath);
		fu.downloadFolderFromFtp(info);


		//导入都是从缓存目录取文件
		if (leafType != null || folderishType != null) {
			log.info("Importing with the specified doc types");
			return getImporterService().importDocuments(this, leafType, folderishType, targetPath, tempPath,
					skipRootContainerCreation, batchSize, nbThreads, interactive);
		} else {
			log.info("Importing with the deafult doc types");
			return getImporterService().importDocuments(this, targetPath, tempPath, skipRootContainerCreation,
					batchSize, nbThreads, interactive);
		}

	}

	@Override
	public String run(ImporterRunner runner, Boolean interactive) {
		return doRun(runner, interactive);
	}

	protected DefaultImporterService getImporterService() {
		if (importerService == null) {
			importerService = Framework.getService(DefaultImporterService.class);
		}
		return importerService;
	}
}
