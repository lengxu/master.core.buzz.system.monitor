[![art](src/main/resources/markdown_file/art.png "art")](http://master.cma.cn/home/art/)

>访问地址：[http://master.cma.cn/home/art/](http://master.cma.cn/home/art/)

## 版本说明

* 版本：V1.0
  * 数据全流程
    * 生成：数据来源于**天镜**es5天索引，告警由我们逻辑判断触发
    * 收集：数据来源于**天镜**es5的demo索引查询出实入数，然后通过mcp接口的实收文件列表与定义好的对应资料模板中的文件名比对。告警由全流程触发
    * 入库：同收集
    * mcp接口：http://master.cma.cn/mcp/openapi/v2/query{CO|DI}StationFileInfo?apikey=e10adc3949ba59abbe56e057f20f88dd
      {
          "co_state":"ACTUAL",
          "id_state":"ACTUAL",
          "cts":"A.0042.0003.R001",
          "dpc":"A.0042.0003.P001",
          "sod":"A.0042.0003.S001",
          "time":"2019-10-16 12:20"
      }
      
  * 高性能
    * 登陆节点：数据来源于曙光接口
    * 作业状态：数据来源于解析推送到ftp中的文件日志，告警由解析完日志后根据用户提供的规则进行触发
    
* 版本：V1.1
  * 数据全流程
      * 生成：数据来源于**大数据**es5天索引
      * 收集：数据来源于**大数据**es5的demo索引查询出实入数，然后通过mcp接口的实收文件列表与定义好的对应资料模板中的文件名比对
      * 入库：同收集
      * mcp接口：http://smart-view.nmic.cma/mcp/openapi/v2/query{CO|DI}StationFileInfo?apikey=e10adc3949ba59abbe56e057f20f88dd
        {
            "co_state":"ACTUAL",
            "id_state":"ACTUAL",
            "cts":"A.0042.0003.R001",
            "dpc":"A.0042.0003.P001",
            "sod":"A.0042.0003.S001",
            "time":"2019-10-16 12:20",
            **"destination":"FIDB"**
        }
        
    * 高性能
      * 登陆节点：数据来源于派接口。
      * 作业状态：数据来源于派推送到es库的DI，告警由派推送EI

## 目录结构
* 数据全流程(dataProcess)
  * DataProcessService为数据全流程的接口
  * impl-abs包中的类为数据全流程中各类资源的抽象类，目前分为10分钟、小时、日等三个抽象类。
  * impl包中的类为实现类,其中9个类对应目前的9类产品。每个类均继承了impl-abs包中对应的抽象类。后续新增一类产品只需在此处新增一个类并集成abs包中对应的抽象类即可
* 高性能日志（gxnlog）
  * ~~cldas包中为cldas的日志~~
  * ~~ecflow包中的日志分为3dcloud与3dcloudRealTime两种~~
  * 已调整为由曙光直接推送状态DI及推送EI告警
* 登陆节点（loginNode）
  * 这里为登陆节点数据获取的业务逻辑部分
* music相关数据获取（music）
  * 分为产品访问下载与music接口数据获取
  
## 切换步骤
该切换步骤仅适用于从天镜平台往大数据平台迁移需要调整的配置说明
1. 切换es访问地址(application.yml中jest.uris)
2. 切换告警推送地址(application.yml中alert-url)
3. 切换music数据获取地址(cn.uyun.feign.client中BasicResourceService、MusicQueryService、ProductQueryService)

## 注意事项
- 总览页面展示的是世界时，详情页面展示的是北京时，部署服务所在的机器是世界时
- 产品生成DI的业务时次除开全球海表是世界时，其余均是北京时，而在查询es时，按@receive_time过滤的话，@receive_time是世界时，因此产品生成业务时次为00的对应@receive_time应该是前天16:00
---
收集/入库
- 从ES库获取应收与应入库
所有日数据的time字段时次是世界时，小时和分均为北京时。因此按照ltime过滤时，可以统一减8小时过滤
- 从mcp接口查询实入文件