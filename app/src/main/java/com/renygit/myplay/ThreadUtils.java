package com.renygit.myplay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2017/7/6.
 * 参看http://www.cnblogs.com/whoislcj/p/5607734.html
 */

public class ThreadUtils {

    private ExecutorService executorService;

    private ThreadUtils(){
        //缓存线程池大小是不定值，可以需要创建不同数量的线程，
        // 在使用缓存型池时，先查看池中有没有以前创建的线程，
        // 如果有，就复用.如果没有，就新建新的线程加入池中，
        // 这种缓存型池子通常用于执行一些生存期很短的异步型任务
        executorService = Executors.newCachedThreadPool();
    }

    private static class SingletonHolder {
        private static final ThreadUtils INSTANCE = new ThreadUtils();
    }

    public static ThreadUtils self() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Runnable runnable){
        executorService.execute(runnable);
    }

    public void shutdown(){
        if(!self().executorService.isShutdown()){
            self().executorService.shutdown();
        }
    }

}
