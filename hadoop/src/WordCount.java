import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        // Map phase: input key = byte offset (Object), value = one line (Text)
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            // Convert line to String, lower-case and remove punctuation
            String line = value.toString().toLowerCase();
            line = line.replaceAll("[^a-z0-9\\s]", " ");

            StringTokenizer itr = new StringTokenizer(line);
            while (itr.hasMoreTokens()) {
                String token = itr.nextToken();
                word.set(token);
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        // Reduce phase: input key = word, values = list of counts (1,1,1,...)
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

     public static void main(String[] args) throws Exception {

         if (args.length != 2) {
            System.err.println("Usage: WordCount <input> <output>");
            System.exit(2);
    }

    long startTime = System.currentTimeMillis();  // ← START TIMER

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");

    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    boolean success = job.waitForCompletion(true);
    long endTime = System.currentTimeMillis();    // ← END TIMER

    System.out.println("Job took: " + (endTime - startTime) + " ms");

    System.exit(success ? 0 : 1);
}

}
