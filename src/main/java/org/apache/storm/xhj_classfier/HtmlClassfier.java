package org.apache.storm.xhj_classfier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HtmlClassfier extends BaseBasicBolt {

	HashMap<String, HashMap<String, String>> tf_idf_map = new HashMap<String, HashMap<String, String>>();

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
		File file = new File("src/main/resources/class_word_tf_idf_value.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String str;
			while ((str = reader.readLine()) != null) {
				String[] arr = str.split(":");
				if (!tf_idf_map.containsKey(arr[0])) {
					tf_idf_map.put(arr[0], new HashMap<String, String>());
				}
				tf_idf_map.get(arr[0]).put(arr[1], arr[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		HashMap<String, Float> probility = new HashMap<String, Float>();

		collector.emit(new Values(probility.get("0").toString()));

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
