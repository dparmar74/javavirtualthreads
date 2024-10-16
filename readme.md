# Java Virtual Threads

Few things to remember before using Virtual Threads

* Virtual Threads provide higher throughput and not higher speed
* Use only with blocking IO operation.  Using for CPU extensive operation will not result any benefit
* Avoid running synchronized block or method. A virtual thread cannot be unmounted during blocking operations when it is pinned to its carrier thread.

This simple example shows the power of virtual threads. 
Running this program on a 12 cores Windows machine results into 80% throughput 

```console
No of Cores:12
Fetching URL tasks started using  platform threads
Fetching URL tasks completed in 1555 ms

No of Cores:12
Fetching URL tasks started using  virtual threads
Fetching URL tasks completed in 313 ms
```

## References
[Java Virtual Threads ](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-704A716D-0662-4BC7-8C7F-66EE74B1EDAD)

