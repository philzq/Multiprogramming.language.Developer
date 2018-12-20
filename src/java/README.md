## 官方网站
id|url|desc
--|----|----
1|Oracle官方文档入口|https://docs.oracle.com/
2|JavaSE文档入口 会跳转到当前版本|https://docs.oracle.com/javase/
3|Language and VM  有html及pdf可下载|https://docs.oracle.com/javase/specs/
4|教程|https://docs.oracle.com/javase/tutorial/
5|API Documentation 各包的介绍|https://docs.oracle.com/en/java/javase/11/docs/api/index.html
6|jshell|https://docs.oracle.com/javase/10/jshell/ https://docs.oracle.com/javase/10/tools/jshell.htm .net没有官方的，LinqPad类似java的这个

## 所谓的：生态
**C#**：默认组件已经做的相当出色了，假如算100分。他已经做的这么好了，就算你有个更好的想法可能只是不停的+1,但只是+1力度不大，你的组件就不容易出名，最终看起来好像就是周边发展的不行。一言以蔽之：**月明星稀**。

**Java**：可能自己做“不够好”算50分，但开源的理念好，用的人多，不好的就重新封装一个，最终一些大公司就出品了一堆优秀的组件。然后最终表现就是：50+10+10+10+........很容易超过了C#。一言以蔽之：**众星璀璨**。

## 学习方法
所以学Java与学C#不同，想要用好C#只要学下官方默认组件就基本满分了，简单高效。而要学好Java则得：1.学好默认组件，有些是不用学的，具体哪些你要能分辨，2.熟悉第三方组件如：File，Http,Json,String,Collection，Lombok，框架如Spring。

## 常用的第三方库有
Name|URL|Desc
----|----|----
Apache Commons Lang|https://commons.apache.org/|Commons的开发者会尽其所能地减少组件与其它开发库的依赖，让部署这些组件更加容易。除此之外，Commons组件还会尽可能保持接口的稳定，让Apache的用户（包括使用Commons的其它Apache项目）可以使用时无需担心未来可能的变化。内容包括：二进制，各种编码，字符串操作，集合扩展与增强，压缩解压，文件操作等。
Apache HttpComponents|https://hc.apache.org/|HttpClient,操作网络就用这个啦，跟java11差不多，但header没有限制，如：Origin，Referer在java11里是不让加的。
Google Guava|https://github.com/google/guava/wiki http://ifeve.com/google-guava/|Guava工程包含了若干被Google的 Java项目广泛依赖 的核心库，例如：集合 [collections] 、缓存 [caching] 、原生类型支持 [primitives support] 、并发库 [concurrency libraries] 、通用注解 [common annotations] 、字符串处理 [string processing] 、I/O 等等。 所有这些工具每天都在被Google的工程师应用在产品服务中。
Jackson|https://github.com/FasterXML/jackson-databind/|json序列化与反序列化的

## 写Demo消耗时间
以下是具体某一项功能研究时间，实际时间比这个要多一些，因为要学习相关周边功能。

内容|耗时|说明
----|----|----
Basic|1天|主要是BigDecimal的与C#不同，花了写时间。
DateTime|2天|场景多，不灵活，要不停调试。
String|3天|场景多，加解密算法2天：调试C#与Java，使其互通，用C#加密再用Java解密，及反向操作。
Collection|？|List花了1天，因为集合的通用操作都是用List演示的。其他还没开始


java中的装箱拆箱相当普遍，int->Integer，而C#int->Int32是同一个，不存在装箱拆箱，C#一般只在int->string->int时才涉及到。