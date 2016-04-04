package org.apache.storm.xhj_wordcount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader extends BaseRichSpout {

	// 文件是否读完
	private boolean					completed	= false;
	private TopologyContext			context;
	private FileReader				fileReader;
	private SpoutOutputCollector	collector;

	// 配置对象，在定义topology对象是创建；TopologyContext对象，包含所有拓扑数据；
	// 还有SpoutOutputCollector对象，它能让我们发布交给bolts处理的数据。
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			this.context = context;
			this.fileReader = new FileReader(conf.get("file_reader_name").toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error[" + conf.get("file_reader_name").toString() + "]");
		}
		this.collector = collector;
	}

	// 通过它向bolts发布待处理的数据。
	public void nextTuple() {
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return;
		}

		try {
			BufferedReader reader = new BufferedReader(fileReader);
			String str;
			while ((str = reader.readLine()) != null) {
				this.collector.emit(new Values(str));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple", e);
		} finally {
			completed = true;
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	public boolean isDistributed() {
		return false;
	}

	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}

	public void close() {
	}

	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}
}
