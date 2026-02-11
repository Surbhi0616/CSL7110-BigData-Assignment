#  CSL7110 Big Data Frameworks Assignment

## Hadoop MapReduce (Q1-Q9)

**Custom WordCount Implementation:**
-  Map: `(LongWritable, Text)` → `(Text, IntWritable)`
-  Reduce: `(Text, Iterable<IntWritable>)` → `(Text, IntWritable)`
-  Punctuation removal: `replaceAll("[^a-z0-9\\s]", " ")`
-  Gutenberg 200.txt (~8MB) processing
-  HDFS operations + replication analysis

**Structure:**
hadoop/
├── src/WordCount.java # Complete MapReduce code
├── WordCount.jar # Compiled executable
└── demo_input.txt # Test input



**Run:**
```bash
cd hadoop
javac -classpath `hadoop classpath` -d classes src/WordCount.java
jar cf WordCount.jar *
hdfs dfs -put demo_input.txt input/
hadoop jar WordCount.jar WordCount input/ output/
hdfs dfs -cat output/part-r-00000

Apache Spark PySpark (Q10-Q12)
Implemented Features:

Q10: Metadata extraction (title/date/language) via regex

Q11: TF-IDF vectors + Cosine similarity (word counts + IDF scores)

Q12: Author influence network (temporal edge analysis)

Structure:
spark/
├── 200.txt        # Gutenberg Project book
└── spark_200.txt  # Processed copy from HDFS

Hadoop WordCount:
all     3
are     3
get     2
lucky   1
night   3

Spark TF-IDF Top Words:

the     5231
and     4321
of      3987
