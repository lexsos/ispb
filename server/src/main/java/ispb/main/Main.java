package ispb.main;


import ispb.main.command.BackwardsDailyPayment;
import ispb.main.command.BillServer;
import ispb.main.command.ClearDb;
import ispb.main.command.ResetAdmin;
import org.apache.commons.cli.*;

public class Main {
    public static void main( String[] args ){

        Options options = makeOptions();
        CommandLine line;

        try {
            line = parse(options, args);
        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            return;
        }

        analyze(line, options);
    }

    private static Options makeOptions(){
        Options options = new Options();

        options.addOption("c", "configfile", true, "config file path");
        options.addOption("billserver", "run billing server");
        options.addOption("cleardb", "delete all data and prepare clear db");
        options.addOption("resetadmin", "reset password for admin user");
        options.addOption("dailyPayment", "Add auto daily payment for specified date");

        return options;
    }

    private static CommandLine parse(Options options,  String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse( options, args );
    }

    private static void analyze(CommandLine line, Options options){
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
        else if (line.hasOption("dailyPayment") && configFile != null){
            BackwardsDailyPayment.run(configFile);
        }
        else{
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ispb", options );
        }
    }
}
