package com.gravity.goose

import java.io._
import scala.collection.JavaConversions._
import scala.io.Source
import com.netflix.astyanax.AstyanaxContext
import com.netflix.astyanax.Keyspace
import com.netflix.astyanax.MutationBatch
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.connectionpool.OperationResult
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.model.Row
import com.netflix.astyanax.serializers.StringSerializer
import com.netflix.astyanax.serializers.StringSerializer
import com.netflix.astyanax.thrift.ThriftFamilyFactory
import com.netflix.astyanax.util.RangeBuilder


object TalkToMeGoose {
	/**
	 * Run Goose on a Cassandra keyspace column_family. Iterates over each key
	 * in the keyspace's column_family, uses that key as a URL to extract
	 * content from, and updates the row corresponding to that key with the
	 * content.
	 * mvn compile
	 * mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="keyspace column_family"
	 */

	def main(args: Array[String]) {
		val keyspace_name: String = args(0)
		val column_family_name: String = args(1)
		val context = new AstyanaxContext.Builder()
					.forCluster("Cluster")
					.forKeyspace(keyspace_name)
					.withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
						.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
						.setTargetCassandraVersion("1.2")
					)
					.withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
						.setPort(9160)
						.setMaxConnsPerHost(1)
						.setSeeds("127.0.0.1:9160")
					)
					.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
					.buildKeyspace(ThriftFamilyFactory.getInstance())

		context.start()

		val keyspace = context.getEntity()
		val column_family = ColumnFamily.newColumnFamily(
							column_family_name,
							StringSerializer.get(),
							StringSerializer.get())

		val url_list = keyspace.prepareQuery(column_family)
						.getAllRows()
						.withColumnRange(new RangeBuilder().setLimit(0).build())
						.execute().getResult()
		val mutation_batch = keyspace.prepareMutationBatch()

		val config: Configuration = new Configuration
		config.enableImageFetching = false
		val goose = new Goose(config)

		for (url_row <- url_list) {
			val url = url_row.getKey()

			try {
				val article = goose.extractContent(url).cleanedArticleText
				val timestamp = System.currentTimeMillis.toString
				mutation_batch.withRow(column_family, url)
					.putColumn(timestamp, article)
				mutation_batch.execute()
				System.out.println("Extracted content from " + url)
			} catch {
				case e: Exception => {
					System.out.println(url + " is not a valid URL: " + e.toString)
				}
			}
		}
	}
}
