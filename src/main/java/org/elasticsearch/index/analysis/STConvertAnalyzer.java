package org.elasticsearch.index.analysis;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;


public final class STConvertAnalyzer extends Analyzer {

    private String delimiter;
    private STConvertType convertType;
    private Boolean keepBoth=false;
    public STConvertAnalyzer(STConvertType type,String delimiter,boolean keepBoth) {
        this.convertType=type;
        this.delimiter=delimiter;
        this.keepBoth=keepBoth;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return  new TokenStreamComponents(new STConvertTokenizer(convertType, delimiter,keepBoth));
    }

}
