package org.apache.storm.xhj_classfier;

import java.util.Map;

import com.huaban.analysis.jieba.JiebaSegmenter;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HtmlTextSegmenter extends BaseBasicBolt {

	private JiebaSegmenter segmenter;

	// bolt 的初始化，加载分词器
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.segmenter = new JiebaSegmenter();
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		// // 分词，并输出结果到控制台
		// System.out.println(segmenter.sentenceProcess(sentence));
		collector.emit(new Values(sentence));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("segmented-text"));
	}

}
