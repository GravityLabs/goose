package com.gravity.goose

import java.io._
import scala.io.Source

object TalkToMeGoose {
	/**
	 * Run Goose on a file containing a list of URLs.
	 * mvn compile
	 * mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="path_to_url_file path_to_extract_to_dir"
	 */

	def main(args: Array[String]) {
		val url_file: String = args(0)
		val extracted: String = args(1)
		val config: Configuration = new Configuration
		config.enableImageFetching = false
		val goose = new Goose(config)

		for (url <- Source.fromFile(url_file).getLines()) {
			try {
				val article = goose.extractContent(url)
				val filename = extracted + "/" + url.replace("http://", "").replace("https://", "").replace("/", "_") + "_" + System.currentTimeMillis
				val writer = new PrintWriter(new File(filename))
				writer.write(article.cleanedArticleText)
				writer.close()
			} catch {
				case e: Exception => {
					System.out.println(url + " is not a valid URL: " + e.toString)
				}
			}
		}
	}
}
