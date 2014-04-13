
@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab('com.github.mperry:functionalgroovy-core:0.5-SNAPSHOT')
@Grab('org.functionaljava:functionaljava:3.1')

import com.github.mperry.fg.*


1.to(5).each {
    println it
}
