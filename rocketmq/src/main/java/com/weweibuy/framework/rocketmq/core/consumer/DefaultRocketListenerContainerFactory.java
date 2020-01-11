package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.RPCHook;

import java.util.List;

/**
 * RocketListenerContainerFactory 默认实现
 *
 * @author durenhao
 * @date 2020/1/11 22:05
 **/
public class DefaultRocketListenerContainerFactory implements RocketListenerContainerFactory {


    @Override
    public RocketListenerContainer createListenerContainer(List<MethodRocketListenerEndpoint> endpointList) {

        boolean orderly = endpointList.get(0).isOrderly();

        if (orderly) {
            return createOrderlyContainer(endpointList);
        }
        return createConcurrentlyContainer(endpointList);

    }


    private RocketListenerContainer createOrderlyContainer(List<MethodRocketListenerEndpoint> endpointList) {
        if (endpointList.size() == 1) {
            MethodRocketListenerEndpoint endpoint = endpointList.get(0);
            return new OrderlyRocketListenerContainer(createMqConsumer(endpoint, endpoint.getTags()));
        }
        String tags = mergeTags(endpointList);
        return new OrderlyRocketListenerContainer(createMqConsumer(endpointList.get(0), tags));
    }


    private RocketListenerContainer createConcurrentlyContainer(List<MethodRocketListenerEndpoint> endpointList) {
        if (endpointList.size() == 1) {
            MethodRocketListenerEndpoint endpoint = endpointList.get(0);
            return new ConcurrentlyRocketListenerContainer(createMqConsumer(endpoint, endpoint.getTags()));
        }
        String tags = mergeTags(endpointList);
        return new ConcurrentlyRocketListenerContainer(createMqConsumer(endpointList.get(0), tags));
    }

    private DefaultMQPushConsumer createMqConsumer(MethodRocketListenerEndpoint endpoint, String tags) {
        RPCHook rpcHook = null;
//        if (StringUtils.isBlank(endpoint.getAccessKey()) || StringUtils.isBlank(endpoint.getSecretKey())) {
//            rpcHook =  new AclClientRPCHook(new SessionCredentials(ak, sk));
//        }
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer(endpoint.getGroup(), rpcHook, new AllocateMessageQueueAveragely(),
                false, null);
        pushConsumer.setNamesrvAddr("");
        pushConsumer.setAccessChannel(AccessChannel.valueOf(endpoint.getAccessChannel()));
        pushConsumer.setConsumeThreadMin(endpoint.getThreadMin());
        pushConsumer.setConsumeThreadMin(endpoint.getThreadMax());
        pushConsumer.setConsumeTimeout(endpoint.getTimeout());
        try {
            pushConsumer.subscribe(endpoint.getTopic(), tags);
        } catch (MQClientException e) {
            throw new IllegalArgumentException(e);
        }
        return pushConsumer;
    }


    private String mergeTags(List<MethodRocketListenerEndpoint> endpointList) {
        return endpointList.stream()
                .map(MethodRocketListenerEndpoint::getTags)
                .reduce((a, b) -> a + "||" + b + "||")
                .map(s -> s.substring(0, s.length() - 2))
                .orElseThrow(() -> new IllegalArgumentException("Tag 错误"));
    }


}
