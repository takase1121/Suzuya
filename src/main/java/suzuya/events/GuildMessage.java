package suzuya.events;

import net.dv8tion.jda.core.entities.*;
import org.apache.commons.lang3.StringUtils;
import suzuya.SuzuyaClient;
import suzuya.handler.CommandHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;
import suzuya.structures.BaseCommand;

public class GuildMessage extends ListenerAdapter {
    public SuzuyaClient suzuya;
    public CommandHandler handler = new CommandHandler();

    public GuildMessage(SuzuyaClient client) {
        suzuya = client;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (!suzuya.isClientReady || event.isWebhookMessage()) return;
        User author = event.getAuthor();
        if (author.isBot()) return;
        Message msg = event.getMessage();
        String content = msg.getContentRaw();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        String prefix = suzuya.settingsHandler.getPrefix(guild.getId());
        if (content.startsWith(prefix))
        {
            MessageChannel channel = msg.getChannel();
            String[] args = msg.getContentRaw().split("\\s+");
            String query = args[0].replace(prefix, "");
            BaseCommand command = handler.getCommand(query);
            if (command != null)
            {
                try {
                    command.run(suzuya, handler, msg, guild, author, member, channel, args);
                } catch (Exception error) {
                    MessageEmbed embed = new EmbedBuilder()
                            .setColor(suzuya.defaultEmbedColor)
                            .addField(String.format("Command %s Raised an error", command.getTitle()), error.toString(), false)
                            .addField("Goumen Admiral...", "You shouldn't be receiving this... unless you are doing something wrong", false)
                            .build();
                    channel.sendMessage(embed).queue();
                    error.printStackTrace();
                }
            }
        }
    }
}
