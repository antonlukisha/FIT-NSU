package csvparser.converter;

public class converterCSV
{
    public converterCSV(String[] args, String name)
    {
        parserCLI parser = new parserCLI(args);
        wordHandler handler = new wordHandler(parser.getTXTName());
        writerCSV writer = new writerCSV(parser.getCSVName());
        writer.writeWithStatistic(handler.getCountWords(), handler.getData());
    }
}
