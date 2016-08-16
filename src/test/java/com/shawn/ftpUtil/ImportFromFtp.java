package com.shawn.ftpUtil;

import org.junit.Test;

import com.shawn.controller.HttpFileImporterExecutor;
import com.shawn.ftpUtil.FtpUtil;

//定义一个类继承HttpFileImporterExecutor，使得该类有了一切import的方法和权限
public class ImportFromFtp extends HttpFileImporterExecutor {

	@Test
	public void importFromFtp(){

		FtpUtil ftpUtil = new FtpUtil();

		String url = "169.254.90.39";
		int port = 21;
		String username = "shawn";
		String password = "shawn";
		String remotePath = "/py";
		String fileName = "123.txt";
		String localPath = "C:/test";

		ftpUtil.downFile(url, port, username, password, remotePath, fileName, localPath);



		String destinationPath = localPath;
		String sourcePath = "/";
        boolean skipRootContainerCreation = false;
        int batchSize = 5;
        int noImportingThreads = 5;
        boolean interactive = false;


        getImporterService().importDocuments(this, destinationPath, sourcePath, skipRootContainerCreation,
                batchSize, noImportingThreads, interactive);

	}

}
