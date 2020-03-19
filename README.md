# springboot-springBatch-demo
springboot 2.2.5.RELEASE + springbatch ( spring-boot-starter-batch 2.2.5.RELEASE ).
<br/>

**Note:** This project will contains all demo of SpringBatch official example (sub-project spring-batch-samples): <br/>
 https://github.com/spring-projects/spring-batch  <br/>
But I did change Gradle to Maven, yes, this is **maven project**.
 <br/>
 
**some demos from :** [https://github.com/spring-projects/spring-batch](https://github.com/spring-projects/spring-batch/tree/master/spring-batch-samples)

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




    $ end...
