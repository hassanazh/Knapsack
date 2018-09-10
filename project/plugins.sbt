resolvers += Resolver.url("bintray-sbt-plugin-releases",url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"        % "0.14.6")
addSbtPlugin("io.spray"           % "sbt-revolver"        % "0.9.1")