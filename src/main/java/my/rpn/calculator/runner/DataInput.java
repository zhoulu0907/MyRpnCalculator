package my.rpn.calculator.runner;

import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import my.rpn.calculator.constant.CommandConstant;
import my.rpn.calculator.utils.IgniteUtils;

@Component
@Order(2)
public class DataInput implements CommandLineRunner {
	
	@Resource
	private IgniteUtils igniteUtils;

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		Ignite ignite = igniteUtils.getInstance();
		IgniteQueue<String> wordQueue = ignite.queue("WordInput", 0, new CollectionConfiguration());
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Scanner sc = new Scanner(System.in);
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] words = line.split("\\s+");
					for (String word : words) {
						//TBD异常校验
						if (word.equals(" ")) {
							continue;
						}
						if (word.toLowerCase().equals(CommandConstant.EXIT)) {
							sc.close();
							ignite.close();
							System.exit(0);
						}
						if (word.toLowerCase().equals(CommandConstant.SQRT)) {
							word = "~";
						}
						if (word.toLowerCase().equals(CommandConstant.UNDO)) {
							word = "<";
						}
						wordQueue.add(word);
					}
					wordQueue.add("end");
				}
			}
			
		}).start();
	}

}
