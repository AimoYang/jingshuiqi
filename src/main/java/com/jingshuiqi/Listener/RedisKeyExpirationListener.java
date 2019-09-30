package com.jingshuiqi.Listener;

import com.jingshuiqi.bean.ExchangeCode;
import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.dao.ExchangeCodeMapper;
import com.jingshuiqi.dao.GoodsOrderMapper;
import groovy.util.logging.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener{

	public RedisKeyExpirationListener(
			RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Autowired
	private GoodsOrderMapper goodsOrderMapper;
	@Autowired
	private ExchangeCodeMapper exchangeCodeMapper;
	
	/**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
	@Override
	public void onMessage(Message message, byte[] pattern) {
	     // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
	     String expiredKey = message.toString();
	     System.out.println("-------------------过期啦"+expiredKey);   
	     if(expiredKey.endsWith("_order")){
	          //取到业务数据进行处理
	    	 String uuid = expiredKey.substring(0,expiredKey.indexOf("_"));
	    	 System.out.println(uuid);
	    	 GoodsOrder orders = goodsOrderMapper.findOrdersByUuid(uuid);
	    	 System.out.println(orders);
	    	 if (orders != null){
				 if (orders.getOrderStatus() == 0) {
				 	if (orders.getOrderType() == 1){
				 		if (orders.getCode() != null){
							exchangeCodeMapper.updateReCode(orders.getCode());
						}
					}
					 orders.setOrderStatus(5);
					 goodsOrderMapper.updateByPrimaryKeySelective(orders);
				 }
			 }
	     }
	  }
	
}
