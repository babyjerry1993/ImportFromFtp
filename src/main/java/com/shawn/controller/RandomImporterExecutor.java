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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.shawn.base.GenericMultiThreadedImporter;
import com.shawn.base.ImporterRunner;
import com.shawn.base.ImporterRunnerConfiguration;
import com.shawn.filter.EventServiceConfiguratorFilter;
import com.shawn.filter.ImporterFilter;
import com.shawn.service.DefaultImporterService;
import com.shawn.source.RandomTextSourceNode;
import com.shawn.source.SourceNode;
import org.nuxeo.runtime.api.Framework;

@Path("randomImporter")
public class RandomImporterExecutor extends AbstractJaxRSImporterExecutor {

    private static final Log log = LogFactory.getLog(RandomImporterExecutor.class);

    @Override
    protected Log getJavaLogger() {
        return log;
    }

    @GET
    @Path("run")
    @Produces("text/plain; charset=UTF-8")
    public String run(@QueryParam("targetPath") String targetPath,
            @QueryParam("skipRootContainerCreation") Boolean skipRootContainerCreation,
            @QueryParam("batchSize") Integer batchSize, @QueryParam("nbThreads") Integer nbThreads,
            @QueryParam("interactive") Boolean interactive, @QueryParam("nbNodes") Integer nbNodes,
            @QueryParam("fileSizeKB") Integer fileSizeKB, @QueryParam("onlyText") Boolean onlyText,
            @QueryParam("nonUniform") Boolean nonUniform, @QueryParam("withProperties") Boolean withProperties,
            @QueryParam("blockSyncPostCommitProcessing") Boolean blockSyncPostCommitProcessing,
            @QueryParam("blockAsyncProcessing") Boolean blockAsyncProcessing,
            @QueryParam("blockIndexing") Boolean blockIndexing, @QueryParam("bulkMode") Boolean bulkMode,
            @QueryParam("transactionTimeout") Integer transactionTimeout) {

        if (onlyText == null) {
            onlyText = true;
        }
        if (nonUniform == null) {
            nonUniform = false;
        }
        if (withProperties == null) {
            withProperties = false;
        }
        if (bulkMode == null) {
            bulkMode = true;
        }
        getLogger().info("Init Random text generator");
        SourceNode source = RandomTextSourceNode.init(nbNodes, fileSizeKB, onlyText, nonUniform, withProperties);
        getLogger().info("Random text generator initialized");

        ImporterRunnerConfiguration configuration = new ImporterRunnerConfiguration.Builder(source, targetPath,
                getLogger()).skipRootContainerCreation(skipRootContainerCreation)
                            .batchSize(batchSize)
                            .nbThreads(nbThreads)
                            .build();
        GenericMultiThreadedImporter runner = new GenericMultiThreadedImporter(configuration);

        ImporterFilter filter = new EventServiceConfiguratorFilter(blockSyncPostCommitProcessing, blockAsyncProcessing,
                !onlyText, blockIndexing, bulkMode);
        runner.addFilter(filter);
        if (transactionTimeout != null) {
            Framework.getService(DefaultImporterService.class).setTransactionTimeout(transactionTimeout);
        }
        String res = run(runner, interactive);
        return res;
    }

    @Override
    public String run(ImporterRunner runner, Boolean interactive) {
        return doRun(runner, interactive);
    }

}
