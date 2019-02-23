STConvert Analysis for Elasticsearch
==================================

STConvert is analyzer that convert Chinese characters between Traditional and Simplified.
[中文简繁體转换][简体到繁体][繁体到简体][简繁查询Expand]

You can download the pre-build package from [release page](https://github.com/medcl/elasticsearch-analysis-stconvert/releases)

The plugin includes  analyzer: `stconvert`,
 tokenizer: `stconvert`,
 token-filter:  `stconvert`,
 and char-filter: `stconvert`

Supported config:

- `delimiter`:default `,`

- `keep_both`:default `false` ,

- `convert_type`: default `s2t` ,optional option:
    1. `s2t` ,convert characters from Simple Chinese to Traditional Chinese
    2. `t2s` ,convert characters from Traditional Chinese to Simple Chinese


Custom example:

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
             "filter": {
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
                    "convert_type" : "t2s"
                }
            }
        }
    }
}
```


Analyze tests

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
