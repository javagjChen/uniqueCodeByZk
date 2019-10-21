package com.zk.uniqueid3.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: chengj
 * Date: 2019-10-20
 * Description: No Description
 */
@Slf4j
@Service
public class ZkUniqueService {

    @Autowired
    private CuratorFramework curatorFramework;


    public String getSerialNumber(String moduleCode){

    String serialNumber = "";
    try {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate date = LocalDate.now().minusDays(1);
        String dateString = date.format(formatters);
        //通过创建临时顺序节点获取序列号
        serialNumber = create(curatorFramework,moduleCode,dateString);
        serialNumber = StringUtils.substringAfterLast(serialNumber,moduleCode);



        //2.拼接流水号（前缀+日期+流水号）返回
        serialNumber = moduleCode+ dateString + serialNumber.substring(serialNumber.length() - 6);

        System.out.println(Thread.currentThread().getName() + "-获得了锁");
        System.out.println(Thread.currentThread().getName() + "-流水号为：" + serialNumber);
    } catch (Exception e) {
        e.printStackTrace();
    }finally {

    }

    return serialNumber;
}

    /**
     *
     *
     * @param dateString
     * @param moduleCode
     * @throws Exception
     */
    public String create(CuratorFramework curatorFramework, String moduleCode, String dateString) throws Exception {


        return curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/" + moduleCode + "/" + dateString + "/" + moduleCode);

    }

}
