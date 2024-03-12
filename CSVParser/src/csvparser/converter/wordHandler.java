package csvparser.converter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import static java.lang.Character.isLetterOrDigit;
import java.util.HashMap;
import java.util.Map;

public class wordHandler
{
    private final String path;
    private int countWords;
    private final Map<String, Integer> data;
    
    public wordHandler(String name)
    {
        this.path = "C://Users/Антон/Documents/NetBeansProjects/CSVParser/src/csvparser/sourse/" + name;
        this.countWords = 0;
        this.data = new HashMap<>();
        countWordsOfFile();
    }
    
    private void countWordsOfFile()
    {
        try (Reader reader = new InputStreamReader(new FileInputStream(this.path));)
        {
            int symb, length = 0;
            StringBuilder stream = new StringBuilder();
            while((symb = reader.read())!= -1)
            {
                if (isLetterOrDigit((char)symb) == true)
                {
                    stream.append((char)symb);
                    length++;
                }
                else
                {
                    if(length != 0)
                    {
                        data.put(stream.toString(), (data.getOrDefault(stream.toString(), 0) + 1));
                        stream = new StringBuilder();
                        countWords++;
                        length = 0;
                    }
                }
            }
        }
        catch (IOException exception)
        {
            System.err.println(exception.toString()); 
        }
    }
    
    public Map<String, Integer> getData()
    {
        return this.data;
    }
    
    public int getCountWords()
    {
        return this.countWords;
    }
}
