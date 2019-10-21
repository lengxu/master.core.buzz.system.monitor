package cn.uyun.service.schedule;

import cn.uyun.service.product.ProductServiceAbs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 吴晗 on 2017/9/20.
 */
@Component
public class Client {
	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
	private static ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

	@Autowired
	private ProductServiceAbs productServiceAbs;

	@Scheduled(cron = "0 */2 * * * ?")
	public void queryDataProcess() {
		LOGGER.debug("开始获取数据全流程的数据!");
		try {
			newCachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					long startTime = System.currentTimeMillis();
					productServiceAbs.queryProductData("Cre");
					productServiceAbs.queryProductData("CO");
					productServiceAbs.queryProductData("DI");
					long endTime = System.currentTimeMillis();
					LOGGER.info("处理数据全流程数据共耗时：" + (endTime - startTime) + " 毫秒");
				}
			});
		} catch (Exception e) {
			LOGGER.error("获取数据全流程数据失败！", e);
		}
	}
}
