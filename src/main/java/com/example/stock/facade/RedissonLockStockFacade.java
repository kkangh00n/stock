package com.example.stock.facade;

import com.example.stock.service.StockService;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedissonLockStockFacade {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity){
        RLock lock = redissonClient.getLock(id.toString());

        try{
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);

            if(!available){
                log.info("Lock 획득 실패");
                return;
            }

            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
