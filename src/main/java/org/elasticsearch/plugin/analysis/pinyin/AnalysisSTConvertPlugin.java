package org.elasticsearch.plugin.analysis.pinyin;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.STConvertAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 */
public class AnalysisSTConvertPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-stconvert";
    }

    @Override
    public String description() {
        return "convert chinese characters between traditional and simplified";
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new STConvertAnalysisBinderProcessor());
    }
}
