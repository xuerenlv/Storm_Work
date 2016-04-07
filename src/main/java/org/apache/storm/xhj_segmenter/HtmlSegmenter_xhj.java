package org.apache.storm.xhj_segmenter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class HtmlSegmenter_xhj {

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("html-reader", new HtmlReader());
		builder.setBolt("html-text-normalizer", new HtmlTextNormalizer()).shuffleGrouping("html-reader");
		builder.setBolt("html-text-segmenter", new HtmlTextSegmenter()).shuffleGrouping("html-text-normalizer");

		Config conf = new Config();
		conf.put("file_reader_name", "src/main/resources/11469.html");
		conf.setDebug(false);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("HtmlSegmenter_xhj", conf, builder.createTopology());
		Utils.sleep(30000);
		cluster.killTopology("HtmlSegmenter_xhj");
		cluster.shutdown();
	}

}
