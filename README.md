#Goose - Article Extractor


##Intro

Goose was originally an article extractor written in Java that has been 
converted to a Scala project. Its mission is to take a news article
or article-type web page and extract the main body of the article, all
metadata, and most probable image candidate.

The extraction goal is the purest extraction from the beginning of the 
article for servicing flipboard/pulse type applications that need to 
show the first snippet of a web article along with an image.

Goose will try to extract the following information:

 - Main text of an article
 - Main image of article
 - Any YouTube/Vimeo movies embedded in article
 - Meta Description
 - Meta tags
 - Publish Date


The wiki has the full details on how to use Goose 
[https://github.com/jiminoc/goose/wiki](https://github.com/jiminoc/goose/wiki)

Goose was open sourced by Gravity.com in 2011

Lead Programmer: Jim Plush (Gravity.com)

Contributors: Robbie Coleman (Gravity.com)


[Try it out online!](http://jimplush.com/blog/goose)


##Licensing

If you find Goose useful or have issues, please drop me a line, I'd love 
to hear how you're using it or what features should be improved.

Goose is licensed by Gravity.com under the Apache 2.0 license, see the 
LICENSE file for more details.


##Take it for a spin

To use goose from the command line:

    cd into the goose directory
    mvn compile
    MAVEN_OPTS="-Xms256m -Xmx2000m"; mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/" -e -q > ~/Desktop/gooseresult.txt


##Regarding the port from Java to Scala

Here are some of the reasons for the port to Scala:

 - Gravity has moved more towards Scala development internally so 
   maintenance started to become an issue
 - There wasn't enough contribution to warrant keeping it in Java
 - The packages were all namespaced under a person's name and not the 
   company's name
 - Scala is more fun


##Issues

The Java to Scala port was done quickly, so many niceties of the 
Scala language aren't in the codebase yet, but those will come over the 
coming months as we re-write alot of the internal methods to be more 
Scala-esque.

We made sure it was still nice and operable from Java as well, so you
should still be able to use goose from java with a few changes to the
method signatures.
