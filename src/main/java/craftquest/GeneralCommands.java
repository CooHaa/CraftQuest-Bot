/*
 * Listener to capture all general commands mainly for testing
 * 
 */

package craftquest;

import java.awt.Color;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class GeneralCommands extends ListenerAdapter {

    static int beanJar = 0;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            // General commands
            case "ping":
                ping(event);
                break;
            case "roll":
                random(event);
                break;
            case "jar":
                jar(event);
                break;
            case "echo":
                echo(event);
                break;
            case "embed":
                embed(event);
                break;
            default:
                break;
        }
        
    }

    /**
     * Return bot ping
     * @param event
     */
    public static void ping(SlashCommandInteractionEvent event) {

        long time = System.currentTimeMillis();
        event.reply("Pong...")
            .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time))
        .queue();
    }

    /**
     * Roll a dice
     * @param event
     */
    public static void random(SlashCommandInteractionEvent event) {
        Random r = new Random();
        OptionMapping boundOpt = event.getOption("bound");
        long bound = (boundOpt == null ? 6 : boundOpt.getAsLong());
        try {
            long roll = r.nextLong(1, bound);
            event.reply(String.format("You rolled a %d!", roll)).queue();
        } 
        catch (IllegalArgumentException e) {
            event.reply("I think you put a bound that was either 0 or negative. Try again?").queue();
        }
    }

    /**
     * Adds to the bean jar
     * @param event
     */
    public static void jar(SlashCommandInteractionEvent event) {
        OptionMapping beansOpt = event.getOption("beans");
        int beans = 0;
        if (beansOpt == null) beans = 1;
        else {
            int input = beansOpt.getAsInt();
            if (input <= 0) beans = 1;
            else if (input > 10) beans = 10;
            else beans = input;
        }
        beanJar += beans;
        event.reply(String.format("There are now %d beans in the jar.", beanJar)).queue();
    }

    /**
     * Repeats user input
     * @param event
     */
    public static void echo(SlashCommandInteractionEvent event) {
        OptionMapping messageOpt = event.getOption("text");
        String message;
        if (messageOpt == null) message = "What was that?";
        else message = messageOpt.getAsString();
        event.reply(message).queue();
    }

    /**
     * Sample display of an embed
     * @param event
     */
    public static void embed(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Sample embed", null);
        eb.setColor(Color.GREEN);
        eb.setDescription("A sample embed to show how it looks.");
        eb.addField("F1", "Ft1", true);
        eb.addField("F2", "Ft2", false);
        eb.setAuthor("Author", null, null);
        eb.setFooter("Footer", null);
        eb.setImage("https://cdn.discordapp.com/attachments/233697516422561792/1060994782270328848/Untitled_6.png");
        eb.setThumbnail("https://cdn.discordapp.com/attachments/233697516422561792/1060994782270328848/Untitled_6.png");
        MessageEmbed embed = eb.build();
        
        event.replyEmbeds(embed)
            .addActionRow(
                Button.primary("b1", "Button 1"),
                Button.success("b2", "Button 2").withEmoji(Emoji.fromUnicode("U+1F438"))
            )
        .queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getButton().getId()) {
            case "b1":
                event.reply("You pressed button 1!").queue();                
                break;
            case "b2":
                event.reply("You pressed button 2! You like frogs?").queue();
                break;
            default:
                break;
        }
    }
    
}
