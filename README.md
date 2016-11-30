STConvert Analysis for Elasticsearch
==================================

STConvert is analyzer that convert Chinese characters between Traditional and Simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

You can download from here: https://github.com/medcl/elasticsearch-analysis-stconvert/releases

    --------------------------------------------------
    | STConvert  Analysis Plugin   | Elasticsearch    |
    --------------------------------------------------
    | 5.0.2                        | 5.0.2            |
    --------------------------------------------------
    | 1.9.1                        | 2.4.1            |
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

The plugin includes  analyzer: `stconvert`,
 tokenizer: `stconvert`,
 token-filter:  `stconvert`,
 and char-filter: `stconvert`

Config:

1.`delimiter`:default `,`

2.`keep_both`:default `false` ,

3.`convert_type`: default `s2t`
,optional option:

1. `s2t` ,convert characters from Simple Chinese to Traditional Chinese
2. `t2s` ,convert characters from Traditional Chinese to Simple Chinese


Custom:

```
PUT /stconvert/
{
    "index" : {
        "analysis" : {
            "analyzer" : {
                "tsconvert" : {
                    "tokenizer" : "tsconvert"
                    }
            },
            "tokenizer" : {
                "tsconvert" : {
                    "type" : "stconvert",
                    "delimiter" : "#",
                    "keep_both" : false,
                    "convert_type" : "t2s"
                }
            },
            "char_filter" : {
                "tsconvert" : {
                    "type" : "stconvert",
                    "delimiter" : "#",
                    "keep_both" : false,
                    "convert_type" : "t2s"
                }
            }
        }
    }
}
```


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

```

```
GET stconvert/_analyze
{
  "tokenizer" : "keyword",
  "filter" : ["lowercase"],
  "char_filter" : ["tsconvert"],
  "text" : "国际國際"
}
{
  "tokens": [
    {
      "token": "国际国际",
      "start_offset": 0,
      "end_offset": 4,
      "type": "word",
      "position": 0
    }
  ]
}
```
