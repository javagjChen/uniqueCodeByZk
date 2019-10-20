package com.zk.uniqueid3.demo.controller;

import com.zk.uniqueid3.demo.service.ZkUniqueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: chengj
 * Date: 2019-10-20
 * Description: No Description
 */
@RestController
@Slf4j
public class DemoController {

    @Autowired
    private ZkUniqueService zkUniqueService;


    @RequestMapping("/makeUniqueId")
    public String makeUniqueId(){
        //并发线程数
        int count = 20;
        //循环屏障
        CyclicBarrier cb  = new CyclicBarrier(count);
        //模拟高并发场景，多线程，创建订单
        for(int i=0; i<count; i++){
            new Thread(() -> {
                log.info(Thread.currentThread().getName()+"--我已经准备好了");
                try {
                    //等待所有线程启动准备好，才一起往下执行
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                //创建订单
                String uniqueId = zkUniqueService.getSerialNumber("HD");
                System.out.println(uniqueId);
            }).start();
        }
        return "ok";
    }
}
