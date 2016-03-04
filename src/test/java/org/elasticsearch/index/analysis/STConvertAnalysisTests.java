/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.settings.Settings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import static org.hamcrest.Matchers.instanceOf;

/**
 */
public class STConvertAnalysisTests {

    @Test
    public void testAnalysis() {

        Index index = new Index("test");
        Settings settings = settingsBuilder()
                .put("path.home", "/")
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(Settings.builder().put("index.version.created",1).build()), new EnvironmentModule(new Environment(settings))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, settings),
                new IndexNameModule(index),
                new AnalysisModule(Settings.EMPTY, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new STConvertAnalysisBinderProcessor()))
                .createChildInjector(parentInjector);

        AnalysisService analysisService = injector.getInstance(AnalysisService.class);

        TokenizerFactory tokenizerFactory = analysisService.tokenizer("stconvert");
        MatcherAssert.assertThat(tokenizerFactory, instanceOf(STConvertTokenizerFactory.class));

        TokenFilterFactory tokenFilterFactory = analysisService.tokenFilter("stconvert");
        MatcherAssert.assertThat(tokenFilterFactory, instanceOf(STConvertTokenFilterFactory.class));

    }

    @Test
    public void testTokenFilter() throws IOException {
        StringReader sr = new StringReader("刘德华");
        Analyzer analyzer = new StandardAnalyzer();
        STConvertTokenFilter filter = new STConvertTokenFilter(analyzer.tokenStream("f", sr), STConvertType.SIMPLE_2_TRADITIONAL, ",", true);
        List<String> list = new ArrayList<String>();
        filter.reset();
        while (filter.incrementToken()) {
            CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
            list.add(ta.toString());
            System.out.println(ta.toString());
        }
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("劉,刘", list.get(0));
        Assert.assertEquals("德,德", list.get(1));
        Assert.assertEquals("華,华", list.get(2));

        sr = new StringReader("刘德华");
        analyzer = new KeywordAnalyzer();
        filter = new STConvertTokenFilter(analyzer.tokenStream("f", sr), STConvertType.SIMPLE_2_TRADITIONAL, ",", false);
        list.clear();
        filter.reset();
        while (filter.incrementToken()) {
            CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
            list.add(ta.toString());
            System.out.println(ta.toString());
        }
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("劉德華", list.get(0));
    }

    @Test
    public void TestTokenizer() throws IOException {
        String[] s = {"刘德华", "劉德華"};
        List<String> list = new ArrayList<String>();

        for (String value : s) {
            System.out.println(value);
            StringReader sr = new StringReader(value);

            STConvertTokenizer tokenizer = new STConvertTokenizer(STConvertType.TRADITIONAL_2_SIMPLE, ",", true);
            tokenizer.setReader(sr);
            tokenizer.reset();

            boolean hasnext = tokenizer.incrementToken();
            while (hasnext) {

                CharTermAttribute ta = tokenizer.getAttribute(CharTermAttribute.class);

                System.out.println(ta.toString());
                list.add(ta.toString());
                hasnext = tokenizer.incrementToken();

            }
        }
        Assert.assertEquals("刘德华,刘德华", list.get(0));
        Assert.assertEquals("刘德华,劉德華", list.get(1));
    }
}
