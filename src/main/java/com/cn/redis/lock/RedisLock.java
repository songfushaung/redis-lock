package com.cn.redis.lock;

import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * 
 * @classDesc: 功能描述:redis分布式锁
 * @author: 宋付双
 * @createTime: 2018年10月14日
 * @version: v1.0
 * @copyright:
 */
public class RedisLock {
    
		private JedisPool jedisPool;
		public RedisLock(JedisPool jedisPool) {
			this.jedisPool=jedisPool;
		}
		//锁的名字
		private static final String LOCKNAME="RedisLock";
		/**
		 * 
			 * 
			 * @methodDesc: 功能描述:获取锁
			 * @author: 宋付双
			 * @param: 
			 * @createTime:2018年10月14日
			 * @returnType:@param 
			 * @copyright:
			 * acquireTimeout:获取锁超时时间(毫秒)从当前时间起acquireTimeout时间后还没有得到锁放弃
			 * timeOut:上锁后key的失效时间(毫秒)
		 */
		public String getRedisLock(Long acquireTimeout, Long timeOut){
		   Jedis conn = null;
		   try {
		   //连接redis
		   conn = jedisPool.getResource();
		   //设置redis的key为LOCKNAME,value为不唯一的值即可为uuid(为什么要不唯一)保证在释放锁时候是自己的锁
		   String keyvalue = UUID.randomUUID().toString();
		   //设置俩个超时时间,是key的失效时间防止死锁,还有就是获取锁超时时间,规定时间得不到锁,就放弃
		     // 上锁成功之后,锁的超时时间
			int expireLock = (int) (timeOut / 1000);
			 //在没有获取锁之前,获取锁的超时时间
			Long endTime = System.currentTimeMillis() + acquireTimeout;
		   //使用while循环,来尝试获取锁
			while(System.currentTimeMillis()<endTime){
				//获取到锁
				//setnx设置值
				if (conn.setnx(LOCKNAME, keyvalue) == 1) {
					//判断返回结果如果为1,成功获取锁,并且设置锁的超时时间
					conn.expire(LOCKNAME, expireLock);
					return keyvalue;
				}
			}
		   } catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		  return null;
		   
		}
	
		/**
		 * 
			 * 
			 * @methodDesc: 功能描述:释放锁
			 * @author: 宋付双
			 * @param: 
		 * @return 
			 * @createTime:2018年10月14日
			 * @returnType:@param 
			 * @copyright:
			 * 有俩种:key超时和当前锁对应的程序执行完删除
		 */
		public boolean closeRedisLock(String keyvalue){
			Jedis conn = null;
			boolean flag = false;
			try {
				//建立redis连接
				conn = jedisPool.getResource();
				//如果value与redis中一致直接删除
				if (keyvalue.equals(conn.get(LOCKNAME))) {
					conn.del(LOCKNAME);
					System.out.println(keyvalue + "解锁成功......");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
			return flag;
		}
}

