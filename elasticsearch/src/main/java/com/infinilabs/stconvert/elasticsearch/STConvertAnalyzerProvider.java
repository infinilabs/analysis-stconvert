package com.infinilabs.stconvert.elasticsearch;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.infinilabs.stconvert.core.STConvertAnalyzer;
import com.infinilabs.stconvert.core.STConvertType;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

public class STConvertAnalyzerProvider extends AbstractIndexAnalyzerProvider<STConvertAnalyzer> {

    private final STConvertAnalyzer analyzer;

    public STConvertAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(name);
        Boolean keepBoth = false;
        String type = settings.get("convert_type", "s2t");
        String delimiter = settings.get("delimiter", ",");
        String keepBothStr = settings.get("keep_both", "false");
        if (keepBothStr.equals("true")) {
            keepBoth = true;
        }

        STConvertType convertType = STConvertType.SIMPLE_2_TRADITIONAL;
        if (type.equals("t2s")) {
            convertType = STConvertType.TRADITIONAL_2_SIMPLE;
        }

        analyzer = new STConvertAnalyzer(convertType, delimiter, keepBoth);
    }

    @Override
    public STConvertAnalyzer get() {
        return this.analyzer;
    }
}
