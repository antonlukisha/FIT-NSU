package csvparser;

import csvparser.converter.converterCSV;

public class CSVParser
{

    public static void main(String[] args)
    {
        converterCSV converter = new converterCSV(args, "TestCSV");
    }
    
}
