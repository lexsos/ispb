package ispb.main;


import org.apache.commons.cli.*;

public class Main {
    public static void main( String[] args ){

        Options options = makeOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine line;

        try {
            line = parser.parse( options, args );
        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            return;
        }

        String configFile = line.getOptionValue("configfile");

        if (line.hasOption("billserver") && configFile != null){
            BillServer.run(configFile);
        }
        else if (line.hasOption("cleardb") && configFile != null){
            ClearDb.run(configFile);
        }
        else if (line.hasOption("resetadmin") && configFile != null){
            ResetAdmin.run(configFile);
        }
        else{
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ispb", options );
        }

    }

    private static Options makeOptions(){
        Options options = new Options();

        options.addOption("c", "configfile", true, "config file path");
        options.addOption("billserver", "run billing server");
        options.addOption("cleardb", "delete all data and prepare clear db");
        options.addOption("resetadmin", "reset password for admin user");

        return options;
    }
}
