package com.mall4j.cloud.common.feign;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * dev的时候，如果本地起了服务，着负载均衡到本地的服务，免得开发的时候找不到日志
 *
 * @author FrozenWatermelon
 * @date 2021/3/4
 */
@Profile("dev")
@Component
public class DevRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(RoundRobinLoadBalancer.class);


    Environment environment;

    LoadBalancerClientFactory loadBalancerClientFactory;

    final AtomicInteger position;

    public String hostAddress;


    public DevRoundRobinLoadBalancer(Environment environment,
                                     LoadBalancerClientFactory loadBalancerClientFactory, InetUtils inetUtils) {
        this.environment = environment;
        this.loadBalancerClientFactory = loadBalancerClientFactory;
        this.position = new AtomicInteger(new Random().nextInt(1000));
        hostAddress = inetUtils.findFirstNonLoopbackAddress().getHostAddress();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        String name = ((RequestDataContext) request.getContext()).getClientRequest().getUrl().getHost();

        ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider = loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class);


        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, name));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances, String serviceId) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, serviceId);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String serviceId) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        int pos = Math.abs(this.position.incrementAndGet());

        for (ServiceInstance instance : instances) {
            if (Objects.equals(hostAddress, instance.getHost())) {
                return new DefaultResponse(instance);
            }
        }


        ServiceInstance instance = instances.get(pos % instances.size());

        return new DefaultResponse(instance);
    }

}
