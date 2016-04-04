package org.apache.storm.xhj_wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class WordCount_xhj {

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter()).shuffleGrouping("word-normalizer");

		Config conf = new Config();
		conf.put("file_reader_name", "src/main/resources/resours_file.txt");
		conf.setDebug(false);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("WordCount_xhj", conf, builder.createTopology());
		Utils.sleep(30000);
		cluster.killTopology("WordCount_xhj");
		cluster.shutdown();
	}

}
