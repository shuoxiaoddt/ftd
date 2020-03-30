package com.xs.middle.compent.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.scheduling.annotation.Schedules;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author xiaos
 * @date 25/03/2020 16:03
 */
public class HelloRxjava {
    public static void main(String[] args) throws InterruptedException {
        //create();
        //just();
        //fromCallable();
        //fromFuture();
        //timer();
        //interval();
        //map();
        //flatMap();
        //buffer();
        //group();
        //scan();
        async();
        Thread.sleep(110000);
    }
    public static void hello(String... args) {
        Flowable.fromArray(args).subscribe(x -> {
            System.out.println(x);
        });
    }
    public static void create(){
        Observable.create((ObservableEmitter<Integer> e) -> {
            System.out.println("=========================currentThread name: "+Thread.currentThread());
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onComplete();
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("======================onSubscribe");
            }

            @Override
            public void onNext(Integer o) {
                System.out.println("======================onNext");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("======================onError");

            }
            @Override
            public void onComplete() {
                System.out.println("======================onComplete");
            }
        });
    }

    public static void just(){
        Observable.just(1,2,3,5,6,7).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
    }

    public static void fromArray(){
        Observable.fromArray(1,2,4).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
    }

    public static void fromCallable(){
        Observable.fromCallable( () ->{
            System.out.println(Thread.currentThread().getName());
            return "1";
        }).subscribe( x -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(x);
        });
    }

    public static void fromFuture(){
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return "返回结果";
        });
        Observable
                .fromFuture(futureTask)
                .doOnSubscribe(x -> {
                    futureTask.run();
                    System.out.println("doOnSubscribe :"+Thread.currentThread().getName());
                    System.out.println("doOnSubscribe :  "+x);
        })
                .subscribe(x ->{
                    System.out.println("subscribe:"+Thread.currentThread().getName());
                    System.out.println("subscribe :  "+x);
        });
    }

    public static void timer(){
        Observable
                .timer(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.single() )
                .subscribe(new Observer < Long > () {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println( Thread.currentThread().getName()+"===============onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static void interval(){
        Observable.interval(4,TimeUnit.SECONDS).subscribe(x -> {
            System.out.println(x);
        });
    }

    public static void map(){
        Observable.just(1,2,3).map(x -> x+1).subscribe(x -> {
            System.out.println(x);
        });
    }

    public static void flatMap(){
        Observable.just(new Person("hello",new ArrayList<>()),new Person("world",new ArrayList()))
                .flatMap(x -> {
                    return Observable.fromIterable(x.getPlanList());
                }).subscribe(x -> {
            System.out.println(x.getActionList());;
        });
    }
    public static void buffer(){
        Observable.just(1,2,3,4,6).buffer(2,1).subscribe(x -> {
            System.out.println(x);
        });
    }

    public static void group(){
        Observable.just(1,2,3,4,5,6,7,8).groupBy(x -> x %3).subscribe(x -> {
            x.subscribe(y -> {
                System.out.println("current:"+Thread.currentThread().getName()+" key : " +x.getKey()+" ->value : " + y.intValue());
            });
        });
    }
    public static void scan(){
        Observable.just(1,2,3,4,5)
                .observeOn(Schedulers.single())
                .scan((a,b) -> {
                    System.out.println("a + b : "+Thread.currentThread().getName());
                    return a + b;
                })
                .subscribe(x -> {
                    System.out.println("x : "+Thread.currentThread().getName());
                });
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void async(){
        Observable.just(1)
                .map(x -> {
                    System.out.println("map1:"+Thread.currentThread().getName());
                    return x;
                })
                .subscribeOn(Schedulers.newThread())
                .map(x -> {
                    System.out.println("map2:"+Thread.currentThread().getName());
                    return x;
                })
                .observeOn(Schedulers.io())
                .map(x -> {
                    System.out.println("map3:"+Thread.currentThread().getName());
                    return x;
                })
                .observeOn(Schedulers.single())
                .subscribe(x -> {
                    System.out.println("subscribe:"+Thread.currentThread().getName());
                });
    }
}
