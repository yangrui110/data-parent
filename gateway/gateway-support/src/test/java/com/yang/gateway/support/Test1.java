package com.yang.gateway.support;

import reactor.core.publisher.Flux;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
public class Test1 {
    public static void main(String[] args) {
        Flux<String> just = Flux.just("1", "2");
        just.subscribe(
                i-> System.out.println(i),
                error-> System.out.println(error),
                ()-> System.out.println("已完成"),
                t->t.request(10));

        just.subscribe(
                i-> System.out.println(i),
                error-> System.out.println(error),
                ()-> System.out.println("已完成2"),
                t->t.request(1));
    }
}
