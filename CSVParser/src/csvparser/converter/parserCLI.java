package csvparser.converter;

import java.io.IOException;

public class parserCLI
{
    private final String txtName;
    private final String csvName;
    private final int countArgs;
    private final String[] args;
    
    public parserCLI(String[] args)
    {
        this.countArgs = args.length;
        this.txtName = getCLIData(1, "txt");
        this.csvName = getCLIData(2, "csv");
        this.args = args;
    }
    
    private void checkArgsNum() throws InputErrorException
    {
	if(this.countArgs != 3)
	{
            throw new InputErrorException();
	}
    }
    
    private String getCLIData(int idxArgs, String type)
    {
	try
	{
            checkArgsNum();
            checkFileType(idxArgs, type);
            return args[idxArgs];
	}
	catch(IOException exception)
	{
            System.err.println(exception.getMessage());
	}
        finally
        {
            return "default." + type;
        }
    }

    private void checkFileType(int idxArgs, String type) throws IOException
    {
        int idx;
	String fileName = args[idxArgs];

	if((idx = fileName.indexOf(".")) == -1)
	{
            throw new InputErrorException();
	}

	if(!fileName.substring(idx).equals(type))
	{
            throw new InputErrorException();
	}
    }
    
    private class InputErrorException extends IOException
    {
        public InputErrorException()
        {
            super("Warning: Incomplete Input");
        }
    }
    
    public String getTXTName()
    {
        return this.txtName;
    }
    
    public String getCSVName()
    {
        return this.csvName;
    }
}
