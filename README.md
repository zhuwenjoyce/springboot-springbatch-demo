# springboot-springBatch-demo
+ springboot 2.2.5.RELEASE 
+ springbatch ( spring-boot-starter-batch 2.2.5.RELEASE )
+ Oracle 10g+
+ Maven 3.5.3+
<br/>

**Note:** 此项目包含SpringBatch官网所有的demo
<br/>
**Note:** This project will contains all demo of SpringBatch official example (sub-project spring-batch-samples): <br/>
But I did change Gradle to Maven, yes, this is **maven project**.
 <br/>
 
**Spring Batch Official docs :** [https://docs.spring.io/spring-batch/docs/4.2.1.RELEASE/reference/html/index.html](https://docs.spring.io/spring-batch/docs/4.2.1.RELEASE/reference/html/index.html)

**some demos from :** [https://github.com/spring-projects/spring-batch](https://github.com/spring-projects/spring-batch/tree/master/spring-batch-samples) [SpringBatch official example]

**some demos from :** [https://howtodoinjava.com/spring-batch](https://howtodoinjava.com/spring-batch/java-config-multiple-steps/)

<br/><br/>
### com.official.demo1
只要com.official.demo1_adhocLoopJob.job.InfiniteLoopReader能一直read到值，RunJob的@Bean("myjob")就能一直跑。
<br/>
**English:** The job can run forever if the reader(com.official.demo1_adhocLoopJob.job.InfiniteLoopReader) can read something.
<br/>

### com.official.demo2
定义一个start step和next step。
<br/>
**English:** Define a start step and a next step
<br/>

### com.official.demo3
Job有一个reader和3个writer。<br/>
因为有3个writer，所以你可以自定义每个writer的写实现，writer1写入到DB，writer2写入report1.txt，writer3写入report2.txt。
<br/>
**English:** Job have 1 reader and 3 writer.<br/>
 so you can customer these writers, one writer store to DB, one writer generate report1.txt, one writer generate report2.txt
<br/>

### com.official.demo4
Job有listener、reader、processor、writer。<br/>
执行顺序依次是：listener、reader、processor、writer。<br/>
如果processor判断符合条件，则正常返回item对象，否则返回null。如果返回null，则不进入下一步writer里执行。writer负责将数据存储到Oracle.
<br/>
**English:** Job have reader、processor、writer、listener.<br/>
The order of execution is: listener, reader, processor, writer.<br/>
If the processor determines that the condition is met, the item object is returned normally, otherwise null is returned. If null is returned, it is not executed in the writer. Writer is responsible for storing data to Oracle.
<br/>

### com.official.demo5
reader和writer可以在用自定义类里的指定方法去实现。
<br/>
**English:** reader and writer can be use custom class and method.
<br/>

### com.official.demo6
1个job3个step，每个step有自己的reader和writer，并且可以定义每个step的执行顺序。
<br/>
**English:** The job have 3 steps, each step has its own reader and writer, and the execution order of each step can be defined.
<br/>

### com.official.demo7
未完成。
<br/>
**English:** unfinished.
<br/>

### com.official.demo8
未完成。
<br/>
**English:** unfinished.
<br/>

### com.official.demo9
定义处理第一行header和最后一行footer的处理类。SummaryFooterCallback监听step执行前和执行后。
<br/>
**English:**  header and footer of writer.
<br/>

### com.official.demo10
集成 hibernate 框架.
<br/>
**English:**  Integration hibernate framework.
<br/>

### com.official.demo11
演示在无限循环任务情况下，如何优雅地关机。但是我只看出了无限循环，没看出咋优雅关机。
<br/>
**English:**  The tasklet used in this job will run in an infinite loop.  This is useful for testing graceful shutdown from
              		multiple environments..
<br/>

### com.official.demo12
这个 ioSampleJob.xml 例子没什么好看的，纯粹是介绍如何在processor中处理东西，略过。。。。
<br/>
**English:**  This iosamplejob. XML example is nothing to look at, it's just an introduction to how to process things in a processor, skip...
<br/>

### com.official.demo13
job里包含的step里还可以再包含job。
<br/>
**English:**  Job container step, which step can container job again.
<br/>

### com.official.demo14
先执行监听器GeneratingTradeResettingListener,再执行决策者LimitDecider。
<br/>
在JobFlowBuilder里设置根据决策者返回什么状态（比如FlowExecutionStatus.COMPLETED）再决定跳转到哪个step执行，或者终止job。
<br/>
**English:**  First perform listener GeneratingTradeResettingListener, then execute decision(LimitDecider).
<br/>
JobFlowBuilder can build decision and which step, If decision return status as FlowExecutionStatus.CONTINUE, then continue execute step2.
 

### com.official.demo15
演示了从DB读取数据，到processor组装邮件内容，到writer发送邮件的例子。
<br/>
**English:**  This demo read data from DB, and build mail content in processor, and send mail in writer.


### com.official.demo16
从txt文件读取的每一行，都可以根据*号模糊匹配各自定义分词器Tokenizer，组装成不同的对象。
<br/>
**English:**  Read data from txt file, and per line can have itself Tokenizer, can build different Object, see:
<br/>
 com.official.demo16_multilineJob.run.BuildStep.getPatternMatchingCompositeLineTokenizer()
 <br/>
        Map<String, LineTokenizer> map = new HashMap<>();
        map.put("BEGIN\*",beginRecordTokenizer); // 行头匹配分词器Tokenizer
        map.put("END\*",endRecordTokenizer);  // 行尾匹配分词器Tokenizer
        map.put("\*",tradeRecordTokenizer);  // 匹配成Trade对象


### com.official.demo17
从txt文件读取的每一行，都可以根据一定规则定义自己的分词器Tokenizer，组装成不同的对象，参考Build_multilineOrderInputTokenizers.java
<br/>
**English:**  Read data from txt file, and per line can have itself Tokenizer, can build different Object, see Build_multilineOrderInputTokenizers.java

### com.official.demo18
参考BuildStep_loading.java的taskExecutor：
为每个任务触发一个新线程的实现，异步地执行它。
支持通过“concurrencyLimit”bean属性限制并发线程。默认情况下，并发线程的数量是无限的。
注意:此实现不重用线程!考虑一个线程池TaskExecutor实现，特别是为执行大量的短期任务。
<br/>
**English:**  See taskExecutor in BuildStep_loading.java:
<br/>
implementation that fires up a new Thread for each task, executing it asynchronously.
 Supports limiting concurrent threads through the "concurrencyLimit" bean property. By default, the number of concurrent threads is unlimited.

NOTE: This implementation does not reuse threads!</b> 
Consider a thread-pooling TaskExecutor implementation instead, in particular for executing a large number of short-lived tasks.








    $ end
