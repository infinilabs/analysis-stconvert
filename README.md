STConvert Analysis for ElasticSearch
==================================

STConvert is analyzer that convert chinese characters between traditional and simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

In order to install the plugin, simply run: `bin/plugin -install medcl/elasticsearch-analysis-stconvert/1.0.0`.

    --------------------------------------------------
    | STConvert  Analysis Plugin    | ElasticSearch  |
    --------------------------------------------------
    | master                        | 0.19 -> master |
    --------------------------------------------------

The plugin includes a `stconvert` analyzer , a tokenizer: `stconvert`  and a token-filter:  `stconvert` .

delimiter:","
keep_both:"false" ,
convert_type:"s2t"

optional config:
1. `s2t` ,convert characters between simple chinese and traditional chinese
2. `t2s` ,convert characters between traditional chinese and simple chinese