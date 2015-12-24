package org.elasticsearch.analysis;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

import java.io.Reader;

/**
 * Registers indices level analysis components so, if not explicitly configured,
 * will be shared among all indices.
 */
public class STConvertIndicesAnalysis extends AbstractComponent {

    @Inject
    public STConvertIndicesAnalysis(final Settings settings,
                                 IndicesAnalysisService indicesAnalysisService, Environment env) {
        super(settings);

        //analyzers
        indicesAnalysisService.analyzerProviderFactories().put("stconvert",
                new PreBuiltAnalyzerProviderFactory("stconvert", AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put("convert_type", "s2t")
                                .put("keep_both", false)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put("tsconvert",
                new PreBuiltAnalyzerProviderFactory("tsconvert", AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put("convert_type", "t2s")
                                .put("keep_both", false)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put("stconvert_keep_both",
                new PreBuiltAnalyzerProviderFactory("stconvert_keep_both", AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put("convert_type", "s2t")
                                .put("keep_both",true)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put("tsconvert_keep_both",
                new PreBuiltAnalyzerProviderFactory("tsconvert_keep_both", AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put("convert_type", "t2s")
                                .put("keep_both", true)
                                .build())));


        //tokenizers

        indicesAnalysisService.tokenizerFactories().put("stconvert",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "stconvert";
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.simple2traditional,",",false);
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put("tsconvert",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "tsconvert";
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.traditional2simple,",",false);
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put("stconvert_keep_both",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "stconvert_keep_both";
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.simple2traditional,",",true);
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put("tsconvert_keep_both",
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return "tsconvert_keep_both";
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.traditional2simple,",",true);
                    }
                }));



        //tokenFilters
        indicesAnalysisService.tokenFilterFactories().put("stconvert",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "stconvert";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.simple2traditional,",",false);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("tsconvert",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "tsconvert";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.traditional2simple,",",false);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("stconvert_keep_both",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "stconvert_keep_both";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.simple2traditional,",",true);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put("tsconvert_keep_both",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "tsconvert_keep_both";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.traditional2simple,",",true);
                    }
                }));

        //char filter
        indicesAnalysisService.charFilterFactories().put("stconvert",
                new PreBuiltCharFilterFactoryFactory(new CharFilterFactory() {
                    @Override
                    public String name() {
                        return "stconvert";
                    }

                    @Override
                    public Reader create(Reader tokenStream) {
                        return new STConvertCharFilter(tokenStream,STConvertType.simple2traditional);
                    }
                }));
        indicesAnalysisService.charFilterFactories().put("tsconvert",
                new PreBuiltCharFilterFactoryFactory(new CharFilterFactory() {
                    @Override
                    public String name() {
                        return "tsconvert";
                    }

                    @Override
                    public Reader create(Reader tokenStream) {
                        return new STConvertCharFilter(tokenStream,STConvertType.traditional2simple);
                    }
                }));

    }
}