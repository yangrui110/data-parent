package com.yang.gateway.support.config;

import com.yang.gateway.support.feign.ServiceInfoApi;
import com.yang.system.client.entity.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Component
public class DynamicRedisRoute implements RouteDefinitionRepository {

    @Autowired
    private ServiceInfoApi serviceInfoApi;

    private List<RouteDefinition> routeDefinitions = new ArrayList();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        if(routeDefinitions.size()==0){
            RouteDefinition routeDefinition = makeDefinition("/system/**", "lb://system");
            return Flux.just(routeDefinition);
        }
        if(serviceInfoApi==null) {
            return Flux.empty();
        }
        List<ServiceInfo> serviceInfos = serviceInfoApi.listServiceInfos();
        ArrayList<RouteDefinition> definitions = new ArrayList<>();
        if(serviceInfos==null) {
            return Flux.fromIterable(definitions);
        }
        for(ServiceInfo serviceInfo: serviceInfos){
            RouteDefinition definition = makeDefinition(serviceInfo.getPath(), serviceInfo.getUri());
            definitions.add(definition);
        }
        // 给值赋值，下次不会走这个逻辑
        routeDefinitions = definitions;
        return Flux.fromIterable(definitions);
    }

    private RouteDefinition makeDefinition(String path,String uri){
        RouteDefinition definition = new RouteDefinition();
        definition.setUri(URI.create(uri));
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        HashMap<String, String> map = new HashMap<>();
        map.put("pattern",path);
        predicateDefinition.setArgs(map);
        ArrayList<PredicateDefinition> arrayList = new ArrayList<>();
        arrayList.add(predicateDefinition);
        definition.setPredicates(arrayList);
        return definition;
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
