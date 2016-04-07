package org.apache.storm.xhj_segmenter;

import java.io.File;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class HtmlReader extends BaseRichSpout {

	// 文件是否读完
	private boolean					completed	= false;
	private File					file;
	private SpoutOutputCollector	collector;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.file = new File(conf.get("file_reader_name").toString());
		this.collector = collector;
	}

	// 解析出 html 中的内容
	public void nextTuple() {
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return;
		}
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "http://www.qingfan.com/zh/node/11469");
			// 标题
			Element title = doc.select("h1[class=title]").first();
			this.collector.emit(new Values(title.text()));

			// 摘要
			Element abstract_t = doc.select("div[class=summary]").first();
			this.collector.emit(new Values(abstract_t.text()));

			// p 标签（段落）里面的内容，大部分均为中文，所以输出它的内容
			Elements p_tag = doc.getElementsByTag("p");
			for (Element e : p_tag) {
				this.collector.emit(new Values(e.text()));
			}

		} catch (Exception e) {
			throw new RuntimeException("解析 html 出错 －", e);
		} finally {
			completed = true;
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tag_context"));
	}

}
