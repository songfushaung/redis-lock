package com.cn;

import com.cn.redis.lock.RedisLock;
import com.cn.redis.redis.RedisPool;

import redis.clients.jedis.JedisPool;
class ThreadRedis extends Thread {
	private LockService lockService;

	public ThreadRedis(LockService lockService) {
		this.lockService = lockService;
	}

	@Override
	public void run() {
		lockService.seckill();

	}

}
public class Test {
	  public static void main(String[] args) {
		  JedisPool jedisPool=new RedisPool().getRedisPool();
		  RedisLock lock=new RedisLock(jedisPool);
		  String keyId=lock.getRedisLock(5000l, 5000l);
		  System.out.println(keyId);
	}
}
