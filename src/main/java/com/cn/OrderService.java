package com.cn;

import com.cn.redis.lock.RedisLock;
import com.cn.redis.redis.RedisPool;

import redis.clients.jedis.JedisPool;
/***
 * 基本锁实现数据一致性
 * @author Administrator
 *
 */
public class OrderService implements Runnable{
	OrderNumGenerator gender=new OrderNumGenerator();
	JedisPool jedisPool=new RedisPool().getRedisPool();
	RedisLock lock=new RedisLock(jedisPool);
	@Override
	public void run() {
		String keyvalue=null;
		try {
			keyvalue=lock.getRedisLock(5000l, 5000l);
			if (keyvalue==null) {
				// 获取锁失败
				System.out.println(Thread.currentThread().getName() + ",获取锁失败，原因时间超时!!!");
				return;
			}
			System.out.println(Thread.currentThread().getName() + "获取锁成功,锁id:" + keyvalue + "，执行业务逻辑");
			Thread.sleep(30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			boolean releaseLock =lock.closeRedisLock(keyvalue);
			if (releaseLock) {
				System.out.println(Thread.currentThread().getName() + "释放锁成功,锁id:" + keyvalue);
			}
		}
	}
	/**
	 * 多线程生成订单号
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0;i<50;i++){
			new Thread(new OrderService()).start();
		}
	}

}
