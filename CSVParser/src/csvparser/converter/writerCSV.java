package csvparser.converter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class writerCSV
{
    
    private final String path;
    
    public writerCSV(String name)
    {
        this.path = "C://Users/Антон/Documents/NetBeansProjects/CSVParser/src/csvparser/sourse/" + name;
    }
    
    public void writeWithStatistic(int countWords, Map<String, Integer> data)
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(this.path)))
        {
            LinkedHashMap<String, Integer> sortedData = data.entrySet()
                    .stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors
                    .toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            NumberFormat template = new DecimalFormat("#0.0");
            for (Map.Entry<String, Integer> entry : sortedData.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();
                String ratio = template.format(((value * 1.0) / countWords) * 100);
                int length = key.length() + String.valueOf(value).length() + ratio.length() + 4;
                writer.write((key + ";" + String.valueOf(value) + ";" + ratio + "%\n"), 0, length);
            }
        }
        catch (IOException exception)
        {
            System.err.println(exception.toString()); 
        }
    }
}
