STConvert Analysis for Elasticsearch
==================================

STConvert is analyzer that convert chinese characters between traditional and simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

You can download from here: https://github.com/medcl/elasticsearch-analysis-stconvert/releases

    --------------------------------------------------
    | STConvert  Analysis Plugin    | Elasticsearch   |
    --------------------------------------------------
    | 1.9.4                        | 2.4.4            |
    --------------------------------------------------
    | 1.8.5                        | 2.3.5            |
    --------------------------------------------------
    | 1.8.4                        | 2.3.4            |
    --------------------------------------------------
    | 1.8.3                        | 2.3.3            |
    --------------------------------------------------
    | 1.7.1                        | 2.2.1            |
    --------------------------------------------------
    | 1.6.0                        | 2.1.0  -> 2.1.x  |
    --------------------------------------------------      
    | 1.5.0                        | 2.0.0  -> 2.0.x  |
    --------------------------------------------------    
    | 1.4.0                        | 1.0.0  -> 1.7.x  |
    --------------------------------------------------
    | 1.2.0                        | 0.90.0 -> 0.90.2 |
    --------------------------------------------------
    | 1.1.0                        | 0.19.0 -> 0.20.x |
    --------------------------------------------------

The plugin includes several analyzers: `stconvert`,`tsconvert`,`stconvert_keep_both`,`tsconvert_keep_both` ,
 several tokenizers: `stconvert`,`tsconvert`,`stconvert_keep_both`,`tsconvert_keep_both` ,
 and several token-filters:  `stconvert`,`tsconvert`,`stconvert_keep_both`,`tsconvert_keep_both` ,
 and two char-filters: `stconvert`,`tsconvert`.

Config:

1.`delimiter`:default `,`

2.`keep_both`:default `false` ,

3.`convert_type`: default `s2t`
,optional option:

1. `s2t` ,convert characters between simple chinese and traditional chinese
2. `t2s` ,convert characters between traditional chinese and simple chinese


Analyze tests

```
  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=stconvert
{"tokens":[{"token":"北京國際電視檯","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":2}]}%                                                      

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=tsconvert
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京国际电视台","start_offset":8,"end_offset":15,"type":"word","position":2}]}%                                                      

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=tsconvert_keep_both
{"tokens":[{"token":"北京国际电视台,北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京国际电视台,北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":2}]}%                        

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=stconvert_keep_both
{"tokens":[{"token":"北京國際電視檯,北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京國際電視臺,北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":2}]}%                        

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&analyzer\=stconvert_keep_both
{"tokens":[{"token":"北京國際電視檯,北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京國際電視臺,北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":102}]}%                      

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&analyzer\=tsconvert_keep_both
{"tokens":[{"token":"北京国际电视台,北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京国际电视台,北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":102}]}%                      

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&analyzer\=tsconvert         
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京国际电视台","start_offset":8,"end_offset":15,"type":"word","position":102}]}%                                                    

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&analyzer\=stconvert
{"tokens":[{"token":"北京國際電視檯","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":102}]}%                                                    

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=keyword\&char_filters\=tsconvert
{"tokens":[{"token":"北京国际电视台","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京国际电视台","start_offset":8,"end_offset":15,"type":"word","position":1}]}%                                                                                                                                                    

➜  ~  curl -XGET http://localhost:9200/index/_analyze\?text\=%e5%8c%97%e4%ba%ac%e5%9b%bd%e9%99%85%e7%94%b5%e8%a7%86%e5%8f%b0%2c%e5%8c%97%e4%ba%ac%e5%9c%8b%e9%9a%9b%e9%9b%bb%e8%a6%96%e8%87%ba\&tokenizer\=keyword\&char_filters\=stconvert
{"tokens":[{"token":"北京國際電視檯","start_offset":0,"end_offset":7,"type":"word","position":0},{"token":"北京國際電視臺","start_offset":8,"end_offset":15,"type":"word","position":1}]}%  

```
