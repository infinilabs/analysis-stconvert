STConvert Analysis for ElasticSearch
==================================

STConvert is analyzer that convert chinese characters between traditional and simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

you can download this plugin from RTF project(https://github.com/medcl/elasticsearch-rtf)

    --------------------------------------------------
    | STConvert  Analysis Plugin    | ElasticSearch   |
    --------------------------------------------------
    | 1.2.0                        | 0.90.0 -> master |
    --------------------------------------------------
    | 1.1.0                        | 0.19.0 -> 0.20.x |
    --------------------------------------------------

The plugin includes a `stconvert` analyzer , a tokenizer: `stconvert`  and a token-filter:  `stconvert` .

delimiter:","
keep_both:"false" ,
convert_type:"s2t"

optional config:
1. `s2t` ,convert characters between simple chinese and traditional chinese
2. `t2s` ,convert characters between traditional chinese and simple chinese

