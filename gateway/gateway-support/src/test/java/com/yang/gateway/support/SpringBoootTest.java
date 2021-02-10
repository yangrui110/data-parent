package com.yang.gateway.support;

import com.yang.gateway.support.feign.ServiceInfoApi;
import com.yang.system.client.entity.ServiceInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBoootTest {

    @Autowired
    private ServiceInfoApi serviceInfoApi;

    @Test
    public void test1(){
        List<ServiceInfo> serviceInfos = serviceInfoApi.listServiceInfos();
        System.out.println(serviceInfos);
    }

}
