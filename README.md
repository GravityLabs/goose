#Goose - Article Extractor

[Goose](https://github.com/GravityLabs/goose) fork published on Maven Central.

## This is a fork

If you haven't guessed already, this is a fork of the wonderful [Goose library](http://github.com/GravityLabs/goose) by Gravity Labs. The original repo hasn't been updated for 2 years now, and there have been quite [a few nice pull requests](https://github.com/GravityLabs/goose/pulls) that are lying dormant.

The project now uses sbt, and is hosted on Sonatype. Add the following to to your `build.sbt` to pull it in:

```
libraryDependencies ++= Seq("com.gravity" %% "goose" % "2.1.25-SNAPSHOT")

resolvers += Resolver.sonatypeRepo("public")
```

##Intro

Goose was originally an article extractor written in Java that has most recently (aug2011) converted to a Scala project. It's mission is to take any news article or article type web page and not only extract what is the main body of the article but also all meta data and most probable image candidate.

The extraction goal is to try and get the purest extraction from the beginning of the article for servicing flipboard/pulse type applications that need to show the first snippet of a web article along with an image.

Goose will try to extract the following information:

 - Main text of an article
 - Main image of article
 - Any Youtube/Vimeo movies embedded in article
 - Meta Description
 - Meta tags
 - Publish Date


The wiki has the full details on how to use Goose [https://github.com/jiminoc/goose/wiki](https://github.com/jiminoc/goose/wiki)

Goose was open sourced by Gravity.com in 2011

Lead Programmer: Jim Plush (Gravity.com)

Contributers: Robbie Coleman (Gravity.com)


Try it out online!
http://jimplush.com/blog/goose


##Licensing
If you find Goose useful or have issues please drop me a line, I'd love to hear how you're using it or what features should be improved

Goose is licensed by Gravity.com under the Apache 2.0 license, see the LICENSE file for more details

##Environment Prerequisites

The default behaviour is by using java image processing capabilities.

### ImageMagick

You will need to have ImageMagick installed for Goose to work correctly.

On osx, you can install with brew:
        $ brew install imagemagick

Update Configuration.scala with the location of identify and convert (eg /usr/local/bin)

##Take it for a spin

### SBT
To use goose from the command line:

    cd into the goose directory
    sbt "run-main com.gravity.goose.TalkToMeGoose http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/"

### MVN
    cd into the goose directory
    mvn compile
    MAVEN_OPTS="-Xms256m -Xmx2000m"; mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/" -e -q > ~/Desktop/gooseresult.txt


##Testing
To run the junit tests, kick off the sbt test target:

    sbt test

Note that there are currently problems in the tests. (8 failures in 41 tests on 2014-07-10 - raisercostin)

##Usage as a maven dependency

Last version (goose_2.10-2.2.0.jar) is hosted at http://raisercostin.googlecode.com/svn/maven2/com/gravity/goose/
Goose is hosted on Sonatype's OSS repository, https://oss.sonatype.org/content/repositories/releases/com/gravity/goose/

    <dependency>
      <groupId>com.gravity</groupId>
      <artifactId>goose</artifactId>
      <version>2.1.22</version>
    </dependency>

##Regarding the port from Java to Scala

Here are some of the reasons for the port to Scala:

 - Gravity has moved more towards Scala development internally so maintenance started to become an issue
 - There wasn't enough contribution to warrant keeping it in Java
 - The packages were all namespaced under a person's name and not the company's name
 - Scala is more fun


##Issues

It was a pretty fast Java to Scala port so lots of the nicities of the Scala language aren't in the codebase yet, but those will come over the coming months as we re-write alot of the internal methods to be more Scalesque.
We made sure it was still nice and operable from Java as well so if you're using goose from java you still should be able to use it with a few changes to the method signatures.


##Goose is now language aware

The stopword lists introduced in the [Python-Goose project](https://github.com/grangier/python-goose) have been incorporated
into Goose.

##Release

### Release with maven
- to release for scala 2.11
	    `mvn release:prepare -Prelease -DskipTests -Darguments="-DskipTests -Prelease"`
- to release for scala 2.10
	    `mvn release:prepare -Prelease -Pscala210 -DskipTests -Darguments="-DskipTests -Prelease -Pscala210"`

The release is done via bintray
- release with standard maven process at http://raisercostin.googlecode.com/svn/maven2/com/gravity/goose/
	    mvn release:prepare -Prelease -DskipTests -Darguments="-DskipTests -Prelease"
	    mvn release:perform -Prelease -DskipTests -Darguments="-DskipTests -Prelease"

- configure your ~/.m2/settings.xml as
	```
		<servers>
		  <server>
			<id>raisercostin-releases</id>
			<username>svn-user</username>
			<password>svn-pass</password>
		  </server>
		</servers>
	```
