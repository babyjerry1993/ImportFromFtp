/*
 * (C) Copyright 2006-2008 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package com.shawn.executor;

import org.apache.commons.logging.Log;
import org.nuxeo.ecm.core.api.NuxeoException;
import com.shawn.base.ImporterRunner;
import com.shawn.factories.DefaultDocumentModelFactory;
import com.shawn.factories.ImporterDocumentModelFactory;
import com.shawn.log.BasicLogger;
import com.shawn.log.ImporterLogger;
import com.shawn.threading.DefaultMultiThreadingPolicy;
import com.shawn.threading.ImporterThreadingPolicy;

/**
 * base class for importers
 *
 * @author Thierry Delprat
 */
public abstract class AbstractImporterExecutor {

    protected abstract Log getJavaLogger();

    protected static ImporterLogger log;

    protected static Thread executorMainThread;

    protected static ImporterRunner lastRunner;

    protected ImporterThreadingPolicy threadPolicy;

    protected ImporterDocumentModelFactory factory;

    protected int transactionTimeout = 0;

    public ImporterLogger getLogger() {
        if (log == null) {
            log = new BasicLogger(getJavaLogger());
        }
        return log;
    }

    public String getStatus() {
        if (isRunning()) {
            return "Running";
        } else {
            return "Not Running";
        }
    }

    public boolean isRunning() {
        if (executorMainThread == null) {
            return false;
        } else {
            return executorMainThread.isAlive();
        }
    }

    public String kill() {
        if (executorMainThread != null) {
            if (lastRunner != null) {
                lastRunner.stopImportProcrocess();
            }
            executorMainThread.interrupt();
            return "Importer killed";
        }
        return "Importer is not running";
    }

    protected void startTask(ImporterRunner runner, boolean interactive) {
        executorMainThread = new Thread(runner);
        executorMainThread.setName("ImporterExecutorMainThread");
        if (interactive) {
            executorMainThread.run();
        } else {
            executorMainThread.start();
        }
    }

    protected String doRun(ImporterRunner runner, Boolean interactive) {
        if (isRunning()) {
            throw new NuxeoException("Task is already running");
        }
        if (interactive == null) {
            interactive = false;
        }
        lastRunner = runner;
        startTask(runner, interactive);

        if (interactive) {
            return "Task compeleted";
        } else {
            return "Started";
        }
    }

    public ImporterThreadingPolicy getThreadPolicy() {
        if (threadPolicy == null) {
            threadPolicy = new DefaultMultiThreadingPolicy();
        }
        return threadPolicy;
    }

    public void setThreadPolicy(ImporterThreadingPolicy threadPolicy) {
        this.threadPolicy = threadPolicy;
    }

    public ImporterDocumentModelFactory getFactory() {
        if (factory == null) {
            factory = new DefaultDocumentModelFactory();
        }
        return factory;
    }

    public void setFactory(ImporterDocumentModelFactory factory) {
        this.factory = factory;
    }

    /**
     * @since 5.9.4
     */
    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    /**
     * @since 5.9.4
     */
    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    /***
     * since 5.5 this method is invoked when using the <code>DefaultImporterService</code> and passing the executor to
     * the importDocuments method
     *
     * @param runner
     * @param interactive
     * @return
     * @throws Exception
     */
    public String run(ImporterRunner runner, Boolean interactive) {
        return doRun(runner, interactive);
    }
}
