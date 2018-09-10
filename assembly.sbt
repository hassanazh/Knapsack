enablePlugins(AssemblyPlugin)

assemblyJarName in assembly := name.value + ".jar"
mainClass       in assembly := Some("com.knapsack.KnapsackApp")
