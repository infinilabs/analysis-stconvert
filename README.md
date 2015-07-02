STConvert Analysis for ElasticSearch
==================================

STConvert is analyzer that convert chinese characters between traditional and simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

you can download this plugin from RTF project(https://github.com/medcl/elasticsearch-rtf)

    --------------------------------------------------
    | STConvert  Analysis Plugin    | ElasticSearch   |
    --------------------------------------------------
    | 1.4.0                        | 1.6.0  -> master |
    --------------------------------------------------
    | 1.3.0                        | 1.0.0  -> 1.0.0  |
    --------------------------------------------------
    | 1.2.0                        | 0.90.0 -> 0.90.2 |
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


Test

elasticsearch.yml
`
index:
  analysis:
    tokenizer:

      s2t_convert:
        type: stconvert
        delimiter: ","
        convert_type: s2t
      t2s_convert:
        type: stconvert
        delimiter: ","
        convert_type: t2s
      s2t_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: s2t
      t2s_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: t2s

    filter:

      s2t_convert:
        type: stconvert
        delimiter: ","
        convert_type: s2t
      t2s_convert:
        type: stconvert
        delimiter: ","
        convert_type: t2s
      s2t_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: s2t
      t2s_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: t2s

    analyzer:

      stconvert:
        alias:
        - st_analyzer
        type: org.elasticsearch.index.analysis.STConvertAnalyzerProvider
      s2t_convert:
        type: stconvert
        delimiter: ","
        convert_type: s2t
      t2s_convert:
        type: stconvert
        delimiter: ","
        convert_type: t2s
      s2t_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: s2t
      t2s_keep_both_convert:
        type: stconvert
        delimiter: ","
        keep_both: 'true'
        convert_type: t2s
`

analyze tests

`
 ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e6%97%b6%e9%97%b4\&tokenizer\=s2t_keep_both_convert
{"tokens":[{"token":"北京時間,北京时间","start_offset":0,"end_offset":4,"type":"word","position":1}]}%
~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=t2s_keep_both_convert
{"tokens":[{"token":"北京国际电视台,北京國際電視臺","start_offset":0,"end_offset":7,"type":"word","position":1}]}%
~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=t2s_convert
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":1}]}%
~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&analyzer\=t2s_convert
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":1}]}%
~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&filters\=t2s_convert
{"tokens":[{"token":"北京國際電視臺","start_offset":0,"end_offset":7,"type":"word","position":1}]}%
~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=keyword\&filters\=t2s_convert
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":1}]}%
`
