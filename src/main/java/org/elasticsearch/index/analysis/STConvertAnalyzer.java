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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.io.Reader;


public final class STConvertAnalyzer extends Analyzer {


    private String delimiter;
    private String type;
    private Boolean keepBoth=false;


    public STConvertAnalyzer(Settings settings) {
        type = settings.get("convert_type", "t2s");
        delimiter = settings.get("delimiter", ",");
        String keepBothStr = settings.get("keep_both", "false");
        if(keepBothStr.equals("true")){
            keepBoth=true;
        }
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        ConvertType convertType=ConvertType.traditional2simple;
        if(type.equals("s2t")){
            convertType = ConvertType.simple2traditional;
        }
        return new STConvertTokenizer(reader,convertType, delimiter,keepBoth);
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {

        //得到上一次使用的TokenStream，如果没有则生成新的，并且用setPreviousTokenStream放入成员变量，使得下一个可用。
        Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();

        if (tokenizer == null) {
            ConvertType convertType=ConvertType.traditional2simple;
            if(type.equals("s2t")){
                convertType = ConvertType.simple2traditional;
            }
            tokenizer = new STConvertTokenizer(reader,convertType, delimiter,keepBoth);
            setPreviousTokenStream(tokenizer);
        } else {
            //如果上一次生成过TokenStream，则reset初始化。
            tokenizer.reset(reader);
        }
        return tokenizer;
    }
}
