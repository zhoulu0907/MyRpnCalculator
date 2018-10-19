package my.rpn.calculator.runner;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import my.rpn.calculator.constant.Constant;
import my.rpn.calculator.utils.IgniteUtils;

@Component
@Order(2)
public class DataInput implements CommandLineRunner {
	
	@Resource
	private IgniteUtils igniteUtils;
	
	private Pattern pattern = Pattern.compile(Constant.NUMBERS_PATTERN);

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
						if (word.toLowerCase().equals(Constant.EXIT)) {
							sc.close();
							ignite.close();
							System.exit(0);
						}
						if (word.toLowerCase().equals(Constant.SQRT)) {
							wordQueue.add("~");
							continue;
						}
						if (word.toLowerCase().equals(Constant.UNDO)) {
							wordQueue.add("<");
							continue;
						}
						if (word.toLowerCase().equals(Constant.CLEAR)
							|| Constant.OPERATOR.contains(word)
							|| pattern.matcher(word).matches()) {
							wordQueue.add(word);
							continue;
						}
					}
					wordQueue.add("end");
				}
			}
			
		}).start();
	}

}
