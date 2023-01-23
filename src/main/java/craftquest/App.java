package craftquest;

import java.util.Collections;

import io.github.cdimascio.dotenv.Dotenv;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class App extends ListenerAdapter {
    public static void main( String[] args ) {

        // Token
        Dotenv de = Dotenv.load();
        String token = de.get("BOT_TOKEN");

        // Building bot
        JDA jda = JDABuilder.createDefault(token, Collections.emptyList())
                            .addEventListeners(new GeneralCommands())
                            .setActivity(Activity.playing("CraftQuest"))
                            .build();

        // Registering commands
        jda.updateCommands().addCommands(
            Commands.slash("ping", "Calculates bot's ping"),
            Commands.slash("roll", "Rolls a dice")
                .addOption(INTEGER, "bound", "The upper boundary of the roll (Defaults to 6)"),
            Commands.slash("jar", "Adds or removes from the bean jar")
                .addOption(INTEGER, "beans", "How many beans you would like to add (Defaults to 1, upper bound is 10)"),
            Commands.slash("echo", "Repeats what you say")
                .addOption(STRING, "text", "What you want the bot to repeat"),
            Commands.slash("embed", "Sample embed to test visuals")
        ).queue();
    }

}
