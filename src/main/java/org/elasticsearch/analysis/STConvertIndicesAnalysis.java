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
    
    private static final String STCONVERT = "stconvert";
    private static final String CONVERT_TYPE = "convert_type";
    private static final String KEEP_BOTH = "keep_both";
    private static final String TSCONVERT = "tsconvert";
    private static final String STCONVERT_KEEP_BOTH = "stconvert_keep_both";
    private static final String TSCONVERT_KEEP_BOTH = "tsconvert_keep_both";

    @Inject
    public STConvertIndicesAnalysis(final Settings settings,
                                 IndicesAnalysisService indicesAnalysisService, Environment env) {
        super(settings);

        //analyzers
        indicesAnalysisService.analyzerProviderFactories().put(STCONVERT,
                new PreBuiltAnalyzerProviderFactory(STCONVERT, AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put(CONVERT_TYPE, "s2t")
                                .put(KEEP_BOTH, false)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put(TSCONVERT,
                new PreBuiltAnalyzerProviderFactory(TSCONVERT, AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put(CONVERT_TYPE, "t2s")
                                .put(KEEP_BOTH, false)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put(STCONVERT_KEEP_BOTH,
                new PreBuiltAnalyzerProviderFactory(STCONVERT_KEEP_BOTH, AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put(CONVERT_TYPE, "s2t")
                                .put(KEEP_BOTH,true)
                                .build())));

        indicesAnalysisService.analyzerProviderFactories().put(TSCONVERT_KEEP_BOTH,
                new PreBuiltAnalyzerProviderFactory(TSCONVERT_KEEP_BOTH, AnalyzerScope.GLOBAL,
                        new STConvertAnalyzer(Settings.builder()
                                .put(CONVERT_TYPE, "t2s")
                                .put(KEEP_BOTH, true)
                                .build())));


        //tokenizers

        indicesAnalysisService.tokenizerFactories().put(STCONVERT,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return STCONVERT;
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.SIMPLE_2_TRADITIONAL,",",false);
                    }
                }));

        indicesAnalysisService.tokenizerFactories().put(TSCONVERT,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return TSCONVERT;
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.TRADITIONAL_2_SIMPLE,",",false);
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put(STCONVERT_KEEP_BOTH,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return STCONVERT_KEEP_BOTH;
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.SIMPLE_2_TRADITIONAL,",",true);
                    }
                }));
        indicesAnalysisService.tokenizerFactories().put(TSCONVERT_KEEP_BOTH,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return TSCONVERT_KEEP_BOTH;
                    }

                    @Override
                    public Tokenizer create() {
                        return new STConvertTokenizer(STConvertType.TRADITIONAL_2_SIMPLE,",",true);
                    }
                }));



        //tokenFilters
        indicesAnalysisService.tokenFilterFactories().put(STCONVERT,
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return STCONVERT;
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.SIMPLE_2_TRADITIONAL,",",false);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put(TSCONVERT,
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return TSCONVERT;
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.TRADITIONAL_2_SIMPLE,",",false);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put(STCONVERT_KEEP_BOTH,
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return STCONVERT_KEEP_BOTH;
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.SIMPLE_2_TRADITIONAL,",",true);
                    }
                }));

        indicesAnalysisService.tokenFilterFactories().put(TSCONVERT_KEEP_BOTH,
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return TSCONVERT_KEEP_BOTH;
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new STConvertTokenFilter(tokenStream, STConvertType.TRADITIONAL_2_SIMPLE,",",true);
                    }
                }));

        //char filter
        indicesAnalysisService.charFilterFactories().put(STCONVERT,
                new PreBuiltCharFilterFactoryFactory(new CharFilterFactory() {
                    @Override
                    public String name() {
                        return STCONVERT;
                    }

                    @Override
                    public Reader create(Reader tokenStream) {
                        return new STConvertCharFilter(tokenStream,STConvertType.SIMPLE_2_TRADITIONAL);
                    }
                }));
        indicesAnalysisService.charFilterFactories().put(TSCONVERT,
                new PreBuiltCharFilterFactoryFactory(new CharFilterFactory() {
                    @Override
                    public String name() {
                        return TSCONVERT;
                    }

                    @Override
                    public Reader create(Reader tokenStream) {
                        return new STConvertCharFilter(tokenStream,STConvertType.TRADITIONAL_2_SIMPLE);
                    }
                }));

    }
}