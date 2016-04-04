package org.apache.storm.xhj_wordcount;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordNormalizer extends BaseBasicBolt {

	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) != ' ' && sentence.charAt(i) != '\t') {
				collector.emit(new Values(new Character(sentence.charAt(i)).toString()));
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
