package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService stockService;

    public OptimisticLockStockFacade(OptimisticLockStockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true){
            try {
                stockService.decrease(id, quantity);

                break;
            } catch (Exception e){
                Thread.sleep(50);
            }
        }
    }
}
