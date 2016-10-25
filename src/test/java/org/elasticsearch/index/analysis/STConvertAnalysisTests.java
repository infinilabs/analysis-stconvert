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
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class STConvertAnalysisTests {


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
